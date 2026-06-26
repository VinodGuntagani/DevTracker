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
<link rel="stylesheet" href="css/syllabus.css">
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
					class="ti ti-route"></i>
					<div>
						<div>DevTracker</div>
						<div class="logo-sub">Learning OS</div>
					</div>
				</a>
			</div>

			<nav class="nav">
				<div class="nav-section-label">Menu</div>

				<a class="nav-item" href="dashboard.jsp"> <i
					class="ti ti-layout-dashboard"></i> Dashboard
				</a><a class="nav-item" href="create-roadmap.jsp"> <i
					class="ti ti-plus"></i> Create Roadmap
				</a> <a class="nav-item" href="openAIRoadmap?id=<%=roadmapId%>"> <i
					class="ti ti-books"></i> Syllabus
				</a><a class="nav-item active" href="#"> <i class="ti ti-books"></i>
					Daily Tasks
				</a>

				<div class="nav-section-label">Manage</div>
				<a class="nav-item" href="trash.jsp"> <i class="ti ti-recycle"></i>
					Recover Deleted
				</a>
			</nav>

			<div class="sidebar-footer">
				<div class="user-row">
					<div class="avatar">
						<i class="ti ti-user" style="font-size: 15px"></i>
					</div>
					<div class="user-info">
						<div class="user-name">My Account</div>
						<div class="user-plan">Free plan</div>
					</div>
					<a href="logout" class="logout-btn" title="Logout"> <i
						class="ti ti-logout"></i>
					</a>
				</div>
			</div>

		</aside>



		<main class="main">

			<!-- Hero — mirrors syllabus-hero style -->
			<div class="syllabus-hero">
				<div class="hero-breadcrumb">
					<a href="dashboard.jsp">Dashboard</a> <i
						class="ti ti-chevron-right"></i> <span>Study Schedule</span>
				</div>
				<h1 class="hero-title">📅 Study Schedule</h1>
				<p class="hero-desc">Your daily learning mission 🚀</p>

				<div class="hero-overall">
					<div class="hero-overall-track">
						<div class="hero-overall-fill" id="heroProgressFill"
							style="width:<%=percent%>%"></div>
					</div>
					<span class="hero-overall-text" id="heroProgressText"><%=percent%>%
						complete</span>
				</div>

				<div class="hero-stats" style="display: flex">
					<div class="hero-stat">
						<span class="hero-stat-val" id="totalTasksVal"><%=tasks.size()%></span>
						<span class="hero-stat-label">Tasks</span>
					</div>
					<div class="hero-stat">
						<span class="hero-stat-val" id="completedVal"><%=completed%></span>
						<span class="hero-stat-label">Done</span>
					</div>
					<div class="hero-stat">
						<span class="hero-stat-val" id="remainingVal"><%=tasks.size() - completed%></span>
						<span class="hero-stat-label">Remaining</span>
					</div>
					<div class="hero-stat">
						<span class="hero-stat-val"><%=percent%>%</span> <span
							class="hero-stat-label">Progress</span>
					</div>
				</div>
			</div>

			<!-- Toolbar -->
			<div class="syllabus-toolbar">
				<span class="syllabus-toolbar-label">Schedule</span>
				<div class="toolbar-actions">
					<button class="btn btn-sm"
						onclick="location.href='openAIRoadmap?id=<%=roadmapId%>'">
						<i class="ti ti-books"></i> Syllabus
					</button>
					<button class="btn btn-sm" onclick="expandAllDays()">
						<i class="ti ti-chevrons-down"></i> Expand all
					</button>
					<button class="btn btn-sm" onclick="collapseAllDays()">
						<i class="ti ti-chevrons-up"></i> Collapse all
					</button>
					<a class="btn btn-sm" href="dashboard.jsp"> <i
						class="ti ti-arrow-left"></i> Back
					</a>
				</div>
			</div>

			<!-- Planner -->
			<div class="planner-container">
				<%
				currentDate = null;
				boolean firstDay = true;
				int taskIndex = 0;
				for (DailyTask task : tasks) {
					taskIndex++;
					boolean isNewDay = (task.getScheduleDate() != null && !task.getScheduleDate().equals(currentDate));
					if (isNewDay) {
						// close previous day
						if (currentDate != null) {
				%>
			</div>
			<%-- /subject-body --%>
	</div>
	<%-- /subject-block --%>
	<%
	}
	currentDate = task.getScheduleDate();
	String dayBlockId = "day-" + currentDate.toString().replace("-", "");
	String openClass = firstDay ? "open" : "";
	firstDay = false;
	%>
	<div class="subject-block <%=openClass%>" id="<%=dayBlockId%>">
		<div class="subject-header" onclick="toggleSubject('<%=dayBlockId%>')"
			style="cursor: pointer">
			<div class="subject-index">
				<i class="ti ti-calendar-event" style="font-size: 14px"></i>
			</div>
			<div class="subject-header-left">
				<span class="subject-name">📅 <%=currentDate%></span>
			</div>
			<div class="subject-meta">
				<div class="subject-progress-box">
					<span class="count-pill">Planned tasks</span>
				</div>
				<i class="ti ti-chevron-down chevron-icon"></i>
			</div>
		</div>
		<div class="subject-body">
			<%
			} // end isNewDay
			boolean isCompleted = "COMPLETED".equals(task.getStatus());
			boolean isLearning = "LEARNING".equals(task.getStatus());
			boolean isRevision = "REVISION".equals(task.getStatus());
			String markComplete = isCompleted ? "false" : "true";
			%>
			<div class="task-card" id="taskRow-<%=task.getId()%>">

				<!-- Time pill -->
				<div class="time-pill">
					🕘
					<%=task.getPlannedMinutes()%>
					min
				</div>

				<!-- Subject › Topic path -->
				<div class="task-path">
					<div>
						📘
						<%=task.getSubjectName()%></div>
					<div class="muted-text">
						└ 📌
						<%=task.getTopicName()%></div>
				</div>

				<!-- Task name row: check icon + title + learn button -->
				<div class="task-main">
					<button class="lesson-check-btn"
						onclick="toggleTask(<%=task.getId()%>,<%=task.getSubtopicId()%>,<%=roadmapId%>,<%=markComplete%>,this)">
						<%
						if (isCompleted) {
						%>
						<i class="ti ti-circle-check-filled"></i>
						<%
						} else if (isLearning) {
						%>
						<i class="ti ti-circle-half-2"></i>
						<%
						} else if (isRevision) {
						%>
						<i class="ti ti-refresh-dot"></i>
						<%
						} else {
						%>
						<i class="ti ti-circle"></i>
						<%
						}
						%>
					</button>
					<span class="task-title-text"><%=task.getSubtopicName()%></span> <a
						class="task-learn-btn"
						href="generateLearning?subtopicId=<%=task.getSubtopicId()%>&roadmapId=<%=roadmapId%>">

						<i class="ti ti-book-2"></i> Learn

					</a>
				</div>

			</div>
			<%-- /task-card --%>
			<%
			} // end for loop
			if (tasks.size() > 0) {
			%>
		</div>
		<%-- /subject-body --%>
	</div>
	<%-- /subject-block --%>
	<%
	}
	%>
	</div>
	<!-- /planner-container -->

	<div class="ai-actions" style="margin: 24px 0 40px">
		<button class="btn ai-btn">✨ Optimize Schedule with AI</button>
	</div>

	</main>

	</div>

	<!-- Bottom tab bar — mirrors syllabus exactly -->
	<nav class="bottom-nav">
		<a class="bn-item" href="dashboard.jsp"><i
			class="ti ti-layout-dashboard"></i>Dashboard</a> <a class="bn-item"
			href="create-roadmap.jsp"><i class="ti ti-plus"></i>New</a> <a
			class="bn-item" href="openAIRoadmap?id=<%=roadmapId%>"> <i
			class="ti ti-books"></i> Syllabus
		</a> <a class="bn-item active" href="#"><i class="ti ti-calendar-week"></i>Daily
			Tasks</a>
	</nav>

	<style>
