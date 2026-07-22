package service.ai;

import dao.AIJobDAO;
import dao.AIJobTaskDAO;
import dao.SubTopicDAO;
import model.AIJobTask;
import model.SubTopic;
import service.GeminiService;

public class AIWorker implements Runnable {

	private final AIJobTaskDAO taskDAO = new AIJobTaskDAO();
	private final AIJobDAO jobDAO = new AIJobDAO();
	private final SubTopicDAO subTopicDAO = new SubTopicDAO();

	private final GeminiService geminiService = new GeminiService();

	@Override
	public void run() {

		System.out.println("========================================");
		System.out.println("🚀 AI Worker Started");
		System.out.println("========================================");

		while (true) {

			try {

				AIJobTask task = taskDAO.getNextPendingTask();

				if (task == null) {

					Thread.sleep(5000);
					continue;
				}

				processTask(task);

			} catch (Exception e) {

				e.printStackTrace();

				try {
					Thread.sleep(5000);
				} catch (InterruptedException ex) {
					ex.printStackTrace();
				}

			}

		}

	}

	private void processTask(AIJobTask task) {

		taskDAO.markRunning(task.getId());

		try {

			SubTopic subTopic = subTopicDAO.getSubTopicById(task.getSubtopicId());

			if (subTopic == null) {

				taskDAO.markFailed(task.getId());
				jobDAO.incrementFailedTasks(task.getJobId());

				return;
			}

			String context = subTopicDAO.getLearningContext(subTopic.getId());

			String learning = geminiService.generateLearning(context);

			if (learning == null || learning.isBlank()) {

				taskDAO.incrementAttempts(task.getId());

				taskDAO.markFailed(task.getId());

				jobDAO.incrementFailedTasks(task.getJobId());

				return;
			}

			learning = learning.replace("```html", "").replace("```", "").trim();

			subTopicDAO.updateAILearning(subTopic.getId(), learning);

			taskDAO.markCompleted(task.getId());

			jobDAO.incrementCompletedTasks(task.getJobId());

			if (jobDAO.isFinished(task.getJobId())) {

				jobDAO.markCompleted(task.getJobId());

				System.out.println("✅ Job Finished : " + task.getJobId());

			}

		} catch (Exception e) {

			e.printStackTrace();

			taskDAO.incrementAttempts(task.getId());

			taskDAO.markFailed(task.getId());

			jobDAO.incrementFailedTasks(task.getJobId());

		}

	}

}