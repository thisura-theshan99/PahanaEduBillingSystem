package com.pahana.edu.billing.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class AuthFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // optional: any init params from web.xml
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req  = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        HttpSession session     = req.getSession(false);

        String loginURI = req.getContextPath() + "/login";

        boolean loggedIn = (session != null && session.getAttribute("user") != null);
        boolean loginRequest = req.getRequestURI().equals(loginURI)
                || req.getRequestURI().endsWith("login.jsp");

        if (loggedIn || loginRequest) {
            chain.doFilter(request, response); // allow
        } else {
            res.sendRedirect(loginURI); // redirect to login
        }
    }

    @Override
    public void destroy() {
        // cleanup if needed
    }
}
