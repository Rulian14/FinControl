// dados.js - Comunicação com a API

const API = "http://localhost:8080";

// IDs conforme banco do backend
const CATEGORIAS_RECEITA = {
  "Salário": 1,
  "Freelance": 2,
  "Investimentos": 3,
  "Venda de Itens": 4,
  "Outros Ganhos": 5
};

const CATEGORIAS_DESPESA = {
  "Aluguel": 6,
  "Condomínio": 7,
  "Energia Elétrica": 8,
  "Água": 9,
  "Gás": 10,
  "Internet": 11,
  "Telefone": 12,
  "Mercado": 13,
  "Transporte": 14,
  "Carro": 15,
  "Cartão de Crédito": 16,
  "Educação": 17,
  "Saúde": 18,
  "Empréstimos": 19,
  "Lazer": 20,
  "Outras Despesas": 21
};

function getToken() {
  return localStorage.getItem("fincontrol_token");
}

function headers() {
  return {
    "Content-Type": "application/json",
    "Authorization": "Bearer " + getToken()
  };
}

function formatarDataAPI(dataStr) {
  // Se já vier com T (ISO completo), usa direto; senão adiciona horário
  return dataStr.includes("T") ? dataStr : dataStr + "T00:00:00";
}

function verificarLogin() {
  if (!getToken()) {
    window.location.href = "../index.html";
  }
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

// ---- USUÁRIO (/auth/me e /User) ----
const UsuarioAPI = {
  async me() {
    try {
      const res = await fetch(API + "/auth/me", { headers: headers() });
      if (!res.ok) return null;
      const data = await res.json();
      if (data.nome) localStorage.setItem("fincontrol_nome", data.nome);
      if (data.email) localStorage.setItem("fincontrol_user", data.email);
      return data; // { nome, email, telefones[] }
    } catch { return null; }
  },
  async atualizar(dados) {
    // dados: { nome, senhaAtual, senhaNova, email }
    // Campos vazios são ignorados pelo backend
    try {
      const res = await fetch(API + "/User", {
        method: "POST",
        headers: headers(),
        body: JSON.stringify({
          nome: dados.nome || "",
          senhaAtual: dados.senhaAtual || "",
          senhaNova: dados.senhaNova || "",
          email: dados.email || ""
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

// ---- DASHBOARD (/dashboard?ano=&mes=) ----
const DashboardAPI = {
  async dados(ano, mes) {
    try {
      const agora = new Date();
      const a = ano || agora.getFullYear();
      const m = mes || (agora.getMonth() + 1);
      const res = await fetch(`${API}/dashboard?ano=${a}&mes=${m}`, { headers: headers() });
      if (!res.ok) return null;
      const data = await res.json();
      // Normaliza para o formato que o front usa
      return {
        saldo: data.saldo ?? 0,
        totalReceitas: data.receitaTotal ?? 0,
        totalDespesas: data.despesaTotal ?? 0,
        ultimosLancamentos: data.ultLancamento ?? []
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
      const data = await res.json();
      return data.map(r => ({
        ...r,
        categoria: nomeCategoriaById(r.idCategoria)
      }));
    } catch { return []; }
  },
  async criar(dados) {
    try {
      const body = {
        descricao: dados.descricao,
        valor: dados.valor,
        data: formatarDataAPI(dados.data),
        idCategoria: CATEGORIAS_RECEITA[dados.categoria] || 1,
        recorrencia: dados.recorrencia || "FIXA"
      };
      const res = await fetch(API + "/receitas", {
        method: "POST",
        headers: headers(),
        body: JSON.stringify(body)
      });
      return res.ok;
    } catch { return false; }
  },
  async editar(id, dados) {
    try {
      const body = {
        descricao: dados.descricao,
        valor: dados.valor,
        data: formatarDataAPI(dados.data),
        idCategoria: CATEGORIAS_RECEITA[dados.categoria] || 1,
        recorrencia: dados.recorrencia || "FIXA"
      };
      const res = await fetch(API + "/receitas/" + id, {
        method: "PUT",
        headers: headers(),
        body: JSON.stringify(body)
      });
      return res.ok;
    } catch { return false; }
  },
  async excluir(id) {
    try {
      const res = await fetch(API + "/receitas/" + id, {
        method: "DELETE",
        headers: headers()
      });
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
      const data = await res.json();
      return data.map(d => ({
        ...d,
        categoria: nomeCategoriaById(d.idCategoria)
      }));
    } catch { return []; }
  },
  async criar(dados) {
    try {
      const body = {
        descricao: dados.descricao,
        valor: dados.valor,
        data: formatarDataAPI(dados.data),
        idCategoria: CATEGORIAS_DESPESA[dados.categoria] || 21,
        recorrencia: dados.recorrencia || "FIXA",
        status: dados.status || "PAGA"
      };
      const res = await fetch(API + "/despesas", {
        method: "POST",
        headers: headers(),
        body: JSON.stringify(body)
      });
      return res.ok;
    } catch { return false; }
  },
  async editar(id, dados) {
    try {
      const body = {
        descricao: dados.descricao,
        valor: dados.valor,
        data: formatarDataAPI(dados.data),
        idCategoria: CATEGORIAS_DESPESA[dados.categoria] || 21,
        recorrencia: dados.recorrencia || "FIXA",
        status: dados.status || "PAGA"
      };
      const res = await fetch(API + "/despesas/" + id, {
        method: "PUT",
        headers: headers(),
        body: JSON.stringify(body)
      });
      return res.ok;
    } catch { return false; }
  },
  async excluir(id) {
    try {
      const res = await fetch(API + "/despesas/" + id, {
        method: "DELETE",
        headers: headers()
      });
      return res.ok || res.status === 204;
    } catch { return false; }
  }
};
