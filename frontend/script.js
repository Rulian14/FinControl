  // ---- LOGIN ----
  const loginForm = document.getElementById("loginForm");
  if (loginForm) {
    loginForm.addEventListener("submit", async function (e) {
      e.preventDefault();
      const email = document.getElementById("email").value.trim();
      const senha = document.getElementById("senha").value.trim();
      const msg = document.getElementById("msg");
      if (!email || !senha) { msg.innerHTML = "Preencha todos os campos."; msg.style.color = "red"; return; }
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
        if (data.userResponseDTO?.nome) localStorage.setItem("fincontrol_nome", data.userResponseDTO.nome);
        msg.innerHTML = "Login realizado com sucesso!";
        msg.style.color = "green";
        setTimeout(() => window.location.href = "paginaInicial/dashboard.html", 1000);
      } catch {
        msg.innerHTML = "Erro ao conectar com o servidor.";
        msg.style.color = "red";
      }
    });
  }

  // ---- CADASTRO ----
  const cadastroForm = document.getElementById("cadastroForm");
  if (cadastroForm) {
    cadastroForm.addEventListener("submit", async function (e) {
      e.preventDefault();
      const nome = document.getElementById("nome").value.trim();
      const email = document.getElementById("emailCad").value.trim();
      const senha = document.getElementById("senhaCad").value.trim();
      const confirmar = document.getElementById("confirmar").value.trim();
      const msg = document.getElementById("msgCadastro");

      if (!nome || !email || !senha || !confirmar) {
        msg.innerHTML = "Preencha todos os campos."; msg.style.color = "red"; return;
      }
      if (senha !== confirmar) {
        msg.innerHTML = "As senhas não coincidem."; msg.style.color = "red"; return;
      }
      if (senha.length < 10 || senha.length > 15) {
        msg.innerHTML = "A senha deve ter entre 10 e 15 caracteres."; msg.style.color = "red"; return;
      }

      try {
        const response = await fetch("http://localhost:8080/auth/register", {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify({ nome, email, senha })
        });

        if (!response.ok) {
          const err = await response.json().catch(() => null);
          msg.innerHTML = err?.detail || "Erro ao cadastrar. Verifique os dados.";
          msg.style.color = "red"; return;
        }

        const data = await response.json();
        localStorage.setItem("fincontrol_token", data.token);
        localStorage.setItem("fincontrol_user", email);
        if (data.userResponseDTO?.nome) localStorage.setItem("fincontrol_nome", data.userResponseDTO.nome);
        msg.innerHTML = "Conta criada com sucesso!";
        msg.style.color = "green";
        setTimeout(() => window.location.href = "paginaInicial/dashboard.html", 1000);
      } catch {
        msg.innerHTML = "Erro ao conectar com o servidor.";
        msg.style.color = "red";
      }
    });
  }