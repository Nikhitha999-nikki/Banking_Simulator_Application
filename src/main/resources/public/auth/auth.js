


function login() {
    const username = document.getElementById("username").value;
    const password = document.getElementById("password").value;

    fetch("/login", {
        method: "POST",
        headers: {
            "Content-Type": "application/x-www-form-urlencoded"
        },
        body: `username=${username}&password=${password}`
    })
    .then(response => {
        if (!response.ok) {
            throw new Error("Invalid username or password");
        }
        return response.text();
    })
    .then(token => {
        localStorage.setItem("authToken", token);
        window.location.href = "/banking.html";
    })
    .catch(err => {
        document.getElementById("msg").innerText = err.message;
    });
}

function register() {
    const username = document.getElementById("username").value;
    const password = document.getElementById("password").value;

    fetch("/register", {
        method: "POST",
        headers: {
            "Content-Type": "application/x-www-form-urlencoded"
        },
        body: `username=${username}&password=${password}`
    })
    .then(res => res.text())
    .then(msg => {
        document.getElementById("msg").innerText = msg;
    });
}
