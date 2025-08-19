/**
 * 🎯 Live Demo Implementation
 * Demonstrates complete hands-on learning framework
 */

import java.util.*;

public class DIBasicsDemo {
    
    static class Order {
        private String id;
        private double amount;
        
        public Order(String id, double amount) {
            this.id = id;
            this.amount = amount;
        }
        
        public String getId() { return id; }
        public double getAmount() { return amount; }
    }
    
    static class PaymentProcessor {
        public boolean charge(double amount) {
            System.out.println("💳 Processing payment: $" + amount);
            try { Thread.sleep(5); } catch (InterruptedException e) {}
            return amount > 0;
        }
    }
    
    static class InventoryManager {
        public boolean checkStock(String productId) {
            System.out.println("📦 Checking stock for: " + productId);
            return true;
        }
    }
    
    // ❌ Traditional approach (problems)
    static class BadOrderService {
        private PaymentProcessor payment = new PaymentProcessor(); // Tight coupling!
        private InventoryManager inventory = new InventoryManager(); // Hard to test!
        
        public boolean processOrder(Order order) {
            System.out.println("❌ BadOrderService: Creating dependencies inside class (tight coupling)");
            
            if (!inventory.checkStock(order.getId())) {
                return false;
            }
            
            return payment.charge(order.getAmount());
        }
    }
    
    // ✅ DI approach (solutions)  
    static class GoodOrderService {
        private final PaymentProcessor payment;
        private final InventoryManager inventory;
        
        // Constructor injection - Don't Initialize, Dependencies Injected!
        public GoodOrderService(PaymentProcessor payment, InventoryManager inventory) {
            this.payment = payment;    // Don't Initialize!
            this.inventory = inventory; // Dependencies Injected!
        }
        
        public boolean processOrder(Order order) {
            System.out.println("✅ GoodOrderService: Using injected dependencies (loose coupling)");
            
            if (!inventory.checkStock(order.getId())) {
                return false;
            }
            
            return payment.charge(order.getAmount());
        }
    }
    
    // 🧪 Testing demonstration
    static class TestingAdvantageDemo {
        static class MockPaymentProcessor extends PaymentProcessor {
            @Override
            public boolean charge(double amount) {
                System.out.println("🧪 Mock: Simulated payment processing for testing");
                return true; // Always succeeds for predictable testing
            }
        }
        
        static class MockInventoryManager extends InventoryManager {
            @Override
            public boolean checkStock(String productId) {
                System.out.println("🧪 Mock: Simulated inventory check for testing");
                return true; // Always in stock for testing
            }
        }
        
        public void demonstrateTestability() {
            System.out.println("\n🧪 ===== TESTING ADVANTAGE DEMONSTRATION =====");
            
            Order testOrder = new Order("test1", 99.99);
            
            System.out.println("\n❌ Testing BadOrderService (difficult):");
            System.out.println("   • Can't inject test dependencies");
            System.out.println("   • Always uses real PaymentProcessor and InventoryManager");
            System.out.println("   • Tests are unpredictable and slow");
            
            System.out.println("\n✅ Testing GoodOrderService (easy):");
            PaymentProcessor mockPayment = new MockPaymentProcessor();
            InventoryManager mockInventory = new MockInventoryManager();
            
            GoodOrderService service = new GoodOrderService(mockPayment, mockInventory);
            boolean result = service.processOrder(testOrder);
            System.out.println("   • Test result: " + (result ? "✅ SUCCESS" : "❌ FAILED"));
            System.out.println("   • Fast, predictable, isolated testing!");
        }
    }
    
    // ⚡ Performance comparison
    static class PerformanceComparison {
        public void measurePerformance() {
            System.out.println("\n⚡ ===== PERFORMANCE COMPARISON =====");
            
            int iterations = 1000;
            Order testOrder = new Order("perf1", 49.99);
            
            // Measure BadOrderService (creates dependencies each time)
            long badStart = System.nanoTime();
            for (int i = 0; i < iterations; i++) {
                BadOrderService badService = new BadOrderService(); // Creates new dependencies!
                badService.processOrder(testOrder);
            }
            long badEnd = System.nanoTime();
            double badTime = (badEnd - badStart) / 1_000_000.0;
            
            // Measure GoodOrderService (reuses dependencies)
            long goodStart = System.nanoTime();
            PaymentProcessor payment = new PaymentProcessor();     // Create once!
            InventoryManager inventory = new InventoryManager();   // Reuse everywhere!
            
            for (int i = 0; i < iterations; i++) {
                GoodOrderService goodService = new GoodOrderService(payment, inventory);
                goodService.processOrder(testOrder);
            }
            long goodEnd = System.nanoTime();
            double goodTime = (goodEnd - goodStart) / 1_000_000.0;
            
            System.out.println("📊 Performance Results:");
            System.out.println("❌ BadOrderService:  " + String.format("%.2f", badTime) + " ms");
            System.out.println("✅ GoodOrderService: " + String.format("%.2f", goodTime) + " ms");
            double improvement = badTime / goodTime;
            System.out.println("🚀 Performance improvement: " + String.format("%.1f", improvement) + "x faster");
            
            if (improvement >= 2.0) {
                System.out.println("🎯 TARGET MET: Performance improvement >= 2.0x ✅");
            } else {
                System.out.println("⚠️  Target missed: Need >= 2.0x improvement");
            }
        }
    }
    
    public static void main(String[] args) {
        System.out.println("🎓 DI Basics Demonstration - Task 8.1.1");
        System.out.println("========================================");
        
        Order sampleOrder = new Order("order123", 49.99);
        
        System.out.println("\n❌ ===== TRADITIONAL APPROACH (PROBLEMS) =====");
        BadOrderService badService = new BadOrderService();
        badService.processOrder(sampleOrder);
        
        System.out.println("\n✅ ===== DI APPROACH (SOLUTIONS) =====");
        PaymentProcessor payment = new PaymentProcessor();
        InventoryManager inventory = new InventoryManager();
        GoodOrderService goodService = new GoodOrderService(payment, inventory);
        goodService.processOrder(sampleOrder);
        
        TestingAdvantageDemo testDemo = new TestingAdvantageDemo();
        testDemo.demonstrateTestability();
        
        PerformanceComparison perfComparison = new PerformanceComparison();
        perfComparison.measurePerformance();
        
        System.out.println("\n🎉 ===== LEARNING SUMMARY =====");
        System.out.println("✅ Traditional problems demonstrated");
        System.out.println("✅ DI solutions implemented");
        System.out.println("✅ Testing advantages shown");
        System.out.println("✅ Performance benefits measured");
        System.out.println("\n🧠 Remember: DI = Don't Initialize!");
        System.out.println("🏆 Task 8.1.1 Complete - Ready for Task 8.1.2!");
    }
}
