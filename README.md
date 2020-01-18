# Дипломная работа по курсу «Java-разработчик c нуля» от Skillbox

В рамках дипломного проекта необходимо реализовать блоговый движок на базе Spring Boot.

Требования к окружению и компонентам дипломного проекта:

|                         |         Ограничение         |
|------------------------:|:---------------------------:|
|              Версия JDK |   11 (указать в `pom.xml`)  |
| Система контроля версий |             Git             |
|      Версия Spring Boot |              5              |
|             База данных | MySQL 8 или PostgreSQL 9/11 |
|         Сборщик проекта |            Maven            |
|           Тестирование* |        JUnit 4 или 5        |

## Полезная информация

Запустить запустить приложение с нужными переменными окружения: 

```bash
$ set -a; . ./.env; java -jar target/BlogApp.jar; set +a
```

Создать БД и пользователя в PostgreSQL:

```sql
CREATE DATABASE blogapp ENCODING 'UTF8' LC_COLLATE = 'ru_RU.UTF-8' LC_CTYPE = 'ru_RU.UTF-8' TEMPLATE = template0;
CREATE USER blogapp WITH ENCRYPTED PASSWORD 'blogapp';
GRANT ALL PRIVILEGES ON DATABASE blogapp TO blogapp;
```
