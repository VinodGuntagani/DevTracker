<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="dao.SubjectDAO"%>
<%@ page import="model.Subject"%>
<%@ page import="java.util.List"%>
<%@ page import="dao.TopicDAO"%>

<%
int roadmapId = Integer.parseInt(request.getParameter("roadmapId"));
SubjectDAO dao = new SubjectDAO();
List<Subject> subjects = dao.getSubjects(roadmapId);
TopicDAO progressDao = new TopicDAO();

int totalSubjects = subjects.size();
int completedSubjects = 0;
int totalProg = 0;
for (Subject s : subjects) {
    int p = progressDao.getSubjectProgress(s.getId());
    totalProg += p;
    if (p >= 100) completedSubjects++;
}
int avgProg = totalSubjects > 0 ? totalProg / totalSubjects : 0;
%>

<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Subjects | DevTracker</title>

<link rel="stylesheet" href="css/main.css">
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/@tabler/icons-webfont@latest/tabler-icons.min.css">

</head>

<body>

<!-- Mobile top bar -->
<div class="mobile-topbar">
    <a class="mobile-logo" href="dashboard.jsp">
        <i class="ti ti-route"></i> DevTracker
    </a>
    <button class="hamburger" onclick="openSidebar()" aria-label="Open menu">
        <i class="ti ti-menu-2"></i>
    </button>
</div>

<!-- Overlay -->
<div class="overlay" id="overlay" onclick="closeSidebar()"></div>

