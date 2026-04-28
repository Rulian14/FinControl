document.addEventListener("DOMContentLoaded", function () {

    const cadastroForm = document.getElementById("cadastroForm");

    if (cadastroForm) {

        cadastroForm.addEventListener("submit", function (e) {
            e.preventDefault();

            let nome = document.getElementById("nome").value.trim();
            let email = document.getElementById("emailCad").value.trim();
            let senha = document.getElementById("senhaCad").value.trim();
            let confirmar = document.getElementById("confirmar").value.trim();
            let msg = document.getElementById("msgCadastro");

            if (nome === "" || email === "" || senha === "" || confirmar === "") {
                msg.innerHTML = "Preencha todos os campos.";
                msg.style.color = "red";
                return;
            }

            if (senha.length < 6) {
                msg.innerHTML = "A senha deve ter no mínimo 6 caracteres.";
                msg.style.color = "red";
                return;
            }

            if (senha !== confirmar) {
                msg.innerHTML = "As senhas não coincidem.";
                msg.style.color = "red";
                return;
            }

            msg.innerHTML = "Cadastro realizado com sucesso!";
            msg.style.color = "green";

            setTimeout(function () {
                window.location.href = "index.html";
            }, 1500);

        });

    }

});