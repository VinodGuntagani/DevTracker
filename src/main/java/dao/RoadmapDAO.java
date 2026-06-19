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

		boolean status = false;

		try {

			Connection con = DBConnection.getConnection();

			String sql = "INSERT INTO roadmaps\r\n" + "(user_id,title,description,start_date,target_date,is_ai)"
					+ "VALUES(?,?,?,?,?,?)";

			PreparedStatement ps = con.prepareStatement(sql);

			ps.setInt(1, roadmap.getUserId());

			ps.setString(2, roadmap.getTitle());

			ps.setString(3, roadmap.getDescription());

			ps.setDate(4, roadmap.getStartDate());

			ps.setDate(5, roadmap.getTargetDate());

			ps.setBoolean(6, false);

			int rows = ps.executeUpdate();

			if (rows > 0) {

				status = true;

			}

		} catch (Exception e) {

			e.printStackTrace();

		}

		return status;

	}

	// GET USER ROADMAPS
	public List<Roadmap> getRoadmaps(int userId) {

		List<Roadmap> list = new ArrayList<>();

		try {

			Connection con = DBConnection.getConnection();

			String sql = "SELECT * FROM roadmaps " + "WHERE user_id=? AND is_deleted=false";

			PreparedStatement ps = con.prepareStatement(sql);

			ps.setInt(1, userId);

			ResultSet rs = ps.executeQuery();

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

		} catch (Exception e) {

			e.printStackTrace();

		}

		return list;

	}

	public void deleteRoadmap(int id) {

		try {

			Connection con = DBConnection.getConnection();

			// delete roadmap

			String sql1 = "UPDATE roadmaps SET is_deleted=true WHERE id=?";

			PreparedStatement ps1 = con.prepareStatement(sql1);

			ps1.setInt(1, id);

			ps1.executeUpdate();

			// delete subjects

			String sql2 = "UPDATE subjects SET is_deleted=true WHERE roadmap_id=?";

			PreparedStatement ps2 = con.prepareStatement(sql2);

			ps2.setInt(1, id);

			ps2.executeUpdate();

			// delete topics

			String sql3 = """
					UPDATE topics t

					JOIN subjects s
					ON t.subject_id=s.id

					SET t.is_deleted=true

					WHERE s.roadmap_id=?
					""";

			PreparedStatement ps3 = con.prepareStatement(sql3);

			ps3.setInt(1, id);

			ps3.executeUpdate();

			// delete subtopics

			String sql4 = """
					UPDATE sub_topics st

					JOIN topics t
					ON st.topic_id=t.id

					JOIN subjects s
					ON t.subject_id=s.id

					SET st.is_deleted=true

					WHERE s.roadmap_id=?
					""";

			PreparedStatement ps4 = con.prepareStatement(sql4);

			ps4.setInt(1, id);

			ps4.executeUpdate();

		} catch (Exception e) {

			e.printStackTrace();

		}

	}

	public void updateName(int id, String title, String description) {

		try {

			Connection con = DBConnection.getConnection();

			String sql = "UPDATE roadmaps SET title=?, description=? WHERE id=?";

			PreparedStatement ps = con.prepareStatement(sql);

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

		try {

			Connection con = DBConnection.getConnection();

			String sql = "SELECT * FROM roadmaps WHERE user_id=? AND is_deleted=true";

			PreparedStatement ps = con.prepareStatement(sql);

			ps.setInt(1, userId);

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {

				Roadmap r = new Roadmap();

				r.setId(rs.getInt("id"));

				r.setTitle(rs.getString("title"));

				r.setDescription(rs.getString("description"));

				list.add(r);

			}

		} catch (Exception e) {

			e.printStackTrace();

		}

		return list;

	}

	public void restoreRoadmap(int id) {

		try {

			Connection con = DBConnection.getConnection();

			// restore roadmap

			String sql1 = "UPDATE roadmaps SET is_deleted=false WHERE id=?";

			PreparedStatement ps1 = con.prepareStatement(sql1);

			ps1.setInt(1, id);

			ps1.executeUpdate();

			// restore subjects

			String sql2 = "UPDATE subjects SET is_deleted=false WHERE roadmap_id=?";

			PreparedStatement ps2 = con.prepareStatement(sql2);

			ps2.setInt(1, id);

			ps2.executeUpdate();

			// restore topics

			String sql3 = """
					UPDATE topics t

					JOIN subjects s
					ON t.subject_id=s.id

					SET t.is_deleted=false

					WHERE s.roadmap_id=?
					""";

			PreparedStatement ps3 = con.prepareStatement(sql3);

			ps3.setInt(1, id);

			ps3.executeUpdate();

			// restore subtopics

			String sql4 = """
					UPDATE sub_topics st

					JOIN topics t
					ON st.topic_id=t.id

					JOIN subjects s
					ON t.subject_id=s.id

					SET st.is_deleted=false

					WHERE s.roadmap_id=?
					""";

			PreparedStatement ps4 = con.prepareStatement(sql4);

			ps4.setInt(1, id);

			ps4.executeUpdate();

		} catch (Exception e) {

			e.printStackTrace();

		}

	}

	public List<SubTopic> getAllSubTopics(int roadmapId) {

		List<SubTopic> list = new ArrayList<>();

		try {

			Connection con = DBConnection.getConnection();

			String sql = """
					SELECT st.*

					FROM sub_topics st

					JOIN topics t
					ON st.topic_id = t.id

					JOIN subjects s
					ON t.subject_id = s.id

					WHERE s.roadmap_id=?
					AND st.is_deleted=false

					ORDER BY st.weight DESC
					""";

			PreparedStatement ps = con.prepareStatement(sql);

			ps.setInt(1, roadmapId);

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {

				SubTopic st = new SubTopic();

				st.setId(rs.getInt("id"));

				st.setTopicId(rs.getInt("topic_id"));

				st.setName(rs.getString("name"));

				st.setCompleted(rs.getBoolean("completed"));

				st.setDifficulty(rs.getString("difficulty"));

				st.setEstimatedMinutes(rs.getInt("estimated_Minutes"));

				st.setWeight(rs.getInt("weight"));

				list.add(st);

			}

		} catch (Exception e) {

			e.printStackTrace();

		}

		return list;

	}

	public Roadmap getRoadmapById(int id) {

		Roadmap roadmap = null;

		try {

			Connection con = DBConnection.getConnection();

			String sql = "SELECT * FROM roadmaps WHERE id=?";

			PreparedStatement ps = con.prepareStatement(sql);

			ps.setInt(1, id);

			ResultSet rs = ps.executeQuery();

			if (rs.next()) {

				roadmap = new Roadmap();

				roadmap.setId(rs.getInt("id"));

				roadmap.setTitle(rs.getString("title"));

				roadmap.setDescription(rs.getString("description"));

				roadmap.setStartDate(rs.getDate("start_date"));

				roadmap.setTargetDate(rs.getDate("target_date"));

			}

		} catch (Exception e) {

			e.printStackTrace();

		}

		return roadmap;

	}

	public int addRoadmapReturnId(Roadmap r) {

		int id = 0;

		try {

			Connection con = DBConnection.getConnection();

			String sql = "INSERT INTO roadmaps(user_id,title,description,start_date,target_date,is_ai)"
					+ " VALUES(?,?,?,?,?,?)";

			PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

			ps.setInt(1, r.getUserId());

			ps.setString(2, r.getTitle());

			ps.setString(3, r.getDescription());

			ps.setDate(4, r.getStartDate());

			ps.setDate(5, r.getTargetDate());

			ps.setBoolean(6, true);

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

	public List<Roadmap> getAIRoadmaps(int userId) {

		List<Roadmap> list = new ArrayList<>();

		try {

			Connection con = DBConnection.getConnection();

			String sql = """
						SELECT *
						FROM roadmaps
						WHERE user_id=?
						AND is_ai=true
						AND is_deleted=false
					""";

			PreparedStatement ps = con.prepareStatement(sql);

			ps.setInt(1, userId);

			ResultSet rs = ps.executeQuery();

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

		} catch (Exception e) {

			e.printStackTrace();

		}

		return list;

	}
}