/**
 * Task 1.1.7: 重量级锁与系统调用开销分析
 * 
 * 演示重量级锁的线程状态变化和系统调用开销
 * 
 * 学习目标：
 * 1. 观察线程状态变化(RUNNABLE, BLOCKED, WAITING)
 * 2. 理解用户态vs内核态切换成本
 * 3. 分析等待队列和同步队列机制
 * 4. 测量上下文切换开销
 */
import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.CountDownLatch;

public class HeavyweightLockDemo {
    
    private static final Object heavyLock = new Object();
    private static volatile boolean monitoring = true;
    
    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== 重量级锁系统调用开销分析 ===");
        
        // 实验1: 线程状态监控
        System.out.println("\n--- 实验1: 线程状态变化监控 ---");
        monitorThreadStates();
        
        Thread.sleep(2000);
        
        // 实验2: 上下文切换开销测量
        System.out.println("\n--- 实验2: 上下文切换开销测量 ---");
        measureContextSwitchOverhead();
        
        Thread.sleep(2000);
        
        // 实验3: 等待队列与同步队列分析
        System.out.println("\n--- 实验3: 等待队列与同步队列 ---");
        analyzeWaitingQueues();
    }
    
    /**
     * 监控线程状态变化
     * 观察BLOCKED状态的形成和解除过程
     */
    private static void monitorThreadStates() throws InterruptedException {
        CountDownLatch startLatch = new CountDownLatch(1);
        
        // 持锁线程 - 长时间持有锁
        Thread lockHolder = new Thread(() -> {
            synchronized (heavyLock) {
                System.out.println("持锁线程: 获得锁，开始长时间工作...");
                startLatch.countDown(); // 通知监控线程开始
                
                try {
                    Thread.sleep(3000); // 模拟长时间工作
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                
                System.out.println("持锁线程: 工作完成，即将释放锁");
            }
            System.out.println("持锁线程: 已释放锁");
        }, "LockHolder");
        
        // 竞争线程1
        Thread competitor1 = new Thread(() -> {
            try {
                startLatch.await(); // 等待持锁线程开始
                Thread.sleep(100);  // 确保持锁线程已获得锁
                
                System.out.println("竞争线程1: 尝试获取锁... (即将进入BLOCKED状态)");
                synchronized (heavyLock) {
                    System.out.println("竞争线程1: 成功获得锁！");
                    Thread.sleep(500);
                }
                System.out.println("竞争线程1: 释放锁");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }, "Competitor1");
        
        // 竞争线程2
        Thread competitor2 = new Thread(() -> {
            try {
                startLatch.await();
                Thread.sleep(200);
                
                System.out.println("竞争线程2: 尝试获取锁... (即将进入BLOCKED状态)");
                synchronized (heavyLock) {
                    System.out.println("竞争线程2: 成功获得锁！");
                    Thread.sleep(500);
                }
                System.out.println("竞争线程2: 释放锁");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }, "Competitor2");
        
        // 状态监控线程
        Thread monitor = new Thread(() -> {
            try {
                startLatch.await();
                
                for (int i = 0; i < 60; i++) { // 监控6秒
                    System.out.println("\n=== 线程状态快照 ===");
                    System.out.println("持锁线程: " + lockHolder.getState());
                    System.out.println("竞争线程1: " + competitor1.getState());
                    System.out.println("竞争线程2: " + competitor2.getState());
                    
                    Thread.sleep(100);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }, "StateMonitor");
        
        // 启动所有线程
        lockHolder.start();
        competitor1.start();
        competitor2.start();
        monitor.start();
        
        // 等待完成
        lockHolder.join();
        competitor1.join();
        competitor2.join();
        monitor.interrupt();
        
        System.out.println("\n💡 观察要点:");
        System.out.println("1. BLOCKED状态: 线程等待获取监视器锁");
        System.out.println("2. 状态转换: RUNNABLE → BLOCKED → RUNNABLE");
        System.out.println("3. 重量级锁: 涉及操作系统的线程调度");
    }
    
    /**
     * 测量上下文切换开销
     * 对比轻量级锁和重量级锁的性能差异
     */
    private static void measureContextSwitchOverhead() throws InterruptedException {
        final int ITERATIONS = 10000;
        
        // 测试场景1: 轻量级锁（无竞争）
        Object lightLock = new Object();
        long startTime = System.nanoTime();
        
        for (int i = 0; i < ITERATIONS; i++) {
            synchronized (lightLock) {
                // 简单操作，不会升级为重量级锁
                Math.random();
            }
        }
        
        long lightLockTime = System.nanoTime() - startTime;
        
        // 测试场景2: 重量级锁（有竞争）
        Object heavyLock = new Object();
        CountDownLatch competitionLatch = new CountDownLatch(2);
        
        startTime = System.nanoTime();
        
        // 创建竞争线程
        Thread thread1 = new Thread(() -> {
            for (int i = 0; i < ITERATIONS / 2; i++) {
                synchronized (heavyLock) {
                    // 模拟工作负载
                    try {
                        Thread.sleep(0, 1000); // 1微秒
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
            competitionLatch.countDown();
        }, "Heavy1");
        
        Thread thread2 = new Thread(() -> {
            for (int i = 0; i < ITERATIONS / 2; i++) {
                synchronized (heavyLock) {
                    try {
                        Thread.sleep(0, 1000);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
            competitionLatch.countDown();
        }, "Heavy2");
        
        thread1.start();
        thread2.start();
        
        competitionLatch.await();
        long heavyLockTime = System.nanoTime() - startTime;
        
        // 结果分析
        System.out.println("轻量级锁耗时: " + (lightLockTime / 1000000) + "ms");
        System.out.println("重量级锁耗时: " + (heavyLockTime / 1000000) + "ms");
        System.out.println("性能差异: " + String.format("%.2f", (double)heavyLockTime / lightLockTime) + "倍");
        
        System.out.println("\n💡 上下文切换开销分析:");
        System.out.println("1. 重量级锁需要系统调用(futex/mutex)");
        System.out.println("2. 线程阻塞需要保存/恢复CPU状态");
        System.out.println("3. 内核态切换比用户态操作慢数百倍");
    }
    
    /**
     * 分析等待队列与同步队列
     * 演示Object.wait()和synchronized的区别
     */
    private static void analyzeWaitingQueues() throws InterruptedException {
        final Object sharedResource = new Object();
        final CountDownLatch setupLatch = new CountDownLatch(1);
        
        System.out.println("演示等待队列(wait set)与同步队列(entry set):");
        
        // 场景1: wait()方法 - 进入等待队列
        Thread waitingThread = new Thread(() -> {
            synchronized (sharedResource) {
                try {
                    System.out.println("等待线程: 获得锁，调用wait()进入等待队列");
                    setupLatch.countDown();
                    sharedResource.wait(); // 释放锁并等待
                    System.out.println("等待线程: 被唤醒，重新获得锁");
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }, "WaitingThread");
        
        // 场景2: synchronized - 进入同步队列
        Thread blockedThread = new Thread(() -> {
            try {
                setupLatch.await();
                Thread.sleep(100);
                
                System.out.println("阻塞线程: 尝试获取锁，进入同步队列");
                synchronized (sharedResource) {
                    System.out.println("阻塞线程: 获得锁");
                    Thread.sleep(1000);
                }
                System.out.println("阻塞线程: 释放锁");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }, "BlockedThread");
        
        // 唤醒线程
        Thread notifierThread = new Thread(() -> {
            try {
                Thread.sleep(2000);
                
                synchronized (sharedResource) {
                    System.out.println("唤醒线程: 获得锁，调用notify()");
                    sharedResource.notify(); // 唤醒等待队列中的线程
                    Thread.sleep(500); // 继续持有锁一段时间
                }
                System.out.println("唤醒线程: 释放锁");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }, "NotifierThread");
        
        // 状态监控
        Thread queueMonitor = new Thread(() -> {
            try {
                setupLatch.await();
                
                for (int i = 0; i < 50; i++) {
                    System.out.println("\n--- 队列状态快照 ---");
                    System.out.println("等待线程状态: " + waitingThread.getState());
                    System.out.println("阻塞线程状态: " + blockedThread.getState());
                    System.out.println("唤醒线程状态: " + notifierThread.getState());
                    
                    Thread.sleep(100);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }, "QueueMonitor");
        
        // 启动线程
        waitingThread.start();
        queueMonitor.start();
        Thread.sleep(200);
        blockedThread.start();
        Thread.sleep(500);
        notifierThread.start();
        
        // 等待完成
        waitingThread.join();
        blockedThread.join();
        notifierThread.join();
        queueMonitor.interrupt();
        
        System.out.println("\n💡 队列机制总结:");
        System.out.println("1. 同步队列: synchronized等待锁的线程 (BLOCKED状态)");
        System.out.println("2. 等待队列: wait()等待唤醒的线程 (WAITING状态)");
        System.out.println("3. notify()将线程从等待队列移到同步队列");
        System.out.println("4. 重量级锁管理这两种队列，涉及大量系统调用");
    }
}