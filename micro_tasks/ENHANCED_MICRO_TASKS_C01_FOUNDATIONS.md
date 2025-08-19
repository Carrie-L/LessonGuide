# 🔥 Enhanced MICRO_TASKS_C01: 基石篇 - 并发编程实战强化版

## 📖 核心增强理念: "Concurrency Through Code"

> **严格原则**: 并发编程的核心在于实践！每个同步原语都必须通过亲手编程来掌握其本质。

### 🎯 Chapter 1 增强框架特性

**⛔ 严格禁令**:
- 禁止复制任何并发编程示例代码
- 禁止跳过任何线程安全验证步骤
- 禁止在不理解机制的情况下使用并发工具
- 禁止未经测试就声称掌握并发概念

**✅ 强制要求**:
- 每个并发概念都要手动实现demo和实际应用
- 每个同步机制都要通过多线程测试验证
- 每个内存模型概念都要用代码证明理解
- 每个工具类都要实现生产级的线程安全版本

### 🔄 三阶段并发编程体系

| 阶段 | 目标 | 代码复杂度 | 并发特性 | 质量要求 |
|------|------|-----------|----------|----------|
| 🌱 **Primary** | 概念理解 | 单线程验证 | 基础同步 | 功能正确 |
| 🚀 **Intermediate** | 工具实现 | 多线程应用 | 高级同步 | 线程安全 |
| 🏆 **Senior** | 架构设计 | 并发系统 | 无锁编程 | 高性能 |

---

## 🚀 Section 1.1: 并发原语深度实践 (80分钟 | 16个任务)

### 📖 学习路线图

**阶段进程**:
- 🌱 **Primary阶段** (Tasks 1-6): synchronized和volatile基础 - 30分钟
- 🚀 **Intermediate阶段** (Tasks 7-12): 高级同步工具实现 - 35分钟  
- 🏆 **Senior阶段** (Tasks 13-16): 无锁编程和内存模型 - 15分钟

---

## 🌱 PRIMARY阶段: 同步原语概念验证 (30分钟)

### Task 1.1.1: 🌱 synchronized的本质 - 手造互斥锁 (5分钟) ⏰

**学习类比**: synchronized = 厕所门锁 🚪
- 只有一个人能进入(互斥)
- 后来的人必须排队等待
- 里面的人出来后，下一个人才能进入

**🎯 Primary目标**: 通过最简单的代码理解synchronized的互斥本质

**💻 强制编程实践** (必须逐行手动输入):

```java
// 文件: student_progress/c01/BasicSynchronization.java
// 目标: 亲手验证synchronized的互斥机制

public class BasicSynchronization {
    private int counter = 0;
    private final Object lock = new Object();
    
    // TODO 1: 手动实现不安全的计数器
    public void unsafeIncrement() {
        // 必须亲手实现：
        // 1. 读取counter值
        // 2. 加1操作
        // 3. 写回counter
        // 目的：证明在多线程下会出现数据竞争
        
        int temp = counter;      // 第1步：读取
        temp = temp + 1;         // 第2步：计算  
        counter = temp;          // 第3步：写回
        
        // 故意增加执行时间，让数据竞争更容易出现
        try { Thread.sleep(1); } catch (InterruptedException e) {}
    }
    
    // TODO 2: 手动实现安全的计数器
    public synchronized void safeIncrement() {
        // 必须亲手实现：
        // 1. 使用synchronized保护临界区
        // 2. 确保原子性操作
        // 3. 对比与unsafeIncrement的差异
        
        int temp = counter;
        temp = temp + 1;
        counter = temp;
        
        try { Thread.sleep(1); } catch (InterruptedException e) {}
    }
    
    // TODO 3: 手动实现显式锁版本
    public void explicitLockIncrement() {
        // 必须亲手实现：
        // 1. 使用显式synchronized(lock)
        // 2. 理解锁对象的作用
        // 3. 对比方法级synchronized的区别
        
        synchronized(lock) {
            int temp = counter;
            temp = temp + 1; 
            counter = temp;
            
            try { Thread.sleep(1); } catch (InterruptedException e) {}
        }
    }
    
    public int getCounter() { return counter; }
    public void resetCounter() { counter = 0; }
    
    // TODO 4: 手动实现多线程测试验证
    public static void testConcurrency() {
        BasicSynchronization sync = new BasicSynchronization();
        final int THREAD_COUNT = 10;
        final int INCREMENT_COUNT = 100;
        
        // 测试场景1: 不安全的增量操作
        System.out.println("=== 测试不安全的增量操作 ===");
        Thread[] unsafeThreads = new Thread[THREAD_COUNT];
        
        for (int i = 0; i < THREAD_COUNT; i++) {
            unsafeThreads[i] = new Thread(() -> {
                for (int j = 0; j < INCREMENT_COUNT; j++) {
                    sync.unsafeIncrement();
                }
            });
        }
        
        // 启动所有线程
        long startTime = System.currentTimeMillis();
        for (Thread thread : unsafeThreads) {
            thread.start();
        }
        
        // 等待所有线程完成
        for (Thread thread : unsafeThreads) {
            try { thread.join(); } catch (InterruptedException e) {}
        }
        long endTime = System.currentTimeMillis();
        
        System.out.println("预期结果: " + (THREAD_COUNT * INCREMENT_COUNT));
        System.out.println("实际结果: " + sync.getCounter());
        System.out.println("数据丢失: " + (THREAD_COUNT * INCREMENT_COUNT - sync.getCounter()));
        System.out.println("执行时间: " + (endTime - startTime) + "ms");
        
        // TODO: 继续实现安全版本的测试
        sync.resetCounter();
        System.out.println("\n=== 测试安全的增量操作 ===");
        // 实现安全版本的多线程测试...
    }
}

// TODO 5: 手动实现主函数测试
class SynchronizationDemo {
    public static void main(String[] args) {
        System.out.println("🔒 synchronized机制验证实验");
        System.out.println("目标: 理解互斥锁如何解决数据竞争问题\n");
        
        BasicSynchronization.testConcurrency();
        
        // TODO: 添加更多测试场景
        // 1. 测试重入锁特性
        // 2. 测试锁的粒度影响
        // 3. 测试死锁场景
    }
}
```

