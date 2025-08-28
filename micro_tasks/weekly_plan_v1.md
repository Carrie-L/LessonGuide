
<iframe src="https://claude.site/public/artifacts/e00b15e1-bec8-4ae4-a41c-dc791c7a209f/embed" title="Claude Artifact" width="100%" height="600" frameborder="0" allow="clipboard-write" allowfullscreen></iframe>


# 📅 Week 1-2 (Compose + Hilt) 学习 & 输出计划

## Week 1 — Jetpack Compose 深入

| Day | 学习重点 | 实践任务 | 产出物 |
| --- | -------- | -------- | ------ |
| Day 1 | Compose 基础语法（Composable、Modifier） | 写 Button、Text、Column/Row 的 Demo | `ComposeBasicDemo.kt` |
| Day 2 | 状态管理（remember、mutableStateOf、derivedStateOf） | 实现计数器 App（+1/-1） | `CounterApp.kt` |
| Day 3 | 列表与LazyColumn、LazyRow | 实现一个新闻列表页面（假数据） | `NewsListScreen.kt` |
| Day 4 | Material3 组件（Card、TopAppBar、Scaffold、Theme） | 添加顶部导航栏、Card 样式 | `NewsCardScreen.kt` |
| Day 5 | 副作用（LaunchedEffect、rememberCoroutineScope、DisposableEffect） | 添加定时刷新的新闻列表 | `NewsAutoRefresh.kt` |
| Day 6 | Compose Navigation | 添加两个页面：新闻列表页 → 新闻详情页 | `NavigationDemo.kt` |
| Day 7 | 本周整合复盘 | 小项目：新闻列表 + 详情 + 刷新 | **Compose Demo v1** |

---

## Week 2 — Hilt 依赖注入 + 数据持久化

| Day | 学习重点 | 实践任务 | 产出物 |
| --- | -------- | -------- | ------ |
| Day 8 | Hilt 基础（@Inject、@HiltAndroidApp、@Module、@Provides） | 配置 Hilt 基础注入（ViewModel + Repository） | `HiltSetup.kt` |
| Day 9 | Hilt 在多模块中的使用 | 拆分数据模块（network + repository） | `HiltMultiModuleDemo.kt` |
| Day 10 | Retrofit + OkHttp + Hilt | Retrofit + Hilt 注入实现网络请求 | `NewsApiService.kt` |
| Day 11 | Room + DataStore + Repository 模式 | 使用 Room 保存新闻缓存；用 DataStore 保存用户配置 | `NewsDatabase.kt` |
| Day 12 | Paging3 + Hilt | 使用 Paging3 分页加载新闻列表 | `NewsPagingDemo.kt` |
| Day 13 | Compose + Hilt 整合 | 完成 Reddit/News App：网络加载 + 缓存 + 列表 + 详情 | **NewsApp v1** |
| Day 14 | 总结 & 文档化 | 输出笔记：《Compose + Hilt 开发速查表》 | Markdown 笔记 + Demo 项目 |

### 🎯 阶段成果

- **项目产出**：一个完整的 **Reddit/News App Demo**（功能：新闻列表、详情页、网络加载 + 缓存 + 分页 + Hilt 注入）。
    
- **知识产出**：一份 **Compose + Hilt 速查笔记**（你的面试武器）。
    
- **面试话术**：你能自信说出：“我最近用 Compose/Hilt 写了一个完整 Demo，并实现了分页、缓存和依赖注入。”


# 📅 Week 3-4 (协程 + 网络原理) 学习 & 输出计划

## Week 3 — 协程深度 & Flow

| Day | 学习重点 | 实践任务 | 产出物 |
| --- | -------- | -------- | ------ |
| Day 15 | 协程基础回顾（suspend、CoroutineScope、Job、Dispatcher） | 写一个并行请求的 Demo（两个 API 并发请求 + 合并结果） | `CoroutineBasicDemo.kt` |
| Day 16 | 协程调度器原理（Main/IO/Default，线程切换机制） | 写一个多 Dispatcher 切换的 Demo，打印线程日志 | `DispatcherDemo.kt` |
| Day 17 | 结构化并发（SupervisorJob、ExceptionHandler） | 写一个「父协程失败 vs 子协程失败」的对比 Demo | `StructuredConcurrencyDemo.kt` |
| Day 18 | Flow 基础（flow{}、collect、冷流/热流） | 实现一个实时倒计时 Timer Flow | `FlowTimerDemo.kt` |
| Day 19 | Flow 高级（StateFlow、SharedFlow、背压、Buffer） | 写一个新闻数据流，支持多订阅者 | `SharedFlowDemo.kt` |
| Day 20 | Flow 底层源码走读 | 走读 Flow.kt 源码，记录 StateFlow/SharedFlow 内部实现 | 笔记：《Flow 源码解析》 |
| Day 21 | 本周整合复盘 | 小项目：用 Flow 构建新闻流订阅系统 | **Coroutine + Flow Demo** |

---

