# 🔥 Enhanced Collections Framework - 集合框架实战强化版

## 📖 核心增强理念: "Data Structures Through Implementation"

> **严格原则**: 数据结构的精髓在于实现！每个集合类都必须亲手构建来理解其内部机制。

### 🎯 Collections Framework 增强特性

**⛔ 严格禁令**:
- 禁止使用现成的集合API而不理解内部实现
- 禁止跳过任何数据结构的手动实现
- 禁止在不了解时间复杂度的情况下选择集合
- 禁止未经并发安全测试就使用并发集合

**✅ 强制要求**:
- 每个集合类都要从零开始手动实现
- 每个数据结构都要分析时间和空间复杂度
- 每个并发集合都要验证线程安全性
- 每个优化策略都要通过性能测试证明

---

## 🚀 Section 1.2: 集合框架深度实践 (65分钟 | 13个任务)

### 📖 学习路线图

**阶段进程**:
- 🌱 **Primary阶段** (Tasks 1-5): 基础数据结构实现 - 25分钟
- 🚀 **Intermediate阶段** (Tasks 6-10): 高性能集合优化 - 25分钟  
- 🏆 **Senior阶段** (Tasks 11-13): 并发集合架构 - 15分钟

---

## 🌱 PRIMARY阶段: 基础数据结构实现 (25分钟)

### Task 1.2.1: 🌱 手造ArrayList - 动态数组的本质 (5分钟) ⏰

**学习类比**: ArrayList = 可伸缩的停车场 🅿️
- 初始容量 = 停车场大小
- 扩容机制 = 建造新的更大停车场，搬迁所有车辆
- 随机访问 = 直接开到指定车位

**🎯 Primary目标**: 从零实现ArrayList，理解动态数组的扩容机制

**💻 强制编程实践** (必须逐行手动输入):

