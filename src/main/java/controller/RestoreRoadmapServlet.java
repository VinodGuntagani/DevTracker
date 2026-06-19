package controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import dao.RoadmapDAO;

@WebServlet("/restoreRoadmap")
public class RestoreRoadmapServlet extends HttpServlet {

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		int id = Integer.parseInt(request.getParameter("id"));

		RoadmapDAO dao = new RoadmapDAO();

		dao.restoreRoadmap(id);

		response.sendRedirect("trash.jsp");

	}

}