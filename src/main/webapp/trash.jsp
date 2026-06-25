<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="dao.RoadmapDAO"%>
<%@ page import="dao.SubjectDAO"%>
<%@ page import="dao.TopicDAO"%>
<%@ page import="dao.SubTopicDAO"%>
<%@ page import="model.Roadmap"%>
<%@ page import="model.Subject"%>
<%@ page import="model.Topic"%>
<%@ page import="model.SubTopic"%>
<%@ page import="model.User"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.ArrayList"%>

<%
User user = (User) session.getAttribute("user");
if (user == null) {
	response.sendRedirect("login.html");
	return;
}

String activeTab = request.getParameter("tab");
if (activeTab == null || activeTab.isEmpty())
	activeTab = "roadmaps";

String roadmapParam = request.getParameter("roadmapId");
boolean hasRoadmapContext = false;
int roadmapId = 0;
if (roadmapParam != null) {
	hasRoadmapContext = true;
	roadmapId = Integer.parseInt(roadmapParam);
}

RoadmapDAO roadmapDAO = new RoadmapDAO();
SubjectDAO subjectDAO = new SubjectDAO();
TopicDAO topicDAO = new TopicDAO();
SubTopicDAO subTopicDAO = new SubTopicDAO();

List<Roadmap> deletedRoadmaps = roadmapDAO.getDeletedRoadmaps(user.getId());
List<Subject> deletedSubjects = subjectDAO.getDeletedSubjectsByUser(user.getId());
List<Topic> deletedTopics = topicDAO.getDeletedTopicsByUser(user.getId());
List<SubTopic> deletedSubTopics = subTopicDAO.getDeletedSubTopicsByUser(user.getId());
int totalDeleted = deletedRoadmaps.size() + deletedSubjects.size() + deletedTopics.size() + deletedSubTopics.size();
%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Deleted | DevTracker</title>
<link rel="stylesheet" href="css/main.css">
<link rel="stylesheet" href="css/syllabus.css">
<link rel="stylesheet"
	href="https://cdn.jsdelivr.net/npm/@tabler/icons-webfont@latest/tabler-icons.min.css">
