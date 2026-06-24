// dados.js - Comunicação com a API

const API = "http://localhost:8080";

const CATEGORIAS_RECEITA = {
  "Salário": 1, "Freelance": 2, "Investimentos": 3,
  "Venda de Itens": 4, "Outros Ganhos": 5
};

const CATEGORIAS_DESPESA = {
  "Aluguel": 6, "Condomínio": 7, "Energia Elétrica": 8, "Água": 9,
  "Gás": 10, "Internet": 11, "Telefone": 12, "Mercado": 13,
  "Transporte": 14, "Carro": 15, "Cartão de Crédito": 16,
  "Educação": 17, "Saúde": 18, "Empréstimos": 19, "Lazer": 20,
  "Outras Despesas": 21
};

const MESES = ["Janeiro","Fevereiro","Março","Abril","Maio","Junho",
  "Julho","Agosto","Setembro","Outubro","Novembro","Dezembro"];

function getToken() { return localStorage.getItem("fincontrol_token"); }

function headers() {
  return {
    "Content-Type": "application/json",
    "Authorization": "Bearer " + getToken()
  };
}

function formatarDataAPI(dataStr) {
  return dataStr.includes("T") ? dataStr : dataStr + "T00:00:00";
}

function verificarLogin() {
  if (!getToken()) window.location.href = "../index.html";
}

function logout() {
  localStorage.removeItem("fincontrol_token");
  localStorage.removeItem("fincontrol_user");
  localStorage.removeItem("fincontrol_nome");
  window.location.href = "../index.html";
}

function formatarMoeda(valor) {
  return Number(valor).toLocaleString("pt-BR", { style: "currency", currency: "BRL" });
}

function nomeCategoriaById(id) {
  const todas = { ...CATEGORIAS_RECEITA, ...CATEGORIAS_DESPESA };
  return Object.keys(todas).find(k => todas[k] === id) || "Outros";
}

function badgeStatus(status) {
  const map = {
    "PAGA":      { label: "✅ Paga",      bg: "#dcfce7", color: "#166534" },
    "PENDENTE":  { label: "⏳ Pendente",  bg: "#fef9c3", color: "#854d0e" },
    "AGENDADA":  { label: "📅 Agendada",  bg: "#e0f2fe", color: "#075985" },
    "SUGESTAO":  { label: "💡 Sugestão",  bg: "#f3e8ff", color: "#6b21a8" }
  };
  const s = map[status] || map["PENDENTE"];
  return `<span style="display:inline-block;padding:2px 9px;border-radius:12px;font-size:11px;font-weight:700;background:${s.bg};color:${s.color}">${s.label}</span>`;
}

// ---- USUÁRIO ----
const UsuarioAPI = {
  async me() {
    try {
      const res = await fetch(API + "/auth/me", { headers: headers() });
      if (!res.ok) return null;
      const data = await res.json();
      if (data.nome) localStorage.setItem("fincontrol_nome", data.nome);
      if (data.email) localStorage.setItem("fincontrol_user", data.email);
      return data;
    } catch { return null; }
  },
  async atualizar(dados) {
    try {
      const res = await fetch(API + "/User", {
        method: "POST", headers: headers(),
        body: JSON.stringify({
          nome: dados.nome || "", senhaAtual: dados.senhaAtual || "",
          senhaNova: dados.senhaNova || "", email: dados.email || ""
        })
      });
      if (!res.ok) return { ok: false, status: res.status };
      const data = await res.json();
      if (data.nome) localStorage.setItem("fincontrol_nome", data.nome);
      if (data.email) localStorage.setItem("fincontrol_user", data.email);
      return { ok: true, data };
    } catch { return { ok: false }; }
  }
};

