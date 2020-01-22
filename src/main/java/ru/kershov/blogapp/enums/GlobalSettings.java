package ru.kershov.blogapp.enums;

import ru.kershov.blogapp.config.Config;

public class GlobalSettings {
    public enum Code {
        MULTIUSER_MODE(Config.STRING_MULTIUSER_MODE),
        POST_PREMODERATION(Config.STRING_POST_PREMODERATION),
        STATISTICS_IS_PUBLIC(Config.STRING_STATISTICS_IS_PUBLIC);

        private final String name;

        Code(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    public enum Value {
        YES(Config.STRING_YES),
        NO(Config.STRING_NO);

        private final String name;

        Value(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
}
