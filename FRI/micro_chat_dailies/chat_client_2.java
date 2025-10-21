import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

public class chat_client_2 {
    private JFrame frame;
    private JPanel chatPanel;
    private JTextField inputField;
    private JScrollPane scrollPane;
    private PrintWriter out;
    private BufferedReader in;
    private Socket socket;

    private final Color myBubbleColor = new Color(0x4A90E2);
    private final Color otherBubbleColor = new Color(0xE5E5EA);
    private final Color bgColor = new Color(0xF5F5F7);

    public chat_client_2(String host, int port) {
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
        frame = new JFrame("Chat Client");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 600);
        frame.setLocationRelativeTo(null);

        chatPanel = new JPanel();
        chatPanel.setLayout(new BoxLayout(chatPanel, BoxLayout.Y_AXIS));
        chatPanel.setBackground(bgColor);

        scrollPane = new JScrollPane(chatPanel);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        inputField = new JTextField();
        inputField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        inputField.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        inputField.addActionListener(e -> {
            String msg = inputField.getText().trim();
            if (!msg.isEmpty() && out != null) {
                out.println(msg);
                appendMessage("You: " + msg, true);
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
                appendMessage(msg, false);
            }
        } catch (IOException e) {
            appendSystem("Disconnected.");
        }
    }

    private void appendMessage(String text, boolean isMine) {
        SwingUtilities.invokeLater(() -> {
            MessageBubble bubble = new MessageBubble(text, isMine ? myBubbleColor : otherBubbleColor, isMine);
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

            // Auto-scroll
            JScrollBar vbar = scrollPane.getVerticalScrollBar();
            vbar.setValue(vbar.getMaximum());
        });
    }

    private void appendSystem(String text) {
        SwingUtilities.invokeLater(() -> {
            JLabel label = new JLabel(text, SwingConstants.CENTER);
            label.setFont(new Font("Segoe UI", Font.ITALIC, 12));
            label.setForeground(Color.GRAY);
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
        new chat_client_2(host.trim(), 3000);
    }
}

class MessageBubble extends JPanel {
    private final String text;
    private final Color color;
    private final boolean isMine;

    public MessageBubble(String text, Color color, boolean isMine) {
        this.text = text;
        this.color = color;
        this.isMine = isMine;
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int arc = 20;
        int padding = 10;
        FontMetrics fm = g2.getFontMetrics(getFont());
        int width = Math.min(300, fm.stringWidth(text) + 20);
        int height = fm.getHeight() + 10;

        int x = isMine ? getWidth() - width - padding : padding;
        int y = padding;

        g2.setColor(color);
        g2.fillRoundRect(x, y, width, height, arc, arc);

        g2.setColor(isMine ? Color.WHITE : Color.BLACK);
        g2.drawString(text, x + 10, y + fm.getAscent() + 5);
        g2.dispose();
    }

    @Override
    public Dimension getPreferredSize() {
        FontMetrics fm = getFontMetrics(getFont());
        int width = Math.min(300, fm.stringWidth(text) + 30);
        int height = fm.getHeight() + 20;
        return new Dimension(width + 20, height);
    }
}
