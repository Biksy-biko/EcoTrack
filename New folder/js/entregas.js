const deliveryForm = document.querySelector("#delivery-form");
const originCitySelect = document.querySelector("#cidade-origem");
const originStateSelect = document.querySelector("#uf-origem");
const destinationCitySelect = document.querySelector("#cidade-destino");
const destinationStateSelect = document.querySelector("#uf-destino");
const cargoTotalInput = document.querySelector("#carga-total");
const deliveryStatus = document.querySelector("#delivery-status");
const deliverySubmit = document.querySelector("#delivery-submit");
const deliveryPointCepInput = document.querySelector("#point-cep");
const deliveryPointCepStatus = document.querySelector("#point-cep-status");
const deliveryPointCityInput = document.querySelector("#point-city");
const deliveryPointStateInput = document.querySelector("#point-state");
const deliveryPointStreetInput = document.querySelector("#point-street");
const deliveryPointFields = [
  deliveryPointCityInput,
  deliveryPointStateInput,
  deliveryPointStreetInput
];
const addDeliveryPointButton = document.querySelector("#add-delivery-point");
const deliveryPointsStatus = document.querySelector("#delivery-points-status");
const deliveryPointsList = document.querySelector("#delivery-points-list");
const deliveriesListStatus = document.querySelector("#deliveries-list-status");
const deliveriesTableBody = document.querySelector("#deliveries-table-body");
const refreshDeliveriesButton = document.querySelector("#refresh-deliveries");
const deliveryActionsStatus = document.querySelector("#delivery-actions-status");
const deliveryFilterButtons = Array.from(document.querySelectorAll("[data-delivery-filter]"));
const deliveryDetailsDialog = document.querySelector("#delivery-details-dialog");
const deliveryDetailsStatus = document.querySelector("#delivery-details-status");
const deliveryDetailsList = document.querySelector("#delivery-details-list");
const deliveryDetailsPoints = document.querySelector("#delivery-details-points");
const closeDeliveryDetailsButton = document.querySelector("#close-delivery-details");

let availablePlaces = [];
let deliveries = [];
let collectionPoints = [];
let nextCollectionPointId = 0;
let activeDeliveryFilter = "all";
let latestPointCepRequest = 0;
let lastPointCepInput = deliveryPointCepInput.value;
let pointAddressResolved = false;
let isLookingUpPointCep = false;
let isSubmittingDelivery = false;

function normalizePlace(city, state) {
  if (typeof city !== "string" || typeof state !== "string") {
    return null;
  }

  const normalizedCity = city.trim();
  const normalizedState = state.trim().toUpperCase();
  if (!normalizedCity || !normalizedState) {
    return null;
  }

  return { city: normalizedCity, state: normalizedState };
}

function getAvailablePlaces(cities) {
  const uniquePlaces = new Map();

  cities.forEach((city) => {
    const place = normalizePlace(city.cidade, city.uf);
    if (place) {
      uniquePlaces.set(JSON.stringify(place), place);
    }
  });

  return Array.from(uniquePlaces.values()).sort((first, second) => {
    return first.city.localeCompare(second.city, "pt-BR") || first.state.localeCompare(second.state, "pt-BR");
  });
}

function addPlaceholder(select, label) {
  const placeholder = document.createElement("option");
  placeholder.value = "";
  placeholder.textContent = label;
  placeholder.disabled = true;
  placeholder.selected = true;
  select.replaceChildren(placeholder);
}

function populateStates(select, places) {
  addPlaceholder(select, "Selecione a UF");
  const states = Array.from(new Set(places.map((place) => place.state))).sort();

  states.forEach((state) => {
    const option = document.createElement("option");
    option.value = state;
    option.textContent = state;
    select.append(option);
  });
}

