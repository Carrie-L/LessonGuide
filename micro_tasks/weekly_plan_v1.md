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
