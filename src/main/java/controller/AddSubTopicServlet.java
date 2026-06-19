package controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import dao.SubTopicDAO;
import dao.StudyPlanDAO;
import model.SubTopic;

@WebServlet("/addSubTopic")
public class AddSubTopicServlet extends HttpServlet {

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		int topicId = Integer.parseInt(request.getParameter("topicId"));

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

		SubTopic subTopic = new SubTopic();

		subTopic.setTopicId(topicId);

		subTopic.setName(name);

		subTopic.setDifficulty(difficulty);

		subTopic.setEstimatedMinutes(minutes);

		subTopic.setWeight(weight);

		SubTopicDAO dao = new SubTopicDAO();

		int subtopicId = dao.addSubTopic(subTopic);

		StudyPlanDAO planDAO = new StudyPlanDAO();

		planDAO.addNewSubtopicToSchedule(roadmapId, subtopicId, weight);

		response.sendRedirect("openAIRoadmap?id=" + roadmapId);

	}

}