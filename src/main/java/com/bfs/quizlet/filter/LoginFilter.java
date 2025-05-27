package com.bfs.quizlet.filter;

import org.slf4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * One way to perform the security check. Not very convenient or secure.
 * We will learn more about how to do such thing in Spring on the next week
 */

// the filter should apply to all incoming requests,
// including static resources, API endpoints, and JSP files
@WebFilter("/*")
@Component
public class LoginFilter extends OncePerRequestFilter {
    Logger logger = org.slf4j.LoggerFactory.getLogger(LoginFilter.class);

    /**
     * We need to check for each request passed in
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session != null && session.getAttribute("user") != null) {
            filterChain.doFilter(request, response);
        } else {
            // redirect back to the login page if user is not logged in
            response.sendRedirect("/login");
        }
    }

    /**
     * Some requests don't require the user to log in.
     * For example, login/register page
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String method = request.getMethod();
        String path = request.getRequestURI();
//        logger.debug("path = {}, Method: {}", path, method);

        return "/login".equals(path)
                || "/WEB-INF/jsp/login.jsp".equals(path)
                || "/css/login.css".equals(path)
                || "/register".equals(path)
                || "/WEB-INF/jsp/register.jsp".equals(path)
                || "/css/register.css".equals(path)
                || "/css/navbar.css".equals(path)
                || "/error".equals(path)
                || "/WEB-INF/jsp/error.jsp".equals(path)
                || "/css/error.css".equals(path)
                || "/contact".equals(path)
                || "/contact/send".equals(path)
                || "/WEB-INF/jsp/contact.jsp".equals(path)
                || "/css/contact.css".equals(path);
    }
}
