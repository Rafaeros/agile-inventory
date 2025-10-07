const openBtn = document.getElementById('openModalBtn');
const modal = document.getElementById('modal');
const modalPanel = document.getElementById('modal-panel');
const backdrop = document.getElementById('backdrop');
const cancelBtn = document.getElementById('cancelBtn');
const firstFocusableSelector = 'button, [href], input, select, textarea, [tabindex]:not([tabindex="-1"])';
let lastFocused = null;



function toggleDropdown(id) {
    const content = document.getElementById(id);
    const icon = document.getElementById("icon-" + id);
    if (content.classList.contains("hidden")) {
        content.classList.remove("hidden");
        icon.classList.add("rotate-180");
    } else {
        content.classList.add("hidden");
        icon.classList.remove("rotate-180");
    }
}

function showModal() {
        lastFocused = document.activeElement;
        modal.classList.remove('hidden');
        document.body.style.overflow = 'hidden';

        requestAnimationFrame(() => {
        modalPanel.classList.remove('scale-95', 'opacity-0');
        modalPanel.classList.add('scale-100', 'opacity-100');
        });
        modal.setAttribute('aria-hidden', 'false');

        const focusable = modalPanel.querySelectorAll(firstFocusableSelector);
        if (focusable.length) focusable[0].focus();
        document.addEventListener('keydown', onKeyDown);
        backdrop.addEventListener('click', hideModal);
}

function hideModal() {
    modalPanel.classList.remove('opacity-100');
    modalPanel.classList.add('opacity-0');
    modal.setAttribute('aria-hidden', 'true');

    setTimeout(() => {
    modal.classList.add('hidden');
    document.body.style.overflow = '';
    if (lastFocused) lastFocused.focus();
    }, 180);

    document.removeEventListener('keydown', onKeyDown);
    backdrop.removeEventListener('click', hideModal);
    document.getElementById('searchInput').value = '';
    document.getElementById('searchBtn').disabled = false;
    document.getElementById('searchBtn').hidden = false;
    document.getElementById('searchInput').hidden = false;
    document.getElementById('materials-container').innerHTML = '';
    document.getElementById('scraping-modal').classList.add('hidden');
}

function onKeyDown(e) {
    if (e.key === 'Escape') {
        hideModal();
    } else if (e.key === 'Tab') {

    const focusable = Array.from(modalPanel.querySelectorAll(firstFocusableSelector)).filter(el => !el.disabled);
    if (focusable.length === 0) {
        e.preventDefault();
        return;
    }
    const first = focusable[0];
    const last = focusable[focusable.length - 1];
    if (!e.shiftKey && document.activeElement === last) {
        e.preventDefault();
        first.focus();
    } else if (e.shiftKey && document.activeElement === first) {
        e.preventDefault();
        last.focus();
    }
    }
}

async function populateScrapingForm(orderData){
    const container = document.getElementById('materials-container');
    container.classList.add('flex', 'flex-col', 'w-full', 'max-h-[70vh]', 'overflow-y-auto', 'items-center', 'justify-start', 'gap-5', 'border', 'border-gray-300', 'p-3', 'rounded-md');
    container.innerHTML = ""; // limpa o container antes de adicionar
    const searchInput = document.getElementById('searchInput');
    searchInput.hidden = true;
    const searchBtn = document.getElementById('searchBtn')
    searchBtn.hidden = true;
    searchBtn.disabled = true;  
    
    const orderHeader = document.createElement("div");
    const orderNumberLabel = document.createElement("h3");
    const orderCodeLabel = document.createElement("h3");
    const orderQuantityLabel = document.createElement("h3");

    orderNumberLabel.classList.add('order-number','text-md', 'font-bold', 'truncate');
    orderCodeLabel.classList.add('order-code', 'text-md', 'font-bold', 'truncate');
    orderQuantityLabel.classList.add('order-qty', 'text-md', 'font-bold', 'truncate');

    orderCode = Object.keys(orderData)[0];
    orderNumberLabel.textContent = `${orderCode}`;
    orderCodeLabel.textContent = `${orderData[orderCode].product_material}`;
    orderQuantityLabel.textContent = `${orderData[orderCode].product_quantity}`;

    orderHeader.appendChild(orderNumberLabel);
    orderHeader.appendChild(orderCodeLabel);
    orderHeader.appendChild(orderQuantityLabel);


    orderHeader.classList.add('material-header', 'w-full', 'flex', 'items-center', 'justify-around', 'mb-4', 'p-3', 'border-b', 'border-gray-300');
    container.appendChild(orderHeader);

    orderHeader.classList.add('w-full', 'mb-4', 'p-3', 'border-b', 'border-gray-600', 'text-center');


    orderData[orderCode].materials.forEach((material, index) => {
        const div = document.createElement('div');
        div.classList.add('material-item', 'w-full', 'mb-2', 'flex', 'flex-row', 'justify-start', 'gap-4', 'items-center', 'p-2', 'border', 'border-gray-600', 'rounded-md');

        // Código
        const codeLabel = document.createElement('label');
        codeLabel.textContent = `Cód. ${material.code}`;
        codeLabel.classList.add('text-lg', 'font-semibold', 'text-gray-900', 'w-48', 'flex-shrink-0');
        div.appendChild(codeLabel);
        
        // Descrição (readonly ou input se quiser permitir editar)
        const desc = document.createElement('p');
        desc.textContent = material.description;
        desc.classList.add('text-base', 'text-gray-600', 'flex-grow', 'truncate');
        div.appendChild(desc);
        
        // Quantidade (input editável)
        const qtyInput = document.createElement('input');
        qtyInput.type = 'number';
        qtyInput.name = `quantity_${index}`;
        qtyInput.value = material.quantity;
        qtyInput.classList.add("border", "border-blue-300", "rounded-md", "p-2", "text-center", "text-xl", "font-bold", "w-30", 'flex-shrink-0');
        div.appendChild(qtyInput);
        container.appendChild(div);
    });
    document.getElementById('scraping-modal').classList.remove('hidden');
}

