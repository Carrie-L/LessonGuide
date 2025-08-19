# 🌱 ADHD-Friendly Android 面试准备系统 (5分钟每个任务)

> **💡 学习哲学**: 从初学者到资深工程师的渐进式成长路径  
> **🎯 目标**: 每个概念都要做到真正理解，而不是死记硬背  
> **🕐 节奏**: 5分钟专注 + 确认理解 + 逐步进阶
> **🔥 新增**: 强制性手动编程 - **No Copy-Paste Policy**

## 🎯 当前学习: 第一章 1.1 并发原语：synchronized, volatile 与 JMM

**学习方法说明**: 每个任务都包含 Primary(基础) → Intermediate(实践) → Senior(架构) 三个层次，确保你能从"知道"进展到"理解"再到"应用"。

**🚨 强制性编程框架**: 
- ✋ **严禁复制粘贴**: 所有代码必须手动输入，培养肌肉记忆
- 🏃 **Learn by Doing**: 每个概念必须通过编程验证
- 📊 **渐进复杂度**: 50行demo → 150行实现 → 300+行企业级系统

---

### 🌟 Level 1: Primary Foundation (基础打牢) - 20分钟

#### Task 1.1.1: JMM概念入门 + 首个编程实验 (5分钟) ⏰

**🎯 Primary Level (新手友好)**
- [✅] **什么是JMM**: 想象你有一个笔记本(工作内存)和一个公共图书馆(主内存)
- [✅] **简单理解**: JMM就是Java定义的"多个程序员如何共享数据"的规则
- [✅] **生活类比**: 就像办公室里多人共用一台打印机，需要排队规则一样
- [✅] **检查点**: 能说出"JMM是用来解决什么问题的"
- [ ] **文件**: 在`student_progress/`创建`jmm_notes.md`，用自己的话写下理解

**🚀 Hands-On Coding Exercise (强制编程练习)**
```java
// 练习目标: 亲眼看到"可见性问题"
public class MemoryVisibilityDemo {
    // 第一步: 创建一个简单的可见性问题演示
    private static boolean flag = false;    // 注意: 不使用volatile
    private static int counter = 0;
    
    public static void main(String[] args) throws InterruptedException {
        // TODO: 学生手动输入以下代码，观察现象
        
        // 线程1: 读取者 - 等待flag变为true
        Thread readerThread = new Thread(() -> {
            System.out.println("读取者: 开始等待flag变为true...");
            while (!flag) {
                // 空循环 - 可能永远等待下去!
            }
            System.out.println("读取者: 检测到flag为true, counter=" + counter);
        });
        
        // 线程2: 写入者 - 修改共享变量
        Thread writerThread = new Thread(() -> {
            try {
                Thread.sleep(1000);  // 等待1秒
                counter = 42;        // 先修改counter
                flag = true;         // 再修改flag
                System.out.println("写入者: 已设置flag=true, counter=" + counter);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        
        readerThread.start();
        writerThread.start();
        
        // 等待3秒，观察是否出现死循环
        Thread.sleep(3000);
        if (readerThread.isAlive()) {
            System.out.println("⚠️  JMM可见性问题重现! 读取者线程仍在等待");
            readerThread.interrupt();
        }
        
        writerThread.join();
    }
}
```

**📝 编程任务检查点**:
- [ ] **手动输入**: 完整代码必须手动输入，体验每一行的含义
- [ ] **运行验证**: 程序能运行并观察到可见性问题(读取者可能永远等待)
- [ ] **现象理解**: 理解为什么flag的修改对读取者不可见
- [ ] **代码位置**: `student_progress/JavaLearning/src/MemoryVisibilityDemo.java`

**🚀 Intermediate Level (实践验证)**  
- [ ] **深入概念**: 理解"线程工作内存"和"主内存"的数据同步机制
- [ ] **技术细节**: 学习缓存一致性、CPU缓存对Java程序的影响
- [ ] **检查点**: 能画出简单的JMM内存结构图

**🏆 Senior Level (架构思维)**
- [ ] **设计原理**: 理解为什么JVM要设计这样的内存模型
- [ ] **性能权衡**: CPU缓存 vs 数据一致性的trade-off
- [ ] **面试深度**: 能从JMM角度分析并发问题的根本原因

#### Task 1.1.2: happens-before原则 + 编程验证 (5分钟) ⏰  

**🎯 Primary Level (新手友好)**
- [✅] **什么是happens-before**: 想象一个接力赛，前一个跑步者的成绩，后一个跑步者一定能看到
- [✅] **简单理解**: "A发生在B之前"意味着"B能看到A的所有结果"
- [✅] **记忆技巧**: happens-before = "发生在前面" + "结果可见"
- [✅] **检查点**: 能用"接力赛"例子解释happens-before
- [ ] **文件**: 在`jmm_notes.md`中用生活例子写下理解

**🚀 Hands-On Coding Exercise (强制编程练习)**
```java
// 练习目标: 验证happens-before规则
public class HappensBeforeDemo {
    private static int sharedData = 0;
    private static boolean dataReady = false;
    
    public static void main(String[] args) throws InterruptedException {
        
        // 实验1: 无同步的数据竞争
        System.out.println("=== 实验1: 无同步情况 ===");
        testWithoutSynchronization();
        
        Thread.sleep(2000);
        
        // 实验2: 使用volatile保证happens-before
        System.out.println("\n=== 实验2: volatile同步情况 ===");
        testWithVolatile();
    }
    
    // TODO: 学生手动实现这两个方法
    private static void testWithoutSynchronization() throws InterruptedException {
        sharedData = 0;
        dataReady = false;
        
        // 写入者线程
        Thread writer = new Thread(() -> {
            sharedData = 42;      // 步骤1: 设置数据
            dataReady = true;     // 步骤2: 设置标志 (无happens-before保证)
            System.out.println("写入者: 数据已准备好");
        });
        
        // 读取者线程  
        Thread reader = new Thread(() -> {
            while (!dataReady) {  // 等待数据准备好
                Thread.yield();   // 让出CPU避免busy-wait
            }
            // 问题: 即使dataReady为true，sharedData可能还是0!
            System.out.println("读取者看到的数据: " + sharedData);
        });
        
        reader.start();
        writer.start();
        
        writer.join();
        reader.join();
    }
    
    private static void testWithVolatile() {
        // TODO: 学生修改上面的代码，给dataReady加上volatile
        // 观察volatile如何建立happens-before关系
    }
}
```

**📝 编程任务检查点**:
- [ ] **手动输入**: 必须手动输入代码，理解每一行的作用
- [ ] **对比实验**: 运行无同步版本，观察数据不一致问题
- [ ] **volatile修复**: 为dataReady添加volatile，观察问题解决
- [ ] **原理理解**: 理解volatile写-读如何建立happens-before关系

**🚀 Intermediate Level (实践验证)**  
- [ ] **技术定义**: 学习"如果A happens-before B，那么A的结果对B可见"
- [ ] **常见场景**: synchronized块、volatile写读、线程start/join
- [ ] **检查点**: 能列出3个具体的happens-before规则

**🏆 Senior Level (架构思维)**
- [ ] **内存语义**: 理解happens-before与内存可见性的关系
- [ ] **编译器优化**: 知道happens-before如何约束重排序
- [ ] **面试深度**: 能分析复杂并发场景的happens-before关系

#### Task 1.1.3: synchronized基本原理 + 线程安全实战 (5分钟) ⏰

**🎯 Primary Level (新手友好)**
- [✅] **什么是synchronized**: 想象一个厕所门锁，一次只能一个人进去
- [✅] **简单理解**: synchronized就是给代码加了一把锁，确保不会"撞车"
- [✅] **生活类比**: 就像银行只有一个窗口，大家要排队一个一个来
- [✅] **检查点**: 能说出"synchronized是用来防止什么问题的"
- [ ] **文件**: 在`student_progress/`创建`synchronized_notes.md`

**🚀 Hands-On Coding Exercise (强制编程练习)**
```java
// 练习目标: 先看到问题，再解决问题
public class SynchronizedDemo {
    private static int unsafeCounter = 0;      // 不安全的计数器
    private static int safeCounter = 0;        // 安全的计数器
    private static final Object lock = new Object();
    
    public static void main(String[] args) throws InterruptedException {
        final int THREAD_COUNT = 10;
        final int INCREMENT_COUNT = 1000;
        
        System.out.println("=== 步骤1: 展示数据竞争问题 ===");
        testUnsafeIncrement(THREAD_COUNT, INCREMENT_COUNT);
        
        System.out.println("\n=== 步骤2: synchronized解决方案 ===");
        testSafeIncrement(THREAD_COUNT, INCREMENT_COUNT);
        
        System.out.println("\n=== 步骤3: 性能对比分析 ===");
        performanceComparison();
    }
    
    // TODO: 学生手动实现不安全的递增操作
    private static void testUnsafeIncrement(int threadCount, int incrementCount) 
            throws InterruptedException {
        unsafeCounter = 0;
        Thread[] threads = new Thread[threadCount];
        
        long startTime = System.currentTimeMillis();
        
        // 创建多个线程同时对counter进行++操作
        for (int i = 0; i < threadCount; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < incrementCount; j++) {
                    unsafeCounter++;  // 线程不安全！可能丢失数据
                }
            });
            threads[i].start();
        }
        
        // 等待所有线程完成
        for (Thread thread : threads) {
            thread.join();
        }
        
        long endTime = System.currentTimeMillis();
        
        System.out.println("期望结果: " + (threadCount * incrementCount));
        System.out.println("实际结果: " + unsafeCounter);
        System.out.println("数据丢失: " + (threadCount * incrementCount - unsafeCounter));
        System.out.println("执行时间: " + (endTime - startTime) + "ms");
    }
    
    // TODO: 学生实现synchronized版本
    private static void testSafeIncrement(int threadCount, int incrementCount) 
            throws InterruptedException {
        // 实现线程安全的版本，使用synchronized
        safeCounter = 0;
        Thread[] threads = new Thread[threadCount];
        
        long startTime = System.currentTimeMillis();
        
        for (int i = 0; i < threadCount; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < incrementCount; j++) {
                    synchronized (lock) {    // 关键: 同步块
                        safeCounter++;       // 现在是线程安全的
                    }
                }
            });
            threads[i].start();
        }
        
        for (Thread thread : threads) {
            thread.join();
        }
        
        long endTime = System.currentTimeMillis();
        
        System.out.println("期望结果: " + (threadCount * incrementCount));
        System.out.println("实际结果: " + safeCounter);
        System.out.println("数据正确: " + (safeCounter == threadCount * incrementCount));
        System.out.println("执行时间: " + (endTime - startTime) + "ms");
    }
    
    // TODO: 学生实现性能对比
    private static void performanceComparison() {
        // 比较不同同步策略的性能差异
        // 1. 无同步 (不安全但快)
        // 2. synchronized代码块 
        // 3. synchronized方法
        // 4. 不同锁粒度的影响
    }
}
```

