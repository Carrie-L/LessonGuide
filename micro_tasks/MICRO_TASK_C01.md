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

### Phase 1: 并发编程基础 - 20分钟

- [ ] **学习目标**: 深入理解JMM对并发编程的影响
- [ ] **具体任务**:
  - 分析主内存和工作内存的抽象模型
  - 理解happens-before关系的建立和传递性
  - 掌握内存屏障(Memory Barrier)的作用机制
- [ ] **检查点**: happens-before关系如何保证内存可见性？有哪些建立方式？
- [ ] **文件**: `student_progress/notes/ C01_java_memory_model_analysis.md`

#### Task 1.1.1: JMM概念入门 + 首个编程实验 (5分钟) ⏰

**🎯 Primary Level (新手友好)**
- [ ] **什么是JMM**: 想象你有一个笔记本(工作内存)和一个公共图书馆(主内存)
- [ ] **简单理解**: JMM就是Java定义的"多个程序员如何共享数据"的规则
- [ ] **生活类比**: 就像办公室里多人共用一台打印机，需要排队规则一样
- [ ] **检查点**: 能说出"JMM是用来解决什么问题的"
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
- [ ] **什么是happens-before**: 想象一个接力赛，前一个跑步者的成绩，后一个跑步者一定能看到
- [ ] **简单理解**: "A发生在B之前"意味着"B能看到A的所有结果"
- [ ] **记忆技巧**: happens-before = "发生在前面" + "结果可见"
- [ ] **检查点**: 能用"接力赛"例子解释happens-before
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
- [ ] **什么是synchronized**: 想象一个厕所门锁，一次只能一个人进去
- [ ] **简单理解**: synchronized就是给代码加了一把锁，确保不会"撞车"
- [ ] **生活类比**: 就像银行只有一个窗口，大家要排队一个一个来
- [ ] **检查点**: 能说出"synchronized是用来防止什么问题的"
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
- [ ] **什么是volatile**: 想象一个公告板，任何人更新后，所有人立即能看到
- [ ] **简单理解**: volatile确保变量的"最新消息"能被所有线程看到
- [ ] **常见错误**: volatile ≠ synchronized，它不能防止"撞车"
- [ ] **检查点**: 能说出"volatile解决什么问题，不解决什么问题"
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

### Phase 2: synchronized互斥锁原理 - 15分钟

- [ ] **学习目标**: 理解synchronized的实现原理和性能特性
- [ ] **具体任务**:
  - 分析synchronized的偏向锁、轻量级锁、重量级锁升级
  - 理解对象头Mark Word的锁状态标识
  - 掌握synchronized的可重入性和内存语义
- [ ] **检查点**: synchronized如何从重量级锁优化为轻量级锁？升级条件是什么？
- [ ] **文件**: `student_progress/notes/synchronized_lock_mechanism.md`
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


### Phase 3: JMM实践

#### Task 1.1.8: [实践]synchronized性能测试 (5分钟) ⏰
- [ ] **学习目标**: 通过实验验证synchronized的性能特性
- [ ] **具体任务**:
  - 测试不同并发度下synchronized的性能表现
  - 对比偏向锁、轻量级锁、重量级锁的切换开销
  - 分析锁粗化和锁消除优化的效果
- [ ] **检查点**: 什么情况下synchronized性能最好？什么情况下性能很差？
- [ ] **文件**: `student_progress/c01/SynchronizedPerformanceTest.kt`
#### Task 1.1.9: 🚀 Intermediate - 手动实现无锁栈 (5分钟) ⏰
**实践目标**: 亲手打造高性能无锁数据结构 ⚡

- [ ] **Intermediate目标**: 使用CAS原子操作实现线程安全的栈
- [ ] **💻 必须手动编程** (禁止复制，一次一次手动输入):
  ```kotlin
  import java.util.concurrent.atomic.AtomicReference
  
  // 目标：实现一个完全无锁的栈
  class LockFreeStack<T> {
      private val head = AtomicReference<Node<T>?>(null)
      
      private data class Node<T>(
          val value: T,
          val next: Node<T>?
      )
      
      fun push(value: T): Boolean {
          val newNode = Node(value, null)
          
          // TODO 1: 亲手实现CAS循环推入逻辑
          while (true) {
              val currentHead = head.get()
              // 亲手实现这里的逻辑
          }
      }
      
      fun pop(): T? {
          // TODO 2: 亲手实现CAS循环弹出逻辑
          while (true) {
              val currentHead = head.get()
              if (currentHead == null) {
                  return null
              }
              
              // 亲手实现这里的逻辑
          }
      }
      
      fun peek(): T? {
          // TODO 3: 亲手实现无锁查看逻辑
      }
      
      fun size(): Int {
          // TODO 4: 亲手实现大小计算(注意线程安全)
      }
  }
  
  // 解决ABA问题的版本
  class VersionedLockFreeStack<T> {
      private val head = AtomicReference<VersionedNode<T>?>(null)
      
      private data class VersionedNode<T>(
          val value: T,
          val next: VersionedNode<T>?,
          val version: Long
      )
      
      fun push(value: T): Boolean {
          // TODO 5: 亲手实现带版本号的推入
      }
  }
  ```
- [ ] **实践步骤** (每一行都要亲手输入):
  1. 📝 手动输入完整的LockFreeStack结构
  2. 🔍 实现 `push()` 的CAS循环逻辑
  3. 🔍 实现 `pop()` 的CAS循环逻辑
  4. 🔍 实现 `peek()` 和 `size()` 方法
  5. 🔍 实现带版本号的ABA问题解决方案
- [ ] **实战测试项目**: 多线程压力测试
  ```kotlin
  // 亲手实现并发测试
  class LockFreeStackTest {
      fun concurrencyTest() {
          val stack = LockFreeStack<Int>()
          val threadCount = 10
          val operationsPerThread = 10000
          
          // TODO: 启动多个线程同时push和pop
          // TODO: 验证数据一致性和线程安全
      }
  }
  ```
- [ ] **实战测试场景**:
  - 🟢 单线程功能测试
  - 🟡 10个线程并发push/pop
  - 🔴 100个线程极限压力测试
  - 🟣 ABA问题复现和验证
- [ ] **Intermediate检查点**: 你的无锁栈在高并发下能保证数据一致性吗？
- [ ] **性能对比测试**: 对比你的无锁栈 vs Java的ConcurrentLinkedDeque性能
- [ ] **代码质量检查**: □ 解决了ABA问题 □ 处理了内存回收 □ 有性能测试
- [ ] **文件**: `student_progress/c01/LockFreeStack.kt`

#### Task 1.1.10: ReentrantLock与AQS框架 (5分钟) ⏰
- [ ] **学习目标**: 理解Java并发包的核心框架AQS
- [ ] **具体任务**:
  - 分析AbstractQueuedSynchronizer的队列管理机制
  - 理解ReentrantLock的公平锁和非公平锁实现
  - 掌握Condition条件变量的等待/通知机制
- [ ] **检查点**: AQS如何实现不同类型的同步器？公平锁和非公平锁的区别？
- [ ] **文件**: `student_progress/c01/reentrant_lock_aqs_analysis.md`

#### Task 1.1.11: [实践]读写锁优化方案 (5分钟) ⏰
- [ ] **学习目标**: 实现高效的读写分离锁机制
- [ ] **具体任务**:
  - 实现支持读写分离的缓存管理器
  - 处理写锁饥饿问题和公平性保证
  - 分析StampedLock的乐观读锁机制
- [ ] **检查点**: 读写锁在什么场景下性能最好？如何防止写锁饥饿？
- [ ] **文件**: `student_progress/c01/ReadWriteLockCache.kt`

#### Task 1.1.12: 线程安全的集合类 (5分钟) ⏰
- [ ] **学习目标**: 分析并发集合的实现原理和性能特性
- [ ] **具体任务**:
  - 分析ConcurrentHashMap的分段锁和CAS优化
  - 理解CopyOnWriteArrayList的写时复制机制
  - 掌握BlockingQueue的生产者-消费者模式
- [ ] **检查点**: 不同并发集合的适用场景是什么？性能权衡如何？
- [ ] **文件**: `student_progress/c01/concurrent_collections_analysis.md`

#### 1.1.13: [实践]高性能并发缓存 (5分钟) ⏰
- [ ] **学习目标**: 实现线程安全的高性能缓存系统
- [ ] **具体任务**:
  - 实现支持LRU淘汰的并发缓存
  - 使用分段锁减少锁竞争
  - 实现缓存的异步刷新和过期处理
- [ ] **检查点**: 如何设计一个既线程安全又高性能的缓存？
- [ ] **文件**: `student_progress/c01/HighPerformanceCache.kt`

#### Task 1.1.14: 死锁检测与预防 (5分钟) ⏰
- [ ] **学习目标**: 理解死锁的成因和预防策略
- [ ] **具体任务**:
  - 分析死锁的四个必要条件
  - 实现银行家算法进行死锁预防
  - 设计超时机制和死锁检测工具
- [ ] **检查点**: 如何系统性地预防和检测死锁？有哪些实用策略？
- [ ] **文件**: `student_progress/c01/deadlock_detection_prevention.md`

#### Task 1.1.15: [实践]协程与线程性能对比 (5分钟) ⏰
- [ ] **学习目标**: 对比协程和线程在并发编程中的优劣
- [ ] **具体任务**:
  - 实现相同功能的线程版本和协程版本
  - 测试高并发场景下的内存使用和性能
  - 分析协程的调度开销和上下文切换成本
- [ ] **检查点**: 协程相比线程有什么优势？什么场景下选择协程？
- [ ] **文件**: `student_progress/c01/CoroutineVsThreadPerformance.kt`

#### Task 1.1.16: 内存一致性模型 (5分钟) ⏰
- [ ] **学习目标**: 理解不同平台的内存一致性模型差异
- [ ] **具体任务**:
  - 对比x86的强内存模型和ARM的弱内存模型
  - 分析内存重排序对程序正确性的影响
  - 理解内存屏障在不同架构上的实现
- [ ] **检查点**: 为什么移动设备(ARM)上的并发问题可能更复杂？
- [ ] **文件**: `student_progress/c01/memory_consistency_models.md`

#### Task 1.1.17: [高级]无锁编程模式 (5分钟) ⏰
- [ ] **学习目标**: 掌握高级的无锁编程技术和模式
- [ ] **具体任务**:
  - 实现生产者-消费者的无锁队列
  - 设计RCU(Read-Copy-Update)模式的应用
  - 分析内存回收和hazard pointer技术
- [ ] **检查点**: 无锁编程的主要挑战是什么？如何保证内存回收的安全性？
- [ ] **文件**: `student_progress/c01/LockFreeProgramming.kt`

#### Task 1.1.18: [设计]高并发系统架构 (5分钟) ⏰
- [ ] **学习目标**: 设计支持高并发的系统架构
- [ ] **具体任务**:
  - 设计分层的并发控制策略
  - 实现限流、熔断、降级等保护机制
  - 分析异步处理和事件驱动架构
- [ ] **检查点**: 如何设计一个能处理高并发访问的移动后端系统？
- [ ] **文件**: `student_progress/c01/high_concurrency_architecture.md`

#### Task 1.1.19: [面试]并发编程综合应用 (5分钟) ⏰
- [ ] **学习目标**: 准备并发编程相关的面试问题
- [ ] **具体任务**:
  - 整理并发编程的核心概念和常见陷阱
  - 准备线程安全问题的实战解决方案
  - 模拟设计题：设计高并发的数据处理系统
- [ ] **检查点**: 如何展示对并发编程的深入理解和实践能力？
- [ ] **文件**: `student_progress/c01/concurrent_programming_interview.md`

#### Task 1.1.20: [总结]并发编程知识体系 (5分钟) ⏰
- [ ] **学习目标**: 构建完整的并发编程知识框架
- [ ] **具体任务**:
  - 总结并发编程的核心原理和实现模式
  - 整理Android/移动端并发编程的最佳实践
  - 准备技术分享：《移动应用并发编程实战指南》
- [ ] **检查点**: 能否独立设计和实现复杂的并发系统？
- [ ] **文件**: `student_progress/c01/concurrent_programming_system.md`

---


### Phase 4: Senior Application (架构应用) - 30分钟

**学习说明**: 这个阶段要求你**亲手编码**，通过实际运行代码来验证理论知识。记住：**Learn by Doing** - 只有动手才能真正理解！

#### Task 1.1.21: 企业级线程安全组件设计 (10分钟) ⏰

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

#### Task 1.1.22: 性能基准测试框架 (10分钟) ⏰

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

#### Task 1.1.23: 综合项目 - 分布式锁实现 (10分钟) ⏰

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

---

## 🎯 第二章节 1.2 集合框架：HashMap, ArrayList 与数据结构优化

**学习方法说明**: 从底层数据结构入手，理解Java集合框架的设计原理和性能特性。通过手动实现核心数据结构，掌握内存管理、时间复杂度优化等关键技能。

**🚨 强制性编程框架**: 
- ✋ **从零实现**: 不依赖Java内置集合，手动实现所有数据结构
- 🏃 **性能导向**: 每个实现都必须包含性能测试和优化
- 📊 **渐进复杂度**: 基础实现 → 优化版本 → 生产级系统

---

### Phase 5: 集合基础 - 20分钟

#### Task 1.2.1: ArrayList动态数组实现 + 内存管理 (5分钟) ⏰

**🎯 Primary Level (新手友好)**
- [ ] **什么是ArrayList**: 想象一个可以自动扩容的书架，书多了就换更大的书架
- [ ] **简单理解**: 底层是数组，满了就创建更大数组并复制数据
- [ ] **生活类比**: 就像搬家，房子小了就换大房子，把东西搬过去
- [ ] **检查点**: 能说出"ArrayList如何实现动态扩容"
- [ ] **文件**: 在`student_progress/`创建`collections_notes.md`，记录学习心得

**🚀 Hands-On Coding Exercise (强制编程练习)**
```java
// 练习目标: 从零实现ArrayList核心功能
public class MyArrayList<T> {
    private Object[] elements;      // 底层数组
    private int size = 0;          // 当前元素个数
    private static final int DEFAULT_CAPACITY = 10;
    
    public MyArrayList() {
        elements = new Object[DEFAULT_CAPACITY];
    }
    
    public MyArrayList(int initialCapacity) {
        if (initialCapacity < 0) {
            throw new IllegalArgumentException("Illegal Capacity: " + initialCapacity);
        }
        elements = new Object[initialCapacity];
    }
    
    // TODO: 学生手动实现添加元素
    public boolean add(T element) {
        ensureCapacity(size + 1);  // 确保容量足够
        elements[size++] = element;
        return true;
    }
    
    // TODO: 学生实现容量扩展机制
    private void ensureCapacity(int minCapacity) {
        if (minCapacity > elements.length) {
            grow(minCapacity);
        }
    }
    
    private void grow(int minCapacity) {
        int oldCapacity = elements.length;
        // 扩容策略: 1.5倍扩容 (为什么不是2倍?)
        int newCapacity = oldCapacity + (oldCapacity >> 1);
        
        if (newCapacity < minCapacity) {
            newCapacity = minCapacity;
        }
        
        // 数组复制 - 这是性能关键点!
        elements = Arrays.copyOf(elements, newCapacity);
        System.out.println("扩容: " + oldCapacity + " -> " + newCapacity);
    }
    
    // TODO: 学生实现获取元素
    @SuppressWarnings("unchecked")
    public T get(int index) {
        rangeCheck(index);
        return (T) elements[index];
    }
    
    // TODO: 学生实现删除元素
    public T remove(int index) {
        rangeCheck(index);
        
        @SuppressWarnings("unchecked")
        T oldValue = (T) elements[index];
        
        int numMoved = size - index - 1;
        if (numMoved > 0) {
            // 数组复制删除元素 - 理解为什么删除操作昂贵
            System.arraycopy(elements, index + 1, elements, index, numMoved);
        }
        
        elements[--size] = null;  // 防止内存泄漏!
        return oldValue;
    }
    
    private void rangeCheck(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
    }
    
    public int size() {
        return size;
    }
    
    public boolean isEmpty() {
        return size == 0;
    }
    
    // 性能测试方法
    public void performanceTest() {
        long startTime = System.nanoTime();
        
        // 测试添加性能
        for (int i = 0; i < 100000; i++) {
            add((T) Integer.valueOf(i));
        }
        
        long endTime = System.nanoTime();
        System.out.println("添加100000个元素耗时: " + (endTime - startTime) / 1_000_000 + "ms");
        
        // 测试随机访问性能
        startTime = System.nanoTime();
        for (int i = 0; i < 10000; i++) {
            get(i * 10);  // 随机访问
        }
        endTime = System.nanoTime();
        System.out.println("随机访问10000次耗时: " + (endTime - startTime) / 1_000_000 + "ms");
    }
    
    // 测试主函数
    public static void main(String[] args) {
        MyArrayList<String> list = new MyArrayList<>(3);  // 小容量测试扩容
        
        // 观察扩容过程
        System.out.println("=== 扩容测试 ===");
        for (int i = 0; i < 12; i++) {
            list.add("Element-" + i);
            System.out.println("添加第" + (i + 1) + "个元素，当前size: " + list.size());
        }
        
        // 测试删除
        System.out.println("\n=== 删除测试 ===");
        System.out.println("删除索引5的元素: " + list.remove(5));
        System.out.println("删除后size: " + list.size());
        
        // 性能测试
        System.out.println("\n=== 性能测试 ===");
        MyArrayList<Integer> perfList = new MyArrayList<>();
        perfList.performanceTest();
    }
}
```

**📝 编程任务检查点**:
- [ ] **手动输入**: 完整实现MyArrayList类，理解每一行代码
- [ ] **扩容观察**: 运行程序观察1.5倍扩容过程
- [ ] **性能分析**: 理解为什么ArrayList随机访问快，删除操作慢
- [ ] **内存管理**: 理解为什么删除后要设置null防止内存泄漏
- [ ] **代码位置**: `student_progress/JavaLearning/src/MyArrayList.java`

#### Task 1.2.2: HashMap哈希表基础 + 冲突解决 (5分钟) ⏰

**🎯 Primary Level (新手友好)**
- [ ] **什么是HashMap**: 想象一个图书馆索引，通过书名快速找到书的位置
- [ ] **简单理解**: 把key通过哈希函数转换成数组索引，实现O(1)查找
- [ ] **生活类比**: 就像电话簿，通过姓名首字母快速定位到页码
- [ ] **检查点**: 能说出"HashMap如何实现快速查找"

