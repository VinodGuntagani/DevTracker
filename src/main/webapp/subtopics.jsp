<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="dao.SubTopicDAO"%>
<%@ page import="model.SubTopic"%>
<%@ page import="java.util.List"%>

<%
int topicId = Integer.parseInt(request.getParameter("topicId"));
int subjectId = Integer.parseInt(request.getParameter("subjectId"));
int roadmapId = Integer.parseInt(request.getParameter("roadmapId"));

SubTopicDAO dao = new SubTopicDAO();

List<SubTopic> list = dao.getSubTopics(topicId);

int progress = dao.getProgress(topicId);

int totalSubTopics = list.size();

int completedSubTopics = 0;

for (SubTopic s : list) {

	if (s.isCompleted()) {

		completedSubTopics++;

	}

}
%>




<!DOCTYPE html>

<html lang="en">

<head>


<meta charset="UTF-8">


<meta name="viewport" content="width=device-width, initial-scale=1.0">


<title>Subtopics | DevTracker</title>




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


					Subtopics



				</a> <a class="nav-item" href="ai-roadmap.jsp"> <i
					class="ti ti-robot"></i> AI tools



				</a> <a class="nav-item" href="viewAIRoadmaps"> <i
					class="ti ti-layout-list"></i> AI roadmaps



				</a>









				<div class="nav-section-label">Manage</div>









				<a class="nav-item"
					href="recoverSubTopics.jsp?topicId=<%=topicId%>&subjectId=<%=subjectId%>&roadmapId=<%=roadmapId%>">



					<i class="ti ti-recycle"></i> Recover deleted



				</a>





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
		<!-- Main -->

		<main class="main">



			<!-- Page Header -->


			<div class="page-header">



				<div>



					<div class="breadcrumb">



						<a href="dashboard.jsp"> <i class="ti ti-layout-dashboard"></i>

							Dashboard


						</a> <i class="ti ti-chevron-right"></i> <span> Subtopics </span>



					</div>






					<div class="page-title">✅ Subtopics</div>






					<div class="page-sub">


						<%=totalSubTopics%>
						learning tasks in this topic


					</div>




				</div>








				<div class="header-actions">






					<a class="btn"
						href="recoverSubTopics.jsp?topicId=<%=topicId%>&subjectId=<%=subjectId%>&roadmapId=<%=roadmapId%>">



						<i class="ti ti-recycle"></i> Recover deleted



					</a> <a class="btn"
						href="topics.jsp?subjectId=<%=subjectId%>&roadmapId=<%=roadmapId%>">



						<i class="ti ti-arrow-left"></i> Back



					</a>





				</div>




			</div>









			<!-- Stats -->



			<div class="stats-grid">








				<div class="stat-card">



					<div class="stat-icon si-purple">


						<i class="ti ti-list-check"></i>


					</div>





					<div>



						<div class="stat-val">


							<%=totalSubTopics%>


						</div>




						<div class="stat-label">Total tasks</div>



					</div>




				</div>











				<div class="stat-card">




					<div class="stat-icon si-teal">


						<i class="ti ti-circle-check"></i>


					</div>






					<div>



						<div class="stat-val">


							<%=completedSubTopics%>


						</div>





						<div class="stat-label">Completed</div>



					</div>




				</div>












				<div class="stat-card">



					<div class="stat-icon si-blue">


						<i class="ti ti-trending-up"></i>


					</div>





					<div>




						<div class="stat-val">


							<%=progress%>%


						</div>





						<div class="stat-label">Progress</div>




					</div>




				</div>






			</div>











			<!-- Add Subtopic -->



			<div class="add-panel">






				<div class="add-panel-title">Add new subtopic</div>







				<form action="addSubTopic" method="post" class="add-form">






					<input type="hidden" name="topicId" value="<%=topicId%>"> <input
						type="hidden" name="subjectId" value="<%=subjectId%>"> <input
						type="hidden" name="roadmapId" value="<%=roadmapId%>"> <input
						type="text" name="name" placeholder="Subtopic name..." required>







					<select name="difficulty">


						<option>Easy</option>


						<option selected>Medium</option>


						<option>Hard</option>


					</select> <input type="number" name="hours" min="1" value="1">








					<button class="btn btn-primary" type="submit">



						<i class="ti ti-plus"></i> Add


					</button>






				</form>





			</div>











			<!-- Subtopic Cards -->


			<div>





				<div class="section-hd">





					<h2>All subtopics</h2>






					<span style="font-size: 12px; color: #aaa;"> <%=totalSubTopics%>
						total


					</span>






				</div>








				<div class="card-grid">









					<%
					if (list.isEmpty()) {
					%>






					<p style="color: #aaa; font-size: 14px;">No subtopics yet. Add
						one above.</p>








					<%
					} else {

					int idx = 1;

					for (SubTopic s : list) {

						String badgeClass = s.isCompleted() ? "badge-done" : "badge-ongoing";

						String badgeLabel = s.isCompleted() ? "Completed" : "Pending";

						String badgeIcon = s.isCompleted() ? "ti-circle-check" : "ti-clock";
					%>
					<div class="card">


						<!-- Head -->

						<div class="card-head">


							<div class="card-head-left">


								<div class="subject-number">

									SUBTOPIC
									<%=idx%>

								</div>



								<div class="card-title" id="titleText<%=s.getId()%>">


									<%=s.getName()%>


								</div>



							</div>





							<button class="card-edit-btn"
								onclick="enableEdit(<%=s.getId()%>)">



								<i class="ti ti-pencil"></i>



							</button>




						</div>







						<!-- Status -->


						<div id="badgeArea<%=s.getId()%>">



							<span class="badge <%=badgeClass%>"> <i
								class="ti <%=badgeIcon%>"></i> <%=badgeLabel%>


							</span>




						</div>









						<!-- Complete checkbox -->


						<form action="updateSubTopic" method="post"
							style="margin-top: 15px;">






							<input type="hidden" name="id" value="<%=s.getId()%>"> <input
								type="hidden" name="topicId" value="<%=topicId%>"> <input
								type="hidden" name="subjectId" value="<%=subjectId%>"> <input
								type="hidden" name="roadmapId" value="<%=roadmapId%>"> <label>


								<input type="checkbox" name="completed"
								onchange="this.form.submit()"
								<%=s.isCompleted() ? "checked" : ""%>> Mark completed



							</label>





						</form>









						<!-- Details -->


						<div style="margin-top: 15px;">



							<span class="difficulty-badge"> <%=s.getDifficulty()%>


							</span> <span class="time-pill"> ⏱ <%=s.getEstimatedMinutes()%> hrs


							</span> <span class="time-pill"> Weight <%=s.getWeight()%>


							</span>




						</div>









						<!-- Edit -->


						<form id="editForm<%=s.getId()%>" action="editSubTopic"
							method="post" class="edit-form">






							<input type="hidden" name="id" value="<%=s.getId()%>"> <input
								type="hidden" name="topicId" value="<%=topicId%>"> <input
								type="hidden" name="subjectId" value="<%=subjectId%>"> <input
								type="hidden" name="roadmapId" value="<%=roadmapId%>"> <input
								type="text" name="name" value="<%=s.getName()%>">







							<div style="display: flex; gap: 8px;">



								<button class="btn btn-primary btn-xs">


									<i class="ti ti-check"></i> Save


								</button>





								<button type="button" class="btn btn-xs"
									onclick="cancelEdit(<%=s.getId()%>)">Cancel</button>



							</div>





						</form>









						<!-- Actions -->


						<div class="card-actions">








							<form action="deleteSubTopic" method="post"
								style="margin-left: auto;"
								onsubmit="return confirm('Delete this subtopic?')">






								<input type="hidden" name="id" value="<%=s.getId()%>"> <input
									type="hidden" name="topicId" value="<%=topicId%>"> <input
									type="hidden" name="subjectId" value="<%=subjectId%>">






								<input type="hidden" name="roadmapId" value="<%=roadmapId%>">







								<button class="btn btn-sm btn-danger">


									<i class="ti ti-trash"></i> Delete


								</button>




							</form>




						</div>





					</div>






					<%
					idx++;

					}

					}
					%>








					<div class="card-empty"
						onclick="document.querySelector('.add-form input[type=text]').focus()">



						<i class="ti ti-plus"></i>



						<p>Add a subtopic</p>



					</div>








				</div>



			</div>




		</main>


	</div>








	<script>


function openSidebar(){

	document.getElementById("sidebar").classList.add("open");

	document.getElementById("overlay").classList.add("open");

}



function closeSidebar(){

	document.getElementById("sidebar").classList.remove("open");

	document.getElementById("overlay").classList.remove("open");

}





function enableEdit(id){


	document.getElementById("editForm"+id).style.display="flex";


	document.getElementById("titleText"+id).style.display="none";


	document.getElementById("badgeArea"+id).style.display="none";


}




function cancelEdit(id){


	document.getElementById("editForm"+id).style.display="none";


	document.getElementById("titleText"+id).style.display="block";


	document.getElementById("badgeArea"+id).style.display="block";


}



</script>




</body>


</html>