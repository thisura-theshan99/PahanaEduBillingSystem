package com.pahana.edu.billing.dao;

import com.pahana.edu.billing.model.Customer;
import com.pahana.edu.billing.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAO {

    // ---- CREATE (alias kept for your current servlet calls) ----
    public boolean addCustomer(Customer c) { return create(c); }

    public boolean create(Customer c) {
        String sql = "INSERT INTO customers (name, phone, email, address) VALUES (?,?,?,?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, c.getName());
            ps.setString(2, c.getPhone());
            ps.setString(3, c.getEmail());
            ps.setString(4, c.getAddress());

            int rows = ps.executeUpdate();
            if (rows > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) c.setId(rs.getInt(1));
                }
                return true;
            }
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }

    // ===== Sorting helpers =====
    private String buildOrderBy(String sort, String dir) {
        // Whitelist columns
        String column = "id";
        if ("name".equalsIgnoreCase(sort)) column = "name";

        // Whitelist direction
        String direction = "ASC";
        if ("desc".equalsIgnoreCase(dir)) direction = "DESC";

        return " ORDER BY " + column + " " + direction + " ";
    }

    // ---- READ ALL (with sorting) ----
    public List<Customer> findAll(String sort, String dir) {
        List<Customer> list = new ArrayList<>();
        String sql = "SELECT id, name, phone, email, address, created_at FROM customers"
                + buildOrderBy(sort, dir);
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Customer c = new Customer();
                c.setId(rs.getInt("id"));
                c.setName(rs.getString("name"));
                c.setPhone(rs.getString("phone"));
                c.setEmail(rs.getString("email"));
                c.setAddress(rs.getString("address"));
                // If your model has createdAt, map it here (Date/LocalDateTime)
                // c.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                list.add(c);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    // ---- READ ALL (default: ID ASC) ----
    public List<Customer> findAll() {
        return findAll("id", "asc");
    }

    // ---- READ ONE ----
    public Customer findById(int id) {
        String sql = "SELECT id, name, phone, email, address, created_at FROM customers WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Customer c = new Customer();
                    c.setId(rs.getInt("id"));
                    c.setName(rs.getString("name"));
                    c.setPhone(rs.getString("phone"));
                    c.setEmail(rs.getString("email"));
                    c.setAddress(rs.getString("address"));
                    return c;
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
        return null;
    }

    // ---- UPDATE ----
    public boolean updateCustomer(Customer c) {
        String sql = "UPDATE customers SET name=?, phone=?, email=?, address=? WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, c.getName());
            ps.setString(2, c.getPhone());
            ps.setString(3, c.getEmail());
            ps.setString(4, c.getAddress());
            ps.setInt(5, c.getId());
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }

    // ---- DELETE ----
    public boolean deleteCustomer(int id) {
        String sql = "DELETE FROM customers WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }

    // ---- SEARCH (kept simple; ordered by newest by default) ----
    public List<Customer> search(String q) {
        List<Customer> list = new ArrayList<>();
        String like = "%" + (q == null ? "" : q.trim()) + "%";
        String sql = "SELECT id, name, phone, email, address FROM customers " +
                "WHERE name LIKE ? OR phone LIKE ? OR email LIKE ? ORDER BY id DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, like);
            ps.setString(2, like);
            ps.setString(3, like);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Customer c = new Customer();
                    c.setId(rs.getInt("id"));
                    c.setName(rs.getString("name"));
                    c.setPhone(rs.getString("phone"));
                    c.setEmail(rs.getString("email"));
                    c.setAddress(rs.getString("address"));
                    list.add(c);
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }
}