## Week 4 — OkHttp & 网络层设计

| Day | 学习重点 | 实践任务 | 产出物 |
| --- | -------- | -------- | ------ |
| Day 22 | OkHttp 基础（拦截器、连接池、缓存机制） | 写一个 LoggingInterceptor（打印 URL、耗时） | `LoggingInterceptor.kt` |
| Day 23 | OkHttp 深度（连接池复用、Call/RealCall 源码） | 走读 OkHttp 源码，画出连接池与 Dispatcher 流程图 | 笔记：《OkHttp 连接池与 Dispatcher》 |
| Day 24 | 自定义拦截器（统一 Header、Token 刷新） | 写一个 AuthInterceptor（自动刷新 Token） | `AuthInterceptor.kt` |
| Day 25 | Retrofit + OkHttp 封装 | 写一个 ApiServiceManager（支持泛型解析 + 错误处理） | `ApiServiceManager.kt` |
| Day 26 | 缓存策略设计（网络优先、本地优先、离线优先） | 封装一个 CacheInterceptor | `CacheInterceptor.kt` |
| Day 27 | 网络层整合 | 组合 OkHttp + Retrofit + 拦截器，形成可复用网络框架 | **CustomNetworkLayer.kt** |
| Day 28 | 总结 & 文档化 | 输出笔记：《协程 & 网络原理面试速查表》 | Markdown 笔记 + Demo 项目 |

### 🎯 阶段成果

- **项目产出**：
    
    1. **Coroutine + Flow Demo** → 展示协程并发控制 & Flow 数据流。
        
    2. **Custom Network Layer** → 一个企业级网络层，包含 OkHttp + Retrofit 封装 + 缓存策略 + Token 刷新。
        
- **知识产出**：
    
    - 笔记：《Flow 源码解析》《OkHttp Dispatcher & 连接池机制》《协程 & 网络面试速查表》。
        
- **面试话术**：
    
    - “我实现过一个自定义网络层，支持缓存策略和 Token 刷新。”
        
    - “我可以解释 Flow 的冷流/热流区别和背压机制。”
        
    - “我能画出 OkHttp Dispatcher 的任务调度流程图。”


# 📅 Week 5-6 (Framework 深度) 学习 & 输出计划

## Week 5 — Android 系统机制 (AMS/Binder/Activity 启动)

| Day | 学习重点 | 实践任务 | 产出物 |
| --- | -------- | -------- | ------ |
| Day 29 | Android 启动流程概览（Zygote → SystemServer → AMS） | 画出 Android 启动流程时序图 | 图解：《Android 启动全景图》 |
| Day 30 | AMS (ActivityManagerService) 职责与流程 | 走读 AMS 启动 Activity 的流程 | 笔记：《AMS 启动流程源码解析》 |
| Day 31 | Activity 启动流程（startActivity → ActivityThread） | Debug 一个 Activity 启动，打印关键日志 | Demo：《Activity 启动日志追踪》 |
| Day 32 | Binder 机制（IBinder、Binder 驱动、AIDL） | 写一个简单的 AIDL Demo（跨进程传输数据） | `SimpleAidlDemo` |
| Day 33 | Binder 源码走读（ServiceManager、BinderProxy） | 绘制 Binder IPC 调用链图 | 图解：《Binder 通信流程》 |
| Day 34 | 四大组件管理机制（Activity/Service/Broadcast/ContentProvider） | 总结四大组件的生命周期管理差异 | 笔记：《四大组件 AMS 管理总结》 |
| Day 35 | 本周整合复盘 | 输出报告：《AMS + Binder + Activity 启动机制》 | Markdown 笔记 + 流程图 |

---

## Week 6 — UI 渲染机制 & Framework 进阶

| Day | 学习重点 | 实践任务 | 产出物 |
| --- | -------- | -------- | ------ |
| Day 36 | View 绘制流程（measure → layout → draw） | 写一个自定义 View，打印 measure/layout/draw 调用日志 | `CustomViewDemo.kt` |
| Day 37 | SurfaceFlinger 与硬件加速 | 学习 Choreographer + VSync 同步机制 | 笔记：《UI 渲染与 SurfaceFlinger》 |
| Day 38 | Input 事件分发机制（ViewGroup → 子View → OnTouch） | 写一个多层嵌套 ViewGroup，观察事件分发 | `TouchEventDemo.kt` |
| Day 39 | Handler/Looper/MessageQueue 消息机制 | 手写一个简化版 Handler 模型 | `MiniHandlerDemo.kt` |
| Day 40 | WMS (WindowManagerService) 与窗口管理 | 总结 Activity、Window、View 的关系 | 图解：《WMS 与 ViewRootImpl》 |
| Day 41 | 多进程与应用沙箱机制 | 写一个多进程 Demo，验证数据隔离 | `MultiProcessDemo` |
| Day 42 | 本周整合复盘 | 输出报告：《UI 渲染 & Framework 深度总结》 | Markdown 笔记 + 流程图 |

### 🎯 阶段成果

