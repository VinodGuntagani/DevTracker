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

</head>


<body>


	<div class="auth-container">


		<div class="auth-card">


			<h1>📚 Create Roadmap</h1>


			<p class="auth-subtitle">Plan your learning journey</p>




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






				<button class="btn primary-btn" type="submit">Create
					Roadmap</button>




			</form>





			<p class="auth-link">


				<a href="dashboard.jsp"> ← Back Dashboard </a>


			</p>





		</div>


	</div>



</body>


</html>