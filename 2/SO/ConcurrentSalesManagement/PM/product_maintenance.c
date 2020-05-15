#include "../basics.h"
#include "../stack.h"
#include <sys/wait.h>
#include <time.h>

// $ ma
// i <name> <price>      --> inserts the new products, shows the code
// n <code> <new name>   --> changes the name of the product
// p <code> <new price>  --> changes the price of the product
// ...
// <EOF>

// code of the last product     : id = file_size(products) / SIZE_A
// To read product with code id : pread(,,(id-1)*(SIZE_A))

// ---------------------------------
// ------ PRODUCT MAINTENANCE ------
// ---------------------------------

/*
This function adds a new product, showing a new code
Note: The first code is 1, not 0!
*/
void add_product(char *name, double price)
{
    int products, strings, stock;
    if ((products = open("files/PRODUCTS", O_RDWR | O_APPEND)) == -1)
    {
        perror("add_product[PRODUCTS]");
        exit(-1);
    }
    if ((strings = open("files/STRINGS", O_RDWR | O_APPEND)) == -1)
    {
        perror("add_product[STRINGS]");
        exit(-1);
    }
    if ((stock = open("files/STOCK"  , O_RDWR | O_APPEND)) == -1)
    {
        perror("add_product[STOCK]");
        exit(-1);
    }

    Product new_product;
    new_product.position = get_lines(strings) + 1;
    new_product.price = price;

    Stock new_stock;
    new_stock.quantity = 0;

    char str[strlen(name+1)];
    sprintf(str, "%s\n", name);

    write(products, &new_product, SIZE_A);
    write(strings, str, strlen(str));
    write(stock, &new_stock, SIZE_S);

    int id = (file_size(products)) / (int)SIZE_A;
    printf("Codigo: %d\n", id);

    close(products);
    close(strings);
    close(stock);
}

/* Changes the name of a product of a given code */
void change_name(int code, char *name)
{
    int products, strings;
    if ((products = open("files/PRODUCTS", O_RDWR)) == -1)
    {
        perror("change_name[PRODUCTS]");
        exit(-1);
    }
    if ((strings = open("files/STRINGS", O_RDWR | O_APPEND)) == -1)
    {
        perror("change_name[STRINGS]");
        exit(-1);
    }

    int pos = (code - 1) * (SIZE_A);

    Product old;
    pread(products, &old, SIZE_A, pos);

    Product new;
    new.position = get_lines(strings) + 1;
    new.price = old.price;

    char str[strlen(name+1)];
    sprintf(str, "%s\n", name);

    pwrite(products, &new, SIZE_A, pos);
    write(strings, str, strlen(str));

    close(products);
    close(strings);
}

/* Changes the price of a product given its code */
void change_price(int code, double price)
{
    int products;
    if ((products = open("files/PRODUCTS", O_RDWR)) == -1)
    {
        perror("change_price[PRODUCTS]");
        exit(-1);
    }

    int pos = (code - 1) * (SIZE_A);

    Product old;
    pread(products, &old, SIZE_A, pos);

    Product new;
    new.position = old.position;
    new.price = price;

    pwrite(products, &new, SIZE_A, pos);

    close(products);
}

/* Test function
   Returns the position of the name of the product
   User(Usage):
   "1 code"
*/
void show_name(int code)
{
    int products, strings;
    if ((products = open("files/PRODUCTS", O_RDONLY)) == -1)
    {
        perror("show_name[PRODUCTS]");
        exit(-1);
    }
    if ((strings = open("files/STRINGS", O_RDONLY)) == -1)
    {
        perror("show_name[STRINGS]");
        exit(-1);
    }

    Product old;
    int pos = (code - 1) * (SIZE_A);
    pread(products, &old, SIZE_A, pos);

    char buffer[100];
    readline(old.position, strings, buffer);
    printf("Name: %s\nPosition: %d\n", buffer, old.position);

    close(products);
    close(strings);
}

