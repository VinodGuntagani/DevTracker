package controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import dao.RoadmapDAO;

@WebServlet("/deleteRoadmap")
public class DeleteRoadmapServlet extends HttpServlet {

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		int id = Integer.parseInt(request.getParameter("id"));

		RoadmapDAO dao = new RoadmapDAO();

		dao.deleteRoadmap(id);

		response.sendRedirect("dashboard.jsp");

	}

}