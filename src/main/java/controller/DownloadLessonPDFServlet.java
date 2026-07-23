package controller;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.SubTopicDAO;
import model.SubTopic;
import service.pdf.PDFService;

@WebServlet("/downloadLessonPDF")
public class DownloadLessonPDFServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		try {

			// Get SubTopic ID from URL
			String idParam = request.getParameter("subtopicId");

			if (idParam == null || idParam.isBlank()) {
				response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing subtopicId.");
				return;
			}

			int subtopicId = Integer.parseInt(idParam);

			// Fetch lesson
			SubTopicDAO dao = new SubTopicDAO();
			SubTopic subTopic = dao.getSubTopicById(subtopicId);

			if (subTopic == null || subTopic.getAiLearning() == null
					|| subTopic.getAiLearning().isBlank()) {

				response.sendError(HttpServletResponse.SC_NOT_FOUND, "Lesson not found.");
				return;
			}

			// Lesson HTML
			String html = subTopic.getAiLearning();

			// Load PDF CSS
			InputStream cssStream = getServletContext()
					.getResourceAsStream("/css/pdf-theme.css");

			if (cssStream == null) {
				throw new IOException("pdf-theme.css not found.");
			}

			String css = new String(cssStream.readAllBytes(), StandardCharsets.UTF_8);

			// Generate PDF
			PDFService pdfService = new PDFService();

			byte[] pdf = pdfService.generateLessonPDF(
					html,
					css,
					getServletContext());

			// Safe filename
			String fileName = subTopic.getName()
					.replaceAll("[\\\\/:*?\"<>|]", "_")
					.replaceAll("\\s+", "_");

			// Response
			response.setContentType("application/pdf");
			response.setHeader(
					"Content-Disposition",
					"attachment; filename=\"" + fileName + ".pdf\"");
			response.setContentLength(pdf.length);

			response.getOutputStream().write(pdf);
			response.getOutputStream().flush();

		} catch (NumberFormatException e) {

			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid subtopicId.");

		} catch (Exception e) {

			throw new ServletException(e);

		}
	}
}