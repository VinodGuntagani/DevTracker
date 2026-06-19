<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="dao.SubjectDAO"%>
<%@ page import="model.Subject"%>
<%@ page import="java.util.List"%>

<%
int roadmapId = Integer.parseInt(request.getParameter("roadmapId"));

SubjectDAO dao = new SubjectDAO();

List<Subject> subjects = dao.getDeletedSubjects(roadmapId);
%>

<!DOCTYPE html>
<html>

<head>
<meta charset="UTF-8">

<title>Recover Subjects</title>

<link rel="stylesheet" href="css/main.css">
</head>

<body>

	<div class="dashboard-container">

		<div class="top-bar">

			<div>
				<h1>♻️ Recover Subjects</h1>

				<p class="muted-text">Restore deleted subjects</p>
			</div>


			<a class="btn" href="subjects.jsp?roadmapId=<%=roadmapId%>"> Back
			</a>

		</div>


		<div class="roadmap-grid">

			<%
			if (subjects.isEmpty()) {
			%>

			<div class="empty-card">
				<h3>No deleted subjects</h3>
			</div>

			<%
			}

			for (Subject s : subjects) {
			%>

			<div class="roadmap-card">

				<h3>
					📘
					<%=s.getName()%></h3>

				<form action="restoreSubject" method="post">

					<input type="hidden" name="id" value="<%=s.getId()%>"> <input
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