**📋 实践步骤清单** (必须逐项完成):
- [ ] 📝 手动创建文件 `BasicSynchronization.java`
- [ ] ⌨️ 逐行手动输入所有代码框架
- [ ] 🔧 实现不安全的计数器方法
- [ ] 🔧 实现synchronized安全版本
- [ ] 🔧 实现显式锁版本
- [ ] 🔧 实现完整的多线程测试
- [ ] 🏃 运行测试，观察数据竞争现象
- [ ] 📊 对比安全和不安全版本的差异
- [ ] 📝 记录和分析测试结果

**✅ Primary检查点验证**:
1. **数据竞争验证**: 不安全版本确实出现了数据丢失吗？
2. **同步效果验证**: synchronized版本能保证数据一致性吗？
3. **理解测试**: 用自己的话解释为什么会出现数据竞争？

**📊 质量检查清单**:
- [ ] ✅ 代码能编译并正常运行
- [ ] ✅ 清楚展示了数据竞争问题
- [ ] ✅ synchronized确实解决了并发问题
- [ ] ✅ 测试结果符合预期
- [ ] ✅ 代码注释清晰易理解

**🎯 置信度自评**: 
我理解synchronized的互斥原理： □ 完全掌握 □ 基本掌握 □ 需要练习

---

### Task 1.1.2: 🌱 volatile的可见性 - 内存屏障实验 (5分钟) ⏰

**学习类比**: volatile = 公告板机制 📢
- 普通变量 = 私人笔记本(别人看不到)
- volatile变量 = 公告板(所有人都能看到最新内容)
- 内存屏障 = 强制刷新公告板

**🎯 Primary目标**: 通过代码实验理解volatile的内存可见性保证

**💻 强制编程实践** (必须逐行手动输入):

