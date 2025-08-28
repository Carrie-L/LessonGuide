/**
 * Task 1.1.9: 无锁栈实现 - CAS高级应用
 * 
 * 使用CAS操作实现线程安全的无锁栈数据结构
 * 
 * 学习目标：
 * 1. 掌握无锁数据结构的设计原理
 * 2. 解决ABA问题的实际应用
 * 3. 理解内存回收在无锁结构中的挑战
 * 4. 对比无锁vs加锁的性能差异
 */
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicStampedReference;
import java.util.concurrent.atomic.AtomicInteger;

public class LockFreeStack<T> {
    
    /**
     * 栈节点类
     */
    private static class Node<T> {
        final T data;
        volatile Node<T> next;
        
        Node(T data, Node<T> next) {
            this.data = data;
            this.next = next;
        }
    }
    
    // 方案1: 简单CAS实现（存在ABA问题）
    private final AtomicReference<Node<T>> top = new AtomicReference<>();
    
    // 方案2: 版本号解决ABA问题
    private final AtomicStampedReference<Node<T>> versionedTop = 
        new AtomicStampedReference<>(null, 0);
    
    // 性能统计
    private final AtomicInteger pushRetries = new AtomicInteger(0);
    private final AtomicInteger popRetries = new AtomicInteger(0);
    
    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== 无锁栈实现与性能测试 ===");
        
        // 基本功能测试
        testBasicOperations();
        
        // ABA问题演示
        demonstrateABAProblem();
        
        // 性能对比测试
        performanceComparison();
        
