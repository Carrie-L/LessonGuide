
首席面试官的剧本：解构安卓面试“八股文”


引言：面试“八股文”的哲学——我为什么要问这些？

你好，作为一名在字节跳动、抖音、剪映、腾讯、百度、比亚迪、大疆都担任过技术面试官，并最终走到首席面试官位置的工程师，我见过形形色色的候选人。我深知，许多人对所谓的“八股文”面试题心存芥蒂，认为这是对记忆力的考验，而非对真实能力的评估。今天，我想从我的视角，为你揭示这些经典问题背后的真正意图。
我们之所以反复提问这些看似固定的问题，并非为了让你背诵标准答案。恰恰相反，这些问题是经过千锤百炼的“探针”，旨在快速、高效地探测你知识体系的深度、广度和结构。一个看似孤立的问题，比如“请解释一下HashMap的原理”，在一位优秀的工程师口中，可以延展成一幅涵盖数据结构、算法复杂度、并发控制、内存模型乃至系统设计权衡的宏大画卷。这，就是区分普通与卓越工程师的关键所在。
本指南的目的，不是给你一份答案清单，而是帮助你围绕这些核心问题，构建一个关于安卓平台的坚实心至模型。当你真正理解了安卓系统各个模块（如Runtime、Framework、UI）的设计初衷、演进脉络以及它们之间的协同关系后，任何“八股文”都将不再是障碍，而是你展示技术深度、工程思维和解决问题能力的绝佳舞台。让我们开始吧。

第一章：基石篇 - Java & Kotlin 语言精要

语言是工程师的武器。在安卓开发这个战场上，Java和Kotlin是我们最核心的工具。对它们的掌握程度，直接决定了你构建的应用是否坚固、高效和可维护。在面试中，我通常会从并发和集合这两个最能体现语言功底的领域切入。

1.1 并发原语：synchronized, volatile 与 JMM

并发编程是衡量一位工程师能力的重要标尺。synchronized 和 volatile 是Java并发包的基石，对它们的理解深度，直接反映了你对并发问题本质的认知。

核心概念与底层原理

synchronized 是Java提供的原生锁机制，用于保证代码块的原子性和线程间的可见性。它的底层实现依赖于每个Java对象都关联的一个monitor对象。当一个线程进入synchronized代码块时，它会尝试获取monitor的锁。
更深层次地，为了优化性能，JVM在JDK 1.6之后为synchronized引入了锁升级机制。这个过程体现了JVM设计者的一种核心思想：乐观假设。即“锁竞争在大多数情况下是不存在的”，然后随着竞争的加剧，逐步升级锁的粒度和开销。这个过程如下 1：
偏向锁 (Biased Locking)：当一个线程首次获得锁时，锁会“偏向”这个线程。该线程后续再次进入同步代码块时，无需进行任何同步操作，极大地降低了无竞争情况下的开销。
轻量级锁 (Lightweight Locking)：当另一个线程尝试获取这个偏向锁时，偏向锁会升级为轻量级锁。此时，线程不会被阻塞，而是通过自旋（CAS操作）的方式尝试获取锁。这适用于锁占用时间极短的场景。
重量级锁 (Heavyweight Locking)：如果自旋一定次数后仍无法获取锁，或者有更多线程加入竞争，轻量级锁就会升级为重量级锁。此时，未能获取锁的线程将被阻塞，并进入等待队列，直到锁被释放后由操作系统唤醒。这涉及到用户态到内核态的切换，开销最大。
volatile 关键字则扮演着不同的角色。它是一个轻量级的同步机制，主要有两个作用：
保证可见性 (Visibility)：当一个线程修改了被volatile修饰的变量，这个修改会立即被刷新到主内存中，并且其他线程在读取该变量时，会从主内存中重新加载，从而保证能看到最新的值。
禁止指令重排序 (Reordering)：volatile通过内存屏障（Memory Barrier）来防止编译器和处理器为了优化性能而对指令进行重排序，确保代码的执行顺序与程序员的预期一致。
这两个作用的理论基础是Java内存模型（JMM）中的happens-before原则。对一个volatile变量的写操作，happens-before于后续对这个变量的读操作。

对比分析与面试官视角

在面试中，一个经典的问题是：“synchronized 和 volatile 有什么区别？” 2。一个合格的回答应该至少包含以下几点：
原子性：synchronized保证代码块的原子性，而volatile不保证复合操作的原子性（例如 i++）。
可见性：两者都保证可见性，但实现机制不同。synchronized通过释放锁时刷新主内存，获取锁时清空工作内存来实现；volatile通过更轻量级的内存屏障实现。
使用场景：synchronized用于保护需要原子性访问的共享资源；volatile用于实现线程间的通信，例如标记一个状态位的变化。
作为面试官，我更期待你从设计的角度去思考。volatile的存在是为了在不引入重量级锁的情况下解决多线程间的通信问题。它与synchronized并非替代关系，而是针对不同并发场景的工具。理解这一点，才能在实际开发中做出正确的选择。
面试追问示例：
“既然有了synchronized，为什么还需要volatile？反之亦然？”
“谈谈你对CAS（Compare-And-Swap）的理解，它和synchronized的锁升级有什么关系？”（提示：轻量级锁的自旋就是基于CAS实现的）
“JDK高版本（如JDK 15）为何要逐步废弃偏向锁？”（提示：因为在现代高并发应用和容器化环境中，锁竞争变得更加普遍，偏向锁的撤销开销反而成了性能负担）。

1.2 主力集合：HashMap & ConcurrentHashMap 源码剖析

HashMap是Java中使用最频繁的集合类，没有之一。对它的源码级理解，是每个高级工程师的必备技能。

HashMap 的演进

HashMap的内部实现并非一成不变，其在JDK 1.7和1.8之间有一次重大的升级，这也是面试中的高频考点 3。
JDK 1.7 实现：结构为“数组 + 链表”。当发生哈希冲突时，新的键值对会通过头插法的方式加入到对应数组索引的链表中。这种实现在多线程环境下进行put操作时，可能会因为链表成环而导致死循环。
JDK 1.8 实现：结构升级为“数组 + 链表 / 红黑树”。当链表长度超过一个阈值（TREEIFY_THRESHOLD，默认为8）且数组容量大于64时，链表会转换为红黑树。
这次升级的核心动机，是为了解决极端情况下的性能问题。当哈希冲突严重导致链表过长时，HashMap的查询复杂度会从理想的 O(1) 退化到 O(n)。而红黑树能将最坏情况下的查询复杂度稳定在 O(logn)。这体现了在工程设计中，必须同时考虑平均情况和最坏情况的鲁棒性。
在解释put和get流程时，必须清晰地描述hashCode()和equals()方法的核心作用：hashCode()用于快速定位到数组的索引（桶），而equals()则用于在链表或红黑树中精确地找到目标键 4。

