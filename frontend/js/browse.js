const API_URL = "http://localhost:8080/api/reports";

const reportsContainer = document.getElementById("reportsContainer");
const searchInput = document.getElementById("searchInput");
const typeFilter = document.getElementById("typeFilter");
const searchBtn = document.getElementById("searchBtn");
const message = document.getElementById("message");
const logoutBtn = document.getElementById("logoutBtn");

let reports = [];

async function loadReports() {
    message.textContent = "Loading reports...";
    reportsContainer.replaceChildren();

    try {
        const token = sessionStorage.getItem("token");

        const response = await fetch(API_URL, {
            headers: token
                ? { Authorization: `Bearer ${token}` }
                : {}
        });

        if (!response.ok) {
            throw new Error(`Could not load reports (${response.status}).`);
        }

        reports = await response.json();
        renderReports();
    } catch (error) {
        message.textContent =
            "Could not load reports. Check that the backend is running and you are logged in.";
        console.error(error);
    }
}

function renderReports() {
    const query = searchInput.value.trim().toLowerCase();
    const selectedType = typeFilter.value;

    const filteredReports = reports.filter(report => {
        const matchesSearch =
            (report.title || "").toLowerCase().includes(query) ||
            (report.description || "").toLowerCase().includes(query) ||
            (report.location || "").toLowerCase().includes(query);

        const matchesType =
            selectedType === "ALL" || report.type === selectedType;

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
        type.className = `type-badge ${String(report.type || "").toLowerCase()}`;
        type.textContent = report.type || "ITEM";

        const title = document.createElement("h2");
        title.textContent = report.title || "Untitled item";

        const description = document.createElement("p");
        description.textContent = report.description || "No description provided.";

        const location = document.createElement("p");
        location.textContent = `Location: ${report.location || "Not specified"}`;

        const date = document.createElement("p");
        date.textContent = `Date: ${report.dateOccurred || "Not specified"}`;

        const reporter = document.createElement("p");
        reporter.textContent = `Reported by: ${report.reportedByName || "Campus user"}`;

        const status = document.createElement("p");
        status.textContent = `Status: ${report.status || "Unknown"}`;

        content.append(type, title, description, location, date, reporter, status);
        card.appendChild(content);
        reportsContainer.appendChild(card);
    });
}

searchBtn.addEventListener("click", renderReports);
searchInput.addEventListener("input", renderReports);
typeFilter.addEventListener("change", renderReports);

logoutBtn.addEventListener("click", () => {
    sessionStorage.removeItem("token");
    sessionStorage.removeItem("tokenType");
    sessionStorage.removeItem("user");
    sessionStorage.removeItem("redirectAfterLogin");

    window.location.href = "auth.html";
});

loadReports();