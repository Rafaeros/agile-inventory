<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
    <%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
        <!doctype html>
        <html lang="pt-br">

        <head>
            <meta charset="utf-8">
            <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
            <link rel="shortcut icon" href="<c:url value='/img/favicon.ico'/>">
            <link href="<c:url value='/css/main.css'/>" rel="stylesheet">
            <title>Ordem de Produção</title>
        </head>

        <body class="bg-gray-100 h-20 text-5xl fixed top-0 inset-x-0 text-center bg-slate-300/50">
            <!-- Navbar -->
            <div class="navbar">
                <nav class="bg-white shadow-md p-10 flex relative items-center">
                    <a href="<c:url value='/'/>" class="absolute left-10 top-1/2 transform -translate-y-1/2">
                        <img src="<c:url value='/img/back.png'/>" alt="Voltar"
                            class="h-8 w-8 hover:scale-110 transition-transform duration-300">
                    </a>
                    <div class="logo mx-auto">
                        <a href="<c:url value='/'/>" class="href flex justify-center items-center">
                            <img src="<c:url value='/img/logo.png'/>" alt="Logo" class="h-12 w-12 mr-4">
                            <h1 class="text-2xl text-blue-400">Materiais da Ordem De Produção</h1>
                        </a>
                    </div>
                </nav>
            </div>

            <!-- Main Content -->
            <div class="main-content flex flex-col items-center justify-start w-full min-h-screen">
                <!-- Main Content Header-->
                <div class="flex flex-row items-center justify-evenly mt-10 gap-5">
                    <h2 class="text-3xl font-bold">Lista de Ordem de Produção</h2>
                    <a href="#"
                        class="flex flex-row items-center justify-center gap-3 bg-blue-500 hover:bg-blue-700 text-white font-bold text-3xl px-4 py-2 rounded"><img
                            src="<c:url value='/img/plus.png'/>" class="h-6 w-6" alt="+"><button id="openModalBtn">Adicionar</button></a>
                </div>

                <!-- Content -->
                <div class="flex flex-col items-center justify-start mt-10 w-full gap-5">
                    <c:if test="${not empty orders}">
                        <!-- UL principal agora ocupa toda a largura -->
                        <ul class="w-screen flex flex-col items-center gap-5 px-10">
                            <c:forEach var="order" items="${orders}">

                                <!-- Linha principal da OP -->
                                <li id="order-${order.id}"
                                    class="w-full bg-white p-5 rounded shadow-md hover:shadow-lg transition-shadow duration-300 flex justify-between items-center">

                                    <div class="flex flex-row items-center justify-between gap-10 w-full">
                                        <span class="font-bold text-3xl">${order.orderNumber}</span>
                                        <span class="font-bold text-3xl">${order.description}</span>
                                        <span class="font-bold text-3xl">Quantidade:
                                            ${order.quantity} PÇS</span>
                                    </div>

                                    <button class="ml-4" onclick="toggleDropdown('drop-${order.id}')">
                                        <img src="<c:url value='/img/down-arrow.png'/>" alt="Mostrar"
                                            class="h-8 w-8 transition-transform duration-300"
                                            id="icon-drop-${order.id}">
                                    </button>
                                </li>

                                <!-- Dropdown dos materiais -->
                                <ul id="drop-${order.id}"
                                    class="hidden w-full bg-gray-50 p-5 rounded-lg shadow-inner flex-col gap-4 transition-all duration-300">
                                    <c:forEach var="material" items="${order.materials}">
                                        <li class="flex justify-between items-center bg-white p-4 rounded shadow-sm">
                                            <span class="text-gray-600 font-bold text-2xl">${material.code}</span>
                                            <span
                                                class="text-gray-600 font-bold text-2xl">${material.description}</span>
                                            <span class="text-gray-600 font-bold text-2xl">Qtd:
                                                ${material.quantity}</span>
                                        </li>
                                    </c:forEach>
                                </ul>
                            </c:forEach>
                        </ul>
                    </c:if>
                </div>
            </div>

            <div id="modal" class="fixed inset-0 z-50 hidden items-center justify-start" aria-hidden="true" aria-labelledby="modal-title" role="dialog">
                    <div id="backdrop" class="absolute inset-0 bg-black/60 transition-opacity"></div>
                    
                    <!-- Container central do modal -->
                    <div class="fixed inset-0 flex justify-start items-center h-full w-full p-4">
                        <div id="modalPanel"
                            class="bg-white rounded-2xl shadow-xl w-full mx-auto transform transition-all scale-95 opacity-0"
                            role="document"
                            tabindex="-1">
                            <!-- Cabeçalho -->
                            <div class="px-6 py-4 border-b">
                                <h2 id="modal-title" class="text-lg font-semibold text-gray-800">Adicionar Ordem de Produção</h2>
                            </div>
                            
                            <!-- Procurar Itens pelo ID da ordem de produção -->
                            <div id="search-section">
                                <input type="number" id="searchInput"
                                        class="w-7/12 border rounded-md px-3 py-2 mb-4 focus:outline-none focus:ring-2 focus:ring-blue-400"
                                        placeholder="ID da Ordem de Produção EX: 16586558" />

                                <button id="searchBtn"
                                        class="w-7/12 bg-blue-600 text-white py-2 rounded-md hover:bg-blue-700 transition-colors">
                                    Procurar
                                </button>
                                <p id="errorMsg" class="text-red-600 text-sm mt-2 hidden">ID não encontrado.</p>
                                <p id="loadingMsg" class="text-green-600 text-sm mt-2 hidden">Buscando dados...</p>
                            </div>
                            
                            <!-- Corpo do forms -->
                            <div class="px-6 py-4">
                                <form id="addOrderForm">
                                    <label class="block text-sm text-gray-700 mb-2">Nome</label>
                                    <input type="text" name="name" class="w-7/12 px-3 py-2 border rounded-md mb-4 focus:outline-none focus:ring-2 focus:ring-blue-300" />

                                    <div class="flex justify-between gap-3">
                                        <button type="button" id="cancelBtn" class="px-4 py-2 bg-red-500 text-white rounded-md hover:bg-red-600">Cancelar</button>
                                        <button type="submit" class="px-4 py-2 bg-green-600 text-white rounded-md hover:bg-green-700">Salvar</button>
                                    </div>
                                </form>
                            </div>
                        </div>
                    </div>
                </div>

            <c:if test="${not empty message}">
                <p class="text-xl">${message}</p>
            </c:if>

            <script>
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

                document.getElementById('searchBtn').addEventListener('click', async () => {
                    const orderId = document.getElementById('searchInput').value.trim();
                    if (!orderId) return alert("Digite um ID!");

                    console.log('Buscando dados para ID:', orderId);

                    fetch('http://10.48.0.188:8000/scraping/' + orderId)
                        .then(response => {
                            if (!response.ok) {
                                throw new Error('Erro na requisição: ' + response.status);
                            }
                            return response.json(); // Parse a resposta como JSON
                        })
                        .then(data => {
                            console.log('Dados recebidos:', data); // Aqui você manipula os dados da API
                        })
                        .catch(error => {
                            console.error('Erro:', error); // Caso dê algum erro na requisição
                        });
                });



                (function() {
                    const openBtn = document.getElementById('openModalBtn');
                    const modal = document.getElementById('modal');
                    const modalPanel = document.getElementById('modalPanel');
                    const backdrop = document.getElementById('backdrop');
                    const cancelBtn = document.getElementById('cancelBtn');
                    const firstFocusableSelector = 'button, [href], input, select, textarea, [tabindex]:not([tabindex="-1"])';
                    let lastFocused = null;

                    openBtn.addEventListener('click', function(e) {
                        e.preventDefault();
                        showModal();
                    });

                    function showModal() {
                        lastFocused = document.activeElement;
                        modal.classList.remove('hidden');
                        document.body.style.overflow = 'hidden'; // evita scroll por baixo do modal
                        // animação simples usando classes do Tailwind (adapte conforme preferir)
                        requestAnimationFrame(() => {
                        modalPanel.classList.remove('scale-95', 'opacity-0');
                        modalPanel.classList.add('scale-100', 'opacity-100');
                        });
                        modal.setAttribute('aria-hidden', 'false');
                        // focar primeiro elemento do modal
                        const focusable = modalPanel.querySelectorAll(firstFocusableSelector);
                        if (focusable.length) focusable[0].focus();
                        // adicionar listeners
                        document.addEventListener('keydown', onKeyDown);
                        backdrop.addEventListener('click', hideModal);
                    }

                    function hideModal() {
                        // animação de fechamento
                        modalPanel.classList.remove('scale-100', 'opacity-100');
                        modalPanel.classList.add('scale-95', 'opacity-0');
                        modal.setAttribute('aria-hidden', 'true');

                        // depois da animação (200ms), esconder de fato
                        setTimeout(() => {
                        modal.classList.add('hidden');
                        document.body.style.overflow = '';
                        if (lastFocused) lastFocused.focus();
                        }, 180);

                        // remover listeners
                        document.removeEventListener('keydown', onKeyDown);
                        backdrop.removeEventListener('click', hideModal);
                    }

                    function onKeyDown(e) {
                        if (e.key === 'Escape') {
                        hideModal();
                        } else if (e.key === 'Tab') {
                        // simples foco-trap
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

                   

                    // eventos
                    openBtn.addEventListener('click', showModal);
                    cancelBtn.addEventListener('click', hideModal);

                    // exemplo de submit (prevenir reload e fechar)
                    document.getElementById('addOrderForm').addEventListener('submit', function(e) {
                        e.preventDefault();
                        // aqui você enviaria via fetch/AJAX ou faria lógica
                        alert('Salvo (exemplo). Fecha o modal.');
                        hideModal();
                    });
                })();
            </script>


        </body>

        </html>