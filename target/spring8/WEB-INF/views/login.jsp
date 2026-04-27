<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Login</title>
    <style>
        .login-container {
            max-width: 400px;
            margin: 50px auto;
            padding: 20px;
            border: 1px solid #ddd;
            border-radius: 5px;
        }
        .form-group {
            margin-bottom: 15px;
        }
        .form-group label {
            display: block;
            margin-bottom: 5px;
        }
        .form-group input {
            width: 100%;
            padding: 8px;
            box-sizing: border-box;
        }
        .error {
            color: red;
            margin-bottom: 15px;
        }
    </style>
</head>
<body>
    <div class="login-container">
        <h2>Login</h2>

        <c:if test="${param.error != null}">
            <div class="error">
                Неправильное имя пользователя или пароль
            </div>
        </c:if>

        <c:if test="${param.logout != null}">
            <div style="color: green;">
                Вы успешно вышли из системы
            </div>
        </c:if>

        <!-- Убедитесь, что action пустой или равен "/login" -->
        <form method="post" action="<c:url value='/login'/>">
            <div class="form-group">
                <label for="username">Username:</label>
                <input type="text" id="username" name="username" required>
            </div>
            <div class="form-group">
                <label for="password">Password:</label>
                <input type="password" id="password" name="password" required>
            </div>
            <div>
                <input type="submit" value="Sign In">
            </div>

            <!-- Добавьте CSRF токен если включен CSRF -->
            <!-- <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/> -->
        </form>

        <div style="margin-top: 20px;">
            <p>Тестовые пользователи:</p>
            <ul>
                <li>admin / qwerty (Администратор)</li>
                <li>user / password (Пользователь)</li>
            </ul>
        </div>
    </div>
</body>
</html>