<style>
/* ─── Tab bar ─────────────────────────────────────────── */
.tab-bar {
	display: flex;
	gap: 2px;
	background: var(--bg-overlay, #f0f0ee);
	border: 0.5px solid var(--border-subtle, rgba(0, 0, 0, 0.07));
	border-radius: 10px;
	padding: 3px;
	width: fit-content;
	flex-wrap: wrap;
	margin-bottom: 20px;
}

.tab-btn {
	display: inline-flex;
	align-items: center;
	gap: 6px;
	padding: 7px 14px;
	border-radius: 7px;
	border: none;
	background: transparent;
	font-size: 13px;
	font-weight: 500;
	color: #888;
	cursor: pointer;
	text-decoration: none;
	white-space: nowrap;
	transition: background 0.15s, color 0.15s;
}

.tab-btn:hover {
	background: rgba(255, 255, 255, 0.7);
	color: #1a1a1a;
	text-decoration: none;
}

.tab-btn.active {
	background: #fff;
	color: #1a1a1a;
	box-shadow: 0 1px 3px rgba(0, 0, 0, 0.08);
}

.tab-count {
	display: inline-flex;
	align-items: center;
	justify-content: center;
	min-width: 18px;
	height: 18px;
	padding: 0 5px;
	border-radius: 99px;
	font-size: 11px;
	font-weight: 600;
	background: #f0f0ee;
	color: #888;
}

.tab-btn.active .tab-count {
	background: #EEEDFE;
	color: #534AB7;
}

.tab-count.has-items {
	background: #fff0f0;
	color: #a32d2d;
}

.tab-btn.active .tab-count.has-items {
	background: #fff0f0;
	color: #a32d2d;
}

/* ─── Tab panels ──────────────────────────────────────── */
.tab-panel {
	display: none;
}

.tab-panel.active {
	display: block;
}

/* ─── Trash card ──────────────────────────────────────── */
.trash-card {
	background: var(--surface, #fff);
	border: 0.5px solid var(--border, #e8e8e8);
	border-radius: 14px;
	padding: 1rem 1.25rem;
	display: flex;
	align-items: flex-start;
	justify-content: space-between;
	gap: 1rem;
	transition: border-color 0.15s, box-shadow 0.15s;
}

.trash-card:hover {
	border-color: #c8c8c8;
	box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
}

.trash-card-left {
	flex: 1;
	min-width: 0;
}

.trash-card-type {
	font-size: 10px;
	font-weight: 700;
	text-transform: uppercase;
	letter-spacing: 0.08em;
	color: var(--text-muted, #bbb);
	margin-bottom: 6px;
}

.trash-hierarchy {
	display: flex;
	flex-direction: column;
	gap: 3px;
	margin-bottom: 8px;
}

.trash-hierarchy-row {
	display: flex;
	align-items: center;
	gap: 6px;
	font-size: 12px;
	color: var(--text-muted, #aaa);
	font-weight: 500;
}

.trash-hierarchy-row.current {
	color: var(--text, #1a1a1a);
	font-size: 14px;
	font-weight: 600;
}

.trash-hierarchy-row i {
	font-size: 13px;
	flex-shrink: 0;
}

.tree-indent-1 {
	padding-left: 18px;
}

.tree-indent-2 {
	padding-left: 36px;
}

.tree-indent-3 {
	padding-left: 54px;
}

.tree-connector {
	color: #ddd;
	font-size: 11px;
	margin-right: 2px;
	font-family: monospace;
	user-select: none;
}

.trash-card-meta {
	display: flex;
	flex-wrap: wrap;
	gap: 6px;
	align-items: center;
	margin-top: 6px;
}

.trash-card-desc {
	font-size: 12px;
	color: var(--text-muted, #aaa);
	margin-top: 4px;
	display: -webkit-box;
	-webkit-line-clamp: 2;
	-webkit-box-orient: vertical;
	overflow: hidden;
}

.trash-card-actions {
	display: flex;
	flex-direction: column;
	gap: 6px;
	flex-shrink: 0;
	align-items: flex-end;
}

.deleted-pill {
	display: inline-flex;
	align-items: center;
	gap: 4px;
	padding: 2px 8px;
	border-radius: 99px;
	font-size: 11px;
	background: #fff8f8;
	border: 0.5px solid #f5c1c1;
	color: #a32d2d;
}

/* ─── Empty state ─────────────────────────────────────── */
.trash-empty {
	display: flex;
	flex-direction: column;
	align-items: center;
	justify-content: center;
	gap: 10px;
	padding: 3rem 1rem;
	color: var(--text-muted, #bbb);
	text-align: center;
}

.trash-empty .empty-icon {
	width: 56px;
	height: 56px;
	border-radius: 14px;
	background: var(--surface-2, #f7f7f7);
	display: flex;
	align-items: center;
	justify-content: center;
	font-size: 26px;
	color: #ccc;
	border: 0.5px solid var(--border, #eee);
}

.trash-empty p {
	font-size: 13px;
	color: var(--text-muted, #bbb);
	margin: 0;
}

.trash-empty .empty-hint {
	font-size: 12px;
	color: #d0d0d0;
}

/* ─── Danger zone ─────────────────────────────────────── */
.danger-zone {
	background: #fff8f8;
	border: 0.5px solid #f5c1c1;
	border-radius: 14px;
	padding: 1.1rem 1.4rem;
	display: flex;
	align-items: center;
	justify-content: space-between;
	gap: 1rem;
	flex-wrap: wrap;
	margin-top: 24px;
}

.danger-zone-label {
	font-size: 10px;
	font-weight: 700;
	text-transform: uppercase;
	letter-spacing: 0.08em;
	color: #c0766a;
	margin-bottom: 3px;
}

.danger-zone-text {
	font-size: 13px;
	color: #7a2020;
}

.danger-zone-text strong {
	font-weight: 600;
}

/* ─── Section list wrapper ────────────────────────────── */
.trash-list {
	display: flex;
	flex-direction: column;
	gap: 10px;
}

/* ─── Responsive ──────────────────────────────────────── */
@media ( max-width : 640px) {
	.trash-card {
		flex-direction: column;
		gap: 0.75rem;
	}
	.trash-card-actions {
		flex-direction: row;
		width: 100%;
	}
	.tab-btn {
		padding: 6px 10px;
		font-size: 12px;
	}
}
</style>
</head>
<body>

	<!-- Mobile topbar -->
	<div class="mobile-topbar">
		<a class="mobile-logo" href="dashboard.jsp"><i class="ti ti-route"></i>
			DevTracker</a>
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
					href="ai-roadmap.jsp"><i class="ti ti-plus"></i> Create Roadmap</a> 
				<div class="nav-section-label">Manage</div>
				<a class="nav-item active" href="trash.jsp"><i
					class="ti ti-recycle"></i> Deleted</a>
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

			<!-- Hero — matches syllabus-hero pattern -->
			<div class="syllabus-hero">
				<div class="hero-breadcrumb">
					<a href="dashboard.jsp">Dashboard</a>
					<%
					if (hasRoadmapContext) {
					%>
					<i class="ti ti-chevron-right"></i> <a
						href="subjects.jsp?roadmapId=<%=roadmapId%>">Subjects</a>
					<%
					}
					%>
					<i class="ti ti-chevron-right"></i> <span>Deleted</span>
				</div>
				<h1 class="hero-title"><i class="ti ti-recycle"></i> Deleted</h1>
				<p class="hero-desc">
					<%
					if (totalDeleted == 0) {
					%>
					Nothing deleted — you're all clear
					<%
					} else {
					%>
					<%=totalDeleted%>
					deleted item<%=totalDeleted != 1 ? "s" : ""%>
					— restore or permanently delete
					<%
					}
					%>
				</p>

				<div class="hero-stats" style="display: flex">
					<div class="hero-stat">
						<span class="hero-stat-val"><%=deletedRoadmaps.size()%></span> <span
							class="hero-stat-label">Roadmaps</span>
					</div>
					<div class="hero-stat">
						<span class="hero-stat-val"><%=deletedSubjects.size()%></span> <span
							class="hero-stat-label">Subjects</span>
					</div>
					<div class="hero-stat">
						<span class="hero-stat-val"><%=deletedTopics.size()%></span> <span
							class="hero-stat-label">Topics</span>
					</div>
					<div class="hero-stat">
						<span class="hero-stat-val"><%=deletedSubTopics.size()%></span> <span
							class="hero-stat-label">Subtopics</span>
					</div>
				</div>
			</div>

			<!-- Toolbar -->
			<div class="syllabus-toolbar">
				<span class="syllabus-toolbar-label"></span>
				<div class="toolbar-actions">
					<%
					if (hasRoadmapContext) {
					%>
					<a class="btn btn-sm" href="subjects.jsp?roadmapId=<%=roadmapId%>"><i
						class="ti ti-arrow-left"></i> Back to subjects</a>
					<%
					} else {
					%>
					<a class="btn btn-sm" href="dashboard.jsp"><i
						class="ti ti-arrow-left"></i> Back</a>
					<%
					}
					%>
				</div>
			</div>

			<!-- Tab bar -->
			<div class="tab-bar">
				<a class="tab-btn <%="roadmaps".equals(activeTab) ? "active" : ""%>"
					href="?tab=roadmaps<%=hasRoadmapContext ? "&roadmapId=" + roadmapId : ""%>">
					<i class="ti ti-map"></i> Roadmaps <span
					class="tab-count <%=deletedRoadmaps.size() > 0 ? "has-items" : ""%>"><%=deletedRoadmaps.size()%></span>
				</a> <a class="tab-btn <%="subjects".equals(activeTab) ? "active" : ""%>"
					href="?tab=subjects<%=hasRoadmapContext ? "&roadmapId=" + roadmapId : ""%>">
					<i class="ti ti-books"></i> Subjects <span
					class="tab-count <%=deletedSubjects.size() > 0 ? "has-items" : ""%>"><%=deletedSubjects.size()%></span>
				</a> <a class="tab-btn <%="topics".equals(activeTab) ? "active" : ""%>"
					href="?tab=topics<%=hasRoadmapContext ? "&roadmapId=" + roadmapId : ""%>">
					<i class="ti ti-notebook"></i> Topics <span
					class="tab-count <%=deletedTopics.size() > 0 ? "has-items" : ""%>"><%=deletedTopics.size()%></span>
				</a> <a class="tab-btn <%="subtopics".equals(activeTab) ? "active" : ""%>"
					href="?tab=subtopics<%=hasRoadmapContext ? "&roadmapId=" + roadmapId : ""%>">
					<i class="ti ti-list-check"></i> Subtopics <span
					class="tab-count <%=deletedSubTopics.size() > 0 ? "has-items" : ""%>"><%=deletedSubTopics.size()%></span>
				</a>
			</div>

			<%-- ══ TAB: ROADMAPS ══ --%>
			<div class="tab-panel <%="roadmaps".equals(activeTab) ? "active" : ""%>">
				<%
				if (deletedRoadmaps.isEmpty()) {
				%>
				<div class="trash-empty">
					<div class="empty-icon">
						<i class="ti ti-map-off"></i>
					</div>
					<p>No deleted roadmaps</p>
					<span class="empty-hint">Deleted roadmaps will appear here</span>
				</div>
				<%
				} else {
				%>
				<div class="trash-list">
					<%
					for (Roadmap r : deletedRoadmaps) {
					%>
					<div class="trash-card">
						<div class="trash-card-left">
							<div class="trash-card-type">Roadmap</div>
							<div class="trash-hierarchy">
								<div class="trash-hierarchy-row current">
									<i class="ti ti-map" style="color: #7c6ef5"></i>
									<%=r.getTitle()%>
								</div>
							</div>
							<%
							if (r.getDescription() != null && !r.getDescription().isEmpty()) {
							%>
							<div class="trash-card-desc"><%=r.getDescription()%></div>
							<%
							}
							%>
							<div class="trash-card-meta">
								<%
								if (r.isAi()) {
								%>
								<span class="badge badge-ai"><i class="ti ti-robot"
									style="font-size: 10px"></i> AI</span>
								<%
								}
								%>
								<%
								if (r.getStartDate() != null) {
								%>
								<span class="deleted-pill"><i class="ti ti-calendar"
									style="font-size: 11px"></i> <%=r.getStartDate()%> – <%=r.getTargetDate()%></span>
								<%
								}
								%>
								<span class="deleted-pill"><i class="ti ti-trash"
									style="font-size: 11px"></i> Deleted</span>
							</div>
						</div>
						<div class="trash-card-actions">
							<form action="restoreRoadmap" method="post" style="margin: 0">
								<input type="hidden" name="id" value="<%=r.getId()%>">
								<button class="btn btn-sm btn-primary" type="submit">
									<i class="ti ti-restore"></i> Restore
								</button>
							</form>
							<form action="purgeRoadmap" method="post" style="margin: 0"
								onsubmit="return confirm('Permanently delete this roadmap? This cannot be undone.')">
								<input type="hidden" name="id" value="<%=r.getId()%>">
								<button class="btn btn-sm btn-danger" type="submit">
									<i class="ti ti-trash-x"></i> Delete
								</button>
							</form>
						</div>
					</div>
					<%
					}
					%>
				</div>
				<%
				}
				%>
			</div>

			<%-- ══ TAB: SUBJECTS ══ --%>
			<div class="tab-panel <%="subjects".equals(activeTab) ? "active" : ""%>">
				<%
				if (deletedSubjects.isEmpty()) {
				%>
				<div class="trash-empty">
					<div class="empty-icon">
						<i class="ti ti-books-off"></i>
					</div>
					<p>No deleted subjects</p>
					<span class="empty-hint">Deleted subjects will appear here</span>
				</div>
				<%
				} else {
				%>
				<div class="trash-list">
					<%
					for (Subject s : deletedSubjects) {
						Roadmap parentRoadmap = null;
						try {
							parentRoadmap = roadmapDAO.getRoadmapById(s.getRoadmapId());
						} catch (Exception e) {
						}
					%>
					<div class="trash-card">
						<div class="trash-card-left">
							<div class="trash-card-type">Subject</div>
							<div class="trash-hierarchy">
								<%
								if (parentRoadmap != null) {
								%>
								<div class="trash-hierarchy-row">
									<i class="ti ti-map" style="color: #7c6ef5"></i>
									<%=parentRoadmap.getTitle()%>
								</div>
								<%
								}
								%>
								<div
									class="trash-hierarchy-row current <%=parentRoadmap != null ? "tree-indent-1" : ""%>">
									<%
									if (parentRoadmap != null) {
									%><span class="tree-connector">└</span>
									<%
									}
									%>
									<i class="ti ti-books" style="color: #3b82f6"></i>
									<%=s.getName()%>
								</div>
							</div>
							<div class="trash-card-meta">
								<span class="deleted-pill"><i class="ti ti-trash"
									style="font-size: 11px"></i> Deleted</span>
							</div>
						</div>
						<div class="trash-card-actions">
							<form action="restoreSubject" method="post" style="margin: 0">
								<input type="hidden" name="id" value="<%=s.getId()%>"> <input
									type="hidden" name="roadmapId" value="<%=s.getRoadmapId()%>">
								<button class="btn btn-sm btn-primary" type="submit">
									<i class="ti ti-restore"></i> Restore
								</button>
							</form>
							<form action="purgeSubject" method="post" style="margin: 0"
								onsubmit="return confirm('Permanently delete this subject? This cannot be undone.')">
								<input type="hidden" name="id" value="<%=s.getId()%>"> <input
									type="hidden" name="roadmapId" value="<%=s.getRoadmapId()%>">
								<button class="btn btn-sm btn-danger" type="submit">
									<i class="ti ti-trash-x"></i> Delete
								</button>
							</form>
						</div>
					</div>
					<%
					}
					%>
				</div>
				<%
				}
				%>
			</div>

			<%-- ══ TAB: TOPICS ══ --%>
			<div class="tab-panel <%="topics".equals(activeTab) ? "active" : ""%>">
				<%
				if (deletedTopics.isEmpty()) {
				%>
				<div class="trash-empty">
					<div class="empty-icon">
						<i class="ti ti-notebook-off"></i>
					</div>
					<p>No deleted topics</p>
					<span class="empty-hint">Deleted topics will appear here</span>
				</div>
				<%
				} else {
				%>
				<div class="trash-list">
					<%
					for (Topic t : deletedTopics) {
						Subject parentSubject = null;
						Roadmap parentRoadmap = null;
						try {
							parentSubject = subjectDAO.getSubjectById(t.getSubjectId());
						} catch (Exception e) {
						}
						if (parentSubject != null) {
							try {
						parentRoadmap = roadmapDAO.getRoadmapById(parentSubject.getRoadmapId());
							} catch (Exception e) {
							}
						}
					%>
					<div class="trash-card">
						<div class="trash-card-left">
							<div class="trash-card-type">Topic</div>
							<div class="trash-hierarchy">
								<%
								if (parentRoadmap != null) {
								%>
								<div class="trash-hierarchy-row">
									<i class="ti ti-map" style="color: #7c6ef5"></i>
									<%=parentRoadmap.getTitle()%>
								</div>
								<%
								}
								%>
								<%
								if (parentSubject != null) {
								%>
								<div class="trash-hierarchy-row tree-indent-1">
									<span class="tree-connector">└</span> <i class="ti ti-books"
										style="color: #3b82f6"></i>
									<%=parentSubject.getName()%>
								</div>
								<%
								}
								%>
								<div class="trash-hierarchy-row current tree-indent-2">
									<span class="tree-connector">└</span> <i class="ti ti-notebook"
										style="color: #0d9488"></i>
									<%=t.getName()%>
								</div>
							</div>
							<div class="trash-card-meta">
								<span class="deleted-pill"><i class="ti ti-trash"
									style="font-size: 11px"></i> Deleted</span>
							</div>
						</div>
						<div class="trash-card-actions">
							<form action="restoreTopic" method="post" style="margin: 0">
								<input type="hidden" name="id" value="<%=t.getId()%>"> <input
									type="hidden" name="subjectId" value="<%=t.getSubjectId()%>">
								<input type="hidden" name="roadmapId" value="<%=roadmapId%>">
								<button class="btn btn-sm btn-primary" type="submit">
									<i class="ti ti-restore"></i> Restore
								</button>
							</form>
							<form action="purgeTopic" method="post" style="margin: 0"
								onsubmit="return confirm('Permanently delete this topic? This cannot be undone.')">
								<input type="hidden" name="id" value="<%=t.getId()%>"> <input
									type="hidden" name="roadmapId" value="<%=roadmapId%>">
								<button class="btn btn-sm btn-danger" type="submit">
									<i class="ti ti-trash-x"></i> Delete
								</button>
							</form>
						</div>
					</div>
					<%
					}
					%>
				</div>
				<%
				}
				%>
			</div>

			<%-- ══ TAB: SUBTOPICS ══ --%>
			<div class="tab-panel <%="subtopics".equals(activeTab) ? "active" : ""%>">
				<%
				if (deletedSubTopics.isEmpty()) {
				%>
				<div class="trash-empty">
					<div class="empty-icon">
						<i class="ti ti-list-search"></i>
					</div>
					<p>No deleted subtopics</p>
					<span class="empty-hint">Deleted subtopics will appear here</span>
				</div>
				<%
				} else {
				%>
				<div class="trash-list">
					<%
					for (SubTopic st : deletedSubTopics) {
						Topic parentTopic = null;
						Subject parentSubject = null;
						Roadmap parentRoadmap = null;
						try {
							parentTopic = topicDAO.getTopicById(st.getTopicId());
						} catch (Exception e) {
						}
						if (parentTopic != null) {
							try {
						parentSubject = subjectDAO.getSubjectById(parentTopic.getSubjectId());
							} catch (Exception e) {
							}
						}
						if (parentSubject != null) {
							try {
						parentRoadmap = roadmapDAO.getRoadmapById(parentSubject.getRoadmapId());
							} catch (Exception e) {
							}
						}
						String diff = st.getDifficulty() != null ? st.getDifficulty().toLowerCase().trim() : "medium";
						String diffClass = diff.equals("easy")
						? "difficulty-badge easy"
						: diff.equals("hard") ? "difficulty-badge hard" : "difficulty-badge medium";
					%>
					<div class="trash-card">
						<div class="trash-card-left">
							<div class="trash-card-type">Subtopic</div>
							<div class="trash-hierarchy">
								<%
								if (parentRoadmap != null) {
								%>
								<div class="trash-hierarchy-row">
									<i class="ti ti-map" style="color: #7c6ef5"></i>
									<%=parentRoadmap.getTitle()%>
								</div>
								<%
								}
								%>
								<%
								if (parentSubject != null) {
								%>
								<div class="trash-hierarchy-row tree-indent-1">
									<span class="tree-connector">└</span> <i class="ti ti-books"
										style="color: #3b82f6"></i>
									<%=parentSubject.getName()%>
								</div>
								<%
								}
								%>
								<%
								if (parentTopic != null) {
								%>
								<div class="trash-hierarchy-row tree-indent-2">
									<span class="tree-connector">└</span> <i class="ti ti-notebook"
										style="color: #0d9488"></i>
									<%=parentTopic.getName()%>
								</div>
								<%
								}
								%>
								<div class="trash-hierarchy-row current tree-indent-3">
									<span class="tree-connector">└</span> <i
										class="ti ti-list-check" style="color: #d97706"></i>
									<%=st.getName()%>
								</div>
							</div>
							<div class="trash-card-meta">
								<span class="<%=diffClass%>"><%=st.getDifficulty()%></span> <span
									class="time-pill"><i class="ti ti-clock"
									style="font-size: 11px"></i> <%=st.getEstimatedMinutes()%> min</span>
								<span class="deleted-pill"><i class="ti ti-trash"
									style="font-size: 11px"></i> Deleted</span>
							</div>
						</div>
						<div class="trash-card-actions">
							<form action="restoreSubTopic" method="post" style="margin: 0">
								<input type="hidden" name="id" value="<%=st.getId()%>">
								<input type="hidden" name="topicId" value="<%=st.getTopicId()%>">
								<input type="hidden" name="roadmapId" value="<%=roadmapId%>">
								<button class="btn btn-sm btn-primary" type="submit">
									<i class="ti ti-restore"></i> Restore
								</button>
							</form>
							<form action="purgeSubTopic" method="post" style="margin: 0"
								onsubmit="return confirm('Permanently delete this subtopic? This cannot be undone.')">
								<input type="hidden" name="id" value="<%=st.getId()%>">
								<input type="hidden" name="roadmapId" value="<%=roadmapId%>">
								<button class="btn btn-sm btn-danger" type="submit">
									<i class="ti ti-trash-x"></i> Delete
								</button>
							</form>
						</div>
					</div>
					<%
					}
					%>
				</div>
				<%
				}
				%>
			</div>

			<%-- ── Danger zone ── --%>
			<%
			if (totalDeleted > 0) {
			%>
			<div class="danger-zone">
				<div>
					<div class="danger-zone-label">Danger zone</div>
					<div class="danger-zone-text">
						<strong>Permanently delete everything</strong> — removes all
						<%=totalDeleted%>
						deleted item<%=totalDeleted != 1 ? "s" : ""%>
						and cannot be undone.
					</div>
				</div>
				<form action="purgeAll" method="post" style="margin: 0"
					onsubmit="return confirm('Permanently delete ALL items? This cannot be undone.')">
					<input type="hidden" name="userId" value="<%=user.getId()%>">
					<%
					if (hasRoadmapContext) {
					%>
					<input type="hidden" name="roadmapId" value="<%=roadmapId%>">
					<%
					}
					%>
					<button class="btn btn-danger" type="submit">
						<i class="ti ti-trash-x"></i> Empty trash
					</button>
				</form>
			</div>
			<%
			}
			%>

		</main>
	</div>

	<!-- Bottom tab bar — matches syllabus/schedule -->
	<nav class="bottom-nav">
		<a class="bn-item" href="dashboard.jsp"><i
			class="ti ti-layout-dashboard"></i>Dashboard</a> <a class="bn-item"
			href="create-roadmap.jsp"><i class="ti ti-plus"></i>New Roadmap</a>
			 <a class="bn-item active"
			href="trash.jsp"><i class="ti ti-recycle"></i>Deleted</a>
	</nav>

	<script>
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
	</script>
</body>
</html>
