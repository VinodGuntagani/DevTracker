<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="model.*"%>
<%@ page import="java.util.*"%>

<%
Roadmap roadmap = (Roadmap) request.getAttribute("roadmap");

int totalSubjects = roadmap.getSubjects().size();
int totalTopics = 0;
int totalSubtopics = 0;
int totalMinutes = 0;

for (Subject subject : roadmap.getSubjects()) {
	totalTopics += subject.getTopics().size();
	for (Topic topic : subject.getTopics()) {
		totalSubtopics += topic.getSubtopics().size();
		for (SubTopic sub : topic.getSubtopics()) {
	totalMinutes += sub.getEstimatedMinutes();
		}
	}
}
%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title><%=roadmap.getTitle()%> — Syllabus | DevTracker</title>
<link rel="stylesheet" href="css/main.css">
<link rel="stylesheet"
	href="https://cdn.jsdelivr.net/npm/@tabler/icons-webfont@latest/tabler-icons.min.css">

</head>
<body>

	<!-- Mobile topbar -->
	<div class="mobile-topbar">
		<a class="mobile-logo" href="dashboard.jsp"> <i
			class="ti ti-route"></i> DevTracker
		</a>
		<button class="hamburger" onclick="openSidebar()"
			aria-label="Open menu">
			<i class="ti ti-menu-2"></i>
		</button>
	</div>

	<div class="overlay" id="overlay" onclick="closeSidebar()"></div>

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
					href="ai-roadmap.jsp"><i class="ti ti-robot"></i> AI Tools</a> <a
					class="nav-item active" href="viewAIRoadmaps"><i
					class="ti ti-layout-list"></i> AI Roadmaps</a>
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
							class="hero-stat-label">Subtopics</span>
					</div>
					<div class="hero-stat">
						<span class="hero-stat-val"><%=totalMinutes / 60%></span> <span
							class="hero-stat-label">Est. Hours</span>
					</div>
				</div>
			</div>

			<!-- Toolbar -->
			<div class="syllabus-toolbar">
				<span class="syllabus-toolbar-label">Syllabus</span>
				<div class="toolbar-actions">
					<button class="btn btn-sm" onclick="expandAll()">
						<i class="ti ti-chevrons-down"></i> Expand all
					</button>
					<button class="btn btn-sm" onclick="collapseAll()">
						<i class="ti ti-chevrons-up"></i> Collapse all
					</button>
					<button class="btn btn-sm" id="customizeBtn"
						onclick="toggleCustomize()">
						<i class="ti ti-pencil"></i> Customize
					</button>
					<a class="btn btn-sm" href="dashboard.jsp"><i
						class="ti ti-arrow-left"></i> Back</a>
				</div>
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
			%>

			<div class="subject-block" id="<%=sBlockId%>">

				<%-- Subject header row --%>
				<div class="subject-header">

					<%-- Index badge — clicking it toggles accordion --%>
					<div class="subject-index" style="cursor: pointer"
						onclick="toggleSubject('<%=sBlockId%>')"><%=si%></div>

					<%-- Name (read mode) --%>
					<div class="subject-header-left"
						onclick="toggleSubject('<%=sBlockId%>')" style="cursor: pointer">
						<span class="subject-name" id="<%=sNameId%>"><%=subject.getName()%></span>
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
						<span class="count-pill" id="<%=sCountId%>"><%=tCount%>
							topics · <%=stCount%> subtopics</span>
						<button class="inline-edit-btn" title="Edit subject"
							onclick="enableSubjectEdit('<%=si%>')">
							<i class="ti ti-pencil"></i>
						</button>
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
					%>

					<div class="topic-section" id="<%=tSectionId%>">

						<%-- Topic header row --%>
						<div class="topic-header">
							<div class="topic-dot" onclick="toggleTopic('<%=tSectionId%>')"
								style="cursor: pointer; width: 10px; height: 10px; flex-shrink: 0"></div>

							<%-- Topic name (read mode) --%>
							<span class="topic-name" id="<%=tNameId%>"
								onclick="toggleTopic('<%=tSectionId%>')" style="cursor: pointer"><%=topic.getName()%></span>

							<%-- Topic inline edit form --%>
							<form id="<%=tEditFormId%>" class="topic-edit-form"
								action="editAITopic" method="post">
								<input type="hidden" name="topicId" value="<%=topic.getId()%>">
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
							<span class="count-pill" style="font-size: 11px"><%=topic.getSubtopics().size()%>
								subtopics</span>
							<button class="inline-edit-btn" title="Edit topic"
								onclick="enableTopicEdit('<%=si%>','<%=ti%>')">
								<i class="ti ti-pencil"></i>
							</button>
							<i class="ti ti-chevron-down topic-chevron"
								onclick="toggleTopic('<%=tSectionId%>')"></i>
						</div>

						<%-- Subtopics --%>
						<div class="subtopic-table">
							<%
							for (SubTopic sub : topic.getSubtopics()) {
								String diff = sub.getDifficulty() != null ? sub.getDifficulty().toLowerCase().trim() : "medium";
								String diffClass = diff.equals("easy") ? "diff-easy" : diff.equals("hard") ? "diff-hard" : "diff-medium";
								String stRowId = "str-" + sub.getId();
								String stEditId = "ste-" + sub.getId();
							%>

							<%-- Read row --%>
							<div class="subtopic-row" id="<%=stRowId%>">
								<span class="subtopic-name-text"><%=sub.getName()%></span>
								<div class="subtopic-right">
									<span class="diff-badge <%=diffClass%>"><%=sub.getDifficulty()%></span>
									<span class="hours-pill"> <i class="ti ti-clock"
										style="font-size: 11px"></i> <%=sub.getEstimatedMinutes() / 60%>h
										<%=sub.getEstimatedMinutes() % 60%>m
									</span>
									<button class="inline-edit-btn" title="Edit subtopic"
										onclick="enableSubtopicEdit(<%=sub.getId()%>)">
										<i class="ti ti-pencil"></i>
									</button>

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
							<i class="ti ti-plus"></i><span>Add Subtopic</span>


						</div>


						<form id="addSub-<%=topic.getId()%>" class="subtopic-edit-row"
							style="display: none; background: #f0f7ff;" action="addSubTopic"
							method="post">
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
					style="display: none; background: #f0f7ff;" action="addTopic"
					method="post">
					<input type="hidden" name="subjectId" value="<%=subject.getId()%>">
					<input type="hidden" name="roadmapId" value="<%=roadmap.getId()%>">
					<i class="ti ti-plus"
						style="color: var(--accent-primary); font-size: 14px; flex-shrink: 0"></i>
					<input type="text" name="name" placeholder="Add a topic…" required
						style="flex: 1; padding: 5px 10px; font-size: 13px; border-radius: 7px; border: 0.5px solid var(--border-normal); font-family: var(--font-body); outline: none; background: var(--bg-surface); color: var(--text-primary);">
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
	</main>
	</div>
	<%-- /layout --%>

	<script>
/* ── Sidebar ── */
function openSidebar() {
    document.getElementById('sidebar').classList.add('open');
    document.getElementById('overlay').classList.add('open');
    document.body.style.overflow = 'hidden';
}
function closeSidebar() {
    document.getElementById('sidebar').classList.remove('open');
    document.getElementById('overlay').classList.remove('open');
    document.body.style.overflow = '';
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
var first = document.querySelector('.subject-block');
if (first) first.classList.add('open');
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

	if(customizeMode){

		btn.innerHTML =
		'<i class="ti ti-check"></i> Done';

	}else{

		btn.innerHTML =
		'<i class="ti ti-pencil"></i> Customize';

	}

}
</script>

</body>
</html>
