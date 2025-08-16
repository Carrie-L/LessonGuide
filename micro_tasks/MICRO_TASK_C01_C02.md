# ADHD-Friendly Micro Tasks (5分钟每个任务)

## 🎯 当前学习: 第一章 1.1 并发原语：synchronized, volatile 与 JMM

### Phase 1: 基础概念理解 (20分钟总计)

#### Task 1.1.1: JMM概念入门 (5分钟) ⏰
- [ ] **学习目标**: 理解什么是Java内存模型
- [ ] **具体任务**: 阅读并理解"工作内存vs主内存"的概念
- [ ] **检查点**: 能用自己的话解释JMM的基本结构
- [ ] **文件**: 在`student_progress/`创建`jmm_notes.md`记录理解

#### Task 1.1.2: happens-before原则 (5分钟) ⏰  
- [ ] **学习目标**: 理解happens-before的核心思想
- [ ] **具体任务**: 学习"如果A happens-before B，那么A的结果对B可见"
- [ ] **检查点**: 能举例说明happens-before关系
- [ ] **文件**: 在`jmm_notes.md`中添加happens-before例子

#### Task 1.1.3: synchronized基本原理 (5分钟) ⏰
- [ ] **学习目标**: 理解synchronized的monitor机制
- [ ] **具体任务**: 学习"每个对象都有monitor锁"的概念
- [ ] **检查点**: 能解释线程如何获取和释放monitor
- [ ] **文件**: 在`student_progress/`创建`synchronized_notes.md`

#### Task 1.1.4: volatile基本原理 (5分钟) ⏰
- [ ] **学习目标**: 理解volatile的两个作用
- [ ] **具体任务**: 学习"可见性+禁止重排序"
- [ ] **检查点**: 能解释为什么volatile不能保证原子性
- [ ] **文件**: 在`student_progress/`创建`volatile_notes.md`

### Phase 2: 锁升级机制深入 (15分钟总计)

#### Task 1.1.5: 偏向锁概念 (5分钟) ⏰
- [ ] **学习目标**: 理解偏向锁的优化思想
- [ ] **具体任务**: 学习"锁偏向第一个获取它的线程"
- [ ] **检查点**: 能解释偏向锁什么时候有效
- [ ] **文件**: 在`synchronized_notes.md`中添加偏向锁部分

#### Task 1.1.6: 轻量级锁机制 (5分钟) ⏰
- [ ] **学习目标**: 理解CAS自旋的原理
- [ ] **具体任务**: 学习轻量级锁的CAS操作
- [ ] **检查点**: 能解释为什么叫"轻量级"
- [ ] **文件**: 继续在`synchronized_notes.md`中添加

#### Task 1.1.7: 重量级锁升级 (5分钟) ⏰
- [ ] **学习目标**: 理解什么时候升级为重量级锁
- [ ] **具体任务**: 学习"用户态vs内核态切换"的开销
- [ ] **检查点**: 能说出锁升级的完整流程
- [ ] **文件**: 完善`synchronized_notes.md`

### Phase 3: 动手编码验证 (30分钟总计)

#### Task 1.1.8: 创建多线程同步实验室 (5分钟) ⏰
- [ ] **学习目标**: 搭建多线程测试环境，观察数据竞争
- [ ] **具体任务**: 
  ```java
  public class ConcurrencyLab {
      private static int counter = 0;
      private static final Object lock = new Object();
      
      // 第一步：实现不安全的计数器增加
      public static void unsafeIncrement() {
          for (int i = 0; i < 1000; i++) {
              counter++; // 观察数据竞争
          }
      }
      
      // 第二步：使用synchronized实现安全增加
      public static void safeIncrement() {
          synchronized (lock) {
              for (int i = 0; i < 1000; i++) {
                  counter++; // 观察同步效果
              }
          }
      }
  }
  ```
- [ ] **检查点**: 运行多线程测试，观察unsafe vs safe的结果差异
- [ ] **编程练习**: 创建10个线程，每个线程调用unsafeIncrement()，观察最终counter值是否等于10000
- [ ] **文件**: `student_progress/ConcurrencyLab.java`

#### Task 1.1.9: synchronized锁升级观察实验 (5分钟) ⏰
- [ ] **学习目标**: 验证synchronized的锁升级机制
- [ ] **具体任务**: 
  ```java
  public class LockEscalationDemo {
      private final Object monitor = new Object();
      
      // 练习1：单线程偏向锁观察
      public void biasedLockTest() {
          long startTime = System.nanoTime();
          for (int i = 0; i < 1000000; i++) {
              synchronized (monitor) {
                  // 空操作，观察偏向锁的性能
              }
          }
          long endTime = System.nanoTime();
          System.out.println("偏向锁耗时: " + (endTime - startTime) + "ns");
      }
      
      // 练习2：多线程轻量级锁
      public void lightweightLockTest() throws InterruptedException {
          Thread t1 = new Thread(() -> {
              for (int i = 0; i < 100000; i++) {
                  synchronized (monitor) {
                      // 观察CAS自旋
                  }
              }
          });
          
          Thread t2 = new Thread(() -> {
              for (int i = 0; i < 100000; i++) {
                  synchronized (monitor) {
                      // 观察锁竞争
                  }
              }
          });
          
          t1.start();
          t2.start();
          t1.join();
          t2.join();
      }
  }
  ```
- [ ] **检查点**: 运行时添加JVM参数 `-XX:+PrintGCDetails -XX:+TraceBiasedLocking` 观察锁状态
- [ ] **编程练习**: 对比单线程vs多线程场景下的执行时间差异
- [ ] **文件**: 完善`ConcurrencyLab.java`，添加锁升级实验

#### Task 1.1.10: synchronized代码块测试 (5分钟) ⏰
- [ ] **学习目标**: 对比方法锁vs对象锁
- [ ] **具体任务**: 实现synchronized(this)和synchronized(object)
- [ ] **检查点**: 理解不同锁对象的影响
- [ ] **文件**: 继续完善`SynchronizedDemo.java`

#### Task 1.1.11: volatile可见性实战验证 (5分钟) ⏰
- [ ] **学习目标**: 验证volatile的可见性和禁止重排序
- [ ] **具体任务**: 
  ```java
  public class VolatileDemo {
      // 练习1：可见性问题演示
      private static boolean flag = false; // 先不加volatile
      
      public static void visibilityTest() throws InterruptedException {
          Thread writerThread = new Thread(() -> {
              try {
                  Thread.sleep(1000);
                  flag = true; // 写线程修改flag
                  System.out.println("Writer: flag设置为true");
              } catch (InterruptedException e) {
                  e.printStackTrace();
              }
          });
          
          Thread readerThread = new Thread(() -> {
              while (!flag) {
                  // 读线程可能永远看不到flag的变化
              }
              System.out.println("Reader: 检测到flag为true");
          });
          
          readerThread.start();
          writerThread.start();
          writerThread.join();
          readerThread.join();
      }
      
      // 练习2：然后将flag改为volatile，对比结果
      private static volatile boolean volatileFlag = false;
      
      // 练习3：volatile不保证原子性演示
      private static volatile int counter = 0;
      
      public static void atomicityTest() throws InterruptedException {
          Thread[] threads = new Thread[10];
          for (int i = 0; i < 10; i++) {
              threads[i] = new Thread(() -> {
                  for (int j = 0; j < 1000; j++) {
                      counter++; // volatile不能保证原子性
                  }
              });
              threads[i].start();
          }
          
          for (Thread thread : threads) {
              thread.join();
          }
          
          System.out.println("期望值: 10000, 实际值: " + counter);
      }
  }
  ```
- [ ] **检查点**: 先运行不加volatile的版本，观察死循环；再加volatile观察正常结束
- [ ] **编程练习**: 验证volatile在i++操作中的数据丢失问题
- [ ] **文件**: `student_progress/VolatileDemo.java`

#### Task 1.1.12: volatile不保证原子性测试 (5分钟) ⏰
- [ ] **学习目标**: 验证volatile在i++时的问题
- [ ] **具体任务**: 多线程对volatile变量进行i++操作
- [ ] **检查点**: 观察到数据丢失问题
- [ ] **文件**: 完善`VolatileDemo.java`

#### Task 1.1.13: 对比测试程序 (5分钟) ⏰
- [ ] **学习目标**: 直观对比两者差异
- [ ] **具体任务**: 创建对比测试，展示适用场景
- [ ] **检查点**: 能清楚说明何时用哪个
- [ ] **文件**: `student_progress/ComparisonDemo.java`

### Phase 4: 面试题实战 (15分钟总计)

#### Task 1.1.14: 经典问题准备 (5分钟) ⏰
- [ ] **学习目标**: 准备标准面试回答
- [ ] **具体任务**: 整理"synchronized vs volatile"的标准答案
- [ ] **检查点**: 回答包含原子性、可见性、使用场景
- [ ] **文件**: `student_progress/interview_qa.md`

#### Task 1.1.15: 追问题练习 (5分钟) ⏰
- [ ] **学习目标**: 准备深度问题
- [ ] **具体任务**: 准备CAS、偏向锁废弃等追问的回答
- [ ] **检查点**: 能从设计角度思考问题
- [ ] **文件**: 完善`interview_qa.md`

