package com.otusproject.justforfun;

import org.openjdk.jol.info.ClassLayout;
import org.openjdk.jol.vm.VM;

public class HashCodeInHashMapTest {
    private static volatile Object sink;

    public static void main(String[] args) {
        objectLayout();
    }

    private static void objectLayout() {
        ClassExample objectExample = new ClassExample();
        System.out.println(ClassLayout.parseInstance(objectExample).toPrintable());
        System.out.println("Hash code is " + objectExample.hashCode() + "\n\n\n");

        long lastAddr = VM.current().addressOf(objectExample);
        int moves = 0;
        for (int idx = 0; idx < 100000; idx++) {
            long cur = VM.current().addressOf(objectExample);
            if (cur != lastAddr) {
                moves++;
                System.out.printf("*** Move %2d, object is at %x%n%n", moves, cur);
                System.out.println(ClassLayout.parseInstance(objectExample).toPrintable());
                System.out.println("Hash code is " + objectExample.hashCode() + "\n\n\n");
                lastAddr = cur;
            }

            // make garbage
            for (int c = 0; c < 1000; c++) {
                sink = new Object();
            }
        }

        long finalAddr = VM.current().addressOf(objectExample);
        System.out.printf("*** Final object is at %x%n", finalAddr);
        System.out.println(ClassLayout.parseInstance(objectExample).toPrintable());
        System.out.println("Hash code is " + objectExample.hashCode() + "\n\n\n");


        System.gc();
        makeGarbage();
        sleep();
        System.out.println(ClassLayout.parseInstance(objectExample).toPrintable());
        System.out.println("Hash code is " + objectExample.hashCode());

        System.gc();
        makeGarbage();
        sleep();

        System.out.println(ClassLayout.parseInstance(objectExample).toPrintable());
        System.out.println("Hash code is " + objectExample.hashCode());
    }

    public static class ClassExample {
        boolean valBool;
    }

    private static void makeGarbage() {
        for (int idx = 0; idx < Integer.MAX_VALUE; idx++) {
            Object obj = new String[10000];
        }
    }


    private static void sleep() {
        try {
            Thread.sleep(1_000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
