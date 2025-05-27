<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c"%>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt"%> <%-- Added for number formatting --%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Admin Dashboard - Quizlet</title>

    <%-- Include the navbar CSS --%>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/navbar.css">
    <%-- Link to the specific admin home CSS --%>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/admin.css">
</head>
<%@ include file="/WEB-INF/jsp/includes/navbar.jsp" %>
<body>
<c:if test="${!sessionScope.user.isAdmin}">
    <c:redirect url="${pageContext.request.contextPath}/home"/>
</c:if>
<div class="main-admin-container">
    <h1>Admin Dashboard</h1>

    <div class="admin-content-flex">
        <%-- Left Panel: Statistics --%>
        <div class="admin-panel stats-panel">
            <h2>Statistics Overview</h2>
            <c:if test="${adminStats != null}">
                <div class="stats-list">
                    <div class="stat-item">
                        <strong>Total Registered Users:</strong> <c:out value="${adminStats.userCount}"/>
                    </div>
                    <div class="stat-item">
                        <strong>Total Questions Available:</strong> <c:out value="${adminStats.questionCount}"/>
                    </div>
                    <div class="stat-item">
                        <strong>Total Quizzes Completed:</strong> <c:out value="${adminStats.completedQuizCount}"/>
                    </div>
                    <div class="stat-item">
                        <strong>Average Quiz Score:</strong>
                        <c:if test="${adminStats.completedQuizCount > 0}">
                            <fmt:formatNumber value="${adminStats.averageQuizScore*100}" type="number" maxFractionDigits="1"/>
                        </c:if>
                        <c:if test="${adminStats.completedQuizCount == 0}">
                            N/A
                        </c:if>
                    </div>
                    <div class="stat-item">
                        <strong>Total Quiz Categories:</strong> <c:out value="${adminStats.activeCategoryCount}"/>
                    </div>
                    <div class="stat-item">
                        <strong>Most Popular Category:</strong> <c:out value="${adminStats.mostPopularCategoryName}"/>
                    </div>
                </div>
            </c:if>
            <c:if test="${adminStats == null}">
                <p>Statistics are currently unavailable.</p>
            </c:if>
        </div>

        <%-- Right Panel --%>
        <div class="admin-panel nav-panel">
            <h2>Management</h2>
            <div class="nav-buttons-list">
                <a href="${pageContext.request.contextPath}/admin/users" class="admin-nav-button">
                    User
                </a>
                <a href="${pageContext.request.contextPath}/admin/quiz-results" class="admin-nav-button">
                    Quiz Result
                </a>
                <a href="${pageContext.request.contextPath}/admin/questions" class="admin-nav-button">
                    Question
                </a>
                <a href="${pageContext.request.contextPath}/admin/messages" class="admin-nav-button">
                    Message
                </a>
            </div>
        </div>
    </div>

</div>

</body>
</html>