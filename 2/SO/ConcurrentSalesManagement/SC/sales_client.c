#include "../basics.h"

// $ cv
// <numeric_code>            --> shows stock and price in stdout mostra no stdout stock e preço
// <numeric_code> <quantity> --> updates stock and shows the new stock
// ...<EOF>

// -------------------------------
// -------- SALES CLIENT  --------
// -------------------------------

/* Sends params to server and receives the results */
int main()
{
    int fifo, fifo_pid;
    int pid = getpid();

	mkfifo("tmp/fifo", 0666);
	if ((fifo = open("tmp/fifo", O_WRONLY)) == -1)
	{
		perror("client/main[fifo]");
		exit(-1);
	}

    char *str = malloc(sizeof(char)*32);
    sprintf(str,"tmp/%d",pid);
    if ((fifo_pid = open(str, O_RDONLY)) == -1)
    {
      	mkfifo(str, 0666);
    }

    int n;
    char buffer[512];
    char buffer2[512];
    char buffer_pid[32];

    while(readln(0, buffer) > 0)
    {
		Request request;
		strcpy(request.buffer,buffer);
		sprintf(buffer_pid, "%d", pid);
		strcpy(request.pid, buffer_pid);
		write(fifo, &request, SIZE_P);

		if ((fifo_pid = open(str, O_RDONLY)) == -1)
		{
			perror("client/main[PID]");
			exit(-1);
		}

		n = readln(fifo_pid, buffer2);
		write(1, buffer2, n);
		write(1,"\n",1);

		close(fifo_pid);
    }

    close(fifo);
    remove(str);
	free(str);
    return 0;
}
