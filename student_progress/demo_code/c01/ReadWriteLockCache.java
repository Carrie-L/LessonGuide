/**
 * Task 1.1.11: 读写锁优化方案 - 缓存系统实现
 * 
 * 使用ReadWriteLock实现高性能缓存系统
 * 
 * 学习目标：
 * 1. 掌握读写锁的适用场景和优势
 * 2. 理解读写锁的性能特点
 * 3. 实现生产级的线程安全缓存
 * 4. 学习锁升级和锁降级的概念
 */
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class ReadWriteLockCache<K, V> {
    
    private final Map<K, V> cache = new HashMap<>();
    private final ReadWriteLock rwLock = new ReentrantReadWriteLock();
    private final java.util.concurrent.locks.Lock readLock = rwLock.readLock();
    private final java.util.concurrent.locks.Lock writeLock = rwLock.writeLock();
    
    // 性能统计
    private volatile long readCount = 0;
    private volatile long writeCount = 0;
    private volatile long readTime = 0;
    private volatile long writeTime = 0;
    
    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== 读写锁缓存系统实现与性能分析 ===");
        
        // 基本功能测试
        demonstrateBasicOperations();
        
        // 性能对比测试
        performanceComparison();
        
        // 并发读写测试
        concurrentReadWriteTest();
        
        // 锁升级降级演示
        demonstrateLockUpgradeDowngrade();
        
        // 实际场景模拟
        simulateRealWorldScenario();
    }
    
    /**
     * 基本功能演示
     */
    private static void demonstrateBasicOperations() {
        System.out.println("\n--- 基本功能测试 ---");
        
        ReadWriteLockCache<String, String> cache = new ReadWriteLockCache<>();
        
        // 写入操作
        System.out.println("🔄 写入操作:");
        cache.put("key1", "value1");
        cache.put("key2", "value2");
        cache.put("key3", "value3");
        System.out.println("写入完成，缓存大小: " + cache.size());
        
        // 读取操作
        System.out.println("\n📖 读取操作:");
        System.out.println("key1: " + cache.get("key1"));
        System.out.println("key2: " + cache.get("key2"));
        System.out.println("不存在的key: " + cache.get("nonexistent"));
        
        // 条件操作
        System.out.println("\n🎯 条件操作:");
        System.out.println("包含key1: " + cache.containsKey("key1"));
        System.out.println("移除key2: " + cache.remove("key2"));
        System.out.println("移除后大小: " + cache.size());
    }
    
    /**
     * 线程安全的put操作
     */
    public void put(K key, V value) {
        long startTime = System.nanoTime();
        writeLock.lock();
        try {
            cache.put(key, value);
            writeCount++;
        } finally {
            writeLock.unlock();
            writeTime += (System.nanoTime() - startTime);
        }
    }
    
    /**
     * 线程安全的get操作
     */
    public V get(K key) {
        long startTime = System.nanoTime();
        readLock.lock();
        try {
            readCount++;
            return cache.get(key);
        } finally {
            readLock.unlock();
            readTime += (System.nanoTime() - startTime);
        }
    }
    
    /**
     * 线程安全的remove操作
     */
    public V remove(K key) {
        writeLock.lock();
        try {
            writeCount++;
            return cache.remove(key);
        } finally {
            writeLock.unlock();
        }
    }
    
    /**
     * 线程安全的containsKey操作
     */
    public boolean containsKey(K key) {
        readLock.lock();
        try {
            readCount++;
            return cache.containsKey(key);
        } finally {
            readLock.unlock();
        }
    }
    
    /**
     * 获取缓存大小
     */
    public int size() {
        readLock.lock();
        try {
            return cache.size();
        } finally {
            readLock.unlock();
        }
    }
    
    /**
     * 清空缓存
     */
    public void clear() {
        writeLock.lock();
        try {
            cache.clear();
            writeCount++;
        } finally {
            writeLock.unlock();
        }
    }
    
    /**
     * 性能对比测试
     */
    private static void performanceComparison() throws InterruptedException {
        System.out.println("\n--- 性能对比测试 ---");
        
        final int THREAD_COUNT = 8;
        final int OPERATIONS_PER_THREAD = 50000;
        
        // 测试读写锁缓存
        System.out.println("🔄 测试读写锁缓存...");
        ReadWriteLockCache<Integer, String> rwCache = new ReadWriteLockCache<>();
        long rwTime = testCachePerformance("读写锁缓存", rwCache, THREAD_COUNT, OPERATIONS_PER_THREAD);
        
        // 测试同步缓存
        System.out.println("🔄 测试synchronized缓存...");
        SynchronizedCache<Integer, String> syncCache = new SynchronizedCache<>();
        long syncTime = testCachePerformance("同步缓存", syncCache, THREAD_COUNT, OPERATIONS_PER_THREAD);
        
        // 测试ConcurrentHashMap
        System.out.println("🔄 测试ConcurrentHashMap...");
        ConcurrentHashMapCache<Integer, String> concurrentCache = new ConcurrentHashMapCache<>();
        long concurrentTime = testCachePerformance("ConcurrentHashMap", concurrentCache, THREAD_COUNT, OPERATIONS_PER_THREAD);
        
        // 性能对比
        System.out.println("\n=== 性能对比结果 ===");
        System.out.println("读写锁缓存: " + rwTime + "ms");
        System.out.println("同步缓存: " + syncTime + "ms");
        System.out.println("ConcurrentHashMap: " + concurrentTime + "ms");
        
        System.out.println("\n=== 性能提升 ===");
        System.out.println("读写锁 vs 同步: " + String.format("%.2fx", (double)syncTime / rwTime));
        System.out.println("读写锁 vs ConcurrentHashMap: " + String.format("%.2fx", (double)concurrentTime / rwTime));
        
        // 读写锁统计
        System.out.println("\n=== 读写锁统计 ===");
        System.out.println("读操作次数: " + rwCache.readCount);
        System.out.println("写操作次数: " + rwCache.writeCount);
        System.out.println("平均读时间: " + String.format("%.2f", (double)rwCache.readTime / rwCache.readCount / 1000) + "μs");
        System.out.println("平均写时间: " + String.format("%.2f", (double)rwCache.writeTime / rwCache.writeCount / 1000) + "μs");
    }
    
    /**
     * 缓存性能测试通用方法
     */
    private static long testCachePerformance(String cacheName, CacheInterface<Integer, String> cache, 
                                           int threadCount, int operationsPerThread) throws InterruptedException {
        
        // 预填充缓存
        for (int i = 0; i < 1000; i++) {
            cache.put(i, "value" + i);
        }
        
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch endLatch = new CountDownLatch(threadCount);
        
        long startTime = System.currentTimeMillis();
        
        for (int i = 0; i < threadCount; i++) {
            final int threadId = i;
            new Thread(() -> {
                try {
                    startLatch.await();
                    Random random = new Random(threadId);
                    
                    for (int j = 0; j < operationsPerThread; j++) {
                        int key = random.nextInt(1000);
                        
                        if (random.nextDouble() < 0.8) { // 80%读操作
                            cache.get(key);
                        } else { // 20%写操作
                            cache.put(key, "newValue" + key);
                        }
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    endLatch.countDown();
                }
            }, cacheName + "-Thread" + i).start();
        }
        
        startLatch.countDown(); // 开始测试
        endLatch.await(); // 等待结束
        
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        
        System.out.println(cacheName + " 完成，耗时: " + duration + "ms，缓存大小: " + cache.size());
        
        return duration;
    }
    
    /**
     * 并发读写测试
     */
    private static void concurrentReadWriteTest() throws InterruptedException {
        System.out.println("\n--- 并发读写压力测试 ---");
        
        ReadWriteLockCache<String, String> cache = new ReadWriteLockCache<>();
        final int READERS = 6;
        final int WRITERS = 2;
        final CountDownLatch latch = new CountDownLatch(READERS + WRITERS);
        
        // 启动读者线程
        for (int i = 0; i < READERS; i++) {
            final int readerId = i;
            new Thread(() -> {
                try {
                    for (int j = 0; j < 1000; j++) {
                        String key = "key" + (j % 100);
                        String value = cache.get(key);
                        
                        if (j % 200 == 0) {
                            System.out.println("读者" + readerId + ": 读取 " + key + " = " + value);
                        }
                        
                        Thread.sleep(1); // 模拟处理时间
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    latch.countDown();
                }
            }, "Reader-" + i).start();
        }
        
        // 启动写者线程
        for (int i = 0; i < WRITERS; i++) {
            final int writerId = i;
            new Thread(() -> {
                try {
                    for (int j = 0; j < 200; j++) {
                        String key = "key" + (j % 100);
                        String value = "value" + writerId + "-" + j;
                        cache.put(key, value);
                        
                        if (j % 50 == 0) {
                            System.out.println("写者" + writerId + ": 写入 " + key + " = " + value);
                        }
                        
                        Thread.sleep(5); // 写操作稍慢
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    latch.countDown();
                }
            }, "Writer-" + i).start();
        }
        
        latch.await();
        System.out.println("并发测试完成，最终缓存大小: " + cache.size());
    }
    
    /**
     * 锁升级降级演示
     */
    private static void demonstrateLockUpgradeDowngrade() {
        System.out.println("\n--- 锁升级降级演示 ---");
        
        ReadWriteLockCache<String, String> cache = new ReadWriteLockCache<>();
        
        // 演示锁降级（写锁->读锁）
        System.out.println("🔽 锁降级演示:");
        cache.demonstrateLockDowngrade("testKey", "testValue");
        
        // 注意：读锁无法直接升级为写锁，需要释放读锁后重新获取写锁
        System.out.println("\n⚠️ 读锁无法直接升级为写锁");
    }
    
    /**
     * 演示锁降级
     */
    public void demonstrateLockDowngrade(K key, V value) {
        writeLock.lock();
        try {
            System.out.println("1. 获取写锁");
            
            // 执行写操作
            cache.put(key, value);
            System.out.println("2. 执行写操作: " + key + " = " + value);
            
            // 获取读锁（锁降级）
            readLock.lock();
            System.out.println("3. 获取读锁（锁降级）");
            
        } finally {
            writeLock.unlock();
            System.out.println("4. 释放写锁");
        }
        
        try {
            // 现在只持有读锁
            V result = cache.get(key);
            System.out.println("5. 使用读锁读取: " + key + " = " + result);
        } finally {
            readLock.unlock();
            System.out.println("6. 释放读锁");
        }
    }
    
    /**
     * 模拟真实世界场景 - 配置缓存系统
     */
    private static void simulateRealWorldScenario() throws InterruptedException {
        System.out.println("\n--- 真实场景模拟：配置缓存系统 ---");
        
        ConfigCache configCache = new ConfigCache();
        
        // 模拟配置初始化
        configCache.initializeConfig();
        
        // 模拟大量并发读取配置
        final int READER_THREADS = 10;
        CountDownLatch readerLatch = new CountDownLatch(READER_THREADS);
        
        for (int i = 0; i < READER_THREADS; i++) {
            final int threadId = i;
            new Thread(() -> {
                try {
                    for (int j = 0; j < 100; j++) {
                        String config = configCache.getConfig("app.timeout");
                        if (j == 0) {
                            System.out.println("线程" + threadId + " 读取配置: " + config);
                        }
                        Thread.sleep(10);
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    readerLatch.countDown();
                }
            }, "ConfigReader-" + i).start();
        }
        
        // 模拟偶尔的配置更新
        Thread configUpdater = new Thread(() -> {
            try {
                Thread.sleep(500);
                configCache.updateConfig("app.timeout", "5000");
                System.out.println("⚡ 配置已更新");
                
                Thread.sleep(1000);
                configCache.updateConfig("app.maxConnections", "200");
                System.out.println("⚡ 配置已更新");
                
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }, "ConfigUpdater");
        
        configUpdater.start();
        readerLatch.await();
        configUpdater.join();
        
        System.out.println("配置缓存系统测试完成");
        configCache.printStatistics();
    }
    
    /**
     * 配置缓存类 - 真实场景应用
     */
    static class ConfigCache {
        private final ReadWriteLockCache<String, String> cache = new ReadWriteLockCache<>();
        
        public void initializeConfig() {
            System.out.println("📝 初始化配置...");
            cache.put("app.timeout", "3000");
            cache.put("app.maxConnections", "100");
            cache.put("app.retryCount", "3");
            cache.put("db.url", "jdbc:mysql://localhost:3306/app");
            cache.put("cache.ttl", "300");
        }
        
        public String getConfig(String key) {
            return cache.get(key);
        }
        
        public void updateConfig(String key, String value) {
            cache.put(key, value);
        }
        
        public void printStatistics() {
            System.out.println("\n📊 缓存统计:");
            System.out.println("读操作: " + cache.readCount);
            System.out.println("写操作: " + cache.writeCount);
            System.out.println("缓存大小: " + cache.size());
        }
    }
    
    /**
     * 缓存接口 - 便于测试不同实现
     */
    interface CacheInterface<K, V> {
        void put(K key, V value);
        V get(K key);
        int size();
    }
    
    /**
     * 同步缓存实现 - 用于性能对比
     */
    static class SynchronizedCache<K, V> implements CacheInterface<K, V> {
        private final Map<K, V> cache = new HashMap<>();
        
        @Override
        public synchronized void put(K key, V value) {
            cache.put(key, value);
        }
        
        @Override
        public synchronized V get(K key) {
            return cache.get(key);
        }
        
        @Override
        public synchronized int size() {
            return cache.size();
        }
    }
    
    /**
     * ConcurrentHashMap缓存实现 - 用于性能对比
     */
    static class ConcurrentHashMapCache<K, V> implements CacheInterface<K, V> {
        private final ConcurrentHashMap<K, V> cache = new ConcurrentHashMap<>();
        
        @Override
        public void put(K key, V value) {
            cache.put(key, value);
        }
        
        @Override
        public V get(K key) {
            return cache.get(key);
        }
        
        @Override
        public int size() {
            return cache.size();
        }
    }
}