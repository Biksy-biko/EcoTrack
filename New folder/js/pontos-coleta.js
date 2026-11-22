const collectionPointForm = document.querySelector("#collection-point-form");
const collectionCepInput = document.querySelector("#collection-cep");
const cepStatus = document.querySelector("#cep-status");
const collectionPointStatus = document.querySelector("#collection-point-status");
const collectionPointSubmit = document.querySelector("#collection-point-submit");
const collectionAddressFields = [
  document.querySelector("#collection-city"),
  document.querySelector("#collection-state"),
  document.querySelector("#collection-street")
];
let latestCepRequest = 0;
let lastCepInput = collectionCepInput.value;
let addressResolved = false;
let isLookingUpCep = false;
let isSubmittingCollectionPoint = false;

function updateCollectionPointSubmit() {
  const hasAddress = collectionAddressFields.every((field) => field.value.trim());
  collectionPointSubmit.disabled = !addressResolved || !hasAddress || isSubmittingCollectionPoint;
}

function clearCollectionAddress() {
  addressResolved = false;
  collectionAddressFields.forEach((field) => {
    field.value = "";
    field.disabled = true;
  });
  updateCollectionPointSubmit();
}

async function loadAddressForCep(cep) {
  const requestId = ++latestCepRequest;
  isLookingUpCep = true;
  cepStatus.textContent = "Consultando CEP...";
  cepStatus.dataset.state = "loading";
  clearCollectionAddress();
  collectionPointForm.setAttribute("aria-busy", "true");

  try {
    const address = await getAddressByCep(cep);
    if (requestId !== latestCepRequest || collectionCepInput.value !== cep) {
      return;
    }

    collectionAddressFields[0].value = address.localidade || "";
    collectionAddressFields[1].value = address.uf || "";
    collectionAddressFields[2].value = address.logradouro || "";
    collectionAddressFields.forEach((field) => {
      field.disabled = false;
    });
    addressResolved = true;
    cepStatus.textContent = "Endereço preenchido pelo ViaCEP.";
    cepStatus.dataset.state = "success";
    updateCollectionPointSubmit();
  } catch (error) {
    if (requestId !== latestCepRequest) {
      return;
    }

    clearCollectionAddress();
    cepStatus.textContent = error.message || "Não foi possível consultar o CEP.";
    cepStatus.dataset.state = "error";
  } finally {
    if (requestId === latestCepRequest) {
      isLookingUpCep = false;
      collectionPointForm.setAttribute("aria-busy", "false");
    }
  }
}

function handleCepInput() {
  const normalizedCep = collectionCepInput.value.replace(/\D/g, "");
  collectionCepInput.value = normalizedCep;

  if (normalizedCep === lastCepInput) {
    return;
  }

  lastCepInput = normalizedCep;
  latestCepRequest += 1;
  isLookingUpCep = false;
  collectionPointForm.setAttribute("aria-busy", "false");
  clearCollectionAddress();
  collectionPointStatus.textContent = "";
  collectionPointStatus.dataset.state = "";

  if (normalizedCep.length === 8) {
    loadAddressForCep(normalizedCep);
    return;
  }

  cepStatus.textContent = normalizedCep ? "Digite os 8 dígitos do CEP." : "";
  cepStatus.dataset.state = normalizedCep ? "info" : "";
}

function handleCepBlur() {
  const normalizedCep = collectionCepInput.value.replace(/\D/g, "");
  if (normalizedCep !== collectionCepInput.value) {
    collectionCepInput.value = normalizedCep;
    lastCepInput = normalizedCep;
    latestCepRequest += 1;
    isLookingUpCep = false;
    collectionPointForm.setAttribute("aria-busy", "false");
    clearCollectionAddress();
  }
  if (!normalizedCep) {
    return;
  }
  if (normalizedCep.length !== 8) {
    cepStatus.textContent = "O CEP deve conter exatamente 8 dígitos.";
    cepStatus.dataset.state = "error";
    return;
  }
  if (!addressResolved && !isLookingUpCep) {
    loadAddressForCep(normalizedCep);
  }
}

collectionCepInput.addEventListener("input", handleCepInput);
collectionCepInput.addEventListener("blur", handleCepBlur);
collectionAddressFields.forEach((field) => {
  field.addEventListener("input", updateCollectionPointSubmit);
});

collectionPointForm.addEventListener("submit", async (event) => {
  event.preventDefault();
  if (!addressResolved || isSubmittingCollectionPoint || !collectionPointForm.reportValidity()) {
    return;
  }

  isSubmittingCollectionPoint = true;
  updateCollectionPointSubmit();
  collectionPointForm.setAttribute("aria-busy", "true");
  collectionPointStatus.textContent = "Cadastrando ponto de coleta...";
  collectionPointStatus.dataset.state = "loading";

  try {
    await createCollectionPoint({
      cidadePonto: collectionAddressFields[0].value.trim(),
      ufPonto: collectionAddressFields[1].value.trim(),
      logradouro: collectionAddressFields[2].value.trim()
    });

    collectionPointStatus.textContent = "Ponto de coleta cadastrado com sucesso.";
    collectionPointStatus.dataset.state = "success";
    collectionPointForm.reset();
    lastCepInput = "";
    clearCollectionAddress();
    cepStatus.textContent = "";
    cepStatus.dataset.state = "";
  } catch (error) {
    collectionPointStatus.textContent = `Não foi possível cadastrar o ponto de coleta. ${error.message}`;
    collectionPointStatus.dataset.state = "error";
  } finally {
    isSubmittingCollectionPoint = false;
    collectionPointForm.setAttribute("aria-busy", "false");
    updateCollectionPointSubmit();
  }
});
