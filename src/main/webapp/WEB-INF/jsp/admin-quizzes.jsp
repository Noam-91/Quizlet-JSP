<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>


<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Quiz Results</title>
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/admin-quizzes.css'/>">
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/navbar.css'/>">
</head>
<body>
<%@ include file="/WEB-INF/jsp/includes/navbar.jsp" %>
<c:if test="${!sessionScope.user.isAdmin}">
    <c:redirect url="${pageContext.request.contextPath}/home"/>
</c:if>
<h1>Quiz Results Management</h1>

<div class="filter-options">
    <form action="<c:url value='/admin/quiz-results'/>" method="get">
        <label for="filterCategoryId">Category:</label>
        <select name="filterCategoryId" id="filterCategoryId">
            <option value="">All Categories</option>
            <c:forEach var="category" items="${categories}">
                <%-- Use filterCategoryId from model to preserve selection --%>
                <option value="${category.categoryId}" ${category.categoryId == filterCategoryId ? 'selected' : ''}>${category.name}</option>
            </c:forEach>
        </select>

        <label for="filterCreatorId">User:</label>
        <select name="filterCreatorId" id="filterCreatorId">
            <option value="">All Users</option>
            <c:forEach var="user" items="${users}">
                <%-- Use filterCreatorId from model to preserve selection --%>
                <option value="${user.userId}" ${user.userId == filterCreatorId ? 'selected' : ''}>${user.firstname} ${user.lastname}</option>
            </c:forEach>
        </select>

        <%-- Preserve current sort and pagination parameters in the filter form --%>
        <c:if test="${not empty sortBy}">
            <input type="hidden" name="sortBy" value="<c:out value="${sortBy}"/>">
        </c:if>
        <c:if test="${not empty currentSortOrder}">
            <input type="hidden" name="sortOrder" value="<c:out value="${currentSortOrder}"/>">
        </c:if>
        <%-- Keep current page parameter to return to the same page after filtering --%>
        <input type="hidden" name="page" value="<c:out value="${currentPage}"/>">


        <button type="submit">Filter</button>
    </form>
</div>

<table class="quiz-results-table">
    <thead>
    <tr>
        <%-- Taken Time Header (Default Sort) --%>
        <th class="sortable ${sortBy eq 'createdAt' ? 'sorted-' : ''}${sortBy eq 'createdAt' ? currentSortOrder : ''}">
            <c:url var="sortUrl" value="/admin/quiz-results">
                <c:param name="filterCategoryId" value="${filterCategoryId}"/>
                <c:param name="filterCreatorId" value="${filterCreatorId}"/>
                <c:param name="sortBy" value="createdAt"/>
                <%-- Toggle sort order --%>
                <c:param name="sortOrder" value="${sortBy eq 'createdAt' && currentSortOrder eq 'asc' ? 'desc' : 'asc'}"/>
                <c:param name="page" value="${currentPage}"/> <%-- Stay on current page --%>
            </c:url>
            <a href="${sortUrl}">Taken Time</a>
            <%-- Display sort indicator --%>
            <c:if test="${sortBy eq 'createdAt'}">
                <c:choose>
                    <c:when test="${currentSortOrder eq 'asc'}"> &uarr;</c:when>
                    <c:otherwise> &darr;</c:otherwise>
                </c:choose>
            </c:if>
        </th>

        <%-- Category Header (Sortable) --%>
        <th class="sortable ${sortBy eq 'categoryName' ? 'sorted-' : ''}${sortBy eq 'categoryName' ? currentSortOrder : ''}">
            <c:url var="sortUrl" value="/admin/quiz-results">
                <c:param name="filterCategoryId" value="${filterCategoryId}"/>
                <c:param name="filterCreatorId" value="${filterCreatorId}"/>
                <c:param name="sortBy" value="categoryName"/>
                <%-- Toggle sort order --%>
                <c:param name="sortOrder" value="${sortBy eq 'categoryName' && currentSortOrder eq 'asc' ? 'desc' : 'asc'}"/>
                <c:param name="page" value="${currentPage}"/> <%-- Stay on current page --%>
            </c:url>
            <a href="${sortUrl}">Category</a>
            <%-- Display sort indicator --%>
            <c:if test="${sortBy eq 'categoryName'}">
                <c:choose>
                    <c:when test="${currentSortOrder eq 'asc'}"> &uarr;</c:when>
                    <c:otherwise> &darr;</c:otherwise>
                </c:choose>
            </c:if>
        </th>

        <%-- User Full Name Header (Sortable) --%>
        <th class="sortable ${sortBy eq 'creatorName' ? 'sorted-' : ''}${sortBy eq 'creatorName' ? currentSortOrder : ''}">
            <c:url var="sortUrl" value="/admin/quiz-results">
                <c:param name="filterCategoryId" value="${filterCategoryId}"/>
                <c:param name="filterCreatorId" value="${filterCreatorId}"/>
                <c:param name="sortBy" value="creatorName"/>
                <%-- Toggle sort order --%>
                <c:param name="sortOrder" value="${sortBy eq 'creatorName' && currentSortOrder eq 'asc' ? 'desc' : 'asc'}"/>
                <c:param name="page" value="${currentPage}"/> <%-- Stay on current page --%>
            </c:url>
            <a href="${sortUrl}">User Full Name</a>
            <%-- Display sort indicator --%>
            <c:if test="${sortBy eq 'creatorName'}">
                <c:choose>
                    <c:when test="${currentSortOrder eq 'asc'}"> &uarr;</c:when>
                    <c:otherwise> &darr;</c:otherwise>
                </c:choose>
            </c:if>
        </th>

        <th>Number of Questions</th>
        <th>Score</th>
        <th>Link</th>
    </tr>
    </thead>
    <tbody>
    <c:choose>
        <c:when test="${not empty quizResults}">
            <c:forEach var="result" items="${quizResults}">
                <%-- Apply relative positioning to the row for absolute positioning of the link --%>
                <tr class="quiz-result-row">
                    <td><fmt:formatDate value="${result.createdAt}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                    <td><c:out value="${result.categoryName}"/></td>
                    <td><c:out value="${result.creatorName}"/></td>
                    <td><c:out value="${result.questions.size()}"/></td>

                    <td><fmt:formatNumber value="${result.correctQuestionCount*100/result.questions.size()}" type="number" maxFractionDigits="1"/>
                    </td>
                    <td><a href="<c:url value='/quiz-result/${result.quizId}'/>" class="row-link">View Details</a></td>
                </tr>
            </c:forEach>
        </c:when>
        <c:otherwise>
            <tr>
                <td colspan="6">No quiz results found.</td>
            </tr>
        </c:otherwise>
    </c:choose>
    </tbody>
