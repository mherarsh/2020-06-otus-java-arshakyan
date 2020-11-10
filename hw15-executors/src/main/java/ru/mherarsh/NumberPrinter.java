package ru.mherarsh;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

public class NumberPrinter {
    private static Logger logger = LoggerFactory.getLogger(Number.class);

    private static final int MAIN_THREAD_ID = 0;
    private static final int MAX_THREADS = 100;
    private int currentThreadId = MAIN_THREAD_ID;
    private final int threadsCount;
    private final int printerMaxValue;
    private int printValue;

    public NumberPrinter(int threadsCount, int printerMaxValue) {
        if (threadsCount < 1 || threadsCount > MAX_THREADS) {
            throw new IllegalArgumentException(String.format("%s: invalid threads count, count should be > 0 and < %s",
                    getClass().getName(), MAX_THREADS));
        }

        this.threadsCount = threadsCount + 1; // +1 - основной поток
        this.printerMaxValue = printerMaxValue;
    }

    public void run() throws InterruptedException {
        var printerThreads = startPrinterThreads();

        printNumbers();

        waitThreadsToComplete(printerThreads);
    }

    private void printNumbers() throws InterruptedException {
        for (int i = 1; i < printerMaxValue; i++) {
            setPrintValue(i);
        }

        for (int i = printerMaxValue; i > 0; i--) {
            setPrintValue(i);
        }
    }

    private void setPrintValue(int value) throws InterruptedException {
        synchronized (this) {
            while (currentThreadId != MAIN_THREAD_ID) {
                wait();
            }

            printValue = value;
            processNextThread();
        }
    }

    private void waitThreadsToComplete(ArrayList<Thread> printerThreads) throws InterruptedException {
        for (var thread : printerThreads) {
            thread.interrupt();
            thread.join();
        }
    }

    private ArrayList<Thread> startPrinterThreads() {
        var printerThreads = new ArrayList<Thread>();
        for (int i = 1; i < threadsCount; i++) {
            var thread = createThreadById(i);
            thread.start();

            printerThreads.add(thread);
        }

        return printerThreads;
    }

    private Thread createThreadById(int threadId) {
        return new Thread(() -> printAction(threadId), "thread" + threadId);
    }

    synchronized private void printAction(int threadId) {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                while (currentThreadId != threadId) {
                    wait();
                }

                printValue();
                processNextThread();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private void printValue() {
        logger.info(Thread.currentThread().getName() + " - " + printValue);
    }

    private void processNextThread() {
        currentThreadId = (currentThreadId + 1) % threadsCount;
        notifyAll();
    }
}