#### Task 1.1.16: 知识点总结 (5分钟) ⏰
- [ ] **学习目标**: 形成完整知识图谱
- [ ] **具体任务**: 画出synchronized和volatile的知识导图
- [ ] **检查点**: 能快速回顾所有要点
- [ ] **文件**: `student_progress/summary.md`

## 🎯 学习检查点 (Checkpoint Questions)

在完成每个Phase后，我会问你这些问题来确认理解：

### Phase 1 检查问题:
1. "请用你自己的话解释什么是JMM？"
2. "给我举个happens-before的例子"
3. "synchronized和volatile的核心区别是什么？"

### Phase 2 检查问题:
1. "为什么JVM要设计锁升级机制？"
2. "什么情况下偏向锁会失效？"
3. "轻量级锁的自旋有什么风险？"

### Phase 3 检查问题:
1. "你的代码中，为什么synchronized能保证线程安全？"
2. "为什么volatile的i++会丢失数据？"
3. "什么场景下你会选择volatile而不是synchronized？"

### Phase 4 检查问题:
1. "如果面试官问'既然有了synchronized，为什么还需要volatile？'你怎么回答？"
2. "CAS和synchronized的锁升级有什么关系？"

---

# 🎯 1.2 主力集合：HashMap & ConcurrentHashMap 源码剖析

## Phase 5: HashMap基础原理 (25分钟总计)

#### Task 1.2.1: HashMap基本结构理解 (5分钟) ⏰
- [ ] **学习目标**: 理解"数组+链表+红黑树"结构
- [ ] **具体任务**: 学习HashMap内部的Node数组概念
- [ ] **检查点**: 能画出HashMap的基本结构图
- [ ] **文件**: 创建`student_progress/hashmap_notes.md`

#### Task 1.2.2: hash函数和索引计算 (5分钟) ⏰
- [ ] **学习目标**: 理解hashCode()到数组索引的转换
- [ ] **具体任务**: 学习`(n-1) & hash`的位运算技巧
- [ ] **检查点**: 能解释为什么容量必须是2的幂次方
- [ ] **文件**: 在`hashmap_notes.md`中添加hash计算部分

#### Task 1.2.3: JDK1.7 vs 1.8差异 (5分钟) ⏰
- [ ] **学习目标**: 理解链表到红黑树的优化
- [ ] **具体任务**: 学习TREEIFY_THRESHOLD=8的设计原理
- [ ] **检查点**: 能解释为什么选择8作为阈值
- [ ] **文件**: 继续完善`hashmap_notes.md`

#### Task 1.2.4: put方法流程分析 (5分钟) ⏰
- [ ] **学习目标**: 理解HashMap插入元素的完整流程
- [ ] **具体任务**: 梳理hash计算→定位→插入→扩容的流程
- [ ] **检查点**: 能完整描述put方法的执行步骤
- [ ] **文件**: 添加put流程图到notes中

#### Task 1.2.5: 扩容机制深入 (5分钟) ⏰
- [ ] **学习目标**: 理解resize()方法和rehash过程
- [ ] **具体任务**: 学习负载因子0.75的设计考虑
- [ ] **检查点**: 能解释扩容时元素重新分布的规律
- [ ] **文件**: 完善扩容机制说明

## Phase 6: ConcurrentHashMap并发控制 (25分钟总计)

#### Task 1.2.6: JDK1.7分段锁机制 (5分钟) ⏰
- [ ] **学习目标**: 理解Segment数组的设计思想
- [ ] **具体任务**: 学习分段锁如何降低锁竞争
- [ ] **检查点**: 能解释为什么并发度等于Segment数量
- [ ] **文件**: 创建`student_progress/concurrenthashmap_notes.md`

#### Task 1.2.7: JDK1.8优化策略 (5分钟) ⏰
- [ ] **学习目标**: 理解CAS+synchronized的新方案
- [ ] **具体任务**: 学习锁粒度从Segment缩小到Node头节点
- [ ] **检查点**: 能对比1.7和1.8的并发性能差异
- [ ] **文件**: 在notes中添加1.8优化部分

#### Task 1.2.8: get操作的无锁设计 (5分钟) ⏰
- [ ] **学习目标**: 理解读操作如何实现近似无锁
- [ ] **具体任务**: 学习volatile保证可见性的机制
- [ ] **检查点**: 能解释为什么get方法几乎不需要加锁
- [ ] **文件**: 添加get操作分析

#### Task 1.2.9: CAS操作原理 (5分钟) ⏰
- [ ] **学习目标**: 理解Compare-And-Swap的无锁算法
- [ ] **具体任务**: 学习CAS的ABA问题和解决方案
- [ ] **检查点**: 能解释CAS在ConcurrentHashMap中的应用
- [ ] **文件**: 添加CAS原理说明

#### Task 1.2.10: 计数器CounterCell设计 (5分钟) ⏰
- [ ] **学习目标**: 理解size()方法在并发环境下的挑战
- [ ] **具体任务**: 学习分段计数的设计思想
- [ ] **检查点**: 能解释为什么不直接用AtomicLong
- [ ] **文件**: 完善并发控制机制说明

## Phase 7: 手写HashMap实现 (30分钟总计)

#### Task 1.2.11: 手写HashMap核心实现 (5分钟) ⏰
- [ ] **学习目标**: 从零实现HashMap的核心数据结构和算法
- [ ] **具体任务**: 
  ```java
  public class MyHashMap<K, V> {
      // 练习1：设计Node节点结构
      static class Node<K, V> {
          final int hash;
          final K key;
          V value;
          Node<K, V> next;
          
          Node(int hash, K key, V value, Node<K, V> next) {
              // TODO: 学生实现构造方法
          }
      }
      
      private Node<K, V>[] table;
      private int size = 0;
      private static final int DEFAULT_CAPACITY = 16;
      private static final float LOAD_FACTOR = 0.75f;
      
      // 练习2：实现高效的hash函数
      private int hash(K key) {
          if (key == null) return 0;
          int h = key.hashCode();
          // TODO: 为什么要进行扰动函数？ h ^ (h >>> 16)
          return h ^ (h >>> 16);
      }
      
      // 练习3：实现索引计算
      private int indexFor(int hash, int length) {
          // TODO: 为什么用 (length-1) & hash 而不是 hash % length？
          // 学生需要理解位运算的性能优势
          return hash & (length - 1);
      }
      
      // 练习4：实现核心put方法
      public V put(K key, V value) {
          // TODO: 实现完整的put逻辑
          // 1. 计算hash值
          // 2. 找到对应bucket
          // 3. 处理hash冲突（链表插入）
          // 4. 检查是否需要扩容
          return null;
      }
  }
  ```
- [ ] **检查点**: 理解为什么容量必须是2的幂次方，扰动函数的作用
- [ ] **编程练习**: 测试不同hash函数的分布均匀性
- [ ] **文件**: `student_progress/MyHashMap.java`

#### Task 1.2.12: 实现hash函数 (5分钟) ⏰
- [ ] **学习目标**: 实现高效的hash计算方法
- [ ] **具体任务**: 实现hash()和indexFor()方法
- [ ] **检查点**: hash分布均匀，索引计算正确
- [ ] **文件**: 完善`MyHashMap.java`

#### Task 1.2.13: 实现put方法 (5分钟) ⏰
- [ ] **学习目标**: 实现元素插入逻辑
- [ ] **具体任务**: 处理hash冲突和链表插入
- [ ] **检查点**: 能正确插入键值对
- [ ] **文件**: 继续完善`MyHashMap.java`

#### Task 1.2.14: 实现get方法 (5分钟) ⏰
- [ ] **学习目标**: 实现元素查找逻辑
- [ ] **具体任务**: 遍历链表查找目标key
- [ ] **检查点**: 能正确获取对应的value
- [ ] **文件**: 继续完善`MyHashMap.java`

#### Task 1.2.15: 实现扩容机制 (5分钟) ⏰
- [ ] **学习目标**: 实现动态扩容和rehash
- [ ] **具体任务**: 当负载因子超过阈值时自动扩容
- [ ] **检查点**: 扩容后数据完整性保持
- [ ] **文件**: 完善`MyHashMap.java`

#### Task 1.2.16: 测试和优化 (5分钟) ⏰
- [ ] **学习目标**: 验证实现的正确性
- [ ] **具体任务**: 编写测试用例，对比性能
- [ ] **检查点**: 与JDK HashMap行为一致
- [ ] **文件**: 创建`student_progress/HashMapTest.java`

## Phase 8: 并发安全对比测试 (20分钟总计)

#### Task 1.2.17: 创建并发测试框架 (5分钟) ⏰
- [ ] **学习目标**: 搭建多线程测试环境
- [ ] **具体任务**: 创建多线程同时操作Map的测试
- [ ] **检查点**: 能模拟高并发场景
- [ ] **文件**: `student_progress/ConcurrencyTest.java`

