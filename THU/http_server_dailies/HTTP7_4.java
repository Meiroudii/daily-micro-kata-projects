import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import javax.swing.*;
import java.awt.Color;

public class HTTP7_4 {
    private static JTextArea textArea;

    public static void main(String[] args) throws IOException {
        // Create JFrame and JTextArea for GUI
        JFrame frame = new JFrame("HTTP Server Logs");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 500);

        // Create a JTextArea with scroll pane to display messages
        textArea = new JTextArea();
        textArea.setEditable(false);
        
        // Set dark mode colors
        textArea.setBackground(Color.BLACK); // Set background to black
        textArea.setForeground(Color.WHITE); // Set text color to white (whitesmoke)
        
        // Set a larger font size for better readability
        textArea.setFont(new java.awt.Font("Monospaced", java.awt.Font.PLAIN, 16)); // Larger font size (16)

        JScrollPane scrollPane = new JScrollPane(textArea);
        frame.add(scrollPane);

        // Set the frame to be visible
        frame.setVisible(true);

        // Set up server socket
        final ServerSocket SERVER = new ServerSocket(3000);
        System.out.println("localhost:3000");

        while(true) {
            try (Socket client = SERVER.accept()) {
                InputStreamReader isr = new InputStreamReader(client.getInputStream());
                BufferedReader b = new BufferedReader(isr);
                String l = b.readLine();

                while(l != null && !l.isEmpty()) {
                    // Update GUI with incoming request data
                    displayMessageInGUI(l);
                    l = b.readLine();
                }
            }
        }
    }

    // Method to append messages to the JTextArea
    private static void displayMessageInGUI(String message) {
        SwingUtilities.invokeLater(() -> {
            textArea.append(message + "\n");
        });
    }
}

