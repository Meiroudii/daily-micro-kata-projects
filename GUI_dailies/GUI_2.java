import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import java.awt.Color;
import java.awt.Font;
import java.awt.BorderLayout;

public class GUI_2 {
  private static JTextArea texts;
  private static void visual_interface() {
    JFrame frame = new JFrame("Label");
    frame.setSize(600, 600);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setLayout(new BorderLayout());

    texts = new JTextArea();
    texts.setBackground(Color.BLACK);
    texts.setForeground(Color.GREEN);
    texts.setFont(new Font("Mono", Font.PLAIN, 52));

    JScrollPane scroll_pane = new JScrollPane(texts);
    frame.add(scroll_pane, BorderLayout.CENTER);

    JLabel label = new JLabel("This is the blogs to use for the happening stuffs");
    try {
      frame.add(label, BorderLayout.NORTH);
    } catch (IllegalArgumentException err) {
      System.out.println(err.getMessage());
      err.printStackTrace();
    }
    frame.setVisible(true); 
  }

  public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {
        visual_interface();
        texts.append("It works?");
    });
  }

}
