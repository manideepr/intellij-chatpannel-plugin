package org.chatbot;
import javax.swing.*;
import java.awt.*;

public class ChatBotPanel extends JPanel {
    private JTextArea chatArea = new JTextArea(20, 50);
    private JTextField inputField = new JTextField();
    private JButton sendButton = new JButton("Ask");

    public ChatBotPanel() {
        setLayout(new BorderLayout());
        JScrollPane scroll = new JScrollPane(chatArea);
        chatArea.setEditable(false);
        add(scroll, BorderLayout.CENTER);

        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.add(inputField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);
        add(inputPanel, BorderLayout.SOUTH);

        sendButton.addActionListener(evt -> {
            String question = inputField.getText();
            inputField.setText("");
            chatArea.append("You: " + question + "\n");
            String answer = ChatBotAPI.ask(question);
            chatArea.append("Bot: " + answer + "\n\n");
        });
    }
}
