package ru.mherarsh.fremwork.test.simple.exceptions;

public class AssertFailException extends RuntimeException {
    public AssertFailException() {
        super();
    }

    public AssertFailException(String s) {
        super(s);
    }
}