```java
// 文件: student_progress/c01/CustomArrayList.java
// 目标: 亲手实现ArrayList的核心机制

import java.util.*;

public class CustomArrayList<E> {
    
    // TODO 1: 手动实现核心数据结构
    private static final int DEFAULT_CAPACITY = 10;
    private static final Object[] EMPTY_ELEMENTDATA = {};
    private static final Object[] DEFAULTCAPACITY_EMPTY_ELEMENTDATA = {};
    
    // 存储元素的数组
    transient Object[] elementData;
    
    // 实际元素数量
    private int size;
    
    // 修改次数统计(用于fail-fast机制)
    protected transient int modCount = 0;
    
    // TODO 2: 手动实现构造函数
    public CustomArrayList(int initialCapacity) {
        // 必须亲手实现：
        // 1. 参数验证
        // 2. 根据容量初始化数组
        // 3. 处理特殊情况
        
        if (initialCapacity > 0) {
            this.elementData = new Object[initialCapacity];
        } else if (initialCapacity == 0) {
            this.elementData = EMPTY_ELEMENTDATA;
        } else {
            throw new IllegalArgumentException("Illegal Capacity: " + initialCapacity);
        }
    }
    
    public CustomArrayList() {
        // 必须亲手实现：默认构造函数
        this.elementData = DEFAULTCAPACITY_EMPTY_ELEMENTDATA;
    }
    
    // TODO 3: 手动实现核心扩容逻辑
    private void ensureCapacityInternal(int minCapacity) {
        // 必须亲手实现：
        // 1. 计算所需的最小容量
        // 2. 触发扩容检查
        
        if (elementData == DEFAULTCAPACITY_EMPTY_ELEMENTDATA) {
            minCapacity = Math.max(DEFAULT_CAPACITY, minCapacity);
        }
        
        ensureExplicitCapacity(minCapacity);
    }
    
    private void ensureExplicitCapacity(int minCapacity) {
        modCount++;
        
        // 必须亲手实现：
        // 检查是否需要扩容
        if (minCapacity - elementData.length > 0) {
            grow(minCapacity);
        }
    }
    
    private void grow(int minCapacity) {
        // 必须亲手实现：
        // 1. 计算新容量(1.5倍扩容)
        // 2. 处理整数溢出
        // 3. 创建新数组并复制元素
        
        int oldCapacity = elementData.length;
        int newCapacity = oldCapacity + (oldCapacity >> 1); // 1.5倍扩容
        
        if (newCapacity - minCapacity < 0) {
            newCapacity = minCapacity;
        }
        
        if (newCapacity - MAX_ARRAY_SIZE > 0) {
            newCapacity = hugeCapacity(minCapacity);
        }
        
        // 复制元素到新数组
        elementData = Arrays.copyOf(elementData, newCapacity);
        
        System.out.println("📈 扩容: " + oldCapacity + " -> " + newCapacity);
    }
    
    private static final int MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8;
    
    private static int hugeCapacity(int minCapacity) {
        if (minCapacity < 0) // overflow
            throw new OutOfMemoryError();
        return (minCapacity > MAX_ARRAY_SIZE) ?
            Integer.MAX_VALUE :
            MAX_ARRAY_SIZE;
    }
    
    // TODO 4: 手动实现添加元素操作
    public boolean add(E e) {
        // 必须亲手实现：
        // 1. 确保容量足够
        // 2. 添加元素到末尾
        // 3. 更新size
        
        ensureCapacityInternal(size + 1);
        elementData[size++] = e;
        return true;
    }
    
    public void add(int index, E element) {
        // 必须亲手实现：
        // 1. 检查索引范围
        // 2. 确保容量足够
        // 3. 移动元素为新元素腾出空间
        // 4. 插入新元素
        
        rangeCheckForAdd(index);
        
        ensureCapacityInternal(size + 1);
        
        // 移动元素
        System.arraycopy(elementData, index, elementData, index + 1, size - index);
        elementData[index] = element;
        size++;
    }
    
    // TODO 5: 手动实现获取和设置操作
    @SuppressWarnings("unchecked")
    public E get(int index) {
        // 必须亲手实现：
        // 1. 检查索引范围
        // 2. 返回指定位置的元素
        
        rangeCheck(index);
        return (E) elementData[index];
    }
    
    public E set(int index, E element) {
        // 必须亲手实现：
        // 1. 检查索引范围
        // 2. 替换元素
        // 3. 返回旧元素
        
        rangeCheck(index);
        
        @SuppressWarnings("unchecked")
        E oldValue = (E) elementData[index];
        elementData[index] = element;
        return oldValue;
    }
    
    // TODO 6: 手动实现删除操作
    public E remove(int index) {
        // 必须亲手实现：
        // 1. 检查索引范围
        // 2. 保存要删除的元素
        // 3. 移动后续元素
        // 4. 清空最后一个位置
        // 5. 更新size
        
        rangeCheck(index);
        
        modCount++;
        @SuppressWarnings("unchecked")
        E oldValue = (E) elementData[index];
        
        int numMoved = size - index - 1;
        if (numMoved > 0) {
            System.arraycopy(elementData, index + 1, elementData, index, numMoved);
        }
        elementData[--size] = null; // 清空引用，帮助GC
        
        return oldValue;
    }
    
    public boolean remove(Object o) {
        // 必须亲手实现：
        // 1. 遍历查找元素
        // 2. 调用索引删除方法
        
        if (o == null) {
            for (int index = 0; index < size; index++) {
                if (elementData[index] == null) {
                    fastRemove(index);
                    return true;
                }
            }
        } else {
            for (int index = 0; index < size; index++) {
                if (o.equals(elementData[index])) {
                    fastRemove(index);
                    return true;
                }
            }
        }
        return false;
    }
    
    private void fastRemove(int index) {
        modCount++;
        int numMoved = size - index - 1;
        if (numMoved > 0) {
            System.arraycopy(elementData, index + 1, elementData, index, numMoved);
        }
        elementData[--size] = null;
    }
    
    // TODO 7: 手动实现辅助方法
    public int size() { return size; }
    
    public boolean isEmpty() { return size == 0; }
    
    public void clear() {
        modCount++;
        
        // 清空所有元素引用
        for (int i = 0; i < size; i++) {
            elementData[i] = null;
        }
        
        size = 0;
    }
    
    private void rangeCheck(int index) {
        if (index >= size) {
            throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
        }
    }
    
    private void rangeCheckForAdd(int index) {
        if (index > size || index < 0) {
            throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
        }
    }
    
    private String outOfBoundsMsg(int index) {
        return "Index: " + index + ", Size: " + size;
    }
    
    // TODO 8: 手动实现性能测试方法
    public void printInternalState() {
        System.out.println("=== ArrayList内部状态 ===");
        System.out.println("Size: " + size);
        System.out.println("Capacity: " + elementData.length);
        System.out.println("Load Factor: " + (size * 100.0 / elementData.length) + "%");
        System.out.println("ModCount: " + modCount);
    }
}

// TODO 9: 手动实现性能测试和验证
class ArrayListPerformanceTest {
    
    public static void testBasicOperations() {
        System.out.println("=== 基本操作测试 ===");
        
        CustomArrayList<Integer> list = new CustomArrayList<>();
        
        // 测试添加操作
        System.out.println("1. 测试添加操作");
        for (int i = 0; i < 15; i++) {
            list.add(i);
            if (i % 5 == 4) {
                list.printInternalState();
            }
        }
        
        // 测试获取操作
        System.out.println("\n2. 测试随机访问");
        long startTime = System.nanoTime();
        for (int i = 0; i < list.size(); i++) {
            Integer value = list.get(i);
        }
        long endTime = System.nanoTime();
        System.out.println("随机访问时间: " + (endTime - startTime) + " ns");
        
        // 测试插入操作
        System.out.println("\n3. 测试中间插入");
        startTime = System.nanoTime();
        list.add(5, 999);
        endTime = System.nanoTime();
        System.out.println("中间插入时间: " + (endTime - startTime) + " ns");
        list.printInternalState();
        
        // 测试删除操作
        System.out.println("\n4. 测试删除操作");
        startTime = System.nanoTime();
        list.remove(5);
        endTime = System.nanoTime();
        System.out.println("删除操作时间: " + (endTime - startTime) + " ns");
        list.printInternalState();
    }
    
    public static void testExpansionBehavior() {
        System.out.println("\n=== 扩容行为分析 ===");
        
        CustomArrayList<String> list = new CustomArrayList<>(1);
        
        System.out.println("初始状态:");
        list.printInternalState();
        
        // 观察扩容过程
        for (int i = 0; i < 20; i++) {
            list.add("Element " + i);
            
            if (i == 0 || i == 1 || i == 2 || i == 4 || i == 7 || i == 11 || i == 17) {
                System.out.println("\n添加第 " + (i + 1) + " 个元素后:");
                list.printInternalState();
            }
        }
    }
    
    public static void compareWithBuiltInArrayList() {
        System.out.println("\n=== 性能对比测试 ===");
        
        final int ELEMENT_COUNT = 100000;
        
        // 测试自定义ArrayList
        CustomArrayList<Integer> customList = new CustomArrayList<>();
        long startTime = System.currentTimeMillis();
        
        for (int i = 0; i < ELEMENT_COUNT; i++) {
            customList.add(i);
        }
        
        long customTime = System.currentTimeMillis() - startTime;
        
        // 测试原生ArrayList
        ArrayList<Integer> builtInList = new ArrayList<>();
        startTime = System.currentTimeMillis();
        
        for (int i = 0; i < ELEMENT_COUNT; i++) {
            builtInList.add(i);
        }
        
        long builtInTime = System.currentTimeMillis() - startTime;
        
        System.out.println("添加 " + ELEMENT_COUNT + " 个元素:");
        System.out.println("自定义ArrayList: " + customTime + " ms");
        System.out.println("原生ArrayList: " + builtInTime + " ms");
        System.out.println("性能比率: " + (customTime * 100.0 / builtInTime) + "%");
    }
}

// TODO 10: 手动实现主函数和综合测试
class ArrayListDemo {
    public static void main(String[] args) {
        System.out.println("📚 自制ArrayList实验室");
        System.out.println("目标: 深度理解动态数组的内部机制\n");
        
        // 测试1: 基本操作
        ArrayListPerformanceTest.testBasicOperations();
        
        // 测试2: 扩容行为
        ArrayListPerformanceTest.testExpansionBehavior();
        
        // 测试3: 性能对比
        ArrayListPerformanceTest.compareWithBuiltInArrayList();
        
        System.out.println("\n🎯 学习总结:");
        System.out.println("1. ArrayList基于动态数组实现");
        System.out.println("2. 扩容机制采用1.5倍策略平衡时间和空间");
        System.out.println("3. 随机访问O(1)，插入删除最坏O(n)");
        System.out.println("4. 适用于读多写少的场景");
    }
}
```