**🚀 Hands-On Coding Exercise (强制编程练习)**
```java
// 练习目标: 理解哈希表的核心原理和冲突处理
public class MyHashMap<K, V> {
    
    // 内部节点类 - 用链表处理冲突
    static class Node<K, V> {
        final int hash;
        final K key;
        V value;
        Node<K, V> next;
        
        Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
        
        @Override
        public String toString() {
            return key + "=" + value;
        }
    }
    
    private Node<K, V>[] table;      // 哈希表数组
    private int size = 0;            // 当前键值对数量
    private int threshold;           // 扩容阈值
    private static final double LOAD_FACTOR = 0.75;  // 负载因子
    private static final int DEFAULT_CAPACITY = 16;   // 默认容量
    
    @SuppressWarnings("unchecked")
    public MyHashMap() {
        table = (Node<K, V>[]) new Node[DEFAULT_CAPACITY];
        threshold = (int) (DEFAULT_CAPACITY * LOAD_FACTOR);
    }
    
    // TODO: 学生实现哈希函数
    private int hash(Object key) {
        if (key == null) return 0;
        
        int h = key.hashCode();
        // 高16位异或低16位，减少冲突
        return h ^ (h >>> 16);
    }
    
    // 通过哈希值计算数组索引
    private int indexFor(int hash, int length) {
        return hash & (length - 1);  // 等价于 hash % length，但更快
    }
    
    // TODO: 学生实现put操作
    public V put(K key, V value) {
        return putVal(hash(key), key, value);
    }
    
    private V putVal(int hash, K key, V value) {
        Node<K, V>[] tab = table;
        int n = tab.length;
        int i = indexFor(hash, n);  // 计算数组索引
        
        Node<K, V> p = tab[i];
        if (p == null) {
            // 位置为空，直接放入
            tab[i] = new Node<>(hash, key, value, null);
        } else {
            // 发生哈希冲突，使用链表处理
            Node<K, V> e = null;
            K k = p.key;
            
            if (p.hash == hash && (k == key || (key != null && key.equals(k)))) {
                // key相同，覆盖value
                e = p;
            } else {
                // 遍历链表查找或添加到末尾
                while (true) {
                    if (p.next == null) {
                        p.next = new Node<>(hash, key, value, null);
                        break;
                    }
                    
                    p = p.next;
                    if (p.hash == hash && (k = p.key) == key || (key != null && key.equals(k))) {
                        e = p;
                        break;
                    }
                }
            }
            
            if (e != null) {
                // 覆盖已存在的key
                V oldValue = e.value;
                e.value = value;
                return oldValue;
            }
        }
        
        size++;
        
        // 检查是否需要扩容
        if (size > threshold) {
            resize();
        }
        
        return null;
    }
    
    // TODO: 学生实现get操作
    public V get(Object key) {
        int hash = hash(key);
        int i = indexFor(hash, table.length);
        
        Node<K, V> p = table[i];
        while (p != null) {
            if (p.hash == hash && (p.key == key || (key != null && key.equals(p.key)))) {
                return p.value;
            }
            p = p.next;
        }
        
        return null;  // 未找到
    }
    
    // TODO: 学生实现扩容机制
    @SuppressWarnings("unchecked")
    private void resize() {
        Node<K, V>[] oldTab = table;
        int oldCap = oldTab.length;
        int newCap = oldCap << 1;  // 容量翻倍
        
        Node<K, V>[] newTab = (Node<K, V>[]) new Node[newCap];
        table = newTab;
        threshold = (int) (newCap * LOAD_FACTOR);
        
        System.out.println("HashMap扩容: " + oldCap + " -> " + newCap);
        
        // 重新哈希所有元素
        for (int j = 0; j < oldCap; ++j) {
            Node<K, V> e = oldTab[j];
            if (e != null) {
                oldTab[j] = null;
                
                if (e.next == null) {
                    // 只有一个元素，直接放入新位置
                    newTab[indexFor(e.hash, newCap)] = e;
                } else {
                    // 有链表，需要分离
                    Node<K, V> loHead = null, loTail = null;  // 低位链表
                    Node<K, V> hiHead = null, hiTail = null;  // 高位链表
                    
                    Node<K, V> next;
                    do {
                        next = e.next;
                        if ((e.hash & oldCap) == 0) {  // 留在原位置
                            if (loTail == null) loHead = e;
                            else loTail.next = e;
                            loTail = e;
                        } else {  // 移到新位置
                            if (hiTail == null) hiHead = e;
                            else hiTail.next = e;
                            hiTail = e;
                        }
                    } while ((e = next) != null);
                    
                    if (loTail != null) {
                        loTail.next = null;
                        newTab[j] = loHead;
                    }
                    if (hiTail != null) {
                        hiTail.next = null;
                        newTab[j + oldCap] = hiHead;
                    }
                }
            }
        }
    }
    
    public int size() {
        return size;
    }
    
    // 打印哈希表结构，观察冲突分布
    public void printStructure() {
        System.out.println("=== HashMap内部结构 ===");
        for (int i = 0; i < table.length; i++) {
            if (table[i] != null) {
                System.out.print("Bucket[" + i + "]: ");
                Node<K, V> p = table[i];
                while (p != null) {
                    System.out.print(p + " -> ");
                    p = p.next;
                }
                System.out.println("null");
            }
        }
    }
    
    // 测试冲突和性能
    public static void main(String[] args) {
        MyHashMap<String, Integer> map = new MyHashMap<>();
        
        // 测试基本操作
        System.out.println("=== 基本操作测试 ===");
        map.put("apple", 1);
        map.put("banana", 2);
        map.put("orange", 3);
        map.put("grape", 4);
        
        System.out.println("apple: " + map.get("apple"));
        System.out.println("banana: " + map.get("banana"));
        System.out.println("size: " + map.size());
        
        map.printStructure();
        
        // 测试冲突处理
        System.out.println("\n=== 冲突测试 ===");
        // 故意制造冲突
        for (int i = 0; i < 20; i++) {
            map.put("key" + i, i);
        }
        
        map.printStructure();
        
        // 性能测试
        System.out.println("\n=== 性能测试 ===");
        MyHashMap<Integer, String> perfMap = new MyHashMap<>();
        
        long startTime = System.nanoTime();
        for (int i = 0; i < 100000; i++) {
            perfMap.put(i, "value" + i);
        }
        long endTime = System.nanoTime();
        System.out.println("插入100000个元素耗时: " + (endTime - startTime) / 1_000_000 + "ms");
        
        startTime = System.nanoTime();
        for (int i = 0; i < 100000; i += 100) {
            perfMap.get(i);
        }
        endTime = System.nanoTime();
        System.out.println("查找1000次耗时: " + (endTime - startTime) / 1_000_000 + "ms");
    }
}
```

**📝 编程任务检查点**:
- [ ] **哈希函数理解**: 理解为什么要高16位异或低16位
- [ ] **冲突处理**: 观察链表如何处理哈希冲突
- [ ] **扩容机制**: 理解为什么要重新哈希，以及高低位分离技巧
- [ ] **负载因子**: 理解0.75负载因子的意义
- [ ] **性能分析**: 对比ArrayList和HashMap的不同使用场景
- [ ] **代码位置**: `student_progress/JavaLearning/src/MyHashMap.java`

#### Task 1.2.3: LinkedList链表实现 + 双向链表操作 (5分钟) ⏰

**🎯 Primary Level (新手友好)**
- [ ] **什么是LinkedList**: 想象一串手链，每个珠子都知道下一个在哪里
- [ ] **简单理解**: LinkedList用链表存储数据，插入删除快，访问慢
- [ ] **生活类比**: 就像火车车厢，加车厢容易，但找特定车厢要一节节找
- [ ] **检查点**: 能说出"LinkedList和ArrayList的区别"

**🚀 Hands-On Coding Exercise (强制编程练习)**
```java
// 练习目标: 从零实现双向链表和操作
public class MyLinkedList<T> {
    
    // 内部节点类 - 双向链表节点
    private static class Node<T> {
        T data;
        Node<T> next;
        Node<T> prev;
        
        Node(T data) {
            this.data = data;
            this.next = null;
            this.prev = null;
        }
        
        Node(T data, Node<T> prev, Node<T> next) {
            this.data = data;
            this.prev = prev;
            this.next = next;
        }
        
        @Override
        public String toString() {
            return "Node(" + data + ")";
        }
    }
    
    private Node<T> head;    // 头节点
    private Node<T> tail;    // 尾节点
    private int size = 0;    // 列表大小
    
    public MyLinkedList() {
        head = null;
        tail = null;
    }
    
    // TODO: 学生实现在尾部添加元素
    public boolean add(T element) {
        Node<T> newNode = new Node<>(element);
        
        if (head == null) {
            // 第一个元素
            head = newNode;
            tail = newNode;
        } else {
            // 添加到尾部
            newNode.prev = tail;
            tail.next = newNode;
            tail = newNode;
        }
        
        size++;
        return true;
    }
    
    // TODO: 学生实现在指定位置插入元素
    public void add(int index, T element) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        
        if (index == size) {
            // 在尾部添加
            add(element);
            return;
        }
        
        Node<T> newNode = new Node<>(element);
        
        if (index == 0) {
            // 在头部插入
            newNode.next = head;
            head.prev = newNode;
            head = newNode;
        } else {
            // 在中间插入
            Node<T> current = getNodeAt(index);
            Node<T> prevNode = current.prev;
            
            newNode.prev = prevNode;
            newNode.next = current;
            prevNode.next = newNode;
            current.prev = newNode;
        }
        
        size++;
    }
    
    // TODO: 学生实现获取指定位置的元素
    public T get(int index) {
        Node<T> node = getNodeAt(index);
        return node.data;
    }
    
    // 优化: 双向搜索 - 从更近的端开始搜索
    private Node<T> getNodeAt(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        
        Node<T> current;
        
        if (index < size / 2) {
            // 从头部开始搜索
            current = head;
            for (int i = 0; i < index; i++) {
                current = current.next;
            }
        } else {
            // 从尾部开始搜索
            current = tail;
            for (int i = size - 1; i > index; i--) {
                current = current.prev;
            }
        }
        
        return current;
    }
    
    // TODO: 学生实现删除指定位置的元素
    public T remove(int index) {
        Node<T> nodeToRemove = getNodeAt(index);
        T data = nodeToRemove.data;
        
        if (size == 1) {
            // 只有一个元素
            head = null;
            tail = null;
        } else if (nodeToRemove == head) {
            // 删除头节点
            head = head.next;
            head.prev = null;
        } else if (nodeToRemove == tail) {
            // 删除尾节点
            tail = tail.prev;
            tail.next = null;
        } else {
            // 删除中间节点
            Node<T> prevNode = nodeToRemove.prev;
            Node<T> nextNode = nodeToRemove.next;
            prevNode.next = nextNode;
            nextNode.prev = prevNode;
        }
        
        // 清理被删除节点的引用
        nodeToRemove.prev = null;
        nodeToRemove.next = null;
        size--;
        
        return data;
    }
    
    // TODO: 学生实现查找元素
    public int indexOf(T element) {
        Node<T> current = head;
        int index = 0;
        
        while (current != null) {
            if (element == null ? current.data == null : element.equals(current.data)) {
                return index;
            }
            current = current.next;
            index++;
        }
        
        return -1;  // 未找到
    }
    
    // 作为队列使用
    public void addFirst(T element) {
        add(0, element);
    }
    
    public void addLast(T element) {
        add(element);
    }
    
    public T removeFirst() {
        if (isEmpty()) {
            throw new RuntimeException("List is empty");
        }
        return remove(0);
    }
    
    public T removeLast() {
        if (isEmpty()) {
            throw new RuntimeException("List is empty");
        }
        return remove(size - 1);
    }
    
    public T peekFirst() {
        return isEmpty() ? null : head.data;
    }
    
    public T peekLast() {
        return isEmpty() ? null : tail.data;
    }
    
    public int size() {
        return size;
    }
    
    public boolean isEmpty() {
        return size == 0;
    }
    
    // 打印链表结构
    public void printForward() {
        System.out.print("正向遍历: ");
        Node<T> current = head;
        while (current != null) {
            System.out.print(current.data + " -> ");
            current = current.next;
        }
        System.out.println("null");
    }
    
    public void printBackward() {
        System.out.print("反向遍历: ");
        Node<T> current = tail;
        while (current != null) {
            System.out.print(current.data + " -> ");
            current = current.prev;
        }
        System.out.println("null");
    }
    
    // 性能测试
    public void performanceTest() {
        System.out.println("\n=== LinkedList性能测试 ===");
        
        // 测试头部插入性能
        long startTime = System.nanoTime();
        for (int i = 0; i < 10000; i++) {
            addFirst(i);
        }
        long endTime = System.nanoTime();
        System.out.println("头部插入10000个元素耗时: " + (endTime - startTime) / 1_000_000 + "ms");
        
        // 测试随机访问性能
        startTime = System.nanoTime();
        for (int i = 0; i < 1000; i++) {
            get(i * 5);  // 随机访问
        }
        endTime = System.nanoTime();
        System.out.println("随机访问1000次耗时: " + (endTime - startTime) / 1_000_000 + "ms");
    }
    
    // 测试主函数
    public static void main(String[] args) {
        MyLinkedList<String> list = new MyLinkedList<>();
        
        // 基础操作测试
        System.out.println("=== LinkedList基础操作测试 ===");
        list.add("A");
        list.add("B");
        list.add("C");
        list.printForward();
        
        list.add(1, "X");  // 在位置1插入X
        list.printForward();
        
        System.out.println("删除索引2的元素: " + list.remove(2));
        list.printForward();
        list.printBackward();
        
        // 作为队列使用
        System.out.println("\n=== 作为双端队列使用 ===");
        MyLinkedList<Integer> deque = new MyLinkedList<>();
        
        deque.addFirst(1);
        deque.addLast(2);
        deque.addFirst(0);
        deque.addLast(3);
        deque.printForward();  // 0 -> 1 -> 2 -> 3
        
        System.out.println("从头部移除: " + deque.removeFirst());  // 0
        System.out.println("从尾部移除: " + deque.removeLast());   // 3
        deque.printForward();  // 1 -> 2
        
        // 性能测试
        MyLinkedList<Integer> perfList = new MyLinkedList<>();
        perfList.performanceTest();
        
        // 与ArrayList对比
        System.out.println("\n=== 与ArrayList性能对比 ===");
        compareWithArrayList();
    }
    
    // 与ArrayList性能对比
    private static void compareWithArrayList() {
        int testSize = 10000;
        
        // ArrayList测试
        java.util.ArrayList<Integer> arrayList = new java.util.ArrayList<>();
        long startTime = System.nanoTime();
        
        // 测试ArrayList的头部插入
        for (int i = 0; i < testSize; i++) {
            arrayList.add(0, i);  // 头部插入
        }
        long endTime = System.nanoTime();
        System.out.println("ArrayList头部插入" + testSize + "个元素: " + (endTime - startTime) / 1_000_000 + "ms");
        
        // LinkedList测试
        MyLinkedList<Integer> linkedList = new MyLinkedList<>();
        startTime = System.nanoTime();
        
        // 测试LinkedList的头部插入
        for (int i = 0; i < testSize; i++) {
            linkedList.addFirst(i);  // 头部插入
        }
        endTime = System.nanoTime();
        System.out.println("LinkedList头部插入" + testSize + "个元素: " + (endTime - startTime) / 1_000_000 + "ms");
        
        // 测试随机访问性能
        int accessCount = 1000;
        
        // ArrayList随机访问
        startTime = System.nanoTime();
        for (int i = 0; i < accessCount; i++) {
            arrayList.get(i * 5);
        }
        endTime = System.nanoTime();
        System.out.println("ArrayList随机访问" + accessCount + "次: " + (endTime - startTime) / 1_000_000 + "ms");
        
        // LinkedList随机访问
        startTime = System.nanoTime();
        for (int i = 0; i < accessCount; i++) {
            linkedList.get(i * 5);
        }
        endTime = System.nanoTime();
        System.out.println("LinkedList随机访问" + accessCount + "次: " + (endTime - startTime) / 1_000_000 + "ms");
    }
}
```

**📝 编程任务检查点**:
- [ ] **双向链表结构**: 理解prev和next指针的作用
- [ ] **插入删除**: 掌握链表的高效插入和删除操作
- [ ] **双向搜索优化**: 理解从更近端开始搜索的优化
- [ ] **双端队列**: 学会LinkedList作为双端队列的使用
- [ ] **性能对比**: 理解LinkedList和ArrayList的性能特点
- [ ] **内存开销**: 理解链表节点的内存开销
- [ ] **代码位置**: `student_progress/JavaLearning/src/MyLinkedList.java`

#### Task 1.2.4: HashMap扩容机制深入 + 重哈希优化 (5分钟) ⏰

**🎯 Primary Level (新手友好)**
- [ ] **什么是扩容**: 想象一个停车场，车太多了就需要扩建停车位
- [ ] **简单理解**: HashMap满了就需要创建更大的数组，把所有数据重新放置
- [ ] **生活类比**: 就像搬家到更大的房子，需要重新整理所有物品
- [ ] **检查点**: 能说出"为什么HashMap扩容要重新哈希"