ConcurrentHashMap 的并发控制演进

从HashMap到ConcurrentHashMap，再到ConcurrentHashMap自身的版本迭代，其核心演进路线是锁粒度的不断细化，这是为了在多核CPU时代最大化并发性能的必然选择。
JDK 1.7 实现：采用**分段锁（Segment）**机制。ConcurrentHashMap内部由一个Segment数组构成，每个Segment本身就是一个类似HashMap的结构，并继承自ReentrantLock。当对Map进行写操作时，只需要锁定键所对应的那个Segment，而不是整个Map。这使得并发度理论上可以达到Segment的数量（默认为16）7。
JDK 1.8 实现：放弃了Segment的笨重设计，转而采用更细粒度的**CAS + synchronized + 红黑树**方案。其写操作的并发控制逻辑如下 9：
如果数组的某个索引位置为空，则使用CAS操作尝试写入新的节点。
如果该位置已经有节点，则对该位置的头节点使用synchronized加锁。
加锁后，再进行后续的链表或红黑树操作。
这种设计将锁的粒度从一个Segment（包含多个桶）缩小到了一个哈希桶的头节点，极大地提升了并发性能。同时，get操作几乎是无锁的，通过volatile来保证Node节点中值的可见性，从而实现了高效的并发读取 10。
面试追问示例：
“为什么HashMap的容量（数组长度）必须是2的幂次方？”（提示：为了通过位运算 (n - 1) & hash 来代替取模运算 %，效率更高，且能保证哈希值均匀分布）。
“ConcurrentHashMap 1.8的get操作为什么几乎是无锁的，它如何保证能读到最新的值？”
“让你自己设计一个线程安全的Map，你会怎么做？谈谈你的设计思路和权衡。”

1.3 现代范式：Kotlin 协程

Kotlin协程是Android官方推荐的异步编程解决方案。它并非“银弹”，而是一种协作式多任务模型，与线程的抢占式多任务模型有本质区别。理解这一点，才能避免误用协程。

核心区别与优势

协程与线程的本质区别在于，协程是用户态的、轻量级的“线程”，它们的调度由应用程序自己控制，而不是操作系统内核。这带来了几个核心优势 11：
轻量级：创建和销毁协程的开销远小于线程。可以在单个线程上运行成千上万个协程。
挂起而非阻塞：当协程遇到一个耗时操作（如网络请求）时，它会挂起（suspend），将底层线程释放给其他协程使用，而不是**阻塞（block）**整个线程。这极大地提高了线程的利用率。
结构化并发：协程的生命周期与一个CoroutineScope绑定。当scope被取消时，其中启动的所有协程都会被自动取消，这从根本上解决了异步任务的生命周期管理和内存泄漏问题。
简化代码：协程可以将异步的回调式代码，写成看似同步的顺序代码，极大地提高了代码的可读性和可维护性 12。

核心组件与主线程安全

CoroutineScope: 定义了协程的生命周期范围。例如，在Android中，viewModelScope和lifecycleScope就是与ViewModel和Lifecycle绑定的scope。
Job: 协程的句柄，可以用来控制协程的生命周期（如取消）。
CoroutineContext: 协程的上下文，包含了Job、调度器等元素。
Dispatchers: 调度器，决定了协程在哪个线程或线程池上执行。
Dispatchers.Main: Android主线程，用于UI操作。
Dispatchers.IO: 专门为I/O密集型任务优化的线程池。
Dispatchers.Default: 专门为CPU密集型任务优化的线程池。
通过withContext(Dispatchers.IO)这样的函数，我们可以非常方便地将一段代码的执行切换到后台线程，并在完成后自动切回原来的线程，从而保证主线程安全 11。
协程的出现，是语言层面为解决异步编程复杂性提供的高级抽象。它将线程切换、回调、取消、异常处理等繁琐细节封装起来，让开发者更专注于业务逻辑。这代表了编程语言向着更高级、更安全的并发模型发展的趋势。
面试追问示例：
“协程的suspend关键字底层是如何实现的？”（提示：编译器会将其转换为一个状态机）。
“launch和async有什么区别？分别在什么场景下使用？”（提示：launch返回Job，用于“发射后不管”的任务；async返回Deferred，用于需要获取返回值的任务）。
“请解释一下协程的取消机制和异常传播机制。”

第二章：支柱篇 - 解构安卓框架内核

理解Android框架的内核机制，是区分中高级工程师和初级工程师的分水岭。这些机制是整个平台的骨架，决定了应用的运行效率、响应速度和交互逻辑。

2.1 运行时引擎：ART 的混合编译模式

应用的执行效率，从根本上取决于运行时（Runtime）。Android的运行时从Dalvik演进到ART，其核心变化在于编译策略。

ART vs. Dalvik

Dalvik虚拟机主要依赖JIT（Just-In-Time，即时编译）。当应用运行时，JIT会把频繁执行的Dalvik字节码编译成本地机器码，以提高后续执行速度。但这意味着应用每次冷启动时，性能都相对较差。
ART（Android Runtime）则引入了AOT（Ahead-Of-Time，预先编译）。在应用安装时，ART会使用dex2oat工具将应用的DEX文件直接编译成包含本地机器码的OAT文件。这使得应用启动和运行速度更快，但代价是安装时间更长，并且占用的存储空间更大 13。

混合模式的演进

