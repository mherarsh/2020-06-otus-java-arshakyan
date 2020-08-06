package ru.mherarsh;

import java.io.IOException;

public class App {
    public static void main(String[] args) throws InterruptedException, IOException {
        final GCBenchmark gcBenchmark = new GCBenchmark();

        try {
            gcBenchmark.runTest();
        } finally {
            gcBenchmark.dumpToFile();
        }
    }
}
