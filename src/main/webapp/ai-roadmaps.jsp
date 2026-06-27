<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ page import="java.util.*"%>
<%@ page import="model.Roadmap"%>


<!DOCTYPE html>

<html>

<head>

<meta charset="UTF-8">

<title>AI Roadmaps | DevTracker</title>

<link rel="stylesheet" href="css/main.css">
<link rel="stylesheet" href="css/loading.css">
<link rel="stylesheet" href="css/animation.css">
<link rel="stylesheet" href="css/auth.css">
</head>


<body>


	<div class="dashboard-container">


		<div class="top-bar">


			<div>

				<h1>🤖 AI Roadmaps</h1>

				<p class="muted-text">Your AI generated learning plans</p>


			</div>



			<a class="btn" href="dashboard.jsp"> Back </a>



		</div>





		<div class="roadmap-grid">



			<%
			List<Roadmap> roadmaps = (List<Roadmap>) request.getAttribute("roadmaps");

			if (roadmaps == null || roadmaps.size() == 0) {
			%>



			<div class="empty-card">

				<h3>No AI roadmaps yet</h3>

				<p>Create your first AI powered roadmap.</p>


				<a class="btn ai-btn" href="ai-roadmap.jsp"> Generate AI Roadmap

				</a>


			</div>



			<%
			} else {

			for (Roadmap r : roadmaps) {
			%>





			<div class="roadmap-card">



				<div class="card-header">


					<h3>

						<%=r.getTitle()%>

					</h3>



					<span class="ai-badge"> AI </span>



				</div>





				<p class="description">

					<%=r.getDescription()%>

				</p>





				<div class="card-actions">



					<a class="btn" href="openAIRoadmap?id=<%=r.getId()%>"> Open
						Roadmap </a> <a class="btn ai-btn"
						href="viewAITimetable?roadmapId=<%=r.getId()%>"> AI Timetable

					</a>




				</div>





			</div>






			<%
			}

			}
			%>



		</div>



	</div>


<script src="js/loading.js"></script>
<div id="loadingContainer"></div>

<script>
window.addEventListener("DOMContentLoaded", async () => {

    const res = await fetch("includes/loading.html");
    const html = await res.text();

    document.getElementById("loadingContainer").innerHTML = html;

});
</script>
</body>

</html>