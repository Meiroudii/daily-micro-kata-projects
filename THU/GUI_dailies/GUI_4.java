import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

class GUI_4 {
  JLabel label;

  GUI_4() {
    JFrame frame = new JFrame("Ask");
    frame.setLayout(new FlowLayout());
    frame.setSize(220, 90);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    JButton accept = new JButton("YES");
    JButton decline = new JButton("NO");

    accept.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent as) {
        label.setText("You have been joined the Chaos Covenant.");
      }
    });

    decline.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent as) {
        label.setText("Oh... that's unfortunate.");
      }
    });

    frame.add(accept);
    frame.add(decline);

    label = new JLabel("If you want, you can seek for my bleesing.");
    frame.add(label);
    frame.setVisible(true);
  }

  public static void main(String[] args) {
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        new GUI_4();
      }
    });
  }
}
