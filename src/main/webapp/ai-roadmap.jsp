<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>

<html>

<head>

<meta charset="UTF-8">

<title>AI Roadmap Generator | DevTracker</title>

<link rel="stylesheet" href="css/main.css">

</head>


<body>


	<div class="auth-container">


		<div class="auth-card ai-generator">


			<h1>🤖 AI Roadmap Generator</h1>


			<p class="auth-subtitle">Tell AI your goal and get a complete
				learning plan</p>




			<form action="generateAIRoadmap" method="post">



				<div class="input-group">


					<label>Your Goal</label> <input type="text" name="goal"
						placeholder="Example: Java Backend Developer" required>


				</div>





				<div class="input-group">


					<label>Target Days</label> <input type="number" name="days"
						value="90" min="1" required>


				</div>

				<div class="input-group">

					<label>Daily Study Time (minutes)</label> <input type="number"
						name="dailyMinutes" value="120" min="5" required>

				</div>




				<button class="btn ai-btn" type="submit">Generate AI
					Roadmap 🤖</button>



			</form>






			<p class="auth-link">

				<a href="dashboard.jsp"> ← Back Dashboard </a>

			</p>





		</div>


	</div>



</body>


</html>