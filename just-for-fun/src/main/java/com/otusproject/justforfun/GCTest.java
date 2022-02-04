package com.otusproject.justforfun;

import com.sun.management.GarbageCollectionNotificationInfo;

import javax.management.NotificationEmitter;
import javax.management.NotificationListener;
import javax.management.openmbean.CompositeData;
import java.lang.management.GarbageCollectorMXBean;
import java.util.List;

public class GCTest {
    public static void main(String[] args) throws InterruptedException {
        switchOnListening();
        System.out.println("Start creating objects");
        createObject(5 * 1000 * 1000);
    }

    private static void switchOnListening() {
        List<GarbageCollectorMXBean> gcbeans = java.lang.management.ManagementFactory.getGarbageCollectorMXBeans();
        for (GarbageCollectorMXBean gcbean : gcbeans) {
            System.out.println("GC name:" + gcbean.getName());
            NotificationEmitter emitter = (NotificationEmitter) gcbean;
            NotificationListener listener = (notification, handback) -> {
                GarbageCollectionNotificationInfo info = GarbageCollectionNotificationInfo.from((CompositeData) notification.getUserData());
                String gcName = info.getGcName();
                String gcAction = info.getGcAction();
                String gcCause = info.getGcCause();

                long startTime = info.getGcInfo().getStartTime();
                long duration = info.getGcInfo().getDuration();

                System.out.println("start:" + startTime + " Name:" + gcName + ", action:" + gcAction + ", gcCause:" + gcCause + "(" + duration + " ms)");
            };
            emitter.addNotificationListener(listener, notification -> notification.getType().equals(GarbageCollectionNotificationInfo.GARBAGE_COLLECTION_NOTIFICATION), null);
        }
    }

    private static void createObject(int local) throws InterruptedException {
        int loopCounter = 1000;
        for (int loopIdx = 0; loopIdx < loopCounter; loopIdx++) {
            Object[] array = new Object[local];
            for (int idx = 0; idx < local; idx++) {
                array[idx] = new String(new char[0]);
            }
            Thread.sleep(10); //Label_1
        }
    }
}
