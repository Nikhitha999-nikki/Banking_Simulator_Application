function verifyOTP() {
    let otp=document.getElementById("otp").value;
    if (otp == "") {
        document.getElementById("msg").innerHTML ="Please enter OTP";
        return;
    }
    fetch("/verify-otp",{
        method:"POST",
        headers: {
            "Content-Type":"application/json"
        },
        body:JSON.stringify({
            email: localStorage.getItem("email"),
            otp: otp
        })
    })
    .then(res => res.json())
    .then(data => {
        document.getElementById("msg").innerHTML =
        data.message;
        if (data.success) {
            window.location =
            "reset-password.html";
        }
    });
}