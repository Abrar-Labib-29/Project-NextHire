package application;

import java.io.*;
import java.net.*;
import java.sql.*;
import java.util.*;
import java.util.concurrent.*;

public class ChatServer {
    private static final int PORT = 8080;
    private static final Map<String, PrintWriter> clients = new ConcurrentHashMap<>();
    private static final Database database = new Database();
    
    public static void main(String[] args) {
        System.out.println("Chat Server starting on port " + PORT);
        startServer();
    }
    
    public static void startServer() {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Chat Server is running on port " + PORT);
            
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected: " + clientSocket.getInetAddress());
                
                // Handle each client in a separate thread
                ClientHandler clientHandler = new ClientHandler(clientSocket);
                new Thread(clientHandler).start();
            }
        } catch (IOException e) {
            System.err.println("Server error: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static class ClientHandler implements Runnable {
        private Socket clientSocket;
        private PrintWriter out;
        private BufferedReader in;
        private String username;
        
        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
        }
        
        @Override
        public void run() {
            try {
                out = new PrintWriter(clientSocket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                
                // First message should be the username
                username = in.readLine();
                if (username != null && !username.isEmpty()) {
                    clients.put(username, out);
                    System.out.println(username + " joined the chat");
                    
                    // Send previous messages to the new client
                    sendPreviousMessages(username);
                    
                    // Broadcast user joined message
                    broadcastMessage("SYSTEM: " + username + " joined the chat", username);
                    
                    // Handle incoming messages
                    String inputLine;
                    while ((inputLine = in.readLine()) != null) {
                        if (inputLine.equals("QUIT")) {
                            break;
                        }
                        
                        // Save message to database
                        saveMessageToDatabase(username, inputLine);
                        
                        // Broadcast message to all clients
                        broadcastMessage(username + ": " + inputLine, username);
                    }
                }
            } catch (IOException e) {
                System.err.println("Error handling client " + username + ": " + e.getMessage());
            } finally {
                try {
                    if (username != null) {
                        clients.remove(username);
                        broadcastMessage("SYSTEM: " + username + " left the chat", username);
                        System.out.println(username + " left the chat");
                    }
                    if (out != null) out.close();
                    if (in != null) in.close();
                    if (clientSocket != null) clientSocket.close();
                } catch (IOException e) {
                    System.err.println("Error closing client connection: " + e.getMessage());
                }
            }
        }
        
        private void saveMessageToDatabase(String sender, String message) {
            try (Connection conn = Database.getConnection()) {
                String sql = "INSERT INTO chat_messages (sender_username, message_text) VALUES (?, ?)";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, sender);
                pstmt.setString(2, message);
                pstmt.executeUpdate();
            } catch (SQLException e) {
                System.err.println("Error saving message to database: " + e.getMessage());
            }
        }
        
        private void sendPreviousMessages(String username) {
            try (Connection conn = Database.getConnection()) {
                String sql = "SELECT sender_username, message_text, timestamp FROM chat_messages ORDER BY timestamp DESC LIMIT 50";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                ResultSet rs = pstmt.executeQuery();
                
                List<String> messages = new ArrayList<>();
                while (rs.next()) {
                    String sender = rs.getString("sender_username");
                    String message = rs.getString("message_text");
                    String timestamp = rs.getString("timestamp");
                    messages.add(0, sender + ": " + message + " [" + timestamp + "]");
                }
                
                // Send previous messages to the client
                for (String message : messages) {
                    out.println("HISTORY: " + message);
                }
            } catch (SQLException e) {
                System.err.println("Error loading previous messages: " + e.getMessage());
            }
        }
    }
    
    private static void broadcastMessage(String message, String sender) {
        System.out.println("Broadcasting: " + message);
        for (Map.Entry<String, PrintWriter> client : clients.entrySet()) {
            if (!client.getKey().equals(sender)) {
                client.getValue().println("MESSAGE: " + message);
            }
        }
    }
    
    public static boolean isUserEligibleForCourse(String username) {
        try (Connection conn = Database.getConnection()) {
            String sql = "SELECT COUNT(*) FROM course_eligible_users WHERE username = ? AND is_active = TRUE";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error checking user eligibility: " + e.getMessage());
        }
        return false;
    }
    
    public static void addUserToCourseEligible(String username, String jobTitle, String company) {
        try (Connection conn = Database.getConnection()) {
            String sql = "INSERT INTO course_eligible_users (username, job_title, company) VALUES (?, ?, ?) " +
                        "ON DUPLICATE KEY UPDATE is_active = TRUE, job_title = ?, company = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            pstmt.setString(2, jobTitle);
            pstmt.setString(3, company);
            pstmt.setString(4, jobTitle);
            pstmt.setString(5, company);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error adding user to course eligible: " + e.getMessage());
        }
    }
} 