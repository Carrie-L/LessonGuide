package com.concurrency.c01;

public class VolatileDemo {
    // 实验一： 验证可见性
    private static volatile boolean visibilityFlag = false;

    // 实验二：验证非原子性
    private static volatile int atomicCounter = 0;

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== 实验1：volatile 保证内存可见性 ===");
        testVisibility();

        Thread.sleep(2000); // 等待实验完成

        System.out.println("\n=== 实验2：volatile 不保证原子性 ===");
        testAtomicity();
    }

    private static void testVisibility() throws InterruptedException {
        // 重置标志
        visibilityFlag = false;

        // 读取者线程
        Thread reader = new Thread(() -> {
            System.out.println("读取者线程：等待visibilityFlag变为true...");
            while (!visibilityFlag) {
                // 因为visibilityFlag 是volatile的，CPU回强制从主内存读取，所以这个循环会正常结束。
            }
            System.out.println("读取者线程：成功检测到visibilityFlag的变化！");
        });

        // 写入者线程
        Thread writer = new Thread(() -> {
            try {
                // 延迟一下，确保读取者先进入等待状态
                Thread.sleep(500);
                System.out.println("写入者线程：正在修改visibilityFlag...");
                visibilityFlag = true; // volatile 写操作
                System.out.println("写入者线程：修改完成。");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        reader.start();
        writer.start();

        reader.join(); // 等待读取者线程结束
        writer.join(); // 等待写入者线程结束
    }

    private static void testAtomicity() throws InterruptedException {
        // 重置计数器
        atomicCounter = 0;

        final int THREAD_COUNT = 10;
        final int INCREMENT_COUNT = 1000;

        Thread[] threads = new Thread[THREAD_COUNT];
        for (int i = 0; i < THREAD_COUNT; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < INCREMENT_COUNT; j++) {
                    // 关键点：虽然atomicCounter是volatile的，但 ++ 操作仍然不是原子的，
                    // 它仍然包含“读-改-写”三个步骤，多个线程可能在同一时刻执行这三步，导致数据丢失
                    atomicCounter++;
                }
            });
        }

        for (Thread thread : threads) {
            thread.start();
        }

        for (Thread thread : threads) {
            thread.join();
        }

        System.out.println("预期结果：" + (THREAD_COUNT * INCREMENT_COUNT));
        System.out.println("实际结果：" + atomicCounter);

    }
}
