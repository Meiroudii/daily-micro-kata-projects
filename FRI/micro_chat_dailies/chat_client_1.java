import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

public class chat_client_1 {
    private JFrame frame;
    private JTextPane chatPane;
    private JTextField inputField;
    private PrintWriter out;
    private BufferedReader in;
    private Socket socket;
    private StyledDocument doc;
    private SimpleAttributeSet centerStyle, userStyle, systemStyle;

    public chat_client_1(String serverHost, int port) {
        setupUI();

        inputField.setEditable(false);
        appendSystem("Connecting to " + serverHost + ":" + port + " ...");

        new Thread(() -> {
            try {
                socket = new Socket(serverHost, port);
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);

                SwingUtilities.invokeLater(() -> {
                    inputField.setEditable(true);
                    inputField.requestFocusInWindow();
                    appendSystem("Connected!");
                });

                listen();
            } catch (IOException e) {
                appendSystem("Connection error: " + e.getMessage());
            }
        }).start();
    }

    private void setupUI() {
        frame = new JFrame("Chat Client");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(550, 450);
        frame.setLocationRelativeTo(null);

        chatPane = new JTextPane();
        chatPane.setEditable(false);
        chatPane.setBackground(new Color(245, 245, 245));
        chatPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        chatPane.setFont(new Font("JetBrains Mono", Font.PLAIN, 32));

        doc = chatPane.getStyledDocument();
        centerStyle = new SimpleAttributeSet();
        StyleConstants.setAlignment(centerStyle, StyleConstants.ALIGN_CENTER);
        StyleConstants.setFontSize(centerStyle, 14);

        userStyle = new SimpleAttributeSet();
        StyleConstants.setForeground(userStyle, new Color(33, 150, 243)); // blue
        StyleConstants.setAlignment(userStyle, StyleConstants.ALIGN_CENTER);

        systemStyle = new SimpleAttributeSet();
        StyleConstants.setForeground(systemStyle, new Color(120, 120, 120));
        StyleConstants.setItalic(systemStyle, true);
        StyleConstants.setAlignment(systemStyle, StyleConstants.ALIGN_CENTER);

        JScrollPane scrollPane = new JScrollPane(chatPane);
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1, true));

        inputField = new JTextField();
        inputField.setFont(new Font("JetBrains Mono", Font.PLAIN, 14));
        inputField.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        inputField.addActionListener(e -> {
            String msg = inputField.getText().trim();
            if (!msg.isEmpty() && out != null) {
                out.println(msg);
                appendUser("You: " + msg);
                inputField.setText("");
            }
        });

        frame.setLayout(new BorderLayout(0, 10));
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(inputField, BorderLayout.SOUTH);
        frame.getRootPane().setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        frame.setVisible(true);
    }

    private void appendText(String text, SimpleAttributeSet style) {
        SwingUtilities.invokeLater(() -> {
            try {
                doc.setParagraphAttributes(doc.getLength(), 1, style, false);
                doc.insertString(doc.getLength(), text + "\n", style);
                chatPane.setCaretPosition(doc.getLength());
            } catch (BadLocationException e) {
                e.printStackTrace();
            }
        });
    }

    private void appendUser(String msg) {
        appendText(msg, userStyle);
    }

    private void appendServer(String msg) {
        appendText(msg, centerStyle);
    }

    private void appendSystem(String msg) {
        appendText("[System] " + msg, systemStyle);
    }

    private void listen() {
        try {
            String msg;
            while ((msg = in.readLine()) != null) {
                appendServer(msg);
            }
        } catch (IOException e) {
            appendSystem("Disconnected: " + e.getMessage());
        } finally {
            try { if (socket != null) socket.close(); } catch (IOException ignored) {}
            SwingUtilities.invokeLater(() -> inputField.setEditable(false));
        }
    }

    public static void main(String[] args) {
        String host = JOptionPane.showInputDialog("Enter server IP:", "localhost");
        if (host == null || host.trim().isEmpty()) return;
        new chat_client_1(host.trim(), 3000);
    }
}
