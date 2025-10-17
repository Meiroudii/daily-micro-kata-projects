import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.net.*;
import java.util.Date;

class HTTP27 {
  private static JTextArea buffer_texts;
  public static void main(String[] args) throws IOException {
    final int PORT = 3000;
    final ServerSocket SERVER = new ServerSocket(PORT);
    JFrame jfrm = new JFrame("SERVER");
    jfrm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    jfrm.setSize(900, 900);
    buffer_texts = new JTextArea();
    buffer_texts.setFont(new Font("Courier", Font.BOLD, 54));
    buffer_texts.setForeground(Color.GRAY);
    buffer_texts.setBackground(Color.BLUE);
    JScrollPane scroll_pane = new JScrollPane(buffer_texts);
    jfrm.add(scroll_pane);
    jfrm.setVisible(true);

    final String NOTICE = """
    \t\t|
    \t\t| --->  LISTENING ON PORT: 3000
    \t\t|
    """;
    System.out.println(NOTICE);
    puts(NOTICE);
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
      }
      catch (NullPointerException err) {
        err.printStackTrace();
      }
    }
  }
  public static void puts(String str) {
    SwingUtilities.invokeLater(() -> {
      buffer_texts.append(str+"\n");
    });
  }
}
