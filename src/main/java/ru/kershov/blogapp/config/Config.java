package ru.kershov.blogapp.config;

public class Config {
    public static final String STRING_MULTIUSER_MODE = "Многопользовательский режим";
    public static final String STRING_POST_PREMODERATION = "Премодерация постов";
    public static final String STRING_STATISTICS_IS_PUBLIC = "Показывать всем статистику блога";

    public static final String STRING_YES = "Да";
    public static final String STRING_NO = "Нет";

    public static final String STRING_POST_NOT_FOUND = "Пост с идентификатором '%d' не найден!";
    public static final String STRING_POST_NO_SUCH_MODE = "Неподдерживаемый режим вывода: '%s'!";
    public static final String STRING_POST_INVALID_DATE = "Неправильный формат даты! Используйте: YYYY-MM-DD.";
    public static final String STRING_POST_INVALID_TAG = "Тег '%s' не найден!";
    public static final int INT_POST_MIN_QUERY_LENGTH = 3;
    public static final String STRING_POST_INVALID_QUERY = String.format("Параметр 'query' должен быть " +
            "не менее %d символов.", INT_POST_MIN_QUERY_LENGTH);

    public static final String STRING_AUTH_EMAIL_ALREADY_REGISTERED = "Адрес '%s' уже зарегистрирован.";
    public static final String STRING_AUTH_WRONG_NAME = "Имя указано неверно.";
    public static final int INT_AUTH_MIN_PASSWORD_LENGTH = 6;
    public static final String STRING_AUTH_INVALID_PASSWORD_LENGTH = String.format("Пароль короче " +
            "%d символов.", INT_AUTH_MIN_PASSWORD_LENGTH);
    public static final String STRING_AUTH_SHORT_PASSWORD = "Слишком короткий пароль.";
    public static final String STRING_AUTH_INVALID_CAPTCHA = "Код с картинки введен неверно.";
    public static final int INT_AUTH_BCRYPT_STRENGTH = 8;
    public static final String STRING_AUTH_INVALID_EMAIL = "Адрес указан неверно.";

    public static final String STRING_FIELD_CANT_BE_BLANK = "Поле не может быть пустым.";
    public static final String STRING_AUTH_LOGIN_NO_SUCH_USER = "Пользователь с адресом '%s' не найден.";
    public static final String STRING_AUTH_ERROR = "Ошибка аутентификации";
    public static final String STRING_AUTH_REGISTRATION_ERROR = "Ошибка регистрации";
    public static final String STRING_AUTH_WRONG_PASSWORD = "Пароль указан неверно.";
    public static final String STRING_AUTH_AUTHORIZED = "Пользователь авторизован.";
    public static final String STRING_AUTH_EMPTY_EMAIL_OR_PASSWORD = "Адрес или пароль не указаны.";
}
