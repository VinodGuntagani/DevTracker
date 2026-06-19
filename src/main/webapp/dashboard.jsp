<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="model.User"%>
<%@ page import="model.Roadmap"%>
<%@ page import="dao.RoadmapDAO"%>
<%@ page import="java.util.List"%>
<%@ page import="dao.SubjectDAO"%>
<%@ page import="dao.AITimetableDAO"%>

<%
User user = (User) session.getAttribute("user");
if (user == null) {
	response.sendRedirect("login.html");
	return;
}

RoadmapDAO dao = new RoadmapDAO();
List<Roadmap> roadmaps = dao.getRoadmaps(user.getId());
SubjectDAO progressDao = new SubjectDAO();
AITimetableDAO aiProgressDao = new AITimetableDAO();

String name = user.getName();
String initials = "";
if (name != null && !name.isEmpty()) {
	String[] parts = name.trim().split("\\s+");
	initials += Character.toUpperCase(parts[0].charAt(0));
	if (parts.length > 1)
		initials += Character.toUpperCase(parts[parts.length - 1].charAt(0));
}

int total = roadmaps.size();
int completed = 0;
int totalProgress = 0;
int aiCount = 0;
for (Roadmap r : roadmaps) {
	int p = r.isAi() ? aiProgressDao.getAIProgress(r.getId()) : progressDao.getRoadmapProgress(r.getId());
	totalProgress += p;
	if (p >= 100)
		completed++;
	if (r.isAi())
		aiCount++;
}
int avgProgress = total > 0 ? totalProgress / total : 0;
%>

