package controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import dao.RoadmapDAO;
import dao.SubjectDAO;

import model.Roadmap;
import model.Subject;

import dao.TopicDAO;
import dao.SubTopicDAO;

import model.Topic;
import model.SubTopic;

@WebServlet("/openAIRoadmap")
public class OpenAIRoadmapServlet extends HttpServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		int roadmapId = Integer.parseInt(request.getParameter("id"));

		RoadmapDAO roadmapDAO = new RoadmapDAO();

		SubjectDAO subjectDAO = new SubjectDAO();

		TopicDAO topicDAO = new TopicDAO();

		SubTopicDAO subDAO = new SubTopicDAO();

		Roadmap roadmap = roadmapDAO.getRoadmapById(roadmapId);

		List<Subject> subjects = subjectDAO.getSubjectsByRoadmap(roadmapId);

		for (Subject s : subjects) {

			List<Topic> topics = topicDAO.getTopicsBySubject(s.getId());

			for (Topic t : topics) {

				List<SubTopic> subs = subDAO.getSubTopicsByTopic(t.getId());

				t.setSubtopics(subs);

			}

			s.setTopics(topics);

		}

		roadmap.setSubjects(subjects);

		request.setAttribute("roadmap", roadmap);

		request.getRequestDispatcher("ai-roadmap-view.jsp").forward(request, response);

	}

}