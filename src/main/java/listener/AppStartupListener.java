package listener;

import java.sql.Driver;
import java.sql.DriverManager;
import java.util.Enumeration;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import com.mysql.cj.jdbc.AbandonedConnectionCleanupThread;

import util.DatabaseInitializer;

@WebListener
public class AppStartupListener implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent event) {

		System.out.println("Application Starting...");

		try {

			DatabaseInitializer.initDatabase();

			System.out.println("Database Initialization Complete");

		} catch (Exception e) {

			System.out.println("DATABASE INIT FAILED - APP STILL STARTING");

			e.printStackTrace();

		}
	}

	@Override
	public void contextDestroyed(ServletContextEvent event) {

		System.out.println("Application Stopped");

		try {

			AbandonedConnectionCleanupThread.checkedShutdown();

			Enumeration<Driver> drivers = DriverManager.getDrivers();

			while (drivers.hasMoreElements()) {

				Driver driver = drivers.nextElement();

				DriverManager.deregisterDriver(driver);

			}

			System.out.println("MySQL cleanup completed");

		} catch (Exception e) {

			e.printStackTrace();

		}

	}

}