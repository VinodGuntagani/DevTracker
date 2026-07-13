package controller;

import java.io.IOException;
import java.sql.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import dao.RoadmapDAO;
import model.Roadmap;
import model.User;

@WebServlet("/createRoadmap")
public class CreateRoadmapServlet extends HttpServlet {

	protected void doPost(HttpServletRequest request, HttpServletResponse response)

			throws ServletException, IOException {

		// get logged in user

		HttpSession session = request.getSession(false);

		if (session == null || session.getAttribute("user") == null) {

			response.sendRedirect("login.jsp");

			return;

		}

		User user = (User) session.getAttribute("user");

		// form data

		String title = request.getParameter("title");

		String description = request.getParameter("description");

		Date startDate = Date.valueOf(request.getParameter("startDate"));

		Date targetDate = Date.valueOf(request.getParameter("targetDate"));

		// create object

		Roadmap roadmap = new Roadmap();

		roadmap.setUserId(user.getId());

		roadmap.setTitle(title);

		roadmap.setDescription(description);

		roadmap.setStartDate(startDate);

		roadmap.setTargetDate(targetDate);

		// save

		RoadmapDAO dao = new RoadmapDAO();

		boolean result = dao.addRoadmap(roadmap);

		if (result) {

			response.sendRedirect("dashboard.jsp");

		} else {

			response.getWriter().println("Roadmap creation failed");

		}

	}

}