/**
 * Mark Word 观察实验
 * 目标：观察不同锁状态下对象头的变化
 */
public class MarkWordDemo {
    
    public static void main(String[] args) throws InterruptedException {
        Object lock = new Object();
        
        System.out.println("=== Mark Word 状态观察实验 ===");
        
        // 1. 无锁状态
        System.out.println("1. 无锁状态下的对象:");
        printObjectInfo(lock);
        
        // 2. 偏向锁状态  
        System.out.println("\n2. 偏向锁状态:");
        synchronized(lock) {
            printObjectInfo(lock);
        }
        
        // 3. 调用hashCode后
        System.out.println("\n3. 调用hashCode后:");
        int hash = lock.hashCode();
        System.out.println("HashCode: " + hash);
        
        synchronized(lock) {
            printObjectInfo(lock);
        }
        
        // 4. 多线程竞争 - 轻量级锁
        System.out.println("\n4. 多线程竞争时:");
        Thread thread = new Thread(() -> {
            synchronized(lock) {
                printObjectInfo(lock);
            }
        });
        
        thread.start();
        thread.join();
    }
    
    private static void printObjectInfo(Object obj) {
        // 简单的对象信息打印
        System.out.println("Object: " + obj.getClass().getSimpleName() + 
                         "@" + Integer.toHexString(obj.hashCode()));
        System.out.println("Identity: " + System.identityHashCode(obj));
    }
}