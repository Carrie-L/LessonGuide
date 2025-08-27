import java.util.concurrent.atomic.AtomicReference

// 目标：实现一个完全无锁的栈
class LockFreeStack<T> {
    private val head = AtomicReference<Node<T>?>(null)
    
    private data class Node<T>(
        val value: T,
        val next: Node<T>?
    )
    
    fun push(value: T): Boolean {
        val newNode = Node(value, null)
        
        // TODO 1: 亲手实现CAS循环推入逻辑
        while (true) {
            val currentHead = head.get()
//            newNode.next = currentHead
            // 亲手实现这里的逻辑
        }
    }
    
    fun pop(): T? {
        // TODO 2: 亲手实现CAS循环弹出逻辑
        while (true) {
            val currentHead = head.get()
            if (currentHead == null) {
                return null
            }
            
            // 亲手实现这里的逻辑
        }
    }
    
//    fun peek(): T? {
//        // TODO 3: 亲手实现无锁查看逻辑
//    }
//
//    fun size(): Int {
//        // TODO 4: 亲手实现大小计算(注意线程安全)
//    }
}

// 解决ABA问题的版本
class VersionedLockFreeStack<T> {
    private val head = AtomicReference<VersionedNode<T>?>(null)
    
    private data class VersionedNode<T>(
        val value: T,
        val next: VersionedNode<T>?,
        val version: Long
    )

    // TODO 5: 亲手实现带版本号的推入
//    fun push(value: T): Boolean {
//
//    }
}