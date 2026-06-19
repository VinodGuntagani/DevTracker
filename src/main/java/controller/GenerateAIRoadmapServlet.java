package controller;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import model.User;
import model.AIRoadmapResponse;

import service.GeminiService;
import service.AIRoadmapSaver;

@WebServlet("/generateAIRoadmap")
public class GenerateAIRoadmapServlet extends HttpServlet {

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		try {

			HttpSession session = request.getSession();

			User user = (User) session.getAttribute("user");

			if (user == null) {

				response.sendRedirect("login.jsp");

				return;

			}

			String goal = request.getParameter("goal");

			int days = Integer.parseInt(request.getParameter("days"));

			int dailyMinutes = Integer.parseInt(request.getParameter("dailyMinutes"));

			GeminiService gemini = new GeminiService();

			AIRoadmapResponse aiRoadmap = gemini.generateFullRoadmap(goal, days, dailyMinutes);

			if (aiRoadmap == null) {

				System.out.println("AI generation failed");

				response.sendRedirect("dashboard.jsp");

				return;

			}

			Date start = Date.valueOf(LocalDate.now());

			Date target = Date.valueOf(LocalDate.now().plusDays(days - 1));

			AIRoadmapSaver saver = new AIRoadmapSaver();

			saver.save(

					user.getId(),

					aiRoadmap,

					start,

					target

			);

			response.sendRedirect("dashboard.jsp");

		} catch (Exception e) {

			e.printStackTrace();

		}

	}

}