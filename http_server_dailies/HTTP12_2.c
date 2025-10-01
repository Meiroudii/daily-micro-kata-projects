#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <arpa/inet.h>
#include <netinet/in.h>
#include <sys/socket.h>

#define PORT 3000
#define BUF_SIZE 4096

int main(void) {
    int server_fd, client_fd;
    struct sockaddr_in addr;
    socklen_t addrlen = sizeof(addr);
    char buf[BUF_SIZE];

    // Create TCP socket
    server_fd = socket(AF_INET, SOCK_STREAM, 0);
    if (server_fd < 0) { perror("socket"); exit(1); }

    int yes = 1;
    if (setsockopt(server_fd, SOL_SOCKET, SO_REUSEADDR, &yes, sizeof(yes)) < 0) {
        perror("setsockopt");
        close(server_fd);
        exit(1);
    }

    memset(&addr, 0, sizeof(addr));
    addr.sin_family = AF_INET;
    addr.sin_addr.s_addr = INADDR_ANY;
    addr.sin_port = htons(PORT);

    if (bind(server_fd, (struct sockaddr*)&addr, sizeof(addr)) < 0) {
        perror("bind");
        close(server_fd);
        exit(1);
    }

    if (listen(server_fd, 10) < 0) {
        perror("listen");
        close(server_fd);
        exit(1);
    }

    printf("Server is now live on port %d!\n", PORT);

    while (1) {
        client_fd = accept(server_fd, (struct sockaddr*)&addr, &addrlen);
        if (client_fd < 0) { perror("accept"); continue; }

        printf("---- New connection ----\n");

        FILE *stream = fdopen(client_fd, "r");
        if (!stream) { perror("fdopen"); close(client_fd); continue; }

        while (fgets(buf, sizeof(buf), stream)) {
            if (strcmp(buf, "\r\n") == 0 || strcmp(buf, "\n") == 0) {
                break; // end of HTTP headers
            }
            printf("%s", buf);
        }
        printf("------------------------\n");

        fclose(stream); // closes client_fd too
    }

    close(server_fd);
    return 0;
}
