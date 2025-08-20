package com.pahana.edu.billing.model;

import java.time.LocalDateTime;

public class Item {
    private int id;                   // PK
    private String name;              // unique in DB schema
    private String description;       // optional
    private double price;             // current price
    private boolean active = true;    // maps to is_active TINYINT(1)
    private LocalDateTime createdAt;  // set by DB

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