/* Test Function
   Returns the price of a product
   User (Usage):
   "2 code"
*/
void show_price(int code)
{
    printf("Price: %f\n", get_price(code));
}

// -------------------------------
// --------- COMPACTOR ---------
// -------------------------------

/* Returns the percentage of wasted space in the file STRINGS */
double percentage()
{
    int products, strings;
    if ((products = open("files/PRODUCTS", O_RDONLY)) == -1)
    {
        perror("percentage[PRODUCTS]");
        exit(-1);
    }
    if ((strings = open("files/STRINGS", O_RDONLY)) == -1)
    {
        perror("percentage[STRINGS]");
        exit(-1);
    }

    double max_code = file_size(products) / SIZE_A;
    double number_lines = get_lines(strings);

    close(products);
    close(strings);
    return (number_lines > 0 ? ( 1 - (max_code / number_lines) ) : 0);
}

/* Sends the x line to from fdin to fdout */
void send_line(int x, int fdin, int fdout)
{
    lseek(fdin, 0, SEEK_SET);
    char c;
    int line = 1;
    while(read(fdin, &c, 1) > 0 && line <= x)
    {
        if (line == x)
        {
            write(fdout, &c, 1);
        }
        if (c == '\n')
        {
            line++;
        }
    }
    lseek(fdin, 0, SEEK_SET);
}

/* Compacts the STRINGS file */
void compactor()
{
    int products, strings, aux;
    if ((products = open("files/PRODUCTS", O_RDWR)) == -1)
    {
        perror("compactor[PRODUCTS]");
        exit(-1);
    }
    if ((strings = open("files/STRINGS", O_RDONLY)) == -1)
    {
        perror("compactor[STRINGS]");
        exit(-1);
    }
    if ((aux = open("files/AUX", O_RDWR | O_APPEND | O_CREAT | O_TRUNC, 0666)) == -1)
    {
        perror("compactor[AUX]");
        exit(-1);
    }

    Product help;
    int pos;
    int max_code = file_size(products) / SIZE_A;
    for(int i = 1; i <= max_code; i++)
    {
		pos = (i - 1) * (SIZE_A);
		pread(products, &help, SIZE_A, pos);
        send_line(help.position, strings, aux);
        help.position = get_lines(aux);
		pwrite(products, &help, SIZE_A, pos);
    }

    close(products);
    close(strings);
    close(aux);

    remove("files/STRINGS");
    rename("files/AUX", "files/STRINGS");
    remove("files/AUX");
}

// ------------------------------------------
// --------- AGGREGATOR (TO FILES) ----------
// ------------------------------------------

/* Returns the first line of the sale being aggregated */
int get_start()
{
    int fd;
    if ((fd = open("files/START", O_RDONLY)) == -1)
    {
        perror("get_start[START]");
        exit(-1);
    }
    char buffer[10];
    readln(fd, buffer);
    close(fd);

    return (atoi(buffer) + 1);
}

/* Função que chama o agregador com o argumento 'primeira line a ser agregada', e reescreve
a ultima line que foi agregada no ficheiro início */
void aggregate()
{
    if (fork() == 0)
    {
        int start;
        if ((start = open("files/START", O_CREAT | O_RDWR, 0666)) == -1)
        {
            perror("aggregate[START]");
            exit(-1);
        }
        if (file_size(start) == 0)
        {
            write(start, "0\n", 2);
        }
        close(start);

        int sales;
        if ((sales = open("files/SALES", O_RDONLY)) == -1)
        {
            perror("aggregate[SALES]");
            exit(-1);
        }

        char str[10];
        sprintf(str, "%d", get_start());

        char name[50];
        sprintf(name, "tmp/%s", date());
        int aggregation;
        if ((aggregation = open(name, O_CREAT | O_APPEND | O_WRONLY, 0666)) == -1)
        {
            perror("aggregate[date]");
            exit(-1);
        }

        dup2(sales,0);
        dup2(aggregation, 1);

        close(sales);
        close(aggregation);

        execlp("./ag", "./ag", str , NULL);
        perror("Aggregator(AG)");
        _exit(1);
    }
    else
    {
        wait(NULL);

        int start, sales;
        if ((start = open("files/START", O_TRUNC | O_RDWR)) == -1)
        {
            perror("aggregate[START]");
            exit(-1);
        }
        if ((sales = open("files/SALES", O_RDONLY)) == -1)
        {
            perror("aggregate[SALES]");
            exit(-1);
        }

        char new[10];
        sprintf(new, "%d\n", get_lines(sales));
        write(start, new, strlen(new));

        close(start);
        close(sales);
    }
}

