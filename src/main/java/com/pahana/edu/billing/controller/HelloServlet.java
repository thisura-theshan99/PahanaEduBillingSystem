package com.pahana.edu.billing.controller;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;

public class HelloServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("text/html; charset=UTF-8");
        try (PrintWriter out = resp.getWriter()) {
            out.println("<html><body>");
            out.println("<h1>✅ Hello from Pahana Edu Billing System!</h1>");
            out.println("<p>This is a basic servlet test page.</p>");
            out.println("</body></html>");
        }
    }
}
