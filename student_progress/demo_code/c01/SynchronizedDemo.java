// 演示代码: synchronized同步机制
// 目标: 先看到问题，再解决问题
public class SynchronizedDemo {
    
    private static int unsafeCounter = 0;      // 不安全的计数器
    private static int safeCounter = 0;        // 安全的计数器
    private static final Object lock = new Object();
    
    public static void main(String[] args) throws InterruptedException {
        final int THREAD_COUNT = 10;
        final int INCREMENT_COUNT = 1000;
        
        System.out.println("=== synchronized同步机制演示 ===");
        System.out.println("每个线程递增" + INCREMENT_COUNT + "次，共" + THREAD_COUNT + "个线程");
        System.out.println("期望最终结果: " + (THREAD_COUNT * INCREMENT_COUNT));
        
        System.out.println("\n--- 步骤1: 展示数据竞争问题 ---");
        testUnsafeIncrement(THREAD_COUNT, INCREMENT_COUNT);
        
        System.out.println("\n--- 步骤2: synchronized解决方案 ---");
        testSafeIncrement(THREAD_COUNT, INCREMENT_COUNT);
        
        System.out.println("\n--- 步骤3: 性能对比分析 ---");
        performanceComparison();
    }
    
    // 不安全的递增操作 - 演示数据竞争
    private static void testUnsafeIncrement(int threadCount, int incrementCount) 
            throws InterruptedException {
        unsafeCounter = 0;
        Thread[] threads = new Thread[threadCount];
        
        long startTime = System.currentTimeMillis();
        
        // 创建多个线程同时对counter进行++操作
        for (int i = 0; i < threadCount; i++) {
            final int threadId = i;
            threads[i] = new Thread(() -> {
                for (int j = 0; j < incrementCount; j++) {
                    unsafeCounter++;  // ❌ 线程不安全！可能丢失数据
                    // ++操作不是原子的: 1)读取 2)递增 3)写回
                }
            }, "UnsafeThread-" + threadId);
            threads[i].start();
        }
        
        // 等待所有线程完成
        for (Thread thread : threads) {
            thread.join();
        }
        
        long endTime = System.currentTimeMillis();
        
        System.out.println("期望结果: " + (threadCount * incrementCount));
        System.out.println("实际结果: " + unsafeCounter);
        System.out.println("数据丢失: " + (threadCount * incrementCount - unsafeCounter) + 
                         " (" + String.format("%.2f", 
                         (1.0 - (double)unsafeCounter/(threadCount * incrementCount)) * 100) + "%)");
        System.out.println("执行时间: " + (endTime - startTime) + "ms");
    }
    
    // 安全的递增操作 - 使用synchronized解决
    private static void testSafeIncrement(int threadCount, int incrementCount) 
            throws InterruptedException {
        safeCounter = 0;
        Thread[] threads = new Thread[threadCount];
        
        long startTime = System.currentTimeMillis();
        
        for (int i = 0; i < threadCount; i++) {
            final int threadId = i;
            threads[i] = new Thread(() -> {
                for (int j = 0; j < incrementCount; j++) {
                    synchronized (lock) {    // ✅ 同步块保证原子性
                        safeCounter++;       // 现在是线程安全的
                    }
                }
            }, "SafeThread-" + threadId);
            threads[i].start();
        }
        
        for (Thread thread : threads) {
            thread.join();
        }
        
        long endTime = System.currentTimeMillis();
        
        System.out.println("期望结果: " + (threadCount * incrementCount));
        System.out.println("实际结果: " + safeCounter);
        System.out.println("数据正确: " + (safeCounter == threadCount * incrementCount ? "✅ 是" : "❌ 否"));
        System.out.println("执行时间: " + (endTime - startTime) + "ms");
    }
    
    // 性能对比分析
    private static void performanceComparison() throws InterruptedException {
        final int ITERATIONS = 100000;
        
        System.out.println("\n=== 性能对比 (单线程场景) ===");
        
        // 1. 无同步性能
        long startTime = System.nanoTime();
        int counter = 0;
        for (int i = 0; i < ITERATIONS; i++) {
            counter++;
        }
        long noSyncTime = System.nanoTime() - startTime;
        
        // 2. synchronized代码块性能
        startTime = System.nanoTime();
        int syncCounter = 0;
        Object syncLock = new Object();
        for (int i = 0; i < ITERATIONS; i++) {
            synchronized (syncLock) {
                syncCounter++;
            }
        }
        long syncBlockTime = System.nanoTime() - startTime;
        
        // 3. synchronized方法性能
        SyncMethodTest test = new SyncMethodTest();
        startTime = System.nanoTime();
        for (int i = 0; i < ITERATIONS; i++) {
            test.synchronizedIncrement();
        }
        long syncMethodTime = System.nanoTime() - startTime;
        
        System.out.println("无同步:       " + String.format("%,d", noSyncTime / 1000) + " μs");
        System.out.println("synchronized块: " + String.format("%,d", syncBlockTime / 1000) + " μs (慢 " + 
                         String.format("%.1f", (double)syncBlockTime / noSyncTime) + "倍)");
        System.out.println("synchronized方法: " + String.format("%,d", syncMethodTime / 1000) + " μs (慢 " + 
                         String.format("%.1f", (double)syncMethodTime / noSyncTime) + "倍)");
        
        System.out.println("\n💡 关键洞察:");
        System.out.println("1. synchronized有性能开销，但保证了线程安全");
        System.out.println("2. 在单线程场景下，开销主要是锁的获取和释放");
        System.out.println("3. 多线程竞争时，性能差距会更大");
    }
    
    // 辅助类：测试synchronized方法
    private static class SyncMethodTest {
        private int counter = 0;
        
        public synchronized void synchronizedIncrement() {
            counter++;
        }
    }
}