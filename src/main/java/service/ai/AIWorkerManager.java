package service.ai;

public class AIWorkerManager {

	private static Thread workerThread;

	public static synchronized void start() {

		if (workerThread != null && workerThread.isAlive()) {

			System.out.println("AI Worker already running.");
			return;
		}

		workerThread = new Thread(new AIWorker());

		workerThread.setName("DevTracker-AI-Worker");
		workerThread.setDaemon(true);

		workerThread.start();

		System.out.println("========================================");
		System.out.println("✅ DevTracker AI Worker Started");
		System.out.println("========================================");
	}

}