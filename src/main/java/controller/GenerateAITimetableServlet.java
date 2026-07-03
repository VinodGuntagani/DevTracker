package controller;

import java.io.IOException;
import java.time.temporal.ChronoUnit;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import dao.RoadmapDAO;

import model.Roadmap;
import model.User;

import service.AITimetableGenerator;

@WebServlet("/generateAITimetable")
public class GenerateAITimetableServlet extends HttpServlet {

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		System.out.println("\n========================================");
		System.out.println("🌐 /generateAITimetable HIT");
		System.out.println("========================================");
		HttpSession session = request.getSession();

		User user = (User) session.getAttribute("user");

		if (user == null) {

			response.sendRedirect("login.jsp");
			return;

		}

		int userId = user.getId();

		int roadmapId = Integer.parseInt(request.getParameter("roadmapId"));

		RoadmapDAO roadmapDAO = new RoadmapDAO();

		Roadmap roadmap = roadmapDAO.getRoadmapById(roadmapId);

		long totalDays = ChronoUnit.DAYS.between(

				roadmap.getStartDate().toLocalDate(),

				roadmap.getTargetDate().toLocalDate()

		);

		int days = (int) totalDays + 1;

		AITimetableGenerator generator = new AITimetableGenerator();

		generator.generate(userId, roadmapId, days);

		response.sendRedirect("dashboard.jsp");

	}

}