package controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import dao.SubTopicDAO;

@WebServlet("/restoreSubTopic")
public class RestoreSubTopicServlet extends HttpServlet {

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		int id = Integer.parseInt(request.getParameter("id"));

		SubTopicDAO dao = new SubTopicDAO();

		dao.restoreSubTopic(id);

		response.sendRedirect("trash.jsp");

	}

}