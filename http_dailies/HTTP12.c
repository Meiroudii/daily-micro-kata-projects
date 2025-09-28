// simple_http.c
// Minimal poll()-based HTTP/1.1 server (GET only). For learning/chaos.
// Compile: make
// Run:   ./simple_http
// Test:  curl -v http://127.0.0.1:8080/

#define _POSIX_C_SOURCE 200809L
#include <arpa/inet.h>
#include <errno.h>
#include <fcntl.h>
#include <netinet/in.h>
#include <poll.h>
#include <signal.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/socket.h>
#include <time.h>
#include <unistd.h>

#define PORT 8080
#define LISTEN_BACKLOG 128
#define MAX_CLIENTS 256
#define RECV_BUF_SIZE 8192   // maximum header buffer per connection
#define RESPONSE_BODY "<html><head><style>body { font-size: 3rem;background: hsl(222, 25%, 25%); color: white; display: flex; justify-content: center; align-items: center; height: 100vh; width:100%;}</style></head><body><h1>It works</h1></body></html>"
#define RESPONSE_BODY_LEN (sizeof(RESPONSE_BODY)-1)
#define IDLE_TIMEOUT_SECONDS 10

static volatile int keep_running = 1;

static void handle_sigint(int x) {
    (void)x;
    keep_running = 0;
}

// set non-blocking on fd, fatal on error
static void set_nonblocking(int fd) {
    int flags = fcntl(fd, F_GETFL, 0);
    if (flags == -1) { perror("fcntl(F_GETFL)"); exit(1); }
    if (fcntl(fd, F_SETFL, flags | O_NONBLOCK) == -1) { perror("fcntl(F_SETFL)"); exit(1); }
}

typedef enum {
    REQ_READING_HEADERS,
    REQ_READY,
    REQ_ERROR
} req_state_t;

typedef struct {
    int fd;
    req_state_t state;
    char buf[RECV_BUF_SIZE];
    size_t buflen;
    time_t last_active;
} client_t;

static client_t *clients[MAX_CLIENTS];
static struct pollfd pfds[MAX_CLIENTS + 1]; // [0] = listen socket

static int create_and_bind() {
    int listen_fd = socket(AF_INET, SOCK_STREAM, 0);
    if (listen_fd < 0) { perror("socket"); exit(1); }

    int yes = 1;
    if (setsockopt(listen_fd, SOL_SOCKET, SO_REUSEADDR, &yes, sizeof(yes)) < 0) {
        perror("setsockopt SO_REUSEADDR");
    }

    struct sockaddr_in addr;
    memset(&addr, 0, sizeof(addr));
    addr.sin_family = AF_INET;
    addr.sin_addr.s_addr = INADDR_ANY;
    addr.sin_port = htons(PORT);

    if (bind(listen_fd, (struct sockaddr*)&addr, sizeof(addr)) < 0) {
        perror("bind");
        close(listen_fd);
        exit(1);
    }

    if (listen(listen_fd, LISTEN_BACKLOG) < 0) {
        perror("listen");
        close(listen_fd);
        exit(1);
    }

    set_nonblocking(listen_fd);
    return listen_fd;
}

static void client_close(int idx) {
    client_t *c = clients[idx];
    if (!c) return;
    close(c->fd);
    free(c);
    clients[idx] = NULL;
    pfds[idx+1].fd = -1;
    pfds[idx+1].events = 0;
    pfds[idx+1].revents = 0;
}

// naive parse request line: "GET /path HTTP/1.1"
static int parse_request_line(char *line, char *method, size_t mlen, char *path, size_t plen) {
    // find tokens separated by spaces
    char *p = line;
    char *space = strchr(p, ' ');
    if (!space) return -1;
    size_t ms = space - p;
    if (ms >= mlen) return -1;
    memcpy(method, p, ms);
    method[ms] = '\0';

    p = space + 1;
    space = strchr(p, ' ');
    if (!space) return -1;
    size_t ps = space - p;
    if (ps >= plen) return -1;
    memcpy(path, p, ps);
    path[ps] = '\0';

    // ignore version token for now
    return 0;
}

