## 🧪 第六章：测试利刃 - Android 测试体系精通 (总时长: 350分钟)

### 6.1 单元测试基石：JUnit & Mockito (总时长: 85分钟)

#### Task 6.1.1: 测试金字塔理论 (5分钟) ⏰
- [ ] **学习目标**: 理解单元测试、集成测试、UI测试的分层设计
- [ ] **具体任务**: 画出测试金字塔并解释各层的作用和比例
- [ ] **检查点**: 能解释为什么单元测试应该占最大比例
- [ ] **文件**: 创建`testing_pyramid_notes.md`

#### Task 6.1.2: JUnit 5核心注解 (5分钟) ⏰
- [ ] **学习目标**: 掌握@Test、@BeforeEach、@AfterEach等核心注解
- [ ] **具体任务**: 创建基础测试类演示所有核心注解
- [ ] **检查点**: 理解测试生命周期和setup/teardown的作用
- [ ] **文件**: 创建`JUnitBasicsTest.java`

#### Task 6.1.3: 断言方法详解 (5分钟) ⏰
- [ ] **学习目标**: 掌握assertEquals、assertTrue、assertThrows等断言
- [ ] **具体任务**: 实现各种类型数据的断言测试
- [ ] **检查点**: 能选择合适的断言方法验证不同条件
- [ ] **文件**: 创建`AssertionExamplesTest.java`

#### Task 6.1.4: Mockito基础概念 (5分钟) ⏰
- [ ] **学习目标**: 理解Mock、Spy、Stub的区别和应用场景
- [ ] **具体任务**: 创建示例演示三种Test Double的用法
- [ ] **检查点**: 能解释何时使用Mock vs Spy vs 真实对象
- [ ] **文件**: 创建`MockitoBasicsTest.java`

#### Task 6.1.5: when-thenReturn基础 (5分钟) ⏰
- [ ] **学习目标**: 掌握Mockito的基础打桩语法
- [ ] **具体任务**: 模拟方法返回值、抛异常、连续调用
- [ ] **检查点**: 能灵活配置Mock对象的行为
- [ ] **文件**: 扩展`MockitoBasicsTest.java`

#### Task 6.1.6: verify验证机制 (5分钟) ⏰
- [ ] **学习目标**: 使用verify验证方法调用次数和参数
- [ ] **具体任务**: 实现times()、never()、atLeast()等验证
- [ ] **检查点**: 能验证Mock对象的交互行为
- [ ] **文件**: 创建`MockitoVerifyTest.java`

#### Task 6.1.7: ArgumentCaptor使用 (5分钟) ⏰
- [ ] **学习目标**: 捕获并验证传递给Mock对象的参数
- [ ] **具体任务**: 使用ArgumentCaptor验证复杂对象参数
- [ ] **检查点**: 能深入验证方法调用的参数内容
- [ ] **文件**: 创建`ArgumentCaptorTest.java`

#### Task 6.1.8: Repository单元测试 (5分钟) ⏰
- [ ] **学习目标**: 测试数据访问层的业务逻辑
- [ ] **具体任务**: 模拟数据库操作，测试Repository方法
- [ ] **检查点**: 能隔离外部依赖测试纯业务逻辑
- [ ] **文件**: 创建`UserRepositoryTest.java`

#### Task 6.1.9: ViewModel单元测试 (5分钟) ⏰
- [ ] **学习目标**: 测试ViewModel的状态变化和业务逻辑
- [ ] **具体任务**: 模拟Repository，测试ViewModel交互
- [ ] **检查点**: 能验证LiveData/StateFlow的状态变化
- [ ] **文件**: 创建`UserViewModelTest.java`

#### Task 6.1.10: MockK基础使用 (5分钟) ⏰
- [ ] **学习目标**: 了解Kotlin专用测试框架MockK的优势
- [ ] **具体任务**: 使用MockK测试Kotlin特有功能
- [ ] **检查点**: 能测试extension functions、object等
- [ ] **文件**: 创建`MockKExamplesTest.kt`

