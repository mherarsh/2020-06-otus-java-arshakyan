package ru.mherarsh.fremwork.test.simple.core.dto.result;

import java.util.ArrayList;
import java.util.List;

public class ClassTestResult {
    private final String className;
    private final ArrayList<TestResult> testResults = new ArrayList<>();

    public ClassTestResult(String className) {
        this.className = className;
    }

    public void addTestResult(TestResult testResult) {
        this.testResults.add(testResult);
    }

    public String getClassName() {
        return className;
    }

    public int getAllTestsCount() {
        return testResults.size();
    }

    public int getPassedCount(){
        return (int) testResults.stream().filter(TestResult::isPass).count();
    }

    public List<TestResult> getTests() {
        return testResults;
    }
}
