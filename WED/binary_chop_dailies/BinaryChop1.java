public class BinaryChop1 {
  public static int immaSearch(int[] arr, int target, int left, int right) {
    if (left > right) {
      return -1;
    }
    int mid_point = left + (right- left) / 2;

    if (arr[mid_point]==target) {
      return mid_point;
    }
    if (arr[mid_point] > target) {
      return immaSearch(arr, target, left, mid_point - 1);
    }
    return immaSearch(arr, target, mid_point + 1, right);
  }
  public static void main(String[] args) {
    int[] arr = {1,3,45,5,6,67,23,6,23,6,34,4,6,4,63,64,2};
    int target = 63; 

    int result = immaSearch(arr,target, 0, arr.length - 1);
    if (result == -1) {
      System.out.println("I can't seeeeeeeeEEEEEEEEEeeeeeeeeEEEEeeeeeee");
    } else {
      System.out.println("I found!!! at "+result);
    }
  }
}
