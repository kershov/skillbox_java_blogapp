package ru.kershov.blogapp.enums;

public class GlobalSettings {
    public enum Code {
        MULTIUSER_MODE("Многопользовательский режим"),
        POST_PREMODERATION("Премодерация постов"),
        STATISTICS_IS_PUBLIC("Показывать всем статистику блога");

        private final String name;

        Code(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    public enum Value {
        YES("Да"),
        NO("Нет");

        private final String name;

        Value(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
}
