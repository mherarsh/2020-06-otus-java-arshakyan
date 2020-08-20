package ru.mherarsh.demo;

import ru.mherarsh.aop.annotations.Log;

public class TestLogging {
    @Log
    public int add(int x, int y, int z) {
        return x + y + z;
    }

    @Log
    public void sum(int x, int y) {
    }

    @Log
    public void printHello() {
    }

    @Log
    public void printText(String x, String y, int a, double b) {
    }

    @Log
    public Object returnSomeObject() {
        return new Object();
    }
}
