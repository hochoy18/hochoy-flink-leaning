Tasks and Operator Chains
flink 把 operator 子任务关联到一起形成任务集(tasks), 每一个task由一个线程执行。operator 和 task 的关联
是一项很实用的优化：它降低了线程间切换和缓冲的负荷，降低延迟的同时增加了吞吐。该关联可以配置，详情[see it](https://ci.apache.org/projects/flink/flink-docs-release-1.9/dev/stream/operators/#task-chaining-and-resource-groups)

下图是一个简单的数据流，其中执行有5个子task，因此有5个并行线程
<img src="https://ci.apache.org/projects/flink/flink-docs-release-1.9/fig/tasks_chains.svg" width="60%" >
```
有点类似于spark 的shuffle 依赖划分 stage
```


Job Managers, Task Managers, Clients

flink 运行时包括两种流程类型：
- JobManagers （也称为masters）,协调分布式任务的执行。可以调度任务，协调checkpoint 和 失败任务的恢复 等。  
   - 每个flink 任务至少有一个jobManager，在多 JobManager的部署中支持HA，其中有一个JobManager是leader，其他的则为standby。
   


- TaskManagers（也称为workers），执行dataflow 的任务（或者更确切的说是子任务，即subtasks）， data streams的缓存和交换。
   - 至少有一个taskManager
   
 JobManagers and TaskManagers 有多种不同的启动方式，以 standalone cluster模式直接启动，在容器（container）或者由
 yarn 、mesos等的资源管理框架中由管理框架启动。TaskManagers 连接并通知 JobManagers它们可用以及已被分配
 client 并不是运行时和程序执行的一个部分，但是可以用来准备和发送dataflow 到JobManager，此后，client可以与集群断开连接，
 或者继续连接以接收程序执行进度报告。客户端要么以java/ Scala程序的形式触发执行，要么以命令的形式执行程序：./bin/flink run ...
 
 <img src="https://ci.apache.org/projects/flink/flink-docs-release-1.9/fig/processes.svg" width="60%" >
 
 
 
 
 
 Task Slots and Resources
 
 每个worker（TaskManager）就是一个JVM进程，会在独立的线程中执行一个或多个subtask， 每个worker至少有一个能控制接收多少个task的 task slot。
 每个 task slot 代表TaskManager资源的固定子集。例如：具有三个task slot 的TaskManager会将其管理的内存资源分成三等分给每个 slot。 
 划分资源就意味着 subtask 间不会竞争资源，但是也就意味着他们只拥有固定的资源。
 注意这里没有CPU的隔离，当前slot只是划分任务的内存资源。
 
 用户可以通过调整 task slot的数量来决定 subtask 隔离方式。
 - 每个 TaskManager有一个或多个 slot 的区别 
    - 每个 TaskManager  有一个slot 意味着每组task 在一个单独的JVM中运行。 （例如：也可以在单独的container中启动）。
    - 拥有多个 slot 意味着 多个subtask 共享一个JVM进程。共享同一个JVM的   task 也共享相同的TCP连接（以多路复用的形式）、心跳消息（heartbeat messages）、数据集和数据结构，因此可以减少每个任务的开销。
  <img src="https://ci.apache.org/projects/flink/flink-docs-release-1.9/fig/tasks_slots.svg" width="60%" >
 
 
  默认情况下，flink 允许subtasks 共享 slot中，即使是不同task的subtask，只要subtask是在一个job中。也就是说，一个slot是可能会负责这个job的整个管道（pipeline）。
  slot共享有两个好处：
     - 由于flink 集群所需的slot数 与job中使用的最高并行度的数量一样多。因此就无须计算一个程序总共包含多少个 task。
     - 更容易获取更好的资源利用率，slot不共享时，非密集型 的source/map() 子任务将阻塞与资源密集型窗口子任务一样多的资源。
 
    <img src="https://ci.apache.org/projects/flink/flink-docs-release-1.9/fig/slot_sharing.svg" width="60%">
 
 
 
 
### [Task chaining and resource groups](https://ci.apache.org/projects/flink/flink-docs-release-1.9/dev/stream/operators/#task-chaining-and-resource-groups) 
为获得更好的性能，我们会将两个 transformations 链在一起，也就就相当于将这两个 transformation 放在同一个线程中执行。只要可能，flink会默认的将这些operator 链在一起，例如，两个map 转换。
如果需要，API也可以更细粒度地对链接进行控制：
 如果你不想将整个job链起来，可以使用 StreamExecutionEnvironment.disableOperatorChaining()进行控制。
 对于更多细粒度的控制，可以使用一下函数进行控制。值得注意的是这些函数只能用在DataStream 经过transformation之后才能使用。因为他们引用的是之前的transformation。
 例如：你可以使用someStream.map(...).startNewChain(),但是不能使用someStream.startNewChain().
 
 在flink中，一个资源组（resource group）就是一个slot。see [slots](https://ci.apache.org/projects/flink/flink-docs-release-1.9/ops/config.html#configuring-taskmanager-processing-slots)。如果有必要，你可以手动将这些算子隔离在单独的slot 中。