纯AOT或纯JIT都有其弊端。因此，从Android 7.0开始，ART采用了一种更智能的混合编译模式，它结合了解释执行、JIT和基于配置文件的AOT，旨在应用性能、安装时间和存储空间这三个相互制约的因素之间寻求最佳平衡 14。
在Pixel设备上，这个流程通常是这样的 14：
安装时 (AOT)：应用通过Play商店安装时，会附带一个云配置文件（Cloud Profile）。这个文件基于大量用户的匿名运行数据，指明了应用中的“热点方法”。ART会根据这个配置文件，对这些热点方法进行AOT编译，从而优化首次启动速度。
首次运行 (解释执行 + JIT)：对于未被AOT编译的方法，应用首次运行时会通过解释器执行。对于其中频繁执行的方法，JIT编译器会介入，将其编译成本地代码。
运行时 (生成本地配置)：在应用运行过程中，ART会记录下实际被执行的热点方法，生成一个本地配置文件。
设备空闲时 (Profile-guided AOT)：当设备空闲并充电时，一个后台的编译守护进程会运行。它会结合云配置文件和本地配置文件，对应用进行更全面的AOT编译，生成更优化的代码。
这种混合模式是一种动态的、基于实际使用数据的自适应优化策略。其中，云配置文件的引入，本质上是利用大数据来优化单个设备的性能，是一种典型的云端赋能端侧的工程思想。
面试追问示例：
“ART的AOT编译产物（OAT文件）是什么格式？它和DEX文件是什么关系？”
“这种混合编译模式对开发者意味着什么？我们能做些什么来配合这种优化？”（提示：模块化、使用启动性能分析工具如Perfetto）。

2.2 四大组件深度：Activity 启动模式

Activity的启动模式是Android应用导航的基石。对它的理解，不仅是技术细节的考察，更是对你是否能从用户体验角度思考问题的检验。

任务栈（Task & Back Stack）

理解启动模式的前提是理解任务（Task）和返回栈（Back Stack）。一个任务是用户为了完成某个操作而交互的一系列Activity的集合。这些Activity被组织在一个后进先出（LIFO）的栈中，即返回栈 15。

四种启动模式

这四种模式定义了当通过Intent启动一个Activity时，系统如何创建和管理其实例 15：
standard (默认)：每次启动都会创建一个新的Activity实例，并将其压入启动它的任务栈中。
singleTop：如果要启动的Activity实例已经位于当前任务栈的栈顶，则不会创建新实例，而是复用现有实例，并调用其onNewIntent()方法。如果不在栈顶，则行为与standard模式相同。适用于接收通知等场景，避免重复创建页面。
singleTask：系统会寻找是否存在一个与该Activity有相同taskAffinity的任务。如果存在，系统会将该任务调到前台，并清除该任务中位于目标Activity之上的所有Activity（clear top），然后调用目标Activity的onNewIntent()。如果不存在这样的任务，则会创建一个新任务，并将该Activity作为根Activity放入。这确保了该Activity在任务中只有一个实例。常用于应用的主页。
singleInstance：与singleTask类似，但更加极端。被此模式启动的Activity会独占一个任务栈，该任务栈中只会有这一个Activity实例。任何从这个Activity启动的其他Activity，都会在别的任务栈中打开。适用于系统级的、需要与应用任务栈完全隔离的页面，如来电显示界面。
对启动模式的滥用（尤其是singleTask和singleInstance）可能会严重破坏用户的导航体验。一个优秀的候选人不仅要懂技术细节，还要能从用户体验的角度去思考何时使用何种模式。
面试追问示例：
“请设计一个场景，分别说明singleTop和singleTask的应用，并解释为什么另一个不适用。”
“taskAffinity属性有什么作用？它和singleTask是如何配合工作的？”
“在onNewIntent()中需要注意什么？”（提示：getIntent()返回的仍然是旧的Intent，需要通过setIntent()更新）。

2.3 UI 渲染管线：Measure, Layout, Draw

屏幕上的每一个像素，都是经过Measure, Layout, Draw这三个阶段精心计算后才得以显示的。理解这个流程是UI性能优化的基础。

渲染三部曲

整个流程由ViewRootImpl的performTraversals方法驱动，它是一个典型的深度优先遍历过程 17：
Measure (测量)：从根View开始，自顶向下地递归调用每个View的measure()方法，确定每个View需要占据多大的空间（即测量宽度和测量高度）。这个过程的核心是MeasureSpec。
Layout (布局)：同样从根View开始，自顶向下地递归调用layout()方法，根据Measure阶段得到的大小，确定每个View在父容器中的具体位置（左、上、右、下四个坐标）。
Draw (绘制)：从根View开始，递归调用draw()方法。每个View负责绘制自己的内容，然后分发给子View进行绘制。

MeasureSpec 的奥秘

MeasureSpec是父容器向子View传递布局约束的核心机制。它是一个32位的整数，高2位代表模式，低30位代表尺寸。子View必须在父容器给定的约束下进行测量 17。
三种模式：
EXACTLY：精确模式。父容器已经为子View确定了精确的尺寸，子View应该使用这个尺寸。对应于LayoutParams中的match_parent或具体dp值。
AT_MOST：最大模式。子View的尺寸不能超过父容器指定的尺寸。对应于LayoutParams中的wrap_content。
UNSPECIFIED：不指定模式。父容器对子View没有任何限制，子View可以要多大就多大。通常用于ScrollView等可以滚动的容器。
过度嵌套的布局会导致Measure和Layout过程的重复计算，从而引发性能问题。ConstraintLayout的设计初衷之一就是为了减少布局层级，优化这个过程 18。
面试追问示例：
“自定义View时，为什么必须重写onMeasure？如果不重写，wrap_content会表现得和match_parent一样，为什么？”
“requestLayout(), invalidate(), 和 postInvalidate()有什么区别和联系？”（提示：requestLayout触发Measure和Layout；invalidate在UI线程触发Draw；postInvalidate在非UI线程触发Draw）。

2.4 事件分发机制

用户的每一次触摸，都会在View树中引发一场精妙的“旅行”。理解事件分发机制，是解决所有复杂滑动冲突的基础。

三大核心方法

这个机制是一个精巧的责任链模式的实现。事件从责任链的顶端（Activity）开始传递，链上的每个节点都有机会处理、拦截或传递事件 19。
dispatchTouchEvent(MotionEvent ev): 事件分发的入口。无论是Activity、ViewGroup还是View，都拥有此方法。它的返回值决定了事件是否被消费。
onInterceptTouchEvent(MotionEvent ev): ViewGroup特有的方法。用于判断是否要拦截事件，不让其继续传递给子View。返回true表示拦截，事件将交由自己的onTouchEvent处理；返回false表示不拦截。
onTouchEvent(MotionEvent ev): 处理事件的核心方法。返回true表示消费了该事件，事件流到此结束。返回false表示不消费，事件会向上传递给父View的onTouchEvent处理。

事件流追踪

