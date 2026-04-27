<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
    <title>Bicycles Catalog</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 20px;
            position: relative;
        }
        .header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 20px;
        }
        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
        }
        th, td {
            border: 1px solid #ddd;
            padding: 8px;
            text-align: left;
            position: relative;
        }
        th {
            background-color: #f2f2f2;
            font-weight: bold;
        }
        tr:nth-child(even) {
            background-color: #f9f9f9;
        }
        .add-btn, .del-btn, .edit-mode-btn, .find-mode-btn{
            padding: 8px 16px;
            color: white;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            margin-left: 10px;
        }
        .add-btn {
            background-color: #4CAF50;
        }
        .add-btn:hover {
            background-color: #45a049;
        }
        .del-btn {
            background-color: #f44336;
        }
        .del-btn:hover {
            background-color: #d32f2f;
        }
        .edit-mode-btn {
            background-color: #2196F3;
        }
        .edit-mode-btn:hover {
            background-color: #0b7dda;
        }
        .find-mode-btn {
            background-color: #ffe63b;
        }
        .find-mode-btn:hover {
            background-color: #bdb76b;
        }
        .delete-btn, .edit-btn, .buy-btn {
            display: none;
            cursor: pointer;
            font-weight: bold;
            font-size: 18px;
            margin-left: 10px;
        }
        .delete-btn {
            color: #f44336;
        }
        .edit-btn {
            color: #2196F3;
        }
        .buy-btn {
            color: #4CAF50;
        }
        .show-delete .delete-btn {
            display: inline-block;
        }
        .show-edit .edit-btn {
            display: inline-block;
        }
        .show-buy .buy-btn {
            display: inline-block;
        }
        .modal {
            display: none;
            position: fixed;
            z-index: 1;
            left: 0;
            top: 0;
            width: 100%;
            height: 100%;
            overflow: auto;
            background-color: rgba(0,0,0,0.4);
        }
        .modal-content {
            background-color: #fefefe;
            margin: 15% auto;
            padding: 20px;
            border: 1px solid #888;
            width: 50%;
        }
        .close {
            color: #aaa;
            float: right;
            font-size: 28px;
            font-weight: bold;
            cursor: pointer;
        }
        .close:hover {
            color: black;
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
        .submit-btn {
            background-color: #4CAF50;
            color: white;
            padding: 10px 15px;
            border: none;
            border-radius: 4px;
            cursor: pointer;
        }
        .submit-btn:hover {
            background-color: #45a049;
        }
        .buy-mode-btn {
            background-color: #9c27b0;
            color: white;
            padding: 8px 16px;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            margin-left: 10px;
        }
        .buy-mode-btn:hover {
            background-color: #7b1fa2;
        }
    </style>
</head>
<body>
    <div class="header">
        <a href="./">Главная</a>
        <div>
            <c:if test="${isAuthenticated}">
                <c:if test="${isAdmin}">
                    <button class="add-btn" onclick="openAddModal()">Добавить</button>
                    <button class="del-btn" onclick="toggleDeleteMode()">Удалить</button>
                    <button class="edit-mode-btn" onclick="toggleEditMode()">Редактировать</button>
                </c:if>
                <button class="buy-mode-btn" onclick="toggleBuyMode()">Купить</button>
                <button class="find-mode-btn" onclick="openFindModal()">Поиск по цене</button>
                | <span>Добро пожаловать, ${username}!</span>
                |
                <form action="logout" method="post" style="display: inline;">
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                    <button type="submit" style="background: none; border: none; color: blue; text-decoration: underline; cursor: pointer;">
                        Выйти
                    </button>
                </form>
            </c:if>
            <c:if test="${not isAuthenticated}">
                <a href="login">Войти</a> |
                <a href="register">Регистрация</a>
            </c:if>
        </div>
    </div>

    <h1>Bicycles Catalog</h1>
    <table id="bicyclesTable">
        <thead>
            <tr>
                <th>ID</th>
                <th>Model</th>
                <th>Producer</th>
                <th>Producing Country</th>
                <th>Gears Number</th>
                <th>Cost</th>
                <th>Actions</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="bicycle" items="${bicycles}">
                <tr data-id="${bicycle.id}">
                    <td>${bicycle.id}</td>
                    <td>${bicycle.model}</td>
                    <td>${bicycle.producer}</td>
                    <td>${bicycle.producingCountry}</td>
                    <td>${bicycle.gearsNum}</td>
                    <td>
                        <fmt:formatNumber value="${bicycle.cost}" type="currency" currencySymbol="$"/>
                    </td>
                    <td>
                        <span class="delete-btn" onclick="deleteBicycle(${bicycle.id}, this)">×</span>
                        <span class="edit-btn" onclick="editBicycle(${bicycle.id})">✎</span>
                        <span class="buy-btn" onclick="buyBicycle(${bicycle.id}, this)">🛒</span>
                    </td>
                </tr>
            </c:forEach>
        </tbody>
    </table>

    <!-- Модальное окно добавления -->
    <div id="addModal" class="modal">
        <div class="modal-content">
            <span class="close" onclick="closeAddModal()">&times;</span>
            <h2>Добавить новый велосипед</h2>
            <form id="addForm" action="/bicycles/add" method="post">
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                <div class="form-group">
                    <label for="model">Model:</label>
                    <input type="text" id="model" name="model" required>
                </div>
                <div class="form-group">
                    <label for="producer">Producer:</label>
                    <input type="text" id="producer" name="producer" required>
                </div>
                <div class="form-group">
                    <label for="producingCountry">Producing Country:</label>
                    <input type="text" id="producingCountry" name="producingCountry" required>
                </div>
                <div class="form-group">
                    <label for="gearsNum">Gears Number:</label>
                    <input type="number" id="gearsNum" name="gearsNum" required>
                </div>
                <div class="form-group">
                    <label for="cost">Cost:</label>
                    <input type="number" step="0.01" id="cost" name="cost" required>
                </div>
                <button type="submit" class="submit-btn">Добавить</button>
            </form>
        </div>
    </div>

    <!-- Модальное окно редактирования -->
    <div id="editModal" class="modal">
        <div class="modal-content">
            <span class="close" onclick="closeEditModal()">&times;</span>
            <h2>Редактировать велосипед</h2>
            <form id="editForm">
                <input type="hidden" id="editId" name="id">
                <div class="form-group">
                    <label for="editModel">Model:</label>
                    <input type="text" id="editModel" name="model" required>
                </div>
                <div class="form-group">
                    <label for="editProducer">Producer:</label>
                    <input type="text" id="editProducer" name="producer" required>
                </div>
                <div class="form-group">
                    <label for="editProducingCountry">Producing Country:</label>
                    <input type="text" id="editProducingCountry" name="producingCountry" required>
                </div>
                <div class="form-group">
                    <label for="editGearsNum">Gears Number:</label>
                    <input type="number" id="editGearsNum" name="gearsNum" required>
                </div>
                <div class="form-group">
                    <label for="editCost">Cost:</label>
                    <input type="number" step="0.01" id="editCost" name="cost" required>
                </div>
                <button type="button" class="submit-btn" onclick="submitEditForm()">Сохранить</button>
            </form>
        </div>
    </div>

    <!-- Модальное окно поиска по цене -->
    <div id="findModal" class="modal">
        <div class="modal-content">
            <span class="close" onclick="closeFindModal()">&times;</span>
            <h2>Поиск велосипедов по цене</h2>
            <form id="findForm" action="/bicycles/find" method="get">
                <div class="form-group">
                    <label for="findCost">Миннимальная цена:</label>
                    <input type="number" step="0.01" id="findCost" name="cost" required>
                </div>
                <button type="submit" class="submit-btn">Найти</button>
            </form>
        </div>
    </div>

    <script>
        // Режим удаления
        function toggleDeleteMode() {
            const table = document.getElementById('bicyclesTable');
            table.classList.toggle('show-delete');
            table.classList.remove('show-edit', 'show-buy');
        }

        // Режим редактирования
        function toggleEditMode() {
            const table = document.getElementById('bicyclesTable');
            table.classList.toggle('show-edit');
            table.classList.remove('show-delete', 'show-buy');
        }

        // Режим покупки
        function toggleBuyMode() {
            const table = document.getElementById('bicyclesTable');
            table.classList.toggle('show-buy');
            table.classList.remove('show-delete', 'show-edit');
        }

        // Удаление велосипеда
        function deleteBicycle(id, element) {
            if (confirm('Вы уверены, что хотите удалить этот велосипед?')) {
                fetch('/api/bicycles/' + id, {
                    method: 'DELETE',
                    headers: {
                        'X-CSRF-TOKEN': '${_csrf.token}'
                    }
                })
                .then(response => {
                    if (response.ok) {
                        element.closest('tr').remove();
                        alert('Велосипед успешно удален!');
                    } else {
                        alert('Ошибка при удалении');
                    }
                })
                .catch(error => {
                    console.error('Error:', error);
                    alert('Ошибка при удалении');
                });
            }
        }

        // Покупка велосипеда
        function buyBicycle(id, element) {
            if (confirm('Вы уверены, что хотите купить этот велосипед?')) {
                fetch('/api/bicycles/buy/' + id, {
                    method: 'POST',
                    headers: {
                        'X-CSRF-TOKEN': '${_csrf.token}'
                    }
                })
                .then(response => {
                    if (response.ok) {
                        element.closest('tr').remove();
                        alert('Велосипед успешно куплен! Сообщение отправлено администратору.');
                    } else {
                        alert('Ошибка при покупке');
                    }
                })
                .catch(error => {
                    console.error('Error:', error);
                    alert('Ошибка при покупке');
                });
            }
        }

        // Редактирование велосипеда
        function editBicycle(id) {
            fetch('/api/bicycles/' + id, {
                headers: {
                    'Accept': 'application/json'
                }
            })
            .then(response => {
                if (!response.ok) throw new Error('Ошибка сети');
                return response.json();
            })
            .then(data => {
                document.getElementById('editId').value = data.id;
                document.getElementById('editModel').value = data.model;
                document.getElementById('editProducer').value = data.producer;
                document.getElementById('editProducingCountry').value = data.producingCountry;
                document.getElementById('editGearsNum').value = data.gearsNum;
                document.getElementById('editCost').value = data.cost;
                openEditModal();
            })
            .catch(error => {
                console.error('Ошибка:', error);
                alert('Не удалось загрузить данные: ' + error.message);
            });
        }

        function submitEditForm() {
            const formData = {
                id: document.getElementById('editId').value,
                model: document.getElementById('editModel').value,
                producer: document.getElementById('editProducer').value,
                producingCountry: document.getElementById('editProducingCountry').value,
                gearsNum: document.getElementById('editGearsNum').value,
                cost: document.getElementById('editCost').value
            };

            fetch('/api/bicycles/' + formData.id, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json',
                    'Accept': 'application/json',
                    'X-CSRF-TOKEN': '${_csrf.token}'
                },
                body: JSON.stringify(formData)
            })
            .then(response => {
                if (!response.ok) throw new Error('Ошибка обновления');
                alert('Велосипед успешно обновлен!');
                location.reload();
            })
            .catch(error => {
                console.error('Ошибка:', error);
                alert('Ошибка при обновлении: ' + error.message);
            });
        }

        // Управление модальными окнами
        function openAddModal() {
            document.getElementById('addModal').style.display = 'block';
        }

        function closeAddModal() {
            document.getElementById('addModal').style.display = 'none';
        }

        function openFindModal() {
            document.getElementById('findModal').style.display = 'block';
        }

        function closeFindModal() {
            document.getElementById('findModal').style.display = 'none';
        }

        function openEditModal() {
            document.getElementById('editModal').style.display = 'block';
        }

        function closeEditModal() {
            document.getElementById('editModal').style.display = 'none';
        }

        // Закрытие модальных окон при клике вне их
        window.onclick = function(event) {
            if (event.target == document.getElementById('addModal')) {
                closeAddModal();
            }
            if (event.target == document.getElementById('editModal')) {
                closeEditModal();
            }
            if (event.target == document.getElementById('findModal')) {
                closeFindModal();
            }
        }
    </script>
</body>
</html>