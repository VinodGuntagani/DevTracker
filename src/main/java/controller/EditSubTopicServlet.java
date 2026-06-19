package controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import dao.SubTopicDAO;

@WebServlet("/editSubTopic")
public class EditSubTopicServlet extends HttpServlet {

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		int id = Integer.parseInt(request.getParameter("subtopicId"));

		int roadmapId = Integer.parseInt(request.getParameter("roadmapId"));

		String name = request.getParameter("name");

		String difficulty = request.getParameter("difficulty");

		int minutes = Integer.parseInt(request.getParameter("minutes"));

		int difficultyValue = 2;

		if (difficulty.equals("Easy")) {
			difficultyValue = 1;
		} else if (difficulty.equals("Hard")) {
			difficultyValue = 3;
		}

		int weight = difficultyValue * minutes;

		SubTopicDAO dao = new SubTopicDAO();

		dao.updateName(id, name);

		dao.updateAIAnalysis(id, difficulty, minutes, weight);

		response.sendRedirect("openAIRoadmap?id=" + roadmapId);

	}

}