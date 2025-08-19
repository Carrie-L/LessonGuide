package com.concurrency.c01;

import java.util.concurrent.CountDownLatch;

public class SynchronizedDemo {

    private int counter = 0;
    private static final int THREAD_COUNT = 10;
    private static final int INCREMENTS_PER_THREAD = 1000;

    public synchronized void safeIncrement() {
        counter++;
    }

    /**
     * 线程不安全方法
     * 实际会执行三个步骤：
     * 1. 读取counter的值
     * 2. counter + 1
     * 3. 将新值写回counter
     */
    public void unsafeIncrement() {
        counter++;
    }

    public void unsafeIncrement_MoreObvious() {
        // 1. 读取当前值
        int currentValue = counter;

        // 2. 模拟一些耗时操作或线程切换的可能点
        Thread.yield(); // 这是一个提示，告诉调度器可以切换到其它线程了

        // 3. 在读取到的旧值基础上加一
        currentValue++;

        // 4. 将新值写回
        counter = currentValue;
    }

    public void runUnsafeTest_MoreObvious() throws InterruptedException {
        // CountDownLatch 用于确保所有线程都执行完毕
        CountDownLatch latch = new CountDownLatch(THREAD_COUNT);
        counter = 0; // 重置计数器

        for (int i = 0; i < THREAD_COUNT; i++) {
            new Thread(() -> {
                for (int j = 0; j < INCREMENTS_PER_THREAD; j++) {
                    unsafeIncrement_MoreObvious();
                }
                latch.countDown(); // 线程执行完毕，计数减1
            }).start();
        }

        latch.await(); // 等待所有线程结束
        System.out.println("更加容易出错的累加结果：" + counter + "（预期：10000)");
    }

    public void runUnsafeTest() throws InterruptedException {
        // CountDownLatch 用于确保所有线程都执行完毕
        CountDownLatch latch = new CountDownLatch(THREAD_COUNT);
        counter = 0; // 重置计数器

        for (int i = 0; i < THREAD_COUNT; i++) {
            new Thread(() -> {
                for (int j = 0; j < INCREMENTS_PER_THREAD; j++) {
                    unsafeIncrement();
                }
                latch.countDown(); // 线程执行完毕，计数减1
            }).start();
        }

        latch.await(); // 等待所有线程结束
        System.out.println("不安全的累加结果：" + counter + "（预期：10000)");
    }

    public void runSafeTest() throws InterruptedException {
        // CountDownLatch 用于确保所有线程都执行完毕
        CountDownLatch latch = new CountDownLatch(THREAD_COUNT);
        counter = 0; // 重置计数器

        for (int i = 0; i < THREAD_COUNT; i++) {
            new Thread(() -> {
                for (int j = 0; j < INCREMENTS_PER_THREAD; j++) {
                    safeIncrement();
                }
                latch.countDown(); // 线程执行完毕，计数减1
            }).start();
        }

        latch.await(); // 等待所有线程结束
        System.out.println("不安全的累加结果：" + counter + "（预期：10000)");
    }

    public static void main(String[] args) throws InterruptedException {
        SynchronizedDemo demo = new SynchronizedDemo();
//        demo.runUnsafeTest();
        demo.runUnsafeTest_MoreObvious();
        demo.runSafeTest();
    }
}
