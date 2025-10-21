import javax.swing.JTextArea;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import java.awt.Color;
import java.text.SimpleDateFormat;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.Date;
import java.net.ServerSocket;
import java.net.Socket;

class HTTP8 {
  
  private static JTextArea text_area;

  public static void main(String[] args) throws IOException {
    JFrame http_frame = new JFrame("View HTTP logs");
    http_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    http_frame.setSize(500, 500);

    text_area = new JTextArea();
    text_area.setEditable(false);
    text_area.setBackground(Color.BLACK);
    text_area.setForeground(Color.WHITE);
    text_area.setFont(new java.awt.Font("Monospaced", java.awt.Font.PLAIN, 29));

    JScrollPane scroll_pane = new JScrollPane(text_area);
    http_frame.add(scroll_pane);
    http_frame.setVisible(true);

    final int PORT = 3000;
    final ServerSocket SERVER = new ServerSocket(PORT);
    views_controller("The Server is Now Live!");

    while(true) {
      try (Socket client = SERVER.accept()) {
        Date today = new Date();
        String read = "HTTP/1.1 200 OK"+today;
        client.getOutputStream().write(read.getBytes("UTF-8"));

        SimpleDateFormat simple_date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String right_now =  simple_date.format(today);
        views_controller(right_now);

        InputStreamReader isr = new InputStreamReader(client.getInputStream());
        BufferedReader bf = new BufferedReader(isr);
        String line = bf.readLine();

        while (line != null && !line.isEmpty()) {
          views_controller(line);
          line = bf.readLine();
        }
      }
    }


  }

  public static void views_controller(String message) {
    SwingUtilities.invokeLater(() -> {
      text_area.append(message + "\n");
    });
  }
}
