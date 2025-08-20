/**
 * happens-before原则在Android配置管理中的实际应用
 * 演示三种不同的解决方案
 */

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

// 方案1: 使用volatile保证可见性
class VolatileConfigLoader {
    private final Map<String, String> config = new HashMap<>();
    private volatile boolean isLoaded = false;
    
    public void loadConfigAsync() {
        new Thread(() -> {
            try {
                // 模拟网络加载延迟
                Thread.sleep(1000);
                
                // 加载配置数据
                config.put("server_url", "https://api.myapp.com");
                config.put("api_version", "v2.1");
                config.put("timeout", "5000");
                
                // volatile写 - 这是关键！
                isLoaded = true;
                
                System.out.println("✅ 配置加载完成");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }, "ConfigLoader").start();
    }
    
    public String getConfig(String key) {
        // volatile读 - happens-before保证
        if (isLoaded) {
            return config.get(key);
        }
        return null;
    }
    
    public boolean isConfigReady() {
        return isLoaded;
    }
}

// 方案2: 使用CountDownLatch的线程同步
class LatchConfigLoader {
    private final Map<String, String> config = new HashMap<>();
    private final CountDownLatch latch = new CountDownLatch(1);
    
    public void loadConfigAsync() {
        new Thread(() -> {
            try {
                Thread.sleep(1000);
                
                config.put("server_url", "https://api.myapp.com");
                config.put("api_version", "v2.1");
                config.put("timeout", "5000");
                
                // 释放等待的线程
                latch.countDown();
                
                System.out.println("✅ 配置加载完成(Latch)");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }, "ConfigLoader-Latch").start();
    }
    
    public String getConfig(String key) throws InterruptedException {
        // 等待配置加载完成，利用happens-before
        latch.await();
        return config.get(key);
    }
}

// 方案3: 使用synchronized的锁机制
class SynchronizedConfigLoader {
    private final Map<String, String> config = new HashMap<>();
    private boolean isLoaded = false;
    private final Object lock = new Object();
    
    public void loadConfigAsync() {
        new Thread(() -> {
            try {
                Thread.sleep(1000);
                
                synchronized (lock) {
                    config.put("server_url", "https://api.myapp.com");
                    config.put("api_version", "v2.1");
                    config.put("timeout", "5000");
                    isLoaded = true;
                }
                
                System.out.println("✅ 配置加载完成(Synchronized)");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }, "ConfigLoader-Sync").start();
    }
    
    public String getConfig(String key) {
        synchronized (lock) {
            if (isLoaded) {
                return config.get(key);
            }
        }
        return null;
    }
}

// 测试类
public class HappensBefore_Example {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("🔬 happens-before原则测试\n");
        
        // 测试方案1: volatile
        testVolatileApproach();
        
        Thread.sleep(2000);
        
        // 测试方案2: CountDownLatch
        testLatchApproach();
        
        Thread.sleep(2000);
        
        // 测试方案3: synchronized
        testSynchronizedApproach();
    }
    
    private static void testVolatileApproach() throws InterruptedException {
        System.out.println("📋 测试1: volatile方案");
        VolatileConfigLoader loader = new VolatileConfigLoader();
        
        // 启动配置加载
        loader.loadConfigAsync();
        
        // 模拟其他线程尝试读取配置
        new Thread(() -> {
            while (!loader.isConfigReady()) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }
            
            // 当isLoaded为true时，根据happens-before，
            // 我们保证能看到完整的配置数据
            System.out.println("🎯 读取到配置: " + loader.getConfig("server_url"));
        }, "Reader").start();
    }
    
    private static void testLatchApproach() throws InterruptedException {
        System.out.println("\n📋 测试2: CountDownLatch方案");
        LatchConfigLoader loader = new LatchConfigLoader();
        
        loader.loadConfigAsync();
        
        new Thread(() -> {
            try {
                // 这里会阻塞直到latch.countDown()被调用
                String url = loader.getConfig("server_url");
                System.out.println("🎯 读取到配置: " + url);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }, "Reader-Latch").start();
    }
    
    private static void testSynchronizedApproach() throws InterruptedException {
        System.out.println("\n📋 测试3: synchronized方案");
        SynchronizedConfigLoader loader = new SynchronizedConfigLoader();
        
        loader.loadConfigAsync();
        
        new Thread(() -> {
            String url = null;
            // 轮询直到获取到配置
            while (url == null) {
                url = loader.getConfig("server_url");
                if (url == null) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        return;
                    }
                }
            }
            System.out.println("🎯 读取到配置: " + url);
        }, "Reader-Sync").start();
    }
}

/*
 * 关键理解点：
 * 
 * 1. volatile方案：
 *    - config写入 happens-before isLoaded=true (程序顺序)
 *    - isLoaded=true happens-before if(isLoaded) (volatile规则)
 *    - 传递性保证config写入 happens-before config读取
 * 
 * 2. CountDownLatch方案：
 *    - config写入 happens-before countDown() (程序顺序)
 *    - countDown() happens-before await()返回 (同步器规则)
 *    - 传递性保证config写入 happens-before config读取
 * 
 * 3. synchronized方案：
 *    - config写入和isLoaded=true在同一个同步块内
 *    - unlock happens-before 后续的lock (监视器规则)
 *    - 保证读取线程看到完整的写入操作
 */