#### Task 6.1.11: object模拟 (5分钟) ⏰
- [ ] **学习目标**: 使用MockK模拟Kotlin object单例
- [ ] **具体任务**: 模拟单例对象进行测试
- [ ] **检查点**: 能测试依赖单例的类
- [ ] **文件**: 扩展`MockKExamplesTest.kt`

#### Task 6.1.12: 扩展函数测试 (5分钟) ⏰
- [ ] **学习目标**: 测试Kotlin extension functions
- [ ] **具体任务**: 模拟和验证扩展函数的行为
- [ ] **检查点**: 能测试包含扩展函数的业务逻辑
- [ ] **文件**: 创建`ExtensionFunctionTest.kt`

#### Task 6.1.13: 依赖注入测试设计 (5分钟) ⏰
- [ ] **学习目标**: 理解可测试代码的设计原则
- [ ] **具体任务**: 重构紧耦合代码使其可测试
- [ ] **检查点**: 能识别和解决测试中的依赖问题
- [ ] **文件**: 创建`TestableDesignExamples.java`

#### Task 6.1.14: Test Double应用场景 (5分钟) ⏰
- [ ] **学习目标**: 分析Mock、Stub、Fake的具体应用场景
- [ ] **具体任务**: 为不同场景选择合适的Test Double类型
- [ ] **检查点**: 能根据测试目标选择最佳策略
- [ ] **文件**: 创建`TestDoubleScenarios.md`

#### Task 6.1.15: 单元测试最佳实践 (5分钟) ⏰
- [ ] **学习目标**: 掌握单元测试的命名、组织、维护最佳实践
- [ ] **具体任务**: 总结测试代码质量标准
- [ ] **检查点**: 能写出可读性强、易维护的测试代码
- [ ] **文件**: 创建`unit_testing_best_practices.md`

#### Task 6.1.16: 单元测试面试准备 (5分钟) ⏰
- [ ] **学习目标**: 准备单元测试相关面试问题
- [ ] **具体任务**: 整理常见问题和深度解答
- [ ] **检查点**: 能从测试驱动开发角度回答问题
- [ ] **文件**: 更新`interview_qa_testing.md`

#### Task 6.1.17: 单元测试阶段总结 (5分钟) ⏰
- [ ] **学习目标**: 总结单元测试核心知识点
- [ ] **具体任务**: 创建知识地图和学习回顾
- [ ] **检查点**: 对单元测试体系有全面理解
- [ ] **文件**: 创建`unit_testing_summary.md`

### 6.2 协程测试：TestCoroutineDispatcher & runTest (总时长: 85分钟)

#### Task 6.2.1: 协程测试挑战分析 (5分钟) ⏰
- [ ] **学习目标**: 理解测试异步代码的难点和解决思路
- [ ] **具体任务**: 分析协程测试的时间控制、状态验证等挑战
- [ ] **检查点**: 能识别协程测试中的常见陷阱
- [ ] **文件**: 创建`coroutine_testing_challenges.md`

#### Task 6.2.2: runTest基础用法 (5分钟) ⏰
- [ ] **学习目标**: 掌握runTest的基本语法和作用域
- [ ] **具体任务**: 创建基础协程测试示例
- [ ] **检查点**: 能使用runTest替代runBlocking进行测试
- [ ] **文件**: 创建`RunTestBasicsTest.kt`

#### Task 6.2.3: TestDispatcher配置 (5分钟) ⏰
- [ ] **学习目标**: 理解StandardTestDispatcher和UnconfinedTestDispatcher区别
- [ ] **具体任务**: 对比两种测试调度器的行为差异
- [ ] **检查点**: 能根据测试需求选择合适的调度器
- [ ] **文件**: 创建`TestDispatcherComparison.kt`