**📝 编程任务检查点**:
- [ ] **问题重现**: 能够运行代码并观察到数据丢失问题
- [ ] **解决方案**: 使用synchronized成功解决数据竞争
- [ ] **性能理解**: 观察同步带来的性能开销
- [ ] **多种方式**: 尝试synchronized方法 vs synchronized代码块

**🚀 Intermediate Level (实践验证)**  
- [ ] **技术细节**: 理解monitor机制，每个对象都有一个内置锁
- [ ] **使用方式**: 同步方法 vs 同步代码块的区别
- [ ] **检查点**: 能写出简单的synchronized代码示例

**🏆 Senior Level (架构思维)**
- [ ] **锁升级**: 偏向锁→轻量级锁→重量级锁的优化过程
- [ ] **性能考虑**: 锁粒度、锁竞争对性能的影响
- [ ] **面试深度**: 能分析synchronized的底层实现机制

#### Task 1.1.4: volatile基本原理 + 可见性验证 (5分钟) ⏰

**🎯 Primary Level (新手友好)**
- [✅] **什么是volatile**: 想象一个公告板，任何人更新后，所有人立即能看到
- [✅] **简单理解**: volatile确保变量的"最新消息"能被所有线程看到
- [✅] **常见错误**: volatile ≠ synchronized，它不能防止"撞车"
- [✅] **检查点**: 能说出"volatile解决什么问题，不解决什么问题"
- [ ] **文件**: 在`student_progress/`创建`volatile_notes.md`

**🚀 Hands-On Coding Exercise (强制编程练习)**
```java
// 练习目标: 对比volatile和非volatile的差异
public class VolatileDemo {
    
    // 实验1: 可见性问题演示
    private static boolean normalFlag = false;          // 普通变量
    private static volatile boolean volatileFlag = false;   // volatile变量
    
    // 实验2: 原子性问题演示  
    private static volatile int volatileCounter = 0;    // volatile不保证原子性
    
    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== 实验1: 可见性对比 ===");
        testVisibility();
        
        Thread.sleep(3000);
        
        System.out.println("\n=== 实验2: volatile不保证原子性 ===");
        testAtomicity();
    }
    
    // TODO: 学生手动实现可见性测试
    private static void testVisibility() throws InterruptedException {
        
        // 测试普通变量的可见性问题
        System.out.println("--- 测试普通变量 ---");
        normalFlag = false;
        
        Thread readerThread1 = new Thread(() -> {
            System.out.println("普通变量读取者: 开始等待...");
            while (!normalFlag) {
                // 可能永远循环下去！
            }
            System.out.println("普通变量读取者: 检测到flag变化!");
        });
        
        Thread writerThread1 = new Thread(() -> {
            try {
                Thread.sleep(1000);
                normalFlag = true;
                System.out.println("普通变量写入者: 已设置flag=true");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        
        readerThread1.start();
        writerThread1.start();
        
        // 等待2秒看是否有死循环
        Thread.sleep(2000);
        if (readerThread1.isAlive()) {
            System.out.println("⚠️  普通变量可见性问题: 读取者仍在等待");
            readerThread1.interrupt();
        }
        writerThread1.join();
        
        Thread.sleep(500);
        
        // 测试volatile变量
        System.out.println("--- 测试volatile变量 ---");
        volatileFlag = false;
        
        Thread readerThread2 = new Thread(() -> {
            System.out.println("volatile读取者: 开始等待...");
            while (!volatileFlag) {
                // volatile保证可见性，会正常退出
            }
            System.out.println("volatile读取者: 检测到flag变化!");
        });
        
        Thread writerThread2 = new Thread(() -> {
            try {
                Thread.sleep(1000);
                volatileFlag = true;
                System.out.println("volatile写入者: 已设置flag=true");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        
        readerThread2.start();
        writerThread2.start();
        readerThread2.join();
        writerThread2.join();
    }
    
    // TODO: 学生实现原子性测试
    private static void testAtomicity() throws InterruptedException {
        final int THREAD_COUNT = 10;
        final int INCREMENT_COUNT = 1000;
        
        volatileCounter = 0;
        Thread[] threads = new Thread[THREAD_COUNT];
        
        for (int i = 0; i < THREAD_COUNT; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < INCREMENT_COUNT; j++) {
                    volatileCounter++;  // volatile不保证原子性！
                }
            });
            threads[i].start();
        }
        
        for (Thread thread : threads) {
            thread.join();
        }
        
        System.out.println("期望结果: " + (THREAD_COUNT * INCREMENT_COUNT));
        System.out.println("实际结果: " + volatileCounter);
        System.out.println("数据丢失: " + (THREAD_COUNT * INCREMENT_COUNT - volatileCounter));
        System.out.println("结论: volatile保证可见性，但不保证原子性");
    }
}
```

**📝 编程任务检查点**:
- [ ] **可见性验证**: 观察普通变量的可见性问题，volatile的解决效果
- [ ] **原子性问题**: 验证volatile在++操作中仍然会丢失数据
- [ ] **场景理解**: 理解volatile适用于纯赋值，不适用于复合操作
- [ ] **代码位置**: `student_progress/JavaLearning/src/VolatileDemo.java`

**🚀 Intermediate Level (实践验证)**  
- [ ] **两个作用**: 保证可见性 + 禁止指令重排序
- [ ] **经典场景**: 单例模式双重检查锁定
- [ ] **检查点**: 能解释为什么volatile不能保证原子性

**🏆 Senior Level (架构思维)**
- [ ] **内存屏障**: volatile如何通过内存屏障实现语义
- [ ] **性能特点**: volatile vs synchronized的性能对比
- [ ] **面试深度**: 能分析volatile在并发框架中的应用

### 🚀 Level 2: Intermediate Practice (实践深入) - 15分钟

#### Task 1.1.5: 锁升级机制理解 + JVM参数实验 (5分钟) ⏰

**🎯 Primary Level (新手友好)**
- [ ] **偏向锁类比**: 想象你的专属停车位，第一次停车后就贴上你的名字
- [ ] **简单理解**: 如果只有一个线程在用锁，JVM就"偏向"给它，减少开销
- [ ] **记忆技巧**: 偏向 = "偏心"，第一个线程会被"偏爱"
- [ ] **检查点**: 能说出"偏向锁适用于什么场景"

**🚀 Hands-On Coding Exercise (强制编程练习)**
```java
// 练习目标: 观察锁升级的实际过程
public class LockEscalationDemo {
    private final Object monitor = new Object();
    private static final int ITERATIONS = 1000000;
    
    public static void main(String[] args) throws InterruptedException {
        LockEscalationDemo demo = new LockEscalationDemo();
        
        System.out.println("=== 锁升级观察实验 ===");
        System.out.println("JVM参数: 运行时添加 -XX:+PrintGCDetails -XX:+TraceBiasedLocking");
        
        // 实验1: 单线程场景 - 偏向锁
        demo.testBiasedLock();
        
        Thread.sleep(1000);
        
        // 实验2: 轻度竞争 - 轻量级锁
        demo.testLightweightLock();
        
        Thread.sleep(1000);
        
        // 实验3: 激烈竞争 - 重量级锁
        demo.testHeavyweightLock();
    }
    
    // TODO: 学生实现偏向锁测试
    private void testBiasedLock() {
        System.out.println("\n--- 实验1: 偏向锁场景 (单线程) ---");
        
        long startTime = System.nanoTime();
        
        // 单线程重复获取同一个锁
        for (int i = 0; i < ITERATIONS; i++) {
            synchronized (monitor) {
                // 空操作，专注观察锁的开销
            }
        }
        
        long endTime = System.nanoTime();
        long duration = endTime - startTime;
        
        System.out.println("单线程执行时间: " + duration / 1000000 + "ms");
        System.out.println("平均每次加锁: " + duration / ITERATIONS + "ns");
        System.out.println("预期: 偏向锁应该有很低的开销");
    }
    
    // TODO: 学生实现轻量级锁测试
    private void testLightweightLock() throws InterruptedException {
        System.out.println("\n--- 实验2: 轻量级锁场景 (交替访问) ---");
        
        final Object sharedMonitor = new Object();
        final int[] sharedCounter = {0};
        
        Thread thread1 = new Thread(() -> {
            for (int i = 0; i < ITERATIONS / 2; i++) {
                synchronized (sharedMonitor) {
                    sharedCounter[0]++;
                }
                // 小延迟，让另一个线程有机会获取锁
                if (i % 1000 == 0) {
                    Thread.yield();
                }
            }
        });
        
        Thread thread2 = new Thread(() -> {
            for (int i = 0; i < ITERATIONS / 2; i++) {
                synchronized (sharedMonitor) {
                    sharedCounter[0]++;
                }
                if (i % 1000 == 0) {
                    Thread.yield();
                }
            }
        });
        
        long startTime = System.nanoTime();
        
        thread1.start();
        thread2.start();
        thread1.join();
        thread2.join();
        
        long endTime = System.nanoTime();
        
        System.out.println("双线程执行时间: " + (endTime - startTime) / 1000000 + "ms");
        System.out.println("共享计数器: " + sharedCounter[0]);
        System.out.println("预期: 轻量级锁通过CAS自旋，开销中等");
    }
    
    // TODO: 学生实现重量级锁测试
    private void testHeavyweightLock() throws InterruptedException {
        System.out.println("\n--- 实验3: 重量级锁场景 (激烈竞争) ---");
        
        final Object contentionMonitor = new Object();
        final int THREAD_COUNT = 10;
        final int[] sharedCounter = {0};
        
        Thread[] threads = new Thread[THREAD_COUNT];
        
        long startTime = System.nanoTime();
        
        for (int i = 0; i < THREAD_COUNT; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < ITERATIONS / THREAD_COUNT; j++) {
                    synchronized (contentionMonitor) {
                        sharedCounter[0]++;
                        // 模拟一些工作，增加锁持有时间
                        try {
                            Thread.sleep(0, 100); // 0.1微秒
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    }
                }
            });
            threads[i].start();
        }
        
        for (Thread thread : threads) {
            thread.join();
        }
        
        long endTime = System.nanoTime();
        
        System.out.println("多线程激烈竞争执行时间: " + (endTime - startTime) / 1000000 + "ms");
        System.out.println("共享计数器: " + sharedCounter[0]);
        System.out.println("预期: 重量级锁涉及系统调用，开销最大");
        
        // 性能总结
        System.out.println("\n=== 锁升级性能总结 ===");
        System.out.println("偏向锁: 适用于单线程重复获取，开销最小");
        System.out.println("轻量级锁: 适用于轻度竞争，CAS自旋");
        System.out.println("重量级锁: 适用于激烈竞争，系统调用阻塞/唤醒");
    }
}
```

