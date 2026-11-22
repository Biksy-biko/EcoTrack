const API_BASE_URL = "http://localhost:8080";
const VIA_CEP_BASE_URL = "https://viacep.com.br/ws";
const COLLECTION_POINTS_API_PATH = "/pontoscoleta";

async function apiFetch(path, options = {}, allowAnonymous = false) {
  const url = new URL(`${API_BASE_URL}${path}`);
  const method = (options.method || "GET").toUpperCase();
  const isPublicRequest = allowAnonymous
    || url.pathname === "/login"
    || (url.pathname === "/gestores" && method === "POST");
  const headers = new Headers(options.headers);
  const token = isPublicRequest ? null : window.EcoTrackAuth.getValidToken();

  if (!isPublicRequest && !token) {
    throw new Error("É necessário entrar novamente para continuar.");
  }

  if (isPublicRequest) {
    headers.delete("Authorization");
  } else if (token) {
    headers.set("Authorization", `Bearer ${token}`);
  }

  const response = await fetch(url, { ...options, headers });

  if (!isPublicRequest && response.status === 401) {
    window.EcoTrackAuth.handleUnauthorized();
  }

  return response;
}

async function getCities() {
  const response = await apiFetch("/cidades", {
    method: "GET",
    headers: {
      accept: "*/*"
    }
  });

  if (!response.ok) {
    throw new Error(`Falha ao carregar cidades (${response.status}).`);
  }

  return response.json();
}

async function createDelivery(delivery) {
  const response = await apiFetch("/entregas", {
    method: "POST",
    headers: {
      accept: "*/*",
      "Content-Type": "application/json"
    },
    body: JSON.stringify(delivery)
  });

  return readApiResponse(response, "Falha ao cadastrar entrega.");
}

async function getDeliveryById(id) {
  const response = await apiFetch(`/entregas/${encodeURIComponent(id)}`, {
    method: "GET",
    headers: {
      accept: "*/*"
    }
  });

  return readApiResponse(response, `Falha ao carregar entrega ${id}.`);
}

async function completeDelivery(id) {
  const response = await apiFetch(`/entregas/${encodeURIComponent(id)}/status`, {
    method: "PATCH",
    headers: {
      accept: "*/*",
      "Content-Type": "application/json"
    },
    body: JSON.stringify({ status: "Entregue" })
  });

  return readApiResponse(response, `Falha ao concluir entrega ${id}.`);
}

async function cancelDelivery(id) {
  const response = await apiFetch(`/entregas/${encodeURIComponent(id)}`, {
    method: "DELETE",
    headers: {
      accept: "*/*"
    }
  });

  return readApiResponse(response, `Falha ao cancelar entrega ${id}.`);
}

async function readApiResponse(response, fallbackMessage) {
  const responseText = await response.text();
  let responseData = null;

  if (responseText) {
    try {
      responseData = JSON.parse(responseText);
    } catch {
      responseData = responseText;
    }
  }

  if (!response.ok) {
    const detail = typeof responseData === "object" && responseData !== null
      ? responseData.message || responseData.error || responseData.detail
      : responseData;
    throw new Error(detail || `${fallbackMessage} (${response.status}).`);
  }

  return responseData;
}

async function getDeliveries() {
  const response = await apiFetch("/entregas", {
    method: "GET",
    headers: {
      accept: "*/*"
    }
  });

  if (!response.ok) {
    throw new Error(`Falha ao carregar entregas (${response.status}).`);
  }

  return response.json();
}

async function getMaterials() {
  const response = await apiFetch("/materiais", {
    method: "GET",
    headers: {
      accept: "*/*"
    }
  });

  if (!response.ok) {
    throw new Error(`Falha ao carregar materiais (${response.status}).`);
  }

  return response.json();
}

async function getManagers() {
  const response = await apiFetch("/gestores", {
    method: "GET",
    headers: {
      accept: "*/*"
    }
  });

  if (!response.ok) {
    throw new Error(`Falha ao carregar gestores (${response.status}).`);
  }

  return response.json();
}

