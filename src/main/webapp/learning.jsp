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
/* ─── Tabler icons (already in your head, just shown for ref) ─── */
@import
	url("https://cdn.jsdelivr.net/npm/@tabler/icons-webfont@latest/tabler-icons.min.css")
	;

/* ════════════════════════════════════════════════════
   BASE LEARNING PAGE
════════════════════════════════════════════════════ */
.main {
	padding: 28px 32px;
}

@media ( max-width : 600px) {
	.main {
		padding: 16px;
	}
	.top-bar {
		flex-direction: column;
		align-items: flex-start;
		gap: 12px;
	}
	.top-bar h1 {
		font-size: 20px;
		line-height: 1.3;
	}
	.stats-card {
		width: 100%;
		padding: 18px;
		border-radius: 18px;
	}
	.video-grid {
		grid-template-columns: 1fr;
	}
	.video-card iframe {
		height: 200px;
	}
}

/* ════════════════════════════════════════════════════
   LEARNING WRAPPER — all three modes live here
════════════════════════════════════════════════════ */
.learning-wrapper {
	display: flex;
	justify-content: center;
}

.learning-card {
	width: 100%;
	max-width: 760px;
	margin: 0 auto;
	padding: 4px;
	/* TASK 4: smooth layout transitions */
	transition: max-width .38s cubic-bezier(.4, 0, .2, 1), padding .38s
		cubic-bezier(.4, 0, .2, 1);
}

