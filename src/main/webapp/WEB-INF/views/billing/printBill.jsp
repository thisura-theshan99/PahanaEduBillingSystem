<%@ page import="java.util.*,com.pahana.edu.billing.model.Bill,com.pahana.edu.billing.model.BillItem" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="/WEB-INF/views/shared/header.jspf" %>
<%
    Bill bill = (Bill) request.getAttribute("bill");
    List<BillItem> billItems = (List<BillItem>) request.getAttribute("billItems");
%>
<h2>Bill #<%= bill.getId() %></h2>
<p>Customer ID: <%= bill.getCustomerId() %></p>
<p>Created: <%= bill.getCreatedAt() %></p>

<table>
    <tr><th>Item ID</th><th>Qty</th><th>Unit Price</th><th>Line Total</th></tr>
    <%
        if (billItems != null) {
            for (BillItem bi : billItems) {
    %>
    <tr>
        <td><%= bi.getItemId() %></td>
        <td><%= bi.getQuantity() %></td>
        <td><%= String.format("%.2f", bi.getUnitPrice()) %></td>
        <td><%= String.format("%.2f", bi.getLineTotal()) %></td>
    </tr>
    <%
            }
        }
    %>
</table>

<h3>Totals</h3>
<p>Gross: <strong><%= String.format("%.2f", bill.getTotalAmount()) %></strong></p>
<p>Discount: <strong><%= bill.getDiscountPercent() %>%</strong></p>
<p>Net: <strong><%= String.format("%.2f", bill.getNetAmount()) %></strong></p>

<p><button onclick="window.print()">Print</button></p>
<%@ include file="/WEB-INF/views/shared/footer.jspf" %>
