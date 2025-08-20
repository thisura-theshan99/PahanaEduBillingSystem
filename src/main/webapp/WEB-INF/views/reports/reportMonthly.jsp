<%@ page import="java.util.*" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="/WEB-INF/views/shared/header.jspf" %>
<h2>Monthly Summary</h2>

<%
    Map<String,Object> summary = (Map<String,Object>) request.getAttribute("summary");
    Integer year  = (Integer) request.getAttribute("year");
    Integer month = (Integer) request.getAttribute("month");
%>

<form method="get" action="${pageContext.request.contextPath}/reports">
    <input type="hidden" name="action" value="monthly"/>
    <label>Year</label> <input type="number" name="year" value="<%= year %>" min="2000" max="2100"/>
    <label>Month</label> <input type="number" name="month" value="<%= month %>" min="1" max="12"/>
    <button type="submit">Show</button>
</form>

<% if (summary != null) { %>
<p>Total Bills: <strong><%= summary.get("totalBills") %></strong></p>
<p>Gross Total: <strong><%= summary.get("grossTotal") %></strong></p>
<p>Net Total: <strong><%= summary.get("netTotal") %></strong></p>
<p>Average Ticket: <strong><%= summary.get("avgTicket") %></strong></p>
<% } %>

<p><a href="${pageContext.request.contextPath}/reports?action=topCustomers">View Top Customers</a></p>
<%@ include file="/WEB-INF/views/shared/footer.jspf" %>