#### Task 6.2.4: 时间控制机制 (5分钟) ⏰
- [ ] **学习目标**: 使用advanceTimeBy和advanceUntilIdle控制虚拟时间
- [ ] **具体任务**: 测试包含delay的协程逻辑
- [ ] **检查点**: 能精确控制协程执行的时间点
- [ ] **文件**: 创建`TimeControlTest.kt`

#### Task 6.2.5: 挂起函数测试 (5分钟) ⏰
- [ ] **学习目标**: 测试简单的suspend function
- [ ] **具体任务**: 创建网络请求模拟并测试
- [ ] **检查点**: 能验证挂起函数的返回值和异常
- [ ] **文件**: 创建`SuspendFunctionTest.kt`

#### Task 6.2.6: Flow基础测试 (5分钟) ⏰
- [ ] **学习目标**: 测试Flow数据流的发射和收集
- [ ] **具体任务**: 使用flowOf和collect测试Flow
- [ ] **检查点**: 能验证Flow发射的数据序列
- [ ] **文件**: 创建`FlowBasicsTest.kt`

#### Task 6.2.7: StateFlow测试 (5分钟) ⏰
- [ ] **学习目标**: 测试StateFlow的状态变化
- [ ] **具体任务**: 验证StateFlow的value变化时序
- [ ] **检查点**: 能测试有状态的响应式数据流
- [ ] **文件**: 创建`StateFlowTest.kt`

#### Task 6.2.8: SharedFlow测试 (5分钟) ⏰
- [ ] **学习目标**: 测试事件发射和多订阅者场景
- [ ] **具体任务**: 验证SharedFlow的热流特性
- [ ] **检查点**: 能测试事件总线类型的数据流
- [ ] **文件**: 创建`SharedFlowTest.kt`

#### Task 6.2.9: 复杂Flow操作测试 (5分钟) ⏰
- [ ] **学习目标**: 测试map、filter、combine等Flow操作符
- [ ] **具体任务**: 组合多个操作符进行集成测试
- [ ] **检查点**: 能验证复杂数据转换逻辑
- [ ] **文件**: 创建`FlowOperatorsTest.kt`

#### Task 6.2.10: 协程取消测试 (5分钟) ⏰
- [ ] **学习目标**: 验证协程的正确取消行为
- [ ] **具体任务**: 测试cancel()调用后的资源清理
- [ ] **检查点**: 能确保协程能正确响应取消信号
- [ ] **文件**: 创建`CoroutineCancellationTest.kt`

#### Task 6.2.11: 异常处理测试 (5分钟) ⏰
- [ ] **学习目标**: 测试协程中的异常传播和处理
- [ ] **具体任务**: 验证try-catch和CoroutineExceptionHandler
- [ ] **检查点**: 能确保异常被正确捕获和处理
- [ ] **文件**: 创建`CoroutineExceptionTest.kt`

#### Task 6.2.12: ViewModel协程测试 (5分钟) ⏰
- [ ] **学习目标**: 测试ViewModel中的协程逻辑
- [ ] **具体任务**: 使用TestDispatcher测试viewModelScope
- [ ] **检查点**: 能验证ViewModel的异步操作
- [ ] **文件**: 创建`ViewModelCoroutineTest.kt`

#### Task 6.2.13: Repository协程测试 (5分钟) ⏰
- [ ] **学习目标**: 测试数据层的协程操作
- [ ] **具体任务**: 模拟网络和数据库的异步操作
- [ ] **检查点**: 能测试复杂的数据获取逻辑
- [ ] **文件**: 创建`RepositoryCoroutineTest.kt`

#### Task 6.2.14: 并发协程测试 (5分钟) ⏰
- [ ] **学习目标**: 测试多个协程的并发执行
- [ ] **具体任务**: 使用async和awaitAll测试并发场景
- [ ] **检查点**: 能验证并发操作的正确性
- [ ] **文件**: 创建`ConcurrentCoroutineTest.kt`

