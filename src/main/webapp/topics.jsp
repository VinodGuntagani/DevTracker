<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="dao.TopicDAO"%>
<%@ page import="model.Topic"%>
<%@ page import="java.util.List"%>
<%@ page import="dao.SubTopicDAO"%>

<%
int subjectId = Integer.parseInt(request.getParameter("subjectId"));
int roadmapId = Integer.parseInt(request.getParameter("roadmapId"));

TopicDAO dao = new TopicDAO();

List<Topic> topics = dao.getTopics(subjectId);

SubTopicDAO progressDao = new SubTopicDAO();

int totalTopics = topics.size();

int completedTopics = 0;

int totalProg = 0;

for (Topic t : topics) {

	int p = progressDao.getProgress(t.getId());

	totalProg += p;

	if (p >= 100) {

		completedTopics++;

	}

}

int avgProg = totalTopics > 0 ? totalProg / totalTopics : 0;
%>



<!DOCTYPE html>

<html lang="en">

<head>


<meta charset="UTF-8">

<meta name="viewport" content="width=device-width, initial-scale=1.0">


<title>Topics | DevTracker</title>




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






				<a class="nav-item"
					href="recoverTopics.jsp?subjectId=<%=subjectId%>&roadmapId=<%=roadmapId%>">


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


			<!-- Page header -->

			<div class="page-header">


				<div>


					<div class="breadcrumb">


						<a href="dashboard.jsp"> <i class="ti ti-layout-dashboard"></i>

							Dashboard

						</a> <i class="ti ti-chevron-right"></i> <span> Topics </span>


					</div>





					<div class="page-title">📌 Topics</div>




					<div class="page-sub">

						<%=totalTopics%>
						topic<%=totalTopics != 1 ? "s" : ""%>
						in this subject

					</div>



				</div>






				<div class="header-actions">


					<a class="btn"
						href="recoverTopics.jsp?subjectId=<%=subjectId%>&roadmapId=<%=roadmapId%>">


						<i class="ti ti-recycle"></i> Recover deleted


					</a> <a class="btn" href="subjects.jsp?roadmapId=<%=roadmapId%>"> <i
						class="ti ti-arrow-left"></i> Back


					</a>



				</div>



			</div>






			<!-- Stats -->


			<div class="stats-grid">



				<div class="stat-card">


					<div class="stat-icon si-purple">

						<i class="ti ti-notebook"></i>

					</div>



					<div>


						<div class="stat-val">

							<%=totalTopics%>

						</div>


						<div class="stat-label">Total topics</div>


					</div>


				</div>






				<div class="stat-card">


					<div class="stat-icon si-teal">

						<i class="ti ti-circle-check"></i>

					</div>




					<div>


						<div class="stat-val">

							<%=completedTopics%>

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

							<%=avgProg%>%

						</div>




						<div class="stat-label">Avg. progress</div>


					</div>



				</div>



			</div>







			<!-- Add Topic -->


			<div class="add-panel">



				<div class="add-panel-title">Add new topic</div>




				<form action="addTopic" method="post" class="add-form">




					<input type="hidden" name="subjectId" value="<%=subjectId%>">





					<input type="hidden" name="roadmapId" value="<%=roadmapId%>">






					<input type="text" name="name"
						placeholder="e.g. OOP, Collections, Spring Boot..." required>






					<button class="btn btn-primary" type="submit">


						<i class="ti ti-plus"></i> Add topic


					</button>



				</form>



			</div>









			<!-- Topic Cards -->


			<div>



				<div class="section-hd">


					<h2>All topics</h2>



					<span style="font-size: 12px; color: #aaa;"> <%=totalTopics%>
						total


					</span>



				</div>





				<div class="card-grid">



					<%
					if (topics.isEmpty()) {
					%>




					<p style="color: #aaa; font-size: 14px;">No topics yet. Add one
						above to get started.</p>




					<%
					} else {

					int idx = 1;

					for (Topic t : topics) {

						int progress = progressDao.getProgress(t.getId());

						boolean isDone = progress >= 100;

						boolean isNew = progress == 0;

						String fillClass = isDone ? "high" : (progress >= 40 ? "medium" : "low");

						String badgeClass = isDone ? "badge-done" : (isNew ? "badge-new" : "badge-ongoing");

						String badgeLabel = isDone ? "Completed" : (isNew ? "Not started" : "In progress");

						String badgeIcon = isDone ? "ti-circle-check" : (isNew ? "ti-circle" : "ti-clock");
					%>
					<div class="card">


						<!-- Head -->

						<div class="card-head">


							<div class="card-head-left">


								<div class="subject-number">

									TOPIC
									<%=idx%>

								</div>



								<div class="card-title" id="titleText<%=t.getId()%>">

									<%=t.getName()%>

								</div>



							</div>





							<button class="card-edit-btn" title="Edit"
								onclick="enableEdit(<%=t.getId()%>)">


								<i class="ti ti-pencil"></i>


							</button>



						</div>






						<!-- Status badge -->


						<div id="badgeArea<%=t.getId()%>">


							<span class="badge <%=badgeClass%>"> <i
								class="ti <%=badgeIcon%>" style="font-size: 11px"></i> <%=badgeLabel%>


							</span>


						</div>








						<!-- Edit form -->


						<form id="editForm<%=t.getId()%>" action="editTopic" method="post"
							class="edit-form">





							<input type="hidden" name="id" value="<%=t.getId()%>"> <input
								type="hidden" name="subjectId" value="<%=subjectId%>"> <input
								type="hidden" name="roadmapId" value="<%=roadmapId%>"> <input
								type="text" name="name" value="<%=t.getName()%>">






							<div style="display: flex; gap: 8px;">



								<button class="btn btn-primary btn-xs" type="submit">


									<i class="ti ti-check"></i> Save


								</button>





								<button type="button" class="btn btn-xs"
									onclick="cancelEdit(<%=t.getId()%>)">Cancel</button>



							</div>



						</form>









						<!-- Progress -->


						<div>


							<div class="progress-row">



								<span> Progress </span> <strong> <%=progress%>%

								</strong>



							</div>





							<div class="progress-track">


								<div class="progress-fill <%=fillClass%>"
									style="width:<%=progress%>%"></div>


							</div>



						</div>









						<!-- Actions -->


						<div class="card-actions">






							<a class="btn btn-sm btn-primary"
								href="subtopics.jsp?topicId=<%=t.getId()%>&subjectId=<%=subjectId%>&roadmapId=<%=roadmapId%>">



								<i class="ti ti-arrow-right"></i> Open



							</a>








							<form action="deleteTopic" method="post"
								style="margin: 0; margin-left: auto;"
								onsubmit="return confirm('Delete this topic?')">






								<input type="hidden" name="id" value="<%=t.getId()%>"> <input
									type="hidden" name="subjectId" value="<%=subjectId%>">






								<input type="hidden" name="roadmapId" value="<%=roadmapId%>">






								<button class="btn btn-sm btn-danger" type="submit">


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








					<!-- Empty add card -->


					<div class="card-empty"
						onclick="document.querySelector('.add-form input[type=text]').focus()">



						<i class="ti ti-plus"></i>


						<p>Add a topic</p>



					</div>






				</div>


			</div>



		</main>


	</div>







	<script>


function openSidebar(){


	document.getElementById('sidebar')
	.classList.add('open');


	document.getElementById('overlay')
	.classList.add('open');


	document.body.style.overflow='hidden';


}




function closeSidebar(){


	document.getElementById('sidebar')
	.classList.remove('open');


	document.getElementById('overlay')
	.classList.remove('open');


	document.body.style.overflow='';


}





function enableEdit(id){



	document.getElementById("editForm"+id)
	.style.display="flex";



	document.getElementById("titleText"+id)
	.style.display="none";



	document.getElementById("badgeArea"+id)
	.style.display="none";



}





function cancelEdit(id){



	document.getElementById("editForm"+id)
	.style.display="none";



	document.getElementById("titleText"+id)
	.style.display="block";



	document.getElementById("badgeArea"+id)
	.style.display="block";



}


</script>




</body>


</html>