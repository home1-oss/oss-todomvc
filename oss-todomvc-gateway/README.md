# oss-todomvc-gateway

## 简介
本项目用于演示如何通过api gateway（zuul）对外提供RESTFul接口，并在zuul上提供服务发现、动态路由、安全等功能。

## 设计思路

+ RESTFUL类型项目，用于提供REST服务;
+ 配置由configserver托管;
+ 引入eureka依赖，发现`configserver`和`oss-todomvc-app`服务;
+ 引入`spring-cloud-starter-zuul`依赖，实现对`oss-todomvc-app`的请求代理，对外暴露RESTFul接口；
+ 接入`oss-todomvc-security`依赖，用于在代理层实现安全认证逻辑。

## 前端构建
oss-todomvc-gateway分别为[oss-todomvc-jquery](../oss-todomvc-jquery) 和 [oss-todomvc-react](../oss-todomvc-react)提供后端服务。 

项目构建时，通过将[oss-todomvc-jquery](../oss-todomvc-jquery) 和 [oss-todomvc-react](../oss-todomvc-react)打包成webjars，由`oss-todomvc-gateway`以jar依赖的方式，将静态资源统一整合到应用的jar包里。

### webjars简介
+ webjars是将css、js、html以及图片等静态资源打包成jar包的方式。通过打包成webjars，可以保证web资源版本的唯一性，简化部署和升级。
+ springboot 提供了对webjars资源访问的支持，请求时默认通过 `/webjars/${name}/${version}/${static filename}`的方式进行访问。比如访问`oss-todomvc-react`项目的首页，可以采用如下方式：  

    /webjars/oss-todomvc-react/index.html  
    
+ 使用webjars方案时，需要注意将静态文件的路径放到security的ignored列表，不走认证安全的逻辑。如下是本项目的ignored列表： 

    security:  
        ignored: '/index.html,/css/**,/js/**,/images/**,/webjars/**,/**/favicon.ico'  

+ 可以通过重写`WebMvcConfigurerAdapter`的方法，实现路径重写。

