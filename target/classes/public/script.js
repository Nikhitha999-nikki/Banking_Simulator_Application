const BASE_URL = "";

/* ---------------- UI HELPERS ---------------- */
function showSection(id) {
    document.querySelectorAll(".section")
        .forEach(section => section.style.display = "none");
    document.getElementById(id).style.display = "block";
    
    // Reset View All Accounts section to show placeholder
    if (id === 'viewall') {
        const resultDiv = document.getElementById("viewall-result");
        resultDiv.innerHTML = '<div class="table-placeholder">Click \'Load Accounts\' to view all account details.</div>';
    }
}

/* ---------------- AUTH ---------------- */
function getToken() {
    return localStorage.getItem("authToken");
}

function authHeaders() {
    return {
        "Authorization": getToken(),
        "Content-Type": "application/json"
    };
}

function handleUnauthorized(response) {
    if (response.status === 401) {
        alert("Session expired. Please login again.");
        window.location.href = "/auth/login.html";
        throw new Error("Unauthorized");
    }
    return response;
}

/* ---------------- CREATE ACCOUNT ---------------- */
function createAccount() {
    const data = {
        name: document.getElementById("c-name").value,
        email: document.getElementById("c-email").value,
        balance: document.getElementById("c-balance").value
    };

    fetch(BASE_URL + "/accounts/create", {
        method: "POST",
        headers: authHeaders(),
        body: JSON.stringify(data)
    })
    .then(handleUnauthorized)
    .then(res => res.json())
    .then(result => {
        const resultDiv = document.getElementById("create-result");
        resultDiv.innerText = "Account created successfully. Account No: " + result.accountNumber;
        resultDiv.style.color = "#27ae60";
    })
    .catch(err => console.error(err));
}

/* ---------------- DEPOSIT ---------------- */
function depositMoney() {
    const data = {
        accNo: document.getElementById("d-acc").value,
        amount: parseFloat(document.getElementById("d-amount").value)
    };

    fetch(BASE_URL + "/accounts/deposit", {
        method: "POST",
        headers: authHeaders(),
        body: JSON.stringify(data)
    })
    .then(handleUnauthorized)
    .then(res => {
        if (!res.ok) {
            return res.json().then(error => Promise.reject(error));
        }
        return res.json();
    })
    .then(result => {
        const resultDiv = document.getElementById("deposit-result");
        resultDiv.innerText = result.message;
        resultDiv.style.color = "#27ae60";
    })
    .catch(err => {
        const resultDiv = document.getElementById("deposit-result");
        resultDiv.innerText = err.message || "Deposit failed";
        resultDiv.style.color = "#ff4757";
    });
}

/* ---------------- WITHDRAW ---------------- */
function withdrawMoney() {
    const data = {
        accNo: document.getElementById("w-acc").value,
        amount: parseFloat(document.getElementById("w-amount").value)
    };

    fetch(BASE_URL + "/accounts/withdraw", {
        method: "POST",
        headers: authHeaders(),
        body: JSON.stringify(data)
    })
    .then(handleUnauthorized)
    .then(async response => {
        const resultDiv = document.getElementById("withdraw-result");
        const text = await response.text();
        try {
            const data = JSON.parse(text || "{}");
            resultDiv.innerText = data.message || (response.ok ? "Withdraw successful" : "Withdraw failed");
            resultDiv.style.color = response.ok ? "#27ae60" : "#ff4757";
        } catch (e) {
            resultDiv.innerText = "Insufficient balance or session expired. Please try again.";
            resultDiv.style.color = "#ff4757";
        }
    })
    .catch(err => {
        const resultDiv = document.getElementById("withdraw-result");
        resultDiv.innerText = err.message || "Withdraw failed";
        resultDiv.style.color = "#ff4757";
    });
}

/* ---------------- TRANSFER ---------------- */
function transferMoney() {
    const data = {
        fromAcc: document.getElementById("t-from-acc").value,
        toAcc: document.getElementById("t-to-acc").value,
        amount: parseFloat(document.getElementById("t-amount").value)
    };

    fetch(BASE_URL + "/accounts/transfer", {
        method: "POST",
        headers: authHeaders(),
        body: JSON.stringify(data)
    })
    .then(handleUnauthorized)
    .then(async response => {
        const resultDiv = document.getElementById("transfer-result");
        const text = await response.text();
        try {
            const data = JSON.parse(text || "{}");
            resultDiv.innerText = data.message || (response.ok ? "Transfer successful" : "Transfer failed");
            resultDiv.style.color = response.ok ? "#27ae60" : "#ff4757";
        } catch (e) {
            resultDiv.innerText = "Insufficient balance or session expired. Please try again.";
            resultDiv.style.color = "#ff4757";
        }
    })
    .catch(err => {
        const resultDiv = document.getElementById("transfer-result");
        resultDiv.innerText = err.message || "Transfer failed";
        resultDiv.style.color = "#ff4757";
    });
}

