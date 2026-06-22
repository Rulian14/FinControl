// dados.js - Comunicação com a API

const API = "http://localhost:8080";

// Mapeamento de categoria nome -> idCategoria (conforme banco)
const CATEGORIAS_RECEITA = {
  "Salário": 1,
  "Freelance": 2,
  "Investimentos": 3
};

const CATEGORIAS_DESPESA = {
  "Moradia": 4,
  "Alimentação": 5,
  "Transporte": 6,
  "Lazer": 7
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

// Formata data para o formato que o back espera: "2026-05-27T14:30:00"
function formatarDataAPI(dataStr) {
  return dataStr + "T00:00:00";
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

// Nome da categoria pelo id
function nomeCategoriaById(id) {
  const todas = { ...CATEGORIAS_RECEITA, ...CATEGORIAS_DESPESA };
  return Object.keys(todas).find(k => todas[k] === id) || "Outros";
}

// ---- USUÁRIO ----
const UsuarioAPI = {
  async me() {
    try {
      const res = await fetch(API + "/me", { headers: headers() });
      if (!res.ok) return null;
      const data = await res.json();
      // Salva nome/email no localStorage para uso em toda a app
      if (data.nome) localStorage.setItem("fincontrol_nome", data.nome);
      if (data.email) localStorage.setItem("fincontrol_user", data.email);
      return data;
    } catch { return null; }
  }
};

// ---- DASHBOARD ----
const DashboardAPI = {
  async dados() {
    try {
      const res = await fetch(API + "/dashboard", { headers: headers() });
      if (!res.ok) return null;
      const data = await res.json();
      // Normaliza diferentes formatos que o backend pode retornar
      return {
        saldo: data.saldo ?? data.saldoAtual ?? 0,
        totalReceitas: data.totalReceitas ?? data.receitas ?? 0,
        totalDespesas: data.totalDespesas ?? data.despesas ?? 0,
        ultimosLancamentos: data.ultimosLancamentos ?? data.lancamentos ?? []
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
        categoria: r.categoria || nomeCategoriaById(r.idCategoria) || "Receita"
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
      return res.ok;
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
        categoria: d.categoria || nomeCategoriaById(d.idCategoria) || "Despesa"
      }));
    } catch { return []; }
  },
  async criar(dados) {
    try {
      const body = {
        descricao: dados.descricao,
        valor: dados.valor,
        data: formatarDataAPI(dados.data),
        idCategoria: CATEGORIAS_DESPESA[dados.categoria] || 5,
        recorrencia: dados.recorrencia || "FIXA"
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
        idCategoria: CATEGORIAS_DESPESA[dados.categoria] || 5,
        recorrencia: dados.recorrencia || "FIXA"
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
      return res.ok;
    } catch { return false; }
  }
};
