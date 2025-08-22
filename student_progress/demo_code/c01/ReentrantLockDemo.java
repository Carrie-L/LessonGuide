/**
 * Task 1.1.10: ReentrantLock与AQS框架深度解析
 * 
 * 深入理解ReentrantLock的实现原理和AQS框架
 * 
 * 学习目标：
 * 1. 掌握ReentrantLock的高级特性
 * 2. 理解AQS(AbstractQueuedSynchronizer)框架原理
 * 3. 对比ReentrantLock与synchronized的差异
 * 4. 学习公平锁与非公平锁的实现
 */
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.CountDownLatch;

public class ReentrantLockDemo {
    
    private static final ReentrantLock fairLock = new ReentrantLock(true);     // 公平锁
    private static final ReentrantLock unfairLock = new ReentrantLock(false);  // 非公平锁
    
    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== ReentrantLock与AQS框架解析 ===");
        
        // 基本功能演示
        demonstrateBasicFeatures();
        
        // 高级特性演示
        demonstrateAdvancedFeatures();
        
        // 公平锁vs非公平锁
        compareFairVsUnfairLock();
        
        // Condition条件变量
        demonstrateCondition();
        
        // 性能对比
        performanceComparison();
        
        // 手写简单AQS
        demonstrateCustomAQS();
    }
    
    /**
     * 基本功能演示
     */
    private static void demonstrateBasicFeatures() throws InterruptedException {
        System.out.println("\n--- ReentrantLock基本功能 ---");
        
        ReentrantLock lock = new ReentrantLock();
        
        // 功能1: 可重入性
        demonstrateReentrant(lock);
        
        // 功能2: 可中断锁获取
        demonstrateInterruptible(lock);
        
        // 功能3: 尝试获取锁
        demonstrateTryLock(lock);
    }
    
    /**
     * 演示可重入性
     */
    private static void demonstrateReentrant(ReentrantLock lock) {
        System.out.println("\n🔄 可重入性演示:");
        
        class ReentrantTask {
            void method1() {
                lock.lock();
                try {
                    System.out.println("Method1: 获得锁，持有计数=" + lock.getHoldCount());
                    method2(); // 重入同一个锁
                } finally {
                    System.out.println("Method1: 释放锁前，持有计数=" + lock.getHoldCount());
                    lock.unlock();
                }
            }
            
            void method2() {
                lock.lock();
                try {
                    System.out.println("Method2: 重入锁成功，持有计数=" + lock.getHoldCount());
                    method3();
                } finally {
                    System.out.println("Method2: 释放锁前，持有计数=" + lock.getHoldCount());
                    lock.unlock();
                }
            }
            
            void method3() {
                lock.lock();
                try {
                    System.out.println("Method3: 再次重入，持有计数=" + lock.getHoldCount());
                } finally {
                    System.out.println("Method3: 释放锁前，持有计数=" + lock.getHoldCount());
                    lock.unlock();
                }
            }
        }
        
        new ReentrantTask().method1();
        System.out.println("最终持有计数: " + lock.getHoldCount());
    }
    
    /**
     * 演示可中断锁获取
     */
    private static void demonstrateInterruptible(ReentrantLock lock) throws InterruptedException {
        System.out.println("\n⚡ 可中断锁获取演示:");
        
        // 主线程持有锁
        lock.lock();
        System.out.println("主线程: 持有锁...");
        
        Thread interruptibleThread = new Thread(() -> {
            try {
                System.out.println("可中断线程: 尝试获取锁...");
                lock.lockInterruptibly(); // 可中断的锁获取
                
                try {
                    System.out.println("可中断线程: 获得锁");
                } finally {
                    lock.unlock();
                }
                
            } catch (InterruptedException e) {
                System.out.println("可中断线程: 被中断，停止等待锁");
            }
        }, "InterruptibleThread");
        
        interruptibleThread.start();
        Thread.sleep(1000);
        
        // 中断等待锁的线程
        System.out.println("主线程: 中断等待锁的线程");
        interruptibleThread.interrupt();
        
        interruptibleThread.join();
        lock.unlock();
        System.out.println("主线程: 释放锁");
    }
    
    /**
     * 演示尝试获取锁
     */
    private static void demonstrateTryLock(ReentrantLock lock) throws InterruptedException {
        System.out.println("\n🎯 尝试获取锁演示:");
        
        // 无参tryLock
        boolean acquired = lock.tryLock();
        System.out.println("无参tryLock: " + (acquired ? "成功" : "失败"));
        if (acquired) {
            lock.unlock();
        }
        
        // 带超时的tryLock
        lock.lock(); // 先持有锁
        
        Thread tryLockThread = new Thread(() -> {
            try {
                System.out.println("TryLock线程: 尝试在2秒内获取锁...");
                boolean success = lock.tryLock(2, TimeUnit.SECONDS);
                
                if (success) {
                    try {
                        System.out.println("TryLock线程: 在超时前获得锁");
                    } finally {
                        lock.unlock();
                    }
                } else {
                    System.out.println("TryLock线程: 超时，未能获得锁");
                }
                
            } catch (InterruptedException e) {
                System.out.println("TryLock线程: 被中断");
            }
        }, "TryLockThread");
        
        tryLockThread.start();
        Thread.sleep(3000); // 3秒后释放锁
        
        System.out.println("主线程: 3秒后释放锁");
        lock.unlock();
        
        tryLockThread.join();
    }
    
    /**
     * 高级特性演示
     */
    private static void demonstrateAdvancedFeatures() {
        System.out.println("\n--- ReentrantLock高级特性 ---");
        
        ReentrantLock lock = new ReentrantLock();
        
        System.out.println("🔍 锁状态查询:");
        System.out.println("是否被锁定: " + lock.isLocked());
        System.out.println("当前线程是否持有锁: " + lock.isHeldByCurrentThread());
        System.out.println("等待队列长度: " + lock.getQueueLength());
        
        lock.lock();
        try {
            System.out.println("\n获取锁后:");
            System.out.println("是否被锁定: " + lock.isLocked());
            System.out.println("当前线程是否持有锁: " + lock.isHeldByCurrentThread());
            System.out.println("持有计数: " + lock.getHoldCount());
        } finally {
            lock.unlock();
        }
    }
    
    /**
     * 公平锁vs非公平锁对比
     */
    private static void compareFairVsUnfairLock() throws InterruptedException {
        System.out.println("\n--- 公平锁 vs 非公平锁 ---");
        
        // 测试非公平锁
        System.out.println("\n📊 非公平锁测试:");
        testLockFairness("非公平锁", unfairLock, 5);
        
        Thread.sleep(1000);
        
        // 测试公平锁
        System.out.println("\n📊 公平锁测试:");
        testLockFairness("公平锁", fairLock, 5);
    }
    
    /**
     * 测试锁的公平性
     */
    private static void testLockFairness(String lockType, ReentrantLock lock, int threadCount) 
            throws InterruptedException {
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch endLatch = new CountDownLatch(threadCount);
        
        for (int i = 0; i < threadCount; i++) {
            final int threadId = i;
            
            new Thread(() -> {
                try {
                    startLatch.await(); // 等待同时开始
                    
                    for (int j = 0; j < 3; j++) {
                        lock.lock();
                        try {
                            System.out.println(lockType + " - 线程" + threadId + " 获得锁 (第" + (j+1) + "次)");
                            Thread.sleep(100); // 模拟工作
                        } finally {
                            lock.unlock();
                        }
                        
                        Thread.sleep(10); // 短暂休息
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    endLatch.countDown();
                }
            }, lockType + "-Thread" + i).start();
        }
        
        startLatch.countDown(); // 开始测试
        endLatch.await(); // 等待所有线程完成
        
        System.out.println(lockType + "测试完成\n");
    }
    
    /**
     * Condition条件变量演示
     */
    private static void demonstrateCondition() throws InterruptedException {
        System.out.println("\n--- Condition条件变量演示 ---");
        
        // 生产者消费者模式
        BoundedBuffer<String> buffer = new BoundedBuffer<>(3);
        
        // 生产者线程
        Thread producer = new Thread(() -> {
            try {
                for (int i = 1; i <= 5; i++) {
                    String item = "Item" + i;
                    buffer.put(item);
                    System.out.println("生产: " + item);
                    Thread.sleep(500);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }, "Producer");
        
        // 消费者线程
        Thread consumer = new Thread(() -> {
            try {
                for (int i = 1; i <= 5; i++) {
                    String item = buffer.take();
                    System.out.println("消费: " + item);
                    Thread.sleep(1000);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }, "Consumer");
        
        producer.start();
        consumer.start();
        
        producer.join();
        consumer.join();
    }
    
    /**
     * 有界缓冲区 - 使用Condition实现
     */
    static class BoundedBuffer<T> {
        private final T[] buffer;
        private int count, putIndex, takeIndex;
        
        private final ReentrantLock lock = new ReentrantLock();
        private final Condition notEmpty = lock.newCondition();
        private final Condition notFull = lock.newCondition();
        
        @SuppressWarnings("unchecked")
        public BoundedBuffer(int capacity) {
            buffer = (T[]) new Object[capacity];
        }
        
        public void put(T item) throws InterruptedException {
            lock.lock();
            try {
                while (count == buffer.length) {
                    System.out.println("缓冲区满，生产者等待...");
                    notFull.await(); // 等待不满条件
                }
                
                buffer[putIndex] = item;
                putIndex = (putIndex + 1) % buffer.length;
                count++;
                
                System.out.println("缓冲区状态: " + count + "/" + buffer.length);
                notEmpty.signal(); // 通知消费者
                
            } finally {
                lock.unlock();
            }
        }
        
        public T take() throws InterruptedException {
            lock.lock();
            try {
                while (count == 0) {
                    System.out.println("缓冲区空，消费者等待...");
                    notEmpty.await(); // 等待不空条件
                }
                
                T item = buffer[takeIndex];
                buffer[takeIndex] = null;
                takeIndex = (takeIndex + 1) % buffer.length;
                count--;
                
                System.out.println("缓冲区状态: " + count + "/" + buffer.length);
                notFull.signal(); // 通知生产者
                
                return item;
            } finally {
                lock.unlock();
            }
        }
    }
    
    /**
     * 性能对比：ReentrantLock vs synchronized
     */
    private static void performanceComparison() throws InterruptedException {
        System.out.println("\n--- 性能对比测试 ---");
        
        final int THREAD_COUNT = 4;
        final int ITERATIONS = 500000;
        
        // 测试ReentrantLock
        long reentrantTime = testLockPerformance("ReentrantLock", THREAD_COUNT, ITERATIONS, true);
        
        // 测试synchronized
        long synchronizedTime = testLockPerformance("Synchronized", THREAD_COUNT, ITERATIONS, false);
        
        System.out.println("\n=== 性能对比结果 ===");
        System.out.println("ReentrantLock: " + reentrantTime + "ms");
        System.out.println("Synchronized: " + synchronizedTime + "ms");
        System.out.println("性能比值: " + String.format("%.2f", (double)reentrantTime / synchronizedTime));
    }
    
    /**
     * 锁性能测试
     */
    private static long testLockPerformance(String lockType, int threadCount, 
                                          int iterations, boolean useReentrantLock) 
                                          throws InterruptedException {
        
        final Counter counter = useReentrantLock ? new ReentrantLockCounter() : new SynchronizedCounter();
        
        long startTime = System.currentTimeMillis();
        
        Thread[] threads = new Thread[threadCount];
        for (int i = 0; i < threadCount; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < iterations; j++) {
                    counter.increment();
                }
            }, lockType + "-Thread" + i);
            threads[i].start();
        }
        
        for (Thread thread : threads) {
            thread.join();
        }
        
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        
        System.out.println(lockType + " 结果: " + counter.getValue() + ", 耗时: " + duration + "ms");
        
        return duration;
    }
    
    /**
     * 计数器接口
     */
    interface Counter {
        void increment();
        int getValue();
    }
    
    /**
     * ReentrantLock版本的计数器
     */
    static class ReentrantLockCounter implements Counter {
        private int count = 0;
        private final ReentrantLock lock = new ReentrantLock();
        
        @Override
        public void increment() {
            lock.lock();
            try {
                count++;
            } finally {
                lock.unlock();
            }
        }
        
        @Override
        public int getValue() {
            lock.lock();
            try {
                return count;
            } finally {
                lock.unlock();
            }
        }
    }
    
    /**
     * synchronized版本的计数器
     */
    static class SynchronizedCounter implements Counter {
        private int count = 0;
        
        @Override
        public synchronized void increment() {
            count++;
        }
        
        @Override
        public synchronized int getValue() {
            return count;
        }
    }
    
    /**
     * 简单AQS实现演示
     */
    private static void demonstrateCustomAQS() {
        System.out.println("\n--- 自定义AQS实现演示 ---");
        
        SimpleMutex mutex = new SimpleMutex();
        
        System.out.println("🔧 自定义互斥锁测试:");
        
        // 测试基本的lock/unlock
        mutex.lock();
        System.out.println("获得自定义锁");
        
        // 测试重复lock（应该阻塞，但我们的简单实现可能不完美）
        boolean acquired = mutex.tryLock();
        System.out.println("重复获取: " + (acquired ? "成功" : "失败"));
        
        mutex.unlock();
        System.out.println("释放自定义锁");
        
        System.out.println("💡 这展示了AQS框架的基本原理");
    }
    
    /**
     * 简单的互斥锁实现（基于AQS思想）
     */
    static class SimpleMutex {
        private volatile boolean locked = false;
        
        public void lock() {
            while (!tryLock()) {
                // 简单的自旋等待
                Thread.yield();
            }
        }
        
        public boolean tryLock() {
            // 简化的CAS实现
            if (!locked) {
                locked = true;
                return true;
            }
            return false;
        }
        
        public void unlock() {
            locked = false;
        }
    }
}