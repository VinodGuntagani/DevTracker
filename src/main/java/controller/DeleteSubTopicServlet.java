package controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import dao.SubTopicDAO;

@WebServlet("/deleteSubTopic")
public class DeleteSubTopicServlet extends HttpServlet {

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		int id = Integer.parseInt(request.getParameter("subtopicId"));

		int roadmapId = Integer.parseInt(request.getParameter("roadmapId"));

		SubTopicDAO dao = new SubTopicDAO();

		dao.deleteSubTopic(id);

		response.sendRedirect("openAIRoadmap?id=" + roadmapId);

	}

}