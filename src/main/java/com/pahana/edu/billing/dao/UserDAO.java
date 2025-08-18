package com.pahana.edu.billing.dao;

import com.pahana.edu.billing.model.User;
import com.pahana.edu.billing.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserDAO {

    /**
     * Validate user login by checking username + password in DB.
     * Returns a User object if valid, otherwise null.
     */
    public User validateUser(String username, String password) {
        User user = null;
        String sql = "SELECT id, username, role FROM users WHERE username=? AND password=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            stmt.setString(2, password);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    user = new User();
                    user.setId(rs.getInt("id"));
                    user.setUsername(rs.getString("username"));
                    user.setRole(rs.getString("role")); // "admin" or "staff"
                    // Notice: password is not set into the model (for security)
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }
}
