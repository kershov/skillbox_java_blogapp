package ru.kershov.blogapp.enums;

public enum ModerationStatus {
    NEW, ACCEPTED, DECLINED;
    private static final ModerationStatus[] values = ModerationStatus.values();

    public static ModerationStatus getById(int id) {
        return values[id];
    }
}
