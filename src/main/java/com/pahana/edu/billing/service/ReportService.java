package com.pahana.edu.billing.service;

import com.pahana.edu.billing.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.*;

public class ReportService {

    /**
     * Monthly summary for a given year & month.
     * Returns keys: totalBills, grossTotal, netTotal, avgTicket
     */
    public Map<String, Object> getMonthlySummary(int year, int month) {
        Map<String, Object> out = new HashMap<>();
        out.put("totalBills", 0);
        out.put("grossTotal", 0.0);
        out.put("netTotal", 0.0);
        out.put("avgTicket", 0.0);

        String sql =
                "SELECT " +
                        " COUNT(*) AS total_bills, " +
                        " IFNULL(SUM(b.total_amount), 0) AS gross_total, " +
                        " IFNULL(SUM(b.net_amount), 0) AS net_total " +
                        "FROM bills b " +
                        "WHERE YEAR(b.created_at) = ? AND MONTH(b.created_at) = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, year);
            ps.setInt(2, month);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int totalBills = rs.getInt("total_bills");
                    double gross   = rs.getDouble("gross_total");
                    double net     = rs.getDouble("net_total");
                    double avg     = (totalBills > 0) ? (net / totalBills) : 0.0;

                    out.put("totalBills", totalBills);
                    out.put("grossTotal", round2(gross));
                    out.put("netTotal", round2(net));
                    out.put("avgTicket", round2(avg));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return out;
    }

    /**
     * Top customers by NET amount (descending).
     * Each map has: customer_id, customer_name, total_net
     */
    public List<Map<String, Object>> getTopCustomers(int limit) {
        if (limit <= 0) limit = 10;

        String sql =
                "SELECT c.id AS customer_id, c.name AS customer_name, " +
                        " IFNULL(SUM(b.net_amount), 0) AS total_net " +
                        "FROM bills b " +
                        "JOIN customers c ON c.id = b.customer_id " +
                        "GROUP BY c.id, c.name " +
                        "ORDER BY total_net DESC " +
                        "LIMIT ?";

        List<Map<String, Object>> list = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, limit);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> row = new LinkedHashMap<>();
                    row.put("customer_id",   rs.getInt("customer_id"));
                    row.put("customer_name", rs.getString("customer_name"));
                    row.put("total_net",     round2(rs.getDouble("total_net")));
                    list.add(row);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Revenue by day for a month. Each map has: day (1..31), gross, net
     */
    public List<Map<String, Object>> getRevenueByDay(int year, int month) {
        String sql =
                "SELECT DAY(created_at) AS d, " +
                        " IFNULL(SUM(total_amount), 0) AS gross, " +
                        " IFNULL(SUM(net_amount), 0) AS net " +
                        "FROM bills " +
                        "WHERE YEAR(created_at) = ? AND MONTH(created_at) = ? " +
                        "GROUP BY DAY(created_at) " +
                        "ORDER BY d";

        List<Map<String, Object>> rows = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, year);
            ps.setInt(2, month);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> row = new LinkedHashMap<>();
                    row.put("day",   rs.getInt("d"));
                    row.put("gross", round2(rs.getDouble("gross")));
                    row.put("net",   round2(rs.getDouble("net")));
                    rows.add(row);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rows;
    }

    /** Helper to round to 2 decimals */
    private static double round2(double v) {
        return Math.round(v * 100.0) / 100.0;
    }

    /** Convenience for default month (now) */
    public Map<String, Object> getThisMonthSummary() {
        LocalDate now = LocalDate.now();
        return getMonthlySummary(now.getYear(), now.getMonthValue());
    }

    public List<Map<String, Object>> getThisMonthRevenueByDay() {
        LocalDate now = LocalDate.now();
        return getRevenueByDay(now.getYear(), now.getMonthValue());
    }
}
