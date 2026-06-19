package dao;

import java.sql.*;
import java.util.*;
import java.sql.Statement;

import model.AIStudyPlan;
import model.AIDailyTask;
import model.StudyPlan;
import model.DailyTask;

import util.DBConnection;

public class AITimetableDAO {

	public int createPlan(AIStudyPlan plan) {

		int id = 0;

		try {

			Connection con = DBConnection.getConnection();

			String sql = """
					INSERT INTO ai_study_plan
					(user_id,roadmap_id,total_days)

					VALUES(?,?,?)
					""";

			PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

			ps.setInt(1, plan.getUserId());

			ps.setInt(2, plan.getRoadmapId());

			ps.setInt(3, plan.getTotalDays());

			ps.executeUpdate();

			ResultSet rs = ps.getGeneratedKeys();

			if (rs.next()) {

				id = rs.getInt(1);

			}

		} catch (Exception e) {

			e.printStackTrace();

		}

		return id;

	}

	public void addTask(AIDailyTask task) {

		try {

			Connection con = DBConnection.getConnection();

			String sql = """
					INSERT INTO ai_daily_tasks
					(ai_plan_id,subtopic_id,day_number,task_order)

					VALUES(?,?,?,?)
					""";

			PreparedStatement ps = con.prepareStatement(sql);

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

		try {

			Connection con = DBConnection.getConnection();

			String sql = """
					DELETE FROM ai_study_plan
					WHERE roadmap_id=?
					""";

			PreparedStatement ps = con.prepareStatement(sql);

			ps.setInt(1, roadmapId);

			ps.executeUpdate();

		} catch (Exception e) {

			e.printStackTrace();

		}

	}

	public List<AIDailyTask> getTasks(int roadmapId) {

		List<AIDailyTask> list = new ArrayList<>();

		try {

			Connection con = DBConnection.getConnection();

			String sql = """
					SELECT
					adt.*,
					st.name

					FROM ai_daily_tasks adt


					JOIN ai_study_plan asp
					ON adt.ai_plan_id = asp.id


					JOIN sub_topics st
					ON adt.subtopic_id = st.id


					WHERE asp.roadmap_id=?


					ORDER BY
					adt.day_number,
					adt.task_order
					""";

			PreparedStatement ps = con.prepareStatement(sql);

			ps.setInt(1, roadmapId);

			ResultSet rs = ps.executeQuery();

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

		} catch (Exception e) {

			e.printStackTrace();

		}

		return list;

	}

	public int createAIPlan(int userId, int roadmapId, int days) {

		int id = 0;

		try {

			Connection con = DBConnection.getConnection();

			String sql = """
					INSERT INTO ai_study_plan
					(user_id,roadmap_id,total_days)

					VALUES(?,?,?)
					""";

			PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

			ps.setInt(1, userId);

			ps.setInt(2, roadmapId);

			ps.setInt(3, days);

			ps.executeUpdate();

			ResultSet rs = ps.getGeneratedKeys();

			if (rs.next()) {

				id = rs.getInt(1);

			}

		} catch (Exception e) {

			e.printStackTrace();

		}

		return id;

	}

	public void addAITask(int aiPlanId, int subtopicId, int day, int order) {

		try {

			Connection con = DBConnection.getConnection();

			String sql = """
					INSERT INTO ai_daily_tasks
					(ai_plan_id,subtopic_id,day_number,task_order)

					VALUES(?,?,?,?)
					""";

			PreparedStatement ps = con.prepareStatement(sql);

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

		try {

			Connection con = DBConnection.getConnection();

			String sql = """
					UPDATE ai_daily_tasks
					SET completed=?
					WHERE id=?
					""";

			PreparedStatement ps = con.prepareStatement(sql);

			ps.setBoolean(1, completed);

			ps.setInt(2, taskId);

			ps.executeUpdate();

		} catch (Exception e) {

			e.printStackTrace();

		}

	}

	public int getAIProgress(int roadmapId) {

		int progress = 0;

		try {

			Connection con = DBConnection.getConnection();

			String sql = """
					SELECT
					COUNT(*) total,
					SUM(completed) done

					FROM ai_daily_tasks adt

					JOIN ai_study_plan asp
					ON adt.ai_plan_id = asp.id

					WHERE asp.roadmap_id=?
					""";

			PreparedStatement ps = con.prepareStatement(sql);

			ps.setInt(1, roadmapId);

			ResultSet rs = ps.executeQuery();

			if (rs.next()) {

				int total = rs.getInt("total");

				int done = rs.getInt("done");

				if (total > 0) {

					progress = (done * 100) / total;

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

			// remove old normal schedule

			studyDAO.deleteExistingPlan(roadmapId);

			// create normal study plan

			StudyPlan plan = new StudyPlan();

			plan.setUserId(userId);

			plan.setRoadmapId(roadmapId);

			plan.setTotalDays(days);

			plan.setStartDate(new java.sql.Date(System.currentTimeMillis()));

			plan.setDailyMinutes(120);

			int planId = studyDAO.createPlan(plan);

			Connection con = DBConnection.getConnection();

			String sql = """
					SELECT
					adt.*,
					st.estimated_minutes,
					st.weight

					FROM ai_daily_tasks adt

					JOIN sub_topics st
					ON adt.subtopic_id = st.id

					WHERE adt.ai_plan_id=?

					ORDER BY
					adt.day_number,
					adt.task_order
					""";

			PreparedStatement ps = con.prepareStatement(sql);

			ps.setInt(1, aiPlanId);

			ResultSet rs = ps.executeQuery();

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

		} catch (Exception e) {

			e.printStackTrace();

		}

	}

	public boolean hasAITimetable(int roadmapId) {

		try {

			Connection con = DBConnection.getConnection();

			String sql = """
					SELECT COUNT(*) total

					FROM ai_daily_tasks adt

					JOIN ai_study_plan asp
					ON adt.ai_plan_id = asp.id

					WHERE asp.roadmap_id=?
					""";

			PreparedStatement ps = con.prepareStatement(sql);

			ps.setInt(1, roadmapId);

			ResultSet rs = ps.executeQuery();

			if (rs.next()) {

				return rs.getInt("total") > 0;

			}

		} catch (Exception e) {

			e.printStackTrace();

		}

		return false;

	}

	public int getAIPlanId(int roadmapId) {

		int id = 0;

		try {

			Connection con = DBConnection.getConnection();

			String sql = """
					SELECT id
					FROM ai_study_plan
					WHERE roadmap_id=?
					ORDER BY id DESC
					LIMIT 1
					""";

			PreparedStatement ps = con.prepareStatement(sql);

			ps.setInt(1, roadmapId);

			ResultSet rs = ps.executeQuery();

			if (rs.next()) {

				id = rs.getInt("id");

			}

		} catch (Exception e) {

			e.printStackTrace();

		}

		return id;

	}
}