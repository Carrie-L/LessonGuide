package com.concurrency.c01;

public class VolatileDemo {
    // 实验一： 验证可见性
    private static volatile boolean visibilityFlag = false;

    // 实验二：验证非原子性
    private static volatile int atomicCounter = 0;

    public static void main(String[] args) {
        System.out.println("=== 实验1：volatile 保证内存可见性 ===");
        testVisibility();

        Thread.sleep(2000); // 等待实验完成

        System.out.println("\n=== 实验2：volatile 不保证原子性 ===");
        testAtomicity();
    }

    private static void testVisibility() throws InterruptedException {

    }
}