以一个ViewGroup包含一个Button的场景为例，一个ACTION_DOWN事件的典型流程如下 19：
Activity.dispatchTouchEvent -> ViewGroup.dispatchTouchEvent
ViewGroup.dispatchTouchEvent 调用 ViewGroup.onInterceptTouchEvent。默认返回false（不拦截）。
事件继续传递给Button：Button.dispatchTouchEvent。
Button.dispatchTouchEvent 调用 Button.onTouchEvent。
由于Button是可点击的，其onTouchEvent会处理ACTION_DOWN并返回true，表示消费了该事件。
因为Button消费了DOWN事件，后续的MOVE和UP事件将不再经过ViewGroup的onInterceptTouchEvent，而是直接传递给Button的onTouchEvent进行处理。
面试追问示例：
“如果一个ViewGroup的onInterceptTouchEvent在ACTION_DOWN时返回false，但在ACTION_MOVE时返回true，会发生什么？”（提示：ViewGroup会夺取事件控制权，并向子View发送一个ACTION_CANCEL事件）。
“View的OnTouchListener的onTouch方法和onTouchEvent方法，哪个会先被调用？它们的返回值如何影响事件处理？”（提示：onTouch先被调用，如果它返回true，onTouchEvent将不会被调用）。

2.5 异步心跳：Handler, Looper, MessageQueue

Handler/Looper机制是Android UI框架单线程模型的基石。它确保了所有对UI的访问都在主线程上串行执行，从而避免了复杂的多线程同步问题。

核心组件与关系

这三个组件协同工作，构成了一个线程的消息循环系统 20：
MessageQueue: 一个消息队列，用于存放Handler发送的Message对象。
Looper: 消息循环器。每个线程最多只能有一个Looper。它的核心方法loop()会不断地从MessageQueue中取出消息并分发。
Handler: 消息处理器。它负责将Message发送到指定的Looper的MessageQueue中，并在Looper分发消息时，负责处理这些消息。
Android应用启动时，系统会为主线程（UI线程）自动创建一个Looper并开启消息循环。这就是为什么我们可以在任何地方创建new Handler(Looper.getMainLooper())来向主线程发送任务以更新UI 21。

经典问题：“Looper.loop()死循环为何不卡死主线程？”

这是一个经典且重要的问题。答案在于，这个“循环”并非空转消耗CPU。Looper.loop()的底层是基于Linux的epoll机制（一种高效的I/O多路复用技术）。当MessageQueue中没有消息时，主线程会进入阻塞休眠状态，让出CPU资源。只有当MessageQueue中有了新的消息，主线程才会被唤醒去处理。因此，它在空闲时是高效的。
正是因为主线程有Looper在不断循环处理消息，才使得**ANR (Application Not Responding)**的发生有了前提。如果一个消息（如点击事件）在主线程上处理时间过长，后续的UI绘制和输入事件消息就无法被及时处理，从而导致ANR。
面试追问示例：
“如何在子线程中创建和使用Handler？”（提示：需要先调用Looper.prepare()，处理完后调用Looper.loop()，最后别忘了调用looper.quit()）。
“Handler导致的内存泄漏是什么原因？如何解决？”（提示：非静态内部类持有外部类引用。解决方法：使用静态内部类+弱引用，或在onDestroy中移除所有消息）。
“Message对象是如何被复用的？”（提示：Message.obtain()和recycle()，内部维护了一个消息池）。

2.6 通信骨干：Binder IPC

Binder不仅是一种技术，更是Android组件化架构的基石。四大组件间的通信、应用与系统服务（AMS, WMS）的通信，都依赖于Binder。

核心原理：一次拷贝

Binder是Android特有的IPC（Inter-Process Communication）机制。相比于Linux传统的IPC（如管道、套接字），它最大的优势是高效。这种高效源于其独特的**内存映射（mmap）**机制，实现了“一次拷贝” 22。
传统IPC通常需要两次数据拷贝：
发送方：从用户空间拷贝到内核空间。
接收方：从内核空间拷贝到用户空间。
Binder则通过Binder驱动，在内核空间和接收方进程的用户空间之间建立一块共享内存。数据传输过程如下：
发送方将数据从用户空间拷贝一次到内核空间的Binder缓冲区。
接收方通过内存映射，可以直接访问这块内核缓冲区的数据，无需再次拷贝。
Android 8.0中引入的“分散-集中”优化，进一步减少了数据在打包成Parcel时的内部拷贝，使得数据传输效率更高 22。
选择Binder作为主要的IPC机制，是谷歌在性能和安全性之间做出的权衡。Binder不仅性能更高，还自带基于UID/PID的身份验证机制，为Android的沙箱安全模型提供了底层支持。
面试追问示例：
“谈谈Binder驱动在整个通信过程中的角色。”
“AIDL（Android Interface Definition Language）和Binder是什么关系？”（提示：AIDL是代码生成工具，帮助我们更方便地使用Binder进行跨进程方法调用）。
“为什么通过Binder传输的数据大小有限制（通常是1MB）？”（提示：为了防止滥用内核缓冲区，保证系统稳定性）。

第三章：蓝图篇 - 高级架构与三方库原理

如果说框架内核是地基，那么应用架构就是建筑的蓝图。一个好的架构能让应用易于维护、扩展和测试。同时，对主流三方库原理的理解，也能体现你是否善于“站在巨人的肩膀上”。

3.1 架构演进之旅：从MVC到Clean Architecture

在Android开发中，架构模式的演进，其背后的核心驱动力始终是解耦和可测试性。没有“最好”的架构，只有“最合适”的架构。一个资深工程师应该能够根据业务复杂度、团队规模、项目生命周期等因素，做出合理的架构选型决策。
Jetpack组件（如ViewModel, LiveData, Lifecycle）的推出，极大地推动了MVVM在Android社区的普及，这表明了官方框架对架构模式的强大影响力。同样，Jetpack Compose的声明式特性天然地契合MVI的单向数据流思想 23。
下表系统性地对比了主流的架构模式 23：
模式
核心思想
优点
缺点
适用场景
MVC
模型-视图-控制器分离
简单、熟悉
安卓中耦合严重，难测试
遗留项目，极简应用
MVP
通过Presenter解耦View和Model
可测试性高，职责清晰
接口多，样板代码，P-V紧耦合
需要高单元测试覆盖率的UI逻辑
MVVM
ViewModel+数据绑定，生命周期感知
解耦，可测试，生命周期安全
数据绑定有学习曲线，复杂场景状态管理难
谷歌官方推荐，绝大多数现代应用
MVI
单向数据流，不可变状态
状态可预测，易于调试，适合响应式UI
学习曲线陡峭，样板代码多，对简单场景过度设计
复杂、状态繁多的页面，响应式编程团队
Clean
严格分层，依赖倒置
业务逻辑独立，高可维护性、可测试性
结构复杂，样板代码极多
大型、长期演进的企业级应用