#### Task 1.2.18: HashMap并发安全性实战验证 (5分钟) ⏰
- [ ] **学习目标**: 观察HashMap在并发环境下的问题和ConcurrentHashMap的解决方案
- [ ] **具体任务**: 
  ```java
  public class ConcurrentMapTest {
      private static final int THREAD_COUNT = 10;
      private static final int OPERATIONS_PER_THREAD = 1000;
      
      // 练习1：HashMap并发问题重现
      public static void testHashMapConcurrency() throws InterruptedException {
          Map<Integer, Integer> map = new HashMap<>();
          CountDownLatch latch = new CountDownLatch(THREAD_COUNT);
          
          for (int i = 0; i < THREAD_COUNT; i++) {
              final int threadId = i;
              new Thread(() -> {
                  try {
                      for (int j = 0; j < OPERATIONS_PER_THREAD; j++) {
                          map.put(threadId * OPERATIONS_PER_THREAD + j, j);
                      }
                  } finally {
                      latch.countDown();
                  }
              }).start();
          }
          
          latch.await();
          System.out.println("HashMap 期望大小: " + (THREAD_COUNT * OPERATIONS_PER_THREAD));
          System.out.println("HashMap 实际大小: " + map.size());
          // 观察数据丢失问题
      }
      
      // 练习2：ConcurrentHashMap安全性验证
      public static void testConcurrentHashMapSafety() throws InterruptedException {
          Map<Integer, Integer> map = new ConcurrentHashMap<>();
          CountDownLatch latch = new CountDownLatch(THREAD_COUNT);
          
          long startTime = System.currentTimeMillis();
          
          for (int i = 0; i < THREAD_COUNT; i++) {
              final int threadId = i;
              new Thread(() -> {
                  try {
                      for (int j = 0; j < OPERATIONS_PER_THREAD; j++) {
                          map.put(threadId * OPERATIONS_PER_THREAD + j, j);
                          // 模拟读操作
                          map.get(threadId * OPERATIONS_PER_THREAD + j);
                      }
                  } finally {
                      latch.countDown();
                  }
              }).start();
          }
          
          latch.await();
          long endTime = System.currentTimeMillis();
          
          System.out.println("ConcurrentHashMap 期望大小: " + (THREAD_COUNT * OPERATIONS_PER_THREAD));
          System.out.println("ConcurrentHashMap 实际大小: " + map.size());
          System.out.println("执行时间: " + (endTime - startTime) + "ms");
      }
      
      // 练习3：性能对比测试
      public static void performanceComparison() throws InterruptedException {
          System.out.println("=== HashMap并发测试 ===");
          testHashMapConcurrency();
          
          System.out.println("\n=== ConcurrentHashMap并发测试 ===");
          testConcurrentHashMapSafety();
      }
  }
  ```
- [ ] **检查点**: 观察HashMap的数据丢失，ConcurrentHashMap的数据完整性
- [ ] **编程练习**: 尝试重现HashMap的死循环问题（需要特定JDK版本和条件）
- [ ] **文件**: 完善`student_progress/ConcurrentMapTest.java`

#### Task 1.2.19: ConcurrentHashMap安全性验证 (5分钟) ⏰
- [ ] **学习目标**: 验证ConcurrentHashMap的线程安全性
- [ ] **具体任务**: 相同条件下测试ConcurrentHashMap
- [ ] **检查点**: 观察到数据一致性和性能优势
- [ ] **文件**: 继续完善测试代码

#### Task 1.2.20: 性能基准测试 (5分钟) ⏰
- [ ] **学习目标**: 对比不同Map在并发场景下的性能
- [ ] **具体任务**: 测试读写操作的吞吐量
- [ ] **检查点**: 理解不同实现的性能特点
- [ ] **文件**: 添加性能测试结果

## Phase 9: 面试题实战训练 (15分钟总计)

#### Task 1.2.21: 经典问题准备 (5分钟) ⏰
- [ ] **学习目标**: 准备HashMap核心面试问题
- [ ] **具体任务**: 整理容量2的幂次方、扩容机制等问题
- [ ] **检查点**: 能从底层原理解释设计决策
- [ ] **文件**: 更新`student_progress/interview_qa.md`

#### Task 1.2.22: 白板编程练习 (5分钟) ⏰
- [ ] **学习目标**: 练习手写HashMap关键方法
- [ ] **具体任务**: 在纸上写出put方法的伪代码
- [ ] **检查点**: 逻辑清晰，考虑边界情况
- [ ] **文件**: 拍照上传手写代码到progress文件夹

#### Task 1.2.23: 追问题深度回答 (5分钟) ⏰
- [ ] **学习目标**: 准备深度技术问题
- [ ] **具体任务**: 准备红黑树、一致性hash等进阶问题
- [ ] **检查点**: 能扩展到分布式系统设计
- [ ] **文件**: 完善面试问答集

---

# 🎯 1.3 现代范式：Kotlin 协程

## Phase 10: 协程基础理念 (25分钟总计)

#### Task 1.3.1: 协程vs线程对比 (5分钟) ⏰
- [ ] **学习目标**: 理解协程的轻量级特性
- [ ] **具体任务**: 学习用户态vs内核态的区别
- [ ] **检查点**: 能解释为什么协程比线程轻量
- [ ] **文件**: 创建`student_progress/coroutine_notes.md`

#### Task 1.3.2: 挂起vs阻塞概念 (5分钟) ⏰
- [ ] **学习目标**: 理解suspend的核心思想
- [ ] **具体任务**: 学习协程如何让出线程执行权
- [ ] **检查点**: 能区分suspend和block的本质差异
- [ ] **文件**: 在notes中添加挂起机制说明

#### Task 1.3.3: 结构化并发原理 (5分钟) ⏰
- [ ] **学习目标**: 理解CoroutineScope的生命周期管理
- [ ] **具体任务**: 学习父子协程的取消传播机制
- [ ] **检查点**: 能解释如何避免协程泄漏
- [ ] **文件**: 添加结构化并发部分

#### Task 1.3.4: CoroutineContext组成 (5分钟) ⏰
- [ ] **学习目标**: 理解协程上下文的构成要素
- [ ] **具体任务**: 学习Job、Dispatcher、CoroutineName等组件
- [ ] **检查点**: 能解释上下文继承和覆盖规则
- [ ] **文件**: 添加Context说明

#### Task 1.3.5: Dispatchers调度器详解 (5分钟) ⏰
- [ ] **学习目标**: 理解不同调度器的适用场景
- [ ] **具体任务**: 学习Main、IO、Default、Unconfined的区别
- [ ] **检查点**: 能为不同任务选择合适的调度器
- [ ] **文件**: 完善调度器说明

## Phase 11: 协程核心API实践 (30分钟总计)

#### Task 1.3.6: 协程基础API实战练习 (5分钟) ⏰
- [ ] **学习目标**: 掌握launch、async、runBlocking的使用场景和区别
- [ ] **具体任务**: 
  ```kotlin
  class CoroutineBasicsLab {
      
      // 练习1：对比阻塞vs非阻塞调用
      fun compareBlockingVsSuspend() {
          // TODO: 先实现传统回调方式的网络请求
          fun fetchUserCallback(userId: String, callback: (User?) -> Unit) {
              Thread {
                  Thread.sleep(1000) // 模拟网络延迟
                  callback(User(userId, "张三"))
              }.start()
          }
          
          // TODO: 改写为suspend函数，体验简洁性
          suspend fun fetchUserSuspend(userId: String): User? {
              return withContext(Dispatchers.IO) {
                  delay(1000) // 非阻塞延迟
                  User(userId, "张三")
              }
          }
          
          // 对比两种方式的代码复杂度
      }
      
      // 练习2：launch vs async的选择决策
      fun practiceJobTypes() = runBlocking {
          println("=== Launch vs Async 对比 ===")
          
          // 场景1：发射后不管 - 使用launch
          val job = launch {
              repeat(3) {
                  delay(500)
                  println("后台任务执行中... $it")
              }
          }
          
          // 场景2：需要返回值 - 使用async
          val deferred1 = async {
              delay(1000)
              "数据源1的结果"
          }
          
          val deferred2 = async {
              delay(1500)
              "数据源2的结果"
          }
          
          // 等待并获取结果
          val result1 = deferred1.await()
          val result2 = deferred2.await()
          
          println("合并结果: $result1 + $result2")
          job.join() // 等待后台任务完成
      }
      
      // 练习3：并发vs串行执行对比
      suspend fun concurrentVsSequential() {
          // 串行执行
          val sequentialTime = measureTimeMillis {
              val result1 = fetchData("数据1") // 1秒
              val result2 = fetchData("数据2") // 1秒  
              println("串行结果: $result1, $result2")
          }
          
          // 并发执行
          val concurrentTime = measureTimeMillis {
              val deferred1 = async { fetchData("数据1") }
              val deferred2 = async { fetchData("数据2") }
              val result1 = deferred1.await()
              val result2 = deferred2.await()
              println("并发结果: $result1, $result2")
          }
          
          println("串行耗时: ${sequentialTime}ms")
          println("并发耗时: ${concurrentTime}ms")
      }
      
      private suspend fun fetchData(name: String): String {
          delay(1000)
          return "${name}的结果"
      }
  }
  
  data class User(val id: String, val name: String)
  ```
- [ ] **检查点**: 理解launch适合"发射后不管"，async适合"需要返回值"
- [ ] **编程练习**: 对比串行vs并发执行的性能差异
- [ ] **文件**: `student_progress/CoroutineBasicsLab.kt`

#### Task 1.3.7: runBlocking使用场景 (5分钟) ⏰
- [ ] **学习目标**: 理解阻塞式协程的适用性
- [ ] **具体任务**: 在main函数和测试中使用runBlocking
- [ ] **检查点**: 理解为什么生产代码要避免runBlocking
- [ ] **文件**: 完善`CoroutineBasics.kt`