// ---- DASHBOARD (com dados brutos para relatório) ----
const DashboardAPI = {
  async dados(ano, mes) {
    try {
      const agora = new Date();
      const a = ano || agora.getFullYear();
      const m = mes || (agora.getMonth() + 1);
      const res = await fetch(`${API}/dashboard?ano=${a}&mes=${m}`, { headers: headers() });
      if (!res.ok) return null;
      const data = await res.json();
      return {
        saldo: data.saldo ?? 0,
        totalReceitas: data.receitaTotal ?? 0,
        totalDespesas: data.despesaTotal ?? 0,
        ultimosLancamentos: data.ultLancamento ?? [],
        despesasPorCategoria: data.despesasPorCategoria ?? [],
        receitasPorCategoria: data.receitasPorCategoria ?? [],
        ano: data.ano ?? a,
        mes: data.mes ?? m
      };
    } catch { return null; }
  }
};

// ---- RECEITAS ----
const ReceitasAPI = {
  async listar() {
    try {
      const res = await fetch(API + "/receitas", { headers: headers() });
      if (!res.ok) return [];
      return (await res.json()).map(r => ({ ...r, categoria: nomeCategoriaById(r.idCategoria) }));
    } catch { return []; }
  },
  async criar(dados) {
    try {
      const res = await fetch(API + "/receitas", {
        method: "POST", headers: headers(),
        body: JSON.stringify({
          descricao: dados.descricao, valor: dados.valor,
          data: formatarDataAPI(dados.data),
          idCategoria: CATEGORIAS_RECEITA[dados.categoria] || 1,
          recorrencia: dados.recorrencia || "FIXA"
        })
      });
      return res.ok;
    } catch { return false; }
  },
  async editar(id, dados) {
    try {
      const res = await fetch(API + "/receitas/" + id, {
        method: "PUT", headers: headers(),
        body: JSON.stringify({
          descricao: dados.descricao, valor: dados.valor,
          data: formatarDataAPI(dados.data),
          idCategoria: CATEGORIAS_RECEITA[dados.categoria] || 1,
          recorrencia: dados.recorrencia || "FIXA"
        })
      });
      return res.ok;
    } catch { return false; }
  },
  async excluir(id) {
    try {
      const res = await fetch(API + "/receitas/" + id, { method: "DELETE", headers: headers() });
      return res.ok || res.status === 204;
    } catch { return false; }
  }
};

// ---- DESPESAS ----
const DespesasAPI = {
  async listar() {
    try {
      const res = await fetch(API + "/despesas", { headers: headers() });
      if (!res.ok) return [];
      return (await res.json()).map(d => ({ ...d, categoria: nomeCategoriaById(d.idCategoria) }));
    } catch { return []; }
  },
  async sugestoes() {
    try {
      const res = await fetch(API + "/despesas/sugestao", { headers: headers() });
      if (!res.ok) return [];
      const data = await res.json();
      return data.map(s => ({
        ...s,
        categoria: s.categoriaNome || nomeCategoriaById(s.categoriaId),
        idCategoria: s.categoriaId,
        valor: s.valorSugerido,
        status: "SUGESTAO"
      }));
    } catch { return []; }
  },
  async criar(dados) {
    try {
      const res = await fetch(API + "/despesas", {
        method: "POST", headers: headers(),
        body: JSON.stringify({
          descricao: dados.descricao, valor: dados.valor,
          data: formatarDataAPI(dados.data),
          idCategoria: dados.idCategoria || CATEGORIAS_DESPESA[dados.categoria] || 21,
          recorrencia: dados.recorrencia || "FIXA",
          status: dados.status || "PAGA"
        })
      });
      return res.ok;
    } catch { return false; }
  },
  async editar(id, dados) {
    try {
      const res = await fetch(API + "/despesas/" + id, {
        method: "PUT", headers: headers(),
        body: JSON.stringify({
          descricao: dados.descricao, valor: dados.valor,
          data: formatarDataAPI(dados.data),
          idCategoria: dados.idCategoria || CATEGORIAS_DESPESA[dados.categoria] || 21,
          recorrencia: dados.recorrencia || "FIXA",
          status: dados.status || "PAGA"
        })
      });
      return res.ok;
    } catch { return false; }
  },
  async excluir(id) {
    try {
      const res = await fetch(API + "/despesas/" + id, { method: "DELETE", headers: headers() });
      return res.ok || res.status === 204;
    } catch { return false; }
  }
};
