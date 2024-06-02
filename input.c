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

    int testout = open("testout.txt", O_CREAT | O_WRONLY | O_APPEND, 0644);

    int pipe_read = open(pipe_location, O_RDONLY);
    char *testprint = malloc(BUFFER_SIZE);
    sprintf(testprint, "Opened pipe (input)\n");
    write(testout, testprint, BUFFER_SIZE);

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
                write(STDOUT_FILENO, str, BUFFER_SIZE);
            }
            // else {
            //     exit(0);
            // }
        }

        if (FD_ISSET(pipe_read, &read_fds)) {
            int bytes = read(pipe_read, buff, BUFFER_SIZE);

            if (bytes) {
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
                    int execerr = execlp("bash", "bash", "print.sh", buff, NULL);
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



