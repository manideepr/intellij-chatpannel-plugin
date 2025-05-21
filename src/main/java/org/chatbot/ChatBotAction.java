package org.chatbot;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.ui.DialogWrapper;
import org.chatbot.ChatBotAPI;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;

public class ChatBotAction extends AnAction {
    @Override
    public void actionPerformed(AnActionEvent e) {
        ChatBotDialog dialog = new ChatBotDialog();
        dialog.show();
    }
}

class ChatBotDialog extends DialogWrapper {
    private JTextArea chatArea = new JTextArea(20, 50);
    private JTextField inputField = new JTextField();
    private JButton sendButton = new JButton("Ask");

    public ChatBotDialog() {
        super(true); // use current window as parent
        init();
        setTitle("Java RAG ChatBot");

        sendButton.addActionListener(evt -> {
            String question = inputField.getText();
            inputField.setText("");
            chatArea.append("You: " + question + "\n");
            String answer = ChatBotAPI.ask(question);
            chatArea.append("Bot: " + answer + "\n\n");
        });
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JScrollPane scroll = new JScrollPane(chatArea);
        chatArea.setEditable(false);
        panel.add(scroll, BorderLayout.CENTER);

        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.add(inputField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);
        panel.add(inputPanel, BorderLayout.SOUTH);

        return panel;
    }
}
