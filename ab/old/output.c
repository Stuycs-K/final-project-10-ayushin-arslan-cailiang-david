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

    if (argc < 3) {
        printf("args\n");
        exit(1);
    }
    char * pipe_location = argv[1];
    char * message_from = argv[2];

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
            if (strlen(buff) > 0) {

                printf("Writing to pipe: %s (strlen %ld)\n", buff, strlen(buff));

                // pid_t p;
                // p = fork();
                // if (p == 0) {
                //     char *MODE = malloc(BUFFER_SIZE);
                //     if (strcmp(message_from, "A") == 0) {
                //         sprintf(MODE, "B->A");
                //     }
                //     else {
                //         sprintf(MODE, "A->B");
                //     }
                //     char *encode_this_location = "encode_this.dat";
                //     int encode_this = open(encode_this_location, O_CREAT | O_TRUNC | O_WRONLY, 0644);
                //     write(encode_this, buff, strlen(buff) + 1);
                //     close(encode_this);
                //     // int execerr = execlp("bash", "bash", "encode.sh", buff, MODE, pipe_location, NULL);
                //     int execerr = execlp("bash", "bash", "encode.sh", encode_this_location, MODE, pipe_location, NULL);
                //     err(execerr, "execlp error");
                //     exit(0);
                // }





                // printf("Writing to pipe: %s (strlen %ld)\n", buff, strlen(buff));
                // // printf("%s\n", pipe_location);
                // write(pipe_write, buff, BUFFER_SIZE);
                // // else {
                // //     exit(0);
                // // }
            }
        }


    }
}



