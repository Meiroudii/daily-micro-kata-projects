import java.util.stream.IntStream;

public class catalan_numbers_2 {

    // factorial using streams (like Ruby's inject)
    public static long factorial(int n) {
        return IntStream.rangeClosed(1, n)
                        .reduce(1, (a, b) -> a * b);
    }

    // catalan sequence using map (like Ruby's map)
    public static long[] catalan(int n) {
        return IntStream.range(0, n)
                        .mapToLong(i -> factorial(2 * i) / (factorial(i + 1) * factorial(i)))
                        .toArray();
    }

    public static void main(String[] args) {
        long[] result = catalan(5);
        System.out.println(java.util.Arrays.toString(result));
    }
}