**📝 编程任务检查点**:
- [ ] **手动输入**: 完整实现三种锁场景的测试代码
- [ ] **性能对比**: 观察不同锁状态下的执行时间差异
- [ ] **JVM参数**: 运行时添加JVM参数观察锁状态变化
- [ ] **原理理解**: 理解为什么不同场景会触发不同的锁升级

**🚀 Intermediate Level (实践验证)**  
- [ ] **技术细节**: 对象头中的偏向锁标记位，线程ID记录
- [ ] **失效条件**: 多线程竞争时偏向锁会撤销
- [ ] **检查点**: 能说出偏向锁失效的3种情况

**🏆 Senior Level (架构思维)**
- [ ] **设计权衡**: 偏向锁的CAS开销 vs 频繁加锁的开销
- [ ] **JVM参数**: -XX:+UseBiasedLocking的影响
- [ ] **面试深度**: 能分析偏向锁在高并发场景下的适用性

#### Task 1.1.6: 轻量级锁与CAS深度实践 (5分钟) ⏰

**🎯 Primary Level (新手友好)**
- [ ] **轻量级锁类比**: 想象排队买奶茶，先"试试看"能不能马上买到
- [ ] **CAS理解**: "Compare And Swap" = "比较一下，如果没变就替换"
- [ ] **自旋概念**: 就像一直按电梯按钮，希望电梯快点来
- [ ] **检查点**: 能说出"为什么叫轻量级锁"

**🚀 Hands-On Coding Exercise (强制编程练习)**
```java
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

// 练习目标: 深入理解CAS机制和ABA问题
public class CASandLightweightLockDemo {
    
    // 实验1: 手写CAS操作模拟
    private static class SimpleCAS {
        private volatile int value = 0;
        
        // 模拟CAS操作 (实际的CAS是原子的硬件指令)
        public synchronized boolean compareAndSet(int expect, int update) {
            if (value == expect) {
                value = update;
                return true;  // CAS成功
            }
            return false;     // CAS失败
        }
        
        public int getValue() {
            return value;
        }
    }
    
    // 实验2: ABA问题演示
    private static class ABADemo {
        private AtomicReference<String> reference = new AtomicReference<>("A");
        
        public void demonstrateABAProblem() throws InterruptedException {
            System.out.println("=== ABA问题演示 ===");
            System.out.println("初始值: " + reference.get());
            
            // 线程1: 想要把A改成B，但被延迟了
            Thread thread1 = new Thread(() -> {
                String original = reference.get();  // 读取到A
                System.out.println("线程1: 读取到值 = " + original);
                
                try {
                    Thread.sleep(1000);  // 模拟延迟
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                
                // 尝试CAS: A -> B
                boolean success = reference.compareAndSet(original, "B");
                System.out.println("线程1: CAS A->B " + (success ? "成功" : "失败"));
                System.out.println("线程1: 当前值 = " + reference.get());
            });
            
            // 线程2: 快速执行 A->C->A 的操作
            Thread thread2 = new Thread(() -> {
                try {
                    Thread.sleep(200);  // 稍等一下
                    
                    // A -> C
                    boolean step1 = reference.compareAndSet("A", "C");
                    System.out.println("线程2: CAS A->C " + (step1 ? "成功" : "失败"));
                    
                    Thread.sleep(300);
                    
                    // C -> A (恢复原值)
                    boolean step2 = reference.compareAndSet("C", "A");
                    System.out.println("线程2: CAS C->A " + (step2 ? "成功" : "失败"));
                    System.out.println("线程2: 已将值恢复为A");
                    
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
            
            thread1.start();
            thread2.start();
            thread1.join();
            thread2.join();
            
            System.out.println("最终值: " + reference.get());
            System.out.println("问题: 线程1不知道值被改动过!");
        }
    }
    
    public static void main(String[] args) throws InterruptedException {
        
        // 实验1: 基础CAS操作
        System.out.println("=== 实验1: CAS基础操作 ===");
        testBasicCAS();
        
        Thread.sleep(1000);
        
        // 实验2: CAS自旋模拟轻量级锁
        System.out.println("\n=== 实验2: CAS自旋机制 ===");
        testCASSpinning();
        
        Thread.sleep(1000);
        
        // 实验3: ABA问题
        System.out.println("\n=== 实验3: ABA问题 ===");
        ABADemo abaDemo = new ABADemo();
        abaDemo.demonstrateABAProblem();
        
        Thread.sleep(1000);
        
        // 实验4: 原子类vs synchronized性能对比
        System.out.println("\n=== 实验4: 性能对比 ===");
        performanceComparison();
    }
    
    // TODO: 学生实现基础CAS测试
    private static void testBasicCAS() throws InterruptedException {
        SimpleCAS cas = new SimpleCAS();
        final int THREAD_COUNT = 5;
        final int ATTEMPTS_PER_THREAD = 1000;
        
        Thread[] threads = new Thread[THREAD_COUNT];
        
        for (int i = 0; i < THREAD_COUNT; i++) {
            final int threadId = i;
            threads[i] = new Thread(() -> {
                int successCount = 0;
                int failureCount = 0;
                
                for (int j = 0; j < ATTEMPTS_PER_THREAD; j++) {
                    int currentValue = cas.getValue();
                    boolean success = cas.compareAndSet(currentValue, currentValue + 1);
                    
                    if (success) {
                        successCount++;
                    } else {
                        failureCount++;
                        // CAS失败，重试
                        j--; // 重试同一次操作
                    }
                }
                
                System.out.println("线程" + threadId + ": 成功=" + successCount + ", 失败=" + failureCount);
            });
            threads[i].start();
        }
        
        for (Thread thread : threads) {
            thread.join();
        }
        
        System.out.println("最终计数器值: " + cas.getValue());
        System.out.println("期望值: " + (THREAD_COUNT * ATTEMPTS_PER_THREAD));
    }
    
    // TODO: 学生实现CAS自旋模拟
    private static void testCASSpinning() throws InterruptedException {
        AtomicInteger atomicCounter = new AtomicInteger(0);
        final int THREAD_COUNT = 3;
        final int TARGET_VALUE = 10000;
        
        Thread[] threads = new Thread[THREAD_COUNT];
        
        for (int i = 0; i < THREAD_COUNT; i++) {
            final int threadId = i;
            threads[i] = new Thread(() -> {
                int spinCount = 0;
                
                while (atomicCounter.get() < TARGET_VALUE) {
                    int currentValue = atomicCounter.get();
                    
                    if (currentValue >= TARGET_VALUE) {
                        break;  // 已经达到目标
                    }
                    
                    // 尝试CAS递增
                    boolean success = atomicCounter.compareAndSet(currentValue, currentValue + 1);
                    
                    if (!success) {
                        spinCount++;  // 记录自旋次数
                    }
                    
                    // 模拟轻量级锁的自旋限制
                    if (spinCount > 100) {
                        Thread.yield();  // 让出CPU
                        spinCount = 0;
                    }
                }
                
                System.out.println("线程" + threadId + ": 自旋次数=" + spinCount);
            });
            threads[i].start();
        }
        
        for (Thread thread : threads) {
            thread.join();
        }
        
        System.out.println("原子计数器最终值: " + atomicCounter.get());
    }
    
    // TODO: 学生实现性能对比
    private static void performanceComparison() throws InterruptedException {
        final int THREAD_COUNT = 10;
        final int OPERATIONS = 100000;
        
        // 测试1: AtomicInteger (CAS)
        AtomicInteger atomicInt = new AtomicInteger(0);
        long startTime = System.nanoTime();
        
        Thread[] atomicThreads = new Thread[THREAD_COUNT];
        for (int i = 0; i < THREAD_COUNT; i++) {
            atomicThreads[i] = new Thread(() -> {
                for (int j = 0; j < OPERATIONS; j++) {
                    atomicInt.incrementAndGet();  // CAS操作
                }
            });
            atomicThreads[i].start();
        }
        
        for (Thread thread : atomicThreads) {
            thread.join();
        }
        
        long atomicTime = System.nanoTime() - startTime;
        
        // 测试2: synchronized
        int[] syncCounter = {0};
        Object lock = new Object();
        
        startTime = System.nanoTime();
        
        Thread[] syncThreads = new Thread[THREAD_COUNT];
        for (int i = 0; i < THREAD_COUNT; i++) {
            syncThreads[i] = new Thread(() -> {
                for (int j = 0; j < OPERATIONS; j++) {
                    synchronized (lock) {
                        syncCounter[0]++;  // synchronized操作
                    }
                }
            });
            syncThreads[i].start();
        }
        
        for (Thread thread : syncThreads) {
            thread.join();
        }
        
        long syncTime = System.nanoTime() - startTime;
        
        System.out.println("AtomicInteger (CAS): " + atomicTime / 1000000 + "ms, 结果=" + atomicInt.get());
        System.out.println("Synchronized: " + syncTime / 1000000 + "ms, 结果=" + syncCounter[0]);
        System.out.println("性能比: " + (double) syncTime / atomicTime + "x");
    }
}
```

**📝 编程任务检查点**:
- [ ] **CAS理解**: 理解比较交换的原子性和重试机制
- [ ] **ABA问题**: 观察和理解ABA问题的产生原因
- [ ] **自旋机制**: 实现自旋重试和自旋次数限制
- [ ] **性能对比**: 对比CAS vs synchronized的性能差异

**🚀 Intermediate Level (实践验证)**  
- [ ] **技术细节**: 栈帧中的Lock Record，CAS操作的原子性
- [ ] **自旋次数**: 默认自旋次数和适应性自旋
- [ ] **检查点**: 能解释CAS的ABA问题