// --------------------------------
// ------------- MAIN -------------
// --------------------------------

int main()
{
    // Creation of the files (if not already created)
    int products, strings, stock;
    if ((products = open("files/PRODUCTS", O_RDWR | O_CREAT, 0666)) == -1)
    {
        perror("maintenance/main[PRODUCTS]");
        return -1;
    }
    if ((strings = open("files/STRINGS", O_RDWR | O_CREAT, 0666)) == -1)
    {
        perror("maintenance/main[STRINGS]");
        return -1;
    }
    if ((stock = open("files/STOCK", O_RDWR | O_CREAT, 0666)) == -1)
    {
        perror("maintenance/main[STOCK]");
        return -1;
    }

    close(products);
    close(strings);
    close(stock);

    // --------------

    int n, code;
    double price;
    char name[20], buffer[100];
    while((n = readln(0, buffer)) > 0)
    {
        switch (buffer[0])
        {
			case 'i':
			{
				if (sscanf(buffer, "i %s %lf", name, &price) == 2)
				{
				    add_product (name, price);
				}
				else
				{
				    puts("Invalid Format!");
				}
				break;
			}
			case 'n':
			{
				if (sscanf(buffer,"n %d %s", &code, name) == 2)
				{
                    if (code_exists(code))
                    {
                        change_name(code, name);
                    }
                    else
                    {
                        puts("Inexistent Code!");
                    }
				}
				else
				{
				    puts("Invalid Format!");
				}
				break;
			}
			case 'p':
			{
				if (sscanf(buffer,"p %d %lf", &code, &price) == 2)
				{
                    if (code_exists(code))
                    {
                        struct node* head = NULL;
                        init(head);
                        head = fileToCache(head);
                        display(head);
                        printf("NEW PRICE:%lf\n",price);
                        changePrice(head,code,price);
                        printf("NEW PRICE2:%lf\n",price);
                        cacheToFile(head);
                        printf("NEW PRICE3:%lf\n",price);
                        change_price(code, price);
                        printf("NEW PRICE4:%lf\n",price);
                    }
                    else
                    {
                        puts("Inexistent Code!");
                    }
				}
				else
				{
				    puts ("Invalid Format!");
				}
				break;
			}
			case '1':
			{
				if (sscanf(buffer,"1 %d", &code) == 1)
				{
                    if (code_exists(code))
                    {
                        show_name(code);
                    }
                    else
                    {
                        puts("Inexistent Code!");
                    }
				}
				else
				{
				    puts ("Invalid Format!");
				}
				break;
			}
			case '2':
			{
				if (sscanf(buffer,"2 %d", &code) == 1)
				{
                    if (code_exists(code))
                    {
                        show_price(code);
                    }
                    else
                    {
                        puts("Inexistent Code![2]");
                    }
				}
				else
				{
				    puts ("Invalid Format!");
				}
				break;
			}
            case 'a':
            {
                char c, garbage;
                if (sscanf(buffer,"%c %c", &c, &garbage) == 1 && c == 'a')
                {
                    aggregate();
                }
                else
                {
                    puts("Invalid Option!");
                }
                break;
            }
			default:
			{
                puts("Not a valid option!");
                break;
			}
		}
		if (percentage() > 0.2)
		{
			compactor();
		}
    }
    return 0;
}