**🚀 Hands-On Coding Exercise (强制编程练习)**
```java
import java.util.Arrays;

// 练习目标: 深入理解HashMap扩容机制和性能优化
public class HashMapResizingDeepDive {
    
    /**
     * 深入分析HashMap扩容的高级实现
     * 技术特性:
     * 1. 动态负载因子调整
     * 2. 高低位分离重哈希优化
     * 3. 扩容过程的性能监控
     * 4. 链表转红黑树优化模拟
     */
    public static class AdvancedHashMap<K, V> {
        
        // 内部节点类
        static class Node<K, V> {
            final int hash;
            final K key;
            V value;
            Node<K, V> next;
            
            Node(int hash, K key, V value, Node<K, V> next) {
                this.hash = hash;
                this.key = key;
                this.value = value;
                this.next = next;
            }
            
            @Override
            public String toString() {
                return key + "=" + value + "(hash=" + hash + ")";
            }
        }
        
        private Node<K, V>[] table;
        private int size = 0;
        private int threshold;
        private double loadFactor;
        
        // 扩容统计信息
        private int resizeCount = 0;
        private long totalResizeTime = 0;
        private int maxChainLength = 0;
        
        // 常量
        private static final int DEFAULT_CAPACITY = 16;
        private static final double DEFAULT_LOAD_FACTOR = 0.75;
        private static final int TREEIFY_THRESHOLD = 8;  // 链表转树阈值
        
        @SuppressWarnings("unchecked")
        public AdvancedHashMap() {
            this.loadFactor = DEFAULT_LOAD_FACTOR;
            this.table = (Node<K, V>[]) new Node[DEFAULT_CAPACITY];
            this.threshold = (int) (DEFAULT_CAPACITY * loadFactor);
        }
        
        @SuppressWarnings("unchecked")
        public AdvancedHashMap(int initialCapacity, double loadFactor) {
            this.loadFactor = loadFactor;
            this.table = (Node<K, V>[]) new Node[initialCapacity];
            this.threshold = (int) (initialCapacity * loadFactor);
        }
        
        // TODO: 学生实现高级哈希函数
        private int hash(Object key) {
            if (key == null) return 0;
            
            int h = key.hashCode();
            // 高级优化: 多次混合减少冲突
            h ^= (h >>> 20) ^ (h >>> 12);
            return h ^ (h >>> 7) ^ (h >>> 4);
        }
        
        private int indexFor(int hash, int length) {
            return hash & (length - 1);
        }
        
        // TODO: 学生实现扩容前的性能检查
        private boolean shouldResize() {
            if (size < threshold) return false;
            
            // 高级策略: 检查链表长度分布
            int longChainCount = 0;
            int maxLength = 0;
            
            for (Node<K, V> node : table) {
                if (node != null) {
                    int chainLength = 0;
                    Node<K, V> current = node;
                    while (current != null) {
                        chainLength++;
                        current = current.next;
                    }
                    
                    maxLength = Math.max(maxLength, chainLength);
                    if (chainLength > 4) {
                        longChainCount++;
                    }
                }
            }
            
            maxChainLength = maxLength;
            
            // 如果过多链表过长，即使未达到负载因子也要扩容
            return longChainCount > table.length * 0.1 || maxLength > TREEIFY_THRESHOLD;
        }
        
        // TODO: 学生实现高效扩容算法
        @SuppressWarnings("unchecked")
        private void resize() {
            long startTime = System.nanoTime();
            
            Node<K, V>[] oldTab = table;
            int oldCap = oldTab.length;
            int newCap = oldCap << 1;  // 双倍扩容
            
            if (newCap >= 1 << 30) {
                threshold = Integer.MAX_VALUE;
                return;
            }
            
            System.out.println(String.format(
                "\n=== 扩容开始 ===\n" +
                "原容量: %d, 新容量: %d\n" +
                "当前大小: %d, 负载因子: %.2f\n" +
                "最长链表: %d",
                oldCap, newCap, size, (double) size / oldCap, maxChainLength
            ));
            
            Node<K, V>[] newTab = (Node<K, V>[]) new Node[newCap];
            table = newTab;
            threshold = (int) (newCap * loadFactor);
            
            // 统计重哈希信息
            int movedNodes = 0;
            int splitOperations = 0;
            
            // 重哈希所有元素
            for (int j = 0; j < oldCap; ++j) {
                Node<K, V> e = oldTab[j];
                if (e != null) {
                    oldTab[j] = null;
                    
                    if (e.next == null) {
                        // 单个节点，直接重新放置
                        newTab[indexFor(e.hash, newCap)] = e;
                        movedNodes++;
                    } else {
                        // 链表分离: 高低位分离优化
                        Node<K, V> loHead = null, loTail = null;  // 低位链表
                        Node<K, V> hiHead = null, hiTail = null;  // 高位链表
                        
                        Node<K, V> next;
                        do {
                            next = e.next;
                            movedNodes++;
                            
                            // 高低位分离技巧
                            if ((e.hash & oldCap) == 0) {
                                // 低位: 保持在原位置
                                if (loTail == null) {
                                    loHead = e;
                                } else {
                                    loTail.next = e;
                                }
                                loTail = e;
                            } else {
                                // 高位: 移动到 j + oldCap 位置
                                if (hiTail == null) {
                                    hiHead = e;
                                } else {
                                    hiTail.next = e;
                                }
                                hiTail = e;
                            }
                        } while ((e = next) != null);
                        
                        // 放置分离后的链表
                        if (loTail != null) {
                            loTail.next = null;
                            newTab[j] = loHead;
                        }
                        if (hiTail != null) {
                            hiTail.next = null;
                            newTab[j + oldCap] = hiHead;
                            splitOperations++;
                        }
                    }
                }
            }
            
            long endTime = System.nanoTime();
            long resizeTime = endTime - startTime;
            totalResizeTime += resizeTime;
            resizeCount++;
            
            System.out.println(String.format(
                "移动节点数: %d\n" +
                "分离操作数: %d\n" +
                "扩容耗时: %.2f ms\n" +
                "=== 扩容完成 ===\n",
                movedNodes, splitOperations, resizeTime / 1_000_000.0
            ));
        }
        
        // TODO: 学生实现put操作与扩容集成
        public V put(K key, V value) {
            int hash = hash(key);
            int i = indexFor(hash, table.length);
            
            // 查找是否已存在
            for (Node<K, V> e = table[i]; e != null; e = e.next) {
                if (e.hash == hash && (e.key == key || (key != null && key.equals(e.key)))) {
                    V oldValue = e.value;
                    e.value = value;
                    return oldValue;
                }
            }
            
            // 新增节点
            addEntry(hash, key, value, i);
            return null;
        }
        
        private void addEntry(int hash, K key, V value, int bucketIndex) {
            // 检查是否需要扩容
            if (shouldResize()) {
                resize();
                bucketIndex = indexFor(hash, table.length);
            }
            
            Node<K, V> e = table[bucketIndex];
            table[bucketIndex] = new Node<>(hash, key, value, e);
            size++;
        }
        
        public V get(Object key) {
            int hash = hash(key);
            int i = indexFor(hash, table.length);
            
            for (Node<K, V> e = table[i]; e != null; e = e.next) {
                if (e.hash == hash && (e.key == key || (key != null && key.equals(e.key)))) {
                    return e.value;
                }
            }
            return null;
        }
        
        // TODO: 学生实现性能分析方法
        public void printPerformanceStats() {
            System.out.println("\n=== HashMap性能统计 ===");
            System.out.println("当前大小: " + size);
            System.out.println("当前容量: " + table.length);
            System.out.println("负载因子: " + String.format("%.2f", (double) size / table.length));
            System.out.println("扩容次数: " + resizeCount);
            System.out.println("总扩容耗时: " + String.format("%.2f ms", totalResizeTime / 1_000_000.0));
            
            if (resizeCount > 0) {
                System.out.println("平均扩容耗时: " + String.format("%.2f ms", totalResizeTime / resizeCount / 1_000_000.0));
            }
            
            // 分析链表分布
            analyzeChainDistribution();
        }
        
        private void analyzeChainDistribution() {
            int[] chainLengths = new int[table.length];
            int maxChain = 0;
            int totalChains = 0;
            int emptyBuckets = 0;
            
            for (int i = 0; i < table.length; i++) {
                int length = 0;
                Node<K, V> node = table[i];
                
                if (node == null) {
                    emptyBuckets++;
                } else {
                    while (node != null) {
                        length++;
                        node = node.next;
                    }
                    totalChains++;
                }
                
                chainLengths[i] = length;
                maxChain = Math.max(maxChain, length);
            }
            
            System.out.println("\n--- 链表分布分析 ---");
            System.out.println("空桶数量: " + emptyBuckets + " (" + String.format("%.1f%%", 100.0 * emptyBuckets / table.length) + ")");
            System.out.println("最长链表: " + maxChain);
            System.out.println("平均链表长度: " + String.format("%.2f", (double) size / Math.max(1, totalChains)));
            
            // 统计不同长度链表的数量
            int[] lengthCount = new int[maxChain + 1];
            for (int length : chainLengths) {
                lengthCount[length]++;
            }
            
            System.out.println("链表长度分布:");
            for (int i = 0; i <= Math.min(maxChain, 10); i++) {
                if (lengthCount[i] > 0) {
                    System.out.println("  长度" + i + ": " + lengthCount[i] + "个桶");
                }
            }
        }
        
        public void printDetailedStructure() {
            System.out.println("\n=== HashMap详细结构 ===");
            for (int i = 0; i < table.length; i++) {
                if (table[i] != null) {
                    System.out.print("桶[" + i + "]: ");
                    Node<K, V> node = table[i];
                    while (node != null) {
                        System.out.print(node + " -> ");
                        node = node.next;
                    }
                    System.out.println("null");
                }
            }
        }
        
        public int size() {
            return size;
        }
    }
    
    // TODO: 学生实现扩容性能测试
    public static void performResizePerformanceTest() {
        System.out.println("=== HashMap扩容性能测试 ===");
        
        // 测试不同初始容量的影响
        int[] initialCapacities = {1, 16, 256, 1024};
        int totalInsertions = 100000;
        
        for (int capacity : initialCapacities) {
            System.out.println("\n--- 初始容量: " + capacity + " ---");
            
            AdvancedHashMap<String, Integer> map = new AdvancedHashMap<>(capacity, 0.75);
            
            long startTime = System.nanoTime();
            
            for (int i = 0; i < totalInsertions; i++) {
                map.put("key" + i, i);
                
                // 每10000次输出一次进度
                if (i > 0 && i % 10000 == 0) {
                    System.out.println("已插入: " + i + ", 当前容量: " + map.table.length);
                }
            }
            
            long endTime = System.nanoTime();
            
            System.out.println("总耗时: " + (endTime - startTime) / 1_000_000 + "ms");
            map.printPerformanceStats();
        }
    }
    
    // TODO: 学生实现哈希冲突模拟
    public static void simulateHashCollisions() {
        System.out.println("\n=== 哈希冲突模拟 ===");
        
        AdvancedHashMap<CollisionKey, String> map = new AdvancedHashMap<>(16, 0.75);
        
        // 创建故意冲突的键
        System.out.println("添加故意冲突的键...");
        for (int i = 0; i < 50; i++) {
            CollisionKey key = new CollisionKey("key" + i, 1);  // 相同的哈希值
            map.put(key, "value" + i);
        }
        
        System.out.println("添加完成，分析结构:");
        map.printPerformanceStats();
        map.printDetailedStructure();
        
        // 测试查找性能
        System.out.println("\n测试查找性能:");
        long startTime = System.nanoTime();
        
        for (int i = 0; i < 1000; i++) {
            for (int j = 0; j < 50; j++) {
                CollisionKey key = new CollisionKey("key" + j, 1);
                map.get(key);
            }
        }
        
        long endTime = System.nanoTime();
        System.out.println("查找50000次耗时: " + (endTime - startTime) / 1_000_000 + "ms");
        System.out.println("平均每次查找: " + (endTime - startTime) / 50000 + "ns");
    }
    
    // 故意冲突的键类
    static class CollisionKey {
        private final String value;
        private final int hashCode;
        
        public CollisionKey(String value, int hashCode) {
            this.value = value;
            this.hashCode = hashCode;
        }
        
        @Override
        public int hashCode() {
            return hashCode;  // 故意返回相同的哈希值
        }
        
        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            CollisionKey that = (CollisionKey) obj;
            return value.equals(that.value);
        }
        
        @Override
        public String toString() {
            return value;
        }
    }
    
    public static void main(String[] args) {
        // 扩容性能测试
        performResizePerformanceTest();
        
        // 哈希冲突模拟
        simulateHashCollisions();
        
        System.out.println("\nHashMap扩容机制深入分析完成！");
    }
}
```

**📝 编程任务检查点**:
- [ ] **扩容触发**: 理解负载因子和链表长度对扩容的影响
- [ ] **重哈希算法**: 掌握高低位分离优化技巧
- [ ] **性能监控**: 学会统计和分析HashMap的性能指标
- [ ] **冲突处理**: 理解哈希冲突对性能的影响
- [ ] **内存优化**: 了解扩容过程中的内存分配和回收
- [ ] **生产环境**: 掌握在高并发环境下的HashMap优化策略
- [ ] **代码位置**: `student_progress/JavaLearning/src/HashMapResizingDeepDive.java`

#### Task 1.2.5: ConcurrentHashMap分段锁机制 + 高并发优化 (5分钟) ⏰

**🎯 Primary Level (新手友好)**
- [ ] **什么是分段锁**: 想象一个大型停车场分成多个区域，每个区域独立管理
- [ ] **简单理解**: ConcurrentHashMap把数据分成多个段，每个段独立加锁
- [ ] **生活类比**: 就像银行多个窗口同时服务，不用排一个长队
- [ ] **检查点**: 能说出"分段锁如何提高并发性能"

**🚀 Hands-On Coding Exercise (强制编程练习)**
```java
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.CountDownLatch;

// 练习目标: 从零实现ConcurrentHashMap的分段锁机制
public class ConcurrentHashMapSegmentation {
    
    /**
     * 分段锁ConcurrentHashMap实现
     * 技术特性:
     * 1. Segment数组实现分段锁
     * 2. 每个Segment独立的ReentrantLock
     * 3. 精细粒度的锁竞争控制
     * 4. 无锁读操作优化
     */
    public static class SegmentedConcurrentHashMap<K, V> {
        
        // 内部节点类
        static class Node<K, V> {
            final int hash;
            final K key;
            volatile V value;  // volatile保证可见性
            volatile Node<K, V> next;
            
            Node(int hash, K key, V value, Node<K, V> next) {
                this.hash = hash;
                this.key = key;
                this.value = value;
                this.next = next;
            }
            
            @Override
            public String toString() {
                return key + "=" + value;
            }
        }
        
        /**
         * Segment类 - 每个段都是一个独立的HashMap
         */
        static class Segment<K, V> extends ReentrantLock {
            volatile Node<K, V>[] table;
            volatile int size;
            volatile int threshold;
            final double loadFactor;
            volatile int modCount;  // 修改计数器
            
            // 统计信息
            final AtomicInteger lockAcquisitions = new AtomicInteger(0);
            final AtomicInteger lockContentions = new AtomicInteger(0);
            
            @SuppressWarnings("unchecked")
            Segment(int initialCapacity, double loadFactor) {
                this.loadFactor = loadFactor;
                this.table = (Node<K, V>[]) new Node[initialCapacity];
                this.threshold = (int) (initialCapacity * loadFactor);
            }
            
            // TODO: 学生实现线程安全的put操作
            V put(K key, int hash, V value, boolean onlyIfAbsent) {
                boolean acquired = tryLock();
                if (!acquired) {
                    lockContentions.incrementAndGet();
                    lock();  // 阻塞等待锁
                }
                lockAcquisitions.incrementAndGet();
                
                try {
                    Node<K, V>[] tab = table;
                    int index = hash & (tab.length - 1);
                    Node<K, V> first = tab[index];
                    
                    // 查找现有节点
                    for (Node<K, V> e = first; e != null; e = e.next) {
                        if (e.hash == hash && (e.key == key || key.equals(e.key))) {
                            V oldValue = e.value;
                            if (!onlyIfAbsent) {
                                e.value = value;
                            }
                            return oldValue;
                        }
                    }
                    
                    // 新增节点
                    modCount++;
                    Node<K, V> newNode = new Node<>(hash, key, value, first);
                    tab[index] = newNode;
                    size++;
                    
                    // 检查是否需要扩容
                    if (size >= threshold) {
                        rehash();
                    }
                    
                    return null;
                    
                } finally {
                    unlock();
                }
            }
            
            // TODO: 学生实现无锁读操作
            V get(Object key, int hash) {
                // 无锁读取，依赖volatile语义保证可见性
                if (size != 0) {
                    Node<K, V>[] tab = table;
                    if (tab != null) {
                        int index = hash & (tab.length - 1);
                        Node<K, V> e = tab[index];
                        
                        while (e != null) {
                            if (e.hash == hash && (e.key == key || key.equals(e.key))) {
                                return e.value;
                            }
                            e = e.next;
                        }
                    }
                }
                return null;
            }
            
            // TODO: 学生实现remove操作
            V remove(Object key, int hash, Object value) {
                lock();
                try {
                    Node<K, V>[] tab = table;
                    int index = hash & (tab.length - 1);
                    Node<K, V> first = tab[index];
                    
                    Node<K, V> e = first;
                    while (e != null && (e.hash != hash || !key.equals(e.key))) {
                        e = e.next;
                    }
                    
                    V oldValue = null;
                    if (e != null) {
                        V v = e.value;
                        if (value == null || value.equals(v)) {
                            oldValue = v;
                            modCount++;
                            
                            // 移除节点
                            Node<K, V> newFirst = first;
                            for (Node<K, V> p = first; p != e; p = p.next) {
                                newFirst = new Node<>(p.hash, p.key, p.value, newFirst);
                            }
                            tab[index] = newFirst;
                            size--;
                        }
                    }
                    return oldValue;
                    
                } finally {
                    unlock();
                }
            }
            
            // Segment内部扩容
            @SuppressWarnings("unchecked")
            private void rehash() {
                Node<K, V>[] oldTable = table;
                int oldCapacity = oldTable.length;
                int newCapacity = oldCapacity << 1;
                
                threshold = (int) (newCapacity * loadFactor);
                Node<K, V>[] newTable = (Node<K, V>[]) new Node[newCapacity];
                int sizeMask = newCapacity - 1;
                
                for (int i = 0; i < oldCapacity; i++) {
                    Node<K, V> e = oldTable[i];
                    if (e != null) {
                        Node<K, V> next = e.next;
                        int idx = e.hash & sizeMask;
                        
                        if (next == null) {
                            newTable[idx] = e;
                        } else {
                            // 有链表，需要分离
                            Node<K, V> lastRun = e;
                            int lastIdx = idx;
                            
                            for (Node<K, V> last = next; last != null; last = last.next) {
                                int k = last.hash & sizeMask;
                                if (k != lastIdx) {
                                    lastIdx = k;
                                    lastRun = last;
                                }
                            }
                            
                            newTable[lastIdx] = lastRun;
                            
                            for (Node<K, V> p = e; p != lastRun; p = p.next) {
                                int k = p.hash & sizeMask;
                                Node<K, V> n = newTable[k];
                                newTable[k] = new Node<>(p.hash, p.key, p.value, n);
                            }
                        }
                    }
                }
                
                table = newTable;
            }
            
            // 获取统计信息
            public SegmentStats getStats() {
                return new SegmentStats(size, table.length, lockAcquisitions.get(), lockContentions.get());
            }
        }
        
        // Segment统计信息
        static class SegmentStats {
            final int size;
            final int capacity;
            final int lockAcquisitions;
            final int lockContentions;
            
            SegmentStats(int size, int capacity, int lockAcquisitions, int lockContentions) {
                this.size = size;
                this.capacity = capacity;
                this.lockAcquisitions = lockAcquisitions;
                this.lockContentions = lockContentions;
            }
        }
        
        // 主类属性
        private final Segment<K, V>[] segments;
        private final int segmentShift;
        private final int segmentMask;
        
        private static final int DEFAULT_INITIAL_CAPACITY = 16;
        private static final double DEFAULT_LOAD_FACTOR = 0.75;
        private static final int DEFAULT_CONCURRENCY_LEVEL = 16;
        
        // TODO: 学生实现构造函数和分段初始化
        @SuppressWarnings("unchecked")
        public SegmentedConcurrentHashMap() {
            this(DEFAULT_INITIAL_CAPACITY, DEFAULT_LOAD_FACTOR, DEFAULT_CONCURRENCY_LEVEL);
        }
        
        @SuppressWarnings("unchecked")
        public SegmentedConcurrentHashMap(int initialCapacity, double loadFactor, int concurrencyLevel) {
            // 计算segment数量（必须是2的幂次）
            int segmentShift = 0;
            int segmentCount = 1;
            while (segmentCount < concurrencyLevel) {
                segmentShift++;
                segmentCount <<= 1;
            }
            
            this.segmentShift = 32 - segmentShift;
            this.segmentMask = segmentCount - 1;
            
            // 初始化segments
            this.segments = (Segment<K, V>[]) new Segment[segmentCount];
            
            int segmentCapacity = initialCapacity / segmentCount;
            if (segmentCapacity * segmentCount < initialCapacity) {
                segmentCapacity++;
            }
            
            // 使用懒初始化，只在需要时创建具体的segment
            for (int i = 0; i < segments.length; i++) {
                segments[i] = new Segment<>(segmentCapacity, loadFactor);
            }
            
            System.out.println(String.format(
                "ConcurrentHashMap初始化:\n" +
                "  Segment数量: %d\n" +
                "  每个Segment容量: %d\n" +
                "  SegmentShift: %d\n" +
                "  SegmentMask: 0x%X",
                segmentCount, segmentCapacity, this.segmentShift, this.segmentMask
            ));
        }
        
        // TODO: 学生实现哈希函数和segment定位
        private int hash(Object key) {
            int h = key.hashCode();
            // 两次散列，提高哈希质量
            h += (h << 15) ^ 0xffffcd7d;
            h ^= (h >>> 10);
            h += (h << 3);
            h ^= (h >>> 6);
            h += (h << 2) + (h << 14);
            return h ^ (h >>> 16);
        }
        
        private Segment<K, V> segmentFor(int hash) {
            return segments[(hash >>> segmentShift) & segmentMask];
        }
        
        // TODO: 学生实现公共API
        public V put(K key, V value) {
            if (value == null) {
                throw new NullPointerException();
            }
            
            int hash = hash(key);
            return segmentFor(hash).put(key, hash, value, false);
        }
        
        public V get(Object key) {
            int hash = hash(key);
            return segmentFor(hash).get(key, hash);
        }
        
        public V remove(Object key) {
            int hash = hash(key);
            return segmentFor(hash).remove(key, hash, null);
        }
        
        public boolean containsKey(Object key) {
            int hash = hash(key);
            return segmentFor(hash).get(key, hash) != null;
        }
        
        // TODO: 学生实现size计算（这是一个有趣的挑战）
        public int size() {
            final Segment<K, V>[] segments = this.segments;
            long sum = 0;
            long check = 0;
            int[] mc = new int[segments.length];
            
            // 尝试无锁计算
            for (int k = 0; k < 3; k++) {
                check = 0;
                sum = 0;
                int mcsum = 0;
                
                for (int i = 0; i < segments.length; ++i) {
                    sum += segments[i].size;
                    mcsum += mc[i] = segments[i].modCount;
                }
                
                if (mcsum != 0) {
                    for (int i = 0; i < segments.length; ++i) {
                        check += segments[i].size;
                        if (mc[i] != segments[i].modCount) {
                            check = -1; // 有修改，需要重试
                            break;
                        }
                    }
                }
                
                if (check == sum) {
                    break;
                }
            }
            
            // 如果无锁计算失败，使用加锁方式
            if (check != sum) {
                sum = 0;
                for (Segment<K, V> segment : segments) {
                    segment.lock();
                }
                
                try {
                    for (Segment<K, V> segment : segments) {
                        sum += segment.size;
                    }
                } finally {
                    for (Segment<K, V> segment : segments) {
                        segment.unlock();
                    }
                }
            }
            
            return sum > Integer.MAX_VALUE ? Integer.MAX_VALUE : (int) sum;
        }
        
        // TODO: 学生实现性能监控
        public void printPerformanceStats() {
            System.out.println("\n=== ConcurrentHashMap性能统计 ===");
            System.out.println("总大小: " + size());
            System.out.println("Segment数量: " + segments.length);
            
            int totalLockAcquisitions = 0;
            int totalLockContentions = 0;
            int totalCapacity = 0;
            
            for (int i = 0; i < segments.length; i++) {
                SegmentStats stats = segments[i].getStats();
                totalLockAcquisitions += stats.lockAcquisitions;
                totalLockContentions += stats.lockContentions;
                totalCapacity += stats.capacity;
                
                System.out.println(String.format(
                    "Segment[%d]: 大小=%d, 容量=%d, 锁获取=%d, 锁竞争=%d",
                    i, stats.size, stats.capacity, stats.lockAcquisitions, stats.lockContentions
                ));
            }
            
            System.out.println(String.format(
                "\n汇总统计:\n" +
                "  总容量: %d\n" +
                "  锁获取总数: %d\n" +
                "  锁竞争总数: %d\n" +
                "  竞争率: %.2f%%",
                totalCapacity, totalLockAcquisitions, totalLockContentions,
                totalLockAcquisitions > 0 ? (double) totalLockContentions / totalLockAcquisitions * 100 : 0
            ));
        }
    }
    
    // TODO: 学生实现并发性能测试
    public static void performConcurrencyTest() throws InterruptedException {
        System.out.println("\n=== 并发性能测试 ===");
        
        final int THREAD_COUNT = 16;
        final int OPERATIONS_PER_THREAD = 10000;
        
        // 测试自定义ConcurrentHashMap
        SegmentedConcurrentHashMap<String, Integer> ourMap = new SegmentedConcurrentHashMap<>();
        
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch completeLatch = new CountDownLatch(THREAD_COUNT);
        
        // 创建测试线程
        for (int i = 0; i < THREAD_COUNT; i++) {
            final int threadId = i;
            new Thread(() -> {
                try {
                    startLatch.await();  // 等待同时开始
                    
                    // 混合操作: 70%写入，30%读取
                    for (int j = 0; j < OPERATIONS_PER_THREAD; j++) {
                        String key = "key" + (threadId * OPERATIONS_PER_THREAD + j);
                        
                        if (j % 10 < 7) {
                            // 写入操作
                            ourMap.put(key, j);
                        } else {
                            // 读取操作
                            ourMap.get(key);
                        }
                        
                        // 偶尔删除
                        if (j % 50 == 0 && j > 0) {
                            String removeKey = "key" + (threadId * OPERATIONS_PER_THREAD + j - 25);
                            ourMap.remove(removeKey);
                        }
                    }
                    
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    completeLatch.countDown();
                }
            }, "TestThread-" + i).start();
        }
        
        long startTime = System.nanoTime();
        startLatch.countDown();  // 开始测试
        completeLatch.await();   // 等待完成
        long endTime = System.nanoTime();
        
        System.out.println(String.format(
            "\n并发测试结果:\n" +
            "  线程数: %d\n" +
            "  每线程操作数: %d\n" +
            "  总操作数: %d\n" +
            "  总耗时: %.2f ms\n" +
            "  平均吸吐量: %.0f ops/sec",
            THREAD_COUNT, OPERATIONS_PER_THREAD, THREAD_COUNT * OPERATIONS_PER_THREAD,
            (endTime - startTime) / 1_000_000.0,
            (double) THREAD_COUNT * OPERATIONS_PER_THREAD / ((endTime - startTime) / 1_000_000_000.0)
        ));
        
        ourMap.printPerformanceStats();
    }
    
    // TODO: 学生实现与标准ConcurrentHashMap对比
    public static void compareWithStandardConcurrentHashMap() throws InterruptedException {
        System.out.println("\n=== 与标准ConcurrentHashMap对比 ===");
        
        final int THREAD_COUNT = 8;
        final int OPERATIONS = 50000;
        
        // 测试标准ConcurrentHashMap
        java.util.concurrent.ConcurrentHashMap<String, Integer> standardMap = 
            new java.util.concurrent.ConcurrentHashMap<>();
        
        long startTime = System.nanoTime();
        
        CountDownLatch standardLatch = new CountDownLatch(THREAD_COUNT);
        
        for (int i = 0; i < THREAD_COUNT; i++) {
            final int threadId = i;
            new Thread(() -> {
                for (int j = 0; j < OPERATIONS; j++) {
                    String key = "key" + (threadId * OPERATIONS + j);
                    standardMap.put(key, j);
                    if (j % 3 == 0) {
                        standardMap.get(key);
                    }
                }
                standardLatch.countDown();
            }).start();
        }
        
        standardLatch.await();
        long standardTime = System.nanoTime() - startTime;
        
        // 测试自定义ConcurrentHashMap
        SegmentedConcurrentHashMap<String, Integer> ourMap = new SegmentedConcurrentHashMap<>();
        
        startTime = System.nanoTime();
        
        CountDownLatch ourLatch = new CountDownLatch(THREAD_COUNT);
        
        for (int i = 0; i < THREAD_COUNT; i++) {
            final int threadId = i;
            new Thread(() -> {
                for (int j = 0; j < OPERATIONS; j++) {
                    String key = "key" + (threadId * OPERATIONS + j);
                    ourMap.put(key, j);
                    if (j % 3 == 0) {
                        ourMap.get(key);
                    }
                }
                ourLatch.countDown();
            }).start();
        }
        
        ourLatch.await();
        long ourTime = System.nanoTime() - startTime;
        
        System.out.println(String.format(
            "\n性能对比结果:\n" +
            "  标准ConcurrentHashMap: %.2f ms\n" +
            "  自定义分段锁Map: %.2f ms\n" +
            "  性能比率: %.2fx\n" +
            "  标准Map大小: %d\n" +
            "  自定义Map大小: %d",
            standardTime / 1_000_000.0, ourTime / 1_000_000.0,
            (double) ourTime / standardTime,
            standardMap.size(), ourMap.size()
        ));
    }
    
    public static void main(String[] args) throws InterruptedException {
        // 并发性能测试
        performConcurrencyTest();
        
        // 与标准实现对比
        compareWithStandardConcurrentHashMap();
        
        System.out.println("\nConcurrentHashMap分段锁机制演示完成！");
    }
}
```

