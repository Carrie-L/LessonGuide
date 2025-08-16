# synchronized机制学习笔记

## 1.1.3 synchronized基本原理

### Monitor锁机制

synchronized的核心是**Monitor（监视器）**机制：

#### 核心概念
- **每个Java对象都有一个内置的monitor锁**
- Monitor是JVM层面的同步原语
- 同一时刻只有一个线程能持有某个对象的monitor
- 其他线程必须在等待队列中等待

#### Monitor结构
```
ObjectMonitor {
    _owner: 当前持有锁的线程
    _count: 重入次数计数器  
    _waiters: 等待队列
    _recursions: 递归调用深度
}
```

### synchronized使用方式

#### 1. 同步方法
```java
// 实例方法：锁定this对象
public synchronized void instanceMethod() {
    // 等价于 synchronized(this) { ... }
}

// 静态方法：锁定Class对象
public static synchronized void staticMethod() {
    // 等价于 synchronized(MyClass.class) { ... }
}
```

#### 2. 同步代码块
```java
// 锁定this对象
synchronized(this) {
    // 临界区代码
}

// 锁定指定对象
Object lock = new Object();
synchronized(lock) {
    // 使用专门的锁对象
}

// 锁定类对象
synchronized(MyClass.class) {
    // 类级别的同步
}
```

### Monitor工作流程

1. **获取(Enter)**
   - 线程尝试获取对象的monitor
   - 如果monitor空闲，获取成功并设置owner
   - 如果monitor被占用，进入等待队列

2. **执行(Own)**
   - 成功获取monitor的线程执行临界区代码
   - 支持重入：同一线程可以多次获取同一monitor

3. **释放(Exit)**
   - 方法正常结束或抛出异常时自动释放
   - 重入计数减1，为0时真正释放monitor

4. **通知(Notify)**
   - 释放monitor后，通知等待队列中的线程
   - 等待线程重新竞争monitor

### 重入性支持

```java
public synchronized void outerMethod() {
    System.out.println("外层方法");
    innerMethod();  // 同一线程可以再次获取锁
}

public synchronized void innerMethod() {
    System.out.println("内层方法");  // 重入成功
}
```

### synchronized的特性

#### 1. 互斥性(Mutual Exclusion)
- 同一时刻只有一个线程能执行同步代码

#### 2. 可见性(Visibility)  
- 释放锁时，修改的变量对后续获取锁的线程可见
- 利用happens-before规则：unlock happens-before lock

#### 3. 原子性(Atomicity)
- 同步块内的操作要么全部执行，要么全部不执行

#### 4. 有序性(Ordering)
- 禁止编译器和处理器对同步块内外的代码重排序

### 为什么每个对象都有monitor？

#### 设计优势
1. **细粒度锁定**: 可以针对不同对象进行独立的同步控制
2. **避免全局锁**: 不同对象的同步操作不会相互阻塞
3. **面向对象设计**: 锁与数据封装在一起，符合OOP原则
4. **灵活性**: 程序员可以选择合适的锁对象

#### 实际应用
```java
public class BankAccount {
    private double balance;
    
    // 每个账户对象都有独立的锁
    public synchronized void withdraw(double amount) {
        if (balance >= amount) {
            balance -= amount;
        }
    }
    
    public synchronized void deposit(double amount) {
        balance += amount;
    }
}

// 不同账户可以并发操作，互不影响
BankAccount account1 = new BankAccount();
BankAccount account2 = new BankAccount();
// account1和account2的操作可以并发执行
```

### 关键理解点

1. **synchronized不仅仅是加锁**，它提供了完整的内存可见性保证
2. **对象级别的锁设计**让Java的并发控制更加灵活和高效
3. **自动释放机制**避免了手动管理锁的复杂性和错误
4. **重入性支持**让递归调用和方法调用链更加自然

---
**完成时间**: [待填写]
**理解程度**: ⭐⭐⭐⭐⭐ (1-5星)
**需要复习**: [ ] 是 [ ] 否