<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<html>
<head>
    <title>Sign Up</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/register.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/navbar.css">   <%-- Link to login page specific CSS --%>
</head>

<%@ include file="/WEB-INF/jsp/includes/navbar.jsp" %>
<body>
<div class="container">
    <h2>Sign Up</h2>

    <form method="post" action="${pageContext.request.contextPath}/register">
        <div class="form-group">
            <label for="email">Email:</label>
            <input type="email" id="email" name="email" required> <%-- Use type="email" for better validation --%>
        </div>
        <div class="form-group">
            <label for="password">Password:</label>
            <input type="password" id="password" name="password" required> <%-- Use type="password" --%>
        </div>
        <div class="form-group">
            <label for="firstname">First Name:</label>
            <input type="text" id="firstname" name="firstname" required>
        </div>
        <div class="form-group">
            <label for="lastname">Last Name:</label>
            <input type="text" id="lastname" name="lastname" required>
        </div>
        <%-- Add other registration fields here if needed (e.g., confirm password) --%>

        <button type="submit">Register</button>
    </form>

    <hr style="margin: 20px 0;"> <%-- Add a separator --%>

    <%-- Link back to Login --%>
    <p>Already have an account? <a href="${pageContext.request.contextPath}/login">Login here</a></p>
</div>
</body>

</html>