**🏆 Senior Level (架构思维)**
- [ ] **性能考量**: 自旋 vs 阻塞的CPU开销对比
- [ ] **硬件支持**: CAS指令的底层实现机制
- [ ] **面试深度**: 能分析什么情况下轻量级锁会退化

#### Task 1.1.7: 重量级锁与系统调用实验 (5分钟) ⏰

**🎯 Primary Level (新手友好)**
- [ ] **重量级锁类比**: 想象去银行办事，需要取号排队，涉及保安管理
- [ ] **用户态vs内核态**: 用户态=家里，内核态=政府部门，切换需要"手续"
- [ ] **阻塞唤醒**: 就像排队时可以坐下休息，叫号时再起来
- [ ] **检查点**: 能说出"为什么叫重量级锁"

**🚀 Hands-On Coding Exercise (强制编程练习)**
```java
import java.util.concurrent.locks.LockSupport;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;

// 练习目标: 观察重量级锁的系统调用开销
public class HeavyweightLockDemo {
    
    private static final Object heavyLock = new Object();
    private static volatile boolean running = true;
    
    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== 重量级锁与系统调用实验 ===");
        
        // 实验1: 观察线程状态变化
        demonstrateThreadStates();
        
        Thread.sleep(2000);
        
        // 实验2: 用户态vs内核态切换开销
        measureContextSwitchOverhead();
        
        Thread.sleep(2000);
        
        // 实验3: 等待队列和同步队列
        demonstrateWaitingQueues();
    }
    
    // TODO: 学生实现线程状态观察
    private static void demonstrateThreadStates() throws InterruptedException {
        System.out.println("\n--- 实验1: 观察线程状态变化 ---");
        
        Thread lockHolder = new Thread(() -> {
            synchronized (heavyLock) {
                System.out.println("锁持有者: 获得锁，开始长时间工作...");
                try {
                    Thread.sleep(3000);  // 持有锁3秒
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                System.out.println("锁持有者: 工作完成，释放锁");
            }
        }, "LockHolder");
        
        Thread lockWaiter1 = new Thread(() -> {
            System.out.println("等待者1: 尝试获取锁...");
            synchronized (heavyLock) {
                System.out.println("等待者1: 获得锁!");
            }
        }, "LockWaiter1");
        
        Thread lockWaiter2 = new Thread(() -> {
            System.out.println("等待者2: 尝试获取锁...");
            synchronized (heavyLock) {
                System.out.println("等待者2: 获得锁!");
            }
        }, "LockWaiter2");
        
        // 状态监控线程
        Thread stateMonitor = new Thread(() -> {
            try {
                while (running) {
                    System.out.println("\n--- 线程状态快照 ---");
                    System.out.println("LockHolder: " + lockHolder.getState());
                    System.out.println("LockWaiter1: " + lockWaiter1.getState());
                    System.out.println("LockWaiter2: " + lockWaiter2.getState());
                    Thread.sleep(500);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }, "StateMonitor");
        
        // 启动实验
        lockHolder.start();
        Thread.sleep(100);  // 确保lockHolder先获得锁
        
        lockWaiter1.start();
        lockWaiter2.start();
        stateMonitor.start();
        
        // 等待所有线程完成
        lockHolder.join();
        lockWaiter1.join();
        lockWaiter2.join();
        running = false;
        stateMonitor.join();
    }
    
    // TODO: 学生实现上下文切换开销测量
    private static void measureContextSwitchOverhead() throws InterruptedException {
        System.out.println("\n--- 实验2: 上下文切换开销测量 ---");
        
        final int ITERATIONS = 10000;
        ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
        
        // 测试1: 重量级锁的阻塞/唤醒开销
        long startTime = System.nanoTime();
        long startCpuTime = threadMXBean.getCurrentThreadCpuTime();
        
        Object contendedLock = new Object();
        volatile boolean shouldStop = false;
        
        // 创建一个持续竞争的场景
        Thread contender1 = new Thread(() -> {
            while (!shouldStop) {
                synchronized (contendedLock) {
                    // 短暂持有锁
                    try {
                        Thread.sleep(1);  // 强制线程阻塞
                    } catch (InterruptedException e) {
                        break;
                    }
                }
            }
        });
        
        Thread contender2 = new Thread(() -> {
            while (!shouldStop) {
                synchronized (contendedLock) {
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        break;
                    }
                }
            }
        });
        
        contender1.start();
        contender2.start();
        
        // 让竞争持续1秒
        Thread.sleep(1000);
        shouldStop = true;
        
        contender1.join();
        contender2.join();
        
        long endTime = System.nanoTime();
        long endCpuTime = threadMXBean.getCurrentThreadCpuTime();
        
        long wallClockTime = endTime - startTime;
        long cpuTime = endCpuTime - startCpuTime;
        
        System.out.println("重量级锁竞争:");
        System.out.println("  墙钟时间: " + wallClockTime / 1000000 + "ms");
        System.out.println("  CPU时间: " + cpuTime / 1000000 + "ms");
        System.out.println("  阻塞时间: " + (wallClockTime - cpuTime) / 1000000 + "ms");
        System.out.println("  系统调用比例: " + ((double)(wallClockTime - cpuTime) / wallClockTime * 100) + "%");
        
        // 测试2: 对比轻量级锁(无竞争)的开销
        System.out.println("\n对比: 无竞争锁开销");
        Object uncontendedLock = new Object();
        
        startTime = System.nanoTime();
        
        for (int i = 0; i < ITERATIONS; i++) {
            synchronized (uncontendedLock) {
                // 空操作
            }
        }
        
        endTime = System.nanoTime();
        
        System.out.println("无竞争锁平均开销: " + (endTime - startTime) / ITERATIONS + "ns");
    }
    
    // TODO: 学生实现等待队列演示
    private static void demonstrateWaitingQueues() throws InterruptedException {
        System.out.println("\n--- 实验3: 等待队列机制 ---");
        
        Object monitor = new Object();
        final int WAITER_COUNT = 5;
        
        // 创建多个等待者
        Thread[] waiters = new Thread[WAITER_COUNT];
        
        for (int i = 0; i < WAITER_COUNT; i++) {
            final int waiterId = i;
            waiters[i] = new Thread(() -> {
                synchronized (monitor) {
                    System.out.println("等待者" + waiterId + ": 进入同步块");
                    try {
                        System.out.println("等待者" + waiterId + ": 调用wait()进入等待队列");
                        monitor.wait();  // 进入等待队列
                        System.out.println("等待者" + waiterId + ": 被唤醒，重新获得锁");
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            }, "Waiter" + i);
        }
        
        // 启动所有等待者
        for (Thread waiter : waiters) {
            waiter.start();
            Thread.sleep(100);  // 确保按顺序进入等待
        }
        
        Thread.sleep(1000);
        
        // 唤醒线程
        Thread notifier = new Thread(() -> {
            synchronized (monitor) {
                System.out.println("\n唤醒者: 开始逐个唤醒等待的线程");
                
                for (int i = 0; i < WAITER_COUNT; i++) {
                    System.out.println("唤醒者: 调用notify()唤醒一个线程");
                    monitor.notify();  // 从等待队列唤醒一个线程到同步队列
                    
                    try {
                        Thread.sleep(500);  // 给被唤醒的线程一些执行时间
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        }, "Notifier");
        
        notifier.start();
        
        // 等待所有线程完成
        for (Thread waiter : waiters) {
            waiter.join();
        }
        notifier.join();
        
        System.out.println("\n总结:");
        System.out.println("1. 等待队列: 调用wait()的线程进入，释放锁");
        System.out.println("2. 同步队列: 等待获取锁的线程排队");
        System.out.println("3. notify(): 从等待队列移动一个线程到同步队列");
        System.out.println("4. 重量级锁涉及内核态系统调用，开销大");
    }
}
```

**📝 编程任务检查点**:
- [ ] **线程状态**: 观察BLOCKED、WAITING、RUNNABLE状态的变化
- [ ] **系统开销**: 测量上下文切换和系统调用的时间开销
- [ ] **队列机制**: 理解等待队列和同步队列的区别
- [ ] **代码位置**: `student_progress/JavaLearning/src/HeavyweightLockDemo.java`

**🚀 Intermediate Level (实践验证)**  
- [ ] **技术细节**: Monitor对象，等待队列和同步队列
- [ ] **系统开销**: 用户态和内核态切换的性能成本
- [ ] **检查点**: 能画出锁升级的完整流程图

**🏆 Senior Level (架构思维)**
- [ ] **升级时机**: JVM何时决定升级为重量级锁
- [ ] **优化策略**: 锁粗化、锁消除等编译器优化
- [ ] **面试深度**: 能分析不同锁状态下的性能特征

### 🏆 Level 3: Senior Application (架构应用) - 30分钟

**学习说明**: 这个阶段要求你**亲手编码**，通过实际运行代码来验证理论知识。记住：**Learn by Doing** - 只有动手才能真正理解！

#### Task 1.1.8: 企业级线程安全组件设计 (10分钟) ⏰

**🎯 综合项目目标**: 设计一个生产级的线程安全缓存系统
- **代码规模**: 200-300行
- **技术要求**: 使用所有学过的并发原语
- **业务场景**: 高并发Web应用的数据缓存

