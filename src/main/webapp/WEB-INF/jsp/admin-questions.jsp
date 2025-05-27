<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c"%>
<%@ taglib uri="jakarta.tags.functions" prefix="fn"%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Question Management - Quizlet</title>

    <%-- Include the navbar CSS --%>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/navbar.css">
    <%-- Link to the specific admin CSS --%>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/admin-questions.css">
</head>
<body>

<%-- Include the navbar --%>
<%@ include file="/WEB-INF/jsp/includes/navbar.jsp" %>
<c:if test="${!sessionScope.user.isAdmin}">
    <c:redirect url="${pageContext.request.contextPath}/home"/>
</c:if>
<div class="main-admin-container">
    <h1>Question Management</h1>

    <div class="admin-content-flex">

        <%-- Right Panel: New Category Button and Categories List --%>
        <div class="admin-panel category-panel">
            <h2>Categories</h2>

            <%-- New Category Button (as a form submission) --%>
            <div class="new-category-button-container">
                <form action="${pageContext.request.contextPath}/admin/categories/create" method="post">
                    <button type="submit" class="admin-button new-category-button">New Category</button>
                </form>
            </div>

            <div class="category-list">
                <c:if test="${not empty categories}">
                    <ul>
                        <c:forEach var="category" items="${categories}">
                            <li class="category-item <c:if test="${selectedCategoryId != null
                            && selectedCategoryId == category.categoryId}">active</c:if>">
                                <a href="${pageContext.request.contextPath}/admin/questions?selectedCategoryId=${category.categoryId}&page=${currentPage != null ? currentPage : 1}&size=${pageSize != null ? pageSize : 5}" class="category-link">
                                    <c:out value="${category.name}"/>
                                </a>
                            </li>
                        </c:forEach>
                    </ul>
                </c:if>
                <c:if test="${empty categories}">
                    <p>No categories found.</p>
                </c:if>
            </div>
        </div>

        <%-- Left Panel: Category Rename, Add Question, Questions Table, Pagination --%>
        <div class="admin-panel question-panel">
            <%-- Only show if a category is selected --%>
            <c:if test="${selectedCategoryId != null}">

                <%-- Category Name, Rename Form, AND Status Toggle Button --%>
                <div class="category-rename-section">
                    <h2>Current Category:</h2>

                        <%-- Form to rename the category --%>
                    <form action="${pageContext.request.contextPath}/admin/categories/rename" method="post">
                        <input type="hidden" name="categoryId" value="${selectedCategoryId}"/>
                            <%-- Pass current page and size to return to the same view state after rename --%>
                        <input type="hidden" name="page" value="${currentPage != null ? currentPage : 1}"/>
                        <input type="hidden" name="size" value="${pageSize != null ? pageSize : 5}"/>

                        <input type="text" name="newCategoryName" value="${selectedCategoryName}" required class="category-rename-input"/>
                        <button type="submit" class="admin-button rename-category-button">Rename</button>
                    </form>

                        <%-- === Category Status Toggle Form === --%>
                    <c:if test="${selectedCategory != null}"> <%-- Check if the full category object is available --%>
                        <form action="${pageContext.request.contextPath}/admin/categories/toggle-status" method="post" class="status-toggle-form"> <%-- Added class status-toggle-form --%>
                            <input type="hidden" name="categoryId" value="${selectedCategoryId}"/>
                                <%-- Pass current page and size to return to the same view state after toggle --%>
                            <input type="hidden" name="page" value="${currentPage != null ? currentPage : 1}"/>
                            <input type="hidden" name="size" value="${pageSize != null ? pageSize : 5}"/>

                            <c:choose>
                                <c:when test="${selectedCategory.isActive}">
                                    <button type="submit" class="admin-button suspend-category-button">Suspend Category</button>
                                </c:when>
                                <c:otherwise>
                                    <button type="submit" class="admin-button activate-category-button">Activate Category</button>
                                </c:otherwise>
                            </c:choose>
                        </form>
                    </c:if>
                        <%-- =================================== --%>
                </div>


                <%-- Add New Question Button --%>
                <div class="add-question-button-container">
                        <%-- Link to a separate page for creating a question, passing the category ID --%>
                    <a href="${pageContext.request.contextPath}/admin/questions/create/${selectedCategoryId}" class="admin-button add-question-button">Add New Question</a>
                </div>

                <div class="table-container">
                    <table>
                        <thead>
                        <tr>
                                <%-- Combined header for Question Description and Choices --%>
                            <th>Question Details</th>
                            <th>Status</th>
                            <th>Actions</th>
                        </tr>
                        </thead>
                        <tbody>
                            <%-- Loop through questions (should be the paginated list) --%>
                            <%-- Assuming 'questions' is List<QuestionDTO> with id, description, isActive, List<ChoiceDTO> choices --%>
                        <c:forEach var="question" items="${questions}">
                            <tr>
                                    <%-- === Combined Question Details Cell === --%>
                                <td class="question-details-cell">
                                        <%-- Add Question ID --%>
                                    <strong><c:out value="${question.questionId}"/></strong>.
                                        <%-- Question Description --%>
                                    <c:out value="${question.description}"/>

                                        <%-- Choices Display --%>
                                    <c:if test="${not empty question.choices}">
                                        <%-- Wrap choices list for styling/spacing --%>
                                        <div class="question-choices">
                                            <ul style="list-style: none">
                                                    <%-- Assuming ChoiceDTO has id, description, and isCorrect --%>
                                                <c:set var="letters" value="ABCDEFGHIJKLMNOPQRSTUVWXYZ" />
                                                <c:forEach var="choice" items="${question.choices}" varStatus="choiceStatus">
                                                    <li>
                                                            <%-- Checkbox indicating if this is the correct answer --%>
                                                        <input type="checkbox" disabled
                                                               <c:if test="${choice.isCorrect}">checked="checked"</c:if>/>
                                                            <%-- Letter and Choice Description --%>
                                                        <span class="choice-text">
                                                                 <c:out value="${fn:substring(letters, choiceStatus.index, choiceStatus.index + 1)}."/>
                                                                 <c:out value="${choice.description}"/>
                                                             </span>
                                                    </li>
                                                </c:forEach>
                                            </ul>
                                        </div>
                                    </c:if>
                                    <c:if test="${empty question.choices}">
                                        <p class="no-choices-message">No choices yet.</p> <%-- Added class --%>
                                    </c:if>
                                </td>
                                    <%-- === End Combined Cell === --%>


                                    <%-- Question Status (Keep existing) --%>
                                <td>
                                    <c:if test="${question.isActive}">
                                        <span class="status-active">Active</span>
                                    </c:if>
                                    <c:if test="${!question.isActive}">
                                        <span class="status-suspended">Suspended</span>
                                    </c:if>
                                </td>

                                    <%-- Question Actions (Keep existing) --%>
                                <td class="actions-cell">
                                        <%-- Edit Button (Link to edit page) --%>
                                    <a href="${pageContext.request.contextPath}/admin/question-edit/${question.questionId}" class="action-button edit-button">Edit</a>

                                        <%-- Toggle Status Form --%>
                                        <%-- Pass categoryId and current page/size for redirect --%>
                                    <form action="${pageContext.request.contextPath}/admin/questions/toggle-status" method="post" style="display:inline;">
                                        <input type="hidden" name="questionId" value="${question.questionId}"/>
                                        <input type="hidden" name="categoryId" value="${selectedCategoryId}"/>
                                        <input type="hidden" name="page" value="${currentPage != null ? currentPage : 1}"/>
                                        <input type="hidden" name="size" value="${pageSize != null ? pageSize : 5}"/>

                                        <c:choose>
                                            <c:when test="${question.isActive}">
                                                <button type="submit" class="action-button suspend-button">Suspend</button>
                                            </c:when>
                                            <c:otherwise>
                                                <button type="submit" class="action-button activate-button">Activate</button>
                                            </c:otherwise>
                                        </c:choose>
                                    </form>
                                </td>
                            </tr>
                        </c:forEach>
                            <%-- Message if no questions found for this category/page --%>
                        <c:if test="${empty questions}">
                            <tr>
                                <td colspan="3" style="text-align: center;">
                                    <c:choose>
                                        <c:when test="${currentPage != null && currentPage > 1}">
                                            No questions found on this page.
                                        </c:when>
                                        <c:otherwise>
                                            No questions found for this category.
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                            </tr>
                        </c:if>
                        </tbody>
                    </table>
                </div>

                <%-- Pagination Controls (Reuse logic from User Management) --%>
                <c:if test="${totalPages > 1}">
                    <div class="pagination-controls">
                        <c:choose>
                            <c:when test="${currentPage > 1}">
                                <%-- Pass selectedCategoryId in pagination links --%>
                                <a href="${pageContext.request.contextPath}/admin/questions?categoryId=${selectedCategoryId}&page=${currentPage - 1}&size=${pageSize != null ? pageSize : 5}" class="pagination-link">Previous</a>
                            </c:when>
                            <c:otherwise>
                                <span class="pagination-link disabled">Previous</span>
                            </c:otherwise>
                        </c:choose>

                        <c:forEach var="i" begin="1" end="${totalPages}">
                            <c:choose>
                                <c:when test="${i == currentPage}">
                                    <span class="pagination-link active"><c:out value="${i}"/></span>
                                </c:when>
                                <c:otherwise>
                                    <%-- Pass selectedCategoryId in pagination links --%>
                                    <a href="${pageContext.request.contextPath}/admin/questions?categoryId=${selectedCategoryId}&page=${i}&size=${pageSize != null ? pageSize : 5}" class="pagination-link"><c:out value="${i}"/></a>
                                </c:otherwise>
                            </c:choose>
                        </c:forEach>

                        <c:choose>
                            <c:when test="${currentPage < totalPages}">
                                <%-- Pass selectedCategoryId in pagination links --%>
                                <a href="${pageContext.request.contextPath}/admin/questions?categoryId=${selectedCategoryId}&page=${currentPage + 1}&size=${pageSize != null ? pageSize : 5}" class="pagination-link">Next</a>
                            </c:when>
                            <c:otherwise>
                                <span class="pagination-link disabled">Next</span>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </c:if>
                <%-- End Pagination Controls --%>


            </c:if> <%-- End c:if selectedCategoryId != null --%>

            <%-- Message if no category is selected --%>
            <c:if test="${selectedCategoryId == null}">
                <div class="select-category-message admin-panel">
                    <p>Please select a category from the right panel to view and manage questions.</p>
                </div>
            </c:if>

        </div> <%-- End admin-panel question-panel --%>

    </div> <%-- End admin-content-flex --%>

</div> <%-- End main-admin-container --%>

</body>
</html>