#### Task 6.2.15: 协程测试库设计原理 (5分钟) ⏰
- [ ] **学习目标**: 理解TestCoroutineScheduler的工作机制
- [ ] **具体任务**: 分析虚拟时间调度的实现原理
- [ ] **检查点**: 能解释为什么能控制协程的执行时间
- [ ] **文件**: 创建`coroutine_test_internals.md`

#### Task 6.2.16: 协程测试最佳实践 (5分钟) ⏰
- [ ] **学习目标**: 总结协程测试的经验和陷阱
- [ ] **具体任务**: 整理常见问题和解决方案
- [ ] **检查点**: 能避免协程测试中的常见错误
- [ ] **文件**: 创建`coroutine_testing_best_practices.md`

#### Task 6.2.17: 协程测试面试准备 (5分钟) ⏰
- [ ] **学习目标**: 准备协程测试相关面试问题
- [ ] **具体任务**: 整理测试异步代码的深度问答
- [ ] **检查点**: 能从异步编程角度回答测试问题
- [ ] **文件**: 更新`interview_qa_testing.md`

### 6.3 UI测试：Espresso & Compose Testing (总时长: 90分钟)

#### Task 6.3.1: UI测试策略分析 (5分钟) ⏰
- [ ] **学习目标**: 理解UI测试的价值、成本和适用场景
- [ ] **具体任务**: 分析何时需要UI测试 vs 单元测试
- [ ] **检查点**: 能制定合理的UI测试策略
- [ ] **文件**: 创建`ui_testing_strategy.md`

#### Task 6.3.2: Espresso基础配置 (5分钟) ⏰
- [ ] **学习目标**: 配置Espresso测试环境和依赖
- [ ] **具体任务**: 设置build.gradle和基础测试类
- [ ] **检查点**: 能成功运行第一个Espresso测试
- [ ] **文件**: 配置项目并创建`EspressoBasicsTest.java`

#### Task 6.3.3: ViewMatchers使用 (5分钟) ⏰
- [ ] **学习目标**: 掌握withId、withText等视图匹配器
- [ ] **具体任务**: 创建各种ViewMatcher的使用示例
- [ ] **检查点**: 能精确定位UI元素
- [ ] **文件**: 创建`ViewMatchersTest.java`

#### Task 6.3.4: ViewActions操作 (5分钟) ⏰
- [ ] **学习目标**: 使用click、typeText、swipe等操作
- [ ] **具体任务**: 模拟用户的各种交互行为
- [ ] **检查点**: 能执行复杂的用户操作序列
- [ ] **文件**: 创建`ViewActionsTest.java`

#### Task 6.3.5: ViewAssertions验证 (5分钟) ⏰
- [ ] **学习目标**: 验证UI状态和内容
- [ ] **具体任务**: 使用matches()验证视图属性
- [ ] **检查点**: 能全面验证UI的显示状态
- [ ] **文件**: 创建`ViewAssertionsTest.java`

#### Task 6.3.6: Activity测试 (5分钟) ⏰
- [ ] **学习目标**: 测试单个Activity的UI交互
- [ ] **具体任务**: 创建登录Activity的完整测试
- [ ] **检查点**: 能测试Activity的生命周期和交互
- [ ] **文件**: 创建`LoginActivityTest.java`

#### Task 6.3.7: Fragment测试 (5分钟) ⏰
- [ ] **学习目标**: 在isolation中测试Fragment
- [ ] **具体任务**: 使用FragmentScenario测试Fragment
- [ ] **检查点**: 能独立测试Fragment的功能
- [ ] **文件**: 创建`UserProfileFragmentTest.java`

#### Task 6.3.8: RecyclerView测试 (5分钟) ⏰
- [ ] **学习目标**: 测试列表的滚动、点击、数据展示
- [ ] **具体任务**: 验证RecyclerView的item交互
- [ ] **检查点**: 能测试复杂列表的各种操作
- [ ] **文件**: 创建`RecyclerViewTest.java`

