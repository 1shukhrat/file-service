# File Storage Microservice

## Описание

Этот микросервис реализует хранилище файлов и их атрибутов, предоставляя HTTP API для работы с файлами. Микросервис позволяет загружать файлы, сохранять их в базе данных PostgreSQL, а также получать файлы и их атрибуты по идентификатору. 

Микросервис разработан на Java с использованием Spring Boot. Все данные хранятся в базе данных PostgreSQL. 

## Основные API методы

### 1. Создание файла

**URL:** `/api/files`

**Метод:** `POST`

**Описание:** Создает новый файл с указанными атрибутами и возвращает идентификатор созданного файла.

**Пример тела запроса:**

```json
{
    "title": "Test File",
    "description": "Test Description",
    "creationDate": "2021-08-12T12:00:00",
    "base64Data": "f9Zb4Bn6B3/Geg=="
}
```

**Пример ответа:**

```json
{
  "fileId": 1,
  "responseMessage": "File was created"
}
```

### 2. Получение файла

**URL:** `/api/files/{id}`

**Метод:** `GET`

**Описание:** Возвращает файл и его атрибуты по указанному идентификатору.

**Пример запроса:**

```
GET /api/files/1
```

**Пример ответа:**

```json
{
    "title": "Test File",
    "description": "Test Description",
    "creationDate": "2021-08-12T12:00:00",
    "base64Data": "f9Zb4Bn6B3/Geg=="
}
```

### 3. Получение всех файлов

**URL:** `/api/files?page={int}&size={int}&sort={string}`

**Метод:** `GET`

**Описание:** Возвращает список файлов с поддержкой пагинации и сортировки по времени создания.

**Параметры запроса:**
```
1. page - Номер страницы. По умолчанию - 0
2. size - Количество элементов на странице. По умолчанию - 10
3. sort - Сортировка по указанному полю. По умолчанию - creationDate. (title - Название, description - Описание, creationDate - Дата создания, data - Base64 формат файла)
```

**Пример запроса:**

```
GET /api/files?page=0&size=2&sort=creationDate
```

**Пример ответа:**

```json
[
  {
    "title": "File 1",
    "description": "Description 1",
    "creation_date": "2024-08-25T12:00:00",
    "file_data": "base64encodedstring1..."
  },
  {
    "title": "File 2",
    "description": "Description 2",
    "creation_date": "2024-08-26T12:00:00",
    "file_data": "base64encodedstring2..."
  }
]
```

## Запуск проекта

1. Установите Docker и Docker Compose.
2. Склонируйте репозиторий `git clone https://github.com/1shukhrat/file-service.git`
3. Запустите команду `docker-compose up` или `docker compose up`.
4. API будет доступно по адресу `http://localhost:8080`.

> [!NOTE]
> При необходимости можете поменять параметры в файле docker-compose.yaml

## Тестирование
Запустите тесты с помощью команды `./gradlew test`.

## Технологии
1. Java 21
2. Spring Boot 3.3.3: Фреймворк для создания приложений.
3. PostgreSQL 15: Реляционная база данных.
4. Flyway 10.10.0 : Инструмент для миграций баз данных
5. JUnit 5.10.3 : Модульное тестирование
6. Gradle 8.9: Система сборки
