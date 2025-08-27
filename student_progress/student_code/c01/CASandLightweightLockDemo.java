/**
 * Task 1.1.6: CAS操作与轻量级锁深度实践
 * <p>
 * 任务要求：
 * 1. 手写CAS操作模拟
 * 2. 演示ABA问题
 * 3. 实现自旋重试机制
 * 4. 对比CAS vs synchronized性能
 * <p>
 * ⚠️ 手动输入代码，理解每个CAS操作的原理
 */

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class CASandLightweightLockDemo {

    private static int normalCounter = 0;
    private static final AtomicInteger atomicCounter = new AtomicInteger(); // 原子操作，线程安全的

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== CAS操作与轻量级锁机制演示 ===");

        System.out.println("\n--- 实验1: CAS vs Synchronized性能对比 ---");
        performanceComparison();

        // TODO 3: 手写CAS操作模拟
        System.out.println("\n--- 实验2: 手写CAS操作模拟 ---");
        simulateCASOperation();

        // TODO 4: ABA问题演示
        System.out.println("\n--- 实验3: ABA问题演示 ---");
        demonstrateABAProblem();
    }

    /**
     * TODO: 实现CAS vs Synchronized性能对比
     */
    private static void performanceComparison() throws InterruptedException {
        final int THREAD_COUNT = 4;
        final int ITERATIONS = 250000;

        System.out.println("=== 测试CAS性能 ===");
        atomicCounter.set(0);
        long startTime = System.nanoTime();
        Thread[] threads = new Thread[THREAD_COUNT];
        for (int i = 0; i < THREAD_COUNT; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < ITERATIONS; j++) {
                    atomicCounter.incrementAndGet(); // 原子操作，增加1并获取新值是一个不可分割的整体
                }
            }, "CAS-Thread" + i);
            threads[i].start();
        }

        for (Thread thread : threads) {
            thread.join();
        }

        long casTime = System.nanoTime() - startTime;
        System.out.println("CAS结果：" + atomicCounter.get() + ", 耗时：" + (casTime));

        startTime = System.nanoTime();
        System.out.println("=== 测试synchronized性能 ===");
        normalCounter = 0;
        Thread[] syncThreads = new Thread[THREAD_COUNT];
        final Object lock = new Object();
        for (int i = 0; i < THREAD_COUNT; i++) {
            syncThreads[i] = new Thread(() -> {
                for (int j = 0; j < ITERATIONS; j++) {
                    synchronized (lock) {
                        normalCounter++;
                    }
                }
            }, "SYNC-Thread" + i);
            syncThreads[i].start();
        }

        for (Thread thread : syncThreads) {
            thread.join();
        }

        long syncTime = System.nanoTime() - startTime;
        System.out.println("Synchronized结果：" + normalCounter + ", 耗时：" + (syncTime));
        System.out.println("\n性能比值: CAS比synchronized快 " +
                String.format("%.2f", (double) syncTime / casTime) + " 倍");
    }

    /**
     * 手写CAS操作模拟
     * 展示CAS的基本原理：比较并交换
     */
    private static void simulateCASOperation() {
        System.out.println("CAS操作原理演示:");

        class SimpleCAS {
            private volatile int value;

            public SimpleCAS(int initValue) {
                this.value = initValue;
            }

            public boolean compareAndSet(int expected, int newValue) {
                // 实现CAS逻辑
                if (expected == value) {
                    value = newValue;
                    return true;
                }
                return false;
            }

            public int getValue() {
                return value;
            }

            public int incrementAndGet() {
                // 实现自旋重试的递增
                int current;
                int next;
                do {
                    current = getValue();
                    next = current + 1;
                } while (!compareAndSet(current, next)); // 自旋直到成功
                return next;
            }
        }

        // 测试CAS操作的成功和失败情况
        SimpleCAS casValue = new SimpleCAS(10);
        System.out.println("初始值：" + casValue.getValue());

        // 成功的CAS操作
        boolean success1 = casValue.compareAndSet(10, 20);
        System.out.println("CAS(10->20): " + success1 + ", 当前值：" + casValue.getValue());

        // 失败的CAS操作
        boolean success2 = casValue.compareAndSet(10, 30);
        System.out.println("CAS(10->30): " + success2 + ", 当前值： " + casValue.getValue());

        // 自旋重试递增
        int newValue = casValue.incrementAndGet();
        System.out.println("自旋递增后：" + newValue);

    }

    /**
     * 实现ABA问题演示
     */
    private static void demonstrateABAProblem() throws InterruptedException {
        System.out.println("ABA问题演示:");

        // 使用AtomicReference模拟栈结构
        AtomicReference<Node> stackTop = new AtomicReference<>();

        // 1. 创建一个初始值为A的 AtomicReference
        final AtomicReference<String> atomicReference = new AtomicReference<>("A");

        // 初始化栈：A->B->C
//        Node nodeC = new Node("C", null);

        // 创建线程1执行慢速CAS操作
        Thread thread1 = new Thread(() -> {
            // 读取当前值A
            String currentValue = atomicReference.get(); // 记住期望值
            System.out.println("线程1：期望值是 " + currentValue);

            // 睡眠1s,让出CPU
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            // 尝试将A更新为B，用当初读到的currentValue去比较
            boolean isSuccess = atomicReference.compareAndSet(currentValue, "B");
            System.out.println("线程1： 慢速CAS操作是否成功：" + isSuccess);
        }, "thread1");


        // 创建线程2执行快速A→B→A操作序列
        Thread thread2 = new Thread(() -> {
            // 先将A更新为C
            atomicReference.compareAndSet("A", "C");
            System.out.println("线程2： A -> C");
            atomicReference.compareAndSet("C", "A");
            System.out.println("线程2： C -> A");
        });

        // 启动并等待线程完成
        thread1.start();
        thread2.start();
        thread1.join();
        thread2.join();

        // 观察ABA问题的发生
        System.out.println("最终结果：" + atomicReference.get());
    }

    // TODO: 定义Node类用于ABA问题演示
    private static class Node {
        final String data;
        final Node next;

        Node(String data, Node next) {
            this.data = data;
            this.next = next;
        }
    }
}