<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>User Management - Quizlet</title>

    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/navbar.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/admin-users.css">
</head>
<body>

<%@ include file="/WEB-INF/jsp/includes/navbar.jsp" %>
<c:if test="${!sessionScope.user.isAdmin}">
    <c:redirect url="${pageContext.request.contextPath}/home"/>
</c:if>
<div class="main-admin-container">
    <h1>User Management</h1>

    <div class="admin-section">
        <h2>User List</h2>

        <div class="table-container">
            <table>
                <thead>
                <tr>
                    <th>Full Name</th>
                    <th>Role</th>
                    <th>Email</th>
                    <th>Status</th>
                    <th>Action</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="user" items="${users}">
                    <tr>
                        <td><c:out value="${user.firstname} ${user.lastname}"/></td>
                        <td>
                            <c:choose>
                                <c:when test="${user.isAdmin}">Admin</c:when>
                                <c:otherwise>Client</c:otherwise>
                            </c:choose>
                        </td>
                        <td><c:out value="${user.email}"/></td>
                        <td>
                            <c:if test="${user.isActive}">
                                <span class="status-active">Active</span>
                            </c:if>
                            <c:if test="${!user.isActive}">
                                <span class="status-suspended">Suspended</span>
                            </c:if>
                        </td>
                        <td>

                            <form action="${pageContext.request.contextPath}/admin/users/toggle-status" method="post" style="display:inline;">
                                <input type="hidden" name="userId" value="${user.userId}"/>
                                <input type="hidden" name="page" value="${currentPage}"/> <%-- Pass current page --%>
                                <input type="hidden" name="size" value="${pageSize}"/> <%-- Pass current size --%>

                                <c:choose>
                                    <c:when test="${user.isActive}">
                                        <button type="submit"
                                                class="action-button suspend-button"
                                                <c:if test="${user.userId == sessionScope.user.userId}">disabled</c:if>
                                        >Suspend</button>
                                    </c:when>
                                    <c:otherwise>
                                        <button type="submit"
                                                class="action-button activate-button"
                                                <c:if test="${user.userId == sessionScope.user.userId}">disabled</c:if>
                                        >Activate</button>
                                    </c:otherwise>
                                </c:choose>
                            </form>
                        </td>
                    </tr>
                </c:forEach>
                <c:if test="${empty users}">
                    <tr>
                        <td colspan="4" style="text-align: center;">No users found for this page.</td>
                    </tr>
                </c:if>
                <c:if test="${empty users and (totalPages == 0 || totalPages == 1)}">
                    <tr>
                        <td colspan="4" style="text-align: center;">No users found in the system.</td>
                    </tr>
                </c:if>
                </tbody>
            </table>
        </div>

        <%-- Pagination Controls --%>
        <%-- Only display if there is more than one page --%>
        <c:if test="${totalPages > 1}">
            <div class="pagination-controls">
                    <%-- Previous Page Link --%>
                <c:choose>
                    <c:when test="${currentPage > 1}">
                        <a href="${pageContext.request.contextPath}/admin/users?page=${currentPage - 1}&size=${pageSize}" class="pagination-link">Previous</a>
                    </c:when>
                    <c:otherwise>
                        <span class="pagination-link disabled">Previous</span>
                    </c:otherwise>
                </c:choose>

                    <%-- Page Numbers (Simple loop for demonstration) --%>
                    <%-- In a real app, you might show a limited range like 1... 5 6 [7] 8 9 ... TotalPages --%>
                <c:forEach var="i" begin="1" end="${totalPages}">
                    <c:choose>
                        <c:when test="${i == currentPage}">
                            <span class="pagination-link active"><c:out value="${i}"/></span>
                        </c:when>
                        <c:otherwise>
                            <a href="${pageContext.request.contextPath}/admin/users?page=${i}&size=${pageSize}" class="pagination-link"><c:out value="${i}"/></a>
                        </c:otherwise>
                    </c:choose>
                </c:forEach>

                    <%-- Next Page Link --%>
                <c:choose>
                    <c:when test="${currentPage < totalPages}">
                        <a href="${pageContext.request.contextPath}/admin/users?page=${currentPage + 1}&size=${pageSize}" class="pagination-link">Next</a>
                    </c:when>
                    <c:otherwise>
                        <span class="pagination-link disabled">Next</span>
                    </c:otherwise>
                </c:choose>
            </div>
        </c:if>
        <%-- End Pagination Controls --%>

    </div> <%-- End admin-section --%>

</div> <%-- End main-admin-container --%>

</body>
</html>