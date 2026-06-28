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
	// Used to wire up Syllabus / Daily Tasks links in the bottom nav.
	// Pass roadmapId=... on the link to generateLearning to populate these.
	int roadmapId = 0;
	try {
		roadmapId = Integer.parseInt(request.getParameter("roadmapId"));
	} catch (Exception e) {
		roadmapId = 0;
	}

	return;

}

// Used to wire up the Syllabus / Daily Tasks links in the sidebar + bottom nav.
// Pass roadmapId=... on the link to generateLearning to make these active.
int roadmapId = 0;
try {
	roadmapId = Integer.parseInt(request.getParameter("roadmapId"));
} catch (Exception e) {
	roadmapId = 0;
}
%>

<!DOCTYPE html>
<html lang="en">
<head>

<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">

<title>Learn | DevTracker</title>

<link rel="stylesheet" href="css/main.css">
<link rel="stylesheet" href="css/learning.css">
<link rel="stylesheet" href="css/animation.css">
<link rel="stylesheet" href="css/auth.css">
<link rel="stylesheet" href="css/loading.css">
<link rel="stylesheet"
	href="https://cdn.jsdelivr.net/npm/@tabler/icons-webfont@latest/tabler-icons.min.css">
<style>
.more-videos-card {
	margin-top: 32px;
	padding: 28px;
	text-align: center;
	border: 1px dashed var(--border);
	border-radius: 18px;
	background: linear-gradient(135deg, #fafafa, #ffffff);
}

.more-videos-card h3 {
	margin-bottom: 8px;
}

.more-videos-card .btn {
	margin-top: 18px;
	display: inline-flex;
}
</style>
</head>

<body>

	<!-- Mobile top bar -->
	<div class="mobile-topbar">
		<a class="mobile-logo" href="dashboard.jsp"> <i
			class="ti ti-route"></i> DevTracker
		</a>
	</div>

	<div class="layout">

		<!-- Sidebar -->
		<aside class="sidebar" id="sidebar">

			<div class="sidebar-logo">
				<a class="logo-mark" href="dashboard.jsp"> <i
					class="ti ti-route"></i>
					<div>
						<div>DevTracker</div>
						<div class="logo-sub">Learning OS</div>
					</div>
				</a>
			</div>

			<nav class="nav">
				<div class="nav-section-label">Menu</div>

				<a class="nav-item" href="dashboard.jsp"> <i
					class="ti ti-layout-dashboard"></i> Dashboard
				</a> <a class="nav-item" href="create-roadmap.jsp"> <i
					class="ti ti-plus"></i> Create Roadmap
				</a>
				<%
				if (roadmapId > 0) {
				%>
				<a class="nav-item" href="openAIRoadmap?id=<%=roadmapId%>"> <i
					class="ti ti-books"></i> Syllabus
				</a> <a class="nav-item" href="schedule.jsp?roadmapId=<%=roadmapId%>">
					<i class="ti ti-calendar-week"></i> Daily Tasks
				</a> <a class="nav-item active" href=""> <i class="ti ti-notes"></i>
					Notes
				</a>
				<%
				}
				%>

				<div class="nav-section-label">Manage</div>
				<a class="nav-item" href="trash.jsp"> <i class="ti ti-recycle"></i>
					Recover Deleted
				</a>
			</nav>

			<div class="sidebar-footer">
				<div class="user-row">
					<div class="avatar">
						<i class="ti ti-user" style="font-size: 15px"></i>
					</div>
					<div class="user-info">
						<div class="user-name">My Account</div>
						<div class="user-plan">Free plan</div>
					</div>
					<a href="logout" class="logout-btn" title="Logout"> <i
						class="ti ti-logout"></i>
					</a>
				</div>
			</div>

		</aside>

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

				<button class="btn" id="displayBtn" type="button"
					aria-haspopup="dialog" aria-expanded="false"
					onclick="toggleDisplayPanel()">
					<!-- desktop: icon + text -->

					<!-- mobile FAB: icon only (shown via CSS) -->
					<span class="btn-icon-only" aria-hidden="true"> <i
						class="ti ti-adjustments"></i>
					</span> <span class="btn-label">Adjust Display</span>
				</button>

				<a class="btn btn-sm" href="openAIRoadmap?id=<%=roadmapId%>"> <i
					class="ti ti-arrow-left"></i> Back
				</a>

			</div>

			<!-- Reading Mode / Display Settings popover (floats above content, fixed position) -->
			<div id="readingModeOverlay" class="reading-mode-overlay"></div>

			<div id="displayPanel" class="reading-mode-popover" role="dialog"
				aria-label="Display settings">
				<div class="reading-mode-handle"></div>

				<div class="reading-mode-header">
					<h3>Reading Mode</h3>
					<button type="button" class="reading-mode-close"
						aria-label="Close display settings" onclick="closeDisplayPanel()">
						<i class="ti ti-x"></i>
					</button>
				</div>

				<div class="reading-mode-slider-wrap">
					<input type="range" min="0" max="2" step="1" value="1"
						id="sizeSlider" oninput="changeReadingSize(this.value)"
						aria-label="Reading mode">

					<div class="reading-mode-track-labels">
						<span data-mode="0" onclick="changeReadingSize(0)">Compact</span>
						<span data-mode="1" onclick="changeReadingSize(1)">Comfortable</span>
						<span data-mode="2" onclick="changeReadingSize(2)">Focus</span>
					</div>
				</div>
			</div>

			<div class="stats-card">
				<div class="learning-wrapper">
					<div class="learning-card">
						<div class="learning-body">
							<%=sub.getAiLearning()%>
						</div>
					</div>
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
							allowfullscreen></iframe>

						<h3><%=v.getTitle()%></h3>

						<p class="muted-text"><%=v.getChannelName()%></p>

						<p><%=v.getReason()%></p>
					</div>

					<%
					}
					}
					%>
					<div class="more-videos-card">

						<h3>Want more videos?</h3>

						<p class="muted-text">Browse more tutorials from YouTube based
							on this topic.</p>

						<a class="btn" href="searchVideos?subtopicId=<%=sub.getId()%>">
							<i class="ti ti-brand-youtube"></i> Explore More Videos
						</a>

					</div>
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

	<!-- Bottom tab bar (mobile only) -->
	<nav class="bottom-nav">

		<a class="bn-item" href="dashboard.jsp"> <i
			class="ti ti-layout-dashboard"></i> Dashboard
		</a> <a class="bn-item active" href=""> <i class="ti ti-notes"></i>
			Notes
		</a> <a class="bn-item" href="openAIRoadmap?id=<%=roadmapId%>"> <i
			class="ti ti-books"></i> Syllabus
		</a> <a class="bn-item " href="schedule.jsp?roadmapId=<%=roadmapId%>">
			<i class="ti ti-calendar-week"></i> Daily Tasks
		</a>

	</nav>

	<script>