#### Task 1.3.8: 线程切换实战演练 (5分钟) ⏰
- [ ] **学习目标**: 掌握withContext进行线程切换的实际应用
- [ ] **具体任务**: 
  ```kotlin
  class ThreadSwitchingPractice {
      
      // 练习：模拟真实的Android开发场景
      suspend fun realWorldNetworkCall() {
          println("开始执行 - 线程: ${Thread.currentThread().name}")
          
          // 第一步：网络线程获取数据
          val userData = withContext(Dispatchers.IO) {
              println("网络请求 - 线程: ${Thread.currentThread().name}")
              delay(1000) // 模拟网络延迟
              "User Data from Network"
          }
          
          // 第二步：计算线程处理数据
          val processedData = withContext(Dispatchers.Default) {
              println("数据处理 - 线程: ${Thread.currentThread().name}")
              // 模拟CPU密集计算
              Thread.sleep(500) // 阻塞当前线程
              userData.uppercase().reversed()
          }
          
          // 第三步：主线程更新UI
          withContext(Dispatchers.Main) {
              println("UI更新 - 线程: ${Thread.currentThread().name}")
              // 模拟TextView更新
              println("UI Updated: $processedData")
          }
          
          println("完成执行 - 线程: ${Thread.currentThread().name}")
      }
      
      // 高级练习：错误处理和取消传播
      suspend fun advancedErrorHandling() {
          val job = CoroutineScope(Dispatchers.IO).launch {
              try {
                  // 模拟可取消的长时间操作
                  repeat(10) { i ->
                      if (!isActive) { // 检查协程是否被取消
                          println("协程被取消，停止执行")
                          return@launch
                      }
                      
                      println("执行步骤 $i")
                      delay(500)
                  }
              } catch (e: CancellationException) {
                  println("捕获取消异常: ${e.message}")
                  // 清理资源
                  withContext(NonCancellable) {
                      println("执行清理工作")
                      delay(200) // 确保清理完成
                  }
                  throw e // 重新抛出取消异常
              } catch (e: Exception) {
                  println("捕获其他异常: ${e.message}")
              }
          }
          
          delay(2000) // 让协程运行2秒
          job.cancel("用户取消操作") // 取消协程
          job.join() // 等待协程完成清理
      }
      
      // 练习：协程作用域管理
      class ViewModelSimulation {
          private val viewModelScope = CoroutineScope(
              SupervisorJob() + Dispatchers.Main
          )
          
          fun loadUserData(userId: String) {
              viewModelScope.launch {
                  try {
                      val userData = withContext(Dispatchers.IO) {
                          // 模拟网络请求
                          delay(1000)
                          if (userId == "error") {
                              throw RuntimeException("网络错误")
                          }
                          "用户数据: $userId"
                      }
                      
                      // 更新UI（主线程）
                      updateUI(userData)
                  } catch (e: Exception) {
                      handleError(e)
                  }
              }
          }
          
          private fun updateUI(data: String) {
              println("UI更新: $data - 线程: ${Thread.currentThread().name}")
          }
          
          private fun handleError(error: Exception) {
              println("错误处理: ${error.message}")
          }
          
          fun onDestroy() {
              viewModelScope.cancel() // 清理所有协程
          }
      }
  }
  ```
- [ ] **检查点**: 观察每步操作在不同线程上执行，理解线程切换的开销
- [ ] **编程练习**: 实现一个完整的网络请求→数据处理→UI更新的流程
- [ ] **文件**: 创建`student_progress/ThreadSwitchingPractice.kt`

#### Task 1.3.9: 协程取消机制 (5分钟) ⏰
- [ ] **学习目标**: 理解协程的取消和异常处理
- [ ] **具体任务**: 实现可取消的长时间运行任务
- [ ] **检查点**: 能正确响应取消信号
- [ ] **文件**: 创建`student_progress/CancellationDemo.kt`

#### Task 1.3.10: suspend函数编写 (5分钟) ⏰
- [ ] **学习目标**: 创建自定义的挂起函数
- [ ] **具体任务**: 包装回调式API为suspend函数
- [ ] **检查点**: 理解suspend的传染性
- [ ] **文件**: 创建`student_progress/SuspendFunctions.kt`

#### Task 1.3.11: 异常处理策略 (5分钟) ⏰
- [ ] **学习目标**: 理解协程中的异常传播
- [ ] **具体任务**: 使用try-catch和CoroutineExceptionHandler
- [ ] **检查点**: 能处理结构化并发中的异常
- [ ] **文件**: 创建`student_progress/ExceptionHandling.kt`

## Phase 12: 实际应用场景练习 (25分钟总计)

#### Task 1.3.12: 网络请求对比实现 (5分钟) ⏰
- [ ] **学习目标**: 对比回调vs协程的代码可读性
- [ ] **具体任务**: 同一个网络请求用两种方式实现
- [ ] **检查点**: 体验协程消除回调地狱的优势
- [ ] **文件**: `student_progress/NetworkComparison.kt`

#### Task 1.3.13: 并发网络请求 (5分钟) ⏰
- [ ] **学习目标**: 实现多个请求的并发执行
- [ ] **具体任务**: 使用async+awaitAll并发获取数据
- [ ] **检查点**: 理解并发vs并行的区别
- [ ] **文件**: 完善`NetworkComparison.kt`

#### Task 1.3.14: Flow响应式编程实践 (5分钟) ⏰
- [ ] **学习目标**: 掌握Flow的响应式数据流编程
- [ ] **具体任务**: 
  ```kotlin
  class FlowPracticalExercises {
      
      // 练习1：构建实时数据流
      fun createRealtimeDataStream(): Flow<String> = flow {
          println("Flow开始发射数据")
          repeat(5) { i ->
              delay(500) // 模拟数据到达间隔
              emit("实时数据 $i 来自线程: ${Thread.currentThread().name}")
          }
          println("Flow发射完成")
      }
      
      // 练习2：流的变换和处理
      suspend fun practiceFlowTransformations() {
          println("=== Flow变换操作演示 ===")
          
          createRealtimeDataStream()
              .map { data ->
                  println("转换: $data")
                  data.uppercase() // 转换为大写
              }
              .filter { data ->
                  val shouldKeep = data.contains("2") || data.contains("4")
                  println("过滤: $data -> ${if (shouldKeep) "保留" else "丢弃"}")
                  shouldKeep
              }
              .collect { processedData ->
                  println("消费: $processedData")
              }
      }
      
      // 练习3：冷流vs热流对比
      suspend fun coldVsHotFlowDemo() {
          println("=== 冷流演示 ===")
          val coldFlow = flow {
              println("冷流：开始执行")
              repeat(3) {
                  emit(it)
                  delay(300)
              }
          }
          
          // 每次collect都会重新执行flow代码块
          println("第一个收集者:")
          coldFlow.collect { println("收集到: $it") }
          
          println("第二个收集者:")
          coldFlow.collect { println("收集到: $it") }
          
          println("\n=== 热流演示 ===")
          val hotFlow = MutableSharedFlow<Int>()
          
          // 启动发射者
          launch {
              repeat(3) {
                  hotFlow.emit(it)
                  delay(300)
              }
          }
          
          delay(100) // 稍微延迟
          println("第一个收集者:")
          launch {
              hotFlow.collect { println("收集者1: $it") }
          }
          
          delay(500) // 延迟更久
          println("第二个收集者:")
          launch {
              hotFlow.collect { println("收集者2: $it") }
          }
          
          delay(1000) // 等待完成
      }
      
      // 练习4：StateFlow状态管理
      class ViewModelSimulation {
          private val _uiState = MutableStateFlow("初始状态")
          val uiState: StateFlow<String> = _uiState.asStateFlow()
          
          private val _userInfo = MutableStateFlow<User?>(null)
          val userInfo: StateFlow<User?> = _userInfo.asStateFlow()
          
          fun updateState(newState: String) {
              println("状态更新: ${_uiState.value} -> $newState")
              _uiState.value = newState
          }
          
          suspend fun loadUser(userId: String) {
              _uiState.value = "加载中..."
              
              try {
                  delay(1000) // 模拟网络请求
                  val user = User(userId, "用户$userId")
                  _userInfo.value = user
                  _uiState.value = "加载完成"
              } catch (e: Exception) {
                  _uiState.value = "加载失败: ${e.message}"
              }
          }
          
          // 组合多个状态流
          val combinedState: Flow<String> = combine(uiState, userInfo) { state, user ->
              when {
                  user == null -> state
                  else -> "$state - 当前用户: ${user.name}"
              }
          }
      }
      
      // 练习5：Flow异常处理
      suspend fun flowErrorHandling() {
          flow {
              repeat(5) { i ->
                  if (i == 3) {
                      throw RuntimeException("模拟网络错误")
                  }
                  emit(i)
              }
          }
          .catch { e ->
              println("捕获异常: ${e.message}")
              emit(-1) // 发射默认值
          }
          .collect { value ->
              println("接收到值: $value")
          }
      }
  }
  ```
- [ ] **检查点**: 理解冷流vs热流的区别，StateFlow的状态管理特性
- [ ] **编程练习**: 实现一个包含状态管理和错误处理的完整数据流
- [ ] **文件**: `student_progress/FlowPracticalExercises.kt`

