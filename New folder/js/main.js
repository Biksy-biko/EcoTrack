const navToggle = document.querySelector("#nav-toggle");
const primaryNavigation = document.querySelector("#primary-navigation");
const loginForm = document.querySelector("#login-form");
const loginInput = document.querySelector("#login");
const passwordInput = document.querySelector("#senha");
const loginStatus = document.querySelector("#login-status");
const submitButton = document.querySelector("#submit-button");
const sessionIndicator = document.querySelector("#session-indicator");
const loginLink = document.querySelector("#login-link");
const logoutButton = document.querySelector("#logout-button");
const managerTableBody = document.querySelector("#manager-table-body");
const managerStatus = document.querySelector("#manager-status");
const refreshManagersButton = document.querySelector("#refresh-managers");

function updateSessionDisplay() {
  const isLoggedIn = Boolean(localStorage.getItem("token"));

  if (sessionIndicator) {
    sessionIndicator.textContent = isLoggedIn ? "Sessão ativa" : "Visitante";
  }
  if (loginLink) {
    loginLink.hidden = isLoggedIn;
  }
  if (logoutButton) {
    logoutButton.hidden = !isLoggedIn;
  }
}

if (navToggle && primaryNavigation) {
  navToggle.addEventListener("click", () => {
    const isExpanded = navToggle.getAttribute("aria-expanded") === "true";
    navToggle.setAttribute("aria-expanded", String(!isExpanded));
    navToggle.setAttribute("aria-label", isExpanded ? "Abrir menu" : "Fechar menu");
    primaryNavigation.classList.toggle("is-open", !isExpanded);
  });

  primaryNavigation.querySelectorAll("a").forEach((link) => {
    link.addEventListener("click", () => {
      navToggle.setAttribute("aria-expanded", "false");
      navToggle.setAttribute("aria-label", "Abrir menu");
      primaryNavigation.classList.remove("is-open");
    });
  });
}

if (loginForm) {
  loginForm.addEventListener("submit", async (event) => {
    event.preventDefault();
    submitButton.disabled = true;
    loginForm.setAttribute("aria-busy", "true");
    loginStatus.textContent = "Validando seus dados...";
    loginStatus.dataset.state = "";

    try {
      const responseData = await loginUser({
        login: loginInput.value.trim(),
        senha: passwordInput.value
      });
      const token = responseData?.token;
      if (typeof token !== "string" || !token.trim()) {
        throw new Error("A resposta do login não contém um token válido.");
      }

      sessionStorage.removeItem("ecotrack.authAlertShown");
      localStorage.setItem("token", token);
      sessionStorage.setItem("ecotrack.loggedIn", "true");
      loginStatus.textContent = "Acesso confirmado. Abrindo o painel...";
      loginStatus.dataset.state = "success";
      window.setTimeout(() => {
        window.location.href = "../index.html";
      }, 350);
    } catch (error) {
      loginStatus.textContent = error.message || "Não foi possível entrar. Verifique seus dados.";
      loginStatus.dataset.state = "error";
      submitButton.disabled = false;
      loginForm.setAttribute("aria-busy", "false");
    }
  });
}

if (logoutButton) {
  logoutButton.addEventListener("click", () => {
    localStorage.removeItem("token");
    sessionStorage.removeItem("ecotrack.loggedIn");
    updateSessionDisplay();
  });
}

function renderManagers(managers) {
  managerTableBody.replaceChildren();

  if (managers.length === 0) {
    managerStatus.textContent = "Nenhum gestor encontrado.";
    managerStatus.dataset.state = "empty";
    return;
  }

  const rows = managers.map((manager) => {
    const row = document.createElement("tr");
    const companyId = manager.empresa_id ?? manager.empresaId;
    const values = [
      manager.id,
      manager.nome,
      manager.cpf,
      manager.dataNascimento,
      manager.razaoSocialEmpresa || (companyId == null ? "Sem vínculo" : `Empresa ${companyId}`)
    ];

    values.forEach((value) => {
      const cell = document.createElement("td");
      cell.textContent = value == null || value === "" ? "-" : String(value);
      row.append(cell);
    });

    return row;
  });

  managerTableBody.replaceChildren(...rows);
  managerStatus.textContent = `${managers.length} ${managers.length === 1 ? "gestor encontrado" : "gestores encontrados"}.`;
  managerStatus.dataset.state = "success";
}

async function loadManagers() {
  if (!managerTableBody || !managerStatus) {
    return;
  }

  managerTableBody.setAttribute("aria-busy", "true");
  managerStatus.textContent = "Carregando gestores...";
  managerStatus.dataset.state = "loading";
  if (refreshManagersButton) {
    refreshManagersButton.disabled = true;
  }

  try {
    const managers = await getManagers();
    if (!Array.isArray(managers)) {
      throw new Error("A resposta da API não contém uma lista de gestores.");
    }
    renderManagers(managers);
  } catch (error) {
    managerTableBody.replaceChildren();
    managerStatus.textContent = `Não foi possível carregar gestores. ${error.message}`;
    managerStatus.dataset.state = "error";
  } finally {
    managerTableBody.setAttribute("aria-busy", "false");
    if (refreshManagersButton) {
      refreshManagersButton.disabled = false;
    }
  }
}

if (managerTableBody) {
  loadManagers();
}

updateSessionDisplay();
