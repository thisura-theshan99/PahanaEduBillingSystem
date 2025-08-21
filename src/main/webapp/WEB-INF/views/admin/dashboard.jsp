<%@ page import="com.pahana.edu.billing.model.User" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<%
    User user = (User) session.getAttribute("user");
    if (user == null) { response.sendRedirect(request.getContextPath()+"/login"); return; }
    if (!"ADMIN".equalsIgnoreCase(user.getRole())) { response.sendRedirect(request.getContextPath()+"/"); return; }
%>
<%@ include file="../shared/header.jspf" %>

<style>
    /* center the actions row on this page only */
    .actions-center{
        display:flex;
        justify-content:center;
        gap:12px;
        flex-wrap:wrap;
        margin-top:16px;
    }
</style>

<div class="section">
    <h2 style="margin:0; text-align:center;">Admin Dashboard</h2>

    <div class="actions-center">
        <a class="btn btn-primary" href="${pageContext.request.contextPath}/customers?action=list">
            Manage Customers
        </a>
        <a class="btn btn-primary" href="${pageContext.request.contextPath}/items?action=list">
            Manage Items
        </a>
        <a class="btn btn-primary" href="${pageContext.request.contextPath}/reports?action=topCustomers">
            Generate Reports
        </a>
    </div>
</div>

<%@ include file="../shared/footer.jspf" %>
