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
  const cards = sortedCompanies.map((company) => {
    const card = document.createElement("article");
    card.className = "company-card";

    const copy = document.createElement("div");
    copy.className = "company-card-copy";

    const name = document.createElement("h2");
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
    card.append(copy, employeeCount);
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
        return { ...company, employeeCount: null };
      }

      try {
        const users = await getCompanyUsers(company.cnpj);
        return {
          ...company,
          employeeCount: Array.isArray(users) ? users.length : null
        };
      } catch {
        return { ...company, employeeCount: null };
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