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

	public String generateLearning(String learningContext) {

		try {

			String prompt = learningPromptBuilder.build(learningContext);

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

	

}