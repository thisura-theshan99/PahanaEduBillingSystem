package com.pahana.edu.billing.controller;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;

public class LogoutServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Kill session
        HttpSession session = request.getSession(false);
        if (session != null) session.invalidate();

        // Optional: clear JSESSIONID cookie + add no-cache to avoid back-button showing old pages
        Cookie kill = new Cookie("JSESSIONID", "");
        kill.setPath(request.getContextPath().isEmpty() ? "/" : request.getContextPath());
        kill.setMaxAge(0);
        response.addCookie(kill);
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1
        response.setHeader("Pragma", "no-cache");                                    // HTTP 1.0
        response.setDateHeader("Expires", 0);                                        // Proxies

        // ➜ Go to welcome page (root)
        response.sendRedirect(request.getContextPath() + "/");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
