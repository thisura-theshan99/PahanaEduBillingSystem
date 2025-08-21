<%@ page import="java.util.*,com.pahana.edu.billing.model.Item" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="/WEB-INF/views/shared/header.jspf" %>

<!-- Title + Add button -->
<div style="display:flex; align-items:center; justify-content:space-between; margin-bottom:8px;">
    <h2 style="margin:0;">Items</h2>
    <a class="btn btn-primary"
       href="${pageContext.request.contextPath}/items?action=new">Add New Item</a>
</div>

<%
    String saved   = request.getParameter("saved");
    String updated = request.getParameter("updated");
    String deleted = request.getParameter("deleted");
    if ("1".equals(saved))   { %><p class="ok">Saved successfully.</p><% }
    if ("1".equals(updated)) { %><p class="ok">Updated successfully.</p><% }
    if ("1".equals(deleted)) { %><p class="ok">Deleted successfully.</p><% }
%>

<form method="get" action="${pageContext.request.contextPath}/items" style="margin:10px 0;">
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
    <tr>
        <th>ID</th>
        <th>Name</th>
        <th>Description</th>
        <th>Price</th>
        <th>Active</th>
        <th>Actions</th>
    </tr>
    <%
        List<Item> items = (List<Item>) request.getAttribute("items");
        if (items != null) {
            for (Item it : items) {
    %>
    <tr>
        <td><%= it.getId() %></td>
        <td><%= it.getName() %></td>
        <td><%= it.getDescription() %></td>
        <td><%= String.format("%.2f", it.getPrice()) %></td>
        <td><%= it.isActive() ? "Yes" : "No" %></td>
        <td class="actions">
            <a href="${pageContext.request.contextPath}/items?action=edit&id=<%= it.getId() %>">Edit</a>
            <a href="${pageContext.request.contextPath}/items?action=delete&id=<%= it.getId() %>" onclick="return confirm('Delete?')">Delete</a>
        </td>
    </tr>
    <%
            }
        }
    %>
</table>

<%@ include file="/WEB-INF/views/shared/footer.jspf" %>
