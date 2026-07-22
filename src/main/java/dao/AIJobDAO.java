package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import model.AIJob;
import util.DBConnection;

public class AIJobDAO {
	public int createJob(AIJob job) {

		String sql = "INSERT INTO ai_jobs "
				+ "(user_id, roadmap_id, job_type, status, total_tasks, completed_tasks, failed_tasks) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?)";

		try (Connection con = DBConnection.getConnection();
				PreparedStatement ps = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

			ps.setInt(1, job.getUserId());
			ps.setInt(2, job.getRoadmapId());
			ps.setString(3, job.getJobType());
			ps.setString(4, job.getStatus());
			ps.setInt(5, job.getTotalTasks());
			ps.setInt(6, job.getCompletedTasks());
			ps.setInt(7, job.getFailedTasks());

			ps.executeUpdate();

			ResultSet rs = ps.getGeneratedKeys();

			if (rs.next()) {
				return rs.getInt(1);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return -1;
	}

	public void updateTotalTasks(int jobId, int totalTasks) {

		String sql = "UPDATE ai_jobs SET total_tasks = ? WHERE id = ?";

		try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setInt(1, totalTasks);
			ps.setInt(2, jobId);

			ps.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void incrementCompletedTasks(int jobId) {

		String sql = """
				UPDATE ai_jobs
				SET completed_tasks = completed_tasks + 1
				WHERE id = ?
				""";

		try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setInt(1, jobId);

			ps.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void incrementFailedTasks(int jobId) {

		String sql = """
				UPDATE ai_jobs
				SET failed_tasks = failed_tasks + 1
				WHERE id = ?
				""";

		try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setInt(1, jobId);

			ps.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void markCompleted(int jobId) {

		String sql = """
				UPDATE ai_jobs
				SET
				    status = 'COMPLETED',
				    finished_at = NOW()
				WHERE id = ?
				""";

		try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setInt(1, jobId);

			ps.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public AIJob getJob(int jobId) {

		String sql = """
				SELECT *
				FROM ai_jobs
				WHERE id = ?
				""";

		try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setInt(1, jobId);

			try (ResultSet rs = ps.executeQuery()) {

				if (rs.next()) {

					AIJob job = new AIJob();

					job.setId(rs.getInt("id"));
					job.setUserId(rs.getInt("user_id"));
					job.setRoadmapId(rs.getInt("roadmap_id"));
					job.setCurrentSubtopicId((Integer) rs.getObject("current_subtopic_id"));
					job.setJobType(rs.getString("job_type"));
					job.setStatus(rs.getString("status"));
					job.setTotalTasks(rs.getInt("total_tasks"));
					job.setCompletedTasks(rs.getInt("completed_tasks"));
					job.setFailedTasks(rs.getInt("failed_tasks"));

					return job;
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public boolean isFinished(int jobId) {

		AIJob job = getJob(jobId);

		if (job == null) {
			return true;
		}

		return job.getCompletedTasks() + job.getFailedTasks() >= job.getTotalTasks();
	}
}