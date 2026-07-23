package controller;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;

import dao.AIJobDAO;
import dao.AIJobTaskDAO;
import model.AIJob;
import model.AIJobTask;

@WebServlet("/aiJobProgress")
public class AIJobProgressServlet extends HttpServlet {

	private final AIJobDAO dao = new AIJobDAO();
	private final AIJobTaskDAO taskDAO = new AIJobTaskDAO();

	// Stores the last known state for each job
	private static final Map<Integer, String> lastState = new HashMap<>();

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");

		int jobId = Integer.parseInt(request.getParameter("jobId"));

		AIJob job = dao.getJob(jobId);

		if (job == null) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			response.getWriter().write("{\"error\":\"Job not found\"}");
			return;
		}

		// Only log when something changes
		String currentState = job.getCompletedTasks() + ":" + job.getFailedTasks() + ":" + job.getTotalTasks() + ":"
				+ job.getStatus();

		String previousState = lastState.get(jobId);

		if (!currentState.equals(previousState)) {

			System.out.println("================================");
			System.out.println("📊 AI Job Progress Updated");
			System.out.println("--------------------------------");
			System.out.println("Job ID        : " + jobId);
			System.out.println("Completed     : " + job.getCompletedTasks());
			System.out.println("Failed        : " + job.getFailedTasks());
			System.out.println("Total         : " + job.getTotalTasks());
			System.out.println("Status        : " + job.getStatus());
			System.out.println("================================");

			lastState.put(jobId, currentState);
		}

		List<AIJobTask> tasks = taskDAO.getTasksByJobId(jobId);

		Map<String, Object> result = new HashMap<>();

		result.put("status", job.getStatus());
		result.put("completed", job.getCompletedTasks());
		result.put("failed", job.getFailedTasks());
		result.put("total", job.getTotalTasks());
		result.put("tasks", tasks);

		Gson gson = new GsonBuilder()
				.registerTypeAdapter(LocalDateTime.class,
						(JsonSerializer<LocalDateTime>) (src, typeOfSrc, context) -> new JsonPrimitive(src.toString()))
				.create();

		response.getWriter().write(gson.toJson(result));
	}
}