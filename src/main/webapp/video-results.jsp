<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ page import="java.util.List"%>
<%@ page import="model.LearningResource"%>


<%
List<LearningResource> videos = (List<LearningResource>) request.getAttribute("videos");
%>


<!DOCTYPE html>


<html>


<head>


<meta charset="UTF-8">


<title>Video Results | DevTracker</title>


<link rel="stylesheet" href="css/main.css">



<style>
.video-grid {
	display: grid;
	grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
	gap: 20px;
	margin-top: 20px;
}

.video-card {
	background: white;
	padding: 15px;
	border-radius: 15px;
	box-shadow: 0 5px 20px rgba(0, 0, 0, .08);
}

.video-card iframe {
	width: 100%;
	height: 220px;
	border: 0;
	border-radius: 12px;
}

.video-card h3 {
	margin-top: 12px;
}
</style>



</head>


<body>



	<div class="layout">



		<main class="main">



			<div class="top-bar">


				<div>


					<h1>🎬 Search Results</h1>


					<p class="muted-text">YouTube learning videos</p>


				</div>



				<button class="btn" onclick="history.back()">Back</button>



			</div>






			<div class="stats-card">



				<div class="video-grid">



					<%
					if (videos != null && videos.size() > 0) {

						for (LearningResource v : videos) {
					%>



					<div class="video-card">



						<iframe src="https://www.youtube.com/embed/<%=v.getVideoId()%>"
							allowfullscreen> </iframe>




						<h3>

							<%=v.getTitle()%>

						</h3>




						<p class="muted-text">


							<%=v.getChannelName()%>


						</p>




					</div>



					<%
					}

					} else {
					%>


					<h3>No videos found 😢</h3>


					<%
					}
					%>



				</div>


			</div>





		</main>



	</div>




</body>


</html>