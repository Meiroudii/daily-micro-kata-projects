#include <bits/stdc++.h>
using namespace std;
int main() {
  int n = 10;

  int a = 0, b = n - 1;
  while(a <= b) {
    int k = (a+b)/2;
    if (array[k] == x) {
      cout << "found";
    }
    if (array[k] < x) a = k+1;
    else b = k-1;
  }
}
