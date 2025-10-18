import java.io.*;
import java.net.*;
import java.util.Date;
import java.awt.*;
import javax.swing.*;

class HTTP27 {
  private static JTextArea texts;
  public static void main(String[] args) throws IOException {
    JFrame jfrm = new JFrame("HTTP Logs");
    jfrm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    jfrm.setSize(900, 900);

    texts = new JTextArea();
    texts.setFont(new Font("Mono", Font.ITALIC, 24));
    texts.setForeground(Color.RED);
    texts.setBackground(Color.WHITE);

    JScrollPane scroll_pane = new JScrollPane(texts);
    jfrm.add(scroll_pane);
    jfrm.setVisible(true);

    final int PORT = 3000;
    final ServerSocket SERVER = new ServerSocket(PORT);
    while(true) {
      try (Socket client = SERVER.accept()) {
        Date today = new Date();
        String res = "HTTP/1.1 200 OK\r\n\r\n"+today;
        client.getOutputStream().write(res.getBytes("UTF-8"));

        InputStreamReader isr = new InputStreamReader(client.getInputStream());
        BufferedReader buf = new BufferedReader(isr);
        String recv = buf.readLine();
        while(recv != null && !recv.isEmpty()) {
          puts(recv);
          System.out.println(recv);
          recv = buf.readLine();
        }

      } catch (NullPointerException err) {
        err.printStackTrace();
      }
    }

  }
  public static void puts(String str) {
    SwingUtilities.invokeLater(() -> {
      texts.append(str+"\n");
    });
  }
}
