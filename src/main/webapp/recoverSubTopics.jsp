<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="dao.SubTopicDAO"%>
<%@ page import="model.SubTopic"%>
<%@ page import="java.util.List"%>

<%
int topicId = Integer.parseInt(request.getParameter("topicId"));
int subjectId = Integer.parseInt(request.getParameter("subjectId"));
int roadmapId = Integer.parseInt(request.getParameter("roadmapId"));

SubTopicDAO dao = new SubTopicDAO();

List<SubTopic> list = dao.getDeletedSubTopics(topicId);
%>

<!DOCTYPE html>
<html>

<head>
<meta charset="UTF-8">

<title>Recover Sub Topics</title>

<link rel="stylesheet" href="css/main.css">
</head>

<body>

	<div class="dashboard-container">

		<div class="top-bar">

			<div>
				<h1>♻️ Recover Sub Topics</h1>

				<p class="muted-text">Restore deleted learning tasks</p>
			</div>


			<a class="btn"
				href="subtopics.jsp?topicId=<%=topicId%>&subjectId=<%=subjectId%>&roadmapId=<%=roadmapId%>">
				Back </a>

		</div>


		<div class="task-list">

			<%
			if (list.isEmpty()) {
			%>

			<div class="empty-card">
				<h3>No deleted subtopics</h3>
			</div>

			<%
			}

			for (SubTopic s : list) {
			%>


			<div class="task-card">

				<h3>
					✅
					<%=s.getName()%></h3>


				<form action="restoreSubTopic" method="post">

					<input type="hidden" name="id" value="<%=s.getId()%>"> <input
						type="hidden" name="topicId" value="<%=topicId%>"> <input
						type="hidden" name="subjectId" value="<%=subjectId%>"> <input
						type="hidden" name="roadmapId" value="<%=roadmapId%>">


					<button class="btn primary-btn">Restore</button>

				</form>

			</div>


			<%
			}
			%>


		</div>


	</div>


</body>

</html>