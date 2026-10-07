function login() {
    const username = document.getElementById("username").value;
    const password = document.getElementById("password").value;

    fetch("/login", {
        method: "POST",
        headers: {
            "Content-Type": "application/x-www-form-urlencoded"
        },
        body: new URLSearchParams({
    username: username,
    password: password
})
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
        const email = document.getElementById("email").value;
        const password = document.getElementById("password").value;

        fetch("/register", {
            method: "POST",
            headers: {
                "Content-Type": "application/x-www-form-urlencoded"
            },
            body: `username=${username}&email=${email}&password=${password}`
        })
        .then(res => {
            return res.text().then(msg => ({
                status: res.status,
                message: msg
            }));
        })
        .then(data => {
            const msg = document.getElementById("msg");

            msg.innerText = data.message;

            if (data.status === 400) {
                msg.style.color = "red";
            } else {
                msg.style.color = "green";
            }
        });
    }

    function handleCredentialResponse(response) {

        fetch("/google-login", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({
                credential: response.credential
            })
        })
        .then(res => res.json())
        .then(data => {

            if(data.success){

                localStorage.setItem("authToken", data.token);

                window.location.href = "/banking.html";

            }else{

                document.getElementById("msg").innerHTML = data.message;

            }

        });

    }
