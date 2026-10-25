
const API_BASE_URL =
    "https://vt-campus-lost-and-found.onrender.com";

const registerForm = document.getElementById("registerForm");
const loginForm = document.getElementById("loginForm");

const registerMessage = document.getElementById("registerMessage");
const loginMessage = document.getElementById("loginMessage");

function showMessage(element, text, type = "") {
    element.textContent = text;
    element.className = type;
}

async function readResponse(response) {
    const data = await response.json().catch(() => null);

    if (!response.ok) {
        throw new Error(
            data?.message ||
            data?.detail ||
            data?.error ||
            `Request failed (${response.status}). Please try again.`
        );
    }

    return data;
}

// Registration
registerForm?.addEventListener("submit", async (event) => {
    event.preventDefault();

    const submitButton = registerForm.querySelector(
        'button[type="submit"]'
    );

    const user = {
        name: document.getElementById("registerName").value.trim(),
        email: document.getElementById("registerEmail").value.trim(),
        password: document.getElementById("registerPassword").value
    };

    if (!user.name || !user.email || !user.password) {
        showMessage(registerMessage, "Please complete all fields.", "error");
        return;
    }

    submitButton.disabled = true;
    submitButton.textContent = "Creating account...";
    showMessage(registerMessage, "Creating your account...");

    try {
        const response = await fetch(
            `${API_BASE_URL}/api/users/register`,
            {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify(user)
            }
        );

        await readResponse(response);

        showMessage(
            registerMessage,
            "Account created successfully! You can now log in.",
            "success"
        );

        registerForm.reset();

    } catch (error) {
        console.error("Registration failed:", error);

        showMessage(
            registerMessage,
            error.message || "Could not connect to the server.",
            "error"
        );

    } finally {
        submitButton.disabled = false;
        submitButton.textContent = "Create Account";
    }
});

// Login
loginForm?.addEventListener("submit", async (event) => {
    event.preventDefault();

    const submitButton = loginForm.querySelector(
        'button[type="submit"]'
    );

    const credentials = {
        email: document.getElementById("loginEmail").value.trim(),
        password: document.getElementById("loginPassword").value
    };

    if (!credentials.email || !credentials.password) {
        showMessage(loginMessage, "Please enter your email and password.", "error");
        return;
    }

    submitButton.disabled = true;
    submitButton.textContent = "Logging in...";
    showMessage(loginMessage, "Logging in...");

    try {
        const response = await fetch(
            `${API_BASE_URL}/api/auth/login`,
            {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify(credentials)
            }
        );

        const data = await readResponse(response);

        if (!data?.token) {
            throw new Error(
                "Login response did not contain an authentication token."
            );
        }

        // Store the authentication details.
        sessionStorage.setItem("token", data.token);
        sessionStorage.setItem(
            "tokenType",
            data.tokenType || "Bearer"
        );

        // Store user details only when the API provides them.
        if (data.user && typeof data.user === "object") {
            sessionStorage.setItem("user", JSON.stringify(data.user));
        } else {
            sessionStorage.removeItem("user");
        }

        showMessage(loginMessage, "Login successful! Redirecting...", "success");

        // Use only a local HTML page as the redirect destination.
        const allowedPages = [
            "index.html",
            "browse.html",
            "report.html",
            "claims.html",
            "profile.html"
        ];

        const requestedPage =
            sessionStorage.getItem("redirectAfterLogin");

        const redirectPage = allowedPages.includes(requestedPage)
            ? requestedPage
            : "browse.html";

        sessionStorage.removeItem("redirectAfterLogin");

        setTimeout(() => {
            window.location.href = redirectPage;
        }, 700);

    } catch (error) {
        console.error("Login failed:", error);

        showMessage(
            loginMessage,
            error.message || "Could not connect to the server.",
            "error"
        );

    } finally {
        submitButton.disabled = false;
        submitButton.textContent = "Log In";
    }
});
