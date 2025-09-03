package application;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.Modality;
import javafx.fxml.FXMLLoader;
import javafx.event.ActionEvent;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ChatController implements Initializable {
    
    @FXML private Button backButton;
    @FXML private Label chatTitleLabel;
    @FXML private Label statusLabel;
    @FXML private Label connectionStatusLabel;
    @FXML private ScrollPane chatScrollPane;
    @FXML private VBox chatContainer;
    @FXML private Button attachButton;
    @FXML private TextField messageTextField;
    @FXML private Button sendButton;
    
    private ChatClient chatClient;
    private String currentUsername;
    private static Stage chatStage;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupEventHandlers();
        setupMessageInput();
    }
    
    private void setupEventHandlers() {
        backButton.setOnAction(this::handleBackButton);
        sendButton.setOnAction(this::handleSendMessage);
        attachButton.setOnAction(this::handleAttachFile);
        
        // Send message on Enter key
        messageTextField.setOnAction(this::handleSendMessage);
    }
    
    private void setupMessageInput() {
        // Enable/disable send button based on text field content
        messageTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            sendButton.setDisable(newValue == null || newValue.trim().isEmpty());
        });
        sendButton.setDisable(true);
    }
    
    public void setUsername(String username) {
        this.currentUsername = username;
        connectToChat();
    }
    
    private void connectToChat() {
        if (currentUsername == null || currentUsername.isEmpty()) {
            showAlert("Error", "Username not set");
            return;
        }
        
        // Check if user is eligible for course chat
        if (!ChatServer.isUserEligibleForCourse(currentUsername)) {
            showAlert("Access Denied", "You are not eligible for the course discussion group.");
            closeChat();
            return;
        }
        
        chatClient = new ChatClient(currentUsername, this::handleIncomingMessage);
        
        if (chatClient.connect()) {
            connectionStatusLabel.setText("Connected");
            connectionStatusLabel.setStyle("-fx-text-fill: #4CAF50;");
        } else {
            connectionStatusLabel.setText("Connection Failed");
            connectionStatusLabel.setStyle("-fx-text-fill: #f44336;");
            showAlert("Connection Error", "Failed to connect to chat server. Please try again.");
        }
    }
    
    private void handleIncomingMessage(String message) {
        if (message.startsWith("MESSAGE: ")) {
            String actualMessage = message.substring(9);
            addMessageToChat(actualMessage, false);
        } else if (message.startsWith("HISTORY: ")) {
            String historyMessage = message.substring(9);
            addMessageToChat(historyMessage, false);
        } else if (message.startsWith("SYSTEM: ")) {
            String systemMessage = message.substring(8);
            addSystemMessage(systemMessage);
        }
    }
    
    private void addMessageToChat(String message, boolean isOwnMessage) {
        HBox messageBox = new HBox();
        messageBox.setAlignment(isOwnMessage ? Pos.CENTER_RIGHT : Pos.CENTER_LEFT);
        messageBox.setPadding(new Insets(5, 10, 5, 10));
        
        VBox messageContent = new VBox();
        messageContent.setMaxWidth(300);
        messageContent.setStyle(
            "-fx-background-color: " + (isOwnMessage ? "#a6275c" : "#f0f0f0") + ";" +
            "-fx-background-radius: 15;" +
            "-fx-padding: 10;"
        );
        
        Label messageLabel = new Label(message);
        messageLabel.setWrapText(true);
        messageLabel.setStyle(
            "-fx-text-fill: " + (isOwnMessage ? "white" : "black") + ";" +
            "-fx-font-size: 14px;"
        );
        
        Label timeLabel = new Label(LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm")));
        timeLabel.setStyle(
            "-fx-text-fill: " + (isOwnMessage ? "#e0e0e0" : "#666666") + ";" +
            "-fx-font-size: 10px;"
        );
        
        messageContent.getChildren().addAll(messageLabel, timeLabel);
        messageBox.getChildren().add(messageContent);
        
        chatContainer.getChildren().add(messageBox);
        
        // Scroll to bottom
        chatScrollPane.setVvalue(1.0);
    }
    
    private void addSystemMessage(String message) {
        HBox messageBox = new HBox();
        messageBox.setAlignment(Pos.CENTER);
        messageBox.setPadding(new Insets(5, 10, 5, 10));
        
        Label systemLabel = new Label(message);
        systemLabel.setStyle(
            "-fx-text-fill: #666666;" +
            "-fx-font-size: 12px;" +
            "-fx-font-style: italic;"
        );
        
        messageBox.getChildren().add(systemLabel);
        chatContainer.getChildren().add(messageBox);
        
        // Scroll to bottom
        chatScrollPane.setVvalue(1.0);
    }
    
    private void handleSendMessage(ActionEvent event) {
        String message = messageTextField.getText().trim();
        if (!message.isEmpty() && chatClient != null && chatClient.isConnected()) {
            chatClient.sendMessage(message);
            addMessageToChat(currentUsername + ": " + message, true);
            messageTextField.clear();
        }
    }
    
    private void handleAttachFile(ActionEvent event) {
        //  Implement file attachment functionality
        showAlert("Info", "File attachment feature coming soon!");
    }
    
    private void handleBackButton(ActionEvent event) {
        closeChat();
    }
    
    private void closeChat() {
        if (chatClient != null) {
            chatClient.disconnect();
        }
        if (chatStage != null) {
            chatStage.close();
        }
    }
    
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
    
    public static void showChat(String username) {
        try {
            FXMLLoader loader = new FXMLLoader(ChatController.class.getResource("/Chat.fxml"));
            Parent root = loader.load();
            
            ChatController controller = loader.getController();
            controller.setUsername(username);
            
            chatStage = new Stage();
            chatStage.setTitle("Course Discussion Group");
            chatStage.setScene(new Scene(root));
            chatStage.setResizable(false);
            chatStage.initModality(Modality.APPLICATION_MODAL);
            
            // Handle window close event
            chatStage.setOnCloseRequest(event -> {
                controller.closeChat();
            });
            
            chatStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
} 