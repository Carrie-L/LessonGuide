/**
 * 📚 Task 8.1.1: 依赖注入基础概念学习
 * 🎯 目标: 理解DI解决什么问题，从"纠缠不清"到"专业分工"
 * 
 * 🍽️ 餐厅老板类比:
 * - 传统方式: 老板什么都自己做（纠缠不清）
 * - DI方式: 雇佣专业的人来帮忙（专业分工）
 */

// ❌ 传统"纠缠"代码示例 - Primary学习者需要先理解问题
class BadUserService {
    // 问题：直接创建依赖，就像老板什么都自己做
    private DatabaseHelper database = new DatabaseHelper(); // 自己建数据库
    private NetworkClient network = new NetworkClient();   // 自己管网络
    private CacheManager cache = new CacheManager();      // 自己做缓存
    
    public User getUser(String id) {
        // 我只想获取用户，但要管理这么多东西！
        // 🤯 问题1: 难以测试（无法替换依赖）
        // 🤯 问题2: 紧耦合（修改一个影响全部）  
        // 🤯 问题3: 违反单一职责（管太多事）
        
        User user = database.findUser(id);
        if (user == null) {
            user = network.fetchUser(id);
            cache.store(id, user);
        }
        return user;
    }
}

// ✅ DI"魔法"代码示例 - Primary学习者看到解决方案
class GoodUserService {
    // 解决方案：依赖由外部提供，就像有专业团队帮你
    private final DatabaseHelper database;  // 数据库专家
    private final NetworkClient network;    // 网络专家  
    private final CacheManager cache;       // 缓存专家
    
    // 构造函数注入：告诉外部"我需要这些专家"
    public GoodUserService(DatabaseHelper database, 
                          NetworkClient network, 
                          CacheManager cache) {
        this.database = database;
        this.network = network;
        this.cache = cache;
    }
    
    public User getUser(String id) {
        // 我只管用户业务逻辑！其他的我不管！
        // ✅ 优势1: 易于测试（可以传入测试用的假对象）
        // ✅ 优势2: 易于修改（换个数据库实现，不影响业务逻辑）
        // ✅ 优势3: 专业分工（我只管用户逻辑，别人管具体实现）
        
        User user = database.findUser(id);
        if (user == null) {
            user = network.fetchUser(id);
            cache.store(id, user);
        }
        return user;
    }
}

// 🎯 Primary练习题：识别问题代码
class OrderService {
    // TODO: Primary学习者练习 - 找出这里的问题
    private PaymentProcessor payment = new PaymentProcessor(); // 问题在哪？
    private InventoryManager inventory = new InventoryManager(); // 问题在哪？
    
    public boolean processOrder(Order order) {
        if (inventory.checkStock(order.getProductId())) {
            return payment.charge(order.getAmount());
        }
        return false;
    }
}

// 💻 Primary练习答案：改写为DI方式
class BetterOrderService {
    // TODO: Primary学习者完成构造函数参数
    private final PaymentProcessor payment;
    private final InventoryManager inventory;
    
    public BetterOrderService(PaymentProcessor payment, InventoryManager inventory) {
        this.payment = payment;
        this.inventory = inventory;
    }
    
    public boolean processOrder(Order order) {
        // 现在这个类只关注订单处理逻辑！
        if (inventory.checkStock(order.getProductId())) {
            return payment.charge(order.getAmount());
        }
        return false;
    }
}

// 🧪 Primary验证测试：如何测试DI代码
class TestUserService {
    public static void demonstrateTestability() {
        System.out.println("🧪 演示DI的可测试性:");
        
        // 创建测试用的假对象（Mock）
        DatabaseHelper mockDatabase = new MockDatabaseHelper();
        NetworkClient mockNetwork = new MockNetworkClient();
        CacheManager mockCache = new MockCacheManager();
        
        // 注入测试对象
        GoodUserService service = new GoodUserService(mockDatabase, mockNetwork, mockCache);
        
        // 现在可以独立测试业务逻辑，不依赖真实的数据库/网络！
        User user = service.getUser("test123");
        System.out.println("✅ 测试通过！用户: " + user.getName());
    }
}

// 🔧 辅助类定义（简化版，帮助Primary学习者理解）
class User {
    private String id;
    private String name;
    
    public User(String id, String name) {
        this.id = id;
        this.name = name;
    }
    
    public String getId() { return id; }
    public String getName() { return name; }
}

class DatabaseHelper {
    public User findUser(String id) {
        System.out.println("📊 从数据库查找用户: " + id);
        return null; // 简化实现
    }
}

class NetworkClient {
    public User fetchUser(String id) {
        System.out.println("🌐 从网络获取用户: " + id);
        return new User(id, "网络用户" + id);
    }
}

class CacheManager {
    public void store(String id, User user) {
        System.out.println("💾 缓存用户: " + id);
    }
}

// 测试用的假对象
class MockDatabaseHelper extends DatabaseHelper {
    @Override
    public User findUser(String id) {
        System.out.println("🧪 Mock: 假数据库查找");
        return new User(id, "测试用户" + id);
    }
}

class MockNetworkClient extends NetworkClient {
    @Override
    public User fetchUser(String id) {
        System.out.println("🧪 Mock: 假网络请求");
        return new User(id, "Mock用户" + id);
    }
}

class MockCacheManager extends CacheManager {
    @Override
    public void store(String id, User user) {
        System.out.println("🧪 Mock: 假缓存存储");
    }
}

class PaymentProcessor {
    public boolean charge(double amount) {
        System.out.println("💳 处理支付: $" + amount);
        return true;
    }
}

class InventoryManager {
    public boolean checkStock(String productId) {
        System.out.println("📦 检查库存: " + productId);
        return true;
    }
}

class Order {
    private String productId;
    private double amount;
    
    public Order(String productId, double amount) {
        this.productId = productId;
        this.amount = amount;
    }
    
    public String getProductId() { return productId; }
    public double getAmount() { return amount; }
}

// 🎯 主函数：Primary学习者的实践入口
public class DependencyInjectionBasics {
    public static void main(String[] args) {
        System.out.println("🎓 依赖注入基础学习 - Primary Level");
        System.out.println("==================================");
        
        // 演示1: 对比传统方式和DI方式
        System.out.println("\n🍽️ 餐厅老板类比演示:");
        System.out.println("传统方式: 老板什么都自己做");
        BadUserService badService = new BadUserService();
        badService.getUser("user123");
        
        System.out.println("\nDI方式: 专业分工，雇佣专家");
        GoodUserService goodService = new GoodUserService(
            new DatabaseHelper(),
            new NetworkClient(), 
            new CacheManager()
        );
        goodService.getUser("user123");
        
        // 演示2: DI的可测试性
        System.out.println("\n" + "=".repeat(40));
        TestUserService.demonstrateTestability();
        
        // Primary学习者自我检验
        System.out.println("\n🎯 Primary自我检验:");
        System.out.println("1. 传统代码的三个问题是什么？");
        System.out.println("   答案: ①难以测试 ②紧耦合 ③违反单一职责");
        System.out.println("2. DI如何解决测试难题？");
        System.out.println("   答案: 可以传入测试用的假对象(Mock)");
        System.out.println("3. 控制反转的含义？");
        System.out.println("   答案: 从'我来创建'变成'别人创建好给我'");
        
        System.out.println("\n🎉 恭喜！Primary Level - Task 8.1.1 完成！");
        System.out.println("📚 记住：DI = Don't Initialize (不要自己初始化)");
        System.out.println("🏆 下一步：学习 Task 8.1.2 - Hilt vs Dagger对比");
    }
}