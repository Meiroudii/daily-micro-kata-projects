class BinaryChop1_1 {
  int binary_search (int arr[], int x) {
    int l = 0, r = arr.length - 1;
    while (l <= r) {
      int m = l + (r - l) / 2;
      if (arr[m] == x) { return m; }
      if (arr[m] < x) {
        l = m + l;
      } else {
        r = m - l;
      }
      return -1;
    }
  }
  public static void main(String[] args) {
    binary_search ob = new 
  }
}
