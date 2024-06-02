#include <stdio.h>
#include <string.h>
#include <errno.h>
#include <unistd.h>
#include <stdlib.h>
#include <sys/types.h>
#include <sys/ipc.h>
#include <sys/stat.h>
#include <fcntl.h>

#define BUFFER_SIZE 1024

void err(int i, char*message){
    if(i < 0){
        printf("Error: %s - %s\n",message, strerror(errno));
        exit(1);
    }
}

int main(int argc, char* argv[]) {
    setlinebuf(stdout);

    fd_set read_fds;

    if (argc < 2) {
        printf("args\n");
        exit(1);
    }
    char * pipe_location = argv[1];

    int pipe_write = open(pipe_location, O_WRONLY);
    printf("Opened pipe\n");

    while(1){

        char *buff = malloc(BUFFER_SIZE);

        FD_ZERO(&read_fds);
        FD_SET(STDIN_FILENO, &read_fds);

        int maxfd = STDIN_FILENO;
        int i = select(maxfd+1, &read_fds, NULL, NULL, NULL);


        //if standard in, use fgets
        if (FD_ISSET(STDIN_FILENO, &read_fds)) {
            fgets(buff, BUFFER_SIZE, stdin);
            buff[strlen(buff)]=0;
            buff = strsep(&buff, "\n");
            printf("Read %s (strlen %ld)\n", buff, strlen(buff));
            printf("%s\n", pipe_location);
            write(pipe_write, buff, BUFFER_SIZE);
            // else {
            //     exit(0);
            // }
        }


    }
}



