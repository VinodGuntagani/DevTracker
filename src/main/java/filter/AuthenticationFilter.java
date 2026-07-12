package filter;

import java.io.IOException;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.*;

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
        System.out.println("AuthenticationFilter: " + req.getRequestURI());
        // Public resources
        if (path.equals("/login")
                || path.equals("/register")
                || path.equals("/login.html")
                || path.equals("/register.html")
                || path.startsWith("/css/")
                || path.startsWith("/js/")
                || path.startsWith("/images/")
                || path.startsWith("/includes/")) {

            chain.doFilter(request, response);
            return;
        }

        HttpSession session = req.getSession(false);

        if (session != null && session.getAttribute("user") != null) {

            chain.doFilter(request, response);

        } else {

            res.sendRedirect(req.getContextPath() + "/login.html");

        }
    }
}