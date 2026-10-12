import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
public class ChatGUI extends JFrame {
    private final JTextArea chatArea = new JTextArea();
    private final JTextField inputField = new JTextField();
    private final Chatbot bot = new Chatbot();

    public ChatGUI() {
        super("CodeBot - Java AI Chatbot");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(520, 480);
        setLocationRelativeTo(null);

        chatArea.setEditable(false);
        chatArea.setLineWrap(true);
        chatArea.setWrapStyleWord(true);
        chatArea.setFont(new Font("SansSerif", Font.PLAIN, 14));
        chatArea.setMargin(new Insets(8, 8, 8, 8));

        JButton sendButton = new JButton("Send");
        JPanel bottom = new JPanel(new BorderLayout(6, 6));
        bottom.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));
        bottom.add(inputField, BorderLayout.CENTER);
        bottom.add(sendButton, BorderLayout.EAST);

        add(new JScrollPane(chatArea), BorderLayout.CENTER);
        add(bottom, BorderLayout.SOUTH);

        ActionListener send = e -> sendMessage();
        inputField.addActionListener(send);
        sendButton.addActionListener(send);

        appendMessage("CodeBot", bot.greeting());
    }

    private void sendMessage() {
        String text = inputField.getText().trim();
        if (text.isEmpty()) return;
        appendMessage("You", text);
        inputField.setText("");
        appendMessage("CodeBot", bot.respond(text));
        if (bot.isFinished()) {
            Timer t = new Timer(900, e -> System.exit(0));
            t.setRepeats(false);
            t.start();
        }
    }

    private void appendMessage(String sender, String message) {
        chatArea.append(sender + ": " + message + "\n\n");
        chatArea.setCaretPosition(chatArea.getDocument().getLength());
    }

    public static void run() {
        SwingUtilities.invokeLater(() -> new ChatGUI().setVisible(true));
    }
}
