<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="/WEB-INF/views/shared/header.jspf" %>

<style>
    /* centers the card within the main container area */
    .login-page{
        min-height: calc(100vh - 180px);   /* room for header/footer; tweak if needed */
        display: flex;
        align-items: center;                /* vertical center */
        justify-content: center;            /* horizontal center */
    }
    .login-card{
        width: 100%;
        max-width: 420px;
        background: #fff;
        border: 1px solid #e6eaf2;
        border-radius: 12px;
        box-shadow: 0 8px 24px rgba(0,0,0,.06);
        padding: 24px;
    }
    .login-card h1{ margin: 0 0 16px; text-align: center; }
    .login-card .form-group{ margin-bottom: 14px; }
    .login-card input[type="text"],
    .login-card input[type="password"]{
        width: 100%;
        padding: 10px;
        border: 1px solid #d9deea;
        border-radius: 10px;
    }
    .login-card .actions{ margin-top: 8px; display:flex; justify-content:center; }
</style>

<div class="login-page">
    <div class="login-card">
        <h1>Login</h1>

        <form method="post" action="${pageContext.request.contextPath}/login">
            <div class="form-group">
                <label>Username</label>
                <input type="text" name="username" required>
            </div>
            <div class="form-group">
                <label>Password</label>
                <input type="password" name="password" required>
            </div>
            <div class="actions">
                <button class="btn btn-primary" type="submit">Login</button>
            </div>
        </form>

        <hr/>
        <small>&copy; 2025 Pahana Edu</small>
    </div>
</div>

<%@ include file="/WEB-INF/views/shared/footer.jspf" %>
