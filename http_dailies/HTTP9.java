import java.awt.Color;
import java.net.ServerSocket;
import java.net.Socket;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import java.text.SimpleDateFormat;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.Date;

class HTTP9 {
  private static JTextArea text_area;

  public static void main(String[] args) throws IOException {
    JFrame http_frame = new JFrame("View HTTP logs");
    http_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    http_frame.setSize(800, 800);

    text_area = new JTextArea();
    text_area.setEditable(true);
    text_area.setBackground(Color.BLACK);
    text_area.setForeground(Color.GREEN);
    text_area.setFont(new java.awt.Font("Monospaced", java.awt.Font.PLAIN, 29));

    JScrollPane scroll_pane = new JScrollPane(text_area);
    http_frame.add(scroll_pane);
    http_frame.setVisible(true);

    final int PORT = 3000;
    final ServerSocket SERVER = new ServerSocket(PORT);
    system_out_print("The logs will showed here:");

    while (true) {
      try(Socket client = SERVER.accept()) {
        Date today = new Date();
        String res = "HTTP/1.1 200 OK"+today;
        client.getOutputStream().write(res.getBytes("UTF-8"));

        SimpleDateFormat simple_date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String right_now = simple_date.format(today);
        system_out_print(right_now);

        InputStreamReader isr = new InputStreamReader(client.getInputStream());
        BufferedReader buffer = new BufferedReader(isr);
        String stdout_line =  buffer.readLine();
        while (stdout_line != null && !stdout_line.isEmpty()) {
          system_out_print(stdout_line);
          stdout_line = buffer.readLine();
        }
      }
    }
  }

  public static void system_out_print(String message) {
    SwingUtilities.invokeLater(() -> {text_area.append(message+"\n");});
  }
}
