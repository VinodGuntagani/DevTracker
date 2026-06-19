package controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import dao.SubTopicDAO;

import model.SubTopic;

import service.GeminiService;

@WebServlet("/generateNotes")
public class GenerateNotesServlet extends HttpServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		try {

			int subtopicId = Integer.parseInt(request.getParameter("subtopicId"));

			SubTopicDAO dao = new SubTopicDAO();

			SubTopic sub = dao.getSubTopicById(subtopicId);

			// no cached notes

			if (sub.getAiNotes() == null || sub.getAiNotes().trim().isEmpty()) {

				GeminiService gemini = new GeminiService();

				String notes = gemini.generateNotes(sub.getName());

				if (notes != null) {

					notes = notes.replace("```html", "").replace("```", "").trim();

					dao.updateAINotes(subtopicId, notes);

					sub.setAiNotes(notes);

				}

				System.out.println("AI NOTES RESULT = " + notes);

				if (notes != null) {

					dao.updateAINotes(subtopicId, notes);

					sub.setAiNotes(notes);

				}

			}

			request.setAttribute("subtopic", sub);

			request.getRequestDispatcher("notes.jsp").forward(request, response);

		} catch (Exception e) {

			e.printStackTrace();

			response.sendRedirect("dashboard.jsp");

		}

	}

}