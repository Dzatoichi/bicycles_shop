# Bicycle Shop

Веб-приложение для управления магазином велосипедов на основе **Spring MVC**. Поддерживает полноценный веб-интерфейс (JSP), REST API, систему ролей, а также асинхронный обмен сообщениями через **Apache ActiveMQ Artemis (JMS)**.

---

## Стек технологий

| Слой | Технологии |
|---|---|
| **Backend** | Java 11, Spring MVC 5.3, Spring Security 5.8, Spring Data JPA 3.3 |
| **ORM** | Hibernate 5.6 |
| **База данных** | PostgreSQL 42.7 |
| **Очереди сообщений** | Apache ActiveMQ Artemis 2.28, Spring JMS 6.0 |
| **Сериализация** | Jackson 2.14, Jackson JSR-310 |
| **Безопасность** | BCrypt (jBCrypt 0.4) |
| **Шаблонизатор** | JSP + JSTL |
| **Сборка** | Maven, упаковка WAR |
| **Сервер** | Apache Tomcat |

---

## Функциональность

### Для пользователей
- Просмотр каталога велосипедов
- Поиск велосипедов по цене
- Регистрация и авторизация
- Покупка велосипеда (для аутентифицированных пользователей)

### Для администраторов
- Добавление, редактирование и удаление велосипедов
- Просмотр лога административных операций (через JMS → БД)
- Просмотр истории покупок пользователей

### Асинхронные уведомления (JMS)
- При каждой CRUD-операции над велосипедом отправляется сообщение в очередь `admin.operations`
- При покупке велосипеда — сообщение в очередь `purchase.notifications`
- `MessageConsumer` слушает обе очереди и сохраняет события в базу данных

---

## Структура проекта

```
src/main/java/com/example/
├── client/
│   └── BicycleRestClient.java       # REST-клиент для тестирования API
├── controller/
│   ├── MainController.java          # MVC-контроллер (HTML-страницы)
│   ├── MainRestController.java      # REST API велосипедов (/api/bicycles)
│   ├── AdminMessageApiController.java  # REST API лога операций
│   ├── PurchaseMessageApiController.java # REST API покупок
│   └── RegistrationController.java  # Регистрация пользователей
├── core/config/
│   ├── Artemis/ArtemisConfig.java   # Подключение к ActiveMQ Artemis
│   ├── JMS/JmsConfig.java           # Имена JMS-очередей
│   ├── security/SecurityConfig.java # Spring Security
│   ├── spring/SpringConfig.java     # Основной Spring-контекст
│   └── web/                         # WebInitializer, WebConfig
├── entity/
│   ├── Bicycle.java                 # Сущность велосипеда
│   ├── User.java                    # Сущность пользователя
│   └── Role.java                    # Роли пользователей
├── jms/
│   ├── AdminMessage.java            # Сообщение об операции администратора
│   ├── PurchaseMessage.java         # Сообщение о покупке
│   └── MessageConsumer.java         # JMS-слушатель обеих очередей
├── repo/
│   ├── BicycleDao.java              # DAO для велосипедов
│   ├── AdminMessageRepository.java
│   └── PurchaseMessageRepository.java
└── services/
    ├── JmsService.java              # Отправка JMS-сообщений
    ├── AdminMessageService.java
    ├── PurchaseMessageService.java
    ├── UserService.java
    └── CustomUserDetailsService.java

src/main/webapp/WEB-INF/views/
├── home.jsp
├── bicycles.jsp
├── login.jsp
├── register.jsp
├── admin-messages.jsp
├── purchase-messages.jsp
├── about.jsp
└── services.jsp
```

---

## Требования

- Java 11+
- Maven 3.6+
- PostgreSQL 14+
- Apache ActiveMQ Artemis 2.28+ (запущен на `localhost:61616`)
- Apache Tomcat 9+ (или через SmartTomcat в IntelliJ IDEA)

---

## Установка и запуск

### 1. Клонирование репозитория

```bash
git clone https://github.com/Dzatoichi/bicycles_shop.git
cd bicycles_shop
```

### 2. Настройка базы данных

Создайте базу данных в PostgreSQL:

```sql
CREATE DATABASE vehicle_shop;
```

Откройте `src/main/resources/application.properties` и укажите свои данные подключения:

```properties
dataSource.url=jdbc:postgresql://localhost:5432/vehicle_shop
dataSource.username=postgres
dataSource.password=your_password
```

Hibernate автоматически создаст таблицы при первом запуске (`ddl-auto=update`).

### 3. Запуск ActiveMQ Artemis

Скачайте и запустите брокер:

```bash
# Создать брокер (один раз)
./bin/artemis create mybroker --user admin --password admin --allow-anonymous true

# Запустить брокер
./mybroker/bin/artemis run
```

Брокер должен быть доступен по адресу `tcp://localhost:61616`.

### 4. Сборка проекта

```bash
mvn clean package
```

### 5. Деплой на Tomcat

Скопируйте собранный WAR-файл в директорию `webapps` вашего Tomcat:

```bash
cp target/spring8.war /path/to/tomcat/webapps/
```