function populateCities(select, places, state = "") {
  addPlaceholder(select, "Selecione a cidade");
  const filteredPlaces = state ? places.filter((place) => place.state === state) : places;

  filteredPlaces.forEach((place) => {
    const option = document.createElement("option");
    option.value = JSON.stringify(place);
    option.textContent = place.city;
    select.append(option);
  });
}

function setupPlaceSelectors(citySelect, stateSelect) {
  populateStates(stateSelect, availablePlaces);
  populateCities(citySelect, availablePlaces);

  stateSelect.addEventListener("change", () => {
    populateCities(citySelect, availablePlaces, stateSelect.value);
  });

  citySelect.addEventListener("change", () => {
    try {
      const place = JSON.parse(citySelect.value);
      if (place.state !== stateSelect.value) {
        stateSelect.value = place.state;
      }
    } catch {
      stateSelect.value = "";
    }
  });
}

function getSelectedPlace(citySelect, stateSelect) {
  if (!citySelect.value || !stateSelect.value) {
    throw new Error("Selecione uma cidade e uma UF para origem e destino.");
  }

  const place = JSON.parse(citySelect.value);
  if (place.state !== stateSelect.value) {
    throw new Error("A cidade selecionada não corresponde à UF escolhida.");
  }

  return place;
}

function clearDeliveryPointAddress() {
  pointAddressResolved = false;
  deliveryPointFields.forEach((field) => {
    field.value = "";
    field.disabled = true;
  });
  updateAddDeliveryPointButton();
}

function updateAddDeliveryPointButton() {
  const addressIsComplete = deliveryPointFields.every((field) => field.value.trim());
  addDeliveryPointButton.disabled = !pointAddressResolved || !addressIsComplete;
  if (pointAddressResolved) {
    deliveryPointCepStatus.textContent = addressIsComplete
      ? "Endereço pronto para adicionar."
      : "Preencha cidade, UF e logradouro para adicionar o ponto.";
    deliveryPointCepStatus.dataset.state = addressIsComplete ? "success" : "info";
  }
}

async function lookupDeliveryPointCep(cep) {
  const requestId = ++latestPointCepRequest;
  lastPointCepInput = cep;
  isLookingUpPointCep = true;
  deliveryPointCepStatus.textContent = "Consultando CEP...";
  deliveryPointCepStatus.dataset.state = "loading";
  clearDeliveryPointAddress();

  try {
    const address = await getAddressByCep(cep);
    if (requestId !== latestPointCepRequest || deliveryPointCepInput.value !== cep) {
      return;
    }

    deliveryPointCityInput.value = address.localidade || "";
    deliveryPointStateInput.value = address.uf || "";
    deliveryPointStreetInput.value = address.logradouro || "";
    deliveryPointFields.forEach((field) => {
      field.disabled = false;
    });
    pointAddressResolved = true;
    deliveryPointCepStatus.textContent = "Endereço preenchido pelo ViaCEP.";
    deliveryPointCepStatus.dataset.state = "success";
    updateAddDeliveryPointButton();
  } catch (error) {
    if (requestId !== latestPointCepRequest) {
      return;
    }

    clearDeliveryPointAddress();
    deliveryPointCepStatus.textContent = error.message || "Não foi possível consultar o CEP.";
    deliveryPointCepStatus.dataset.state = "error";
  } finally {
    if (requestId === latestPointCepRequest) {
      isLookingUpPointCep = false;
    }
  }
}

function handleDeliveryPointCepInput() {
  const normalizedCep = deliveryPointCepInput.value.replace(/\D/g, "");
  deliveryPointCepInput.value = normalizedCep;
  if (normalizedCep === lastPointCepInput) {
    return;
  }

  latestPointCepRequest += 1;
  isLookingUpPointCep = false;
  lastPointCepInput = normalizedCep;
  clearDeliveryPointAddress();

  if (normalizedCep.length === 8) {
    lookupDeliveryPointCep(normalizedCep);
    return;
  }

  deliveryPointCepStatus.textContent = normalizedCep ? "Digite os 8 dígitos do CEP." : "";
  deliveryPointCepStatus.dataset.state = normalizedCep ? "info" : "";
}

