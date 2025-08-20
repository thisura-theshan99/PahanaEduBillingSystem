package com.pahana.edu.billing.controller;

import com.pahana.edu.billing.dao.BillDAO;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Map;

public class ReportServlet extends HttpServlet {

    private BillDAO billDAO;

    @Override
    public void init() {
        billDAO = new BillDAO();
    }

    private boolean ensureLoggedIn(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return false;
        }
        return true;
    }

    // Reports by action: monthly, topCustomers, etc.
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        if (!ensureLoggedIn(req, resp)) return;

        String action = req.getParameter("action");
        if (action == null) action = "monthly";

        switch (action) {
            case "monthly": {
                String yearParam = req.getParameter("year");
                String monthParam = req.getParameter("month"); // 1..12
                LocalDate now = LocalDate.now();

                int year = (yearParam != null) ? Integer.parseInt(yearParam) : now.getYear();
                int month = (monthParam != null) ? Integer.parseInt(monthParam) : now.getMonthValue();

                Map<String, Object> summary = billDAO.getMonthlySummary(year, month);
                req.setAttribute("summary", summary);
                req.setAttribute("year", year);
                req.setAttribute("month", month);
                req.getRequestDispatcher("/WEB-INF/views/reports/reportMonthly.jsp").forward(req, resp);
                break;
            }

            case "topCustomers": {
                req.setAttribute("topCustomers", billDAO.getTopCustomers());
                req.getRequestDispatcher("/WEB-INF/views/reports/reportTopCustomers.jsp").forward(req, resp);
                break;
            }

            default:
                resp.sendRedirect(req.getContextPath() + "/reports?action=monthly");
        }
    }
}
