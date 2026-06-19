package util;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {

	public static Connection getConnection() {

		Connection con = null;

		try {

			Class.forName("com.mysql.cj.jdbc.Driver");

			String url = System.getenv("DB_URL");

			String user = System.getenv("DB_USER");

			String password = System.getenv("DB_PASSWORD");

			// =========================
			// LOCAL DEVELOPMENT FALLBACK
			// =========================

			if (url == null) {

				url = "jdbc:mysql://localhost:3306/devtracker";

				user = "root";

				password = "1234";

			}

			con = DriverManager.getConnection(url, user, password);

			System.out.println("Database Connected");

		} catch (Exception e) {

			e.printStackTrace();

		}

		return con;

	}

}