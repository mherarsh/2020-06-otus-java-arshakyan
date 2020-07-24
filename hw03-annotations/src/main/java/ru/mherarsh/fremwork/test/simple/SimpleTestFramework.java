package ru.mherarsh.fremwork.test.simple;

import ru.mherarsh.fremwork.test.simple.core.TestExecutor;

public class SimpleTestFramework {
    private final TestExecutor testExecutor;

    public SimpleTestFramework(String testPackageName) {
        testExecutor = new TestExecutor(testPackageName);
    }

    public void runTest() {
        testExecutor.execute();
    }
}
