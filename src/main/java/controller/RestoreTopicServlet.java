package controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import dao.TopicDAO;

@WebServlet("/restoreTopic")
public class RestoreTopicServlet extends HttpServlet {

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		int id = Integer.parseInt(request.getParameter("id"));

		int subjectId = Integer.parseInt(request.getParameter("subjectId"));

		int roadmapId = Integer.parseInt(request.getParameter("roadmapId"));

		TopicDAO dao = new TopicDAO();

		dao.restoreTopic(id);

		response.sendRedirect("trash.jsp");

	}

}