<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<html>
<head>
    <title>Home - Quizlet</title>

    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/navbar.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/home.css">
</head>

<%@ include file="/WEB-INF/jsp/includes/navbar.jsp" %>
<body>
<div class="main-content-container">
    <%-- Left Part (Main Content - Recent Results & Stats) --%>
    <div class="left-panel">
        <h2>Recent Quiz Results</h2>

        <%-- Table for Recent Results --%>
        <div class="results-table-container">
            <table>
                <thead>
                <tr>
                    <th>Start Time</th>
                    <th>Category</th>
                    <th>Name</th>
                    <th>Link</th>
                </tr>
                </thead>
                <tbody>

                <c:forEach var="quiz" items="${quizzes}">
                    <tr>
                        <td><c:out value="${quiz.createdAt}"/> </td>
                        <td><c:out value="${quiz.categoryName}"/></td>
                        <td><c:out value="${quiz.name}"/></td>
                        <td>
                            <c:if test="${quiz.isActive}">
                                <a href="${pageContext.request.contextPath}/quiz/${quiz.quizId}/0" class="status-link status-continue">Continue</a>
                            </c:if>
                            <c:if test="${!quiz.isActive}">
                                <a href="${pageContext.request.contextPath}/quiz-result/${quiz.quizId}" class="status-link status-view">View</a>
                            </c:if>
                        </td>
                    </tr>
                </c:forEach>


                <%-- Placeholder rows if no results are available --%>
                <c:if test="${empty quizzes}">
                    <tr>
                        <td colspan="4" style="text-align: center;">No recent quiz results found.</td>
                    </tr>
                </c:if>
                </tbody>
            </table>
            <c:if test="${!empty quizzes}">
<%--                button not responding now--%>
                <a href="${pageContext.request.contextPath}/home" class="more">MORE</a>
            </c:if>
        </div>

        <h2>Quiz Statistics</h2>
        <div class="stats-container">
<%--            todo: quiz stats--%>
            <h3 style="color:green">Coming Soon</h3>
        </div>
    </div>

    <%-- Right Part (New Quiz Section) --%>
    <div class="right-panel">
        <h2>Start a New Quiz</h2>

        <form method="post" action="${pageContext.request.contextPath}/quiz/new">
            <div class="category-selection">
                <h3>Select Category:</h3>
                <c:forEach var="category" items="${categories}">
                    <c:if test="${category.isActive==true}">
                        <div class="radio-option">
                            <input type="radio" id="category${category.categoryId}" name="categoryId"
                                   value="${category.categoryId}" required>
                            <label for="category${category.categoryId}"><c:out value="${category.name}"/></label>
                        </div>
                    </c:if>
                </c:forEach>
                <%-- Placeholder if no categories are available --%>
                <c:if test="${empty categories}">
                    <p>No quiz categories available.</p>
                </c:if>
            </div>
            <button type="submit" class="start-quiz-button">Start Quiz</button>
        </form>
    </div>
</div>

</body>
</html>
