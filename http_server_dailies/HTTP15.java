import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.ServerSocket;
import java.util.Date;
import java.awt.Color;
import java.awt.Font;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.JFrame;
import javax.swing.JScrollPane;

class HTTP15 {
  private static JTextArea text_area;

  public static void main(String[] args) throws IOException {
    JFrame http_server_frame = new JFrame("View HTTP logs");
    http_server_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    http_server_frame.setSize(800, 800);

    text_area = new JTextArea();
    text_area.setEditable(true);
    text_area.setBackground(Color.BLACK);
    text_area.setForeground(Color.GREEN);
    text_area.setFont(new Font("Monospaced", Font.PLAIN, 32));

    JScrollPane scroll_pane = new JScrollPane(text_area);
    http_server_frame.add(scroll_pane);
    http_server_frame.setVisible(true);

    final int PORT = 3000;
    display_this("Listening on port: 3000");
    System.out.println("Listening on port: 3000");
    final ServerSocket server = new ServerSocket(PORT);
    while(true) {
      try(Socket client = server.accept()) {
        Date today = new Date();
        String res = "HTTP/1.1 200 OK"+today;
        client.getOutputStream().write(res.getBytes("UTF-8"));


        InputStreamReader isr = new InputStreamReader(client.getInputStream());
        BufferedReader buff = new BufferedReader(isr);
        String line = buff.readLine();
        while (line != null && !line.isEmpty()) {
          display_this(line);
          line = buff.readLine();
        }
      }
    }
  }


  static void display_this(String text) {
    SwingUtilities.invokeLater(() -> {text_area.append(text+"\n");});
  }
}
