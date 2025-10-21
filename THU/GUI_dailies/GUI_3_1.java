import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

class GUI_3_1 {
  JLabel jlab;

  GUI_3_1() {
    JFrame jfrm = new JFrame("Confirmation");
    jfrm.setLayout(new FlowLayout());
    jfrm.setSize(220, 90);
    jfrm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    JButton jbtnAlpha = new JButton("Alpha");
    JButton jbtnBeta = new JButton("Beta");

    jbtnAlpha.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent as) {
        jlab.setText("Alpha was pressed.");
      }
    });

    jbtnBeta.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent as) {
        jlab.setText("BETA!!");
      }
    });

    jfrm.add(jbtnAlpha);
    jfrm.add(jbtnBeta);

    jlab = new JLabel("Choose.");
    jfrm.add(jlab);
    jfrm.setVisible(true);
  }

  public static void main(String[] args) {
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        new GUI_3_1();
      }
    });
  }
}
