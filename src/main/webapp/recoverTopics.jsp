<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="dao.TopicDAO"%>
<%@ page import="model.Topic"%>
<%@ page import="java.util.List"%>

<%
int subjectId = Integer.parseInt(request.getParameter("subjectId"));
int roadmapId = Integer.parseInt(request.getParameter("roadmapId"));

TopicDAO dao = new TopicDAO();

List<Topic> topics = dao.getDeletedTopics(subjectId);
%>

<!DOCTYPE html>
<html>

<head>
<meta charset="UTF-8">

<title>Recover Topics</title>

<link rel="stylesheet" href="css/main.css">
</head>


<body>

	<div class="dashboard-container">

		<div class="top-bar">

			<div>
				<h1>♻️ Recover Topics</h1>

				<p class="muted-text">Restore deleted topics</p>
			</div>


			<a class="btn"
				href="topics.jsp?subjectId=<%=subjectId%>&roadmapId=<%=roadmapId%>">
				Back </a>

		</div>



		<div class="roadmap-grid">

			<%
			if (topics.isEmpty()) {
			%>

			<div class="empty-card">
				<h3>No deleted topics</h3>
			</div>

			<%
			}

			for (Topic t : topics) {
			%>


			<div class="roadmap-card">

				<h3>
					📌
					<%=t.getName()%></h3>


				<form action="restoreTopic" method="post">

					<input type="hidden" name="id" value="<%=t.getId()%>"> <input
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