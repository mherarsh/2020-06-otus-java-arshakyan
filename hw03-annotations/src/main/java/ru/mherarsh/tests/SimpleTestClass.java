package ru.mherarsh.tests;

import ru.mherarsh.fremwork.test.simple.annotations.After;
import ru.mherarsh.fremwork.test.simple.annotations.Before;
import ru.mherarsh.fremwork.test.simple.annotations.Test;
import ru.mherarsh.fremwork.test.simple.annotations.TestClass;
import ru.mherarsh.fremwork.test.simple.assertions.Assertions;

@TestClass
public class SimpleTestClass {
    @Before
    public void before() {
        System.out.println("before test run message");
    }

    @After
    public void after() {
        System.out.println("after test message");
    }

    @Test
    public void test1() {
       Assertions.fail("Oops!");
    }

    @Test
    public void test2() {
        System.out.println("on test message");
        Assertions.pass();
    }

    @Test
    public void test3() {
        Assertions.pass();
    }
}
