
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

***
## Parallel Dataflows
<img src="https://ci.apache.org/projects/flink/flink-docs-release-1.9/fig/parallel_dataflow.svg" width="50%" >

- 一些概念
   - stream partitions
   - operator subtasks
   - parallelism 

- Streams pattern  between two operators 
   - One-to-one streams 
   - Redistributing streams 