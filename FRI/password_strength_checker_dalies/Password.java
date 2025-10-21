import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;

public class Password extends JFrame {

    private JPasswordField passwordField;

    public Password() {
        setTitle("Password Strength Checker");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new FlowLayout());

        JLabel label = new JLabel("Enter Password:");
        passwordField = new JPasswordField(20);

        add(label);
        add(passwordField);

        // Add DocumentListener to check in real-time
        passwordField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                checkStrength();
            }

            public void removeUpdate(DocumentEvent e) {
                checkStrength();
            }

            public void changedUpdate(DocumentEvent e) {
                checkStrength();
            }
        });

        setVisible(true);
    }

    private void checkStrength() {
        String password = new String(passwordField.getPassword());

        int score = 0;

        if (password.length() >= 8) score++;
        if (password.matches(".*[A-Z].*")) score++;
        if (password.matches(".*[0-9].*")) score++;
        if (password.matches(".*[!@#$%^&*()].*")) score++;

        // Color logic
        if (score <= 1) {
            getContentPane().setBackground(Color.RED);      // weak
        } else if (score == 2 || score == 3) {
            getContentPane().setBackground(Color.ORANGE);   // medium
        } else {
            getContentPane().setBackground(Color.GREEN);    // strong
        }
    }

    public static void main(String[] args) {
        new Password();
    }
}