Или запустите через IntelliJ IDEA с конфигурацией SmartTomcat (`.smarttomcat/` уже настроен).

Приложение будет доступно по адресу: **http://localhost:8080/**

---

## Тестовые пользователи

Для быстрого тестирования в приложении предусмотрены встроенные пользователи (in-memory):

| Логин | Пароль | Роль |
|---|---|---|
| `admin` | `qwerty` | ADMIN |
| `user` | `password` | USER |

Также можно зарегистрировать нового пользователя через страницу `/register` — он получит роль `USER` и будет сохранён в базу данных.

---

## REST API

Базовый URL: `http://localhost:8080`

### Велосипеды `/api/bicycles`

| Метод | Путь | Доступ | Описание |
|---|---|---|---|
| `GET` | `/api/bicycles` | Все | Получить все велосипеды |
| `GET` | `/api/bicycles/{id}` | Все | Получить велосипед по ID |
| `GET` | `/api/bicycles/search?cost={cost}` | Все | Поиск по цене |
| `POST` | `/api/bicycles` | ADMIN | Добавить велосипед |
| `PUT` | `/api/bicycles/{id}` | ADMIN | Обновить велосипед |
| `DELETE` | `/api/bicycles/{id}` | ADMIN | Удалить велосипед |
| `POST` | `/api/bicycles/buy/{id}` | USER / ADMIN | Купить велосипед |

Аутентификация для защищённых эндпоинтов — **HTTP Basic Auth**.

### Лог операций `/api/admin/messages` (только ADMIN)

| Метод | Путь | Описание |
|---|---|---|
| `GET` | `/api/admin/messages` | Все записи |
| `GET` | `/api/admin/messages/{id}` | Запись по ID |
| `GET` | `/api/admin/messages/operation/{op}` | Фильтр по операции (CREATE/UPDATE/DELETE) |
| `GET` | `/api/admin/messages/user/{username}` | Фильтр по пользователю |
| `DELETE` | `/api/admin/messages/{id}` | Удалить запись |

### История покупок `/api/purchase/messages` (только ADMIN)

| Метод | Путь | Описание |
|---|---|---|
| `GET` | `/api/purchase/messages` | Все покупки |
| `GET` | `/api/purchase/messages/{id}` | Покупка по ID |
| `GET` | `/api/purchase/messages/user/{username}` | Покупки пользователя |
| `GET` | `/api/purchase/messages/bicycle/{id}` | Покупки конкретного велосипеда |
| `GET` | `/api/purchase/messages/stats/count` | Общее количество покупок |

### Примеры запросов

```bash
# Получить все велосипеды
curl http://localhost:8080/api/bicycles

# Добавить велосипед (нужны права ADMIN)
curl -X POST http://localhost:8080/api/bicycles \
  -u admin:qwerty \
  -H "Content-Type: application/json" \
  -d '{"model":"Trek X1","producer":"Trek","producingCountry":"USA","gearsNum":21,"cost":45000}'

# Поиск по цене
curl "http://localhost:8080/api/bicycles/search?cost=10000"

# Удалить велосипед
curl -X DELETE http://localhost:8080/api/bicycles/1 -u admin:qwerty
```

Готовые скрипты для тестирования API находятся в `src/main/resources/`:
- `test-api.bat` — базовые запросы
- `test-sec-api.bat` — запросы с аутентификацией

---

## Модель данных

### Bicycle (велосипед)

| Поле | Тип | Описание |
|---|---|---|
| `id` | Long | Идентификатор |
| `model` | String | Модель |
| `producer` | String | Производитель |
| `producingCountry` | String | Страна производства |
| `gearsNum` | Integer | Количество скоростей |
| `cost` | Integer | Цена |

### AdminMessage (лог операций)

| Поле | Тип | Описание |
|---|---|---|
| `id` | Long | Идентификатор |
| `operation` | String | Тип операции: CREATE / UPDATE / DELETE |
| `bicycle` | Bicycle | Объект велосипеда |
| `username` | String | Кто выполнил операцию |
| `timestamp` | LocalDateTime | Время операции |

### PurchaseMessage (покупка)

| Поле | Тип | Описание |
|---|---|---|
| `id` | Long | Идентификатор |
| `bicycle` | Bicycle | Купленный велосипед |
| `username` | String | Покупатель |
| `timestamp` | LocalDateTime | Время покупки |

---

## Безопасность

- Пароли хранятся в БД в виде BCrypt-хэшей
- Веб-интерфейс использует form-login, REST API — HTTP Basic Auth
- CSRF отключён (REST-ориентированный подход)
- Разграничение прав: публичный доступ к каталогу, защищённые операции — только для `ADMIN`

---

## REST-клиент

Класс `BicycleRestClient` содержит готовый клиент на основе `RestTemplate` для программного взаимодействия с API:

```java
BicycleRestClient client = new BicycleRestClient();

// Получить все велосипеды
List<Bicycle> all = client.getAllBicycles();

// Создать велосипед
Bicycle b = new Bicycle("Trek", "Trek Inc", "USA", 21, 45000);
client.createBicycle(b);

// Поиск по цене
List<Bicycle> filtered = client.findBicyclesByCost(10000);
```
