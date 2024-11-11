# Grace-Gateway网关
## 简历描述：
> Grace-Gateway   
> 项目描述：自研网关，作为微服务接入层，用于管理和控制从客户端到后端服务的所有API请求和流量。基于 Netty 搭建网关核心，使用 AsyncHttpClient 实现全异步链路处理。实现高效的请求转发、流量控制、负载均衡以及服务降级保护等多种功能，确保系统在高负载下的稳定性和灵活性。   
> 我负责的部分：   
> - 针对注册中心与配置中心实现 Nacos 、Zookeeper 多选择，支持动态修改路由配置。   
> - 针对请求构建插拔式动态过滤器链条，实现路由谓词匹配、路由转发、灰度分流等功能。   
> - 实现多种负载均衡策略，如轮询、随机、权重、一致性哈希，支持动态增删实例节点。   
> - 实现网关弹性扩展，支持信号量隔离、线程池隔离、失败重试、熔断保护、降级处理等功能。   
> - 实现了多维度流量控制，如滑动窗口限流、令牌桶限流、漏桶限流，支持动态调整限流策略。   
> - 基于 SPI 允许用户自定义扩展网关功能，如自定义过滤器、限流策略、灰度策略、负载均衡策略、降级策略等。

## 启动项目
1. 环境变量里的JDK需要是JDK17，`java -version`查看当前环境变量的JDK版本
2. 进入到项目根目录(GraceGateway)：`mvn clean install -DskipTests`
3. 项目默认使用nacos作为注册中心和配置中心，需要先启动nacos服务(推荐版本2.2.1，不然可能出现兼容问题)
   > nacos中创建配置，Data ID：grace-gateway-data（和gateway.yaml里的配置一致）   
   > 格式如下：   
   > ```
   > {
   >     "routes": [
   >          {
   >              "id": "user-service-route",
   >              "serviceName": "user-service",
   >              "uri": "/api/user/**"
   >          }
   >      ]
   > }
   > ```
   > 更多配置可以看`com.grace.gateway.config.pojo.RouteDefinition`
4. 启动一个或多个下游服务，例如GraceGateway-User，如有需要，可以更改application.yml中的配置
   ```
   cd GraceGateway-User
   mvn clean package spring-boot:run
   ```
5. 启动网关，如GraceGateway-Demo，如有需要，可以更改gateway.yaml中的配置
   ```
   cd GraceGateway-Demo
   mvn clean compile exec:java -Dexec.mainClass="com.grace.gateway.demo.Main"
   ```