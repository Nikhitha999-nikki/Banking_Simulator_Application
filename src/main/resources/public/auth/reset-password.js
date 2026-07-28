function resetPassword() {

    let newPassword = document.getElementById("newPassword").value;
    let confirmPassword = document.getElementById("confirmPassword").value;

    if(newPassword==""){
        document.getElementById("msg").innerHTML = "Enter new password";
        return;
    }

    if(newPassword != confirmPassword){
        document.getElementById("msg").innerHTML = "Passwords do not match";
        return;
    }

    fetch("/reset-password", {

        method:"POST",

        headers:{
            "Content-Type":"application/json"
        },

        body:JSON.stringify({

            email: localStorage.getItem("email"),
            password: newPassword

        })

    })

    .then(res => res.json())

    .then(data => {

        document.getElementById("msg").innerHTML = data.message;

        if(data.success){

            localStorage.removeItem("email");

            alert("Password Reset Successfully");

            window.location = "login.html";

        }

    });
    

}