package dao;

import java.sql.*;
import java.util.*;
import java.sql.Statement;

import model.Subject;
import util.DBConnection;

public class SubjectDAO {

	public boolean addSubject(Subject subject) {

		boolean status = false;

		try {

			Connection con = DBConnection.getConnection();

			String sql = "INSERT INTO subjects(roadmap_id,name) VALUES(?,?)";

			PreparedStatement ps = con.prepareStatement(sql);

			ps.setInt(1, subject.getRoadmapId());

			ps.setString(2, subject.getName());

			status = ps.executeUpdate() > 0;

		} catch (Exception e) {

			e.printStackTrace();

		}

		return status;
	}

	public List<Subject> getSubjects(int roadmapId) {

		List<Subject> list = new ArrayList<>();

		try {

			Connection con = DBConnection.getConnection();

			String sql = "SELECT * FROM subjects WHERE roadmap_id=? AND is_deleted=false";

			PreparedStatement ps = con.prepareStatement(sql);

			ps.setInt(1, roadmapId);

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {

				Subject s = new Subject();

				s.setId(rs.getInt("id"));

				s.setRoadmapId(rs.getInt("roadmap_id"));

				s.setName(rs.getString("name"));

				list.add(s);

			}

		} catch (Exception e) {

			e.printStackTrace();

		}

		return list;
	}

	public int getRoadmapProgress(int roadmapId) {

		int progress = 0;

		try {

			Connection con = DBConnection.getConnection();

			String sql = """
					SELECT
					COUNT(st.id) AS total,
					SUM(st.completed) AS done

					FROM subjects s

					LEFT JOIN topics t
					ON s.id = t.subject_id

					LEFT JOIN sub_topics st
					ON t.id = st.topic_id

					WHERE s.roadmap_id=?
					AND s.is_deleted=false
					AND t.is_deleted=false
					AND st.is_deleted=false
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

	public void deleteSubject(int id) {

		try {

			Connection con = DBConnection.getConnection();

			// delete subject

			String sql1 = "UPDATE subjects SET is_deleted=true WHERE id=?";

			PreparedStatement ps1 = con.prepareStatement(sql1);

			ps1.setInt(1, id);

			ps1.executeUpdate();

			// delete topics

			String sql2 = "UPDATE topics SET is_deleted=true WHERE subject_id=?";

			PreparedStatement ps2 = con.prepareStatement(sql2);

			ps2.setInt(1, id);

			ps2.executeUpdate();

			// delete subtopics

			String sql3 = """
					UPDATE sub_topics st

					JOIN topics t
					ON st.topic_id = t.id

					SET st.is_deleted=true

					WHERE t.subject_id=?
					""";

			PreparedStatement ps3 = con.prepareStatement(sql3);

			ps3.setInt(1, id);

			ps3.executeUpdate();

		} catch (Exception e) {

			e.printStackTrace();

		}

	}

	public void updateName(int id, String name) {

		try {

			Connection con = DBConnection.getConnection();

			String sql = "UPDATE subjects SET name=? WHERE id=?";

			PreparedStatement ps = con.prepareStatement(sql);

			ps.setString(1, name);

			ps.setInt(2, id);

			ps.executeUpdate();

		} catch (Exception e) {

			e.printStackTrace();

		}

	}

	public List<Subject> getDeletedSubjects(int roadmapId) {

		List<Subject> list = new ArrayList<>();

		try {

			Connection con = DBConnection.getConnection();

			String sql = "SELECT * FROM subjects WHERE roadmap_id=? AND is_deleted=true";

			PreparedStatement ps = con.prepareStatement(sql);

			ps.setInt(1, roadmapId);

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {

				Subject s = new Subject();

				s.setId(rs.getInt("id"));

				s.setRoadmapId(rs.getInt("roadmap_id"));

				s.setName(rs.getString("name"));

				list.add(s);

			}

		} catch (Exception e) {

			e.printStackTrace();

		}

		return list;

	}

	public void restoreSubject(int id) {

		try {

			Connection con = DBConnection.getConnection();

			// restore subject

			String sql1 = "UPDATE subjects SET is_deleted=false WHERE id=?";

			PreparedStatement ps1 = con.prepareStatement(sql1);

			ps1.setInt(1, id);

			ps1.executeUpdate();

			// restore topics

			String sql2 = "UPDATE topics SET is_deleted=false WHERE subject_id=?";

			PreparedStatement ps2 = con.prepareStatement(sql2);

			ps2.setInt(1, id);

			ps2.executeUpdate();

			// restore subtopics

			String sql3 = """
					UPDATE sub_topics st

					JOIN topics t
					ON st.topic_id=t.id

					SET st.is_deleted=false

					WHERE t.subject_id=?
					""";

			PreparedStatement ps3 = con.prepareStatement(sql3);

			ps3.setInt(1, id);

			ps3.executeUpdate();

		} catch (Exception e) {

			e.printStackTrace();

		}

	}

	public int addSubjectReturnId(int roadmapId, String name) {

		int id = 0;

		try {

			Connection con = DBConnection.getConnection();

			String sql = "INSERT INTO subjects(roadmap_id,name) VALUES(?,?)";

			PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

			ps.setInt(1, roadmapId);

			ps.setString(2, name);

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

	public List<Subject> getSubjectsByRoadmap(int roadmapId) {

		List<Subject> list = new ArrayList<>();

		try {

			Connection con = DBConnection.getConnection();

			String sql = """
					    SELECT * FROM subjects
					    WHERE roadmap_id=?
					    AND is_deleted=false
					""";

			PreparedStatement ps = con.prepareStatement(sql);

			ps.setInt(1, roadmapId);

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {

				Subject s = new Subject();

				s.setId(rs.getInt("id"));
				s.setRoadmapId(rs.getInt("roadmap_id"));
				s.setName(rs.getString("name"));

				list.add(s);

			}

		} catch (Exception e) {

			e.printStackTrace();

		}

		return list;

	}

	public Subject getSubjectById(int id) {

		Subject s = null;

		try {

			Connection con = DBConnection.getConnection();

			String sql = "SELECT * FROM subjects WHERE id=?";

			PreparedStatement ps = con.prepareStatement(sql);

			ps.setInt(1, id);

			ResultSet rs = ps.executeQuery();

			if (rs.next()) {

				s = new Subject();

				s.setId(rs.getInt("id"));
				s.setRoadmapId(rs.getInt("roadmap_id"));
				s.setName(rs.getString("name"));

			}

		} catch (Exception e) {

			e.printStackTrace();

		}

		return s;

	}

	public List<Subject> getDeletedSubjectsByUser(int userId) {

		List<Subject> list = new ArrayList<>();

		try {

			Connection con = DBConnection.getConnection();

			String sql = """
						SELECT s.*
						FROM subjects s

						JOIN roadmaps r
						ON s.roadmap_id = r.id

						WHERE r.user_id=?
						AND s.is_deleted=true
					""";

			PreparedStatement ps = con.prepareStatement(sql);

			ps.setInt(1, userId);

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {

				Subject s = new Subject();

				s.setId(rs.getInt("id"));
				s.setRoadmapId(rs.getInt("roadmap_id"));
				s.setName(rs.getString("name"));

				list.add(s);

			}

		} catch (Exception e) {

			e.printStackTrace();

		}

		return list;
	}
}