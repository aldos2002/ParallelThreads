package com.epam;

import org.apache.log4j.Logger;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

public class Car implements Runnable {

    private static final long MAX_DISTANCE = 10000;
    private static AtomicBoolean winnerExists = new AtomicBoolean(false);
    private static AtomicBoolean interruptedExists = new AtomicBoolean(false);
    private static String winner;


    Logger log = Logger.getLogger(Car.class);
    private CountDownLatch latch;
    private long friction;
    private long distance;
    private String name;

    public Car(String name, long friction, CountDownLatch latch) {
        this.name = name;
        this.friction = friction*100;
        this.latch = latch;
    }

    public void run() {
        long startTime = System.currentTimeMillis();

        try {
            while (distance < MAX_DISTANCE) {
                long currentTime = System.currentTimeMillis();
                if ((currentTime - startTime) / 1000 >= 5 && interruptedExists.compareAndSet(false, true)) {
                    Thread.currentThread().interrupt();
                }
                Thread.sleep(friction);
                distance += 100;
                log.info(name + " " + distance);
            }
            latch.countDown();
            if (winnerExists.compareAndSet(false, true)) {
                winner = name;
            }
            latch.await();
            if(winner.equals(name)) {
                log.info("The winner is: " + winner);
            }

        } catch (InterruptedException e) {
            latch.countDown();
            log.error("Car "+name+" DISQUALIFIED.", e);
        }
    }

}
