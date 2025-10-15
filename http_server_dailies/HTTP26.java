import java.awt.*;
import java.io.*;
import java.net.*;
import javax.swing.*;
import java.util.Date;

class HTTP26 {
  private static JTextArea texts;
  public static void main(String[] args) throws IOException {
    final int PORT = 3000;
    final ServerSocket SERVER = new ServerSocket(PORT);
    JFrame jfrm = new JFrame("HTTP LOGS");
    texts = new JTextArea();
    JScrollPane scroll_pane = new JScrollPane(texts);

    jfrm.setSize(800, 800);
    jfrm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    texts.setForeground(Color.WHITE);
    texts.setBackground(Color.BLUE);
    texts.setFont(new Font("Courier", Font.PLAIN, 43));

    jfrm.add(scroll_pane);
    jfrm.setVisible(true);

    System.out.println("Listening on port: "+PORT);
    puts("Listening on port: "+PORT);
    while(true) {
      try (Socket client = SERVER.accept()) {
        Date today = new Date();
        String res = "HTTP/1.1 200 OK\r\n\r\n"+today;
        client.getOutputStream().write(res.getBytes("UTF-8"));

        InputStreamReader isr = new InputStreamReader(client.getInputStream());
        BufferedReader buf = new BufferedReader(isr);
        String server_res = buf.readLine();
        while(server_res != null && !server_res.isEmpty()) {
          puts(server_res);
          System.out.println(server_res);
          server_res = buf.readLine();
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
