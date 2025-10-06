import java.io.IOException;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.awt.Color;
import java.awt.Font;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.JFrame;

class HTTP19 {
  private static JTextArea textarea;

  public static void main(String[] args) {
    JFrame http_main_frame = new JFrame("Logs");
    setSize
    setDefaultCloseOperation

    textarea = new JTextArea();
    textarea.setBackground(Color.BLACK);
    textarea.setFore
    set.Bac
    set.Fo
    setFont(new Font(), Font.PLAIN
    http_main_frame.append(fuck);

    JSc new JS(textarea);
    frame.add(JSC)
    setVisible(true)

    ServerSocket s = new ServerSocket(3000);
    System.out.println("Server is now open");
    while (true) {
      try (Socket c = s.accept()) {

      } catch (IOException e) {
        System.out.println(e.printStackTrace());
      }
    }


  }

  static void display_msg(String msg) {
    SwingUtilities.invokeLater(() -> {
      textarea.append(msg+"\n");
    });
  }
}
