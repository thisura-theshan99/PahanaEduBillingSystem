<%@ page import="java.util.*, com.pahana.edu.billing.model.Customer" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="/WEB-INF/views/shared/header.jspf" %>

<h2>Customers</h2>

<!-- Top actions -->
<div style="margin:10px 0 16px;">
    <a class="btn btn-primary" href="${pageContext.request.contextPath}/customers?action=new">Add New Customer</a>
</div>

<!-- Sort form -->
<form method="get" action="${pageContext.request.contextPath}/customers" style="margin:10px 0;">
    <input type="hidden" name="action" value="list"/>
    <label style="margin-right:8px;">Sort by</label>
    <select name="sort" class="form-control" style="width:auto; display:inline-block;">
        <option value="id"   <%= "id".equalsIgnoreCase((String)request.getAttribute("sort")) ? "selected" : "" %>>ID</option>
        <option value="name" <%= "name".equalsIgnoreCase((String)request.getAttribute("sort")) ? "selected" : "" %>>Name</option>
    </select>

    <label style="margin:0 8px 0 16px;">Direction</label>
    <select name="dir" class="form-control" style="width:auto; display:inline-block;">
        <option value="asc"  <%= "asc".equalsIgnoreCase((String)request.getAttribute("dir")) ? "selected" : "" %>>Ascending</option>
        <option value="desc" <%= "desc".equalsIgnoreCase((String)request.getAttribute("dir")) ? "selected" : "" %>>Descending</option>
    </select>

    <button class="btn btn-primary" type="submit" style="margin-left:10px;">Apply</button>
</form>

<%
    @SuppressWarnings("unchecked")
    List<Customer> customers = (List<Customer>) request.getAttribute("customers");
%>

<% if (customers == null) { %>
<div class="alert error">No data provided. Open this page via
    <code><%= request.getContextPath() %>/customers?action=list</code>.
</div>
<% } else if (customers.isEmpty()) { %>
<div class="alert">No customers found.</div>
<% } else { %>

<table class="table">
    <thead>
    <tr>
        <th style="width:70px;">ID</th>
        <th>Name</th>
        <th>Email</th>
        <th>Phone</th>
        <th>Address</th>
        <th style="width:150px;">Actions</th>
    </tr>
    </thead>
    <tbody>
    <%
        for (Customer c : customers) {
    %>
    <tr>
        <td><%= c.getId() %></td>
        <td><%= c.getName() == null ? "" : c.getName() %></td>
        <td><%= c.getEmail() == null ? "" : c.getEmail() %></td>
        <td><%= c.getPhone() == null ? "" : c.getPhone() %></td>
        <td><%= c.getAddress() == null ? "" : c.getAddress() %></td>
        <td class="actions">
            <a class="btn btn-primary" href="${pageContext.request.contextPath}/customers?action=edit&id=<%= c.getId() %>">Edit</a>
            <a class="btn btn-primary" href="${pageContext.request.contextPath}/customers?action=delete&id=<%= c.getId() %>"
               onclick="return confirm('Delete this customer?');">Delete</a>
        </td>
    </tr>
    <%
        } // end for
    %>
    </tbody>
</table>

<% } // end else %>

<%@ include file="/WEB-INF/views/shared/footer.jspf" %>
