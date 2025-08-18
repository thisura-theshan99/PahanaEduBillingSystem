package com.pahana.edu.billing.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static final String URL = "jdbc:mysql://localhost:3306/pahanaedu_billing?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static final String USER = "pahana_user";   // your MySQL user
    private static final String PASSWORD = "ChangeMe_123!"; // your MySQL password

    private static Connection connection = null;

    // Private constructor (Singleton pattern)
    private DBConnection() { }

    // Return a single shared connection
    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                // Load driver (modern JDBC auto-loads, but explicit is safer)
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
            } catch (ClassNotFoundException e) {
                throw new SQLException("MySQL Driver not found!", e);
            }
        }
        return connection;
    }

    // Utility method to close connection
    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                connection = null;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
