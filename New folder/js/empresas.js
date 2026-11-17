const companiesList = document.querySelector("#companies-list");
const companiesStatus = document.querySelector("#companies-status");
const refreshCompaniesButton = document.querySelector("#refresh-companies");

function renderCompanies(companies) {
  companiesList.replaceChildren();

  if (companies.length === 0) {
    companiesStatus.textContent = "Nenhuma empresa cadastrada.";
    companiesStatus.dataset.state = "empty";
    return;
  }

  const sortedCompanies = [...companies].sort((first, second) => {
    return String(first.nomeFantasia || "").localeCompare(String(second.nomeFantasia || ""), "pt-BR");
  });
  const cards = sortedCompanies.map((company, index) => {
    const card = document.createElement("article");
    card.className = "company-card";

    const heading = document.createElement("h2");
    heading.className = "company-card-heading";

    const toggle = document.createElement("button");
    toggle.className = "company-card-toggle";
    toggle.type = "button";
    toggle.setAttribute("aria-expanded", "false");

    const panelId = `company-employees-${index}`;
    const toggleId = `company-toggle-${index}`;
    toggle.id = toggleId;
    toggle.setAttribute("aria-controls", panelId);

    const copy = document.createElement("div");
    copy.className = "company-card-copy";

    const name = document.createElement("span");
    name.className = "company-card-name";
    name.textContent = company.nomeFantasia || "Nome fantasia não informado";

    const cnpj = document.createElement("p");
    cnpj.className = "company-card-cnpj";
    cnpj.textContent = company.cnpj || "CNPJ não informado";
    copy.append(name, cnpj);

    const employeeCount = document.createElement("div");
    employeeCount.className = "company-employee-count";

    const count = document.createElement("strong");
    const hasCount = Number.isInteger(company.employeeCount);
    count.textContent = hasCount ? String(company.employeeCount) : "—";

    const countLabel = document.createElement("span");
    countLabel.textContent = hasCount ? "funcionários" : "contagem indisponível";
    employeeCount.append(count, countLabel);

    const indicator = document.createElement("span");
    indicator.className = "company-accordion-indicator";
    indicator.setAttribute("aria-hidden", "true");
    toggle.append(copy, employeeCount, indicator);
    heading.append(toggle);

    const panel = document.createElement("div");
    panel.className = "company-employees-panel";
    panel.id = panelId;
    panel.setAttribute("role", "region");
    panel.setAttribute("aria-labelledby", toggleId);
    panel.hidden = true;

    if (Array.isArray(company.employees) && company.employees.length > 0) {
      const employeeList = document.createElement("ul");
      employeeList.className = "company-employees-list";
      company.employees.forEach((employee) => {
        const employeeItem = document.createElement("li");
        employeeItem.textContent = employee.nome || "Nome não informado";
        employeeList.append(employeeItem);
      });
      panel.append(employeeList);
    } else {
      const panelMessage = document.createElement("p");
      panelMessage.className = "company-employees-message";
      panelMessage.textContent = Array.isArray(company.employees)
        ? "Nenhum funcionário vinculado a esta empresa."
        : "Não foi possível carregar os funcionários desta empresa. Atualize a lista para tentar novamente.";
      panel.append(panelMessage);
    }

    toggle.addEventListener("click", () => {
      const isExpanded = toggle.getAttribute("aria-expanded") === "true";
      toggle.setAttribute("aria-expanded", String(!isExpanded));
      panel.hidden = isExpanded;
    });

    card.append(heading, panel);
    return card;
  });

  companiesList.replaceChildren(...cards);
  companiesStatus.textContent = `${companies.length} ${companies.length === 1 ? "empresa encontrada" : "empresas encontradas"}.`;
  companiesStatus.dataset.state = "success";
}

async function loadCompanies() {
  companiesList.setAttribute("aria-busy", "true");
  companiesStatus.textContent = "Carregando empresas...";
  companiesStatus.dataset.state = "loading";
  refreshCompaniesButton.disabled = true;

  try {
    const companies = await getCompanies();
    if (!Array.isArray(companies)) {
      throw new Error("A resposta da API não contém uma lista de empresas.");
    }

    const companiesWithCounts = await Promise.all(companies.map(async (company) => {
      if (!company.cnpj) {
        return { ...company, employeeCount: null, employees: null };
      }

      try {
        const users = await getCompanyUsers(company.cnpj);
        if (!Array.isArray(users)) {
          return { ...company, employeeCount: null, employees: null };
        }
        return {
          ...company,
          employeeCount: users.length,
          employees: users
        };
      } catch {
        return { ...company, employeeCount: null, employees: null };
      }
    }));

    renderCompanies(companiesWithCounts);
  } catch (error) {
    companiesList.replaceChildren();
    companiesStatus.textContent = `Não foi possível carregar as empresas. ${error.message}`;
    companiesStatus.dataset.state = "error";
  } finally {
    companiesList.setAttribute("aria-busy", "false");
    refreshCompaniesButton.disabled = false;
  }
}

refreshCompaniesButton.addEventListener("click", loadCompanies);
loadCompanies();