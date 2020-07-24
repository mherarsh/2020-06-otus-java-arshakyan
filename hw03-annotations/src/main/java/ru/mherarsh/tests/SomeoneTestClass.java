package ru.mherarsh.tests;

import ru.mherarsh.fremwork.test.simple.annotations.Test;
import ru.mherarsh.fremwork.test.simple.annotations.TestClass;
import ru.mherarsh.fremwork.test.simple.assertions.Assertions;

@TestClass
public class SomeoneTestClass {
    @Test
    public void test6() {
        Assertions.fail("Oops!");
    }

    @Test
    public void test5() {
        System.out.println("on test message");
        Assertions.pass();
    }

    @Test
    public void test9() {
        Assertions.fail("Oops!");
    }
}
