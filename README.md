# Game Shop - Fullstack Project

## Стек технологий
### Backend
- Java 21 / Spring Boot 3
- Spring Security + JWT (авторизация и роли)
- Spring Data JPA + PostgreSQL (DB)
- Swagger/OpenAPI (документация API)

### Frontend
- Vue 3 (Composition API)
- Vite (сборка)
- Pinia (управление состоянием)
- Axios (запросы к API)

## Структура проекта
- /gaameshop-backend — Серверная часть (API)
- /gameshop-frontend — Клиентская часть (UI)

## Запуск
1. Настройте БД PostgreSQL и обновите application.properties в папке backend.
2. Запустите бэкенд: mvn spring-boot:run
3. Запустите фронтенд: npm install и затем npm run dev
