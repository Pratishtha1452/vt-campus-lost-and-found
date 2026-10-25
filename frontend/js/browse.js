
const API_URL =
    "https://vt-campus-lost-and-found.onrender.com/api/reports";

const CLAIM_API_URL =
    "https://vt-campus-lost-and-found.onrender.com/api/claims";

const reportsContainer = document.getElementById("reportsContainer");
const searchInput = document.getElementById("searchInput");
const typeFilter = document.getElementById("typeFilter");
const searchBtn = document.getElementById("searchBtn");
const message = document.getElementById("message");
const logoutBtn = document.getElementById("logoutBtn");

let reports = [];

// Apply the item type requested by the homepage URL.
const params = new URLSearchParams(window.location.search);
const requestedType = (params.get("type") || "").toUpperCase();

if (requestedType === "LOST" || requestedType === "FOUND") {
    typeFilter.value = requestedType;
}

function getToken() {
    return sessionStorage.getItem("token");
}

function logout() {
    sessionStorage.removeItem("token");
    sessionStorage.removeItem("tokenType");
    sessionStorage.removeItem("user");
    sessionStorage.removeItem("redirectAfterLogin");

    window.location.href = "auth.html";
}

async function loadReports() {
    message.textContent = "Loading reports...";
    reportsContainer.replaceChildren();

    try {
        const token = getToken();

        const response = await fetch(API_URL, {
            headers: token
                ? { Authorization: `Bearer ${token}` }
                : {}
        });

        if (!response.ok) {
            throw new Error(`Could not load reports (${response.status}).`);
        }

        const data = await response.json();

        if (!Array.isArray(data)) {
            throw new Error("The server returned an unexpected reports response.");
        }

        reports = data;
        renderReports();
    } catch (error) {
        console.error("Loading reports failed:", error);

        message.textContent =
            "Could not load reports. Please try refreshing the page.";
    }
}

function renderReports() {
    const query = searchInput.value.trim().toLowerCase();
    const selectedType = typeFilter.value;

    const filteredReports = reports.filter(report => {
        const title = (report.title || "").toLowerCase();
        const description = (report.description || "").toLowerCase();
        const location = (report.location || "").toLowerCase();

        const matchesSearch =
            title.includes(query) ||
            description.includes(query) ||
            location.includes(query);

        const matchesType =
            selectedType === "ALL" ||
            String(report.type || "").toUpperCase() === selectedType;

        return matchesSearch && matchesType;
    });

    reportsContainer.replaceChildren();

    if (filteredReports.length === 0) {
        message.textContent = reports.length === 0
            ? "No items have been reported yet."
            : "No items match your search.";
        return;
    }

    message.textContent = `${filteredReports.length} item(s) found.`;

    filteredReports.forEach(report => {
        const card = document.createElement("article");
        card.className = "report-card";

        if (report.imageUrl) {
            const image = document.createElement("img");
            image.src = report.imageUrl;
            image.alt = report.title || "Reported item";
            image.className = "report-image";

            image.addEventListener("error", () => image.remove());
            card.appendChild(image);
        }

        const content = document.createElement("div");
        content.className = "report-content";

        const type = document.createElement("span");
        type.className =
            `type-badge ${String(report.type || "").toLowerCase()}`;
        type.textContent = report.type || "ITEM";

        const title = document.createElement("h2");
        title.textContent = report.title || "Untitled item";

        const description = document.createElement("p");
        description.textContent =
            report.description || "No description provided.";

        const location = document.createElement("p");
        location.textContent =
            `Location: ${report.location || "Not specified"}`;

        const date = document.createElement("p");
        date.textContent =
            `Date: ${report.dateOccurred || "Not specified"}`;

        const reporter = document.createElement("p");
        reporter.textContent =
            `Reported by: ${report.reportedByName || "Campus user"}`;

        const status = document.createElement("p");
        status.textContent = `Status: ${report.status || "Unknown"}`;

        content.append(
            type,
            title,
            description,
            location,
            date,
            reporter,
            status
        );

        // Only found items can be claimed.
        if (String(report.type || "").toUpperCase() === "FOUND") {
            const claimButton = document.createElement("button");
            claimButton.type = "button";
            claimButton.className = "claim-button";
            claimButton.textContent = "Claim Item";

            claimButton.addEventListener("click", () => {
                submitClaim(report, claimButton);
            });

            content.appendChild(claimButton);
        }

        card.appendChild(content);
        reportsContainer.appendChild(card);
    });
}

async function submitClaim(report, claimButton) {
    const token = getToken();

    if (!token) {
        sessionStorage.setItem("redirectAfterLogin", "browse.html");
        window.location.href = "auth.html";
        return;
    }

    const userText = prompt(
        `Why are you claiming "${report.title}"?\n\n` +
        "Describe identifying details that can help verify it belongs to you:"
    );

    if (userText === null) {
        return;
    }

    const claimMessage = userText.trim();

    if (!claimMessage) {
        alert("Please enter a claim message.");
        return;
    }

    if (claimMessage.length > 1000) {
        alert("Your claim message must not exceed 1000 characters.");
        return;
    }

    claimButton.disabled = true;
    claimButton.textContent = "Submitting...";

    try {
        const response = await fetch(CLAIM_API_URL, {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                Authorization: `Bearer ${token}`
            },
            body: JSON.stringify({
                itemReportId: report.id,
                message: claimMessage
            })
        });

        const result = await response.json().catch(() => null);

        if (response.status === 401 || response.status === 403) {
            alert("Your session may have expired. Please log in again.");
            logout();
            return;
        }

        if (!response.ok) {
            throw new Error(
                result?.message ||
                result?.detail ||
                result?.error ||
                `Could not submit claim (${response.status}).`
            );
        }

        alert("Your claim was submitted successfully.");
    } catch (error) {
        console.error("Claim submission failed:", error);
        alert(error.message || "Could not connect to the server.");
    } finally {
        claimButton.disabled = false;
        claimButton.textContent = "Claim Item";
    }
}

searchBtn.addEventListener("click", renderReports);
searchInput.addEventListener("input", renderReports);
typeFilter.addEventListener("change", renderReports);
logoutBtn.addEventListener("click", logout);

loadReports();
