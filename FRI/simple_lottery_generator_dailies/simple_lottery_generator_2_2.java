import java.util.*;
import java.util.stream.*;

public class simple_lottery_generator_2_2 {
  public static void main(String[] args) {
    System.out.println(lottery_number(6, 49));
  }
  static List<Integer> lottery_number(int n, int max) {
    return IntStream.rangeClosed(1, max)
                    .boxed()
                    .collect(Collectors.collectingAndThen(Collectors.toList(), list -> {
      Collections.shuffle(list);
      return list.subList(0, n).stream().sorted().toList();
    }));
  }
}
