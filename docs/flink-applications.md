# [What is Apache Flink? — Applications](https://flink.apache.org/flink-applications.html)


## 流处理应用的基本组件 (Building Blocks for Streaming Applications)

### Streams
    Flink is a versatile processing framework that can handle any kind of stream.
- Bounded and unbounded streams
- Real-time and recorded streams:  All data are being generated in real-time and processed in real-time 


### State
- state的必要性：每一个具有一定复杂度的流处理应用都是有状态的
- state的作用 ： 任意运行基本业务逻辑的流处理都需要在一定时间内存储（remember）所接收的事件或中间结果，以供在后续的某个时间点（例如收到下一个事件或者经过一段特定时间）进行访问并进行后续处理。
- state的重要性：应用状态是 Flink 中的一等公民，Flink 提供了许多状态管理相关的特性支持
   - Multiple State Primitives(多状态基元)：flink为像atomic values, lists, or maps 等的不同的数据结构提供 State Primitive
   - 插件化的State Backend： state backend 负责管理 应用程序的 State，并在需要的时候进行checkpoint，
   - Exactly-once state consistency
   - 超大数据量状态(Very Large State)
   - 可弹性伸缩的应用 
   
   
### Time



### Layered APIs



### The ProcessFunctions


### The DataStream API




### SQL & Table API




### Libraries