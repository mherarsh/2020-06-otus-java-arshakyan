package ru.mherarsh.fremwork.test.simple.core.dto.result;

import java.io.PrintWriter;
import java.io.StringWriter;

public class TestResult {
    private final String testName;
    private String message = "";
    private boolean isTestPass = true;

    public TestResult(String testName, Exception exception) {
        this.testName = testName;
        this.isTestPass = false;
        this.message = getExceptionMessage(exception);
    }

    public TestResult(String testName) {
        this.testName = testName;
    }

    public String getTestName() {
        return testName;
    }

    public boolean isPass() {
        return isTestPass;
    }

    public String getMessage() {
        return message;
    }

    private String getExceptionMessage(Exception e) {
        StringWriter sw = new StringWriter();
        e.printStackTrace(new PrintWriter(sw));
        return sw.toString();
    }
}