**📝 编程任务检查点**:
- [ ] **分段锁原理**: 理解Segment数组如何实现并发控制
- [ ] **无锁读操作**: 掌握volatile语义如何支持无锁读取
- [ ] **锁粒度优化**: 理解精细粒度锁对并发性能的提升
- [ ] **性能监控**: 学会统计和分析锁竞争情况
- [ ] **size计算**: 理解分布式环境下size计算的挑战
- [ ] **生产级优化**: 掌握高并发场景下的性能调优策略
- [ ] **代码位置**: `student_progress/JavaLearning/src/ConcurrentHashMapSegmentation.java`

#### Task 1.2.6: 生产级LRU缓存实现 + 内存优化策略 (5分钟) ⏰

**🎯 Primary Level (新手友好)**
- [ ] **什么是LRU缓存**: 想象一个书架，新书放前面，老书放后面，没地方就把最后面的书扔掉
- [ ] **简单理解**: LRU = Least Recently Used，最近最少使用的数据首先被淘汰
- [ ] **生活类比**: 就像手机后台程序管理，内存不够就关闭最久未用的应用
- [ ] **检查点**: 能说出"LRU缓存的淘汰策略原理"

**🚀 Hands-On Coding Exercise (强制编程练习)**
```java
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.atomic.AtomicLong;
import java.util.HashMap;
import java.util.Map;

// 练习目标: 实现生产级的高性能LRU缓存系统
public class ProductionLRUCache {
    
    /**
     * 企业级LRU缓存实现
     * 技术特性:
     * 1. 双向链表 + HashMap 实现O(1)复杂度
     * 2. 读写锁优化并发性能
     * 3. 内存监控和自动清理
     * 4. 统计信息和性能分析
     * 5. 缓存穿透防护和预加载
     */
    public static class EnterpriseGradeLRUCache<K, V> {
        
        /**
         * 双向链表节点
         */
        static class CacheNode<K, V> {
            K key;
            V value;
            CacheNode<K, V> prev;
            CacheNode<K, V> next;
            long accessTime;
            long createTime;
            int accessCount;
            
            CacheNode() {
                this.accessTime = System.currentTimeMillis();
                this.createTime = accessTime;
                this.accessCount = 0;
            }
            
            CacheNode(K key, V value) {
                this();
                this.key = key;
                this.value = value;
            }
            
            void updateAccess() {
                this.accessTime = System.currentTimeMillis();
                this.accessCount++;
            }
            
            @Override
            public String toString() {
                return String.format("%s=%s(access=%d)", key, value, accessCount);
            }
        }
        
        /**
         * 缓存统计信息
         */
        public static class CacheStats {
            final long hitCount;
            final long missCount;
            final long evictionCount;
            final long currentSize;
            final long maxSize;
            final double hitRate;
            final long averageLoadTime;
            final long totalMemoryUsed;
            
            CacheStats(long hitCount, long missCount, long evictionCount, 
                      long currentSize, long maxSize, long averageLoadTime, long totalMemoryUsed) {
                this.hitCount = hitCount;
                this.missCount = missCount;
                this.evictionCount = evictionCount;
                this.currentSize = currentSize;
                this.maxSize = maxSize;
                this.hitRate = hitCount + missCount > 0 ? (double) hitCount / (hitCount + missCount) : 0.0;
                this.averageLoadTime = averageLoadTime;
                this.totalMemoryUsed = totalMemoryUsed;
            }
            
            @Override
            public String toString() {
                return String.format(
                    "CacheStats{hits=%d, misses=%d, evictions=%d, size=%d/%d, hitRate=%.2f%%, avgLoadTime=%dms, memory=%dKB}",
                    hitCount, missCount, evictionCount, currentSize, maxSize, hitRate * 100, averageLoadTime, totalMemoryUsed / 1024
                );
            }
        }
        
        // 核心属性
        private final int maxSize;
        private final Map<K, CacheNode<K, V>> cacheMap;
        private final CacheNode<K, V> head;  // 哨兵节点
        private final CacheNode<K, V> tail;  // 哨兵节点
        
        // 并发控制
        private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
        private final ReentrantReadWriteLock.ReadLock readLock = lock.readLock();
        private final ReentrantReadWriteLock.WriteLock writeLock = lock.writeLock();
        
        // 统计信息
        private final AtomicLong hitCount = new AtomicLong(0);
        private final AtomicLong missCount = new AtomicLong(0);
        private final AtomicLong evictionCount = new AtomicLong(0);
        private final AtomicLong totalLoadTime = new AtomicLong(0);
        private final AtomicLong loadOperations = new AtomicLong(0);
        
        // 内存管理
        private volatile long estimatedMemoryUsage = 0;
        private final int nodeOverhead = 64; // 估算每个节点的内存开销
        
        public EnterpriseGradeLRUCache(int maxSize) {
            this.maxSize = maxSize;
            this.cacheMap = new HashMap<>(maxSize);
            
            // 初始化哨兵节点
            this.head = new CacheNode<>();
            this.tail = new CacheNode<>();
            head.next = tail;
            tail.prev = head;
            
            System.out.println("LRU缓存初始化完成，最大容量: " + maxSize);
        }
        
        // TODO: 学生实现线程安全的get操作
        public V get(K key) {
            if (key == null) {
                throw new IllegalArgumentException("Key cannot be null");
            }
            
            readLock.lock();
            try {
                CacheNode<K, V> node = cacheMap.get(key);
                if (node == null) {
                    missCount.incrementAndGet();
                    return null;
                }
                
                // 命中，更新访问信息并移动到头部
                hitCount.incrementAndGet();
                node.updateAccess();
                
                // 需要升级为写锁来移动节点
                readLock.unlock();
                writeLock.lock();
                try {
                    // 双重检查，防止并发修改
                    if (cacheMap.containsKey(key)) {
                        moveToHead(node);
                    }
                    return node.value;
                } finally {
                    writeLock.unlock();
                }
                
            } finally {
                // 确保释放读锁
                if (lock.getReadHoldCount() > 0) {
                    readLock.unlock();
                }
            }
        }
        
        // TODO: 学生实现线程安全的put操作
        public V put(K key, V value) {
            if (key == null || value == null) {
                throw new IllegalArgumentException("Key and value cannot be null");
            }
            
            long startTime = System.nanoTime();
            
            writeLock.lock();
            try {
                CacheNode<K, V> existingNode = cacheMap.get(key);
                
                if (existingNode != null) {
                    // 更新现有节点
                    V oldValue = existingNode.value;
                    existingNode.value = value;
                    existingNode.updateAccess();
                    moveToHead(existingNode);
                    
                    updateMemoryUsage(value, oldValue);
                    return oldValue;
                } else {
                    // 新增节点
                    CacheNode<K, V> newNode = new CacheNode<>(key, value);
                    
                    // 检查容量限制
                    if (cacheMap.size() >= maxSize) {
                        evictLRU();
                    }
                    
                    cacheMap.put(key, newNode);
                    addToHead(newNode);
                    
                    updateMemoryUsage(value, null);
                    
                    long endTime = System.nanoTime();
                    totalLoadTime.addAndGet((endTime - startTime) / 1_000_000);
                    loadOperations.incrementAndGet();
                    
                    return null;
                }
                
            } finally {
                writeLock.unlock();
            }
        }
        
        // TODO: 学生实现LRU淘汰算法
        private void evictLRU() {
            CacheNode<K, V> lastNode = tail.prev;
            if (lastNode != head) {
                removeNode(lastNode);
                cacheMap.remove(lastNode.key);
                evictionCount.incrementAndGet();
                
                updateMemoryUsage(null, lastNode.value);
                
                System.out.println("LRU淘汰: " + lastNode.key + 
                    " (访问次数: " + lastNode.accessCount + 
                    ", 最后访问: " + (System.currentTimeMillis() - lastNode.accessTime) + "ms前)");
            }
        }
        
        // TODO: 学生实现双向链表操作
        private void addToHead(CacheNode<K, V> node) {
            node.prev = head;
            node.next = head.next;
            head.next.prev = node;
            head.next = node;
        }
        
        private void removeNode(CacheNode<K, V> node) {
            node.prev.next = node.next;
            node.next.prev = node.prev;
        }
        
        private void moveToHead(CacheNode<K, V> node) {
            removeNode(node);
            addToHead(node);
        }
        
        // TODO: 学生实现内存管理
        private void updateMemoryUsage(V newValue, V oldValue) {
            long delta = 0;
            
            if (newValue != null) {
                delta += estimateObjectSize(newValue) + nodeOverhead;
            }
            if (oldValue != null) {
                delta -= estimateObjectSize(oldValue) + nodeOverhead;
            }
            
            estimatedMemoryUsage += delta;
        }
        
        private long estimateObjectSize(Object obj) {
            if (obj instanceof String) {
                return ((String) obj).length() * 2 + 40;  // 估算String大小
            } else if (obj instanceof Integer) {
                return 24;
            } else if (obj instanceof Long) {
                return 32;
            } else {
                return 64;  // 默认对象大小
            }
        }
        
        // TODO: 学生实现批量操作
        public void putAll(Map<K, V> map) {
            writeLock.lock();
            try {
                for (Map.Entry<K, V> entry : map.entrySet()) {
                    put(entry.getKey(), entry.getValue());
                }
            } finally {
                writeLock.unlock();
            }
        }
        
        public V remove(K key) {
            if (key == null) {
                return null;
            }
            
            writeLock.lock();
            try {
                CacheNode<K, V> node = cacheMap.remove(key);
                if (node != null) {
                    removeNode(node);
                    updateMemoryUsage(null, node.value);
                    return node.value;
                }
                return null;
            } finally {
                writeLock.unlock();
            }
        }
        
        public void clear() {
            writeLock.lock();
            try {
                cacheMap.clear();
                head.next = tail;
                tail.prev = head;
                estimatedMemoryUsage = 0;
                
                // 重置统计信息
                hitCount.set(0);
                missCount.set(0);
                evictionCount.set(0);
                totalLoadTime.set(0);
                loadOperations.set(0);
            } finally {
                writeLock.unlock();
            }
        }
        
        // TODO: 学生实现高级统计功能
        public CacheStats getStats() {
            readLock.lock();
            try {
                long avgLoadTime = loadOperations.get() > 0 ? 
                    totalLoadTime.get() / loadOperations.get() : 0;
                
                return new CacheStats(
                    hitCount.get(),
                    missCount.get(), 
                    evictionCount.get(),
                    cacheMap.size(),
                    maxSize,
                    avgLoadTime,
                    estimatedMemoryUsage
                );
            } finally {
                readLock.unlock();
            }
        }
        
        public void printCacheStructure() {
            readLock.lock();
            try {
                System.out.println("\n=== LRU缓存结构 (从新到旧) ===");
                CacheNode<K, V> current = head.next;
                int position = 1;
                
                while (current != tail && position <= 10) {
                    long ageMs = System.currentTimeMillis() - current.accessTime;
                    System.out.println(String.format(
                        "%d. %s (访问%d次, %dms前)",
                        position, current, current.accessCount, ageMs
                    ));
                    current = current.next;
                    position++;
                }
                
                if (current != tail) {
                    System.out.println("... (还有" + (cacheMap.size() - 10) + "个项目)");
                }
            } finally {
                readLock.unlock();
            }
        }
        
        public int size() {
            readLock.lock();
            try {
                return cacheMap.size();
            } finally {
                readLock.unlock();
            }
        }
        
        public boolean isEmpty() {
            return size() == 0;
        }
    }
    
    // TODO: 学生实现并发性能测试
    public static void performConcurrencyTest() throws InterruptedException {
        System.out.println("\n=== LRU缓存并发性能测试 ===");
        
        EnterpriseGradeLRUCache<String, String> cache = new EnterpriseGradeLRUCache<>(100);
        final int THREAD_COUNT = 10;
        final int OPERATIONS_PER_THREAD = 1000;
        
        // 预填充一些数据
        for (int i = 0; i < 50; i++) {
            cache.put("init_key_" + i, "init_value_" + i);
        }
        
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch completeLatch = new CountDownLatch(THREAD_COUNT);
        
        // 创建测试线程
        for (int i = 0; i < THREAD_COUNT; i++) {
            final int threadId = i;
            new Thread(() -> {
                try {
                    startLatch.await();
                    
                    for (int j = 0; j < OPERATIONS_PER_THREAD; j++) {
                        String key = "thread_" + threadId + "_key_" + j;
                        String value = "thread_" + threadId + "_value_" + j;
                        
                        // 70%写入，30%读取
                        if (j % 10 < 7) {
                            cache.put(key, value);
                        } else {
                            // 读取随机数据
                            String randomKey = j > 50 ? 
                                "thread_" + threadId + "_key_" + (j - 50) : 
                                "init_key_" + (j % 50);
                            cache.get(randomKey);
                        }
                    }
                    
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    completeLatch.countDown();
                }
            }, "CacheTestThread-" + i).start();
        }
        
        long startTime = System.nanoTime();
        startLatch.countDown();
        completeLatch.await();
        long endTime = System.nanoTime();
        
        double totalTime = (endTime - startTime) / 1_000_000.0;
        int totalOps = THREAD_COUNT * OPERATIONS_PER_THREAD;
        
        System.out.println(String.format(
            "\n并发测试结果:\n" +
            "  线程数: %d\n" +
            "  总操作数: %d\n" +
            "  执行时间: %.2f ms\n" +
            "  平均吞吐量: %.0f ops/sec",
            THREAD_COUNT, totalOps, totalTime, totalOps / (totalTime / 1000)
        ));
        
        System.out.println("\n缓存统计: " + cache.getStats());
        cache.printCacheStructure();
    }
    
    // TODO: 学生实现内存压力测试
    public static void performMemoryStressTest() {
        System.out.println("\n=== 内存压力测试 ===");
        
        EnterpriseGradeLRUCache<String, String> cache = new EnterpriseGradeLRUCache<>(1000);
        
        // 模拟大量数据写入
        System.out.println("正在写入大量数据...");
        
        for (int i = 0; i < 10000; i++) {
            String key = "stress_key_" + i;
            String value = "stress_value_" + i + "_" + "x".repeat(100); // 更大的数据
            
            cache.put(key, value);
            
            if (i % 1000 == 0) {
                CacheStats stats = cache.getStats();
                System.out.println(String.format(
                    "进度: %d/10000, 缓存大小: %d, 淘汰数: %d, 内存: %dKB",
                    i, stats.currentSize, stats.evictionCount, stats.totalMemoryUsed / 1024
                ));
            }
        }
        
        System.out.println("\n最终缓存统计: " + cache.getStats());
        
        // 测试热点数据访问
        System.out.println("\n测试热点数据访问...");
        String[] hotKeys = {"stress_key_9999", "stress_key_9998", "stress_key_9997"};
        
        for (int i = 0; i < 1000; i++) {
            for (String hotKey : hotKeys) {
                cache.get(hotKey);
            }
        }
        
        System.out.println("热点数据访问后: " + cache.getStats());
        cache.printCacheStructure();
    }
    
    public static void main(String[] args) throws InterruptedException {
        // 并发性能测试
        performConcurrencyTest();
        
        // 内存压力测试
        performMemoryStressTest();
        
        System.out.println("\n生产级LRU缓存演示完成！");
    }
}
```

