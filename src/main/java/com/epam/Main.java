package com.epam;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {

    public static final int CARS = 4;

    public static void main(String[] args) {
        CountDownLatch latch = new CountDownLatch(CARS);
        ExecutorService exec = Executors.newCachedThreadPool();
        for (int i = 1; i <= CARS; i++) {
            Runnable car = new Car("car"+i, i, latch);
            exec.execute(car);
        }
        exec.shutdown();
    }
}
