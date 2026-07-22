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

		AIJob job = new AIJob();

		job.setUserId(userId);
		job.setRoadmapId(roadmapId);
		job.setJobType("NOTES");
		job.setStatus("PENDING");

		job.setCompletedTasks(0);
		job.setFailedTasks(0);
		job.setTotalTasks(0);

		int jobId = jobDAO.createJob(job);

		if (jobId == -1) {
			return -1;
		}

		List<SubTopic> subTopics = roadmapDAO.getAllSubTopics(roadmapId);

		int totalTasks = 0;
		System.out.println("Total subtopics fetched = " + subTopics.size());

		for (SubTopic st : subTopics) {
		    System.out.println(st.getId() + " - " + st.getName());
		}
		for (SubTopic subTopic : subTopics) {

		    System.out.println("----------------------------");
		    System.out.println("SubTopic: " + subTopic.getName());

		    if (subTopic.getAiLearning() != null && !subTopic.getAiLearning().isBlank()) {
		        System.out.println("SKIPPED");
		        continue;
		    }

		    System.out.println("QUEUED");

		    AIJobTask task = new AIJobTask();

		    task.setJobId(jobId);
		    task.setSubtopicId(subTopic.getId());
		    task.setStatus("PENDING");
		    task.setAttempts(0);

		    taskDAO.createTask(task);

		    totalTasks++;
		}

		jobDAO.updateTotalTasks(jobId, totalTasks);

		if (totalTasks == 0) {
			jobDAO.markCompleted(jobId);
		}

		System.out.println("Created AI Job " + jobId + " with " + totalTasks + " tasks.");

		return jobId;
	}

}