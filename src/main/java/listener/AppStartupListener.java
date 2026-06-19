package listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import util.DatabaseInitializer;

@WebListener
public class AppStartupListener implements ServletContextListener {

	static {
		System.out.println("APP STARTUP LISTENER CLASS LOADED");
	}

	@Override
	public void contextInitialized(ServletContextEvent event) {

		System.out.println("Application Starting...");

		try {

			DatabaseInitializer.initDatabase();

			System.out.println("Database Initialization Complete");

		} catch (Throwable e) {

			System.out.println("DATABASE INIT FAILED");

			e.printStackTrace();

		}

	}



	@Override
	public void contextDestroyed(ServletContextEvent event) {

		System.out.println("Application Stopped");

	}

}