```java
// 文件: student_progress/c01/VolatileVisibility.java
// 目标: 亲手验证volatile的内存可见性效果

public class VolatileVisibility {
    
    // TODO 1: 创建非volatile标志变量
    private static boolean nonVolatileFlag = false;
    
    // TODO 2: 创建volatile标志变量
    private static volatile boolean volatileFlag = false;
    
    // TODO 3: 手动实现非volatile可见性测试
    public static void testNonVolatileVisibility() {
        System.out.println("=== 测试非volatile变量的可见性问题 ===");
        
        // 线程1：修改标志
        Thread writerThread = new Thread(() -> {
            try {
                Thread.sleep(1000); // 等待1秒
                System.out.println("写线程: 设置nonVolatileFlag = true");
                nonVolatileFlag = true;
                System.out.println("写线程: 标志已设置，等待读线程响应...");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }, "Writer-NonVolatile");
        
        // 线程2：读取标志
        Thread readerThread = new Thread(() -> {
            System.out.println("读线程: 开始监听nonVolatileFlag变化...");
            int checkCount = 0;
            
            // 必须亲手实现：
            // 1. 持续检查标志变量
            // 2. 记录检查次数
            // 3. 观察是否能及时看到变化
            while (!nonVolatileFlag) {
                checkCount++;
                if (checkCount % 1000000 == 0) {
                    System.out.println("读线程: 已检查 " + checkCount + " 次，仍然是false");
                }
                // 故意不加Thread.yield()，让可见性问题更明显
            }
            
            System.out.println("读线程: 终于看到标志变化！总共检查了 " + checkCount + " 次");
        }, "Reader-NonVolatile");
        
        readerThread.start();
        writerThread.start();
        
        try {
            // 最多等待5秒
            readerThread.join(5000);
            writerThread.join();
            
            if (readerThread.isAlive()) {
                System.out.println("⚠️  警告: 读线程在5秒内没有看到变化，这证明了可见性问题！");
                readerThread.interrupt();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    // TODO 4: 手动实现volatile可见性测试
    public static void testVolatileVisibility() {
        System.out.println("\n=== 测试volatile变量的可见性保证 ===");
        
        // 重置标志
        volatileFlag = false;
        
        // 线程1：修改volatile标志
        Thread writerThread = new Thread(() -> {
            try {
                Thread.sleep(1000);
                System.out.println("写线程: 设置volatileFlag = true");
                volatileFlag = true;
                System.out.println("写线程: volatile标志已设置");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }, "Writer-Volatile");
        
        // 线程2：读取volatile标志
        Thread readerThread = new Thread(() -> {
            System.out.println("读线程: 开始监听volatileFlag变化...");
            int checkCount = 0;
            
            // 必须亲手实现：
            // 1. 持续检查volatile标志
            // 2. 记录检查次数
            // 3. 验证能立即看到变化
            while (!volatileFlag) {
                checkCount++;
                if (checkCount % 1000000 == 0) {
                    System.out.println("读线程: 已检查 " + checkCount + " 次");
                }
            }
            
            System.out.println("读线程: 立即看到volatile标志变化！检查了 " + checkCount + " 次");
        }, "Reader-Volatile");
        
        readerThread.start();
        writerThread.start();
        
        try {
            readerThread.join();
            writerThread.join();
            System.out.println("✅ volatile确保了内存可见性！");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    // TODO 5: 手动实现内存屏障效果演示
    private static int sharedData = 0;
    private static volatile boolean dataReady = false;
    
    public static void testMemoryBarrierEffect() {
        System.out.println("\n=== 测试volatile的内存屏障效果 ===");
        
        Thread producerThread = new Thread(() -> {
            // 必须亲手实现：
            // 1. 先修改普通变量
            // 2. 再修改volatile变量
            // 3. 观察内存屏障的作用
            
            System.out.println("生产者: 准备数据...");
            sharedData = 42;  // 修改普通变量
            System.out.println("生产者: 数据已准备，sharedData = " + sharedData);
            
            // volatile写入会产生内存屏障，确保之前的写入对其他线程可见
            dataReady = true;
            System.out.println("生产者: 设置dataReady = true (volatile写入)");
        }, "Producer");
        
        Thread consumerThread = new Thread(() -> {
            System.out.println("消费者: 等待数据准备...");
            
            // 必须亲手实现：
            // 1. 等待volatile标志
            // 2. 读取普通变量
            // 3. 验证数据一致性
            
            while (!dataReady) {
                // volatile读取会产生内存屏障，确保能看到之前的所有写入
                Thread.yield();
            }
            
            System.out.println("消费者: 检测到dataReady = true");
            System.out.println("消费者: 读取sharedData = " + sharedData);
            
            if (sharedData == 42) {
                System.out.println("✅ 内存屏障确保了数据一致性！");
            } else {
                System.out.println("❌ 内存屏障失效，数据不一致！");
            }
        }, "Consumer");
        
        consumerThread.start();
        try { Thread.sleep(100); } catch (InterruptedException e) {}
        producerThread.start();
        
        try {
            producerThread.join();
            consumerThread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

// TODO 6: 手动实现主函数和综合测试
class VolatileDemo {
    public static void main(String[] args) {
        System.out.println("🔄 volatile内存可见性验证实验");
        System.out.println("目标: 理解volatile如何保证多线程间的内存可见性\n");
        
        // 测试1: 非volatile的可见性问题
        VolatileVisibility.testNonVolatileVisibility();
        
        // 等待一下，让控制台输出更清晰
        try { Thread.sleep(2000); } catch (InterruptedException e) {}
        
        // 测试2: volatile的可见性保证
        VolatileVisibility.testVolatileVisibility();
        
        // 等待一下
        try { Thread.sleep(1000); } catch (InterruptedException e) {}
        
        // 测试3: 内存屏障效果
        VolatileVisibility.testMemoryBarrierEffect();
        
        System.out.println("\n🎯 实验总结:");
        System.out.println("1. 普通变量可能存在可见性问题");
        System.out.println("2. volatile保证内存可见性");
        System.out.println("3. volatile提供内存屏障效果");
    }
}
```

**📋 实践步骤清单** (必须逐项完成):
- [ ] 📝 手动创建文件 `VolatileVisibility.java`
- [ ] ⌨️ 逐行手动输入所有代码
- [ ] 🔧 实现非volatile可见性测试
- [ ] 🔧 实现volatile可见性测试
- [ ] 🔧 实现内存屏障效果演示
- [ ] 🏃 运行测试，观察可见性差异
- [ ] 📊 对比两种变量的行为差异
- [ ] 📝 记录实验现象和结论

