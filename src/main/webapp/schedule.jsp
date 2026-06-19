<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ page import="dao.StudyPlanDAO"%>
<%@ page import="model.DailyTask"%>
<%@ page import="java.util.List"%>
<%@ page import="dao.SubTopicDAO"%>
<%@ page import="dao.TopicDAO"%>
<%@ page import="model.Topic"%>
<%@ page import="dao.RoadmapDAO"%>
<%@ page import="model.Roadmap"%>
<%@ page import="model.User"%>
<%@ page import="service.ScheduleGenerator"%>
<%@ page import="java.time.temporal.ChronoUnit"%>
<%@ page import="dao.AITimetableDAO"%>
<%@ page import="java.sql.Date"%>



<%
int roadmapId = Integer.parseInt(request.getParameter("roadmapId"));

StudyPlanDAO dao = new StudyPlanDAO();

List<DailyTask> tasks = dao.getTasks(roadmapId);

//no main schedule exists
if (tasks.size() == 0) {

	User user = (User) session.getAttribute("user");

	if (user != null) {

		AITimetableDAO aiDAO = new AITimetableDAO();

		// priority 1: AI timetable exists

		if (aiDAO.hasAITimetable(roadmapId)) {

	int aiPlanId = aiDAO.getAIPlanId(roadmapId);

	RoadmapDAO roadmapDAO = new RoadmapDAO();

	Roadmap roadmap = roadmapDAO.getRoadmapById(roadmapId);

	int days = (int) ChronoUnit.DAYS.between(

			roadmap.getStartDate().toLocalDate(),

			roadmap.getTargetDate().toLocalDate()

	) + 1;

	aiDAO.syncToMainSchedule(aiPlanId, user.getId(), roadmapId, days);

		}

		// priority 2: normal roadmap

		else {

	RoadmapDAO roadmapDAO = new RoadmapDAO();

	Roadmap roadmap = roadmapDAO.getRoadmapById(roadmapId);

	int days = (int) ChronoUnit.DAYS.between(

			roadmap.getStartDate().toLocalDate(),

			roadmap.getTargetDate().toLocalDate()

	) + 1;

	ScheduleGenerator generator = new ScheduleGenerator();

	generator.generate(user.getId(), roadmapId, days, 120);

		}

		// reload created schedule

		tasks = dao.getTasks(roadmapId);

	}

}
Date currentDate = null;

int completed = 0;

for (DailyTask t : tasks) {

	if ("COMPLETED".equals(t.getStatus())) {

		completed++;

	}

}

int percent = 0;

if (tasks.size() > 0) {

	percent = (completed * 100) / tasks.size();

}
%>




<!DOCTYPE html>


<html>


<head>


<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">


<title>Study Schedule | DevTracker</title>




<link rel="stylesheet" href="css/main.css">
<link rel="stylesheet"
	href="https://cdn.jsdelivr.net/npm/@tabler/icons-webfont@latest/tabler-icons.min.css">



</head>

