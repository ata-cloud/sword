 新版的从common包， 在旧版本上 分离了web的 工程， 增加和细化了工程内容。。。。。
 
 # common
 
  有鱼金融公共依赖项目
 
 ## 简介：
  为了统一公司的开发技术栈，交互接口和方便新项目的开发，因此创建了common项目
 
 ## 工程结构:
 
 ```
 ├─common
 │  │  
 │  ├─common-api---------------------------定义了公共的api接口所需要的类，
 │  ├─common-cache-------------------------缓存相关的工具类，计划采用jetCache
 │  ├─common-dao---------------------------数据库相关的类，包括分页插件，读写分离，多数据源的支持和 多种数据库类型的支持
 │  ├─common-feign-------------------------定义了公共的api接口所需要的类(基于Eureka来实现的服务注册与调用，在Spring Cloud中使用Feign, 我们可以做到使用HTTP请求远程服务时能与调用本地方法一样的编码体验，开发者完全感知不到这是远程方法，更感知不到这是个HTTP请求。)
 │  ├─common-log---------------------------统一的日志处理，
 │  ├─common-mq----------------------------对mq的支持的封装，暂定支持rabbitmq和kafka
 │  ├─common-utils-------------------------工具类包
 │  ├─common-dts---------------------------分布式定时任务，暂时采用xxl-job
 │  ├─common-handler-----------------------统一异常处理
 │  ├─common-monitor-----------------------统一监控(利用Spring Boot Admin 来监控各个独立Service的运行状态；利用turbine来实时查看接口的运行状态和调用频率；通过Zipkin来查看各个服务之间的调用链等。)
 │  ├─common-config------------------------统一配置，配置中心的支持（目前采用apollo做配置中心）
 ```
 
 ## 项目地址
 [github] git@gitlab.gs.9188.com:caiyi.platform/common.git
 
 ![Alt text](https://github.com/username/repository/raw/master/directory/file.jpg/png)
 ![Image text](https://raw.github.com/yourName/repositpry/master/yourprojectName/img-folder/test.jpg)
 本代码采用 Intellij IDEA(2018.1 EAP) 来编写，但源码与具体的 IDE 无关。
 
 ## 开发规范约定：
 -  maven pom:
    -  groupId：如 发布平台 com.youyu.ops=com.youyu(公司名称).ops(产品名称，没有产品名称可用部门名称替代)（GroupID 是项目组织唯一的标识符，实际对应JAVA的包的结构，是main目录里java的目录结构）
    -  artifacted 定义了当前maven项目在组中唯一的ID,比如，platform-ops = platform(产品名称，没有产品名称可用部门名称替代)-ops(发布平台系统)
    -  version 开发中的项目用1.0.0-SNAPSHOT，开发完成并且上线用1.2.0.RELEASE，下一个开发版本根据变更情况升级为1.1.0-SNAPSHOT 或者 2.0.0.RELEASE  解释说明： SNAPSHOT是不稳定版，可能是还在开发中的版本，在开发时用户A可能每天都会更新代码，可能会频繁的发布版本。
    -  依赖管理 所有项目必须依赖 common-parent
 -  项目结构:  
        -  groupId
 - 包名的约定:  
        -  groupId       
 -  类命的预定
 - ops-ticket-api 项目中提供feign客户端，dto
 - 区分po、dto，不要把po中的所有字段都返回给前端。 前端需要什么字段，就返回什么字段
 - 类名：首字母大写驼峰规则；方法名：首字母小写驼峰规则；常量：全大写；变量：首字母小写驼峰规则，尽量非缩写
 - 业务模块接口层命名为`项目`-`业务-api`，如`ops-ticket-api`
 - 业务模块业务层实现层命名为`项目`-`业务-biz`，如`ops-ticket-biz`
 - 数据表命名为：`子系统`_`表`，如`b_role`
 - 更多规范，参考[阿里巴巴Java开发手册]
 
 
  ## 项目架构图
  ![frame](pic/frame.png)
  ## 技术栈/版本介绍：
  - 所涉及的相关的技术有 ：
      - JSON序列化:Jackson
      - 缓存：Redis
      - 消息队列：RibbitMQ
      - 数据库： MySQL 5.7.9(驱动6.0.6)
      - 服务注册与发现：Eureka 
      - 服务消费：Ribbon、OpenFeign
      - 负载均衡：Ribbon
      - 分布式定时任务：xxl-job
      - 配置中心：apollo
      - 负载均衡：Ribbon
      - 服务熔断：Hystrix
      - 项目构建：Maven 3.3
  - 后期引入：
      - Docker
      - Jenkins
      - nginx
      - keepalive
      
      
 
 ## 写在最后：
     本项目正在开发阶段，忘大家一起帮忙补全，有问题请联系 502238410@qq.com(熊成威)