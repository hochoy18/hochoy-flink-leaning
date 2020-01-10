#[What is Apache Flink? — Architecture](https://flink.apache.org/flink-architecture.html)

- 一个基于有界数据和无界数据的有状态的分布式计算引擎（框架）
- 可以运行在所有常用的集群环境/平台上基于内存的以任意规模数据的计算

## Process Unbounded and Bounded Data（有界和无界的数据流）
- flink认为任何类型的数据都是以流的形式产生，信用卡交易数据，传感器测量数据，服务器日志数据，用户在网页或者移动端应用(APP) 交互
   - Unbounded streams ：无界的产生，无界的处理，不可能等所有的数据产生完了之后再一起处理
   - Bounded streams  ：有明确的的开始和结束，数据量有限，可以摄取所有的数据进行处理
   - Apache Flink excels（v. 卓越） at processing unbounded and bounded data sets. 
   
## Deploy Applications Anywhere（部署灵活）
-  Hadoop YARN, Apache Mesos, Kubernetes ,Stand-alone
-  部署Flink应用程序时，Flink会根据应用程序配置的并行性自动识别所需的资源，并向资源管理器请求.如果发生故障，Flink会通过请求新资源来替换发生故障的容器。
  提交或控制应用程序的所有通信均通过REST调用进行,这简化了Flink在许多环境中的集成
  
  
## Run Applications at any Scale（极高的可伸缩性）
- 可被并行化为数千个task，这些task 分布在集群中并发执行，可以充分利用无尽的 CPU、内存、磁盘和网络 IO，
 Flink 容易维护非常大的应用程序状态

- 处理每天处理数万亿的事件,
- 应用维护几TB大小的状态,
- 应用在数千个内核上运行。





## 利用内存性能（Leverage In-Memory Performance）（极致的流处理性能）
- 本地状态访问进行了优化
- (Task state 维护：内存优先) Task state  is always maintained in memory  or, if the state size exceeds the available memory, in access-efficient on-disk data structures. 
   - 任务通过访问 通常处于内存中 的状态 来执行所有计算，从而产生非常低的处理延迟( Hence, tasks perform all computations by accessing local, often in-memory, state yielding very low processing latencies.)
- Flink 通过定期和异步地对本地状态进行持久化存储来保证故障场景下精确一次的状态一致性(Flink guarantees exactly-once state consistency in case of failures by periodically and asynchronously checkpointing the local state to durable storage.)

