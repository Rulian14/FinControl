document.addEventListener("DOMContentLoaded", async function () {
  verificarLogin();

  // 1. Busca dados do usuário via /me
  const usuario = await UsuarioAPI.me();
  const nome = usuario?.nome || localStorage.getItem("fincontrol_nome");
  const email = usuario?.email || localStorage.getItem("fincontrol_user") || "";
  const primeiroNome = nome ? nome.split(" ")[0] : email.split("@")[0];
  document.getElementById("saudacao").textContent = "Olá, " + primeiroNome + " 👋";
  if (usuario?.email) {
    document.getElementById("subtitulo-usuario").textContent = usuario.email;
  }

  // 2. Tenta usar /dashboard; se falhar, cai no fallback manual
  try {
    const dash = await DashboardAPI.dados();

    if (dash) {
      // Backend retornou dados do dashboard diretamente
      document.getElementById("card-saldo").textContent = formatarMoeda(dash.saldo);
      document.getElementById("card-receitas").textContent = formatarMoeda(dash.totalReceitas);
      document.getElementById("card-despesas").textContent = formatarMoeda(dash.totalDespesas);

      const tbody = document.getElementById("tabela-lancamentos");
      const lancamentos = dash.ultimosLancamentos;

      if (lancamentos && lancamentos.length > 0) {
        tbody.innerHTML = lancamentos.map(item => `
          <tr>
            <td>${item.descricao}</td>
            <td><span class="badge badge-${item.tipo || (item.valor >= 0 ? 'receita' : 'despesa')}">${item.categoria || "-"}</span></td>
            <td>${new Date(item.data).toLocaleDateString("pt-BR")}</td>
            <td class="${(item.tipo === 'receita' || item.valor >= 0) ? 'positivo' : 'negativo'}">${(item.tipo === 'receita' || item.valor >= 0) ? '+' : '-'} ${formatarMoeda(Math.abs(item.valor))}</td>
          </tr>`).join("");
      } else {
        tbody.innerHTML = '<tr><td colspan="4" style="text-align:center;color:#94a3b8;padding:30px">Nenhum lançamento ainda.</td></tr>';
      }
    } else {
      // Fallback: busca receitas e despesas individualmente
      await carregarFallback();
    }
  } catch(e) {
    console.error(e);
    await carregarFallback();
  }

  async function carregarFallback() {
    const [receitas, despesas] = await Promise.all([ReceitasAPI.listar(), DespesasAPI.listar()]);
    const totalReceitas = receitas.reduce((a, r) => a + r.valor, 0);
    const totalDespesas = despesas.reduce((a, d) => a + d.valor, 0);
    document.getElementById("card-saldo").textContent = formatarMoeda(totalReceitas - totalDespesas);
    document.getElementById("card-receitas").textContent = formatarMoeda(totalReceitas);
    document.getElementById("card-despesas").textContent = formatarMoeda(totalDespesas);

    const todos = [
      ...receitas.map(r => ({ ...r, tipo: "receita" })),
      ...despesas.map(d => ({ ...d, tipo: "despesa" }))
    ].sort((a, b) => new Date(b.data) - new Date(a.data)).slice(0, 5);

    const tbody = document.getElementById("tabela-lancamentos");
    tbody.innerHTML = todos.length > 0 ? todos.map(item => `
      <tr>
        <td>${item.descricao}</td>
        <td><span class="badge badge-${item.tipo}">${item.categoria}</span></td>
        <td>${new Date(item.data).toLocaleDateString("pt-BR")}</td>
        <td class="${item.tipo === 'receita' ? 'positivo' : 'negativo'}">${item.tipo === 'receita' ? '+' : '-'} ${formatarMoeda(item.valor)}</td>
      </tr>`).join("") : '<tr><td colspan="4" style="text-align:center;color:#94a3b8;padding:30px">Nenhum lançamento ainda.</td></tr>';
  }
});
