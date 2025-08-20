package com.pahana.edu.billing.controller;

import com.pahana.edu.billing.dao.UserDAO;
import com.pahana.edu.billing.model.User;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;

public class LoginServlet extends HttpServlet {

    private UserDAO userDAO;

    @Override
    public void init() {
        userDAO = new UserDAO();
    }

    // Show login page
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/auth/login.jsp").forward(request, response);
    }

    // Handle login
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        User user = userDAO.validateUser(username, password);

        if (user != null) {
            HttpSession session = request.getSession(true);
            session.setAttribute("user", user);

            String target = user.getRole() != null && user.getRole().equalsIgnoreCase("ADMIN")
                    ? "/WEB-INF/views/admin/dashboard.jsp"
                    : "/WEB-INF/views/staff/dashboard.jsp";

            request.getRequestDispatcher(target).forward(request, response);
        } else {
            request.setAttribute("errorMessage", "Invalid username or password!");
            request.getRequestDispatcher("/WEB-INF/views/auth/login.jsp").forward(request, response);
        }
    }
}
