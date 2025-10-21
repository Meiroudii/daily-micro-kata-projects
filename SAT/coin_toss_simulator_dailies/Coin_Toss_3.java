import java.util.Scanner;
import java.util.Random;
import java.util.InputMismatchException;

public class Coin_Toss_3 {
  public static void display_header() {
    System.out.println("\t\t ________________");
    System.out.println("\t\t/================\\");
    System.out.println("\t\t|==== ======= ====|");
    System.out.println("\t\t+=== ========= ===|");
    System.out.println("\t\t|     =======     |");
    System.out.println("\t\t|                 |");
    System.out.println("\t\t|  C  O  I  N     |");
    System.out.println("\t\t|                 |");
    System.out.println("\t\t|   F  L  I  P .  |");
    System.out.println("\t\t|                 |");
    System.out.println("\t\t+=== ========= ===|");
    System.out.println("\t\t|==== ======= ====|");
    System.out.println("\t\t|===== ===== =====|");
    System.out.println("\t\t\\-----------------/");
  }

  public static void main(String[] args) {
    Scanner get_input = new Scanner(System.in);
    Random rng_toss = new Random();

    int human_pts = 0, bot_pts = 0;
    display_header();
    while(true) {
      try {
        System.out.print("select:\n\t(t) Tails\n\t(h) Heads\n\t\t>> ");
        int human_toss = get_input.nextInt();
        int bot_toss = rng_toss.nextInt(1,3);
        if (bot_toss == 1) {
          System.out.println("Bot chooses: Heads");
        } else {
          System.out.println("Bot chooses: Tails");
        }
        if (human_toss == 1) {
          System.out.println("You choose: Heads");
        } else {
          System.out.println("You choose: Tails");
        }
        if (human_toss == bot_toss) {
          System.out.println("Shit, a fuckin tie!");
        } else if (human_toss <= bot_toss) {
          System.out.println("You Win");
          human_pts += 1;
          display_score(human_pts, bot_toss);
        } else {
          System.out.println("You lose!");
          bot_pts ++;
          display_score(human_pts, bot_toss);
        }
      } catch(InputMismatchException e) {
        System.out.println("Invalid Input, Try again");
        get_input.nextLine();
      }
    }
  }
  public static void display_score(int human_pts, int bot_pts) {
    System.out.println("\t\tYour Score: "+human_pts+"\n"+"\t\tBot Score: "+bot_pts);
  }

}
