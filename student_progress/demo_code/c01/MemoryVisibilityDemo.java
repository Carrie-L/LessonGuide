/**
 * JMM内存可见性问题演示
 * 
 * 学习目标：理解Java内存模型(JMM)中的可见性问题
 * 
 * 关键概念：
 * 1. 主内存 vs 工作内存：线程有自己的工作内存副本
 * 2. 可见性问题：一个线程修改的值，其他线程可能看不到
 * 3. JMM规则：没有同步机制时，不保证内存可见性
 */
public class MemoryVisibilityDemo {
    
    // 普通变量 - 可能存在可见性问题
    private static boolean flag = false;
    private static int counter = 0;
    
    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== JMM内存可见性问题演示 ===");
        
        // 读取者线程 - 等待flag变为true
        Thread readerThread = new Thread(() -> {
            System.out.println("读取者线程: 开始等待flag变为true...");
            
            // 这里可能会永远等待！
            // 因为写入者线程对flag的修改可能对读取者线程不可见
            while (!flag) {
                // 空循环等待
                // 在某些JVM实现中，这个循环可能永远不会退出
            }
            
            System.out.println("读取者线程: 检测到flag=true, counter=" + counter);
        }, "ReaderThread");
        
        // 写入者线程 - 修改共享变量
        Thread writerThread = new Thread(() -> {
            try {
                // 等待1秒确保读取者线程先启动
                Thread.sleep(1000);
                
                // 修改共享变量
                counter = 42;
                flag = true;  // 这个修改可能对读取者线程不可见！
                
                System.out.println("写入者线程: 已设置counter=" + counter + ", flag=" + flag);
                
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }, "WriterThread");
        
        // 启动线程
        readerThread.start();
        writerThread.start();
        
        // 等待写入者线程完成
        writerThread.join();
        
        // 等待3秒看读取者线程是否能检测到变化
        Thread.sleep(3000);
        
        if (readerThread.isAlive()) {
            System.out.println("⚠️  可见性问题出现: 读取者线程仍在等待!");
            System.out.println("   主内存中flag=" + flag + ", 但读取者线程看不到这个变化");
            
            // 强制中断读取者线程
            readerThread.interrupt();
        }
        
        System.out.println("\n=== 问题分析 ===");
        System.out.println("1. 写入者线程在自己的工作内存中修改了flag=true");
        System.out.println("2. 但这个修改没有及时同步到主内存");
        System.out.println("3. 或者读取者线程没有从主内存重新读取flag的值");
        System.out.println("4. 导致读取者线程一直看到flag=false的旧值");
        
        System.out.println("\n=== 解决方案预告 ===");
        System.out.println("- 使用volatile关键字保证可见性");
        System.out.println("- 使用synchronized同步块");
        System.out.println("- 使用java.util.concurrent包中的原子类");
    }
}