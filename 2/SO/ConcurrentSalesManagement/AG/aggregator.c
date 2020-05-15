#include "../basics.h"

// $ ag
// <code> <quantity> <total amount>
// ...
// <EOF>

// ---------------------------
// ------- AGGREGATOR --------
// ---------------------------

int main(int argc, char* argv[])
{
    // Decides in which line to start aggregating
    int starting_line;
    if (argc == 1)
        starting_line = 1;
    else
        starting_line = atoi(argv[1]);

    // Used to know the amount of codes (so we can initialize the code with the right size)
    int products;
    if ((products = open("files/PRODUCTS", O_RDONLY)) == -1)
    {
        perror("aggregator[PRODUCTS]");
        return -1;
    }
    int max_code = file_size(products) / SIZE_A;
    close(products);

    // Creates the array with size max_code and puts all values as 0
    int array[max_code];
    for(int i = 0; i < max_code; i++)
    {
        array[i] = 0;
    }

    // Creates and opens the file AUX_SALES
    int aux;
    if ((aux = open("files/AUX_SALES", O_RDWR | O_TRUNC | O_CREAT | O_APPEND, 0666)) == -1)
    {
        perror("aggregator[AUX_SALES]");
        return -1;
    }

    // Read lines and writes to the file AUX_SALES
    int n, x, j = 1, max_read = 0;
    char* buffer = malloc(sizeof(char)*1024);
    int code, quantity,k;
    double total;
    while(j < starting_line)
    {
        readln(0, buffer);
        j++;
    }
    while((n = readln(0, buffer)) > 0)
    {
        x = sscanf(buffer, "%d %d %lf %d", &code, &quantity, &total, &k);

        if (x != 3)
        {
            puts("Invalid Format!");
        }
        else if (x == 3 && !code_exists(code))
        {
            puts("Inexistent Code!");
        }
        else if (x == 3)
        {
            array[code-1]++;
            write(aux, buffer, n);
            write(aux, "\n", 1);
            if (code > max_read)
            {
                max_read = code;
            }
        }
        free(buffer);
        buffer = malloc(sizeof(char)*1024);
    }
    free(buffer);

    // Writes the final version of each product, ordered, in a file named AUX_AGGREGATOR
    int t_quantity, count, track, i;
    double t_total;
    for (track = 1; track <= max_code; track++)
    {
        count = array[track-1];
        if (count > 0) // If the element exists in the current reading
        {
            t_quantity = 0;
            t_total = 0;
            lseek(0, aux, SEEK_SET);
            for (i = 1; readline(i, aux, buffer) > 0 && count > 0 && i <= get_lines(aux); i++)
            {
                x = sscanf(buffer, "%d %d %lf", &code, &quantity, &total); // always correct
                if (code == track)
                {
                    t_quantity += quantity;
                    t_total += total;
                    count--;
                }
                else if(x != 3)
                {
                    puts("Invalid Format!");
                }
            }
            // Prints the information to the user
            char str[128];
            sprintf(str, "%d %d %lf\n", code, t_quantity, t_total);
            write(1, str, strlen(str));
        }
    }

    // Closes and deletes the file AUX_SALES
    close(aux);
    remove("files/AUX_SALES");
    return 0;
}
