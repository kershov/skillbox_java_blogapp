package ru.kershov.blogapp.utils;

import org.jsoup.Jsoup;

public class StringUtils {
    /**
     * Ref: https://core.telegram.org/bots/api#markdownv2-style
     * Characters '_‘, ’*‘, ’[‘, ’]‘, ’(‘, ’)‘, ’~‘, ’`‘, ’>‘, ’#‘, ’+‘, ’-‘, ’=‘, ’|‘, ’{‘, ’}‘, ’.‘, ’!‘
     * must be escaped with the preceding character ’\'.
     */
    public static String escapeString(String string) {
        return Jsoup.parse(string).text()
                .replaceAll("([_*\\[\\]()~`>#+\\-=|{}.!])", "\\\\$1");
    }
}