**📋 实践步骤清单** (必须逐项完成):
- [ ] 📝 手动创建文件 `CustomArrayList.java`
- [ ] ⌨️ 逐行手动输入所有核心代码
- [ ] 🔧 实现数组存储和大小管理
- [ ] 🔧 实现核心扩容逻辑
- [ ] 🔧 实现添加、删除、获取操作
- [ ] 🔧 实现边界检查和异常处理
- [ ] 🔧 实现性能测试和分析工具
- [ ] 🏃 运行所有测试场景
- [ ] 📊 分析扩容行为和性能特征
- [ ] 📝 对比自制版本与原生版本

**✅ Primary检查点验证**:
1. **扩容机制**: 能观察到1.5倍扩容策略的执行吗？
2. **性能特征**: 随机访问确实比插入删除快吗？
3. **内存管理**: 理解为什么要清空引用帮助GC吗？

---

### Task 1.2.2: 🌱 手造HashMap - 哈希表的奥秘 (5分钟) ⏰

**学习类比**: HashMap = 图书馆的分类系统 📚
- 哈希函数 = 图书分类规则
- 桶数组 = 分类书架
- 链表/红黑树 = 同类书籍的存放方式
- 扩容 = 增加书架和重新分类

**🎯 Primary目标**: 从零实现HashMap，掌握哈希冲突解决和动态扩容

