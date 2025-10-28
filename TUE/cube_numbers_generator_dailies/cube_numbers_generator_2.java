import java.util.stream.IntStream;
import java.util.Arrays;

public class cube_numbers_generator_2 {
  public static int[] cubes(int n) {
    return IntStream.rangeClosed(1, n)
                    .map(i -> i * i * i)
                    .toArray();
  }
  public static void main(String[] args) {
    System.out.println(Arrays.toString(cubes(5)));
  }
}