**📝 编程任务检查点**:
- [ ] **LRU算法**: 掌握双向链表+HashMap实现O(1)复杂度
- [ ] **并发优化**: 理解读写锁对缓存性能的提升
- [ ] **内存管理**: 学会监控和估算缓存内存使用
- [ ] **性能统计**: 掌握缓存命中率、淘汰策略等关键指标
- [ ] **生产级特性**: 实现线程安全、批量操作、统计监控
- [ ] **性能调优**: 理解在高并发场景下的优化策略
- [ ] **代码位置**: `student_progress/JavaLearning/src/ProductionLRUCache.java`

#### Task 1.2.7: 布隆过滤器实现 + 概率数据结构优化 (5分钟) ⏰

**🎯 Primary Level (新手友好)**
- [ ] **什么是布隆过滤器**: 想象一个“大概率”回答的智能助手，说没有就真没有，说有可能有也可能没有
- [ ] **简单理解**: 布隆过滤器用很小的内存快速判断元素是否存在，不会误报没有
- [ ] **生活类比**: 就像门卫做初步筛查，说不在就真不在，说在还要进一步检查
- [ ] **检查点**: 能说出"布隆过滤器的误报特性和优势"

**🚀 Hands-On Coding Exercise (强制编程练习)**
```java
import java.util.BitSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

// 练习目标: 从零实现高效的布隆过滤器数据结构
public class BloomFilterImplementation {
    
    /**
     * 高性能布隆过滤器实现
     * 技术特性:
     * 1. 多个独立哈希函数的实现
     * 2. BitSet优化的内存使用
     * 3. 自适应参数计算和优化
     * 4. 误报率监控和分析
     * 5. 序列化支持和持久化
     */
    public static class AdvancedBloomFilter<T> {
        
        private final BitSet bitSet;
        private final int bitSetSize;
        private final int numHashFunctions;
        private final MessageDigest md5;
        private final MessageDigest sha1;
        
        // 统计信息
        private long elementsAdded = 0;
        private long falsePositiveChecks = 0;
        private long trueNegativeChecks = 0;
        
        // 常量
        private static final double DEFAULT_FALSE_POSITIVE_RATE = 0.03; // 3%误报率
        
        /**
         * 根据预期元素数量和误报率自动计算参数
         */
        public AdvancedBloomFilter(int expectedElements) {
            this(expectedElements, DEFAULT_FALSE_POSITIVE_RATE);
        }
        
        public AdvancedBloomFilter(int expectedElements, double falsePositiveRate) {
            // 计算最优参数
            this.bitSetSize = calculateOptimalBitSetSize(expectedElements, falsePositiveRate);
            this.numHashFunctions = calculateOptimalHashFunctions(expectedElements, bitSetSize);
            this.bitSet = new BitSet(bitSetSize);
            
            // 初始化哈希函数
            try {
                this.md5 = MessageDigest.getInstance("MD5");
                this.sha1 = MessageDigest.getInstance("SHA-1");
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException("哈希算法初始化失败", e);
            }
            
            System.out.println(String.format(
                "布隆过滤器初始化完成:\n" +
                "  预期元素数: %,d\n" +
                "  目标误报率: %.3f%%\n" +
                "  BitSet大小: %,d bits (%.2f KB)\n" +
                "  哈希函数数: %d",
                expectedElements, falsePositiveRate * 100, 
                bitSetSize, bitSetSize / 8.0 / 1024, numHashFunctions
            ));
        }
        
        // TODO: 学生实现最优参数计算
        private static int calculateOptimalBitSetSize(int expectedElements, double falsePositiveRate) {
            // 公式: m = -n * ln(p) / (ln(2))^2
            // n = 预期元素数量, p = 误报率, m = 位数组大小
            double m = -expectedElements * Math.log(falsePositiveRate) / (Math.log(2) * Math.log(2));
            return (int) Math.ceil(m);
        }
        
        private static int calculateOptimalHashFunctions(int expectedElements, int bitSetSize) {
            // 公式: k = (m/n) * ln(2)
            // m = 位数组大小, n = 预期元素数量, k = 哈希函数数量
            double k = ((double) bitSetSize / expectedElements) * Math.log(2);
            return Math.max(1, (int) Math.round(k));
        }
        
        // TODO: 学生实现多个独立哈希函数
        private int[] getHashValues(T item) {
            String itemStr = item.toString();
            
            // 使用两个不同的哈希算法生成基础哈希值
            int hash1 = getHash1(itemStr);
            int hash2 = getHash2(itemStr);
            
            int[] hashes = new int[numHashFunctions];
            
            // 使用双重哈希技巧生成多个哈希值
            // 公式: h_i(x) = (hash1(x) + i * hash2(x)) mod m
            for (int i = 0; i < numHashFunctions; i++) {
                int combinedHash = hash1 + i * hash2;
                hashes[i] = Math.abs(combinedHash % bitSetSize);
            }
            
            return hashes;
        }
        
        private int getHash1(String item) {
            md5.reset();
            byte[] bytes = md5.digest(item.getBytes());
            return Math.abs(bytesToInt(bytes, 0));
        }
        
        private int getHash2(String item) {
            sha1.reset();
            byte[] bytes = sha1.digest(item.getBytes());
            int hash = Math.abs(bytesToInt(bytes, 0));
            return hash == 0 ? 1 : hash; // 确保不为0
        }
        
        private int bytesToInt(byte[] bytes, int offset) {
            return ((bytes[offset] & 0xFF) << 24) |
                   ((bytes[offset + 1] & 0xFF) << 16) |
                   ((bytes[offset + 2] & 0xFF) << 8) |
                   (bytes[offset + 3] & 0xFF);
        }
        
        // TODO: 学生实现add操作
        public void add(T item) {
            if (item == null) {
                throw new IllegalArgumentException("不能添加null元素");
            }
            
            int[] hashes = getHashValues(item);
            
            for (int hash : hashes) {
                bitSet.set(hash);
            }
            
            elementsAdded++;
            
            if (elementsAdded % 10000 == 0) {
                System.out.println("已添加 " + elementsAdded + " 个元素，" +
                    "当前误报率估计: " + String.format("%.4f%%", getCurrentFalsePositiveRate() * 100));
            }
        }
        
        // TODO: 学生实现contains操作
        public boolean mightContain(T item) {
            if (item == null) {
                return false;
            }
            
            int[] hashes = getHashValues(item);
            
            for (int hash : hashes) {
                if (!bitSet.get(hash)) {
                    // 只要有一个位为0，就肯定不存在
                    return false;
                }
            }
            
            // 所有位都为1，可能存在（也可能是误报）
            return true;
        }
        
        // TODO: 学生实现误报率计算
        public double getCurrentFalsePositiveRate() {
            // 实际误报率公式: (1 - e^(-k*n/m))^k
            // k = 哈希函数数, n = 已添加元素数, m = 位数组大小
            if (elementsAdded == 0) {
                return 0.0;
            }
            
            double ratio = (double) numHashFunctions * elementsAdded / bitSetSize;
            double probability = Math.pow(1 - Math.exp(-ratio), numHashFunctions);
            return probability;
        }
        
        // TODO: 学生实现统计分析
        public BloomFilterStats getStats() {
            int setBits = bitSet.cardinality(); // 计算置位为1的位数
            double fillRatio = (double) setBits / bitSetSize;
            double currentFPR = getCurrentFalsePositiveRate();
            
            return new BloomFilterStats(
                elementsAdded,
                bitSetSize,
                setBits,
                numHashFunctions,
                fillRatio,
                currentFPR,
                falsePositiveChecks,
                trueNegativeChecks
            );
        }
        
        // 统计信息类
        public static class BloomFilterStats {
            final long elementsAdded;
            final int bitSetSize;
            final int setBits;
            final int numHashFunctions;
            final double fillRatio;
            final double currentFalsePositiveRate;
            final long falsePositiveChecks;
            final long trueNegativeChecks;
            
            BloomFilterStats(long elementsAdded, int bitSetSize, int setBits, 
                           int numHashFunctions, double fillRatio, 
                           double currentFalsePositiveRate,
                           long falsePositiveChecks, long trueNegativeChecks) {
                this.elementsAdded = elementsAdded;
                this.bitSetSize = bitSetSize;
                this.setBits = setBits;
                this.numHashFunctions = numHashFunctions;
                this.fillRatio = fillRatio;
                this.currentFalsePositiveRate = currentFalsePositiveRate;
                this.falsePositiveChecks = falsePositiveChecks;
                this.trueNegativeChecks = trueNegativeChecks;
            }
            
            @Override
            public String toString() {
                return String.format(
                    "BloomFilterStats{\n" +
                    "  已添加元素: %,d\n" +
                    "  BitSet大小: %,d bits\n" +
                    "  已置位数: %,d\n" +
                    "  填充率: %.2f%%\n" +
                    "  哈希函数数: %d\n" +
                    "  当前误报率: %.4f%%\n" +
                    "  误报检查: %,d\n" +
                    "  真阴性检查: %,d\n" +
                    "}",
                    elementsAdded, bitSetSize, setBits, fillRatio * 100,
                    numHashFunctions, currentFalsePositiveRate * 100,
                    falsePositiveChecks, trueNegativeChecks
                );
            }
        }
        
        // TODO: 学生实现序列化支持
        public byte[] serialize() {
            // 简单实现: 将BitSet转换为字节数组
            return bitSet.toByteArray();
        }
        
        public static <T> AdvancedBloomFilter<T> deserialize(byte[] data, int expectedElements, double falsePositiveRate) {
            AdvancedBloomFilter<T> filter = new AdvancedBloomFilter<>(expectedElements, falsePositiveRate);
            filter.bitSet.or(BitSet.valueOf(data));
            return filter;
        }
        
        // 清空过滤器
        public void clear() {
            bitSet.clear();
            elementsAdded = 0;
            falsePositiveChecks = 0;
            trueNegativeChecks = 0;
        }
        
        // 获取内存使用情况
        public long getMemoryUsage() {
            // BitSet内存使用估算
            return (long) Math.ceil(bitSetSize / 8.0) + 64; // 加上对象开销
        }
    }
    
    // TODO: 学生实现性能测试和验证
    public static void performAccuracyTest() {
        System.out.println("\n=== 布隆过滤器准确性测试 ===");
        
        int expectedElements = 100000;
        double targetFPR = 0.01; // 1%误报率
        
        AdvancedBloomFilter<String> filter = new AdvancedBloomFilter<>(expectedElements, targetFPR);
        
        // 添加已知元素
        System.out.println("正在添加" + expectedElements + "个元素...");
        List<String> addedElements = new ArrayList<>();
        
        for (int i = 0; i < expectedElements; i++) {
            String element = "element_" + i;
            filter.add(element);
            addedElements.add(element);
        }
        
        // 测试真阳性（所有添加的元素都应该返回true）
        System.out.println("\n测试真阳性...");
        int truePositives = 0;
        
        for (String element : addedElements) {
            if (filter.mightContain(element)) {
                truePositives++;
            }
        }
        
        double truePositiveRate = (double) truePositives / addedElements.size();
        System.out.println("真阳性率: " + String.format("%.4f%%", truePositiveRate * 100) + 
            " (应该是100%)");
        
        // 测试误报率
        System.out.println("\n测试误报率...");
        int falsePositives = 0;
        int testCount = 100000;
        
        for (int i = 0; i < testCount; i++) {
            String testElement = "test_element_" + (expectedElements + i);
            if (filter.mightContain(testElement)) {
                falsePositives++;
            }
        }
        
        double actualFPR = (double) falsePositives / testCount;
        System.out.println("实际误报率: " + String.format("%.4f%%", actualFPR * 100));
        System.out.println("目标误报率: " + String.format("%.4f%%", targetFPR * 100));
        System.out.println("理论误报率: " + String.format("%.4f%%", filter.getCurrentFalsePositiveRate() * 100));
        
        // 输出详细统计
        System.out.println("\n" + filter.getStats());
    }
    
    // TODO: 学生实现性能测试
    public static void performPerformanceTest() {
        System.out.println("\n=== 布隆过滤器性能测试 ===");
        
        int[] elementCounts = {10000, 100000, 1000000};
        
        for (int count : elementCounts) {
            System.out.println("\n--- 测试" + count + "个元素 ---");
            
            AdvancedBloomFilter<String> filter = new AdvancedBloomFilter<>(count, 0.01);
            
            // 测试添加性能
            long startTime = System.nanoTime();
            
            for (int i = 0; i < count; i++) {
                filter.add("element_" + i);
            }
            
            long addTime = System.nanoTime() - startTime;
            
            // 测试查询性能
            startTime = System.nanoTime();
            
            for (int i = 0; i < count; i++) {
                filter.mightContain("element_" + i);
            }
            
            long queryTime = System.nanoTime() - startTime;
            
            System.out.println(String.format(
                "性能结果:\n" +
                "  添加性能: %.2f ms (%.0f ops/sec)\n" +
                "  查询性能: %.2f ms (%.0f ops/sec)\n" +
                "  内存使用: %,d bytes\n" +
                "  平均每元素: %.2f bytes",
                addTime / 1_000_000.0, count / (addTime / 1_000_000_000.0),
                queryTime / 1_000_000.0, count / (queryTime / 1_000_000_000.0),
                filter.getMemoryUsage(),
                (double) filter.getMemoryUsage() / count
            ));
        }
    }
    
    // TODO: 学生实现与普通HashSet对比
    public static void compareWithHashSet() {
        System.out.println("\n=== 与HashSet对比测试 ===");
        
        int elementCount = 1000000;
        
        // 测试HashSet
        System.out.println("测试HashSet...");
        java.util.HashSet<String> hashSet = new java.util.HashSet<>();
        
        long startTime = System.nanoTime();
        
        for (int i = 0; i < elementCount; i++) {
            hashSet.add("element_" + i);
        }
        
        long hashSetAddTime = System.nanoTime() - startTime;
        
        startTime = System.nanoTime();
        
        for (int i = 0; i < elementCount; i++) {
            hashSet.contains("element_" + i);
        }
        
        long hashSetQueryTime = System.nanoTime() - startTime;
        
        // 估算HashSet内存使用
        long hashSetMemory = elementCount * (32 + 20); // 估算每个元素的开销
        
        // 测试BloomFilter
        System.out.println("测试BloomFilter...");
        AdvancedBloomFilter<String> bloomFilter = new AdvancedBloomFilter<>(elementCount, 0.01);
        
        startTime = System.nanoTime();
        
        for (int i = 0; i < elementCount; i++) {
            bloomFilter.add("element_" + i);
        }
        
        long bloomAddTime = System.nanoTime() - startTime;
        
        startTime = System.nanoTime();
        
        for (int i = 0; i < elementCount; i++) {
            bloomFilter.mightContain("element_" + i);
        }
        
        long bloomQueryTime = System.nanoTime() - startTime;
        
        // 结果对比
        System.out.println(String.format(
            "\n对比结果 (%,d个元素):\n" +
            "\nHashSet:\n" +
            "  添加时间: %.2f ms\n" +
            "  查询时间: %.2f ms\n" +
            "  内存使用: %,d bytes\n" +
            "  准确率: 100%%\n" +
            "\nBloomFilter:\n" +
            "  添加时间: %.2f ms (%.1fx快)\n" +
            "  查询时间: %.2f ms (%.1fx快)\n" +
            "  内存使用: %,d bytes (%.1fx小)\n" +
            "  误报率: %.3f%%",
            elementCount,
            hashSetAddTime / 1_000_000.0,
            hashSetQueryTime / 1_000_000.0,
            hashSetMemory,
            bloomAddTime / 1_000_000.0, (double) hashSetAddTime / bloomAddTime,
            bloomQueryTime / 1_000_000.0, (double) hashSetQueryTime / bloomQueryTime,
            bloomFilter.getMemoryUsage(), (double) hashSetMemory / bloomFilter.getMemoryUsage(),
            bloomFilter.getCurrentFalsePositiveRate() * 100
        ));
    }
    
    // Android应用场景模拟
    public static void androidUseCase() {
        System.out.println("\n=== Android应用场景模拟 ===");
        
        // 模拟网络请求缓存优化
        System.out.println("场景: 网络请求缓存优化");
        
        AdvancedBloomFilter<String> urlFilter = new AdvancedBloomFilter<>(10000, 0.02);
        
        // 模拟已缓存的URL
        String[] cachedUrls = {
            "https://api.example.com/users/123",
            "https://api.example.com/posts/456",
            "https://api.example.com/comments/789",
            "https://cdn.example.com/images/profile.jpg",
            "https://cdn.example.com/js/app.min.js"
        };
        
        for (String url : cachedUrls) {
            urlFilter.add(url);
        }
        
        // 模拟新请求检查
        String[] testUrls = {
            "https://api.example.com/users/123", // 已缓存
            "https://api.example.com/users/456", // 未缓存
            "https://api.example.com/posts/456", // 已缓存
            "https://api.newsite.com/data"       // 未缓存
        };
        
        System.out.println("\n缓存检查结果:");
        for (String url : testUrls) {
            boolean mightBeCached = urlFilter.mightContain(url);
            String result = mightBeCached ? "可能已缓存(需进一步检查)" : "肯定未缓存(需要网络请求)";
            System.out.println("  " + url + " -> " + result);
        }
        
        System.out.println("\n优化效果:");
        System.out.println("- 避免了不必要的缓存查找操作");
        System.out.println("- 减少了网络请求的延迟");
        System.out.println("- 内存开销极小: " + urlFilter.getMemoryUsage() + " bytes");
    }
    
    public static void main(String[] args) {
        // 准确性测试
        performAccuracyTest();
        
        // 性能测试
        performPerformanceTest();
        
        // 与HashSet对比
        compareWithHashSet();
        
        // Android应用场景
        androidUseCase();
        
        System.out.println("\n布隆过滤器实现演示完成！");
    }
}
```

