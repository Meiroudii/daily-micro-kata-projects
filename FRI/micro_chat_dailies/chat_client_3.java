import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class chat_client_3 {
    private JFrame frame;
    private JPanel chatPanel;
    private JTextField inputField;
    private JScrollPane scrollPane;
    private PrintWriter out;
    private BufferedReader in;
    private Socket socket;

    // Discord-style dark mode palette
    private final Color bgColor = new Color(0x2B2D31);
    private final Color bubbleMine = new Color(0x5865F2);
    private final Color bubbleOther = new Color(0x404249);
    private final Color textMine = Color.WHITE;
    private final Color textOther = new Color(0xE4E6EB);
    private final Color textSystem = new Color(0xAAAAAA);

    public chat_client_3(String host, int port) {
        setupUI();

        appendSystem("Connecting to " + host + ":" + port + " ...");
        inputField.setEditable(false);

        new Thread(() -> {
            try {
                socket = new Socket(host, port);
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);

                SwingUtilities.invokeLater(() -> {
                    inputField.setEditable(true);
                    appendSystem("Connected!");
                });

                listen();
            } catch (IOException e) {
                appendSystem("Connection failed: " + e.getMessage());
            }
        }).start();
    }

    private void setupUI() {
        frame = new JFrame("Chat Client (Dark Mode)");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 600);
        frame.setLocationRelativeTo(null);

        chatPanel = new JPanel();
        chatPanel.setLayout(new BoxLayout(chatPanel, BoxLayout.Y_AXIS));
        chatPanel.setBackground(bgColor);

        scrollPane = new JScrollPane(chatPanel);
        scrollPane.setBorder(null);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.getViewport().setBackground(bgColor);

        inputField = new JTextField();
        inputField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        inputField.setBackground(new Color(0x313338));
        inputField.setForeground(Color.WHITE);
        inputField.setCaretColor(Color.WHITE);
        inputField.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        inputField.addActionListener(e -> {
            String msg = inputField.getText().trim();
            if (!msg.isEmpty() && out != null) {
                out.println(msg);
                appendMessage("You", msg, true);
                inputField.setText("");
            }
        });

        frame.setLayout(new BorderLayout());
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(inputField, BorderLayout.SOUTH);
        frame.setVisible(true);
    }

    private void listen() {
        try {
            String msg;
            while ((msg = in.readLine()) != null) {
                appendMessage("Anon", msg, false);
            }
        } catch (IOException e) {
            appendSystem("Disconnected.");
        }
    }

    private void appendMessage(String sender, String text, boolean isMine) {
        SwingUtilities.invokeLater(() -> {
            MessageBubble bubble = new MessageBubble(sender, text, isMine,
                    isMine ? bubbleMine : bubbleOther,
                    isMine ? textMine : textOther);
            JPanel wrapper = new JPanel(new BorderLayout());
            wrapper.setBackground(bgColor);
            wrapper.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

            if (isMine)
                wrapper.add(bubble, BorderLayout.EAST);
            else
                wrapper.add(bubble, BorderLayout.WEST);

            chatPanel.add(wrapper);
            chatPanel.revalidate();
            chatPanel.repaint();

            JScrollBar vbar = scrollPane.getVerticalScrollBar();
            vbar.setValue(vbar.getMaximum());
        });
    }

    private void appendSystem(String text) {
        SwingUtilities.invokeLater(() -> {
            JLabel label = new JLabel("[System] " + text, SwingConstants.CENTER);
            label.setFont(new Font("Segoe UI", Font.ITALIC, 12));
            label.setForeground(textSystem);
            JPanel wrapper = new JPanel();
            wrapper.setBackground(bgColor);
            wrapper.add(label);
            chatPanel.add(wrapper);
            chatPanel.revalidate();
            chatPanel.repaint();
        });
    }

    public static void main(String[] args) {
        String host = JOptionPane.showInputDialog("Enter server IP:", "localhost");
        if (host == null || host.isEmpty()) return;
        new chat_client_3(host.trim(), 3000);
    }
}

class MessageBubble extends JPanel {
    private final String sender;
    private final String text;
    private final boolean isMine;
    private final Color bubbleColor;
    private final Color textColor;
    private final String time;

    public MessageBubble(String sender, String text, boolean isMine, Color bubbleColor, Color textColor) {
        this.sender = sender;
        this.text = text;
        this.isMine = isMine;
        this.bubbleColor = bubbleColor;
        this.textColor = textColor;
        this.time = new SimpleDateFormat("HH:mm").format(new Date());
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Font font = new Font("Segoe UI", Font.PLAIN, 14);
        g2.setFont(font);
        FontMetrics fm = g2.getFontMetrics();

        int textWidth = Math.min(280, fm.stringWidth(text) + 20);
        int height = fm.getHeight() + 25;

        int x = isMine ? getWidth() - textWidth - 15 : 15;
        int y = 10;
        int arc = 16;

        // Shadow for depth
        g2.setColor(new Color(0, 0, 0, 40));
        g2.fillRoundRect(x + 2, y + 2, textWidth, height, arc, arc);

        // Bubble body
        g2.setColor(bubbleColor);
        g2.fillRoundRect(x, y, textWidth, height, arc, arc);

        // Message text
        g2.setColor(textColor);
        g2.drawString(text, x + 10, y + fm.getAscent() + 5);

        // Timestamp
        g2.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        g2.setColor(new Color(180, 180, 180));
        g2.drawString(time, x + textWidth - 35, y + height - 5);

        g2.dispose();
    }

    @Override
    public Dimension getPreferredSize() {
        FontMetrics fm = getFontMetrics(new Font("Segoe UI", Font.PLAIN, 14));
        int textWidth = Math.min(280, fm.stringWidth(text) + 30);
        int height = fm.getHeight() + 30;
        return new Dimension(textWidth + 30, height);
    }
}
