//package start;

import javax.swing.*;

public class GUI {
  private static void AnotherFockinGUI() {
    JFrame frame = new JFrame("FUUUUUUUUUUUU");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    JLabel label = new JLabel("Ubiquotous hello world");
    JLabel poem = new JLabel("Fuck shit right cool Good to Know");
    frame.getContentPane().add(label, poem);

    frame.pack();
    frame.setVisible(true);
  }

  private static void DeathThreat() {
    JFrame frame = new JFrame("We will kill you.");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    JLabel label = new JLabel("Please let us kill you.");
    frame.getContentPane().add(label);

    frame.pack();
    frame.setVisible(true);
  }
  public static void main(String[] args) {
    javax.swing.SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        AnotherFockinGUI();
      }
    });
  }
}

