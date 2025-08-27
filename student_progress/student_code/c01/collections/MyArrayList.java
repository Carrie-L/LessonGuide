package collections;

import java.util.Arrays;

public class MyArrayList {

    private Object[] elements; // 底层数组，用来存储元素
    private int size = 0; // 当前存储的元素个数
    private static final int DEFAULT_CAPACITY = 10; // 默认初始容量

    public MyArrayList() {
        elements = new Object[DEFAULT_CAPACITY];
    }

    public int size() {
        return size();
    }

    // 1. 添加元素的核心逻辑
    public boolean add(T element) {
        ensureCapacity(size + 1); // 确保容量至少能容纳 size + 1 个元素
        elements[size++] = element; // 放入元素，然后size自增
        return true;
    }

    // 2. 实现动态扩容的逻辑
    // 判断是否需要扩容
    public void ensureCapacity(int minCapacity) {
        // 如果需要的最小容量 > 当前数组的长度，就调用grow()去扩容
        if (minCapacity > elements.length) {
            grow();
        }
    }

    // 3. 实现数组的扩容和复制
    private void grow() {
        // 实现创建新数组和复制旧数据的逻辑
        // 扩容到1.5倍. oldCapacity >> 1 是位运算，等效于 oldCapacity/2，但效率更高
        int oldCapacity = elements.length;
        int newCapacity  = oldCapacity + (oldCapacity >> 1);

        // 使用Arrays.copyOf 进行数组扩容和内容复制
        elements = Arrays.copyOf(elements, newCapacity);

    }

    // 4. 实现获取元素的逻辑
    public T get(int index) {
        // 实现索引检查和元素返回
        ;
        return null; // 临时返回值
    }

    // 5. 实现删除元素的逻辑
    public T remove(int index) {
        // 实现元素的移除和数组的整理
        return null;
    }

    // 辅助方法： 检查索引是否越界
    private void rangeCheck(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index);
        }
    }


}
