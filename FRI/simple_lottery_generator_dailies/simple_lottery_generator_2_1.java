import java.util.*;
import java.util.stream.*;

public class simple_lottery_generator_2_1 {
    public static void main(String[] args) {
        System.out.println(lotteryNumber(6, 49));
    }

    static List<Integer> lotteryNumber(int n, int max) {
        return IntStream.rangeClosed(1, max)
                        .boxed()
                        .collect(Collectors.collectingAndThen(Collectors.toList(), list -> {
                            Collections.shuffle(list);
                            return list.subList(0, n).stream().sorted().toList();
                        }));
    }
}
