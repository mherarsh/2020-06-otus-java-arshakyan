package ru.mherarsh;

import ru.mherarsh.demo.TestLogging;

import java.io.IOException;

public class App {
    public static void main(String[] args) throws IOException {
        var testClass = new TestLogging();
        testClass.add(1, 2, 3);
        testClass.sum(1, 2);
        testClass.returnSomeObject();
        testClass.printText("a", "b", 1, 2);
        testClass.printHello();
    }
}
