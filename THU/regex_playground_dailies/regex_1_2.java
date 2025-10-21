import java.util.Scanner;
import java.util.regex.*;

class regex_1_2 {
  private static Scanner get_input_in;
  private static Pattern regex_pattern;
  public static void main(String[] args) {
    get_input_in = new Scanner(System.in);

    System.out.print("What is your rant? ");
    String rants = get_input_in.nextLine();
    regex_pattern = Pattern.compile("[ ,.!]");

    String[] tokens = regex_pattern.split(rants);

    for (String token : tokens) {
      System.out.println("\t\t"+token);
    }
  }
}
