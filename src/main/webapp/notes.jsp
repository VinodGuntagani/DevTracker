<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ page import="model.SubTopic"%>


<%
SubTopic sub = (SubTopic) request.getAttribute("subtopic");

if (sub == null) {

	response.sendRedirect("dashboard.jsp");

	return;

}
%>


<!DOCTYPE html>

<html>

<head>

<meta charset="UTF-8">

<title>Notes | DevTracker</title>


<link rel="stylesheet" href="css/main.css">


<style>
.notes-body {
	line-height: 1.7;
	font-size: 16px;
}

.notes-body h2 {
	margin-top: 10px;
	margin-bottom: 15px;
}

.notes-body h3 {
	margin-top: 25px;
	margin-bottom: 10px;
}

.notes-body ul {
	margin-left: 25px;
}

.notes-body li {
	margin-bottom: 8px;
}

.notes-body table {
	width: 100%;
	border-collapse: collapse;
	margin: 15px 0;
}

.notes-body th, .notes-body td {
	border: 1px solid #ddd;
	padding: 10px;
	text-align: left;
}

.notes-body pre {
	background: #f4f4f5;
	padding: 15px;
	border-radius: 10px;
	overflow-x: auto;
}

.notes-body code {
	font-family: Consolas, monospace;
}
</style>


</head>


<body>


	<div class="layout">


		<main class="main">


			<div class="top-bar">


				<div>

					<h1>📝 AI Notes</h1>

					<p class="muted-text">

						<%=sub.getName()%>

					</p>

				</div>


				<button class="btn" onclick="history.back()">Back</button>


			</div>



			<div class="stats-card">


				<div class="notes-body">

					<%=sub.getAiNotes()%>

				</div>


			</div>



		</main>


	</div>


</body>

</html>