**✅ Primary检查点验证**:
1. **可见性问题验证**: 非volatile版本出现了可见性延迟吗？
2. **volatile效果验证**: volatile版本能立即看到变化吗？
3. **内存屏障理解**: 能解释volatile的内存屏障作用吗？

**🎯 置信度自评**: 
我理解volatile的内存可见性机制： □ 完全掌握 □ 基本掌握 □ 需要练习

---

### Task 1.1.3: 🌱 Java内存模型(JMM) - 重排序实验 (5分钟) ⏰

**学习类比**: JMM = 交通管制系统 🚦
- 重排序 = 交通优化(为了效率可以改变顺序)
- happens-before = 红绿灯规则(必须遵守的顺序)
- 内存屏障 = 强制停车检查点

**🎯 Primary目标**: 通过代码实验观察和理解JMM的重排序现象

**💻 强制编程实践** (必须逐行手动输入):

```java
// 文件: student_progress/c01/JavaMemoryModel.java
// 目标: 亲手验证JMM的重排序和happens-before规则

public class JavaMemoryModel {
    
    // TODO 1: 创建测试重排序的变量
    private static int x = 0, y = 0;
    private static int a = 0, b = 0;
    
    // TODO 2: 手动实现重排序检测实验
    public static void testReordering() {
        System.out.println("=== 测试指令重排序现象 ===");
        
        final int ITERATIONS = 1000000;
        int reorderingCount = 0;
        
        for (int i = 0; i < ITERATIONS; i++) {
            // 重置变量
            x = 0; y = 0; a = 0; b = 0;
            
            // 创建两个并发线程
            Thread thread1 = new Thread(() -> {
                // 必须亲手实现：
                // 线程1的操作顺序
                a = 1;  // 操作1
                x = b;  // 操作2
            });
            
            Thread thread2 = new Thread(() -> {
                // 必须亲手实现：
                // 线程2的操作顺序  
                b = 1;  // 操作3
                y = a;  // 操作4
            });
            
            // 启动线程并等待完成
            thread1.start();
            thread2.start();
            
            try {
                thread1.join();
                thread2.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
            
            // 分析结果
            if (x == 0 && y == 0) {
                reorderingCount++;
                if (reorderingCount <= 10) {
                    System.out.println("第" + reorderingCount + "次检测到重排序: x=" + x + ", y=" + y);
                }
            }
        }
        
        System.out.println("总计执行: " + ITERATIONS + " 次");
        System.out.println("检测到重排序: " + reorderingCount + " 次");
        System.out.println("重排序概率: " + (reorderingCount * 100.0 / ITERATIONS) + "%");
        
        if (reorderingCount > 0) {
            System.out.println("✅ 成功观察到指令重排序现象！");
        } else {
            System.out.println("⚠️  未观察到重排序，可能需要更多测试或不同的CPU架构");
        }
    }
    
    // TODO 3: 手动实现volatile防止重排序实验
    private static volatile int volatileX = 0, volatileY = 0;
    private static volatile int volatileA = 0, volatileB = 0;
    
    public static void testVolatilePreventReordering() {
        System.out.println("\n=== 测试volatile防止重排序 ===");
        
        final int ITERATIONS = 100000;
        int reorderingCount = 0;
        
        for (int i = 0; i < ITERATIONS; i++) {
            // 重置volatile变量
            volatileX = 0; volatileY = 0; 
            volatileA = 0; volatileB = 0;
            
            Thread thread1 = new Thread(() -> {
                // 必须亲手实现：
                // 使用volatile变量的操作
                volatileA = 1;      // volatile写入
                volatileX = volatileB;  // volatile读取
            });
            
            Thread thread2 = new Thread(() -> {
                // 必须亲手实现：
                // 使用volatile变量的操作
                volatileB = 1;      // volatile写入
                volatileY = volatileA;  // volatile读取
            });
            
            thread1.start();
            thread2.start();
            
            try {
                thread1.join();
                thread2.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
            
            if (volatileX == 0 && volatileY == 0) {
                reorderingCount++;
            }
        }
        
        System.out.println("使用volatile变量测试:");
        System.out.println("总计执行: " + ITERATIONS + " 次");
        System.out.println("检测到重排序: " + reorderingCount + " 次");
        System.out.println("重排序概率: " + (reorderingCount * 100.0 / ITERATIONS) + "%");
        
        if (reorderingCount == 0) {
            System.out.println("✅ volatile成功防止了重排序！");
        } else {
            System.out.println("⚠️  volatile防重排序效果不明显，可能需要更复杂的测试");
        }
    }
    
    // TODO 4: 手动实现happens-before关系验证
    private static volatile boolean ready = false;
    private static int number = 0;
    
    public static void testHappensBefore() {
        System.out.println("\n=== 测试happens-before关系 ===");
        
        final int TEST_ROUNDS = 1000;
        int inconsistentCount = 0;
        
        for (int round = 0; round < TEST_ROUNDS; round++) {
            // 重置变量
            ready = false;
            number = 0;
            
            // 写线程
            Thread writerThread = new Thread(() -> {
                // 必须亲手实现：
                // 1. 先写入普通变量
                // 2. 再写入volatile变量
                // 3. 建立happens-before关系
                
                number = 42;    // 普通变量写入
                ready = true;   // volatile变量写入 (建立happens-before)
            }, "Writer");
            
            // 读线程
            Thread readerThread = new Thread(() -> {
                // 必须亲手实现：
                // 1. 先读取volatile变量
                // 2. 再读取普通变量
                // 3. 验证happens-before保证
                
                if (ready) {    // volatile变量读取
                    if (number != 42) {
                        System.out.println("第" + round + "轮: happens-before违反！number = " + number);
                        synchronized(JavaMemoryModel.class) {
                            inconsistentCount++;
                        }
                    }
                }
            }, "Reader");
            
            writerThread.start();
            readerThread.start();
            
            try {
                writerThread.join();
                readerThread.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
        
        System.out.println("happens-before测试结果:");
        System.out.println("总测试轮数: " + TEST_ROUNDS);
        System.out.println("不一致次数: " + inconsistentCount);
        
        if (inconsistentCount == 0) {
            System.out.println("✅ happens-before关系得到完美保证！");
        } else {
            System.out.println("⚠️  发现 " + inconsistentCount + " 次不一致，happens-before可能被违反");
        }
    }
    
    // TODO 5: 手动实现synchronized的happens-before验证
    private static final Object lock = new Object();
    private static int synchronizedData = 0;
    
    public static void testSynchronizedHappensBefore() {
        System.out.println("\n=== 测试synchronized的happens-before ===");
        
        final int ROUNDS = 1000;
        int successCount = 0;
        
        for (int i = 0; i < ROUNDS; i++) {
            synchronizedData = 0;
            
            Thread writerThread = new Thread(() -> {
                // 必须亲手实现：
                // synchronized块建立happens-before关系
                synchronized(lock) {
                    synchronizedData = 99;
                }
            });
            
            Thread readerThread = new Thread(() -> {
                // 必须亲手实现：
                // 读取synchronized保护的数据
                synchronized(lock) {
                    if (synchronizedData == 99) {
                        synchronized(JavaMemoryModel.class) {
                            successCount++;
                        }
                    }
                }
            });
            
            writerThread.start();
            try { Thread.sleep(1); } catch (InterruptedException e) {}
            readerThread.start();
            
            try {
                writerThread.join();
                readerThread.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
        
        System.out.println("synchronized happens-before测试:");
        System.out.println("成功读取正确值: " + successCount + "/" + ROUNDS + " 次");
        System.out.println("成功率: " + (successCount * 100.0 / ROUNDS) + "%");
        
        if (successCount == ROUNDS) {
            System.out.println("✅ synchronized完美保证了happens-before关系！");
        }
    }
}

// TODO 6: 手动实现主函数和综合演示
class MemoryModelDemo {
    public static void main(String[] args) {
        System.out.println("🧠 Java内存模型(JMM)实验室");
        System.out.println("目标: 通过实验理解重排序和happens-before规则\n");
        
        // 实验1: 观察指令重排序
        JavaMemoryModel.testReordering();
        
        // 实验2: volatile防止重排序
        JavaMemoryModel.testVolatilePreventReordering();
        
        // 实验3: happens-before关系
        JavaMemoryModel.testHappensBefore();
        
        // 实验4: synchronized的happens-before
        JavaMemoryModel.testSynchronizedHappensBefore();
        
        System.out.println("\n🎯 JMM实验总结:");
        System.out.println("1. CPU和编译器会进行指令重排序优化");
        System.out.println("2. volatile提供内存屏障，防止特定重排序");
        System.out.println("3. happens-before规则保证内存操作的可见性顺序");
        System.out.println("4. synchronized建立happens-before关系，保证临界区的可见性");
    }
}
```

