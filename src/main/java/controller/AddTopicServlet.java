package controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import dao.TopicDAO;
import model.Topic;

@WebServlet("/addTopic")
public class AddTopicServlet extends HttpServlet {

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		int subjectId = Integer.parseInt(request.getParameter("subjectId"));

		int roadmapId = Integer.parseInt(request.getParameter("roadmapId"));

		String name = request.getParameter("name");

		Topic topic = new Topic();

		topic.setSubjectId(subjectId);
		topic.setName(name);

		TopicDAO dao = new TopicDAO();

		dao.addTopic(topic);

		response.sendRedirect("openAIRoadmap?id=" + roadmapId);

	}

}