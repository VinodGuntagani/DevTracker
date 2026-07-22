package controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import dao.AIJobDAO;
import model.AIJob;

@WebServlet("/aiJobProgress")
public class AIJobProgressServlet extends HttpServlet {

	private final AIJobDAO dao = new AIJobDAO();

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("application/json");

		int jobId = Integer.parseInt(request.getParameter("jobId"));

		AIJob job = dao.getJob(jobId);

		response.getWriter().write("""
				{
				    "status":"%s",
				    "completed":%d,
				    "failed":%d,
				    "total":%d
				}
				""".formatted(job.getStatus(), job.getCompletedTasks(), job.getFailedTasks(), job.getTotalTasks()));
	}

}