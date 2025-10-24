import java.util.Scanner;
import java.util.regex.*;

class regex_2 {
  private static Scanner get_input;
  private static Pattern regex_pattern;
  public static void main(String[] args) {
    get_input = new Scanner(System.in);

    System.out.println("What's your rant?");
    String rants = get_input.nextLine();
    regex_pattern = Pattern.compile("[ ,.!]");

    String[] tokens = regex_pattern.split(rants);

    for (String token : tokens) {
      System.out.println("\t\t"+token);
    }
  }
}
