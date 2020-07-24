package ru.mherarsh.fremwork.test.simple.core;

import ru.mherarsh.fremwork.test.simple.annotations.After;
import ru.mherarsh.fremwork.test.simple.annotations.Before;
import ru.mherarsh.fremwork.test.simple.annotations.Test;
import ru.mherarsh.fremwork.test.simple.core.dto.result.ClassTestResult;
import ru.mherarsh.fremwork.test.simple.core.dto.result.TestResult;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class ClassTester {
    private final ClassTestResult classTestResult;
    private final ClassScanner classScanner;

    public ClassTester(Class<?> clazz) {
        this.classTestResult = new ClassTestResult(clazz.getName());
        this.classScanner = new ClassScanner(clazz);
    }

    public void test() {
        runAllTests();
        new TestResultPrinter(classTestResult).print();
    }

    private void runAllTests() {
        Constructor<?> defaultConstructor = null;

        try {
            defaultConstructor = classScanner.getDefaultConstructor();
        } catch (NoSuchMethodException e) {
            classTestResult.addTestResult(new TestResult("constructor", e));
            return;
        }

        final Method[] testedMethods = classScanner.getAnnotatedMethods(Test.class);
        if (testedMethods.length == 0) {
            return;
        }

        final Method testBefore = classScanner.getAnnotatedMethod(Before.class);
        final Method testAfter = classScanner.getAnnotatedMethod(After.class);

        runSingleTest(defaultConstructor, testBefore, testAfter, testedMethods);
    }

    private void runSingleTest(Constructor<?> constructor, Method testBefore, Method testAfter, Method[] testedMethods) {
        for (Method testedMethod : testedMethods) {
            try {
                final Object instance = constructor.newInstance();

                if (testBefore != null) {
                    testBefore.invoke(instance);
                }

                testedMethod.invoke(instance);

                if (testAfter != null) {
                    testAfter.invoke(instance);
                }

                classTestResult.addTestResult(new TestResult(testedMethod.getName()));
            } catch (Exception e) {
                classTestResult.addTestResult(new TestResult(testedMethod.getName(), e));
            }
        }
    }
}
