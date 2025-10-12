// This will give you a flashbang---you have been warned
import java.awt.Font;
import java.awt.Color;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

class HTTP23 {
  private static JTextArea text_encode; private static BufferedReader buf; private static InputStreamReader isr;
  private static JFrame gui_frame; private static JScrollPane scroll_pane; private static Date date;

  public static void main(String[] args) throws IOException {

    final int PORT = 3000;
    final ServerSocket SERVER = new ServerSocket(PORT);

    gui_frame = new JFrame("LOGS: ");
    gui_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    gui_frame.setSize(500, 500);

    text_encode = new JTextArea();
    text_encode.setBackground(Color.WHITE);
    text_encode.setForeground(Color.ORANGE);
    text_encode.setFont(new Font("Courier", Font.PLAIN, 23));

    scroll_pane = new JScrollPane(text_encode);
    gui_frame.add(scroll_pane);
    gui_frame.setVisible(true);

    String header = """
    \t ___________________
    \t/                   \
    \t| Listening on port |
    \t|    3000           |
    \t|___________________|
    """;
    print_gui(header);
    System.out.println(header);
    while(true) {
      try (Socket client = SERVER.accept()) {
        date = new Date();
        String res = "HTTP/1.1 200 OK\r\n\r\n"+date+"\r\n\r\n"+header;
        client.getOutputStream().write(res.getBytes("UTF-8"));


        InputStreamReader isr = new InputStreamReader(client.getInputStream());
        BufferedReader buf = new BufferedReader(isr);
        String response_buffer = buf.readLine();

        while (response_buffer != null && !response_buffer.isEmpty()) {
          print_gui(response_buffer);
          System.out.println(response_buffer);
          response_buffer =  buf.readLine();
        }


      } catch (NullPointerException err) {
        String msg = """
        \t\t__________________________________
        \t\t| C O N N E C T I O N   C L O S E |
        \t\t| by the user                     |
        \t\t|_________________________________|
        """;
        print_gui(msg);
        System.out.println(msg);
      }
    }

  }

  public static void print_gui(String buffer) {
    SwingUtilities.invokeLater(() -> {
      text_encode.append(buffer+"\r\n");
    });
  }
}


