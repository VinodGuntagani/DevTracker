package service.ai;

import java.util.List;

import dao.AIJobDAO;
import dao.AIJobTaskDAO;
import dao.RoadmapDAO;
import dao.SubTopicDAO;
import model.AIJob;
import model.AIJobTask;
import model.SubTopic;

public class AIJobService {

	private AIJobDAO jobDAO = new AIJobDAO();
	private AIJobTaskDAO taskDAO = new AIJobTaskDAO();
	private SubTopicDAO subTopicDAO = new SubTopicDAO();
	private RoadmapDAO roadmapDAO = new RoadmapDAO();

	public int createNotesJob(int userId, int roadmapId) {

		System.out.println("========================================");
		System.out.println("JOB STEP 1 - Starting createNotesJob()");
		System.out.println("========================================");

		AIJob job = new AIJob();

		job.setUserId(userId);
		job.setRoadmapId(roadmapId);
		job.setJobType("NOTES");
		job.setStatus("PENDING");

		job.setCompletedTasks(0);
		job.setFailedTasks(0);
		job.setTotalTasks(0);

		System.out.println("JOB STEP 2 - Creating AI Job");

		int jobId = jobDAO.createJob(job);

		System.out.println("JOB STEP 3 - Job ID = " + jobId);

		if (jobId == -1) {
			System.out.println("JOB FAILED - createJob() returned -1");
			return -1;
		}

		System.out.println("JOB STEP 4 - Fetching SubTopics");

		List<SubTopic> subTopics = roadmapDAO.getAllSubTopics(roadmapId);

		System.out.println("JOB STEP 5 - Total SubTopics = " + subTopics.size());

		int totalTasks = 0;

		for (SubTopic subTopic : subTopics) {

			System.out.println("----------------------------------------");
			System.out.println("Processing : " + subTopic.getName());

			if (subTopic.getAiLearning() != null && !subTopic.getAiLearning().isBlank()) {

				System.out.println("SKIPPED - Already Generated");
				continue;
			}

			System.out.println("JOB STEP 6 - Creating Task");

			AIJobTask task = new AIJobTask();

			task.setJobId(jobId);
			task.setSubtopicId(subTopic.getId());
			task.setStatus("PENDING");
			task.setAttempts(0);

			taskDAO.createTask(task);

			System.out.println("JOB STEP 7 - Task Created");

			totalTasks++;
		}

		System.out.println("JOB STEP 8 - Updating Total Tasks");

		jobDAO.updateTotalTasks(jobId, totalTasks);

		if (totalTasks == 0) {

			System.out.println("JOB STEP 9 - Nothing To Generate");

			jobDAO.markCompleted(jobId);
		}

		System.out.println("========================================");
		System.out.println("JOB STEP 10 - Finished");
		System.out.println("Job ID      : " + jobId);
		System.out.println("Total Tasks : " + totalTasks);
		System.out.println("========================================");

		return jobId;
	}

}