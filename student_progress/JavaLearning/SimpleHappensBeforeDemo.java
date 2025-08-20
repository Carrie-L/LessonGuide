/**
 * 简化的happens-before演示
 * 专注理解核心概念
 */
public class SimpleHappensBeforeDemo {
    
    // 演示1: 没有happens-before保证的问题
    static class ProblematicExample {
        private int data = 0;
        private boolean ready = false;  // 普通变量
        
        // 线程1: 写入数据
        public void writer() {
            data = 42;       // ①
            ready = true;    // ② 没有happens-before保证
        }
        
        // 线程2: 读取数据
        public void reader() {
            if (ready) {     // ③ 可能看不到ready=true
                System.out.println("Data: " + data);  // ④ 可能是0而不是42
            } else {
                System.out.println("Not ready yet");
            }
        }
    }
    
    // 演示2: 使用volatile提供happens-before保证
    static class VolatileExample {
        private int data = 0;
        private volatile boolean ready = false;  // volatile变量
        
        public void writer() {
            data = 42;       // ①
            ready = true;    // ② volatile写
        }
        
        public void reader() {
            if (ready) {     // ③ volatile读
                System.out.println("Data: " + data);  // ④ 保证是42
            } else {
                System.out.println("Not ready yet");
            }
        }
    }
    
    // 演示3: 使用synchronized提供happens-before保证
    static class SynchronizedExample {
        private int data = 0;
        private boolean ready = false;
        private final Object lock = new Object();
        
        public void writer() {
            synchronized (lock) {  // 获取锁
                data = 42;
                ready = true;
            }  // 释放锁
        }
        
        public void reader() {
            synchronized (lock) {  // 获取同一个锁
                if (ready) {
                    System.out.println("Data: " + data);
                } else {
                    System.out.println("Not ready yet");
                }
            }
        }
    }
    
    public static void main(String[] args) {
        System.out.println("=== happens-before原则演示 ===\n");
        
        // 这里我们通过代码注释来说明happens-before的作用
        // 实际运行可能需要多次执行才能观察到并发问题
        
        System.out.println("1. 问题场景 - 没有happens-before保证:");
        System.out.println("   线程1: data=42; ready=true;");
        System.out.println("   线程2: if(ready) print(data);");
        System.out.println("   ❌ 可能输出: Not ready yet (即使数据已写入)");
        System.out.println("   ❌ 可能输出: Data: 0 (看到ready=true但data还是旧值)\n");
        
        System.out.println("2. 解决方案1 - volatile:");
        System.out.println("   volatile boolean ready;");
        System.out.println("   ✅ volatile写 happens-before volatile读");
        System.out.println("   ✅ 保证看到ready=true时，一定能看到data=42\n");
        
        System.out.println("3. 解决方案2 - synchronized:");
        System.out.println("   synchronized(lock) { data=42; ready=true; }");
        System.out.println("   synchronized(lock) { if(ready) print(data); }");
        System.out.println("   ✅ unlock happens-before lock");
        System.out.println("   ✅ 保证第二个同步块看到第一个同步块的所有操作\n");
        
        System.out.println("🎯 关键理解:");
        System.out.println("happens-before不是时间关系，而是可见性保证！");
        System.out.println("它告诉我们：如果A happens-before B，那么A的结果对B可见。");
    }
}

/*
 * happens-before规则快速记忆：
 * 
 * 1. 程序顺序：同线程内，前面的操作 happens-before 后面的操作
 * 2. volatile：写 happens-before 读
 * 3. 锁：unlock happens-before lock  
 * 4. 线程启动：start() happens-before 线程内的操作
 * 5. 线程终止：线程内操作 happens-before join()返回
 * 6. 传递性：A hb B, B hb C => A hb C
 * 
 * 记住：这些规则让我们不用关心CPU缓存细节，
 * 只要遵循规则就能写出正确的并发程序！
 */