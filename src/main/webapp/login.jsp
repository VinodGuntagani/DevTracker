<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
String error = (String) request.getAttribute("error");
String email = (String) request.getAttribute("email");
%>
<!DOCTYPE html>
<html lang="en">
<head>

<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Login | DevTracker</title>
<link rel="stylesheet" href="css/main.css">
<link rel="stylesheet" href="css/auth.css">
<link rel="stylesheet" href="css/animation.css">
<link rel="stylesheet" href="css/loading.css">

<link rel="stylesheet"
	href="https://cdn.jsdelivr.net/npm/@tabler/icons-webfont@latest/tabler-icons.min.css">


</head>

<body>


	<div class="auth-container">

		<!-- ============================================================
		     ILLUSTRATED BACKGROUND
		     Blobs are pure CSS (::before). This SVG adds:
		       â¢ Two dashed curved paths (orange + blue)
		       â¢ Dot nodes along the paths
		       â¢ Four floating icon badges (book, trophy, checkmark, rocket)
		     ============================================================ -->
		<svg class="auth-bg-svg" viewBox="0 0 1000 750"
			preserveAspectRatio="xMidYMid slice" aria-hidden="true">

			<defs>
				<filter id="cornerBlobBlurLogin" x="-50%" y="-50%" width="200%"
				height="200%">
					<feGaussianBlur stdDeviation="55" />
				</filter>
			</defs>

			<!-- ── Glowing corner blobs (faint, blurred, animated, sit behind everything) ── -->
			<circle class="corner-blob corner-blob-orange" cx="20" cy="20"
				r="150" fill="#b8741f" opacity="0.16"
				filter="url(#cornerBlobBlurLogin)" />
			<circle class="corner-blob corner-blob-blue" cx="985" cy="40" r="180"
				fill="#5b8fc7" opacity="0.14" filter="url(#cornerBlobBlurLogin)" />
			<circle class="corner-blob corner-blob-blue" cx="15" cy="730" r="170"
				fill="#5b8fc7" opacity="0.13" filter="url(#cornerBlobBlurLogin)" />
			<circle class="corner-blob corner-blob-orange" cx="990" cy="720"
				r="190" fill="#b8741f" opacity="0.17"
				filter="url(#cornerBlobBlurLogin)" />

			<!-- ── Orange dashed path (top arc) ── -->
			<path class="bp-orange"
				d="M 374 307 C 249 198 326 174 356 174 S 411 106 479 195" />

			<!-- ── Blue dashed path (right arc) ── -->
			<path class="bp-blue"
				d=" M 581 229 C 615 173 641 141 680 148 S 714 163 712 160" />

			<!-- ── Orange dashed path (bottom arc) ── -->
			<path class="bp-blue"
				d="M 386 371 C 271 376 313 489 348 511 S 312 621 501 602" />
			<path class="bp-orange"
				d=" M 501 602 C 614 553 714 636 667 531 S 819 357 607 351" />
				<path class="bp-orange"
				d=" M 607 351 C 621 278 648 308 648 270 S 622 227 581 232" />
				<path class="bp-orange"
				d="  M 386 371 C 341 350 339 329 354 335 S 325 293 374 307" />
				<path class="bp-orange"
				d="  M 479 195 C 464 159 501 116 535 181 S 567 157 580 229" />
			<!-- Dot nodes on paths -->
			<circle class="pn" cx="362" cy="173" r="4" />
			<circle class="pn" cx="326" cy="257" r="4" />
			<circle class="pn pn-pulse" cx="637" cy="157" r="4" />
			<circle class="pn-blue pn" cx="700" cy="368" r="4" />
			<circle class="pn-blue pn" cx="677" cy="560" r="4" />
			<circle class="pn-green pn" cx="505" cy="600" r="4" />
			<circle class="pn" cx="462" cy="176" r="4" />
			<circle class="pn" cx="350" cy="511" r="4" />

			<!-- ── Badge: Book (top-left) ── -->
			<circle class="badge-ring badge-ring-orange badge-anim-1" cx="300"
				cy="220" r="15" />
			<text class="badge-icon badge-icon-orange badge-anim-1" x="300"
				y="220">📖</text>

			<!-- ── Badge: Trophy (top-right) ── -->
			<circle class="badge-ring badge-ring-blue badge-anim-2" cx="710"
				cy="160" r="15" />
			<text class="badge-icon badge-icon-blue badge-anim-2" x="710" y="160">🏆</text>

			<!-- ── Badge: Checkmark (left-middle) ── -->
		
			<!-- Custom SVG checkmark instead of emoji for better rendering -->
			 	<circle class="badge-ring badge-ring-orange badge-anim-3" cx="315"
				cy="460" r="15" />
			<text class="badge-icon badge-icon-accent badge-anim-3" x="315"
				y="460">✔️</text>

			<!-- ── Badge: Rocket (bottom-right) ── -->
			<circle class="badge-ring badge-ring-orange badge-anim-4" cx="670"
				cy="444" r="15" />
			<text class="badge-icon badge-icon-accent badge-anim-4" x="670"
				y="444">🚀</text>

		</svg>

		<div class="auth-card">

			<div class="auth-brand">
				<div class="auth-brand-mark">🚀</div>
				<div>
					<div class="auth-brand-name">DevTracker</div>
					<div class="auth-brand-sub">Learning OS</div>
				</div>
			</div>

			<h2>Welcome back</h2>
			<p class="auth-subtitle">Continue your learning journey where you
				left off.</p>

			<%
			if (error != null) {
			%>
			<div class="auth-error show">
				<i class="ti ti-alert-circle"></i> <span><%=error%></span>
			</div>
			<%
			}
			%>

			<form action="login" method="post" id="loginForm"
				onsubmit="showLoader('login');handleLoginSubmit(event);">

				<div class="input-group">
					<label for="email">Email</label>
					<div class="input-wrap">
						<span class="input-icon"><i class="ti ti-mail"></i></span> <input
							id="email" type="email" name="email"
							value="<%=email != null ? email : ""%>"
							placeholder="you@example.com" autocomplete="email" required>
					</div>
				</div>

				<div class="input-group">
					<label for="password">Password</label>
					<div class="input-wrap">
						<span class="input-icon"><i class="ti ti-lock"></i></span> <input
							id="password" type="password" name="password"
							placeholder="Enter password" autocomplete="current-password"
							required>
						<button type="button" class="password-toggle"
							onclick="togglePassword('password', this)"
							aria-label="Show password">
							<i class="ti ti-eye"></i>
						</button>
					</div>
				</div>

				<div class="auth-row">
					<label class="remember-me"> <input type="checkbox"
						name="remember"> Remember me
					</label> <a class="forgot-link" href="forgot-password.jsp">Forgot
						password?</a>
				</div>

				<button class="btn primary-btn" type="submit" id="loginBtn">
					<span class="spinner"></span> <span class="btn-label">Log in</span>
				</button>

			</form>

			<div class="auth-divider">or continue with</div>

			<div class="oauth-row">
				<button type="button" class="oauth-btn"
					onclick="oauthLogin('google')">
					<i class="ti ti-brand-google"></i> Google
				</button>
				<button type="button" class="oauth-btn"
					onclick="oauthLogin('github')">
					<i class="ti ti-brand-github"></i> GitHub
				</button>
			</div>

			<p class="auth-link">
				New here? <a href="register.jsp">Create account</a>
			</p>

		</div>
	</div>

	<script>
	function togglePassword(id, btn) {
		const input = document.getElementById(id);
		const icon = btn.querySelector('i');
		const isHidden = input.type === 'password';
		input.type = isHidden ? 'text' : 'password';
		icon.className = isHidden ? 'ti ti-eye-off' : 'ti ti-eye';
		btn.setAttribute('aria-label', isHidden ? 'Hide password' : 'Show password');
	}

	function showAuthError(message) {
		const box = document.getElementById('authError');
		document.getElementById('authErrorText').textContent = message;
		box.classList.add('show');
	}

	function handleLoginSubmit(e) {
		const btn = document.getElementById('loginBtn');
		btn.classList.add('loading');
	}

	function oauthLogin(provider) {
		console.log('OAuth login requested:', provider);
	}
	</script>

	<!-- Loader -->
	<div id="loadingContainer"></div>
	<script>window.addEventListener("DOMContentLoaded", async () => {

    const res = await fetch("includes/loading.html");

    const html = await res.text();

    document
        .getElementById("loadingContainer")
        .innerHTML = html;

});</script>
	<script src="js/loading.js"></script>
</body>
</html>