#### Task 6.3.9: 异步UI更新测试 (5分钟) ⏰
- [ ] **学习目标**: 处理网络请求、数据加载等异步场景
- [ ] **具体任务**: 使用IdlingResource同步异步操作
- [ ] **检查点**: 能稳定测试包含异步操作的UI
- [ ] **文件**: 创建`AsyncUITest.java`

#### Task 6.3.10: 网络请求模拟 (5分钟) ⏰
- [ ] **学习目标**: 在UI测试中模拟网络响应
- [ ] **具体任务**: 使用MockWebServer或Hilt测试替换
- [ ] **检查点**: 能控制网络请求的返回结果
- [ ] **文件**: 创建`NetworkMockingTest.java`

#### Task 6.3.11: Compose测试环境 (5分钟) ⏰
- [ ] **学习目标**: 配置Jetpack Compose的测试依赖
- [ ] **具体任务**: 设置ComposeTestRule和基础结构
- [ ] **检查点**: 能运行基础的Compose测试
- [ ] **文件**: 创建`ComposeTestSetup.kt`

#### Task 6.3.12: ComposeTestRule使用 (5分钟) ⏰
- [ ] **学习目标**: 掌握Compose测试的核心API
- [ ] **具体任务**: 使用onNodeWithText、onNodeWithTag等
- [ ] **检查点**: 能定位和操作Compose UI元素
- [ ] **文件**: 创建`ComposeBasicsTest.kt`

#### Task 6.3.13: Compose语义测试 (5分钟) ⏰
- [ ] **学习目标**: 使用语义属性进行accessibility测试
- [ ] **具体任务**: 验证contentDescription、semantics等
- [ ] **检查点**: 能测试UI的可访问性
- [ ] **文件**: 创建`ComposeSemanticsTest.kt`

#### Task 6.3.14: Compose状态测试 (5分钟) ⏰
- [ ] **学习目标**: 测试Compose组件的状态变化
- [ ] **具体任务**: 验证recomposition和状态更新
- [ ] **检查点**: 能测试复杂的状态管理逻辑
- [ ] **文件**: 创建`ComposeStateTest.kt`

#### Task 6.3.15: 端到端用户流程 (5分钟) ⏰
- [ ] **学习目标**: 测试完整的用户使用场景
- [ ] **具体任务**: 创建注册→登录→浏览→购买的完整流程
- [ ] **检查点**: 能验证跨页面的用户流程
- [ ] **文件**: 创建`E2EUserJourneyTest.java`

#### Task 6.3.16: 页面对象模式 (5分钟) ⏰
- [ ] **学习目标**: 使用Page Object模式组织测试代码
- [ ] **具体任务**: 抽象页面操作和元素定位
- [ ] **检查点**: 能写出可维护的UI测试代码
- [ ] **文件**: 创建`LoginPageObject.java`

#### Task 6.3.17: 测试稳定性优化 (5分钟) ⏰
- [ ] **学习目标**: 解决UI测试的flaky问题
- [ ] **具体任务**: 分析和解决测试不稳定的原因
- [ ] **检查点**: 能写出稳定可靠的UI测试
- [ ] **文件**: 创建`ui_test_stability.md`

#### Task 6.3.18: UI测试成本收益 (5分钟) ⏰
- [ ] **学习目标**: 分析UI测试的ROI和维护成本
- [ ] **具体任务**: 制定UI测试的覆盖策略
- [ ] **检查点**: 能平衡测试覆盖率和维护成本
- [ ] **文件**: 创建`ui_testing_roi_analysis.md`

### 6.4 集成测试：Robolectric & Hilt Testing (总时长: 90分钟)

#### Task 6.4.1: 集成测试概念 (5分钟) ⏰
- [ ] **学习目标**: 理解集成测试在测试金字塔中的位置
- [ ] **具体任务**: 分析集成测试 vs 单元测试 vs UI测试
- [ ] **检查点**: 能选择合适的测试层级
- [ ] **文件**: 创建`integration_testing_overview.md`

