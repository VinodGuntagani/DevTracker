package controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import model.LearningResource;

import service.YouTubeService;

@WebServlet("/searchVideos")
public class SearchVideosServlet extends HttpServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		try {

			int subtopicId = Integer.parseInt(request.getParameter("subtopicId"));

			String query = request.getParameter("query");

			YouTubeService yt = new YouTubeService();

			List<LearningResource> videos = yt.searchVideos(subtopicId, query);

			request.setAttribute("videos", videos);

			request.getRequestDispatcher("video-results.jsp").forward(request, response);

		} catch (Exception e) {

			e.printStackTrace();

		}

	}

}