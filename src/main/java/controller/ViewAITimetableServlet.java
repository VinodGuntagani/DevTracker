package controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import dao.AITimetableDAO;
import model.AIDailyTask;

@WebServlet("/viewAITimetable")
public class ViewAITimetableServlet extends HttpServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		int roadmapId = Integer.parseInt(request.getParameter("roadmapId"));

		AITimetableDAO dao = new AITimetableDAO();

		List<AIDailyTask> tasks = dao.getTasks(roadmapId);

		request.setAttribute("tasks", tasks);

		request.setAttribute("roadmapId", roadmapId);

		request.getRequestDispatcher("ai-timetable.jsp").forward(request, response);

	}

}