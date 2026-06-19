package controller;

import java.io.IOException;

import dao.UserDAO;
import model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

	protected void doPost(HttpServletRequest request, HttpServletResponse response)

			throws ServletException, IOException {

		String email = request.getParameter("email");

		String password = request.getParameter("password");

		UserDAO dao = new UserDAO();

		User user = dao.login(email, password);

		if (user != null) {

			HttpSession session = request.getSession();

			session.setAttribute("user", user);

			response.sendRedirect("dashboard.jsp");

		}

		else {

			response.getWriter().println("Invalid Login");

		}

	}

}