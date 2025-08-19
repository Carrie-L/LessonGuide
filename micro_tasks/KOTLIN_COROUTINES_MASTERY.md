# 🔥 Kotlin Coroutines Mastery - 协程编程实战强化版

## 📖 核心增强理念: "Async Programming Through Practice"

> **严格原则**: 异步编程的精髓在于实践！每个协程概念都必须通过亲手编程来掌握其异步本质。

### 🎯 Kotlin Coroutines 增强特性

**⛔ 严格禁令**:
- 禁止使用suspend函数而不理解挂起机制
- 禁止跳过任何协程作用域的手动实现
- 禁止在不了解调度器差异的情况下选择调度器
- 禁止未经并发安全测试就使用协程通信机制

**✅ 强制要求**:
- 每个协程概念都要从底层原理开始实现
- 每个异步操作都要分析执行时序和线程切换
- 每个并发场景都要验证数据安全性
- 每个性能优化都要通过基准测试证明

---

## 🚀 Section 1.3: Kotlin协程深度实践 (70分钟 | 14个任务)

### 📖 学习路线图

**阶段进程**:
- 🌱 **Primary阶段** (Tasks 1-5): 协程基础概念实现 - 25分钟
- 🚀 **Intermediate阶段** (Tasks 6-10): 高级协程模式 - 30分钟  
- 🏆 **Senior阶段** (Tasks 11-14): 协程架构设计 - 15分钟

---

## 🌱 PRIMARY阶段: 协程基础概念实现 (25分钟)

### Task 1.3.1: 🌱 手造Coroutine Builder - 理解协程启动机制 (5分钟) ⏰

**学习类比**: Coroutine = 可暂停的工作流程 ⏸️
- launch = 启动一个独立的工作流
- async = 启动一个有返回值的工作流  
- runBlocking = 等待工作流完成的阻塞调用
- suspend = 工作流中的暂停点

**🎯 Primary目标**: 从零实现协程构建器，理解协程的启动和执行机制

**💻 强制编程实践** (必须逐行手动输入):

