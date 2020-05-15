#include "../basics.h"
#include "../stack.h"
/* Changes the quantity at stock
   Returns:
   1, if it is a sale
   0, increase in stock
*/
int change_stock(int code, int quantity)
{
    int stock;
    if ((stock = open("files/STOCK", O_RDWR)) == -1)
    {
        perror("change_stock[STOCK]");
        exit(-1);
    }

    int pos = (code - 1) * (SIZE_S);

    Stock old, new;
    pread(stock, &old, SIZE_S, pos);

    if (old.quantity + quantity >= 0)
    {
        new.quantity = old.quantity + quantity;
    }
    else
    {
        return -1;
    }

    pwrite(stock, &new, SIZE_S, pos);

    close(stock);

    return (quantity < 0 ? 1 : 0);
}

/*
    Quantity can be negative or positive
    If is negative adds a sale to the file
*/
void make_sale(int code, int quantity, double price)
{
    int sales;
    if ((sales = open("files/SALES", O_RDWR | O_APPEND)) == -1)
    {
        perror("make_sale[SALES]");
        exit(-1);
    }

    char str[30];
    sprintf(str, "%d %d %f\n", code, abs(quantity), (abs(quantity) * price));
    write(sales, str, strlen(str));

    close(sales);
}

/* Shows the stock of a product */
char* confirm_stock(int code)
{
    int stock;
    if ((stock = open("files/STOCK", O_RDONLY)) == -1)
    {
        perror("confirm_stock[STOCK]");
        exit(-1);
    }

    int pos = (code - 1) * (SIZE_S);

    Stock old;
    pread(stock, &old, SIZE_S, pos);
    char* str = malloc(sizeof(char)*128);
    sprintf(str,"Stock: %d\n", old.quantity);
    close(stock);
    return str;
}

/* Shows the details of the sale:
                            * code,
                            * quantity,
                            * total amount
*/
char* confirm_sale()
{
    int sales;
    if ((sales = open("files/SALES", O_RDONLY)) == -1)
    {
        perror("confirm_sale[SALES]");
        exit(-1);
    }

    char str[30];
    readline(get_lines(sales), sales, str);

    int code, quantity;
    double total;
    sscanf(str, "%d %d %lf", &code, &quantity, &total);

    char* str1 = malloc(sizeof(char)*128);
    sprintf(str1,"Code: %d | Quantity: %d | Total: %f\n", code, quantity, total);

    close(sales);

    return str1;
}

/* Shows stock information:
                            quantity available,
                            product price
*/
char* show_stock(int code)
{
    int stock;
    if ((stock = open("files/STOCK", O_RDONLY)) == -1)
    {
        perror("show_stock[STOCK]");
        exit(-1);
    }

    int pos = (code - 1) * (SIZE_S);

    Stock old;
    pread(stock, &old, SIZE_S, pos);
    char* str = malloc(sizeof(char)*128);
    sprintf(str,"Stock: %d | Price: %f\n", old.quantity, get_price(code));

    close(stock);
    return str;
}

// --------------------------------
// --------- SALES SERVER ---------
// --------------------------------

int main()
{
    mkfifo("tmp/fifo", 0666);

    int fifo, stock, sales, fifo_pid, cache;
    if ((fifo = open("tmp/fifo", O_RDONLY)) == -1)
    {
        perror("server/main[fifo]");
        exit(-1);
    }
    if ((stock = open("files/STOCK", O_RDWR)) == -1)
    {
        perror("server/main[STOCK]");
        exit(-1);
    }
    if ((sales = open("files/SALES", O_RDWR | O_CREAT, 0666)) == -1)
    {
        perror("server/main[SALES]");
        exit(-1);
    }

    int n;
    struct node* head = NULL;
    init(head);
    if( access("files/CACHE", F_OK ) != -1 )
    {
      printf("EXISTS\n");
    }
    else
    {
      printf("DOESNT EXIST\n");
      if ((cache = open("files/CACHE", O_RDWR | O_CREAT , 0666)) == -1)
      {
          perror("server/main[CACHE]");
          exit(-1);
      }
      for (int i = 0; i < CACHE_SIZE; i++)
      {
        write(cache,"0 0.00\n",7);
      }
    }
    for (int i = 0; i < CACHE_SIZE; i++)
    {
      head = push(head, 0, 0.00);
    }
    Request request;
    while((n = read(fifo, &request, SIZE_P)) > 0)
    {
        head = fileToCache(head);

        char *str = malloc(sizeof(char)*40);
        sprintf(str,"tmp/%s",request.pid);

        if ((fifo_pid = open(str, O_WRONLY)) == -1)
        {
          perror("server/main[PID]");
          exit(-1);
        }

        int code = -1;
        char* aux = malloc(sizeof(char)*strlen(request.buffer));
        strcpy(aux,request.buffer);

        if (is_number(strtok(aux, " ")))
        {
            code = atoi(aux);
        }
        char *quantity = strtok(NULL, "\n");
        double price;
        int be_not_be = find(head, code);

        if (!be_not_be && !code_exists(code))
        {
            write(fifo_pid,"Inexistent Code!\n",20);
        }
        else
        {
            if (be_not_be)
            {
              price = getPriceCache(head, code);
            }
            else
            {
              price = get_price(code);
              if (is_full(head))
              {
                head = pop(head);
              }
              head = push(head, code, price);
            }

            display(head);
            cacheToFile(head);
            if (quantity)
            {
                if (is_number(quantity))
                {
                    int sale = change_stock(code, atoi(quantity));
                    if (sale > 0)
                    {
                        make_sale(code, atoi(quantity), price);
                        free(str);
                        str = malloc(sizeof(char)*128);
                        strcpy(str,confirm_sale());
                        write(fifo_pid,str,strlen(str));
                    }
                    else if(sale < 0)
                    {
                        write(fifo_pid,"Out of Stock!\n",29);
                    }
                    free(str);
                    str = malloc(sizeof(char)*128);
                    strcpy(str,confirm_stock(code));
                    write(fifo_pid,str,strlen(str));
                }
                else
                {
                    write(fifo_pid,"Invalid Quantity!\n",21);
                }
            }
            else
            {
                char* str1 = malloc(sizeof(char)*128);
                strcpy(str1, show_stock(code));
                write(fifo_pid,str1,strlen(str1));
                free(str1);
            }
        }
        free(str);
        close(fifo_pid);
    }

    close(fifo);
    close(stock);
    close(sales);

    remove("tmp/fifo");

    return 0;
}
