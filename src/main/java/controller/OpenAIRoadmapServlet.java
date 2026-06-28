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
		System.out.println("STEP 1");
		int roadmapId = Integer.parseInt(request.getParameter("id"));

		RoadmapDAO roadmapDAO = new RoadmapDAO();

		SubjectDAO subjectDAO = new SubjectDAO();

		TopicDAO topicDAO = new TopicDAO();

		SubTopicDAO subDAO = new SubTopicDAO();

		Roadmap roadmap = roadmapDAO.getRoadmapById(roadmapId);
		System.out.println("STEP 2");

		List<Subject> subjects = subjectDAO.getSubjectsByRoadmap(roadmapId);
		System.out.println("STEP 3");
		for (Subject s : subjects) {
			System.out.println("Subject: " + s.getName());
			int subjectProgress = topicDAO.getSubjectProgress(s.getId());

			s.setProgress(subjectProgress);

			List<Topic> topics = topicDAO.getTopicsBySubject(s.getId());

			for (Topic t : topics) {
				System.out.println("Topic: " + t.getName());
				int topicProgress = subDAO.getProgress(t.getId());

				t.setProgress(topicProgress);

				List<SubTopic> subs = subDAO.getSubTopicsByTopic(t.getId());
				System.out.println("subTopic: " + t.getName());
				t.setSubtopics(subs);

			}

			s.setTopics(topics);

		}
		System.out.println("STEP END");
		roadmap.setSubjects(subjects);

		request.setAttribute("roadmap", roadmap);

		request.getRequestDispatcher("ai-roadmap-view.jsp").forward(request, response);

	}

}