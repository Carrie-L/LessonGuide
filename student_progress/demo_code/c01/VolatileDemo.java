// 演示代码: volatile关键字特性
// 目标: 对比volatile和非volatile的差异，理解可见性vs原子性
public class VolatileDemo {
    
    // 实验1: 可见性问题演示
    private static boolean normalFlag = false;          // 普通变量
    private static volatile boolean volatileFlag = false;   // volatile变量
    
    // 实验2: 原子性问题演示  
    private static volatile int volatileCounter = 0;    // volatile不保证原子性
    
    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== volatile关键字特性演示 ===");
        
        System.out.println("--- 实验1: 可见性对比 ---");
        testVisibility();
        
        Thread.sleep(3000);
        
        System.out.println("\n--- 实验2: volatile不保证原子性 ---");
        testAtomicity();
        
        System.out.println("\n--- 实验3: volatile的正确使用场景 ---");
        testCorrectUsage();
    }
    
    // 测试可见性问题
    private static void testVisibility() throws InterruptedException {
        
        // 测试普通变量的可见性问题
        System.out.println("\n🔍 测试普通变量可见性:");
        normalFlag = false;
        
        Thread readerThread1 = new Thread(() -> {
            System.out.println("普通变量读取者: 开始等待...");
            long startTime = System.currentTimeMillis();
            while (!normalFlag) {
                // 可能永远循环下去！
                // 检查是否超时
                if (System.currentTimeMillis() - startTime > 2000) {
                    System.out.println("普通变量读取者: 超时退出 (可见性问题!)");
                    return;
                }
            }
            System.out.println("普通变量读取者: 检测到flag变化!");
        }, "NormalFlagReader");
        
        Thread writerThread1 = new Thread(() -> {
            try {
                Thread.sleep(1000);
                normalFlag = true;
                System.out.println("普通变量写入者: 已设置flag=true");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }, "NormalFlagWriter");
        
        readerThread1.start();
        writerThread1.start();
        readerThread1.join();
        writerThread1.join();
        
        Thread.sleep(500);
        
        // 测试volatile变量
        System.out.println("\n🔍 测试volatile变量可见性:");
        volatileFlag = false;
        
        Thread readerThread2 = new Thread(() -> {
            System.out.println("volatile读取者: 开始等待...");
            while (!volatileFlag) {
                // volatile保证可见性，会正常退出
            }
            System.out.println("volatile读取者: 检测到flag变化! ✅");
        }, "VolatileFlagReader");
        
        Thread writerThread2 = new Thread(() -> {
            try {
                Thread.sleep(1000);
                volatileFlag = true;
                System.out.println("volatile写入者: 已设置flag=true");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }, "VolatileFlagWriter");
        
        readerThread2.start();
        writerThread2.start();
        readerThread2.join();
        writerThread2.join();
    }
    
    // 测试原子性问题
    private static void testAtomicity() throws InterruptedException {
        final int THREAD_COUNT = 10;
        final int INCREMENT_COUNT = 1000;
        
        System.out.println("🔍 测试volatile是否保证原子性:");
        System.out.println("启动" + THREAD_COUNT + "个线程，每个递增" + INCREMENT_COUNT + "次");
        
        volatileCounter = 0;
        Thread[] threads = new Thread[THREAD_COUNT];
        
        for (int i = 0; i < THREAD_COUNT; i++) {
            final int threadId = i;
            threads[i] = new Thread(() -> {
                for (int j = 0; j < INCREMENT_COUNT; j++) {
                    volatileCounter++;  // ❌ volatile不保证原子性！
                    // ++操作仍然是: 读取->递增->写回 三个步骤
                }
            }, "VolatileCounterThread-" + threadId);
            threads[i].start();
        }
        
        for (Thread thread : threads) {
            thread.join();
        }
        
        System.out.println("期望结果: " + (THREAD_COUNT * INCREMENT_COUNT));
        System.out.println("实际结果: " + volatileCounter);
        System.out.println("数据丢失: " + (THREAD_COUNT * INCREMENT_COUNT - volatileCounter));
        if (volatileCounter != THREAD_COUNT * INCREMENT_COUNT) {
            System.out.println("❌ 结论: volatile保证可见性，但不保证原子性");
        } else {
            System.out.println("⚠️  这次运行没有出现数据丢失，但不代表volatile保证原子性");
            System.out.println("   多次运行可能会看到不同结果");
        }
    }
    
    // 演示volatile的正确使用场景
    private static void testCorrectUsage() throws InterruptedException {
        System.out.println("\n🔍 volatile的正确使用场景:");
        
        // 场景1: 状态标志
        System.out.println("✅ 场景1: 状态标志 (单个线程写，多个线程读)");
        StatusFlag statusDemo = new StatusFlag();
        statusDemo.demonstrate();
        
        Thread.sleep(1000);
        
        // 场景2: 双重检查锁定单例模式
        System.out.println("\n✅ 场景2: 双重检查锁定单例模式");
        Singleton instance1 = Singleton.getInstance();
        Singleton instance2 = Singleton.getInstance();
        System.out.println("两次获取的实例相同: " + (instance1 == instance2));
    }
    
    // 正确使用场景1: 状态标志
    private static class StatusFlag {
        private volatile boolean running = true;  // 状态标志用volatile
        
        public void demonstrate() throws InterruptedException {
            // 工作线程
            Thread worker = new Thread(() -> {
                int count = 0;
                while (running) {  // 读取volatile变量
                    count++;
                    if (count % 100000000 == 0) {
                        System.out.println("工作线程: 处理了 " + count + " 次循环");
                    }
                }
                System.out.println("工作线程: 收到停止信号，退出");
            }, "Worker");
            
            worker.start();
            Thread.sleep(100);  // 让工作线程运行一会儿
            
            running = false;  // 写入volatile变量，立即对所有线程可见
            System.out.println("主线程: 已发送停止信号");
            
            worker.join();
        }
    }
    
    // 正确使用场景2: 双重检查锁定单例
    private static class Singleton {
        private static volatile Singleton instance;  // 必须使用volatile
        
        private Singleton() {}
        
        public static Singleton getInstance() {
            if (instance == null) {  // 第一次检查
                synchronized (Singleton.class) {
                    if (instance == null) {  // 第二次检查
                        instance = new Singleton();  // volatile确保正确发布
                    }
                }
            }
            return instance;
        }
    }
}