/* Status select */
.status-select {
	font-size: 12px;
	padding: 5px 10px;
	border-radius: 8px;
	border: 1px solid var(--border);
	background: var(--surface);
	color: var(--text);
	font-family: var(--font-display, inherit);
	cursor: pointer;
	outline: none;
	flex: 1;
}

.status-select:focus {
	border-color: var(--accent);
}

/* Bottom action row inside task-card */
.task-actions-row {
	display: flex;
	align-items: center;
	gap: 10px;
	margin-top: 4px;
}

/* Check button sits inline with the task title */
.task-main {
	display: flex;
	align-items: center;
	gap: 8px;
}

.task-main .lesson-check-btn {
	background: none;
	border: none;
	padding: 0;
	cursor: pointer;
	color: var(--accent);
	font-size: 18px;
	flex-shrink: 0;
	line-height: 1;
	display: flex;
	align-items: center;
}

.task-title-text {
	flex: 1;
	font-size: 15px;
	font-weight: 600;
	color: var(--text);
	min-width: 0;
}

.task-learn-btn {
	flex-shrink: 0;
	display: inline-flex;
	align-items: center;
	gap: 5px;
	font-size: 12px;
	font-weight: 600;
	padding: 5px 12px;
	border-radius: 8px;
	border: 1px solid var(--border);
	background: var(--surface);
	color: var(--accent);
	text-decoration: none;
	transition: background .15s, border-color .15s;
}

