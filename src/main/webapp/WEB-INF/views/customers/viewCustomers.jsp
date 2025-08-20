<%@ page import="java.util.*,com.pahana.edu.billing.model.Customer" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="/WEB-INF/views/shared/header.jspf" %>
<h2>Customers</h2>

<% String saved = request.getParameter("saved");
    if ("1".equals(saved)) { %>
<p class="ok">Saved successfully.</p>
<% } %>


<form method="get" action="${pageContext.request.contextPath}/customers" style="margin:10px 0;">
    <input type="hidden" name="action" value="list"/>
    Sort by:
    <select name="sort">
        <option value="id"   <%= "id".equalsIgnoreCase((String)request.getAttribute("sort")) ? "selected" : "" %>>ID</option>
        <option value="name" <%= "name".equalsIgnoreCase((String)request.getAttribute("sort")) ? "selected" : "" %>>Name</option>
    </select>
    <select name="dir">
        <option value="asc"  <%= "asc".equalsIgnoreCase((String)request.getAttribute("dir")) ? "selected" : "" %>>Ascending</option>
        <option value="desc" <%= "desc".equalsIgnoreCase((String)request.getAttribute("dir")) ? "selected" : "" %>>Descending</option>
    </select>
    <button type="submit">Apply</button>
</form>


<table>
    <tr><th>ID</th><th>Name</th><th>Phone</th><th>Email</th><th>Address</th><th>Actions</th></tr>
    <%
        List<Customer> customers = (List<Customer>) request.getAttribute("customers");
        if (customers != null) {
            for (Customer c : customers) {
    %>
    <tr>
        <td><%= c.getId() %></td>
        <td><%= c.getName() %></td>
        <td><%= c.getPhone() %></td>
        <td><%= c.getEmail() %></td>
        <td><%= c.getAddress() %></td>
        <td class="actions">
            <a href="${pageContext.request.contextPath}/customers?action=edit&id=<%= c.getId() %>">Edit</a>
            <a href="${pageContext.request.contextPath}/customers?action=delete&id=<%= c.getId() %>" onclick="return confirm('Delete?')">Delete</a>
        </td>
    </tr>
    <%
            }
        }
    %>
</table>
<%@ include file="/WEB-INF/views/shared/footer.jspf" %>
