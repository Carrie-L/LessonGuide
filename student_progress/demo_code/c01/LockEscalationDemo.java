/**
 * Task 1.1.5: 锁升级机制观察实验
 * 
 * 演示synchronized从偏向锁→轻量级锁→重量级锁的升级过程
 * 
 * 学习目标：
 * 1. 理解JVM锁优化的三个阶段
 * 2. 观察不同锁状态的性能特征
 * 3. 掌握锁升级的触发条件
 */
public class LockEscalationDemo {
    
    private static final Object lock = new Object();
    private static volatile int counter = 0;
    
    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== synchronized锁升级机制演示 ===");
        
        // 场景1: 偏向锁 - 单线程重复获取同一个锁
        System.out.println("\n--- 场景1: 偏向锁测试 ---");
        testBiasedLock();
        
        Thread.sleep(1000);
        
        // 场景2: 轻量级锁 - 两个线程交替获取锁
        System.out.println("\n--- 场景2: 轻量级锁测试 ---");
        testLightweightLock();
        
        Thread.sleep(1000);
        
        // 场景3: 重量级锁 - 多线程竞争同一个锁
        System.out.println("\n--- 场景3: 重量级锁测试 ---");
        testHeavyweightLock();
    }
    
    /**
     * 偏向锁测试 - 单线程场景
     * JVM会将锁偏向于第一个获取锁的线程
     */
    private static void testBiasedLock() {
        final int ITERATIONS = 1000000;
        
        System.out.println("单线程重复获取锁 " + ITERATIONS + " 次...");
        
        long startTime = System.nanoTime();
        
        for (int i = 0; i < ITERATIONS; i++) {
            synchronized (lock) {
                counter++; // 简单操作
            }
        }
        
        long endTime = System.nanoTime();
        long duration = (endTime - startTime) / 1000000; // 转换为毫秒
        
        System.out.println("偏向锁性能: " + duration + "ms");
        System.out.println("平均每次获锁: " + String.format("%.2f", (double)duration * 1000000 / ITERATIONS) + "ns");
        System.out.println("💡 偏向锁特点: 几乎无开销，适合单线程重复获锁");
    }
    
    /**
     * 轻量级锁测试 - 两线程交替
     * 当出现其他线程尝试获取偏向锁时，升级为轻量级锁
     */
    private static void testLightweightLock() throws InterruptedException {
        final int ITERATIONS = 100000;
        counter = 0;
        
        System.out.println("两个线程交替获取锁...");
        
        long startTime = System.nanoTime();
        
        Thread thread1 = new Thread(() -> {
            for (int i = 0; i < ITERATIONS; i++) {
                synchronized (lock) {
                    counter++;
                    // 短暂持有锁
                    Thread.yield(); // 让出CPU，增加交替的可能性
                }
            }
        }, "LightWeight-Thread1");
        
        Thread thread2 = new Thread(() -> {
            for (int i = 0; i < ITERATIONS; i++) {
                synchronized (lock) {
                    counter++;
                    Thread.yield();
                }
            }
        }, "LightWeight-Thread2");
        
        thread1.start();
        thread2.start();
        
        thread1.join();
        thread2.join();
        
        long endTime = System.nanoTime();
        long duration = (endTime - startTime) / 1000000;
        
        System.out.println("轻量级锁性能: " + duration + "ms");
        System.out.println("最终计数: " + counter);
        System.out.println("💡 轻量级锁特点: 使用CAS操作，适合锁竞争不激烈的场景");
    }
    
    /**
     * 重量级锁测试 - 多线程激烈竞争
     * 当轻量级锁自旋失败时，升级为重量级锁
     */
    private static void testHeavyweightLock() throws InterruptedException {
        final int THREAD_COUNT = 10;
        final int ITERATIONS = 50000;
        counter = 0;
        
        System.out.println(THREAD_COUNT + "个线程激烈竞争同一个锁...");
        
        long startTime = System.nanoTime();
        
        Thread[] threads = new Thread[THREAD_COUNT];
        
        for (int i = 0; i < THREAD_COUNT; i++) {
            final int threadId = i;
            threads[i] = new Thread(() -> {
                for (int j = 0; j < ITERATIONS; j++) {
                    synchronized (lock) {
                        counter++;
                        // 模拟一些工作，增加锁持有时间
                        for (int k = 0; k < 100; k++) {
                            Math.sqrt(k);
                        }
                    }
                }
            }, "Heavy-Thread" + threadId);
        }
        
        // 启动所有线程
        for (Thread thread : threads) {
            thread.start();
        }
        
        // 等待所有线程完成
        for (Thread thread : threads) {
            thread.join();
        }
        
        long endTime = System.nanoTime();
        long duration = (endTime - startTime) / 1000000;
        
        System.out.println("重量级锁性能: " + duration + "ms");
        System.out.println("最终计数: " + counter);
        System.out.println("💡 重量级锁特点: 涉及系统调用，线程阻塞/唤醒开销大");
        
        // 性能对比总结
        System.out.println("\n=== 锁升级性能对比 ===");
        System.out.println("偏向锁 < 轻量级锁 < 重量级锁 (性能开销递增)");
        System.out.println("JVM根据竞争激烈程度自动选择最合适的锁实现");
    }
    
    /**
     * 演示锁升级不可逆性
     */
    public static void demonstrateLockEscalationPath() {
        System.out.println("\n=== 锁升级路径 ===");
        System.out.println("1. 偏向锁: 无锁竞争时，偏向第一个线程");
        System.out.println("2. 轻量级锁: 有其他线程尝试获取时，升级为轻量级锁");
        System.out.println("3. 重量级锁: 自旋失败或等待线程过多时，升级为重量级锁");
        System.out.println("4. 注意: 锁升级是单向的，不会降级");
    }
}