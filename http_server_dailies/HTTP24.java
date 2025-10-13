import java.awt.Color;
import java.awt.Font;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import javax.swing.SwingUtilities;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JButton;
//import java.awt.event.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

class HTTP24 {
  private static JLabel jlab;
  private static JTextArea texts;
  private static JScrollPane scroll_pane;
  public static void main(String[] args) throws IOException {
    JFrame jfrm = new JFrame("HTTP LOGS");
    jfrm.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    jfrm.setSize(900, 900);

    texts = new JTextArea();
    texts.setFont(new Font("Mono", Font.PLAIN, 22));
    texts.setForeground(Color.ORANGE);
    texts.setBackground(Color.BLACK);

    JButton view_logs_btn = new JButton("View Logs");
    JButton remove_logs_btn = new JButton("Remove Logs");

    view_logs_btn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent as) {
        puts("LOGS: ");
        jlab.setText("The logs \n=\n=\n=");
      }
    });

    remove_logs_btn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent as) {
        jlab.setText("Logs removed successfully?");
      }
    });

    scroll_pane = new JScrollPane(texts);
    jfrm.add(view_logs_btn);
    jfrm.add(remove_logs_btn);
    jlab = new JLabel("MENU");
    jfrm.add(jlab);
    jfrm.add(scroll_pane);
    jfrm.setVisible(true);

    final int PORT = 3000;
    final ServerSocket SERVER = new ServerSocket(PORT);
    String header = """
    \t\t|| > > L I S T E N I N G  O N  P O R T 3000
    """;
    puts(header);
    System.out.println(header);
    while(true) {
      try (Socket client = SERVER.accept()) {
        Date today = new Date();
        String res = "HTTP/1.1 200 OK\r\n\r\n"+today;
        client.getOutputStream().write(res.getBytes("UTF-8"));

        InputStreamReader isr = new InputStreamReader(client.getInputStream());
        BufferedReader buffrd = new BufferedReader(isr);
        String recv = buffrd.readLine();
        while(recv != null && !recv.isEmpty()) {
          puts(recv);
          System.out.println(recv);
          recv = buffrd.readLine();
        }
      } catch(NullPointerException err) {
        System.out.println("500");
      }
    }
  }

  public static void puts(String str) {
    SwingUtilities.invokeLater(() -> {
      texts.append(str+"\r\n");
    });
  }
}