#### Task 1.3.15: StateFlow和SharedFlow (5分钟) ⏰
- [ ] **学习目标**: 理解状态流和事件流的区别
- [ ] **具体任务**: 实现MVVM模式中的状态管理
- [ ] **检查点**: 理解热流vs冷流的概念
- [ ] **文件**: 创建`student_progress/StateManagement.kt`

#### Task 1.3.16: 协程测试编写 (5分钟) ⏰
- [ ] **学习目标**: 学会测试协程代码
- [ ] **具体任务**: 使用TestCoroutineDispatcher编写单元测试
- [ ] **检查点**: 能控制测试中的时间流逝
- [ ] **文件**: `student_progress/CoroutineTest.kt`

## Phase 13: 底层原理探索 (20分钟总计)

#### Task 1.3.17: suspend关键字原理 (5分钟) ⏰
- [ ] **学习目标**: 理解编译器如何处理suspend
- [ ] **具体任务**: 查看反编译后的状态机代码
- [ ] **检查点**: 理解CPS(Continuation Passing Style)变换
- [ ] **文件**: 在notes中添加编译原理说明

#### Task 1.3.18: Continuation接口分析 (5分钟) ⏰
- [ ] **学习目标**: 理解协程的底层实现机制
- [ ] **具体任务**: 分析Continuation的resumeWith方法
- [ ] **检查点**: 理解协程如何实现挂起和恢复
- [ ] **文件**: 添加Continuation原理说明

#### Task 1.3.19: 协程调度原理 (5分钟) ⏰
- [ ] **学习目标**: 理解协程如何被调度执行
- [ ] **具体任务**: 分析EventLoop和线程池的配合
- [ ] **检查点**: 理解协程并不绑定线程的原理
- [ ] **文件**: 添加调度机制说明

#### Task 1.3.20: 性能特点分析 (5分钟) ⏰
- [ ] **学习目标**: 理解协程的性能优势和局限性
- [ ] **具体任务**: 对比协程vs线程的创建销毁开销
- [ ] **检查点**: 理解协程适用的场景边界
- [ ] **文件**: 完善性能分析

## Phase 14: 协程面试准备 (15分钟总计)

#### Task 1.3.21: 核心概念问答 (5分钟) ⏰
- [ ] **学习目标**: 准备协程基础概念问题
- [ ] **具体任务**: 整理协程vs线程、挂起机制等答案
- [ ] **检查点**: 能清晰解释协程的核心优势
- [ ] **文件**: 更新`interview_qa.md`

#### Task 1.3.22: 实际应用问题 (5分钟) ⏰
- [ ] **学习目标**: 准备协程在Android中的应用
- [ ] **具体任务**: 整理viewModelScope、lifecycleScope等问题
- [ ] **检查点**: 能结合Android生命周期解释协程使用
- [ ] **文件**: 添加Android相关问答

#### Task 1.3.23: 深度技术问题 (5分钟) ⏰
- [ ] **学习目标**: 准备协程底层原理问题
- [ ] **具体任务**: 整理状态机、CPS变换等深度问题
- [ ] **检查点**: 能从编译器角度解释协程实现
- [ ] **文件**: 完善深度问答

## 🎯 第一章学习检查点汇总

### 1.1 并发原语检查问题:
1. "JMM的核心作用是什么？工作内存和主内存如何交互？"
2. "synchronized的锁升级过程体现了什么设计思想？"
3. "volatile不能保证原子性，那什么时候用它最合适？"
4. "能手写一个线程安全的单例模式吗？用synchronized和volatile两种方式。"

### 1.2 集合框架检查问题:
1. "为什么HashMap的容量必须是2的幂次方？能从位运算角度解释吗？"
2. "ConcurrentHashMap 1.8的get操作几乎无锁，它如何保证读到最新数据？"
3. "让你设计一个线程安全的缓存，你会怎么设计？"
4. "能在白板上画出HashMap的put流程吗？包括扩容场景。"

### 1.3 协程机制检查问题:
1. "协程的suspend关键字底层是如何实现的？"
2. "launch和async的使用场景有什么区别？"
3. "结构化并发如何解决传统异步编程的问题？"
4. "能用协程重写一个回调地狱的网络请求吗？"

## 🏆 第一章总进度跟踪
- **1.1 并发原语**: 0/16 tasks (预计完成时间: 1.5小时)
- **1.2 集合框架**: 0/13 tasks (预计完成时间: 1.2小时)  
- **1.3 协程机制**: 0/14 tasks (预计完成时间: 1.3小时)
- **第一章总计**: 0/43 tasks (预计完成时间: 4小时)

---

# 📱 第二章：支柱篇 - 解构安卓框架内核

# 🎯 2.1 运行时引擎：ART 的混合编译模式

## Phase 15: ART vs Dalvik基础 (25分钟总计)

#### Task 2.1.1: Dalvik虚拟机原理 (5分钟) ⏰
- [ ] **学习目标**: 理解Dalvik的JIT编译机制
- [ ] **具体任务**: 学习DEX字节码和JIT即时编译概念
- [ ] **检查点**: 能解释为什么Dalvik启动慢但运行时优化
- [ ] **文件**: 创建`student_progress/art_dalvik_notes.md`

#### Task 2.1.2: ART的AOT编译 (5分钟) ⏰
- [ ] **学习目标**: 理解预先编译的优势和代价
- [ ] **具体任务**: 学习dex2oat工具和OAT文件格式
- [ ] **检查点**: 能对比AOT vs JIT的性能权衡
- [ ] **文件**: 在notes中添加AOT编译说明

#### Task 2.1.3: 混合编译策略演进 (5分钟) ⏰
- [ ] **学习目标**: 理解Android 7.0后的智能编译
- [ ] **具体任务**: 学习Profile-guided compilation概念
- [ ] **检查点**: 能解释为什么需要混合模式
- [ ] **文件**: 添加混合编译策略分析

#### Task 2.1.4: 云配置文件机制 (5分钟) ⏰
- [ ] **学习目标**: 理解大数据优化单设备的思想
- [ ] **具体任务**: 学习Play Store的云端配置文件
- [ ] **检查点**: 能解释云端赋能端侧的工程价值
- [ ] **文件**: 添加云配置文件说明

#### Task 2.1.5: 编译时机和触发条件 (5分钟) ⏰
- [ ] **学习目标**: 理解何时进行AOT编译
- [ ] **具体任务**: 学习设备空闲时的后台编译守护进程
- [ ] **检查点**: 能说明编译优化的完整生命周期
- [ ] **文件**: 完善编译时机说明

## Phase 16: 性能分析实践 (20分钟总计)

#### Task 2.1.6: APK结构分析 (5分钟) ⏰
- [ ] **学习目标**: 分析APK中的DEX和资源文件
- [ ] **具体任务**: 使用aapt工具查看APK内容结构
- [ ] **检查点**: 能识别DEX文件和编译后的资源
- [ ] **文件**: `student_progress/ApkAnalysis.md`

#### Task 2.1.7: OAT文件探索 (5分钟) ⏰
- [ ] **学习目标**: 观察AOT编译的产物
- [ ] **具体任务**: 在Android设备上查找和分析OAT文件
- [ ] **检查点**: 理解OAT文件与DEX文件的关系
- [ ] **文件**: 添加OAT文件分析结果

#### Task 2.1.8: 启动性能对比 (5分钟) ⏰
- [ ] **学习目标**: 验证编译模式对启动速度的影响
- [ ] **具体任务**: 使用adb命令测量应用启动时间
- [ ] **检查点**: 观察到首次启动vs后续启动的差异
- [ ] **文件**: 记录性能测试数据

#### Task 2.1.9: Perfetto性能分析 (5分钟) ⏰
- [ ] **学习目标**: 使用系统级性能分析工具
- [ ] **具体任务**: 抓取启动过程的性能trace
- [ ] **检查点**: 能在trace中识别编译相关活动
- [ ] **文件**: 保存Perfetto分析结果

## Phase 17: ART面试准备 (15分钟总计)

#### Task 2.1.10: 经典问题准备 (5分钟) ⏰
- [ ] **学习目标**: 准备ART相关面试问题
- [ ] **具体任务**: 整理AOT vs JIT、混合编译等核心问答
- [ ] **检查点**: 能从性能优化角度解释设计决策
- [ ] **文件**: 更新`student_progress/interview_qa.md`

#### Task 2.1.11: 深度原理问答 (5分钟) ⏰
- [ ] **学习目标**: 准备底层实现问题
- [ ] **具体任务**: 整理dex2oat、Profile引导编译等深度问题
- [ ] **检查点**: 能结合实际开发经验回答
- [ ] **文件**: 添加深度技术问答

#### Task 2.1.12: 开发实践建议 (5分钟) ⏰
- [ ] **学习目标**: 总结对开发者的启示
- [ ] **具体任务**: 整理如何配合ART优化的开发建议
- [ ] **检查点**: 能提出具体的优化策略
- [ ] **文件**: 完善开发实践指南

---

# 🎯 2.2 四大组件深度：Activity 启动模式

## Phase 18: 任务栈基础理念 (20分钟总计)

#### Task 2.2.1: Task和Back Stack概念 (5分钟) ⏰
- [ ] **学习目标**: 理解Android的任务栈机制
- [ ] **具体任务**: 学习Task是用户操作的Activity集合
- [ ] **检查点**: 能解释LIFO栈的用户体验意义
- [ ] **文件**: 创建`student_progress/activity_launchmode_notes.md`

