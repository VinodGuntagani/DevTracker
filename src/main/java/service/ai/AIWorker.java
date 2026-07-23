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

		System.out.println("================================");
		System.out.println("Processing Job : " + task.getJobId());
		System.out.println("Task ID        : " + task.getId());
		System.out.println("Subtopic ID    : " + task.getSubtopicId());
		System.out.println("================================");

		taskDAO.markRunning(task.getId());

		System.out.println("✅ Marked RUNNING");

		try {

			SubTopic subTopic = subTopicDAO.getSubTopicById(task.getSubtopicId());

			if (subTopic == null) {

				System.out.println("❌ SubTopic not found");

				taskDAO.markFailed(task.getId());
				jobDAO.incrementFailedTasks(task.getJobId());

				return;
			}

			System.out.println("📚 SubTopic : " + subTopic.getName());

			String context = subTopicDAO.getLearningContext(subTopic.getId());

			System.out.println("🤖 Calling Gemini...");

			String learning = geminiService.generateLearning(context);

			System.out.println("🤖 Gemini Finished");

			if (learning == null || learning.isBlank()) {

				System.out.println("❌ Empty Response");

				taskDAO.incrementAttempts(task.getId());
				taskDAO.markFailed(task.getId());
				jobDAO.incrementFailedTasks(task.getJobId());

				return;
			}

			learning = learning.replace("```html", "").replace("```", "").trim();

			System.out.println("💾 Saving Learning");

			subTopicDAO.updateAILearning(subTopic.getId(), learning);

			System.out.println("✅ Learning Saved");

			taskDAO.markCompleted(task.getId());

			System.out.println("✅ Task Marked COMPLETED");

			jobDAO.incrementCompletedTasks(task.getJobId());

			System.out.println("✅ Job Completed Count Incremented");

			if (jobDAO.isFinished(task.getJobId())) {

				jobDAO.markCompleted(task.getJobId());

				System.out.println("🎉 Job Finished : " + task.getJobId());

			}

		} catch (Exception e) {

			System.out.println("❌ Exception while processing task");

			e.printStackTrace();

			taskDAO.incrementAttempts(task.getId());

			taskDAO.markFailed(task.getId());

			jobDAO.incrementFailedTasks(task.getJobId());

		}

	}

}