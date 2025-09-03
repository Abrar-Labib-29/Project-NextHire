package application;

import java.io.*;
import java.net.*;
import java.util.function.Consumer;

public class ChatClient {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private String username;
    private Consumer<String> messageHandler;
    private boolean isConnected = false;
    
    public ChatClient(String username, Consumer<String> messageHandler) {
        this.username = username;
        this.messageHandler = messageHandler;
    }
    
    public boolean connect() {
        try {
            socket = new Socket("localhost", 8080);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            
            // Send username to server
            out.println(username);
            
            isConnected = true;
            
            // Start listening for messages in a separate thread
            new Thread(this::listenForMessages).start();
            
            return true;
        } catch (IOException e) {
            System.err.println("Error connecting to chat server: " + e.getMessage());
            return false;
        }
    }
    
    public void sendMessage(String message) {
        if (isConnected && out != null) {
            out.println(message);
        }
    }
    
    public void disconnect() {
        try {
            if (out != null) {
                out.println("QUIT");
            }
            isConnected = false;
            
            if (out != null) out.close();
            if (in != null) in.close();
            if (socket != null) socket.close();
        } catch (IOException e) {
            System.err.println("Error disconnecting: " + e.getMessage());
        }
    }
    
    private void listenForMessages() {
        try {
            String message;
            while (isConnected && (message = in.readLine()) != null) {
                final String finalMessage = message;
                if (messageHandler != null) {
                    // Handle message on JavaFX Application Thread
                    javafx.application.Platform.runLater(() -> messageHandler.accept(finalMessage));
                }
            }
        } catch (IOException e) {
            if (isConnected) {
                System.err.println("Error reading from server: " + e.getMessage());
                isConnected = false;
            }
        }
    }
    
    public boolean isConnected() {
        return isConnected;
    }
} 