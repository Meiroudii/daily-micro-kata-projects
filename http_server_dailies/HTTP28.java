import java.io.*;
import java.net.*;
import javax.swing.*;
import java.awt.*;
import java.util.Date;

class HTTP28 {
  private static JTextArea texts;
  public static void main(String[] args) throws IOException {
    JFrame jfrm = new JFrame("HTTP");
    jfrm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    jfrm.setSize(800,800);

    texts = new JTextArea();
    texts.setForeground(Color.GREEN);
    texts.setBackground(Color.WHITE);
    texts.setFont(new Font("Mono", Font.PLAIN, 30));

    JScrollPane js = new JScrollPane(texts);
    jfrm.add(js);
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
    texts.append(str+"\n");
  }
}