**💻 强制编程实践** (必须逐行手动输入):

```java
// 文件: student_progress/c01/CustomHashMap.java
// 目标: 亲手实现HashMap的核心机制

public class CustomHashMap<K, V> {
    
    // TODO 1: 手动实现核心数据结构
    static class Node<K, V> {
        final int hash;
        final K key;
        V value;
        Node<K, V> next;
        
        Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
        
        public final K getKey() { return key; }
        public final V getValue() { return value; }
        public final String toString() { return key + "=" + value; }
        
        public final V setValue(V newValue) {
            V oldValue = value;
            value = newValue;
            return oldValue;
        }
        
        public final boolean equals(Object o) {
            if (o == this) return true;
            if (o instanceof Node) {
                Node<?, ?> e = (Node<?, ?>) o;
                return Objects.equals(key, e.getKey()) && Objects.equals(value, e.getValue());
            }
            return false;
        }
        
        public final int hashCode() {
            return Objects.hashCode(key) ^ Objects.hashCode(value);
        }
    }
    
    // 默认初始容量 (必须是2的幂)
    static final int DEFAULT_INITIAL_CAPACITY = 1 << 4; // 16
    
    // 最大容量
    static final int MAXIMUM_CAPACITY = 1 << 30;
    
    // 默认负载因子
    static final float DEFAULT_LOAD_FACTOR = 0.75f;
    
    // 链表转红黑树的阈值
    static final int TREEIFY_THRESHOLD = 8;
    
    // 红黑树转链表的阈值
    static final int UNTREEIFY_THRESHOLD = 6;
    
    // 核心存储数组
    transient Node<K, V>[] table;
    
    // 键值对数量
    transient int size;
    
    // 修改次数
    transient int modCount;
    
    // 扩容阈值
    int threshold;
    
    // 负载因子
    final float loadFactor;
    
    // TODO 2: 手动实现构造函数
    public CustomHashMap(int initialCapacity, float loadFactor) {
        // 必须亲手实现：
        // 1. 参数验证
        // 2. 设置负载因子
        // 3. 计算初始阈值
        
        if (initialCapacity < 0) {
            throw new IllegalArgumentException("Illegal initial capacity: " + initialCapacity);
        }
        if (initialCapacity > MAXIMUM_CAPACITY) {
            initialCapacity = MAXIMUM_CAPACITY;
        }
        if (loadFactor <= 0 || Float.isNaN(loadFactor)) {
            throw new IllegalArgumentException("Illegal load factor: " + loadFactor);
        }
        
        this.loadFactor = loadFactor;
        this.threshold = tableSizeFor(initialCapacity);
    }
    
    public CustomHashMap() {
        this.loadFactor = DEFAULT_LOAD_FACTOR;
    }
    
    // TODO 3: 手动实现哈希算法
    static final int hash(Object key) {
        // 必须亲手实现：
        // 1. 处理null key
        // 2. 使用高位扰动算法
        // 3. 减少哈希冲突
        
        int h;
        return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
    }
    
    // 计算最接近的2的幂
    static final int tableSizeFor(int cap) {
        int n = cap - 1;
        n |= n >>> 1;
        n |= n >>> 2;
        n |= n >>> 4;
        n |= n >>> 8;
        n |= n >>> 16;
        return (n < 0) ? 1 : (n >= MAXIMUM_CAPACITY) ? MAXIMUM_CAPACITY : n + 1;
    }
    
    // TODO 4: 手动实现PUT操作
    public V put(K key, V value) {
        return putVal(hash(key), key, value, false, true);
    }
    
    final V putVal(int hash, K key, V value, boolean onlyIfAbsent, boolean evict) {
        // 必须亲手实现：
        // 1. 初始化表(如果需要)
        // 2. 计算桶位置
        // 3. 处理空桶情况
        // 4. 处理哈希冲突(链表遍历)
        // 5. 处理键重复情况
        // 6. 添加新节点
        // 7. 检查是否需要扩容
        
        Node<K, V>[] tab;
        Node<K, V> p;
        int n, i;
        
        // 步骤1: 初始化表
        if ((tab = table) == null || (n = tab.length) == 0) {
            n = (tab = resize()).length;
        }
        
        // 步骤2: 计算桶位置并检查是否为空
        if ((p = tab[i = (n - 1) & hash]) == null) {
            // 空桶，直接放入新节点
            tab[i] = newNode(hash, key, value, null);
        } else {
            // 处理哈希冲突
            Node<K, V> e;
            K k;
            
            // 检查第一个节点
            if (p.hash == hash && ((k = p.key) == key || (key != null && key.equals(k)))) {
                e = p;
            } else {
                // 遍历链表查找或添加到末尾
                for (int binCount = 0; ; ++binCount) {
                    if ((e = p.next) == null) {
                        // 添加到链表末尾
                        p.next = newNode(hash, key, value, null);
                        
                        // 检查是否需要树化
                        if (binCount >= TREEIFY_THRESHOLD - 1) {
                            treeifyBin(tab, hash);
                        }
                        break;
                    }
                    
                    // 找到相同key
                    if (e.hash == hash && ((k = e.key) == key || (key != null && key.equals(k)))) {
                        break;
                    }
                    p = e;
                }
            }
            
            // 更新已存在的key的value
            if (e != null) {
                V oldValue = e.value;
                if (!onlyIfAbsent || oldValue == null) {
                    e.value = value;
                }
                afterNodeAccess(e);
                return oldValue;
            }
        }
        
        ++modCount;
        
        // 检查是否需要扩容
        if (++size > threshold) {
            resize();
        }
        
        afterNodeInsertion(evict);
        return null;
    }
    
    // TODO 5: 手动实现GET操作
    public V get(Object key) {
        Node<K, V> e;
        return (e = getNode(hash(key), key)) == null ? null : e.value;
    }
    
    final Node<K, V> getNode(int hash, Object key) {
        // 必须亲手实现：
        // 1. 检查表是否为空
        // 2. 计算桶位置
        // 3. 检查第一个节点
        // 4. 遍历链表查找
        // 5. 处理树结构(简化版本暂时跳过)
        
        Node<K, V>[] tab;
        Node<K, V> first, e;
        int n;
        K k;
        
        if ((tab = table) != null && (n = tab.length) > 0 &&
            (first = tab[(n - 1) & hash]) != null) {
            
            // 检查第一个节点
            if (first.hash == hash && ((k = first.key) == key || (key != null && key.equals(k)))) {
                return first;
            }
            
            // 遍历链表
            if ((e = first.next) != null) {
                do {
                    if (e.hash == hash && ((k = e.key) == key || (key != null && key.equals(k)))) {
                        return e;
                    }
                } while ((e = e.next) != null);
            }
        }
        return null;
    }
    
    // TODO 6: 手动实现扩容机制
    final Node<K, V>[] resize() {
        // 必须亲手实现：
        // 1. 保存旧表信息
        // 2. 计算新表大小和阈值
        // 3. 创建新表
        // 4. 重新哈希所有元素
        // 5. 处理链表拆分
        
        Node<K, V>[] oldTab = table;
        int oldCap = (oldTab == null) ? 0 : oldTab.length;
        int oldThr = threshold;
        int newCap, newThr = 0;
        
        if (oldCap > 0) {
            // 已有表，扩容为原来的2倍
            if (oldCap >= MAXIMUM_CAPACITY) {
                threshold = Integer.MAX_VALUE;
                return oldTab;
            } else if ((newCap = oldCap << 1) < MAXIMUM_CAPACITY && oldCap >= DEFAULT_INITIAL_CAPACITY) {
                newThr = oldThr << 1; // 阈值也翻倍
            }
        } else if (oldThr > 0) {
            // 使用阈值作为初始容量
            newCap = oldThr;
        } else {
            // 使用默认值
            newCap = DEFAULT_INITIAL_CAPACITY;
            newThr = (int) (DEFAULT_LOAD_FACTOR * DEFAULT_INITIAL_CAPACITY);
        }
        
        if (newThr == 0) {
            float ft = (float) newCap * loadFactor;
            newThr = (newCap < MAXIMUM_CAPACITY && ft < (float) MAXIMUM_CAPACITY) ?
                     (int) ft : Integer.MAX_VALUE;
        }
        
        threshold = newThr;
        @SuppressWarnings({"rawtypes", "unchecked"})
        Node<K, V>[] newTab = (Node<K, V>[]) new Node[newCap];
        table = newTab;
        
        if (oldTab != null) {
            // 重新哈希所有元素
            for (int j = 0; j < oldCap; ++j) {
                Node<K, V> e;
                if ((e = oldTab[j]) != null) {
                    oldTab[j] = null;
                    
                    if (e.next == null) {
                        // 单个节点，直接移动
                        newTab[e.hash & (newCap - 1)] = e;
                    } else {
                        // 链表，需要拆分
                        Node<K, V> loHead = null, loTail = null;
                        Node<K, V> hiHead = null, hiTail = null;
                        Node<K, V> next;
                        
                        do {
                            next = e.next;
                            if ((e.hash & oldCap) == 0) {
                                // 低位链表
                                if (loTail == null) {
                                    loHead = e;
                                } else {
                                    loTail.next = e;
                                }
                                loTail = e;
                            } else {
                                // 高位链表
                                if (hiTail == null) {
                                    hiHead = e;
                                } else {
                                    hiTail.next = e;
                                }
                                hiTail = e;
                            }
                        } while ((e = next) != null);
                        
                        // 放置链表到新位置
                        if (loTail != null) {
                            loTail.next = null;
                            newTab[j] = loHead;
                        }
                        if (hiTail != null) {
                            hiTail.next = null;
                            newTab[j + oldCap] = hiHead;
                        }
                    }
                }
            }
        }
        
        System.out.println("🔄 HashMap扩容: " + oldCap + " -> " + newCap + 
                          ", threshold: " + oldThr + " -> " + newThr);
        return newTab;
    }
    
    // TODO 7: 手动实现辅助方法
    Node<K, V> newNode(int hash, K key, V value, Node<K, V> next) {
        return new Node<>(hash, key, value, next);
    }
    
    void treeifyBin(Node<K, V>[] tab, int hash) {
        // 简化实现，暂时不转换为红黑树
        System.out.println("⚠️  链表长度达到阈值，实际生产环境会转换为红黑树");
    }
    
    void afterNodeAccess(Node<K, V> p) {}
    void afterNodeInsertion(boolean evict) {}
    
    public int size() { return size; }
    public boolean isEmpty() { return size == 0; }
    
    // TODO 8: 手动实现分析和调试方法
    public void printInternalState() {
        System.out.println("=== HashMap内部状态 ===");
        System.out.println("Size: " + size);
        System.out.println("Capacity: " + (table == null ? 0 : table.length));
        System.out.println("Threshold: " + threshold);
        System.out.println("Load Factor: " + loadFactor);
        
        if (table != null) {
            int usedBuckets = 0;
            int maxChainLength = 0;
            int totalChainLength = 0;
            
            for (int i = 0; i < table.length; i++) {
                if (table[i] != null) {
                    usedBuckets++;
                    int chainLength = 0;
                    Node<K, V> node = table[i];
                    while (node != null) {
                        chainLength++;
                        node = node.next;
                    }
                    maxChainLength = Math.max(maxChainLength, chainLength);
                    totalChainLength += chainLength;
                }
            }
            
            System.out.println("Used Buckets: " + usedBuckets + "/" + table.length);
            System.out.println("Max Chain Length: " + maxChainLength);
            System.out.println("Avg Chain Length: " + (usedBuckets > 0 ? (double) totalChainLength / usedBuckets : 0));
        }
    }
    
    public void printDistribution() {
        if (table == null) return;
        
        System.out.println("=== 哈希分布情况 ===");
        for (int i = 0; i < table.length; i++) {
            if (table[i] != null) {
                int count = 0;
                Node<K, V> node = table[i];
                StringBuilder chain = new StringBuilder();
                
                while (node != null) {
                    count++;
                    chain.append(node.key).append("(").append(node.hash).append(")");
                    if (node.next != null) chain.append(" -> ");
                    node = node.next;
                }
                
                System.out.println("Bucket[" + i + "] (" + count + "): " + chain.toString());
            }
        }
    }
}

// TODO 9: 手动实现性能测试
class HashMapPerformanceTest {
    
    public static void testBasicOperations() {
        System.out.println("=== 基本操作测试 ===");
        
        CustomHashMap<String, Integer> map = new CustomHashMap<>();
        
        // 测试PUT操作
        System.out.println("1. 测试PUT操作");
        for (int i = 0; i < 20; i++) {
            map.put("key" + i, i);
            if (i % 5 == 4) {
                map.printInternalState();
            }
        }
        
        // 测试GET操作
        System.out.println("\n2. 测试GET操作");
        long startTime = System.nanoTime();
        for (int i = 0; i < 20; i++) {
            Integer value = map.get("key" + i);
        }
        long endTime = System.nanoTime();
        System.out.println("GET操作时间: " + (endTime - startTime) + " ns");
        
        // 显示分布情况
        System.out.println("\n3. 哈希分布情况");
        map.printDistribution();
    }
    
    public static void testHashCollisions() {
        System.out.println("\n=== 哈希冲突测试 ===");
        
        CustomHashMap<String, Integer> map = new CustomHashMap<>(4); // 小容量容易产生冲突
        
        // 故意创造冲突
        String[] keys = {"key1", "key17", "key33", "key49"}; // 这些key在小表中可能冲突
        
        for (int i = 0; i < keys.length; i++) {
            map.put(keys[i], i);
            System.out.println("添加 " + keys[i] + " 后:");
            map.printInternalState();
            map.printDistribution();
            System.out.println();
        }
    }
    
    public static void testResizeBehavior() {
        System.out.println("=== 扩容行为测试 ===");
        
        CustomHashMap<Integer, String> map = new CustomHashMap<>(2, 0.75f);
        
        System.out.println("初始状态:");
        map.printInternalState();
        
        // 观察扩容过程
        for (int i = 0; i < 10; i++) {
            map.put(i, "value" + i);
            System.out.println("\n添加第 " + (i + 1) + " 个元素后:");
            map.printInternalState();
        }
    }
}

// TODO 10: 主函数和综合演示
class HashMapDemo {
    public static void main(String[] args) {
        System.out.println("🗂️ 自制HashMap实验室");
        System.out.println("目标: 深度理解哈希表的内部机制\n");
        
        // 测试1: 基本操作
        HashMapPerformanceTest.testBasicOperations();
        
        // 测试2: 哈希冲突
        HashMapPerformanceTest.testHashCollisions();
        
        // 测试3: 扩容行为
        HashMapPerformanceTest.testResizeBehavior();
        
        System.out.println("\n🎯 学习总结:");
        System.out.println("1. HashMap基于数组+链表实现");
        System.out.println("2. 哈希函数决定分布均匀性");
        System.out.println("3. 负载因子平衡时间和空间效率");
        System.out.println("4. 扩容时需要重新哈希所有元素");
        System.out.println("5. 链表过长时会转换为红黑树");
    }
}
```

