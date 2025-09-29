// mini_vi_safe.c - ultra-minimal vi-like editor (safer checks)
// gcc -std=c99 -Wall -Wextra -g -O2 -o mini_vi_safe mini_vi_safe.c
// ./mini_vi_safe filename

#define _POSIX_C_SOURCE 200809L
#include <termios.h>
#include <unistd.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <errno.h>

struct termios orig_term;

void die(const char *s) {
    perror(s);
    tcsetattr(STDIN_FILENO, TCSAFLUSH, &orig_term);
    exit(1);
}

void disable_raw_mode(void) {
    tcsetattr(STDIN_FILENO, TCSAFLUSH, &orig_term);
}

void enable_raw_mode(void) {
    if (tcgetattr(STDIN_FILENO, &orig_term) == -1) die("tcgetattr");
    if (atexit(disable_raw_mode) != 0) die("atexit");

    struct termios raw = orig_term;
    raw.c_iflag &= ~(BRKINT | ICRNL | IXON);
    raw.c_oflag &= ~(OPOST);
    raw.c_cflag |= (CS8);
    raw.c_lflag &= ~(ECHO | ICANON | ISIG | IEXTEN);
    raw.c_cc[VMIN] = 0;
    raw.c_cc[VTIME] = 1;
    if (tcsetattr(STDIN_FILENO, TCSAFLUSH, &raw) == -1) die("tcsetattr");
}

int safe_read_char(void) {
    char c;
    ssize_t n;
    while ((n = read(STDIN_FILENO, &c, 1)) != 1) {
        if (n == -1) {
            if (errno == EAGAIN || errno == EWOULDBLOCK) continue;
            die("read");
        }
    }
    return (int)((unsigned char)c);
}

typedef struct {
    char *buf;
    size_t len;
    size_t cap;
    size_t cx; // cursor index
    char *fname;
    int dirty;
} Editor;

void editor_init(Editor *E) {
    E->cap = 1024;
    E->buf = malloc(E->cap);
    if (!E->buf) die("malloc");
    E->buf[0] = '\0';
    E->len = 0;
    E->cx = 0;
    E->fname = NULL;
    E->dirty = 0;
}

void *xrealloc(void *p, size_t newsize) {
    void *q = realloc(p, newsize);
    if (!q) die("realloc");
    return q;
}

void editor_open(Editor *E, const char *fname) {
    if (!fname) return;
    E->fname = strdup(fname);
    if (!E->fname) die("strdup");
    FILE *f = fopen(fname, "r");
    if (!f) {
        // new empty buffer is fine
        return;
    }
    if (fseek(f, 0, SEEK_END) == -1) { fclose(f); die("fseek"); }
    long sz = ftell(f);
    if (sz < 0) { fclose(f); die("ftell"); }
    rewind(f);

    if ((size_t)sz + 1 > E->cap) {
        E->cap = (size_t)sz + 1;
        E->buf = xrealloc(E->buf, E->cap);
    }
    size_t r = fread(E->buf, 1, sz, f);
    if (r != (size_t)sz && ferror(f)) { fclose(f); die("fread"); }
    E->len = r;
    E->buf[E->len] = '\0';
    fclose(f);
    if (E->cx > E->len) E->cx = E->len;
}

void editor_save(Editor *E) {
    if (!E->fname) return;
    FILE *f = fopen(E->fname, "w");
    if (!f) {
        // can't save; leave dirty flag
        return;
    }
    if (E->len > 0) {
        if (fwrite(E->buf, 1, E->len, f) != E->len) {
            fclose(f);
            return;
        }
    }
    fclose(f);
    E->dirty = 0;
}

void ensure_capacity(Editor *E, size_t need) {
    if (E->cap > need) return;
    size_t newcap = E->cap ? E->cap * 2 : 1024;
    while (newcap <= need) newcap *= 2;
    E->buf = xrealloc(E->buf, newcap);
    E->cap = newcap;
}

void editor_insert(Editor *E, char c) {
    ensure_capacity(E, E->len + 2);
    memmove(E->buf + E->cx + 1, E->buf + E->cx, E->len - E->cx + 1);
    E->buf[E->cx] = c;
    E->len++;
    E->cx++;
    E->dirty = 1;
}

void editor_backspace(Editor *E) {
    if (E->cx == 0) return;
    memmove(E->buf + E->cx - 1, E->buf + E->cx, E->len - E->cx + 1);
    E->cx--;
    E->len--;
    E->dirty = 1;
}

void editor_move_left(Editor *E) {
    if (E->cx > 0) E->cx--;
}
void editor_move_right(Editor *E) {
    if (E->cx < E->len) E->cx++;
}
void editor_move_home(Editor *E) {
    while (E->cx > 0 && E->buf[E->cx - 1] != '\n') E->cx--;
}
void editor_move_end(Editor *E) {
    while (E->cx < E->len && E->buf[E->cx] != '\n') E->cx++;
}

