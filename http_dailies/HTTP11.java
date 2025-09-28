import javax.swing.*;
import java.awt.Color;
import java.text.SimpleDateFormat;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.ServerSocket;
import java.util.Date;

class HTTP11 {
  private static JTextArea text_area;
  public static void main(String[] args) throws IOException {

    JFrame http_frame = new JFrame("View HTTP logs");
    http_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    http_frame.setSize(1024, 1024);

    text_area = new JTextArea();
    text_area.setEditable(true);
    text_area.setBackground(Color.BLACK);
    text_area.setForeground(Color.GREEN);
    text_area.setFont(new java.awt.Font("Monospaced", java.awt.Font.PLAIN, 34));

    JScrollPane sp = new JScrollPane(text_area);
    http_frame.add(sp);
    http_frame.setVisible(true);


    final ServerSocket SERVER = new ServerSocket(3000);
    System.out.println("The server is up!");
    on_gui("The server is up!");
    while(true) {
      try (Socket client = SERVER.accept()) {
        Date today = new Date();
        String res = "HTTP/1.1 200 OK\r\n\r\n"+today;
        client.getOutputStream().write(res.getBytes("UTF-8"));

        InputStreamReader isr = new InputStreamReader(client.getInputStream());
        BufferedReader buf = new BufferedReader(isr);

        String line = buf.readLine();
        while (line != null && !line.isEmpty()) {
          on_gui(line);
          line = buf.readLine();
        }
      }
    }
  }

  public static void on_gui(String message) {
    SwingUtilities.invokeLater(() -> {text_area.append("\t\t"+message+"\n"); });
  }
}
