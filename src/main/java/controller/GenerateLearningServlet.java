package controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import dao.SubTopicDAO;
import dao.LearningResourceDAO;

import model.SubTopic;
import model.LearningResource;

import service.GeminiService;
import service.YouTubeService;

@WebServlet("/generateLearning")
public class GenerateLearningServlet extends HttpServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		try {

			int subtopicId = Integer.parseInt(request.getParameter("subtopicId"));

			SubTopicDAO subDAO = new SubTopicDAO();

			SubTopic sub = subDAO.getSubTopicById(subtopicId);

			GeminiService gemini = new GeminiService();

			// =========================
			// AI LEARNING CACHE
			// =========================

			if (sub.getAiLearning() == null || sub.getAiLearning().trim().isEmpty()) {

				String learning = gemini.generateLearning(sub.getName());

				if (learning != null) {

					learning = learning.replace("```html", "").replace("```", "").trim();

					subDAO.updateAILearning(subtopicId, learning);

					sub.setAiLearning(learning);

				}

			}

			// =========================
			// AI SEARCH KEYWORDS CACHE
			// =========================

			if (sub.getAiKeywords() == null || sub.getAiKeywords().trim().isEmpty()) {

				String context = subDAO.getLearningContext(subtopicId);

				String keywords = gemini.generateSearchKeywords(context);

				if (keywords != null) {

					keywords = keywords.replace("```json", "").replace("```", "").trim();

					subDAO.updateAIKeywords(subtopicId, keywords);

					sub.setAiKeywords(keywords);

				}

			}

			// =========================
			// YOUTUBE CACHE
			// =========================

			LearningResourceDAO resourceDAO = new LearningResourceDAO();

			if (!resourceDAO.exists(subtopicId)) {

				YouTubeService yt = new YouTubeService();

				String query = "";

				if (sub.getAiKeywords() != null && !sub.getAiKeywords().isBlank()) {

					String keywords = sub.getAiKeywords().replace("[", "").replace("]", "").replace("\"", "");

					query = keywords.split(",")[0].trim();

				} else {

					query = sub.getName() + " tutorial beginner";

				}

				System.out.println("YT QUERY = " + query);

				List<LearningResource> newVideos = yt.searchVideos(subtopicId, query);

				System.out.println("VIDEOS FOUND = " + newVideos.size());

				for (LearningResource video : newVideos) {

					resourceDAO.addResource(video);

				}

			}

			List<LearningResource> videos = resourceDAO.getResources(subtopicId);

			request.setAttribute("subtopic", sub);

			request.setAttribute("videos", videos);

			request.getRequestDispatcher("learning.jsp").forward(request, response);

		} catch (Exception e) {

			e.printStackTrace();

			response.sendRedirect("dashboard.jsp");

		}

	}

}