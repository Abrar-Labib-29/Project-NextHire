package application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {

    public static Connection getConnection() {
        Connection connection = null;
        try {
            // Load the MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            // Establish the connection
            String url = "jdbc:mysql://localhost:3306/next_hire";
            String user = "root";
            String password = ""; // XAMPP default password is empty
            
            connection = DriverManager.getConnection(url, user, password);
            
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            // In a real application, you'd want to handle this more gracefully
            // For now, we'll just print the stack trace
        }
        return connection;
    }
} 