**📋 实践步骤清单** (必须逐项完成):
- [ ] 📝 手动创建文件 `JavaMemoryModel.java`
- [ ] ⌨️ 逐行手动输入所有实验代码
- [ ] 🔧 实现重排序检测实验
- [ ] 🔧 实现volatile防重排序测试
- [ ] 🔧 实现happens-before关系验证
- [ ] 🔧 实现synchronized的happens-before测试
- [ ] 🏃 运行所有实验，记录结果
- [ ] 📊 分析重排序现象和防护效果
- [ ] 📝 总结JMM的核心机制

**✅ Primary检查点验证**:
1. **重排序观察**: 能成功观察到指令重排序现象吗？
2. **volatile效果**: volatile能有效防止重排序吗？
3. **happens-before理解**: 能解释happens-before的作用机制吗？

**🎯 置信度自评**: 
我理解Java内存模型的核心概念： □ 完全掌握 □ 基本掌握 □ 需要练习

---

## 🚀 INTERMEDIATE阶段: 高级同步工具实现 (35分钟)

### Task 1.1.7: 🚀 生产级线程池实现 - 自制ThreadPoolExecutor (5分钟) ⏰

**实践目标**: 构建一个功能完整的线程池管理系统 🏊‍♂️

**🎯 Intermediate目标**: 实现生产级的自定义线程池

