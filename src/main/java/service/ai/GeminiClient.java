package service.ai;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import model.AIRequest;
import model.AIResponse;

public class GeminiClient implements AIProvider {

	private final HttpClient client = HttpClient.newHttpClient();

	@Override
	public AIResponse generate(AIRequest request, String apiKey) {

		AIResponse aiResponse = new AIResponse();

		try {

			String model = request.getPreferredModel();

			String url = "https://generativelanguage.googleapis.com/v1beta/models/" + model + ":generateContent?key="
					+ apiKey;

			Gson gson = new Gson();

			JsonObject textPart = new JsonObject();
			textPart.addProperty("text", request.getPrompt());

			JsonObject content = new JsonObject();
			content.add("parts", gson.toJsonTree(new JsonObject[] { textPart }));

			JsonObject requestJson = new JsonObject();
			requestJson.add("contents", gson.toJsonTree(new JsonObject[] { content }));

			HttpRequest httpRequest = HttpRequest.newBuilder().uri(URI.create(url))
					.header("Content-Type", "application/json")
					.POST(HttpRequest.BodyPublishers.ofString(requestJson.toString())).build();

			HttpResponse<String> httpResponse = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());

			JsonObject root = JsonParser.parseString(httpResponse.body()).getAsJsonObject();

			if (root.has("error")) {

			    String error = root.getAsJsonObject("error")
			            .get("message")
			            .getAsString();

			    int statusCode = httpResponse.statusCode();

			    aiResponse.setSuccess(false);
			    aiResponse.setErrorMessage(error);

			    boolean retryable = false;

			    // Retryable HTTP status codes
			    if (statusCode == 429 ||
			        statusCode == 500 ||
			        statusCode == 502 ||
			        statusCode == 503 ||
			        statusCode == 504) {

			        retryable = true;
			    }

			    // Fallback to message matching
			    String lower = error.toLowerCase();

			    if (lower.contains("quota")
			            || lower.contains("rate")
			            || lower.contains("429")
			            || lower.contains("resource exhausted")
			            || lower.contains("temporarily")
			            || lower.contains("temporary")
			            || lower.contains("high demand")
			            || lower.contains("try again")
			            || lower.contains("unavailable")
			            || lower.contains("overloaded")) {

			        retryable = true;
			    }

			    aiResponse.setRetryable(retryable);

			    return aiResponse;
			}

			String text = root.getAsJsonArray("candidates").get(0).getAsJsonObject().getAsJsonObject("content")
					.getAsJsonArray("parts").get(0).getAsJsonObject().get("text").getAsString();

			text = text.replace("```json", "").replace("```", "").trim();

			aiResponse.setSuccess(true);
			aiResponse.setResponse(text);
			aiResponse.setModelUsed(model);

		} catch (Exception e) {

			aiResponse.setSuccess(false);
			aiResponse.setRetryable(true);
			aiResponse.setErrorMessage(e.getMessage());

		}

		return aiResponse;

	}

}