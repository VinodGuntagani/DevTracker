package controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import dao.SubjectDAO;

@WebServlet("/deleteSubject")
public class DeleteSubjectServlet extends HttpServlet {

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		int id = Integer.parseInt(request.getParameter("id"));

		int roadmapId = Integer.parseInt(request.getParameter("roadmapId"));

		SubjectDAO dao = new SubjectDAO();

		dao.deleteSubject(id);

		response.sendRedirect("openAIRoadmap?id=" + roadmapId);

	}

}