</table>

<%-- =============================== --%>
<%-- Pagination Controls           --%>
<%-- =============================== --%>
<div class="pagination">
    <%-- Previous Page Link --%>
    <c:choose>
        <c:when test="${currentPage > 1}">
            <c:url var="prevUrl" value="/admin/quiz-results">
                <c:param name="filterCategoryId" value="${filterCategoryId}"/>
                <c:param name="filterCreatorId" value="${filterCreatorId}"/>
                <c:param name="sortBy" value="${sortBy}"/>
                <c:param name="sortOrder" value="${currentSortOrder}"/>
                <c:param name="page" value="${currentPage - 1}"/>
            </c:url>
            <a href="${prevUrl}" class="pagination-link">&laquo; Previous</a>
        </c:when>
        <c:otherwise>
            <span class="pagination-link disabled">&laquo; Previous</span>
        </c:otherwise>
    </c:choose>

    <%-- Page Number Links --%>
    <c:forEach var="i" begin="1" end="${totalPages}">
        <c:url var="pageUrl" value="/admin/quiz-results">
            <c:param name="filterCategoryId" value="${filterCategoryId}"/>
            <c:param name="filterCreatorId" value="${filterCreatorId}"/>
            <c:param name="sortBy" value="${sortBy}"/>
            <c:param name="sortOrder" value="${currentSortOrder}"/>
            <c:param name="page" value="${i}"/>
        </c:url>
        <c:choose>
            <c:when test="${i == currentPage}">
                <span class="pagination-link active"><c:out value="${i}"/></span>
            </c:when>
            <c:otherwise>
                <a href="${pageUrl}" class="pagination-link"><c:out value="${i}"/></a>
            </c:otherwise>
        </c:choose>
    </c:forEach>

    <%-- Next Page Link --%>
    <c:choose>
        <c:when test="${currentPage < totalPages}">
            <c:url var="nextUrl" value="/admin/quiz-results">
                <c:param name="filterCategoryId" value="${filterCategoryId}"/>
                <c:param name="filterCreatorId" value="${filterCreatorId}"/>
                <c:param name="sortBy" value="${sortBy}"/>
                <c:param name="sortOrder" value="${currentSortOrder}"/>
                <c:param name="page" value="${currentPage + 1}"/>
            </c:url>
            <a href="${nextUrl}" class="pagination-link">Next &raquo;</a>
        </c:when>
        <c:otherwise>
            <span class="pagination-link disabled">Next &raquo;</span>
        </c:otherwise>
    </c:choose>
</div>
<a href="/admin">Back to Dashboard</a>


</body>
</html>