function handleDeliveryPointCepBlur() {
  const normalizedCep = deliveryPointCepInput.value.replace(/\D/g, "");
  if (normalizedCep !== deliveryPointCepInput.value) {
    deliveryPointCepInput.value = normalizedCep;
    latestPointCepRequest += 1;
    isLookingUpPointCep = false;
    clearDeliveryPointAddress();
  }
  if (!normalizedCep) {
    return;
  }
  if (normalizedCep.length !== 8) {
    deliveryPointCepStatus.textContent = "O CEP deve conter exatamente 8 dígitos.";
    deliveryPointCepStatus.dataset.state = "error";
    return;
  }
  if (!pointAddressResolved && !isLookingUpPointCep) {
    lookupDeliveryPointCep(normalizedCep);
  }
}

function renderCollectionPoints() {
  deliveryPointsList.replaceChildren();

  if (collectionPoints.length === 0) {
    deliveryPointsStatus.textContent = "Nenhum ponto adicionado.";
    deliveryPointsStatus.dataset.state = "empty";
    return;
  }

  const items = collectionPoints.map((point, index) => {
    const item = document.createElement("li");
    item.className = "delivery-point-item";

    const description = document.createElement("div");
    const heading = document.createElement("strong");
    heading.textContent = `Ponto ${index + 1}: ${point.cidadePonto} / ${point.ufPonto}`;
    const street = document.createElement("p");
    street.textContent = point.logradouro;
    description.append(heading, street);

    const removeButton = document.createElement("button");
    removeButton.className = "delivery-point-remove";
    removeButton.type = "button";
    removeButton.dataset.pointId = String(point.id);
    removeButton.textContent = "Remover";
    removeButton.setAttribute("aria-label", `Remover ponto ${index + 1}`);

    item.append(description, removeButton);
    return item;
  });

  deliveryPointsList.replaceChildren(...items);
  deliveryPointsStatus.textContent = `${collectionPoints.length} ${collectionPoints.length === 1 ? "ponto adicionado" : "pontos adicionados"}.`;
  deliveryPointsStatus.dataset.state = "success";
}

function addDeliveryPoint() {
  if (!pointAddressResolved) {
    return;
  }

  const missingField = deliveryPointFields.find((field) => !field.value.trim());
  if (missingField) {
    deliveryPointCepStatus.textContent = "Preencha cidade, UF e logradouro para adicionar o ponto.";
    deliveryPointCepStatus.dataset.state = "error";
    missingField.focus();
    return;
  }

  collectionPoints.push({
    id: ++nextCollectionPointId,
    cidadePonto: deliveryPointCityInput.value.trim(),
    ufPonto: deliveryPointStateInput.value.trim().toUpperCase(),
    logradouro: deliveryPointStreetInput.value.trim()
  });
  renderCollectionPoints();

  deliveryPointCepInput.value = "";
  lastPointCepInput = "";
  latestPointCepRequest += 1;
  isLookingUpPointCep = false;
  clearDeliveryPointAddress();
  deliveryPointCepStatus.textContent = "";
  deliveryPointCepStatus.dataset.state = "";
}

function removeDeliveryPoint(pointId) {
  collectionPoints = collectionPoints.filter((point) => String(point.id) !== pointId);
  renderCollectionPoints();
}

function getDeliveryStatus(delivery) {
  const rawStatus = String(delivery.status ?? "").trim();
  const normalizedStatus = rawStatus
    .normalize("NFD")
    .replace(/[\u0300-\u036f]/g, "")
    .toLocaleLowerCase("pt-BR");

  if (["entregue", "delivered", "concluido", "concluida"].includes(normalizedStatus)) {
    return { filter: "delivered", label: "Entregue" };
  }
  if (["pendente", "pending"].includes(normalizedStatus)) {
    return { filter: "pending", label: "Pendente" };
  }
  if (["cancelado", "cancelada", "cancelled"].includes(normalizedStatus)) {
    return { filter: "cancelled", label: "Cancelada" };
  }

  return { filter: "other", label: rawStatus || "Não informado" };
}

