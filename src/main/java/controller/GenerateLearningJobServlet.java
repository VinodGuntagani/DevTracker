package controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import model.User;
import service.ai.AIJobService;

@WebServlet("/generateLearningJob")
public class GenerateLearningJobServlet extends HttpServlet {

	private final AIJobService jobService = new AIJobService();

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("application/json");

		try {

			User user = (User) request.getSession().getAttribute("user");

			if (user == null) {

				response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

				response.getWriter().write("""
						{
						    "success":false,
						    "message":"User not logged in."
						}
						""");

				return;
			}

			int userId = user.getId();

			int roadmapId = Integer.parseInt(request.getParameter("roadmapId"));

			int jobId = jobService.createNotesJob(userId, roadmapId);

			response.getWriter().write("""
					{
					    "success":true,
					    "jobId":%d
					}
					""".formatted(jobId));

		} catch (Exception e) {

			e.printStackTrace();

			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

			response.getWriter().write("""
					{
					    "success":false,
					    "message":"Failed to create AI learning job."
					}
					""");
		}
	}
}