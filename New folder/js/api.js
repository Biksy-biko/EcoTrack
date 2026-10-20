const API_BASE_URL = "http://localhost:8080";

async function getCities() {
  const response = await fetch(`${API_BASE_URL}/cidades`, {
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
  const response = await fetch(`${API_BASE_URL}/entregas`, {
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

async function getMaterials() {
  const response = await fetch(`${API_BASE_URL}/materiais`, {
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
  const response = await fetch(`${API_BASE_URL}/gestores`, {
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
  const response = await fetch(`${API_BASE_URL}/login`, {
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
