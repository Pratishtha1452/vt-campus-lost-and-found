const API_BASE_URL = "http://localhost:8080";

const registerForm = document.getElementById("registerForm");
const loginForm = document.getElementById("loginForm");

const registerMessage = document.getElementById("registerMessage");
const loginMessage = document.getElementById("loginMessage");

// Registration
registerForm?.addEventListener("submit", async (event) => {
    event.preventDefault();

    registerMessage.textContent = "Creating your account...";

    const user = {
        name: document.getElementById("registerName").value.trim(),
        email: document.getElementById("registerEmail").value.trim(),
        password: document.getElementById("registerPassword").value
    };

    try {
        const response = await fetch(`${API_BASE_URL}/api/users/register`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(user)
        });

        const data = await response.json().catch(() => null);

        if (!response.ok) {
            throw new Error(data?.message || data?.error || "Registration failed.");
        }

        registerMessage.textContent =
            "Account created successfully! You can now log in.";

        registerForm.reset();

    } catch (error) {
        registerMessage.textContent =
            error.message || "Could not connect to the server.";
    }
});

// Login
loginForm?.addEventListener("submit", async (event) => {
    event.preventDefault();

    loginMessage.textContent = "Logging in...";

    const credentials = {
        email: document.getElementById("loginEmail").value.trim(),
        password: document.getElementById("loginPassword").value
    };

    try {
        const response = await fetch(`${API_BASE_URL}/api/auth/login`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(credentials)
        });

        const data = await response.json().catch(() => null);

        if (!response.ok) {
            throw new Error(data?.message || data?.error || "Login failed.");
        }

        // We'll add token storage and redirect after confirming
        // the exact response structure from your backend.
        if (!data?.token) {
            throw new Error("Login succeeded, but no token was returned.");
        }

        sessionStorage.setItem("token", data.token);
        sessionStorage.setItem("tokenType", data.tokenType || "Bearer");
        sessionStorage.setItem("user", JSON.stringify(data.user));

        loginMessage.textContent = "Login successful! Redirecting...";

        const redirectPage =
            sessionStorage.getItem("redirectAfterLogin") || "browse.html";

        sessionStorage.removeItem("redirectAfterLogin");

        setTimeout(() => {
            window.location.href = redirectPage;
        }, 700);

    } catch (error) {
        loginMessage.textContent =
            error.message || "Could not connect to the server.";
    }
});