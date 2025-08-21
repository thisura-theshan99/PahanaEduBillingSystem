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
        String insertBill = """
            INSERT INTO bills (customer_id, total_amount, discount_percent, net_amount, created_at)
            VALUES (?, 0.00, ?, 0.00, NOW())
        """;

        String upsertItemGenerated = """
            INSERT INTO bill_items (bill_id, item_id, quantity, unit_price)
            VALUES (?, ?, ?, ?)
            ON DUPLICATE KEY UPDATE
                quantity   = VALUES(quantity),
                unit_price = VALUES(unit_price)
        """;

        // For non-generated schemas, we include line_total explicitly
        String upsertItemManual = """
            INSERT INTO bill_items (bill_id, item_id, quantity, unit_price, line_total)
            VALUES (?, ?, ?, ?, (? * ?))
            ON DUPLICATE KEY UPDATE
                quantity   = VALUES(quantity),
                unit_price = VALUES(unit_price),
                line_total = VALUES(line_total)
        """;

        // Recalc that works for both schemas: SUM(COALESCE(line_total, quantity*unit_price))
        String recalc = """
            UPDATE bills b
            JOIN (
              SELECT bi.bill_id,
                     COALESCE(SUM(COALESCE(bi.line_total, bi.quantity * bi.unit_price)), 0.00) AS items_total
              FROM bill_items bi
              WHERE bi.bill_id = ?
              GROUP BY bi.bill_id
            ) x ON x.bill_id = b.id
            SET b.total_amount = x.items_total,
                b.net_amount   = ROUND(x.items_total - (x.items_total * b.discount_percent / 100.0), 2)
            WHERE b.id = ?
        """;

        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);

            // 0) detect if line_total is a STORED GENERATED column
            final boolean lineTotalIsGenerated = isLineTotalGenerated(conn);

            // 1) bill header shell
            int billId;
            try (PreparedStatement ps = conn.prepareStatement(insertBill, Statement.RETURN_GENERATED_KEYS)) {
                ps.setInt(1, bill.getCustomerId());
                ps.setBigDecimal(2, java.math.BigDecimal.valueOf(bill.getDiscountPercent()));
                ps.executeUpdate();
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (!rs.next()) throw new SQLException("No bill id generated");
                    billId = rs.getInt(1);
                }
            }

            // 2) items batch (UPSERT to tolerate duplicates)
            if (lineTotalIsGenerated) {
                try (PreparedStatement ps = conn.prepareStatement(upsertItemGenerated)) {
                    for (BillItem it : items) {
                        ps.setInt(1, billId);
                        ps.setInt(2, it.getItemId());
                        ps.setInt(3, it.getQuantity());
                        ps.setBigDecimal(4, java.math.BigDecimal.valueOf(it.getUnitPrice()));
                        ps.addBatch();
                    }
                    ps.executeBatch();
                }
            } else {
                try (PreparedStatement ps = conn.prepareStatement(upsertItemManual)) {
                    for (BillItem it : items) {
                        ps.setInt(1, billId);
                        ps.setInt(2, it.getItemId());
                        ps.setInt(3, it.getQuantity());
                        ps.setBigDecimal(4, java.math.BigDecimal.valueOf(it.getUnitPrice()));
                        // line_total = qty * unit_price (pass twice for (? * ?))
                        ps.setInt(5, it.getQuantity());
                        ps.setBigDecimal(6, java.math.BigDecimal.valueOf(it.getUnitPrice()));
                        ps.addBatch();
                    }
                    ps.executeBatch();
                }
            }

            // 3) recalc totals on DB
            try (PreparedStatement ps = conn.prepareStatement(recalc)) {
                ps.setInt(1, billId);
                ps.setInt(2, billId);
                ps.executeUpdate();
            }

            conn.commit();
            return billId;

        } catch (SQLException e) {
            System.err.println("Failed to create bill:");
            System.err.println("  SQLState    = " + e.getSQLState());
            System.err.println("  ErrorCode   = " + e.getErrorCode());
            System.err.println("  Message     = " + e.getMessage());
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException ignored) {
            }
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException ignored) {
            }
            return 0;
        } finally {
            try {
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (Exception ignored) {
            }
        }
    }

    /**
     * Detects whether bill_items.line_total is a STORED GENERATED column.
     */
    private boolean isLineTotalGenerated(Connection conn) {
        String sql = """
            SELECT EXTRA
            FROM INFORMATION_SCHEMA.COLUMNS
            WHERE TABLE_SCHEMA = DATABASE()
              AND TABLE_NAME = 'bill_items'
              AND COLUMN_NAME = 'line_total'
            LIMIT 1
        """;
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                String extra = rs.getString("EXTRA");
                return extra != null && extra.toUpperCase().contains("STORED GENERATED");
            }
        } catch (SQLException e) {
            System.err.println("Warning: couldn't detect if line_total is generated; assuming manual. SQLState="
                    + e.getSQLState() + " code=" + e.getErrorCode());
        }
        return false;
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
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // --- Reports: Top Customers (match JSP keys) ---
    // --- Reports: Top Customers (return both 'name' and 'customer_name') ---
    public List<Map<String, Object>> getTopCustomers() {
        String sql = """
        SELECT c.id AS customer_id,
               c.name AS customer_name,
               ROUND(SUM(b.net_amount), 2) AS total_net
        FROM bills b
        JOIN customers c ON c.id = b.customer_id
        GROUP BY c.id, c.name
        ORDER BY total_net DESC
        LIMIT 10
    """;

        List<Map<String, Object>> list = new ArrayList<>();
        try (Connection cx = DBConnection.getConnection();
             PreparedStatement ps = cx.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Map<String, Object> row = new java.util.LinkedHashMap<>();
                int cid = rs.getInt("customer_id");
                String cname = rs.getString("customer_name");
                double total = rs.getDouble("total_net");

                row.put("customer_id", cid);
                row.put("name", cname);           // for JSPs expecting 'name'
                row.put("customer_name", cname);           // for JSPs expecting 'customer_name'
                row.put("total_net", total);
                row.put("netSum", total);           // extra alias for older JSPs
                list.add(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list; // empty list if none
    }


    // --- Reports: Monthly summary (compatible keys) ---
    // --- Reports: Monthly summary (billsCount, totalAmount, netAmount, avgTicket) ---
    public Map<String, Object> getMonthlySummary(int year, int month) {
        String sql = """
        SELECT
          COUNT(*) AS bill_count,
          ROUND(COALESCE(SUM(total_amount), 0.00), 2) AS total_gross,
          ROUND(COALESCE(SUM(net_amount),   0.00), 2) AS total_net
        FROM bills
        WHERE YEAR(created_at)=? AND MONTH(created_at)=?
    """;

        Map<String, Object> out = new java.util.LinkedHashMap<>();

        try (Connection cx = DBConnection.getConnection();
             PreparedStatement ps = cx.prepareStatement(sql)) {

            ps.setInt(1, year);
            ps.setInt(2, month);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int billCount = rs.getInt("bill_count");
                    double totalGross = rs.getDouble("total_gross");
                    double totalNet = rs.getDouble("total_net");
                    double avgTicket = billCount > 0 ? Math.round((totalNet / billCount) * 100.0) / 100.0 : 0.0;

                    // Keys many JSPs expect
                    out.put("billsCount", billCount);
                    out.put("totalAmount", totalGross);
                    out.put("netAmount", totalNet);
                    out.put("avgTicket", avgTicket);

                    // Also provide snake_case aliases (in case other JSPs use these)
                    out.put("bill_count", billCount);
                    out.put("total_gross", totalGross);
                    out.put("total_net", totalNet);
                    out.put("avg_ticket", avgTicket);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return out;
    }
}
