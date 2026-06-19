package controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import dao.StudyPlanDAO;

import dao.SubTopicDAO;

@WebServlet("/updateSubTopic")
public class UpdateSubTopicServlet extends HttpServlet {

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		int id = Integer.parseInt(request.getParameter("id"));

		int topicId = Integer.parseInt(request.getParameter("topicId"));

		int subjectId = Integer.parseInt(request.getParameter("subjectId"));

		int roadmapId = Integer.parseInt(request.getParameter("roadmapId"));

		boolean completed = request.getParameter("completed") != null;

		SubTopicDAO dao = new SubTopicDAO();

		dao.updateStatus(id, completed);
		StudyPlanDAO planDAO = new StudyPlanDAO();

		planDAO.updateTaskBySubTopic(id, completed);

		response.sendRedirect(
				"subtopics.jsp?topicId=" + topicId + "&subjectId=" + subjectId + "&roadmapId=" + roadmapId);

	}

}