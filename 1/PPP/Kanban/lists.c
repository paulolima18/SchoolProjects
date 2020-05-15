#include "lists.h"

int valid(char *str)
{
  int k = -1;
  if (str[0] == '@') return 1;
  for (int i = 0;i < strlen(str)-1;i++)
  {
    if ( (str[i] == '@') && k < 0) k = i;
    if ( (str[i] == '.') && i <= k+1) return 1;
    if ( (str[i] < 'a') || (str[i] > 'z') )
      if (str[i] != '.' && k < 0)
        return 1;
  }
  return 0;
}
int dias(void *a,void *b)
{
  struct tasks *d1 = a;
  struct tasks *d2 = b;
  if (d1 -> y1 == d2 -> y1)
  {
    if (d1 -> m1 == d2 -> m1)
    {
      if (d1 -> d1 != d2 -> d1)
      {
        return abs( (d1 -> d1) - (d2 -> d1) );
      }
    }
  }
  return 10;
}

void outfile(void*p)
{
  struct tasks *task = p;
  fprintf(fp,"%d\n",task->id);
  fprintf(fp,"%d\n",task->priority);
  fprintf(fp,"%d\n",task->d);
  fprintf(fp,"%d\n",task->m);
  fprintf(fp,"%d\n",task->y);
}

void bubbleSort(struct mnode *start,int (*compare)(void *,void *))
{
  struct mnode *pointer;
  struct mnode *pointer2;
  struct mnode *fake;

  pointer = start -> next;
  pointer2 = pointer;
	if (pointer -> next == NULL)
		return;

  fake = start -> next;
  while(fake -> next != NULL)
  {
    while (pointer -> next != NULL)
	  {
		  if (compare(pointer -> data,pointer -> next -> data))
		  {
			   swap(pointer, pointer -> next);
		  }
      pointer = pointer -> next;
	  }
    pointer = pointer2;
    fake = fake -> next;
  }
}

void swap(struct mnode *a, struct mnode *b)
{
  struct mnode *temp;
  temp = a -> data;
	a -> data = b -> data;
	b -> data = temp;
}

void unchain(struct mnode * list){
    struct mnode *aux;
    aux = list -> next;
    free(list);
    list = aux;
    while(list != NULL){
        aux = list -> next;
        free(list);
        list = aux;
    }
}

int todocompare(void *a, void *b)
{
  struct tasks *d1 = a;
  struct tasks *d2 = b;
  if(d1 -> priority < d2 -> priority)
    return 1;
  else if(d1 -> priority == d2 -> priority)
  {
    if (d1 -> y > d2 -> y)
        return 1;
    if (d1 -> y == d2 -> y && d1 -> m > d2 -> m)
        return 1;
    if (d1 -> y == d2 -> y && d1 -> m == d2 -> m && d1 -> d > d2 -> d)
        return 1;
  }
  return 0;
}

int allcompare(void *a, void *b)
{
  struct tasks *d1 = a;
  struct tasks *d2 = b;
  if (d1 -> y > d2 -> y)
      return 1;
  if (d1 -> y == d2 -> y && d1 -> m > d2 -> m)
      return 1;
  if (d1 -> y == d2 -> y && d1 -> m == d2 -> m && d1 -> d > d2 -> d)
      return 1;
  return 0;
}

int peoplecompare(void *a, void *b)
{
  struct tasks *d1 = a;
  struct tasks *d2 = b;
  struct mnode *test;
  struct mnode *test2;
  struct person *p;
  struct person *p2;
  if(d1 -> worker -> next != NULL)
  {
    test = d1 -> worker -> next;
    p = test -> data;
  }
  if(d2 -> worker -> next != NULL)
  {
    test2 = d2 -> worker -> next;
    p2 = test2 -> data;
  }
  int i;
  i = 0;
  do {
    if ((p->name)[i] > (p2->name)[i])
        return 1;
    else return 0;
    i++;
  } while((p->name)[i] || (p2->name)[i] != ('\n' || '\0') );
  return 0;
}

int donecompare(void *a, void *b)
{
  struct tasks *d1 = a;
  struct tasks *d2 = b;
  if (d1 -> y1 > d2 -> y1)
      return 1;
  if (d1 -> y1 == d2 -> y1 && d1 -> m1 > d2 -> m1)
      return 1;
  if (d1 -> y1 == d2 -> y1 && d1 -> m1 == d2 -> m1 && d1 -> d1 > d2 -> d1)
      return 1;
  return 0;
}

