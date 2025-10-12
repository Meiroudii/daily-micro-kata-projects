
import java.util.regex.*;

class regex_1 {
  public static void main(String[] args) {
    Pattern regex_pattern;
    Matcher matcher;
    boolean found;

    regex_pattern = Pattern.compile("Java");
    matcher = regex_pattern.matcher("Java");
    found = matcher.matches();

    System.out.println("Testing the Java against Java");
    if (found) System.out.println("Java got matched");
    else System.out.println("No match");

    System.out.println();

    System.out.println("Testing Java against Java 8.");
    matcher = regex_pattern.matcher("Java 8");

    found = matcher.matches();

    if (found) System.out.println("It matched");
    else System.out.println("Not matched");
  }
}
