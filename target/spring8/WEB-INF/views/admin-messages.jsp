<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Admin Messages</title>
    <style>
        .message-card {
            border: 1px solid #ddd;
            padding: 15px;
            margin: 10px 0;
            border-radius: 5px;
            background-color: #f9f9f9;
        }
        .filter-form {
            margin-bottom: 20px;
            padding: 15px;
            background-color: #e9ecef;
            border-radius: 5px;
        }
    </style>
</head>
<body>
    <h1>Administrative Messages</h1>

    <div class="filter-form">
        <form method="get" action="messages">
            <input type="text" name="operation" value="${param.operation}" placeholder="Filter by operation">
            <input type="text" name="username" value="${param.username}" placeholder="Filter by username">
            <button type="submit">Filter</button>
            <a href="messages">Clear</a>
        </form>
    </div>

    <c:choose>
        <c:when test="${not empty messages}">
            <c:forEach items="${messages}" var="message">
                <div class="message-card">
                    <h3>Operation: <c:out value="${message.operation}"/></h3>

                    <!-- Простой вывод через bicycle.id -->
                    <p><strong>Bicycle ID:</strong>
                        <c:choose>
                            <c:when test="${not empty message.bicycle and not empty message.bicycle.id}">
                                <c:out value="${message.bicycle.id}"/>
                            </c:when>
                            <c:otherwise>
                                N/A
                            </c:otherwise>
                        </c:choose>
                    </p>

                    <p><strong>User:</strong> <c:out value="${message.username}"/></p>
                    <p><strong>Timestamp:</strong> <c:out value="${message.timestamp}"/></p>

                    <!-- Отладочная информация -->
                    <div style="background-color: #f0f0f0; padding: 5px; margin-top: 10px; font-size: 12px;">
                        <strong>Debug:</strong>
                        Bicycle object: ${not empty message.bicycle},
                        Bicycle: ${message.bicycle}
                    </div>
                </div>
            </c:forEach>
        </c:when>
        <c:otherwise>
            <p>No administrative messages found.</p>
        </c:otherwise>
    </c:choose>
</body>
</html>