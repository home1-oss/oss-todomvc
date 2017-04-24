-----
如果你正在通过git服务查看此文档，请移步项目网站或gitbook查看文档，因为git服务生成的文档链接有问题。
+ [gitbook](http://mvn-site.internal/oss-develop/gitbook)
+ [RELEASE版网站](http://mvn-site.internal/oss/staging)
+ [SNAPSHOT版网站](http://mvn-site.internal/oss-develop/staging)
-----

# oss-todomvc

## 简介
### TodoMVC介绍

TodoMVC项目旨在帮助前端在海量的MV*框架中进行选择，它使用不同的最流行的JavaScript MV*框架（例如Backbone、 Ember、AngularJS、Spine…）实现了一个相同的Todo应用。可以在TodoMVC的首页找到完整的框架实现列表，前端开发人员可以通过这个项目快速进入前端MV*框架的学习。

### oss-todomvc项目介绍

oss-todomvc主要目的如下:
+ 作为完整的demo项目，介绍oss相关服务、框架库的基本使用
+ 演示不同的前端方案与后端接入方式的区别
+ 说明在接入oss的场景下，项目的基本架构、形态
+ 说明微服务场景下，针对软件的测试、构建、部署，我们提供的解决方案
                 
我们选取了 todomvc 的 jquery 和 react 项目,同时我们创建了通过后端模板技术实现的 oss-todomvc-thymeleaf 项目.在原有TodoMVC项目的基础之上,我们扩充了如下功能:
+ 登录页面和登录/登出/用户身份认证逻辑
+ 从java后端获取数据,替代之前将存储放在localStorage的方案
+ 加入了错误反馈

从工程的角度，我们加入了如下实践：
+ 所有项目的Docker构建
+ 针对RPC场景下，集成测试时启动依赖服务的支持
+ 从ci到cd的best practise

## 架构与模块简介
oss-todomvc大体架构如下所示：

![todomvc-serials-architecture](src/readme/todomvc-serials-architecture.jpg)

> + 绿色的箭头表示前后端、服务提供者和消费者之间的数据交互。  
> + 红色虚线表示服务提供者和消费者在eureka上的注册与发现。  
> + 橙色的箭头表示web服务从configserver上拉取配置信息。  

### configserver && eureka

oss-todomvc所有的web service统一从configserver拉取配置，使用eureka作为RPC框架。其中oss-todomvc-app作为服务提供者provider，oss-todomvc-thymeleaf
以及oss-todomvc-gateway作为服务消费者。

+ configserver是oss提供的配置中心服务。configserver启动之后会在eureka上注册自身。
+ eureka是oss提供的服务注册/发现的服务。接入OSS的RPC项目与eureka会有两方面的交互：
    - 从eureka获取configsrver的信息，然后再到configserver上面获取应用自身的配置；
    - 在eureka进行服务注册，以及通过名字发现服务提供者。

具体使用请参见相关文档。

### oss-todomvc-app

`oss-todomvc-app`是`RESOURCE`类型的项目，作为服务提供者，为所有的消费者提供基于认证的资源访问接口。

### oss-todomvc-sdk

`oss-todomvc-sdk`作为`oss-todomvc-app`的客户端SDK，封装了对服务提供方资源请求的所有操作，供服务消费者调用。  

### oss-todomvc-security

`oss-todomvc-security`作为安全层，分别作用在`oss-todomvc-app`、`oss-todomvc-gateway`和`oss-todomvc-thymeleaf`项目，提供用户登录认证的支持。

### oss-todomvc-thymeleaf

+ 使用模板技术thymeleaf生成前端。
+ 作为服务消费者，通过`oss-todomvc-sdk`，向服务提供者发起请求。

### oss-todomvc-gateway

+ `oss-todomvc-gateway`项目通过引入spring cloud zuul依赖，实现对`oss-todomvc-app`的代理请求。
+ 作为`oss-todomvc-jquery`项目和`oss-todomvc-react`项目的后端。

### oss-todomvc-jquery

+ 基于todomvc的jquery项目
+ 多页面前端程序，未使用前端构建系统
+ 使用ajax向后端`oss-todomvc-gateway`发起请求
+ 项目打包成webjars形式，与`oss-todomvc-gateway`整合到一个jar包。

### oss-todomvc-react

+ 基于todomvc的react项目
+ 单页面应用程序，使用wbepack作为构建系统
+ 开发中使用大量的es6以及es7 stageX标准。使用fetch + promise实现异步数据请求。
+ 项目打包成webjars形式，与`oss-todomvc-gateway`整合到一个jar包。

> 详细实现请阅读各模块文档。

## 本地启动

### 环境配置及启动 docker 容器
为方便用户快速接入，我们提供了todomvc系列的docker镜像。用户可以按照如下步骤在本机启动todomvc的docker容器: 

+ 用户首先按照[这篇文档](http://mvn-site.internal/oss-develop/gitbook/docs/oss/CONTRIBUTION.html)设置本机的环境变量
+ 其次，需要按照[这篇文档](http://mvn-site.internal/oss-develop/gitbook/docs/oss-environment/)设置本机的环境变量，并启动相关的基础服务如configserver、eureka等。
+ 最后，在 oss-todomvc 目录通过如下命令启动：

    export DOCKER_REGISTRY=<公司内部docker-registry域名> && \
    docker-compose pull && docker-compose up -d

### 访问
+ oss-todomvc-gateway 的前端页面包括 oss-todomvc-jquery 和 oss-todomvc-react 的入口，用户可以在浏览器地址栏里面输入 http://local-oss-todomvc-gateway:8090/index.html 访问
+ oss-todomvc-thymeleaf 的前端用于演示基于模板的 todomvc 实现，用户可以在浏览器地址栏里面输入 http://local-oss-todomvc-thymeleaf:8083/login.html 进行访问

测试用户名及密码：
+ 用户名： user0 ~ user9
+ 测试密码： password0 ~ password9

## 集成测试支持

TODO

## 测试及生产部署方案

TODO

## References
1. [TodoMVC官网](http://todomvc.com/)
2. [oss-todomvc-app文档](oss-todomvc-app/README.md)
3. [oss-todomvc-sdk文档](oss-todomvc-sdk/README.md)
4. [oss-todomvc-security文档](oss-todomvc-security/README.md)
5. [oss-todomvc-thymeleaf文档](oss-todomvc-thymeleaf/README.md)
6. [oss-todomvc-gateway文档](oss-todomvc-gateway/README.md)
7. [oss-todomvc-jquery文档](oss-todomvc-jquery/README.md)
8. [oss-todomvc-react文档](oss-todomvc-react/README.md)
