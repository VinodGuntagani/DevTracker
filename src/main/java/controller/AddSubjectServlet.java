package controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import dao.SubjectDAO;
import model.Subject;

@WebServlet("/addSubject")
public class AddSubjectServlet extends HttpServlet {

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		int roadmapId = Integer.parseInt(request.getParameter("roadmapId"));

		String name = request.getParameter("name");

		Subject subject = new Subject();

		subject.setRoadmapId(roadmapId);

		subject.setName(name);

		SubjectDAO dao = new SubjectDAO();

		dao.addSubject(subject);

		response.sendRedirect("openAIRoadmap?id=" + roadmapId);

	}

}