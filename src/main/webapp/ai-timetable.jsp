<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ page import="java.util.*"%>
<%@ page import="model.AIDailyTask"%>


<!DOCTYPE html>

<html>

<head>

<meta charset="UTF-8">

<title>AI Timetable | DevTracker</title>

<link rel="stylesheet" href="css/main.css">

</head>


<body>



	<div class="dashboard-container">



		<div class="top-bar">


			<div>

				<h1>🤖 AI Study Planner</h1>

				<p class="muted-text">Your optimized daily learning schedule</p>

			</div>



			<a class="btn" href="viewAIRoadmaps"> Back </a>



		</div>





		<%
		List<AIDailyTask> tasks = (List<AIDailyTask>) request.getAttribute("tasks");

		int roadmapId = (int) request.getAttribute("roadmapId");

		int completed = 0;

		for (AIDailyTask t : tasks) {

			if (t.isCompleted()) {

				completed++;

			}

		}

		int percent = 0;

		if (tasks.size() > 0) {

			percent = (completed * 100) / tasks.size();

		}
		%>





		<div class="stats-card">


			<h3>

				Overall Progress

				<%=percent%>%

			</h3>




			<div class="progress-bar">


				<div class="progress-fill" style="width:<%=percent%>%"></div>


			</div>



		</div>







		<div class="planner-container">



			<%
			int currentDay = -1;

			for (AIDailyTask t : tasks) {

				if (currentDay != t.getDayNumber()) {

					if (currentDay != -1) {
			%>


		</div>


		<%
		}

		currentDay = t.getDayNumber();
		%>





		<div class="day-card">


			<h2>

				📅 Day
				<%=currentDay%>

			</h2>




			<%
			}
			%>







			<form action="updateAITask" method="post" class="task-row">





				<label class="checkbox-area"> <input type="checkbox"
					name="completed" <%=t.isCompleted() ? "checked" : ""%>
					onchange="this.form.submit()"> <span> <%=t.getSubtopicName()%>

				</span>



				</label> <span class="time-pill"> ⏱ Planned </span> <input type="hidden"
					name="taskId" value="<%=t.getId()%>"> <input type="hidden"
					name="roadmapId" value="<%=roadmapId%>">






			</form>






			<%
			}

			if (tasks.size() > 0) {
			%>


		</div>


		<%
		}
		%>





	</div>





	<div class="ai-actions">


		<button class="btn ai-btn">✨ Ask AI to Adjust Plan</button>



		<button class="btn">Edit Timetable</button>


	</div>





	</div>



</body>


</html>