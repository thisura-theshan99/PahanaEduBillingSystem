<%@ page import="java.util.*,com.pahana.edu.billing.model.Item" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="/WEB-INF/views/shared/header.jspf" %>
<h2>Generate Bill</h2>

<% String err = (String)request.getAttribute("errorMessage");
    if (err != null) { %><p class="error"><%= err %></p><% } %>

<form action="${pageContext.request.contextPath}/bills" method="post">
    <input type="hidden" name="action" value="create"/>

    <form method="post" action="${pageContext.request.contextPath}/bills">
        <input type="hidden" name="action" value="create"/>

    <label>Customer ID</label><br/>
    <input type="number" name="customerId" min="1" required/><br/><br/>

    <label>Discount (%)</label><br/>
    <input type="number" name="discountPercent" min="0" max="100" step="0.01" value="0"/><br/><br/>

        <div class="table-toolbar">
            <button type="submit" class="btn btn-primary">Generate Bill</button>
        </div>

    <h3>Items</h3>

    <table>
        <tr><th>Select</th><th>Item</th><th>Price</th><th>Quantity</th></tr>
        <%
            List<Item> items = (List<Item>) request.getAttribute("items");
            if (items != null) {
                for (Item it : items) {
        %>
        <tr>
            <td><input type="checkbox" name="itemId" value="<%= it.getId() %>"/></td>
            <td><%= it.getName() %></td>
            <td><%= String.format("%.2f", it.getPrice()) %></td>
            <td><input type="number" name="quantity" min="0" value="1"/></td>
        </tr>
        <%
                }
            }
        %>
    </table>
    <br/>
</form>
<%@ include file="/WEB-INF/views/shared/footer.jspf" %>
