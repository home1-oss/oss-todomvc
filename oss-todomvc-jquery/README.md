# oss-todomvc-jquery

## 简介

本项目基于todomvc的jquery项目进行封装，前后分离架构，旨在从以下方面演示传统的多页面应用如何与接入OSS的服务进行对接： 
+ 用户登录、认证逻辑  
+ 显示后端通过errorhandle返回的错误信息  

## 设计思路

oss-todomvc-jquery 使用传统的多页面方式，通过ajax向后端发起请求。  

### 用户登录 && 身份认证逻辑

用户的登录及后续身份认证流程如下：  
+ 通过form表单提交用户名和密码  
+ 登录之后，后端返回加密的cookie和token以及用户的基本信息。  
+ 用户再次请求时，可以选择通过cookie或者`X-Auth-Token`携带token的方式。  

针对用户登录逻辑，本项目实现了如下页面跳转：  
+ 用户登录之后，跳转到主页。  
+ 用户未登出，再次访问登录页面，页面会自动跳转到主页。  
+ 用户登出，跳转到登陆页面。  

对于用户身份认证以及其他的错误，走下面的错误处理流程。  

### 错误处理

后端在基本的身份认证错误、其他服务端错误之外，额外加入了todo的title长度20字节的限制。目前针对各种错误，我们的处理如下：   

+ 身份认证错误，提示用户跳转回登录页面。如果用户“确认”则发生页面跳转，“取消”不做任何处理。  
+ 对于title长度限制，会alert提示errorhandle返回的异常信息。  
+ 其他错误，通过alert提示errorhandle返回的异常信息。  

**后端返回的错误信息结构**  

| field | description | 
| --- | --- |
| timestamp | 时间戳，精确到ms |
| status | HTTP status code |
| exception | 异常类 | 
| message | 具体的错误信息 | 
| trace | 异常栈 | 
| localizedMessage | 用于前端展示的异常信息 | 
| path | 请求路径 |
| datetime | ISO8601 格式的时间信息 | 
| paths | 本次请求的curl命令 | 

## 项目组织结构及构建
项目目录结构如下所示:  

    .
    ├── pom.xml
    ├── README.md
    ├── src
    │   ├── main
    │   ├── site
    │   └── test
    ├── target
    │   ├── oss-todomvc-jquery-1.0.6.OSS-SNAPSHOT.jar
    │   └── oss-todomvc-jquery-1.0.6.OSS-SNAPSHOT-javadoc.jar
    └── oss-todomvc-jquery.iml

我们建议在前端项目中使用这套与后端类似的目录组织结构。
+ 在src/main目录存放工程文件，如css js html以及图片等静态文件
+ 在src/test目录存放前端的测试文件
+ 在src/site中存储文档相关，方便构建mvn-sites或者gitbook等静态网站。

本项目中有一个pom.xml文件，通过maven来管理前端项目，将所有的静态文件打包到target目录的webjars文件中。打包命令与java项目一致：

    mvn package

关于webjars的介绍和这种方式的优点以及需要注意的地方，请参照 [oss-todomvc-gateway](../oss-todomvc-gateway/README.md) 文档。

## 相关项目
[oss-todomvc-react](../oss-todomvc-react)
[oss-todomvc-thymeleaf](../oss-todomvc-thymeleaf)
