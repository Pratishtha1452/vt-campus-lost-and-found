const API_URL = "http://localhost:8080/api/reports";

const form = document.getElementById("reportForm");
const message = document.getElementById("message");
const submitButton = document.getElementById("submitButton");
const logoutButton = document.getElementById("logoutButton");
const dateInput = document.getElementById("dateOccurred");

function getToken() {
    return sessionStorage.getItem("token");
}

function showMessage(text, type) {
    message.textContent = text;
    message.className = `message ${type}`;
    message.hidden = false;
}

function clearMessage() {
    message.textContent = "";
    message.hidden = true;
    message.className = "message";
}

function redirectToLogin() {
    sessionStorage.setItem("redirectAfterLogin", "report.html");
    window.location.href = "auth.html";
}

// Require login before submitting a report.
if (!getToken()) {
    redirectToLogin();
}

// Prevent selecting a future date.
const today = new Date();
const localToday = [
    today.getFullYear(),
    String(today.getMonth() + 1).padStart(2, "0"),
    String(today.getDate()).padStart(2, "0")
].join("-");

dateInput.max = localToday;

logoutButton.addEventListener("click", () => {
    sessionStorage.removeItem("token");
    sessionStorage.removeItem("tokenType");
    sessionStorage.removeItem("user");
    sessionStorage.removeItem("redirectAfterLogin");

    window.location.href = "auth.html";
});

form.addEventListener("submit", async (event) => {
    event.preventDefault();
    clearMessage();

    const token = getToken();

    if (!token) {
        redirectToLogin();
        return;
    }

    const formData = new FormData(form);

    const report = {
        title: formData.get("title").trim(),
        description: formData.get("description").trim(),
        type: formData.get("type"),
        location: formData.get("location").trim(),
        dateOccurred: formData.get("dateOccurred"),
        imageUrl: formData.get("imageUrl").trim() || null
    };

    // Check required fields before making the request.
    if (
        !report.title ||
        !report.description ||
        !report.type ||
        !report.location ||
        !report.dateOccurred
    ) {
        showMessage("Please complete all required fields.", "error");
        return;
    }

    submitButton.disabled = true;
    submitButton.textContent = "Submitting...";

    try {
        const response = await fetch(API_URL, {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                "Authorization": `Bearer ${token}`
            },
            body: JSON.stringify(report)
        });

        const result = await response.json().catch(() => null);

        if (response.status === 401 || response.status === 403) {
            sessionStorage.removeItem("token");
            sessionStorage.removeItem("tokenType");
            sessionStorage.removeItem("user");

            showMessage(
                "Your session may have expired. Please log in again.",
                "error"
            );

            setTimeout(redirectToLogin, 1500);
            return;
        }

        if (!response.ok) {
            const errorText =
                result?.message ||
                result?.detail ||
                result?.error ||
                "Could not submit the report. Please check your details.";

            showMessage(errorText, "error");
            return;
        }

        showMessage(
            "Report submitted successfully! Redirecting to browse items...",
            "success"
        );

        form.reset();
        dateInput.max = localToday;

        setTimeout(() => {
            window.location.href = "browse.html";
        }, 1200);

    } catch (error) {
        console.error("Report submission failed:", error);

        showMessage(
            "Could not connect to the server. Make sure your Spring Boot backend is running.",
            "error"
        );
    } finally {
        submitButton.disabled = false;
        submitButton.textContent = "Submit Report";
    }
});