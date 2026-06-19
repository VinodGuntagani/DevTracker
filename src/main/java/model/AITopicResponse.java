package model;

import java.util.List;

public class AITopicResponse {

	private String name;

	private List<AISubTopicResponse> subtopics;

	public String getName() {

		return name;

	}

	public void setName(String name) {

		this.name = name;

	}

	public List<AISubTopicResponse> getSubtopics() {

		return subtopics;

	}

	public void setSubtopics(List<AISubTopicResponse> subtopics) {

		this.subtopics = subtopics;

	}

}