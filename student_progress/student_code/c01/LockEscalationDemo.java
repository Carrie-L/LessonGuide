public class LockEscalationDemo {

    private static final Object lock = new Object(); // 共享锁对象
    private static volatile int counter = 0; // 计数器


    public static void main(String[] args) throws InterruptedException {

        System.out.println("\n--- 场景1: 偏向锁测试 ---");
        testBiasedLock();

        Thread.sleep(1000);
        System.out.println("\n--- 场景2: 轻量级锁测试 ---");
        testLightWeightLock();

        Thread.sleep(1000);
        System.out.println("\n--- 场景3: 重量级锁测试 ---");
        testHeavyweightLock();

        // 性能对比总结
        System.out.println("\n=== 锁升级性能对比总结 ===");
        System.out.println("偏向锁 < 轻量级锁 < 重量级锁（性能开销递增）");
        System.out.println("JVM根据竞争激烈程度自动选择最合适的锁实现");


    }

    /**
     * 偏向锁测试
     * 单线程重复获取同一个锁
     * <p>
     * 1. 偏向锁的平均获锁时间通常在 1-5纳秒 范围内，这几乎是无开销的
     * 2. 这个指标让我们能够量化锁优化的效果 - 数字比理论更有说服力
     * 3. 后面对比轻量级锁(50-100ns)和重量级锁(1000ns+)时，差距会非常明显
     */
    private static void testBiasedLock() {
        final int ITERATIONS = 1000000; // 100万次迭代

        System.out.println("单线程重复获取锁" + ITERATIONS + "次... ");

        // 记录开始时间（纳秒级精度）
        long startTime = System.nanoTime(); // 偏向锁的开销极小，需要高精度测量

        // 单线程循环 - 这里会触发偏向锁优化
        for (int i = 0; i < ITERATIONS; i++) {
            synchronized (lock) {
                counter++;
            }
        }

        // 计算并输出性能数据
        long endTime = System.nanoTime();
        long duration = (endTime - startTime) / 1000000; // 转换为毫秒
        System.out.println("偏向锁性能：" + duration + " ms");
        System.out.println("平均每次获锁：" + String.format("%.2f", (double) duration * 1000000 / ITERATIONS) + "ns");
        System.out.println("\uD83D\uDCA1 偏向锁特点：几乎无开销，适合单线程重复获锁");
    }

    /**
     * 轻量级锁会在第二个线程尝试获取偏向锁时被触发，JVM会撤销偏向，升级为基于CAS的轻量级锁。
     * <p>
     * 🧠 关键设计解释：
     * <p>
     * 为什么用 Thread.yield()？
     * - 主动让出CPU时间片，增加线程切换频率
     * - 让两个线程更有可能"交替"获取锁，而不是一个线程连续获取
     * - 模拟真实应用中的线程调度情况
     * <p>
     * 为什么迭代次数减少到10万？
     * - 线程创建和切换有开销，太多迭代会掩盖锁本身的性能
     * - 重点是观察锁升级的效果，不是测试极限性能
     * <p>
     * 为什么验证最终计数？
     * - 确保synchronized正确保护了共享状态
     * - 如果计数不等于20万，说明有并发问题
     * <p>
     * ★ 深度分析 ─────────────────────────────────────
     * 1. 轻量级锁使用 CAS (Compare-And-Swap) 操作来竞争锁，每次都要检查锁状态
     * 2. 线程切换导致的 CPU缓存失效 也会增加获锁开销
     * 3. 偏向锁只需要检查 "这还是我的锁吗？"，而轻量级锁需要 "我能获取这个锁吗？"
     * ─────────────────────────────────────────────────
     */
    private static void testLightWeightLock() throws InterruptedException {
        final int ITERATIONS = 100000; // 10万次（比偏向锁少，因为设计到线程切换）
        counter = 0;

        System.out.println("两个线程交替获取锁。。。");

        long startTime = System.nanoTime();

        // 创建两个线程交替获取锁
        Thread thread1 = new Thread(() -> {
            for (int i = 0; i < ITERATIONS; i++) {
                synchronized (lock) {
                    counter++;
                    Thread.yield(); // 主动让出CPU, 增加线程交替的概率
                }
            }
        }, "LightWeight-Thread1");

        Thread thread2 = new Thread(() -> {
            for (int i = 0; i < ITERATIONS; i++) {
                synchronized (lock) {
                    counter++;
                    Thread.yield(); // 主动让出CPU, 增加线程交替的概率
                }
            }
        }, "LightWeight-Thread2");

        thread1.start();
        thread2.start();

        // 等待两个线程都完成
        thread1.join();
        thread2.join();

        long endTime = System.nanoTime();
        long duration = endTime - startTime;

        System.out.println("轻量级锁性能：" + (duration) / 1000000 + " ms.");
        System.out.println("平均每次获锁：" + duration / (ITERATIONS * 2) + " ns");
        System.out.println("最终计数：" + counter + "(应该等于 " + (ITERATIONS * 2) + ")");
        System.out.println("\uD83D\uDCA1 轻量级锁特点：使用CAS操作，适合锁竞争不激烈的场景");
    }

    /**
     * 重量级锁是最”昂贵“的锁实现，当轻量级锁的CAS自旋失败次数过多时，JVM会升级为重量级锁。这涉及系统调用和线程阻塞/唤醒。
     * 重量级锁测试 - 多线程激烈竞争同一个锁
     * 目标：观察高竞争场景下JVM升级为重量级锁的性能特征
     * <p>
     * 🧠 关键设计解释：
     * <p>
     * 为什么用10个线程？
     * - 足够多的线程确保激烈竞争，迫使JVM升级到重量级锁
     * - 模拟真实服务器应用的高并发场景
     * <p>
     * 为什么加入 Math.sqrt(k) 计算？
     * - 增加锁持有时间，让其他线程更容易发生阻塞
     * - 模拟真实业务中synchronized块内的计算逻辑
     * - 放大重量级锁的性能差异
     * <p>
     * 预期结果：
     * - 重量级锁应该比轻量级锁慢 5-10倍
     * - 总耗时主要花在线程阻塞和唤醒上
     */
    private static void testHeavyweightLock() throws InterruptedException {
        final int THREAD_COUNT = 10; // 10个线程同时竞争
        final int ITERATIONS = 50000; // 每个线程5万操作
        counter = 0;

        System.out.println(THREAD_COUNT + "个线程激烈竞争同一个锁... ");

        long startTime = System.nanoTime();
        Thread[] threads = new Thread[THREAD_COUNT];

        // 创建多个线程进行锁竞争
        for (int i = 0; i < THREAD_COUNT; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < ITERATIONS; j++) {
                    synchronized (lock) {
                        counter++;
                        // 模拟一些工作
                        for (int k = 0; k < 100; k++) {
                            Math.sqrt(k); // 简单的CPU密集操作
                        }
                    }
                }
            }, "HeavyWeightLock-" + i);
        }

        // 启动所有线程并等待完成
        for (int i = 0; i < THREAD_COUNT; i++) {
            threads[i].start();
        }

        for (int i = 0; i < THREAD_COUNT; i++) {
            threads[i].join();
        }

        // 计算并输出性能数据和锁升级分析
        long endTime = System.nanoTime();
        long duration = endTime - startTime;
        System.out.println("重量级锁性能：" + (duration / 1000000) + "ms");
        System.out.println("平均每次获锁(并发吞吐量)：" + (duration / (THREAD_COUNT * ITERATIONS)) + " ns");
        System.out.println("最终计数：" + counter + "应等于 " + (THREAD_COUNT * ITERATIONS));
        System.out.println("\uD83D\uDCA1 重量级锁特点：设计系统调用，线程阻塞/唤醒开销大");

        lock.hashCode();

    }
}
