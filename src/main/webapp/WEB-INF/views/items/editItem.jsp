<%@ page import="com.pahana.edu.billing.model.Item" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<%
    Item it = (Item) request.getAttribute("item");
%>
<%@ include file="/WEB-INF/views/shared/header.jspf" %>
<h2>Edit Item</h2>

<form action="${pageContext.request.contextPath}/items" method="post">
    <input type="hidden" name="action" value="update"/>
    <input type="hidden" name="id" value="<%= it.getId() %>"/>
    <label>Name</label><br/><input name="name" value="<%= it.getName() %>" required/><br/><br/>
    <label>Description</label><br/><input name="description" value="<%= it.getDescription() %>"/><br/><br/>
    <label>Price</label><br/><input name="price" type="number" step="0.01" min="0" value="<%= it.getPrice() %>" required/><br/><br/>
    <button type="submit">Update</button>
    <a href="${pageContext.request.contextPath}/items?action=list">Cancel</a>
</form>
<%@ include file="/WEB-INF/views/shared/footer.jspf" %>
