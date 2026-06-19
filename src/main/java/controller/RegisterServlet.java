package controller;

import java.io.IOException;

import dao.UserDAO;
import model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

	protected void doPost(HttpServletRequest request, HttpServletResponse response)

			throws ServletException, IOException {

		String name = request.getParameter("name");

		String email = request.getParameter("email");

		String password = request.getParameter("password");

		User user = new User();

		user.setName(name);
		user.setEmail(email);
		user.setPassword(password);

		UserDAO dao = new UserDAO();

		boolean result = dao.register(user);

		if (result) {

			response.sendRedirect("login.html");

		} else {

			response.getWriter().println("Registration Failed");

		}

	}

}