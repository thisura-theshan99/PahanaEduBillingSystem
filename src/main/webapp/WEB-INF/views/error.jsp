<%@ page isErrorPage="true" contentType="text/html; charset=UTF-8" %>
<%@ include file="/WEB-INF/views/shared/header.jspf" %>
<h2>Oops! Something went wrong.</h2>
<p class="error">An unexpected error occurred.</p>
<% if (exception != null) { %>
<pre><%= exception.getMessage() %></pre>
<% } %>
<%@ include file="/WEB-INF/views/shared/footer.jspf" %>
