<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<html>
<head>
    <title>Quiz Result</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/navbar.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/quiz-result.css">

</head>
    <meta charset="UTF-8">
    <title>Quiz Results</title>
    <%@ include file="/WEB-INF/jsp/includes/navbar.jsp" %>
<body>
<c:if test="${sessionScope.user.userId!=quiz.userId && !sessionScope.user.isAdmin}">
    <c:redirect url="${pageContext.request.contextPath}/home"/>
</c:if>
<div class="container">
    <h1>Quiz Results</h1>

    <div class="quiz-info">
        <p><strong>Quiz Name:</strong> <c:out value="${quiz.name}"/></p>
        <p><strong>User:</strong> <c:out value="${quiz.creatorName}"/></p>
        <p><strong>Start Time:</strong> <c:out value="${quiz.createdAt}"/></p>
        <p><strong>End Time:</strong> <c:out value="${quiz.updatedAt}"/></p>
    </div>

    <div class="overall-result">
        <h2>Overall Result:</h2>
        <c:if test="${quiz.correctQuestionCount >= (0.6*quiz.questions.size())}">
            <p class="passed">Passed!</p>
        </c:if>
        <c:if test="${quiz.correctQuestionCount < (0.6*quiz.questions.size())}">
            <p class="failed">Failed</p>
        </c:if>
        <p>You got <c:out value="${quiz.correctQuestionCount} / ${quiz.questions.size()}"/> questions correct.</p>
    </div>

    <div class="question-results">
        <h2>Question Results:</h2>
        <c:forEach var="question" items="${quiz.questions}" varStatus="loop">
            <div class="question-block <c:if test="${question.isCorrect}">correct</c:if><c:if test="${!question.isCorrect}">incorrect</c:if>">
                <p><strong>Question <c:out value="${loop.count}"/>:</strong> <c:out value="${question.description}"/></p>

                <div class="options">
                    <p>Options:</p>
                    <ul>
                        <c:set var="letters" value="ABCDEFGHIJKLMNOPQRSTUVWXYZ" />
                        <c:forEach var="option" items="${question.choices}" varStatus="optionStatus">
                            <li class="option">
                                <input type="checkbox" disabled
                                       <c:if test="${question.userChoicesIds.contains(option.choiceId)}">checked="checked"</c:if> />

                                <span class="option-text <c:if test="${option.isCorrect}">correct</c:if><c:if test="${!option.isCorrect}">incorrect</c:if>">
                    <%-- Display Letter --%>
                    <c:out value="${fn:substring(letters, optionStatus.index, optionStatus.index + 1)}."/>
                    <c:out value="${option.description}"/>
                </span>
                            </li>
                        </c:forEach>
                    </ul>
                </div>

                <p><strong>Result:</strong>
                    <c:if test="${question.isCorrect}">
                        <span class="correct-result">Correct</span>
                    </c:if>
                    <c:if test="${!question.isCorrect}">
                        <span class="incorrect-result">Incorrect</span>
                    </c:if>
                </p>
            </div>
        </c:forEach>
    </div>

    <div class="navigation">
        <p><a href="/home">Take Another Quiz</a></p> <%-- Adjust the URL to your homepage --%>
    </div>
</div>
</body>

</html>