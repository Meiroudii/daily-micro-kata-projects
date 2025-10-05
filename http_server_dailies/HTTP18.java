import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.JFrame;
import java.awt.Color;
import java.awt.Font;


class HTTP18 {
  private static JTextArea text_area;

  public static void main(String[] args) throws IOException {
    JFrame frame = new JFrame("HTTP");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize(880,880);

    text_area = new JTextArea();
    text_area.setBackground(Color.BLACK);
    text_area.setForeground(Color.GREEN);
    text_area.setFont(new Font("Courier", Font.PLAIN, 32));

    JScrollPane sp = new JScrollPane(text_area);
    frame.add(sp);
    frame.setVisible(true);

    ServerSocket s = new ServerSocket(3000);
    display_msg("Server now kickin");
    System.out.println("Server now kickin");
    while (true) {
      try (Socket c = s.accept()) {
        InputStreamReader isr = new InputStreamReader(c.getInputStream());
        BufferedReader b = new BufferedReader(isr);
        String line = b.readLine();
        while (line != null && !line.isEmpty()) {
          display_msg(line);
          line = b.readLine();
        }
      }
    }
  }

  static void display_msg(String msg) {
    SwingUtilities.invokeLater(() -> {
      text_area.append(msg+"\n");
    });
  }
}
