package ru.mherarsh;

import com.sun.management.GarbageCollectionNotificationInfo;

import javax.management.NotificationEmitter;
import javax.management.NotificationListener;
import javax.management.openmbean.CompositeData;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.management.GarbageCollectorMXBean;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GCBenchmark {
    private long beginTime;
    final GCLogger gcLogger;

    public GCBenchmark() {
        gcLogger = new GCLogger();
        switchOnMonitoring();
    }

    public void runTest() throws InterruptedException {
        beginTime = System.currentTimeMillis();

        ArrayList<Double[]> objects = new ArrayList<>();
        for (int idx = 0; idx < 10000000; idx++) {
            int local = 100000;
            Object[] array = new Object[local];
            for (int i = 0; i < local; i++) {
                array[i] = new String(new char[0]);
            }
            objects.add(new Double[10000]);

            Thread.sleep(1);
        }
    }

    public void dumpToFile() throws IOException {
        gcLogger.dumpToFile();
    }

    private void switchOnMonitoring() {
        List<GarbageCollectorMXBean> gcbeans = java.lang.management.ManagementFactory.getGarbageCollectorMXBeans();
        for (GarbageCollectorMXBean gcbean : gcbeans) {
            NotificationEmitter emitter = (NotificationEmitter) gcbean;
            NotificationListener listener = (notification, handback) -> {
                if (notification.getType().equals(GarbageCollectionNotificationInfo.GARBAGE_COLLECTION_NOTIFICATION)) {
                    GarbageCollectionNotificationInfo info = GarbageCollectionNotificationInfo.from((CompositeData) notification.getUserData());
                    String gcName = info.getGcName();
                    String gcAction = info.getGcAction();
                    String gcCause = info.getGcCause();

                    long startTime = info.getGcInfo().getStartTime();
                    long duration = info.getGcInfo().getDuration();

                    final Runtime rt = Runtime.getRuntime();
                    int freeMemory = (int) (rt.freeMemory() / (rt.totalMemory()*1.0) * 100.0);

                    gcLogger.put("" + startTime, gcName, gcAction, gcCause, "" + duration, "" + freeMemory);
                }
            };
            emitter.addNotificationListener(listener, null, null);
        }
    }

    private class GCLogger {
        private final ArrayList<String> logs = new ArrayList<>();

        public GCLogger() {
            put("startTime", "gcName", "gcAction", "gcCause", "duration (ms)", "freeMemory (%)");
        }

        public void put(String startTime, String gcName, String gcAction, String gcCause, String duration, String freeMemory) {
            logs.add(startTime + ":" + gcName + ":" + gcAction + ":" + gcCause + ":" + duration + ":" + freeMemory);
        }

        public void dumpToFile() throws IOException {
            System.out.println("time:" + (System.currentTimeMillis() - beginTime) / 1000);
            String fileName = new SimpleDateFormat("'dump-gc-'yyyy-MM-dd.HH.mm'.csv'").format(new Date());

            FileWriter writer = new FileWriter(fileName);
            for (String str : logs) {
                writer.write(str + System.lineSeparator());
            }
            writer.close();
        }
    }
}
