package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import model.Roadmap;
import model.SubTopic;
import util.DBConnection;

public class RoadmapDAO {

	// CREATE ROADMAP
	public boolean addRoadmap(Roadmap roadmap) {

		String sql = """
				INSERT INTO roadmaps
				(user_id, title, description, start_date, target_date, is_ai)
				VALUES (?, ?, ?, ?, ?, ?)
				""";

		try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setInt(1, roadmap.getUserId());
			ps.setString(2, roadmap.getTitle());
			ps.setString(3, roadmap.getDescription());
			ps.setDate(4, roadmap.getStartDate());
			ps.setDate(5, roadmap.getTargetDate());
			ps.setBoolean(6, false);

			return ps.executeUpdate() > 0;

		} catch (Exception e) {

			e.printStackTrace();

		}

		return false;
	}

	// GET USER ROADMAPS
	public List<Roadmap> getRoadmaps(int userId) {

		List<Roadmap> list = new ArrayList<>();

		String sql = """
				SELECT *
				FROM roadmaps
				WHERE user_id = ?
				AND is_deleted = false
				""";

		try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setInt(1, userId);

			try (ResultSet rs = ps.executeQuery()) {

				while (rs.next()) {

					Roadmap r = new Roadmap();

					r.setId(rs.getInt("id"));
					r.setTitle(rs.getString("title"));
					r.setDescription(rs.getString("description"));
					r.setStartDate(rs.getDate("start_date"));
					r.setTargetDate(rs.getDate("target_date"));
					r.setAi(rs.getBoolean("is_ai"));

					list.add(r);

				}

			}

		} catch (Exception e) {

			e.printStackTrace();

		}

		return list;

	}

	public void deleteRoadmap(int id) {

		try (Connection con = DBConnection.getConnection()) {

			con.setAutoCommit(false);

			try {

				// Delete roadmap
				try (PreparedStatement ps = con.prepareStatement("UPDATE roadmaps SET is_deleted=true WHERE id=?")) {

					ps.setInt(1, id);
					ps.executeUpdate();
				}

				// Delete subjects
				try (PreparedStatement ps = con
						.prepareStatement("UPDATE subjects SET is_deleted=true WHERE roadmap_id=?")) {

					ps.setInt(1, id);
					ps.executeUpdate();
				}

				// Delete topics
				try (PreparedStatement ps = con.prepareStatement("""
						UPDATE topics t
						JOIN subjects s
						ON t.subject_id = s.id
						SET t.is_deleted = true
						WHERE s.roadmap_id = ?
						""")) {

					ps.setInt(1, id);
					ps.executeUpdate();
				}

				// Delete subtopics
				try (PreparedStatement ps = con.prepareStatement("""
						UPDATE sub_topics st
						JOIN topics t
						ON st.topic_id = t.id
						JOIN subjects s
						ON t.subject_id = s.id
						SET st.is_deleted = true
						WHERE s.roadmap_id = ?
						""")) {

					ps.setInt(1, id);
					ps.executeUpdate();
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

	public void updateName(int id, String title, String description) {

		try (Connection con = DBConnection.getConnection();
				PreparedStatement ps = con.prepareStatement("UPDATE roadmaps SET title=?, description=? WHERE id=?")) {

			ps.setString(1, title);
			ps.setString(2, description);
			ps.setInt(3, id);

			ps.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public List<Roadmap> getDeletedRoadmaps(int userId) {

		List<Roadmap> list = new ArrayList<>();

		try (Connection con = DBConnection.getConnection();
				PreparedStatement ps = con
						.prepareStatement("SELECT * FROM roadmaps WHERE user_id=? AND is_deleted=true")) {

			ps.setInt(1, userId);

			try (ResultSet rs = ps.executeQuery()) {

				while (rs.next()) {

					Roadmap r = new Roadmap();

					r.setId(rs.getInt("id"));
					r.setTitle(rs.getString("title"));
					r.setDescription(rs.getString("description"));

					list.add(r);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;

	}

	public void restoreRoadmap(int id) {

		try (Connection con = DBConnection.getConnection()) {

			con.setAutoCommit(false);

			try {

				// Restore roadmap
				try (PreparedStatement ps = con
						.prepareStatement("UPDATE roadmaps SET is_deleted=true WHERE id=?".replace("true", "false"))) {

					ps.setInt(1, id);
					ps.executeUpdate();
				}

				// Restore subjects
				try (PreparedStatement ps = con.prepareStatement(
						"UPDATE subjects SET is_deleted=true WHERE roadmap_id=?".replace("true", "false"))) {

					ps.setInt(1, id);
					ps.executeUpdate();
				}

				// Restore topics
				try (PreparedStatement ps = con.prepareStatement("""
						UPDATE topics t
						JOIN subjects s
						ON t.subject_id = s.id
						SET t.is_deleted = false
						WHERE s.roadmap_id = ?
						""")) {

					ps.setInt(1, id);
					ps.executeUpdate();
				}

				// Restore subtopics
				try (PreparedStatement ps = con.prepareStatement("""
						UPDATE sub_topics st
						JOIN topics t
						ON st.topic_id = t.id
						JOIN subjects s
						ON t.subject_id = s.id
						SET st.is_deleted = false
						WHERE s.roadmap_id = ?
						""")) {

					ps.setInt(1, id);
					ps.executeUpdate();
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

	public List<SubTopic> getAllSubTopics(int roadmapId) {

		List<SubTopic> list = new ArrayList<>();

		String sql = """
				SELECT st.*
				FROM sub_topics st
				JOIN topics t
				ON st.topic_id = t.id
				JOIN subjects s
				ON t.subject_id = s.id
				WHERE s.roadmap_id = ?
				AND st.is_deleted = false
				ORDER BY st.weight DESC
				""";

		try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setInt(1, roadmapId);

			try (ResultSet rs = ps.executeQuery()) {

				while (rs.next()) {
					SubTopic st = new SubTopic();

					st.setId(rs.getInt("id"));
					st.setTopicId(rs.getInt("topic_id"));
					st.setName(rs.getString("name"));
					st.setCompleted(rs.getBoolean("completed"));
					st.setDifficulty(rs.getString("difficulty"));
					st.setEstimatedMinutes(rs.getInt("estimated_minutes"));
					st.setWeight(rs.getInt("weight"));
					st.setAiLearning(rs.getString("ai_learning"));

					list.add(st);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;

	}

	public Roadmap getRoadmapById(int id) {

		Roadmap roadmap = null;

		try (Connection con = DBConnection.getConnection();
				PreparedStatement ps = con.prepareStatement("SELECT * FROM roadmaps WHERE id=?")) {

			ps.setInt(1, id);

			try (ResultSet rs = ps.executeQuery()) {

				if (rs.next()) {

					roadmap = new Roadmap();

					roadmap.setId(rs.getInt("id"));
					roadmap.setTitle(rs.getString("title"));
					roadmap.setDescription(rs.getString("description"));
					roadmap.setStartDate(rs.getDate("start_date"));
					roadmap.setTargetDate(rs.getDate("target_date"));

				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return roadmap;

	}

	public int addRoadmapReturnId(Roadmap r) {

		int id = 0;

		String sql = "INSERT INTO roadmaps(user_id, title, description, start_date, target_date, is_ai) "
				+ "VALUES (?, ?, ?, ?, ?, ?)";

		try (Connection con = DBConnection.getConnection();
				PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

			ps.setInt(1, r.getUserId());
			ps.setString(2, r.getTitle());
			ps.setString(3, r.getDescription());
			ps.setDate(4, r.getStartDate());
			ps.setDate(5, r.getTargetDate());
			ps.setBoolean(6, true);

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

	public List<Roadmap> getAIRoadmaps(int userId) {

		List<Roadmap> list = new ArrayList<>();

		String sql = """
				SELECT *
				FROM roadmaps
				WHERE user_id = ?
				AND is_ai = true
				AND is_deleted = false
				""";

		try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setInt(1, userId);

			try (ResultSet rs = ps.executeQuery()) {

				while (rs.next()) {

					Roadmap r = new Roadmap();

					r.setId(rs.getInt("id"));
					r.setTitle(rs.getString("title"));
					r.setDescription(rs.getString("description"));
					r.setStartDate(rs.getDate("start_date"));
					r.setTargetDate(rs.getDate("target_date"));
					r.setAi(rs.getBoolean("is_ai"));

					list.add(r);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;

	}
}
