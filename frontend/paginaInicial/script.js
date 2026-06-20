document.addEventListener("DOMContentLoaded", function(){

    const loginForm = document.getElementById("loginForm");

    if(loginForm){

        loginForm.addEventListener("submit", function(e){
            e.preventDefault();

            let email = document.getElementById("email").value.trim();
            let senha = document.getElementById("senha").value.trim();
            let msg = document.getElementById("msg");

            if(email === "" || senha === ""){
                msg.innerHTML = "Preencha todos os campos.";
                msg.style.color = "red";
                return;
            }

            msg.innerHTML = "Login realizado com sucesso!";
            msg.style.color = "green";

            setTimeout(function(){
                window.location.href = "dashboard.html";
            }, 1000);

        });

    }

});