.task-learn-btn:hover {
	background: var(--accent);
	color: #fff;
	border-color: var(--accent);
}

/* Spinner on the check button while saving */
.lesson-check-btn .ti-loader-2 {
	animation: spin .7s linear infinite;
}

@
keyframes spin {to { transform:rotate(360deg);
	
}
}
</style>

	<script>
		/* ── Sidebar (mobile) ── */
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

		/* ── Accordion (reuse syllabus functions) ── */
		function toggleSubject(id) {
			document.getElementById(id).classList.toggle('open');
		}
		function expandAllDays() {
			document.querySelectorAll('.subject-block').forEach(el => el.classList.add('open'));
		}
		function collapseAllDays() {
			document.querySelectorAll('.subject-block').forEach(el => el.classList.remove('open'));
		}

		/* ── Shared progress helpers ── */
		function recalcOverall() {
			var allBtns  = document.querySelectorAll('.lesson-check-btn');
			var total    = allBtns.length;
			var done     = document.querySelectorAll('.lesson-check-btn .ti-circle-check-filled').length;
			var pct      = total > 0 ? Math.round(done * 100 / total) : 0;

			var fill = document.getElementById('heroProgressFill');
			var text = document.getElementById('heroProgressText');
			var completedVal  = document.getElementById('completedVal');
			var remainingVal  = document.getElementById('remainingVal');

			if (fill)         fill.style.width   = pct + '%';
			if (text)         text.innerText      = pct + '% complete';
			if (completedVal) completedVal.innerText  = done;
			if (remainingVal) remainingVal.innerText  = (total - done);
		}

		/* ── Toggle completion (circle / check) — same pattern as syllabus ── */
		function toggleTask(taskId, subtopicId, roadmapId, markComplete, btn) {
			var oldHTML = btn.innerHTML;
			btn.disabled = true;
			btn.innerHTML = '<i class="ti ti-loader-2"></i>';

			var newStatus = markComplete ? 'COMPLETED' : 'NOT_STARTED';

			fetch('updateDailyTask', {

				method : 'POST',

				headers: { 

					'Content-Type':'application/x-www-form-urlencoded',

					'X-Requested-With':'XMLHttpRequest'

				},
				body   : 'id=' + taskId
				       + '&subtopicId=' + subtopicId
				       + '&roadmapId='  + roadmapId
				       + '&status='     + newStatus
			})
			.then(function(r) {
				/* Accept both JSON and empty/non-JSON responses gracefully */
				return r.text();
			})
			.then(function() {
				/* Update icon */
				btn.innerHTML = markComplete
					? '<i class="ti ti-circle-check-filled"></i>'
					: '<i class="ti ti-circle"></i>';

				/* Flip the onclick for next press */
				btn.setAttribute('onclick',
					'toggleTask(' + taskId + ',' + subtopicId + ',' + roadmapId + ','
					+ !markComplete + ',this)');

				/* Keep the status <select> in sync */
				var sel = document.querySelector('[data-task-id="' + taskId + '"]');
				if (sel) sel.value = newStatus;

				/* Recalc overall hero progress */
				recalcOverall();
			})
			.catch(function() {
				btn.innerHTML = oldHTML;
			})
			.finally(function() {
				btn.disabled = false;
			});
		}

		/* ── Status dropdown change ── */
		function changeStatus(sel, taskId, subtopicId, roadmapId) {
			var newStatus = sel.value;

			/* Update the check icon to match the new status */
			var btn = document.querySelector('#taskRow-' + taskId + ' .lesson-check-btn');
			if (btn) {
				var iconMap = {
					'COMPLETED'  : 'ti-circle-check-filled',
					'LEARNING'   : 'ti-circle-half-2',
					'REVISION'   : 'ti-refresh-dot',
					'NOT_STARTED': 'ti-circle'
				};
				btn.innerHTML = '<i class="ti ' + (iconMap[newStatus] || 'ti-circle') + '"></i>';

				/* Rewire the toggle so clicking the icon flips to COMPLETED / NOT_STARTED */
				var markComplete = newStatus !== 'COMPLETED';
				btn.setAttribute('onclick',
					'toggleTask(' + taskId + ',' + subtopicId + ',' + roadmapId + ','
					+ markComplete + ',this)');
			}

			fetch('updateDailyTask', {
				method : 'POST',
				headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
				body   : 'id=' + taskId
				       + '&subtopicId=' + subtopicId
				       + '&roadmapId='  + roadmapId
				       + '&status='     + newStatus
			})
			.catch(function() {
				/* silent — the UI already updated optimistically */
			});

			recalcOverall();
		}
	</script>


</body>


</html>