<div class="layout">

    <!-- Sidebar -->
    <aside class="sidebar" id="sidebar">
        <div class="sidebar-logo">
            <a class="logo-mark" href="dashboard.jsp">
                <i class="ti ti-route"></i> DevTracker
            </a>
            <div class="logo-sub">Learning OS</div>
        </div>
        <nav class="nav">
            <div class="nav-section-label">Menu</div>
            <a class="nav-item" href="dashboard.jsp">
                <i class="ti ti-layout-dashboard"></i> Dashboard
            </a>
            <a class="nav-item active" href="#">
                <i class="ti ti-books"></i> Subjects
            </a>
            <a class="nav-item" href="ai-roadmap.jsp">
                <i class="ti ti-robot"></i> AI tools
            </a>
            <a class="nav-item" href="viewAIRoadmaps">
                <i class="ti ti-layout-list"></i> AI roadmaps
            </a>
            <div class="nav-section-label">Manage</div>
            <a class="nav-item" href="recoverSubjects.jsp?roadmapId=<%=roadmapId%>">
                <i class="ti ti-recycle"></i> Recover deleted
            </a>
        </nav>
        <div class="sidebar-footer">
            <div class="user-row">
                <div class="avatar"><i class="ti ti-user" style="font-size:15px"></i></div>
                <div class="user-info">
                    <div class="user-name">My Account</div>
                    <div class="user-plan">Free plan</div>
                </div>
                <a href="logout" class="logout-btn" title="Logout">
                    <i class="ti ti-logout"></i>
                </a>
            </div>
        </div>
    </aside>

    <!-- Main -->
    <main class="main">

        <!-- Page header -->
        <div class="page-header">
            <div>
                <div class="breadcrumb">
                    <a href="dashboard.jsp"><i class="ti ti-layout-dashboard"></i> Dashboard</a>
                    <i class="ti ti-chevron-right"></i>
                    <span>Subjects</span>
                </div>
                <div class="page-title">📚 Subjects</div>
                <div class="page-sub"><%=totalSubjects%> subject<%=totalSubjects != 1 ? "s" : ""%> in this roadmap</div>
            </div>
            <div class="header-actions">
                <a class="btn" href="recoverSubjects.jsp?roadmapId=<%=roadmapId%>">
                    <i class="ti ti-recycle"></i> Recover deleted
                </a>
                <a class="btn" href="dashboard.jsp">
                    <i class="ti ti-arrow-left"></i> Back
                </a>
            </div>
        </div>

        <!-- Stats -->
        <div class="stats-grid">
            <div class="stat-card">
                <div class="stat-icon si-purple"><i class="ti ti-books"></i></div>
                <div>
                    <div class="stat-val"><%=totalSubjects%></div>
                    <div class="stat-label">Total subjects</div>
                </div>
            </div>
            <div class="stat-card">
                <div class="stat-icon si-teal"><i class="ti ti-circle-check"></i></div>
                <div>
                    <div class="stat-val"><%=completedSubjects%></div>
                    <div class="stat-label">Completed</div>
                </div>
            </div>
            <div class="stat-card">
                <div class="stat-icon si-blue"><i class="ti ti-trending-up"></i></div>
                <div>
                    <div class="stat-val"><%=avgProg%>%</div>
                    <div class="stat-label">Avg. progress</div>
                </div>
            </div>
        </div>

        <!-- Add subject -->
        <div class="add-panel">
            <div class="add-panel-title">Add new subject</div>
            <form action="addSubject" method="post" class="add-form">
                <input type="hidden" name="roadmapId" value="<%=roadmapId%>">
                <input type="text" name="name" placeholder="e.g. Arrays, Sorting Algorithms, OOP…" required>
                <button class="btn btn-primary" type="submit">
                    <i class="ti ti-plus"></i> Add subject
                </button>
            </form>
        </div>

        <!-- Subject cards -->
        <div>
            <div class="section-hd">
                <h2>All subjects</h2>
                <span style="font-size:12px;color:#aaa;"><%=totalSubjects%> total</span>
            </div>

            <div class="card-grid">

                <%
                if (subjects.isEmpty()) {
                %>
                <p style="color:#aaa;font-size:14px;">No subjects yet. Add one above to get started.</p>
                <%
                } else {
                int idx = 1;
                for (Subject s : subjects) {
                    int progress = progressDao.getSubjectProgress(s.getId());
                    boolean isDone = progress >= 100;
                    boolean isNew  = progress == 0;
                    String fillClass = isDone ? "high" : (progress >= 40 ? "medium" : "low");
                    String badgeClass = isDone ? "badge-done" : (isNew ? "badge-new" : "badge-ongoing");
                    String badgeLabel = isDone ? "Completed" : (isNew ? "Not started" : "In progress");
                    String badgeIcon  = isDone ? "ti-circle-check" : (isNew ? "ti-circle" : "ti-clock");
                %>

                <div class="card">

                    <!-- Head -->
                    <div class="card-head">
                        <div class="card-head-left">
                            <div class="subject-number">SUBJECT <%=idx%></div>
                            <div class="card-title" id="titleText<%=s.getId()%>"><%=s.getName()%></div>
                        </div>
                        <button class="card-edit-btn" title="Edit" onclick="enableEdit(<%=s.getId()%>)">
                            <i class="ti ti-pencil"></i>
                        </button>
                    </div>

                    <!-- Status badge -->
                    <div id="badgeArea<%=s.getId()%>">
                        <span class="badge <%=badgeClass%>">
                            <i class="ti <%=badgeIcon%>" style="font-size:11px"></i> <%=badgeLabel%>
                        </span>
                    </div>

                    <!-- Edit form (inline) -->
                    <form id="editForm<%=s.getId()%>" action="editSubject" method="post" class="edit-form">
                        <input type="hidden" name="id"        value="<%=s.getId()%>">
                        <input type="hidden" name="roadmapId" value="<%=roadmapId%>">
                        <input type="text"   name="name"      value="<%=s.getName()%>" placeholder="Subject name">
                        <div style="display:flex;gap:8px;">
                            <button class="btn btn-primary btn-xs" type="submit">
                                <i class="ti ti-check"></i> Save
                            </button>
                            <button type="button" class="btn btn-xs" onclick="cancelEdit(<%=s.getId()%>)">
                                Cancel
                            </button>
                        </div>
                    </form>

                    <!-- Progress -->
                    <div>
                        <div class="progress-row">
                            <span>Progress</span>
                            <strong><%=progress%>%</strong>
                        </div>
                        <div class="progress-track">
                            <div class="progress-fill <%=fillClass%>" style="width:<%=progress%>%"></div>
                        </div>
                    </div>

                    <!-- Actions -->
                    <div class="card-actions">
                        <a class="btn btn-sm btn-primary" href="topics.jsp?subjectId=<%=s.getId()%>&roadmapId=<%=roadmapId%>">
                            <i class="ti ti-arrow-right"></i> Open
                        </a>

                        

                        <!-- Delete (with confirm) -->
                        <form action="deleteSubject" method="post" style="margin:0;margin-left:auto;"
                              onsubmit="return confirm('Delete \'<%=s.getName()%>\'? This can be recovered later.')">
                            <input type="hidden" name="id"        value="<%=s.getId()%>">
                            <input type="hidden" name="roadmapId" value="<%=roadmapId%>">
                            <button class="btn btn-sm btn-danger" type="submit">
                                <i class="ti ti-trash"></i> Delete
                            </button>
                        </form>
                    </div>

                </div>

                <% idx++; } } %>

                <!-- Empty-state add card -->
                <div class="card-empty" onclick="document.querySelector('.add-form input[type=text]').focus()">
                    <i class="ti ti-plus"></i>
                    <p>Add a subject</p>
                </div>

            </div>
        </div>

    </main>
</div>

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
function enableEdit(id) {
    document.getElementById("editForm"  + id).style.display = "flex";
    document.getElementById("titleText" + id).style.display = "none";
    document.getElementById("badgeArea" + id).style.display = "none";
}
function cancelEdit(id) {
    document.getElementById("editForm"  + id).style.display = "none";
    document.getElementById("titleText" + id).style.display = "block";
    document.getElementById("badgeArea" + id).style.display = "block";
}
</script>
</body>
</html>