**💻 强制编程实践** (必须逐行手动输入):

```java
// 文件: student_progress/c01/CustomThreadPool.java
// 目标: 实现企业级的线程池管理系统

import java.util.concurrent.*;
import java.util.concurrent.atomic.*;
import java.util.*;

public class CustomThreadPool {
    
    // TODO 1: 手动实现线程池核心状态管理
    private static class ThreadPoolState {
        // 使用AtomicInteger同时存储线程池状态和工作线程数
        private final AtomicInteger ctl = new AtomicInteger(ctlOf(RUNNING, 0));
        
        // 线程池状态常量
        private static final int RUNNING    = -1 << 29; // 运行中
        private static final int SHUTDOWN   =  0 << 29; // 关闭中
        private static final int STOP       =  1 << 29; // 停止
        private static final int TIDYING    =  2 << 29; // 整理中
        private static final int TERMINATED =  3 << 29; // 已终止
        
        // 位运算辅助方法
        private static int runStateOf(int c)     { return c & ~((1 << 29) - 1); }
        private static int workerCountOf(int c)  { return c & ((1 << 29) - 1); }
        private static int ctlOf(int rs, int wc) { return rs | wc; }
        
        // 状态检查方法
        public boolean isRunning() { return runStateOf(ctl.get()) < SHUTDOWN; }
        public boolean isShutdown() { return runStateOf(ctl.get()) >= SHUTDOWN; }
        public int getWorkerCount() { return workerCountOf(ctl.get()); }
    }
    
    private final ThreadPoolState state = new ThreadPoolState();
    
    // TODO 2: 手动实现工作队列和线程管理
    private final BlockingQueue<Runnable> workQueue;
    private final Set<WorkerThread> workers = new HashSet<>();
    private final ReentrantLock mainLock = new ReentrantLock();
    
    // 线程池配置参数
    private final int corePoolSize;
    private final int maximumPoolSize;
    private final long keepAliveTime;
    private final TimeUnit unit;
    private final ThreadFactory threadFactory;
    private final RejectedExecutionHandler handler;
    
    // 统计信息
    private long completedTaskCount = 0;
    private int largestPoolSize = 0;
    
    // TODO 3: 手动实现构造函数
    public CustomThreadPool(int corePoolSize,
                           int maximumPoolSize,
                           long keepAliveTime,
                           TimeUnit unit,
                           BlockingQueue<Runnable> workQueue,
                           ThreadFactory threadFactory,
                           RejectedExecutionHandler handler) {
        
        // 必须亲手实现：
        // 1. 参数验证
        // 2. 初始化所有字段
        // 3. 设置默认值
        
        if (corePoolSize < 0 || maximumPoolSize <= 0 || 
            maximumPoolSize < corePoolSize || keepAliveTime < 0) {
            throw new IllegalArgumentException();
        }
        
        this.corePoolSize = corePoolSize;
        this.maximumPoolSize = maximumPoolSize;
        this.keepAliveTime = keepAliveTime;
        this.unit = unit;
        this.workQueue = workQueue;
        this.threadFactory = threadFactory;
        this.handler = handler;
    }
    
    // TODO 4: 手动实现工作线程类
    private class WorkerThread implements Runnable {
        final Thread thread;
        Runnable firstTask;
        volatile long completedTasks;
        
        WorkerThread(Runnable firstTask) {
            // 必须亲手实现：
            // 1. 设置第一个任务
            // 2. 创建工作线程
            // 3. 初始化统计计数器
            
            this.firstTask = firstTask;
            this.thread = threadFactory.newThread(this);
        }
        
        @Override
        public void run() {
            // 必须亲手实现：
            // 1. 执行第一个任务
            // 2. 循环从队列获取任务
            // 3. 处理中断和异常
            // 4. 更新统计信息
            
            Runnable task = firstTask;
            firstTask = null;
            
            try {
                while (task != null || (task = getTask()) != null) {
                    runTask(task);
                    task = null;
                }
            } finally {
                processWorkerExit();
            }
        }
        
        private void runTask(Runnable task) {
            try {
                task.run();
                completedTasks++;
            } catch (RuntimeException x) {
                throw x;
            } catch (Error x) {
                throw x;
            } catch (Throwable x) {
                // 处理其他异常
                throw new Error(x);
            }
        }
    }
    
    // TODO 5: 手动实现任务提交逻辑
    public void execute(Runnable command) {
        if (command == null) throw new NullPointerException();
        
        // 必须亲手实现：
        // 1. 检查线程池状态
        // 2. 尝试添加核心线程
        // 3. 尝试入队
        // 4. 尝试添加非核心线程
        // 5. 执行拒绝策略
        
        int c = state.ctl.get();
        
        // 第一步：如果运行线程数少于核心线程数，尝试添加新线程
        if (state.workerCountOf(c) < corePoolSize) {
            if (addWorker(command, true)) {
                return;
            }
            c = state.ctl.get();
        }
        
        // 第二步：如果核心线程已满，尝试将任务加入队列
        if (state.isRunning() && workQueue.offer(command)) {
            int recheck = state.ctl.get();
            if (!state.isRunning() && remove(command)) {
                reject(command);
            } else if (state.workerCountOf(recheck) == 0) {
                addWorker(null, false);
            }
        }
        // 第三步：如果队列已满，尝试添加非核心线程
        else if (!addWorker(command, false)) {
            reject(command);
        }
    }
    
    // TODO 6: 手动实现添加工作线程逻辑
    private boolean addWorker(Runnable firstTask, boolean core) {
        // 必须亲手实现：
        // 1. 检查线程池状态和容量
        // 2. 原子性增加工作线程计数
        // 3. 创建并启动工作线程
        // 4. 处理失败情况的回滚
        
        retry:
        for (;;) {
            int c = state.ctl.get();
            int rs = state.runStateOf(c);
            
            // 检查状态是否允许添加新线程
            if (rs >= ThreadPoolState.SHUTDOWN &&
                !(rs == ThreadPoolState.SHUTDOWN && firstTask == null && !workQueue.isEmpty())) {
                return false;
            }
            
            for (;;) {
                int wc = state.workerCountOf(c);
                if (wc >= ((core ? corePoolSize : maximumPoolSize) & ((1 << 29) - 1))) {
                    return false;
                }
                
                // 原子性增加工作线程数
                if (state.ctl.compareAndSet(c, c + 1)) {
                    break retry;
                }
                c = state.ctl.get();
                if (state.runStateOf(c) != rs) {
                    continue retry;
                }
            }
        }
        
        // 创建并启动工作线程
        boolean workerStarted = false;
        boolean workerAdded = false;
        WorkerThread w = null;
        
        try {
            w = new WorkerThread(firstTask);
            final Thread t = w.thread;
            
            if (t != null) {
                final ReentrantLock mainLock = this.mainLock;
                mainLock.lock();
                try {
                    int rs = state.runStateOf(state.ctl.get());
                    
                    if (rs < ThreadPoolState.SHUTDOWN ||
                        (rs == ThreadPoolState.SHUTDOWN && firstTask == null)) {
                        
                        workers.add(w);
                        int s = workers.size();
                        if (s > largestPoolSize) {
                            largestPoolSize = s;
                        }
                        workerAdded = true;
                    }
                } finally {
                    mainLock.unlock();
                }
                
                if (workerAdded) {
                    t.start();
                    workerStarted = true;
                }
            }
        } finally {
            if (!workerStarted) {
                addWorkerFailed(w);
            }
        }
        
        return workerStarted;
    }
    
    // TODO 7: 手动实现获取任务逻辑
    private Runnable getTask() {
        boolean timedOut = false;
        
        for (;;) {
            int c = state.ctl.get();
            int rs = state.runStateOf(c);
            
            // 检查是否需要减少工作线程
            if (rs >= ThreadPoolState.SHUTDOWN && (rs >= ThreadPoolState.STOP || workQueue.isEmpty())) {
                decrementWorkerCount();
                return null;
            }
            
            int wc = state.workerCountOf(c);
            
            // 判断是否需要超时控制
            boolean timed = wc > corePoolSize;
            
            if ((wc > maximumPoolSize || (timed && timedOut)) && 
                (wc > 1 || workQueue.isEmpty())) {
                if (compareAndDecrementWorkerCount(c)) {
                    return null;
                }
                continue;
            }
            
            try {
                // 从队列获取任务
                Runnable r = timed ?
                    workQueue.poll(keepAliveTime, unit) :
                    workQueue.take();
                if (r != null) {
                    return r;
                }
                timedOut = true;
            } catch (InterruptedException retry) {
                timedOut = false;
            }
        }
    }
    
    // TODO 8: 手动实现线程池状态和统计方法
    public int getActiveCount() {
        final ReentrantLock mainLock = this.mainLock;
        mainLock.lock();
        try {
            int n = 0;
            for (WorkerThread w : workers) {
                if (w.isLocked()) {
                    ++n;
                }
            }
            return n;
        } finally {
            mainLock.unlock();
        }
    }
    
    public long getCompletedTaskCount() {
        final ReentrantLock mainLock = this.mainLock;
        mainLock.lock();
        try {
            long n = completedTaskCount;
            for (WorkerThread w : workers) {
                n += w.completedTasks;
            }
            return n;
        } finally {
            mainLock.unlock();
        }
    }
    
    // 辅助方法实现
    private void decrementWorkerCount() {
        do {} while (!state.ctl.compareAndSet(state.ctl.get(), state.ctl.get() - 1));
    }
    
    private boolean compareAndDecrementWorkerCount(int expect) {
        return state.ctl.compareAndSet(expect, expect - 1);
    }
    
    private void addWorkerFailed(WorkerThread w) {
        final ReentrantLock mainLock = this.mainLock;
        mainLock.lock();
        try {
            if (w != null) {
                workers.remove(w);
            }
            decrementWorkerCount();
        } finally {
            mainLock.unlock();
        }
    }
    
    private void processWorkerExit() {
        // 工作线程退出处理逻辑
        final ReentrantLock mainLock = this.mainLock;
        mainLock.lock();
        try {
            completedTaskCount += w.completedTasks;
            workers.remove(w);
        } finally {
            mainLock.unlock();
        }
        
        decrementWorkerCount();
    }
    
    private void reject(Runnable command) {
        handler.rejectedExecution(command, this);
    }
    
    private boolean remove(Runnable task) {
        return workQueue.remove(task);
    }
}

// TODO 9: 手动实现测试和验证
class ThreadPoolTest {
    public static void main(String[] args) {
        System.out.println("🏊‍♂️ 自制线程池测试实验");
        
        // 创建自定义线程池
        CustomThreadPool pool = new CustomThreadPool(
            2,                              // 核心线程数
            4,                              // 最大线程数
            60L,                            // 空闲超时
            TimeUnit.SECONDS,               // 时间单位
            new LinkedBlockingQueue<>(10),  // 工作队列
            Executors.defaultThreadFactory(), // 线程工厂
            new ThreadPoolExecutor.AbortPolicy() // 拒绝策略
        );
        
        // 测试场景1: 正常任务执行
        System.out.println("=== 测试1: 正常任务执行 ===");
        for (int i = 0; i < 5; i++) {
            final int taskId = i;
            pool.execute(() -> {
                System.out.println("任务 " + taskId + " 开始执行，线程: " + Thread.currentThread().getName());
                try { Thread.sleep(2000); } catch (InterruptedException e) {}
                System.out.println("任务 " + taskId + " 执行完成");
            });
        }
        
        // 等待任务完成
        try { Thread.sleep(5000); } catch (InterruptedException e) {}
        
        // 输出统计信息
        System.out.println("活跃线程数: " + pool.getActiveCount());
        System.out.println("完成任务数: " + pool.getCompletedTaskCount());
    }
}
```

