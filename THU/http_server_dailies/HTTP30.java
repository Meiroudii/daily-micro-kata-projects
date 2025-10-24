import java.awt.*;
import java.net.*;
import java.util.Date;
import java.io.*;
import javax.swing.*;

class HTTP30 {
  private static JTextArea _text;

  public static void print_gui(String str) {
    SwingUtilities.invokeLater(() -> {
      _text.append(str);
    });
  }

  public static void main(String[] args) throws IOException {
    final int _PORT = 3000;
    final ServerSocket _SERVER = new ServerSocket(_PORT);
    JFrame frame = new JFrame("Logs");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize(900, 900);

    _text = new JTextArea();
    _text.setForeground(Color.GREEN);
    _text.setBackground(Color.RED);
    _text.setFont(new Font("Courier", Font.BOLD, 34));

    JScrollPane _scroll = new JScrollPane(_text);
    frame.add(_scroll);
    frame.setVisible(true);

    print_gui("Listening on port"+_PORT);
    while(true) {
      try (Socket _client = _SERVER.accept()) {
        Date today = new Date();
        String res = "HTTP/1.1 200 OK\r\n\r\n"+today;
        _client.getOutputStream().write(res.getBytes("UTF-8"));

        InputStreamReader isr = new InputStreamReader(_client.getInputStream());
        BufferedReader buf = new BufferedReader(isr);
        String recv = buf.readLine();

        while (recv != null & !recv.isEmpty()) {
          print_gui(recv);
          System.out.printf("""
            \t\t+-----------------------------------------------+
            \t\t|
            \t\t| %s
            \t\t|
            \t\t|
            \t\t+-----------------------------------------------+
          """, recv
          );
          recv = buf.readLine();
        }
      }
    }
  }
}
