package dao;

import java.sql.*;
import java.util.*;
import java.sql.Statement;

import model.SubTopic;
import util.DBConnection;

public class SubTopicDAO {

	public int addSubTopic(SubTopic subTopic) {

		int id = 0;

		String sql = """
				INSERT INTO sub_topics
				(topic_id, name, difficulty, estimated_minutes, weight)
				VALUES (?, ?, ?, ?, ?)
				""";

		try (Connection con = DBConnection.getConnection();
				PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

			ps.setInt(1, subTopic.getTopicId());
			ps.setString(2, subTopic.getName());
			ps.setString(3, subTopic.getDifficulty());
			ps.setInt(4, subTopic.getEstimatedMinutes());
			ps.setInt(5, subTopic.getWeight());

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

	public List<SubTopic> getSubTopics(int topicId) {

		List<SubTopic> list = new ArrayList<>();

		String sql = """
				SELECT *
				FROM sub_topics
				WHERE topic_id = ?
				AND is_deleted = false
				""";

		try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setInt(1, topicId);

			try (ResultSet rs = ps.executeQuery()) {

				while (rs.next()) {

					SubTopic s = new SubTopic();

					s.setId(rs.getInt("id"));
					s.setTopicId(rs.getInt("topic_id"));
					s.setName(rs.getString("name"));
					s.setCompleted(rs.getBoolean("completed"));
					s.setDifficulty(rs.getString("difficulty"));
					s.setEstimatedMinutes(rs.getInt("estimated_minutes"));
					s.setWeight(rs.getInt("weight"));

					list.add(s);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;

	}

	public void updateStatus(int id, boolean completed) {

		String sql = "UPDATE sub_topics SET completed = ? WHERE id = ?";

		try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setBoolean(1, completed);
			ps.setInt(2, id);

			ps.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public int getProgress(int topicId) {

		int progress = 0;

		String sql = """
				SELECT
					COUNT(*) AS total,
					SUM(completed) AS done
				FROM sub_topics
				WHERE topic_id = ?
				AND is_deleted = false
				""";

		try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setInt(1, topicId);

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

	public void deleteSubTopic(int id) {

		String sql = "UPDATE sub_topics SET is_deleted = true WHERE id = ?";

		try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setInt(1, id);

			ps.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void updateName(int id, String name) {

		String sql = "UPDATE sub_topics SET name = ? WHERE id = ?";

		try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setString(1, name);
			ps.setInt(2, id);

			ps.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public List<SubTopic> getDeletedSubTopics(int topicId) {

		List<SubTopic> list = new ArrayList<>();

		String sql = """
				SELECT *
				FROM sub_topics
				WHERE topic_id = ?
				AND is_deleted = true
				""";

		try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setInt(1, topicId);

			try (ResultSet rs = ps.executeQuery()) {

				while (rs.next()) {

					SubTopic s = new SubTopic();

					s.setId(rs.getInt("id"));
					s.setTopicId(rs.getInt("topic_id"));
					s.setName(rs.getString("name"));
					s.setCompleted(rs.getBoolean("completed"));
					s.setDifficulty(rs.getString("difficulty"));
					s.setEstimatedMinutes(rs.getInt("estimated_minutes"));
					s.setWeight(rs.getInt("weight"));

					list.add(s);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;

	}

	public void restoreSubTopic(int id) {

		String sql = "UPDATE sub_topics SET is_deleted = false WHERE id = ?";

		try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setInt(1, id);

			ps.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void updateAIAnalysis(int id, String difficulty, int minutes, int weight) {

		String sql = """
				UPDATE sub_topics
				SET
					difficulty = ?,
					estimated_minutes = ?,
					weight = ?
				WHERE id = ?
				""";

		try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setString(1, difficulty);
			ps.setInt(2, minutes);
			ps.setInt(3, weight);
			ps.setInt(4, id);

			ps.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public int addSubTopicReturnId(SubTopic sub) {

		int id = 0;

		String sql = """
				INSERT INTO sub_topics
				(topic_id, name, difficulty, estimated_minutes, weight)
				VALUES (?, ?, ?, ?, ?)
				""";

		try (Connection con = DBConnection.getConnection();
				PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

			ps.setInt(1, sub.getTopicId());
			ps.setString(2, sub.getName());
			ps.setString(3, sub.getDifficulty());
			ps.setInt(4, sub.getEstimatedMinutes());
			ps.setInt(5, sub.getWeight());

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

	public List<SubTopic> getSubTopicsByTopic(int topicId) {

		List<SubTopic> list = new ArrayList<>();

		String sql = """
				SELECT *
				FROM sub_topics
				WHERE topic_id = ?
				AND is_deleted = false
				""";

		try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setInt(1, topicId);

			try (ResultSet rs = ps.executeQuery()) {

				while (rs.next()) {

					SubTopic st = new SubTopic();

					st.setId(rs.getInt("id"));
					st.setTopicId(rs.getInt("topic_id"));
					st.setName(rs.getString("name"));
					st.setDifficulty(rs.getString("difficulty"));
					st.setEstimatedMinutes(rs.getInt("estimated_minutes"));
					st.setCompleted(rs.getBoolean("completed"));

					list.add(st);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;

	}

	public List<SubTopic> getDeletedSubTopicsByUser(int userId) {

		List<SubTopic> list = new ArrayList<>();

		String sql = """
				SELECT st.*
				FROM sub_topics st
				JOIN topics t
					ON st.topic_id = t.id
				JOIN subjects s
					ON t.subject_id = s.id
				JOIN roadmaps r
					ON s.roadmap_id = r.id
				WHERE r.user_id = ?
				AND st.is_deleted = true
				""";

		try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setInt(1, userId);

			try (ResultSet rs = ps.executeQuery()) {

				while (rs.next()) {

					SubTopic st = new SubTopic();

					st.setId(rs.getInt("id"));
					st.setTopicId(rs.getInt("topic_id"));
					st.setName(rs.getString("name"));
					st.setDifficulty(rs.getString("difficulty"));
					st.setEstimatedMinutes(rs.getInt("estimated_minutes"));
					st.setWeight(rs.getInt("weight"));

					list.add(st);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;

	}

	public SubTopic getSubTopicById(int id) {

		SubTopic sub = null;

		String sql = """
				SELECT *
				FROM sub_topics
				WHERE id = ?
				""";

		try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setInt(1, id);

			try (ResultSet rs = ps.executeQuery()) {

				if (rs.next()) {

					sub = new SubTopic();

					sub.setId(rs.getInt("id"));
					sub.setTopicId(rs.getInt("topic_id"));
					sub.setName(rs.getString("name"));
					sub.setCompleted(rs.getBoolean("completed"));
					sub.setDifficulty(rs.getString("difficulty"));
					sub.setEstimatedMinutes(rs.getInt("estimated_minutes"));
					sub.setWeight(rs.getInt("weight"));
					sub.setAiNotes(rs.getString("ai_notes"));
					sub.setAiKeywords(rs.getString("ai_keywords"));
					sub.setAiLearning(rs.getString("ai_learning"));

				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return sub;

	}

	public void updateAINotes(int id, String notes) {

		String sql = """
				UPDATE sub_topics
				SET ai_notes = ?
				WHERE id = ?
				""";

		try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setString(1, notes);
			ps.setInt(2, id);

			ps.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void updateAILearning(int id, String learning) {

		String sql = """
				UPDATE sub_topics
				SET ai_learning = ?
				WHERE id = ?
				""";

		try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setString(1, learning);
			ps.setInt(2, id);

			ps.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public String getLearningContext(int subtopicId) {

		String context = "";

		String sql = """
					SELECT
				    r.title AS roadmap,
				    r.description AS roadmap_description,
				    s.name AS subject,
				    t.name AS topic,
				    st.name AS subtopic
				FROM sub_topics st
				JOIN topics t
				    ON st.topic_id = t.id
				JOIN subjects s
				    ON t.subject_id = s.id
				JOIN roadmaps r
				    ON s.roadmap_id = r.id
				WHERE st.id = ?;
												""";

		try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setInt(1, subtopicId);

			try (ResultSet rs = ps.executeQuery()) {

				if (rs.next()) {

					context = """
							LEARNING CONTEXT

							Roadmap:
							%s

							Roadmap Goal:
							%s

							Subject:
							%s

							Topic:
							%s

							Target Subtopic:
							%s
							""".formatted(rs.getString("roadmap"), rs.getString("roadmap_description"),
							rs.getString("subject"), rs.getString("topic"), rs.getString("subtopic"));
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return context;

	}

	public void updateAIKeywords(int id, String keywords) {

		String sql = """
				UPDATE sub_topics
				SET ai_keywords = ?
				WHERE id = ?
				""";

		try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setString(1, keywords);
			ps.setInt(2, id);

			ps.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}