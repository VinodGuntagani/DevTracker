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

				String error = root.getAsJsonObject("error").get("message").getAsString();

				aiResponse.setSuccess(false);
				aiResponse.setErrorMessage(error);

				String lower = error.toLowerCase();

				if (lower.contains("quota") || lower.contains("rate") || lower.contains("429")
						|| lower.contains("resource exhausted") || lower.contains("temporarily")) {

					aiResponse.setRetryable(true);

				} else {

					aiResponse.setRetryable(false);

				}

				return aiResponse;
			}

			String text = root.getAsJsonArray("candidates").get(0).getAsJsonObject().getAsJsonObject("content")
					.getAsJsonArray("parts").get(0).getAsJsonObject().get("text").getAsString();

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