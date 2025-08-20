<%@ page import="com.pahana.edu.billing.model.User" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<%
    User user = (User) session.getAttribute("user");
    if (user == null) { response.sendRedirect(request.getContextPath()+"/login"); return; }
    if (!"ADMIN".equalsIgnoreCase(user.getRole())) { response.sendRedirect(request.getContextPath()+"/"); return; }
%>
<%@ include file="/WEB-INF/views/shared/header.jspf" %>

<h2>Admin Dashboard</h2>
<p>Welcome, <strong><%= user.getUsername() %></strong></p>
<hr/>

<h3>Customers</h3>
<ul>
    <li><a href="${pageContext.request.contextPath}/customers?action=list">Manage Customers</a></li>
    <li><a href="${pageContext.request.contextPath}/customers?action=new">Add New Customer</a></li>
</ul>

<h3>Items</h3>
<ul>
    <li><a href="${pageContext.request.contextPath}/items?action=list">Manage Items</a></li>
    <li><a href="${pageContext.request.contextPath}/items?action=new">Add New Item</a></li>
</ul>

<h3>Reports</h3>
<ul>
    <li><a href="${pageContext.request.contextPath}/reports?action=topCustomers">Top Customers</a></li>
</ul>

<%@ include file="/WEB-INF/views/shared/footer.jspf" %>
