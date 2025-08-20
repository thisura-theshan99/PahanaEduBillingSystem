<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="/WEB-INF/views/shared/header.jspf" %>
<h2>Add Customer</h2>

<form action="${pageContext.request.contextPath}/customers" method="post">
    <input type="hidden" name="action" value="create"/>
    <label>Name</label><br/><input name="name" required/><br/><br/>
    <label>Phone</label><br/><input name="phone"/><br/><br/>
    <label>Email</label><br/><input name="email"/><br/><br/>
    <label>Address</label><br/><textarea name="address" rows="3"></textarea><br/><br/>
    <button type="submit">Save</button>
    <a href="${pageContext.request.contextPath}/customers?action=list">Cancel</a>
</form>
<%@ include file="/WEB-INF/views/shared/footer.jspf" %>
