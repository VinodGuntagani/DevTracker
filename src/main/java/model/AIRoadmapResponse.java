package model;

import java.util.List;

public class AIRoadmapResponse {

	private String title;

	private String description;

	private List<AISubjectResponse> subjects;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<AISubjectResponse> getSubjects() {
		return subjects;
	}

	public void setSubjects(List<AISubjectResponse> subjects) {

		this.subjects = subjects;

	}

}