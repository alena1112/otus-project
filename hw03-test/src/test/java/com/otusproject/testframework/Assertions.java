package com.otusproject.testframework;

import org.apache.commons.collections4.CollectionUtils;

import java.util.List;

public class Assertions {

    public static void assertEquals(List<?> expected, List<?> actual) {
        if (!CollectionUtils.isEqualCollection(expected, actual)) {
            throw new AssertionError(String.format("expected: <%s> but was: <%s>", expected, actual));
        }
    }

    public static void assertEquals(long expected, long actual) {
        if (expected != actual) {
            throw new AssertionError(String.format("expected: <%s> but was: <%s>", expected, actual));
        }
    }
}