struct mnode *head(){
    struct mnode *header = malloc(sizeof(struct mnode));
    if(header == NULL)return NULL;
    header -> next = NULL;
    return header;
};

int idcomp(void *p,char *num)
{
  struct person *person = p;
  int x = atoi(num);
  if(person -> id == x)
    return 1;
  else
    return 0;
}

int idcompt(void *p,char *num)
{
  struct tasks *task = p;
  int x = atoi(num);
  if(task -> id == x)
    return 1;
  else
    return 0;
}

int emailcomp(void *p,char *email){
  struct person *person = p;
  if(strcmp(person -> email,email))
    return 0;
  else
    return 1;
}

int compare(struct mnode *list,char *str,int(*comp)(void *,char *)){
  char c[TAM];
  list = list -> next;
  strcpy(c,str);
  while(list != NULL)
  {
    if(comp(list->data,c))
      return 1;
    list = list -> next;
  }
  return 0;
}

struct mnode *find(struct mnode *list,int (*compare)(void *,char *),char*id)
{
  while(list -> next != NULL)
  {
    list = list -> next;
    if(compare(list -> data,id))
    {
      return list;
    }
  }
  return 0;
};

struct mnode *out(struct mnode *list,int (*compare)(void *,char *),char *id) {
    struct mnode **pptr = &list;
    struct mnode *aux;
    aux = list;
    list = list -> next;
    while(list  != NULL){
        pptr = &(aux -> next);
        if(compare(list->data,id))
        {
            *pptr = list -> next;
            free(list);
            break;
        }
        aux = aux -> next;
        list = list -> next;
    }
    return 0;
};

struct mnode *putss(struct mnode * list,void *pt)
{
  struct mnode *newp = malloc(sizeof(struct mnode));
  if(newp == NULL) return NULL;
  while(list -> next != NULL)
    list = list -> next;

  newp -> prev = newp;
  newp -> data = pt;
  list -> next = newp;
  newp -> next = NULL;
  return newp;
};

void print_person(void *p)
{
  struct person *person = p;
  strtok(person->name,"\n");
  printf(magenta"\n*************%s*************\n",person->name);
  printf("E-Mail: %sID: %d \n\n",person->email,person ->id);
}

void print_taskd(void *p)
{
  struct tasks *task = p;
  printf("ID: %d \nPriority: %d \nStart Date: %d/%d/%d \nEnd Date: %d/%d/%d\nInformation: %s \n\n",task->id,task -> priority,task->d,task->m,task->y,task->d1,task->m1,task->y1,task->info);
}

void print_taskt(void *p)
{
  struct tasks *task = p;
  printf("\nID: %d \nPriority: %d \nStart Date: %d/%d/%d \nInformation: %s \n",task->id,task -> priority,task->d,task->m,task->y,task->info);
}

void print_taskf(void *p)
{
  struct tasks *task = p;
  printf("ID: %d \nPriority: %d \nStart Date: %d/%d/%d \nFinish Date: %d/%d/%d \nInformation: %s \n",task->id,task -> priority,task->d,task->m,task->y,task->d1,task->m1,task->y1,task->info);
}

void print_ptasks(void *p)
{
  struct tasks *task = p;
  if(task -> ox == 0 || task -> ox == 1)
  {
    if(task -> ox == 0)
      printf(blue"'Doing'\n");
      else
      printf(white"'Done'\n");
      printf("ID: %d \nPriority: %d \nStart Date: %d/%d/%d \nEnd Date: %d/%d/%d\nInformation: %s \n\n",task->id,task -> priority,task->d,task->m,task->y,task->d1,task->m1,task->y1,task->info);
  }
  else
  {
    printf(magenta"'To Do'\n");
    printf("ID: %d \nPriority: %d \nStart Date: %d/%d/%d \nInformation: %s \n",task->id,task -> priority,task->d,task->m,task->y,task->info);
  }
}
void read(struct mnode *list,void (*imprime)(void *)){
    list = list -> next;
    while(list != NULL)
    {
      imprime(list->data);
      list = list -> next;
    }
}
