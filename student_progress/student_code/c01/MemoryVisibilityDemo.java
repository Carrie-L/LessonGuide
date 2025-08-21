public class MemoryVisibilityDemo {
    private static volatile boolean flag = false;
    private static int counter = 0;

    /**
     *   正常情况的时间线：
     *   0ms:    reader线程启动，进入while(!flag)循环
     *   1000ms: writer线程醒来，设置counter=42，flag=true
     *   1001ms: reader线程应该看到flag=true，退出while循环，线程结束
     *   3000ms: 主线程检查，reader.isAlive()应该返回false
     *
     *   可见性问题发生的时间线：
     *   0ms:    reader线程启动，进入while(!flag)循环
     *   1000ms: writer线程设置counter=42，flag=true（写入主内存）
     *   1001ms: ❌ reader线程的工作内存中flag仍然是false！
     *   1002ms: ❌ reader继续在while(!flag)中循环...
     *   1003ms: ❌ reader继续在while(!flag)中循环...
     *   3000ms: ❌ reader.isAlive()返回true - 问题确认！
     *
     */
    public static void main(String[] args) throws InterruptedException {
        Thread reader = new Thread(() -> {
            while (!flag) {
//                System.out.println(" in reader while");
            }
            System.out.println("reader end.");
        });

        Thread writer = new Thread(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            counter = 42;
            flag = true;
            System.out.println("写入者: 已设置flag=true, counter=" + counter);
        });

        reader.start();
        writer.start();

        Thread.sleep(3000);  // 给reader和writer 3秒时间运行
        if (reader.isAlive()) {   // 检查reader是否还在运行
            // 如果还在运行，说明它被困在while循环中了！ 说明 writer 进程修改的flag  对 reader不可见，循环才没有停止
            System.out.println("⚠️  JMM可见性问题重现!");
            System.out.println("🔍 原因: flag的修改对读取者线程不可见");
            reader.interrupt();
        } else {
            System.out.println("ok");
        }

//        reader.join();
        writer.join();


    }


}