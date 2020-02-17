package ru.kershov.blogapp.enums;

import org.springframework.core.convert.converter.Converter;

public enum ModerationStatus {
    NEW, ACCEPTED, DECLINED;
    private static final ModerationStatus[] values = ModerationStatus.values();

    public static ModerationStatus getById(int id) {
        return values[id];
    }

    public static class StringToEnumConverter implements Converter<String, ModerationStatus> {
        @Override
        public ModerationStatus convert(String source) {
            return ModerationStatus.valueOf(source.toUpperCase());
        }
    }
}
