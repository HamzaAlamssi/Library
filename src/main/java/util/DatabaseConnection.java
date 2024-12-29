package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/LibraryManagementSystem";
    private static final String DB_USERNAME = "root";
    private static final String DB_PASSWORD = "0000";

    private static DatabaseConnection instance;
    private Connection connection;

    // Private constructor to prevent instantiation
    private DatabaseConnection() throws SQLException, ClassNotFoundException {
        // Load MySQL JDBC driver and establish connection
        Class.forName("com.mysql.cj.jdbc.Driver");
        this.connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
    }

    // Singleton instance retrieval
    public static DatabaseConnection getInstance() throws SQLException, ClassNotFoundException {
        if (instance == null || instance.connection.isClosed()) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    // Method to provide the connection
    public Connection getConnection() {
        return connection;
    }
}
