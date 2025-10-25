public class ConsoleUI {

    // Helper to repeat a character 'n' times (since String.repeat() isn't in Java 8)
    private static String repeat(char c, int count) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < count; i++) {
            sb.append(c);
        }
        return sb.toString();
    }

    public void displayMessages(String[] messages) {
        int maxLength = 0;

        // Find the longest message to determine the box width
        for (String msg : messages) {
            if (msg.length() > maxLength) {
                maxLength = msg.length();
            }
        }

        int boxWidth = maxLength + 4; // add some padding

        // Print top border
        System.out.printf("\t\t\t|%s|%n", repeat('-', boxWidth - 2));

        // Print each message centered
        for (String msg : messages) {
            int paddingLeft = (boxWidth - 2 - msg.length()) / 2;
            int paddingRight = (boxWidth - 2 - msg.length() - paddingLeft);
            String line = "\t\t\t|" + repeat(' ', paddingLeft) + msg + repeat(' ', paddingRight) + "|";
            System.out.println(line);
        }

        // Print bottom border
        System.out.printf("\t\t\t|%s|%n", repeat('-', boxWidth - 2));
    }

    public static void main(String[] args) {
        ConsoleUI ui = new ConsoleUI();
        String[] messages = {
            "Hello, Java 8!",
            "UI using printf, loops, and conditionals!",
            "No libraries, just code!"
        };
        ui.displayMessages(messages);
    }
}