**🚀 Hands-On Coding Exercise (强制编程练习)**
```java
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

// 练习目标: 设计企业级线程安全缓存组件
public class EnterpriseThreadSafeCacheSystem {
    
    /**
     * 高性能线程安全缓存实现
     * 技术特性:
     * 1. 使用ConcurrentHashMap作为存储结构
     * 2. 读写锁提升读操作性能 
     * 3. 原子计数器统计缓存指标
     * 4. volatile保证配置可见性
     * 5. synchronized保证批量操作原子性
     */
    public static class HighPerformanceCache<K, V> {
        
        // 核心存储: 线程安全的HashMap
        private final ConcurrentHashMap<K, CacheEntry<V>> cache;
        
        // 读写锁: 读操作并发，写操作互斥
        private final ReadWriteLock rwLock = new ReentrantReadWriteLock();
        
        // 原子计数器: 线程安全的统计信息
        private final AtomicLong hitCount = new AtomicLong(0);
        private final AtomicLong missCount = new AtomicLong(0);
        private final AtomicLong evictionCount = new AtomicLong(0);
        
        // volatile配置: 保证可见性
        private volatile int maxSize;
        private volatile long defaultTtl;
        
        // 定时清理服务
        private final ScheduledExecutorService cleanupService;
        
        // 缓存条目
        private static class CacheEntry<V> {
            final V value;
            final long createTime;
            final long ttl;
            volatile long lastAccessTime;  // volatile保证访问时间可见性
            
            CacheEntry(V value, long ttl) {
                this.value = value;
                this.createTime = System.currentTimeMillis();
                this.ttl = ttl;
                this.lastAccessTime = createTime;
            }
            
            boolean isExpired() {
                return ttl > 0 && (System.currentTimeMillis() - createTime) > ttl;
            }
            
            void updateAccessTime() {
                this.lastAccessTime = System.currentTimeMillis();
            }
        }
        
        public HighPerformanceCache(int maxSize, long defaultTtl) {
            this.cache = new ConcurrentHashMap<>(maxSize);
            this.maxSize = maxSize;
            this.defaultTtl = defaultTtl;
            this.cleanupService = Executors.newSingleThreadScheduledExecutor(r -> {
                Thread t = new Thread(r, "CacheCleanup");
                t.setDaemon(true);  // 守护线程
                return t;
            });
            
            // 启动定时清理任务
            startCleanupTask();
        }
        
        // TODO: 学生实现线程安全的put操作
        public V put(K key, V value) {
            return put(key, value, defaultTtl);
        }
        
        public V put(K key, V value, long ttl) {
            if (key == null || value == null) {
                throw new IllegalArgumentException("Key and value cannot be null");
            }
            
            // 检查容量限制 (需要同步保证一致性)
            synchronized (this) {  // 使用synchronized保证原子性
                if (cache.size() >= maxSize && !cache.containsKey(key)) {
                    evictLRUEntry();  // 驱逐最少使用的条目
                }
            }
            
            CacheEntry<V> newEntry = new CacheEntry<>(value, ttl);
            CacheEntry<V> oldEntry = cache.put(key, newEntry);
            
            return oldEntry != null ? oldEntry.value : null;
        }
        
        // TODO: 学生实现读写锁优化的get操作
        public V get(K key) {
            if (key == null) {
                return null;
            }
            
            // 使用读锁进行并发读取
            rwLock.readLock().lock();
            try {
                CacheEntry<V> entry = cache.get(key);
                
                if (entry == null) {
                    missCount.incrementAndGet();  // 原子递增
                    return null;
                }
                
                if (entry.isExpired()) {
                    // 升级为写锁移除过期条目
                    rwLock.readLock().unlock();
                    rwLock.writeLock().lock();
                    try {
                        // 双重检查: 防止其他线程已经移除
                        entry = cache.get(key);
                        if (entry != null && entry.isExpired()) {
                            cache.remove(key);
                            evictionCount.incrementAndGet();
                        }
                        missCount.incrementAndGet();
                        return null;
                    } finally {
                        rwLock.writeLock().unlock();
                    }
                } else {
                    entry.updateAccessTime();  // volatile写操作
                    hitCount.incrementAndGet();  // 原子递增
                    return entry.value;
                }
                
            } finally {
                if (rwLock.readLock().tryLock()) {
                    rwLock.readLock().unlock();
                }
            }
        }
        
        // TODO: 学生实现LRU驱逐算法
        private void evictLRUEntry() {
            if (cache.isEmpty()) return;
            
            K lruKey = null;
            long oldestTime = Long.MAX_VALUE;
            
            // 找到最少使用的条目
            for (ConcurrentHashMap.Entry<K, CacheEntry<V>> entry : cache.entrySet()) {
                long accessTime = entry.getValue().lastAccessTime;
                if (accessTime < oldestTime) {
                    oldestTime = accessTime;
                    lruKey = entry.getKey();
                }
            }
            
            if (lruKey != null) {
                cache.remove(lruKey);
                evictionCount.incrementAndGet();
            }
        }
        
        // TODO: 学生实现定时清理任务
        private void startCleanupTask() {
            cleanupService.scheduleAtFixedRate(() -> {
                try {
                    rwLock.writeLock().lock();
                    try {
                        // 移除所有过期条目
                        cache.entrySet().removeIf(entry -> {
                            if (entry.getValue().isExpired()) {
                                evictionCount.incrementAndGet();
                                return true;
                            }
                            return false;
                        });
                    } finally {
                        rwLock.writeLock().unlock();
                    }
                } catch (Exception e) {
                    System.err.println("缓存清理异常: " + e.getMessage());
                }
            }, 60, 60, TimeUnit.SECONDS);  // 每分钟清理一次
        }
        
        // TODO: 学生实现批量操作 (需要synchronized保证原子性)
        public synchronized void putAll(java.util.Map<K, V> map) {
            for (java.util.Map.Entry<K, V> entry : map.entrySet()) {
                put(entry.getKey(), entry.getValue());
            }
        }
        
        public synchronized void clear() {
            cache.clear();
            // 重置统计计数器
            hitCount.set(0);
            missCount.set(0);
            evictionCount.set(0);
        }
        
        // 线程安全的统计信息获取
        public CacheStats getStats() {
            long hits = hitCount.get();
            long misses = missCount.get();
            long evictions = evictionCount.get();
            double hitRate = hits + misses > 0 ? (double) hits / (hits + misses) : 0.0;
            
            return new CacheStats(hits, misses, evictions, cache.size(), hitRate);
        }
        
        public void shutdown() {
            cleanupService.shutdown();
        }
    }
    
    // 统计信息类
    public static class CacheStats {
        public final long hitCount;
        public final long missCount;
        public final long evictionCount;
        public final int currentSize;
        public final double hitRate;
        
        CacheStats(long hitCount, long missCount, long evictionCount, int currentSize, double hitRate) {
            this.hitCount = hitCount;
            this.missCount = missCount;
            this.evictionCount = evictionCount;
            this.currentSize = currentSize;
            this.hitRate = hitRate;
        }
        
        @Override
        public String toString() {
            return String.format(
                "CacheStats{hits=%d, misses=%d, evictions=%d, size=%d, hitRate=%.2f%%}",
                hitCount, missCount, evictionCount, currentSize, hitRate * 100
            );
        }
    }
    
    // TODO: 学生实现并发压力测试
    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== 企业级线程安全缓存系统测试 ===");
        
        HighPerformanceCache<String, String> cache = new HighPerformanceCache<>(1000, 5000);
        
        // 并发测试
        final int THREAD_COUNT = 20;
        final int OPERATIONS_PER_THREAD = 1000;
        
        Thread[] threads = new Thread[THREAD_COUNT];
        
        // 创建并发读写测试
        for (int i = 0; i < THREAD_COUNT; i++) {
            final int threadId = i;
            threads[i] = new Thread(() -> {
                for (int j = 0; j < OPERATIONS_PER_THREAD; j++) {
                    String key = "key_" + (threadId * OPERATIONS_PER_THREAD + j);
                    String value = "value_" + threadId + "_" + j;
                    
                    // 写操作
                    cache.put(key, value);
                    
                    // 读操作
                    String retrieved = cache.get(key);
                    
                    // 验证数据一致性
                    if (!value.equals(retrieved)) {
                        System.err.println("数据不一致! 期望: " + value + ", 实际: " + retrieved);
                    }
                    
                    // 随机读取已存在的数据
                    if (j > 100) {
                        String randomKey = "key_" + (threadId * OPERATIONS_PER_THREAD + j - 50);
                        cache.get(randomKey);
                    }
                }
            }, "TestThread-" + i);
        }
        
        long startTime = System.currentTimeMillis();
        
        // 启动所有测试线程
        for (Thread thread : threads) {
            thread.start();
        }
        
        // 等待所有线程完成
        for (Thread thread : threads) {
            thread.join();
        }
        
        long endTime = System.currentTimeMillis();
        
        // 输出测试结果
        System.out.println("并发测试完成!");
        System.out.println("总操作数: " + (THREAD_COUNT * OPERATIONS_PER_THREAD * 2)); // 读+写
        System.out.println("执行时间: " + (endTime - startTime) + "ms");
        System.out.println("缓存统计: " + cache.getStats());
        
        // 测试缓存功能
        testCacheFeatures(cache);
        
        cache.shutdown();
    }
    
    private static void testCacheFeatures(HighPerformanceCache<String, String> cache) throws InterruptedException {
        System.out.println("\n=== 缓存功能测试 ===");
        
        // 测试TTL过期
        cache.put("ttl_test", "will_expire", 1000);  // 1秒后过期
        System.out.println("TTL测试 - 立即读取: " + cache.get("ttl_test"));
        
        Thread.sleep(1100);  // 等待过期
        System.out.println("TTL测试 - 过期后读取: " + cache.get("ttl_test"));
        
        // 测试批量操作
        java.util.Map<String, String> batch = new java.util.HashMap<>();
        batch.put("batch1", "value1");
        batch.put("batch2", "value2");
        batch.put("batch3", "value3");
        cache.putAll(batch);
        
        System.out.println("批量操作测试: " + cache.get("batch2"));
        
        System.out.println("最终统计: " + cache.getStats());
    }
}
```

**📝 编程任务检查点**:
- [ ] **架构设计**: 使用合适的并发原语解决不同场景的问题
- [ ] **性能优化**: 读写锁提升读性能，原子计数器减少锁竞争
- [ ] **数据一致性**: 在高并发场景下保证数据正确性
- [ ] **功能完整性**: TTL过期、LRU驱逐、统计监控等企业级功能
- [ ] **代码质量**: 异常处理、资源管理、线程安全性
- [ ] **压力测试**: 多线程并发测试验证系统稳定性

#### Task 1.1.9: 性能基准测试框架 (10分钟) ⏰

**🎯 项目目标**: 创建专业的并发性能测试框架
- **测试维度**: 吞吐量、延迟、可扩展性、稳定性  
- **对比分析**: synchronized vs volatile vs CAS vs 无锁算法

