package ru.kershov.blogapp.enums;

import lombok.Getter;
import lombok.ToString;
import org.springframework.core.convert.converter.Converter;

@ToString
public enum MyPostsModerationStatus {
    INACTIVE(false, ModerationStatus.NEW),
    PENDING(true, ModerationStatus.NEW),
    DECLINED(true, ModerationStatus.DECLINED),
    PUBLISHED(true, ModerationStatus.ACCEPTED);

    @Getter
    final boolean isActive;

    @Getter
    final ModerationStatus moderationStatus;

    MyPostsModerationStatus(boolean isActive, ModerationStatus moderationStatus) {
        this.isActive = isActive;
        this.moderationStatus = moderationStatus;
    }

    public static class StringToEnumConverter implements Converter<String, MyPostsModerationStatus> {
        @Override
        public MyPostsModerationStatus convert(String source) {
            return MyPostsModerationStatus.valueOf(source.toUpperCase());
        }
    }
}
