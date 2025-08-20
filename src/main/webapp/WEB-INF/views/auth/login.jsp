<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="/WEB-INF/views/shared/header.jspf" %>

<h2>Login</h2>

<form action="${pageContext.request.contextPath}/login" method="post" autocomplete="off">
    <label>Username</label><br/>
    <input type="text" name="username" required/><br/><br/>
    <label>Password</label><br/>
    <input type="password" name="password" required/><br/><br/>
    <button type="submit">Login</button>
</form>

<%
    String err = (String) request.getAttribute("errorMessage");
    if (err != null) {
%>
<p class="error"><%= err %></p>
<%
    }
%>

<%@ include file="/WEB-INF/views/shared/footer.jspf" %>
