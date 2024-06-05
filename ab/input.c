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
    // setlinebuf(stdout);

    fd_set read_fds;

    if (argc < 4) {
        printf("args\n");
        exit(1);
    }
    char * pipe_location = argv[1];
    char * message_from = argv[2];

    int testout = open("testout.txt", O_CREAT | O_WRONLY | O_APPEND, 0644);

    int pipe_read = open(pipe_location, O_RDONLY | O_NONBLOCK);
    char *testprint = malloc(BUFFER_SIZE);
    sprintf(testprint, "Opened pipe (input)\n");
    write(testout, testprint, BUFFER_SIZE);


    char * write_pipe_location = argv[3];

    int pipe_write = open(write_pipe_location, O_WRONLY);
    printf("Opened pipe\n");


    while(1){

        char *buff = malloc(BUFFER_SIZE);

        FD_ZERO(&read_fds);
        FD_SET(STDIN_FILENO, &read_fds);
        FD_SET(pipe_read,&read_fds);

        int maxfd = (pipe_read > STDIN_FILENO)?pipe_read:STDIN_FILENO;
        int i = select(maxfd+1, &read_fds, NULL, NULL, NULL);


        //if standard in, use fgets
        if (FD_ISSET(STDIN_FILENO, &read_fds)) {
            char *read = fgets(buff, BUFFER_SIZE, stdin);
            if (read) {
                buff[strlen(buff)]=0;
                buff = strsep(&buff, "\n");
                char *str = malloc(BUFFER_SIZE);
                sprintf(str, "%s: '%s'\n", message_from, buff);
                // write(STDOUT_FILENO, str, BUFFER_SIZE);
                
                write(testout, str, BUFFER_SIZE);


                // printf("Writing to pipe: %s (strlen %ld)\n", buff, strlen(buff));

                pid_t p;
                p = fork();
                if (p == 0) {
                    char *MODE = malloc(BUFFER_SIZE);
                    if (strcmp(message_from, "A") == 0) {
                        sprintf(MODE, "B->A");
                    }
                    else {
                        sprintf(MODE, "A->B");
                    }
                    char *encode_this_location = "encode_this.dat";
                    int encode_this = open(encode_this_location, O_CREAT | O_TRUNC | O_WRONLY, 0644);
                    write(encode_this, str, strlen(str) + 1);
                    close(encode_this);
                    // int execerr = execlp("bash", "bash", "encode.sh", str, MODE, write_pipe_location, NULL);
                    int execerr = execlp("bash", "bash", "encode.sh", encode_this_location, MODE, write_pipe_location, NULL);
                    err(execerr, "execlp error");
                    exit(0);
                }

            }
            // else {
            //     exit(0);
            // }
        }

        if (FD_ISSET(pipe_read, &read_fds)) {
            int bytes = read(pipe_read, buff, BUFFER_SIZE);

            if (bytes) {
                buff[strlen(buff)]=0;
                buff = strsep(&buff, "\n");

                // printf("%s read pipe %d bytes %ld strlen\n", message_from, bytes, strlen(buff));
               
                // printf("%s read pipe %ld strlen\n", message_from, strlen(buff));
                // printf("%s\n", buff);

                char *str = malloc(BUFFER_SIZE);
                sprintf(str, "%s read pipe: %s -- %ld strlen\n", message_from, buff, strlen(buff));
                // write(testout, str, BUFFER_SIZE);

                pid_t p;
                p = fork();
                // printf("Forked! %s %d %d\n", buff, bytes, getpid());
                // err(p, "fork fail");
                if (p == 0) {
                    char *printmsg = malloc(BUFFER_SIZE);
                    sprintf(printmsg, "%s\n", buff);
                    write(testout, printmsg, BUFFER_SIZE);
                    char *MODE = malloc(BUFFER_SIZE);
                    if (strcmp(message_from, "A") == 0) {
                        sprintf(MODE, "B->A");
                    }
                    else {
                        sprintf(MODE, "A->B");
                    }
                    // char *print_this_location = "print_this.dat";
                    // int print_this = open(print_this_location, O_CREAT | O_TRUNC | O_WRONLY, 0644);
                    // write(print_this, buff, BUFFER_SIZE);
                    // close(print_this);
                    int execerr = execlp("bash", "bash", "print.sh", buff, MODE, NULL);
                    // int execerr = execlp("bash", "bash", "print.sh", print_this_location, MODE, NULL);
                    err(execerr, "execlp error");
                    exit(0);
                }
            }
            // if (bytes > 1) {
            //     buff[strlen(buff) - 1]=0;
            //     buff = strsep(&buff, "\n");
            //     printf("%s\n",buff);
            // }
        }


    }
}



