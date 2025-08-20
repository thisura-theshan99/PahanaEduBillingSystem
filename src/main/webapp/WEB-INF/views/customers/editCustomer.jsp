<%@ page import="com.pahana.edu.billing.model.Customer" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<%
    Customer c = (Customer) request.getAttribute("customer");
%>
<%@ include file="/WEB-INF/views/shared/header.jspf" %>
<h2>Edit Customer</h2>

<form action="${pageContext.request.contextPath}/customers" method="post">
    <input type="hidden" name="action" value="update"/>
    <input type="hidden" name="id" value="<%= c.getId() %>"/>
    <label>Name</label><br/><input name="name" value="<%= c.getName() %>" required/><br/><br/>
    <label>Phone</label><br/><input name="phone" value="<%= c.getPhone() %>"/><br/><br/>
    <label>Email</label><br/><input name="email" value="<%= c.getEmail() %>"/><br/><br/>
    <label>Address</label><br/><textarea name="address" rows="3"><%= c.getAddress() %></textarea><br/><br/>
    <button type="submit">Update</button>
    <a href="${pageContext.request.contextPath}/customers?action=list">Cancel</a>
</form>
<%@ include file="/WEB-INF/views/shared/footer.jspf" %>
