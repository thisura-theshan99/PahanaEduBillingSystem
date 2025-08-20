package com.pahana.edu.billing.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    // ✅ Update USER/PASSWORD to match your MySQL credentials
    private static final String URL =
            "jdbc:mysql://localhost:3306/pahanaedu_billing?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "root";

    // Single shared connection (simple for assignments)
    private static Connection connection;

    private DBConnection() { }

    public static synchronized Connection getConnection() throws SQLException {
        try {
            if (connection == null || connection.isClosed()) {
                // Explicit load (modern JDBC auto-loads, but this is safe)
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                // Optional: set sane defaults
                connection.setAutoCommit(true);
            }
            return connection;
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL Driver not found (com.mysql.cj.jdbc.Driver). Add mysql-connector-java to pom.xml.", e);
        }
    }

    public static synchronized void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException ignored) { }
            finally {
                connection = null;
            }
        }
    }
}
