const searchButton = document.getElementById("add-order-btn");
const dropDownButtons = document.querySelectorAll('button[onclick^="toggleDropdown"]');

function toggleDropdown(dropId) {
    const dropdown = document.getElementById(dropId);
    const icon = document.getElementById('icon-' + dropId);

    if (!dropdown) return;

    dropdown.classList.toggle('hidden');

    if (icon) {
        icon.classList.toggle('rotate-180');
    }
}



function openModal() {
    const addOrderModal = document.getElementById("add-order-modal");
    addOrderModal.classList.remove("hidden");
}

searchButton.addEventListener("click", openModal);
dropDownButtons.forEach(btn => {
        btn.addEventListener('click', (e) => {
            e.preventDefault(); // evita comportamento padrão do botão
            // Pega o valor do onclick que foi gerado pelo Thymeleaf
            const onclickAttr = btn.getAttribute('onclick');
            // Extrai o dropId usando regex
            const match = onclickAttr.match(/'drop-(\d+)'/);
            if (match) {
                toggleDropdown('drop-' + match[1]);
            }
        });
    });