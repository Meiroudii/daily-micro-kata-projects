import java.awt.Color;
import java.awt.Font;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.net.ServerSocket;
import java.net.Socket;
import javax.swing.JTextArea;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

class HTTP21 {
  private static JTextArea texts;

  public static void main(String[] args) throws IOException {
    JFrame gui = new JFrame("HTTP Logs");
    gui.setSize(500,500);
    gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    texts = new JTextArea();
    texts.setBackground(Color.BLACK);
    texts.setForeground(Color.GREEN);
    texts.setFont(new Font("Mono", Font.PLAIN, 33));

    JScrollPane js = new JScrollPane(texts);
    gui.add(js);
    gui.setVisible(true);

    ServerSocket s = new ServerSocket(3000);
    System.out.println("Server is now live!");
    puts("Listening in port 3000");
    while (true) {
      try (Socket c = s.accept()) {
        InputStreamReader isr = new InputStreamReader(c.getInputStream());
        BufferedReader  bf = new BufferedReader(isr);
        String line = bf.readLine();
        while (line != null || !line.isEmpty()) {
          puts(line);
          line = bf.readLine();
        }
      } catch(IOException err) {
        System.out.println(err.getMessage());
      }
    }
  }

  public static void puts(String str) {
    SwingUtilities.invokeLater(() -> {
      texts.append(str+"\n");
    });
  }
}
