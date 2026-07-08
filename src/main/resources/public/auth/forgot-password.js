function sendOTP(){
    let email=document.getElementById("email").value;
    if(email=="")
    {
        document.getElementById("msg").innerHTML="Please enter your email";
        return;
    }
    fetch("/forgot-password",{
        method:"POST",
        headers:{
            "Content-Type":"application/json"
        }
        ,
        body:JSON.stringify(
        {
            email:email
        })
    })
    .then(response=>response.json())

    .then(data=>{

        document.getElementById("msg").innerHTML =
        data.message;

        if(data.success)
        {
            localStorage.setItem("email",email);

            window.location="verify-otp.html";
        }

    })


}

// function sendOTP() {
//     let email = document.getElementById("email").value;

//     console.log("Current URL:", window.location.href);
//     console.log("Fetch URL:", "/forgot-password");

//     fetch("/forgot-password", {
//         method: "POST",
//         headers: {
//             "Content-Type": "application/json"
//         },
//         body: JSON.stringify({
//             email: email
//         })
//     })
//     .then(async response => {
//         console.log("Status:", response.status);

//         const text = await response.text();
//         console.log("Response:", text);

//         return JSON.parse(text);
//     })
//     .then(data => {
//         console.log(data);
//     })
//     .catch(err => console.error(err));
// }