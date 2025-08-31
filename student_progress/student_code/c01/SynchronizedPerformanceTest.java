/**
 * 🎯 Task 1.1.8: synchronized性能测试实践
 *
 * 📋 目标: 对比不同同步机制的性能表现
 * 🔧 技术: synchronized vs AtomicInteger vs volatile
 * 📊 测试指标: 吞吐量、延迟、数据正确性
 */
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

public class SynchronizedPerformanceTest {

    // 🔧 测试参数配置
    private static final int THREAD_COUNT = Runtime.getRuntime().availableProcessors() * 2;
    private static final int OPERATIONS_PER_THREAD = 100000;

    // 🎯 测试用共享变量
    private static volatile int volatileCounter = 0;
    private static int synchronizedCounter = 0;
    private static AtomicInteger atomicCounter = new AtomicInteger(0);

    /**
     * 🏭 synchronized性能测试
     * 使用内置锁保护共享变量
     */
    public static long testSynchronized() throws InterruptedException {
        // 📊 重置计数器
        synchronizedCounter = 0;

        // ⏰ 记录开始时间
        long startTime = System.currentTimeMillis();

        // 🔗 创建同步辅助工具
        CountDownLatch latch = new CountDownLatch(THREAD_COUNT);

        // 🧵 创建并启动线程数组
        Thread[] threads = new Thread[THREAD_COUNT];
        for (int i = 0; i < THREAD_COUNT; i++) {
            threads[i] = new Thread(() -> {
                try {
                    // 🔄 执行同步操作
                    for (int j = 0; j < OPERATIONS_PER_THREAD; j++) {
                        // 🔒 SYNCHRONIZED: 内置锁保护
                        synchronized(SynchronizedPerformanceTest.class) {
                            synchronizedCounter++;
                        }
                    }
                } finally {
                    // ✅ 通知完成
                    latch.countDown();
                }
            });
            threads[i].start();
        }

        // ⏳ 等待所有线程完成
        latch.await();

        // ⏰ 计算执行时间
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        // 📊 打印结果
        printResults("Synchronized", duration, synchronizedCounter);

        return duration;
    }

    /**
     * ⚛️ AtomicInteger性能测试
     * 使用原子类保证线程安全
     */
    public static long testAtomic() throws InterruptedException {
        // 📊 重置计数器
        atomicCounter.set(0);

        // ⏰ 记录开始时间
        long startTime = System.currentTimeMillis();

        // 🔗 创建同步辅助工具
        CountDownLatch latch = new CountDownLatch(THREAD_COUNT);

        // 🧵 创建并启动线程数组
        Thread[] threads = new Thread[THREAD_COUNT];
        for (int i = 0; i < THREAD_COUNT; i++) {
            threads[i] = new Thread(() -> {
                try {
                    // 🔄 执行原子操作
                    for (int j = 0; j < OPERATIONS_PER_THREAD; j++) {
                        // ⚛️ ATOMIC: 无锁原子操作
                        atomicCounter.incrementAndGet();
                    }
                } finally {
                    // ✅ 通知完成
                    latch.countDown();
                }
            });
            threads[i].start();
        }

        // ⏳ 等待所有线程完成
        latch.await();

        // ⏰ 计算执行时间
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        // 📊 打印结果
        printResults("AtomicInteger", duration, atomicCounter.get());

        return duration;
    }

    /**
     * 🔥 volatile性能测试 (展示数据竞争问题)
     * volatile只保证可见性，不保证原子性
     */
    public static long testVolatile() throws InterruptedException {
        // 📊 重置计数器
        volatileCounter = 0;

        // ⏰ 记录开始时间
        long startTime = System.currentTimeMillis();

        // 🔗 创建同步辅助工具
        CountDownLatch latch = new CountDownLatch(THREAD_COUNT);

        // 🧵 创建并启动线程数组
        Thread[] threads = new Thread[THREAD_COUNT];
        for (int i = 0; i < THREAD_COUNT; i++) {
            threads[i] = new Thread(() -> {
                try {
                    // 🔄 执行非安全操作
                    for (int j = 0; j < OPERATIONS_PER_THREAD; j++) {
                        // ⚡ VOLATILE: 可见但非原子
                        volatileCounter++;
                    }
                } finally {
                    // ✅ 通知完成
                    latch.countDown();
                }
            });
            threads[i].start();
        }

        // ⏳ 等待所有线程完成
        latch.await();

        // ⏰ 计算执行时间
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        // 📊 打印结果
        printResults("Volatile", duration, volatileCounter);

        return duration;
    }

    /**
     * 📊 结果打印方法
     */
    private static void printResults(String testName, long duration, int finalCount) {
        long totalOperations = (long) THREAD_COUNT * OPERATIONS_PER_THREAD;
        double opsPerSecond = (totalOperations * 1000.0) / duration;

        System.out.printf("=== %s 测试结果 ===\n", testName);
        System.out.printf("执行时间: %d ms\n", duration);
        System.out.printf("总操作数: %d\n", totalOperations);
        System.out.printf("最终计数: %d\n", finalCount);
        System.out.printf("每秒操作数: %.2f ops/sec\n", opsPerSecond);
        System.out.printf("数据正确性: %s\n",
            finalCount == totalOperations ? "✅ 正确" : "❌ 错误 (数据竞争)");
        System.out.println();
    }

    /**
     * 🚀 主函数：运行所有性能测试
     */
    public static void main(String[] args) throws InterruptedException {
        System.out.println("🚀 开始并发性能测试...");
        System.out.printf("测试环境: %d个线程，每个执行%d次操作\n\n",
                         THREAD_COUNT, OPERATIONS_PER_THREAD);

        // 🏃 依次运行三种测试
        long syncTime = testSynchronized();
        long atomicTime = testAtomic();
        long volatileTime = testVolatile();

        // 📈 性能对比分析
        System.out.println("📊 性能对比分析:");
        System.out.printf("🔒 Synchronized: %d ms (基准)\n", syncTime);
        System.out.printf("⚛️  AtomicInteger: %d ms (%.1fx faster)\n",
                         atomicTime, (double)syncTime / atomicTime);
        System.out.printf("⚡ Volatile: %d ms (%.1fx faster, 但数据错误!)\n",
                         volatileTime, (double)syncTime / volatileTime);

        System.out.println("\n💡 关键洞察:");
        System.out.println("1. 🔒 synchronized: 安全可靠，但性能开销较大");
        System.out.println("2. ⚛️ AtomicInteger: 安全高效，无锁编程的典型代表");
        System.out.println("3. ⚡ volatile: 性能最高但无法保证复合操作的原子性");
        System.out.println("4. 🎯 实际项目中，优先使用AtomicXXX类处理计数器场景");
    }
}