static void send_400(int fd) {
    const char *resp = "HTTP/1.1 400 Bad Request\r\nConnection: close\r\nContent-Length: 11\r\n\r\nBad Request";
    send(fd, resp, strlen(resp), MSG_NOSIGNAL);
}

static void send_501(int fd) {
    const char *resp = "HTTP/1.1 501 Not Implemented\r\nConnection: close\r\nContent-Length: 15\r\n\r\nNot Implemented";
    send(fd, resp, strlen(resp), MSG_NOSIGNAL);
}

static void send_500(int fd) {
    const char *resp = "HTTP/1.1 500 Internal Server Error\r\nConnection: close\r\nContent-Length: 21\r\n\r\nInternal Server Error";
    send(fd, resp, strlen(resp), MSG_NOSIGNAL);
}

static void handle_request_and_respond(client_t *c) {
    // ensure null-terminated buffer for parsing convenience
    if (c->buflen == 0) {
        send_400(c->fd);
        return;
    }
    c->buf[c->buflen] = '\0';

    // find first line
    char *line_end = strstr(c->buf, "\r\n");
    if (!line_end) { send_400(c->fd); return; }
    size_t line_len = line_end - c->buf;
    char reqline[1024];
    if (line_len >= sizeof(reqline)) { send_400(c->fd); return; }
    memcpy(reqline, c->buf, line_len);
    reqline[line_len] = '\0';

    char method[16], path[1024];
    if (parse_request_line(reqline, method, sizeof(method), path, sizeof(path)) != 0) {
        send_400(c->fd);
        return;
    }

    if (strcmp(method, "GET") != 0) {
        send_501(c->fd);
        return;
    }

    // naive path handling: block path traversal attempts
    if (strstr(path, "..")) {
        send_400(c->fd);
        return;
    }

    // Build response
    char header[512];
    int header_len = snprintf(header, sizeof(header),
        "HTTP/1.1 200 OK\r\n"
        "Connection: close\r\n"
        "Content-Type: text/html; charset=utf-8\r\n"
        "Content-Length: %zu\r\n"
        "\r\n",
        (size_t)RESPONSE_BODY_LEN);

    if (header_len < 0 || header_len >= (int)sizeof(header)) {
        send_500(c->fd);
        return;
    }

    // send header then body. ignore partial send handling for brevity.
    ssize_t sent = send(c->fd, header, header_len, MSG_NOSIGNAL);
    if (sent < 0) { return; }
    sent = send(c->fd, RESPONSE_BODY, RESPONSE_BODY_LEN, MSG_NOSIGNAL);
    (void)sent;
}

