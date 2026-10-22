const API_BASE_URL = "http://localhost:8080";
const AUTH_TOKEN_KEY = "token";

function redirectToLogin() {
  localStorage.removeItem(AUTH_TOKEN_KEY);
  sessionStorage.removeItem("ecotrack.loggedIn");

  if (window.location.pathname.endsWith("/login.html")) {
    return;
  }

  const loginPath = window.location.pathname.includes("/html/")
    ? "login.html"
    : "html/login.html";
  window.location.assign(new URL(loginPath, window.location.href).href);
}

async function apiFetch(path, options = {}) {
  const url = new URL(`${API_BASE_URL}${path}`);
  const isLoginRequest = url.pathname === "/login";
  const headers = new Headers(options.headers);
  const token = localStorage.getItem(AUTH_TOKEN_KEY);

  if (isLoginRequest) {
    headers.delete("Authorization");
  } else if (token) {
    headers.set("Authorization", `Bearer ${token}`);
  }

  const response = await fetch(url, { ...options, headers });

  if (!isLoginRequest && (response.status === 401 || response.status === 403)) {
    redirectToLogin();
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
