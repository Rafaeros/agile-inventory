<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
  <%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <!doctype html>
    <html lang="pt-br">

    <head>
      <meta charset="utf-8">
      <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
      <link rel="shortcut icon" href="<c:url value='/img/favicon.ico'/>">
      <link href="<c:url value='/css/main.css'/>" rel="stylesheet">
      <title>Materiais</title>
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
              <h1 class="text-2xl text-purple-400">Materiais</h1>
            </a>
          </div>
        </nav>
      </div>

      <div class="main-content flex flex-col items-center justify-start min-h-screen">
        <div class="flex flex-col items-center justify-start mt-10 w-full gap-5">
          <c:if test="${not empty materials}">
            <!-- UL principal agora ocupa toda a largura -->
            <ul class="w-screen flex flex-col items-center gap-5 px-10">
              <c:forEach var="material" items="${materials}">
                
                <!-- Linha principal da OP -->
                <li id="material-${material.id}" 
                    class="w-full bg-white p-5 rounded shadow-md hover:shadow-lg transition-shadow duration-300 flex justify-between items-center">

                  <div class="flex flex-row items-center justify-between gap-10 w-full">
                    <span class="font-bold text-3xl">${material.productOrder.orderNumber}</span>
                    <span class="font-bold text-3xl">${material.code}</span>
                    <span class="font-bold text-3xl">${material.description}</span>
                    <span class="font-bold text-3xl">Quantidade: ${material.quantity}</span>
                  </div>
              </c:forEach>
            </ul>
          </c:if>
          <c:if test="${not empty message}">
            <p class="text-xl">${message}</p>
          </c:if>
        </div>
      </div>
    </body>
    </html>