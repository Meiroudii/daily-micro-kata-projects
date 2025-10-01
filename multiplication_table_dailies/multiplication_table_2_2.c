#include <stdio.h>

void mult_table(int n) {
    for (int i = 1; i <= n; i++) {
        for (int j = 1; j <= n; j++) {
            printf("| %d\t", i * j);
        }
        printf("\n");
    }
}

int main() {
    mult_table(10);
    return 0;
}
