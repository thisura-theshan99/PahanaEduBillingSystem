package com.pahana.edu.billing.dao;

import com.pahana.edu.billing.model.User;
import com.pahana.edu.billing.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserDAO {

    /**
     * Validate user login by checking username + password in DB.
     * Uses SHA2 hashing to match stored password_hash.
     * Returns a User object if valid, otherwise null.
     */
    public User validateUser(String username, String password) {
        User user = null;
        // ✅ Correct query for password_hash column
        String sql = "SELECT id, username, role FROM users WHERE username=? AND password_hash=SHA2(?,256)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            System.out.println("Trying login with: " + username);
            System.out.println("SQL executed: " + sql);

            stmt.setString(1, username);
            stmt.setString(2, password);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    user = new User();
                    user.setId(rs.getInt("id"));
                    user.setUsername(rs.getString("username"));
                    user.setRole(rs.getString("role")); // ADMIN or STAFF
                    System.out.println("✅ Login success for: " + user.getUsername() + " (Role: " + user.getRole() + ")");
                } else {
                    System.out.println("❌ No matching user found!");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }
}