**📋 实践步骤清单** (必须逐项完成):
- [ ] 📝 手动创建文件 `CustomHashMap.java`
- [ ] ⌨️ 逐行手动输入所有核心代码
- [ ] 🔧 实现Node数据结构和哈希算法
- [ ] 🔧 实现PUT操作和冲突处理
- [ ] 🔧 实现GET操作和链表遍历
- [ ] 🔧 实现扩容机制和重新哈希
- [ ] 🔧 实现分析和调试工具
- [ ] 🏃 运行所有测试场景
- [ ] 📊 分析哈希分布和冲突情况
- [ ] 📝 观察扩容过程和性能影响

**✅ Primary检查点验证**:
1. **哈希冲突**: 能观察到链表形成和冲突处理吗？
2. **扩容机制**: 理解为什么要重新哈希所有元素吗？
3. **性能特征**: 能解释为什么HashMap查找是O(1)吗？

**🎯 置信度自评**: 
我理解HashMap的内部实现机制： □ 完全掌握 □ 基本掌握 □ 需要练习

---

<function_calls>
<invoke name="TodoWrite">
<parameter name="todos">[{"id": "1", "content": "Apply framework to Chapter 1 - Java/Kotlin fundamentals", "status": "completed"}, {"id": "2", "content": "Create detailed concurrent programming exercises", "status": "completed"}, {"id": "3", "content": "Design collection framework hands-on labs", "status": "completed"}, {"id": "4", "content": "Build Kotlin coroutines practical project", "status": "in_progress"}, {"id": "5", "content": "Create comprehensive integration project", "status": "pending"}]