function getDeliveryId(delivery) {
  return delivery.idEntrega ?? delivery.id;
}

function getDeliveryPoints(delivery) {
  if (Array.isArray(delivery.pontoColeta)) {
    return delivery.pontoColeta;
  }
  return Array.isArray(delivery.pontosColeta) ? delivery.pontosColeta : [];
}

function formatDeliveryPlace(city, state) {
  const parts = [city, state].filter((value) => typeof value === "string" && value.trim());
  return parts.length ? parts.join(" / ") : "-";
}

function formatDeliveryDate(value) {
  if (!value) {
    return "-";
  }

  const date = new Date(value);
  return Number.isNaN(date.getTime())
    ? String(value)
    : new Intl.DateTimeFormat("pt-BR", { dateStyle: "short" }).format(date);
}

function formatCargoTotal(value) {
  if (value == null || value === "" || !Number.isFinite(Number(value))) {
    return "-";
  }

  return new Intl.NumberFormat("pt-BR", { maximumFractionDigits: 2 }).format(Number(value));
}

function createDeliveryActionButton(label, action, id, modifier = "") {
  const button = document.createElement("button");
  button.className = `delivery-row-action${modifier ? ` delivery-row-action--${modifier}` : ""}`;
  button.type = "button";
  button.dataset.deliveryAction = action;
  button.dataset.deliveryId = String(id);
  button.textContent = label;
  button.setAttribute("aria-label", `${label} entrega ${id}`);
  return button;
}

function renderDeliveries() {
  const visibleDeliveries = deliveries.filter((delivery) => {
    return activeDeliveryFilter === "all" || getDeliveryStatus(delivery).filter === activeDeliveryFilter;
  });
  deliveriesTableBody.replaceChildren();

  if (visibleDeliveries.length === 0) {
    deliveriesListStatus.textContent = deliveries.length === 0
      ? "Nenhuma entrega cadastrada."
      : "Nenhuma entrega encontrada para este status.";
    deliveriesListStatus.dataset.state = "empty";
    return;
  }

  const rows = visibleDeliveries.map((delivery) => {
    const row = document.createElement("tr");
    const id = getDeliveryId(delivery);
    const points = getDeliveryPoints(delivery);
    const values = [
      id ?? "-",
      delivery.codigoRastreio || "-",
      formatDeliveryPlace(delivery.cidadeOrigem, delivery.ufOrigem),
      formatDeliveryPlace(delivery.cidadeDestino, delivery.ufDestino),
      formatCargoTotal(delivery.cargaTotal),
      String(points.length)
    ];

    values.forEach((value) => {
      const cell = document.createElement("td");
      cell.textContent = String(value);
      row.append(cell);
    });

    const statusCell = document.createElement("td");
    const status = getDeliveryStatus(delivery);
    const statusLabel = document.createElement("span");
    statusLabel.className = `delivery-state delivery-state--${status.filter}`;
    statusLabel.textContent = status.label;
    statusCell.append(statusLabel);
    row.append(statusCell);

    const dateCell = document.createElement("td");
    dateCell.textContent = formatDeliveryDate(delivery.dataCriacao);
    row.append(dateCell);

    const actionsCell = document.createElement("td");
    const actions = document.createElement("div");
    actions.className = "delivery-row-actions";
    if (id != null) {
      actions.append(createDeliveryActionButton("Detalhes", "details", id));
      if (status.filter !== "delivered" && status.filter !== "cancelled") {
        actions.append(
          createDeliveryActionButton("Concluir", "complete", id),
          createDeliveryActionButton("Cancelar", "cancel", id, "cancel")
        );
      }
    }
    actionsCell.append(actions);
    row.append(actionsCell);
    return row;
  });

  deliveriesTableBody.replaceChildren(...rows);
  deliveriesListStatus.textContent = `${visibleDeliveries.length} ${visibleDeliveries.length === 1 ? "entrega encontrada" : "entregas encontradas"}.`;
  deliveriesListStatus.dataset.state = "success";
}

