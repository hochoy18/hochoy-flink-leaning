# [What is Apache Flink? — Applications](https://flink.apache.org/flink-applications.html)


## 流处理应用的基本组件 (Building Blocks for Streaming Applications)

### Streams
    Flink is a versatile processing framework that can handle any kind of stream.
- Bounded and unbounded streams
- Real-time and recorded streams:  All data are being generated in real-time and processed in real-time 
    unbounded stream 是有始无终的数据流，即无限数据流；而bounded stream 是限定大小的有始有终的数据集合，即有限数据流，
- Bounded and unbounded streams 区别 :
   - 无限数据流的数据会**随时间的推演而持续增加**，**计算持续进行且不存在结束的状态**，
   - 相对的有限数据流**数据大小固定**，**计算最终会完成并处于结束的状态**。

### State
***State 是<u>计算过程中的数据信息</u> ，在容错恢复和 checkpoint 中有重要作用，流计算的<u>本质是 Incremental Processing</u>，因此<u>需要不断查询保持状态</u>***

- state的必要性：每一个具有一定复杂度的流处理应用都是有状态的
- state的作用 ： 任意运行基本业务逻辑的流处理都需要在***一定时间内存储（remember）所接收的事件或中间结果***，以供在<u>后续的某个时间点</u>（例如收到下一个事件或者经过一段特定时间）<u>进行访问并进行后续处理</u>。
<img src="https://flink.apache.org/img/function-state.png" width= "60%">
- state的重要性：应用状态是 Flink 中的一等公民，Flink 提供了许多状态管理相关的特性支持
   - **Multiple State Primitives(多状态基元)**：flink为像 atomic values, lists, or maps 等的不同的数据结构提供 State Primitive
   - **插件化的State Backend**： state backend 负责管理 应用程序的 State，并在需要的时候进行checkpoint，
   - **Exactly-once state consistency**
   - **超大数据量状态(Very Large State)**
   - **超大数据量状态(Very Large State)**
   
   
### Time
时间是流处理应用的另一个重要组成部分。大部分的事件流固有时间语义，因为每个事件都会在某一个特定的时间点产生。此外，许多常见的流也是基于时间计算的，像窗口聚合，session，模式检测（pattern detection），以及基于时间的JOIN等。
流处理的的一个重要方面就是应用如何测量时间，例如，事件时间和处理时间的差异

Flink提供了一组丰富的跟时间相关的功能：
- **Event-time 模式**：事件时间处理只管
- **watermark 支持**：
- **Late Data 处理**(迟到数据处理)：
- **Processing-time 模式**：



## Layered APIs

<img src="https://flink.apache.org/img/api-stack.png" width="60%">


### The ProcessFunctions


### The DataStream API




### SQL & Table API




## Libraries