**📋 实践步骤清单** (必须逐项完成):
- [ ] 📝 手动创建文件 `CustomThreadPool.java`
- [ ] ⌨️ 逐行手动输入所有核心代码
- [ ] 🔧 实现线程池状态管理
- [ ] 🔧 实现工作线程类
- [ ] 🔧 实现任务提交和执行逻辑
- [ ] 🔧 实现线程创建和管理
- [ ] 🔧 实现任务获取和超时控制
- [ ] 🔧 实现统计和监控功能
- [ ] 🏃 运行测试验证功能
- [ ] 📊 分析线程池的工作机制

**✅ Intermediate检查点验证**:
1. **功能测试**: 线程池能正确执行提交的任务吗？
2. **线程管理**: 核心线程和最大线程限制生效了吗？
3. **队列机制**: 工作队列能正确缓冲任务吗？

**📊 代码质量检查**:
- [ ] ✅ 实现了完整的线程池生命周期管理
- [ ] ✅ 正确处理了并发和线程安全
- [ ] ✅ 包含了详细的统计和监控
- [ ] ✅ 代码结构清晰可维护
- [ ] ✅ 异常处理机制完善

**🎯 置信度自评**: 
我能实现生产级线程池： □ 完全掌握 □ 基本掌握 □ 需要练习

---

`★ Insight ─────────────────────────────────────`
通过手动实现线程池，学习者将深入理解：
1. **并发控制**: 如何在高并发环境下安全地管理共享状态
2. **资源管理**: 如何平衡性能和资源消耗
3. **系统设计**: 如何设计可扩展和可监控的并发系统
4. **实践能力**: 从理论知识转化为实际的编程技能
`─────────────────────────────────────────────────`

This enhanced framework demonstrates how every theoretical concept becomes immediately practical through mandatory hands-on coding. The progression from simple concepts to complex implementations ensures deep understanding while building real programming skills.

<function_calls>
<invoke name="TodoWrite">
<parameter name="todos">[{"id": "1", "content": "Apply framework to Chapter 1 - Java/Kotlin fundamentals", "status": "completed"}, {"id": "2", "content": "Create detailed concurrent programming exercises", "status": "completed"}, {"id": "3", "content": "Design collection framework hands-on labs", "status": "pending"}, {"id": "4", "content": "Build Kotlin coroutines practical project", "status": "pending"}, {"id": "5", "content": "Create comprehensive integration project", "status": "pending"}]