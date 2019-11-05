
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

- Streams pattern  between two operators 
   - One-to-one streams （例如：上图中Source 和map()算子之间的 stream）保持着元素之间的分区和顺序。也就是说map()算子的subtask[1] 和source算子的 subtask[1]中 保持着相同的顺序的相同元素。
  
   - Redistributing streams（如上图中 map() 和 keyBy/window 以及keyBy/window 和 Sink之间的stream）会改变stream的分区。每个subtask 根据所选择的transformation 算子将数据发送到
   不同的subtask中。诸如：keyBy()(根据key的hash值重分区)，broadcast(),rebalance()(随机重分区)。在重新分配交换（redistribution exchange）中元素之间的顺序仅保留在每对发送和接收
   的subtask中（ 例如：map()的subtask[1] 和keyBy/window的subtask[2]) // TODO
   
   
   
## Windows 

流和批处理的聚合事件时有区别的。例如：在stream中，由于stream一般无限（无界的），计算所有的元素是不现实的。相反，stream的聚合（count,sum等）可以由window 限制范围，
比如："求最近5分钟的count "或者“求最近100条元素的和(sum)”

窗口可以由事件时间驱动（每30秒）也可以由事件驱动（每100条记录）。通常可以区分不同类型的窗口，例如：滚动窗口（无重叠），滑动窗口（有重叠），和session窗口（）
<img src="https://ci.apache.org/projects/flink/flink-docs-release-1.9/fig/windows.svg" width="60%" bgcolor="white" >

More window examples can be found in this blog post. More details are in the window docs.


##Time





## Stateful Operations





## Checkpoints for Fault Tolerance




## Batch on Streaming









