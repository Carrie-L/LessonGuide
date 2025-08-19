package com.concurrency.c01;

import java.util.concurrent.ThreadFactory;

// 目标：亲手验证synchronized的互斥机制
public class BasicSynchronization {
    private int counter = 0;
    private final Object lock = new Object();

    // 1，手动实现不安全的计数器
    public void unsafeIncrement() {
        int temp = counter;
        temp = temp + 1;
        counter = temp;

        // 故意增加执行时间，让数据竞争更容易出现
        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    // 2. 手动实现安全的计数器
    public synchronized void safeIncrement() {
        // 1. 使用synchronized保护临界区
        // 2. 确保原子性操作
        // 3. 对比与unsafeIncrement的差异
        int temp = counter;
        temp = temp + 1;
        counter = temp;

        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void explicitLockIncrement() {
        // 1. 使用显式synchronized(lock)
        // 2. 理解锁对象的作用
        // 3. 对比方法级synchronized的区别
        synchronized (lock) {  // 如果不声明一个lock，而是每次都 new Object() 一个锁，会导致锁失效。大家都有锁就相当于没有锁。
            int temp = counter;
            temp = temp + 1;
            counter = temp;
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
            }
        }
        // 要想让多个线程互斥，它们必须竞争同一个对象的监视器锁（monitor lock）。
    }


    public int getCounter() {
        return counter;
    }

    public void resetCounter() {
        counter = 0;
    }

    // 4. 手动实现多线程测试验证
    public static void testConcurrency() {
        BasicSynchronization sync = new BasicSynchronization();
        final int THREAD_COUNT = 10;
        final int INCREMENT_COUNT = 100;

        // 测试场景1： 不安全的增量操作
        System.out.println("=== 测试不安全的增量操作 ===");
        Thread[] unsafeThreads = new Thread[THREAD_COUNT];

        for (int i = 0; i < THREAD_COUNT; i++) {
            unsafeThreads[i] = new Thread(() -> {
                for (int j = 0; j < INCREMENT_COUNT; j++) {
                    sync.unsafeIncrement();
                }
            });
        }

        // 启动所有线程
        long startTime = System.currentTimeMillis();
        for (Thread thread : unsafeThreads) {
            thread.start();
        }

        // 等待所有线程完成
        for (Thread thread : unsafeThreads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        long endTime = System.currentTimeMillis();

        System.out.println("预期结果: " + (THREAD_COUNT * INCREMENT_COUNT));
        System.out.println("实际结果: " + sync.getCounter());
        System.out.println("数据丢失: " + (THREAD_COUNT * INCREMENT_COUNT - sync.getCounter()));
        System.out.println("执行时间: " + (endTime - startTime) + "ms");

        // 继续实现安全版本的测试
        sync.resetCounter();
        System.out.println("\n=== 测试安全的增量操作 : 方法级synchronized ===");

        // 实现安全版本的多线程测试
        Thread[] safeThreads = new Thread[THREAD_COUNT];
        for (int i = 0; i < THREAD_COUNT; i++) {
            safeThreads[i] = new Thread(() -> {
                for (int j = 0; j < INCREMENT_COUNT; j++) {
                    sync.safeIncrement();
                }
            });
        }

        long startTime1 = System.currentTimeMillis();
        for (int i = 0; i < THREAD_COUNT; i++) {
            safeThreads[i].start();
        }
        for (Thread thread : safeThreads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        long endTime1 = System.currentTimeMillis();
        System.out.println("预期结果: " + (THREAD_COUNT * INCREMENT_COUNT));
        System.out.println("实际结果: " + sync.getCounter());
        System.out.println("数据丢失: " + (THREAD_COUNT * INCREMENT_COUNT - sync.getCounter()));
        System.out.println("执行时间: " + (endTime1 - startTime1) + "ms");


        sync.resetCounter();
        System.out.println("\n=== 测试安全的增量操作 : 显式级synchronized ===");

        // 实现安全版本的多线程测试
        Thread[] explicitThreads = new Thread[THREAD_COUNT];
        for (int i = 0; i < THREAD_COUNT; i++) {
            explicitThreads[i] = new Thread(() -> {
                for (int j = 0; j < INCREMENT_COUNT; j++) {
                    sync.explicitLockIncrement();
                }
            });
        }

        long startTime2 = System.currentTimeMillis();
        for (int i = 0; i < THREAD_COUNT; i++) {
            explicitThreads[i].start();
        }
        for (Thread thread : explicitThreads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        long endTime2 = System.currentTimeMillis();
        System.out.println("预期结果: " + (THREAD_COUNT * INCREMENT_COUNT));
        System.out.println("实际结果: " + sync.getCounter());
        System.out.println("数据丢失: " + (THREAD_COUNT * INCREMENT_COUNT - sync.getCounter()));
        System.out.println("执行时间: " + (endTime2 - startTime2) + "ms");

    }


}

// 实现主函数测试
class SynchronizationDemo {
    public static void main(String[] args) {
        System.out.println("🔒synchronized机制验证实验");
        System.out.println("目标： 理解互斥锁如何解决数据竞争问题\n");

        BasicSynchronization.testConcurrency();

        // 添加更多测试场景
        // 1. 测试重入锁特性
        // 2. 测试锁的粒度影响
        // 3. 测试死锁场景
    }
}
