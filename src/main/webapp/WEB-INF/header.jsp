<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <title>Project Quest</title>
    <!-- Bootstrap core CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
    <style>
        .border-bottom {
            border-bottom: 1px solid #dee2e6 !important;
        }
    </style>
</head>
<body>
<header class="d-flex flex-wrap align-items-center justify-content-center justify-content-md-between py-3 border-bottom">
    <div class="col-md-3 mb-2 mb-md-0">
        <svg width="32px" height="32px" viewBox="0 0 1024 1024" class="icon" version="1.1"
             xmlns="http://www.w3.org/2000/svg" fill="#000000">
            <g id="SVGRepo_bgCarrier" stroke-width="0"></g>
            <g id="SVGRepo_tracerCarrier" stroke-linecap="round" stroke-linejoin="round"></g>
            <g id="SVGRepo_iconCarrier">
                <path d="M640 213.333333L384 128 128 213.333333v682.666667l256-85.333333 256 85.333333 256-85.333333V128z"
                      fill="#FFECB3"></path>
                <path d="M384 128v682.666667l256 85.333333V213.333333z" fill="#FFE082"></path>
                <path d="M590.677333 607.146667c0-24.106667 2.944-43.328 8.832-57.6 5.909333-14.293333 16.704-28.373333 32.448-42.24 15.701333-13.824 26.176-25.130667 31.402667-33.834667 5.205333-8.704 7.786667-17.877333 7.786667-27.541333 0-29.12-13.546667-43.669333-40.618667-43.669334-12.864 0-23.125333 3.925333-30.848 11.754667-7.722667 7.850667-11.776 18.666667-12.117333 32.448H512c0.341333-32.874667 11.072-58.624 32.170667-77.248 21.077333-18.56 49.877333-27.882667 86.336-27.882667 36.821333 0 65.365333 8.832 85.717333 26.496 20.266667 17.642667 30.442667 42.538667 30.442667 74.752 0 14.656-3.285333 28.458667-9.898667 41.450667-6.613333 13.013333-18.154667 27.434667-34.645333 43.285333l-21.077334 19.882667c-13.205333 12.586667-20.757333 27.285333-22.677333 44.181333l-1.045333 15.744h-66.645334z m-7.594666 79.104c0-11.498667 3.968-20.949333 11.882666-28.458667 7.893333-7.445333 18.005333-11.178667 30.336-11.178667s22.442667 3.754667 30.336 11.178667c7.914667 7.509333 11.882667 16.96 11.882667 28.458667 0 11.285333-3.882667 20.650667-11.605333 28.010666-7.744 7.36-17.962667 11.072-30.613334 11.072s-22.869333-3.712-30.634666-11.093333c-7.701333-7.338667-11.584-16.704-11.584-27.989333z"
                      fill="#9C27B0"></path>
            </g>
        </svg>
    </div>

    <ul class="nav col-12 col-md-auto mb-2 justify-content-center mb-md-0">
        <li><a href="/" class="nav-link px-2 link-secondary">Главная</a></li>
        <li><a href="/quests-list" class="nav-link px-2">Квесты</a></li>
        <li><a href="/statistics" class="nav-link px-2">Статистика</a></li>
    </ul>

    <div class="col-md-3 text-end">
        <c:choose>
            <c:when test="${sessionScope.userId > 0}">
                <div class="btn"><b>${sessionScope.username}</b></div>
                <button onclick="document.location='/logout'" type="button" class="btn btn-outline-primary me-2">Выйти</button>
            </c:when>
            <c:otherwise>
                <button onclick="document.location='/login'" type="button" class="btn btn-outline-primary me-2">Войти</button>
                <button onclick="document.location='/signup'" type="button" class="btn btn-primary">Зарегистрироваться</button>
            </c:otherwise>
        </c:choose>
    </div>
</header>