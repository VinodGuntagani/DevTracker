<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="model.User"%>

<%
User user = (User) session.getAttribute("user");

if (user == null) {

	response.sendRedirect("login.html");
	return;

}
%>


<!DOCTYPE html>

<html>

<head>

<meta charset="UTF-8">

<title>Create Roadmap | DevTracker</title>

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

	</div>



	<div class="layout">



		<!-- Sidebar -->
		<aside class="sidebar">


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

				</a> <a class="nav-item active" href="create-roadmap.jsp"> <i
					class="ti ti-map"></i> Create Roadmap

				</a>


			</nav>



			<div class="sidebar-footer">


				<div class="user-row">


					<div class="avatar">

						<%=user.getName().charAt(0)%>

					</div>


					<div class="user-info">

						<div class="user-name">

							<%=user.getName()%>

						</div>

						<div class="user-plan">Free plan</div>

					</div>


					<a href="logout" class="logout-btn"> <i class="ti ti-logout"></i>

					</a>


				</div>


			</div>


		</aside>




		<!-- Main -->
		<main class="main">


			<div class="page-header">


				<div>

					<h1 class="page-title">📚 Create Roadmap</h1>


					<p class="page-sub">Plan your learning journey</p>

				</div>


			</div>




			<!-- Choice cards -->
			<div id="choiceBox" class="card-grid">


				<!-- AI CARD -->
				<div class="card" onclick="location.href='ai-roadmap.jsp'"
					style="cursor: pointer">


					<div class="stat-icon si-green">

						<i class="ti ti-sparkles"></i>

					</div>


					<h2>Generate with AI</h2>


					<p class="muted-text">Describe your goal and let AI create
						subjects, topics, lessons and schedules.</p>


					<button class="btn btn-primary">Use AI</button>


				</div>





				<!-- MANUAL CARD -->
				<div class="card" onclick="showManualForm()" style="cursor: pointer">


					<div class="stat-icon si-amber">

						<i class="ti ti-pencil"></i>

					</div>


					<h2>Create Manually</h2>


					<p class="muted-text">Create your own roadmap and customize
						everything yourself.</p>


					<button class="btn">Manual Create</button>


				</div>


			</div>





			<!-- Your old manual form (hidden) -->
			<div id="manualForm" class="card" style="display: none">


				<form action="createRoadmap" method="post">



					<div class="input-group">

						<label>Roadmap Name</label> <input type="text" name="title"
							placeholder="Example: Java Developer" required>

					</div>




					<div class="input-group">

						<label>Description</label>

						<textarea name="description" placeholder="Describe your goal"></textarea>

					</div>





					<div class="input-group">

						<label>Start Date</label> <input type="date" name="startDate"
							required>

					</div>





					<div class="input-group">

						<label>Target Date</label> <input type="date" name="targetDate"
							required>

					</div>




					<button class="btn btn-primary">

						<i class="ti ti-plus"></i> Create Roadmap

					</button>



				</form>


			</div>


		</main>


	</div>




	<nav class="bottom-nav">


		<a class="bn-item" href="dashboard.jsp"> <i
			class="ti ti-layout-dashboard"></i> Dashboard

		</a> <a class="bn-item active" href="create-roadmap.jsp"> <i
			class="ti ti-map"></i> Create

		</a>


	</nav>

	<script>
		function showManualForm() {

			document.getElementById("choiceBox").style.display = "none";

			document.getElementById("manualForm").style.display = "block";

		}
	</script>
</body>


</html>