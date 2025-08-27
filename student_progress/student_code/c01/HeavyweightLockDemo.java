/**
 * Task 1.1.7: 重量级锁与系统调用开销分析
 * <p>
 * 任务要求：
 * 1. 观察线程状态变化(RUNNABLE, BLOCKED, WAITING)
 * 2. 测量上下文切换开销
 * 3. 理解等待队列和同步队列
 * 4. 分析用户态vs内核态切换成本
 * <p>
 * ⚠️ 重点观察线程状态的变化过程
 */

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.concurrent.CountDownLatch;

public class HeavyweightLockDemo {

    // TODO 1: 声明重量级锁对象
    private static final Object heavyLock = new Object();
    private static volatile boolean shouldStop = false;

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== 重量级锁系统调用开销分析 ===");

        // TODO 2: 线程状态监控
        System.out.println("\n--- 实验1: 线程状态变化监控 ---");
        monitorThreadStates();

        Thread.sleep(2000);

        // TODO 3: 上下文切换开销测量
        System.out.println("\n--- 实验2: 上下文切换开销测量 ---");
        measureContextSwitchOverhead();

        Thread.sleep(2000);

        // TODO 4: 等待队列与同步队列分析
        System.out.println("\n--- 实验3: 等待队列与同步队列 ---");
        analyzeWaitingQueues();
    }

    /**
     * TODO: 实现线程状态监控
     */
    private static void monitorThreadStates() throws InterruptedException {
        CountDownLatch startLatch = new CountDownLatch(1);

        // TODO: 创建持锁线程
        Thread lockHolder = new Thread(() -> {
            synchronized (heavyLock) {
                System.out.println("持锁线程: 获得锁，开始长时间工作...");
                // 长时间持有锁
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        // TODO: 创建竞争线程，观察BLOCKED状态
        Thread lockWeight1 = new Thread(() -> {
            System.out.println("LockWaiter1: 尝试获取锁...");
            synchronized (heavyLock) {
                System.out.println("LockWaiter1: 成功获取锁。");
            }
        }, "竞争线程1");

        Thread lockWeight2 = new Thread(() -> {
            System.out.println("LockWaiter2: 尝试获取锁...");
            synchronized (heavyLock) {
                System.out.println("LockWaiter2: 成功获取锁。");
            }
        }, "竞争线程2");

        // 状态监控线程
        Thread stateMonitor = new Thread(() -> {
            try {

                // 只要 lockHolder 还在运行，就持续监控
                while (lockHolder.isAlive()) {
                    System.out.println("\n--- 线程状态快照 --- ");
                    System.out.println("LockHolder: " + lockHolder.getState());
                    System.out.println("LockWaiter1: " + lockWeight1.getState());
                    System.out.println("LockWaiter2: " + lockWeight2.getState());
                }
                Thread.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }, " MonitorThread");

        // TODO: 启动线程并观察状态变化
        lockHolder.start();
        Thread.sleep(100); // 确保lockHolder先获得锁

        lockWeight1.start();
        lockWeight2.start();
        stateMonitor.start();

        // 等待所有线程完成
        lockHolder.join();
        lockWeight1.join();
        lockWeight2.join();
        stateMonitor.join();

        System.out.println("\n--- 实验结束 ---");

//        --- 线程状态快照 ---
//                LockHolder: TIMED_WAITING  // LockHolder线程持有锁且sleep，是 TIMED_WAITING
//        LockWaiter1: BLOCKED  // 其它线程试图获得锁，但因为LockHolder持有，代替自旋和浪费CPU，JVM升级到重量级锁。OS阻塞这两个线程，让它们进入睡眠状态，在等待队列。
//        LockWaiter2: BLOCKED
//
//                --- 线程状态快照 ---
//                LockWaiter2: 成功获取锁。 // 当LockHolder3s后释放锁，OS首先唤醒LockWaiter1，线程1获取锁，释放，然后OS唤醒线程2
//        LockWaiter1: 成功获取锁。
//        LockHolder: RUNNABLE
//        LockWaiter1: TERMINATED
//        LockWaiter2: TERMINATED
    }

    /**
     * TODO: 实现上下文切换开销测量
     * <p>
     * Here's the sequence of events:
     * 1. The main thread starts contender1 and contender2. These two threads
     * immediately start competing for the lock in the background.
     * 2. The main thread then executes Thread.sleep(2000);. This pauses the main thread
     * for 2 seconds.
     * 3. Crucially, while the main thread is sleeping, the contender1 and contender2
     * threads are still running and fighting over the lock. This is the period of
     * intense competition we wanted to create.
     * 4. After 2 seconds, the main thread wakes up and sets shouldStop = true;, which
     * signals the contender threads to finish.
     */
    private static void measureContextSwitchOverhead() throws InterruptedException {
        final int ITERATIONS = 10000;

        // TODO: 测试轻量级锁场景（无竞争）
        Object lightLock = new Object();
        // 单线程循环获取锁，测量时间
        long startTime = System.nanoTime();
        Thread lThread = new Thread(() -> {
            synchronized (lightLock) {

            }
        }, "lightweight lock");
        lThread.start();
        lThread.join();
        long duration = System.nanoTime() - startTime;
        System.out.println("轻量级锁测量时间：" + duration / 1000000 + " ms");

        // TODO: 测试重量级锁场景（有竞争）
        // 多线程竞争同一个锁，测量时间
        ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();

        // create a scenario with intense lock competition
        Object contentedLock = new Object(); // 竞争锁
        Runnable contenderTask = () -> {
            while (!shouldStop) {
                synchronized (contentedLock) {
                    // hold the lock for a very short time to maximize contention
                }
            }
        };

        Thread contender1 = new Thread(contenderTask);
        Thread contender2 = new Thread(contenderTask);

        startTime = System.nanoTime();
        long startCPUTime = threadMXBean.getCurrentThreadCpuTime();

        contender1.start();
        contender2.start();

        // let the competition run for 2s
        Thread.sleep(2000); // 让主线程睡觉
        shouldStop = true;

        contender1.join();
        contender2.join();

        long endCPUTime = threadMXBean.getCurrentThreadCpuTime();
        long wallClockTime = System.nanoTime() - startTime;  // 现实时间，因为sleep 2s，所以这里也基本是2s
        long cpuTime = endCPUTime - startCPUTime; //  因为测量CPU时间仅在主线程，而2个竞争线程争锁时，主线程什么也没做，几乎没有消耗CPU。CPU的消耗在其它线程
        long blockedTime = wallClockTime - cpuTime;  // 主线程sleep

        System.out.println("重量级锁竞争 (2秒):");
        System.out.printf("  墙钟时间 (Wall Clock Time): %,d ns%n", wallClockTime);
        System.out.printf("  CPU 时间 (CPU Time):         %,d ns%n", cpuTime);
        System.out.printf("  阻塞/等待时间 (Blocked Time): %,d ns%n", blockedTime);
        System.out.printf("  %% 时间用于阻塞 (%% Time Blocked): %.2f%%%n", ((double) blockedTime / wallClockTime * 100));

    }

    /**
     * TODO: 实现等待队列与同步队列分析
     * 演示Object.wait() 和 synchronized 的区别
     */
    private static void analyzeWaitingQueues() throws InterruptedException {
        final Object sharedResource = new Object();
        final CountDownLatch setupLatch = new CountDownLatch(1);

        // 场景1：进入等待队列 - wait()
        Thread waitingThread = new Thread(() -> {
            synchronized (sharedResource) {
                try {
                    System.out.println("WaitingThread: 获得锁，调用wait()进入等待队列");
                    setupLatch.countDown(); // 通知其他线程可以开始了
                    sharedResource.wait(); // 释放锁进入WAITING状态
                    System.out.println("WaitingThread: 被唤醒，重新获得锁并完成");
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }, "waitingthread");

        // 场景2： 进入同步队列 - synchronized
        Thread blockedThread = new Thread(() -> {
            try {
                setupLatch.await(); // 等待WaitingThread 进入wait()状态
                System.out.println("BlockedThread: 尝试获取锁，将进入同步队列（BLOCKED)");
                synchronized (sharedResource) {
                    System.out.println("成功获得锁");
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }, "BlockedThread");

        // 唤醒线程
        Thread notifierThread = new Thread(() -> {
            try {
                setupLatch.await(); // 确保waiting thread 已经wait()
                Thread.sleep(1000); // 等待BlockedThread也开始尝试获取锁
                synchronized (sharedResource) {
                    System.out.println("NotifierThread: 获得锁，准备调用notify()");
                    sharedResource.notify(); // 唤醒waiting thread
                    System.out.println("NotifierThread: 已调用notify(), 但将继续持有锁2s");
                    Thread.sleep(2000); // 继续持有锁，让BlockedThread保持BLOCKED

                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println("NotifierThread: 释放锁");
        }, "NotifierThread");

        waitingThread.start();
        blockedThread.start();
        notifierThread.start();

        // 监控线程
        Thread monitor = new Thread(()->{
            try {
                setupLatch.await();
                for(int i=0;i<5;i++){
                    Thread.sleep(1000);
                    System.out.printf("\n---状态快照 [%d s]---\n", i+1);
                    System.out.println("   WaitingThread: "+waitingThread.getState());
                    System.out.println("   blockedThread: "+blockedThread.getState());
                    System.out.println("   notifierThread: "+notifierThread.getState());
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        monitor.start();

        waitingThread.join();
        blockedThread.join();
        notifierThread.join();
        monitor.interrupt();


        // TODO: 创建wait()等待的线程（进入等待队列）
        // Thread waitingThread = new Thread(() -> {
        //     synchronized (sharedResource) {
        //         try {
        //             System.out.println("进入等待队列");
        //             sharedResource.wait();
        //             System.out.println("被唤醒");
        //         } catch (InterruptedException e) { ... }
        //     }
        // });

        // TODO: 创建synchronized阻塞的线程（进入同步队列）

        // TODO: 创建notify()唤醒线程

        // TODO: 监控不同队列中线程的状态变化
    }
}