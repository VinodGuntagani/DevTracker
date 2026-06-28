package util;

import java.sql.Connection;
import java.sql.SQLException;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class DBConnection {

	private static HikariDataSource dataSource;

	static {

		try {

			String host = System.getenv("DB_HOST");
			String port = System.getenv("DB_PORT");
			String db = System.getenv("DB_NAME");

			String user = System.getenv("DB_USER");
			String pass = System.getenv("DB_PASSWORD");

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

			HikariConfig config = new HikariConfig();

			config.setJdbcUrl(url);
			config.setUsername(user);
			config.setPassword(pass);

			config.setMaximumPoolSize(10);
			config.setMinimumIdle(2);

			config.setConnectionTimeout(30000);
			config.setIdleTimeout(600000);
			config.setMaxLifetime(1800000);

			dataSource = new HikariDataSource(config);

			System.out.println("HikariCP Pool Initialized");

		} catch (Exception e) {

			e.printStackTrace();

		}

	}

	public static Connection getConnection() throws SQLException {

		return dataSource.getConnection();

	}

}