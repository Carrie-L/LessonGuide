# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Repository Overview

This is an interview preparation repository focused on Android development. The repository contains comprehensive study materials in Chinese for Android technical interviews.

## Primary Content

- **android_interview.md**: A comprehensive guide structured as "首席面试官的剧本：解构安卓面试'八股文'" (Chief Interviewer's Script: Deconstructing Android Interview Topics)
  - Covers fundamental Android concepts from a senior interviewer's perspective
  - Includes detailed explanations of Java/Kotlin, Android framework, architecture patterns, third-party libraries, performance optimization, and modern Android UI development
  - Contains real interview questions and follow-up examples
  - Structured in 5 main chapters covering progressively advanced topics

## Key Study Areas Covered

1. **基石篇 (Foundation)**: Java & Kotlin fundamentals including concurrency, collections, and coroutines
2. **支柱篇 (Pillars)**: Android framework internals including ART runtime, Activity lifecycle, UI rendering, event handling, Handler/Looper, and Binder IPC
3. **蓝图篇 (Architecture)**: High-level architecture patterns (MVC, MVP, MVVM, MVI, Clean Architecture) and third-party library internals (OkHttp, Glide, ARouter)
4. **淬炼篇 (Refinement)**: Performance and stability including ANR diagnosis, OOM analysis, and APK building
5. **地平线 (Horizon)**: Modern Android UI with Jetpack Compose

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
- **MICRO_TASKS.md**: Detailed breakdown of all learning topics into 5-minute micro-tasks
- **PROGRESS.md**: High-level learning roadmap with progress tracking
- **student_progress/**: Directory containing all student work, code implementations, and notes

### Learning Methodology
1. **Micro-Task Approach**: Each task takes exactly 5 minutes to maintain ADHD-friendly focus
2. **Theory + Practice + Interview**: Every topic includes theoretical understanding, hands-on coding, and interview preparation
3. **Progress Tracking**: Checkbox system to track completion and maintain motivation
4. **Checkpoint Testing**: Regular knowledge verification through targeted questions
5. **Tutor Interaction**: AI tutor acts as "首席面试官" providing guidance and assessment

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

### Usage Instructions for Claude Code
When resuming learning sessions:
1. Check `MICRO_TASKS.md` for current progress and next tasks
2. Verify student work in `student_progress/` directory
3. Act as supportive tutor providing explanations and checkpoint questions
4. Maintain ADHD-friendly 5-minute task duration
5. Update progress tracking as tasks are completed
6. Provide encouragement and celebrate small wins

### Student Progress Monitoring
- All code implementations should be in `student_progress/` directory
- Notes and summaries should be in markdown format
- Interview Q&A preparations should be consolidated in `interview_qa.md`
- Regular checkpoint questions ensure true understanding before proceeding

## Notes

- Content is in Chinese and specifically tailored for Android development interviews
- Includes references to industry best practices from major tech companies
- Contains detailed explanations of underlying principles rather than just surface-level knowledge
- ADHD-friendly micro-learning approach with 5-minute focused tasks
- Comprehensive practical coding exercises alongside theoretical learning