/* ── keywords (your existing logic, untouched) ── */
let rawKeywords = /*= keywordJson =*/[];   // JSP fills this: <%=keywordJson%>

let keywords = [];
try {
  keywords = Array.isArray(rawKeywords) ? rawKeywords : JSON.parse(rawKeywords);
} catch (e) { keywords = []; }

let box = document.getElementById("keywordBox");
keywords.forEach(k => {
  let link = document.createElement("a");
  link.className = "keyword-chip";
  link.innerText = k;
  link.href = "searchVideos?subtopicId=<%=sub.getId()%>&query=" + encodeURIComponent(k);
  box.appendChild(link);
});

/* ── Mark Complete AJAX (untouched) ── */
function toggleLesson(id, completed, btn) {
  let old = btn.innerHTML;
  btn.innerHTML = "Saving…";

  fetch("updateSubTopic", {
    method: "POST",
    headers: { "Content-Type": "application/x-www-form-urlencoded" },
    body: "id=" + id + "&completed=" + completed
  })
  .then(() => {
    if (completed) {
      btn.innerHTML = '<i class="ti ti-circle-check-filled"></i> Completed';
    } else {
      btn.innerHTML = '<i class="ti ti-circle"></i> Mark Complete';
    }
    btn.setAttribute("onclick", "toggleLesson(" + id + "," + !completed + ",this)");
  })
  .catch(() => { btn.innerHTML = old; });
}

/* ══════════════════════════════════════════════════
   READING MODE / DISPLAY SETTINGS
   localStorage key preserved from the original page
══════════════════════════════════════════════════ */
(function () {
  var STORAGE_KEY = "devtracker-reading-mode";        // same key as before
  var MODES       = ["compact", "comfortable", "focus"];

  var displayBtn = document.getElementById("displayBtn");
  var panel      = document.getElementById("displayPanel");
  var overlay    = document.getElementById("readingModeOverlay");
  var slider     = document.getElementById("sizeSlider");
  var labels     = panel.querySelectorAll(".reading-mode-track-labels span");

  /* ── apply a mode by index ── */
  function applyMode(index, persist) {
    index = Math.max(0, Math.min(2, parseInt(index, 10) || 0));

    MODES.forEach(function (m) {
      document.body.classList.remove("reading-" + m);
    });
    document.body.classList.add("reading-" + MODES[index]);

    slider.value = index;

    labels.forEach(function (l) {
      l.classList.toggle("active",
        parseInt(l.getAttribute("data-mode"), 10) === index);
    });

    if (persist) {
      try { localStorage.setItem(STORAGE_KEY, MODES[index]); } catch (e) {}
    }
  }

  /* ── open / close ── */
  function openPanel() {
    panel.classList.add("is-open");
    overlay.classList.add("is-open");
    displayBtn.setAttribute("aria-expanded", "true");
  }
  function closePanel() {
    panel.classList.remove("is-open");
    overlay.classList.remove("is-open");
    displayBtn.setAttribute("aria-expanded", "false");
  }

  window.toggleDisplayPanel = function () {
    panel.classList.contains("is-open") ? closePanel() : openPanel();
  };
  window.closeDisplayPanel  = closePanel;
  window.changeReadingSize  = function (value) { applyMode(value, true); };

  overlay.addEventListener("click", closePanel);
  document.addEventListener("keydown", function (e) {
    if (e.key === "Escape") closePanel();
  });

  /* ── restore saved preference ── */
  var saved = null;
  try { saved = localStorage.getItem(STORAGE_KEY); } catch (e) {}
  var savedIndex = MODES.indexOf(saved);
  applyMode(savedIndex >= 0 ? savedIndex : 1, false);  // default: Comfortable
})();
</script>
	<script src="js/loading.js"></script>
	<div id="loadingContainer"></div>

	<script>
window.addEventListener("DOMContentLoaded", async () => {

    const res = await fetch("includes/loading.html");
    const html = await res.text();

    document.getElementById("loadingContainer").innerHTML = html;

});
</script>
</body>
</html>
