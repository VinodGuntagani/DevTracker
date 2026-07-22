<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="model.*"%>
<%@ page import="java.util.*"%>
<%@ page import="model.User"%>
<%
User user = (User) session.getAttribute("user");
%>

<%
Roadmap roadmap = (Roadmap) request.getAttribute("roadmap");

int totalSubjects = roadmap.getSubjects().size();
int totalTopics = 0;
int totalSubtopics = 0;
int totalMinutes = 0;
int totalCompleted = 0;

for (Subject subject : roadmap.getSubjects()) {
	totalTopics += subject.getTopics().size();
	for (Topic topic : subject.getTopics()) {
		totalSubtopics += topic.getSubtopics().size();
		for (SubTopic sub : topic.getSubtopics()) {
	totalMinutes += sub.getEstimatedMinutes();
	if (sub.isCompleted())
		totalCompleted++;
		}
	}
}
int overallPct = totalSubtopics > 0 ? Math.round(100f * totalCompleted / totalSubtopics) : 0;
%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title><%=roadmap.getTitle()%> — Syllabus | DevTracker</title>
<link rel="stylesheet" href="css/main.css">
<link rel="stylesheet" href="css/syllabus.css">
<link rel="stylesheet" href="css/loading.css">
<link rel="stylesheet" href="css/animation.css">
<link rel="stylesheet" href="css/auth.css">


<link rel="stylesheet"
	href="https://cdn.jsdelivr.net/npm/@tabler/icons-webfont@latest/tabler-icons.min.css">

