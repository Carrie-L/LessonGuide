// 演示代码: JMM内存可见性问题
// 目标: 学员通过观察现象理解可见性问题的根本原因
public class MemoryVisibilityDemo {
    
    // 关键点: 不使用volatile修饰，观察可见性问题
    private static boolean flag = false;
    private static int counter = 0;
    
    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== JMM可见性问题演示 ===");
        
        // 线程1: 读取者 - 等待flag变为true
        Thread readerThread = new Thread(() -> {
            System.out.println("读取者: 开始等待flag变为true...");
            while (!flag) {
                // 空循环 - 可能永远等待下去!
                // 这里演示了JMM的可见性问题
            }
            System.out.println("读取者: 检测到flag为true, counter=" + counter);
        }, "ReaderThread");
        
        // 线程2: 写入者 - 修改共享变量
        Thread writerThread = new Thread(() -> {
            try {
                Thread.sleep(1000);  // 等待1秒
                counter = 42;        // 先修改counter
                flag = true;         // 再修改flag
                System.out.println("写入者: 已设置flag=true, counter=" + counter);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }, "WriterThread");
        
        // 启动线程
        readerThread.start();
        writerThread.start();
        
        // 等待3秒，观察是否出现死循环
        Thread.sleep(3000);
        if (readerThread.isAlive()) {
            System.out.println("⚠️  JMM可见性问题重现! 读取者线程仍在等待");
            System.out.println("🔍 原因: flag的修改对读取者线程不可见");
            System.out.println("💡 解决方案: 使用volatile关键字");
            readerThread.interrupt();
        }
        
        writerThread.join();
        System.out.println("实验结束");
    }
}