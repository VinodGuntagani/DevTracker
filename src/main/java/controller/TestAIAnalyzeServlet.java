package controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import service.GeminiService;
import model.AIAnalysis;

@WebServlet("/testAnalyze")
public class TestAIAnalyzeServlet extends HttpServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		GeminiService service = new GeminiService();

		AIAnalysis result = service.analyzeSubTopic("Spring Security JWT Authentication");

		if (result != null) {

			response.getWriter().println("Difficulty: " + result.getDifficulty());

			response.getWriter().println("Hours: " + result.getMinutes());

		} else {

			response.getWriter().println("AI failed");

		}

	}

}