const materialName = document.querySelector("#material-name");
const materialDescription = document.querySelector("#material-description");
const materialDecomposition = document.querySelector("#material-decomposition");
const materialWeight = document.querySelector("#material-weight");
const materialCounter = document.querySelector("#material-counter");
const materialsStatus = document.querySelector("#materials-status");
const materialsLayout = document.querySelector(".materials-layout");
const refreshMaterialsButton = document.querySelector("#refresh-materials");
const previousMaterialButton = document.querySelector("#previous-material");
const nextMaterialButton = document.querySelector("#next-material");
const previewImage = document.querySelector("#preview-image");
const previewPlaceholder = document.querySelector("#preview-placeholder");
let materials = [];
let currentMaterialIndex = 0;
let imageRequestId = 0;

function normalizeMaterial(material) {
  const weight = material.peso;

  return {
    name: material.nomeMaterial || material.nome || "Material sem nome",
    description: material.descricao || "Descrição não informada.",
    decomposition: material.decomposicao ?? material.tempoDecomposicao ?? "Não informado",
    weight: typeof weight === "number" ? `${weight} g` : weight || "Não informado",
    imageUrl: material.urlImagem || material.imagemUrl || ""
  };
}

function getSafeImageUrl(imageUrl) {
  if (!imageUrl) {
    return "";
  }

  try {
    const parsedUrl = new URL(imageUrl, window.location.href);
    return parsedUrl.protocol === "http:" || parsedUrl.protocol === "https:"
      ? parsedUrl.href
      : "";
  } catch {
    return "";
  }
}

function showMaterial(index) {
  if (materials.length === 0) {
    return;
  }

  currentMaterialIndex = (index + materials.length) % materials.length;
  const material = materials[currentMaterialIndex];
  const requestId = ++imageRequestId;

  materialName.textContent = material.name;
  materialDescription.textContent = material.description;
  materialDecomposition.textContent = String(material.decomposition);
  materialWeight.textContent = String(material.weight);
  materialCounter.textContent = `${currentMaterialIndex + 1} / ${materials.length}`;

  const safeImageUrl = getSafeImageUrl(material.imageUrl);
  previewImage.hidden = true;
  previewImage.removeAttribute("src");
  previewImage.alt = "";
  previewPlaceholder.hidden = false;
  previewPlaceholder.textContent = safeImageUrl ? "Carregando imagem..." : "Imagem não disponível para este material.";

  if (safeImageUrl) {
    previewImage.alt = `Imagem de ${material.name}`;
    previewImage.onload = () => {
      if (requestId !== imageRequestId) {
        return;
      }
      previewImage.hidden = false;
      previewPlaceholder.hidden = true;
    };
    previewImage.onerror = () => {
      if (requestId !== imageRequestId) {
        return;
      }
      previewImage.hidden = true;
      previewPlaceholder.hidden = false;
      previewPlaceholder.textContent = "Não foi possível carregar esta imagem.";
    };
    previewImage.src = safeImageUrl;
  }
}

function renderMaterials(materialList) {
  materials = materialList.map(normalizeMaterial);

  if (materials.length === 0) {
    imageRequestId += 1;
    materialsStatus.textContent = "Nenhum material encontrado.";
    materialsStatus.dataset.state = "empty";
    materialName.textContent = "Nenhum material disponível";
    materialDescription.textContent = "";
    materialDecomposition.textContent = "-";
    materialWeight.textContent = "-";
    materialCounter.textContent = "0 / 0";
    previewImage.hidden = true;
    previewImage.removeAttribute("src");
    previewImage.alt = "";
    previewPlaceholder.hidden = false;
    previewPlaceholder.textContent = "A imagem aparecerá aqui";
    previousMaterialButton.disabled = true;
    nextMaterialButton.disabled = true;
    return;
  }

  currentMaterialIndex = 0;
  previousMaterialButton.disabled = materials.length < 2;
  nextMaterialButton.disabled = materials.length < 2;
  showMaterial(currentMaterialIndex);
  materialsStatus.textContent = `${materials.length} ${materials.length === 1 ? "material encontrado" : "materiais encontrados"}.`;
  materialsStatus.dataset.state = "success";
}

async function loadMaterials() {
  materialsLayout.setAttribute("aria-busy", "true");
  materialsStatus.textContent = "Carregando materiais...";
  materialsStatus.dataset.state = "loading";
  refreshMaterialsButton.disabled = true;

  try {
    const response = await getMaterials();
    if (!Array.isArray(response)) {
      throw new Error("A resposta da API não contém uma lista de materiais.");
    }
    renderMaterials(response);
  } catch (error) {
    imageRequestId += 1;
    materialsStatus.textContent = `Não foi possível carregar os materiais. ${error.message}`;
    materialsStatus.dataset.state = "error";
    materialName.textContent = "Materiais indisponíveis";
    materialDescription.textContent = "";
    materialDecomposition.textContent = "-";
    materialWeight.textContent = "-";
    materialCounter.textContent = "0 / 0";
    previewImage.hidden = true;
    previewImage.removeAttribute("src");
    previewImage.alt = "";
    previewPlaceholder.hidden = false;
    previewPlaceholder.textContent = "Tente atualizar a lista.";
    previousMaterialButton.disabled = true;
    nextMaterialButton.disabled = true;
  } finally {
    materialsLayout.setAttribute("aria-busy", "false");
    refreshMaterialsButton.disabled = false;
  }
}

previousMaterialButton.addEventListener("click", () => showMaterial(currentMaterialIndex - 1));
nextMaterialButton.addEventListener("click", () => showMaterial(currentMaterialIndex + 1));
refreshMaterialsButton.addEventListener("click", loadMaterials);
loadMaterials();
