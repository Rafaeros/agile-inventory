const openModalBtn = document.getElementById("add-order-btn");
const searchBtn = document.getElementById("searchBtn");
const addOrderModal = document.getElementById("add-order-modal");
const buttons = document.querySelectorAll("[data-dropdown]");
const searchInput = document.getElementById("searchInput");
const scanBtn = document.getElementById("scanBtn");
const camera = document.getElementById("camera");

function toggleDropdown(dropId) {
    const dropdown = document.getElementById(dropId);
    const icon = document.getElementById('icon-' + dropId);

    if (!dropdown) return;

    dropdown.classList.toggle('hidden');
    dropdown.classList.toggle('block');

    if (dropdown.classList.contains('hidden')) {
        icon.style.transform = 'rotate(0deg)';
    } else {
        icon.style.transform = 'rotate(180deg)';
    }
}

function openModal() {
    addOrderModal.classList.remove("hidden");
}

function closeModal() {
    searchInput.value = "";
    addOrderModal.classList.add("hidden");
}

searchBtn.addEventListener("click", (e) => {
  e.preventDefault
  if (searchInput.value === ''){
    document.getElementById("warning").classList.remove("hidden");
    return
  } else {
    document.getElementById("warning").classList.add("hidden");
  }
});

document.addEventListener("keydown", (e) => {
  if (e.key === "Escape") {
    const modals = document.querySelectorAll(".modal"); // todos os modais
    modals.forEach(modal => {
      modal.classList.add("hidden"); // esconde qualquer modal aberto
    });
  }
});

openModalBtn.addEventListener("click", () => {
    openModal();
});

// Orders Dropdown Buttons
buttons.forEach(button => {
    button.addEventListener("click", () => {
      const dropId = button.getAttribute("data-dropdown");
      const dropdown = document.getElementById(dropId);
      const icon = button.querySelector("img");

      if (!dropdown) return;

      dropdown.classList.toggle("hidden");
      icon.classList.toggle("rotate-180");
    });
});

// Barcode
const config = {
  inputStream: {
    name: "Live",
    type: "LiveStream",
    target: camera,
    constraints: {
      facingMode: "environment",
    },
  },
  decoder: {
    readers: ["code_128_reader", "ean_reader", "upc_reader", "ean_8_reader"],
  },
  locate: true,
};

function stopScanner() {
  Quagga.stop();
  camera.classList.add("hidden");
}

scanBtn.addEventListener("click", () => {

  Quagga.init(config, (err) => {
    if (err) {
      console.error("Erro ao inicializar o QuaggaJS:", err);
      alert("Erro ao acessar a câmera. Verifique as permissões.");
      stopScanner();
      return;
    }
    Quagga.start();
  });
});

Quagga.onDetected((result) => {
  const code = result.codeResult.code;
  console.log("Código detectado:", code);
  searchInput.value = code;
  stopScanner(); 
});

const closeModalBtn = document.getElementById("close-modal");
closeModalBtn.addEventListener('click', () => {
    if (Quagga.initialized) {
        stopScanner();
    }
});