Clean Architecture尤其值得关注。它通过严格的分层（表现层、领域层、数据层）和依赖倒置原则（依赖关系总是指向内部），使得核心的业务逻辑（领域层）可以完全独立于Android框架，从而实现最高级别的可测试性和可维护性 23。
面试追问示例：
“在你的项目中，你是如何选择架构的？遇到了什么问题？”
“Clean Architecture中的领域层（Domain Layer）有什么核心价值？为什么它不应该依赖任何Android框架？”
“谈谈你对单向数据流（UDF）的理解。”

3.2 网络利器 OkHttp：拦截器责任链

OkHttp的强大之处在于其极高的可扩展性，而这一特性主要归功于它的拦截器（Interceptors）机制。该机制是责任链设计模式的经典应用 26。
通过拦截器，OkHttp将网络请求的各个关注点（如缓存、重试、日志、认证、数据压缩）解耦成一个个独立的模块。这使得框架本身保持简洁，同时又提供了极高的灵活性。

两种拦截器

OkHttp提供了两种类型的拦截器 27：
应用拦截器 (Application Interceptors)：通过addInterceptor()添加。它们位于责任链的最顶端，最先接触到用户的原始请求，最后接触到最终的响应。它们只会被调用一次，不关心重定向和重试等中间过程。适用于记录日志、添加通用Header等场景。
网络拦截器 (Network Interceptors)：通过addNetworkInterceptor()添加。它们位于责任链的底端，更接近真实的网络I/O。它们能够观察到网络传输的真实数据，并且在发生重定向或重试时，可能会被多次调用。适用于处理Gzip压缩、监控网络流量等场景。
责任链的传递依赖于chain.proceed(request)这个关键方法。每个拦截器在完成自己的任务后，调用此方法将请求传递给链上的下一个拦截器，直到最终发起网络请求 27。
面试追问示例：
“请描述一次网络请求在OkHttp内部大致经过了哪些默认的拦截器？”（提示：RetryAndFollowUpInterceptor, BridgeInterceptor, CacheInterceptor, ConnectInterceptor, CallServerInterceptor等）。
“如果要实现一个全局的请求加密/响应解密功能，你应该使用应用拦截器还是网络拦截器？为什么？”（提示：通常使用应用拦截器，因为它处理的是最原始的、未被修改的请求体和最终的、解压后的响应体）。

3.3 图片加载 Glide：多级缓存策略

Glide是Android上最受欢迎的图片加载库之一。它的核心优势在于其智能且高效的多级缓存机制，这是一个典型的空间换时间的性能优化案例 29。

缓存层次与读取顺序

Glide的缓存读取遵循一个清晰的优先级顺序，旨在最大化地避免最耗时、最昂贵的网络操作 30：
活动资源 (Active Resources)：使用弱引用缓存。这里存放的是正在使用中的图片。当图片不再被任何ImageView显示时，它就有可能被GC回收。
内存缓存 (Memory Cache)：使用**LruCache（Least Recently Used，最近最少使用算法）缓存。这里存放的是最近使用过**的图片。当缓存满时，会移除最久未被使用的图片。
磁盘缓存 (Disk Cache)：使用**DiskLruCache**实现。将图片文件持久化到设备存储中。
网络 (Network)：如果以上缓存都未命中，则从网络下载图片。

缓存设计细节

内存缓存的双重保障：同时使用弱引用和LruCache是一种精巧的设计。弱引用保证了正在显示的图片不会因为LruCache的淘汰策略而被回收，而LruCache则保留了那些刚刚显示过、很可能马上又会再次使用的图片，两者互为补充。
磁盘缓存的两种类型：DiskLruCache可以缓存两种数据 30：
SOURCE: 原始图片，即从网络下载的、未经任何处理的完整图片。
RESULT: 转换后的图片，即经过解码、缩放、变换后，最终要显示在ImageView上的图片。
默认情况下，Glide会根据情况智能选择缓存哪种类型，以在加载速度和磁盘空间之间取得平衡。例如，加载一张大图并显示为缩略图时，同时缓存SOURCE和RESULT，下次需要显示大图时就可以直接使用SOURCE，避免重新下载。
面试追问示例：
“Glide的Bitmap池（Bitmap Pool）有什么作用？它是如何减少内存抖动和GC的？”
“让你自己设计一个图片加载框架，你会如何设计它的缓存系统？需要考虑哪些因素？”
“Glide、Picasso、Fresco这几个主流框架在设计上有什么异同？”（提示：Fresco在Android 5.0以下使用匿名共享内存Ashmem来存储图片，可以有效避免OOM 29）。

3.4 组件化路由 ARouter：APT 与动态加载

随着应用规模的扩大，组件化成为必然选择。而原生的页面跳转方式（Intent）在组件化架构中存在明显弊端：模块间的强依赖、无法方便地进行跨模块跳转、参数传递复杂等。ARouter这类路由框架应运而生。
ARouter这类框架的精髓在于用编译期的自动化代码生成，来换取运行时的解耦。它将模块间的直接类依赖，转换为了对字符串路径的间接依赖，从而实现了模块间的物理隔离。

核心原理：APT

ARouter的核心技术是APT（Annotation Processing Tool，注解处理器）。
编译期：开发者在Activity类上添加@Route(path = "/test/activity")等注解。在项目编译时，APT会扫描到这些注解，并自动生成一些Java类。这些类中包含了路由表，即“路径字符串”到“Activity类”的映射关系。
运行时：当应用调用ARouter.getInstance().build("/test/activity").navigation()时，ARouter框架会加载编译期生成的路由表，根据路径找到对应的Activity.class，然后通过反射或ClassLoader来创建Intent并启动Activity。
APT技术的应用，使得路由框架的性能损耗主要集中在编译期，而运行时的性能开销相对较小（一次Map查找和一次反射），这是它能被广泛应用的关键。
注：由于提供的研究材料31至32与ARouter技术无关，本节内容基于行业通用知识和我的专业经验编写。
面试追问示例：
“ARouter是如何实现依赖注入（如自动填充Intent传递的参数）的？”（提示：同样是利用APT，在编译期生成辅助类来完成赋值操作）。
“除了页面路由，ARouter还能做什么？”（提示：通过IProvider接口实现跨模块的服务调用/接口发现）。
“APT技术在Android开发中还有哪些常见的应用？”（提示：Dagger2, ButterKnife, Room等）。

