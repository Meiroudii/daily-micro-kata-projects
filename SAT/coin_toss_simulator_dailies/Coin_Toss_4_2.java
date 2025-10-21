import java.util.Scanner;
import java.util.Random;
import java.util.InputMismatchException;
import java.util.Arrays;
import java.util.List;

class Coin_Toss_4_2 {
  public static void main(String[] args) {
    Scanner gets = new Scanner(System.in);
    List<String> options = Arrays.asList("h", "t", "heads", "tails");
    Random rand = new Random();
    String result = rand.nextDouble() < 0.5 ? "h" : "t";
    int your_score = 0, bot_score = 0;
    while (true) {

      System.out.print("\t\tCoinflip (h/t): ");
      String guess = gets.nextLine().toLowerCase();

      if (options.contains(guess)) {
        if (guess.equals(result)) {
          System.out.println("\t\tYou win");
          your_score++;
          System.out.println("\t\tYou: "+your_score+"\n\t\tBot: "+bot_score);
        } else {
          System.out.println("\t\tYou lose");
          bot_score++;
          System.out.println("\t\tYou: "+your_score+"\n\t\tBot: "+bot_score);
        }
      } else {
        System.out.println("Command Error");
        break;
      }
    }
  }
}
