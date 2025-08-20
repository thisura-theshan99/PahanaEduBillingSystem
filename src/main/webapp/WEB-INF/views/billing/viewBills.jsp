<%@ page import="java.util.*,com.pahana.edu.billing.model.Bill" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="/WEB-INF/views/shared/header.jspf" %>
<h2>All Bills</h2>

<table>
    <tr><th>ID</th><th>Customer ID</th><th>Gross</th><th>Discount %</th><th>Net</th><th>Created</th><th>Actions</th></tr>
    <%
        List<Bill> bills = (List<Bill>) request.getAttribute("bills");
        if (bills != null) {
            for (Bill b : bills) {
    %>
    <tr>
        <td><%= b.getId() %></td>
        <td><%= b.getCustomerId() %></td>
        <td><%= String.format("%.2f", b.getTotalAmount()) %></td>
        <td><%= b.getDiscountPercent() %></td>
        <td><%= String.format("%.2f", b.getNetAmount()) %></td>
        <td><%= b.getCreatedAt() %></td>
        <td><a href="${pageContext.request.contextPath}/bills?action=print&id=<%= b.getId() %>">Print</a></td>
    </tr>
    <%
            }
        }
    %>
</table>
<%@ include file="/WEB-INF/views/shared/footer.jspf" %>
