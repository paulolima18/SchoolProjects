#include <stdio.h>
#include "basics.h"
#include "stack.h"
struct node
{
    int code;
    double price;
    struct node* next;
};

/*
    init the stack
*/
void init(struct node* head)
{
    head = NULL;
}

/*
    push an element into stack
*/
struct node* push(struct node* head,int code,double price)
{
    struct node* tmp = (struct node*)malloc(sizeof(struct node));
    if(tmp == NULL)
    {
        exit(0);
    }
    tmp->code = code;
    tmp->price = price;
    tmp->next = head;
    head = tmp;
    return head;
}

/*
    Pop an element from the stack
*/
struct node* pop(struct node *head)
{
    struct node* tmp = head;
    while(tmp -> next -> next != NULL)
    {
      tmp = tmp->next;
    }
    tmp -> next = NULL;
    return head;
}

/*
    returns 1 if the stack is full, otherwise returns 0
*/
int is_full(struct node *head)
{
  struct node *current;
  int res = 0, nodes = 0;
  current = head;
  while(current!= NULL)
  {
    nodes++;
    current = current->next;
    if (nodes == CACHE_SIZE)
    {
      res = 1;
    }
  }
  return res;
}

/*
    returns 1 if the stack is empty, otherwise returns 0
*/
int empty(struct node* head)
{
    return head == NULL ? 1 : 0;
}

/*
    returns 1 if found data, otherwise returns 0
*/
int find(struct node* head, int code)
{
    struct node *current;
    int res = 0;
    current = head;
    while(current!= NULL)
    {
      if (current->code == code)
      {
        res = 1;
        break;
      }
      current = current -> next;
    }
    return res;
}

/*
    returns the price of a certain product stored in cache, if not existant return -1
*/
double getPriceCache(struct node* head, int code)
{
  struct node *current;
  double res = -1.0;
  current = head;
  while(current != NULL)
  {
    if (current -> code == code)
    {
      res = current -> price;
      break;
    }
    current = current -> next;
  }
  return res;
}

/*
    display the stack content
*/
void display(struct node* head)
{
    struct node *current;
    current = head;
    if(current!= NULL)
    {
        puts("Stack:");
        do
        {
            printf("%d | %lf \n",current->code, current->price);
            current = current->next;
        }
        while (current!= NULL);
        printf("\n");
    }
    else
    {
        printf("The Stack is empty\n");
    }

}

/*
    Places all codes and prices inside the cache
*/
struct node* fileToCache(struct node* head)
{
  int cache;

  if ((cache = open("files/CACHE", O_RDWR | O_CREAT , 0666)) == -1)
  {
      perror("server/main[CACHE]");
      exit(-1);
  }

  char buffer[32];
  int code;
  double price;

  for (int i=0; i< CACHE_SIZE;i++)
  {
    readln(cache,buffer);
    sscanf(buffer,"%d %lf", &code, &price);

    if(is_full(head))
    {
      head = pop(head);
    }
    head = push(head, code, price);
  }
  close(cache);
  return head;
}

void writer(int cache,struct node* tmp)
{

  if (tmp -> next != NULL)
  {
    writer(cache,tmp->next);
  }
  char str[16];
  sprintf(str,"%d %.2lf\n",tmp->code,tmp->price);
  write(cache,str,strlen(str));
}

/*
    Places all codes and prices inside the cache
*/
void cacheToFile(struct node *head)
{
  int cache;
  struct node* tmp = head;
  if ((cache = open("files/CACHE", O_CREAT | O_TRUNC | O_WRONLY, 0666)) == -1)
  {
      perror("server/main[CACHE]");
      exit(-1);
  }
  writer(cache,tmp);
  close(cache);
}
/*
  Change price of a certain product
*/
struct node* changePrice(struct node *head, int code, double price)
{
  struct node* tmp = head;
  while(tmp -> next != NULL)
  {
    if (tmp -> code == code)
    {
      tmp -> price = price;
      return head;
    }
    tmp = tmp -> next;
  }
  return head;
}
