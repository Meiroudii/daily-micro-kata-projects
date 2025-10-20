import java.io.*;
import java.net.*;
import javax.swing.*;
import java.util.Date;
import java.awt.*;

class HTTP29 {
  private static JTextArea text_area;
  static void show(String str) { SwingUtilities.invokeLater(() -> { text_area.append(str+"\n"); }); }
  static void show(int x) { SwingUtilities.invokeLater(() -> { text_area.append(x); }); }
  public static void main(String[] args) {
    JFrame frame = new JFrame("HTTP Logs");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize(500, 500);

    text_area = new JTextArea();
  }
}
