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

## СУБД

База данных (Dev): MariaDB 10.3.13 (MySQL 8)

База данных (Heroku): PostgreSQL 11.6

## Запуск приложения

Подготовить окружение: 

```bash
$ cp .env_example .env
```

Задать необходимые значения переменным `JDBC_DATABASE_*`. в файле `.env`.

Запустить приложение с нужными переменными окружения: 

```bash
$ set -a; . ./.env; java -jar target/BlogApp.jar; set +a
```

Или в IntelliJ IDEA: `Edit Configuration / EnvFile / Enable EnvFile / + .env`.

## Деплой на Heroku

```bash
$ git push heroku develop:master
```

Для активации профиля `heroku-postgresql` установить для приложения переменную окружения `MAVEN_SETTINGS_PATH` со ссылкой на файл активации профиля.

```bash
$ heroku config:set MAVEN_SETTINGS_PATH=heroku-settings.xml
```
