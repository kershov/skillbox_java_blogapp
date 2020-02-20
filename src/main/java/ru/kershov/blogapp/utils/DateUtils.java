package ru.kershov.blogapp.utils;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;

public class DateUtils {
    public static boolean isValidDate(String date) {
        final String YEAR = "(19[789][0-9]|2[0-9]{3})";
        final String MONTH = "(0[1-9]|1[0-2])";
        final String DAY = "(0[1-9]|1[0-9]|2[0-9]|3[0-1])";
        final Pattern VALID_DATE_PATTERN = Pattern.compile(String.format(
                "^%s-%s-%s$", YEAR, MONTH, DAY));

        return VALID_DATE_PATTERN.matcher(date).matches();
    }

    public static String formatDate(final Instant time, final String format) {
        return DateTimeFormatter.ofPattern(format)
                .withZone(ZoneId.systemDefault()).format(time);
    }
}
