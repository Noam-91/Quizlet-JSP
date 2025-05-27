<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<html>
<head>
    <title>Login</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/navbar.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/login.css">
</head>

<body>

<%@ include file="/WEB-INF/jsp/includes/navbar.jsp" %>

<div class="container">
    <h2>Login</h2>
    <form method="post" action="${pageContext.request.contextPath}/login">
        <div class="form-group">
            <label for="username">Username</label>
            <input type="text" id="username" name="username" required>
        </div>
        <div class="form-group">
            <label for="password">Password</label>
            <input type="password" id="password" name="password" required>
        </div>
        <button type="submit">Login</button>
    </form>

    <hr style="margin: 20px 0;">

    <form action="${pageContext.request.contextPath}/register" method="get" class="signup-form">
        <button type="submit">Sign Up</button>
    </form>
</div>

</body>
</html>
