# Intershop

## Простой интернет-магазин на Spring WebFlux с реактивным доступом к базе данных PostgreSQL.

### Описание проекта
Проект реализует базовый функционал корзины покупок: добавление, изменение количества и удаление товаров. Используется Spring WebFlux для реактивного программирования и Spring Data R2DBC для работы с PostgreSQL.

### Требования
Java 21
Docker и Docker Compose
PostgreSQL (или запуск через Docker)

#### Быстрый старт

#### 1. Клонируйте репозиторий

   git clone https://github.com/SemenovRustam/intershop.git
   cd intershop

#### 2. Настройте базу данных

   Создайте базу данных intershop в PostgreSQL.

 Если хотите запустить PostgreSQL через Docker, используйте команду:

docker run -d \
--name intershop-postgres \
-e POSTGRES_DB=intershop \
-e POSTGRES_USER=postgres \
-e POSTGRES_PASSWORD= \
-p 5432:5432 \
postgres:15

База доступна по адресу: localhost:5432
Имя базы: intershop
Пользователь: postgres
Пароль: 
3. #### Настройте параметры подключения  

   В файле src/main/resources/application.yml укажите параметры подключения к базе данных:

`spring:
r2dbc:
url: r2dbc:postgresql://localhost:5432/intershop
username: postgres
password:`

4. ##### Запуск проекта  
   Соберите и запустите приложение командой:

./gradlew clean spring-boot:run
Или соберите Docker-образ и запустите через Docker Compose:

docker-compose up --build


5. ##### Использование
   
   Приложение доступно по адресу: http://localhost:8080
   Основные эндпоинты:
   /cart/items — управление товарами в корзине
   /main/items — просмотр товаров