**📝 编程任务检查点**:
- [ ] **数学原理**: 理解布隆过滤器的数学公式和最优参数计算
- [ ] **多哈希函数**: 掌握双重哈希技巧生成多个独立哈希值
- [ ] **概率特性**: 理解误报率计算和预测机制
- [ ] **内存优化**: 学会BitSet的高效内存使用
- [ ] **性能对比**: 理解布隆过滤器相比传统数据结构的优势
- [ ] **Android应用**: 掌握在移动应用中的实际使用场景
- [ ] **代码位置**: `student_progress/JavaLearning/src/BloomFilterImplementation.java`

---

## 🎯 第三章节 1.3 协程机制：Kotlin Coroutines 与异步编程

**学习方法说明**: 从协程的基本概念入手，理解挂起函数、调度器、结构化并发等核心概念。通过实际编程体验协程相比传统线程的优势，掌握现代异步编程范式。

**🚨 强制性编程框架**: 
- ✋ **渐进实践**: 从简单的suspend函数到复杂的Flow数据流
- 🏃 **Android集成**: 每个概念都要结合Android开发场景
- 📊 **性能对比**: 协程 vs 线程的性能和内存使用对比

---

### 🌟 Level 1: Primary Foundation (基础打牢) - 20分钟

#### Task 1.3.1: 协程基础概念 + 首个挂起函数 (5分钟) ⏰

**🎯 Primary Level (新手友好)**
- [ ] **什么是协程**: 想象一个超级多任务的厨师，可以同时准备多道菜
- [ ] **简单理解**: 协程是轻量级的"线程"，可以在执行过程中暂停和恢复
- [ ] **生活类比**: 就像看书时接电话，放下书接电话，接完继续看书
- [ ] **检查点**: 能说出"协程比线程轻量在哪里"
- [ ] **文件**: 在`student_progress/`创建`coroutines_notes.md`，记录理解

**🚀 Hands-On Coding Exercise (强制编程练习)**
```kotlin
import kotlinx.coroutines.*
import kotlin.system.measureTimeMillis

// 练习目标: 理解协程的基本概念和挂起函数
class CoroutineBasics {
    
    // TODO: 学生实现第一个挂起函数
    suspend fun fetchUserData(userId: String): String {
        println("开始获取用户[$userId]数据...")
        delay(1000)  // 模拟网络请求延迟
        println("用户[$userId]数据获取完成")
        return "User-$userId-Data"
    }
    
    suspend fun fetchUserProfile(userId: String): String {
        println("开始获取用户[$userId]资料...")
        delay(800)   // 模拟网络延迟
        println("用户[$userId]资料获取完成")
        return "Profile-$userId-Info"
    }
    
    // 演示协程的并发执行
    suspend fun demonstrateCoroutineConcurrency() {
        println("=== 协程并发演示 ===")
        
        // 方式1: 顺序执行 (串行)
        println("\n--- 顺序执行 ---")
        val sequentialTime = measureTimeMillis {
            val userData = fetchUserData("Alice")
            val userProfile = fetchUserProfile("Alice")
            println("顺序结果: $userData, $userProfile")
        }
        println("顺序执行总耗时: ${sequentialTime}ms")
        
        // 方式2: 并发执行 (并行)
        println("\n--- 并发执行 ---")
        val concurrentTime = measureTimeMillis {
            val userDataDeferred = async { fetchUserData("Bob") }
            val userProfileDeferred = async { fetchUserProfile("Bob") }
            
            // await等待所有结果
            val userData = userDataDeferred.await()
            val userProfile = userProfileDeferred.await()
            println("并发结果: $userData, $userProfile")
        }
        println("并发执行总耗时: ${concurrentTime}ms")
    }
    
    // 演示协程的轻量性
    suspend fun demonstrateCoroutineLightweight() {
        println("\n=== 协程轻量性演示 ===")
        
        val jobList = mutableListOf<Job>()
        val startTime = System.currentTimeMillis()
        
        // 创建大量协程
        repeat(10000) { i ->
            val job = GlobalScope.launch {
                delay(1000)  // 每个协程都挂起1秒
                if (i % 1000 == 0) {
                    println("协程$i 执行完成")
                }
            }
            jobList.add(job)
        }
        
        // 等待所有协程完成
        jobList.joinAll()
        
        val endTime = System.currentTimeMillis()
        println("10000个协程执行完成，总耗时: ${endTime - startTime}ms")
    }
    
    // 演示不同的协程构建器
    suspend fun demonstrateCoroutineBuilders() {
        println("\n=== 协程构建器对比 ===")
        
        // 1. launch: 启动协程，不返回结果
        println("--- launch构建器 ---")
        val job = launch {
            delay(500)
            println("launch协程执行完成")
        }
        job.join()  // 等待完成
        
        // 2. async: 启动协程，返回Deferred结果
        println("--- async构建器 ---")
        val deferred = async {
            delay(500)
            "async协程的返回值"
        }
        val result = deferred.await()
        println("async结果: $result")
        
        // 3. runBlocking: 阻塞式协程
        println("--- runBlocking构建器 ---")
        runBlocking {
            delay(500)
            println("runBlocking协程执行完成")
        }
    }
    
    // 演示协程的异常处理
    suspend fun demonstrateExceptionHandling() {
        println("\n=== 协程异常处理 ===")
        
        try {
            val result = async {
                delay(100)
                throw RuntimeException("模拟网络错误")
                "不会返回的结果"
            }
            result.await()
        } catch (e: Exception) {
            println("捕获到异常: ${e.message}")
        }
        
        // 使用SupervisorJob处理子协程异常
        supervisorScope {
            val job1 = launch {
                delay(100)
                println("正常协程1执行完成")
            }
            
            val job2 = launch {
                delay(50)
                throw RuntimeException("子协程异常")
            }
            
            val job3 = launch {
                delay(200)
                println("正常协程3执行完成")
            }
            
            // job2异常不会影响job1和job3
            try {
                joinAll(job1, job2, job3)
            } catch (e: Exception) {
                println("supervisor scope捕获异常: ${e.message}")
            }
        }
    }
}

// Android场景演示
class AndroidCoroutineExample {
    
    // 模拟Android中的网络请求
    suspend fun loadDataFromNetwork(): String {
        return withContext(Dispatchers.IO) {
            // 模拟网络I/O操作
            delay(2000)
            "从网络加载的数据"
        }
    }
    
    // 模拟在UI线程更新界面
    suspend fun updateUI(data: String) {
        withContext(Dispatchers.Main) {
            println("在主线程更新UI: $data")
            // 在实际Android中这里会更新View
        }
    }
    
    // 演示Android中的典型协程使用模式
    suspend fun androidCoroutinePattern() {
        println("\n=== Android协程模式演示 ===")
        
        try {
            // 1. 在后台线程执行网络请求
            val data = loadDataFromNetwork()
            
            // 2. 切换到主线程更新UI
            updateUI(data)
            
        } catch (e: Exception) {
            // 3. 错误处理
            println("加载数据失败: ${e.message}")
        }
    }
}

fun main() {
    val coroutineBasics = CoroutineBasics()
    val androidExample = AndroidCoroutineExample()
    
    runBlocking {
        // 基础协程演示
        coroutineBasics.demonstrateCoroutineConcurrency()
        
        coroutineBasics.demonstrateCoroutineBuilders()
        
        coroutineBasics.demonstrateExceptionHandling()
        
        // 轻量性演示 (注意: 这个会创建很多协程，需要一些时间)
        println("\n准备演示10000个协程的轻量性...")
        coroutineBasics.demonstrateCoroutineLightweight()
        
        // Android场景演示
        androidExample.androidCoroutinePattern()
    }
    
    println("\n协程基础演示完成！")
}
```

**📝 编程任务检查点**:
- [ ] **挂起函数理解**: 理解suspend关键字的作用和原理
- [ ] **协程构建器**: 掌握launch、async、runBlocking的区别
- [ ] **并发vs并行**: 体验协程的并发执行优势
- [ ] **轻量性验证**: 观察10000个协程的内存和性能表现
- [ ] **异常处理**: 理解协程中的异常传播机制
- [ ] **Android集成**: 理解协程在Android开发中的应用场景
- [ ] **代码位置**: `student_progress/JavaLearning/src/CoroutineBasics.kt`

#### Task 1.3.2: Channel通信机制 + 生产者消费者模式 (5分钟) ⏰

**🎯 Primary Level (新手友好)**
- [ ] **什么是Channel**: 想象一个传送带，生产者放东西，消费者取东西
- [ ] **简单理解**: Channel是协程之间传递数据的管道
- [ ] **生活类比**: 就像餐厅的传菜窗口，厨师放菜，服务员取菜
- [ ] **检查点**: 能说出"Channel如何实现协程间通信"

**🚀 Hands-On Coding Exercise (强制编程练习)**
```kotlin
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

// 练习目标: 掌握Channel的使用和生产者-消费者模式
class ChannelCommunication {
    
    // TODO: 学生实现基础的Channel通信
    suspend fun basicChannelDemo() {
        println("=== 基础Channel演示 ===")
        
        val channel = Channel<String>()
        
        // 生产者协程
        launch {
            println("生产者: 开始发送数据...")
            for (i in 1..5) {
                val message = "消息-$i"
                channel.send(message)
                println("生产者: 发送了 $message")
                delay(500)  // 模拟生产耗时
            }
            channel.close()  // 关闭通道
            println("生产者: 通道已关闭")
        }
        
        // 消费者协程
        launch {
            println("消费者: 开始接收数据...")
            for (message in channel) {  // 自动处理通道关闭
                println("消费者: 接收到 $message")
                delay(200)  // 模拟消费耗时
            }
            println("消费者: 接收完成")
        }
        
        delay(4000)  // 等待演示完成
    }
    
    // TODO: 学生实现多生产者多消费者模式
    suspend fun multiProducerConsumerDemo() {
        println("\n=== 多生产者多消费者演示 ===")
        
        val channel = Channel<String>(capacity = 10)  // 带缓冲的通道
        
        // 多个生产者
        repeat(3) { producerId ->
            launch {
                repeat(5) { i ->
                    val message = "P$producerId-M$i"
                    channel.send(message)
                    println("生产者$producerId: 发送 $message")
                    delay((100..300).random().toLong())
                }
            }
        }
        
        // 多个消费者
        repeat(2) { consumerId ->
            launch {
                while (true) {
                    try {
                        val message = channel.receive()
                        println("  消费者$consumerId: 处理 $message")
                        delay((50..200).random().toLong())
                    } catch (e: ClosedReceiveChannelException) {
                        println("  消费者$consumerId: 通道已关闭")
                        break
                    }
                }
            }
        }
        
        delay(3000)  // 让生产者完成
        channel.close()
        delay(1000)  // 让消费者完成处理
    }
    
    // TODO: 学生实现不同类型的Channel
    suspend fun channelTypesDemo() {
        println("\n=== 不同类型Channel演示 ===")
        
        // 1. 无缓冲Channel (默认)
        println("--- 无缓冲Channel ---")
        val rendezvousChannel = Channel<String>()
        
        launch {
            println("无缓冲: 准备发送")
            rendezvousChannel.send("立即交换")  // 会阻塞直到有接收者
            println("无缓冲: 发送完成")
        }
        
        delay(100)  // 让发送者先运行
        
        launch {
            println("无缓冲: 准备接收")
            val msg = rendezvousChannel.receive()
            println("无缓冲: 接收到 $msg")
        }
        
        delay(500)
        
        // 2. 带缓冲Channel
        println("--- 带缓冲Channel ---")
        val bufferedChannel = Channel<String>(capacity = 3)
        
        launch {
            repeat(5) { i ->
                bufferedChannel.send("缓冲消息-$i")
                println("缓冲发送: 消息-$i (缓冲区可能有空间)")
            }
        }
        
        delay(200)  // 让一些消息缓冲
        
        launch {
            repeat(5) { i ->
                val msg = bufferedChannel.receive()
                println("缓冲接收: $msg")
                delay(100)
            }
        }
        
        delay(1000)
        
        // 3. 无限容量Channel
        println("--- 无限容量Channel ---")
        val unlimitedChannel = Channel<String>(capacity = Channel.UNLIMITED)
        
        launch {
            repeat(1000) { i ->
                unlimitedChannel.send("无限消息-$i")
                if (i % 100 == 0) {
                    println("无限发送: 已发送${i + 1}条消息")
                }
            }
        }
        
        delay(100)
        
        launch {
            repeat(1000) { i ->
                val msg = unlimitedChannel.receive()
                if (i % 100 == 0) {
                    println("无限接收: 已接收${i + 1}条消息")
                }
            }
        }
        
        delay(2000)
    }
    
    // Android场景: 实现图片下载队列
    suspend fun imageDownloadQueueDemo() {
        println("\n=== Android图片下载队列演示 ===")
        
        data class ImageRequest(val url: String, val imageId: Int)
        data class ImageResult(val imageId: Int, val bitmap: String, val success: Boolean)
        
        val requestChannel = Channel<ImageRequest>(capacity = 50)
        val resultChannel = Channel<ImageResult>(capacity = 10)
        
        // 图片下载工作者 (模拟多个下载线程)
        repeat(3) { workerId ->
            launch(Dispatchers.IO) {
                while (true) {
                    try {
                        val request = requestChannel.receive()
                        println("下载器$workerId: 开始下载 ${request.url}")
                        
                        // 模拟网络下载
                        delay((500..1500).random().toLong())
                        
                        val success = (0..10).random() > 1  // 90%成功率
                        val result = ImageResult(
                            imageId = request.imageId,
                            bitmap = if (success) "Bitmap-${request.imageId}" else "null",
                            success = success
                        )
                        
                        resultChannel.send(result)
                        println("下载器$workerId: ${if (success) "下载成功" else "下载失败"} ${request.url}")
                        
                    } catch (e: ClosedReceiveChannelException) {
                        println("下载器$workerId: 下载队列已关闭")
                        break
                    }
                }
            }
        }
        
        // UI更新协程 (模拟主线程处理结果)
        launch(Dispatchers.Main) {
            while (true) {
                try {
                    val result = resultChannel.receive()
                    if (result.success) {
                        println("UI更新: 图片${result.imageId}加载成功，显示${result.bitmap}")
                    } else {
                        println("UI更新: 图片${result.imageId}加载失败，显示占位图")
                    }
                } catch (e: ClosedReceiveChannelException) {
                    println("UI更新: 结果通道已关闭")
                    break
                }
            }
        }
        
        // 模拟添加下载请求
        launch {
            repeat(20) { i ->
                val request = ImageRequest("https://example.com/image$i.jpg", i)
                requestChannel.send(request)
                println("请求队列: 添加下载任务 image$i.jpg")
                delay(100)  // 模拟用户滚动速度
            }
            
            delay(3000)  // 等待下载完成
            requestChannel.close()
            delay(1000)  // 等待工作者完成
            resultChannel.close()
        }
        
        delay(6000)  // 等待演示完成
    }
}

fun main() {
    val channelDemo = ChannelCommunication()
    
    runBlocking {
        // 基础Channel使用
        channelDemo.basicChannelDemo()
        
        // 多生产者多消费者
        channelDemo.multiProducerConsumerDemo()
        
        // 不同类型的Channel
        channelDemo.channelTypesDemo()
        
        // Android实际应用场景
        channelDemo.imageDownloadQueueDemo()
    }
    
    println("\nChannel通信机制演示完成！")
}
```

**📝 编程任务检查点**:
- [ ] **Channel基础**: 理解Channel的发送和接收机制
- [ ] **缓冲策略**: 掌握不同容量Channel的使用场景
- [ ] **多对多通信**: 实现多生产者多消费者模式
- [ ] **异常处理**: 正确处理Channel关闭异常
- [ ] **Android应用**: 理解Channel在图片加载等场景的应用
- [ ] **性能考虑**: 观察不同缓冲策略的性能影响

**🚀 Intermediate Level (实践验证)**  
- [ ] **Flow数据流**: 进一步学习Flow的响应式编程
- [ ] **协程上下文**: 理解Dispatchers和协程上下文切换
- [ ] **结构化并发**: 掌握作用域管理和协程生命周期

**🏆 Senior Level (架构思维)**
- [ ] **Android架构**: 在MVVM架构中正确使用协程
- [ ] **错误处理**: 设计健壮的协程错误处理机制
- [ ] **性能优化**: 协程在大型Android项目中的性能优化策略

#### Task 1.3.3: Flow响应式编程 + 数据流转换 (5分钟) ⏰

**🎯 Primary Level (新手友好)**
- [ ] **什么是Flow**: 想象一条河流，数据像水一样持续流动
- [ ] **简单理解**: Flow是协程版本的Observable，用于处理异步数据流
- [ ] **生活类比**: 就像新闻直播，数据不断更新，观察者实时接收
- [ ] **检查点**: 能说出"Flow与Channel的区别"

