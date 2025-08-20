package com.pahana.edu.billing.dao;

import com.pahana.edu.billing.model.Item;
import com.pahana.edu.billing.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ItemDAO {

    // ---- CREATE (alias kept for your current servlet calls) ----
    public boolean addItem(Item it) { return create(it); }

    public boolean create(Item it) {
        String sql = "INSERT INTO items (name, description, price, is_active) VALUES (?,?,?,?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, it.getName());
            ps.setString(2, it.getDescription());
            ps.setDouble(3, it.getPrice());
            ps.setBoolean(4, it.isActive());

            int rows = ps.executeUpdate();
            if (rows > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) it.setId(rs.getInt(1));
                }
                return true;
            }
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }

    // ===== Sorting helpers =====
    private String buildOrderBy(String sort, String dir) {
        // whitelist columns
        String column = "id";
        if ("name".equalsIgnoreCase(sort)) column = "name";

        // whitelist direction
        String direction = "ASC";
        if ("desc".equalsIgnoreCase(dir)) direction = "DESC";

        return " ORDER BY " + column + " " + direction + " ";
    }

    // ---- READ ALL (with sorting) ----
    public List<Item> findAll(String sort, String dir) {
        List<Item> list = new ArrayList<>();
        String sql = "SELECT id, name, description, price, is_active, created_at FROM items"
                + buildOrderBy(sort, dir);
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Item it = new Item();
                it.setId(rs.getInt("id"));
                it.setName(rs.getString("name"));
                it.setDescription(rs.getString("description"));
                it.setPrice(rs.getDouble("price"));
                it.setActive(rs.getBoolean("is_active"));
                // it.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                list.add(it);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    // ---- READ ALL (default: ID ASC) ----
    public List<Item> findAll() {
        return findAll("id", "asc");
    }

    // ---- READ ONE ----
    public Item findById(int id) {
        String sql = "SELECT id, name, description, price, is_active, created_at FROM items WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Item it = new Item();
                    it.setId(rs.getInt("id"));
                    it.setName(rs.getString("name"));
                    it.setDescription(rs.getString("description"));
                    it.setPrice(rs.getDouble("price"));
                    it.setActive(rs.getBoolean("is_active"));
                    return it;
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
        return null;
    }

    // ---- UPDATE ----
    public boolean updateItem(Item it) {
        String sql = "UPDATE items SET name=?, description=?, price=?, is_active=? WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, it.getName());
            ps.setString(2, it.getDescription());
            ps.setDouble(3, it.getPrice());
            ps.setBoolean(4, it.isActive());
            ps.setInt(5, it.getId());
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }

    // ---- DELETE ----
    public boolean deleteItem(int id) {
        String sql = "DELETE FROM items WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }

    // ---- SEARCH ----
    public List<Item> search(String q) {
        List<Item> list = new ArrayList<>();
        String like = "%" + (q == null ? "" : q.trim()) + "%";
        String sql = "SELECT id, name, description, price, is_active FROM items " +
                "WHERE name LIKE ? OR description LIKE ? ORDER BY id DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, like);
            ps.setString(2, like);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Item it = new Item();
                    it.setId(rs.getInt("id"));
                    it.setName(rs.getString("name"));
                    it.setDescription(rs.getString("description"));
                    it.setPrice(rs.getDouble("price"));
                    it.setActive(rs.getBoolean("is_active"));
                    list.add(it);
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }
}
