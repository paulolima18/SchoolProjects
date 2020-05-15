#ifndef BASICS_H_
#define BASICS_H_

#include <fcntl.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/stat.h>
#include <sys/types.h>
#include <unistd.h>
#include <time.h>

typedef struct Product
{
    double price;
    int position; /* Position (line) in the file STRINGS */
} Product;

typedef struct Stock
{
    int quantity;
} Stock;

typedef struct Request
{
    char pid[32];
    char buffer[512];
} Request;

#define SIZE_A sizeof(struct Product)
#define SIZE_S sizeof(struct Stock)
#define SIZE_P sizeof(struct Request)

char* date();
int get_lines(int);
int file_size(int);
int is_number(char*);
int code_exists(int);
double get_price(int);
ssize_t readln(int, char*);
ssize_t readline(int, int, char*);

#endif
