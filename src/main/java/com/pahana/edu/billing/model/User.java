package com.pahana.edu.billing.model;

public class User {
    private int id;
    private String username;
    private String password; // optional to keep; you can avoid storing it in session
    private String role;     // "admin" or "staff"

    // Getters / Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    // Convenience helpers
    public boolean isAdmin() { return "admin".equalsIgnoreCase(role); }
    public boolean isStaff() { return "staff".equalsIgnoreCase(role); }
}