**🚀 Hands-On Coding Exercise (强制编程练习)**
```java
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAdder;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantLock;

// 练习目标: 构建专业的并发性能基准测试框架
public class ConcurrencyPerformanceBenchmark {
    
    /**
     * 性能测试框架
     * 测试不同并发原语在各种场景下的性能表现
     */
    public static class BenchmarkSuite {
        
        private final int warmupIterations = 3;
        private final int benchmarkIterations = 5;
        
        // 测试场景定义
        public interface BenchmarkTask {
            void execute(int threadCount, int operationsPerThread) throws InterruptedException;
            String getName();
        }
        
        // 性能测试结果
        public static class BenchmarkResult {
            final String testName;
            final int threadCount;
            final long totalOperations;
            final long executionTimeMs;
            final double throughputOpsPerSec;
            final double avgLatencyNs;
            
            BenchmarkResult(String testName, int threadCount, long totalOperations, 
                          long executionTimeMs, double avgLatencyNs) {
                this.testName = testName;
                this.threadCount = threadCount;
                this.totalOperations = totalOperations;
                this.executionTimeMs = executionTimeMs;
                this.throughputOpsPerSec = (double) totalOperations / executionTimeMs * 1000;
                this.avgLatencyNs = avgLatencyNs;
            }
            
            @Override
            public String toString() {
                return String.format("%s [线程数=%d]: 吞吐量=%.0f ops/sec, 平均延迟=%.2f ns, 总时间=%d ms",
                    testName, threadCount, throughputOpsPerSec, avgLatencyNs, executionTimeMs);
            }
        }
        
        // TODO: 学生实现性能测试执行器
        public List<BenchmarkResult> runBenchmark(BenchmarkTask task, int[] threadCounts, int operationsPerThread) {
            List<BenchmarkResult> results = new ArrayList<>();
            
            System.out.println("\n=== 开始基准测试: " + task.getName() + " ===");
            
            for (int threadCount : threadCounts) {
                System.out.println("预热阶段 - 线程数: " + threadCount);
                
                // 预热阶段
                for (int i = 0; i < warmupIterations; i++) {
                    try {
                        task.execute(threadCount, operationsPerThread / 10);  // 预热用较少操作
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        return results;
                    }
                }
                
                System.out.println("正式测试 - 线程数: " + threadCount);
                
                // 正式测试
                long totalTime = 0;
                long totalLatency = 0;
                int validRuns = 0;
                
                for (int i = 0; i < benchmarkIterations; i++) {
                    try {
                        long startTime = System.nanoTime();
                        task.execute(threadCount, operationsPerThread);
                        long endTime = System.nanoTime();
                        
                        long runTime = (endTime - startTime) / 1_000_000;  // 转换为毫秒
                        totalTime += runTime;
                        totalLatency += (endTime - startTime) / (threadCount * operationsPerThread);
                        validRuns++;
                        
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                }
                
                if (validRuns > 0) {
                    long avgTimeMs = totalTime / validRuns;
                    double avgLatencyNs = (double) totalLatency / validRuns;
                    long totalOps = (long) threadCount * operationsPerThread;
                    
                    BenchmarkResult result = new BenchmarkResult(
                        task.getName(), threadCount, totalOps, avgTimeMs, avgLatencyNs);
                    
                    results.add(result);
                    System.out.println("  " + result);
                }
            }
            
            return results;
        }
    }
    
    // TODO: 学生实现各种并发原语的性能测试
    
    // 测试1: synchronized性能
    static class SynchronizedCounterTask implements BenchmarkSuite.BenchmarkTask {
        @Override
        public void execute(int threadCount, int operationsPerThread) throws InterruptedException {
            final Object lock = new Object();
            final int[] counter = {0};
            
            CountDownLatch latch = new CountDownLatch(threadCount);
            
            for (int i = 0; i < threadCount; i++) {
                new Thread(() -> {
                    try {
                        for (int j = 0; j < operationsPerThread; j++) {
                            synchronized (lock) {
                                counter[0]++;
                            }
                        }
                    } finally {
                        latch.countDown();
                    }
                }).start();
            }
            
            latch.await();
        }
        
        @Override
        public String getName() {
            return "Synchronized计数器";
        }
    }
    
    // 测试2: AtomicLong性能
    static class AtomicCounterTask implements BenchmarkSuite.BenchmarkTask {
        @Override
        public void execute(int threadCount, int operationsPerThread) throws InterruptedException {
            AtomicLong counter = new AtomicLong(0);
            CountDownLatch latch = new CountDownLatch(threadCount);
            
            for (int i = 0; i < threadCount; i++) {
                new Thread(() -> {
                    try {
                        for (int j = 0; j < operationsPerThread; j++) {
                            counter.incrementAndGet();
                        }
                    } finally {
                        latch.countDown();
                    }
                }).start();
            }
            
            latch.await();
        }
        
        @Override
        public String getName() {
            return "AtomicLong计数器";
        }
    }
    
    // 测试3: LongAdder性能 (高并发优化)
    static class LongAdderCounterTask implements BenchmarkSuite.BenchmarkTask {
        @Override
        public void execute(int threadCount, int operationsPerThread) throws InterruptedException {
            LongAdder counter = new LongAdder();
            CountDownLatch latch = new CountDownLatch(threadCount);
            
            for (int i = 0; i < threadCount; i++) {
                new Thread(() -> {
                    try {
                        for (int j = 0; j < operationsPerThread; j++) {
                            counter.increment();
                        }
                    } finally {
                        latch.countDown();
                    }
                }).start();
            }
            
            latch.await();
        }
        
        @Override
        public String getName() {
            return "LongAdder计数器";
        }
    }
    
    // 测试4: ReentrantLock性能
    static class ReentrantLockCounterTask implements BenchmarkSuite.BenchmarkTask {
        @Override
        public void execute(int threadCount, int operationsPerThread) throws InterruptedException {
            ReentrantLock lock = new ReentrantLock();
            final int[] counter = {0};
            CountDownLatch latch = new CountDownLatch(threadCount);
            
            for (int i = 0; i < threadCount; i++) {
                new Thread(() -> {
                    try {
                        for (int j = 0; j < operationsPerThread; j++) {
                            lock.lock();
                            try {
                                counter[0]++;
                            } finally {
                                lock.unlock();
                            }
                        }
                    } finally {
                        latch.countDown();
                    }
                }).start();
            }
            
            latch.await();
        }
        
        @Override
        public String getName() {
            return "ReentrantLock计数器";
        }
    }
    
    // 测试5: volatile读写性能
    static class VolatileReadWriteTask implements BenchmarkSuite.BenchmarkTask {
        @Override
        public void execute(int threadCount, int operationsPerThread) throws InterruptedException {
            volatile long value = 0;
            CountDownLatch latch = new CountDownLatch(threadCount);
            
            for (int i = 0; i < threadCount; i++) {
                final int threadId = i;
                new Thread(() -> {
                    try {
                        for (int j = 0; j < operationsPerThread; j++) {
                            if (threadId % 2 == 0) {
                                // 50%的线程进行写操作
                                value = j;
                            } else {
                                // 50%的线程进行读操作
                                long temp = value;
                            }
                        }
                    } finally {
                        latch.countDown();
                    }
                }).start();
            }
            
            latch.await();
        }
        
        @Override
        public String getName() {
            return "Volatile读写操作";
        }
    }
    
    // TODO: 学生实现主测试方法
    public static void main(String[] args) {
        BenchmarkSuite suite = new BenchmarkSuite();
        
        // 测试配置
        int[] threadCounts = {1, 2, 4, 8, 16, 32};
        int operationsPerThread = 100_000;
        
        // 所有测试任务
        BenchmarkSuite.BenchmarkTask[] tasks = {
            new SynchronizedCounterTask(),
            new AtomicCounterTask(),
            new LongAdderCounterTask(),
            new ReentrantLockCounterTask(),
            new VolatileReadWriteTask()
        };
        
        // 收集所有结果
        List<List<BenchmarkSuite.BenchmarkResult>> allResults = new ArrayList<>();
        
        for (BenchmarkSuite.BenchmarkTask task : tasks) {
            List<BenchmarkSuite.BenchmarkResult> results = suite.runBenchmark(task, threadCounts, operationsPerThread);
            allResults.add(results);
        }
        
        // 生成性能对比报告
        generatePerformanceReport(allResults, threadCounts);
    }
    
    // TODO: 学生实现性能报告生成
    private static void generatePerformanceReport(List<List<BenchmarkSuite.BenchmarkResult>> allResults, int[] threadCounts) {
        System.out.println("\n" + "=".repeat(80));
        System.out.println("                         性能基准测试报告");
        System.out.println("=".repeat(80));
        
        // 按线程数生成对比表格
        for (int threadCount : threadCounts) {
            System.out.println(String.format("\n线程数: %d", threadCount));
            System.out.println("-".repeat(70));
            System.out.printf("%-20s %-15s %-15s %-15s%n", "测试类型", "吞吐量(ops/sec)", "平均延迟(ns)", "相对性能");
            System.out.println("-".repeat(70));
            
            double maxThroughput = 0;
            List<BenchmarkSuite.BenchmarkResult> threadResults = new ArrayList<>();
            
            // 收集同线程数的所有结果
            for (List<BenchmarkSuite.BenchmarkResult> results : allResults) {
                for (BenchmarkSuite.BenchmarkResult result : results) {
                    if (result.threadCount == threadCount) {
                        threadResults.add(result);
                        maxThroughput = Math.max(maxThroughput, result.throughputOpsPerSec);
                    }
                }
            }
            
            // 输出对比结果
            for (BenchmarkSuite.BenchmarkResult result : threadResults) {
                double relativePerf = result.throughputOpsPerSec / maxThroughput;
                System.out.printf("%-20s %-15.0f %-15.2f %-15.2f%%\n",
                    result.testName,
                    result.throughputOpsPerSec,
                    result.avgLatencyNs,
                    relativePerf * 100);
            }
        }
        
        // 生成可扩展性分析
        System.out.println("\n" + "=".repeat(80));
        System.out.println("                         可扩展性分析");
        System.out.println("=".repeat(80));
        
        for (List<BenchmarkSuite.BenchmarkResult> results : allResults) {
            if (!results.isEmpty()) {
                String testName = results.get(0).testName;
                System.out.println("\n" + testName + " 可扩展性:");
                
                double singleThreadThroughput = results.get(0).throughputOpsPerSec;
                
                for (BenchmarkSuite.BenchmarkResult result : results) {
                    double scalingEfficiency = result.throughputOpsPerSec / singleThreadThroughput / result.threadCount;
                    System.out.printf("  %d线程: %.2fx speedup, %.2f%% 扩展效率\n",
                        result.threadCount,
                        result.throughputOpsPerSec / singleThreadThroughput,
                        scalingEfficiency * 100);
                }
            }
        }
        
        // 总结和建议
        System.out.println("\n" + "=".repeat(80));
        System.out.println("                         性能优化建议");
        System.out.println("=".repeat(80));
        System.out.println("1. 低竞争场景: 优先使用volatile，性能最佳");
        System.out.println("2. 中等竞争场景: AtomicLong提供良好的性能与安全性平衡");
        System.out.println("3. 高竞争场景: LongAdder在多核环境下扩展性最好");
        System.out.println("4. 复杂同步: synchronized简单易用，ReentrantLock功能更强");
        System.out.println("5. 读多写少: 考虑使用ReadWriteLock优化读性能");
    }
}
```

