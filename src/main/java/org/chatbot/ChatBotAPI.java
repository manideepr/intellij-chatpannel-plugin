package org.chatbot;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class ChatBotAPI {
    public static String ask(String question) {
        try {
            URL url = new URL("http://localhost:8000/chat"); // Point to your FastAPI endpoint!
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/json");

            String jsonBody = "{\"question\": \"" + question.replace("\"", "\\\"") + "\"}";
            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
            writer.write(jsonBody);
            writer.flush();
            writer.close();

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                return response.toString();
            } else {
                return "Error from server: " + responseCode;
            }
        } catch (Exception e) {
            return "Error contacting chatbot: " + e.getMessage();
        }
    }
}