#### Task 2.2.2: taskAffinity属性 (5分钟) ⏰
- [ ] **学习目标**: 理解Activity的任务亲和性
- [ ] **具体任务**: 学习如何控制Activity进入哪个任务栈
- [ ] **检查点**: 能解释affinity与包名的关系
- [ ] **文件**: 添加taskAffinity说明

#### Task 2.2.3: Intent标志位影响 (5分钟) ⏰
- [ ] **学习目标**: 理解Intent Flag对启动行为的影响
- [ ] **具体任务**: 学习FLAG_ACTIVITY_NEW_TASK等关键标志
- [ ] **检查点**: 能预测不同Flag组合的行为
- [ ] **文件**: 添加Intent标志位分析

#### Task 2.2.4: 系统任务管理 (5分钟) ⏰
- [ ] **学习目标**: 理解系统如何管理多个任务
- [ ] **具体任务**: 学习任务切换和后台任务管理
- [ ] **检查点**: 能解释最近任务列表的工作原理
- [ ] **文件**: 完善任务管理机制说明

## Phase 19: 四种启动模式实战 (30分钟总计)

#### Task 2.2.5: standard模式演示 (5分钟) ⏰
- [ ] **学习目标**: 验证默认启动模式的行为
- [ ] **具体任务**: 创建Demo应用测试standard模式
- [ ] **检查点**: 观察到每次启动都创建新实例
- [ ] **文件**: `student_progress/LaunchModeDemo/`项目

#### Task 2.2.6: singleTop模式测试 (5分钟) ⏰
- [ ] **学习目标**: 验证栈顶复用机制
- [ ] **具体任务**: 测试栈顶和非栈顶的不同行为
- [ ] **检查点**: 观察onNewIntent()的调用时机
- [ ] **文件**: 扩展Demo应用

#### Task 2.2.7: singleTask模式验证 (5分钟) ⏰
- [ ] **学习目标**: 验证栈内唯一和clear top行为
- [ ] **具体任务**: 测试singleTask的任务栈清理机制
- [ ] **检查点**: 观察上层Activity被清除的过程
- [ ] **文件**: 继续扩展Demo

#### Task 2.2.8: singleInstance模式实现 (5分钟) ⏰
- [ ] **学习目标**: 验证独占任务栈机制
- [ ] **具体任务**: 测试singleInstance的隔离效果
- [ ] **检查点**: 观察到独立任务栈的创建
- [ ] **文件**: 完善Demo应用

#### Task 2.2.9: 复杂跳转场景设计 (5分钟) ⏰
- [ ] **学习目标**: 设计复杂的页面跳转流程
- [ ] **具体任务**: 模拟真实应用的多页面跳转
- [ ] **检查点**: 能预测和验证跳转结果
- [ ] **文件**: 创建复杂场景测试

#### Task 2.2.10: onNewIntent()处理 (5分钟) ⏰
- [ ] **学习目标**: 正确处理Intent更新
- [ ] **具体任务**: 实现onNewIntent中的参数更新
- [ ] **检查点**: 避免getIntent()返回旧数据的陷阱
- [ ] **文件**: 完善Intent处理逻辑

## Phase 20: 用户体验考量 (15分钟总计)

#### Task 2.2.11: 导航体验分析 (5分钟) ⏰
- [ ] **学习目标**: 从UX角度分析启动模式选择
- [ ] **具体任务**: 分析不同模式对用户导航的影响
- [ ] **检查点**: 能说明何时使用何种模式
- [ ] **文件**: 添加UX分析到notes

#### Task 2.2.12: 实际应用场景设计 (5分钟) ⏰
- [ ] **学习目标**: 为真实功能选择合适的启动模式
- [ ] **具体任务**: 设计主页、通知页、播放器页的启动模式
- [ ] **检查点**: 能从产品角度解释选择理由
- [ ] **文件**: 创建场景设计文档

#### Task 2.2.13: 启动模式面试准备 (5分钟) ⏰
- [ ] **学习目标**: 准备启动模式相关面试问题
- [ ] **具体任务**: 整理经典问题和最佳实践
- [ ] **检查点**: 能结合用户体验解释技术选择
- [ ] **文件**: 更新面试问答集

---

# 🎯 2.3 UI 渲染管线：Measure, Layout, Draw

## Phase 21: 渲染流程基础 (25分钟总计)

#### Task 2.3.1: ViewRootImpl入口分析 (5分钟) ⏰
- [ ] **学习目标**: 理解UI渲染的总控制器
- [ ] **具体任务**: 学习performTraversals方法的作用
- [ ] **检查点**: 能解释从Window到View树的渲染链路
- [ ] **文件**: 创建`student_progress/ui_rendering_notes.md`

#### Task 2.3.2: Measure阶段详解 (5分钟) ⏰
- [ ] **学习目标**: 理解测量阶段的递归过程
- [ ] **具体任务**: 学习measure()和onMeasure()的关系
- [ ] **检查点**: 能说明自顶向下的测量顺序
- [ ] **文件**: 添加Measure阶段分析

#### Task 2.3.3: MeasureSpec机制 (5分钟) ⏰
- [ ] **学习目标**: 理解父子View间的约束传递
- [ ] **具体任务**: 学习EXACTLY、AT_MOST、UNSPECIFIED三种模式
- [ ] **检查点**: 能解释32位整数的高2位和低30位设计
- [ ] **文件**: 添加MeasureSpec详解

#### Task 2.3.4: Layout阶段原理 (5分钟) ⏰
- [ ] **学习目标**: 理解布局定位的计算过程
- [ ] **具体任务**: 学习layout()方法确定View位置坐标
- [ ] **检查点**: 能说明left、top、right、bottom的含义
- [ ] **文件**: 添加Layout阶段说明

#### Task 2.3.5: Draw阶段流程 (5分钟) ⏰
- [ ] **学习目标**: 理解绘制的层次顺序
- [ ] **具体任务**: 学习Canvas绘制和分发机制
- [ ] **检查点**: 能说明draw()的六个步骤
- [ ] **文件**: 完善Draw阶段分析

## Phase 22: 自定义View实战 (25分钟总计)

#### Task 2.3.6: 自定义View框架搭建 (5分钟) ⏰
- [ ] **学习目标**: 创建自定义View的基础结构
- [ ] **具体任务**: 继承View类，重写构造方法
- [ ] **检查点**: 基础框架能在布局中显示
- [ ] **文件**: `student_progress/CustomViewDemo.java`

#### Task 2.3.7: onMeasure实现 (5分钟) ⏰
- [ ] **学习目标**: 实现正确的测量逻辑
- [ ] **具体任务**: 处理wrap_content和match_parent
- [ ] **检查点**: wrap_content不再等同于match_parent
- [ ] **文件**: 完善CustomViewDemo

#### Task 2.3.8: onDraw绘制实现 (5分钟) ⏰
- [ ] **学习目标**: 使用Canvas绘制自定义内容
- [ ] **具体任务**: 绘制几何图形和文字
- [ ] **检查点**: 能看到自定义的绘制效果
- [ ] **文件**: 继续完善绘制逻辑

#### Task 2.3.9: 性能优化验证 (5分钟) ⏰
- [ ] **学习目标**: 分析渲染性能问题
- [ ] **具体任务**: 使用Layout Inspector观察View层级
- [ ] **检查点**: 能识别过度绘制和层级过深问题
- [ ] **文件**: 记录性能分析结果

#### Task 2.3.10: ConstraintLayout对比 (5分钟) ⏰
- [ ] **学习目标**: 理解扁平化布局的优势
- [ ] **具体任务**: 对比嵌套布局vs ConstraintLayout性能
- [ ] **检查点**: 观察到测量和布局耗时的差异
- [ ] **文件**: 添加性能对比数据

## Phase 23: 渲染优化实践 (20分钟总计)

#### Task 2.3.11: requestLayout()触发条件 (5分钟) ⏰
- [ ] **学习目标**: 理解何时需要重新测量布局
- [ ] **具体任务**: 测试改变View大小时的渲染触发
- [ ] **检查点**: 观察Measure和Layout的重新执行
- [ ] **文件**: `student_progress/RenderingOptimization.java`

#### Task 2.3.12: invalidate()绘制更新 (5分钟) ⏰
- [ ] **学习目标**: 理解仅绘制更新的优化机制
- [ ] **具体任务**: 测试改变颜色等不影响大小的属性
- [ ] **检查点**: 观察只有Draw阶段被执行
- [ ] **文件**: 完善渲染测试

#### Task 2.3.13: postInvalidate()线程安全 (5分钟) ⏰
- [ ] **学习目标**: 理解在子线程中更新UI的方法
- [ ] **具体任务**: 在后台线程中使用postInvalidate
- [ ] **检查点**: 验证线程安全的UI更新
- [ ] **文件**: 添加多线程UI更新示例

#### Task 2.3.14: 渲染面试准备 (5分钟) ⏰
- [ ] **学习目标**: 准备UI渲染相关面试问题
- [ ] **具体任务**: 整理渲染流程、MeasureSpec等核心问答
- [ ] **检查点**: 能从性能优化角度回答问题
- [ ] **文件**: 更新面试问答集

---

# 🎯 2.4 事件分发机制

## Phase 24: 事件分发基础 (25分钟总计)