**📝 编程任务检查点**:
- [ ] **基准测试**: 实现科学的性能测试方法(预热、多次测试、统计分析)
- [ ] **多维度对比**: 吞吐量、延迟、可扩展性全面评估
- [ ] **场景覆盖**: 不同并发度下的性能表现对比
- [ ] **数据分析**: 生成专业的性能报告和优化建议
- [ ] **代码质量**: 测试框架的可扩展性和重用性

#### Task 1.1.10: 综合项目 - 分布式锁实现 (10分钟) ⏰

**🎯 最终项目目标**: 实现生产级分布式锁系统
- **技术栈**: 所有学过的并发原语 + 网络编程
- **业务场景**: 微服务架构下的分布式同步
- **代码规模**: 300+行企业级实现

**🚀 Hands-On Coding Exercise (强制编程练习)**
```java
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.LockSupport;
import java.util.UUID;
import java.util.Map;

// 练习目标: 实现企业级分布式锁系统
public class DistributedLockSystem {
    
    /**
     * 基于内存的分布式锁实现 (模拟Redis/Zookeeper)
     * 核心技术:
     * 1. ConcurrentHashMap存储锁信息
     * 2. 原子操作保证获锁/释锁的原子性
     * 3. volatile保证锁状态可见性
     * 4. 超时机制防止死锁
     * 5. 重入锁支持
     * 6. 公平锁队列
     */
    public static class DistributedLock {
        
        // 锁信息存储
        private final ConcurrentHashMap<String, LockInfo> locks = new ConcurrentHashMap<>();
        
        // 等待队列 (公平锁)
        private final ConcurrentHashMap<String, ConcurrentLinkedQueue<LockWaiter>> waitQueues = new ConcurrentHashMap<>();
        
        // 锁清理服务
        private final ScheduledExecutorService cleanupService;
        
        // 锁信息
        private static class LockInfo {
            final String lockId;
            final String ownerId;
            final long createTime;
            final long ttl;
            volatile int reentrantCount;  // 重入次数
            
            LockInfo(String lockId, String ownerId, long ttl) {
                this.lockId = lockId;
                this.ownerId = ownerId;
                this.createTime = System.currentTimeMillis();
                this.ttl = ttl;
                this.reentrantCount = 1;
            }
            
            boolean isExpired() {
                return ttl > 0 && (System.currentTimeMillis() - createTime) > ttl;
            }
            
            boolean isOwnedBy(String ownerId) {
                return this.ownerId.equals(ownerId);
            }
        }
        
        // 等待者信息
        private static class LockWaiter {
            final String ownerId;
            final CountDownLatch latch;
            final long waitStartTime;
            volatile boolean cancelled = false;
            
            LockWaiter(String ownerId) {
                this.ownerId = ownerId;
                this.latch = new CountDownLatch(1);
                this.waitStartTime = System.currentTimeMillis();
            }
        }
        
        public DistributedLock() {
            this.cleanupService = Executors.newSingleThreadScheduledExecutor(r -> {
                Thread t = new Thread(r, "DistributedLock-Cleanup");
                t.setDaemon(true);
                return t;
            });
            
            // 启动过期锁清理任务
            startCleanupTask();
        }
        
        // TODO: 学生实现分布式锁获取
        public boolean tryLock(String lockKey, String ownerId, long ttlMs) {
            if (lockKey == null || ownerId == null) {
                throw new IllegalArgumentException("LockKey and ownerId cannot be null");
            }
            
            // 原子操作: 尝试获取锁
            LockInfo existingLock = locks.get(lockKey);
            
            if (existingLock == null) {
                // 锁不存在，尝试获取
                LockInfo newLock = new LockInfo(lockKey, ownerId, ttlMs);
                LockInfo prevLock = locks.putIfAbsent(lockKey, newLock);
                
                if (prevLock == null) {
                    // 成功获取锁
                    notifyNextWaiter(lockKey);  // 这里不会有等待者，但保持一致性
                    return true;
                } else {
                    // 其他线程抢先获取了锁
                    existingLock = prevLock;
                }
            }
            
            // 检查重入锁
            if (existingLock.isOwnedBy(ownerId) && !existingLock.isExpired()) {
                // 重入锁: 同一个owner可以多次获取
                synchronized (existingLock) {  // 同步保证重入计数的原子性
                    if (existingLock.isOwnedBy(ownerId) && !existingLock.isExpired()) {
                        existingLock.reentrantCount++;
                        return true;
                    }
                }
            }
            
            // 检查锁是否过期
            if (existingLock.isExpired()) {
                // 原子地移除过期锁并重试
                if (locks.remove(lockKey, existingLock)) {
                    return tryLock(lockKey, ownerId, ttlMs);  // 递归重试
                }
            }
            
            return false;  // 获取锁失败
        }
        
        // TODO: 学生实现阻塞式锁获取
        public boolean lock(String lockKey, String ownerId, long ttlMs, long timeoutMs) 
                throws InterruptedException {
            
            long startTime = System.currentTimeMillis();
            
            // 先尝试非阻塞获取
            if (tryLock(lockKey, ownerId, ttlMs)) {
                return true;
            }
            
            if (timeoutMs <= 0) {
                return false;  // 不等待
            }
            
            // 加入等待队列
            ConcurrentLinkedQueue<LockWaiter> waitQueue = waitQueues.computeIfAbsent(
                lockKey, k -> new ConcurrentLinkedQueue<>());
            
            LockWaiter waiter = new LockWaiter(ownerId);
            waitQueue.offer(waiter);
            
            try {
                while (System.currentTimeMillis() - startTime < timeoutMs) {
                    // 计算剩余等待时间
                    long remainingTime = timeoutMs - (System.currentTimeMillis() - startTime);
                    if (remainingTime <= 0) {
                        break;
                    }
                    
                    // 等待被唤醒或超时
                    boolean signaled = waiter.latch.await(remainingTime, TimeUnit.MILLISECONDS);
                    
                    if (waiter.cancelled) {
                        return false;  // 被取消
                    }
                    
                    // 被唤醒后尝试获取锁
                    if (signaled && tryLock(lockKey, ownerId, ttlMs)) {
                        return true;
                    }
                    
                    // 如果没有被信号唤醒，可能是虚假唤醒，继续等待
                }
                
                return false;  // 超时
                
            } finally {
                // 清理等待队列
                waiter.cancelled = true;
                waitQueue.remove(waiter);
                if (waitQueue.isEmpty()) {
                    waitQueues.remove(lockKey);
                }
            }
        }
        
        // TODO: 学生实现锁释放
        public boolean unlock(String lockKey, String ownerId) {
            if (lockKey == null || ownerId == null) {
                return false;
            }
            
            LockInfo lockInfo = locks.get(lockKey);
            if (lockInfo == null || !lockInfo.isOwnedBy(ownerId)) {
                return false;  // 锁不存在或不是当前owner
            }
            
            synchronized (lockInfo) {  // 同步保证重入计数的原子性
                if (!lockInfo.isOwnedBy(ownerId)) {
                    return false;  // 双重检查
                }
                
                lockInfo.reentrantCount--;
                
                if (lockInfo.reentrantCount <= 0) {
                    // 完全释放锁
                    if (locks.remove(lockKey, lockInfo)) {
                        // 唤醒下一个等待者
                        notifyNextWaiter(lockKey);
                        return true;
                    }
                } else {
                    // 减少重入计数但保持锁
                    return true;
                }
            }
            
            return false;
        }
        
        // TODO: 学生实现公平锁队列通知
        private void notifyNextWaiter(String lockKey) {
            ConcurrentLinkedQueue<LockWaiter> waitQueue = waitQueues.get(lockKey);
            if (waitQueue != null) {
                LockWaiter nextWaiter;
                
                // 找到下一个有效的等待者
                while ((nextWaiter = waitQueue.poll()) != null) {
                    if (!nextWaiter.cancelled) {
                        nextWaiter.latch.countDown();  // 唤醒等待者
                        break;
                    }
                }
                
                // 如果队列为空，移除它
                if (waitQueue.isEmpty()) {
                    waitQueues.remove(lockKey);
                }
            }
        }
        
        // TODO: 学生实现锁续期 (防止业务逻辑执行时间过长导致锁过期)
        public boolean renewLock(String lockKey, String ownerId, long newTtlMs) {
            LockInfo lockInfo = locks.get(lockKey);
            if (lockInfo == null || !lockInfo.isOwnedBy(ownerId) || lockInfo.isExpired()) {
                return false;
            }
            
            // 创建新的锁信息 (更新TTL)
            LockInfo newLockInfo = new LockInfo(lockKey, ownerId, newTtlMs);
            newLockInfo.reentrantCount = lockInfo.reentrantCount;
            
            return locks.replace(lockKey, lockInfo, newLockInfo);
        }
        
        // TODO: 学生实现锁清理任务
        private void startCleanupTask() {
            cleanupService.scheduleAtFixedRate(() -> {
                try {
                    // 清理过期锁
                    locks.entrySet().removeIf(entry -> {
                        LockInfo lockInfo = entry.getValue();
                        if (lockInfo.isExpired()) {
                            String lockKey = entry.getKey();
                            System.out.println("清理过期锁: " + lockKey);
                            
                            // 通知等待者
                            notifyNextWaiter(lockKey);
                            return true;
                        }
                        return false;
                    });
                    
                    // 清理过期的等待者
                    waitQueues.entrySet().removeIf(entry -> {
                        ConcurrentLinkedQueue<LockWaiter> queue = entry.getValue();
                        queue.removeIf(waiter -> {
                            long waitTime = System.currentTimeMillis() - waiter.waitStartTime;
                            if (waitTime > 30000) {  // 30秒超时
                                waiter.cancelled = true;
                                waiter.latch.countDown();
                                return true;
                            }
                            return false;
                        });
                        return queue.isEmpty();
                    });
                    
                } catch (Exception e) {
                    System.err.println("锁清理异常: " + e.getMessage());
                }
            }, 10, 10, TimeUnit.SECONDS);  // 每10秒清理一次
        }
        
        // 获取锁统计信息
        public LockStatistics getStatistics() {
            int totalLocks = locks.size();
            int totalWaiters = waitQueues.values().stream()
                .mapToInt(ConcurrentLinkedQueue::size)
                .sum();
            
            return new LockStatistics(totalLocks, totalWaiters);
        }
        
        public void shutdown() {
            cleanupService.shutdown();
        }
    }
    
    // 锁统计信息
    public static class LockStatistics {
        public final int activeLocks;
        public final int waitingThreads;
        
        LockStatistics(int activeLocks, int waitingThreads) {
            this.activeLocks = activeLocks;
            this.waitingThreads = waitingThreads;
        }
        
        @Override
        public String toString() {
            return String.format("LockStats{activeLocks=%d, waitingThreads=%d}", 
                activeLocks, waitingThreads);
        }
    }
    
    // TODO: 学生实现综合测试
    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== 分布式锁系统综合测试 ===");
        
        DistributedLock distributedLock = new DistributedLock();
        
        // 测试1: 基本功能测试
        testBasicLockingBehavior(distributedLock);
        
        Thread.sleep(1000);
        
        // 测试2: 重入锁测试
        testReentrantLocking(distributedLock);
        
        Thread.sleep(1000);
        
        // 测试3: 并发竞争测试
        testConcurrentCompetition(distributedLock);
        
        Thread.sleep(1000);
        
        // 测试4: 超时和过期测试
        testTimeoutAndExpiration(distributedLock);
        
        Thread.sleep(1000);
        
        // 测试5: 高并发压力测试
        performStressTest(distributedLock);
        
        distributedLock.shutdown();
        System.out.println("所有测试完成!");
    }
    
    private static void testBasicLockingBehavior(DistributedLock lock) {
        System.out.println("\n--- 测试1: 基本锁功能 ---");
        
        String lockKey = "test_lock_1";
        String owner1 = "thread_1";
        String owner2 = "thread_2";
        
        // 获取锁
        boolean acquired = lock.tryLock(lockKey, owner1, 5000);
        System.out.println("线程1获取锁: " + acquired);
        
        // 其他线程尝试获取同一个锁
        boolean acquired2 = lock.tryLock(lockKey, owner2, 5000);
        System.out.println("线程2获取锁: " + acquired2);
        
        // 释放锁
        boolean released = lock.unlock(lockKey, owner1);
        System.out.println("线程1释放锁: " + released);
        
        // 线程2现在应该能获取锁
        boolean acquired3 = lock.tryLock(lockKey, owner2, 5000);
        System.out.println("线程2重新获取锁: " + acquired3);
        
        lock.unlock(lockKey, owner2);
    }
    
    private static void testReentrantLocking(DistributedLock lock) {
        System.out.println("\n--- 测试2: 重入锁功能 ---");
        
        String lockKey = "reentrant_lock";
        String owner = "reentrant_thread";
        
        // 多次获取同一个锁
        System.out.println("第1次获取: " + lock.tryLock(lockKey, owner, 5000));
        System.out.println("第2次获取: " + lock.tryLock(lockKey, owner, 5000));
        System.out.println("第3次获取: " + lock.tryLock(lockKey, owner, 5000));
        
        // 需要对应次数的释放
        System.out.println("第1次释放: " + lock.unlock(lockKey, owner));
        System.out.println("第2次释放: " + lock.unlock(lockKey, owner));
        
        // 其他线程仍然无法获取
        System.out.println("其他线程获取: " + lock.tryLock(lockKey, "other", 5000));
        
        // 最后一次释放
        System.out.println("第3次释放: " + lock.unlock(lockKey, owner));
        
        // 现在其他线程可以获取了
        System.out.println("其他线程获取: " + lock.tryLock(lockKey, "other", 5000));
        lock.unlock(lockKey, "other");
    }
    
    private static void testConcurrentCompetition(DistributedLock lock) throws InterruptedException {
        System.out.println("\n--- 测试3: 并发竞争测试 ---");
        
        String lockKey = "concurrent_lock";
        final int THREAD_COUNT = 10;
        final int OPERATIONS = 5;
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch completeLatch = new CountDownLatch(THREAD_COUNT);
        AtomicReference<String> currentHolder = new AtomicReference<>();
        
        for (int i = 0; i < THREAD_COUNT; i++) {
            final String threadId = "Thread-" + i;
            
            new Thread(() -> {
                try {
                    startLatch.await();  // 等待同时开始
                    
                    for (int j = 0; j < OPERATIONS; j++) {
                        try {
                            if (lock.lock(lockKey, threadId, 2000, 1000)) {  // 1秒超时
                                currentHolder.set(threadId);
                                System.out.println(threadId + " 获得锁，执行操作 " + (j + 1));
                                
                                // 模拟业务逻辑
                                Thread.sleep(100);
                                
                                lock.unlock(lockKey, threadId);
                                System.out.println(threadId + " 释放锁");
                            } else {
                                System.out.println(threadId + " 获取锁超时");
                            }
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            break;
                        }
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    completeLatch.countDown();
                }
            }, threadId).start();
        }
        
        startLatch.countDown();  // 开始竞争
        completeLatch.await();   // 等待所有线程完成
        
        System.out.println("并发竞争测试完成，统计: " + lock.getStatistics());
    }
    
    private static void testTimeoutAndExpiration(DistributedLock lock) throws InterruptedException {
        System.out.println("\n--- 测试4: 超时和过期测试 ---");
        
        String lockKey = "timeout_lock";
        String owner1 = "timeout_thread_1";
        String owner2 = "timeout_thread_2";
        
        // 测试锁超时
        System.out.println("获取短TTL锁: " + lock.tryLock(lockKey, owner1, 500));  // 0.5秒TTL
        
        Thread.sleep(600);  // 等待锁过期
        
        // 现在其他线程应该能获取锁
        System.out.println("过期后获取锁: " + lock.tryLock(lockKey, owner2, 5000));
        
        // 测试等待超时
        Thread waitingThread = new Thread(() -> {
            try {
                long startTime = System.currentTimeMillis();
                boolean acquired = lock.lock("waiting_lock", "waiter", 5000, 1000);  // 1秒超时
                long endTime = System.currentTimeMillis();
                
                System.out.println("等待线程结果: " + acquired + ", 等待时间: " + (endTime - startTime) + "ms");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        
        // 先让主线程获取锁
        lock.tryLock("waiting_lock", "main", 5000);
        waitingThread.start();
        
        Thread.sleep(1500);  // 让等待线程超时
        
        lock.unlock("waiting_lock", "main");
        waitingThread.join();
        
        lock.unlock(lockKey, owner2);
    }
    
    private static void performStressTest(DistributedLock lock) throws InterruptedException {
        System.out.println("\n--- 测试5: 高并发压力测试 ---");
        
        final int THREAD_COUNT = 50;
        final int OPERATIONS_PER_THREAD = 20;
        final String LOCK_KEY = "stress_test_lock";
        
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch completeLatch = new CountDownLatch(THREAD_COUNT);
        AtomicLong successCount = new AtomicLong(0);
        AtomicLong timeoutCount = new AtomicLong(0);
        
        long testStartTime = System.currentTimeMillis();
        
        for (int i = 0; i < THREAD_COUNT; i++) {
            final String threadId = "StressThread-" + i;
            
            new Thread(() -> {
                try {
                    startLatch.await();
                    
                    for (int j = 0; j < OPERATIONS_PER_THREAD; j++) {
                        try {
                            if (lock.lock(LOCK_KEY, threadId, 1000, 500)) {  // 0.5秒超时
                                successCount.incrementAndGet();
                                
                                // 快速业务逻辑
                                Thread.sleep(10);
                                
                                lock.unlock(LOCK_KEY, threadId);
                            } else {
                                timeoutCount.incrementAndGet();
                            }
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            break;
                        }
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    completeLatch.countDown();
                }
            }, threadId).start();
        }
        
        startLatch.countDown();
        completeLatch.await();
        
        long testEndTime = System.currentTimeMillis();
        long totalTime = testEndTime - testStartTime;
        
        System.out.println("压力测试结果:");
        System.out.println("  总线程数: " + THREAD_COUNT);
        System.out.println("  每线程操作数: " + OPERATIONS_PER_THREAD);
        System.out.println("  总操作数: " + (THREAD_COUNT * OPERATIONS_PER_THREAD));
        System.out.println("  成功获取锁: " + successCount.get());
        System.out.println("  获取锁超时: " + timeoutCount.get());
        System.out.println("  总耗时: " + totalTime + "ms");
        System.out.println("  平均吞吐量: " + (successCount.get() * 1000 / totalTime) + " locks/sec");
        System.out.println("  成功率: " + (successCount.get() * 100.0 / (THREAD_COUNT * OPERATIONS_PER_THREAD)) + "%");
        System.out.println("  最终统计: " + lock.getStatistics());
    }
}
```