async function loadDeliveries() {
  deliveriesTableBody.setAttribute("aria-busy", "true");
  deliveriesListStatus.textContent = "Carregando entregas...";
  deliveriesListStatus.dataset.state = "loading";
  refreshDeliveriesButton.disabled = true;

  try {
    const response = await getDeliveries();
    if (!Array.isArray(response)) {
      throw new Error("A resposta da API não contém uma lista de entregas.");
    }
    deliveries = response;
    renderDeliveries();
    return true;
  } catch (error) {
    deliveries = [];
    deliveriesTableBody.replaceChildren();
    deliveriesListStatus.textContent = `Não foi possível carregar entregas. ${error.message}`;
    deliveriesListStatus.dataset.state = "error";
    return false;
  } finally {
    deliveriesTableBody.setAttribute("aria-busy", "false");
    refreshDeliveriesButton.disabled = false;
  }
}

async function loadCities() {
  deliveryForm.setAttribute("aria-busy", "true");
  deliveryStatus.textContent = "Carregando cidades e UFs...";
  deliveryStatus.dataset.state = "loading";
  deliverySubmit.disabled = true;

  try {
    const cities = await getCities();
    if (!Array.isArray(cities)) {
      throw new Error("A resposta da API não contém uma lista de cidades.");
    }

    availablePlaces = getAvailablePlaces(cities);
    if (availablePlaces.length === 0) {
      addPlaceholder(originCitySelect, "Nenhuma cidade disponível");
      addPlaceholder(destinationCitySelect, "Nenhuma cidade disponível");
      addPlaceholder(originStateSelect, "UF indisponível");
      addPlaceholder(destinationStateSelect, "UF indisponível");
      deliveryStatus.textContent = "A API não retornou cidades/UFs válidas.";
      deliveryStatus.dataset.state = "error";
      return;
    }

    setupPlaceSelectors(originCitySelect, originStateSelect);
    setupPlaceSelectors(destinationCitySelect, destinationStateSelect);
    deliveryStatus.textContent = `${availablePlaces.length} combinações de cidade e UF disponíveis.`;
    deliveryStatus.dataset.state = "success";
  } catch (error) {
    availablePlaces = [];
    addPlaceholder(originCitySelect, "Falha ao carregar cidades");
    addPlaceholder(destinationCitySelect, "Falha ao carregar cidades");
    addPlaceholder(originStateSelect, "UF indisponível");
    addPlaceholder(destinationStateSelect, "UF indisponível");
    deliveryStatus.textContent = `Não foi possível carregar as cidades. ${error.message}`;
    deliveryStatus.dataset.state = "error";
  } finally {
    deliveryForm.setAttribute("aria-busy", "false");
    deliverySubmit.disabled = availablePlaces.length === 0;
  }
}

function resetDeliveryPointEntry() {
  deliveryPointCepInput.value = "";
  lastPointCepInput = "";
  latestPointCepRequest += 1;
  isLookingUpPointCep = false;
  clearDeliveryPointAddress();
  deliveryPointCepStatus.textContent = "";
  deliveryPointCepStatus.dataset.state = "";
}

