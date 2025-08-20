<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="/WEB-INF/views/shared/header.jspf" %>

<h1>Welcome to Pahana Edu Billing System</h1>
<p style="max-width:700px;">
    Please log in to continue.
</p>

<%-- If user already logged in, show dashboard link instead --%>
<%
    com.pahana.edu.billing.model.User u =
            (com.pahana.edu.billing.model.User) session.getAttribute("user");
    if (u != null) {
%>
<p><a href="<%= request.getContextPath() %>/bills?action=new">Go to Dashboard</a></p>
<% } %>

<%@ include file="/WEB-INF/views/shared/footer.jspf" %>
