<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Message Detail - Admin</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/admin-contact-detail.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/navbar.css">

</head>
<body>
<%@ include file="/WEB-INF/jsp/includes/navbar.jsp" %>
<c:if test="${!sessionScope.user.isAdmin}">
    <c:redirect url="${pageContext.request.contextPath}/home"/>
</c:if>
<div class="message-detail-container">
    <h1>Message Detail</h1>

    <div class="detail-item">
        <label>Subject:</label>
        <span><c:out value="${contact.subject}"/></span>
    </div>

    <div class="detail-item">
        <label>From Email:</label>
        <span><c:out value="${contact.email}"/></span>
    </div>

    <div class="detail-item">
        <label>Received At:</label>
        <span><fmt:formatDate value="${contact.createdAt}" pattern="yyyy-MM-dd HH:mm:ss"/></span>
    </div>

    <div class="detail-item">
        <label>Status:</label>
        <span class="status-<c:out value="${contact.isSolved ? 'solved' : 'unsolved'}"/>">
                <c:out value="${contact.isSolved ? 'Solved' : 'Unsolved'}"/>
            </span>
    </div>

    <div class="detail-item full-message">
        <label>Message:</label>
        <p><c:out value="${contact.message}"/></p>
    </div>

    <div class="action-form">
        <h2>Update Status</h2>
        <%-- Form to update the solved status --%>
        <form action="/admin/messages/update" method="post">
            <input type="hidden" name="contactId" value="${contact.contactId}">
            <c:if test="${contact.isSolved}">
                <%-- If currently solved, show button to mark as unsolved --%>
                <input type="hidden" name="isSolved" value="false">
                <button type="submit" class="status-button mark-unsolved">Mark as Unsolved</button>
            </c:if>
            <c:if test="${!contact.isSolved}">
                <%-- If currently unsolved, show button to mark as solved --%>
                <input type="hidden" name="isSolved" value="true">
                <button type="submit" class="status-button mark-solved">Mark as Solved</button>
            </c:if>
        </form>
    </div>

    <p class="back-link"><a href="<c:url value='/admin/messages'/>">&larr; Back to Messages List</a></p>

</div>

</body>
</html>