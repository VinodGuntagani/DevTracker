package dao;

import java.sql.*;
import java.util.*;
import java.sql.Statement;

import model.Topic;
import util.DBConnection;

public class TopicDAO {

	public boolean addTopic(Topic topic) {

		String sql = "INSERT INTO topics(subject_id, name) VALUES(?, ?)";

		try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setInt(1, topic.getSubjectId());
			ps.setString(2, topic.getName());

			return ps.executeUpdate() > 0;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;

	}

	public List<Topic> getTopics(int subjectId) {

		List<Topic> list = new ArrayList<>();

		String sql = """
				SELECT *
				FROM topics
				WHERE subject_id = ?
				AND is_deleted = false
				""";

		try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setInt(1, subjectId);

			try (ResultSet rs = ps.executeQuery()) {

				while (rs.next()) {

					Topic t = new Topic();

					t.setId(rs.getInt("id"));
					t.setSubjectId(rs.getInt("subject_id"));
					t.setName(rs.getString("name"));

					list.add(t);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;

	}

	public int getSubjectProgress(int subjectId) {

		int progress = 0;

		String sql = """
				SELECT
					COUNT(st.id) AS total,
					SUM(st.completed) AS done
				FROM topics t
				LEFT JOIN sub_topics st
					ON t.id = st.topic_id
				WHERE t.subject_id = ?
				AND t.is_deleted = false
				AND st.is_deleted = false
				""";

		try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setInt(1, subjectId);

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

	public void deleteTopic(int id) {

		try (Connection con = DBConnection.getConnection()) {

			con.setAutoCommit(false);

			try {

				// Delete topic
				String sql1 = "UPDATE topics SET is_deleted = true WHERE id = ?";

				try (PreparedStatement ps1 = con.prepareStatement(sql1)) {

					ps1.setInt(1, id);
					ps1.executeUpdate();
				}

				// Delete subtopics inside topic
				String sql2 = "UPDATE sub_topics SET is_deleted = true WHERE topic_id = ?";

				try (PreparedStatement ps2 = con.prepareStatement(sql2)) {

					ps2.setInt(1, id);
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

	public void updateName(int id, String name) {

		String sql = "UPDATE topics SET name = ? WHERE id = ?";

		try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setString(1, name);
			ps.setInt(2, id);

			ps.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public List<Topic> getDeletedTopics(int subjectId) {

		List<Topic> list = new ArrayList<>();

		String sql = """
				SELECT *
				FROM topics
				WHERE subject_id = ?
				AND is_deleted = true
				""";

		try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setInt(1, subjectId);

			try (ResultSet rs = ps.executeQuery()) {

				while (rs.next()) {

					Topic t = new Topic();

					t.setId(rs.getInt("id"));
					t.setSubjectId(rs.getInt("subject_id"));
					t.setName(rs.getString("name"));

					list.add(t);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;

	}

	public void restoreTopic(int id) {

		try (Connection con = DBConnection.getConnection()) {

			con.setAutoCommit(false);

			try {

				// Restore topic
				String sql1 = "UPDATE topics SET is_deleted = false WHERE id = ?";

				try (PreparedStatement ps1 = con.prepareStatement(sql1)) {

					ps1.setInt(1, id);
					ps1.executeUpdate();
				}

				// Restore subtopics
				String sql2 = "UPDATE sub_topics SET is_deleted = false WHERE topic_id = ?";

				try (PreparedStatement ps2 = con.prepareStatement(sql2)) {

					ps2.setInt(1, id);
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

	public int addTopicReturnId(int subjectId, String name) {

		int id = 0;

		String sql = "INSERT INTO topics(subject_id, name) VALUES(?, ?)";

		try (Connection con = DBConnection.getConnection();
				PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

			ps.setInt(1, subjectId);
			ps.setString(2, name);

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

	public List<Topic> getTopicsBySubject(int subjectId) {

		List<Topic> list = new ArrayList<>();

		String sql = """
				SELECT *
				FROM topics
				WHERE subject_id = ?
				AND is_deleted = false
				""";

		try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setInt(1, subjectId);

			try (ResultSet rs = ps.executeQuery()) {

				while (rs.next()) {

					Topic t = new Topic();

					t.setId(rs.getInt("id"));
					t.setSubjectId(rs.getInt("subject_id"));
					t.setName(rs.getString("name"));

					list.add(t);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;

	}

	public List<Topic> getDeletedTopicsByUser(int userId) {

		List<Topic> list = new ArrayList<>();

		String sql = """
				SELECT t.*
				FROM topics t
				JOIN subjects s
					ON t.subject_id = s.id
				JOIN roadmaps r
					ON s.roadmap_id = r.id
				WHERE r.user_id = ?
				AND t.is_deleted = true
				""";

		try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setInt(1, userId);

			try (ResultSet rs = ps.executeQuery()) {

				while (rs.next()) {

					Topic t = new Topic();

					t.setId(rs.getInt("id"));
					t.setSubjectId(rs.getInt("subject_id"));
					t.setName(rs.getString("name"));

					list.add(t);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;

	}

	public Topic getTopicById(int id) {

		Topic t = null;

		String sql = "SELECT * FROM topics WHERE id = ?";

		try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setInt(1, id);

			try (ResultSet rs = ps.executeQuery()) {

				if (rs.next()) {

					t = new Topic();

					t.setId(rs.getInt("id"));
					t.setSubjectId(rs.getInt("subject_id"));
					t.setName(rs.getString("name"));

				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return t;
	}
}