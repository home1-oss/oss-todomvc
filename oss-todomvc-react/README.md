# oss-todomvc-react

## 简介

本项目基于todomvc的react项目进行封装，前后分离架构。与`oss-todomvc-jquery`、 `oss-todomvc-thymeleaf`项目一起，从以下方面演示传统的多页面应用如何与接入OSS的服务进行对接：  
+ 用户登录、认证逻辑
+ 显示后端通过errorhandle返回的错误信息

同时，thymeleaf、jquery、react三个项目，分别以模板方式、传统多页面方式、单页面加构建系统来演示前端项目的演进。  

## 设计思路

用户登录 && 身份认证逻辑、错误处理方式与`oss-todomvc-jquery`项目类似，不再赘述，请参照[oss-todomvc-jquery](../oss-todomvc-jquery/README.md)文档。

## 项目组织与构建

### 目录组织
项目组织方式与`oss-todomvc-jquery`类似。如下所示：  

    .
    ├── src
    │   ├── main
    │   └── site
    ├── publish
    │   ├── assets
    │   ├── bundle.js
    │   ├── bundle.js.map
    │   └── index.html
    ├── target
    │   ├── oss-todomvc-react-1.0.6.OSS-SNAPSHOT.jar
    │   └── oss-todomvc-react-1.0.6.OSS-SNAPSHOT-javadoc.jar
    ├── pom.xml
    ├── package.json
    ├── README.md
    ├── webpack.config.js
    └── oss-todomvc-react.iml
    
本项目是使用webpack构建系统的项目，package.json描述项目依赖的第三方库以及定义构建语句；webpack.config.js定义本项目的构建规范，定义不同类型文件的loader，代码路径以及编译输出路径等。

### 两种构建方式

构建有两种方式：
+ 前端的webpack构建。
    这是前端项目支持的通用构建方式，通过`npm run build`命令对项目进行编译，在本项目中输出文件放在`publish`目录(webpack配置中可自定义)。
+ maven构建。
    通过maven-frontend-plugin插件，可以下载指定的node和npm版本，调用node的构建命令，node的构建过程与上述webpack构建过程一致。之后将输出到publish目录的文件打包成jar。

> **通过maven构建的方案，并不影响之前的前后分离构建和部署。只是在之前的基础上提供新的选择**
    
## 测试开发

启动 dev-server，支持在开发过程实时看到更新。通过如下命令:

    npm install
    npm start
    
之后可以访问http://127.0.0.1:3000，随着开发实时查看页面变化。