void get_row_col(Editor *E, size_t idx, int *row, int *col) {
    int r = 1, c = 1;
    for (size_t i = 0; i < idx && i < E->len; ++i) {
        if (E->buf[i] == '\n') { r++; c = 1; }
        else c++;
    }
    *row = r; *col = c;
}

void editor_refresh(Editor *E, const char *status) {
    write(STDOUT_FILENO, "\x1b[2J\x1b[H", 7);
    if (E->len > 0) write(STDOUT_FILENO, E->buf, E->len);
    // Status line
    write(STDOUT_FILENO, "\n-- ", 4);
    write(STDOUT_FILENO, status, strlen(status));
    // Move cursor
    int row = 1, col = 1;
    get_row_col(E, E->cx, &row, &col);
    char esc[64];
    int n = snprintf(esc, sizeof(esc), "\x1b[%d;%dH", row, col);
    if (n > 0) write(STDOUT_FILENO, esc, n);
}

int main(int argc, char **argv) {
    if (argc < 2) {
        fprintf(stderr, "usage: %s filename\n", argv[0]);
        return 1;
    }

    enable_raw_mode();

    Editor E;
    editor_init(&E);
    editor_open(&E, argv[1]);

    enum { NORMAL, INSERT, CMD } mode = NORMAL;
    char cmd[64] = {0};
    int cmdlen = 0;
    char status[128] = "NORMAL";

    while (1) {
        editor_refresh(&E, status);
        int c = safe_read_char();

        if (mode == NORMAL) {
            if (c == 'h') editor_move_left(&E);
            else if (c == 'l') editor_move_right(&E);
            else if (c == '0') editor_move_home(&E); // '0' to line start (very tiny vi-ish)
            else if (c == '$') editor_move_end(&E);  // '$' to line end
            else if (c == 'i') { mode = INSERT; strcpy(status, "INSERT"); }
            else if (c == ':') { mode = CMD; cmdlen = 0; cmd[0] = '\0'; strcpy(status, ":"); }
            else if (c == 'j') {
                // basic down: move to next newline if present
                size_t p = E.cx;
                while (p < E.len && E.buf[p] != '\n') p++;
                if (p < E.len) {
                    p++; // move to start of next line
                    // try to preserve column by scanning forward
                    int currow, curcol;
                    get_row_col(&E, E.cx, &currow, &curcol);
                    // simple approach: move forward curcol-1 chars or until newline
                    size_t q = p;
                    int moved = 1;
                    while (moved < curcol && q < E.len && E.buf[q] != '\n') { q++; moved++; }
                    E.cx = q;
                }
            } else if (c == 'k') {
                // basic up: find previous newline and move roughly same column left
                if (E.cx == 0) { /* top */ }
                else {
                    size_t p = (E.cx > 0) ? E.cx - 1 : 0;
                    // find start of current line
                    while (p > 0 && E.buf[p - 1] != '\n') p--;
                    if (p > 0) {
                        // find previous line start
                        size_t q = p - 1;
                        while (q > 0 && E.buf[q - 1] != '\n') q--;
                        int currow, curcol;
                        get_row_col(&E, E.cx, &currow, &curcol);
                        size_t r = q;
                        int moved = 1;
                        while (moved < curcol && r < E.len && E.buf[r] != '\n') { r++; moved++; }
                        E.cx = r;
                    }
                }
            }
        }
        else if (mode == INSERT) {
            if (c == 27) { // ESC
                mode = NORMAL; strcpy(status, "NORMAL");
            } else if (c == 127 || c == 8) {
                editor_backspace(&E);
            } else if (c == '\r' || c == '\n') {
                editor_insert(&E, '\n');
            } else if (c >= 32 && c <= 126) {
                editor_insert(&E, (char)c);
            }
        }
        else if (mode == CMD) {
            if (c == '\r' || c == '\n') {
                cmd[cmdlen] = '\0';
                if (strcmp(cmd, "q") == 0) break;
                else if (strcmp(cmd, "w") == 0) editor_save(&E);
                else if (strcmp(cmd, "wq") == 0) { editor_save(&E); break; }
                // unknown commands ignored
                mode = NORMAL; strcpy(status, "NORMAL");
            } else if (c == 27) { // ESC
                mode = NORMAL; strcpy(status, "NORMAL");
            } else if (c == 127 || c == 8) {
                if (cmdlen > 0) cmd[--cmdlen] = '\0';
                snprintf(status, sizeof(status), ":%s", cmd);
            } else if (c >= 32 && c <= 126) {
                if (cmdlen < (int)sizeof(cmd) - 1) {
                    cmd[cmdlen++] = (char)c;
                    cmd[cmdlen] = '\0';
                    snprintf(status, sizeof(status), ":%s", cmd);
                }
            }
        }
    }

    write(STDOUT_FILENO, "\x1b[2J\x1b[H", 7); // clear
    free(E.buf);
    free(E.fname);
    return 0;
}
