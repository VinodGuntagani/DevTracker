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
import model.AIRequest;
import model.AIResponse;
import service.ai.AIService;
import service.ai.GeminiClient;
import service.prompt.LearningPromptBuilder;
import service.prompt.RoadmapPromptBuilder;
import model.AIAnalysis;

public class GeminiService {

    private static final String API_KEY = System.getenv("GEMINI_API_KEY");

    private final AIService aiService = new AIService();

    private final LearningPromptBuilder learningPromptBuilder = new LearningPromptBuilder();
	private final GeminiClient geminiClient = new GeminiClient();
	private final RoadmapPromptBuilder roadmapPromptBuilder = new RoadmapPromptBuilder();
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

			System.out.println("\n========================================");
			System.out.println("🤖 generateAITimetable() CALLED");
			System.out.println("========================================");
			System.out.println("[AI] Roadmap JSON Length : " + roadmapJson.length());

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

			System.out.println("[AI] Sending timetable request to Gemini...");

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

			JsonObject root = JsonParser.parseString(body).getAsJsonObject();

			if (root.has("error")) {

				String error = root.getAsJsonObject("error").get("message").getAsString();

				System.out.println("❌ Gemini Error : " + error);
				System.out.println("========================================\n");

				return null;
			}

			String aiJson = root.getAsJsonArray("candidates")
					.get(0).getAsJsonObject()
					.getAsJsonObject("content")
					.getAsJsonArray("parts")
					.get(0).getAsJsonObject()
					.get("text").getAsString();

			aiJson = aiJson.replace("```json", "").replace("```", "").trim();

			System.out.println("✅ AI Timetable Generated Successfully");
			System.out.println("========================================\n");

			return gson.fromJson(aiJson, AIScheduleResponse.class);

		} catch (Exception e) {

			System.out.println("❌ Exception in generateAITimetable()");
			e.printStackTrace();

		}

		return null;

	}

	public AIRoadmapResponse generateFullRoadmap(String userPrompt, int days, int dailyMinutes) {

		try {

			String prompt = roadmapPromptBuilder.build(userPrompt, days, dailyMinutes);

			AIRequest request = new AIRequest();

			request.setPrompt(prompt);
			request.setPreferredModel("gemini-2.5-flash");

			AIResponse response = aiService.generate(request);

			if (!response.isSuccess()) {

				System.out.println(response.getErrorMessage());

				return null;
			}

			Gson gson = new Gson();

			return gson.fromJson(response.getResponse(), AIRoadmapResponse.class);

		} catch (Exception e) {

			e.printStackTrace();

		}

		return null;

	}
	
	public String generateLearning(String topicName) {

	    try {

	        String prompt = learningPromptBuilder.build(topicName);

	        AIRequest request = new AIRequest();

	        request.setPrompt(prompt);
	        request.setPreferredModel("gemini-2.5-flash");

	        AIResponse response = aiService.generate(request);

	        if (!response.isSuccess()) {

	            System.out.println(response.getErrorMessage());

	            return null;
	        }

	        return response.getResponse();

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