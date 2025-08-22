## 第一章：基石篇 - Java & Kotlin 语言精要 (33个微任务)

## Learning Goals
- Master Java/Kotlin concurrency fundamentals
- Understand thread safety and synchronization
- Implement lock-free programming techniques
- Build production-ready concurrent systems

### 1.1 并发原语：synchronized, volatile 与 JMM (23个微任务)
**🎯 学习目标**: 从基础概念到企业级并发系统设计的完整掌握

#### Phase 1: 并发编程基础 (4个微任务 - 20分钟)
✅ **Task 1.1.1**: JMM概念入门 + 首个编程实验 (5分钟) ⏰
✅ **Task 1.1.2**: happens-before原则 + 编程验证 (5分钟) ⏰
✅ **Task 1.1.3**: synchronized基本原理 + 线程安全实战 (5分钟) ⏰
✅ **Task 1.1.4**: volatile基本原理 + 可见性验证 (5分钟) ⏰
#### Phase 2: synchronized互斥锁原理 (4个微任务 - 20分钟)
- [ ] **Task 1.1.5**: 锁升级机制理解 + JVM参数实验 (5分钟) ⏰
- [ ] **Task 1.1.6**: 轻量级锁与CAS深度实践 (5分钟) ⏰
- [ ] **Task 1.1.7**: 重量级锁与系统调用实验 (5分钟) ⏰
- [ ] **Task 1.1.8**: synchronized性能测试实践 (5分钟) ⏰`
#### Phase 3: 高级并发编程 (11个微任务 - 55分钟)
- [ ] **Task 1.1.9**: 🚀 手动实现无锁栈 (5分钟) ⏰
  - [ ] **Intermediate级别**: 使用CAS原子操作实现线程安全的栈
  - [ ] **强制编程**: `LockFreeStack.kt` - 完全无锁的数据结构
  - [ ] **解决ABA问题**: 实现带版本号的ABA解决方案

- [ ] **Task 1.1.10**: ReentrantLock与AQS框架 (5分钟) ⏰
  - [ ] 理解Java并发包的核心框架AQS
  - [ ] **文件**: `learning_data/c01/reentrant_lock_aqs_analysis.md`

- [ ] **Task 1.1.11**: 读写锁优化方案 (5分钟) ⏰
  - [ ] 实现高效的读写分离锁机制
  - [ ] **文件**: `learning_data/c01/ReadWriteLockCache.kt`

- [ ] **Task 1.1.12**: 线程安全的集合类 (5分钟) ⏰
  - [ ] 分析并发集合的实现原理和性能特性
  - [ ] **文件**: `learning_data/c01/concurrent_collections_analysis.md`

- [ ] **Task 1.1.13**: 高性能并发缓存实践 (5分钟) ⏰
  - [ ] 实现线程安全的高性能缓存系统
  - [ ] **文件**: `learning_data/c01/HighPerformanceCache.kt`

- [ ] **Task 1.1.14**: 死锁检测与预防 (5分钟) ⏰
  - [ ] 理解死锁的四个必要条件和预防策略
  - [ ] **文件**: `learning_data/c01/deadlock_detection_prevention.md`

- [ ] **Task 1.1.15**: 协程与线程性能对比 (5分钟) ⏰
  - [ ] 对比协程和线程在并发编程中的优劣
  - [ ] **文件**: `learning_data/c01/CoroutineVsThreadPerformance.kt`

- [ ] **Task 1.1.16**: 内存一致性模型 (5分钟) ⏰
  - [ ] 理解不同平台的内存一致性模型差异
  - [ ] **文件**: `learning_data/c01/memory_consistency_models.md`

- [ ] **Task 1.1.17**: 高级无锁编程模式 (5分钟) ⏰
  - [ ] 掌握高级的无锁编程技术和模式
  - [ ] **文件**: `learning_data/c01/LockFreeProgramming.kt`

- [ ] **Task 1.1.18**: 高并发系统架构设计 (5分钟) ⏰
  - [ ] 设计支持高并发的系统架构
  - [ ] **文件**: `learning_data/c01/high_concurrency_architecture.md`

- [ ] **Task 1.1.19**: 并发编程综合面试准备 (5分钟) ⏰
  - [ ] 准备并发编程相关的面试问题
  - [ ] **文件**: `learning_data/c01/concurrent_programming_interview.md`

#### Phase 4: Senior Application (架构应用) (4个微任务 - 40分钟)
- [ ] **Task 1.1.20**: 并发编程知识体系总结 (10分钟) ⏰
  - [ ] 构建完整的并发编程知识框架
  - [ ] **文件**: `learning_data/c01/concurrent_programming_system.md`

- [ ] **Task 1.1.21**: 🏆 企业级线程安全组件设计 (10分钟) ⏰
  - [ ] **综合项目**: 设计生产级的线程安全缓存系统 (200-300行)
  - [ ] **技术栈**: ConcurrentHashMap + ReadWriteLock + AtomicLong + volatile + synchronized
  - [ ] **强制编程**: `JavaLearning/src/com/concurrency/c01/EnterpriseThreadSafeCacheSystem.java` - 企业级缓存系统
  - [ ] **功能特性**: TTL过期、LRU驱逐、统计监控、并发压力测试

- [ ] **Task 1.1.22**: 🏆 性能基准测试框架 (10分钟) ⏰
  - [ ] **测试框架**: 创建专业的并发性能测试框架
  - [ ] **对比维度**: synchronized vs volatile vs CAS vs 无锁算法
  - [ ] **强制编程**: `JavaLearning/src/com/concurrency/c01/ConcurrencyPerformanceBenchmark.java` - 性能基准测试
  - [ ] **测试指标**: 吞吐量、延迟、可扩展性、稳定性分析

- [ ] **Task 1.1.23**: 🏆 分布式锁系统实现 (10分钟) ⏰
  - [ ] **最终项目**: 实现生产级分布式锁系统 (300+行)
  - [ ] **技术特性**: 重入锁、公平锁队列、超时机制、锁续期、过期清理
  - [ ] **强制编程**: `JavaLearning/src/com/concurrency/c01/DistributedLockSystem.java` - 企业级分布式锁
  - [ ] **测试场景**: 基本功能、重入锁、并发竞争、超时过期、高并发压力测试

### 1.2 集合框架：HashMap & ConcurrentHashMap 源码剖析 (7个微任务)
**🎯 学习目标**: 深度掌握Java集合框架核心实现和高级数据结构

#### 集合基础与高级应用 (7个微任务 - 35分钟)
- [ ] **Task 1.2.1**: HashMap基础实现与红黑树转换 (5分钟) ⏰
  - [ ] 理解HashMap 1.7 vs 1.8的演进
  - [ ] **强制编程**: 手写简化版HashMap（数组+链表+红黑树）
  - [ ] **文件**: `student_progress/JavaLearning/src/com/concurrency/c01/MyHashMap.java`

- [ ] **Task 1.2.2**: ConcurrentHashMap线程安全机制 (5分钟) ⏰
  - [ ] 分析分段锁到CAS+synchronized的演进
  - [ ] **强制编程**: 线程安全Map操作对比测试
  - [ ] **性能测试**: 不同并发场景下的性能对比

- [ ] **Task 1.2.3**: hashCode分布与性能优化 (5分钟) ⏰
  - [ ] 分析hashCode分布对HashMap性能的影响
  - [ ] **强制编程**: 自定义hashCode函数的性能测试
  - [ ] **容量设计**: 理解2的幂次方设计原理

- [ ] **Task 1.2.4**: HashMap扩容与rehashing机制 (5分钟) ⏰
  - [ ] 深入理解HashMap的resize过程和rehashing优化
  - [ ] **强制编程**: 模拟扩容过程和性能影响分析
  - [ ] **内存管理**: 扩容时的内存使用和GC影响

- [ ] **Task 1.2.5**: ConcurrentHashMap分段锁深度分析 (5分钟) ⏰
  - [ ] 理解分段锁(Segment)的设计原理和高并发优化
  - [ ] **强制编程**: 实现简化版分段锁机制
  - [ ] **并发控制**: 读写操作的锁粒度优化

- [ ] **Task 1.2.6**: 生产级LRU缓存实现 (5分钟) ⏰
  - [ ] 设计企业级LRU缓存系统
  - [ ] **强制编程**: 基于LinkedHashMap的LRU + 内存管理
  - [ ] **功能特性**: 线程安全、容量限制、过期策略、统计监控

- [ ] **Task 1.2.7**: 布隆过滤器实现与优化 (5分钟) ⏰
  - [ ] 理解概率性数据结构的设计和应用
  - [ ] **强制编程**: 多哈希函数布隆过滤器 + BitSet优化
  - [ ] **应用场景**: 缓存穿透防护、大数据去重、分布式系统

### 1.3 现代范式：Kotlin协程机制 (6个微任务)
**🎯 学习目标**: 全面掌握Kotlin协程从基础到Android集成的完整体系

#### 协程核心机制与Android应用 (6个微任务 - 30分钟)
- [ ] **Task 1.3.1**: 协程基础概念与挂起机制 (5分钟) ⏰
  - [ ] 理解协程 vs 线程、挂起函数原理
  - [ ] **强制编程**: 基础协程创建和挂起函数使用
  - [ ] **状态机理解**: suspend关键字底层编译原理

- [ ] **Task 1.3.2**: 结构化并发与作用域管理 (5分钟) ⏰
  - [ ] 掌握CoroutineScope和结构化并发原则
  - [ ] **强制编程**: 协程作用域管理和生命周期控制
  - [ ] **异常处理**: 协程异常传播和处理机制

- [ ] **Task 1.3.3**: Flow异步数据流处理 (5分钟) ⏰
  - [ ] 理解Flow的背压处理和操作符链
  - [ ] **强制编程**: 实现网络请求的Flow封装
  - [ ] **响应式编程**: 数据流转换和组合操作

- [ ] **Task 1.3.4**: 协程取消与异常处理 (5分钟) ⏰
  - [ ] 掌握协程取消机制和异常处理策略
  - [ ] **强制编程**: 优雅的协程取消和资源清理
  - [ ] **最佳实践**: 避免协程泄漏和异常传播

- [ ] **Task 1.3.5**: CoroutineDispatchers线程调度原理 (5分钟) ⏰
  - [ ] 深入理解不同Dispatcher的线程模型和使用场景
  - [ ] **强制编程**: 线程切换和调度器选择实战
  - [ ] **性能优化**: 不同调度器的性能特性和适用场景

- [ ] **Task 1.3.6**: Android生命周期感知协程 (5分钟) ⏰
  - [ ] 集成协程与Android生命周期管理
  - [ ] **强制编程**: ViewModel + Fragment协程集成
  - [ ] **生命周期绑定**: lifecycleScope和viewModelScope应用



## Key Files
- Review: `../micro_tasks/MICRO_TASK_C01.md`
