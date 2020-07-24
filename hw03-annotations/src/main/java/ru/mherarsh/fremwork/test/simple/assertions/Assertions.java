package ru.mherarsh.fremwork.test.simple.assertions;

import ru.mherarsh.fremwork.test.simple.exceptions.AssertFailException;

public class Assertions {
    public static void fail() throws AssertFailException {
        throw new AssertFailException();
    }

    public static void fail(String message) throws AssertFailException {
        throw new AssertFailException(message);
    }

    public static void pass() {
    }
}
