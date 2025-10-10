const searchButton = document.getElementById("add-order-btn");
const buttons = document.querySelectorAll("[data-dropdown]");

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
    const addOrderModal = document.getElementById("add-order-modal");
    addOrderModal.classList.remove("hidden");
}

function closeModal() {
    const addOrderModal = document.getElementById("add-order-modal");
    const searchInput = document.getElementById("searchInput");
    searchInput.value = "";
    addOrderModal.classList.add("hidden");
}


document.addEventListener("keydown", (e) => {
  if (e.key === "Escape") {
    const modals = document.querySelectorAll(".modal"); // todos os modais
    modals.forEach(modal => {
      modal.classList.add("hidden"); // esconde qualquer modal aberto
    });
  }
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