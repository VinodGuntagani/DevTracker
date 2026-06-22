package service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import model.AIScheduleResponse;
import model.AIRoadmapResponse;

import model.AIAnalysis;

public class GeminiService {

	private static final String API_KEY = System.getenv("GEMINI_API_KEY");

	public String testGemini() {

		try {

			String url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash-lite:generateContent?key="
					+ API_KEY;

			String json = """
					{
					  "contents": [
					    {
					      "parts": [
					        {
					          "text": "Say hello from Gemini"
					        }
					      ]
					    }
					  ]
					}
					""";

			HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url))
					.header("Content-Type", "application/json").POST(HttpRequest.BodyPublishers.ofString(json)).build();

			HttpClient client = HttpClient.newHttpClient();

			HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

			return response.body();

		} catch (Exception e) {

			e.printStackTrace();

		}

		return null;

	}

	public AIAnalysis analyzeSubTopic(String name) {

		try {

			String url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash-lite:generateContent?key="
					+ API_KEY;

			String prompt = """
					Analyze this programming study topic.

					Return ONLY JSON.

					Rules:
					- Estimate time in minutes only.
					- minutes must be an integer.
					- Use multiples of 5.
					- Never return decimal values.

					Format:

					{
					  "difficulty":"Easy/Medium/Hard",
					  "minutes":120
					}

					Topic:
					""" + name;

			Gson gson = new Gson();

			JsonObject textPart = new JsonObject();

			textPart.addProperty("text", prompt);

			JsonObject content = new JsonObject();

			content.add("parts", gson.toJsonTree(new JsonObject[] { textPart }));

			JsonObject requestJson = new JsonObject();

			requestJson.add("contents", gson.toJsonTree(new JsonObject[] { content }));

			String json = requestJson.toString();

			HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url))
					.header("Content-Type", "application/json").POST(HttpRequest.BodyPublishers.ofString(json)).build();

			HttpClient client = HttpClient.newHttpClient();

			HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

			String body = response.body();

			JsonObject root = JsonParser.parseString(body).getAsJsonObject();
			// CHECK IF GEMINI RETURNED ERROR
			if (root.has("error")) {

				System.out.println(root.getAsJsonObject("error").get("message").getAsString());

				return null;
			}

			String aiJson = root.getAsJsonArray("candidates").get(0).getAsJsonObject()

					.getAsJsonObject("content")

					.getAsJsonArray("parts").get(0).getAsJsonObject()

					.get("text").getAsString();

			aiJson = aiJson.replace("```json", "").replace("```", "").trim();

			return gson.fromJson(aiJson, AIAnalysis.class);

		} catch (Exception e) {

			e.printStackTrace();

		}

		return null;

	}

	public AIScheduleResponse generateAITimetable(String roadmapJson) {

		try {

			String url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash-lite:generateContent?key="
					+ API_KEY;

			String prompt = """
					You are an expert study planner.

					Create the best study timetable.

					Rules:

					- Understand prerequisites.
					- Basics should come before advanced topics.
					- Balance difficulty.
					- Return ONLY JSON.
					- Do not create new topics.
					- Use only given subtopic ids.

					Output format:

					{
					  "schedule":[

					    {
					      "day":1,
					      "tasks":[1,2]
					    }

					  ]
					}


					Roadmap data:

					""" + roadmapJson;

			Gson gson = new Gson();

			JsonObject textPart = new JsonObject();

			textPart.addProperty("text", prompt);

			JsonObject content = new JsonObject();

			content.add("parts", gson.toJsonTree(new JsonObject[] { textPart }));

			JsonObject requestJson = new JsonObject();

			requestJson.add("contents", gson.toJsonTree(new JsonObject[] { content }));

			HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url))
					.header("Content-Type", "application/json")
					.POST(HttpRequest.BodyPublishers.ofString(requestJson.toString())).build();

			HttpClient client = HttpClient.newHttpClient();

			HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

			String body = response.body();

			System.out.println(body);

			JsonObject root = JsonParser.parseString(body).getAsJsonObject();
			// CHECK IF GEMINI RETURNED ERROR
			if (root.has("error")) {

				System.out.println(root.getAsJsonObject("error").get("message").getAsString());

				return null;
			}

			String aiJson = root.getAsJsonArray("candidates").get(0).getAsJsonObject()

					.getAsJsonObject("content")

					.getAsJsonArray("parts").get(0).getAsJsonObject()

					.get("text").getAsString();

			aiJson = aiJson.replace("```json", "").replace("```", "").trim();

			return gson.fromJson(aiJson, AIScheduleResponse.class);

		} catch (Exception e) {

			e.printStackTrace();

		}

		return null;

	}

	public AIRoadmapResponse generateFullRoadmap(String userPrompt, int days, int dailyMinutes) {

		try {

			String url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash-lite:generateContent?key="
					+ API_KEY;

			String prompt = """
					You are an expert software career mentor and study planner.

					Create a complete realistic learning roadmap.

					Rules:

					1. Create main subjects.
					2. Create topics inside subjects.
					3. Create subtopics inside topics.
					4. Estimate difficulty.
					5. Estimate learning minutes for every subtopic.
					6. Use the user's daily available study time.
					7. Total study minutes per day must not exceed user's daily limit.
					8. Allocate every subtopic to a day number.
					9. Finish within given days.
					10. Order by prerequisites.
					11. Return ONLY JSON.
					12. Minutes must be integer values.
					13. Use multiples of 5 minutes.

					JSON format:

					{
					  "title":"",

					  "description":"",

					  "subjects":[

					    {
					      "name":"",

					      "topics":[

					        {
					          "name":"",

					          "subtopics":[

					            {
					              "name":"",
					              "difficulty":"Easy",
					              "minutes":60,
					              "day":1
					            }

					          ]

					        }

					      ]

					    }

					  ]

					}


					User goal:
					""" + userPrompt + """

					Available days:
					""" + days + """

					Available study minutes per day:
					""" + dailyMinutes;

			Gson gson = new Gson();

			JsonObject textPart = new JsonObject();

			textPart.addProperty("text", prompt);

			JsonObject content = new JsonObject();

			content.add("parts", gson.toJsonTree(new JsonObject[] { textPart }));

			JsonObject requestJson = new JsonObject();

			requestJson.add("contents", gson.toJsonTree(new JsonObject[] { content }));

			HttpRequest request = HttpRequest.newBuilder()

					.uri(URI.create(url))

					.header("Content-Type", "application/json")

					.POST(HttpRequest.BodyPublishers.ofString(requestJson.toString()))

					.build();

			HttpClient client = HttpClient.newHttpClient();

			HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

			String body = response.body();

			System.out.println(body);

			JsonObject root = JsonParser.parseString(body).getAsJsonObject();

			if (root.has("error")) {

				System.out.println(root.getAsJsonObject("error").get("message").getAsString());

				return null;
			}

			String aiJson = root.getAsJsonArray("candidates").get(0).getAsJsonObject()

					.getAsJsonObject("content")

					.getAsJsonArray("parts").get(0).getAsJsonObject()

					.get("text").getAsString();

			aiJson = aiJson.replace("```json", "").replace("```", "").trim();

			return gson.fromJson(aiJson, AIRoadmapResponse.class);

		} catch (Exception e) {

			e.printStackTrace();

		}

		return null;

	}

	public String generateNotes(String topicName) {

		try {

			String url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash-lite:generateContent?key="
					+ API_KEY;

			String prompt = """
					You are an expert teacher creating premium interactive study notes for DevTracker.

					Create beautiful HTML content for a learning card.

					VERY IMPORTANT:
					- Return ONLY raw HTML.
					- Do NOT write ```html.
					- Do NOT include <html>, <head>, <body>.
					- This HTML will be inserted inside an existing page.

					You are allowed to use:

					Text:
					<h1>, <h2>, <h3>, <h4>
					<p>
					<strong>
					<em>
					<span>

					Lists:
					<ul>
					<ol>
					<li>

					Tables:
					<table>
					<thead>
					<tbody>
					<tr>
					<th>
					<td>

					Math:
					- Use <sup> for powers.
					Example:
					2<sup>8</sup>
					10<sup>3</sup>

					- Use proper symbols:
					→
					≥
					≤
					×
					÷

					Code:
					<pre><code>
					for programming examples.

					Design freedom:
					- You MAY use inline styles.
					- You MAY use colors.
					- Highlight important formulas.
					- Create small info boxes.
					- Create warning boxes for mistakes.
					- Create interview tip boxes.

					Preferred colors:
					Blue = concepts
					Green = examples
					Orange = important
					Red = mistakes

					Content structure:

					1. Clear title

					2. Simple explanation

					3. Core concepts

					4. Important formulas / rules

					5. Examples with step-by-step solving

					6. Tables if comparison is useful

					7. Real world usage

					8. Interview questions with answers

					9. Common mistakes

					10. Quick revision box

					Teaching style:
					- Beginner friendly
					- Visual
					- Less boring paragraphs
					- More structured blocks
					- Explain like a mentor

					Topic:
					""" + topicName;

			Gson gson = new Gson();

			JsonObject textPart = new JsonObject();

			textPart.addProperty("text", prompt);

			JsonObject content = new JsonObject();

			content.add("parts", gson.toJsonTree(new JsonObject[] { textPart }));

			JsonObject requestJson = new JsonObject();

			requestJson.add("contents", gson.toJsonTree(new JsonObject[] { content }));

			HttpRequest request = HttpRequest.newBuilder()

					.uri(URI.create(url))

					.header("Content-Type", "application/json")

					.POST(HttpRequest.BodyPublishers.ofString(requestJson.toString()))

					.build();

			HttpClient client = HttpClient.newHttpClient();

			HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

			JsonObject root = JsonParser.parseString(response.body()).getAsJsonObject();

			if (root.has("error")) {

				System.out.println(root.getAsJsonObject("error").get("message").getAsString());

				return null;

			}

			return root.getAsJsonArray("candidates").get(0).getAsJsonObject()

					.getAsJsonObject("content")

					.getAsJsonArray("parts").get(0).getAsJsonObject()

					.get("text").getAsString();

		} catch (Exception e) {

			e.printStackTrace();

		}

		return null;

	}

	public String generateLearning(String topicName) {

		try {

			String url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash-lite:generateContent?key="
					+ API_KEY;
			String prompt = """
					You are an expert teacher creating lesson content for DevTracker.

					DevTracker is a learning platform for:
					- Programming
					- Computer Science
					- Mathematics
					- Science
					- Technical subjects


					OUTPUT RULES:

					Return ONLY HTML fragment.

					Never include:
					<html>
					<head>
					<body>
					<style>
					<script>
					markdown code fences


					IMPORTANT:
					DevTracker controls:
					- layout
					- cards
					- colors
					- responsive design

					You control:
					- explanation
					- teaching content only



					ALLOWED HTML:

					Headings:
					<h2>
					<h3>
					<h4>

					Text:
					<p>
					<strong>
					<em>
					<span>

					Lists:
					<ul>
					<ol>
					<li>

					Tables:
					<table>
					<thead>
					<tbody>
					<tr>
					<th>
					<td>

					Code:
					<pre><code>


					Diagrams:
					You MAY create simple text diagrams using:

					<pre>
					diagram here
					</pre>

					Example:

					Client
					  |
					  v
					Server
					  |
					  v
					Database


					Mathematics:

					Use HTML friendly math.

					Use:

					<sup>
					<sub>

					Examples:

					x<sup>2</sup>

					H<sub>2</sub>O


					Use symbols:

					→
					←
					≥
					≤
					×
					÷
					π
					√


					Programming code rules:

					All code MUST be inside:

					<pre><code>

					code

					</code></pre>


					Escape HTML characters inside code:

					< becomes &lt;

					> becomes &gt;


					Do NOT:
					- use inline CSS
					- use fixed widths
					- create cards
					- create buttons
					- create navigation
					- create links



					Create lesson:


					1. Introduction

					Explain what it is.


					2. Why Learn This?

					Real world importance.


					3. Core Concepts

					Explain important ideas.


					4. Visual Explanation

					If useful:
					- diagrams
					- flow
					- memory representation
					- architecture


					5. Formulas / Rules

					Only when needed.

					Explain step by step.


					6. Examples

					Programming:
					include code examples.

					Math:
					include solved examples.

					Theory:
					include scenarios.


					7. Comparison Tables

					Only when useful.


					8. Common Mistakes

					Explain mistakes beginners make.


					9. Practice Section

					Beginner tasks.

					Intermediate tasks.

					Advanced tasks.


					10. Interview / Exam Questions

					Question + answer.


					11. Quick Revision Notes

					Short bullet summary.



					Teaching style:

					- Beginner friendly
					- Practical
					- Like a mentor
					- Avoid unnecessary paragraphs
					- Prefer examples


					Topic:

					""" + topicName;

			Gson gson = new Gson();

			JsonObject textPart = new JsonObject();

			textPart.addProperty("text", prompt);

			JsonObject content = new JsonObject();

			content.add("parts", gson.toJsonTree(new JsonObject[] { textPart }));

			JsonObject requestJson = new JsonObject();

			requestJson.add("contents", gson.toJsonTree(new JsonObject[] { content }));

			HttpRequest request = HttpRequest.newBuilder()

					.uri(URI.create(url))

					.header("Content-Type", "application/json")

					.POST(HttpRequest.BodyPublishers.ofString(requestJson.toString()))

					.build();

			HttpClient client = HttpClient.newHttpClient();

			HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

			JsonObject root = JsonParser.parseString(response.body()).getAsJsonObject();

			if (root.has("error")) {

				return null;

			}

			return root.getAsJsonArray("candidates").get(0).getAsJsonObject()

					.getAsJsonObject("content")

					.getAsJsonArray("parts").get(0).getAsJsonObject()

					.get("text").getAsString();

		} catch (Exception e) {

			e.printStackTrace();

		}

		return null;

	}

	public String generateYouTubeQuery(String context) {

		try {

			String url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash-lite:generateContent?key="
					+ API_KEY;

			String prompt = """
					You are a YouTube search optimization engine.

					Your job:
					Convert the given learning context into the BEST YouTube search query.

					Rules:
					- Return ONLY ONE search query.
					- Maximum 8 words.
					- Remove roadmap names.
					- Remove company names.
					- Remove course names.
					- Remove unnecessary words.
					- Keep only the actual topic.
					- Detect the technology or subject.
					- Add beginner/tutorial if useful.

					Examples:

					Input:
					Nextraa Technologies Software Developer Intern Crash Course Core Programming Concepts Programming Fundamentals Variables Data Types Operators

					Output:
					JavaScript variables data types operators tutorial beginner


					Input:
					Java Backend Developer Core Java Object Oriented Programming Inheritance

					Output:
					Java inheritance OOP tutorial beginner


					Input:
					Class 10 Science Physics Electricity Current Voltage

					Output:
					Class 10 electricity physics explanation


					Now convert:

					"""
					+ context;

			String result = callGeminiText(prompt);

			if (result == null || result.isBlank()) {

				System.out.println("Gemini query failed. Using fallback.");

				return context;

			}

			return result.replace("\"", "").replace("\n", " ").trim();

		} catch (Exception e) {

			e.printStackTrace();

		}

		return context;
	}

	private String callGeminiText(String prompt) {

		try {

			String url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash-lite:generateContent?key="
					+ API_KEY;

			Gson gson = new Gson();

			JsonObject textPart = new JsonObject();

			textPart.addProperty("text", prompt);

			JsonObject content = new JsonObject();

			content.add("parts", gson.toJsonTree(new JsonObject[] { textPart }));

			JsonObject requestJson = new JsonObject();

			requestJson.add("contents", gson.toJsonTree(new JsonObject[] { content }));

			HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url))
					.header("Content-Type", "application/json")
					.POST(HttpRequest.BodyPublishers.ofString(requestJson.toString())).build();

			HttpClient client = HttpClient.newHttpClient();

			HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

			JsonObject root = JsonParser.parseString(response.body()).getAsJsonObject();

			if (root.has("error")) {

				System.out.println(root.getAsJsonObject("error").get("message").getAsString());

				return null;

			}

			return root.getAsJsonArray("candidates").get(0).getAsJsonObject()

					.getAsJsonObject("content")

					.getAsJsonArray("parts").get(0).getAsJsonObject()

					.get("text").getAsString();

		} catch (Exception e) {

			e.printStackTrace();

		}

		return null;

	}

	public String generateSearchKeywords(String context) {

		String prompt = """
				Create YouTube search queries.

				Return ONLY JSON array.

				Rules:
				- Create exactly 6 queries.
				- Beginner friendly.
				- Keep programming language if available.
				- Keep school class/subject if available.
				- Remove company names.
				- Remove roadmap names.
				- No explanation.

				Example:

				[
				"JavaScript variables tutorial beginner",
				"JavaScript data types explained",
				"JavaScript operators examples"
				]


				Content:


				""" + context;

		return callGeminiText(prompt);

	}
}