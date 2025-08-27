/**
 * Task 1.1.8: Synchronized Performance Test Analysis
 * 
 * Requirements:
 * 1. Quantify synchronized performance overhead
 * 2. Compare different synchronization granularities
 * 3. Analyze lock contention impact on performance
 * 4. Master performance optimization strategies
 * 
 * ⚠️ Focus on understanding different optimization strategies
 */
public class SynchronizedPerformanceTest {
    
    // 测试变量声明
    private static final int THREAD_COUNT = Runtime.getRuntime().availableProcessors();
    private static final int ITERATIONS = 1000000;
    private static volatile int volatileCounter = 0;
    private static int unsafeCounter = 0;
    private static int synchronizedCounter = 0;
    
    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== Synchronized Performance Test Analysis ===");
        System.out.println("Test Environment: " + THREAD_COUNT + " threads, each performing " + ITERATIONS + " operations");
        
        // 基准性能测试
        runPerformanceBenchmarks();
        
        // 锁粒度影响测试
        testLockGranularity();
        
        // 锁竞争程度测试
        testLockContention();
        
        // 优化策略测试
        testOptimizationStrategies();
        
        // 总结最佳实践
        summarizeBestPractices();
    }
    
    /**
     * Benchmark Performance Test
     * Compare performance of no synchronization, volatile, and synchronized
     */
    private static void runPerformanceBenchmarks() throws InterruptedException {
        System.out.println("\n--- Benchmark Performance Test ---");
        
        // 1. No synchronization benchmark
        unsafeCounter = 0;
        long noSyncTime = measureTime("No Sync", () -> {
            Thread[] threads = new Thread[THREAD_COUNT];
            for (int i = 0; i < THREAD_COUNT; i++) {
                threads[i] = new Thread(() -> {
                    for (int j = 0; j < ITERATIONS; j++) {
                        unsafeCounter++; // unsafe operation
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
        
        // 2. Volatile test
        volatileCounter = 0;
        long volatileTime = measureTime("Volatile", () -> {
            Thread[] threads = new Thread[THREAD_COUNT];
            for (int i = 0; i < THREAD_COUNT; i++) {
                threads[i] = new Thread(() -> {
                    for (int j = 0; j < ITERATIONS; j++) {
                        volatileCounter++; // volatile but not atomic
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
        
        // 3. Synchronized test
        synchronizedCounter = 0;
        final Object lock = new Object();
        long syncTime = measureTime("Synchronized", () -> {
            Thread[] threads = new Thread[THREAD_COUNT];
            for (int i = 0; i < THREAD_COUNT; i++) {
                threads[i] = new Thread(() -> {
                    for (int j = 0; j < ITERATIONS; j++) {
                        synchronized (lock) {
                            synchronizedCounter++; // safe operation
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
        
        // Result analysis
        System.out.println("\n=== Performance Results ===");
        System.out.println("No Sync: " + noSyncTime + "ms (Result: " + unsafeCounter + ")");
        System.out.println("Volatile: " + volatileTime + "ms (Result: " + volatileCounter + ")");
        System.out.println("Synchronized: " + syncTime + "ms (Result: " + synchronizedCounter + ")");
        
        System.out.println("\n=== Performance Comparison ===");
        System.out.println("Volatile vs No Sync: " + String.format("%.2fx", (double)volatileTime / noSyncTime));
        System.out.println("Synchronized vs No Sync: " + String.format("%.2fx", (double)syncTime / noSyncTime));
        System.out.println("Synchronized vs Volatile: " + String.format("%.2fx", (double)syncTime / volatileTime));
    }
    
    /**
     * Lock Granularity Test
     * Compare performance of coarse-grained and fine-grained locks
     */
    private static void testLockGranularity() throws InterruptedException {
        System.out.println("\n--- Lock Granularity Test ---");
        
        // Coarse-grained lock: entire method synchronized
        class CoarseGrainedCounter {
            private int count = 0;
            
            public synchronized void increment() {
                count++;
                // Simulate some extra work
                Math.sqrt(count);
            }
            
            public synchronized int getCount() {
                return count;
            }
        }
        
        // Fine-grained lock: only synchronize necessary parts
        class FineGrainedCounter {
            private int count = 0;
            private final Object lock = new Object();
            
            public void increment() {
                // Non-synchronized part
                double temp = Math.sqrt(System.nanoTime());
                
                synchronized (lock) {
                    count++; // Only synchronize necessary part
                }
            }
            
            public int getCount() {
                synchronized (lock) {
                    return count;
                }
            }
        }
        
        // Test coarse-grained lock
        CoarseGrainedCounter coarseCounter = new CoarseGrainedCounter();
        long coarseTime = measureTime("Coarse-grained lock", () -> {
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
        
        // Test fine-grained lock
        FineGrainedCounter fineCounter = new FineGrainedCounter();
        long fineTime = measureTime("Fine-grained lock", () -> {
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
        
        System.out.println("Coarse-grained lock: " + coarseTime + "ms (Result: " + coarseCounter.getCount() + ")");
        System.out.println("Fine-grained lock: " + fineTime + "ms (Result: " + fineCounter.getCount() + ")");
        System.out.println("Fine-grained performance improvement: " + String.format("%.2fx", (double)coarseTime / fineTime));
    }
    
    /**
     * Lock Contention Test
     * Test the impact of different contention levels on performance
     */
    private static void testLockContention() throws InterruptedException {
        System.out.println("\n--- Lock Contention Test ---");
        
        final Object lock = new Object();
        
        // Low contention: short lock holding time
        long lowContentionTime = testContention("Low contention", lock, 1);
        
        // Medium contention: medium lock holding time
        long mediumContentionTime = testContention("Medium contention", lock, 10);
        
        // High contention: long lock holding time
        long highContentionTime = testContention("High contention", lock, 100);
        
        System.out.println("\n=== Contention Level Impact ===");
        System.out.println("Low contention: " + lowContentionTime + "ms");
        System.out.println("Medium contention: " + mediumContentionTime + "ms");
        System.out.println("High contention: " + highContentionTime + "ms");
        System.out.println("High/Low contention ratio: " + String.format("%.2fx", (double)highContentionTime / lowContentionTime));
    }
    
    /**
     * Test performance with specific contention level
     */
    private static long testContention(String name, Object lock, int workload) throws InterruptedException {
        return measureTime(name, () -> {
            Thread[] threads = new Thread[THREAD_COUNT];
            for (int i = 0; i < THREAD_COUNT; i++) {
                threads[i] = new Thread(() -> {
                    for (int j = 0; j < ITERATIONS / 100; j++) {
                        synchronized (lock) {
                            // Simulate different workload intensities
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
     * Lock Optimization Strategy Test
     * Demonstrate effects of different optimization techniques
     */
    private static void testOptimizationStrategies() throws InterruptedException {
        System.out.println("\n--- Lock Optimization Strategy Test ---");
        
        // Strategy 1: Lock separation
        testLockSeparation();
        
        // Strategy 2: ThreadLocal
        testThreadLocal();
    }
    
    /**
     * Lock Separation Strategy Test
     */
    private static void testLockSeparation() throws InterruptedException {
        // Single lock version
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
        
        // Separated lock version
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
        long singleTime = measureMixedOperations("Single lock", singleCounter::read, singleCounter::write);
        
        SeparatedLockCounter separatedCounter = new SeparatedLockCounter();
        long separatedTime = measureMixedOperations("Separated locks", separatedCounter::read, separatedCounter::write);
        
        System.out.println("Single lock: " + singleTime + "ms");
        System.out.println("Separated locks: " + separatedTime + "ms");
        System.out.println("Separated lock improvement: " + String.format("%.2fx", (double)singleTime / separatedTime));
    }
    
    /**
     * ThreadLocal Test
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
        
        System.out.println("ThreadLocal: " + threadLocalTime + "ms (no lock contention)");
    }
    
    /**
     * Measure performance of mixed operations
     */
    private static long measureMixedOperations(String name, Runnable readOp, Runnable writeOp) throws InterruptedException {
        return measureTime(name, () -> {
            Thread[] threads = new Thread[THREAD_COUNT];
            for (int i = 0; i < THREAD_COUNT; i++) {
                final int threadId = i;
                threads[i] = new Thread(() -> {
                    for (int j = 0; j < ITERATIONS / 100; j++) {
                        if (threadId % 4 == 0) {
                            writeOp.run(); // 25% write operations
                        } else {
                            readOp.run();  // 75% read operations
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
     * Summary of synchronized performance optimization best practices
     */
    private static void summarizeBestPractices() {
        System.out.println("\n=== Synchronized Performance Optimization Best Practices ===");
        System.out.println("1. Reduce lock holding time:");
        System.out.println("   - Only use synchronized on necessary code blocks, avoid locking entire methods");
        System.out.println("   - Move non-synchronized operations out of synchronized blocks");
        
        System.out.println("\n2. Reduce lock granularity:");
        System.out.println("   - Use fine-grained locks instead of coarse-grained locks");
        System.out.println("   - Break large objects into smaller ones, each with its own lock");
        
        System.out.println("\n3. Lock separation strategy:");
        System.out.println("   - Use different lock objects based on operation type (read/write)");
        System.out.println("   - Especially effective in read-heavy scenarios");
        
        System.out.println("\n4. Avoid lock contention:");
        System.out.println("   - Performance decreases significantly in high contention scenarios");
        System.out.println("   - Consider using ThreadLocal or other lock-free alternatives");
        
        System.out.println("\n5. Leverage JVM optimizations:");
        System.out.println("   - JVM optimizes synchronized in low contention (biased locks, lightweight locks)");
        System.out.println("   - Avoid artificially creating contention");
        
        System.out.println("\n6. Choose appropriate synchronization approach:");
        System.out.println("   - Simple scenarios: volatile variables (suitable for single-write-multiple-read)");
        System.out.println("   - Complex scenarios: consider concurrent utilities in java.util.concurrent");
        System.out.println("   - Read-heavy scenarios: consider ReadWriteLock");
        
        System.out.println("\nConclusion: synchronized performance is closely related to lock contention level,");
        System.out.println("lock granularity, and synchronization strategy. Proper use can achieve good performance");
        System.out.println("while ensuring thread safety.");
    }
    
    /**
     * General performance measurement utility
     */
    private static long measureTime(String operation, Runnable task) {
        System.gc(); // Suggest garbage collection
        
        long startTime = System.currentTimeMillis();
        task.run();
        long endTime = System.currentTimeMillis();
        
        long duration = endTime - startTime;
        System.out.println(operation + " completed in: " + duration + "ms");
        
        return duration;
    }
}