#### Task 6.4.2: Robolectric环境配置 (5分钟) ⏰
- [ ] **学习目标**: 配置Robolectric在JVM上运行Android测试
- [ ] **具体任务**: 设置robolectric.properties和基础配置
- [ ] **检查点**: 能在JVM上快速运行Android组件测试
- [ ] **文件**: 配置项目并创建`RobolectricSetupTest.java`

#### Task 6.4.3: Activity集成测试 (5分钟) ⏰
- [ ] **学习目标**: 使用Robolectric测试Activity生命周期
- [ ] **具体任务**: 测试Activity的创建、暂停、恢复等状态
- [ ] **检查点**: 能验证Activity在不同状态下的行为
- [ ] **文件**: 创建`ActivityLifecycleTest.java`

#### Task 6.4.4: Service集成测试 (5分钟) ⏰
- [ ] **学习目标**: 测试Service的启动、绑定、通信
- [ ] **具体任务**: 创建Service的完整生命周期测试
- [ ] **检查点**: 能验证Service与其他组件的交互
- [ ] **文件**: 创建`ServiceIntegrationTest.java`

#### Task 6.4.5: BroadcastReceiver测试 (5分钟) ⏰
- [ ] **学习目标**: 测试广播接收和处理逻辑
- [ ] **具体任务**: 模拟系统广播和自定义广播
- [ ] **检查点**: 能验证广播的正确接收和响应
- [ ] **文件**: 创建`BroadcastReceiverTest.java`

#### Task 6.4.6: ContentProvider测试 (5分钟) ⏰
- [ ] **学习目标**: 测试数据提供者的CRUD操作
- [ ] **具体任务**: 验证ContentProvider的增删改查
- [ ] **检查点**: 能测试跨应用的数据共享
- [ ] **文件**: 创建`ContentProviderTest.java`

#### Task 6.4.7: Shadow对象使用 (5分钟) ⏰
- [ ] **学习目标**: 理解Robolectric的Shadow机制
- [ ] **具体任务**: 使用ShadowActivity、ShadowApplication等
- [ ] **检查点**: 能控制Android框架的行为
- [ ] **文件**: 创建`ShadowObjectsTest.java`

#### Task 6.4.8: 自定义Shadow (5分钟) ⏰
- [ ] **学习目标**: 创建自定义Shadow类模拟系统行为
- [ ] **具体任务**: 为第三方库创建Shadow实现
- [ ] **检查点**: 能扩展Robolectric的模拟能力
- [ ] **文件**: 创建`CustomShadowTest.java`

#### Task 6.4.9: Hilt测试基础配置 (5分钟) ⏰
- [ ] **学习目标**: 配置Hilt用于依赖注入测试
- [ ] **具体任务**: 设置@HiltAndroidTest和测试规则
- [ ] **检查点**: 能在测试中使用依赖注入
- [ ] **文件**: 创建`HiltTestSetup.java`

#### Task 6.4.10: 测试模块替换 (5分钟) ⏰
- [ ] **学习目标**: 使用@TestInstallIn替换生产模块
- [ ] **具体任务**: 创建测试专用的依赖提供模块
- [ ] **检查点**: 能为测试提供Mock依赖
- [ ] **文件**: 创建`TestModuleExample.java`

#### Task 6.4.11: Repository集成测试 (5分钟) ⏰
- [ ] **学习目标**: 测试Repository与真实数据源的集成
- [ ] **具体任务**: 结合Room和网络层测试数据流
- [ ] **检查点**: 能验证完整的数据获取链路
- [ ] **文件**: 创建`RepositoryIntegrationTest.java`

#### Task 6.4.12: Room数据库测试 (5分钟) ⏰
- [ ] **学习目标**: 使用内存数据库测试Room操作
- [ ] **具体任务**: 测试DAO的增删改查和复杂查询
- [ ] **检查点**: 能验证数据库操作的正确性
- [ ] **文件**: 创建`RoomDatabaseTest.java`