        // 并发压力测试
        concurrencyStressTest();
    }
    
    /**
     * 基本功能测试
     */
    private static void testBasicOperations() {
        System.out.println("\n--- 基本功能测试 ---");
        
        LockFreeStack<String> stack = new LockFreeStack<>();
        
        // 测试push操作
        System.out.println("测试Push操作:");
        stack.push("第一个元素");
        stack.push("第二个元素");
        stack.push("第三个元素");
        
        System.out.println("栈大小: " + stack.size());
        System.out.println("栈顶元素: " + stack.peek());
        
        // 测试pop操作
        System.out.println("\n测试Pop操作:");
        while (!stack.isEmpty()) {
            System.out.println("弹出: " + stack.pop());
        }
        
        System.out.println("栈是否为空: " + stack.isEmpty());
    }
    
    /**
     * Push操作 - 使用CAS实现
     */
    public void push(T item) {
        Node<T> newNode = new Node<>(item, null);
        
        while (true) {
            Node<T> currentTop = top.get();
            newNode.next = currentTop;
            
            // CAS操作：如果top还是currentTop，就更新为newNode
            if (top.compareAndSet(currentTop, newNode)) {
                break; // 成功，退出循环
            }
            
            // 失败，记录重试次数
            pushRetries.incrementAndGet();
        }
    }
    
    /**
     * Pop操作 - 使用CAS实现
     */
    public T pop() {
        while (true) {
            Node<T> currentTop = top.get();
            
            if (currentTop == null) {
                return null; // 栈为空
            }
            
            Node<T> newTop = currentTop.next;
            
            // CAS操作：如果top还是currentTop，就更新为newTop
            if (top.compareAndSet(currentTop, newTop)) {
                return currentTop.data; // 成功，返回数据
            }
            
            // 失败，记录重试次数
            popRetries.incrementAndGet();
        }
    }
    
    /**
     * 使用版本号的Push操作 - 解决ABA问题
     */
    public void pushVersioned(T item) {
        Node<T> newNode = new Node<>(item, null);
        
        while (true) {
            int[] stampHolder = new int[1];
            Node<T> currentTop = versionedTop.get(stampHolder);
            int currentStamp = stampHolder[0];
            
            newNode.next = currentTop;
            
            // 使用版本号的CAS操作
            if (versionedTop.compareAndSet(currentTop, newNode, 
                                         currentStamp, currentStamp + 1)) {
                break;
            }
        }
    }
    
    /**
     * 使用版本号的Pop操作
     */
    public T popVersioned() {
        while (true) {
            int[] stampHolder = new int[1];
            Node<T> currentTop = versionedTop.get(stampHolder);
            int currentStamp = stampHolder[0];
            
            if (currentTop == null) {
                return null;
            }
            
            Node<T> newTop = currentTop.next;
            
            if (versionedTop.compareAndSet(currentTop, newTop, 
                                         currentStamp, currentStamp + 1)) {
                return currentTop.data;
            }
        }
    }
    
    /**
     * Peek操作 - 查看栈顶元素但不弹出
     */
    public T peek() {
        Node<T> currentTop = top.get();
        return currentTop != null ? currentTop.data : null;
    }
    
    /**
     * 判断栈是否为空
     */
    public boolean isEmpty() {
        return top.get() == null;
    }
    
    /**
     * 计算栈大小
     */
    public int size() {
        int count = 0;
        Node<T> current = top.get();
        
        while (current != null) {
            count++;
            current = current.next;
        }
        
        return count;
    }
    
    /**
     * ABA问题演示
     */
    private static void demonstrateABAProblem() throws InterruptedException {
        System.out.println("\n--- ABA问题演示 ---");
        
        LockFreeStack<String> stack = new LockFreeStack<>();
        stack.push("C");
        stack.push("B");
        stack.push("A");
        
        System.out.println("初始栈: A->B->C");
        
        // 线程1: 准备执行慢速的pop操作
        Thread thread1 = new Thread(() -> {
            Node<String> currentTop = stack.top.get(); // 读取A
            System.out.println("线程1: 读取栈顶 " + currentTop.data);
            
            try {
                Thread.sleep(100); // 模拟延迟
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            
            // 此时栈可能已经被修改
            Node<String> newTop = currentTop.next; // B
            boolean success = stack.top.compareAndSet(currentTop, newTop);
            
            System.out.println("线程1: CAS " + (success ? "成功" : "失败"));
            if (success) {
                System.out.println("线程1: 弹出了 " + currentTop.data);
            }
        }, "SlowPop");
        
        // 线程2: 快速执行A->B->A的操作序列
        Thread thread2 = new Thread(() -> {
            try {
                Thread.sleep(50); // 让线程1先读取
                
                // 操作1: 弹出A
                String item1 = stack.pop();
                System.out.println("线程2: 弹出 " + item1);
                
                // 操作2: 弹出B
                String item2 = stack.pop();
                System.out.println("线程2: 弹出 " + item2);
                
                // 操作3: 重新压入A
                stack.push("A");
                System.out.println("线程2: 重新压入 A");
                
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }, "FastOperations");
        
        thread1.start();
        thread2.start();
        
        thread1.join();
        thread2.join();
        
        System.out.println("最终栈内容: " + stack.peek());
        System.out.println("⚠️  这演示了ABA问题可能导致的意外结果");
    }
    
    /**
     * 性能对比测试：无锁栈 vs 同步栈
     */
    private static void performanceComparison() throws InterruptedException {
        System.out.println("\n--- 性能对比测试 ---");
        
        final int THREAD_COUNT = 4;
        final int OPERATIONS_PER_THREAD = 100000;
        
        // 测试无锁栈
        LockFreeStack.StackInterface<Integer> lockFreeStack = new StackInterface<Integer>() {
            @Override
            public void push(Integer item) {

            }

            @Override
            public Integer pop() {
                return 0;
            }
        };
        long lockFreeTime = measureStackPerformance("无锁栈", THREAD_COUNT, 
                                                   OPERATIONS_PER_THREAD, lockFreeStack);
        
        // 测试同步栈
        SynchronizedStack<Integer> syncStack = new SynchronizedStack<>();
        long syncTime = measureStackPerformance("同步栈", THREAD_COUNT, 
                                               OPERATIONS_PER_THREAD, syncStack);
        
        System.out.println("\n=== 性能对比结果 ===");
        System.out.println("无锁栈: " + lockFreeTime + "ms");
        System.out.println("同步栈: " + syncTime + "ms");
        System.out.println("性能提升: " + String.format("%.2fx", (double)syncTime / lockFreeTime));
        
        System.out.println("\n=== CAS重试统计 ===");
//        System.out.println("Push重试次数: " + lockFreeStack.pushRetries.get());
//        System.out.println("Pop重试次数: " + lockFreeStack.popRetries.get());
    }
    
    /**
     * 测量栈性能的通用方法
     */
    private static long measureStackPerformance(String name, int threadCount, 
                                               int operationsPerThread, StackInterface<Integer> stack) 
                                               throws InterruptedException {
        System.out.println("测试 " + name + "...");
        
        long startTime = System.currentTimeMillis();
        
        Thread[] threads = new Thread[threadCount];
        for (int i = 0; i < threadCount; i++) {
            final int threadId = i;
            threads[i] = new Thread(() -> {
                // 每个线程执行混合的push/pop操作
                for (int j = 0; j < operationsPerThread; j++) {
                    if (j % 2 == 0) {
                        stack.push(threadId * operationsPerThread + j);
                    } else {
                        stack.pop();
                    }
                }
            }, name + "-Thread" + i);
            threads[i].start();
        }
        
        for (Thread thread : threads) {
            thread.join();
        }
        
        long endTime = System.currentTimeMillis();
        return endTime - startTime;
    }
    
    /**
     * 并发压力测试
     */
    private static void concurrencyStressTest() throws InterruptedException {
        System.out.println("\n--- 并发压力测试 ---");
        
        LockFreeStack<Integer> stack = new LockFreeStack<>();
        final int THREAD_COUNT = 8;
        final int OPERATIONS = 50000;
        
        // 预填充一些数据
        for (int i = 0; i < 1000; i++) {
            stack.push(i);
        }
        
        Thread[] threads = new Thread[THREAD_COUNT];
        
        long startTime = System.currentTimeMillis();
        
        for (int i = 0; i < THREAD_COUNT; i++) {
            final int threadId = i;
            threads[i] = new Thread(() -> {
                for (int j = 0; j < OPERATIONS; j++) {
                    // 随机选择操作
                    if (Math.random() < 0.7) { // 70% push
                        stack.push(threadId * OPERATIONS + j);
                    } else { // 30% pop
                        stack.pop();
                    }
                }
            }, "StressTest-" + i);
            threads[i].start();
        }
        
        for (Thread thread : threads) {
            thread.join();
        }
        
        long endTime = System.currentTimeMillis();
        
        System.out.println("压力测试完成，耗时: " + (endTime - startTime) + "ms");
        System.out.println("最终栈大小: " + stack.size());
        System.out.println("Push重试总数: " + stack.pushRetries.get());
        System.out.println("Pop重试总数: " + stack.popRetries.get());
        System.out.println("平均每操作重试: " + 
                         String.format("%.2f", 
                         (double)(stack.pushRetries.get() + stack.popRetries.get()) / 
                         (THREAD_COUNT * OPERATIONS)));
    }
    
    /**
     * 栈接口 - 便于性能对比
     */
    interface StackInterface<T> {
        void push(T item);
        T pop();
    }
    
    /**
     * 同步栈实现 - 用于性能对比
     */
    static class SynchronizedStack<T> implements StackInterface<T> {
        private Node<T> top;
        
        public synchronized void push(T item) {
            Node<T> newNode = new Node<>(item, top);
            top = newNode;
        }
        
        public synchronized T pop() {
            if (top == null) {
                return null;
            }
            
            T data = top.data;
            top = top.next;
            return data;
        }
    }
}