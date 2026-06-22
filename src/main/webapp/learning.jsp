<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ page import="model.SubTopic"%>
<%@ page import="model.LearningResource"%>
<%@ page import="java.util.List"%>
<%@ page import="com.google.gson.Gson"%>


<%
SubTopic sub = (SubTopic) request.getAttribute("subtopic");

List<LearningResource> videos = (List<LearningResource>) request.getAttribute("videos");

String keywordJson = new Gson().toJson(sub.getAiKeywords());

if (sub == null) {

	response.sendRedirect("dashboard.jsp");

	return;

}
%>


<!DOCTYPE html>


<html>


<head>


<meta charset="UTF-8">

<meta name="viewport" content="width=device-width, initial-scale=1.0">


<title>Learn | DevTracker</title>


<link rel="stylesheet" href="css/main.css">


<style>
</style>


</head>


<body>


	<div class="layout">


		<main class="main">



			<div class="top-bar">


				<div>


					<h1>
						📚
						<%=sub.getName()%>
					</h1>


					<button class="lesson-check-btn"
						onclick="toggleLesson(
									<%=sub.getId()%>,
									<%=!sub.isCompleted()%>,
									this
								)">


						<%
						if (sub.isCompleted()) {
						%>

						<i class="ti ti-circle-check-filled"></i> Completed

						<%
						} else {
						%>

						<i class="ti ti-circle"></i> Mark Complete

						<%
						}
						%>


					</button>

				</div>



				<button class="btn" onclick="history.back()">Back</button>



			</div>





			<div class="stats-card">


				<div class="learning-body">


					<%=sub.getAiLearning()%>


				</div>


			</div>






			<div class="stats-card">


				<h2>🎬 Recommended Videos</h2>



				<div class="video-grid">


					<%
					if (videos != null) {

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



						<p>

							<%=v.getReason()%>

						</p>



					</div>

					<%
					}

					}
					%>

					<div class="stats-card">


						<h2>🔍 Didn't find what you need?</h2>


						<p class="muted-text">Try these searches</p>


						<div id="keywordBox" class="keyword-box"></div>



						<br>


						<form action="searchVideos" method="get">


							<input type="hidden" name="subtopicId" value="<%=sub.getId()%>">


							<input type="text" name="query"
								placeholder="Search videos (topic, language, teacher, exam...)"
								required>


							<button class="btn">Search</button>


						</form>


					</div>


				</div>


			</div>





		</main>


	</div>

	<script>


let rawKeywords =
	<%=keywordJson%>;


let keywords = [];


try {


	keywords =
		JSON.parse(rawKeywords);


}
catch(e){


	keywords=[];


}



let box =
	document.getElementById(
		"keywordBox"
	);



keywords.forEach(k=>{


	let link =
		document.createElement("a");


	link.className =
		"keyword-chip";


	link.innerText =
		k;



	link.href =
		"searchVideos?subtopicId=<%=sub.getId()%>&query="
		+ encodeURIComponent(k);



	box.appendChild(link);



});
function toggleLesson(id, completed, btn){


	let old = btn.innerHTML;


	btn.innerHTML="Saving...";


	fetch("updateSubTopic", {


		method:"POST",


		headers:{
			"Content-Type":
			"application/x-www-form-urlencoded"
		},


		body:
		"id="+id+
		"&completed="+completed


	})
	.then(()=>{


		if(completed){


			btn.innerHTML =
			'<i class="ti ti-circle-check-filled"></i> Completed';


		}else{


			btn.innerHTML =
			'<i class="ti ti-circle"></i> Mark Complete';


		}


		btn.setAttribute(
			"onclick",
			"toggleLesson("+id+","+!completed+",this)"
		);


	})
	.catch(()=>{

		btn.innerHTML=old;

	});


}


</script>
</body>


</html>