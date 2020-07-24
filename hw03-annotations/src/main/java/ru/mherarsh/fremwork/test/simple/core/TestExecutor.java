package ru.mherarsh.fremwork.test.simple.core;

import ru.mherarsh.fremwork.test.simple.annotations.TestClass;

public class TestExecutor {
    private final PackageScanner packageScanner;

    public TestExecutor(String testPackageName) {
        packageScanner = new PackageScanner(testPackageName);
    }

    public void execute() {
        packageScanner.getAnnotatedClasses(TestClass.class)
                .forEach(testClass -> new ClassTester(testClass).test());
    }
}
