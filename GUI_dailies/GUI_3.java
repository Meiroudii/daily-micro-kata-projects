import javax.swing.*;
class GUI_3 {
 GUI_3() {
 JFrame jfrm = new JFrame("A Simple Swing Application");
 jfrm.setSize(275, 100);
 jfrm.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
 JLabel jlab = new JLabel(" Swing means powerful GUIs.");
 // Add the label to the content pane.
  
 jfrm.add(jlab);
 // Display the frame.
 jfrm.setVisible(true);
 }
 public static void main(String args[]) {
 // Create the frame on the event dispatching thread.
 SwingUtilities.invokeLater(new Runnable() {
 public void run() {
 new GUI_3();
 }
 });
 }
}