```kotlin
// 文件: student_progress/c01/CustomCoroutineBuilder.kt
// 目标: 亲手实现协程构建器的核心机制

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlin.coroutines.*
import kotlin.system.measureTimeMillis
import java.util.concurrent.*
import java.util.concurrent.atomic.*

// TODO 1: 手动实现简化的协程作用域
class CustomCoroutineScope(
    private val context: CoroutineContext
) : CoroutineScope {
    
    override val coroutineContext: CoroutineContext = context
    
    private val jobs = mutableSetOf<Job>()
    private val isActive = AtomicBoolean(true)
    
    // TODO: 手动实现launch构建器
    fun customLaunch(
        context: CoroutineContext = EmptyCoroutineContext,
        start: CoroutineStart = CoroutineStart.DEFAULT,
        block: suspend CoroutineScope.() -> Unit
    ): Job {
        // 必须亲手实现：
        // 1. 创建新的协程上下文
        // 2. 创建Job对象
        // 3. 启动协程执行
        // 4. 管理协程生命周期
        
        if (!isActive.get()) {
            throw IllegalStateException("CoroutineScope is not active")
        }
        
        val newContext = coroutineContext + context
        val job = Job(newContext[Job])
        val completedContext = newContext + job
        
        val coroutine = object : AbstractCoroutine<Unit>(completedContext, true) {
            override fun onCompleted(value: Unit) {
                jobs.remove(this.coroutineContext[Job])
            }
            
            override fun onCancelled(cause: Throwable, handled: Boolean) {
                jobs.remove(this.coroutineContext[Job])
            }
        }
        
        jobs.add(job)
        
        // 启动协程
        coroutine.start(start, coroutine, block)
        
        println("🚀 启动协程: ${job.javaClass.simpleName}@${job.hashCode()}")
        return job
    }
    
    // TODO: 手动实现async构建器
    fun <T> customAsync(
        context: CoroutineContext = EmptyCoroutineContext,
        start: CoroutineStart = CoroutineStart.DEFAULT,
        block: suspend CoroutineScope.() -> T
    ): Deferred<T> {
        // 必须亲手实现：
        // 1. 创建Deferred对象
        // 2. 管理异步结果
        // 3. 处理异常传播
        
        if (!isActive.get()) {
            throw IllegalStateException("CoroutineScope is not active")
        }
        
        val newContext = coroutineContext + context
        val job = Job(newContext[Job])
        val completedContext = newContext + job
        
        val deferred = object : AbstractCoroutine<T>(completedContext, true), Deferred<T> {
            override fun onCompleted(value: T) {
                jobs.remove(this.coroutineContext[Job])
            }
            
            override fun onCancelled(cause: Throwable, handled: Boolean) {
                jobs.remove(this.coroutineContext[Job])
            }
        }
        
        jobs.add(job)
        deferred.start(start, deferred, block)
        
        println("🎯 启动异步协程: ${job.javaClass.simpleName}@${job.hashCode()}")
        return deferred
    }
    
    // TODO: 手动实现作用域取消
    fun cancel(cause: CancellationException? = null) {
        if (isActive.compareAndSet(true, false)) {
            val exception = cause ?: CancellationException("CoroutineScope cancelled")
            
            jobs.forEach { job ->
                job.cancel(exception)
            }
            jobs.clear()
            
            println("❌ 取消协程作用域，影响 ${jobs.size} 个协程")
        }
    }
    
    // TODO: 手动实现等待所有协程完成
    suspend fun joinAll() {
        jobs.toList().forEach { job ->
            job.join()
        }
    }
    
    fun getActiveJobCount(): Int = jobs.count { it.isActive }
}

// TODO 2: 手动实现协程生命周期监控
class CoroutineLifecycleMonitor {
    
    private val createdCount = AtomicInteger(0)
    private val completedCount = AtomicInteger(0)
    private val cancelledCount = AtomicInteger(0)
    private val activeCoroutines = ConcurrentHashMap<String, Long>()
    
    fun onCoroutineCreated(name: String) {
        createdCount.incrementAndGet()
        activeCoroutines[name] = System.currentTimeMillis()
        println("📊 协程创建: $name (总创建: ${createdCount.get()})")
    }
    
    fun onCoroutineCompleted(name: String) {
        completedCount.incrementAndGet()
        val startTime = activeCoroutines.remove(name)
        val duration = if (startTime != null) System.currentTimeMillis() - startTime else 0
        println("✅ 协程完成: $name (耗时: ${duration}ms, 总完成: ${completedCount.get()})")
    }
    
    fun onCoroutineCancelled(name: String) {
        cancelledCount.incrementAndGet()
        activeCoroutines.remove(name)
        println("❌ 协程取消: $name (总取消: ${cancelledCount.get()})")
    }
    
    fun printStatistics() {
        println("=== 协程生命周期统计 ===")
        println("已创建: ${createdCount.get()}")
        println("已完成: ${completedCount.get()}")
        println("已取消: ${cancelledCount.get()}")
        println("活跃中: ${activeCoroutines.size}")
        println("活跃协程: ${activeCoroutines.keys}")
    }
}

// TODO 3: 手动实现协程调度器测试
class CoroutineDispatcherAnalyzer {
    
    suspend fun analyzeDispatchers() {
        println("=== 协程调度器分析 ===")
        
        val monitor = CoroutineLifecycleMonitor()
        
        // 测试Default调度器
        println("\n1. 测试 Dispatchers.Default (CPU密集型)")
        testDispatcher("Default", Dispatchers.Default, monitor) {
            // CPU密集型任务
            var sum = 0L
            repeat(1_000_000) {
                sum += it
            }
            sum
        }
        
        // 测试IO调度器
        println("\n2. 测试 Dispatchers.IO (I/O密集型)")
        testDispatcher("IO", Dispatchers.IO, monitor) {
            // 模拟I/O操作
            delay(100)
            "I/O operation completed"
        }
        
        // 测试Main调度器(在Android环境中)
        println("\n3. 测试 Dispatchers.Unconfined (无限制)")
        testDispatcher("Unconfined", Dispatchers.Unconfined, monitor) {
            delay(50)
            "Unconfined operation completed"
        }
        
        // 测试自定义线程池调度器
        println("\n4. 测试自定义线程池调度器")
        val customDispatcher = Executors.newFixedThreadPool(2).asCoroutineDispatcher()
        testDispatcher("Custom", customDispatcher, monitor) {
            Thread.sleep(100) // 阻塞操作
            "Custom dispatcher operation completed"
        }
        
        delay(1000) // 等待所有协程完成
        monitor.printStatistics()
        
        customDispatcher.close()
    }
    
    private suspend fun <T> testDispatcher(
        name: String,
        dispatcher: CoroutineDispatcher,
        monitor: CoroutineLifecycleMonitor,
        block: suspend () -> T
    ) {
        val jobs = mutableListOf<Job>()
        
        repeat(3) { index ->
            val job = GlobalScope.launch(dispatcher) {
                val coroutineName = "$name-$index"
                monitor.onCoroutineCreated(coroutineName)
                
                try {
                    println("🔄 [$coroutineName] 开始执行，线程: ${Thread.currentThread().name}")
                    val result = block()
                    println("✅ [$coroutineName] 执行完成，结果: $result")
                    monitor.onCoroutineCompleted(coroutineName)
                } catch (e: Exception) {
                    println("❌ [$coroutineName] 执行异常: ${e.message}")
                    monitor.onCoroutineCancelled(coroutineName)
                }
            }
            jobs.add(job)
        }
        
        jobs.joinAll()
    }
}

// TODO 4: 手动实现协程异常处理测试
class CoroutineExceptionAnalyzer {
    
    suspend fun analyzeExceptionHandling() {
        println("=== 协程异常处理分析 ===")
        
        // 测试1: launch中的异常处理
        println("\n1. 测试 launch 中的异常处理")
        testLaunchException()
        
        // 测试2: async中的异常处理
        println("\n2. 测试 async 中的异常处理")
        testAsyncException()
        
        // 测试3: 协程作用域异常处理
        println("\n3. 测试协程作用域异常处理")
        testScopeException()
        
        // 测试4: 异常传播
        println("\n4. 测试异常传播")
        testExceptionPropagation()
    }
    
    private suspend fun testLaunchException() {
        val scope = CoroutineScope(Dispatchers.Default + SupervisorJob())
        
        val job = scope.launch {
            println("🚀 launch协程开始")
            delay(100)
            throw RuntimeException("launch中的异常")
        }
        
        try {
            job.join()
        } catch (e: Exception) {
            println("❌ 捕获到launch异常: ${e.message}")
        }
    }
    
    private suspend fun testAsyncException() {
        val scope = CoroutineScope(Dispatchers.Default)
        
        val deferred = scope.async {
            println("🎯 async协程开始")
            delay(100)
            throw RuntimeException("async中的异常")
        }
        
        try {
            val result = deferred.await()
            println("✅ async结果: $result")
        } catch (e: Exception) {
            println("❌ 捕获到async异常: ${e.message}")
        }
    }
    
    private suspend fun testScopeException() {
        val exceptionHandler = CoroutineExceptionHandler { _, exception ->
            println("🔥 CoroutineExceptionHandler捕获: ${exception.message}")
        }
        
        val scope = CoroutineScope(Dispatchers.Default + exceptionHandler)
        
        scope.launch {
            println("🚀 子协程1开始")
            delay(100)
            throw RuntimeException("子协程1异常")
        }
        
        scope.launch {
            println("🚀 子协程2开始") 
            delay(200)
            println("✅ 子协程2正常完成")
        }
        
        delay(300) // 等待所有协程完成
    }
    
    private suspend fun testExceptionPropagation() {
        val parentJob = Job()
        val scope = CoroutineScope(Dispatchers.Default + parentJob)
        
        val child1 = scope.launch {
            println("🚀 子协程1开始")
            delay(100)
            throw RuntimeException("子协程1异常")
        }
        
        val child2 = scope.launch {
            println("🚀 子协程2开始")
            try {
                delay(200)
                println("✅ 子协程2尝试完成")
            } catch (e: CancellationException) {
                println("❌ 子协程2被取消: ${e.message}")
            }
        }
        
        try {
            joinAll(child1, child2)
        } catch (e: Exception) {
            println("❌ 异常传播到父级: ${e.message}")
        }
    }
}

// TODO 5: 手动实现综合测试和演示
class CoroutineBuilderDemo {
    
    suspend fun runComprehensiveDemo() {
        println("🔧 自制协程构建器综合演示")
        
        // 创建自定义协程作用域
        val customScope = CustomCoroutineScope(
            Dispatchers.Default + SupervisorJob() + CoroutineName("CustomScope")
        )
        
        try {
            // 测试1: 基本协程启动
            println("\n=== 测试1: 基本协程启动 ===")
            testBasicCoroutineLaunch(customScope)
            
            // 测试2: 异步操作
            println("\n=== 测试2: 异步操作测试 ===")
            testAsyncOperations(customScope)
            
            // 测试3: 协程取消
            println("\n=== 测试3: 协程取消测试 ===")
            testCoroutineCancellation(customScope)
            
            // 测试4: 调度器分析
            println("\n=== 测试4: 调度器分析 ===")
            val analyzer = CoroutineDispatcherAnalyzer()
            analyzer.analyzeDispatchers()
            
            // 测试5: 异常处理
            println("\n=== 测试5: 异常处理分析 ===")
            val exceptionAnalyzer = CoroutineExceptionAnalyzer()
            exceptionAnalyzer.analyzeExceptionHandling()
            
        } finally {
            customScope.cancel()
        }
    }
    
    private suspend fun testBasicCoroutineLaunch(scope: CustomCoroutineScope) {
        val jobs = mutableListOf<Job>()
        
        repeat(3) { index ->
            val job = scope.customLaunch {
                println("🚀 协程$index 开始执行，线程: ${Thread.currentThread().name}")
                delay(1000)
                println("✅ 协程$index 执行完成")
            }
            jobs.add(job)
        }
        
        println("📊 活跃协程数: ${scope.getActiveJobCount()}")
        jobs.joinAll()
        println("📊 所有协程执行完成")
    }
    
    private suspend fun testAsyncOperations(scope: CustomCoroutineScope) {
        val deferreds = mutableListOf<Deferred<String>>()
        
        repeat(3) { index ->
            val deferred = scope.customAsync {
                println("🎯 异步任务$index 开始，线程: ${Thread.currentThread().name}")
                delay(500)
                "异步任务$index 的结果"
            }
            deferreds.add(deferred)
        }
        
        val results = deferreds.awaitAll()
        results.forEach { result ->
            println("📥 获取结果: $result")
        }
    }
    
    private suspend fun testCoroutineCancellation(scope: CustomCoroutineScope) {
        val longRunningJob = scope.customLaunch {
            try {
                println("🔄 长时间运行的任务开始")
                repeat(10) { index ->
                    delay(500)
                    println("📊 任务进度: ${index + 1}/10")
                }
                println("✅ 长时间任务完成")
            } catch (e: CancellationException) {
                println("❌ 任务被取消: ${e.message}")
            }
        }
        
        delay(2000) // 运行2秒后取消
        longRunningJob.cancel()
        longRunningJob.join()
    }
}

// TODO 6: 手动实现主函数
suspend fun main() {
    println("🔬 Kotlin协程构建器实验室")
    println("目标: 深度理解协程的启动和执行机制\n")
    
    val demo = CoroutineBuilderDemo()
    demo.runComprehensiveDemo()
    
    println("\n🎯 学习总结:")
    println("1. 协程是轻量级的线程，可以在同一线程上挂起和恢复")
    println("2. launch启动独立协程，async启动有返回值的协程")
    println("3. 协程作用域管理协程的生命周期")
    println("4. 不同调度器适用于不同类型的任务")
    println("5. 协程异常处理有特殊的传播规则")
}

// 运行入口
fun main() = runBlocking {
    main()
}
```

