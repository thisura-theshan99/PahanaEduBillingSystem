package com.pahana.edu.billing.controller;

import com.pahana.edu.billing.dao.BillDAO;
import com.pahana.edu.billing.dao.ItemDAO;
import com.pahana.edu.billing.model.Bill;
import com.pahana.edu.billing.model.BillItem;
import com.pahana.edu.billing.model.Item;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.*;

public class BillServlet extends HttpServlet {

    private BillDAO billDAO;
    private ItemDAO itemDAO;

    @Override
    public void init() {
        billDAO = new BillDAO();
        itemDAO = new ItemDAO();
    }

    private boolean ensureLoggedIn(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return false;
        }
        return true;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        if (!ensureLoggedIn(req, resp)) return;

        String action = req.getParameter("action");
        if (action == null) action = "new";

        switch (action) {
            case "view": {
                // list bills
                req.setAttribute("bills", billDAO.findAll());
                req.getRequestDispatcher("/WEB-INF/views/billing/viewBills.jsp").forward(req, resp);
                break;
            }
            case "print": {
                int billId = Integer.parseInt(req.getParameter("id"));
                Bill bill = billDAO.findById(billId);
                req.setAttribute("bill", bill);
                req.setAttribute("billItems", billDAO.findItemsByBillId(billId));
                req.getRequestDispatcher("/WEB-INF/views/billing/printBill.jsp").forward(req, resp);
                break;
            }
            case "new":
            default:
                // Show form to generate a bill (needs list of items)
                req.setAttribute("items", itemDAO.findAll());
                req.getRequestDispatcher("/WEB-INF/views/billing/generateBill.jsp").forward(req, resp);
        }
    }

    // Create a bill
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        if (!ensureLoggedIn(req, resp)) return;

        String action = req.getParameter("action");
        if (!"create".equals(action)) {
            resp.sendRedirect(req.getContextPath() + "/bills?action=new");
            return;
        }

        try {
            int customerId = Integer.parseInt(req.getParameter("customerId"));
            double discountPercent = 0.0;
            try {
                discountPercent = Double.parseDouble(req.getParameter("discountPercent"));
            } catch (Exception ignore) {}

            String[] itemIds = req.getParameterValues("itemId");
            String[] quantities = req.getParameterValues("quantity");

            if (itemIds == null || quantities == null || itemIds.length == 0) {
                req.setAttribute("errorMessage", "Please select at least one item.");
                req.setAttribute("items", itemDAO.findAll());
                req.getRequestDispatcher("/WEB-INF/views/billing/generateBill.jsp").forward(req, resp);
                return;
            }

            List<BillItem> billItems = new ArrayList<>();
            double total = 0.0;

            for (int i = 0; i < itemIds.length; i++) {
                int itemId = Integer.parseInt(itemIds[i]);
                int qty = Integer.parseInt(quantities[i]);

                if (qty <= 0) continue;

                Item it = itemDAO.findById(itemId);
                if (it == null) continue;

                double unitPrice = it.getPrice();
                double lineTotal = unitPrice * qty;

                BillItem bi = new BillItem();
                bi.setItemId(itemId);
                bi.setQuantity(qty);
                bi.setUnitPrice(unitPrice);
                bi.setLineTotal(lineTotal);

                billItems.add(bi);
                total += lineTotal;
            }

            double net = Math.round((total - (total * (discountPercent / 100.0))) * 100.0) / 100.0;

            Bill bill = new Bill();
            bill.setCustomerId(customerId);
            bill.setTotalAmount(total);
            bill.setDiscountPercent(discountPercent);
            bill.setNetAmount(net);

            // Persist (DAO should: insert bill header, then bill_items)
            int billId = billDAO.createBill(bill, billItems);

            // Forward to printable view
            req.setAttribute("bill", billDAO.findById(billId));
            req.setAttribute("billItems", billDAO.findItemsByBillId(billId));
            req.getRequestDispatcher("/WEB-INF/views/billing/printBill.jsp").forward(req, resp);

        } catch (Exception ex) {
            ex.printStackTrace();
            req.setAttribute("errorMessage", "Failed to create bill. Please try again.");
            req.setAttribute("items", itemDAO.findAll());
            req.getRequestDispatcher("/WEB-INF/views/billing/generateBill.jsp").forward(req, resp);
        }
    }
}
