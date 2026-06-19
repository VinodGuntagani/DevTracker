package service;

import java.sql.Date;
import java.util.List;

import dao.RoadmapDAO;
import dao.StudyPlanDAO;

import model.SubTopic;
import model.StudyPlan;
import model.DailyTask;

public class ScheduleGenerator {

	public void generate(int userId, int roadmapId, int days, int dailyMinutes) {

		RoadmapDAO roadmapDAO = new RoadmapDAO();

		StudyPlanDAO planDAO = new StudyPlanDAO();

		List<SubTopic> subtopics = roadmapDAO.getAllSubTopics(roadmapId);

		// create plan

		StudyPlan plan = new StudyPlan();

		plan.setUserId(userId);

		plan.setRoadmapId(roadmapId);

		plan.setTotalDays(days);

		plan.setStartDate(new Date(System.currentTimeMillis()));

		plan.setDailyMinutes(dailyMinutes);

		int planId = planDAO.createPlan(plan);

		// calculate total weight

		int totalWeight = 0;

		for (SubTopic s : subtopics) {

			totalWeight += s.getWeight();

		}

		int dailyCapacity = dailyMinutes;

		if (dailyCapacity == 0) {

			dailyCapacity = 1;

		}

		int currentDay = 1;

		int currentMinutes = 0;

		int order = 1;

		for (SubTopic s : subtopics) {

			if (currentMinutes != 0

					&&

					currentMinutes + s.getEstimatedMinutes() > dailyCapacity

					&&

					currentDay < days) {

				currentDay++;

				currentMinutes = 0;

				order = 1;

			}

			DailyTask task = new DailyTask();

			task.setPlanId(planId);

			task.setSubtopicId(s.getId());

			task.setDayNumber(currentDay);

			task.setScheduleDate(new Date(System.currentTimeMillis() + (long) (currentDay - 1) * 24 * 60 * 60 * 1000));

			task.setTaskOrder(order);

			task.setWeight(s.getWeight());

			task.setPlannedMinutes(s.getEstimatedMinutes());

			task.setCompleted(s.isCompleted());

			task.setStatus("NOT_STARTED");

			if (order <= 2) {

				task.setStartTime("09:00");

				task.setEndTime("11:00");

			} else {

				task.setStartTime("16:00");

				task.setEndTime("18:00");

			}

			planDAO.addTask(task);

			currentMinutes += s.getEstimatedMinutes();

			order++;

		}

	}

}