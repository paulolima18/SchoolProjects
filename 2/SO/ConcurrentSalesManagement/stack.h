#ifndef LINKEDSTACK_H_INCLUDED
#define LINKEDSTACK_H_INCLUDED
#define CACHE_SIZE 200
struct node;

double getPriceCache(struct node*, int);
int empty(struct node *);
int is_full(struct node *);
int find(struct node* , int );
void init(struct node* );
void display(struct node* );
struct node* pop(struct node * );
struct node* fileToCache(struct node *);
struct node* changePrice(struct node *, int , double );
void cacheToFile(struct node *);
struct node* push(struct node *, int, double);

#endif
