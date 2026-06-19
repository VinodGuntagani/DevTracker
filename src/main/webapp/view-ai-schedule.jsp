<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="dao.AITimetableDAO"%>
<%@ page import="model.AIDailyTask"%>
<%@ page import="java.util.List"%>

<%
int roadmapId = Integer.parseInt(request.getParameter("roadmapId"));

AITimetableDAO dao = new AITimetableDAO();

List<AIDailyTask> tasks = dao.getTasks(roadmapId);

int currentDay = -1;
%>

<!DOCTYPE html>
<html>

<head>
<meta charset="UTF-8">

<title>AI Timetable</title>

<link rel="stylesheet" href="css/main.css">
</head>

<body>

	<div class="dashboard-container">

		<div class="top-bar">

			<div>
				<h1>🤖 AI Timetable</h1>

				<p class="muted-text">Your optimized study schedule</p>
			</div>

			<a class="btn" href="dashboard.jsp"> Back </a>

		</div>


		<div class="planner-container">


			<%
			if (tasks.isEmpty()) {
			%>

			<div class="empty-card">
				<h3>No AI timetable generated</h3>
			</div>

			<%
			}

			for (AIDailyTask task : tasks) {

			if (currentDay != task.getDayNumber()) {

				if (currentDay != -1) {
			%>

		</div>

		<%
		}

		currentDay = task.getDayNumber();
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

				<input type="hidden" name="taskId" value="<%=task.getId()%>">


				<input type="hidden" name="roadmapId" value="<%=roadmapId%>">


				<label class="checkbox-area"> <input type="checkbox"
					name="completed" onchange="this.form.submit()"
					<%=task.isCompleted() ? "checked" : ""%>> <span> <%=task.getSubtopicName()%>
				</span>


				</label>


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


	</div>


</body>

</html>