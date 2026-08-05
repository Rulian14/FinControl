// cadastro.js

document.addEventListener("DOMContentLoaded", function () {
  const cadastroForm = document.getElementById("cadastroForm");

  if (cadastroForm) {
    cadastroForm.addEventListener("submit", async function (e) {
      e.preventDefault();

      const nome = document.getElementById("nome").value.trim();
      const email = document.getElementById("emailCad").value.trim();
      const senha = document.getElementById("senhaCad").value.trim();
      const confirmar = document.getElementById("confirmar").value.trim();
      const msg = document.getElementById("msgCadastro");

      if (nome === "" || email === "" || senha === "" || confirmar === "") {
        msg.innerHTML = "Preencha todos os campos.";
        msg.style.color = "red";
        return;
      }

      const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
      if (!emailRegex.test(email)) {
        msg.innerHTML = "Digite um e-mail válido.";
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

      try {
        // 1. Registra o usuário
        const resRegister = await fetch("http://localhost:8080/auth/register", {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify({ nome, email, senha })
        });

        if (!resRegister.ok) {
          msg.innerHTML = "Erro ao cadastrar. E-mail já pode estar em uso.";
          msg.style.color = "red";
          return;
        }

        // 2. Faz login automático para obter o token
        const resLogin = await fetch("http://localhost:8080/auth/login", {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify({ email, senha })
        });

        if (!resLogin.ok) {
          msg.innerHTML = "Cadastro feito! Faça login para continuar.";
          msg.style.color = "green";
          setTimeout(() => window.location.href = "index.html", 1500);
          return;
        }

        const data = await resLogin.json();

        // 3. Salva token e vai direto pro dashboard
        localStorage.setItem("fincontrol_token", data.token);
        localStorage.setItem("fincontrol_user", email);
        localStorage.setItem("fincontrol_nome", nome);

        msg.innerHTML = "Cadastro realizado com sucesso!";
        msg.style.color = "green";

        setTimeout(() => {
          window.location.href = "paginaInicial/dashboard.html";
        }, 1000);

      } catch (error) {
        msg.innerHTML = "Erro ao conectar com o servidor.";
        msg.style.color = "red";
      }
    });
  }
});
