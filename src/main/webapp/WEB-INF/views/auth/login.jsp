<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Pahana Edu Billing System - Login</title>
</head>
<body>
<h2>Login</h2>
<form action="${pageContext.request.contextPath}/login" method="post">
    <label>Username:</label>
    <input type="text" name="username" required /><br/>

    <label>Password:</label>
    <input type="password" name="password" required /><br/>

    <input type="submit" value="Login"/>
</form>

<p style="color:red;">
    ${requestScope.errorMessage}
</p>
</body>
</html>
