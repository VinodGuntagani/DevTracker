package controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import org.mindrot.jbcrypt.BCrypt;

import dao.UserDAO;
import model.User;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        String email = request.getParameter("email");
        String password = request.getParameter("password");

        UserDAO dao = new UserDAO();

        User user = dao.getUserByEmail(email);

        if (user != null &&
            BCrypt.checkpw(password, user.getPassword())) {

            HttpSession session = request.getSession();

            session.setAttribute("user", user);

            response.sendRedirect("dashboard.jsp");

        } else {

            response.getWriter().println("Invalid Login");

        }
    }
}