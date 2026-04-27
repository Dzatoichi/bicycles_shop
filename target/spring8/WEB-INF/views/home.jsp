<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Главная</title>
</head>
<body>
    <h1>${message}</h1>
    <nav>
        <a href="about">О нас</a> |
        <a href="bicycles">Велосипеды</a> |
        <a href="services">Услуги</a>
        <c:if test="${isAuthenticated}">
             | <span>Добро пожаловать, ${username}!</span>
             |
             <form action="logout" method="post" style="display: inline;">
                 <sec:csrfInput />
                 <button type="submit" style="background: none; border: none; color: blue; text-decoration: underline; cursor: pointer;">
                     Выйти
                 </button>
             </form>
         </c:if>
        <c:if test="${not isAuthenticated}">
            | <a href="login">Войти</a>
            | <a href="register">Регистрация</a>
        </c:if>
    </nav>
</body>
</html>