async function submitDelivery(event) {
  event.preventDefault();
  if (!deliveryForm.reportValidity()) {
    return;
  }

  deliverySubmit.disabled = true;
  isSubmittingDelivery = true;
  deliveryForm.setAttribute("aria-busy", "true");
  deliveryStatus.textContent = "Cadastrando entrega...";
  deliveryStatus.dataset.state = "loading";

  try {
    const origin = getSelectedPlace(originCitySelect, originStateSelect);
    const destination = getSelectedPlace(destinationCitySelect, destinationStateSelect);
    const cargaTotal = Number(cargoTotalInput.value);
    if (!Number.isFinite(cargaTotal) || cargaTotal < 0) {
      throw new Error("Informe uma carga total válida.");
    }

    // O backend gera o código de rastreio; ele será exibido após atualizar a lista.
    const createdDelivery = await createDelivery({
      cidadeOrigem: origin.city,
      ufOrigem: origin.state,
      cidadeDestino: destination.city,
      ufDestino: destination.state,
      cargaTotal,
      pontosColeta: collectionPoints.map((point) => ({
        cidadePonto: point.cidadePonto,
        ufPonto: point.ufPonto,
        logradouro: point.logradouro
      }))
    });

    deliveryForm.reset();
    collectionPoints = [];
    renderCollectionPoints();
    resetDeliveryPointEntry();
    await loadDeliveries();

    const createdId = createdDelivery?.idEntrega ?? createdDelivery?.id;
    const savedDelivery = deliveries.find((delivery) => String(getDeliveryId(delivery)) === String(createdId));
    const trackingCode = savedDelivery?.codigoRastreio || createdDelivery?.codigoRastreio;
    const resultMessage = createdDelivery?.mensagem || "Entrega cadastrada com sucesso.";
    deliveryStatus.textContent = trackingCode
      ? `${resultMessage} Código de rastreio: ${trackingCode}.`
      : resultMessage;
    deliveryStatus.dataset.state = "success";
  } catch (error) {
    deliveryStatus.textContent = `Não foi possível cadastrar a entrega. ${error.message}`;
    deliveryStatus.dataset.state = "error";
  } finally {
    isSubmittingDelivery = false;
    deliveryForm.setAttribute("aria-busy", "false");
    deliverySubmit.disabled = availablePlaces.length === 0;
  }
}

function appendDeliveryDetail(label, value) {
  const wrapper = document.createElement("div");
  const term = document.createElement("dt");
  term.textContent = label;
  const description = document.createElement("dd");
  description.textContent = value == null || value === "" ? "-" : String(value);
  wrapper.append(term, description);
  deliveryDetailsList.append(wrapper);
}

function renderDeliveryDetails(delivery) {
  deliveryDetailsList.replaceChildren();
  appendDeliveryDetail("ID", getDeliveryId(delivery));
  appendDeliveryDetail("Código de rastreio", delivery.codigoRastreio);
  appendDeliveryDetail("Origem", formatDeliveryPlace(delivery.cidadeOrigem, delivery.ufOrigem));
  appendDeliveryDetail("Destino", formatDeliveryPlace(delivery.cidadeDestino, delivery.ufDestino));
  appendDeliveryDetail("Carga total", formatCargoTotal(delivery.cargaTotal));
  appendDeliveryDetail("Status", getDeliveryStatus(delivery).label);
  appendDeliveryDetail("Data de cadastro", formatDeliveryDate(delivery.dataCriacao));

  deliveryDetailsPoints.replaceChildren();
  const points = getDeliveryPoints(delivery);
  if (points.length === 0) {
    const item = document.createElement("li");
    item.textContent = "Nenhum ponto de coleta.";
    deliveryDetailsPoints.append(item);
    return;
  }

  points.forEach((point) => {
    const item = document.createElement("li");
    item.textContent = `${formatDeliveryPlace(point.cidadePonto, point.ufPonto)} - ${point.logradouro || "Logradouro não informado"}`;
    deliveryDetailsPoints.append(item);
  });
}

