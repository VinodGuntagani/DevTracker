package service;

import java.net.URI;
import java.net.URLEncoder;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import java.nio.charset.StandardCharsets;

import java.util.*;

import com.google.gson.*;

import model.LearningResource;

public class YouTubeService {

	private static final String API_KEY = System.getenv("YOUTUBE_API_KEY");

	public List<LearningResource> searchVideos(int subtopicId, String query) {

		List<LearningResource> list = new ArrayList<>();

		try {

			String encodedQuery = URLEncoder.encode(query, StandardCharsets.UTF_8);

			String url = "https://www.googleapis.com/youtube/v3/search" + "?part=snippet" + "&type=video"
					+ "&maxResults=8" + "&q=" + encodedQuery + "&key=" + API_KEY;

			HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).GET().build();

			HttpClient client = HttpClient.newHttpClient();

			HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

			JsonObject root = JsonParser.parseString(response.body()).getAsJsonObject();

			JsonArray items = root.getAsJsonArray("items");

			int rank = 1;

			for (JsonElement element : items) {

				JsonObject item = element.getAsJsonObject();

				JsonObject id = item.getAsJsonObject("id");

				JsonObject snippet = item.getAsJsonObject("snippet");

				LearningResource r = new LearningResource();

				r.setSubtopicId(subtopicId);

				r.setVideoId(id.get("videoId").getAsString());

				r.setTitle(snippet.get("title").getAsString());

				r.setChannelName(snippet.get("channelTitle").getAsString());

				r.setThumbnail(
						snippet.getAsJsonObject("thumbnails").getAsJsonObject("medium").get("url").getAsString());

				r.setRankNo(rank);

				if (rank == 1) {

					r.setReason("🥇 Best match");

				} else {

					r.setReason("Recommended learning resource");

				}

				list.add(r);

				rank++;

			}

		} catch (Exception e) {

			e.printStackTrace();

		}

		return list;

	}

}