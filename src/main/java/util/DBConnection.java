package util;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {

	public static Connection getConnection() {

		try {

			String host = System.getenv("DB_HOST");
			String port = System.getenv("DB_PORT");
			String db = System.getenv("DB_NAME");

			String user = System.getenv("DB_USER");
			String pass = System.getenv("DB_PASSWORD");

			// LOCAL FALLBACK
			if (host == null || host.isEmpty()) {

				System.out.println("Using LOCAL database");

				host = "localhost";
				port = "3306";
				db = "devtracker";

				user = "root";
				pass = "1234";

			} else {

				System.out.println("Using CLOUD database");

			}

			String url = "jdbc:mysql://" + host + ":" + port + "/" + db;

			Class.forName("com.mysql.cj.jdbc.Driver");

			Connection con = DriverManager.getConnection(url, user, pass);

			System.out.println("Database Connected");

			return con;

		} catch (Exception e) {

			e.printStackTrace();

		}

		return null;
	}
}