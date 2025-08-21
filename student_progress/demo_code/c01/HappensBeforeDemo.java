/**
 * Happens-Before关系演示
 * 
 * 学习目标：理解happens-before关系如何建立内存可见性保证
 * 
 * 关键概念：
 * 1. Happens-Before关系：如果A happens-before B，那么A的结果对B可见
 * 2. Volatile写-读：volatile写操作 happens-before 后续的volatile读操作
 * 3. 传递性：如果A happens-before B，B happens-before C，那么A happens-before C
 */
public class HappensBeforeDemo {
    
    // 实验1：无同步版本 - 可能出现数据竞争
    private static int data = 0;
    private static boolean ready = false;
    
    // 实验2：volatile版本 - 建立happens-before关系
    private static int volatileData = 0;
    private static volatile boolean volatileReady = false;
    
    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== Happens-Before关系验证实验 ===");
        
        // 实验1：展示数据竞争问题
        System.out.println("\n--- 实验1：无同步版本（可能出现问题）---");
        testWithoutSync();
        
        Thread.sleep(2000);
        
        // 实验2：使用volatile建立happens-before关系
        System.out.println("\n--- 实验2：volatile版本（建立happens-before）---");
        testWithVolatile();
    }
    
    /**
     * 无同步版本测试
     * 问题：可能出现数据竞争，读取者可能看到data的旧值
     */
    private static void testWithoutSync() throws InterruptedException {
        // 重置状态
        data = 0;
        ready = false;
        
        // 读取者线程
        Thread reader = new Thread(() -> {
            System.out.println("读取者: 等待ready信号...");
            
            // 等待ready变为true
            while (!ready) {
                // 可能永远等待，或者看到ready=true但data还是旧值
            }
            
            // 读取data
            System.out.println("读取者: ready=true, 读取到data=" + data);
            // 问题：这里可能读到data=0（旧值），而不是42（新值）
            
            if (data != 42) {
                System.out.println("⚠️  数据竞争问题：期望data=42，实际读到data=" + data);
            }
        }, "Reader-NoSync");
        
        // 写入者线程
        Thread writer = new Thread(() -> {
            try {
                Thread.sleep(100); // 确保读取者先启动
                
                // 先修改数据，再设置标志
                data = 42;           // 第1步：修改数据
                ready = true;        // 第2步：设置标志
                
                System.out.println("写入者: 已设置data=42, ready=true");
                
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }, "Writer-NoSync");
        
        reader.start();
        writer.start();
        
        // 等待完成或超时
        writer.join();
        Thread.sleep(1000);
        
        if (reader.isAlive()) {
            System.out.println("⚠️  可见性问题：读取者仍在等待ready信号");
            reader.interrupt();
        }
        reader.join(1000);
    }
    
    /**
     * 使用volatile建立happens-before关系
     * 解决方案：volatile写 happens-before volatile读
     */
    private static void testWithVolatile() throws InterruptedException {
        // 重置状态
        volatileData = 0;
        volatileReady = false;
        
        // 读取者线程
        Thread reader = new Thread(() -> {
            System.out.println("读取者: 等待volatile ready信号...");
            
            // 等待volatileReady变为true
            while (!volatileReady) {
                // volatile读操作会从主内存读取最新值
            }
            
            // 读取data
            System.out.println("读取者: volatileReady=true, 读取到volatileData=" + volatileData);
            
            // 由于happens-before关系，这里一定能读到正确的值
            if (volatileData == 42) {
                System.out.println("✅ 成功：通过volatile建立了happens-before关系，数据一致");
            } else {
                System.out.println("❌ 异常：volatileData=" + volatileData + "，期望42");
            }
        }, "Reader-Volatile");
        
        // 写入者线程
        Thread writer = new Thread(() -> {
            try {
                Thread.sleep(100); // 确保读取者先启动
                
                // 关键：写入顺序很重要
                volatileData = 42;          // 第1步：修改普通变量
                volatileReady = true;       // 第2步：volatile写操作
                
                System.out.println("写入者: 已设置volatileData=42, volatileReady=true");
                
                // happens-before关系保证：
                // 1. volatileData=42 happens-before volatileReady=true（程序顺序规则）
                // 2. volatileReady=true（写）happens-before volatileReady读取（volatile规则）
                // 3. 因此 volatileData=42 happens-before 读取者读取volatileData（传递性）
                
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }, "Writer-Volatile");
        
        reader.start();
        writer.start();
        
        writer.join();
        reader.join();
        
        System.out.println("\n=== Happens-Before关系分析 ===");
        System.out.println("1. 程序顺序规则：在同一线程中，前面的操作 happens-before 后面的操作");
        System.out.println("2. Volatile规则：volatile写 happens-before 后续的volatile读");
        System.out.println("3. 传递性：A happens-before B，B happens-before C ⟹ A happens-before C");
        System.out.println("4. 因此：volatileData=42 happens-before 读取volatileData");
        System.out.println("5. 结果：读取者能够看到写入者的所有修改");
    }
}