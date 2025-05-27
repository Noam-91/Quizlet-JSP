<%@ page contentType="text/html;charset=UTF-8" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Contact Us</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/contact.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/navbar.css">

</head>
<body>
<%@ include file="/WEB-INF/jsp/includes/navbar.jsp" %>

<div class="contact-container">
    <h1>Contact Us</h1>

    <%-- Display success or error messages if available in request scope --%>
    <c:if test="${not empty successMessage}">
        <p class="success-message"><c:out value="${successMessage}"/></p>
    </c:if>
    <c:if test="${not empty errorMessage}">
        <p class="error-message"><c:out value="${errorMessage}"/></p>
    </c:if>

    <form action="<c:url value='${pageContext.request.contextPath}/contact/send'/>" method="post">
        <div class="form-group">
            <label for="subject">Subject:</label>
            <input type="text" id="subject" name="subject" required>
        </div>

        <div class="form-group">
            <label for="email">Your Email:</label>
            <input type="email" id="email" name="email" required>
        </div>

        <div class="form-group">
            <label for="message">Message:</label>
            <textarea id="message" name="message" rows="6" required></textarea>
        </div>

        <button type="submit" class="submit-button">Send Message</button>
    </form>
</div>

</body>
</html>