import java.awt.Color;
import java.awt.Font;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.JFrame;
import javax.swing.JScrollPane;

class HTTP22 {
  private static JTextArea texts;
  private static JScrollPane scroll_pane;

  public static void main(String[] args) throws IOException {
    JFrame gui = new JFrame("HTTP Logs");
    gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    gui.setSize(500, 500);

    texts = new JTextArea();
    texts.setBackground(Color.BLACK);
    texts.setForeground(Color.GREEN);
    texts.setFont(new Font("Mono", Font.PLAIN, 20));

    scroll_pane = new JScrollPane(texts);
    gui.add(scroll_pane);
    gui.setVisible(true);



    final ServerSocket s = new ServerSocket(3000);
    System.out.println("\t\tListening on 3000");
    puts("\t\tListening on 3000");
    while(true) {

      try (Socket c = s.accept()) {
        InputStreamReader isr = new InputStreamReader(c.getInputStream());
        BufferedReader buf = new BufferedReader(isr);
        String res = buf.readLine();
        try {
          while (res != null || !res.isEmpty()) {
            puts(res);
            System.out.println(res);
            res = buf.readLine();
          }
        } catch (NullPointerException err) {
          String msg = """
          \t\t\t___________________
          \t\t\t|                  |
          \t\t\t|   N O T I C E:   |
          \t\t\t|------------------|
          \t\t\t| connection close |
          \t\t\t|  by the user.    |
          \t\t\t|__________________|
          """;
          puts(msg);
          System.out.println(msg);
        }
      }
    }
  }
  public static void puts(String str) {
    SwingUtilities.invokeLater(() -> {
      texts.append(str+"\r\n");
    });
  }
}
