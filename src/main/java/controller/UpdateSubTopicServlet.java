package controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import dao.StudyPlanDAO;
import dao.SubTopicDAO;
import dao.TopicDAO;

import model.SubTopic;
import model.Topic;

@WebServlet("/updateSubTopic")
public class UpdateSubTopicServlet extends HttpServlet {

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		int id = Integer.parseInt(request.getParameter("id"));

		boolean completed = Boolean.parseBoolean(request.getParameter("completed"));

		SubTopicDAO subDAO = new SubTopicDAO();

		subDAO.updateStatus(id, completed);

		StudyPlanDAO planDAO = new StudyPlanDAO();

		planDAO.updateTaskBySubTopic(id, completed);

		// get updated subtopic

		SubTopic sub = subDAO.getSubTopicById(id);

		// topic progress

		int topicProgress = subDAO.getProgress(sub.getTopicId());

		// subject progress

		TopicDAO topicDAO = new TopicDAO();

		Topic topic = topicDAO.getTopicById(sub.getTopicId());

		int subjectProgress = topicDAO.getSubjectProgress(topic.getSubjectId());

		// return JSON

		response.setContentType("application/json");

		response.getWriter().write(

				"{" + "\"topicProgress\":" + topicProgress + "," + "\"subjectProgress\":" + subjectProgress + "}"

		);

	}

}