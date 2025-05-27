<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<html>
<head>
    <title>Quiz</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/navbar.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/quiz.css">

</head>
<%@ include file="/WEB-INF/jsp/includes/navbar.jsp" %>

<body>
<c:if test="${sessionScope.user.userId!=quiz.userId}">
    <c:redirect url="${pageContext.request.contextPath}/home"/>
</c:if>
<div class="container">
        <span id="timer" class="timer">05:00</span>
    <div class="question-panel">
        <h2>Question <c:out value="${currentQuestionIndex + 1}"/> of <c:out value="${quiz.getQuestions().size()}"/></h2>

        <div class="question-text">
            <p><c:out value="${quiz.getQuestions().get(currentQuestionIndex).description}"/></p>
        </div>

        <form action="/quiz/${quiz.getQuizId()}" method="post">
            <%-- Hidden input to keep track of the current question index or ID --%>
            <input type="hidden" name="questionIndex" value="${currentQuestionIndex}">
            <input type="hidden" name="questionId" value="${quiz.getQuestions().get(currentQuestionIndex).getQuestionId()}">

            <div class="answer-options">
                <c:forEach var="choice" items="${quiz.getQuestions().get(currentQuestionIndex).getChoices()}" varStatus="loop">
                    <div>
                        <input type='checkbox'
                               id="choice_${currentQuestionIndex}_${loop.index}"
                               name="userChoices"
                               value="${choice.getChoiceId()}"
                               <c:if test="${!quiz.getIsActive()}">disabled</c:if>
                               <c:if test="${quiz.getQuestions().get(currentQuestionIndex).getUserChoicesIds().contains(choice.getChoiceId())}">checked</c:if>
                        />
                        <label for="choice_${currentQuestionIndex}_${loop.index}">
                            <c:out value="${choice.description}"/>
                        </label>
                    </div>
                </c:forEach>
            </div>

            <div class="navigation-buttons" <c:if test="${!quiz.getIsActive()}">hidden</c:if>>
                <c:choose>
                    <c:when test="${currentQuestionIndex < quiz.getQuestions().size() - 1}">
                        <button type="submit" >Save</button>
                    </c:when>
                    <c:otherwise>
                        <button type="submit" >Submit</button>
                    </c:otherwise>
                </c:choose>
            </div>
        </form>
    </div>

    <div class="navigation-panel">
        <h3>Questions</h3>
        <ul class="question-nav-list">
            <c:forEach begin="0" end="${quiz.getQuestions().size() - 1}" var="i">
                <li>
                        <%-- Get the current question object from the list --%>
                    <c:set var="currentQuestion" value="${quiz.getQuestions().get(i)}"/>

                        <%-- Build the CSS class string conditionally --%>
                    <c:set var="linkClasses" value=""/>
                    <c:if test="${i == currentQuestionIndex}">
                        <c:set var="linkClasses" value="${linkClasses} active"/>
                    </c:if>
                        <%-- Check if the user has selected choices for this question --%>
                    <c:if test="${not empty currentQuestion.userChoicesIds}">
                        <c:set var="linkClasses" value="${linkClasses} answered-question"/>
                    </c:if>
                        <%-- Trim leading space if only one class was added --%>
                    <c:set var="linkClasses" value="${linkClasses.trim()}"/>


                    <a href="${pageContext.request.contextPath}/quiz/${quiz.getQuizId()}/${i}"
                       class="${linkClasses}">
                        <c:out value="${i + 1}"/>
                    </a>
                </li>
            </c:forEach>
        </ul>
    </div>
</div>
</body>

<script>
    window.onload = function () {
        const totalTimeKey = "quiz_timer_seconds";
        const defaultTime = ${timeLeft}; // 5 minutes in seconds
        const timerElement = document.getElementById("timer");
        const form = document.querySelector("form");

        let totalTime = sessionStorage.getItem(totalTimeKey);
        totalTime = totalTime ? parseInt(totalTime, 10) : defaultTime;

        function updateTimer() {
            if (totalTime > 0) {
                totalTime--;
                sessionStorage.setItem(totalTimeKey, totalTime);
            } else {
                alert("Time's up!");
                sessionStorage.removeItem(totalTimeKey);
                form.submit();
                return;
            }

            const minutes = Math.floor(totalTime / 60);
            const seconds = totalTime % 60;
            timerElement.textContent =
                minutes.toString().padStart(2, '0') + ":" + seconds.toString().padStart(2, '0');
        }

        updateTimer();
        setInterval(updateTimer, 1000);
    };
</script>
</html>
