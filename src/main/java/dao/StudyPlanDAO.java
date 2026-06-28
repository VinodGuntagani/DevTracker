package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import model.DailyTask;
import model.StudyPlan;
import util.DBConnection;

public class StudyPlanDAO {

	public int createPlan(StudyPlan plan) {

		int planId = 0;

		String sql = """
				INSERT INTO study_plan
				(user_id, roadmap_id, total_days, start_date, daily_minutes)
				VALUES (?, ?, ?, ?, ?)
				""";

		try (Connection con = DBConnection.getConnection();
				PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

			ps.setInt(1, plan.getUserId());
			ps.setInt(2, plan.getRoadmapId());
			ps.setInt(3, plan.getTotalDays());
			ps.setDate(4, plan.getStartDate());
			ps.setInt(5, plan.getDailyMinutes());

			ps.executeUpdate();

			try (ResultSet rs = ps.getGeneratedKeys()) {
				if (rs.next()) {
					planId = rs.getInt(1);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return planId;

	}

	public void addTask(DailyTask task) {

		String sql = """
				INSERT INTO daily_tasks
				(plan_id,
				 subtopic_id,
				 day_number,
				 task_order,
				 weight,
				 schedule_date,
				 planned_minutes,
				 completed,
				 status,
				 start_time,
				 end_time)
				VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
				""";

		try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setInt(1, task.getPlanId());
			ps.setInt(2, task.getSubtopicId());
			ps.setInt(3, task.getDayNumber());
			ps.setInt(4, task.getTaskOrder());
			ps.setInt(5, task.getWeight());
			ps.setDate(6, task.getScheduleDate());
			ps.setInt(7, task.getPlannedMinutes());
			ps.setBoolean(8, task.isCompleted());
			ps.setString(9, "NOT_STARTED");
			ps.setString(10, task.getStartTime());
			ps.setString(11, task.getEndTime());

			ps.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public List<DailyTask> getTasks(int roadmapId) {

		List<DailyTask> list = new ArrayList<>();

		String sql = """
				SELECT
					dt.*,
					st.name AS subtopic_name,
					st.difficulty,
					st.estimated_minutes,
					t.name AS topic_name,
					s.name AS subject_name
				FROM daily_tasks dt
				JOIN study_plan sp
					ON dt.plan_id = sp.id
				JOIN sub_topics st
					ON dt.subtopic_id = st.id
				JOIN topics t
					ON st.topic_id = t.id
				JOIN subjects s
					ON t.subject_id = s.id
				WHERE sp.roadmap_id = ?
				ORDER BY
					dt.schedule_date,
					dt.task_order
				""";

		try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setInt(1, roadmapId);

			try (ResultSet rs = ps.executeQuery()) {

				while (rs.next()) {

					DailyTask task = new DailyTask();

					task.setId(rs.getInt("id"));
					task.setPlanId(rs.getInt("plan_id"));
					task.setSubtopicId(rs.getInt("subtopic_id"));
					task.setDayNumber(rs.getInt("day_number"));
					task.setTaskOrder(rs.getInt("task_order"));
					task.setWeight(rs.getInt("weight"));
					task.setScheduleDate(rs.getDate("schedule_date"));
					task.setPlannedMinutes(rs.getInt("planned_minutes"));
					task.setCompleted(rs.getBoolean("completed"));
					task.setSubtopicName(rs.getString("subtopic_name"));
					task.setTopicName(rs.getString("topic_name"));
					task.setSubjectName(rs.getString("subject_name"));
					task.setDifficulty(rs.getString("difficulty"));
					task.setEstimatedMinutes(rs.getInt("estimated_minutes"));
					task.setStatus(rs.getString("status"));
					task.setStartTime(rs.getString("start_time"));
					task.setEndTime(rs.getString("end_time"));

					list.add(task);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;

	}

	public void updateTaskStatus(int taskId, boolean completed) {

		String sql = "UPDATE daily_tasks SET completed = ? WHERE id = ?";

		try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setBoolean(1, completed);
			ps.setInt(2, taskId);

			ps.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void updateTaskState(int taskId, String status) {

		String sql = "UPDATE daily_tasks SET status = ? WHERE id = ?";

		try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setString(1, status);
			ps.setInt(2, taskId);

			ps.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void deleteExistingPlan(int roadmapId) {

		try (Connection con = DBConnection.getConnection()) {

			con.setAutoCommit(false);

			try {

				// Delete old tasks first
				String sql1 = """
						DELETE dt
						FROM daily_tasks dt
						JOIN study_plan sp
							ON dt.plan_id = sp.id
						WHERE sp.roadmap_id = ?
						""";

				try (PreparedStatement ps1 = con.prepareStatement(sql1)) {

					ps1.setInt(1, roadmapId);
					ps1.executeUpdate();
				}

				// Delete old plan
				String sql2 = "DELETE FROM study_plan WHERE roadmap_id = ?";

				try (PreparedStatement ps2 = con.prepareStatement(sql2)) {

					ps2.setInt(1, roadmapId);
					ps2.executeUpdate();
				}

				con.commit();

			} catch (Exception e) {

				con.rollback();
				throw e;

			}

		} catch (Exception e) {

			e.printStackTrace();

		}

	}

	public void updateTaskBySubTopic(int subtopicId, boolean completed) {

		String sql = """
				UPDATE daily_tasks
				SET completed = ?,
				    status = ?
				WHERE subtopic_id = ?
				""";

		try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setBoolean(1, completed);
			ps.setString(2, completed ? "COMPLETED" : "NOT_STARTED");
			ps.setInt(3, subtopicId);

			ps.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void addNewSubtopicToSchedule(int roadmapId, int subtopicId, int weight) {

		try (Connection con = DBConnection.getConnection()) {

			// Get active plan
			String planSql = """
					SELECT id
					FROM study_plan
					WHERE roadmap_id = ?
					""";

			int planId;

			try (PreparedStatement ps = con.prepareStatement(planSql)) {

				ps.setInt(1, roadmapId);

				try (ResultSet rs = ps.executeQuery()) {

					if (!rs.next()) {
						return;
					}

					planId = rs.getInt("id");
				}
			}

			// Find last day
			String daySql = """
					SELECT MAX(day_number) AS lastDay
					FROM daily_tasks
					WHERE plan_id = ?
					""";

			int newDay = 1;

			try (PreparedStatement ps = con.prepareStatement(daySql)) {

				ps.setInt(1, planId);

				try (ResultSet rs = ps.executeQuery()) {

					if (rs.next()) {
						newDay = rs.getInt("lastDay") + 1;
					}
				}
			}

			// Insert new task
			String insertSql = """
					INSERT INTO daily_tasks
					(plan_id, subtopic_id, day_number,
					 task_order, weight, completed, status)
					VALUES (?, ?, ?, ?, ?, ?, ?)
					""";

			try (PreparedStatement ps = con.prepareStatement(insertSql)) {

				ps.setInt(1, planId);
				ps.setInt(2, subtopicId);
				ps.setInt(3, newDay);
				ps.setInt(4, 1);
				ps.setInt(5, weight);
				ps.setBoolean(6, false);
				ps.setString(7, "NOT_STARTED");

				ps.executeUpdate();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}