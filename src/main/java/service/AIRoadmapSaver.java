package service;

import java.sql.Date;
import dao.AITimetableDAO;
import java.util.HashMap;
import java.util.Map;
import dao.RoadmapDAO;
import dao.SubjectDAO;
import dao.TopicDAO;
import dao.SubTopicDAO;

import model.*;

public class AIRoadmapSaver {

	public void save(int userId, AIRoadmapResponse aiRoadmap, Date startDate, Date targetDate) {

		try {

			RoadmapDAO roadmapDAO = new RoadmapDAO();

			SubjectDAO subjectDAO = new SubjectDAO();

			TopicDAO topicDAO = new TopicDAO();

			SubTopicDAO subDAO = new SubTopicDAO();

			AITimetableDAO aiDAO = new AITimetableDAO();

			// ROADMAP

			Roadmap roadmap = new Roadmap();

			Map<Integer, Integer> dayOrder = new HashMap<>();

			roadmap.setUserId(userId);

			roadmap.setTitle(aiRoadmap.getTitle());

			roadmap.setDescription(aiRoadmap.getDescription());

			roadmap.setStartDate(startDate);

			roadmap.setTargetDate(targetDate);

			int roadmapId = roadmapDAO.addRoadmapReturnId(roadmap);
			System.out.println("ROADMAP ID = " + roadmapId);

			int totalDays = (int) ((targetDate.getTime() - startDate.getTime()) / (1000 * 60 * 60 * 24)) + 1;

			int aiPlanId = aiDAO.createAIPlan(userId, roadmapId, totalDays);
			System.out.println("AI PLAN ID = " + aiPlanId);

			// SUBJECTS

			for (AISubjectResponse aiSubject : aiRoadmap.getSubjects()) {

				int subjectId = subjectDAO.addSubjectReturnId(roadmapId, aiSubject.getName());

				// TOPICS

				for (AITopicResponse aiTopic : aiSubject.getTopics()) {

					int topicId = topicDAO.addTopicReturnId(subjectId, aiTopic.getName());

					// SUBTOPICS

					for (AISubTopicResponse aiSub : aiTopic.getSubtopics()) {

						int difficultyValue = 2;

						if (aiSub.getDifficulty().equals("Easy")) {

							difficultyValue = 1;

						}

						else if (aiSub.getDifficulty().equals("Hard")) {

							difficultyValue = 3;

						}

						int weight = difficultyValue * aiSub.getMinutes();

						SubTopic sub = new SubTopic();

						sub.setTopicId(topicId);

						sub.setName(aiSub.getName());

						sub.setDifficulty(aiSub.getDifficulty());

						sub.setEstimatedMinutes(aiSub.getMinutes());

						sub.setWeight(weight);

						int subtopicId = subDAO.addSubTopicReturnId(sub);

						int day = aiSub.getDay();

						int order = dayOrder.getOrDefault(day, 0) + 1;

						dayOrder.put(day, order);

						aiDAO.addAITask(

								aiPlanId,

								subtopicId,

								day,

								order

						);

					}

				}

			}

		} catch (Exception e) {

			e.printStackTrace();

		}

	}

}