</head>
<body>

	<!-- Mobile topbar -->
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
				<a class="nav-item" href="dashboard.jsp"><i
					class="ti ti-layout-dashboard"></i> Dashboard</a> <a class="nav-item"
					href="create-roadmap.jsp"><i class="ti ti-plus"></i> Create
					Roadmap</a> <a class="nav-item active" href=""><i
					class="ti ti-books"></i> Syllabus</a> <a class="nav-item"
					href="schedule.jsp?roadmapId=<%=roadmap.getId()%>"> <i
					class="ti ti-calendar-week"></i> Daily Tasks
				</a>
				<div class="nav-section-label">Manage</div>
				<a class="nav-item " href="trash.jsp"><i class="ti ti-recycle"></i>
					Recover Deleted</a>
			</nav>
			<div class="sidebar-footer">
				<div class="user-row">
					<div class="avatar">
						<i class="ti ti-user" style="font-size: 15px"></i>
					</div>
					<div class="user-info">
						<div class="user-name"><%=user.getName()%></div>
						<div class="user-plan">Free plan</div>
					</div>
					<a href="logout" class="logout-btn" title="Logout"><i
						class="ti ti-logout"></i></a>
				</div>
			</div>
		</aside>

		<!-- Main -->
		<main class="main">

			<!-- Hero -->
			<div class="syllabus-hero">
				<div class="hero-breadcrumb">
					<a href="dashboard.jsp">Dashboard</a> <i
						class="ti ti-chevron-right"></i> <a href="viewAIRoadmaps">AI
						Roadmaps</a> <i class="ti ti-chevron-right"></i> <span><%=roadmap.getTitle()%></span>
				</div>
				<div class="hero-ai-pill">
					<i class="ti ti-sparkles"></i> AI Generated
				</div>
				<h1 class="hero-title"><%=roadmap.getTitle()%></h1>
				<p class="hero-desc"><%=roadmap.getDescription()%></p>

				<div class="hero-overall">
					<div class="hero-overall-track">
						<div id="heroProgressFill" class="hero-overall-fill"
							style="width:<%=overallPct%>%"></div>
					</div>
					<span id="heroProgressText" class="hero-overall-text"> <%=overallPct%>%
						complete

					</span>
				</div>

				<button type="button" class="hero-stats-toggle"
					onclick="toggleHeroStats(this)">
					<span>View roadmap stats</span> <i class="ti ti-chevron-down"></i>
				</button>

				<div class="hero-stats">
					<div class="hero-stat">
						<span class="hero-stat-val"><%=totalSubjects%></span> <span
							class="hero-stat-label">Subjects</span>
					</div>
					<div class="hero-stat">
						<span class="hero-stat-val"><%=totalTopics%></span> <span
							class="hero-stat-label">Topics</span>
					</div>
					<div class="hero-stat">
						<span class="hero-stat-val"><%=totalSubtopics%></span> <span
							class="hero-stat-label">Lessons</span>
					</div>
					<div class="hero-stat">
						<span class="hero-stat-val"><%=totalMinutes / 60%></span> <span
							class="hero-stat-label">Est. Hours</span>
					</div>
					<div class="hero-stat">

						<span id="heroPercentVal" class="hero-stat-val"> <%=overallPct%>%

						</span> <span class="hero-stat-label"> Complete </span>

					</div>
				</div>
			</div>
			<div class="learning-hint" id="learningHint">

				<button type="button" class="hint-toggle"
					onclick="toggleLearningHint()">
					<div class="hint-icon">
						<i class="ti ti-bulb"></i>
					</div>
					<strong>How DevTracker works</strong> <i
						class="ti ti-chevron-down hint-chevron"></i>
				</button>

				<div class="hint-body">
					<div class="hint-body-inner">

						<p>Your roadmap is organized like:</p>

						<div class="hint-tree">

							<div>📚 Subject</div>
							<div class="indent">└ 📖 Topic</div>
							<div class="indent2">└ 🎯 Lesson</div>

						</div>


						<p>Lessons are where you learn, track progress, take notes and
							follow schedules.</p>


						<p>
							Creating your own roadmap? Use <a href="#customizeBtn"
								class="hint-link"> Customize Mode </a> to add Subjects, Topics,
							and Lessons.
						</p>

					</div>
				</div>

			</div>
			<!-- Toolbar -->
			<div class="toolbar-actions">
				<button class="btn btn-sm"
					onclick="location.href='schedule.jsp?roadmapId=<%=roadmap.getId()%>'">
					<i class="ti ti-calendar-week"></i> Daily Tasks
				</button>

				<button class="btn btn-sm" id="generateLearningBtn"
					onclick="generateAILearning()">
					<i class="ti ti-sparkles"></i> Generate AI Learning
				</button>

				<button class="btn btn-sm" id="customizeBtn"
					onclick="toggleCustomize()">
					<i class="ti ti-pencil"></i> Customize
				</button>
				<a class="btn btn-sm" href="dashboard.jsp"><i
					class="ti ti-arrow-left"></i> Back</a>
			</div>

			<%-- ══════════════════════════════════════════
             SYLLABUS TREE
             ══════════════════════════════════════════ --%>
			<%
			int si = 0;
			for (Subject subject : roadmap.getSubjects()) {
				si++;
				int tCount = subject.getTopics().size();
				int stCount = 0;
				for (Topic t : subject.getTopics())
					stCount += t.getSubtopics().size();
				String sBlockId = "sb-" + si;
				String sNameId = "sName-" + si;
				String sEditFormId = "sEF-" + si;
				String sCountId = "sCnt-" + si;
				String sBarClass = subject.getProgress() >= 100 ? "high" : subject.getProgress() >= 40 ? "medium" : "low";
			%>

			<div class="subject-block" id="<%=sBlockId%>">

				<%-- Subject header row --%>
				<div class="subject-header">

					<%-- Index badge — clicking it toggles accordion --%>
					<div class="subject-index" style="cursor: pointer"
						onclick="toggleSubject('<%=sBlockId%>')"><%=si%></div>

					<%-- Name (read mode) --%>
					<div class="subject-header-left" style="cursor: pointer">
						<button class="inline-edit-btn" title="Edit subject"
							onclick="enableSubjectEdit('<%=si%>')">

							<i class="ti ti-pencil"></i>

						</button>
						<span class="subject-name" id="<%=sNameId%>"
							onclick="toggleSubject('<%=sBlockId%>')"> <%=subject.getName()%>

						</span>




					</div>

					<%-- Inline edit form (hidden by default) --%>
					<form id="<%=sEditFormId%>" class="subject-edit-form"
						action="editAISubject" method="post">
						<input type="hidden" name="subjectId" value="<%=subject.getId()%>">
						<input type="hidden" name="roadmapId" value="<%=roadmap.getId()%>">
						<input type="text" name="name" value="<%=subject.getName()%>"
							placeholder="Subject name">
						<div class="subject-edit-form-btns">
							<button class="btn btn-primary btn-xs" type="submit">
								<i class="ti ti-check"></i> Save
							</button>
							<button type="button" class="btn btn-xs"
								onclick="cancelSubjectEdit('<%=si%>')">Cancel</button>
						</div>
					</form>
					<form action="deleteSubject" method="post" id="sdel-<%=si%>"
						class="subject-delete-form" style="display: none"
						onsubmit="return confirm('Delete this subject and everything inside?')">

						<input type="hidden" name="id" value="<%=subject.getId()%>">

						<input type="hidden" name="roadmapId" value="<%=roadmap.getId()%>">

						<button class="row-delete-btn" type="submit">
							<i class="ti ti-trash"></i>
						</button>

					</form>
					<div class="subject-meta">
						<div class="subject-progress-box">

							<span class="count-pill" id="<%=sCountId%>"> <%=tCount%>
								topics · <%=stCount%> Lessons
							</span> <span id="subjectProgress-<%=subject.getId()%>"
								class="progress-text"> <%=subject.getProgress()%>%

							</span>
							<div class="mini-progress">
								<div id="subjectBar-<%=subject.getId()%>"
									class="progress-fill <%=sBarClass%>"
									style="width:<%=subject.getProgress()%>%"></div>
							</div>

						</div>
						<i class="ti ti-chevron-down chevron-icon"
							onclick="toggleSubject('<%=sBlockId%>')"></i>
					</div>
				</div>

				<%-- Subject body (topics) --%>
				<div class="subject-body">


					<%
					int ti = 0;
					for (Topic topic : subject.getTopics()) {
						ti++;
						String tSectionId = "ts-" + si + "-" + ti;
						String tNameId = "tName-" + si + "-" + ti;
						String tEditFormId = "tEF-" + si + "-" + ti;
						int topicDoneCount = 0;
						for (SubTopic ts : topic.getSubtopics())
							if (ts.isCompleted())
						topicDoneCount++;
						String tBarClass = topic.getProgress() >= 100 ? "high" : topic.getProgress() >= 40 ? "medium" : "low";
					%>

					<div class="topic-section" id="<%=tSectionId%>">

						<%-- Topic header row --%>
						<div class="topic-header">

							<div class="topic-dot" onclick="toggleTopic('<%=tSectionId%>')"
								style="cursor: pointer; width: 8px; height: 8px; flex-shrink: 0"></div>

							<button class="inline-edit-btn" title="Edit topic"
								onclick="enableTopicEdit('<%=si%>','<%=ti%>')">

								<i class="ti ti-pencil"></i>

							</button>

							<%-- Topic name (read mode) --%>
							<span class="topic-name" id="<%=tNameId%>"
								onclick="toggleTopic('<%=tSectionId%>')" style="cursor: pointer"><%=topic.getName()%></span>


							<%-- Topic inline edit form --%>
							<form id="<%=tEditFormId%>" class="topic-edit-form"
								action="editTopic" method="post">
								<input type="hidden" name="id" value="<%=topic.getId()%>">
								<input type="hidden" name="subjectId"
									value="<%=subject.getId()%>"> <input type="hidden"
									name="roadmapId" value="<%=roadmap.getId()%>"> <input
									type="text" name="name" value="<%=topic.getName()%>"
									placeholder="Topic name">
								<div class="topic-edit-form-btns">
									<button class="btn btn-primary btn-xs" type="submit">
										<i class="ti ti-check"></i> Save
									</button>
									<button type="button" class="btn btn-xs"
										onclick="cancelTopicEdit('<%=si%>','<%=ti%>')">Cancel</button>
								</div>
							</form>
							<form action="deleteTopic" method="post"
								id="tdel-<%=si%>-<%=ti%>" class="topic-delete-form"
								style="display: none"
								onsubmit="return confirm('Delete this topic and its subtopics?')">

								<input type="hidden" name="id" value="<%=topic.getId()%>">

								<input type="hidden" name="roadmapId"
									value="<%=roadmap.getId()%>">

								<button class="row-delete-btn" type="submit">
									<i class="ti ti-trash"></i>
								</button>

							</form>
							<div class="topic-progress-box">

								<span class="count-pill" id="topicCnt-<%=topic.getId()%>">
									<%=topicDoneCount%>/<%=topic.getSubtopics().size()%> lessons
									completed
								</span>
								<div class="mini-progress">
									<div id="topicBar-<%=topic.getId()%>"
										class="progress-fill <%=tBarClass%>"
										style="width:<%=topic.getProgress()%>%"></div>
								</div>
								<span id="topicProgress-<%=topic.getId()%>"> <%=topic.getProgress()%>%
								</span>

							</div>
							<i class="ti ti-chevron-down topic-chevron"
								onclick="toggleTopic('<%=tSectionId%>')"></i>
						</div>

						<%-- Subtopics --%>
						<div class="subtopic-table" id="subtable-<%=topic.getId()%>">
							<%
							for (SubTopic sub : topic.getSubtopics()) {
								String diff = sub.getDifficulty() != null ? sub.getDifficulty().toLowerCase().trim() : "medium";
								String diffClass = diff.equals("easy") ? "diff-easy" : diff.equals("hard") ? "diff-hard" : "diff-medium";
								String stRowId = "str-" + sub.getId();
								String stEditId = "ste-" + sub.getId();
							%>

							<%-- Read row --%>
							<%
							boolean isFirstLessonEver = (si == 1 && ti == 1 && sub == topic.getSubtopics().get(0));
							%>
							<div
								class="subtopic-row<%=isFirstLessonEver ? " tutorial-target" : ""%>"
								id="<%=stRowId%>">
								<button class="inline-edit-btn" title="Edit subtopic"
									onclick="enableSubtopicEdit(<%=sub.getId()%>)">
									<i class="ti ti-pencil"></i>
								</button>
								<div class="lesson-title">


									<!-- completion button -->
									<button class="lesson-check-btn"
										title="<%=sub.isCompleted() ? "Mark as not complete" : "Mark as complete"%>"
										aria-label="<%=sub.isCompleted() ? "Mark as not complete" : "Mark as complete"%>"
										onclick="toggleLesson(
											<%=sub.getId()%>,
											<%=topic.getId()%>,
											<%=subject.getId()%>,
											<%=!sub.isCompleted()%>,
											this
											)">

										<%
										if (sub.isCompleted()) {
										%>

										<i class="ti ti-circle-check-filled"></i>

										<%
										} else {
										%>

										<i class="ti ti-circle"></i>

										<%
										}
										%>

									</button>





									<!-- open learning -->
									<!-- open learning -->
									<a class="lesson-link"
										href="generateLearning?subtopicId=<%=sub.getId()%>&roadmapId=<%=roadmap.getId()%>">
										<%=sub.getName()%>
									</a> <a class="task-learn-btn"
										href="generateLearning?subtopicId=<%=sub.getId()%>&roadmapId=<%=roadmap.getId()%>">
										<i class="ti ti-book-2"></i> <span>Learn</span>
									</a>
									<button class="inline-edit-btn" title="Edit lesson"
										onclick="enableSubtopicEdit(<%=sub.getId()%>)">

										<i class="ti ti-pencil"></i>

									</button>

								</div>
								<div class="subtopic-right">

									<span class="hours-pill <%=diffClass%>"> <i
										class="ti ti-clock" style="font-size: 11px"></i> <%=sub.getEstimatedMinutes() / 60%>h
										<%=sub.getEstimatedMinutes() % 60%>m
									</span>


								</div>
							</div>

							<%-- Edit row (hidden) --%>
							<form id="<%=stEditId%>" class="subtopic-edit-row"
								action="editSubTopic" method="post">
								<input type="hidden" name="subtopicId" value="<%=sub.getId()%>">
								<input type="hidden" name="topicId" value="<%=topic.getId()%>">
								<input type="hidden" name="roadmapId"
									value="<%=roadmap.getId()%>"> <input type="text"
									name="name" value="<%=sub.getName()%>"
									placeholder="Subtopic name"> <select name="difficulty">
									<option
										<%="easy".equalsIgnoreCase(sub.getDifficulty()) ? "selected" : ""%>>Easy</option>
									<option
										<%="medium".equalsIgnoreCase(sub.getDifficulty()) ? "selected" : ""%>>Medium</option>
									<option
										<%="hard".equalsIgnoreCase(sub.getDifficulty()) ? "selected" : ""%>>Hard</option>
								</select><input type="number" name="minutes" min="1" step="1"
									value="<%=sub.getEstimatedMinutes()%>">
								<div class="subtopic-edit-btns">
									<button class="btn btn-primary btn-xs" type="submit">
										<i class="ti ti-check"></i> Save
									</button>
									<button type="button" class="btn btn-xs"
										onclick="cancelSubtopicEdit(<%=sub.getId()%>)">Cancel</button>
							</form>
							<form action="deleteSubTopic" method="post"
								class="subtopic-delete-form" style="display: none"
								id="stdel-<%=sub.getId()%>"
								onsubmit="return confirm('Delete this subtopic?')">

								<input type="hidden" name="subtopicId" value="<%=sub.getId()%>">

								<input type="hidden" name="roadmapId"
									value="<%=roadmap.getId()%>">

								<button class="row-delete-btn">
									<i class="ti ti-trash"></i>
								</button>


							</form>
						</div>

						<%
						}
						%>

						<%-- Add subtopic row --%>
						<div id="addSubBtn-<%=topic.getId()%>" class="add-plus"
							onclick="showAddSub(<%=topic.getId()%>)">
							<i class="ti ti-plus"></i><span>Add Lesson</span>


						</div>


						<form id="addSub-<%=topic.getId()%>" class="subtopic-edit-row"
							style="display: none; background: var(--surface-2);"
							action="addSubTopic" method="post">
							<input type="hidden" name="topicId" value="<%=topic.getId()%>">
							<input type="hidden" name="subjectId"
								value="<%=subject.getId()%>"> <input type="hidden"
								name="roadmapId" value="<%=roadmap.getId()%>"> <input
								type="text" name="name" placeholder="New subtopic name…"
								required> <select name="difficulty">
								<option>Easy</option>
								<option selected>Medium</option>
								<option>Hard</option>
							</select> <input type="number" name="minutes" min="15" step="1" value="60">
							<div class="subtopic-edit-btns">

								<button class="btn btn-xs btn-primary" type="submit">
									<i class="ti ti-plus"></i> Add
								</button>

								<button type="button" class="btn btn-xs"
									onclick="hideAddSub(<%=topic.getId()%>)">Cancel</button>

							</div>
						</form>

					</div>
					<%-- /subtopic-table --%>

				</div>
				<%-- /topic-section --%>

				<%
				}
				%>

				<%-- Add topic row --%>
				<div id="addTopicBtn-<%=subject.getId()%>" class="add-plus"
					onclick="showAddTopic(<%=subject.getId()%>)">

					<i class="ti ti-plus"></i><span>Add Topic</span>

				</div>


				<form id="addTopic-<%=subject.getId()%>" class="topic-header"
					style="display: none; background: var(--surface-2);"
					action="addTopic" method="post">
					<input type="hidden" name="subjectId" value="<%=subject.getId()%>">
					<input type="hidden" name="roadmapId" value="<%=roadmap.getId()%>">
					<i class="ti ti-plus"
						style="color: var(--accent); font-size: 14px; flex-shrink: 0"></i>
					<input type="text" name="name" placeholder="Add a topic…" required
						style="flex: 1; padding: 7px 11px; font-size: 13px; border-radius: 8px; border: 1px solid var(--border); font-family: var(--font-display); outline: none; background: var(--surface); color: var(--text);">
					<button class="btn btn-xs btn-primary" type="submit">
						<i class="ti ti-plus"></i> Add topic
					</button>
					<button type="button" class="btn btn-xs"
						onclick="hideAddTopic(<%=subject.getId()%>)">Cancel</button>
				</form>

			</div>
			<%-- /subject-body --%>
	</div>
	<%-- /subject-block --%>

	<%
	}
	%>

	<%-- Add subject panel --%>
	<div class="add-panel">
		<div id="addSubjectBtn" class="add-plus" onclick="showAddSubject()">

			<i class="ti ti-plus"></i><span>Add Subject</span>
		</div>



		<div id="addSubject" class="add-panel1" style="display: none">
			<div class="add-panel-title">Add new subject</div>
			<form action="addSubject" method="post" class="add-form">
				<input type="hidden" name="roadmapId" value="<%=roadmap.getId()%>">
				<input type="text" name="name" placeholder="Subject name…" required>
				<button class="btn btn-primary" type="submit">
					<i class="ti ti-plus"></i> Add subject
				</button>
				<button type="button" class="btn" onclick="hideAddSubject()">
					Cancel</button>
			</form>
		</div>
	</div>
	<div class="customize-hint">

		💡 Want to change this roadmap? Use <a href="#customizeBtn">
			Customize Mode </a> to add, edit, or remove subjects, topics and lessons.

	</div>
	<button class="fab-expand" id="expandToggleBtn"
		onclick="toggleExpandAll()">
		<i class="ti ti-chevrons-up"></i> <span>Collapse all</span>
	</button>
	</main>
	</div>
	<%-- /layout --%>

	<!-- Bottom tab bar (mobile only — sidebar nav mirrored here) -->
	<nav class="bottom-nav">
		<a class="bn-item" href="dashboard.jsp"><i
			class="ti ti-layout-dashboard"></i>Dashboard</a> <a class="bn-item"
			href="create-roadmap.jsp"><i class="ti ti-plus"></i>New</a> <a
			class="bn-item active" href=""><i class="ti ti-books"></i>
			Syllabus</a> <a class="bn-item"
			href="schedule.jsp?roadmapId=<%=roadmap.getId()%>"> <i
			class="ti ti-calendar-week"></i> Schedule
		</a>
	</nav>

	<script>