**📋 实践步骤清单** (必须逐项完成):
- [ ] 📝 手动创建文件 `CustomCoroutineBuilder.kt`
- [ ] ⌨️ 逐行手动输入所有核心代码
- [ ] 🔧 实现自定义协程作用域
- [ ] 🔧 实现launch和async构建器
- [ ] 🔧 实现协程生命周期监控
- [ ] 🔧 实现调度器分析工具
- [ ] 🔧 实现异常处理分析
- [ ] 🏃 运行所有测试场景
- [ ] 📊 分析协程执行时序和线程切换
- [ ] 📝 观察异常传播和取消机制

**✅ Primary检查点验证**:
1. **协程启动**: 能成功创建和启动自定义协程吗？
2. **调度器理解**: 能区分不同调度器的适用场景吗？
3. **异常处理**: 能正确处理协程中的异常传播吗？

**🎯 置信度自评**: 
我理解协程的启动和执行机制： □ 完全掌握 □ 基本掌握 □ 需要练习

---

### Task 1.3.2: 🌱 手造Channel - 协程间通信机制 (5分钟) ⏰

**学习类比**: Channel = 协程间的管道通信 📡
- Channel = 传输带，可以传递数据
- send = 向传输带放入数据
- receive = 从传输带取出数据
- buffer = 传输带的缓冲容量

