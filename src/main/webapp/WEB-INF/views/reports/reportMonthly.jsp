<%@ page import="java.util.*" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="/WEB-INF/views/shared/header.jspf" %>

<h2>Monthly Summary</h2>

<%
    // read year/month from request to keep values in the form
    String yearStr  = request.getAttribute("year")  == null ? request.getParameter("year")  : String.valueOf(request.getAttribute("year"));
    String monthStr = request.getAttribute("month") == null ? request.getParameter("month") : String.valueOf(request.getAttribute("month"));
    if (yearStr == null || yearStr.isBlank())  yearStr  = String.valueOf(java.time.LocalDate.now().getYear());
    if (monthStr == null || monthStr.isBlank()) monthStr = String.valueOf(java.time.LocalDate.now().getMonthValue());

    @SuppressWarnings("unchecked")
    Map<String,Object> summary = (Map<String,Object>) request.getAttribute("summary");
    int    billsCount  = 0;
    double totalAmount = 0.0;
    double netAmount   = 0.0;
    double avgTicket   = 0.0;
    if (summary != null) {
        Object v;
        v = summary.get("billsCount");  if (v instanceof Number) billsCount  = ((Number)v).intValue();
        v = summary.get("totalAmount"); if (v instanceof Number) totalAmount = ((Number)v).doubleValue();
        v = summary.get("netAmount");   if (v instanceof Number) netAmount   = ((Number)v).doubleValue();
        v = summary.get("avgTicket");   if (v instanceof Number) avgTicket   = ((Number)v).doubleValue();

        // fallbacks if another key set was used
        if (billsCount == 0 && summary.get("bill_count") instanceof Number)
            billsCount = ((Number)summary.get("bill_count")).intValue();
        if (totalAmount == 0.0 && summary.get("total_gross") instanceof Number)
            totalAmount = ((Number)summary.get("total_gross")).doubleValue();
        if (netAmount == 0.0 && summary.get("total_net") instanceof Number)
            netAmount = ((Number)summary.get("total_net")).doubleValue();
        if (avgTicket == 0.0 && summary.get("avg_ticket") instanceof Number)
            avgTicket = ((Number)summary.get("avg_ticket")).doubleValue();
    }
%>

<form method="get" action="${pageContext.request.contextPath}/reports" style="margin:12px 0;">
    <input type="hidden" name="action" value="monthly"/>
    Year
    <input class="form-control" type="number" name="year" value="<%= yearStr %>" style="width:120px; display:inline-block; margin-right:10px;">
    Month
    <input class="form-control" type="number" name="month" value="<%= monthStr %>" min="1" max="12" style="width:90px; display:inline-block; margin-right:10px;">
    <button class="btn btn-primary" type="submit">Show</button>
</form>

<p>Total Bills: <strong><%= billsCount %></strong></p>
<p>Gross Total: <strong><%= String.format(java.util.Locale.US, "%.2f", totalAmount) %></strong></p>
<p>Net Total: <strong><%= String.format(java.util.Locale.US, "%.2f", netAmount) %></strong></p>


<p style="margin-top:12px;">
    <a href="${pageContext.request.contextPath}/reports?action=topCustomers">View Top Customers</a>
</p>

<%@ include file="/WEB-INF/views/shared/footer.jspf" %>