/* ── Progress bar color tier (mirrors the JSP thresholds: >=100 high, >=40 medium, else low) ── */
function progressClass(el, pct) {
    if (!el) return;
    el.classList.remove('high', 'medium', 'low');
    el.classList.add(pct >= 100 ? 'high' : pct >= 40 ? 'medium' : 'low');
}

/* ── "How DevTracker works" dropdown ── */
function toggleLearningHint() {
    document.getElementById('learningHint').classList.toggle('open');
}

/* ── Mobile hero stats reveal ── */
function toggleHeroStats(btn) {
    var hero = btn.closest('.syllabus-hero');
    hero.classList.toggle('stats-open');
}

/* ── Accordion ── */
function toggleSubject(id) { document.getElementById(id).classList.toggle('open'); }
function toggleTopic(id)   { document.getElementById(id).classList.toggle('open'); }
function expandAll() {
    document.querySelectorAll('.subject-block').forEach(el => el.classList.add('open'));
    document.querySelectorAll('.topic-section').forEach(el => el.classList.add('open'));
}
function collapseAll() {
    document.querySelectorAll('.subject-block').forEach(el => el.classList.remove('open'));
    document.querySelectorAll('.topic-section').forEach(el => el.classList.remove('open'));
}

let allExpanded = true;
function toggleExpandAll() {
    const btn = document.getElementById('expandToggleBtn');
    if (allExpanded) {
        collapseAll();
        btn.querySelector('span').textContent = 'Expand all';
        btn.classList.add('collapsed');
    } else {
        expandAll();
        btn.querySelector('span').textContent = 'Collapse all';
        btn.classList.remove('collapsed');
    }
    allExpanded = !allExpanded;
}

