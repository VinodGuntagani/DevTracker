package controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import dao.RoadmapDAO;

@WebServlet("/editRoadmap")
public class EditRoadmapServlet extends HttpServlet {

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		int id = Integer.parseInt(request.getParameter("id"));

		String title = request.getParameter("title");

		String description = request.getParameter("description");

		RoadmapDAO dao = new RoadmapDAO();

		dao.updateName(id, title, description);

		response.sendRedirect("dashboard.jsp");

	}

}