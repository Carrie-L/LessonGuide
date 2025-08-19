package com.concurrency.c01;

// 练习目标：通过代码验证 Happens-before 规则的力量
public class HappensBeforeDemo {
    // 共享变量
    private static int sharedData = 0;
    private static boolean dataReady = false;

    // 用于修复问题的volatile变量
    private static volatile boolean volatileDataReady = false;

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== 实验1: 没有 happens-before 保证，可能出现指令重排 ===");
        // 在这个实验中，我们可能会看到 dataReady=true 了，但sharedData还是0！
        for (int i = 0; i < 1000; i++) {
            runTestWithoutHappensBefore();
        }

        Thread.sleep(2000); // 等待输出

        System.out.println("\n=== 实验2：使用 volatile 建立happens-before关系 ===");
        // 在这个实验中，只要我们看到volatileDataReady=true, 就一定能看到 sharedData=42
        for (int i = 0; i < 100; i++) {
            runTestWithVolatile();
        }
    }

    private static void runTestWithoutHappensBefore() throws InterruptedException {
        // 重置状态
        sharedData = 0;
        dataReady = false;

        // 写入者线程
        Thread writer = new Thread(() -> {
            // 这两行代码没有 Happens-before 关系，可能被重排序
            // 编译器或cpu可能会为了优化，先执行 dataReady=true
            sharedData = 42;
            dataReady = true;
        });

        // 读取者线程
        Thread reader = new Thread(() -> {
            // 等待数据准备好
            while (!dataReady) {
                // 让出CPU，给writer线程执行的机会
                Thread.yield();
            }
            // 当循环退出时，dataReady = true
            // 但因为没有 Happens-before 保证，此时的sharedData可能是0
            if (sharedData == 0) {
                System.out.println("问题重现！dataReady=true, 但sharedData仍然是0！");
            } else {
                System.out.println("实验1： dataReady=true, sharedData = " + sharedData);
            }
        });

        reader.start();
        Thread.sleep(1); // 稍微延迟一下，让reader线程先进入while循环等待，这能增加writer线程被重排序抓住的机会
        writer.start();

        reader.join();
        writer.join();
    }

    // 任务： 实现这个方法
    private static void runTestWithVolatile() throws InterruptedException {
        // 重置状态
        sharedData = 0;
        volatileDataReady = false;

        // 写入者线程
        Thread writer = new Thread(() -> {
            // 对volatile变量的写操作，会确保之前的普通写操作不会被重排序到它之后
            sharedData = 42; // 1. 普通写
            volatileDataReady = true; // 2. volatile写（内存屏障）
        });



        // 读取者线程
        Thread reader = new Thread(() -> {
            // 等待volatile变量
            while (!volatileDataReady) {
                // 3. volatile读（内存屏障）
            }
            // 对volatile变量的读，会确保我们能看到所有在volatile写之前的操作结果
            // 所以，这里的sharedData一定是42
            if (sharedData == 0) {
                System.out.println("逻辑错误！ volatile 保证了可见性，这里不应该发生！");
            }else {
                System.out.println("实验2： dataReady=true, sharedData = " + sharedData);
            }
        });

        reader.start();
        writer.start();

        reader.join();
        writer.join();
    }
}
