document.addEventListener("DOMContentLoaded", function () {
  const loginForm = document.getElementById("loginForm");
  if (loginForm) {
    loginForm.addEventListener("submit", async function (e) {
      e.preventDefault();
      const email = document.getElementById("email").value.trim();
      const senha = document.getElementById("senha").value.trim();
      const msg = document.getElementById("msg");
      if (email === "" || senha === "") { msg.innerHTML = "Preencha todos os campos."; msg.style.color = "red"; return; }
      try {
        const response = await fetch("http://localhost:8080/auth/login", {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify({ email, senha })
        });
        if (!response.ok) { msg.innerHTML = "E-mail ou senha inválidos."; msg.style.color = "red"; return; }
        const data = await response.json();
        localStorage.setItem("fincontrol_token", data.token);
        localStorage.setItem("fincontrol_user", email);
        msg.innerHTML = "Login realizado com sucesso!";
        msg.style.color = "green";
        setTimeout(() => window.location.href = "paginaInicial/dashboard.html", 1000);
      } catch (error) {
        msg.innerHTML = "Erro ao conectar com o servidor.";
        msg.style.color = "red";
      }
    });
  }
});
