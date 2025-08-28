/**
 * Task 1.1.8: Synchronized性能测试深度分析
 * 
 * 全面测试synchronized在不同场景下的性能表现
 * 
 * 学习目标：
 * 1. 量化synchronized的性能开销
 * 2. 对比不同同步粒度的影响
 * 3. 分析锁竞争对性能的影响
 * 4. 掌握性能优化策略
 */
public class SynchronizedPerformanceTest {
    
    private static final int THREAD_COUNT = Runtime.getRuntime().availableProcessors();
    private static final int ITERATIONS = 1000000;
    
    private static volatile int volatileCounter = 0;
    private static int unsafeCounter = 0;
    private static int synchronizedCounter = 0;
    
    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== Synchronized性能测试分析 ===");
        System.out.println("测试环境: " + THREAD_COUNT + "个线程，每个执行" + ITERATIONS + "次操作");
        
        // 性能基准测试
        runPerformanceBenchmarks();
        
        // 锁粒度影响测试
        testLockGranularity();
        
        // 锁竞争程度测试
        testLockContention();
        
        // 锁优化策略测试
        testOptimizationStrategies();
    }
    
    /**
     * 性能基准测试
     * 对比无同步、volatile、synchronized的性能
     */
    private static void runPerformanceBenchmarks() throws InterruptedException {
        System.out.println("\n--- 基准性能测试 ---");
        
        // 1. 无同步基准
        long noSyncTime = measureTime("无同步", () -> {
            Thread[] threads = new Thread[THREAD_COUNT];
            for (int i = 0; i < THREAD_COUNT; i++) {
                threads[i] = new Thread(() -> {
                    for (int j = 0; j < ITERATIONS; j++) {
                        unsafeCounter++; // 不安全操作
                    }
                });
                threads[i].start();
            }
            
            for (Thread thread : threads) {
                try {
                    thread.join();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });
        
        // 2. Volatile测试
        volatileCounter = 0;
        long volatileTime = measureTime("Volatile", () -> {
            Thread[] threads = new Thread[THREAD_COUNT];
            for (int i = 0; i < THREAD_COUNT; i++) {
                threads[i] = new Thread(() -> {
                    for (int j = 0; j < ITERATIONS; j++) {
                        volatileCounter++; // volatile但非原子
                    }
                });
                threads[i].start();
            }
            
            for (Thread thread : threads) {
                try {
                    thread.join();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });
        
        // 3. Synchronized测试
        synchronizedCounter = 0;
        final Object lock = new Object();
        long syncTime = measureTime("Synchronized", () -> {
            Thread[] threads = new Thread[THREAD_COUNT];
            for (int i = 0; i < THREAD_COUNT; i++) {
                threads[i] = new Thread(() -> {
                    for (int j = 0; j < ITERATIONS; j++) {
                        synchronized (lock) {
                            synchronizedCounter++; // 安全操作
                        }
                    }
                });
                threads[i].start();
            }
            
            for (Thread thread : threads) {
                try {
                    thread.join();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });
        
        // 结果分析
        System.out.println("\n=== 性能结果 ===");
        System.out.println("无同步: " + noSyncTime + "ms (结果: " + unsafeCounter + ")");
        System.out.println("Volatile: " + volatileTime + "ms (结果: " + volatileCounter + ")");
        System.out.println("Synchronized: " + syncTime + "ms (结果: " + synchronizedCounter + ")");
        
        System.out.println("\n=== 性能比较 ===");
        System.out.println("Volatile vs 无同步: " + String.format("%.2fx", (double)volatileTime / noSyncTime));
        System.out.println("Synchronized vs 无同步: " + String.format("%.2fx", (double)syncTime / noSyncTime));
        System.out.println("Synchronized vs Volatile: " + String.format("%.2fx", (double)syncTime / volatileTime));
    }
    
    /**
     * 锁粒度影响测试
     * 对比粗粒度锁和细粒度锁的性能
     */
    private static void testLockGranularity() throws InterruptedException {
        System.out.println("\n--- 锁粒度影响测试 ---");
        
        // 粗粒度锁：整个方法同步
        class CoarseGrainedCounter {
            private int count = 0;
            
            public synchronized void increment() {
                count++;
                // 模拟一些额外工作
                Math.sqrt(count);
            }
            
            public synchronized int getCount() {
                return count;
            }
        }
        
        // 细粒度锁：只同步必要部分
        class FineGrainedCounter {
            private int count = 0;
            private final Object lock = new Object();
            
            public void increment() {
                // 非同步部分
                double temp = Math.sqrt(System.nanoTime());
                
                synchronized (lock) {
                    count++; // 只同步必要部分
                }
            }
            
            public int getCount() {
                synchronized (lock) {
                    return count;
                }
            }
        }
        
        // 测试粗粒度锁
        CoarseGrainedCounter coarseCounter = new CoarseGrainedCounter();
        long coarseTime = measureTime("粗粒度锁", () -> {
            Thread[] threads = new Thread[THREAD_COUNT];
            for (int i = 0; i < THREAD_COUNT; i++) {
                threads[i] = new Thread(() -> {
                    for (int j = 0; j < ITERATIONS / 10; j++) {
                        coarseCounter.increment();
                    }
                });
                threads[i].start();
            }
            
            for (Thread thread : threads) {
                try {
                    thread.join();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });
        
        // 测试细粒度锁
        FineGrainedCounter fineCounter = new FineGrainedCounter();
        long fineTime = measureTime("细粒度锁", () -> {
            Thread[] threads = new Thread[THREAD_COUNT];
            for (int i = 0; i < THREAD_COUNT; i++) {
                threads[i] = new Thread(() -> {
                    for (int j = 0; j < ITERATIONS / 10; j++) {
                        fineCounter.increment();
                    }
                });
                threads[i].start();
            }
            
            for (Thread thread : threads) {
                try {
                    thread.join();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });
        
        System.out.println("粗粒度锁: " + coarseTime + "ms (结果: " + coarseCounter.getCount() + ")");
        System.out.println("细粒度锁: " + fineTime + "ms (结果: " + fineCounter.getCount() + ")");
        System.out.println("细粒度锁性能提升: " + String.format("%.2fx", (double)coarseTime / fineTime));
    }
    
    /**
     * 锁竞争程度测试
     * 测试不同竞争强度对性能的影响
     */
    private static void testLockContention() throws InterruptedException {
        System.out.println("\n--- 锁竞争程度测试 ---");
        
        final Object lock = new Object();
        
        // 低竞争：短时间持锁
        long lowContentionTime = testContention("低竞争", lock, 1);
        
        // 中等竞争：中等时间持锁
        long mediumContentionTime = testContention("中等竞争", lock, 10);
        
        // 高竞争：长时间持锁
        long highContentionTime = testContention("高竞争", lock, 100);
        
        System.out.println("\n=== 竞争程度影响 ===");
        System.out.println("低竞争: " + lowContentionTime + "ms");
        System.out.println("中等竞争: " + mediumContentionTime + "ms");
        System.out.println("高竞争: " + highContentionTime + "ms");
        System.out.println("高/低竞争比: " + String.format("%.2fx", (double)highContentionTime / lowContentionTime));
    }
    
    /**
     * 测试特定竞争强度的性能
     */
    private static long testContention(String name, Object lock, int workload) throws InterruptedException {
        return measureTime(name, () -> {
            Thread[] threads = new Thread[THREAD_COUNT];
            for (int i = 0; i < THREAD_COUNT; i++) {
                threads[i] = new Thread(() -> {
                    for (int j = 0; j < ITERATIONS / 100; j++) {
                        synchronized (lock) {
                            // 模拟不同强度的工作负载
                            for (int k = 0; k < workload; k++) {
                                Math.sqrt(k);
                            }
                        }
                    }
                });
                threads[i].start();
            }
            
            for (Thread thread : threads) {
                try {
                    thread.join();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });
    }
    
    /**
     * 锁优化策略测试
     * 展示不同优化技术的效果
     */
    private static void testOptimizationStrategies() throws InterruptedException {
        System.out.println("\n--- 锁优化策略测试 ---");
        
        // 策略1：锁分离
        testLockSeparation();
        
        // 策略2：读写锁
        testReadWriteLock();
        
        // 策略3：ThreadLocal
        testThreadLocal();
    }
    
    /**
     * 锁分离策略测试
     */
    private static void testLockSeparation() throws InterruptedException {
        // 单锁版本
        class SingleLockCounter {
            private int readCount = 0;
            private int writeCount = 0;
            
            public synchronized void read() {
                readCount++;
            }
            
            public synchronized void write() {
                writeCount++;
            }
            
            public synchronized int getTotal() {
                return readCount + writeCount;
            }
        }
        
        // 分离锁版本
        class SeparatedLockCounter {
            private int readCount = 0;
            private int writeCount = 0;
            private final Object readLock = new Object();
            private final Object writeLock = new Object();
            
            public void read() {
                synchronized (readLock) {
                    readCount++;
                }
            }
            
            public void write() {
                synchronized (writeLock) {
                    writeCount++;
                }
            }
            
            public int getTotal() {
                synchronized (readLock) {
                    synchronized (writeLock) {
                        return readCount + writeCount;
                    }
                }
            }
        }
        
        SingleLockCounter singleCounter = new SingleLockCounter();
        long singleTime = measureMixedOperations("单锁", singleCounter::read, singleCounter::write);
        
        SeparatedLockCounter separatedCounter = new SeparatedLockCounter();
        long separatedTime = measureMixedOperations("分离锁", separatedCounter::read, separatedCounter::write);
        
        System.out.println("单锁: " + singleTime + "ms");
        System.out.println("分离锁: " + separatedTime + "ms");
        System.out.println("分离锁优化: " + String.format("%.2fx", (double)singleTime / separatedTime));
    }
    
    /**
     * 读写锁测试
     */
    private static void testReadWriteLock() {
        System.out.println("读写锁策略: 适合读多写少场景");
        // 实际实现会在后续的ReadWriteLockCache任务中详细展示
    }
    
    /**
     * ThreadLocal测试
     */
    private static void testThreadLocal() throws InterruptedException {
        ThreadLocal<Integer> threadLocalCounter = new ThreadLocal<Integer>() {
            @Override
            protected Integer initialValue() {
                return 0;
            }
        };
        
        long threadLocalTime = measureTime("ThreadLocal", () -> {
            Thread[] threads = new Thread[THREAD_COUNT];
            for (int i = 0; i < THREAD_COUNT; i++) {
                threads[i] = new Thread(() -> {
                    for (int j = 0; j < ITERATIONS; j++) {
                        threadLocalCounter.set(threadLocalCounter.get() + 1);
                    }
                });
                threads[i].start();
            }
            
            for (Thread thread : threads) {
                try {
                    thread.join();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });
        
        System.out.println("ThreadLocal: " + threadLocalTime + "ms (无锁竞争)");
    }
    
    /**
     * 测量混合操作的性能
     */
    private static long measureMixedOperations(String name, Runnable readOp, Runnable writeOp) throws InterruptedException {
        return measureTime(name, () -> {
            Thread[] threads = new Thread[THREAD_COUNT];
            for (int i = 0; i < THREAD_COUNT; i++) {
                final int threadId = i;
                threads[i] = new Thread(() -> {
                    for (int j = 0; j < ITERATIONS / 100; j++) {
                        if (threadId % 4 == 0) {
                            writeOp.run(); // 25%写操作
                        } else {
                            readOp.run();  // 75%读操作
                        }
                    }
                });
                threads[i].start();
            }
            
            for (Thread thread : threads) {
                try {
                    thread.join();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });
    }
    
    /**
     * 通用性能测量工具
     */
    private static long measureTime(String operation, Runnable task) {
        System.gc(); // 建议进行垃圾回收
        
        long startTime = System.currentTimeMillis();
        task.run();
        long endTime = System.currentTimeMillis();
        
        long duration = endTime - startTime;
        System.out.println(operation + "完成，耗时: " + duration + "ms");
        
        return duration;
    }
}