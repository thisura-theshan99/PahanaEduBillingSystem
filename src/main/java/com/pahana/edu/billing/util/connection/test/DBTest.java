package com.pahana.edu.billing.util.connection.test;

import com.pahana.edu.billing.util.DBConnection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class DBTest {
    public static void main(String[] args) {
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT NOW() AS db_time")) {

            if (rs.next()) {
                System.out.println("✅ Database connected. Current time: " + rs.getString("db_time"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
