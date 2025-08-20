package com.pahana.edu.billing.controller;

import com.pahana.edu.billing.dao.CustomerDAO;
import com.pahana.edu.billing.dao.ItemDAO;
import com.pahana.edu.billing.model.Customer;
import com.pahana.edu.billing.model.Item;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;

public class SearchServlet extends HttpServlet {

    private CustomerDAO customerDAO;
    private ItemDAO itemDAO;

    @Override
    public void init() {
        customerDAO = new CustomerDAO();
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

    // GET /search?type=customers&q=...  or  /search?type=items&q=...
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        if (!ensureLoggedIn(req, resp)) return;

        String type = req.getParameter("type");
        String q = req.getParameter("q");
        if (type == null) type = "customers";
        if (q == null) q = "";

        switch (type) {
            case "items": {
                List<Item> items = itemDAO.search(q);
                req.setAttribute("items", items);
                req.setAttribute("query", q);
                req.getRequestDispatcher("/WEB-INF/views/items/viewItems.jsp").forward(req, resp);
                break;
            }
            case "customers":
            default: {
                List<Customer> customers = customerDAO.search(q);
                req.setAttribute("customers", customers);
                req.setAttribute("query", q);
                req.getRequestDispatcher("/WEB-INF/views/customers/viewCustomers.jsp").forward(req, resp);
            }
        }
    }
}
