# oss-todomvc-sdk

## 简介

本项目为`oss-todomvc-app`的客户端SDK项目。旨在演示接入OSS的RPC框架的服务提供者，如何提供客户端工具包供服务消费者使用。

本项目非常简单，设计方面不作赘述。

## 客户端工具包开发之圭臬

接入OSS的RPC框架的服务提供者，需要为消费者提供客户端工具包，基本思路如下：

+ 通过feign调用后端app服务，可以将hystrix以及ribbon挂在Feign上，加入熔断机制和负载均衡策略；  
+ 项目接入`lib-security`的场景下，需要注意以下事情：  
    - 服务的provider与consumer，生成cookie和token的jwtKey以及cookieKey必须一致，且共用一套用户认证以及权限逻辑；  
    - 会自动读取请求header的token、cookie等认证信息，向后端传递，不需要在SDK里作额外处理。  
    - SDK项目应保持最小依赖集合。  
    > 其他的Java项目也应该如此，不要有多余的依赖。
