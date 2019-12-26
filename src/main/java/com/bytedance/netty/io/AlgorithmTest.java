package com.bytedance.netty.io;

import java.time.Instant;
import java.util.concurrent.atomic.AtomicInteger;

public class AlgorithmTest {

    public static void main(String[] args) {
        long start = Instant.now().toEpochMilli();
        for (int i = 0; i < 1000000000; i++ ) {
            int a = 8000 << 1;
        }
        long end = Instant.now().toEpochMilli();
        System.out.println(end - start);


        long start2 = Instant.now().toEpochMilli();
        for (int i = 0; i < 1000000000; i++ ) {
            int b = 8000 * 2;
        }
        long end2 = Instant.now().toEpochMilli();
        System.out.println(end2 - start2);

    }

}
