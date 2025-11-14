import java.util.*;
class Coin_Toss_6 {
public static void main(String[] args) {
Random rand = new Random();
ArrayList<String> data = new ArrayList<>();
List<String> coins = Arrays.asList("heads", "tails");
for (int i = 0; i < 600; i++) {
data.add(coins.get(rand.nextInt(coins.size())));
}
System.out.printf("%8s\n", "roll");
for (int i = 0; i < data.size(); i++) {
System.out.printf("%-5d %8s\n", i, data.get(i));
}
System.out.println("\n[" + data.size() + " rows x 1 columns]");
}
}
