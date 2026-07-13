package filter;

import java.io.IOException;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.*;

import model.User;

@WebFilter("/*")
public class AuthenticationFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        String path = req.getRequestURI().substring(req.getContextPath().length());
        HttpSession session = req.getSession(false);

        User user = null;

        if (session != null) {
            user = (User) session.getAttribute("user");
        }

        System.out.println(
            "Path: " + path +
            " | Session: " + (session != null) +
            " | User: " + (user != null ? user.getName() : "NULL")
        );
        // Public resources
        if (path.equals("/login")
                || path.equals("/register")
                || path.equals("/login.jsp")
                || path.equals("/register.jsp")
                || path.startsWith("/css/")
                || path.startsWith("/js/")
                || path.startsWith("/images/")
                || path.startsWith("/includes/")) {

            chain.doFilter(request, response);
            return;
        }


        if (session != null && session.getAttribute("user") != null) {

            chain.doFilter(request, response);

        } else {

            res.sendRedirect(req.getContextPath() + "/login.jsp");

        }
    }
}