<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Dashboard | DevTracker</title>
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
		<button class="hamburger" onclick="openSidebar()"
			aria-label="Open menu">
			<i class="ti ti-menu-2"></i>
		</button>
	</div>

	<!-- Overlay (closes drawer on tap) -->
	<div class="overlay" id="overlay" onclick="closeSidebar()"></div>

	<div class="layout">

		<!-- Sidebar -->
		<aside class="sidebar">
			<div class="sidebar-logo">
				<a class="logo-mark" href="dashboard.jsp"> <i
					class="ti ti-route"></i> DevTracker
				</a>
				<div class="logo-sub">Learning OS</div>
			</div>

			<nav class="nav">
				<div class="nav-section-label">Menu</div>
				<a class="nav-item active" href="dashboard.jsp"> <i
					class="ti ti-layout-dashboard"></i> Dashboard
				</a> <a class="nav-item" href="create-roadmap.jsp"> <i
					class="ti ti-map"></i> Create Roadmap
				</a> <a class="nav-item" href="ai-roadmap.jsp"> <i
					class="ti ti-robot"></i> Generate Roadmap
				</a> 

				<div class="nav-section-label">Manage</div>
				<a class="nav-item" href="analytics.jsp"> 
				<i class="ti ti-chart-bar"></i>Analytics
				</a>
				<a class="nav-item" href="trash.jsp"> <i class="ti ti-recycle"></i>
					Recover deleted
				</a>

			</nav>

			<div class="sidebar-footer">
				<div class="user-row">
					<div class="avatar"><%=initials%></div>
					<div class="user-info">
						<div class="user-name"><%=user.getName()%></div>
						<div class="user-plan">Free plan</div>
					</div>
					<a href="logout" class="logout-btn" title="Logout"> <i
						class="ti ti-logout"></i>
					</a>
				</div>
			</div>
		</aside>

		<!-- Main content -->
		<main class="main">

			<!-- Page header -->
			<div class="page-header">
				<div>
					<div class="page-title">Dashboard</div>
					<div class="page-sub">Track your learning progress</div>
				</div>
				<div class="header-actions">
					<a class="btn btn-ai" href="ai-roadmap.jsp"> <i
						class="ti ti-plus"></i>Generate Roadmap
					</a> <a class="btn btn-primary" href="create-roadmap.jsp"> <i
						class="ti ti-plus"></i> Create Roadmap
					</a>
				</div>
			</div>

			<!-- Stats -->
			<div class="stats-grid">
				<div class="stat-card">
					<div class="stat-icon si-purple">
						<i class="ti ti-map"></i>
					</div>
					<div class="stat-body">
						<div class="stat-val"><%=total%></div>
						<div class="stat-label">Total roadmaps</div>
					</div>
				</div>
				<div class="stat-card">
					<div class="stat-icon si-teal">
						<i class="ti ti-trending-up"></i>
					</div>
					<div class="stat-body">
						<div class="stat-val"><%=avgProgress%>%
						</div>
						<div class="stat-label">Avg. progress</div>
					</div>
				</div>
				<div class="stat-card">
					<div class="stat-icon si-blue">
						<i class="ti ti-circle-check"></i>
					</div>
					<div class="stat-body">
						<div class="stat-val"><%=completed%></div>
						<div class="stat-label">Completed</div>
					</div>
				</div>
				<div class="stat-card">
					<div class="stat-icon si-amber">
						<i class="ti ti-sparkles"></i>
					</div>
					<div class="stat-body">
						<div class="stat-val"><%=aiCount%></div>
						<div class="stat-label">AI generated</div>
					</div>
				</div>
			</div>

			<!-- Roadmaps section -->
			<div>
				<div class="section-hd" style="margin-bottom: 1rem;">
					<h2>Your roadmaps</h2>
					<span style="font-size: 12px; color: #aaa;"><%=total%>
						active</span>
				</div>

				<div class="card-grid">

					<%
					if (roadmaps.isEmpty()) {
					%>
					<p style="color: #aaa; font-size: 14px;">No roadmaps yet.
						Create one to get started.</p>
					<%
					} else {
					for (Roadmap r : roadmaps) {
						int progress = r.isAi() ? aiProgressDao.getAIProgress(r.getId()) : progressDao.getRoadmapProgress(r.getId());
						boolean isDone = progress >= 100;
						String fillClass = isDone ? "high" : (progress >= 40 ? "medium" : "low");
					%>

					<div class="card">

						<!-- Card head -->
						<div class="card-head">
							<div class="card-head-left">
								<div class="card-badges">
									<%
									if (r.isAi()) {
									%>
									<span class="badge badge-ai"> <i class="ti ti-robot"
										style="font-size: 11px"></i> AI
									</span>
									<%
									}
									%>
									<%
									if (isDone) {
									%>
									<span class="badge badge-done"> <i class="ti ti-check"
										style="font-size: 11px"></i> Completed
									</span>
									<%
									}
									%>
								</div>
								<div class="card-title" id="titleText<%=r.getId()%>"><%=r.getTitle()%></div>
							</div>
							<button class="card-edit-btn" title="Edit"
								onclick="enableEdit(<%=r.getId()%>)">
								<i class="ti ti-pencil"></i>
							</button>
						</div>

						<!-- Description -->
						<p class="card-desc" id="descText<%=r.getId()%>"><%=r.getDescription()%></p>

						<!-- Edit form -->
						<form id="editForm<%=r.getId()%>" action="editRoadmap"
							method="post" class="edit-form">
							<input type="hidden" name="id" value="<%=r.getId()%>"> <input
								type="text" name="title" value="<%=r.getTitle()%>"
								placeholder="Roadmap title">
							<textarea name="description" rows="3" placeholder="Description"><%=r.getDescription()%></textarea>
							<div style="display: flex; gap: 8px;">
								<button class="btn btn-primary btn-xs">
									<i class="ti ti-check"></i> Save
								</button>
								<button type="button" class="btn btn-xs"
									onclick="cancelEdit(<%=r.getId()%>)">Cancel</button>
							</div>
						</form>
						<div class="card-bottom">

							<!-- Progress -->
							<div>
								<div class="progress-row">
									<span>Progress</span> <strong><%=progress%>%</strong>
								</div>
								<div class="progress-track">
									<div class="progress-fill <%=fillClass%>"
										style="width:<%=progress%>%"></div>
								</div>
							</div>

							<!-- Dates -->
							<div class="date-row">
								<i class="ti ti-calendar"></i>
								<%=r.getStartDate()%>
								&ndash;
								<%=r.getTargetDate()%>
							</div>

							<!-- Actions -->
							<div class="card-actions">
								<a class="btn" href="openAIRoadmap?id=<%=r.getId()%>"> <i
									class="ti ti-map"></i> Syllabus
								</a> <a class="btn btn-sm"
									href="schedule.jsp?roadmapId=<%=r.getId()%>"> <i
									class="ti ti-calendar-event"></i> Timetable
								</a>

								<form id="deleteForm<%=r.getId()%>" action="deleteRoadmap"
									method="post" class="delete-form">

									<input type="hidden" name="id" value="<%=r.getId()%>">

									<button class="btn btn-sm btn-danger">
										<i class="ti ti-trash"></i> Delete
									</button>

								</form>
							</div>
						</div>

					</div>

					<%
					}
					}
					%>

					<!-- Empty state / add new -->
					<a class="card-empty" href="create-roadmap.jsp"> <i
						class="ti ti-plus"></i>
						<p>Start a new roadmap</p>
					</a>

				</div>
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

    document.getElementById("descText" + id).style.display = "none";

    document.getElementById("deleteForm" + id).style.display = "block";
}
function cancelEdit(id) {

    document.getElementById("editForm" + id).style.display = "none";

    document.getElementById("titleText" + id).style.display = "block";

    document.getElementById("descText" + id).style.display = "block";

    document.getElementById("deleteForm" + id).style.display = "none";
}
</script>
</body>
</html>
