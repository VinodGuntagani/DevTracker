package controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import dao.SubTopicDAO;
import dao.StudyPlanDAO;

@WebServlet("/updateDailyTask")
public class UpdateDailyTaskServlet extends HttpServlet {

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		int id = Integer.parseInt(request.getParameter("id"));

		int roadmapId = Integer.parseInt(request.getParameter("roadmapId"));

		int subtopicId = Integer.parseInt(request.getParameter("subtopicId"));

		String status = request.getParameter("status");

		if (status == null) {

			status = "NOT_STARTED";

		}

		boolean completed = status.equals("COMPLETED");

		StudyPlanDAO dao = new StudyPlanDAO();

		// old system

		dao.updateTaskStatus(id, completed);

		// new system

		dao.updateTaskState(id, status);

		SubTopicDAO subDao = new SubTopicDAO();

		subDao.updateStatus(subtopicId, completed);

		response.sendRedirect("schedule.jsp?roadmapId=" + roadmapId);

	}

}