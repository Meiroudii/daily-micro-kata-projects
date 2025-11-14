import java.util.*;

class coin_toss_7 {
  public static void main(String[] args) {
    Random $r = new Random();
    ArrayList<String> $data = new ArrayList<>();
    List<String> $coins = Arrays.asList("heads", "tails");
    for (int $i = 0; $i < 600; $i++) {
      $data.add($coins.get($r.nextInt($coins.size())));
    }
    System.out.printf("%8s\n","rolls");
    for (int i = 0; i < $data.size(); i++) {
      System.out.printf("%-5d %8s\n", i, $data.get(i));
    }
    System.out.println("\n["+$data.size()+" rows x 1 columns]");
  }
}
