#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <sys/socket.h>
#include <sys/epoll.h>
#include <arpa/inet.h>
#include <fcntl.h>
#include <errno.h>

#define TIMEOUT 3000  // 3 seconds timeout for connection
#define PORTS_SIZE 5  // Number of ports to scan

// List of ports to scan
int ports[] = {22, 80, 443, 8080, 3306};

// Set socket to non-blocking mode
void set_nonblocking(int sockfd) {
    int flags = fcntl(sockfd, F_GETFL, 0);
    fcntl(sockfd, F_SETFL, flags | O_NONBLOCK);
}

// Function to scan ports using epoll
void scan_ports(const char *target_ip) {
    int epoll_fd = epoll_create1(0);
    if (epoll_fd == -1) {
        perror("epoll_create1 failed");
        return;
    }

    struct epoll_event events[PORTS_SIZE];
    struct sockaddr_in target_addr;
    int sockfd[PORTS_SIZE];

    for (int i = 0; i < PORTS_SIZE; i++) {
        sockfd[i] = socket(AF_INET, SOCK_STREAM, 0);
        if (sockfd[i] < 0) {
            perror("socket creation failed");
            continue;
        }

        set_nonblocking(sockfd[i]);

        target_addr.sin_family = AF_INET;
        target_addr.sin_port = htons(ports[i]);
        target_addr.sin_addr.s_addr = inet_addr(target_ip);

        // Attempt to connect (non-blocking)
        if (connect(sockfd[i], (struct sockaddr *)&target_addr, sizeof(target_addr)) == -1) {
            if (errno != EINPROGRESS) {
                close(sockfd[i]);
                continue; // Connection failed
            }
        }

        // Add socket to epoll for monitoring
        struct epoll_event ev;
        ev.events = EPOLLOUT | EPOLLERR | EPOLLHUP;
        ev.data.fd = sockfd[i];
        if (epoll_ctl(epoll_fd, EPOLL_CTL_ADD, sockfd[i], &ev) == -1) {
            perror("epoll_ctl failed");
            close(sockfd[i]);
            continue;
        }
    }

    // Wait for events (select or epoll_wait)
    int ready_fds = epoll_wait(epoll_fd, events, PORTS_SIZE, TIMEOUT);
    if (ready_fds < 0) {
        perror("epoll_wait failed");
    } else if (ready_fds == 0) {
        printf("Timeout reached, no ports responded in %d seconds.\n", TIMEOUT / 1000);
    }

    // Check which ports are open
    for (int i = 0; i < ready_fds; i++) {
        int port = ntohs(((struct sockaddr_in *)&events[i].data.ptr)->sin_port);
        if (events[i].events & (EPOLLERR | EPOLLHUP)) {
            printf("Port %d is closed or filtered.\n", port);
        } else {
            printf("Port %d is open.\n", port);
        }
    }

    // Cleanup
    for (int i = 0; i < PORTS_SIZE; i++) {
        close(sockfd[i]);
    }
    close(epoll_fd);
}

int main(int argc, char *argv[]) {
    if (argc < 2) {
        fprintf(stderr, "Usage: %s <target_ip>\n", argv[0]);
        return 1;
    }

    char *target_ip = argv[1];
    scan_ports(target_ip);

    return 0;
}