- **项目产出**
    
    1. `SimpleAidlDemo`：演示跨进程通信。
        
    2. `CustomViewDemo`：掌握 View 绘制流程。
        
    3. `TouchEventDemo`：验证事件分发机制。
        
    4. `MiniHandlerDemo`：加深消息循环机制理解。
        
- **知识产出**
    
    - 《AMS 启动流程源码解析》
        
    - 《Binder 通信流程图》
        
    - 《UI 渲染与 SurfaceFlinger》
        
    - 《Framework 深度总结》
        
- **面试话术**
    
    - “我能完整描述 **startActivity** 的调用链：从 ContextImpl.startActivity → AMS → ActivityThread.handleLaunchActivity → Activity.onCreate。”
        
    - “我写过一个简化版 Handler 模型，可以解释 MessageQueue/Looper 的底层机制。”
        
    - “我能画出 Binder IPC 的调用链图，并解释 ServiceManager 的角色。”


# 📅 Week 7-8 (性能优化 + 面试准备) 学习 & 输出计划

## Week 7 — 性能优化实战

| Day | 学习重点 | 实践任务 | 产出物 |
| --- | -------- | -------- | ------ |
| Day 43 | 启动优化（冷启动、懒加载、预加载） | 用 Trace 工具分析 App 冷启动耗时 | `StartupTraceDemo.kt` + 报告 |
| Day 44 | 内存优化（内存泄漏、Bitmap 优化） | 使用 LeakCanary 定位内存泄漏 | `MemoryLeakDemo.kt` |
| Day 45 | OOM & GC 原理（内存回收、弱引用/软引用） | 写一个 OOM Demo（加载大图），分析 GC 日志 | `OOMDemo.kt` |
| Day 46 | 卡顿优化（Choreographer、FrameDrop、Systrace） | 实现一个 FPS 监控工具（FrameCallback） | `FpsMonitor.kt` |
| Day 47 | ANR 原理（Input/Service/Broadcast 超时） | 写一个模拟 ANR Demo，查看 traces.txt | `ANRDemo.kt` |
| Day 48 | 网络性能优化（缓存、CDN、断点续传） | 在自定义网络层中添加缓存 & 重试机制 | `NetworkOptimizer.kt` |
| Day 49 | 本周整合复盘 | 输出报告：《Android 性能优化实践总结》 | Markdown 笔记 + Demo 项目 |

---

## Week 8 — 工程化 & 面试冲刺

| Day | 学习重点 | 实践任务 | 产出物 |
| --- | -------- | -------- | ------ |
| Day 50 | 单元测试与 UI 测试（JUnit、Espresso、MockK） | 给 NewsApp 添加单元测试（ViewModel/Repository） | `NewsAppTest.kt` |
| Day 51 | CI/CD（Gradle、GitHub Actions、Firebase App Distribution） | 配置一次 CI/CD 流程（自动构建 + 单元测试） | `.github/workflows/android-ci.yml` |
| Day 52 | 设计模式回顾（单例、工厂、观察者、策略） | 在 NewsApp 中加入策略模式实现主题切换 | `ThemeStrategy.kt` |
| Day 53 | 系统设计题 1 | 设计一个离线优先的 IM 系统 | 架构图 + 方案文档 |
| Day 54 | 系统设计题 2 | 设计一个高并发的 Feed 流（新闻流/朋友圈） | 架构图 + 方案文档 |
| Day 55 | 算法冲刺 1 | 刷 LeetCode 高频题（数组/字符串/二叉树） | 算法笔记 |
| Day 56 | 算法冲刺 2 | 刷 LeetCode 高频题（图/堆/并发题） | 算法笔记 |
| Day 57 | 模拟面试 1 | Android 高频八股（协程、OkHttp、AMS、View） | 录音 or 笔记 |
| Day 58 | 模拟面试 2 | 系统设计题演练（App 架构设计） | 面试答题稿 |
| Day 59 | 模拟面试 3 | 算法题 + 行为面试（项目经历、亮点） | 面试答题稿 |
| Day 60 | 总结 & 知识体系梳理 | 输出《Android 高级开发面试速查手册》 | Markdown 笔记 + 面试集锦 |

### 🎯 阶段成果

- **项目产出**
    
    1. `FpsMonitor.kt` → 自研性能监控工具。
        
    2. `NetworkOptimizer.kt` → 带缓存 & 重试的网络层优化。
        
    3. CI/CD 自动化构建 Demo。
        
- **知识产出**
    
    - 《性能优化实践总结》
        
    - 《系统设计题答题稿》
        
    - 《Android 高级开发面试速查手册》
        
- **面试话术**
    
    - “我实现过一个 FPS 监控工具，可以实时检测卡顿。”
        
    - “我给项目加了 CI/CD 流程，自动构建 & 发布，提升了团队效率。”
        
    - “如果面试官问：如何优化冷启动？我能直接给出分阶段优化方案。”
        
    - “我能清晰描述一个离线优先的 IM 系统的架构设计。”