**📝 编程任务检查点**:
- [ ] **分布式系统设计**: 理解分布式环境下的同步挑战
- [ ] **原子操作应用**: 正确使用CAS操作保证分布式锁的原子性
- [ ] **超时机制**: 实现锁超时和等待超时，防止死锁
- [ ] **重入锁支持**: 支持同一线程多次获取同一把锁
- [ ] **公平性**: 实现公平锁队列，保证先到先得
- [ ] **高可用性**: 过期锁清理、异常处理、资源管理
- [ ] **性能优化**: 在正确性基础上优化并发性能
- [ ] **监控统计**: 提供锁使用统计和监控能力

**🏆 综合项目完成标准**:
- [ ] **功能完整**: 所有核心功能正确实现并通过测试
- [ ] **性能达标**: 高并发场景下的良好性能表现
- [ ] **代码质量**: 清晰的架构设计、完善的错误处理
- [ ] **面试就绪**: 能够详细解释设计思路和技术选择

<function_calls>
<invoke name="TodoWrite">
<parameter name="todos">[{"id": "1", "content": "Read and analyze current MICRO_TASK_C01.md structure", "status": "completed"}, {"id": "2", "content": "Design comprehensive hands-on practice framework", "status": "completed"}, {"id": "3", "content": "Add progressive coding exercises for each micro-task", "status": "completed"}, {"id": "4", "content": "Create quality assurance and verification framework", "status": "in_progress"}, {"id": "5", "content": "Integrate real-world project components", "status": "pending"}]