async function showDeliveryDetails(id) {
  deliveryDetailsList.replaceChildren();
  deliveryDetailsPoints.replaceChildren();
  deliveryDetailsStatus.textContent = "Carregando detalhes...";
  deliveryDetailsStatus.dataset.state = "loading";
  deliveryDetailsDialog.showModal();

  try {
    const response = await getDeliveryById(id);
    const listRecord = deliveries.find((delivery) => String(getDeliveryId(delivery)) === String(id));
    const delivery = response && typeof response === "object" ? response : listRecord;
    if (!delivery) {
      throw new Error("A API não retornou os dados da entrega.");
    }

    renderDeliveryDetails(delivery);
    if (typeof response === "string") {
      deliveryDetailsStatus.textContent = "O endpoint individual retornou apenas o identificador; os detalhes abaixo vêm da listagem carregada.";
      deliveryDetailsStatus.dataset.state = "info";
    } else {
      deliveryDetailsStatus.textContent = "Detalhes carregados.";
      deliveryDetailsStatus.dataset.state = "success";
    }
  } catch (error) {
    deliveryDetailsStatus.textContent = `Não foi possível carregar os detalhes. ${error.message}`;
    deliveryDetailsStatus.dataset.state = "error";
  }
}

async function handleDeliveryAction(event) {
  if (!(event.target instanceof Element)) {
    return;
  }

  const button = event.target.closest("[data-delivery-action]");
  if (!button) {
    return;
  }

  const { deliveryAction: action, deliveryId: id } = button.dataset;
  if (!id) {
    deliveryActionsStatus.textContent = "A entrega selecionada não possui um ID válido.";
    deliveryActionsStatus.dataset.state = "error";
    return;
  }

  if (action === "details") {
    await showDeliveryDetails(id);
    return;
  }
  if (action === "cancel" && !window.confirm(`Deseja cancelar a entrega ${id}?`)) {
    return;
  }

  button.disabled = true;
  deliveryActionsStatus.textContent = action === "complete"
    ? `Concluindo entrega ${id}...`
    : `Cancelando entrega ${id}...`;
  deliveryActionsStatus.dataset.state = "loading";

  try {
    if (action === "complete") {
      await completeDelivery(id);
      deliveryActionsStatus.textContent = `Entrega ${id} concluída.`;
    } else {
      await cancelDelivery(id);
      deliveryActionsStatus.textContent = `Entrega ${id} cancelada.`;
    }
    deliveryActionsStatus.dataset.state = "success";
    await loadDeliveries();
  } catch (error) {
    deliveryActionsStatus.textContent = `Não foi possível atualizar a entrega ${id}. ${error.message}`;
    deliveryActionsStatus.dataset.state = "error";
  } finally {
    button.disabled = false;
  }
}

deliveryForm.addEventListener("submit", submitDelivery);
deliveryPointCepInput.addEventListener("input", handleDeliveryPointCepInput);
deliveryPointCepInput.addEventListener("blur", handleDeliveryPointCepBlur);
deliveryPointFields.forEach((field) => {
  field.addEventListener("input", updateAddDeliveryPointButton);
});
addDeliveryPointButton.addEventListener("click", addDeliveryPoint);
deliveryPointsList.addEventListener("click", (event) => {
  if (!(event.target instanceof Element)) {
    return;
  }
  const button = event.target.closest("[data-point-id]");
  if (button) {
    removeDeliveryPoint(button.dataset.pointId);
  }
});
deliveriesTableBody.addEventListener("click", handleDeliveryAction);
deliveryFilterButtons.forEach((button) => {
  button.addEventListener("click", () => {
    activeDeliveryFilter = button.dataset.deliveryFilter;
    deliveryFilterButtons.forEach((filterButton) => {
      filterButton.setAttribute("aria-pressed", String(filterButton === button));
    });
    renderDeliveries();
  });
});
refreshDeliveriesButton.addEventListener("click", loadDeliveries);
closeDeliveryDetailsButton.addEventListener("click", () => deliveryDetailsDialog.close());

loadCities();
renderCollectionPoints();
loadDeliveries();
