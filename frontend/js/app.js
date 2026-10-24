
const reportBtn = document.getElementById("reportBtn");
const browseBtn = document.getElementById("browseBtn");

// Update this path if your folders are arranged differently.
const htmlPath = "../html/";

reportBtn?.addEventListener("click", () => {
  const token = sessionStorage.getItem("token");

  if (token) {
    window.location.href = htmlPath + "report.html";
  } else {
    // Remember where the user wanted to go after login.
    sessionStorage.setItem("redirectAfterLogin", "report.html");
    window.location.href = htmlPath + "auth.html";
  }
});

browseBtn?.addEventListener("click", () => {
  window.location.href = htmlPath + "browse.html";
});


const navLogin = document.getElementById("navLogin");
const navProfile = document.getElementById("navProfile");
const navReport = document.getElementById("navReport");

const hasToken = Boolean(sessionStorage.getItem("token"));

if (hasToken) {
  if (navLogin) navLogin.hidden = true;
  if (navProfile) navProfile.hidden = false;
} else {
  if (navLogin) navLogin.hidden = false;
  if (navProfile) navProfile.hidden = true;
}