#### Task 2.4.1: 责任链模式理解 (5分钟) ⏰
- [ ] **学习目标**: 理解事件分发的设计模式
- [ ] **具体任务**: 学习责任链如何传递处理事件
- [ ] **检查点**: 能画出事件传递的链条结构
- [ ] **文件**: 创建`student_progress/event_dispatch_notes.md`

#### Task 2.4.2: 三大核心方法 (5分钟) ⏰
- [ ] **学习目标**: 理解dispatchTouchEvent、onInterceptTouchEvent、onTouchEvent
- [ ] **具体任务**: 学习每个方法的职责和返回值含义
- [ ] **检查点**: 能说明true/false返回值的不同效果
- [ ] **文件**: 添加三大方法详解

#### Task 2.4.3: 事件类型和序列 (5分钟) ⏰
- [ ] **学习目标**: 理解DOWN、MOVE、UP事件序列
- [ ] **具体任务**: 学习一次完整触摸的事件流
- [ ] **检查点**: 能解释为什么DOWN事件决定后续事件归属
- [ ] **文件**: 添加事件序列说明

#### Task 2.4.4: ViewGroup vs View差异 (5分钟) ⏰
- [ ] **学习目标**: 理解容器和叶子节点的处理差异
- [ ] **具体任务**: 学习为什么View没有onInterceptTouchEvent
- [ ] **检查点**: 能解释分发和拦截的职责分工
- [ ] **文件**: 添加ViewGroup特有机制说明

#### Task 2.4.5: 事件消费机制 (5分钟) ⏰
- [ ] **学习目标**: 理解事件消费对后续事件的影响
- [ ] **具体任务**: 学习消费DOWN事件的重要性
- [ ] **检查点**: 能预测不消费DOWN事件的后果
- [ ] **文件**: 完善事件消费机制

## Phase 25: 事件流追踪实战 (30分钟总计)

#### Task 2.4.6: 简单事件流演示 (5分钟) ⏰
- [ ] **学习目标**: 追踪最简单的点击事件流
- [ ] **具体任务**: Button点击的完整事件传递路径
- [ ] **检查点**: 能完整描述Activity→ViewGroup→Button的流程
- [ ] **文件**: `student_progress/EventDispatchDemo/`

#### Task 2.4.7: 拦截机制验证 (5分钟) ⏰
- [ ] **学习目标**: 验证ViewGroup拦截事件的效果
- [ ] **具体任务**: 重写onInterceptTouchEvent返回true
- [ ] **检查点**: 观察子View收不到事件
- [ ] **文件**: 扩展EventDispatchDemo

#### Task 2.4.8: MOVE事件拦截场景 (5分钟) ⏰
- [ ] **学习目标**: 理解运行时夺取事件控制权
- [ ] **具体任务**: DOWN不拦截，MOVE时开始拦截
- [ ] **检查点**: 观察子View收到CANCEL事件
- [ ] **文件**: 实现动态拦截逻辑

#### Task 2.4.9: OnTouchListener优先级 (5分钟) ⏰
- [ ] **学习目标**: 验证监听器和方法的调用顺序
- [ ] **具体任务**: 测试onTouch和onTouchEvent的优先级
- [ ] **检查点**: 验证onTouch返回true会阻止onTouchEvent
- [ ] **文件**: 添加监听器测试

#### Task 2.4.10: 滑动冲突场景创建 (5分钟) ⏰
- [ ] **学习目标**: 重现典型的滑动冲突问题
- [ ] **具体任务**: 创建嵌套滑动的冲突场景
- [ ] **检查点**: 观察到滑动手势的干扰现象
- [ ] **文件**: 创建滑动冲突Demo

#### Task 2.4.11: 滑动冲突解决方案 (5分钟) ⏰
- [ ] **学习目标**: 实现智能的事件分发策略
- [ ] **具体任务**: 根据滑动方向决定拦截策略
- [ ] **检查点**: 实现流畅的嵌套滑动体验
- [ ] **文件**: 完善冲突解决方案

## Phase 26: 高级事件处理 (15分钟总计)

#### Task 2.4.12: 多点触控处理 (5分钟) ⏰
- [ ] **学习目标**: 理解多指触摸的事件分发
- [ ] **具体任务**: 处理ACTION_POINTER_DOWN等多点事件
- [ ] **检查点**: 能正确识别和处理多个触摸点
- [ ] **文件**: 添加多点触控示例

#### Task 2.4.13: 手势识别集成 (5分钟) ⏰
- [ ] **学习目标**: 结合GestureDetector简化手势处理
- [ ] **具体任务**: 识别单击、双击、长按、滑动手势
- [ ] **检查点**: 实现丰富的手势交互
- [ ] **文件**: 创建手势识别Demo

#### Task 2.4.14: 事件分发面试准备 (5分钟) ⏰
- [ ] **学习目标**: 准备事件分发机制面试问题
- [ ] **具体任务**: 整理经典流程分析和冲突解决问题
- [ ] **检查点**: 能现场分析复杂的事件传递场景
- [ ] **文件**: 更新面试问答集

---

# 🎯 2.5 异步心跳：Handler, Looper, MessageQueue

## Phase 27: 消息机制基础 (25分钟总计)

#### Task 2.5.1: 消息循环架构 (5分钟) ⏰
- [ ] **学习目标**: 理解Handler/Looper/MessageQueue三者关系
- [ ] **具体任务**: 学习消息队列的生产者-消费者模式
- [ ] **检查点**: 能画出三者协作的架构图
- [ ] **文件**: 创建`student_progress/handler_looper_notes.md`

#### Task 2.5.2: Looper的prepare和loop (5分钟) ⏰
- [ ] **学习目标**: 理解Looper的创建和启动过程
- [ ] **具体任务**: 学习ThreadLocal存储Looper实例
- [ ] **检查点**: 能解释一个线程只能有一个Looper的原因
- [ ] **文件**: 添加Looper机制说明

#### Task 2.5.3: MessageQueue的数据结构 (5分钟) ⏰
- [ ] **学习目标**: 理解消息队列的内部实现
- [ ] **具体任务**: 学习按时间排序的链表结构
- [ ] **检查点**: 能解释延时消息的实现原理
- [ ] **文件**: 添加消息队列结构分析

#### Task 2.5.4: Handler的发送和处理 (5分钟) ⏰
- [ ] **学习目标**: 理解Handler如何关联目标Looper
- [ ] **具体任务**: 学习Handler构造时的Looper绑定
- [ ] **检查点**: 能解释Handler如何找到正确的消息队列
- [ ] **文件**: 添加Handler机制详解

#### Task 2.5.5: Message对象复用 (5分钟) ⏰
- [ ] **学习目标**: 理解Message的对象池机制
- [ ] **具体任务**: 学习obtain()和recycle()的内存优化
- [ ] **检查点**: 能解释为什么推荐使用obtain()
- [ ] **文件**: 完善Message复用说明

## Phase 28: 主线程Looper原理 (20分钟总计)

#### Task 2.5.6: 主线程Looper创建 (5分钟) ⏰
- [ ] **学习目标**: 理解应用启动时的Looper初始化
- [ ] **具体任务**: 学习ActivityThread.main()方法的初始化
- [ ] **检查点**: 能解释为什么主线程自动有Looper
- [ ] **文件**: 添加主线程初始化流程

#### Task 2.5.7: epoll机制原理 (5分钟) ⏰
- [ ] **学习目标**: 理解Looper.loop()不卡死的原理
- [ ] **具体任务**: 学习Linux epoll的IO多路复用
- [ ] **检查点**: 能解释阻塞休眠vs空转消耗的区别
- [ ] **文件**: 添加epoll机制说明

#### Task 2.5.8: ANR产生机制 (5分钟) ⏰
- [ ] **学习目标**: 理解消息阻塞导致ANR的原因
- [ ] **具体任务**: 学习输入事件和绘制消息的处理
- [ ] **检查点**: 能解释为什么耗时操作会导致ANR
- [ ] **文件**: 添加ANR产生原理

#### Task 2.5.9: IdleHandler机制 (5分钟) ⏰
- [ ] **学习目标**: 理解主线程空闲时的任务处理
- [ ] **具体任务**: 学习IdleHandler的注册和回调
- [ ] **检查点**: 能说明IdleHandler的使用场景
- [ ] **文件**: 完善空闲处理机制

## Phase 29: 子线程Handler实践 (25分钟总计)

#### Task 2.5.10: 子线程Looper创建 (5分钟) ⏰
- [ ] **学习目标**: 在子线程中手动创建消息循环
- [ ] **具体任务**: 调用prepare()、loop()、quit()完整流程
- [ ] **检查点**: 子线程能接收和处理Handler消息
- [ ] **文件**: `student_progress/HandlerDemo.java`

#### Task 2.5.11: HandlerThread使用 (5分钟) ⏰
- [ ] **学习目标**: 使用封装好的Handler线程
- [ ] **具体任务**: 对比HandlerThread和手动创建的差异
- [ ] **检查点**: 理解HandlerThread的便利性
- [ ] **文件**: 完善HandlerDemo

#### Task 2.5.12: 线程间通信实现 (5分钟) ⏰
- [ ] **学习目标**: 实现主线程和子线程的双向通信
- [ ] **具体任务**: 子线程处理任务，主线程更新UI
- [ ] **检查点**: 实现安全的跨线程数据传递
- [ ] **文件**: 创建线程通信示例