#### Task 6.4.13: 数据库迁移测试 (5分钟) ⏰
- [ ] **学习目标**: 测试Room数据库的版本迁移
- [ ] **具体任务**: 验证Migration的数据完整性
- [ ] **检查点**: 能确保数据库升级不丢失数据
- [ ] **文件**: 创建`DatabaseMigrationTest.java`

#### Task 6.4.14: 网络层集成测试 (5分钟) ⏰
- [ ] **学习目标**: 测试网络请求和响应处理
- [ ] **具体任务**: 使用MockWebServer模拟网络交互
- [ ] **检查点**: 能验证网络层的完整功能
- [ ] **文件**: 创建`NetworkLayerTest.java`

#### Task 6.4.15: 缓存机制测试 (5分钟) ⏰
- [ ] **学习目标**: 测试多层缓存的读写逻辑
- [ ] **具体任务**: 验证内存缓存、磁盘缓存的一致性
- [ ] **检查点**: 能确保缓存策略的正确实现
- [ ] **文件**: 创建`CacheIntegrationTest.java`

#### Task 6.4.16: 测试环境隔离 (5分钟) ⏰
- [ ] **学习目标**: 确保测试之间的独立性
- [ ] **具体任务**: 实现测试数据的清理和重置
- [ ] **检查点**: 能避免测试之间的相互影响
- [ ] **文件**: 创建`TestIsolationExample.java`

#### Task 6.4.17: 测试类型选择策略 (5分钟) ⏰
- [ ] **学习目标**: 分析何时使用不同类型的测试
- [ ] **具体任务**: 制定项目的测试策略矩阵
- [ ] **检查点**: 能为不同场景选择最适合的测试方法
- [ ] **文件**: 创建`testing_strategy_matrix.md`

#### Task 6.4.18: 测试金字塔实践 (5分钟) ⏰
- [ ] **学习目标**: 在实际项目中应用测试金字塔原理
- [ ] **具体任务**: 分析当前项目的测试分布和优化建议
- [ ] **检查点**: 能建立高效的测试体系
- [ ] **文件**: 创建`testing_pyramid_implementation.md`

---

### 📋 第六章检验点问题

1. **测试设计哲学**: 如何在测试覆盖率、执行速度、维护成本之间找到平衡？
2. **Mock策略**: 什么时候使用Mock，什么时候使用真实对象？如何避免过度Mocking？
3. **协程测试**: 如何测试复杂的异步操作链？如何处理协程的取消和异常？
4. **UI测试稳定性**: 如何解决UI测试的flaky问题？如何设计可维护的UI测试？
5. **集成测试边界**: 如何定义集成测试的范围？如何平衡集成测试和单元测试？
6. **测试驱动开发**: TDD在Android开发中的实践挑战和解决方案？
7. **测试自动化**: 如何构建高效的测试自动化流水线？
8. **性能测试**: 如何测试Android应用的性能指标？
9. **测试数据管理**: 如何设计和管理测试数据？如何处理测试环境的数据一致性？
10. **代码覆盖率**: 代码覆盖率的合理目标是什么？如何提高有效覆盖率？



## 🏆 第六章总进度跟踪
**总计**: 第六章包含70个微任务，总学习时长350分钟（约5.8小时），涵盖Android测试的完整技能体系。
- **6.1 单元测试基石：JUnit & Mockito**: 0/84 tasks (预计完成时间: 85分钟)

## 🎯 全书总进度统计
- **第六章 测试利刃**: 0/70 tasks (5.8小时)

- **全书总计**: 

## 📝 学习节奏建议
- 每个Phase完成后休息10分钟
- 每完成一个大主题进行全面checkpoint
- 发现不理解的概念立即深入讨论
- 代码必须亲手实现，不能只看不写
- 保持ADHD友好的学习节奏！
- 保持初学者友好，难度循序渐进，讲解详细

## 🎓 课程完成指南
恭喜！你现在拥有了完整的Android面试知识体系：


你已准备好迎接任何Android技术面试挑战！🚀