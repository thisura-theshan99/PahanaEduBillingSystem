<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="/WEB-INF/views/shared/header.jspf" %>
<h2>Add Item</h2>

<form action="${pageContext.request.contextPath}/items" method="post">
    <input type="hidden" name="action" value="create"/>
    <label>Name</label><br/><input name="name" required/><br/><br/>
    <label>Description</label><br/><input name="description"/><br/><br/>
    <label>Price</label><br/><input name="price" type="number" step="0.01" min="0" required/><br/><br/>
    <button type="submit">Save</button>
    <a href="${pageContext.request.contextPath}/items?action=list">Cancel</a>
</form>
<%@ include file="/WEB-INF/views/shared/footer.jspf" %>
