# oss-todomvc-app

## 简介

`oss-todomvc-app`作为服务提供者，为所有的消费者提供基于认证的资源访问接口。

本项目旨在为接入OSS的开发者演示如何开发微服务架构下的provider服务。

## 设计思路

+ `oss-todomvc-app`是`RESOURCE`类型的项目，该项目不提供用户登录注册的逻辑，仅作为服务提供方，向服务的消费方提供资源访问的接口。  
> 声明为`RESOURCE`非强制要求，但是建议在服务粒度拆分的足够细的条件下，采用这种方式。  
+ 接入`oss-todomvc-security`依赖，用于在处理资源访问请求时提供身份认证逻辑。  

## 服务启动逻辑

+ 首先通过`bootstrap.xml`里的eureka配置，在eureka里找到configserver的地址，从configserver拉取`oss-todomvc-app`的项目配置。
+ 启动服务
+ 在eureka上注册自身，供服务消费者发现。

## 相关项目
[oss-todomvc-sdk](../oss-todomvc-sdk)
作为本项目的client，封装资源访问的操作，为服务消费方提供访问`oss-todomvc-app`的sdk。
