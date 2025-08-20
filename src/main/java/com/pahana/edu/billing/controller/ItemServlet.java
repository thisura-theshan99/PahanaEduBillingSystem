package com.pahana.edu.billing.controller;

import com.pahana.edu.billing.dao.ItemDAO;
import com.pahana.edu.billing.model.Item;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;

public class ItemServlet extends HttpServlet {

    private ItemDAO itemDAO;

    @Override
    public void init() {
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
        if (action == null) action = "list";

        switch (action) {
            case "new": {
                req.getRequestDispatcher("/WEB-INF/views/items/addItem.jsp")
                        .forward(req, resp);
                break;
            }

            case "edit": {
                try {
                    int id = Integer.parseInt(req.getParameter("id"));
                    Item it = itemDAO.findById(id);
                    req.setAttribute("item", it);
                    req.getRequestDispatcher("/WEB-INF/views/items/editItem.jsp")
                            .forward(req, resp);
                } catch (Exception e) {
                    resp.sendRedirect(req.getContextPath() + "/items?action=list&err=badId");
                }
                break;
            }

            case "delete": {
                try {
                    int id = Integer.parseInt(req.getParameter("id"));
                    boolean ok = itemDAO.deleteItem(id);
                    resp.sendRedirect(req.getContextPath() + "/items?action=list&deleted=" + (ok ? "1" : "0"));
                } catch (Exception e) {
                    resp.sendRedirect(req.getContextPath() + "/items?action=list&deleted=0");
                }
                break;
            }

            case "search": {
                String q = req.getParameter("q");
                List<Item> results = itemDAO.search(q);
                req.setAttribute("items", results);
                req.setAttribute("query", q);
                req.getRequestDispatcher("/WEB-INF/views/items/viewItems.jsp")
                        .forward(req, resp);
                break;
            }

            case "list":
            default: {
                String sort = req.getParameter("sort"); // "id" or "name"
                String dir  = req.getParameter("dir");  // "asc" or "desc"
                List<Item> items = itemDAO.findAll(sort, dir);
                req.setAttribute("items", items);
                req.setAttribute("sort", (sort == null ? "id" : sort));
                req.setAttribute("dir",  (dir  == null ? "asc" : dir));
                req.getRequestDispatcher("/WEB-INF/views/items/viewItems.jsp").forward(req, resp);
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
                Item it = new Item();
                it.setName(req.getParameter("name"));
                it.setDescription(req.getParameter("description"));

                boolean ok = false;
                try {
                    String priceStr = req.getParameter("price");
                    double price = Double.parseDouble(priceStr);
                    it.setPrice(price);
                    // Optional: default to active
                    it.setActive(true);
                    ok = itemDAO.addItem(it);
                } catch (Exception e) {
                    ok = false;
                }
                resp.sendRedirect(req.getContextPath() + "/items?action=list&saved=" + (ok ? "1" : "0"));
                return;
            }

            case "update": {
                Item it = new Item();
                boolean ok = false;
                try {
                    it.setId(Integer.parseInt(req.getParameter("id")));
                    it.setName(req.getParameter("name"));
                    it.setDescription(req.getParameter("description"));
                    double price = Double.parseDouble(req.getParameter("price"));
                    it.setPrice(price);
                    // Optional: you could read active flag from form if you have it
                    ok = itemDAO.updateItem(it);
                } catch (Exception e) {
                    ok = false;
                }
                resp.sendRedirect(req.getContextPath() + "/items?action=list&updated=" + (ok ? "1" : "0"));
                return;
            }

            default:
                resp.sendRedirect(req.getContextPath() + "/items?action=list");
        }
    }
}
