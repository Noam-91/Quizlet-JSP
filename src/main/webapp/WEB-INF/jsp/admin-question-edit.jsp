<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Edit Question</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/admin-question-edit.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/navbar.css">

</head>
<body>
<%@ include file="/WEB-INF/jsp/includes/navbar.jsp" %>
<c:if test="${!sessionScope.user.isAdmin}">
    <c:redirect url="${pageContext.request.contextPath}/home"/>
</c:if>
<div class="container">
    <h1>Edit Question</h1>

    <form method="POST" action="${pageContext.request.contextPath}/admin/question-edit/${question.questionId}/update">
        <input type="hidden" name="questionId" value="${question.questionId}"/>
        <input type="hidden" name="createdAt" value="${question.createdAt}"/>
        <input type="hidden" name="createdBy" value="${question.createdBy}"/>

        <%-- Question Description --%>
        <div class="form-group">
            <label for="description">Question Description:</label><br>
            <textarea name="description" id="description" rows="4" cols="60" class="form-control">${question.description}</textarea>
        </div>

        <h2>Choices</h2>
        <div id="choicesContainer">
            <%-- Iterate over the existing choices using JSTL --%>
            <c:forEach var="choice" items="${question.choices}" varStatus="loopStatus">
                <div class="choice-item">
                        <%-- Hidden field for choiceId. Essential for the backend to identify existing choices --%>
                    <input type="hidden" name="choices[${loopStatus.index}].choiceId" value="${choice.choiceId}"/>
                    <input type="hidden" name="choices[${loopStatus.index}].questionId" value="${choice.questionId}"/>
                    <input type="hidden" name="choices[${loopStatus.index}].createdAt" value="${choice.createdAt}"/>
                    <input type="hidden" name="choices[${loopStatus.index}].createdBy" value="${choice.createdBy}"/>



                        <%-- Choice Description --%>
                    <div class="form-group inline-group">
                        <label>Choice ${loopStatus.count}:</label>
                        <input type="text" name="choices[${loopStatus.index}].description" value="${choice.description}" size="50" class="form-control inline"/>
                    </div>

                        <%-- isCorrect checkbox --%>
                    <div class="form-group inline-group checkbox-group">
                            <%-- Checkbox for 'true' value when checked --%>
                        <input type="checkbox" name="choices[${loopStatus.index}].isCorrect" value="true" id="choices${loopStatus.index}.isCorrect" ${choice.isCorrect ? 'checked' : ''}/>
                        <label for="choices${loopStatus.index}.isCorrect">Correct Answer</label>
                            <%-- Potential error message display --%>
                    </div>

                        <%-- isActive checkbox (assuming you want to allow deactivating choices) --%>
                    <div class="form-group inline-group checkbox-group">
                            <%-- Checkbox for 'true' value when checked --%>
                        <input type="checkbox" name="choices[${loopStatus.index}].isActive" value="true" id="choices${loopStatus.index}.isActive" ${choice.isActive ? 'checked' : ''}/>
                        <label for="choices${loopStatus.index}.isActive">Active</label>
                            <%-- <c:if test="${not empty errors['choices[' + loopStatus.index + '].isActive']}"><span class="error-message">${errors['choices[' + loopStatus.index + '].isActive']}</span></c:if> --%>
                    </div>
                </div>
            </c:forEach>
                <a href="${pageContext.request.contextPath}/admin/question-edit/${question.questionId}/add-choice"
                class="anchor-as-button">
                    Add a Choice
                </a>

        </div>

        <br>

        <%-- Submit Button --%>
        <div class="form-group">
            <button type="submit" class="btn btn-primary">Save Question</button>
        </div>

    </form>
</div>
<a href="/admin/questions">Back to Question List</a>
</body>
</html>
