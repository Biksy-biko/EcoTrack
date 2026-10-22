const deliveryForm = document.querySelector("#delivery-form");
const originCitySelect = document.querySelector("#cidade-origem");
const originStateSelect = document.querySelector("#uf-origem");
const destinationCitySelect = document.querySelector("#cidade-destino");
const destinationStateSelect = document.querySelector("#uf-destino");
const deliveryStatus = document.querySelector("#delivery-status");
const deliverySubmit = document.querySelector("#delivery-submit");
const deliveriesListStatus = document.querySelector("#deliveries-list-status");
const deliveriesTableBody = document.querySelector("#deliveries-table-body");
const refreshDeliveriesButton = document.querySelector("#refresh-deliveries");
const deliveryFilterButtons = Array.from(document.querySelectorAll("[data-delivery-filter]"));

let availablePlaces = [];
let deliveries = [];
let activeDeliveryFilter = "all";

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
    const values = [
      delivery.idEntrega ?? delivery.id ?? "-",
      formatDeliveryPlace(delivery.cidadeOrigem, delivery.ufOrigem),
      formatDeliveryPlace(delivery.cidadeDestino, delivery.ufDestino)
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
  } catch (error) {
    deliveriesTableBody.replaceChildren();
    deliveriesListStatus.textContent = `Não foi possível carregar entregas. ${error.message}`;
    deliveriesListStatus.dataset.state = "error";
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
    addPlaceholder(originCitySelect, "Falha ao carregar cidades");
    addPlaceholder(destinationCitySelect, "Falha ao carregar cidades");
    deliveryStatus.textContent = `Não foi possível carregar as cidades. ${error.message}`;
    deliveryStatus.dataset.state = "error";
  } finally {
    deliveryForm.setAttribute("aria-busy", "false");
    deliverySubmit.disabled = availablePlaces.length === 0;
  }
}

async function submitDelivery(event) {
  event.preventDefault();
  if (!deliveryForm.reportValidity()) {
    return;
  }

  deliverySubmit.disabled = true;
  deliveryForm.setAttribute("aria-busy", "true");
  deliveryStatus.textContent = "Cadastrando entrega...";
  deliveryStatus.dataset.state = "loading";

  try {
    const origin = getSelectedPlace(originCitySelect, originStateSelect);
    const destination = getSelectedPlace(destinationCitySelect, destinationStateSelect);

    await createDelivery({
      cidadeOrigem: origin.city,
      ufOrigem: origin.state,
      cidadeDestino: destination.city,
      ufDestino: destination.state
    });

    deliveryForm.reset();
    deliveryStatus.textContent = "Entrega cadastrada com sucesso.";
    deliveryStatus.dataset.state = "success";
    await loadDeliveries();
  } catch (error) {
    deliveryStatus.textContent = `Não foi possível cadastrar a entrega. ${error.message}`;
    deliveryStatus.dataset.state = "error";
  } finally {
    deliveryForm.setAttribute("aria-busy", "false");
    deliverySubmit.disabled = availablePlaces.length === 0;
  }
}

deliveryForm.addEventListener("submit", submitDelivery);
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
loadCities();
loadDeliveries();
