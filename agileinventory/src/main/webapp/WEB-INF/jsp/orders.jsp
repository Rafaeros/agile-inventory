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
                            <img src="<c:url value='/img/logo.png'/>" alt="Logo" class="h-18 w-25 mr-4">
                            <h1 class="text-2xl text-purple-400">Materiais da Ordem De Produção</h1>
                        </a>
                    </div>
                </nav>
            </div>

            <!-- Main Content -->
            <div class="main-content flex flex-col items-center justify-start w-full min-h-screen overflow-y-auto">
                <!-- Main Content Header-->
                <div class="flex flex-row items-center justify-evenly mt-10 gap-5">
                    <h2 class="text-3xl font-bold">Lista de Ordem de Produção</h2>
                    <a href="#"
                        class="flex flex-row items-center justify-center gap-3 bg-purple-500 hover:bg-purple-700 text-white font-bold text-3xl px-4 py-2 rounded"><img
                            src="<c:url value='/img/plus.png'/>" class="h-6 w-6" alt="+"><button
                            id="openModalBtn">Adicionar</button></a>
                </div>

                <!-- Content -->
                <div class="flex flex-col items-center justify-start mt-10 w-full gap-5">
                    <c:if test="${not empty orders}">
                        <!-- UL principal agora ocupa toda a largura -->
                        <ul class="w-full max-h-[70vh] overflow-y-auto flex flex-col items-center gap-5 px-10">
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

            <div id="modal" class="fixed inset-0 z-50 hidden items-center mt-5 justify-start max-h-[90vh]"
                aria-hidden="true" aria-labelledby="modal-title" role="dialog">
                <div id="backdrop" class="absolute inset-0 bg-black/60 transition-opacity"></div>

                <!-- Container central do modal -->
                <div
                    class="fixed inset-0 flex justify-start items-center h-full w-full p-4 max-h[90vh] overflow-y-auto">
                    <div id="modal-panel"
                        class="bg-white rounded-2xl shadow-xl w-full h-full mx-auto transform transition-all opacity-0"
                        role="document" tabindex="-1">
                        <!-- Cabeçalho -->
                        <div class="p-6 border-b">
                            <h2 id="modal-title" class="text-lg font-semibold text-gray-800">Adicionar Ordem de Produção
                            </h2>
                        </div>

                        <!-- Procurar Itens pelo ID da ordem de produção -->
                        <div id="search-section">
                            <form action="<c:url value='/orders/scrape' />" method="get"
                                class="flex flex-row items-center justify-center m-2 w-full h-full gap-5"
                                id="search-form">
                                <input name="orderId" type="number" id="searchInput"
                                    class="w-6/12 h-10 border rounded-md px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-400 text-sm"
                                    placeholder="ID da Ordem de Produção" />
                                <button id="searchBtn"
                                    class="flex items-center justify-center p-3 bg-purple-600 text-white font-bold rounded-md hover:bg-purple-700 transition-colors cursor-pointer">
                                    <img src="<c:url value='/img/search.png'/>" alt="Procurar" class="h-6 w-6">
                                </button>
                            </form>
                            <p id="existsHint" class="hidden font-bold text-sm text-yellow-500 m-3">Ordem de Produção já
                                Cadastrada!</p>
                            <p id="searchHint" class="hidden font-bold text-sm text-yellow-500 m-3">Insira o ID da Ordem
                                de Produção</p>
                        </div>

                        <!-- Corpo do forms -->
                        <div id="scraping-modal"
                            class="overflow-y-auto max-h-[80vh] h-full p-5 hidden flex-col items-center justify-start mt-10 mb-10 w-full gap-5">
                            <form id="scraping-form" class="flex flex-col items-center justify-start mt-10 w-full h-full gap-5">
                                <!-- Container dos materiais -->
                                <div id="materials-container"></div>
                                <button id="submit-order-btn" class="w-7/12 bg-purple-600 text-white py-2 m-5 font-bold rounded-md hover:bg-purple-700 transition-colors"
                                    type="submit">Salvar
                                </button>
                            </form>
                        </div>
                    </div>
                </div>
            </div>

            <c:if test="${not empty message}">
                <p class="text-xl">${message}</p>
            </c:if>

            <script src="<c:url value='/js/orders.js'/>"></script>
        </body>

        </html>