第四章：淬炼篇 - 线上性能与稳定性实战

对于一个资深工程师而言，写出功能只是基础，保证应用在线上环境高性能、高稳定地运行，才是真正的价值所在。排查ANR和OOM这类线上问题，是对工程师系统性问题定位能力的终极考验。

4.1 ANR 诊断与修复

ANR（Application Not Responding）是用户体验的“红线”。Google Play将用户感知的ANR率作为核心Vitals指标，直接影响应用的商店曝光度 33。

ANR 的定义与常见原因

ANR的本质是主线程被长时间阻塞，无法及时响应用户输入或系统消息。常见的触发条件包括 34：
输入事件（如点击、触摸）在5秒内未得到响应。
BroadcastReceiver的onReceive()方法在10秒（前台）或60秒（后台）内未执行完毕。
Service的关键生命周期方法在20秒内未执行完毕。
导致ANR的根本原因都是在主线程上执行了耗时操作，具体可归为几类 34：
耗时I/O：网络请求、数据库读写、文件操作。
复杂计算：大量的JSON解析、Bitmap处理、复杂的算法。
锁竞争：主线程等待其他线程持有的锁。
死锁：主线程与其他线程发生死锁。
Binder长调用：主线程同步调用一个耗时的Binder方法。

排查思路与工具

一个优秀的回答不应止于“把耗时操作放子线程”，而应包含监控、定位、分析、解决、验证的完整闭环。
监控：使用Android Vitals平台，在Google Play管理中心监控线上的ANR发生率、聚类和受影响的用户数 33。这是定位线上问题的第一步。
定位：当ANR发生时，系统会在/data/anr/目录下生成一个traces.txt文件。这个文件记录了ANR发生时所有线程的堆栈信息。可以通过adb命令将其拉取到本地。
分析：分析traces.txt文件的核心是找到**主线程（main thread）**的堆栈。
查看主线程的状态。如果是RUNNABLE，说明它正在执行耗时代码，直接看堆栈就能找到问题。
如果是BLOCKED或WAITING，说明它在等待某个锁或条件，需要找到持有锁的那个线程，分析它为什么长时间不释放锁。
有时主线程堆栈看起来是空闲的，这可能是因为ANR是由其他组件（如广播接收器、Content Provider）超时引起的，或者系统负载过高导致进程调度问题 35。
面试追问示例：
“在traces.txt文件中，你主要关注哪些线程的状态？”
“如果主线程堆栈看起来是空闲的，可能是什么原因导致的ANR？”
“除了Android Vitals和手动分析trace文件，你还知道哪些ANR监控和分析工具？”（提示：StrictMode, TraceView, Perfetto, 第三方APM平台）。

4.2 OOM 追猎与内存优化

OOM（OutOfMemoryError）是另一类严重的稳定性问题。解决OOM问题的核心是数据驱动。盲目地猜测和修改代码是低效的。只有通过对线上数据的分析和对内存Dump文件的精确解剖，才能找到问题的根源。

OOM 的本质与实战方法论

OOM并非指设备的物理内存耗尽，而是指应用进程向JVM申请堆内存时，已分配的堆内存加上本次申请的内存，超过了系统为该进程设定的堆内存上限（Heap Limit）。
借鉴美团等大厂的最佳实践，解决线上OOM问题通常遵循一套系统方法 36：
挖掘特征：聚合线上的OOM日志（例如通过APM系统），分析其在机型、系统版本、业务场景、堆栈信息上的分布特征。这有助于缩小问题范围，例如发现某个OOM主要集中在低内存的OPPO手机上 36。
稳定复现：根据挖掘出的特征，搭建与线上环境相似的测试环境。设计内存压力测试场景（例如，在目标页面上反复进行消耗内存的操作），以稳定地复现OOM。
分析Dump文件：在OOM即将发生时，使用Android Studio的Profiler或adb命令抓取应用的hprof内存快照文件。然后使用**MAT (Memory Analyzer Tool)**进行离线分析。

MAT 使用技巧

MAT是分析Java堆内存问题的神器。核心功能包括 36：
直方图 (Histogram)：显示内存中所有类的实例数量和大小，可以快速找到占用内存最多的对象类型（如byte, Bitmap）。
支配树 (Dominator Tree)：显示对象间的支配关系。如果对象A支配对象B，意味着B要被GC，必须先回收A。支配树可以帮助快速找到持有大量内存的关键对象。
查找GC Roots路径 (Path to GC Roots)：这是定位内存泄漏最关键的功能。它可以展示一个对象为什么没有被GC回收的完整引用链。通过分析这条链，就能找到泄漏的源头。
面试追问示例：
“在MAT中，Shallow Heap和Retained Heap有什么区别？”（提示：Shallow Heap是对象自身大小，Retained Heap是该对象被回收后，能连带释放的总内存大小）。
“请举例说明几种常见的Android内存泄漏场景，并解释其原因。”（例如：Handler非静态内部类、静态变量持有Activity Context、资源未关闭等）。
“除了OOM，你还关注哪些内存指标？”（提示：PSS、内存抖动、GC频率等）。

4.3 APK 打包与安装流程

理解应用的构建和分发流程，是进行编译优化和包体积优化的基础。

打包流程

一个Android项目从源码到最终的APK，大致经历以下步骤 37：
资源编译 (AAPT2)：使用AAPT2（Android Asset Packaging Tool）编译所有资源文件（XML布局、图片等），生成R.java文件和编译后的资源文件。
代码编译：使用javac将Java源码编译成.class文件，使用kotlinc将Kotlin源码编译成.class文件。
DEX化 (D8/R8)：使用D8编译器将所有的.class文件（包括三方库的）转换成一个或多个.dex文件。在这个阶段，R8工具会进行代码的压缩（移除无用代码）、混淆（重命名）和优化。
打包 (APK Packager)：将编译后的资源、.dex文件、so库、签名文件等所有内容，打包成一个未签名的APK文件。
签名 (apksigner)：使用你的私钥对APK进行签名，以保证应用的来源和完整性。
对齐 (zipalign)：对签名后的APK进行对齐操作，优化内存使用。

