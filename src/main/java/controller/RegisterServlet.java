package controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import org.mindrot.jbcrypt.BCrypt;

import dao.UserDAO;
import model.User;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String name = request.getParameter("name");

		String email = request.getParameter("email");

		String password = request.getParameter("password");

		// Hash the password
		String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

		User user = new User();

		user.setName(name);
		user.setEmail(email);
		user.setPassword(hashedPassword);

		UserDAO dao = new UserDAO();

		boolean result = dao.register(user);

		if (result) {

			response.sendRedirect("login.jsp");

		} else {

			request.setAttribute("error", "Registration failed. Please try again.");
			request.setAttribute("name", name);
			request.setAttribute("email", email);
			request.getRequestDispatcher("register.jsp").forward(request, response);

		}
	}
}