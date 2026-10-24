const API_BASE = "http://localhost:8080/api";

const claimsContainer = document.getElementById("claimsContainer");
const message = document.getElementById("message");
const refreshBtn = document.getElementById("refreshBtn");
const logoutBtn = document.getElementById("logoutBtn");

function getToken() {
    return sessionStorage.getItem("token");
}

function getCurrentUserId() {
    try {
        const user = JSON.parse(sessionStorage.getItem("user") || "null");
        return user?.id ?? user?.userId ?? null;
    } catch {
        return null;
    }
}

function logout() {
    sessionStorage.removeItem("token");
    sessionStorage.removeItem("tokenType");
    sessionStorage.removeItem("user");
    sessionStorage.removeItem("redirectAfterLogin");
    window.location.href = "auth.html";
}

function addTextElement(parent, tag, text, className = "") {
    const element = document.createElement(tag);
    element.textContent = text;

    if (className) {
        element.className = className;
    }

    parent.appendChild(element);
    return element;
}

async function apiFetch(url, options = {}) {
    const token = getToken();

    const response = await fetch(url, {
        ...options,
        headers: {
            ...(options.headers || {}),
            Authorization: `Bearer ${token}`
        }
    });

    if (response.status === 401 || response.status === 403) {
        throw new Error("AUTH");
    }

    return response;
}

async function loadClaims() {
    if (!getToken()) {
        sessionStorage.setItem("redirectAfterLogin", "claims.html");
        window.location.href = "auth.html";
        return;
    }

    message.textContent = "Loading incoming claims...";
    claimsContainer.replaceChildren();
    refreshBtn.disabled = true;

    try {
        const [claimsResponse, reportsResponse] = await Promise.all([
            apiFetch(`${API_BASE}/claims`),
            apiFetch(`${API_BASE}/reports`)
        ]);

        if (!claimsResponse.ok || !reportsResponse.ok) {
            throw new Error("Could not load claims or reports.");
        }

        const [claims, reports] = await Promise.all([
            claimsResponse.json(),
            reportsResponse.json()
        ]);

        const currentUserId = getCurrentUserId();

        if (currentUserId == null) {
            message.textContent =
                "Your user details are missing from this session. Log out and log in again.";
            return;
        }

        const ownFoundReportIds = new Set(
            reports
                .filter(report =>
                    report.type === "FOUND" &&
                    String(report.reportedById) === String(currentUserId)
                )
                .map(report => String(report.id))
        );

        const incomingClaims = claims.filter(claim =>
            ownFoundReportIds.has(String(claim.itemReportId))
        );

        renderClaims(incomingClaims);

    } catch (error) {
        console.error(error);

        if (error.message === "AUTH") {
            message.textContent =
                "Your session has expired. Please log in again.";
            setTimeout(logout, 1200);
        } else {
            message.textContent =
                "Could not load incoming claims. Check the backend and try refreshing.";
        }
    } finally {
        refreshBtn.disabled = false;
    }
}

function renderClaims(claims) {
    claimsContainer.replaceChildren();

    if (claims.length === 0) {
        message.textContent =
            "You don't have any claims on your found items yet.";
        return;
    }

    message.textContent = `${claims.length} incoming claim(s).`;

    claims.forEach(claim => {
        const card = document.createElement("article");
        card.className = "claim-card";

        addTextElement(card, "h3", claim.itemTitle || "Reported item");

        addTextElement(
            card,
            "p",
            `Claimant: ${claim.claimantName || "Campus user"}`
        );

        addTextElement(
            card,
            "p",
            `Submitted: ${claim.createdAt
                ? new Date(claim.createdAt).toLocaleString()
                : "Date unavailable"}`
        );

        addTextElement(card, "p", "Claim message:");
        addTextElement(
            card,
            "p",
            claim.message || "No message provided.",
            "claim-message"
        );

        const status = String(claim.status || "UNKNOWN").toLowerCase();
        addTextElement(
            card,
            "span",
            String(claim.status || "UNKNOWN"),
            `claim-status ${status}`
        );

        if (claim.status === "PENDING") {
            const actions = document.createElement("div");
            actions.className = "claim-actions";

            const approveBtn = document.createElement("button");
            approveBtn.type = "button";
            approveBtn.className = "approve-button";
            approveBtn.textContent = "Approve";

            const rejectBtn = document.createElement("button");
            rejectBtn.type = "button";
            rejectBtn.className = "reject-button";
            rejectBtn.textContent = "Reject";

            approveBtn.addEventListener("click", () =>
                updateClaimStatus(claim, "APPROVED", approveBtn, rejectBtn)
            );

            rejectBtn.addEventListener("click", () =>
                updateClaimStatus(claim, "REJECTED", approveBtn, rejectBtn)
            );

            actions.append(approveBtn, rejectBtn);
            card.appendChild(actions);
        }

        claimsContainer.appendChild(card);
    });
}

async function updateClaimStatus(claim, status, approveBtn, rejectBtn) {
    const action = status === "APPROVED" ? "approve" : "reject";

    if (!confirm(`Are you sure you want to ${action} this claim?`)) {
        return;
    }

    approveBtn.disabled = true;
    rejectBtn.disabled = true;

    try {
        const response = await apiFetch(
            `${API_BASE}/claims/${claim.id}/status?status=${status}`,
            { method: "PATCH" }
        );

        const result = await response.json().catch(() => null);

        if (!response.ok) {
            throw new Error(
                result?.message ||
                result?.detail ||
                `Could not update claim (${response.status}).`
            );
        }

        message.textContent = `Claim ${action}d successfully.`;
        await loadClaims();

    } catch (error) {
        console.error(error);

        if (error.message === "AUTH") {
            alert("Your session has expired. Please log in again.");
            logout();
        } else {
            alert(error.message || "Could not update this claim.");
        }

        approveBtn.disabled = false;
        rejectBtn.disabled = false;
    }
}

refreshBtn.addEventListener("click", loadClaims);
logoutBtn.addEventListener("click", logout);

loadClaims();