**🚀 Hands-On Coding Exercise (强制编程练习)**
```kotlin
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlin.random.Random

// 练习目标: 掌握Flow的创建、转换和收集
class FlowProgramming {
    
    // TODO: 学生实现基础Flow创建和收集
    suspend fun basicFlowDemo() {
        println("=== 基础Flow演示 ===\n")
        
        // 创建简单的Flow
        val numberFlow = flow {
            println("Flow开始生产数据...")
            for (i in 1..5) {
                emit(i)
                println("生产: $i")
                delay(300)
            }
            println("Flow数据生产完成")
        }
        
        // 收集Flow数据
        numberFlow.collect { value ->
            println("消费: $value")
        }
        
        println("基础Flow演示完成\n")
    }
    
    // TODO: 学生实现Flow的转换操作
    suspend fun flowTransformationDemo() {
        println("=== Flow转换操作演示 ===\n")
        
        val originalFlow = (1..10).asFlow()
        
        // map转换
        println("--- map转换 ---")
        originalFlow
            .map { it * it }  // 平方
            .collect { println("平方: $it") }
        
        delay(500)
        
        // filter过滤
        println("\n--- filter过滤 ---")
        originalFlow
            .filter { it % 2 == 0 }  // 偶数
            .collect { println("偶数: $it") }
        
        delay(500)
        
        // transform高级转换
        println("\n--- transform转换 ---")
        originalFlow
            .transform { value ->
                emit("字符串: $value")
                if (value % 3 == 0) {
                    emit("3的倍数: $value")
                }
            }
            .collect { println(it) }
        
        delay(500)
        
        // take限制数量
        println("\n--- take限制 ---")
        (1..100).asFlow()
            .take(3)
            .collect { println("前3个: $it") }
        
        println("\nFlow转换操作演示完成\n")
    }
    
    // TODO: 学生实现Flow的组合操作
    suspend fun flowCombinationDemo() {
        println("=== Flow组合操作演示 ===\n")
        
        val flow1 = flow {
            repeat(3) { i ->
                emit("A$i")
                delay(400)
            }
        }
        
        val flow2 = flow {
            repeat(5) { i ->
                emit("B$i")
                delay(300)
            }
        }
        
        // zip组合 - 一对一配对
        println("--- zip组合 ---")
        flow1.zip(flow2) { a, b -> "$a + $b" }
            .collect { println("zip: $it") }
        
        delay(500)
        
        // combine组合 - 最新值组合
        println("\n--- combine组合 ---")
        val fastFlow = flow {
            repeat(6) { i ->
                emit("Fast$i")
                delay(200)
            }
        }
        
        val slowFlow = flow {
            repeat(3) { i ->
                emit("Slow$i")
                delay(600)
            }
        }
        
        fastFlow.combine(slowFlow) { fast, slow ->
            "$fast & $slow"
        }.collect { println("combine: $it") }
        
        println("\nFlow组合操作演示完成\n")
    }
    
    // TODO: 学生实现异常处理和完成回调
    suspend fun flowExceptionHandlingDemo() {
        println("=== Flow异常处理演示 ===\n")
        
        // 上游异常处理
        flow {
            for (i in 1..5) {
                if (i == 3) {
                    throw RuntimeException("模拟上游异常")
                }
                emit(i)
            }
        }
        .catch { e ->
            println("捕获上游异常: ${e.message}")
            emit(-1)  // 发射默认值
        }
        .collect { value ->
            println("接收: $value")
        }
        
        delay(500)
        
        // 下游异常处理
        println("\n--- 下游异常处理 ---")
        (1..5).asFlow()
            .onEach { value ->
                if (value == 4) {
                    throw RuntimeException("模拟下游异常")
                }
            }
            .catch { e ->
                println("捕获异常: ${e.message}")
            }
            .collect { value ->
                println("处理: $value")
            }
        
        delay(500)
        
        // 完成回调
        println("\n--- 完成回调 ---")
        (1..3).asFlow()
            .onStart { println("Flow开始") }
            .onEach { println("处理: $it") }
            .onCompletion { cause ->
                if (cause == null) {
                    println("Flow正常完成")
                } else {
                    println("Flow异常完成: $cause")
                }
            }
            .collect()
        
        println("\nFlow异常处理演示完成\n")
    }
    
    // Android场景: 实时搜索功能
    suspend fun realtimeSearchDemo() {
        println("=== Android实时搜索演示 ===\n")
        
        // 模拟用户输入流
        val userInputFlow = flow {
            val inputs = listOf("", "a", "an", "and", "andr", "andro", "android")
            inputs.forEach { input ->
                emit(input)
                delay(300)  // 模拟用户打字间隔
            }
        }
        
        // 模拟搜索API
        suspend fun searchAPI(query: String): List<String> {
            delay(200)  // 模拟网络延迟
            return if (query.isEmpty()) {
                emptyList()
            } else {
                listOf("${query}1", "${query}2", "${query}3")
                    .filter { Random.nextBoolean() }  // 随机结果
            }
        }
        
        userInputFlow
            .debounce(100)  // 防抖动，避免频繁搜索
            .filter { it.length >= 2 }  // 至少2个字符才搜索
            .distinctUntilChanged()  // 去重，避免重复搜索
            .flatMapLatest { query ->  // 取消之前的搜索，只保留最新的
                flow {
                    println("搜索: '$query'")
                    val results = searchAPI(query)
                    emit(SearchResult(query, results))
                }
            }
            .catch { e ->
                println("搜索异常: ${e.message}")
                emit(SearchResult("", emptyList()))
            }
            .collect { result ->
                println("搜索结果 '${result.query}': ${result.results}")
            }
        
        println("\n实时搜索演示完成\n")
    }
    
    data class SearchResult(val query: String, val results: List<String>)
    
    // 冷流vs热流演示
    suspend fun coldVsHotFlowDemo() {
        println("=== 冷流vs热流演示 ===\n")
        
        // 冷流 - 每个收集器都会重新开始
        println("--- 冷流演示 ---")
        val coldFlow = flow {
            println("冷流: 开始生产数据")
            repeat(3) { i ->
                emit(i)
                delay(300)
            }
        }
        
        println("第一个收集器:")
        coldFlow.collect { println("收集器1: $it") }
        
        delay(500)
        
        println("第二个收集器:")
        coldFlow.collect { println("收集器2: $it") }
        
        delay(1000)
        
        // 热流 - SharedFlow
        println("\n--- 热流演示 ---")
        val hotFlow = MutableSharedFlow<Int>()
        
        // 启动数据生产者
        val producerJob = launch {
            repeat(5) { i ->
                println("热流: 生产 $i")
                hotFlow.emit(i)
                delay(400)
            }
        }
        
        delay(200)  // 稍后开始收集
        
        // 第一个收集器
        val job1 = launch {
            hotFlow.collect { println("热流收集器1: $it") }
        }
        
        delay(800)  // 更晚开始收集
        
        // 第二个收集器 (会错过一些数据)
        val job2 = launch {
            hotFlow.collect { println("热流收集器2: $it") }
        }
        
        producerJob.join()
        job1.cancel()
        job2.cancel()
        
        println("\n冷流vs热流演示完成\n")
    }
}

fun main() {
    val flowDemo = FlowProgramming()
    
    runBlocking {
        // 基础Flow使用
        flowDemo.basicFlowDemo()
        
        // Flow转换操作
        flowDemo.flowTransformationDemo()
        
        // Flow组合操作
        flowDemo.flowCombinationDemo()
        
        // 异常处理
        flowDemo.flowExceptionHandlingDemo()
        
        // Android实际应用
        flowDemo.realtimeSearchDemo()
        
        // 冷流vs热流
        flowDemo.coldVsHotFlowDemo()
    }
    
    println("Flow响应式编程演示完成！")
}
```

**📝 编程任务检查点**:
- [ ] **Flow创建**: 掌握flow构建器和各种Flow创建方法
- [ ] **转换操作**: 熟练使用map、filter、transform等转换操作符
- [ ] **组合操作**: 理解zip、combine等Flow组合策略
- [ ] **异常处理**: 正确使用catch操作符处理Flow异常
- [ ] **背压处理**: 理解debounce、buffer等背压处理机制
- [ ] **冷热流**: 区分冷流和热流的使用场景
- [ ] **代码位置**: `student_progress/JavaLearning/src/FlowProgramming.kt`

#### Task 1.3.4: 协程作用域与结构化并发 (5分钟) ⏰

**🎯 Primary Level (新手友好)**
- [ ] **什么是作用域**: 想象一个公司部门，部门解散时所有员工都离开
- [ ] **简单理解**: 协程作用域管理协程的生命周期，统一取消和异常处理
- [ ] **生活类比**: 就像家长管理孩子，孩子不听话就一起惩罚
- [ ] **检查点**: 能说出"为什么需要协程作用域"

**🚀 Hands-On Coding Exercise (强制编程练习)**
```kotlin
import kotlinx.coroutines.*
import kotlin.coroutines.cancellation.CancellationException

// 练习目标: 理解协程作用域的管理和结构化并发
class CoroutineScopes {
    
    // TODO: 学生实现作用域的基础使用
    suspend fun basicScopeDemo() {
        println("=== 基础作用域演示 ===\n")
        
        // GlobalScope - 全局作用域 (不推荐在Android中使用)
        println("--- GlobalScope ---")
        val globalJob = GlobalScope.launch {
            repeat(5) { i ->
                println("GlobalScope: $i")
                delay(200)
            }
        }
        delay(500)
        globalJob.cancel()
        
        // runBlocking - 阈塞当前线程
        println("\n--- runBlocking ---")
        runBlocking {
            println("runBlocking: 开始")
            delay(300)
            println("runBlocking: 结束")
        }
        
        // coroutineScope - 继承上下文的作用域
        println("\n--- coroutineScope ---")
        coroutineScope {
            launch {
                println("coroutineScope: 子协程1")
                delay(200)
            }
            launch {
                println("coroutineScope: 子协程2")
                delay(300)
            }
            println("coroutineScope: 等待所有子协程完成")
        }
        println("coroutineScope: 所有子协程完成")
    }
    
    // TODO: 学生实现取消传播机制
    suspend fun cancellationPropagationDemo() {
        println("\n=== 取消传播机制演示 ===\n")
        
        val parentJob = launch {
            println("父协程: 开始")
            
            val child1 = launch {
                try {
                    println("子协程1: 开始工作")
                    repeat(10) { i ->
                        println("子协程1: 步骤$i")
                        delay(200)
                        ensureActive()  // 检查取消状态
                    }
                } catch (e: CancellationException) {
                    println("子协程1: 被取消")
                    throw e  // 重新抛出取消异常
                } finally {
                    println("子协程1: 清理资源")
                }
            }
            
            val child2 = launch {
                try {
                    println("子协程2: 开始工作")
                    delay(1000)
                    println("子协程2: 工作完成")
                } catch (e: CancellationException) {
                    println("子协程2: 被取消")
                    throw e
                } finally {
                    println("子协程2: 清理资源")
                }
            }
            
            delay(500)  // 让子协程工作一会儿
            println("父协程: 完成")
        }
        
        delay(300)  // 让协程工作一会儿
        println("主线程: 取消父协程")
        parentJob.cancel("Manual cancellation")
        parentJob.join()  // 等待取消完成
        
        println("取消传播演示完成\n")
    }
    
    // TODO: 学生实现SupervisorJob的使用
    suspend fun supervisorJobDemo() {
        println("=== SupervisorJob演示 ===\n")
        
        // 普通Job - 一个子协程失败，所有兄弟协程都被取消
        println("--- 普通Job ---")
        try {
            coroutineScope {
                launch {
                    delay(300)
                    println("普通子协程1: 正常完成")
                }
                
                launch {
                    delay(100)
                    throw RuntimeException("普通子协程2: 失败")
                }
                
                launch {
                    try {
                        delay(500)
                        println("普通子协程3: 正常完成")
                    } catch (e: CancellationException) {
                        println("普通子协程3: 被取消")
                        throw e
                    }
                }
            }
        } catch (e: Exception) {
            println("捕获普通作用域异常: ${e.message}")
        }
        
        delay(500)
        
        // SupervisorJob - 子协程隔离，一个失败不影响其他
        println("\n--- SupervisorJob ---")
        supervisorScope {
            launch {
                delay(300)
                println("Supervisor子协程1: 正常完成")
            }
            
            launch {
                try {
                    delay(100)
                    throw RuntimeException("Supervisor子协程2: 失败")
                } catch (e: Exception) {
                    println("处理Supervisor子协程2异常: ${e.message}")
                }
            }
            
            launch {
                delay(500)
                println("Supervisor子协程3: 正常完成")
            }
        }
        
        println("SupervisorJob演示完成\n")
    }
    
    // TODO: 学生实现超时处理
    suspend fun timeoutDemo() {
        println("=== 超时处理演示 ===\n")
        
        // withTimeout - 超时异常
        try {
            withTimeout(500) {
                println("开始执行超时任务...")
                delay(1000)  // 超过超时时间
                println("任务正常完成")  // 不会执行
            }
        } catch (e: TimeoutCancellationException) {
            println("任务超时: ${e.message}")
        }
        
        // withTimeoutOrNull - 超时返回null
        val result = withTimeoutOrNull(300) {
            delay(500)
            "任务结果"
        }
        
        if (result == null) {
            println("任务超时，返回null")
        } else {
            println("任务完成: $result")
        }
        
        println("超时处理演示完成\n")
    }
    
    // Android场景: 模拟ViewModel中的协程使用
    class AndroidViewModelExample {
        // 模拟Android ViewModel的viewModelScope
        private val viewModelScope = CoroutineScope(
            Dispatchers.Main + SupervisorJob()
        )
        
        fun loadUserData(userId: String) {
            viewModelScope.launch {
                try {
                    println("ViewModel: 开始加载用户数据")
                    
                    // 并发加载多个数据源
                    val userData = async(Dispatchers.IO) {
                        delay(800)
                        "User-$userId-Data"
                    }
                    
                    val userProfile = async(Dispatchers.IO) {
                        delay(600)
                        "Profile-$userId-Info"
                    }
                    
                    val userSettings = async(Dispatchers.IO) {
                        delay(400)
                        "Settings-$userId-Config"
                    }
                    
                    // 等待所有数据
                    val data = userData.await()
                    val profile = userProfile.await()
                    val settings = userSettings.await()
                    
                    // 切换到主线程更新UI
                    withContext(Dispatchers.Main) {
                        println("ViewModel: 更新UI - $data, $profile, $settings")
                    }
                    
                } catch (e: Exception) {
                    println("ViewModel: 加载数据失败 - ${e.message}")
                }
            }
        }
        
        fun onCleared() {
            // ViewModel清理时取消所有协程
            viewModelScope.cancel("ViewModel cleared")
        }
    }
    
    suspend fun androidViewModelDemo() {
        println("=== Android ViewModel协程演示 ===\n")
        
        val viewModel = AndroidViewModelExample()
        
        // 模拟用户操作
        viewModel.loadUserData("12345")
        
        delay(1000)  // 让加载完成
        
        // 模拟ViewModel清理
        viewModel.onCleared()
        
        println("Android ViewModel演示完成\n")
    }
}

fun main() {
    val scopeDemo = CoroutineScopes()
    
    runBlocking {
        // 基础作用域
        scopeDemo.basicScopeDemo()
        
        // 取消传播
        scopeDemo.cancellationPropagationDemo()
        
        // SupervisorJob
        scopeDemo.supervisorJobDemo()
        
        // 超时处理
        scopeDemo.timeoutDemo()
        
        // Android实际应用
        scopeDemo.androidViewModelDemo()
    }
    
    println("协程作用域演示完成！")
}
```

**📝 编程任务检查点**:
- [ ] **作用域管理**: 理解不同作用域的特点和使用场景
- [ ] **取消传播**: 正确处理协程的取消和清理逻辑
- [ ] **SupervisorJob**: 理解监督作用域的错误隔离机制
- [ ] **超时处理**: 掌握withTimeout系列函数的使用
- [ ] **Android集成**: 理解在ViewModel中如何正确使用协程
- [ ] **结构化并发**: 理解结构化并发的设计原则
- [ ] **代码位置**: `student_progress/JavaLearning/src/CoroutineScopes.kt`

#### Task 1.3.5: 协程调度器原理 + 线程池模型深入 (5分钟) ⏰

**🎯 Primary Level (新手友好)**
- [ ] **什么是调度器**: 想象一个交通指挥员，决定哪辆车在哪条路上行驶
- [ ] **简单理解**: Dispatcher决定协程在哪个线程上执行
- [ ] **生活类比**: 就像餐厅经理安排服务员的工作区域
- [ ] **检查点**: 能说出"不同Dispatcher的适用场景"

