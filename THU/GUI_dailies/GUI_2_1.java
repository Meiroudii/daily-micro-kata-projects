import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import java.awt.Color;
import java.awt.Font;
import java.awt.BorderLayout;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class GUI_2_1 {

    private static JTextArea texts;

    private static void visual_interface() {
        
        JFrame frame = new JFrame("Label");
        frame.setSize(600, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        
        frame.setLayout(new BorderLayout());

        
        texts = new JTextArea();
        texts.setBackground(Color.BLACK);
        texts.setForeground(Color.GREEN);
        texts.setFont(new Font("Mono", Font.PLAIN, 20));  

        
        JScrollPane scrollPane = new JScrollPane(texts);
        frame.add(scrollPane, BorderLayout.CENTER);  

        
        JLabel label = new JLabel("This is the blogs to use for the happening stuffs");
        frame.add(label, BorderLayout.NORTH);  

        
        readFileAndDisplay();

        
        frame.setVisible(true);
    }

    private static void readFileAndDisplay() {
        try (BufferedReader br = new BufferedReader(new FileReader("notes.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                texts.append(line + "\n");
            }
        } catch (IOException e) {
            texts.append("Error reading the file: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            visual_interface();
        });
    }
}