int main(void) {
    signal(SIGINT, handle_sigint);
    int listen_fd = create_and_bind();
    fprintf(stderr, "[+] Listening on 0.0.0.0:%d\n", PORT);

    // initialize clients array and pollfd array
    for (int i = 0; i < MAX_CLIENTS; ++i) {
        clients[i] = NULL;
        pfds[i+1].fd = -1;
        pfds[i+1].events = 0;
        pfds[i+1].revents = 0;
    }
    pfds[0].fd = listen_fd;
    pfds[0].events = POLLIN;

    while (keep_running) {
        int nfds = 1; // we always have the listen socket at index 0
        for (int i = 0; i < MAX_CLIENTS; ++i) {
            if (clients[i]) {
                pfds[nfds] = pfds[i+1];
                ++nfds;
            }
        }

        int timeout_ms = 1000; // wake periodically for idle cleanup
        int ret = poll(pfds, nfds, timeout_ms);
        if (ret < 0) {
            if (errno == EINTR) continue;
            perror("poll");
            break;
        }
        time_t now = time(NULL);

        // check listen socket events
        if (pfds[0].revents & POLLIN) {
            // accept loop
            while (1) {
                struct sockaddr_in peer;
                socklen_t plen = sizeof(peer);
                int cfd = accept(listen_fd, (struct sockaddr*)&peer, &plen);
                if (cfd < 0) {
                    if (errno == EAGAIN || errno == EWOULDBLOCK) break;
                    perror("accept");
                    break;
                }
                // find free slot
                int idx = -1;
                for (int i = 0; i < MAX_CLIENTS; ++i) {
                    if (!clients[i]) { idx = i; break; }
                }
                if (idx == -1) {
                    // too many clients
                    close(cfd);
                    fprintf(stderr, "[-] Too many clients, rejecting\n");
                    break;
                }
                set_nonblocking(cfd);
                client_t *cl = calloc(1, sizeof(client_t));
                cl->fd = cfd;
                cl->state = REQ_READING_HEADERS;
                cl->buflen = 0;
                cl->last_active = now;
                clients[idx] = cl;
                pfds[idx+1].fd = cfd;
                pfds[idx+1].events = POLLIN;
                pfds[idx+1].revents = 0;

                char addrbuf[INET_ADDRSTRLEN];
                inet_ntop(AF_INET, &peer.sin_addr, addrbuf, sizeof(addrbuf));
                fprintf(stderr, "[+] New conn %d from %s:%d (slot %d)\n", cfd, addrbuf, ntohs(peer.sin_port), idx);
            }
        }

        // check client events (note: pollfd array compacted into pfds[1..nfds-1])
        for (int i = 1; i < nfds; ++i) {
            int cfd = pfds[i].fd;
            short revents = pfds[i].revents;
            if (cfd < 0) continue;

            // find client index for this fd
            int client_idx = i - 1;
            client_t *c = clients[client_idx];
            if (!c) continue;

            if (revents & (POLLERR | POLLHUP | POLLNVAL)) {
                fprintf(stderr, "[-] Client %d hung up or error\n", c->fd);
                client_close(client_idx);
                continue;
            }

            if (revents & POLLIN) {
                // read loop until EAGAIN
                while (1) {
                    ssize_t r = recv(c->fd, c->buf + c->buflen, RECV_BUF_SIZE - c->buflen - 1, 0);
                    if (r > 0) {
                        c->buflen += (size_t)r;
                        c->last_active = now;
                        // enforce header size limit
                        if (c->buflen >= RECV_BUF_SIZE - 1) {
                            fprintf(stderr, "[-] Header too large from fd %d\n", c->fd);
                            send_400(c->fd);
                            client_close(client_idx);
                            break;
                        }
                        // check if we have end of headers
                        if (strstr(c->buf, "\r\n\r\n")) {
                            c->state = REQ_READY;
                            handle_request_and_respond(c);
                            // close connection after response (simple)
                            client_close(client_idx);
                            break;
                        } else {
                            // continue reading more
                            continue;
                        }
                    } else if (r == 0) {
                        // peer closed connection
                        client_close(client_idx);
                        break;
                    } else {
                        if (errno == EAGAIN || errno == EWOULDBLOCK) {
                            // no more data right now
                            break;
                        } else {
                            perror("recv");
                            client_close(client_idx);
                            break;
                        }
                    }
                } // end read loop
            } // end POLLIN

            // idle timeout cleanup
            if (c && (now - c->last_active) > IDLE_TIMEOUT_SECONDS) {
                fprintf(stderr, "[-] Closing idle client %d\n", c->fd);
                client_close(client_idx);
            }
        } // end client loop
    } // end main loop

    fprintf(stderr, "[*] Shutting down\n");
    // close all clients
    for (int i = 0; i < MAX_CLIENTS; ++i) {
        if (clients[i]) client_close(i);
    }
    close(listen_fd);
    return 0;
}