/* ── Subject inline edit ── */
function enableSubjectEdit(si) {
    document.getElementById('sName-' + si).style.display   = 'none';
    document.getElementById('sCnt-'  + si).style.display   = 'none';
    document.getElementById('sEF-'   + si).style.display   = 'flex';
    // Stop the header from toggling accordion while editing
    document.getElementById('sEF-' + si)
        .closest('.subject-header')
        .querySelector('[onclick*="toggleSubject"]') && null;
    document.getElementById('sdel-' + si).style.display = 'flex';
}
function cancelSubjectEdit(si) {
    document.getElementById('sName-' + si).style.display   = '';
    document.getElementById('sCnt-'  + si).style.display   = '';
    document.getElementById('sEF-'   + si).style.display   = 'none';
    document.getElementById('sdel-' + si).style.display = 'none';
}

/* ── Topic inline edit ── */
function enableTopicEdit(si, ti) {

    var key = si + '-' + ti;

    document.getElementById('tName-' + key).style.display = 'none';
    document.getElementById('tEF-' + key).style.display = 'flex';

    document.getElementById('tdel-' + key).style.display = 'flex';
}
function cancelTopicEdit(si, ti) {

    var key = si + '-' + ti;

    document.getElementById('tName-' + key).style.display = '';
    document.getElementById('tEF-' + key).style.display = 'none';

    document.getElementById('tdel-' + key).style.display = 'none';
}
/* ── Subtopic inline edit ── */
function enableSubtopicEdit(id) {

    document.getElementById('str-' + id).style.display = 'none';

    document.getElementById('ste-' + id).style.display = 'flex';

    document.getElementById('stdel-' + id).style.display = 'flex';
}
function cancelSubtopicEdit(id) {

    document.getElementById('str-' + id).style.display = '';

    document.getElementById('ste-' + id).style.display = 'none';

    document.getElementById('stdel-' + id).style.display = 'none';
}

