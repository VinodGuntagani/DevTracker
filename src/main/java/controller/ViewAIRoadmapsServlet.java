package controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import dao.RoadmapDAO;
import model.Roadmap;
import model.User;

@WebServlet("/viewAIRoadmaps")
public class ViewAIRoadmapsServlet extends HttpServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		HttpSession session = request.getSession();

		User user = (User) session.getAttribute("user");

		if (user == null) {

			response.sendRedirect("login.jsp");

			return;

		}

		RoadmapDAO dao = new RoadmapDAO();

		List<Roadmap> list = dao.getAIRoadmaps(user.getId());

		request.setAttribute("roadmaps", list);

		request.getRequestDispatcher("ai-roadmaps.jsp").forward(request, response);

	}

}