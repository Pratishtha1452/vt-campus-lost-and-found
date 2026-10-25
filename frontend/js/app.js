
const reportBtn = document.getElementById("reportBtn");
const browseBtn = document.getElementById("browseBtn");

// All HTML pages are now in the same directory.
const htmlPath = "";

reportBtn?.addEventListener("click", () => {
  const token = sessionStorage.getItem("token");

  if (token) {
    window.location.href = htmlPath + "report.html";
  } else {
    // Remember the destination for after login.
    sessionStorage.setItem("redirectAfterLogin", "report.html");
    window.location.href = htmlPath + "auth.html";
  }
});

browseBtn?.addEventListener("click", () => {
  window.location.href = htmlPath + "browse.html";
});

// Navigation elements
const navLogin = document.getElementById("navLogin");
const navProfile = document.getElementById("navProfile");

const hasToken = Boolean(sessionStorage.getItem("token"));

// Update navigation based on login status.
if (hasToken) {
  if (navLogin) {
    navLogin.hidden = true;
  }

  if (navProfile) {
    navProfile.hidden = false;
  }
} else {
  if (navLogin) {
    navLogin.hidden = false;
  }

  if (navProfile) {
    navProfile.hidden = true;
  }
}