<body>
	<!-- Mobile top bar -->


	<div class="mobile-topbar">


		<a class="mobile-logo" href="dashboard.jsp"> <i
			class="ti ti-route"></i> DevTracker


		</a>


		<button class="hamburger" onclick="openSidebar()">


			<i class="ti ti-menu-2"></i>


		</button>


	</div>





	<!-- Overlay -->

	<div class="overlay" id="overlay" onclick="closeSidebar()"></div>





	<div class="layout">



		<!-- Sidebar -->


		<aside class="sidebar" id="sidebar">



			<div class="sidebar-logo">


				<a class="logo-mark" href="dashboard.jsp"> <i
					class="ti ti-route"></i> DevTracker


				</a>


				<div class="logo-sub">Learning OS</div>


			</div>





			<nav class="nav">


				<div class="nav-section-label">Menu</div>



				<a class="nav-item" href="dashboard.jsp"> <i
					class="ti ti-layout-dashboard"></i> Dashboard


				</a> <a class="nav-item active" href="#"> <i class="ti ti-books"></i>

					Topics


				</a> <a class="nav-item" href="ai-roadmap.jsp"> <i
					class="ti ti-robot"></i> AI tools


				</a> <a class="nav-item" href="viewAIRoadmaps"> <i
					class="ti ti-layout-list"></i> AI roadmaps


				</a>






				<div class="nav-section-label">Manage</div>











			</nav>






			<div class="sidebar-footer">


				<div class="user-row">


					<div class="avatar">


						<i class="ti ti-user"></i>


					</div>




					<div class="user-info">


						<div class="user-name">My Account</div>



						<div class="user-plan">Free plan</div>


					</div>





					<a href="logout" class="logout-btn"> <i class="ti ti-logout"></i>


					</a>



				</div>


			</div>



		</aside>



		<main class="main">



			<div class="top-bar">






				<div>




					<h1>📅 Study Schedule</h1>





					<p class="muted-text">Your daily learning mission 🚀</p>





				</div>






				<a class="btn" href="dashboard.jsp"> Back </a>






			</div>







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
				for (DailyTask task : tasks) {

					if (task.getScheduleDate() != null && !task.getScheduleDate().equals(currentDate)) {

						if (currentDate != null) {
				%>


			</div>


			<%
			}

			currentDate = task.getScheduleDate();
			%>






			<div class="day-card">






				<div class="day-header">





					<h2>

						📅
						<%=currentDate%>

					</h2>







					<p class="muted-text">Your planned tasks</p>





				</div>








				<%
				}
				%>









				<!-- TASK CARD -->



				<div class="task-card">









					<!-- Time -->



					<div class="time-pill">



						🕘

						<%=task.getPlannedMinutes()%>
						min



					</div>









					<!-- Subject + Topic -->



					<div class="task-path">





						<div>


							📘
							<%=task.getSubjectName()%>


						</div>







						<div class="muted-text">


							└ 📌
							<%=task.getTopicName()%>


						</div>





					</div>











					<!-- Main Task -->



					<div class="task-main">






						<h3>



							<%
							if ("COMPLETED".equals(task.getStatus())) {
							%>


							✓


							<%
							} else if ("LEARNING".equals(task.getStatus())) {
							%>


							◐



							<%
							} else if ("REVISION".equals(task.getStatus())) {
							%>


							↻


							<%
							} else {
							%>


							○


							<%
							}
							%>






							<%=task.getSubtopicName()%>




						</h3>














					</div>
					<!-- Status / Actions -->


					<form action="updateDailyTask" method="post"
						class="schedule-actions">






						<input type="hidden" name="id" value="<%=task.getId()%>">
						<input type="hidden" name="subtopicId"
							value="<%=task.getSubtopicId()%>"> <input type="hidden"
							name="roadmapId" value="<%=roadmapId%>"> <select
							name="status" onchange="this.form.submit()">






							<option value="NOT_STARTED"
								<%="NOT_STARTED".equals(task.getStatus()) ? "selected" : ""%>>


								○ Not Started</option>








							<option value="LEARNING"
								<%="LEARNING".equals(task.getStatus()) ? "selected" : ""%>>


								◐ Learning</option>








							<option value="COMPLETED"
								<%="COMPLETED".equals(task.getStatus()) ? "selected" : ""%>>


								✓ Completed</option>








							<option value="REVISION"
								<%="REVISION".equals(task.getStatus()) ? "selected" : ""%>>


								↻ Need Revision</option>







						</select>






					</form>











					<div class="task-buttons">


						<a class="btn"
							href="generateLearning?subtopicId=<%=task.getSubtopicId()%>">

							📚 Learn </a> <a class="btn"
							href="generateNotes?subtopicId=<%=task.getSubtopicId()%>&roadmapId=<%=roadmapId%>">

							📝 Notes </a>


					</div>









				</div>



				<!-- END TASK CARD -->








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






		<button class="btn ai-btn">✨ Optimize Schedule with AI</button>







	</div>

	</main>



	</div>







	<script>
		function openSidebar() {

			document.querySelector('.sidebar').classList.add('open');

			document.getElementById('overlay').classList.add('open');

			document.body.style.overflow = 'hidden';

		}

		function closeSidebar() {

			document.querySelector('.sidebar').classList.remove('open');

			document.getElementById('overlay').classList.remove('open');

			document.body.style.overflow = '';

		}
		function enableEdit(id) {

			document.getElementById("editForm" + id).style.display = "flex";

			document.getElementById("titleText" + id).style.display = "none";

			document.getElementById("badgeArea" + id).style.display = "none";

		}

		function cancelEdit(id) {

			document.getElementById("editForm" + id).style.display = "none";

			document.getElementById("titleText" + id).style.display = "block";

			document.getElementById("badgeArea" + id).style.display = "block";

		}
	</script>


</body>


</html>