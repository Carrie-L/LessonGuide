# C01 并发原语 - 学生编程练习

## 📁 目录说明

这个目录是你的编程练习工作区。按照以下步骤进行学习：

### 🔄 学习流程

1. **查看演示代码** 📖
   - 先阅读 `../../demo_code/c01/` 中的完整演示代码
   - 理解代码结构和实现目标
   - 阅读注释，理解核心概念

2. **使用模板编程** ✍️
   - 复制对应的 `.template` 文件，去掉 `.template` 后缀
   - 例如：`MemoryVisibilityDemo.java.template` → `MemoryVisibilityDemo.java`
   - **严禁直接复制演示代码！必须手动输入每一行**

3. **实现和测试** 🔬
   - 按照模板中的 TODO 指导逐步实现
   - 编译运行：`javac XXXDemo.java && java XXXDemo`
   - 观察运行结果，理解并发概念

4. **AI导师检查** 🤖
   - 完成后按格式请求AI导师检查
   - 分析代码质量和概念理解

## 📋 编程任务列表

### Phase 1: 并发编程基础 (20分钟)

- [ ] **Task 1.1.1**: `MemoryVisibilityDemo.java` - JMM内存可见性问题演示
- [ ] **Task 1.1.2**: `HappensBeforeDemo.java` - Happens-Before关系验证  
- [ ] **Task 1.1.3**: `SynchronizedDemo.java` - Synchronized线程安全实战
- [ ] **Task 1.1.4**: `VolatileDemo.java` - Volatile可见性验证

### Phase 2: 锁机制进阶 (15分钟)

- [ ] **Task 1.1.5**: `LockEscalationDemo.java` - 锁升级机制观察
- [ ] **Task 1.1.6**: `CASandLightweightLockDemo.java` - CAS与轻量级锁
- [ ] **Task 1.1.7**: `HeavyweightLockDemo.java` - 重量级锁与系统调用

### Phase 3: 高级实践 (20分钟)

- [ ] **Task 1.1.8**: `SynchronizedPerformanceTest.java` - 性能测试
- [ ] **Task 1.1.9**: `LockFreeStack.java` - 无锁栈实现 🚀
- [ ] **Task 1.1.10**: `ReentrantLockDemo.java` - ReentrantLock与AQS
- [ ] **Task 1.1.11**: `ReadWriteLockCache.java` - 读写锁优化

## 🤖 AI导师检查格式

每完成一个任务后，请按以下格式请求检查：

```
@AI导师 请检查我的代码:
文件: student_progress/student_code/c01/[文件名]
任务: [任务描述]
请分析：
1. 代码质量和正确性
2. 对并发概念的理解深度
3. 实现中的潜在问题
4. 改进建议
```

## 💡 学习建议

1. **手动输入代码**：严禁复制粘贴，培养肌肉记忆
2. **先理解后实现**：确保明白每行代码的作用
3. **多次运行观察**：并发程序的结果可能每次不同
4. **思考实际应用**：将概念与Android开发场景结合
5. **记录学习笔记**：在 `../notes/` 目录下记录心得

## 🎯 成功标准

- [ ] 代码能正确编译运行
- [ ] 能观察到预期的并发现象
- [ ] 能解释运行结果的原因
- [ ] 通过AI导师的代码评审
- [ ] 能回答相关的概念检查点问题