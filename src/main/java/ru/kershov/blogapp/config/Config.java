package ru.kershov.blogapp.config;

public class Config {
    public static final String STRING_MULTIUSER_MODE = "Многопользовательский режим";
    public static final String STRING_POST_PREMODERATION = "Премодерация постов";
    public static final String STRING_STATISTICS_IS_PUBLIC = "Показывать всем статистику блога";

    public static final String STRING_YES = "Да";
    public static final String STRING_NO = "Нет";

    public static final String STRING_POST_NOT_FOUND = "Пост с идентификатором '%d' не найден!";
    public static final String STRING_POST_NO_SUCH_MODE = "Неподдерживаемый режим вывода: '%s'!";
    public static final String STRING_POST_INVALID_DATE = "Неправильный формат даты! Используйте: 'yyyy-MM-dd'.";
    public static final String STRING_POST_INVALID_TAG = "Тег '%s' не найден!";
    public static final int INT_POST_MIN_QUERY_LENGTH = 3;
    public static final String STRING_POST_INVALID_QUERY = String.format("Параметр 'query' должен быть " +
            "не менее %d символов.", INT_POST_MIN_QUERY_LENGTH);

    public static final String STRING_AUTH_EMAIL_ALREADY_REGISTERED = "Этот адрес уже зарегистрирован.";
    public static final int INT_AUTH_MIN_NAME_LENGTH = 3;
    public static final String STRING_AUTH_WRONG_NAME = "Имя указано неверно.";
    public static final int INT_AUTH_MIN_PASSWORD_LENGTH = 6;
    public static final int INT_AUTH_MAX_FIELD_LENGTH = 255;
    public static final String STRING_AUTH_INVALID_PASSWORD_LENGTH = String.format("Пароль короче " +
            "%d символов.", INT_AUTH_MIN_PASSWORD_LENGTH);
    public static final String STRING_AUTH_SHORT_PASSWORD = "Слишком короткий пароль.";
    public static final String STRING_AUTH_INVALID_CAPTCHA = "Код с картинки введен неверно.";
    public static final int INT_AUTH_BCRYPT_STRENGTH = 8;
    public static final String STRING_AUTH_INVALID_EMAIL = "Адрес указан неверно.";

    public static final String STRING_FIELD_CANT_BE_BLANK = "Поле не может быть пустым.";
    public static final String STRING_AUTH_LOGIN_NO_SUCH_USER = "Пользователь не найден.";
    public static final String STRING_AUTH_ERROR = "Ошибка аутентификации";
    public static final String STRING_AUTH_REGISTRATION_ERROR = "Ошибка регистрации";
    public static final String STRING_AUTH_WRONG_PASSWORD = "Пароль указан неверно.";
    public static final String STRING_AUTH_AUTHORIZED = "Пользователь авторизован.";
    public static final String STRING_AUTH_EMPTY_EMAIL_OR_PASSWORD = "Адрес или пароль не указаны.";
    public static final String STRING_AUTH_MAIL_SUBJECT = "Ссылка на восстановление пароля";

    public static final String STRING_AUTH_SERVER_URL = "http://%s:%s";
    public static final String STRING_AUTH_MAIL_MESSAGE = "Для восстановления пароля, " +
            "пройдите по этой ссылке: %s/login/change-password/%s";
    public static final String STRING_AUTH_CODE_IS_OUTDATED = "Ссылка для восстановления пароля устарела. " +
            "Вы можете <a href=\"/login/restore-password\">запросить ссылку снова</a>.";

    public static final int INT_IMAGES_MAX_CACHE_AGE = 3;
    public static final String STRING_VALIDATION_MESSAGE = "Тело запроса пустое, сформировано неверно или содержит ошибки.";
    public static final String STRING_COMMENT_POST_ID_IS_MANDATORY = "Поле 'post_id' является обязательным.";
    public static final String STRING_WRONG_POST_ID = "Поле 'post_id' содержит неверный идентификатор.";
    public static final String STRING_COMMENT_WRONG_PARENT_ID = "Поле 'parent_id' содержит неверный идентификатор.";
    public static final String STRING_COMMENT_WRONG_TEXT = "Поле 'text' является обязательным и не может быть пустым.";

    public static final int STRING_POST_TITLE_MIN_LENGTH = 5;
    public static final int STRING_POST_TITLE_MAX_LENGTH = 255;
    public static final String STRING_POST_INVALID_TITLE = "Заголовок поста не может быть пустым и " +
            "должен состоять не менее чем из 5 символов и не более чем из 255 символов.";

    public static final int STRING_POST_TEXT_MIN_LENGTH = 10;
    public static final int STRING_POST_TEXT_MAX_LENGTH = 5000;
    public static final String STRING_POST_INVALID_TEXT = "Текст поста поста не может быть пустым и " +
            "должен состоять не менее чем из 10 символов и не более чем из 500 символов.";
    public static final String STRING_NEW_POST_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm";
    public static final String STRING_NEW_POST_INVALID_DATE = "Неправильный формат даты! Используйте: 'yyyy-MM-ddTHH:mm'.";

    public static final String STRING_ERROR_HANDLER_INVALID_OPTION = "Параметру '%s' установлено неверное значение: '%s'.";
    public static final String STRING_MODERATED_POST_DATE_FORMAT = "dd-MM-yyyy HH:mm";
    public static final String STRING_MODERATION_WRONG_DECISION = "Неверное значение параметра! Используйте 'accept' или 'decline'.";
    public static final String STRING_MODERATION_INVALID_POST = "Модерирование постов, закрепленных за другими модераторами запрещено!";

    public static final String STRING_TELEGRAM_COMMENT_ADDED = "Пользователь *%s* \\(%s\\) добавил комментарий к посту " +
            "\"[%s](http://kershov.ru:5000/post/%d)\"\\.\n\n*Текст комментария:*\n%s\n\n\\#комментарии";

    public static final String STRING_TELEGRAM_USER_REGISTERED = "Зарегистрирован новый пользователь: %s\n\n\\#регистрация";

    public static final String STRING_TELEGRAM_POST_ADDED = "Пользователь *%s* \\(%s\\) добавил новый или изменил ранее опубликованный пост " +
            "\"[%s](http://kershov.ru:5000/post/%d)\"\\.\n\n\\#модерация";

    public static final String STRING_EMAIL_PATTERN = "^(.+)@(.+)\\.(.+)$";
}
