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

#### Task 1.1.8: 创建基础测试类 (5分钟) ⏰
- [ ] **学习目标**: 搭建测试环境
- [ ] **具体任务**: 创建`SynchronizedDemo.java`类框架
- [ ] **检查点**: 类能编译运行
- [ ] **文件**: `student_progress/SynchronizedDemo.java`

#### Task 1.1.9: synchronized方法测试 (5分钟) ⏰
- [ ] **学习目标**: 验证synchronized方法的互斥性
- [ ] **具体任务**: 写一个synchronized方法，多线程调用
- [ ] **检查点**: 观察到线程串行执行
- [ ] **文件**: 完善`SynchronizedDemo.java`

#### Task 1.1.10: synchronized代码块测试 (5分钟) ⏰
- [ ] **学习目标**: 对比方法锁vs对象锁
- [ ] **具体任务**: 实现synchronized(this)和synchronized(object)
- [ ] **检查点**: 理解不同锁对象的影响
- [ ] **文件**: 继续完善`SynchronizedDemo.java`

#### Task 1.1.11: 创建volatile测试 (5分钟) ⏰
- [ ] **学习目标**: 验证volatile的可见性
- [ ] **具体任务**: 创建`VolatileDemo.java`，测试可见性问题
- [ ] **检查点**: 观察有无volatile的区别
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

## 🏆 进度跟踪
- **当前阶段**: Phase 1 - 基础概念理解
- **当前任务**: Task 1.1.1 - JMM概念入门
- **预计完成时间**: 今天
- **总进度**: 0/16 tasks completed

## 📝 学习节奏建议
- 每完成2-3个tasks休息5分钟
- 每完成一个Phase进行checkpoint测试
- 发现不理解的地方立即停下来讨论
- 保持轻松愉快的学习氛围！