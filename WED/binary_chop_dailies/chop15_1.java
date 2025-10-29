import java.util.Arrays;
public class chop15_1 {
  public static boolean recursive_binary_search(int[] arr, int target) {
    return recursive_binary_search_helper(arr, target, 0, arr.length - 1);
  }
  private static boolean recursive_binary_search_helper(int[] arr, int target, int low,  int high) {
    if (low > high) {
      return false;
    }
    int mid = low + (high - low) / 2;
    if (arr[mid] == target) {
      return true;
    } else if (arr[mid] > target) {
      return recursive_binary_search_helper(arr, target, low, mid - 1);
    } else {
      return recursive_binary_search_helper(arr, target, mid + 1, high);
    }
  }

  public static void main(String[] args) {
    int[] arr = {1,3,4,5,5,5,54,3,2,35};
    int target = 5;
    System.out.printf("""
      target: %d
      array: %s
      is it there?: %b
    """, target,Arrays.toString(arr), recursive_binary_search(arr, target));
  }
}
