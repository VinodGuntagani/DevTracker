package controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import service.GeminiService;

@WebServlet("/testGemini")
public class TestGeminiServlet extends HttpServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		GeminiService gemini = new GeminiService();

		String result = gemini.testGemini();

		System.out.println(result);

		response.getWriter().print(result);

	}

}