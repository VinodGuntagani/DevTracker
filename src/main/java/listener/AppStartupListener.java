package listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import service.ai.AIWorkerManager;
import util.DatabaseInitializer;

@WebListener
public class AppStartupListener implements ServletContextListener {

	static {
		System.out.println("APP STARTUP LISTENER CLASS LOADED");
	}

	@Override
	public void contextInitialized(ServletContextEvent event) {

		System.out.println("========================================");
		System.out.println("Application Starting...");
		System.out.println("========================================");

		try {

			// Initialize database
			DatabaseInitializer.initDatabase();

			System.out.println("✅ Database Initialization Complete");

			// Start AI Worker
			AIWorkerManager.start();

			System.out.println("✅ AI Worker Started");

		} catch (Throwable e) {

			System.out.println("❌ APPLICATION STARTUP FAILED");

			e.printStackTrace();

		}

	}

	@Override
	public void contextDestroyed(ServletContextEvent event) {

		System.out.println("========================================");
		System.out.println("Application Stopped");
		System.out.println("========================================");

	}

}