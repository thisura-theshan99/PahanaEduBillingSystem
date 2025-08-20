package com.pahana.edu.billing.controller;

import com.pahana.edu.billing.dao.CustomerDAO;
import com.pahana.edu.billing.model.Customer;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;

public class CustomerServlet extends HttpServlet {

    private CustomerDAO customerDAO;

    @Override
    public void init() {
        customerDAO = new CustomerDAO();
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
        if (action == null) action = "list";

        switch (action) {
            case "new": {
                req.getRequestDispatcher("/WEB-INF/views/customers/addCustomer.jsp")
                        .forward(req, resp);
                break;
            }

            case "edit": {
                try {
                    int id = Integer.parseInt(req.getParameter("id"));
                    Customer c = customerDAO.findById(id);
                    req.setAttribute("customer", c);
                    req.getRequestDispatcher("/WEB-INF/views/customers/editCustomer.jsp")
                            .forward(req, resp);
                } catch (Exception e) {
                    resp.sendRedirect(req.getContextPath() + "/customers?action=list&err=badId");
                }
                break;
            }

            case "delete": {
                try {
                    int id = Integer.parseInt(req.getParameter("id"));
                    customerDAO.deleteCustomer(id);
                    // redirect to list so it reloads from DB
                    resp.sendRedirect(req.getContextPath() + "/customers?action=list&deleted=1");
                } catch (Exception e) {
                    resp.sendRedirect(req.getContextPath() + "/customers?action=list&deleted=0");
                }
                break;
            }

            case "search": {
                String q = req.getParameter("q");
                List<Customer> results = customerDAO.search(q);
                req.setAttribute("customers", results);
                req.setAttribute("query", q);
                req.getRequestDispatcher("/WEB-INF/views/customers/viewCustomers.jsp")
                        .forward(req, resp);
                break;
            }

            case "list":
            default: {
                String sort = req.getParameter("sort"); // "id" or "name"
                String dir  = req.getParameter("dir");  // "asc" or "desc"
                List<Customer> customers = customerDAO.findAll(sort, dir);
                req.setAttribute("customers", customers);
                req.setAttribute("sort", (sort == null ? "id" : sort));
                req.setAttribute("dir",  (dir  == null ? "asc" : dir));
                req.getRequestDispatcher("/WEB-INF/views/customers/viewCustomers.jsp").forward(req, resp);
                break;
            }

        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        if (!ensureLoggedIn(req, resp)) return;

        String action = req.getParameter("action");
        if (action == null) action = "";

        switch (action) {
            case "create": {
                Customer c = new Customer();
                c.setName(req.getParameter("name"));
                c.setPhone(req.getParameter("phone"));
                c.setEmail(req.getParameter("email"));
                c.setAddress(req.getParameter("address"));

                boolean ok = false;
                try {
                    ok = customerDAO.addCustomer(c);
                } catch (Exception ignored) { }
                // redirect so the list reloads from DB (prevents stale forwards / resubmits)
                resp.sendRedirect(req.getContextPath() + "/customers?action=list&saved=" + (ok ? "1" : "0"));
                return;
            }

            case "update": {
                Customer c = new Customer();
                try {
                    c.setId(Integer.parseInt(req.getParameter("id")));
                } catch (NumberFormatException e) {
                    resp.sendRedirect(req.getContextPath() + "/customers?action=list&updated=0");
                    return;
                }
                c.setName(req.getParameter("name"));
                c.setPhone(req.getParameter("phone"));
                c.setEmail(req.getParameter("email"));
                c.setAddress(req.getParameter("address"));

                boolean ok = false;
                try {
                    ok = customerDAO.updateCustomer(c);
                } catch (Exception ignored) { }
                resp.sendRedirect(req.getContextPath() + "/customers?action=list&updated=" + (ok ? "1" : "0"));
                return;
            }

            default:
                resp.sendRedirect(req.getContextPath() + "/customers?action=list");
        }
    }
}
