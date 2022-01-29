package com.otusproject.testframework;

public class TestFrameworkException extends Exception {
    public TestFrameworkException(String message) {
        super(message);
    }

    public TestFrameworkException(String message, Object... args) {
        super(String.format(message, args));
    }
}