**🎯 Primary目标**: 从零实现Channel，理解协程间的通信机制

**💻 强制编程实践** (必须逐行手动输入):

```kotlin
// 文件: student_progress/c01/CustomChannel.kt
// 目标: 亲手实现协程Channel的通信机制

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*
import kotlinx.coroutines.selects.*
import java.util.concurrent.*
import java.util.concurrent.atomic.*
import kotlin.coroutines.*

// TODO 1: 手动实现简化的Channel接口
interface CustomSendChannel<in E> {
    suspend fun send(element: E)
    fun trySend(element: E): Boolean
    fun close(): Boolean
    val isClosedForSend: Boolean
}

interface CustomReceiveChannel<out E> {
    suspend fun receive(): E
    fun tryReceive(): E?
    val isClosedForReceive: Boolean
}

interface CustomChannel<E> : CustomSendChannel<E>, CustomReceiveChannel<E>

// TODO 2: 手动实现无缓冲Channel (Rendezvous)
class RendezvousChannel<E> : CustomChannel<E> {
    
    private val sendQueue = LinkedBlockingQueue<SendRequest<E>>()
    private val receiveQueue = LinkedBlockingQueue<ReceiveRequest<E>>()
    private val isClosed = AtomicBoolean(false)
    
    private data class SendRequest<E>(
        val element: E,
        val continuation: CancellableContinuation<Unit>
    )
    
    private data class ReceiveRequest<E>(
        val continuation: CancellableContinuation<E>
    )
    
    override suspend fun send(element: E) {
        // 必须亲手实现：
        // 1. 检查通道是否已关闭
        // 2. 尝试直接传递给等待的接收者
        // 3. 如果没有接收者，挂起等待
        
        if (isClosed.get()) {
            throw ClosedSendChannelException("Channel is closed")
        }
        
        // 尝试直接传递
        val receiver = receiveQueue.poll()
        if (receiver != null) {
            receiver.continuation.resume(element)
            return
        }
        
        // 没有等待的接收者，挂起当前协程
        suspendCancellableCoroutine<Unit> { continuation ->
            val sendRequest = SendRequest(element, continuation)
            sendQueue.offer(sendRequest)
            
            continuation.invokeOnCancellation {
                sendQueue.remove(sendRequest)
            }
        }
    }
    
    override fun trySend(element: E): Boolean {
        // 必须亲手实现：
        // 1. 非阻塞的发送尝试
        // 2. 立即返回成功或失败
        
        if (isClosed.get()) return false
        
        val receiver = receiveQueue.poll()
        if (receiver != null) {
            receiver.continuation.resume(element)
            return true
        }
        
        return false // 没有等待的接收者
    }
    
    override suspend fun receive(): E {
        // 必须亲手实现：
        // 1. 检查通道是否已关闭
        // 2. 尝试直接从等待的发送者获取
        // 3. 如果没有发送者，挂起等待
        
        if (isClosed.get() && sendQueue.isEmpty()) {
            throw ClosedReceiveChannelException("Channel is closed")
        }
        
        // 尝试直接获取
        val sender = sendQueue.poll()
        if (sender != null) {
            sender.continuation.resume(Unit)
            return sender.element
        }
        
        // 没有等待的发送者，挂起当前协程
        return suspendCancellableCoroutine { continuation ->
            val receiveRequest = ReceiveRequest<E>(continuation)
            receiveQueue.offer(receiveRequest)
            
            continuation.invokeOnCancellation {
                receiveQueue.remove(receiveRequest)
            }
        }
    }
    
    override fun tryReceive(): E? {
        // 必须亲手实现：非阻塞的接收尝试
        
        val sender = sendQueue.poll()
        if (sender != null) {
            sender.continuation.resume(Unit)
            return sender.element
        }
        
        return null // 没有等待的发送者
    }
    
    override fun close(): Boolean {
        if (isClosed.compareAndSet(false, true)) {
            // 取消所有等待的发送者
            while (true) {
                val sender = sendQueue.poll() ?: break
                sender.continuation.cancel(ClosedSendChannelException("Channel is closed"))
            }
            
            // 取消所有等待的接收者
            while (true) {
                val receiver = receiveQueue.poll() ?: break
                receiver.continuation.cancel(ClosedReceiveChannelException("Channel is closed"))
            }
            
            return true
        }
        return false
    }
    
    override val isClosedForSend: Boolean get() = isClosed.get()
    override val isClosedForReceive: Boolean get() = isClosed.get() && sendQueue.isEmpty()
    
    fun getQueueSizes(): Pair<Int, Int> = sendQueue.size to receiveQueue.size
}

// TODO 3: 手动实现有缓冲Channel
class BufferedChannel<E>(private val capacity: Int) : CustomChannel<E> {
    
    private val buffer = LinkedBlockingQueue<E>(capacity)
    private val receiveQueue = LinkedBlockingQueue<ReceiveRequest<E>>()
    private val isClosed = AtomicBoolean(false)
    
    private data class ReceiveRequest<E>(
        val continuation: CancellableContinuation<E>
    )
    
    override suspend fun send(element: E) {
        // 必须亲手实现：
        // 1. 检查通道是否已关闭
        // 2. 尝试放入缓冲区
        // 3. 如果缓冲区满了，挂起等待
        
        if (isClosed.get()) {
            throw ClosedSendChannelException("Channel is closed")
        }
        
        // 尝试直接传递给等待的接收者
        val receiver = receiveQueue.poll()
        if (receiver != null) {
            receiver.continuation.resume(element)
            return
        }
        
        // 尝试放入缓冲区
        if (buffer.offer(element)) {
            return // 成功放入缓冲区
        }
        
        // 缓冲区满了，挂起等待
        suspendCancellableCoroutine<Unit> { continuation ->
            // 简化实现：等待缓冲区有空间
            GlobalScope.launch {
                while (!buffer.offer(element) && !isClosed.get()) {
                    delay(1) // 等待缓冲区有空间
                }
                
                if (isClosed.get()) {
                    continuation.cancel(ClosedSendChannelException("Channel is closed"))
                } else {
                    continuation.resume(Unit)
                }
            }
        }
    }
    
    override fun trySend(element: E): Boolean {
        if (isClosed.get()) return false
        
        // 尝试直接传递
        val receiver = receiveQueue.poll()
        if (receiver != null) {
            receiver.continuation.resume(element)
            return true
        }
        
        // 尝试放入缓冲区
        return buffer.offer(element)
    }
    
    override suspend fun receive(): E {
        // 必须亲手实现：
        // 1. 尝试从缓冲区获取
        // 2. 如果缓冲区为空，挂起等待
        
        // 尝试从缓冲区获取
        val element = buffer.poll()
        if (element != null) {
            return element
        }
        
        if (isClosed.get()) {
            throw ClosedReceiveChannelException("Channel is closed")
        }
        
        // 缓冲区为空，挂起等待
        return suspendCancellableCoroutine { continuation ->
            val receiveRequest = ReceiveRequest<E>(continuation)
            receiveQueue.offer(receiveRequest)
            
            continuation.invokeOnCancellation {
                receiveQueue.remove(receiveRequest)
            }
        }
    }
    
    override fun tryReceive(): E? {
        val element = buffer.poll()
        if (element != null) {
            return element
        }
        
        return null
    }
    
    override fun close(): Boolean {
        if (isClosed.compareAndSet(false, true)) {
            // 取消所有等待的接收者
            while (true) {
                val receiver = receiveQueue.poll() ?: break
                receiver.continuation.cancel(ClosedReceiveChannelException("Channel is closed"))
            }
            return true
        }
        return false
    }
    
    override val isClosedForSend: Boolean get() = isClosed.get()
    override val isClosedForReceive: Boolean get() = isClosed.get() && buffer.isEmpty()
    
    fun getBufferSize(): Int = buffer.size
    fun getCapacity(): Int = capacity
}

// TODO 4: 手动实现Channel通信模式测试
class ChannelCommunicationPatterns {
    
    suspend fun demonstrateProducerConsumer() {
        println("=== 生产者-消费者模式 ===")
        
        val channel = BufferedChannel<Int>(5)
        
        // 生产者协程
        val producer = GlobalScope.launch {
            try {
                for (i in 1..10) {
                    channel.send(i)
                    println("🏭 生产者发送: $i (缓冲区大小: ${channel.getBufferSize()})")
                    delay(100)
                }
                println("🏭 生产者完成")
            } finally {
                channel.close()
            }
        }
        
        // 消费者协程
        val consumer = GlobalScope.launch {
            try {
                while (true) {
                    val item = channel.receive()
                    println("🍽️ 消费者接收: $item")
                    delay(200) // 消费比生产慢
                }
            } catch (e: ClosedReceiveChannelException) {
                println("🍽️ 消费者完成：通道已关闭")
            }
        }
        
        joinAll(producer, consumer)
    }
    
    suspend fun demonstrateFanOutPattern() {
        println("\n=== 扇出模式 (一对多) ===")
        
        val channel = RendezvousChannel<String>()
        
        // 单个生产者
        val producer = GlobalScope.launch {
            try {
                repeat(6) { index ->
                    val message = "Message-$index"
                    channel.send(message)
                    println("📡 发送: $message")
                    delay(100)
                }
            } finally {
                channel.close()
            }
        }
        
        // 多个消费者
        val consumers = List(3) { consumerId ->
            GlobalScope.launch {
                try {
                    while (true) {
                        val message = channel.receive()
                        println("📱 消费者$consumerId 接收: $message")
                        delay(50)
                    }
                } catch (e: ClosedReceiveChannelException) {
                    println("📱 消费者$consumerId 完成")
                }
            }
        }
        
        joinAll(producer, *consumers.toTypedArray())
    }
    
    suspend fun demonstrateFanInPattern() {
        println("\n=== 扇入模式 (多对一) ===")
        
        val channel = BufferedChannel<String>(10)
        
        // 多个生产者
        val producers = List(3) { producerId ->
            GlobalScope.launch {
                repeat(3) { index ->
                    val message = "Producer$producerId-Message$index"
                    channel.send(message)
                    println("🏭 生产者$producerId 发送: $message")
                    delay(100 + producerId * 50)
                }
                println("🏭 生产者$producerId 完成")
            }
        }
        
        // 等待所有生产者完成后关闭通道
        GlobalScope.launch {
            producers.joinAll()
            channel.close()
        }
        
        // 单个消费者
        val consumer = GlobalScope.launch {
            try {
                while (true) {
                    val message = channel.receive()
                    println("📦 聚合消费者接收: $message")
                    delay(80)
                }
            } catch (e: ClosedReceiveChannelException) {
                println("📦 聚合消费者完成")
            }
        }
        
        consumer.join()
    }
    
    suspend fun demonstratePipelinePattern() {
        println("\n=== 管道模式 ===")
        
        val input = BufferedChannel<Int>(5)
        val intermediate = BufferedChannel<Int>(5)
        val output = BufferedChannel<String>(5)
        
        // 阶段1: 数据生成
        val generator = GlobalScope.launch {
            try {
                for (i in 1..5) {
                    input.send(i)
                    println("🎲 生成数据: $i")
                    delay(100)
                }
            } finally {
                input.close()
            }
        }
        
        // 阶段2: 数据处理
        val processor = GlobalScope.launch {
            try {
                while (true) {
                    val data = input.receive()
                    val processed = data * data
                    intermediate.send(processed)
                    println("⚙️ 处理数据: $data -> $processed")
                    delay(50)
                }
            } catch (e: ClosedReceiveChannelException) {
                intermediate.close()
            }
        }
        
        // 阶段3: 数据格式化
        val formatter = GlobalScope.launch {
            try {
                while (true) {
                    val data = intermediate.receive()
                    val formatted = "Result: $data"
                    output.send(formatted)
                    println("📝 格式化数据: $data -> $formatted")
                    delay(30)
                }
            } catch (e: ClosedReceiveChannelException) {
                output.close()
            }
        }
        
        // 最终消费者
        val finalConsumer = GlobalScope.launch {
            try {
                while (true) {
                    val result = output.receive()
                    println("✅ 最终结果: $result")
                    delay(20)
                }
            } catch (e: ClosedReceiveChannelException) {
                println("✅ 管道处理完成")
            }
        }
        
        joinAll(generator, processor, formatter, finalConsumer)
    }
}

// TODO 5: 手动实现性能测试和比较
class ChannelPerformanceTest {
    
    suspend fun compareChannelTypes() {
        println("=== Channel性能对比测试 ===")
        
        val messageCount = 10000
        
        // 测试无缓冲Channel
        println("\n1. 测试无缓冲Channel (Rendezvous)")
        val rendezvousTime = measureTimeMillis {
            testChannelPerformance(RendezvousChannel<Int>(), messageCount)
        }
        println("无缓冲Channel耗时: ${rendezvousTime}ms")
        
        // 测试小缓冲Channel
        println("\n2. 测试小缓冲Channel (容量=10)")
        val smallBufferTime = measureTimeMillis {
            testChannelPerformance(BufferedChannel<Int>(10), messageCount)
        }
        println("小缓冲Channel耗时: ${smallBufferTime}ms")
        
        // 测试大缓冲Channel
        println("\n3. 测试大缓冲Channel (容量=1000)")
        val largeBufferTime = measureTimeMillis {
            testChannelPerformance(BufferedChannel<Int>(1000), messageCount)
        }
        println("大缓冲Channel耗时: ${largeBufferTime}ms")
        
        // 性能对比
        println("\n📊 性能对比:")
        println("无缓冲: ${rendezvousTime}ms (基准)")
        println("小缓冲: ${smallBufferTime}ms (${smallBufferTime * 100 / rendezvousTime}%)")
        println("大缓冲: ${largeBufferTime}ms (${largeBufferTime * 100 / rendezvousTime}%)")
    }
    
    private suspend fun testChannelPerformance(channel: CustomChannel<Int>, messageCount: Int) {
        val producer = GlobalScope.launch {
            try {
                repeat(messageCount) { i ->
                    channel.send(i)
                }
            } finally {
                channel.close()
            }
        }
        
        val consumer = GlobalScope.launch {
            var receivedCount = 0
            try {
                while (true) {
                    channel.receive()
                    receivedCount++
                }
            } catch (e: ClosedReceiveChannelException) {
                println("接收消息数: $receivedCount")
            }
        }
        
        joinAll(producer, consumer)
    }
}

// TODO 6: 手动实现主函数和综合演示
class CustomChannelDemo {
    
    suspend fun runComprehensiveDemo() {
        println("📡 自制Channel通信系统演示")
        
        // 演示1: 通信模式
        println("\n=== 演示1: 基本通信模式 ===")
        val patterns = ChannelCommunicationPatterns()
        patterns.demonstrateProducerConsumer()
        patterns.demonstrateFanOutPattern()
        patterns.demonstrateFanInPattern()
        patterns.demonstratePipelinePattern()
        
        // 演示2: 性能对比
        println("\n=== 演示2: 性能对比测试 ===")
        val performanceTest = ChannelPerformanceTest()
        performanceTest.compareChannelTypes()
        
        println("\n🎯 Channel学习总结:")
        println("1. Channel是协程间通信的桥梁")
        println("2. 无缓冲Channel实现同步通信")
        println("3. 有缓冲Channel提高通信效率")
        println("4. 不同通信模式适用于不同场景")
        println("5. 缓冲区大小影响性能和内存使用")
    }
}

// 运行入口
suspend fun main() {
    val demo = CustomChannelDemo()
    demo.runComprehensiveDemo()
}

fun main() = runBlocking {
    main()
}
```

