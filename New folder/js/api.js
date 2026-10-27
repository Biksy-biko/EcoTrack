const API_BASE_URL = "http://localhost:8080";

async function apiFetch(path, options = {}) {
  const url = new URL(`${API_BASE_URL}${path}`);
  const isLoginRequest = url.pathname === "/login";
  const headers = new Headers(options.headers);
  const token = isLoginRequest ? null : window.EcoTrackAuth.getValidToken();

  if (!isLoginRequest && !token) {
    throw new Error("É necessário entrar novamente para continuar.");
  }

  if (isLoginRequest) {
    headers.delete("Authorization");
  } else if (token) {
    headers.set("Authorization", `Bearer ${token}`);
  }

  const response = await fetch(url, { ...options, headers });

  if (!isLoginRequest && response.status === 401) {
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
    throw new Error(detail || `Falha ao cadastrar entrega (${response.status}).`);
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

async function getCompanies() {
  const response = await apiFetch("/empresas", {
    method: "GET",
    headers: {
      accept: "*/*"
    }
  });

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
