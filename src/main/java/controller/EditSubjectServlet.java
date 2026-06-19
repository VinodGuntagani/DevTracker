package controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import dao.SubjectDAO;

@WebServlet("/editSubject")
public class EditSubjectServlet extends HttpServlet {

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		int id = Integer.parseInt(request.getParameter("id"));

		int roadmapId = Integer.parseInt(request.getParameter("roadmapId"));

		String name = request.getParameter("name");

		SubjectDAO dao = new SubjectDAO();

		dao.updateName(id, name);

		response.sendRedirect("subjects.jsp?roadmapId=" + roadmapId);

	}

}