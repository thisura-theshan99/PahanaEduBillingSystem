package com.pahana.edu.billing.dao;

import com.pahana.edu.billing.model.Bill;
import com.pahana.edu.billing.model.BillItem;
import com.pahana.edu.billing.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BillDAO {

    // Insert bill header + items in one transaction
    public int createBill(Bill bill, List<BillItem> items) {
        String insertBill = "INSERT INTO bills (customer_id, total_amount, discount_percent, net_amount, created_at) " +
                "VALUES (?, ?, ?, ?, NOW())";
        String insertItem = "INSERT INTO bill_items (bill_id, item_id, quantity, unit_price, line_total) " +
                "VALUES (?, ?, ?, ?, ?)";

        Connection conn = null;
        PreparedStatement psBill = null;
        PreparedStatement psItem = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);

            // Insert Bill Header
            psBill = conn.prepareStatement(insertBill, Statement.RETURN_GENERATED_KEYS);
            psBill.setInt(1, bill.getCustomerId());
            psBill.setDouble(2, bill.getTotalAmount());
            psBill.setDouble(3, bill.getDiscountPercent());
            psBill.setDouble(4, bill.getNetAmount());
            psBill.executeUpdate();

            rs = psBill.getGeneratedKeys();
            int billId = 0;
            if (rs.next()) {
                billId = rs.getInt(1);
            } else {
                conn.rollback();
                return 0;
            }

            // Insert Bill Items
            psItem = conn.prepareStatement(insertItem);
            for (BillItem item : items) {
                psItem.setInt(1, billId);
                psItem.setInt(2, item.getItemId());
                psItem.setInt(3, item.getQuantity());
                psItem.setDouble(4, item.getUnitPrice());
                psItem.setDouble(5, item.getLineTotal());
                psItem.addBatch();
            }
            psItem.executeBatch();

            conn.commit();
            return billId;

        } catch (Exception e) {
            e.printStackTrace();
            try { if (conn != null) conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception ignored) {}
            try { if (psBill != null) psBill.close(); } catch (Exception ignored) {}
            try { if (psItem != null) psItem.close(); } catch (Exception ignored) {}
            try { if (conn != null) conn.setAutoCommit(true); conn.close(); } catch (Exception ignored) {}
        }
        return 0;
    }

    // --- READ ONE BILL HEADER ---
    public Bill findById(int id) {
        String sql = "SELECT id, customer_id, total_amount, discount_percent, net_amount, created_at " +
                "FROM bills WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Bill b = new Bill();
                    b.setId(rs.getInt("id"));
                    b.setCustomerId(rs.getInt("customer_id"));
                    b.setTotalAmount(rs.getDouble("total_amount"));
                    b.setDiscountPercent(rs.getDouble("discount_percent"));
                    b.setNetAmount(rs.getDouble("net_amount"));
                    b.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                    return b;
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
        return null;
    }

    // --- READ ITEMS FOR BILL ---
    public List<BillItem> findItemsByBillId(int billId) {
        List<BillItem> list = new ArrayList<>();
        String sql = "SELECT id, bill_id, item_id, quantity, unit_price, line_total " +
                "FROM bill_items WHERE bill_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, billId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    BillItem bi = new BillItem();
                    bi.setId(rs.getInt("id"));
                    bi.setBillId(rs.getInt("bill_id"));
                    bi.setItemId(rs.getInt("item_id"));
                    bi.setQuantity(rs.getInt("quantity"));
                    bi.setUnitPrice(rs.getDouble("unit_price"));
                    bi.setLineTotal(rs.getDouble("line_total"));
                    list.add(bi);
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    // --- READ ALL BILLS ---
    public List<Bill> findAll() {
        List<Bill> list = new ArrayList<>();
        String sql = "SELECT id, customer_id, total_amount, discount_percent, net_amount, created_at " +
                "FROM bills ORDER BY id DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Bill b = new Bill();
                b.setId(rs.getInt("id"));
                b.setCustomerId(rs.getInt("customer_id"));
                b.setTotalAmount(rs.getDouble("total_amount"));
                b.setDiscountPercent(rs.getDouble("discount_percent"));
                b.setNetAmount(rs.getDouble("net_amount"));
                b.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                list.add(b);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    // --- Reports (later) ---
    public Map<String, Object> getMonthlySummary(int year, int month) { return null; }
    public List<Map<String, Object>> getTopCustomers() { return null; }
}
