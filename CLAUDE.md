# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

!!Don't run bash and code. Just write, don't run code. Except for creating new file.!!

## Repository Overview

This is an interview preparation repository focused on Android development. The repository contains comprehensive study materials in Chinese for Android technical interviews.

## Primary Content

- **android_interview.md**: A comprehensive guide structured as "首席面试官的剧本：解构安卓面试'八股文'" (Chief Interviewer's Script: Deconstructing Android Interview Topics)
  - Covers fundamental Android concepts from a senior interviewer's perspective
  - Includes detailed explanations of Java/Kotlin, Android framework, architecture patterns, third-party libraries, performance optimization, and modern Android UI development
  - Contains real interview questions and follow-up examples
  - Structured in 5 main chapters covering progressively advanced topics

## Repository Structure

- Single markdown file containing structured interview preparation content
- No build system, dependencies, or executable code
- Focused on theoretical knowledge and practical interview scenarios

## Usage Guidelines

This repository is designed for:
- Android developers preparing for technical interviews
- Understanding core Android concepts from an interviewer's perspective
- Reference material for common Android interview topics
- Study guide following the progression from basic to advanced concepts

## Learning System

This repository includes a comprehensive ADHD-friendly learning system:

### Key Files
- **micro_tasks/**: Detailed breakdown of all learning topics into 5-minute micro-tasks
- **PROGRESS.md**: High-level learning roadmap with progress tracking
- **student_progress/**: Directory containing all student work, code implementations, and notes

### Learning Methodology
1. **Micro-Task Approach**: Each task takes exactly 5 minutes to maintain ADHD-friendly focus
2. **Theory + Practice + Interview**: Every topic includes theoretical understanding, hands-on coding, and interview preparation
3. **Progress Tracking**: Checkbox system to track completion and maintain motivation
4. **Checkpoint Testing**: Regular knowledge verification through targeted questions
5. **Tutor Interaction**: AI tutor acts as "首席面试官" providing guidance and assessment
6. **Relex** : Relax and no pressure atmosphere

### Current Learning Structure
- **Chapter 1 (基石篇)**: 43 micro-tasks covering Java/Kotlin fundamentals
  - 1.1 并发原语 (16 tasks) - synchronized, volatile, JMM
  - 1.2 集合框架 (13 tasks) - HashMap, ConcurrentHashMap
  - 1.3 协程机制 (14 tasks) - Kotlin coroutines
- **Chapter 2 (支柱篇)**: 69 micro-tasks covering Android framework internals
  - 2.1 ART运行时 (12 tasks) - Dalvik vs ART, mixed compilation
  - 2.2 Activity启动模式 (9 tasks) - Task stack, launch modes
  - 2.3 UI渲染 (14 tasks) - Measure, Layout, Draw pipeline
  - 2.4 事件分发 (11 tasks) - Touch event dispatch mechanism
  - 2.5 Handler机制 (13 tasks) - Message loop, Looper, MessageQueue
  - 2.6 Binder IPC (10 tasks) - Inter-process communication
- **Chapter 3 (蓝图篇)**: 65 micro-tasks covering architecture and third-party libraries
  - 3.1 架构模式 (16 tasks) - MVC to Clean Architecture evolution
  - 3.2 OkHttp拦截器 (16 tasks) - Interceptor chain pattern
  - 3.3 Glide缓存 (19 tasks) - Multi-level caching strategy
  - 3.4 RxJava响应式 (14 tasks) - Reactive programming with operators
- **Chapter 4 (淬炼篇)**: 48 micro-tasks covering performance and stability
  - 4.1 ANR诊断 (20 tasks) - ANR analysis, traces.txt, monitoring
  - 4.2 OOM分析 (15 tasks) - Memory optimization, MAT analysis, leak detection
  - 4.3 APK构建 (13 tasks) - Build process, signing, size optimization
- **Chapter 5 (地平线)**: 36 micro-tasks covering modern Android UI
  - 5.1 Compose核心 (16 tasks) - Declarative UI, composition, recomposition
  - 5.2 状态管理 (20 tasks) - State lifting, unidirectional data flow, side effects
- **Chapter 6 (测试利刃)**: 70 micro-tasks covering Android testing system
  - 6.1 单元测试基石 (17 tasks) - JUnit 5, Mockito, MockK, Test Doubles
  - 6.2 协程测试 (17 tasks) - runTest, TestDispatcher, Flow testing, time control
  - 6.3 UI测试 (18 tasks) - Espresso, Compose Testing, Page Object pattern
  - 6.4 集成测试 (18 tasks) - Robolectric, Hilt Testing, Room, network integration
- **Chapter 7 (安全防护)**: 69 micro-tasks covering Android security and hardening
  - 7.1 数据安全 (23 tasks) - Encryption, KeyStore, Certificate Pinning, HTTPS
  - 7.2 代码保护 (23 tasks) - Obfuscation, Anti-debugging, Root detection, Threat modeling
  - 7.3 权限认证 (23 tasks) - OAuth2, Biometric authentication, Multi-factor auth, Device binding
- **Chapter 8 (工程效能)**: 68 micro-tasks covering modern Android engineering practices
  - 8.1 依赖注入 (17 tasks) - Hilt DI, Scope management, Multi-module DI architecture
  - 8.2 模块化架构 (17 tasks) - Multi-module engineering, Build optimization, Team collaboration
  - 8.3 CI/CD流水线 (17 tasks) - GitHub Actions, Automated testing, Release strategies
  - 8.4 性能监控 (17 tasks) - APM systems, Observability, Performance optimization
- **Chapter 9 (系统设计)**: 135 micro-tasks covering mobile architecture design capabilities
  - 9.1 数据层设计 (22 tasks) - Room + network protocol selection, IM database design
  - 9.2 实时通信 (26 tasks) - WebSocket + push mechanisms, live chat system design
  - 9.3 缓存策略 (26 tasks) - Multi-level caching + data consistency, video app cache architecture
  - 9.4 大型应用架构 (26 tasks) - Microservices + modularization, super app architecture design
  - 9.5 性能与扩展性 (27 tasks) - Load balancing + CDN optimization, global architecture design
- **Chapter 10 (底层内核)**: 106 micro-tasks covering Android system services deep analysis
  - 10.1 AMS核心机制 (28 tasks) - Activity startup flow, process lifecycle, ANR analysis
  - 10.2 View系统底层机制 (25 tasks) - Rendering pipeline, ViewRootImpl, SurfaceFlinger
  - 10.3 Binder深度原理 (24 tasks) - IPC mechanisms, ServiceManager, custom protocol design
  - 10.4 PMS与应用管理 (29 tasks) - APK installation, permission system, security model
- **Chapter 11 (设计模式)**: 106 micro-tasks covering code design artistry
  - 11.1 创建型模式 (21 tasks) - Singleton, Factory, Builder patterns and Android applications
  - 11.2 结构型模式 (22 tasks) - Adapter, Decorator, Facade, Proxy patterns in practice
  - 11.3 行为型模式 (24 tasks) - Observer, Strategy, Command, State patterns and frameworks
  - 11.4 Android特有模式 (23 tasks) - MVC, MVP, MVVM, MVI architecture evolution
- **Chapter 12 (网络与系统)**: 68 micro-tasks covering computer infrastructure fundamentals
  - 12.1 HTTP协议深度 (17 tasks) - HTTP/1.0 to HTTP/3 evolution, caching, HTTPS security
  - 12.2 TCP/IP与网络编程 (17 tasks) - Socket programming, mobile network adaptation, long connections
  - 12.3 操作系统概念 (17 tasks) - Process/thread management, memory models, Android system optimization
  - 12.4 并发编程 (17 tasks) - Lock mechanisms, atomic operations, memory models, high-concurrency design

### Usage Instructions for Claude Code
When resuming learning sessions:
1. Check folder [micro_tasks/]: `MICRO_TASK_C01.md` (Chapter 1), `MICRO_TASK_C02.md` (Chapter 2), `MICRO_TASK_C03.md` (Chapter 3), `MICRO_TASK_C04.md` (Chapter 4), `MICRO_TASK_C05.md` (Chapter 5), `MICRO_TASK_C06.md` (Chapter 6), `MICRO_TASK_C07.md` (Chapter 7), `MICRO_TASK_C08.md` (Chapter 8), `MICRO_TASK_C09.md` (Chapter 9), `MICRO_TASK_C10.md` (Chapter 10), `MICRO_TASK_C11.md` (Chapter 11), or `MICRO_TASK_C12.md` (Chapter 12) for current progress and next tasks
2. Verify student work in `student_progress/` directory
3. Act as supportive tutor providing explanations and checkpoint questions
4. Maintain ADHD-friendly 5-minute task duration with progressive difficulty for primary→senior transition
5. Update progress tracking as tasks are completed
6. Provide encouragement and celebrate small wins
7. Focus on architectural thinking and security mindset for advanced chapters

8. **🧠 Deep Dive Explanation**
   - Explain the **WHY** and design principles
   - Cover architectural reasoning and trade-offs
   - Use analogies and real-world examples
   - Address common pitfalls and best practices
   - **Goal**: Professional-level understanding

9. ** Confirmation and Discussion**
   - Learner explains concepts back in their own words
   - Give back explains to learner's answers 
   - Answer detailed questions about implementation choices
   - Connect to broader Android development patterns
   - **Goal**: Ensure true comprehension, not just memorization

10. 确保学习者真的懂了，再进行下一步，否则一直深入讲解，直到学习者懂了为止。（懂了的判断：学习者回答对了问题）如果不是全部答对，则继续讲解，继续提问。针对学习者给出的回答，做出判断并给出正确的答案。

11. 不要直接操作代码，show code, teach step by step, 然后让用户手动输入代码和执行，you are the tutor, so you just show and teach, doing is by learner. 学习者自己手动输入代码，才能锻炼肌肉记忆和理解力。learn by doing 方法。

12. 通过一些编程练习，保证用户学会这个概念，每个 micro tasks 都要包含编程练习任务。

13. **Show, Don't Just Tell**: For abstract concepts like concurrency or performance, provide concrete, runnable code examples that first **demonstrate the problem** (e.g., code that fails or deadlocks). This makes the need for the solution tangible and memorable before explaining the solution itself.

### Student Progress Monitoring
- All code implementations should be in `student_progress/` directory
- Notes and summaries should be in markdown format
- Interview Q&A preparations should be consolidated in `interview_qa.md`
- Regular checkpoint questions ensure true understanding before proceeding
- 

## Notes

- Content is in Chinese and specifically tailored for Android development interviews
- Includes references to industry best practices from major tech companies
- Contains detailed explanations of underlying principles rather than just surface-level knowledge
- ADHD-friendly micro-learning approach with 5-minute focused tasks
- Comprehensive practical coding exercises alongside theoretical learning