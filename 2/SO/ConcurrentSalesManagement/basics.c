#include "basics.h"

/* Returns the number of lines in a file */
int get_lines(int fd)
{
    lseek(fd, 0, SEEK_SET); /* makes sure it starts at the beginning */
    char c;
    int n, lines = 0;
    while((n = read(fd, &c, 1)) > 0)
    {
        if (c == '\n')
        {
            lines++;
        }
    }
    lseek(fd, 0, SEEK_SET); /* makes sure it leaves at the beginning */
    return lines;
}

/* Read one line of text to the buffer
   Returns the size of the line read*/
ssize_t readln(int fildes, char* buf)
{
    int i = 0, n;
    while((n = read(fildes, buf+i, 1)) > 0)
    {
        if (buf[i] == '\n')
        break;
        i++;
    }
    buf[i] = '\0';

    if (n <= 0 && i == 0)
        i = n;

    return i;
}

/* Read one line of a certain text "x" to the buffer
   Return the size of the line read*/
ssize_t readline(int x, int fildes, char* buf)
{
    int i = 0, n, line = 1, count = 1;
    char c;
    while((n = read(fildes, &c, 1)) > 0 && line < x)
    {
        if (c == '\n')
        {
            line++;
        }
        count++;
    }
    lseek(fildes, count-1, SEEK_SET);
    while((n = read(fildes, buf+i, 1)) > 0)
    {
        if (buf[i] == '\n')
        {
            break;
        }
        i++;
    }
    buf[i] = '\0';

    if (n <= 0 && i == 0)
        i = n;

    return i;
}

/* Returns de price of the product */
double get_price(int code)
{
    int products;
    if ((products = open("files/PRODUCTS", O_RDONLY)) == -1)
    {
        perror("get_price[PRODUCTS]");
        exit(-1);
    }

    Product old;
    int pos = (code - 1) * (SIZE_A);
    pread(products, &old, SIZE_A, pos);

    close(products);
    return old.price;
}

/* Returns the size of the file */
int file_size(int fd)
{
    lseek(fd, 0, SEEK_SET); /* makes sure it starts at the beginning */

    char c;
    int count;
    for(count = 0; (read(fd, &c, 1)) > 0; count++);

    lseek(fd, 0, SEEK_SET); /* makes sure it leaves at the beginning */

    return count;
}

/* Confirms if the code given exists
   Returns:
   1, exists
   0, doesnt exist
*/
int code_exists(int code)
{
    int products;
    if ((products = open("files/PRODUCTS", O_RDONLY)) == -1)
    {
        perror("code_exists[PRODUCTS]");
        exit(-1);
    }
    int file = file_size(products);
    close(products);
    return ((code * (int)SIZE_A > file || code <= 0) ? 0 : 1);
}

/* Verify if a given string is a number */
int is_number(char* string)
{
    int res = 1;
    int i = (string[0] == '-');
    for(; string[i] && string[i] != ' ' && string[i] != '\n' && res; i++)
    {
        if (string[i] < '0' || string[i] > '9')
        {
            res = 0;
        }
    }
    return res;
}

char* date()
{
    char *str = malloc(sizeof(char)*100);
    time_t t = time(NULL);
    struct tm tm = *localtime(&t);
    sprintf(str, "%d-%d-%dT%d:%d:%d", tm.tm_year + 1900, tm.tm_mon + 1, tm.tm_mday, tm.tm_hour, tm.tm_min, tm.tm_sec);
    return str;
}