async function createManager(manager) {
  const response = await apiFetch("/gestores", {
    method: "POST",
    headers: {
      accept: "*/*",
      "Content-Type": "application/json"
    },
    body: JSON.stringify({
      nome: manager.nome,
      cpf: manager.cpf,
      dataNascimento: manager.dataNascimento,
      senha: manager.senha,
      empresa_id: manager.empresa_id
    })
  });

  const responseText = await response.text();
  let responseData = null;

  if (responseText) {
    try {
      responseData = JSON.parse(responseText);
    } catch {
      responseData = responseText;
    }
  }

  if (!response.ok) {
    const detail = typeof responseData === "object" && responseData !== null
      ? responseData.message || responseData.error || responseData.detail
      : responseData;
    throw new Error(detail || `Falha ao cadastrar gestor (${response.status}).`);
  }

  return responseData;
}

async function getAddressByCep(cep) {
  const normalizedCep = String(cep).replace(/\D/g, "");
  if (!/^\d{8}$/.test(normalizedCep)) {
    throw new Error("O CEP deve conter 8 dígitos.");
  }

  const response = await fetch(`${VIA_CEP_BASE_URL}/${normalizedCep}/json/`, {
    method: "GET",
    headers: {
      accept: "application/json"
    }
  });

  if (!response.ok) {
    throw new Error(`Falha ao consultar o CEP (${response.status}).`);
  }

  const address = await response.json();
  if (address.erro === true) {
    throw new Error("CEP não encontrado. Confira o número informado.");
  }

  return address;
}

async function createCollectionPoint(collectionPoint) {
  const response = await apiFetch(COLLECTION_POINTS_API_PATH, {
    method: "POST",
    headers: {
      accept: "*/*",
      "Content-Type": "application/json"
    },
    body: JSON.stringify({
      cidadePonto: collectionPoint.cidadePonto,
      ufPonto: collectionPoint.ufPonto,
      logradouro: collectionPoint.logradouro
    })
  });

  const responseText = await response.text();
  let responseData = null;

  if (responseText) {
    try {
      responseData = JSON.parse(responseText);
    } catch {
      responseData = responseText;
    }
  }

  if (!response.ok) {
    const detail = typeof responseData === "object" && responseData !== null
      ? responseData.message || responseData.error || responseData.detail
      : responseData;
    throw new Error(detail || `Falha ao cadastrar ponto de coleta (${response.status}).`);
  }

  return responseData;
}

async function getCompanies({ allowAnonymous = false } = {}) {
  const response = await apiFetch("/empresas", {
    method: "GET",
    headers: {
      accept: "*/*"
    }
  }, allowAnonymous);

  if (!response.ok) {
    throw new Error(`Falha ao carregar empresas (${response.status}).`);
  }

  return response.json();
}

async function getCompanyUsers(cnpj) {
  const response = await apiFetch(`/empresas/cnpj/${encodeURIComponent(cnpj)}/usuarios`, {
    method: "GET",
    headers: {
      accept: "*/*"
    }
  });

  if (!response.ok) {
    throw new Error(`Falha ao carregar funcionários da empresa (${response.status}).`);
  }

  return response.json();
}

async function loginUser(credentials) {
  const response = await apiFetch("/login", {
    method: "POST",
    headers: {
      accept: "*/*",
      "Content-Type": "application/json"
    },
    body: JSON.stringify({
      login: credentials.login,
      senha: credentials.senha
    })
  });

  const responseText = await response.text();
  let responseData = null;

  if (responseText) {
    try {
      responseData = JSON.parse(responseText);
    } catch {
      responseData = responseText;
    }
  }

  if (!response.ok) {
    const detail = typeof responseData === "object" && responseData !== null
      ? responseData.message || responseData.error || responseData.detail
      : responseData;
    throw new Error(detail || `Falha no login (${response.status}).`);
  }

  return responseData;
}
