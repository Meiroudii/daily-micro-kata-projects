import java.io.*;
import java.net.*;
import javax.swing.*;
import java.util.Date;
import java.awt.*;

class HTTP29 {
  private static JTextArea text_area;
  static void show(String str) { SwingUtilities.invokeLater(() -> { text_area.append(str+"\n"); }); }
  public static void main(String[] args) throws IOException {
    String body = """
    <p style="font-size: 2rem;">WORKING?</p>
    """;
    JFrame frame = new JFrame("HTTP Logs");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize(500, 500);

    text_area = new JTextArea();
    text_area.setForeground(Color.RED);
    text_area.setBackground(Color.WHITE);
    text_area.setFont(new Font("Mono", Font.PLAIN, 32));

    JScrollPane js = new JScrollPane(text_area);
    frame.add(js);
    frame.setVisible(true);

    final ServerSocket SERVER = new ServerSocket(3000);
    while(true) {
      try (Socket client = SERVER.accept()) {
        Date today = new Date();
        String res = "HTTP/1.1 200 OK\r\n\r\n"+today+"\n"+body;
        client.getOutputStream().write(res.getBytes("UTF-8"));

        InputStreamReader isr = new InputStreamReader(client.getInputStream());
        BufferedReader buf = new BufferedReader(isr);
        String recv = buf.readLine();

        while (recv != null && !recv.isEmpty()) {
          show(recv);
          System.out.println(recv);
          recv = buf.readLine();
        }
      }
    }
  }
}
