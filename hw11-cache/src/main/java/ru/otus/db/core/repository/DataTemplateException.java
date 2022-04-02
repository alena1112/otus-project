package ru.otus.db.core.repository;

public class DataTemplateException extends RuntimeException {
    public DataTemplateException(Exception ex) {
        super(ex);
    }

    public DataTemplateException(String message, Throwable cause) {
        super(message, cause);
    }

    public DataTemplateException(String message) {
        super(message);
    }
}
