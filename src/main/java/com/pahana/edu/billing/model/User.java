package com.pahana.edu.billing.model;

import java.time.LocalDateTime;

public class User {
    private int id;                   // PK
    private String username;
    // We do NOT keep password or hash in memory after validation
    private String role;              // "ADMIN" or "STAFF"
    private boolean active = true;    // maps to is_active
    private LocalDateTime createdAt;  // set by DB

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    // Helpers
    public boolean isAdmin() { return "ADMIN".equalsIgnoreCase(role); }
    public boolean isStaff() { return "STAFF".equalsIgnoreCase(role); }
}
