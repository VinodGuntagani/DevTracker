package dao;

import java.sql.*;
import java.util.*;

import model.LearningResource;

import util.DBConnection;

public class LearningResourceDAO {

	public void addResource(LearningResource r) {

		String sql = """
				INSERT INTO learning_resources
				(subtopic_id,
				 video_id,
				 title,
				 channel_name,
				 thumbnail,
				 rank_no,
				 reason)
				VALUES (?, ?, ?, ?, ?, ?, ?)
				""";

		try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setInt(1, r.getSubtopicId());
			ps.setString(2, r.getVideoId());
			ps.setString(3, r.getTitle());
			ps.setString(4, r.getChannelName());
			ps.setString(5, r.getThumbnail());
			ps.setInt(6, r.getRankNo());
			ps.setString(7, r.getReason());

			ps.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public List<LearningResource> getResources(int subtopicId) {

		List<LearningResource> list = new ArrayList<>();

		String sql = """
				SELECT *
				FROM learning_resources
				WHERE subtopic_id = ?
				ORDER BY rank_no
				""";

		try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setInt(1, subtopicId);

			try (ResultSet rs = ps.executeQuery()) {

				while (rs.next()) {

					LearningResource r = new LearningResource();

					r.setId(rs.getInt("id"));
					r.setSubtopicId(rs.getInt("subtopic_id"));
					r.setVideoId(rs.getString("video_id"));
					r.setTitle(rs.getString("title"));
					r.setChannelName(rs.getString("channel_name"));
					r.setThumbnail(rs.getString("thumbnail"));
					r.setRankNo(rs.getInt("rank_no"));
					r.setReason(rs.getString("reason"));

					list.add(r);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;

	}

	public boolean exists(int subtopicId) {

		String sql = """
				SELECT COUNT(*) AS total
				FROM learning_resources
				WHERE subtopic_id = ?
				""";

		try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setInt(1, subtopicId);

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
}