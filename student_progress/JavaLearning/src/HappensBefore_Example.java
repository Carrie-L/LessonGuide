public class HappensBefore_Example {

    private volatile boolean isRunning = true;
    private int count = 0;

    // 线程A执行这个方法
    public void work() {
        System.out.println("A:子线程启动，开始循环...");
        while (isRunning) {
            count++;
        }
        System.out.println("A:子线程循环结束，count = " + count);
    }

    // 线程B执行这个方法
    public void stopWork() {
        System.out.println("B:主线程尝试停止子线程...");
        isRunning = false;
        System.out.println("B:主线程已将 isRunning 设置为 " + isRunning);
    }

    public static void main(String[] args) throws InterruptedException {
        HappensBefore_Example demo = new HappensBefore_Example();

        // 创建并启动子线程
        Thread workerThread = new Thread(demo::work);
        workerThread.start();

        // 让子线程先运行1s
        Thread.sleep(1000);

        // 在主线程中停止子线程
        demo.stopWork();

        // 等待子线程结束
        workerThread.join(1000); // 最多再等1s

        if (workerThread.isAlive()) {
            System.out.println("!!!警告： 1秒后，子线程依然在运行，可见性问题复现 ！！！");
        } else {
            System.out.println("程序正常结束。");
        }

    }


}
