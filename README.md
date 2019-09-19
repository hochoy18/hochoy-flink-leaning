
Architecture 架构 https://flink.apache.org/flink-architecture.html
	Process Unbounded and Bounded Data:有界和无界数据的处理
	Deploy Applications Anywhere ：能与所有常见的集群资源管理器（如Hadoop YARN，Apache Mesos和Kubernetes）集成
	Run Applications at any Scale：在多种数据规模下均能运行
	Leverage In-Memory Performance ：基于内存计算
	
	


Applications 应用
	Building Blocks for Streaming Applications：流处理应用的基本组件
		Streams：流 
			Bounded and unbounded streams
			Real-time and recorded streams
		State: 状态
		Time：时间
			Event-time Mode
			Watermark Support
			Late Data Handling
			Processing-time Mode
	Layered APIs
		The ProcessFunctions:                   stateful event-driven Applications
		The DataStream API(Streams & Windows):  Stream & Batch data Processing
		SQL & Table API:                        High-Level Analytics API
	
	Libraries 库
	    Complex Event Processing (CEP)
	    DataSet API
	    Gelly

	
	
Operations 运维




Flink 用例：https://flink.apache.org/usecases.html
    flink功能：
        support for stream and batch processing 对流和批处理的支持
        sophisticated state management 复杂状态管理
        event-time processing semantics 事件-时间处理语义
        exactly-once consistency guarantees for state 状态的仅此一次的持续性保证

    Flink 最常见的应用场景：
    	Event-driver Application (事件驱动)
            欺诈检测
    		异常检测
    		基于规则的告警
    		业务流程监控
    		Web应用程序（社交网络）
    		CEP：复杂事件处理
    	Data Analytics Application（数据分析）
            周期性查询
    		连续性查询
    	Data Pipeline Application （）
            Periodic ETL
    		Data Pipeline

