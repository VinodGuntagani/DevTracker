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

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String email = request.getParameter("email");
		String password = request.getParameter("password");

		UserDAO dao = new UserDAO();

		User user = dao.getUserByEmail(email);

		if (user != null && BCrypt.checkpw(password, user.getPassword())) {

			// Destroy any existing session
			HttpSession oldSession = request.getSession(false);

			if (oldSession != null) {
				oldSession.invalidate();
			}

			// Create a brand new session
			HttpSession session = request.getSession(true);

			session.setAttribute("user", user);

			// 30 minutes of inactivity
			session.setMaxInactiveInterval(30 * 60);

			response.sendRedirect("dashboard.jsp");

		} else {

			request.setAttribute("error", "Invalid email or password.");
			request.setAttribute("email", email);
			request.getRequestDispatcher("login.jsp").forward(request, response);

		}
	}
}