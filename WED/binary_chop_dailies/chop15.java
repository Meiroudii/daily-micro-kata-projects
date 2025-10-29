public class chop15 {

    // Recursive binary search method
    public static boolean recursiveBinarySearch(int[] arr, int target) {
        return recursiveBinarySearchHelper(arr, target, 0, arr.length - 1);
    }

    // Helper method that performs the actual recursive search
    private static boolean recursiveBinarySearchHelper(int[] arr, int target, int low, int high) {
        if (low > high) {
            return false; // Base case: target not found
        }

        int mid = low + (high - low) / 2;

        if (arr[mid] == target) {
            return true; // Base case: found the target
        } else if (arr[mid] > target) {
            // Search in the left half
            return recursiveBinarySearchHelper(arr, target, low, mid - 1);
        } else {
            // Search in the right half
            return recursiveBinarySearchHelper(arr, target, mid + 1, high);
        }
    }

    public static void main(String[] args) {
        int[] arr = {1, 3, 5, 7, 9, 11, 13};
        int target = 5;
        System.out.println(recursiveBinarySearch(arr, target)); // Output: true
    }
}
