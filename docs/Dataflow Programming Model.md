
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
程序中  operator subtasks的数量就是 operator 的并行度。stream 的并行度总是其产生operator 的并行度。同一个程序不同的算子（operator）可能会有不同的并行度。
- 一些概念
   - stream partitions
   - operator subtasks
   - parallelism 

- Streams pattern  between two operators 
   - One-to-one streams 
   - Redistributing streams
   
   
## Windows 