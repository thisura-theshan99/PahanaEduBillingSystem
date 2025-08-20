<%@ page import="com.pahana.edu.billing.model.User" %>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%
    // ✅ Guard: only logged-in users can view dashboard
    User user = (User) session.getAttribute("user");
    if (user == null) {
        response.sendRedirect(request.getContextPath() + "/login");
        return;
    }
%>
<!DOCTYPE html>
<html>
<head>
    <title>Pahana Edu — Dashboard</title>
    <meta charset="UTF-8">
</head>
<body>

<h2>Welcome, <%= user.getUsername() %> (Role: <%= user.getRole() %>)</h2>
<hr/>

<h3>Quick Actions</h3>
<ul>
    <li><a href="<%= request.getContextPath() %>/bills?action=new">Generate Bill</a></li>
    <li><a href="<%= request.getContextPath() %>/bills?action=view">View Bills</a></li>
    <li><a href="<%= request.getContextPath() %>/reports?action=monthly">Monthly Report</a></li>
</ul>

<% if ("ADMIN".equalsIgnoreCase(user.getRole())) { %>
<h3>Admin Actions</h3>
<ul>
    <li><a href="<%= request.getContextPath() %>/customers?action=list">Manage Customers</a></li>
    <li><a href="<%= request.getContextPath() %>/customers?action=new">Add Customer</a></li>
    <li><a href="<%= request.getContextPath() %>/items?action=list">Manage Items</a></li>
    <li><a href="<%= request.getContextPath() %>/items?action=new">Add Item</a></li>
    <li><a href="<%= request.getContextPath() %>/reports?action=topCustomers">Top Customers</a></li>
</ul>
<% } %>

<p style="margin-top:20px;">
    <a href="<%= request.getContextPath() %>/logout">Logout</a>
</p>

</body>
</html>
