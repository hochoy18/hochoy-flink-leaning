# 1 课程介绍

## 1.1 课程概述及Flink简介

flink 是什么
- Apache Flink 是一个面向分布式数据流处理和批量数据处理的开源计算平台，提供支持流处理和批处理两种类型应用的功能
- Apache Flink 的前身是柏林理工大学的一个研究性项目，在2014年被Apache 孵化器所接受，然后迅速成为了ASF 的顶级项目之一

flink 的特点
- 现有的开源计算方案，会把流处理和批处理作为两种不同的应用类型：流处理一般需要支持低延迟、Exactly-Once 保证，而批处理需要支持高吞吐、高效处理
- Flink 完全支持批处理，也就是说作为流处理看待时输入数据是无界的；批处理被作为一种特殊的流处理，只是他的输入数据流被定义为有界


## 1.2 Flink 简介

flink 组件栈

<img src="https://ci.apache.org/projects/flink/flink-docs-release-1.9/fig/stack.png" width= "60%">


## 1.3 Flink 自身优势
- 支持高吞吐、低延迟、高性能的流处理
- 