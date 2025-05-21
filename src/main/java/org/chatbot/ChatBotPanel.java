package org.chatbot;

import javax.swing.*;
import java.awt.*;

public class ChatBotPanel extends JPanel {
    private final JEditorPane chatArea = new JEditorPane("text/html", "<html><body></body></html>");
    private final JTextField inputField = new JTextField();
    private final JButton sendButton = new JButton("Ask");

    private StringBuilder htmlContent = new StringBuilder();

    public ChatBotPanel() {
        setLayout(new BorderLayout());
        chatArea.setEditable(false);
        chatArea.setContentType("text/html");

        JScrollPane scroll = new JScrollPane(chatArea);
        add(scroll, BorderLayout.CENTER);

        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.add(inputField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);
        add(inputPanel, BorderLayout.SOUTH);

        // Initial empty chat content
        htmlContent.append("<html><body>");

        sendButton.addActionListener(evt -> sendUserQuestion());
        inputField.addActionListener(evt -> sendUserQuestion());
    }

    private void sendUserQuestion() {
        String question = inputField.getText().trim();
        if (question.isEmpty()) return;
        inputField.setText("");
        appendMessage("You: " + escapeHtml(question), false);

        // Simulate an async backend call (for testing, replace this with ChatBotAPI.ask)
        new SwingWorker<String, Void>() {
            @Override
            protected String doInBackground() {

                //return "Here is a code sample:\n```java\npublic class Hello { public static void main(String[] args) { System.out.println(\"Hi\"); } }\n```";
                 return ChatBotAPI.ask(question); // <-- use this in real version
            }
            @Override
            protected void done() {
                try {
                    String answer = get();
                    String htmlAnswer = markdownToHtml(answer);
                    appendMessage("Bot: " + htmlAnswer, true);
                } catch (Exception ex) {
                    appendMessage("Bot: (Error: " + ex.getMessage() + ")", true);
                }
            }
        }.execute();
    }

    private void appendMessage(String message, boolean isBot) {
        String color = "white";
        htmlContent.append(
                "<div style='margin-bottom:8px;"
                        + "color:" + color + ";"
                        + "word-break:break-word;"
                        + "white-space:normal;'>"
                        + message
                        + "</div>"
        );        chatArea.setText(htmlContent.toString() + "</body></html>");
        chatArea.setCaretPosition(chatArea.getDocument().getLength());
    }

    private String markdownToHtml(String markdown) {
        String safe = escapeHtml(markdown);
        safe = safe.replaceAll(
                "```java([\\s\\S]*?)```",
                "<pre style='background:#f5f5f5;border:1px solid #ddd;padding:8px;white-space:pre-wrap;word-break:break-all;'><code style='color:black;'>$1</code></pre>"
        );
        safe = safe.replaceAll(
                "```([\\s\\S]*?)```",
                "<pre style='background:#f5f5f5;border:1px solid #ddd;padding:8px;white-space:pre-wrap;word-break:break-all;'><code style='color:black;'>$1</code></pre>"
        );
        safe = safe.replaceAll("`([^`]+)`", "<code>$1</code>");
        return safe.replace("\n", "<br>");
    }


    private String escapeHtml(String text) {
        if (text == null) return "";
        return text.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;");
    }
}
