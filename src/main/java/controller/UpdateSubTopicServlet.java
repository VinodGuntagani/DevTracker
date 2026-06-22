package controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import dao.StudyPlanDAO;
import dao.SubTopicDAO;

@WebServlet("/updateSubTopic")
public class UpdateSubTopicServlet extends HttpServlet {

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		int id = Integer.parseInt(request.getParameter("id"));

		boolean completed = Boolean.parseBoolean(request.getParameter("completed"));

		SubTopicDAO dao = new SubTopicDAO();

		dao.updateStatus(id, completed);

		StudyPlanDAO planDAO = new StudyPlanDAO();

		planDAO.updateTaskBySubTopic(id, completed);

		String redirect = request.getParameter("redirect");

		if (redirect != null) {

			response.sendRedirect(redirect);

		} else {

			response.sendRedirect("dashboard.jsp");

		}

	}

}