async function fetchOrderData() {
    const orderId = document.getElementById('searchInput').value;
    const hint = document.getElementById('searchHint');
    const existsHint = document.getElementById('existsHint');

    if (!orderId) {
        hint.classList.remove('hidden');
        return;
    }
    try {
        const response = await fetch(`/orders/scrape?orderId=${orderId}`);
        if (!response.ok) {
            throw new Error(`Erro na requisição: ${response.status}`);
        }
        const data = await response.json();
        
        if (data.orderData.error) {
            alert("Erro ao buscar dados da ordem, erro de conexão ou ordem inválida.");
            return;
        };

        if (data.orderExists) {
            existsHint.classList.remove('hidden');
            hint.classList.add('hidden');
            return;
        }
        console.log('Dados recebidos:', data);
        if (!data || data.materials) return;
        existsHint.classList.add('hidden');
        hint.classList.add('hidden');

        const orderData = data.orderData
        await populateScrapingForm(orderData);
    } catch (error) {
        console.error('Erro:', error);
        throw error;
    }
}

async function addOrder() {
    let payload = {};

    const orderIdElement = document.getElementById('searchInput');

    // 1. Encontre o container pai que armazena as informações da ordem
    // (O .material-header é criado dentro de populateScrapingForm)
    const orderHeader = document.querySelector('.material-header'); 

    if (!orderHeader) {
        console.error('Erro: Cabeçalho da ordem (.material-header) não encontrado. A ordem deve ser buscada primeiro.');
        alert('Erro: Busque e carregue os dados da Ordem de Produção antes de salvar.');
        return; 
    }

    // 2. Busque os elementos internos USANDO SUAS CLASSES
    // Você não atribuiu IDs, apenas classes, por isso usamos querySelector(Classe)
    const orderNumberElement = orderHeader.querySelector('.order-number'); 
    const productCodeElement = orderHeader.querySelector('.order-code');
    const productQtyElement = orderHeader.querySelector('.order-qty');
    
    // Verificação de segurança adicional
    if (!orderNumberElement || !productCodeElement || !productQtyElement) {
         console.error('Erro: Elementos internos da ordem não encontrados. Verifique a função populateScrapingForm.');
         return;
    }

    payload = {
        orderId: parseInt(orderIdElement.value),
        // Agora, os elementos não são nulos e podemos acessar o textContent
        orderNumber: orderNumberElement.textContent,
        description: productCodeElement.textContent,
        quantity: parseInt(productQtyElement.textContent),
        materials: []
    }
    
    // O restante do código permanece o mesmo, pois as classes dos materiais (.material-item)
    // e os inputs dentro delas estão sendo buscados corretamente.
    const materialItems = document.querySelectorAll('.material-item');
    materialItems.forEach(item => {
        const code = item.querySelector('label').textContent.replace('Cód. ', '');
        const description = item.querySelector('p').textContent;
        const quantity = parseFloat(item.querySelector('input').value);
        payload.materials.push({ code, description, quantity });
    });
    
    console.log('Payload a ser enviado:', payload);
    try {
        const response = await fetch('/orders', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(payload)
        });
        if (!response.ok) {
            alert('Erro na requisição. Ver console para detalhes.');
            throw new Error(`Erro na requisição: ${response.status}`);
        }
        const result = await response.json();
        console.log('Resposta do servidor:', result);
        alert('Ordem de Produção e Materiais salva com sucesso!');
    } catch (error) {
        console.error('Erro:', error);
        alert('Erro ao salvar a Ordem de Produção e Materiais.');
    }
}

document.getElementById('searchBtn').addEventListener('click', async (event) => {
    event.preventDefault();
    fetchOrderData();
})


openBtn.addEventListener('click', function(e) {
    e.preventDefault();
    showModal();
});

    
openBtn.addEventListener('click', showModal);
cancelBtn.addEventListener('click', hideModal);


const scrapingForm = document.getElementById('scraping-form');
scrapingForm.addEventListener('submit', async (e) => {
    e.preventDefault(); 
    
    console.log('Formulário enviado');
    await addOrder();
});