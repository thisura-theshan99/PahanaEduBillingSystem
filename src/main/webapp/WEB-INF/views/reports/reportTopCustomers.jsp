<%@ page import="java.util.*" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="/WEB-INF/views/shared/header.jspf" %>
<h2>Top Customers</h2>

<table>
    <tr><th>Customer ID</th><th>Name</th><th>Total Net</th></tr>
    <%
        List<Map<String,Object>> rows = (List<Map<String,Object>>) request.getAttribute("topCustomers");
        if (rows != null) {
            for (Map<String,Object> r : rows) {
    %>
    <tr>
        <td><%= r.get("customer_id") %></td>
        <td><%= r.get("customer_name") %></td>
        <td><%= r.get("total_net") %></td>
    </tr>
    <%
            }
        }
    %>
</table>
<p><a href="${pageContext.request.contextPath}/reports?action=monthly">Back to Monthly</a></p>
<%@ include file="/WEB-INF/views/shared/footer.jspf" %>
