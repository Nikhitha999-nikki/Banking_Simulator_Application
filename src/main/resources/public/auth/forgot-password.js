function sendOTP() {

    let email = document.getElementById("email").value.trim();

    let msg = document.getElementById("msg");

    if(email === "")
    {
        msg.innerHTML = "Please enter your email";
        return;
    }

    // Email Pattern
    let emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

    if(!emailPattern.test(email))
    {
        msg.innerHTML = "Please enter a valid email address";
        return;
    }

    fetch("/forgot-password",{
        method:"POST",
        headers:{
            "Content-Type":"application/json"
        },
        body:JSON.stringify({
            email:email
        })
    })
    .then(response => response.json())
    .then(data=>{

        msg.innerHTML = data.message;

        if(data.success)
        {
            localStorage.setItem("email",email);
            window.location="verify-otp.html";
        }

    });
}
