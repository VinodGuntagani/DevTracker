package util;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {

	public static Connection getConnection() {

		try {

			String host = System.getenv("DB_HOST");
			String port = System.getenv("DB_PORT");
			String db = System.getenv("DB_NAME");

			String url = "jdbc:mysql://" + host + ":" + port + "/" + db + "?sslMode=REQUIRED";

			String user = System.getenv("DB_USER");
			String pass = System.getenv("DB_PASSWORD");

			Class.forName("com.mysql.cj.jdbc.Driver");

			System.out.println("Database Connected");

			return DriverManager.getConnection(url, user, pass);

		} catch(Exception e) {

			e.printStackTrace();

		}

		return null;
	}
}