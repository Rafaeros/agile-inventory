<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
  <%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <!doctype html>
    <html lang="pt-br">

    <head>
      <meta charset="utf-8">
      <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
      <link rel="shortcut icon" href="<c:url value='/img/favicon.ico'/>">
      <link href="<c:url value='/css/main.css'/>" rel="stylesheet">
      <title>Agile</title>
    </head>

    <body class="bg-gray-100 h-20 text-5xl fixed top-0 inset-x-0 text-center bg-slate-300/50">
      <!-- Navbar -->
      <div class="navbar">
        <nav class="bg-white shadow-md p-10 flex justify-center items-center">
          <img src="<c:url value='/img/logo.png'/>" alt="Logo" class="h-18 w-25 mr-4">
          <h1 class="text-2xl text-purple-600">Agile Inventory</h1>
        </nav>
      </div>

      <div class="main-content flex flex-col items-center justify-start min-h-screen pt-10 mt-5">
        <h2 class="text-4xl font-bold mb-6 text-gray-800">Bem vindo ao Agile Inventory</h2>
        <p class="text-lg text-gray-600 mb-8">Sua solução completa para gerenciamento de estoque.</p>
        <div class="flex flex-row gap-10 items-center justify-center">
          <a href="<c:url value='/materials'/>"
            class="bg-purple-500 text-white w-100 h-50 font-regular flex items-center justify-center rounded-lg hover:bg-purple-600 transition duration-300">Ver Materiais </a>
          <a href="<c:url value='/orders'/>"
          class="bg-purple-500 text-white w-100 h-50 font-regular flex items-center justify-center rounded-lg hover:bg-purple-600 transition duration-300">Ver Ordens de Produção </a>
        </div>
      </div>

    </body>

    </html>