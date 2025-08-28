# 线程生命周期与volatile机制详解

## 🔄 完整执行流程

### 情况A: 无volatile (可见性问题)
```
主线程                    Reader线程                Writer线程
  │                         │                         │
  ├─reader.start()         ├─开始执行                   │
  ├─writer.start()         ├─while(!flag) [flag=false] │
  │                        ├─继续循环...                │
  │                        │                         ├─sleep(1000)
  │                        │                         ├─flag=true (写入主内存)
  │                        ├─while(!flag) [仍读取false] ├─输出"写入者..."
  │                        ├─继续循环...                │
  ├─sleep(3000)           ├─继续循环...                ├─结束
  ├─isAlive()=true        ├─仍在循环...                │
  ├─输出"可见性问题"        ├─仍在循环...                │
  └─interrupt()           └─被中断                    │
```

### 情况B: 使用volatile (正常情况)
```
主线程                    Reader线程                Writer线程  
  │                         │                         │
  ├─reader.start()         ├─开始执行                   │
  ├─writer.start()         ├─while(!flag) [flag=false] │
  │                        ├─继续循环...                │
  │                        │                         ├─sleep(1000)
  │                        │                         ├─volatile flag=true
  │                        ├─while(!flag) [读取true]   ├─输出"写入者..."
  │                        ├─退出while循环              │
  │                        ├─输出"reader end."         ├─结束
  │                        ├─线程结束                  │
  ├─sleep(3000)           │                         │
  ├─isAlive()=false       │                         │
  └─输出"ok"               │                         │
```

## 🎯 关键理解点

1. **线程自动结束**: 当while条件为false时，线程自然执行完run()方法而结束
2. **volatile保证可见性**: 确保reader能看到writer的修改
3. **isAlive()检测**: 判断线程是否被困在循环中
4. **无需join()**: 我们要检测问题，不是等待解决

## 💡 volatile vs 普通变量

| 变量类型 | 读取方式 | 可见性 | 结果 |
|---------|---------|--------|------|
| 普通变量 | 从CPU缓存 | 可能过期 | 可能无限循环 |
| volatile变量 | 从主内存 | 立即同步 | 正常结束 |