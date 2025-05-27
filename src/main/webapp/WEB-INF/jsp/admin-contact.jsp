<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Admin Messages</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/admin-contact.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/navbar.css">
</head>
<body>
<%@ include file="/WEB-INF/jsp/includes/navbar.jsp" %>
<c:if test="${!sessionScope.user.isAdmin}">
    <c:redirect url="${pageContext.request.contextPath}/home"/>
</c:if>
<div class="admin-messages-container">
    <h1>Admin Messages</h1>

    <table class="messages-table">
        <thead>
        <tr>
            <th>Subject</th>
            <th>Email</th>
            <th>Received At</th>
            <th>Status</th>
            <th>Message Summary</th>
            <th>Link</th>
        </tr>
        </thead>
        <tbody>
        <c:choose>
            <c:when test="${not empty contacts}">
                <c:forEach var="message" items="${contacts}">
                    <%-- Apply relative positioning to the row for absolute positioning of the link --%>
                    <tr class="message-row">
                        <td><c:out value="${message.subject}"/></td>
                        <td><c:out value="${message.email}"/></td>
                        <td><fmt:formatDate value="${message.createdAt}" pattern="yyyy-MM-dd HH:mm"/></td>
                        <td class="solved-status <c:out value="${message.isSolved ? 'solved-yes' : 'solved-no'}"/>">
                            <c:out value="${message.isSolved ? 'Solved' : 'Unsolved'}"/>
                        </td>
                        <td><c:out value="${message.messageSummary}"/></td>
                        <td><a href="${pageContext.request.contextPath}/admin/messages/${message.contactId}" class="row-link">View</a></td>
                    </tr>
                </c:forEach>
            </c:when>
            <c:otherwise>
                <tr>
                    <td colspan="6">No messages found.</td><%-- colspan is now 6 --%>
                </tr>
            </c:otherwise>
        </c:choose>
        </tbody>
    </table>
</div>

</body>
</html>