/* ---------------- VIEW ACCOUNT ---------------- */
function viewAccount() {
    const accNo = document.getElementById("v-acc").value;
    const resultDiv = document.getElementById("view-result");

    if (!accNo) {
        resultDiv.innerHTML = '<p style="text-align: center; color: #ff4757; font-weight: bold;">Please enter an account number</p>';
        return;
    }

    fetch(BASE_URL + "/accounts/" + accNo, {
        method: "GET",
        headers: { "Authorization": getToken() }
    })
    .then(res => {
        if (res.status === 401) {
            window.location.href = "/auth/login.html";
            throw new Error("Unauthorized");
        }
        if (res.status === 404) {
            throw new Error("Account not found");
        }
        if (!res.ok) throw new Error("Server error");
        return res.json();
    })
    .then(account => {
        const output = `
            <div class="account-detail-card">
                <div class="detail-row">
                    <div class="detail-label">Account Number</div>
                    <div class="detail-value">${account.accountNumber}</div>
                </div>
                <div class="detail-row">
                    <div class="detail-label">Holder Name</div>
                    <div class="detail-value">${account.holderName}</div>
                </div>
                <div class="detail-row">
                    <div class="detail-label">Email</div>
                    <div class="detail-value">${account.email}</div>
                </div>
                <div class="detail-row highlight">
                    <div class="detail-label">Account Balance</div>
                    <div class="detail-value balance">₹${account.balance.toLocaleString()}</div>
                </div>
            </div>
        `;
        resultDiv.innerHTML = output;
        document.getElementById("v-acc").value = "";
    })
    .catch(err => {
        resultDiv.innerHTML = `<p style="text-align: center; color: #ff4757; font-weight: bold;">❌ ${err.message}</p>`;
    });
}

/* ---------------- VIEW ALL ACCOUNTS ---------------- */
function viewAllAccounts() {
    const resultDiv = document.getElementById("viewall-result");
    
    fetch(BASE_URL + "/accounts/all", {
        method: "GET",
        headers: { "Authorization": getToken() }
    })
    .then(handleUnauthorized)
    .then(res => res.json())
    .then(accounts => {
        if (!accounts || accounts.length === 0) {
            resultDiv.innerHTML = '<div class="table-placeholder">No accounts found. Create an account to get started.</div>';
            return;
        }
        let output = `
            <table class="accounts-table">
                <thead>
                    <tr>
                        <th>Account No</th>
                        <th>Name</th>
                        <th>Email</th>
                        <th>Balance</th>
                    </tr>
                </thead>
                <tbody>
        `;

        accounts.forEach((acc, index) => {
            output += `
                <tr class="${index % 2 === 0 ? 'even' : 'odd'}">
                    <td data-label="Account No"><strong>${acc.accountNumber}</strong></td>
                    <td data-label="Name">${acc.holderName}</td>
                    <td data-label="Email">${acc.email}</td>
                    <td data-label="Balance"><strong>₹${acc.balance.toLocaleString()}</strong></td>
                </tr>
            `;
        });

        output += "</tbody></table>";
        resultDiv.innerHTML = output;
    })
    .catch(err => console.error(err));
}
/* ---------------- VIEW TRANSACTION HISTORY ---------------- */
function viewTransactions() {

    const accNo = document.getElementById("tx-acc").value.trim();
    const resultDiv = document.getElementById("transactions-result");

    if (!accNo) {
        resultDiv.innerHTML =
            '<div class="table-placeholder">Please enter an account number.</div>';
        return;
    }

    resultDiv.innerHTML =
        '<div class="table-placeholder">Loading transactions...</div>';

    fetch(BASE_URL + "/accounts/" + accNo + "/transactions", {
        method: "GET",
        headers: {
            "Authorization": getToken()
        }
    })
    .then(handleUnauthorized)
    .then(async response => {

        const data = await response.json();

        if (!response.ok) {
            throw new Error(data.error || "Failed to fetch transactions");
        }

        return data;
    })
    .then(transactions => {

        if (!transactions || transactions.length === 0) {
            resultDiv.innerHTML =
                '<div class="table-placeholder">No transactions found for this account.</div>';
            return;
        }

        let output = `
            <table class="accounts-table">
                <thead>
                    <tr>
                        <th>Date & Time</th>
                        <th>Type</th>
                        <th>Amount</th>
                        <th>Target Account</th>
                    </tr>
                </thead>
                <tbody>
        `;

        transactions.forEach((transaction, index) => {

            const date = transaction.timestamp
                ? new Date(transaction.timestamp).toLocaleString()
                : "-";

            const amount = Number(transaction.amount).toLocaleString(
                "en-IN",
                {
                    minimumFractionDigits: 2,
                    maximumFractionDigits: 2
                }
            );

            output += `
                <tr class="${index % 2 === 0 ? 'even' : 'odd'}">
                    <td data-label="Date & Time">${date}</td>
                    <td data-label="Type"><strong>${transaction.type}</strong></td>
                    <td data-label="Amount"><strong>₹${amount}</strong></td>
                    <td data-label="Target Account">
                        ${transaction.targetAccount || "-"}
                    </td>
                </tr>
            `;
        });

        output += `
                </tbody>
            </table>
        `;

        resultDiv.innerHTML = output;

        // Clear account number after successful search
        document.getElementById("tx-acc").value = "";
    })
    .catch(err => {

        resultDiv.innerHTML =
            `<div class="table-placeholder" style="color: #ff4757;">
                ❌ ${err.message}
            </div>`;
    });
}
function downloadTransactionPDF() {

    const accNo = document.getElementById("tx-acc").value.trim();

    if (!accNo) {
        alert("Please enter an account number.");
        return;
    }

    fetch(BASE_URL + "/accounts/" + accNo + "/transactions/pdf", {
        method: "GET",
        headers: {
            "Authorization": getToken()
        }
    })
    .then(handleUnauthorized)
    .then(async response => {

        if (!response.ok) {
            let errorMessage = "Failed to download transaction PDF.";

            try {
                const data = await response.json();
                errorMessage = data.error || errorMessage;
            } catch (e) {
                // Ignore JSON parsing error
            }

            throw new Error(errorMessage);
        }

        return response.blob();
    })
    .then(blob => {

        const url = window.URL.createObjectURL(blob);

        const link = document.createElement("a");
        link.href = url;
        link.download = "Transaction_Statement_" + accNo + ".pdf";

        document.body.appendChild(link);
        link.click();

        link.remove();
        window.URL.revokeObjectURL(url);
    })
    .catch(err => {
        alert("❌ " + err.message);
    });
}