import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 🚀 ExecutorService版本的性能测试
 *
 * 📋 与SimplePerformanceTest.java的对比学习
 * 🔧 展示线程池的高级用法和优势
 */
public class ExecutorServicePerformanceTest {

    private static final int THREAD_COUNT = Runtime.getRuntime().availableProcessors() * 2;
    private static final int OPERATIONS_PER_THREAD = 100000;

    private static volatile int volatileCounter = 0;
    private static int synchronizedCounter = 0;
    private static AtomicInteger atomicCounter = new AtomicInteger(0);

    /**
     * 🏭 使用ExecutorService的synchronized性能测试
     *
     * 🎯 对比Thread数组版本的差异
     */
    public static long testSynchronizedWithExecutor() throws InterruptedException, ExecutionException {
        synchronizedCounter = 0;
        long startTime = System.currentTimeMillis();

        // 🏭 创建固定大小的线程池
        ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);

        try {
            // 📋 提交任务到线程池
            Future<?>[] futures = new Future[THREAD_COUNT];
            for (int i = 0; i < THREAD_COUNT; i++) {
                // 🔄 提交Callable任务（有返回值）
                futures[i] = executor.submit(() -> {
                    for (int j = 0; j < OPERATIONS_PER_THREAD; j++) {
                        synchronized(ExecutorServicePerformanceTest.class) {
                            synchronizedCounter++;
                        }
                    }
                    return "completed"; // 返回执行结果
                });
            }

            // ⏳ 等待所有任务完成
            for (Future<?> future : futures) {
                future.get(); // 获取结果，会等待任务完成
            }

        } finally {
            // 🔄 优雅关闭线程池
            executor.shutdown();
            executor.awaitTermination(5, TimeUnit.SECONDS);
        }

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        printResults("ExecutorService-Synchronized", duration, synchronizedCounter);
        return duration;
    }

    /**
     * ⚛️ 使用ExecutorService的AtomicInteger性能测试
     */
    public static long testAtomicWithExecutor() throws InterruptedException, ExecutionException {
        atomicCounter.set(0);
        long startTime = System.currentTimeMillis();

        ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);

        try {
            Future<?>[] futures = new Future[THREAD_COUNT];
            for (int i = 0; i < THREAD_COUNT; i++) {
                futures[i] = executor.submit(() -> {
                    for (int j = 0; j < OPERATIONS_PER_THREAD; j++) {
                        atomicCounter.incrementAndGet();
                    }
                    return "completed";
                });
            }

            for (Future<?> future : futures) {
                future.get();
            }

        } finally {
            executor.shutdown();
            executor.awaitTermination(5, TimeUnit.SECONDS);
        }

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        printResults("ExecutorService-Atomic", duration, atomicCounter.get());
        return duration;
    }

    /**
     * 🚀 高级ExecutorService用法示例
     */
    public static void demonstrateAdvancedUsage() throws InterruptedException, ExecutionException {
        System.out.println("\n=== ExecutorService高级用法示例 ===\n");

        // 1. 📊 线程池监控信息
        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(THREAD_COUNT);
        System.out.println("🏭 线程池初始状态:");
        System.out.println("   核心线程数: " + executor.getCorePoolSize());
        System.out.println("   最大线程数: " + executor.getMaximumPoolSize());
        System.out.println("   当前线程数: " + executor.getPoolSize());
        System.out.println("   活跃线程数: " + executor.getActiveCount());
        System.out.println("   队列大小: " + executor.getQueue().size());

        // 2. 🔄 提交不同类型的任务
        System.out.println("\n🔄 提交混合任务:");

        // Runnable任务（无返回值）
        executor.execute(() -> {
            System.out.println("   📤 Runnable任务执行完成");
        });

        // Callable任务（有返回值）
        Future<String> future = executor.submit(() -> {
            Thread.sleep(100);
            return "Callable任务结果";
        });

        System.out.println("   📥 Callable任务结果: " + future.get());

        // 3. ⏰ 超时控制
        Future<String> timeoutFuture = executor.submit(() -> {
            Thread.sleep(2000);
            return "慢任务结果";
        });

        try {
            String result = timeoutFuture.get(1, TimeUnit.SECONDS);
            System.out.println("   ⏰ 超时任务结果: " + result);
        } catch (TimeoutException e) {
            System.out.println("   ⏰ 任务超时，已取消");
            timeoutFuture.cancel(true);
        }

        // 4. 📊 执行后的监控信息
        System.out.println("\n🏭 线程池执行后状态:");
        System.out.println("   当前线程数: " + executor.getPoolSize());
        System.out.println("   活跃线程数: " + executor.getActiveCount());
        System.out.println("   已完成任务数: " + executor.getCompletedTaskCount());
        System.out.println("   队列大小: " + executor.getQueue().size());

        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);
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
     * 🚀 主函数：对比测试
     */
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        System.out.println("🚀 ExecutorService性能测试\n");
        System.out.printf("测试环境: %d个线程，每个执行%d次操作\n\n",
                         THREAD_COUNT, OPERATIONS_PER_THREAD);

        // 1. 🏃 运行ExecutorService版本的测试
        long syncTime = testSynchronizedWithExecutor();
        long atomicTime = testAtomicWithExecutor();

        // 2. 🎯 展示高级用法
        demonstrateAdvancedUsage();

        // 3. 📊 对比分析
        System.out.println("\n📊 ExecutorService vs Thread数组对比:");
        System.out.println("✅ ExecutorService优势:");
        System.out.println("   🏭 自动管理线程生命周期");
        System.out.println("   📊 内置监控和统计");
        System.out.println("   🛡️ 统一的异常处理");
        System.out.println("   🔄 优雅的关闭机制");
        System.out.println("   📋 更好的资源管理");
        System.out.println("   ⚡ 支持任务取消和超时");

        System.out.println("\n❌ ExecutorService缺点:");
        System.out.println("   🔧 学习曲线稍陡峭");
        System.out.println("   📊 增加了一层抽象");
        System.out.println("   ⚡ 轻量级场景可能性能稍差");

        System.out.println("\n💡 适用场景:");
        System.out.println("   🏭 企业级应用，长期运行的服务");
        System.out.println("   📊 需要监控和管理的场景");
        System.out.println("   🔄 复杂的任务调度需求");
        System.out.println("   🛡️ 对稳定性要求高的场景");
    }
}

