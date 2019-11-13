
# [Dataflow Programming Model](https://ci.apache.org/projects/flink/flink-docs-release-1.9/concepts/programming-model.html#dataflow-programming-model)
 

## Levels of Abstraction
<img src="https://ci.apache.org/projects/flink/flink-docs-release-1.9/fig/levels_of_abstraction.svg" width="50%"  >

***
- Stateful Stream Processing
- Core APIs : DataStream / DataSet API 
- Table APIs
- SQL APIs
***

## Programs and Dataflows
<img src="https://ci.apache.org/projects/flink/flink-docs-release-1.9/fig/program_dataflow.svg" width="50%" >

 streams 和 transformations是flink程序的基本构建块（值得注意的是：在 DataSet API中使用的 DataSets其内部也是streams，）。从概念上将，流就是数据记录的流（可能永无止境），
 transformation 就是一种将一个或多个stream作为输入并输出一个或多个输出的操作（operator）。flink程序在运行的时候会被映射成streams 和 transformation operators的 streaming dataflows。
 每个dataflow 以一个或多个source开始，并以一个或多个sink结束。dataflows 有点类似于DAG图。
 通常，程序中的 transformations 和dataflow中的operators是一一对应的，然后，有时候，一个transformations可能包含多个transformations operator。
 Sources and sinks are documented in the [streaming connectors](https://ci.apache.org/projects/flink/flink-docs-release-1.9/dev/connectors/index.html) 
 and [batch connectors](https://ci.apache.org/projects/flink/flink-docs-release-1.9/dev/batch/connectors.html) docs. Transformations are documented in 
 [DataStream operators](https://ci.apache.org/projects/flink/flink-docs-release-1.9/dev/stream/operators/index.html) 
 and [DataSet transformations](https://ci.apache.org/projects/flink/flink-docs-release-1.9/dev/batch/dataset_transformations.html).


***
## Parallel Dataflows
<img src="https://ci.apache.org/projects/flink/flink-docs-release-1.9/fig/parallel_dataflow.svg" width="50%" >

flink 程序本质上也是并行和分布式的。在程序执行期间，每个stream 会有一个或多个stream partition，每个operator也会有一个或多个operator subtask。这些 operator subtasks之间相互独立，
并且在不同的线程（thread）中执行，甚至可能会在不同的机器或者容器中执行。
程序中  operator subtasks的数量就是 operator 的并行度。stream 的并行度是其产生operator 的并行度。同一个程序不同的算子（operator）可能会有不同的并行度。
- 一些概念
   - stream partitions
   - operator subtasks
   - parallelism 


算子（operators）之间的数据传输有一对一 和 重新分配两种形式： 
- Streams pattern  between two operators 
   - One-to-one streams ：类似于spark的窄依赖（Narrow Dependency）（例如：上图中Source 和map()算子之间的 stream）保持着元素之间的分区和顺序。
   也就是说map()算子的subtask[1] 和source算子的 subtask[1]中 保持着相同的顺序的相同元素。
  
   - Redistributing streams：类似于spark中的宽依赖（Shuffle Dependency）（如上图中 map() 和 keyBy/window 以及keyBy/window 和 Sink之间的stream）会改变stream的分区。每个subtask 根据所选择的transformation 算子将数据发送到
   不同的subtask中。诸如：keyBy()(根据key的hash值重分区)，broadcast(),rebalance()(随机重分区)。
   在重新分配交换（redistribution exchange）情况下，元素之间的顺序仅保留在每对发送和接收的subtask中（ 例如：map()的subtask[1] 和keyBy/window的subtask[2]) // TODO
   因此，在此示例中，保留了每个key内的顺序，但是并行度也确实引入了不确定性，即不同key的聚合结果到达sink 的顺序。
   
   Details about configuring and controlling parallelism can be found in the docs on [parallel execution](https://ci.apache.org/projects/flink/flink-docs-release-1.9/zh/dev/parallel.html).
   
   
## Windows 

流和批处理的聚合事件时有区别的。例如：在stream中，由于stream一般无限（无界的），计算所有的元素是不现实的。相反，stream的聚合（count,sum等）可以由window 限制范围，
比如："求最近5分钟的count "或者“求最近100条元素的和(sum)”

窗口可以由事件时间驱动（每30秒）也可以由事件驱动（每100条记录）。通常可以区分不同类型的窗口，例如：滚动窗口（无重叠），滑动窗口（有重叠），和session窗口（）
<img src="https://ci.apache.org/projects/flink/flink-docs-release-1.9/fig/windows.svg" width="60%" bgcolor="white" >

More window examples can be found in this blog post. More details are in the window docs.


##Time
提到streaming 程序的时间时（例如定义窗口），有以下几种概念可以参考：
 - Event Time：即事件创建的时间。通常用事件中的时间戳表示，例如会被生产传感器或者生产服务附带。
 - Ingestion time ：事件通过source 算子进入 flink 的时间,
 - Processing Time：每个算子执行基于时间的操作的本地时间(？？？是否类似于spark程序 在executor 上的执行时间)
 
 <img src="https://ci.apache.org/projects/flink/flink-docs-release-1.9/fig/event_ingestion_processing_time.svg" width="60%" bgcolor="white">
 
 More details on how to handle time are in the [event time docs](https://gitbook.cn/m/mazi/comp/column?columnId=5dad4a20669f843a1a37cb4f&sceneId=3da0e320046a11ea882fbf9ba8c963fb&utm_source=columninvitecard&utm_campaign=%E5%A4%A7%E6%95%B0%E6%8D%AE%E5%AE%9E%E6%97%B6%E8%AE%A1%E7%AE%97%E5%BC%95%E6%93%8E%20Flink%20%E5%AE%9E%E6%88%98%E4%B8%8E%E6%80%A7%E8%83%BD%E4%BC%98%E5%8C%96).




## Stateful Operations(有状态的操作)
 




## Checkpoints for Fault Tolerance（容错检查点）
flink通过 stream replay 和 checkpointing 的组合来实现容错。



## Batch on Streaming
flink 把 Batch 处理当做是 streaming 处理的一种特殊形式，在批处理中，stream是有界的（元素个数有限）。其内部DataSet也是按照 stream 来处理的。因此以上这些适用于streaming 的概念也同样适用于 
batch 处理，除了一下几点之外：
- batch program 的容错不使用checkpoint，它是通过回放整个流来恢复的。由于输入的有界性，这也就称为可能。着虽然增加了恢复的成本但是也同时降低了常规操作的成本，因为它避免了checkpoints。









## TODO 
### Redistributing streams 
###