/**
 * Task 1.1.6: CAS操作与轻量级锁深度实践
 * 
 * 演示CAS(Compare-And-Swap)原理和ABA问题
 * 
 * 学习目标：
 * 1. 理解CAS操作的原子性保证
 * 2. 观察ABA问题的发生和解决方案
 * 3. 掌握自旋重试机制
 * 4. 对比CAS vs synchronized性能
 */
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicStampedReference;

public class CASandLightweightLockDemo {
    
    private static volatile int normalCounter = 0;
    private static AtomicInteger atomicCounter = new AtomicInteger(0);
    
    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== CAS操作与轻量级锁机制演示 ===");
        
        // 实验1: CAS vs synchronized性能对比
        System.out.println("\n--- 实验1: CAS vs Synchronized性能对比 ---");
        performanceComparison();
        
        // 实验2: 手写CAS操作模拟
        System.out.println("\n--- 实验2: 手写CAS操作模拟 ---");
        simulateCASOperation();
        
        // 实验3: ABA问题演示
        System.out.println("\n--- 实验3: ABA问题演示 ---");
        demonstrateABAProblem();
        
        // 实验4: ABA问题解决方案
        System.out.println("\n--- 实验4: ABA问题解决方案 ---");
        solveABAProblem();
    }
    
    /**
     * CAS vs Synchronized性能对比
     */
    private static void performanceComparison() throws InterruptedException {
        final int THREAD_COUNT = 4;
        final int ITERATIONS = 250000;
        
        // 测试CAS性能
        atomicCounter.set(0);
        long startTime = System.nanoTime();
        
        Thread[] casThreads = new Thread[THREAD_COUNT];
        for (int i = 0; i < THREAD_COUNT; i++) {
            casThreads[i] = new Thread(() -> {
                for (int j = 0; j < ITERATIONS; j++) {
                    atomicCounter.incrementAndGet(); // CAS操作
                }
            }, "CAS-Thread" + i);
            casThreads[i].start();
        }
        
        for (Thread thread : casThreads) {
            thread.join();
        }
        
        long casTime = System.nanoTime() - startTime;
        
        // 测试synchronized性能
        normalCounter = 0;
        final Object lock = new Object();
        startTime = System.nanoTime();
        
        Thread[] syncThreads = new Thread[THREAD_COUNT];
        for (int i = 0; i < THREAD_COUNT; i++) {
            syncThreads[i] = new Thread(() -> {
                for (int j = 0; j < ITERATIONS; j++) {
                    synchronized (lock) {
                        normalCounter++; // synchronized操作
                    }
                }
            }, "Sync-Thread" + i);
            syncThreads[i].start();
        }
        
        for (Thread thread : syncThreads) {
            thread.join();
        }
        
        long syncTime = System.nanoTime() - startTime;
        
        System.out.println("CAS结果: " + atomicCounter.get() + ", 耗时: " + (casTime / 1000000) + "ms");
        System.out.println("Synchronized结果: " + normalCounter + ", 耗时: " + (syncTime / 1000000) + "ms");
        System.out.println("性能比值: CAS比synchronized快 " + 
                         String.format("%.2f", (double)syncTime / casTime) + " 倍");
    }
    
    /**
     * 手写CAS操作模拟
     * 展示CAS的基本原理：比较并交换
     */
    private static void simulateCASOperation() {
        System.out.println("CAS操作原理演示:");
        
        // 模拟CAS操作类
        class SimpleCAS {
            private volatile int value;
            
            public SimpleCAS(int initialValue) {
                this.value = initialValue;
            }
            
            // 模拟CAS操作：比较并交换
            public boolean compareAndSet(int expected, int newValue) {
                // 注意：这里的实现不是真正的原子操作，仅用于演示逻辑
                if (value == expected) {
                    value = newValue;
                    return true; // 交换成功
                }
                return false; // 交换失败
            }
            
            public int getValue() {
                return value;
            }
            
            // 带自旋重试的递增操作
            public int incrementAndGet() {
                int current;
                int next;
                do {
                    current = getValue();
                    next = current + 1;
                } while (!compareAndSet(current, next)); // 自旋直到成功
                
                return next;
            }
        }
        
        SimpleCAS casValue = new SimpleCAS(10);
        
        System.out.println("初始值: " + casValue.getValue());
        
        // 成功的CAS操作
        boolean success1 = casValue.compareAndSet(10, 20);
        System.out.println("CAS(10→20): " + success1 + ", 当前值: " + casValue.getValue());
        
        // 失败的CAS操作
        boolean success2 = casValue.compareAndSet(10, 30);
        System.out.println("CAS(10→30): " + success2 + ", 当前值: " + casValue.getValue());
        
        // 自旋重试递增
        int newValue = casValue.incrementAndGet();
        System.out.println("自旋递增后: " + newValue);
    }
    
    /**
     * ABA问题演示
     * A→B→A的变化过程可能导致CAS误判
     */
    private static void demonstrateABAProblem() throws InterruptedException {
        System.out.println("ABA问题演示:");
        
        // 使用AtomicReference模拟栈结构
        AtomicReference<Node> stackTop = new AtomicReference<>();
        
        // 初始化栈：A→B→C
        Node nodeC = new Node("C", null);
        Node nodeB = new Node("B", nodeC);
        Node nodeA = new Node("A", nodeB);
        stackTop.set(nodeA);
        
        System.out.println("初始栈: " + printStack(stackTop.get()));
        
        // 线程1：准备从A→B的CAS操作，但会被延迟
        Thread thread1 = new Thread(() -> {
            Node currentTop = stackTop.get(); // 读取当前栈顶A
            System.out.println("线程1: 读取到栈顶 " + currentTop.data);
            
            try {
                Thread.sleep(100); // 模拟延迟，让线程2有机会执行
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            
            // 此时栈顶可能已经被其他线程修改过
            Node newTop = currentTop.next; // 准备弹出A，让B成为新栈顶
            boolean success = stackTop.compareAndSet(currentTop, newTop);
            
            System.out.println("线程1: CAS操作" + (success ? "成功" : "失败"));
            if (success) {
                System.out.println("线程1: 栈变为 " + printStack(stackTop.get()));
            }
        }, "ABA-Thread1");
        
        // 线程2：快速执行A→B→A的操作
        Thread thread2 = new Thread(() -> {
            try {
                Thread.sleep(50); // 等待线程1读取栈顶
                
                // 操作1: 弹出A
                Node oldTop = stackTop.get();
                stackTop.compareAndSet(oldTop, oldTop.next);
                System.out.println("线程2: 弹出A，栈变为 " + printStack(stackTop.get()));
                
                // 操作2: 弹出B  
                oldTop = stackTop.get();
                stackTop.compareAndSet(oldTop, oldTop.next);
                System.out.println("线程2: 弹出B，栈变为 " + printStack(stackTop.get()));
                
                // 操作3: 重新压入A
                Node newA = new Node("A", stackTop.get());
                stackTop.set(newA);
                System.out.println("线程2: 压入新A，栈变为 " + printStack(stackTop.get()));
                
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }, "ABA-Thread2");
        
        thread1.start();
        thread2.start();
        
        thread1.join();
        thread2.join();
        
        System.out.println("⚠️ ABA问题: 线程1的CAS可能成功，但基于过时的假设");
    }
    
    /**
     * ABA问题解决方案：使用版本号
     */
    private static void solveABAProblem() throws InterruptedException {
        System.out.println("ABA问题解决方案 - 版本号机制:");
        
        // 使用AtomicStampedReference，包含版本号
        Node nodeC = new Node("C", null);
        Node nodeB = new Node("B", nodeC);
        Node nodeA = new Node("A", nodeB);
        
        AtomicStampedReference<Node> versionedStackTop = 
            new AtomicStampedReference<>(nodeA, 0); // 初始版本号为0
        
        System.out.println("初始栈: " + printStack(versionedStackTop.getReference()));
        
        Thread thread1 = new Thread(() -> {
            int[] stampHolder = new int[1];
            Node currentTop = versionedStackTop.get(stampHolder);
            int currentStamp = stampHolder[0];
            
            System.out.println("线程1: 读取栈顶 " + currentTop.data + "，版本号 " + currentStamp);
            
            try {
                Thread.sleep(100); // 延迟
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            
            Node newTop = currentTop.next;
            boolean success = versionedStackTop.compareAndSet(
                currentTop, newTop, currentStamp, currentStamp + 1);
            
            System.out.println("线程1: 带版本号的CAS " + (success ? "成功" : "失败"));
        }, "Versioned-Thread1");
        
        Thread thread2 = new Thread(() -> {
            try {
                Thread.sleep(50);
                
                // 执行多个版本号递增的操作
                for (int i = 0; i < 3; i++) {
                    int[] stampHolder = new int[1];
                    Node current = versionedStackTop.get(stampHolder);
                    int currentStamp = stampHolder[0];
                    
                    // 模拟栈操作，每次都更新版本号
                    versionedStackTop.compareAndSet(current, current, 
                                                  currentStamp, currentStamp + 1);
                    System.out.println("线程2: 版本号更新为 " + (currentStamp + 1));
                }
                
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }, "Versioned-Thread2");
        
        thread1.start();
        thread2.start();
        
        thread1.join();
        thread2.join();
        
        System.out.println("✅ 版本号机制防止了ABA问题");
    }
    
    /**
     * 链表节点类
     */
    private static class Node {
        final String data;
        final Node next;
        
        Node(String data, Node next) {
            this.data = data;
            this.next = next;
        }
    }
    
    /**
     * 打印栈结构的辅助方法
     */
    private static String printStack(Node top) {
        if (top == null) return "空栈";
        
        StringBuilder sb = new StringBuilder();
        Node current = top;
        while (current != null) {
            sb.append(current.data);
            if (current.next != null) {
                sb.append("→");
            }
            current = current.next;
        }
        return sb.toString();
    }
}