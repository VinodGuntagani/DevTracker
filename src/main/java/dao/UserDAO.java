package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import model.User;
import util.DBConnection;

public class UserDAO {

	public boolean register(User user) {

		boolean status = false;

		try {

			Connection con = DBConnection.getConnection();

			String sql = "INSERT INTO users(name,email,password) VALUES(?,?,?)";

			PreparedStatement ps = con.prepareStatement(sql);

			ps.setString(1, user.getName());
			ps.setString(2, user.getEmail());
			ps.setString(3, user.getPassword());

			int rows = ps.executeUpdate();

			if (rows > 0) {
				status = true;
			}

		} catch (Exception e) {

			e.printStackTrace();

		}

		return status;
	}

	public User login(String email, String password) {

		User user = null;

		try {

			Connection con = DBConnection.getConnection();

			String sql = "SELECT * FROM users WHERE email=? AND password=?";

			PreparedStatement ps = con.prepareStatement(sql);

			ps.setString(1, email);
			ps.setString(2, password);

			ResultSet rs = ps.executeQuery();

			if (rs.next()) {

				user = new User();

				user.setId(rs.getInt("id"));

				user.setName(rs.getString("name"));

				user.setEmail(rs.getString("email"));

			}

		} catch (Exception e) {

			e.printStackTrace();

		}

		return user;
	}

}