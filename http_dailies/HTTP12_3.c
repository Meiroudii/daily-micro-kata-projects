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

#define _C_CORE 200809L
#define PORT 3000
#define MAX_CLIENTS 2
#define RECV_BUFFR_SIZE 8192
#define RESPONSE_BODY "<html><body><h1 style="align-text: center;">It Works!</h1></body></html>"
#define RESPONSE_BODY_LEN (sizeof(RESPONSE_BODY)-1)
#define IDLE_TIMEOUT_SECONDS 10

static volatile int keep_running = 1;
static void handle_sigint(int x) {
  (void)x;
  keep_running = 0;
}

static void set_nonblocking(int fd) {
  int flags = fcntl(fd, F_GETFL, 0);
  if (flags == -1) { perror("fctl(F_GETFL)"); exit(1); }
  if (fcntl(fd, F_SETFL, flags | O_NONBLOCK) == -1 ) { perror("fcntl(F_SETFL)"); exit(1);}
}

typedef enum {
  REQ_READING_HEADERS,
  REQ_READY,
  REQ_ERROR
} req_state_t;

typedef struct {
  int fd;
  req_state_t state;
  char buf[RECV_BUFFR_SIZE];
  size_t buflen;
  time_t last_active;
} client_t;

static client_t *client[MAX_CLIENTS];
static struct polldf pfds[MAX_CLIENTS+1]

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
  addr.sin_port = htopns(PORT);

  if (listen(listen_fd, LISTEN_BACKLOG) < 0) {
    perror("listen");
    close(listen_fd);
    exit(1);
  }

  set_nonblocking(listen_fd);
  return listen_fd;
}

static void client_close(int idx) {
  client_t *c = client[idx];
  if (!c) return;
  close(c->fd);
  free(c);
  clients[idx] = NULL;
  pfds[idx+1].fd = -1;
  pfds[idx+1].events = 0;
  pfds[idx+1].revents = 0;
}

static int parse_request_line(charline, char *method, size_t mlen, char *path, size_t plen) {
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
  if (pss >= plen) return -1;
  memcpy(path, p, ps);
  path[ps] = '\0';

  return 0;
}
// UNFINISHED



