**🚀 Hands-On Coding Exercise (强制编程练习)**
```kotlin
import kotlinx.coroutines.*
import kotlin.system.measureTimeMillis

// 练习目标: 深入理解协程调度器的工作原理和线程模型
class CoroutineDispatchersPrinciple {
    
    // TODO: 学生实现调度器基础对比
    suspend fun basicDispatchersDemo() {
        println("=== 协程调度器基础演示 ===\n")
        
        // Dispatchers.Main - Android主线程调度器
        println("--- Dispatchers.Main ---")
        withContext(Dispatchers.Main) {
            println("Main调度器: 线程=${Thread.currentThread().name}")
            println("Main调度器: 用于UI更新操作")
        }
        
        // Dispatchers.IO - I/O密集型任务调度器
        println("\n--- Dispatchers.IO ---")
        withContext(Dispatchers.IO) {
            println("IO调度器: 线程=${Thread.currentThread().name}")
            println("IO调度器: 用于网络请求、文件读写")
            delay(100)  // 模拟I/O操作
        }
        
        // Dispatchers.Default - CPU密集型任务调度器
        println("\n--- Dispatchers.Default ---")
        withContext(Dispatchers.Default) {
            println("Default调度器: 线程=${Thread.currentThread().name}")
            println("Default调度器: 用于计算密集型任务")
            // 模拟CPU密集型计算
            var sum = 0
            repeat(1000) { sum += it }
        }
        
        // Dispatchers.Unconfined - 不限制调度器
        println("\n--- Dispatchers.Unconfined ---")
        withContext(Dispatchers.Unconfined) {
            println("Unconfined调度器: 线程=${Thread.currentThread().name}")
            delay(1)  // 第一个挂起点
            println("恢复后线程: ${Thread.currentThread().name}")
        }
    }
    
    // TODO: 学生实现线程池模型分析
    suspend fun threadPoolAnalysisDemo() {
        println("\n=== 线程池模型分析 ===\n")
        
        // 观察IO调度器的线程复用
        println("--- IO调度器线程复用演示 ---")
        val ioJobs = mutableListOf<Job>()
        
        repeat(20) { i ->
            val job = GlobalScope.launch(Dispatchers.IO) {
                println("任务$i: 线程=${Thread.currentThread().name}")
                delay(100)  // 模拟I/O操作
                println("任务$i: 完成在线程=${Thread.currentThread().name}")
            }
            ioJobs.add(job)
        }
        
        ioJobs.joinAll()
        
        delay(500)
        
        // 观察Default调度器的CPU核心数限制
        println("\n--- Default调度器CPU核心限制 ---")
        val cpuCores = Runtime.getRuntime().availableProcessors()
        println("系统CPU核心数: $cpuCores")
        
        val defaultJobs = mutableListOf<Job>()
        val threadNames = mutableSetOf<String>()
        
        repeat(cpuCores * 2) { i ->
            val job = GlobalScope.launch(Dispatchers.Default) {
                synchronized(threadNames) {
                    threadNames.add(Thread.currentThread().name)
                }
                println("CPU任务$i: 线程=${Thread.currentThread().name}")
                // 模拟CPU密集型工作
                var result = 0.0
                repeat(1000000) { result += kotlin.math.sqrt(it.toDouble()) }
            }
            defaultJobs.add(job)
        }
        
        defaultJobs.joinAll()
        println("Default调度器实际使用线程数: ${threadNames.size}")
    }
    
    // TODO: 学生实现自定义调度器
    suspend fun customDispatcherDemo() {
        println("\n=== 自定义调度器演示 ===\n")
        
        // 创建固定大小的线程池调度器
        val customThreadPool = newFixedThreadPoolContext(3, "CustomPool")
        
        try {
            println("--- 自定义线程池调度器 ---")
            val customJobs = mutableListOf<Job>()
            
            repeat(10) { i ->
                val job = GlobalScope.launch(customThreadPool) {
                    println("自定义任务$i: 线程=${Thread.currentThread().name}")
                    delay(200)
                    println("自定义任务$i: 完成")
                }
                customJobs.add(job)
            }
            
            customJobs.joinAll()
            
        } finally {
            customThreadPool.close()  // 重要: 关闭自定义线程池
        }
        
        // 创建单线程调度器
        val singleThreadDispatcher = newSingleThreadContext("SingleThread")
        
        try {
            println("\n--- 单线程调度器 ---")
            val singleJobs = mutableListOf<Job>()
            
            repeat(5) { i ->
                val job = GlobalScope.launch(singleThreadDispatcher) {
                    println("单线程任务$i: 线程=${Thread.currentThread().name}")
                    delay(100)
                }
                singleJobs.add(job)
            }
            
            singleJobs.joinAll()
            
        } finally {
            singleThreadDispatcher.close()
        }
    }
    
    // TODO: 学生实现调度器性能对比
    suspend fun dispatcherPerformanceComparison() {
        println("\n=== 调度器性能对比 ===\n")
        
        val taskCount = 1000
        
        // IO密集型任务性能测试
        println("--- I/O密集型任务性能对比 ---")
        
        // 使用IO调度器
        val ioTime = measureTimeMillis {
            val ioJobs = (1..taskCount).map {
                GlobalScope.async(Dispatchers.IO) {
                    delay(1)  // 模拟短暂I/O操作
                    it * 2
                }
            }
            ioJobs.awaitAll()
        }
        println("IO调度器执行${taskCount}个I/O任务: ${ioTime}ms")
        
        // 使用Default调度器(不适合I/O)
        val defaultTime = measureTimeMillis {
            val defaultJobs = (1..taskCount).map {
                GlobalScope.async(Dispatchers.Default) {
                    delay(1)  // 模拟短暂I/O操作
                    it * 2
                }
            }
            defaultJobs.awaitAll()
        }
        println("Default调度器执行${taskCount}个I/O任务: ${defaultTime}ms")
        
        delay(500)
        
        // CPU密集型任务性能测试
        println("\n--- CPU密集型任务性能对比 ---")
        val cpuTaskCount = 100
        
        // 使用Default调度器
        val defaultCpuTime = measureTimeMillis {
            val defaultJobs = (1..cpuTaskCount).map {
                GlobalScope.async(Dispatchers.Default) {
                    // CPU密集型计算
                    var result = 0.0
                    repeat(100000) { i ->
                        result += kotlin.math.sqrt(i.toDouble())
                    }
                    result
                }
            }
            defaultJobs.awaitAll()
        }
        println("Default调度器执行${cpuTaskCount}个CPU任务: ${defaultCpuTime}ms")
        
        // 使用IO调度器(不适合CPU密集型)
        val ioCpuTime = measureTimeMillis {
            val ioJobs = (1..cpuTaskCount).map {
                GlobalScope.async(Dispatchers.IO) {
                    // CPU密集型计算
                    var result = 0.0
                    repeat(100000) { i ->
                        result += kotlin.math.sqrt(i.toDouble())
                    }
                    result
                }
            }
            ioJobs.awaitAll()
        }
        println("IO调度器执行${cpuTaskCount}个CPU任务: ${ioCpuTime}ms")
        
        println("\n性能分析:")
        println("- I/O任务: IO调度器比Default调度器快 ${(defaultTime - ioTime)}ms")
        println("- CPU任务: Default调度器比IO调度器快 ${(ioCpuTime - defaultCpuTime)}ms")
    }
    
    // TODO: 学生实现调度器切换开销分析
    suspend fun contextSwitchOverheadDemo() {
        println("\n=== 调度器切换开销分析 ===\n")
        
        val switchCount = 1000
        
        // 频繁切换调度器的开销
        val switchTime = measureTimeMillis {
            repeat(switchCount) {
                withContext(Dispatchers.IO) {
                    // 简单操作
                }
                withContext(Dispatchers.Default) {
                    // 简单操作
                }
                withContext(Dispatchers.Main) {
                    // 简单操作
                }
            }
        }
        
        // 不切换调度器的对比
        val noSwitchTime = measureTimeMillis {
            withContext(Dispatchers.Default) {
                repeat(switchCount * 3) {
                    // 相同数量的简单操作
                }
            }
        }
        
        println("频繁切换调度器${switchCount}次耗时: ${switchTime}ms")
        println("不切换调度器执行相同操作耗时: ${noSwitchTime}ms")
        println("调度器切换开销: ${switchTime - noSwitchTime}ms")
        
        // 建议
        println("\n优化建议:")
        println("1. 避免不必要的调度器切换")
        println("2. 将相同调度器的操作批量执行")
        println("3. 根据任务类型选择合适的调度器")
    }
}

// Android场景: 调度器在实际开发中的应用
class AndroidDispatcherExample {
    
    // 模拟复杂的数据加载场景
    suspend fun complexDataLoadingScenario() {
        println("\n=== Android复杂数据加载场景 ===\n")
        
        try {
            // 并发执行多个不同类型的任务
            val userProfile = async(Dispatchers.IO) {
                println("加载用户资料: 线程=${Thread.currentThread().name}")
                delay(800)  // 模拟网络请求
                "用户资料数据"
            }
            
            val processedData = async(Dispatchers.Default) {
                println("处理复杂计算: 线程=${Thread.currentThread().name}")
                // 模拟CPU密集型数据处理
                var result = 0.0
                repeat(1000000) { result += kotlin.math.sqrt(it.toDouble()) }
                "处理后的数据: $result"
            }
            
            val localCache = async(Dispatchers.IO) {
                println("读取本地缓存: 线程=${Thread.currentThread().name}")
                delay(200)  // 模拟文件I/O
                "缓存数据"
            }
            
            // 等待所有任务完成
            val profile = userProfile.await()
            val processed = processedData.await()
            val cache = localCache.await()
            
            // 切换到主线程更新UI
            withContext(Dispatchers.Main) {
                println("更新UI: 线程=${Thread.currentThread().name}")
                println("数据加载完成: $profile, $processed, $cache")
            }
            
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                println("错误处理: 线程=${Thread.currentThread().name}")
                println("加载失败: ${e.message}")
            }
        }
    }
    
    // 模拟图片处理场景
    suspend fun imageProcessingScenario() {
        println("\n=== 图片处理场景 ===\n")
        
        val imageUrls = listOf(
            "https://example.com/image1.jpg",
            "https://example.com/image2.jpg",
            "https://example.com/image3.jpg"
        )
        
        val processedImages = imageUrls.map { url ->
            async {
                // 下载图片 (I/O密集型)
                val imageData = withContext(Dispatchers.IO) {
                    println("下载图片: $url, 线程=${Thread.currentThread().name}")
                    delay(500)  // 模拟下载
                    "ImageData for $url"
                }
                
                // 处理图片 (CPU密集型)
                val processedImage = withContext(Dispatchers.Default) {
                    println("处理图片: $url, 线程=${Thread.currentThread().name}")
                    // 模拟图片处理计算
                    repeat(100000) { kotlin.math.sin(it.toDouble()) }
                    "Processed $imageData"
                }
                
                // 缓存图片 (I/O密集型)
                withContext(Dispatchers.IO) {
                    println("缓存图片: $url, 线程=${Thread.currentThread().name}")
                    delay(100)  // 模拟写入缓存
                }
                
                processedImage
            }
        }
        
        // 等待所有图片处理完成
        val results = processedImages.awaitAll()
        
        // 更新UI
        withContext(Dispatchers.Main) {
            println("\n所有图片处理完成，更新UI:")
            results.forEach { println("  $it") }
        }
    }
}

fun main() {
    val dispatcherDemo = CoroutineDispatchersPrinciple()
    val androidExample = AndroidDispatcherExample()
    
    runBlocking {
        // 基础调度器演示
        dispatcherDemo.basicDispatchersDemo()
        
        // 线程池模型分析
        dispatcherDemo.threadPoolAnalysisDemo()
        
        // 自定义调度器
        dispatcherDemo.customDispatcherDemo()
        
        // 性能对比
        dispatcherDemo.dispatcherPerformanceComparison()
        
        // 切换开销分析
        dispatcherDemo.contextSwitchOverheadDemo()
        
        // Android实际应用场景
        androidExample.complexDataLoadingScenario()
        androidExample.imageProcessingScenario()
    }
    
    println("\n协程调度器原理演示完成！")
}
```

**📝 编程任务检查点**:
- [ ] **调度器类型**: 理解Main、IO、Default、Unconfined调度器的适用场景
- [ ] **线程池模型**: 观察不同调度器的线程复用和限制机制
- [ ] **自定义调度器**: 学会创建和管理自定义线程池调度器
- [ ] **性能优化**: 根据任务类型选择合适的调度器
- [ ] **切换开销**: 理解频繁切换调度器的性能影响
- [ ] **Android集成**: 掌握在复杂场景中的调度器使用策略
- [ ] **代码位置**: `student_progress/JavaLearning/src/CoroutineDispatchersPrinciple.kt`

#### Task 1.3.6: Android生命周期协程 + ViewModel集成 (5分钟) ⏰

**🎯 Primary Level (新手友好)**
- [ ] **什么是生命周期协程**: 想象宠物跟着主人，主人离开宠物也要跟着离开
- [ ] **简单理解**: 协程会随着Android组件的生命周期自动取消和清理
- [ ] **生活类比**: 就像员工跟着部门，部门解散员工也要重新分配
- [ ] **检查点**: 能说出"为什么需要生命周期感知的协程"

**🚀 Hands-On Coding Exercise (强制编程练习)**
```kotlin
import kotlinx.coroutines.*
import androidx.lifecycle.*

// 练习目标: 掌握Android生命周期协程的使用和最佳实践
class AndroidLifecycleCoroutines {
    
    // 模拟Android Activity生命周期
    class MockActivity : LifecycleOwner {
        private val lifecycleRegistry = LifecycleRegistry(this)
        override fun getLifecycle(): Lifecycle = lifecycleRegistry
        
        fun onCreate() {
            println("Activity: onCreate()")
            lifecycleRegistry.currentState = Lifecycle.State.CREATED
        }
        
        fun onStart() {
            println("Activity: onStart()")
            lifecycleRegistry.currentState = Lifecycle.State.STARTED
        }
        
        fun onResume() {
            println("Activity: onResume()")
            lifecycleRegistry.currentState = Lifecycle.State.RESUMED
        }
        
        fun onPause() {
            println("Activity: onPause()")
            lifecycleRegistry.currentState = Lifecycle.State.STARTED
        }
        
        fun onStop() {
            println("Activity: onStop()")
            lifecycleRegistry.currentState = Lifecycle.State.CREATED
        }
        
        fun onDestroy() {
            println("Activity: onDestroy()")
            lifecycleRegistry.currentState = Lifecycle.State.DESTROYED
        }
    }
    
    // TODO: 学生实现ViewModel协程作用域
    class UserViewModel : ViewModel() {
        
        // ViewModel内置的协程作用域
        fun loadUserData(userId: String) {
            viewModelScope.launch {
                try {
                    println("ViewModel: 开始加载用户数据 $userId")
                    
                    // 并发加载多个数据源
                    val userInfo = async(Dispatchers.IO) {
                        delay(1000)
                        println("ViewModel: 用户基本信息加载完成")
                        "UserInfo-$userId"
                    }
                    
                    val userPreferences = async(Dispatchers.IO) {
                        delay(800)
                        println("ViewModel: 用户偏好设置加载完成")
                        "Preferences-$userId"
                    }
                    
                    val userStatistics = async(Dispatchers.Default) {
                        delay(600)
                        // 模拟统计计算
                        var stats = 0
                        repeat(1000) { stats += it }
                        println("ViewModel: 用户统计数据计算完成")
                        "Stats-$userId-$stats"
                    }
                    
                    // 等待所有数据加载完成
                    val info = userInfo.await()
                    val prefs = userPreferences.await()
                    val stats = userStatistics.await()
                    
                    // 切换到主线程更新UI状态
                    withContext(Dispatchers.Main) {
                        println("ViewModel: 更新UI状态")
                        println("ViewModel: 数据加载完成 - $info, $prefs, $stats")
                    }
                    
                } catch (e: CancellationException) {
                    println("ViewModel: 协程被取消 - ${e.message}")
                    throw e
                } catch (e: Exception) {
                    println("ViewModel: 加载失败 - ${e.message}")
                }
            }
        }
        
        fun performBackgroundTask() {
            viewModelScope.launch {
                repeat(10) { i ->
                    if (!isActive) {
                        println("ViewModel: 检测到协程已取消，停止执行")
                        return@launch
                    }
                    
                    println("ViewModel: 后台任务步骤 $i")
                    delay(300)
                }
                println("ViewModel: 后台任务完成")
            }
        }
        
        override fun onCleared() {
            super.onCleared()
            println("ViewModel: onCleared() - viewModelScope将自动取消所有协程")
        }
    }
    
    // TODO: 学生实现Fragment生命周期协程
    class UserFragment : Fragment() {
        
        // Fragment的生命周期协程作用域
        fun loadFragmentData() {
            // viewLifecycleOwner.lifecycleScope - 跟随View生命周期
            viewLifecycleOwner.lifecycleScope.launch {
                try {
                    println("Fragment: 开始加载Fragment数据")
                    
                    // 长时间运行的任务
                    repeat(20) { i ->
                        if (!isActive) {
                            println("Fragment: 协程被取消")
                            return@launch
                        }
                        
                        println("Fragment: 数据加载进度 ${i + 1}/20")
                        delay(200)
                    }
                    
                    println("Fragment: 数据加载完成")
                    
                } catch (e: CancellationException) {
                    println("Fragment: 协程被生命周期取消")
                    throw e
                }
            }
        }
        
        fun startPeriodicUpdate() {
            // lifecycleScope - 跟随Fragment生命周期
            lifecycleScope.launch {
                while (isActive) {
                    println("Fragment: 定期更新数据")
                    delay(1000)
                }
            }
        }
        
        // 模拟Fragment生命周期
        fun onViewCreated() {
            println("Fragment: onViewCreated()")
            loadFragmentData()
        }
        
        fun onStart() {
            println("Fragment: onStart()")
            startPeriodicUpdate()
        }
        
        fun onDestroyView() {
            println("Fragment: onDestroyView() - viewLifecycleOwner协程将被取消")
        }
        
        fun onDestroy() {
            println("Fragment: onDestroy() - lifecycleScope协程将被取消")
        }
    }
    
    // TODO: 学生实现生命周期感知的网络请求
    class NetworkRepository {
        
        suspend fun fetchDataWithLifecycle(lifecycleOwner: LifecycleOwner): String? {
            return try {
                // 使用生命周期协程作用域
                lifecycleOwner.lifecycleScope.async {
                    println("Repository: 开始网络请求")
                    
                    // 模拟长时间网络请求
                    repeat(10) { i ->
                        if (!isActive) {
                            println("Repository: 网络请求被取消")
                            return@async null
                        }
                        println("Repository: 网络请求进度 ${i + 1}/10")
                        delay(300)
                    }
                    
                    println("Repository: 网络请求完成")
                    "网络数据"
                }.await()
                
            } catch (e: CancellationException) {
                println("Repository: 请求因生命周期取消")
                null
            }
        }
        
        fun startRealtimeUpdates(lifecycleOwner: LifecycleOwner) {
            lifecycleOwner.lifecycleScope.launch {
                println("Repository: 开始实时更新")
                
                try {
                    while (isActive) {
                        // 模拟实时数据更新
                        val currentTime = System.currentTimeMillis()
                        println("Repository: 实时数据更新 - $currentTime")
                        delay(2000)
                    }
                } catch (e: CancellationException) {
                    println("Repository: 实时更新因生命周期取消")
                    throw e
                } finally {
                    println("Repository: 清理实时更新资源")
                }
            }
        }
    }
    
    // TODO: 学生实现生命周期协程的最佳实践演示
    suspend fun lifecycleBestPracticesDemo() {
        println("=== Android生命周期协程最佳实践 ===\n")
        
        val activity = MockActivity()
        val viewModel = UserViewModel()
        val fragment = UserFragment()
        val repository = NetworkRepository()
        
        // 1. Activity生命周期演示
        println("--- Activity生命周期协程演示 ---")
        activity.onCreate()
        activity.onStart()
        activity.onResume()
        
        // 开始长时间任务
        val activityJob = activity.lifecycleScope.launch {
            try {
                println("Activity: 开始长时间任务")
                repeat(15) { i ->
                    println("Activity: 任务进度 ${i + 1}/15")
                    delay(200)
                }
                println("Activity: 任务完成")
            } catch (e: CancellationException) {
                println("Activity: 任务被生命周期取消")
                throw e
            }
        }
        
        delay(1000)  // 让任务运行一会儿
        
        // 模拟Activity被销毁
        activity.onPause()
        activity.onStop()
        activity.onDestroy()  // 这里会自动取消所有lifecycleScope协程
        
        delay(500)  // 观察取消效果
        
        // 2. ViewModel协程作用域演示
        println("\n--- ViewModel协程作用域演示 ---")
        viewModel.loadUserData("user123")
        viewModel.performBackgroundTask()
        
        delay(1500)
        
        // 模拟ViewModel被清理
        viewModel.onCleared()  // 这里会自动取消所有viewModelScope协程
        
        delay(500)
        
        // 3. Fragment生命周期演示
        println("\n--- Fragment生命周期协程演示 ---")
        fragment.onViewCreated()
        fragment.onStart()
        
        delay(2000)
        
        fragment.onDestroyView()  // viewLifecycleOwner协程被取消
        delay(500)
        fragment.onDestroy()      // lifecycleScope协程被取消
        
        delay(500)
        
        // 4. 生命周期感知的Repository演示
        println("\n--- 生命周期感知Repository演示 ---")
        val newActivity = MockActivity()
        newActivity.onCreate()
        newActivity.onStart()
        newActivity.onResume()
        
        // 开始网络请求和实时更新
        repository.startRealtimeUpdates(newActivity)
        
        val networkJob = newActivity.lifecycleScope.launch {
            val data = repository.fetchDataWithLifecycle(newActivity)
            println("Activity: 收到网络数据 - $data")
        }
        
        delay(3000)
        
        // 模拟Activity被意外销毁
        println("\n模拟Activity被系统回收...")
        newActivity.onDestroy()
        
        delay(1000)
        
        println("\n生命周期协程演示完成")
    }
    
    // 错误场景演示 - 不使用生命周期协程的问题
    suspend fun problemsWithoutLifecycleCoroutines() {
        println("\n=== 不使用生命周期协程的问题演示 ===\n")
        
        val activity = MockActivity()
        activity.onCreate()
        activity.onResume()
        
        // 错误做法: 使用GlobalScope
        val globalJob = GlobalScope.launch {
            try {
                println("错误做法: 使用GlobalScope启动协程")
                repeat(20) { i ->
                    println("GlobalScope协程: 步骤 ${i + 1}/20")
                    delay(300)
                }
                println("GlobalScope协程: 完成 (这可能导致内存泄漏!)")
            } catch (e: Exception) {
                println("GlobalScope协程异常: ${e.message}")
            }
        }
        
        delay(2000)
        
        // Activity被销毁，但GlobalScope协程继续运行
        activity.onDestroy()
        println("Activity已销毁，但GlobalScope协程仍在运行...")
        
        delay(3000)
        
        // 手动取消(在实际应用中很容易忘记)
        globalJob.cancel()
        println("手动取消GlobalScope协程 (容易忘记，导致内存泄漏)")
        
        println("\n正确做法: 使用lifecycleScope可以自动管理协程生命周期")
    }
}

fun main() {
    val lifecycleDemo = AndroidLifecycleCoroutines()
    
    runBlocking {
        // 最佳实践演示
        lifecycleDemo.lifecycleBestPracticesDemo()
        
        // 错误做法对比
        lifecycleDemo.problemsWithoutLifecycleCoroutines()
    }
    
    println("\nAndroid生命周期协程演示完成！")
}
```

**📝 编程任务检查点**:
- [ ] **生命周期协程**: 理解lifecycleScope和viewLifecycleOwner的区别
- [ ] **ViewModel集成**: 掌握viewModelScope的使用和自动取消机制
- [ ] **Fragment协程**: 区分Fragment的不同生命周期协程作用域
- [ ] **网络请求**: 实现生命周期感知的Repository模式
- [ ] **内存泄漏防护**: 理解不使用生命周期协程的风险
- [ ] **最佳实践**: 掌握Android协程的标准使用模式
- [ ] **代码位置**: `student_progress/JavaLearning/src/AndroidLifecycleCoroutines.kt`