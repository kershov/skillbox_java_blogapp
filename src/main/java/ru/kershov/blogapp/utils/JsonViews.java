package ru.kershov.blogapp.utils;

public final class JsonViews {
    /**
     * For explicit entities field serialization in Json
     */
    public interface Id {}

    public interface IdName extends Id {}

    public interface FullMessage extends IdName {}
}