.learning-body {
	font-size: 16px;
	line-height: 1.75;
	color: var(--text, #3a2c1a);
	transition: font-size .38s cubic-bezier(.4, 0, .2, 1), line-height .38s
		cubic-bezier(.4, 0, .2, 1);
	overflow-wrap: break-word;
	word-break: break-word;
}

/* ── TASK 3: AI content never breaks mobile ── */
.learning-body img, .learning-body video, .learning-body iframe {
	max-width: 100%;
	height: auto;
	display: block;
}

.learning-body table {
	display: block;
	max-width: 100%;
	overflow-x: auto;
	-webkit-overflow-scrolling: touch;
	border-collapse: collapse;
}

.learning-body table th, .learning-body table td {
	padding: 8px 12px;
	border: 1px solid var(--border, #e8dfd2);
	text-align: left;
}

.learning-body pre, .learning-body code {
	max-width: 100%;
	overflow-x: auto;
	-webkit-overflow-scrolling: touch;
	white-space: pre-wrap;
	word-break: break-word;
}

.learning-body pre {
	padding: 14px 16px;
	border-radius: 10px;
	background: var(--surface-2, #f5f0e8);
	font-size: .88em;
}

/* Animate all child section spacing */
.learning-body>*, .learning-body div, .learning-body section {
	transition: padding .38s cubic-bezier(.4, 0, .2, 1), margin .38s
		cubic-bezier(.4, 0, .2, 1);
}

/* ════════════════════════════════════════════════════
   TASK 2: THREE READING MODES — stronger differences
════════════════════════════════════════════════════ */

/* ── COMPACT: tight, phone-friendly revision mode ── */
body.reading-compact .learning-card {
	max-width: 520px;
	padding: 0;
}

body.reading-compact .learning-body {
	font-size: 13.5px;
	line-height: 1.55;
}

body.reading-compact .learning-body h1 {
	font-size: 1.25em;
	margin: 10px 0 6px;
}

body.reading-compact .learning-body h2 {
	font-size: 1.1em;
	margin: 8px 0 5px;
}

body.reading-compact .learning-body h3 {
	font-size: 1em;
	margin: 6px 0 4px;
}

body.reading-compact .learning-body p {
	margin: 5px 0;
}

body.reading-compact .learning-body div, body.reading-compact .learning-body section,
	body.reading-compact .learning-body [class] {
	padding: 8px 10px !important;
	margin: 6px 0 !important;
	border-radius: 8px !important;
}

body.reading-compact .learning-body pre {
	padding: 8px 10px;
}

body.reading-compact .learning-body ul, body.reading-compact .learning-body ol
	{
	padding-left: 16px;
	margin: 4px 0;
}

body.reading-compact .learning-body li {
	margin: 2px 0;
}

/* ── COMFORTABLE: default balanced reading ── */
body.reading-comfortable .learning-card {
	max-width: 760px;
	padding: 4px;
}

body.reading-comfortable .learning-body {
	font-size: 16px;
	line-height: 1.75;
}

body.reading-comfortable .learning-body h1 {
	font-size: 1.6em;
	margin: 18px 0 10px;
}

body.reading-comfortable .learning-body h2 {
	font-size: 1.3em;
	margin: 14px 0 8px;
}

body.reading-comfortable .learning-body h3 {
	font-size: 1.1em;
	margin: 10px 0 6px;
}

body.reading-comfortable .learning-body p {
	margin: 10px 0;
}

body.reading-comfortable .learning-body div, body.reading-comfortable .learning-body section,
	body.reading-comfortable .learning-body [class] {
	padding: 18px 20px !important;
	margin: 14px 0 !important;
	border-radius: 12px !important;
}

body.reading-comfortable .learning-body ul, body.reading-comfortable .learning-body ol
	{
	padding-left: 22px;
	margin: 8px 0;
}

body.reading-comfortable .learning-body li {
	margin: 5px 0;
}

/* ── FOCUS: immersive, wide, desktop reading ── */
body.reading-focus .learning-card {
	max-width: 980px;
	padding: 12px;
}

body.reading-focus .learning-body {
	font-size: 18.5px;
	line-height: 1.95;
}

body.reading-focus .learning-body h1 {
	font-size: 2em;
	margin: 28px 0 14px;
}

body.reading-focus .learning-body h2 {
	font-size: 1.6em;
	margin: 22px 0 12px;
}

body.reading-focus .learning-body h3 {
	font-size: 1.25em;
	margin: 16px 0 8px;
}

body.reading-focus .learning-body p {
	margin: 14px 0;
}

body.reading-focus .learning-body div, body.reading-focus .learning-body section,
	body.reading-focus .learning-body [class] {
	padding: 28px 32px !important;
	margin: 22px 0 !important;
	border-radius: 16px !important;
}

body.reading-focus .learning-body ul, body.reading-focus .learning-body ol
	{
	padding-left: 28px;
	margin: 12px 0;
}

body.reading-focus .learning-body li {
	margin: 8px 0;
}

body.reading-focus .learning-body pre {
	padding: 20px 22px;
}

/* never overflow on small screens regardless of mode */
@media ( max-width : 600px) {
	.learning-card, body.reading-compact   .learning-card, body.reading-comfortable .learning-card,
		body.reading-focus     .learning-card {
		max-width: 100%;
		padding: 0;
	}
}

/* ════════════════════════════════════════════════════
   TASK 1: DISPLAY BUTTON — desktop normal, mobile FAB
════════════════════════════════════════════════════ */

/* Desktop: normal btn — nothing special needed */
#displayBtn .btn-icon-only {
	display: none;
}

#displayBtn .btn-label {
	display: inline;
}

/* Mobile: floating circular action button */
@media ( max-width : 600px) {
	#displayBtn {
		position: fixed;
		top: max(16px, env(safe-area-inset-top, 16px));
		right: max(16px, env(safe-area-inset-right, 16px));
		width: 48px;
		height: 48px;
		border-radius: 50%;
		padding: 0;
		display: flex;
		align-items: center;
		justify-content: center;
		/* cream/glass card style matching theme */
		background: rgba(255, 250, 244, 0.88);
		backdrop-filter: blur(12px);
		-webkit-backdrop-filter: blur(12px);
		border: 1px solid rgba(184, 116, 31, .18);
		box-shadow: 0 4px 16px rgba(60, 40, 10, .14), 0 1px 4px
			rgba(60, 40, 10, .08);
		z-index: 900;
		transition: transform .18s ease, box-shadow .18s ease, background .18s
			ease;
	}
	#displayBtn:hover, #displayBtn:active {
		transform: scale(1.08);
		box-shadow: 0 8px 24px rgba(60, 40, 10, .2), 0 2px 6px
			rgba(60, 40, 10, .12);
		background: rgba(255, 250, 244, 0.97);
	}
	#displayBtn:active {
		transform: scale(.96);
	}

	/* hide text label, show only icon */
	#displayBtn .btn-label {
		display: none;
	}
	#displayBtn .btn-icon-only {
		display: inline-flex;
		align-items: center;
	}
	#displayBtn .btn-icon-only i {
		font-size: 22px;
		color: var(--accent, #b8741f);
	}
}

/* ════════════════════════════════════════════════════
   READING MODE OVERLAY (click-catcher)
════════════════════════════════════════════════════ */
.reading-mode-overlay {
	position: fixed;
	inset: 0;
	background: transparent;
	opacity: 0;
	visibility: hidden;
	transition: opacity .25s ease, visibility .25s ease;
	z-index: 998;
}

.reading-mode-overlay.is-open {
	opacity: 1;
	visibility: visible;
}

