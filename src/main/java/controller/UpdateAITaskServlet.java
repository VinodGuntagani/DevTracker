package controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import dao.AITimetableDAO;

@WebServlet("/updateAITask")
public class UpdateAITaskServlet extends HttpServlet {

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		int taskId = Integer.parseInt(request.getParameter("taskId"));

		int roadmapId = Integer.parseInt(request.getParameter("roadmapId"));

		boolean completed = request.getParameter("completed") != null;

		AITimetableDAO dao = new AITimetableDAO();

		dao.updateCompleted(taskId, completed);

		response.sendRedirect("viewAITimetable?roadmapId=" + roadmapId);

	}

}