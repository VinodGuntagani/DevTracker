package util;

import java.sql.Connection;
import java.sql.Statement;

public class DatabaseInitializer {

	public static void initDatabase() {

		try {

			Class.forName("com.mysql.cj.jdbc.Driver");

			Connection con = DBConnection.getConnection();

			Statement st = con.createStatement();

			// USERS
			st.executeUpdate("CREATE TABLE IF NOT EXISTS users(" + "id INT PRIMARY KEY AUTO_INCREMENT,"
					+ "name VARCHAR(100) NOT NULL," + "email VARCHAR(100) UNIQUE NOT NULL,"
					+ "password VARCHAR(100) NOT NULL," + "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP" + ")");

			// ROADMAPS
			st.executeUpdate("CREATE TABLE IF NOT EXISTS roadmaps(" + "id INT PRIMARY KEY AUTO_INCREMENT,"
					+ "user_id INT," + "title VARCHAR(100)," + "description TEXT," + "start_date DATE,"
					+ "target_date DATE," + "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,"
					+ "updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,"
					+ "is_deleted BOOLEAN DEFAULT FALSE," + "is_ai BOOLEAN DEFAULT FALSE,"
					+ "FOREIGN KEY(user_id) REFERENCES users(id)" + ")");

			// SUBJECTS
			st.executeUpdate("CREATE TABLE IF NOT EXISTS subjects(" + "id INT PRIMARY KEY AUTO_INCREMENT,"
					+ "roadmap_id INT," + "name VARCHAR(100)," + "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,"
					+ "updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,"
					+ "is_deleted BOOLEAN DEFAULT FALSE," + "FOREIGN KEY(roadmap_id) REFERENCES roadmaps(id)" + ")");

			// TOPICS
			st.executeUpdate("CREATE TABLE IF NOT EXISTS topics(" + "id INT PRIMARY KEY AUTO_INCREMENT,"
					+ "subject_id INT," + "name VARCHAR(100)," + "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,"
					+ "updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,"
					+ "is_deleted BOOLEAN DEFAULT FALSE," + "FOREIGN KEY(subject_id) REFERENCES subjects(id)" + ")");

			// SUB TOPICS
			st.executeUpdate("CREATE TABLE IF NOT EXISTS sub_topics(" + "id INT PRIMARY KEY AUTO_INCREMENT,"
					+ "topic_id INT," + "name VARCHAR(100)," + "completed BOOLEAN DEFAULT FALSE,"

					+ "difficulty VARCHAR(20) DEFAULT 'Medium'," + "estimated_minutes INT DEFAULT 60,"
					+ "weight INT DEFAULT 120," + "ai_notes LONGTEXT," + "ai_learning LONGTEXT," + "ai_keywords TEXT,"

					+ "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,"
					+ "updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,"
					+ "is_deleted BOOLEAN DEFAULT FALSE," + "FOREIGN KEY(topic_id) REFERENCES topics(id)" + ")");

			// STUDY PLAN
			st.executeUpdate("CREATE TABLE IF NOT EXISTS study_plan(" + "id INT PRIMARY KEY AUTO_INCREMENT,"
					+ "user_id INT," + "roadmap_id INT,"

					+ "total_days INT," + "start_date DATE," + "daily_minutes INT,"
					+ "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,"

					+ "FOREIGN KEY(user_id) REFERENCES users(id)," + "FOREIGN KEY(roadmap_id) REFERENCES roadmaps(id)"
					+ ")");

			// DAILY TASKS
			// DAILY TASKS
			st.executeUpdate("CREATE TABLE IF NOT EXISTS daily_tasks(" + "id INT PRIMARY KEY AUTO_INCREMENT,"

					+ "plan_id INT," + "subtopic_id INT,"

					+ "day_number INT," + "task_order INT," + "weight INT," + "schedule_date DATE,"
					+ "planned_minutes INT,"

					// old completion system (keep for progress)
					+ "completed BOOLEAN DEFAULT FALSE,"

					// new schedule system
					+ "status VARCHAR(30) DEFAULT 'NOT_STARTED',"

					+ "start_time VARCHAR(10)," + "end_time VARCHAR(10),"

					+ "FOREIGN KEY(plan_id) REFERENCES study_plan(id),"
					+ "FOREIGN KEY(subtopic_id) REFERENCES sub_topics(id)"

					+ ")");
			// LEARNING VIDEOS
			st.executeUpdate("CREATE TABLE IF NOT EXISTS learning_resources("

					+ "id INT PRIMARY KEY AUTO_INCREMENT,"

					+ "subtopic_id INT,"

					// youtube data
					+ "video_id VARCHAR(100),"

					+ "title VARCHAR(255),"

					+ "channel_name VARCHAR(150),"

					+ "thumbnail TEXT,"

					// AI / ranking
					+ "rank_no INT,"

					+ "reason TEXT,"

					+ "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,"

					+ "FOREIGN KEY(subtopic_id) REFERENCES sub_topics(id)"

					+ ")");
			// AI STUDY PLAN
			st.executeUpdate("CREATE TABLE IF NOT EXISTS ai_study_plan(" + "id INT PRIMARY KEY AUTO_INCREMENT,"
					+ "user_id INT," + "roadmap_id INT,"

					+ "total_days INT," + "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,"

					+ "FOREIGN KEY(user_id) REFERENCES users(id)," + "FOREIGN KEY(roadmap_id) REFERENCES roadmaps(id)"
					+ ")");

			// AI DAILY TASKS
			st.executeUpdate("CREATE TABLE IF NOT EXISTS ai_daily_tasks(" + "id INT PRIMARY KEY AUTO_INCREMENT,"
					+ "ai_plan_id INT," + "subtopic_id INT,"

					+ "day_number INT," + "task_order INT,"

					+ "completed BOOLEAN DEFAULT FALSE,"

					+ "FOREIGN KEY(ai_plan_id) REFERENCES ai_study_plan(id)" + " ON DELETE CASCADE,"

					+ "FOREIGN KEY(subtopic_id) REFERENCES sub_topics(id)" + ")");

			System.out.println("Database Ready");

			con.close();

		} catch (Exception e) {

			e.printStackTrace();

		}

	}
}