import java.io.IOException;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.awt.Color;
import java.awt.Font;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.JFrame;

class HTTP19 {
  private static JTextArea textarea;

  public static void main(String[] args) throws IOException {
    JFrame http_main_frame = new JFrame("Logs");
    http_main_frame.setSize(500, 500);
    http_main_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    textarea = new JTextArea();
    textarea.setBackground(Color.BLACK);
    textarea.setForeground(Color.GREEN);
    textarea.setFont(new Font("Mono", Font.PLAIN, 33));

    JScrollPane js = new JScrollPane(textarea);
    http_main_frame.add(js);
    http_main_frame.setVisible(true);

    ServerSocket s = new ServerSocket(3000);
    System.out.println("Server is now open");
    while (true) {
      try (Socket c = s.accept()) {
        InputStreamReader isr = new InputStreamReader(c.getInputStream());
        BufferedReader buf = new BufferedReader(isr);
        String line = buf.readLine();
        while (line != null && !line.isEmpty()) {
          display_msg(line);
          line = buf.readLine();
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
  static void display_msg(String msg) {
    SwingUtilities.invokeLater(() -> {
      textarea.append(msg+"\n");
    });
  }
}
