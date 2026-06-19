package service;

import java.util.List;

import dao.RoadmapDAO;
import dao.SubTopicDAO;

import model.SubTopic;
import model.AIAnalysis;

public class AIScheduleGenerator {

	public void generate(int userId, int roadmapId, int days) {

		RoadmapDAO roadmapDAO = new RoadmapDAO();

		SubTopicDAO subDAO = new SubTopicDAO();

		GeminiService gemini = new GeminiService();

		List<SubTopic> list = roadmapDAO.getAllSubTopics(roadmapId);

		for (SubTopic s : list) {

			AIAnalysis result = gemini.analyzeSubTopic(s.getName());

			if (result != null) {

				int value = 1;

				if (result.getDifficulty().equalsIgnoreCase("Medium")) {

					value = 2;

				}

				if (result.getDifficulty().equalsIgnoreCase("Hard")) {

					value = 3;

				}

				int weight = value * result.getMinutes();

				subDAO.updateAIAnalysis(

						s.getId(),

						result.getDifficulty(),

						result.getMinutes(),

						weight

				);

			}

		}

		// after AI updates difficulty/minutes
		// use your existing scheduler

		int dailyMinutes = 120;

		ScheduleGenerator generator = new ScheduleGenerator();

		generator.generate(userId, roadmapId, days, dailyMinutes);

	}

}