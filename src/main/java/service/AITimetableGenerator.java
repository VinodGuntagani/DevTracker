package service;

import java.util.List;

import com.google.gson.Gson;

import dao.RoadmapDAO;
import dao.AITimetableDAO;

import model.SubTopic;
import model.AIStudyPlan;
import model.AIDailyTask;

import model.AIScheduleResponse;
import model.AIScheduleDay;

public class AITimetableGenerator {

	public void generate(int userId, int roadmapId, int days) {

		try {

			RoadmapDAO roadmapDAO = new RoadmapDAO();

			AITimetableDAO aiDAO = new AITimetableDAO();

			// remove old AI timetable

			aiDAO.deleteExistingPlan(roadmapId);

			List<SubTopic> subtopics = roadmapDAO.getAllSubTopics(roadmapId);

			Gson gson = new Gson();

			String roadmapJson = gson.toJson(subtopics);

			GeminiService gemini = new GeminiService();

			AIScheduleResponse aiResult = gemini.generateAITimetable(roadmapJson);

			if (aiResult == null) {

				return;

			}

			AIStudyPlan plan = new AIStudyPlan();

			plan.setUserId(userId);

			plan.setRoadmapId(roadmapId);

			plan.setTotalDays(days);

			int planId = aiDAO.createPlan(plan);

			for (AIScheduleDay day : aiResult.getSchedule()) {

				int order = 1;

				for (int subId : day.getTasks()) {

					AIDailyTask task = new AIDailyTask();

					task.setPlanId(planId);

					task.setSubtopicId(subId);

					task.setDayNumber(day.getDay());

					task.setTaskOrder(order);

					aiDAO.addTask(task);

					order++;

				}

			}
			// copy AI timetable into main schedule
			aiDAO.syncToMainSchedule(planId, userId, roadmapId, days);

		} catch (Exception e) {

			e.printStackTrace();

		}

	}

}