**📋 实践步骤清单** (必须逐项完成):
- [ ] 📝 手动创建文件 `CustomChannel.kt`
- [ ] ⌨️ 逐行手动输入所有核心代码
- [ ] 🔧 实现无缓冲Channel (Rendezvous)
- [ ] 🔧 实现有缓冲Channel
- [ ] 🔧 实现各种通信模式
- [ ] 🔧 实现性能测试工具
- [ ] 🏃 运行所有通信模式演示
- [ ] 📊 分析不同Channel类型的性能差异
- [ ] 📝 观察协程间的同步和异步通信

**✅ Primary检查点验证**:
1. **通信机制**: 能理解Channel的发送和接收机制吗？
2. **缓冲效果**: 能观察到缓冲区对性能的影响吗？
3. **通信模式**: 能灵活运用不同的通信模式吗？

**🎯 置信度自评**: 
我理解协程间的Channel通信机制： □ 完全掌握 □ 基本掌握 □ 需要练习

---

<function_calls>
<invoke name="TodoWrite">
<parameter name="todos">[{"id": "1", "content": "Apply framework to Chapter 1 - Java/Kotlin fundamentals", "status": "completed"}, {"id": "2", "content": "Create detailed concurrent programming exercises", "status": "completed"}, {"id": "3", "content": "Design collection framework hands-on labs", "status": "completed"}, {"id": "4", "content": "Build Kotlin coroutines practical project", "status": "completed"}, {"id": "5", "content": "Create comprehensive integration project", "status": "in_progress"}]