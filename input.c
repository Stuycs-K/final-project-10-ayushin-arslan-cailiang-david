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

int main() {
    setlinebuf(stdout);

    fd_set read_fds;

    char const * pipe_location = "mario";

    int pipe_read = open(pipe_location, O_RDONLY);

    printf("%d\n", STDIN_FILENO);
    printf("HEY\n");
    fflush( stdout );

    while(1){

        char *buff = malloc(BUFFER_SIZE);

        FD_ZERO(&read_fds);
        FD_SET(STDIN_FILENO, &read_fds);
        FD_SET(pipe_read,&read_fds);

        int maxfd = (pipe_read > STDIN_FILENO)?pipe_read:STDIN_FILENO;
        int i = select(maxfd+1, &read_fds, NULL, NULL, NULL);


        //if standard in, use fgets
        if (FD_ISSET(STDIN_FILENO, &read_fds)) {
            fgets(buff, sizeof(buff), stdin);
            buff[strlen(buff)]=0;
            buff = strsep(&buff, "\n");
            printf("Recieved from terminal: '%s'\n",buff);
        }

        if (FD_ISSET(pipe_read, &read_fds)) {
            int bytes = read(pipe_read, buff, sizeof(buff));
            if (bytes > 1) {
                buff[strlen(buff) - 1]=0;
                buff = strsep(&buff, "\n");
                printf("%s\n",buff);
            }
        }


    }
}



