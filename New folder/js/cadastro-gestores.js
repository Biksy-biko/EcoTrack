const registrationForm = document.querySelector("#manager-registration-form");
const registrationStatus = document.querySelector("#registration-status");
const registrationSubmitButton = document.querySelector("#submit-button");
const companySelect = document.querySelector("#manager-company");
const companiesStatus = document.querySelector("#companies-status");

function setCompanyPlaceholder(message) {
  const placeholder = document.createElement("option");
  placeholder.value = "";
  placeholder.textContent = message;
  placeholder.disabled = true;
  placeholder.selected = true;
  companySelect.replaceChildren(placeholder);
}

async function loadCompanyOptions() {
  companiesStatus.textContent = "Carregando empresas...";
  companiesStatus.dataset.state = "loading";
  companySelect.disabled = true;
  setCompanyPlaceholder("Carregando empresas...");

  try {
    const companies = await getCompanies({ allowAnonymous: true });
    if (!Array.isArray(companies)) {
      throw new Error("A resposta da API não contém uma lista de empresas.");
    }

    if (companies.some((company) => !Number.isInteger(company.id) || company.id < 0)) {
      throw new Error("A resposta da API contém uma empresa sem ID válido.");
    }

    if (companies.length === 0) {
      setCompanyPlaceholder("Nenhuma empresa cadastrada");
      companiesStatus.textContent = "Nenhuma empresa cadastrada.";
      companiesStatus.dataset.state = "empty";
      return;
    }

    const sortedCompanies = [...companies].sort((first, second) => {
      const firstName = first.nomeFantasia || first.razaoSocial || "";
      const secondName = second.nomeFantasia || second.razaoSocial || "";
      return String(firstName).localeCompare(String(secondName), "pt-BR");
    });
    const options = sortedCompanies.map((company) => {
      const option = document.createElement("option");
      const companyName = company.nomeFantasia || company.razaoSocial || "Empresa";
      option.value = String(company.id);
      option.textContent = `${companyName}${company.cnpj ? ` - ${company.cnpj}` : ""}`;
      return option;
    });

    setCompanyPlaceholder("Selecione uma empresa");
    companySelect.append(...options);
    companySelect.disabled = false;
    companiesStatus.textContent = `${companies.length} ${companies.length === 1 ? "empresa encontrada" : "empresas encontradas"}.`;
    companiesStatus.dataset.state = "success";
    registrationSubmitButton.disabled = false;
  } catch (error) {
    setCompanyPlaceholder("Não foi possível carregar as empresas");
    companiesStatus.textContent = `Não foi possível carregar as empresas. ${error.message}`;
    companiesStatus.dataset.state = "error";
  }
}

if (registrationForm) {
  registrationForm.addEventListener("submit", async (event) => {
    event.preventDefault();
    registrationSubmitButton.disabled = true;
    registrationForm.setAttribute("aria-busy", "true");
    registrationStatus.textContent = "Cadastrando gestor...";
    registrationStatus.dataset.state = "loading";

    const formData = new FormData(registrationForm);

    try {
      await createManager({
        nome: formData.get("nome").trim(),
        cpf: formData.get("cpf").trim(),
        dataNascimento: formData.get("dataNascimento"),
        senha: formData.get("senha"),
        empresa_id: Number(formData.get("empresa_id"))
      });

      registrationStatus.textContent = "Cadastro realizado com sucesso. Você já pode acessar o login.";
      registrationStatus.dataset.state = "success";
      registrationForm.reset();
    } catch (error) {
      registrationStatus.textContent = `Não foi possível cadastrar o gestor. ${error.message}`;
      registrationStatus.dataset.state = "error";
    } finally {
      registrationSubmitButton.disabled = false;
      registrationForm.setAttribute("aria-busy", "false");
    }
  });
}

loadCompanyOptions();