#### Task 2.5.13: 内存泄漏分析 (5分钟) ⏰
- [ ] **学习目标**: 理解Handler内存泄漏的原因
- [ ] **具体任务**: 分析非静态内部类持有外部类引用的问题
- [ ] **检查点**: 能说明Activity无法被GC的原因链
- [ ] **文件**: 添加内存泄漏分析

#### Task 2.5.14: 内存泄漏解决方案 (5分钟) ⏰
- [ ] **学习目标**: 实现安全的Handler使用模式
- [ ] **具体任务**: 使用静态内部类+弱引用的解决方案
- [ ] **检查点**: 验证Activity能正常被回收
- [ ] **文件**: 实现安全的Handler模式

## Phase 30: Handler高级应用 (15分钟总计)

#### Task 2.5.15: 延时任务和定时器 (5分钟) ⏰
- [ ] **学习目标**: 实现延时和周期性任务
- [ ] **具体任务**: 使用postDelayed和removeCallbacks
- [ ] **检查点**: 能实现可控制的定时任务
- [ ] **文件**: 创建定时器Demo

#### Task 2.5.16: 消息优先级和同步屏障 (5分钟) ⏰
- [ ] **学习目标**: 理解消息的优先级处理机制
- [ ] **具体任务**: 学习sync barrier和异步消息
- [ ] **检查点**: 理解UI绘制消息的优先级保证
- [ ] **文件**: 添加高级消息机制说明

#### Task 2.5.17: Handler面试准备 (5分钟) ⏰
- [ ] **学习目标**: 准备Handler机制面试问题
- [ ] **具体任务**: 整理epoll、内存泄漏等核心问答
- [ ] **检查点**: 能从系统架构角度解释设计意义
- [ ] **文件**: 更新面试问答集

---

# 🎯 2.6 通信骨干：Binder IPC

## Phase 31: Binder基础原理 (25分钟总计)

#### Task 2.6.1: IPC需求和挑战 (5分钟) ⏰
- [ ] **学习目标**: 理解进程间通信的必要性
- [ ] **具体任务**: 学习Android沙箱模型对IPC的需求
- [ ] **检查点**: 能说明为什么需要跨进程通信
- [ ] **文件**: 创建`student_progress/binder_ipc_notes.md`

#### Task 2.6.2: 传统IPC方式对比 (5分钟) ⏰
- [ ] **学习目标**: 了解Linux传统IPC的局限性
- [ ] **具体任务**: 学习管道、套接字、共享内存的特点
- [ ] **检查点**: 能对比各种IPC方式的优缺点
- [ ] **文件**: 添加IPC方式对比分析

#### Task 2.6.3: Binder的一次拷贝优势 (5分钟) ⏰
- [ ] **学习目标**: 理解Binder的性能优势
- [ ] **具体任务**: 学习mmap内存映射的工作原理
- [ ] **检查点**: 能解释一次拷贝vs两次拷贝的差异
- [ ] **文件**: 添加性能优势分析

#### Task 2.6.4: Binder驱动角色 (5分钟) ⏰
- [ ] **学习目标**: 理解Binder驱动的中介作用
- [ ] **具体任务**: 学习内核态Binder驱动的职责
- [ ] **检查点**: 能说明驱动如何管理进程间的通信
- [ ] **文件**: 添加Binder驱动说明

#### Task 2.6.5: 安全机制设计 (5分钟) ⏰
- [ ] **学习目标**: 理解Binder的身份验证机制
- [ ] **具体任务**: 学习基于UID/PID的权限控制
- [ ] **检查点**: 能解释Binder如何支持Android安全模型
- [ ] **文件**: 完善安全机制说明

## Phase 32: AIDL实战开发 (30分钟总计)

#### Task 2.6.6: AIDL接口定义 (5分钟) ⏰
- [ ] **学习目标**: 创建跨进程服务接口
- [ ] **具体任务**: 定义.aidl文件和方法声明
- [ ] **检查点**: 编译生成正确的Java接口代码
- [ ] **文件**: `student_progress/BinderDemo/`项目

#### Task 2.6.7: 服务端实现 (5分钟) ⏰
- [ ] **学习目标**: 实现AIDL接口的具体逻辑
- [ ] **具体任务**: 继承Stub类，实现业务方法
- [ ] **检查点**: 服务能在独立进程中运行
- [ ] **文件**: 完善BinderDemo服务端

#### Task 2.6.8: 客户端调用 (5分钟) ⏰
- [ ] **学习目标**: 绑定服务并进行跨进程调用
- [ ] **具体任务**: 使用bindService连接远程服务
- [ ] **检查点**: 成功调用跨进程方法并获得返回值
- [ ] **文件**: 实现BinderDemo客户端

#### Task 2.6.9: 复杂数据传输 (5分钟) ⏰
- [ ] **学习目标**: 传输自定义对象和集合
- [ ] **具体任务**: 实现Parcelable接口，传输复杂数据
- [ ] **检查点**: 能正确序列化和反序列化对象
- [ ] **文件**: 添加复杂数据类型支持

#### Task 2.6.10: 回调机制实现 (5分钟) ⏰
- [ ] **学习目标**: 实现服务端向客户端的回调
- [ ] **具体任务**: 定义回调AIDL接口，实现双向通信
- [ ] **检查点**: 服务端能主动通知客户端
- [ ] **文件**: 实现回调通信机制

#### Task 2.6.11: 性能和限制测试 (5分钟) ⏰
- [ ] **学习目标**: 验证Binder的性能特性和限制
- [ ] **具体任务**: 测试大数据传输和1MB限制
- [ ] **检查点**: 观察到超过限制时的异常
- [ ] **文件**: 记录性能测试结果

## Phase 33: Binder深度原理 (20分钟总计)

#### Task 2.6.12: 代理模式分析 (5分钟) ⏰
- [ ] **学习目标**: 理解Binder的代理设计模式
- [ ] **具体任务**: 分析Stub和Proxy类的关系
- [ ] **检查点**: 能解释本地调用如何转换为远程调用
- [ ] **文件**: 添加代理模式分析

#### Task 2.6.13: Parcel序列化机制 (5分钟) ⏰
- [ ] **学习目标**: 理解数据序列化的底层机制
- [ ] **具体任务**: 学习Parcel的读写顺序和类型标记
- [ ] **检查点**: 能解释为什么读写顺序必须一致
- [ ] **文件**: 添加序列化机制说明

#### Task 2.6.14: Binder线程池 (5分钟) ⏰
- [ ] **学习目标**: 理解服务端的线程处理机制
- [ ] **具体任务**: 学习Binder线程池的管理策略
- [ ] **检查点**: 能解释多客户端并发访问的处理
- [ ] **文件**: 添加线程池机制说明

#### Task 2.6.15: Binder面试准备 (5分钟) ⏰
- [ ] **学习目标**: 准备Binder IPC面试问题
- [ ] **具体任务**: 整理一次拷贝、安全机制等核心问答
- [ ] **检查点**: 能从系统架构角度解释Binder选择
- [ ] **文件**: 更新面试问答集

## 🎯 第二章学习检查点汇总

### 2.1 ART运行时检查问题:
1. "ART的混合编译模式解决了什么问题？它是如何工作的？"
2. "Profile-guided AOT体现了什么工程思想？"
3. "云配置文件如何优化应用启动性能？"

### 2.2 Activity启动模式检查问题:
1. "设计一个音乐播放器，你会为播放页面选择什么启动模式？为什么？"
2. "singleTask模式的clear top行为对用户体验有什么影响？"
3. "taskAffinity和singleTask是如何配合工作的？"

### 2.3 UI渲染检查问题:
1. "自定义View时，为什么必须重写onMeasure？"
2. "requestLayout和invalidate有什么区别？什么时候用哪个？"
3. "MeasureSpec的设计体现了什么思想？"

### 2.4 事件分发检查问题:
1. "追踪一个触摸事件从Activity到最终处理的完整路径"
2. "如何解决ScrollView嵌套RecyclerView的滑动冲突？"
3. "为什么ViewGroup有onInterceptTouchEvent而View没有？"

### 2.5 Handler机制检查问题:
1. "Looper.loop()是死循环，为什么不会卡死主线程？"
2. "Handler内存泄漏的根本原因是什么？如何解决？"
3. "子线程能直接new Handler()吗？为什么？"

### 2.6 Binder IPC检查问题:
1. "Binder相比传统IPC有什么优势？一次拷贝是如何实现的？"
2. "为什么Binder传输数据有1MB限制？"
3. "AIDL生成的代码中Stub和Proxy分别起什么作用？"

## 🏆 第二章总进度跟踪
- **2.1 ART运行时**: 0/12 tasks (预计完成时间: 1小时)
- **2.2 Activity启动模式**: 0/9 tasks (预计完成时间: 65分钟)
- **2.3 UI渲染**: 0/14 tasks (预计完成时间: 70分钟)
- **2.4 事件分发**: 0/11 tasks (预计完成时间: 70分钟)
- **2.5 Handler机制**: 0/13 tasks (预计完成时间: 65分钟)
- **2.6 Binder IPC**: 0/10 tasks (预计完成时间: 75分钟)
- **第二章总计**: 0/69 tasks (预计完成时间: 5.5小时)