签名机制演进

V1 (Jar Signature)：基于JAR文件的签名方式，只校验每个文件的内容，不校验APK整体。安装时需要解压校验，速度慢，且存在安全漏洞（Janus漏洞）。
V2 (Full APK Signature)：Android 7.0引入。对整个APK文件的二进制内容进行签名校验，而不是单个文件。安装速度更快，安全性更高。
V3 (APK Key Rotation)：Android 9.0引入。在V2的基础上，增加了密钥轮换的支持，允许应用在更新时更换签名密钥。
面试追问示例：
“V2签名相比V1签名，在安全性和安装速度上有什么优势？”
“什么是AAB（Android App Bundle）？它和APK有什么关系？它如何帮助优化应用分发？”（提示：AAB是发布格式，Google Play会根据用户设备配置，动态生成最优化的APK进行下发）。
“ProGuard和R8有什么关系？”（提示：R8是下一代的代码压缩和混淆工具，整合了ProGuard的功能，并且性能更好）。

第五章：地平线 - 掌握现代安卓UI

Jetpack Compose正在引领Android UI开发的范式革命。从命令式转向声明式，这不仅仅是API的改变，更是思维方式的转变。

5.1 Jetpack Compose 核心思想


声明式UI vs 命令式UI

传统Android开发使用XML布局，是一种命令式UI。你需要手动获取View实例（findViewById），然后通过调用方法（如textView.setText()）来命令式地修改UI。
Jetpack Compose则是一种声明式UI。你不再关心“如何”修改UI，而只专注于描述在某个特定状态下，UI应该是什么样子。当状态变化时，Compose框架会自动、高效地更新UI，使其与新状态保持一致。

组合 (Composition) 与重组 (Recomposition)

Compose的核心工作机制可以概括为两个过程 38：
组合 (Composition)：当你调用一个@Composable函数时，Compose会执行它，并生成一份描述UI的树状结构。这个过程称为“组合”。首次执行称为“初始组合”。
重组 (Recomposition)：当一个被@Composable函数读取的**State**对象的值发生变化时，Compose会智能地找到所有读取了该State的@Composable函数，并重新执行它们，以更新UI树中受影响的部分。这个过程称为“重组”。
Compose的重组机制是其性能的关键。它不是全量刷新UI，而是进行智能的、局部的更新。一个优秀的Compose开发者必须理解并遵循其规则（如可组合函数应是幂等的、无副作用的），以编写出高效的UI代码。
声明式UI范式天然地要求UI是状态的纯函数（UI=f(State)）。这种思想直接催生了MVI等单向数据流架构在Compose生态中的流行。

5.2 Compose 中的状态管理：状态提升

在Compose中，如何有效地管理状态，是构建可维护应用的核心。**状态提升（State Hoisting）**是其中最重要、最基础的设计模式 38。

核心概念与优势

状态提升是一种将状态从子组件移动到它们的公共父组件中，使得子组件变为**无状态（stateless）**的模式。一个无状态的组件，其显示内容完全由传入的参数决定，自身不持有任何可变状态。
实现方式通常是将子组件中的状态变量，例如：
val text by remember { mutableStateOf("") }
替换为一对参数：
value: String 和 onValueChange: (String) -> Unit
这样做带来了巨大的好处 38：
单一数据源：状态由唯一的父组件持有，避免了状态不一致的问题。
可复用性：无状态的组件不与任何特定的状态存储方式耦合，可以在任何地方复用。
可测试性：测试一个无状态组件变得非常简单，只需传入不同的参数，断言其UI输出即可。
解耦：UI组件只负责显示，状态变化的逻辑由父组件（通常是ViewModel）处理，职责清晰。
状态提升并非Compose独创，它是在声明式UI范式下，解决状态共享和逻辑解耦问题的通用模式。它与MVVM中将状态放在ViewModel中的思想异曲同工，都是为了让UI组件本身保持“纯粹”。
面试追问示例：
“remember和rememberSaveable有什么区别？”（提示：remember只在重组间保持状态，rememberSaveable可以在Activity或进程重建后（如屏幕旋转）恢复状态）。
“当状态非常复杂时，除了状态提升，还有什么模式可以用来管理状态？”（提示：创建专门的“状态容器类”，将相关的状态和逻辑封装在一起）。
“Compose中的Side-effect（副作用）是什么？请举例说明几个处理副作用的API（如LaunchedEffect, DisposableEffect）。”

结论：超越技术问题——首席工程师的特质

