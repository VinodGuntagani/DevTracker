package model;

import java.util.List;

public class AISubjectResponse {

	private String name;

	private List<AITopicResponse> topics;

	public String getName() {

		return name;

	}

	public void setName(String name) {

		this.name = name;

	}

	public List<AITopicResponse> getTopics() {

		return topics;

	}

	public void setTopics(List<AITopicResponse> topics) {

		this.topics = topics;

	}

}