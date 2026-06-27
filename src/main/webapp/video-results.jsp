<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="model.LearningResource"%>

<%
List<LearningResource> videos = (List<LearningResource>) request.getAttribute("videos");

int roadmapId = 0;
try {
    roadmapId = Integer.parseInt(request.getParameter("roadmapId"));
} catch (Exception e) { roadmapId = 0; }

String subtopicIdParam = request.getParameter("subtopicId");
int subtopicId = 0;
try { subtopicId = Integer.parseInt(subtopicIdParam); } catch (Exception e) {}
%>

<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Video Results | DevTracker</title>
<link rel="stylesheet" href="css/main.css">
<link rel="stylesheet" href="css/learning.css">
<link rel="stylesheet" href="css/animation.css">
<link rel="stylesheet" href="css/auth.css">
<link rel="stylesheet" href="css/loading.css">
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/@tabler/icons-webfont@latest/tabler-icons.min.css">
<style>
.video-grid {
    display: grid;
    grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
    gap: 20px;
    margin-top: 20px;
}
.video-card {
    background: var(--surface, #fffaf4);
    border: 1px solid var(--border, #e8dfd2);
    padding: 15px;
    border-radius: 16px;
    box-shadow: 0 2px 12px rgba(60,40,10,.07);
    transition: box-shadow .18s, border-color .18s;
}
.video-card:hover {
    box-shadow: 0 6px 24px rgba(60,40,10,.13);
    border-color: var(--accent, #b8741f);
}
.video-card iframe {
    width: 100%;
    height: 220px;
    border: 0;
    border-radius: 12px;
}
.video-card h3 {
    margin-top: 12px;
    font-size: 14px;
    color: var(--text, #3a2c1a);
}
.video-card .muted-text {
    font-size: 12px;
    color: var(--text-muted, #8a7a64);
}
.no-videos {
    text-align: center;
    padding: 48px 24px;
    color: var(--text-muted, #8a7a64);
}
.no-videos .empty-icon {
    font-size: 48px;
    margin-bottom: 12px;
}
@media (max-width: 600px) {
    .video-grid { grid-template-columns: 1fr; }
    .video-card iframe { height: 200px; }
}
</style>
</head>
<body>

<!-- Mobile topbar -->
<div class="mobile-topbar">
    <a class="mobile-logo" href="dashboard.jsp"><i class="ti ti-route"></i> DevTracker</a>
</div>

<div class="layout">

    <!-- Sidebar -->
    <aside class="sidebar" id="sidebar">
        <div class="sidebar-logo">
            <a class="logo-mark" href="dashboard.jsp">
                <i class="ti ti-route"></i>
                <div>
                    <div>DevTracker</div>
                    <div class="logo-sub">Learning OS</div>
                </div>
            </a>
        </div>
        <nav class="nav">
            <div class="nav-section-label">Menu</div>
            <a class="nav-item" href="dashboard.jsp"><i class="ti ti-layout-dashboard"></i> Dashboard</a>
            <a class="nav-item" href="create-roadmap.jsp"><i class="ti ti-plus"></i> Create Roadmap</a>
            <%
            if (roadmapId > 0) {
            %>
            <a class="nav-item" href="openAIRoadmap?id=<%=roadmapId%>"><i class="ti ti-books"></i> Syllabus</a>
            <a class="nav-item" href="schedule.jsp?roadmapId=<%=roadmapId%>"><i class="ti ti-calendar-week"></i> Daily Tasks</a>
            <%
            }
            %>
            <a class="nav-item active" href="#"><i class="ti ti-brand-youtube"></i> Videos</a>
            <div class="nav-section-label">Manage</div>
            <a class="nav-item" href="trash.jsp"><i class="ti ti-recycle"></i> Recover Deleted</a>
        </nav>
        <div class="sidebar-footer">
            <div class="user-row">
                <div class="avatar"><i class="ti ti-user" style="font-size:15px"></i></div>
                <div class="user-info">
                    <div class="user-name">My Account</div>
                    <div class="user-plan">Free plan</div>
                </div>
                <a href="logout" class="logout-btn" title="Logout"><i class="ti ti-logout"></i></a>
            </div>
        </div>
    </aside>

    <main class="main">

        <div class="top-bar">
            <div>
                <h1>🎬 Video Results</h1>
                <p class="muted-text">YouTube learning videos</p>
            </div>
            <button class="btn" onclick="history.back()">
                <i class="ti ti-arrow-left"></i> Back
            </button>
        </div>

        <div class="stats-card">
            <% if (videos != null && videos.size() > 0) { %>

            <div class="video-grid">
                <% for (LearningResource v : videos) { %>
                <div class="video-card">
                    <iframe src="https://www.youtube.com/embed/<%=v.getVideoId()%>" allowfullscreen></iframe>
                    <h3><%=v.getTitle()%></h3>
                    <p class="muted-text"><%=v.getChannelName()%></p>
                </div>
                <% } %>
            </div>

            <% } else { %>
            <div class="no-videos">
                <div class="empty-icon">😢</div>
                <h3>No videos found</h3>
                <p class="muted-text">Try a different search term</p>
                <button class="btn" style="margin-top:16px" onclick="history.back()">
                    <i class="ti ti-arrow-left"></i> Go back and try again
                </button>
            </div>
            <% } %>
        </div>

        <% if (subtopicId > 0) { %>
        <div class="stats-card" style="margin-top:20px">
            <h2>🔍 Search again</h2>
            <p class="muted-text">Try a different keyword or teacher name</p>
            <form action="searchVideos" method="get" style="display:flex;gap:10px;margin-top:12px;flex-wrap:wrap">
                <input type="hidden" name="subtopicId" value="<%=subtopicId%>">
                <% if (roadmapId > 0) { %>
                <input type="hidden" name="roadmapId" value="<%=roadmapId%>">
                <% } %>
                <input type="text" name="query" placeholder="Search videos (topic, language, teacher…)" required
                    style="flex:1;min-width:200px">
                <button class="btn btn-primary" type="submit"><i class="ti ti-search"></i> Search</button>
            </form>
        </div>
        <% } %>

    </main>
</div>

<!-- Bottom nav — mirrors generateLearning.jsp exactly -->
<nav class="bottom-nav">
    <a class="bn-item" href="dashboard.jsp"><i class="ti ti-layout-dashboard"></i>Dashboard</a>
    <% if (roadmapId > 0) { %>
    <a class="bn-item" href="openAIRoadmap?id=<%=roadmapId%>"><i class="ti ti-books"></i>Syllabus</a>
    <a class="bn-item" href="schedule.jsp?roadmapId=<%=roadmapId%>"><i class="ti ti-calendar-week"></i>Daily Tasks</a>
    <% } else { %>
    <a class="bn-item" href="viewAIRoadmaps"><i class="ti ti-layout-list"></i>Roadmaps</a>
    <% } %>
    <a class="bn-item active" href="#"><i class="ti ti-brand-youtube"></i>Videos</a>
</nav>
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
