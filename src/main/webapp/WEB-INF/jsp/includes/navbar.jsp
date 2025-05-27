<%@ page %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<nav class="navbar">
    <div class="navbar-brand">Quizlet</div>
    <div class="nav-container">
        <ul class="nav-list">
            <li class="nav-item" <c:if test="${sessionScope.user==null}">hidden</c:if>
            ><a href="${pageContext.request.contextPath}/home" class="nav-link">Home</a></li>
            <li class="nav-item"><a href="${pageContext.request.contextPath}/contact" class="nav-link">Contact Us</a></li>

            <%-- Conditional display of Login/Register or Logout and Admin Link --%>
            <c:choose>
                <c:when test="${sessionScope.user != null}">
                    <%--  Admin Link  --%>
                    <c:if test="${sessionScope.user.isAdmin}">
                        <li class="nav-item"><a href="${pageContext.request.contextPath}/admin" class="nav-link">Admin</a></li> <%-- Link to your Admin Home page URL --%>
                    </c:if>

                    <li class="nav-item"><a href="${pageContext.request.contextPath}/logout" class="nav-link">Logout</a></li>
                </c:when>
                <c:otherwise>
                    <li class="nav-item"><a href="${pageContext.request.contextPath}/login" class="nav-link">Login</a></li>
                </c:otherwise>
            </c:choose>
        </ul>
    </div>
</nav>