/* Open first subject by default */
/* Expand everything by default */
expandAll();
function showAddSub(id){

	document.getElementById("addSubBtn-" + id)
	.style.display="none";

	document.getElementById("addSub-" + id)
	.style.display="flex";
}


function showAddTopic(id){

	document.getElementById("addTopicBtn-" + id)
	.style.display="none";

	document.getElementById("addTopic-" + id)
	.style.display="flex";
}


function showAddSubject(){

	document.getElementById("addSubjectBtn")
	.style.display="none";

	document.getElementById("addSubject")
	.style.display="block";
}
function hideAddSub(id){

	document.getElementById("addSub-" + id)
	.style.display="none";

	document.getElementById("addSubBtn-" + id)
	.style.display="flex";

}


function hideAddTopic(id){

	document.getElementById("addTopic-" + id)
	.style.display="none";

	document.getElementById("addTopicBtn-" + id)
	.style.display="flex";

}


function hideAddSubject(){

	document.getElementById("addSubject")
	.style.display="none";

	document.getElementById("addSubjectBtn")
	.style.display="flex";

}
let customizeMode = false;

function toggleCustomize(){

	customizeMode = !customizeMode;

	document.body.classList.toggle(
		"customize-mode",
		customizeMode
	);

	let btn = document.getElementById("customizeBtn");

	btn.classList.toggle("active", customizeMode);

	if(customizeMode){

		btn.innerHTML =
		'<i class="ti ti-check"></i> Done';

	}else{

		btn.innerHTML =
		'<i class="ti ti-pencil"></i> Customize';

	}

}
function updateHeroProgress(){

	let all =
	document.querySelectorAll(".lesson-check-btn i");

	let done =
	document.querySelectorAll(
	".lesson-check-btn i.ti-circle-check-filled"
	).length;


	let total = all.length;


	let percent = 0;


	if(total > 0){

		percent =
		Math.round(done * 100 / total);

	}


	document
	.getElementById("heroProgressFill")
	.style.width = percent + "%";


	document
	.getElementById("heroProgressText")
	.innerText = percent + "% complete";


	document
	.getElementById("heroPercentVal")
	.innerText = percent + "%";

}
function toggleLesson(
		id,
		topicId,
		subjectId,
		completed,
		btn
	){

		let oldIcon = btn.innerHTML;

		btn.disabled = true;

		btn.innerHTML =
		'<i class="ti ti-loader-2"></i>';


		fetch("updateSubTopic", {

			method:"POST",

			headers:{
				"Content-Type":
				"application/x-www-form-urlencoded"
			},

			body:
			"id=" + id +
			"&completed=" + completed

		})

		.then(r => r.json())

		.then(data => {


			if(completed){

				btn.innerHTML =
				'<i class="ti ti-circle-check-filled"></i>';

			}else{

				btn.innerHTML =
				'<i class="ti ti-circle"></i>';

			}


			let topicText =
			document.getElementById(
				"topicProgress-" + topicId
			);


			if(topicText){

				topicText.innerText =
				data.topicProgress + "%";

			}



			document
			.getElementById(
				"topicBar-" + topicId
			)
			.style.width =
			data.topicProgress + "%";

			progressClass(
				document.getElementById("topicBar-" + topicId),
				data.topicProgress
			);



			document
			.getElementById(
				"subjectProgress-" + subjectId
			)
			.innerText =
			data.subjectProgress + "%";



			document
			.getElementById(
				"subjectBar-" + subjectId
			)
			.style.width =
			data.subjectProgress + "%";

			progressClass(
				document.getElementById("subjectBar-" + subjectId),
				data.subjectProgress
			);
			updateHeroProgress();

			/* Keep the "X/Y lessons completed" pill in sync — derive the done
			   count straight from the DOM so it never depends on the server
			   response shape, then update both the topic and subject totals. */
			let topicTable = document.getElementById("subtable-" + topicId);
			if (topicTable) {
				let allIcons = topicTable.querySelectorAll(".lesson-check-btn i");
				let total = allIcons.length;
				let done = topicTable.querySelectorAll(".lesson-check-btn i.ti-circle-check-filled").length;

				let topicCnt = document.getElementById("topicCnt-" + topicId);
				if (topicCnt) {
					topicCnt.innerText = done + "/" + total + " lessons completed";
				}
			}


			btn.setAttribute(
				"onclick",
				"toggleLesson("
				+ id + ","
				+ topicId + ","
				+ subjectId + ","
				+ !completed +
				",this)"
			);


		})


		.catch(()=>{

			btn.innerHTML = oldIcon;

		})


		.finally(()=>{

			btn.disabled=false;

		});

	}
</script>
	<script src="js/loading.js"></script>
	<div id="loadingContainer"></div>

	<script>
window.addEventListener("DOMContentLoaded", async () => {

    const res = await fetch("includes/loading.html");
    const html = await res.text();

    document.getElementById("loadingContainer").innerHTML = html;

});
function generateAILearning() {
    const btn = document.getElementById("generateLearningBtn");
    const originalHTML = btn.innerHTML;

    btn.disabled = true;
    btn.innerHTML = "Generating...";

    fetch("generateLearningJob", {
        method: "POST",
        headers: {
            "Content-Type": "application/x-www-form-urlencoded"
        },
        body: "roadmapId=<%=roadmap.getId()%>"
    })
    .then(r => r.json())
    .then(data => {
        if (data.success) {
            console.log("Job Started", data.jobId);
        } else {
            alert("Failed to start AI generation.");
        }
    })
    .catch(() => {
        alert("Server error while starting AI generation.");
    })
    .finally(() => {
        btn.innerHTML = '<i class="ti ti-sparkles"></i> Generate AI Learning';
        btn.disabled = false;
    });
}
</script>
</body>
</html>
