<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<!DOCTYPE html>
<html>

<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Generating AI Notes</title>

<link rel="preconnect" href="https://fonts.googleapis.com">
<link href="https://fonts.googleapis.com/css2?family=Sora:wght@400;600;700&family=JetBrains+Mono:wght@400;600&display=swap" rel="stylesheet">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/ai-progress.css">
</head>

<body>

	<div class="container" id="pageContainer">

		<div class="header-card">
			<span class="badge">Generating</span>
			<h1>AI Notes Generator</h1>
			<p>Sit tight — your learning notes are being written topic by topic.</p>
		</div>

		<div class="progress-card">

			<div class="progress-ring-wrap">
				<svg viewBox="0 0 100 100">
					<defs>
						<linearGradient id="ringGradient" x1="0%" y1="0%" x2="100%" y2="100%">
							<stop offset="0%" stop-color="#d99a3f" />
							<stop offset="100%" stop-color="#8f5a15" />
						</linearGradient>
					</defs>
					<circle class="progress-ring-track" cx="50" cy="50" r="42"></circle>
					<circle id="progressRing" class="progress-ring-fill" cx="50" cy="50" r="42"
						stroke-dasharray="264" stroke-dashoffset="264"></circle>
				</svg>
				<div class="progress-ring-label"><span id="ringPercent">0%</span></div>
			</div>

			<div class="progress-main">
				<div class="card-title">
					<h2>Overall Progress</h2>
				</div>
				<div class="progress-bar" role="progressbar" aria-valuemin="0" aria-valuemax="100" aria-valuenow="0">
					<div id="progressFill" class="progress-fill"></div>
				</div>
				<div class="progress-info">
					<span id="progressText">0%</span>
					<span id="completedText">0 / 0 Completed</span>
				</div>
			</div>

		</div>

		<div id="currentCard" class="current-card">
			<div class="card-title">
				<h2><span class="pulse-dot"></span>Currently Generating</h2>
			</div>
			<div class="current-info">
				<div class="current-row">
					<span class="label">Subject</span>
					<span class="value" id="currentSubject">Waiting...</span>
				</div>
				<div class="current-row">
					<span class="label">Topic</span>
					<span class="value" id="currentTopic">Waiting...</span>
				</div>
			</div>
		</div>

		<div class="tasks-card">
			<div class="card-title">
				<h2>Lessons</h2>
			</div>
			<div id="taskList">
				<p class="empty-hint">Lessons will appear here once generation starts.</p>
			</div>
		</div>

		<div class="status-card">
			<div class="card-title">
				<h2>Status</h2>
			</div>
			<p id="statusText">Preparing AI worker...</p>
		</div>

	</div>

	<script>
		const jobId = new URLSearchParams(window.location.search).get("jobId");
	</script>
	<script src="${pageContext.request.contextPath}/js/ai-progress.js"></script>

</body>

</html>
