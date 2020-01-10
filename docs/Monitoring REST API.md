# [Monitoring REST API](https://ci.apache.org/projects/flink/flink-docs-release-1.9/monitoring/rest_api.html)

- what can do : query status and statistics of running jobs, as well as recent completed jobs.

## Overview
- 默认监听 8081 端口的web服务 （ 端口设置 flink-conf.yaml via rest.port ）
- Note that the monitoring API web server and the web dashboard web server are currently the same and thus run together at the same port.
 They respond to different HTTP URLs, though.
 
 
## Developing
- 使用Netty 和 Netty 路由 处理 REST请求和 翻译（translate）URL(这个组合的轻量级依赖和 Netty HTTP 性能好)
- To add new requests, one needs to
   - add a new MessageHeaders class 
   - add a new AbstractRestHandler class 
   - add the handler to org.apache.flink.runtime.webmonitor.WebMonitorEndpoint#initializeHandlers().
   
   
## API
- 版本话（versioned）：  For example, to access version 1 of /foo/bar one would query /v1/foo/bar.default the oldest version