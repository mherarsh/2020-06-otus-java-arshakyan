package ru.mherarsh.fremwork.test.simple.core;

import ru.mherarsh.fremwork.test.simple.core.dto.result.ClassTestResult;
import ru.mherarsh.fremwork.test.simple.core.dto.result.TestResult;

public class TestResultPrinter {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLUE = "\u001B[34m";
    private static final String ANSI_BLACK = "\u001B[30m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String TEST_HEADER = "\n***********************-CLASS TEST START-***********************";
    private static final String TEST_FOOTER = "************************-CLASS TEST END-************************\n";
    private static final String TEST_HEADER_SEPARATOR = "\n---------------------- TEST -----------------------";

    private final ClassTestResult classTestResult;

    public TestResultPrinter(ClassTestResult classTestResult) {
        this.classTestResult = classTestResult;
    }

    public void print() {
        printHeader();
        printTests();
        printFooter();
    }

    private void printTests() {
        classTestResult.getTests().forEach(this::printTestResult);
    }

    private void printTestResult(TestResult testResult) {
        StringBuilder resultString = new StringBuilder()
                .append("===================================================\n")
                .append(testResult.getTestName() + " : " + (testResult.isPass() ? "passed\n" : "fail\n"));

        if(!testResult.isPass()){
            resultString.append("                      message                      \n");
            resultString.append("---------------------------------------------------\n");
            resultString.append(testResult.getMessage());
        }

        resultString.append("===================================================\n");

        printColorize(resultString.toString(), testResult.isPass() ? ANSI_GREEN : ANSI_RED);
    }

    private void printHeader() {
        int testCount = classTestResult.getAllTestsCount();
        int passTestCount = classTestResult.getPassedCount();

        printColorize(TEST_HEADER, ANSI_BLUE);
        printColorize("Test class: " + classTestResult.getClassName(), ANSI_BLACK);
        printColorize("Tests count: " + testCount, ANSI_BLACK);
        printColorize("Test passed " + passTestCount + " of " + testCount,
                testCount == passTestCount ? ANSI_GREEN : ANSI_RED);
        printColorize(TEST_HEADER_SEPARATOR, ANSI_BLACK);
    }

    private void printFooter() {
        printColorize(TEST_FOOTER, ANSI_BLUE);
    }

    private void printColorize(String text, String colore) {
        System.out.println(colore + text + colore + ANSI_RESET);
    }
}
