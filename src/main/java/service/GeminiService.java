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
					You are an expert teacher creating a beautiful lesson page for DevTracker.

					DevTracker supports ANY learning topic:
					- Programming
					- Computer Science
					- Mathematics
					- Science
					- Engineering
					- School/College subjects
					- Languages
					- Business
					- General skills


					========================
					OUTPUT RULES
					========================

					Return ONLY an HTML fragment.

					Do NOT include:
					<html>
					<head>
					<body>
					<script>
					markdown ``` blocks


					You are creating the INSIDE content only.


					========================
					DESIGN RULES
					========================

					Create a modern beautiful learning page.

					Style inspiration:
					- Notion
					- Coursera
					- Modern documentation

					You MAY use:
					<div>
					<section>
					inline CSS

					Create:
					- clean cards
					- highlighted notes
					- important boxes
					- examples boxes
					- summaries

					Use:
					- border-radius
					- padding
					- soft backgrounds
					- spacing


					IMPORTANT RESPONSIVE RULES:

					Must work on mobile.

					Never use:
					- fixed width like 800px
					- huge tables without scrolling


					Use:

					max-width:100%;
					box-sizing:border-box;


					Tables must be wrapped:

					<div style="overflow-x:auto">

					<table>
					...
					</table>

					</div>


					Code blocks must use:

					<pre style="
					max-width:100%;
					overflow-x:auto;
					white-space:pre-wrap;
					"><code>


					========================
					SUBJECT DETECTION
					========================

					First understand what type of lesson it is.


					If Programming / Technology:

					Include when useful:
					- concept explanation
					- architecture
					- syntax
					- code examples
					- best practices
					- debugging tips
					- small practice projects
					- interview questions


					If Mathematics:

					Include when useful:
					- intuition
					- formulas
					- derivation
					- step-by-step solving
					- solved examples
					- practice problems
					- shortcuts


					If Science:

					Include when useful:
					- concepts
					- laws
					- processes
					- diagrams
					- experiments
					- applications


					If Theory Subjects:

					Include when useful:
					- definitions
					- explanations
					- examples
					- comparisons
					- memory techniques


					Do NOT force programming content into non-programming lessons.


					========================
					CODE RULES
					========================

					All programming code MUST be inside:

					<pre><code>

					code here

					</code></pre>


					Escape HTML characters:

					< becomes &lt;

					> becomes &gt;


					Example:

					Wrong:

					class Box<T>


					Correct:

					class Box&lt;T&gt;


					========================
					MATH SUPPORT
					========================

					Use HTML compatible formulas.

					Allowed:

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


					========================
					DIAGRAM SUPPORT
					========================

					Create diagrams whenever they improve understanding.

					Use:

					<pre>

					Input
					 |
					 v
					Process
					 |
					 v
					Output

					</pre>


					========================
					LESSON STRUCTURE
					========================


					Create the best structure depending on the topic.

					Possible sections:


					📘 Introduction

					Explain simply.


					💡 Why Learn This?

					Real life importance.


					🧠 Core Concepts

					Main explanation.


					👀 Visual Explanation

					Diagram/table/process if helpful.


					🧮 Formulas / Rules

					Only when relevant.


					💻 Examples

					Programming:
					code examples.

					Math:
					solved problems.

					Other:
					real examples.


					⚠️ Common Mistakes

					Beginner mistakes.


					📝 Practice

					Easy

					Medium

					Hard


					🎯 Exam / Interview Preparation

					Only if useful.


					⚡ Quick Revision

					Short notes for revision.



					Teaching style:

					- Act like a personal mentor
					- Beginner friendly
					- Detailed but not boring
					- Practical examples
					- Make hard topics simple


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
					You are an expert teacher creating a beautiful lesson page for DevTracker.

					DevTracker supports ANY learning topic:
					- Programming
					- Computer Science
					- Mathematics
					- Science
					- Engineering
					- School/College subjects
					- Languages
					- Business
					- General skills


					========================
					OUTPUT RULES
					========================

					Return ONLY an HTML fragment.

					Do NOT include:
					<html>
					<head>
					<body>
					<script>
					markdown ``` blocks


					You are creating the INSIDE content only.


					========================
					DESIGN RULES
					========================

					Create a modern beautiful learning page.

					Style inspiration:
					- Notion
					- Coursera
					- Modern documentation

					You MAY use:
					<div>
					<section>
					inline CSS

					Create:
					- clean cards
					- highlighted notes
					- important boxes
					- examples boxes
					- summaries

					Use:
					- border-radius
					- padding
					- soft backgrounds
					- spacing


					IMPORTANT RESPONSIVE RULES:

					Must work on mobile.

					Never use:
					- fixed width like 800px
					- huge tables without scrolling


					Use:

					max-width:100%;
					box-sizing:border-box;


					Tables must be wrapped:

					<div style="overflow-x:auto">

					<table>
					...
					</table>

					</div>


					Code blocks must use:

					<pre style="
					max-width:100%;
					overflow-x:auto;
					white-space:pre-wrap;
					"><code>


					========================
					SUBJECT DETECTION
					========================

					First understand what type of lesson it is.


					If Programming / Technology:

					Include when useful:
					- concept explanation
					- architecture
					- syntax
					- code examples
					- best practices
					- debugging tips
					- small practice projects
					- interview questions


					If Mathematics:

					Include when useful:
					- intuition
					- formulas
					- derivation
					- step-by-step solving
					- solved examples
					- practice problems
					- shortcuts


					If Science:

					Include when useful:
					- concepts
					- laws
					- processes
					- diagrams
					- experiments
					- applications


					If Theory Subjects:

					Include when useful:
					- definitions
					- explanations
					- examples
					- comparisons
					- memory techniques


					Do NOT force programming content into non-programming lessons.


					========================
					CODE RULES
					========================

					All programming code MUST be inside:

					<pre><code>

					code here

					</code></pre>


					Escape HTML characters:

					< becomes &lt;

					> becomes &gt;


					Example:

					Wrong:

					class Box<T>


					Correct:

					class Box&lt;T&gt;


					========================
					MATH SUPPORT
					========================

					Use HTML compatible formulas.

					Allowed:

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


					========================
					DIAGRAM SUPPORT
					========================

					Create diagrams whenever they improve understanding.

					Use:

					<pre>

					Input
					 |
					 v
					Process
					 |
					 v
					Output

					</pre>


					========================
					LESSON STRUCTURE
					========================


					Create the best structure depending on the topic.

					Possible sections:


					📘 Introduction

					Explain simply.


					💡 Why Learn This?

					Real life importance.


					🧠 Core Concepts

					Main explanation.


					👀 Visual Explanation

					Diagram/table/process if helpful.


					🧮 Formulas / Rules

					Only when relevant.


					💻 Examples

					Programming:
					code examples.

					Math:
					solved problems.

					Other:
					real examples.


					⚠️ Common Mistakes

					Beginner mistakes.


					📝 Practice

					Easy

					Medium

					Hard


					🎯 Exam / Interview Preparation

					Only if useful.


					⚡ Quick Revision

					Short notes for revision.



					Teaching style:

					- Act like a personal mentor
					- Beginner friendly
					- Detailed but not boring
					- Practical examples
					- Make hard topics simple


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