至此，我们已经深入探讨了安卓面试中一系列核心的技术问题。然而，作为一名首席面试官，当我在寻找团队未来的技术骨干时，我关注的远不止于此。技术深度固然重要，但在顶尖公司，我们更看重候选人身上体现出的软实力和工程素养。
问题分解能力：面对一个模糊而复杂的系统设计题，你是否能主动提问、澄清需求，并将其拆解成更小、更易于管理和讨论的模块？
系统性思维：你是否能看到一个技术点在整个系统中的位置和影响？例如，在讨论缓存设计时，你是否能考虑到它对内存、磁盘、网络乃至用户体验的综合影响，并清晰地阐述你设计背后的权衡（trade-off）？
沟通与协作：你是否能用清晰、准确的语言表达自己的想法？在面对质疑时，你是否能建设性地进行讨论，而不是固执己见？
工程热情：你是否对技术抱有持续的好奇心和追求卓越的精神？你是否关注行业动态，并主动学习新的技术？
这些特质，与扎实的技术基础相结合，共同构成了一位“首席工程师”的画像。它们决定了一个工程师能走多远，能达到怎样的高度。
希望这份指南能帮助你更好地准备面试，但更重要的是，希望它能启发你去进行更深层次的思考，不仅仅是“知道它是什么”，更是“理解它为什么是这样”。祝你在面试中取得优异的成绩。
引用的著作
【Java】Synchronized实现原理与常见面试题- 周二鸭 - 博客园, 访问时间为 八月 16, 2025， https://www.cnblogs.com/jojop/p/14022029.html
Java并发常见面试题总结（中） | JavaGuide, 访问时间为 八月 16, 2025， https://javaguide.cn/java/concurrent/java-concurrent-questions-02.html
How a Java HashMap internal implementation works | TheServerSide, 访问时间为 八月 16, 2025， https://www.theserverside.com/video/How-a-Java-HashMap-internal-implementation-works
Internal Working of HashMap in Java - GeeksforGeeks, 访问时间为 八月 16, 2025， https://www.geeksforgeeks.org/java/internal-working-of-hashmap-java/
Internal Working of HashMap in Java and Performance Improvement in Java 8 - Medium, 访问时间为 八月 16, 2025， https://medium.com/@yashodhara.chowkar/internal-working-of-hashmap-in-java-and-performance-improvement-in-java-8-a28ee1660cda
A Guide to Java HashMap - Baeldung, 访问时间为 八月 16, 2025， https://www.baeldung.com/java-hashmap
ConcurrentHashMap原理分析- 平凡希- 博客园, 访问时间为 八月 16, 2025， https://www.cnblogs.com/xiaoxi/p/7474026.html
Java并发编程笔记之ConcurrentHashMap原理探究- 国见比吕 - 博客园, 访问时间为 八月 16, 2025， https://www.cnblogs.com/huangjuncong/p/9478505.html
ConcurrentHashMap 源码分析 - JavaGuide, 访问时间为 八月 16, 2025， https://javaguide.cn/java/collection/concurrent-hash-map-source-code.html
Map 综述（三）：彻头彻尾理解ConcurrentHashMap 原创 - CSDN博客, 访问时间为 八月 16, 2025， https://blog.csdn.net/justloveyou_/article/details/72783008
Android 上的Kotlin 协程 | Android Developers, 访问时间为 八月 16, 2025， https://developer.android.com/kotlin/coroutines?hl=zh-cn
在Android 应用中使用Kotlin 协程, 访问时间为 八月 16, 2025， https://developer.android.com/codelabs/kotlin-coroutines?hl=zh-cn
Android 运行时和Dalvik | Android Open Source Project, 访问时间为 八月 16, 2025， https://source.android.com/docs/core/runtime?hl=zh-cn
配置ART | Android Open Source Project, 访问时间为 八月 16, 2025， https://source.android.com/docs/core/runtime/configure?hl=zh-cn
Deep dive into Android Activity launch modes | by Abhishek Kumar ..., 访问时间为 八月 16, 2025， https://medium.com/@me-abhishek92/deep-dive-into-android-activity-launch-modes-7e15abee3f
Activity Launch Modes in Android Explained - Codework-AI, 访问时间为 八月 16, 2025， https://codework.ai/launch-modes-andorid
Introduction on how does Android View rendering | by Needone App ..., 访问时间为 八月 16, 2025， https://needoneapp.medium.com/introduction-on-how-does-android-view-rendering-90aee9b2773c
Lesson 5 Layouts.pptx - SlideShare, 访问时间为 八月 16, 2025， https://www.slideshare.net/slideshow/lesson-5-layoutspptx/254891887
Android事件分发机制完全解析，带你从源码的角度彻底理解(上 ..., 访问时间为 八月 16, 2025， https://blog.csdn.net/guolin_blog/article/details/9097463
Decoding Handler and Looper in Android | by Vasya Drobushkov | ProAndroidDev, 访问时间为 八月 16, 2025， https://proandroiddev.com/decoding-handler-and-looper-in-android-d4f3f2449513
All About Looper, MessageQueue, and Handler in Android - Henry Techie, 访问时间为 八月 16, 2025， https://namanh11611.github.io/p/looper-message-queue-handler/
使用binder IPC | Android Open Source Project, 访问时间为 八月 16, 2025， https://source.android.com/docs/core/architecture/hidl/binder-ipc?hl=zh-cn
Android Architecture Patterns: A Comprehensive Pre-Interview Guide, 访问时间为 八月 16, 2025， https://blog.droidchef.dev/android-architecture-patterns-a-comprehensive-pre-interview-guide/
Android Architecture: Definition, Layers, Components, Patterns, and Benefits - Intelivita, 访问时间为 八月 16, 2025， https://www.intelivita.com/blog/android-architecture/
Android Architecture Patterns — MVC, MVP, MVVM, MVI, Clean ..., 访问时间为 八月 16, 2025， https://medium.com/droidblogs/android-architecture-patterns-mvc-mvp-mvvm-mvi-clean-architecture-cde8029b8f37
OkHttp Interceptors - Mindbowser, 访问时间为 八月 16, 2025， https://www.mindbowser.com/okhttp-interceptors/
Interceptors - OkHttp, 访问时间为 八月 16, 2025， https://square.github.io/okhttp/features/interceptors/
OkHttp Interceptors in Android, Episode 1 - Kodeco, 访问时间为 八月 16, 2025， https://www.kodeco.com/20781027-okhttp-interceptors-in-android/lessons/1
【后篇】史上最全的Android面试题集锦-华为开发者话题, 访问时间为 八月 16, 2025， https://developer.huawei.com/consumer/cn/forum/topic/41598890
Glide Caching - How it works - CodingWithMitch.com, 访问时间为 八月 16, 2025， https://codingwithmitch.com/blog/glide-caching-how-it-works/
How to Use a Router - The Home Depot, 访问时间为 八月 16, 2025， https://www.homedepot.com/c/ah/how-to-use-a-router/9ba683603be9fa5395fab90dea9fe0b
Basic Guide to Using a Router - Woodshop Diaries, 访问时间为 八月 16, 2025， https://www.woodshopdiaries.com/basic-woodworking-router-guide/
查看崩溃和应用无响应错误- Play 管理中心帮助, 访问时间为 八月 16, 2025， https://support.google.com/googleplay/android-developer/answer/9859174?hl=zh-Hans
ANR | App quality | Android Developers, 访问时间为 八月 16, 2025， https://developer.android.com/topic/performance/vitals/anr?hl=zh-cn
诊断和修复ANR | App quality, 访问时间为 八月 16, 2025， https://developer.android.com/topic/performance/anrs/diagnose-and-fix-anrs?hl=zh-cn
Android OOM案例分析- 美团技术团队, 访问时间为 八月 16, 2025， https://tech.meituan.com/2017/04/14/oom-analysis.html
Android apk流程-阿里云, 访问时间为 八月 16, 2025， https://www.aliyun.com/sswb/438552.html
状态和Jetpack Compose - Android Developers, 访问时间为 八月 16, 2025， https://developer.android.com/develop/ui/compose/state?hl=zh-cn
Jetpack Compose 中的高级状态和附带效应, 访问时间为 八月 16, 2025， https://developer.android.com/codelabs/jetpack-compose-advanced-state-side-effects?hl=zh-cn