/* ════════════════════════════════════════════════════
   READING MODE POPOVER — cream themed
════════════════════════════════════════════════════ */
.reading-mode-popover {
	position: fixed;
	top: 72px;
	right: 20px;
	width: 290px;
	max-width: calc(100vw - 32px);
	background: var(--card-bg, #fffaf4);
	border: 1px solid var(--border, #e8dfd2);
	border-radius: 18px;
	box-shadow: 0 16px 40px rgba(60, 40, 10, .16), 0 2px 8px
		rgba(60, 40, 10, .08);
	padding: 18px 20px 22px;
	z-index: 999;
	opacity: 0;
	visibility: hidden;
	transform: translateY(-10px) scale(.96);
	transform-origin: top right;
	transition: opacity .22s cubic-bezier(.2, .8, .2, 1), transform .22s
		cubic-bezier(.2, .8, .2, 1), visibility .22s ease;
}

.reading-mode-popover.is-open {
	opacity: 1;
	visibility: visible;
	transform: translateY(0) scale(1);
}

.reading-mode-handle {
	display: none;
}

.reading-mode-header {
	display: flex;
	align-items: center;
	justify-content: space-between;
	margin-bottom: 16px;
}

.reading-mode-header h3 {
	margin: 0;
	font-size: 14px;
	font-weight: 700;
	letter-spacing: .03em;
	color: var(--text, #3a2c1a);
	text-transform: uppercase;
}

.reading-mode-close {
	background: none;
	border: none;
	cursor: pointer;
	font-size: 16px;
	color: var(--text-muted, #8a7a64);
	width: 28px;
	height: 28px;
	border-radius: 8px;
	display: flex;
	align-items: center;
	justify-content: center;
	transition: background .15s;
}

.reading-mode-close:hover {
	background: var(--bg, #f7f0e6);
}

/* ── slider ── */
.reading-mode-slider-wrap {
	position: relative;
}

.reading-mode-slider-wrap input[type="range"] {
	width: 100%;
	height: 6px;
	appearance: none;
	-webkit-appearance: none;
	background: var(--border, #e8dfd2);
	border-radius: 99px;
	outline: none;
	cursor: pointer;
	margin-bottom: 10px;
	accent-color: var(--accent, #b8741f);
}

.reading-mode-slider-wrap input[type="range"]::-webkit-slider-thumb {
	-webkit-appearance: none;
	width: 20px;
	height: 20px;
	border-radius: 50%;
	background: var(--accent, #b8741f);
	box-shadow: 0 2px 8px rgba(184, 116, 31, .35);
	transition: transform .15s ease, box-shadow .15s ease;
	cursor: pointer;
}

.reading-mode-slider-wrap input[type="range"]::-webkit-slider-thumb:hover
	{
	transform: scale(1.2);
	box-shadow: 0 4px 14px rgba(184, 116, 31, .45);
}

/* mode pill labels */
.reading-mode-track-labels {
	display: flex;
	justify-content: right;
	gap: 4px;
}

.reading-mode-track-labels span {
	flex: 1;
	text-align: center;
	font-size: 12px;
	font-weight: 500;
	color: var(--text-muted, #8a7a64);
	padding: 5px 4px;
	border-radius: 8px;
	cursor: pointer;
	transition: color .15s, background .15s, font-weight .15s;
}

.reading-mode-track-labels span:hover {
	background: rgba(184, 116, 31, .06);
	color: var(--accent, #b8741f);
}

.reading-mode-track-labels span.active {
	color: var(--accent, #b8741f);
	font-weight: 700;
	background: rgba(184, 116, 31, .1);
}

/* preview icons above labels */
.reading-mode-track-labels span::before {
	display: block;
	font-size: 15px;
	margin-bottom: 2px;
}

.reading-mode-track-labels span[data-mode="0"]::before {
	content: "▪";
}

.reading-mode-track-labels span[data-mode="1"]::before {
	content: "◼";
}

.reading-mode-track-labels span[data-mode="2"]::before {
	content: "⬛";
}

/* ── mobile: bottom sheet ── */
@media ( max-width : 600px) {
	.reading-mode-overlay.is-open {
		background: rgba(20, 14, 6, .4);
	}
	.reading-mode-popover {
		top: auto;
		left: 0;
		right: 0;
		bottom: 0;
		width: 100%;
		max-width: none;
		border-radius: 22px 22px 0 0;
		transform: translateY(105%);
		transform-origin: bottom center;
		padding: 14px 22px calc(22px + env(safe-area-inset-bottom, 0px));
	}
	.reading-mode-popover.is-open {
		transform: translateY(0) scale(1);
	}
	.reading-mode-handle {
		display: block;
		width: 36px;
		height: 4px;
		border-radius: 2px;
		background: var(--border, #e3d8c8);
		margin: 0 auto 14px;
	}
}
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

				<button class="btn" id="displayBtn" type="button"
					aria-haspopup="dialog" aria-expanded="false"
					onclick="toggleDisplayPanel()">
					<!-- desktop: icon + text -->

					<!-- mobile FAB: icon only (shown via CSS) -->
					<span class="btn-icon-only" aria-hidden="true"> <i
						class="ti ti-adjustments"></i>
					</span> <span class="btn-label">Adjust Display</span>
				</button>

				<button class="btn" onclick="history.back()">Back</button>

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
   TASK 5: localStorage key preserved
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

  /* ── restore saved preference (Task 5) ── */
  var saved = null;
  try { saved = localStorage.getItem(STORAGE_KEY); } catch (e) {}
  var savedIndex = MODES.indexOf(saved);
  applyMode(savedIndex >= 0 ? savedIndex : 1, false);  // default: Comfortable
})();
</script>

</body>
</html>
