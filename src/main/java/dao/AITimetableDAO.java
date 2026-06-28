package dao;

import java.sql.*;
import java.util.*;

import model.AIStudyPlan;
import model.AIDailyTask;
import model.StudyPlan;
import model.DailyTask;

import util.DBConnection;

public class AITimetableDAO {

	public int createPlan(AIStudyPlan plan) {

		int id = 0;

		String sql = """
				INSERT INTO ai_study_plan
				(user_id, roadmap_id, total_days)
				VALUES (?, ?, ?)
				""";

		try (Connection con = DBConnection.getConnection();
				PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

			ps.setInt(1, plan.getUserId());
			ps.setInt(2, plan.getRoadmapId());
			ps.setInt(3, plan.getTotalDays());

			ps.executeUpdate();

			try (ResultSet rs = ps.getGeneratedKeys()) {
				if (rs.next()) {
					id = rs.getInt(1);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return id;

	}

	public void addTask(AIDailyTask task) {

		String sql = """
				INSERT INTO ai_daily_tasks
				(ai_plan_id, subtopic_id, day_number, task_order)
				VALUES (?, ?, ?, ?)
				""";

		try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setInt(1, task.getPlanId());
			ps.setInt(2, task.getSubtopicId());
			ps.setInt(3, task.getDayNumber());
			ps.setInt(4, task.getTaskOrder());

			ps.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void deleteExistingPlan(int roadmapId) {

		String sql = """
				DELETE FROM ai_study_plan
				WHERE roadmap_id = ?
				""";

		try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setInt(1, roadmapId);

			ps.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public List<AIDailyTask> getTasks(int roadmapId) {

		List<AIDailyTask> list = new ArrayList<>();

		String sql = """
				SELECT
					adt.*,
					st.name
				FROM ai_daily_tasks adt
				JOIN ai_study_plan asp
					ON adt.ai_plan_id = asp.id
				JOIN sub_topics st
					ON adt.subtopic_id = st.id
				WHERE asp.roadmap_id = ?
				ORDER BY
					adt.day_number,
					adt.task_order
				""";

		try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setInt(1, roadmapId);

			try (ResultSet rs = ps.executeQuery()) {

				while (rs.next()) {

					AIDailyTask task = new AIDailyTask();

					task.setId(rs.getInt("id"));
					task.setPlanId(rs.getInt("ai_plan_id"));
					task.setSubtopicId(rs.getInt("subtopic_id"));
					task.setDayNumber(rs.getInt("day_number"));
					task.setTaskOrder(rs.getInt("task_order"));
					task.setCompleted(rs.getBoolean("completed"));
					task.setSubtopicName(rs.getString("name"));

					list.add(task);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;

	}

	public int createAIPlan(int userId, int roadmapId, int days) {

		int id = 0;

		String sql = """
				INSERT INTO ai_study_plan
				(user_id, roadmap_id, total_days)
				VALUES (?, ?, ?)
				""";

		try (Connection con = DBConnection.getConnection();
				PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

			ps.setInt(1, userId);
			ps.setInt(2, roadmapId);
			ps.setInt(3, days);

			ps.executeUpdate();

			try (ResultSet rs = ps.getGeneratedKeys()) {
				if (rs.next()) {
					id = rs.getInt(1);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return id;

	}

	public void addAITask(int aiPlanId, int subtopicId, int day, int order) {

		String sql = """
				INSERT INTO ai_daily_tasks
				(ai_plan_id, subtopic_id, day_number, task_order)
				VALUES (?, ?, ?, ?)
				""";

		try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setInt(1, aiPlanId);
			ps.setInt(2, subtopicId);
			ps.setInt(3, day);
			ps.setInt(4, order);

			ps.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void updateCompleted(int taskId, boolean completed) {

		String sql = """
				UPDATE ai_daily_tasks
				SET completed = ?
				WHERE id = ?
				""";

		try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setBoolean(1, completed);
			ps.setInt(2, taskId);

			ps.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public int getAIProgress(int roadmapId) {

		int progress = 0;

		String sql = """
				SELECT
					COUNT(*) AS total,
					SUM(completed) AS done
				FROM ai_daily_tasks adt
				JOIN ai_study_plan asp
					ON adt.ai_plan_id = asp.id
				WHERE asp.roadmap_id = ?
				""";

		try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setInt(1, roadmapId);

			try (ResultSet rs = ps.executeQuery()) {

				if (rs.next()) {

					int total = rs.getInt("total");
					int done = rs.getInt("done");

					if (total > 0) {
						progress = (done * 100) / total;
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return progress;

	}

	public void syncToMainSchedule(int aiPlanId, int userId, int roadmapId, int days) {

		try {

			StudyPlanDAO studyDAO = new StudyPlanDAO();

			// Remove old normal schedule
			studyDAO.deleteExistingPlan(roadmapId);

			// Create normal study plan
			StudyPlan plan = new StudyPlan();

			plan.setUserId(userId);
			plan.setRoadmapId(roadmapId);
			plan.setTotalDays(days);
			plan.setStartDate(new java.sql.Date(System.currentTimeMillis()));
			plan.setDailyMinutes(120);

			int planId = studyDAO.createPlan(plan);

			String sql = """
					SELECT
						adt.*,
						st.estimated_minutes,
						st.weight
					FROM ai_daily_tasks adt
					JOIN sub_topics st
						ON adt.subtopic_id = st.id
					WHERE adt.ai_plan_id = ?
					ORDER BY
						adt.day_number,
						adt.task_order
					""";

			try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

				ps.setInt(1, aiPlanId);

				try (ResultSet rs = ps.executeQuery()) {

					while (rs.next()) {

						DailyTask task = new DailyTask();

						task.setPlanId(planId);
						task.setSubtopicId(rs.getInt("subtopic_id"));
						task.setDayNumber(rs.getInt("day_number"));

						java.time.LocalDate date = java.time.LocalDate.now().plusDays(rs.getInt("day_number") - 1);

						task.setScheduleDate(java.sql.Date.valueOf(date));

						task.setTaskOrder(rs.getInt("task_order"));
						task.setWeight(rs.getInt("weight"));
						task.setPlannedMinutes(rs.getInt("estimated_minutes"));
						task.setCompleted(false);
						task.setStatus("NOT_STARTED");
						task.setStartTime("09:00");
						task.setEndTime("11:00");

						studyDAO.addTask(task);
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public boolean hasAITimetable(int roadmapId) {

		String sql = """
				SELECT COUNT(*) AS total
				FROM ai_daily_tasks adt
				JOIN ai_study_plan asp
					ON adt.ai_plan_id = asp.id
				WHERE asp.roadmap_id = ?
				""";

		try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setInt(1, roadmapId);

			try (ResultSet rs = ps.executeQuery()) {

				if (rs.next()) {
					return rs.getInt("total") > 0;
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;

	}

	public int getAIPlanId(int roadmapId) {

		int id = 0;

		String sql = """
				SELECT id
				FROM ai_study_plan
				WHERE roadmap_id = ?
				ORDER BY id DESC
				LIMIT 1
				""";

		try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setInt(1, roadmapId);

			try (ResultSet rs = ps.executeQuery()) {

				if (rs.next()) {
					id = rs.getInt("id");
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return id;

	}
}