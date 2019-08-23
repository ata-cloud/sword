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
 │  ├─common-workwechat------------------------企业微信SDK
 ```
 
 ## 项目地址
 [github] git@gitlab.gs.9188.com:caiyi.platform/common.git
 
 
 本代码采用 Intellij IDEA(2018.1 EAP) 来编写，但源码与具体的 IDE 无关。
 
 ## 项目结构
 
   ![project_frame](pic/project_frame.png)
   
  - ops-ticket(项目名称)
  - ops-ticket-api(子项目名称)业务模块接口层命名为`项目`-`业务-api`，如`ops-ticket-api` (ops-ticket-api 项目中提供feign客户端，dto)
  - ops-ticket-biz(子项目名称)业务模块业务层实现层命名为`项目`-`业务-biz`，如`ops-ticket-biz`
  
 ## 包结构
 
  ![package_frame](pic/package_frame.png)
  
  - api 对外暴露的接口方法
  - dto 数据传输， 通常和 对应po进行转换
  - param 请求参数对象，默认以XxxParam结尾，如果请求是新增，修改，则可以用XxxDto作为入参
  - exception 方法签名异常
  
  ![biz_package_frame](pic/biz_package_frame.png)
  
  - config 配置文件
  - controller 
  - service 
  - dal 持久化层
    - dao   ：接口方法
    - entity：实体
    - mapper：xml文件 
    
 ## 开发规范约定：
 -  maven pom:
    -  groupId  com.youyu.platform=com.youyu(公司名称).platform(产品名称，没有产品名称可用部门名称替代)（GroupID 是项目组织唯一的标识符，实际对应JAVA的包的结构，是main目录里java的目录结构）
    -  artifacted 定义了当前maven项目在组中唯一的ID,比如，platform-ops = platform(产品名称，没有产品名称可用部门名称替代)-ops(发布平台系统)
    -  version 开发中的项目用1.0.0-SNAPSHOT，开发完成并且上线用1.0.0.RELEASE，下一个开发版本根据变更情况升级为1.1.0-SNAPSHOT 或者 2.0.0.RELEASE  解释说明： SNAPSHOT是不稳定版，可能是还在开发中的版本，在开发时用户A可能每天都会更新代码，可能会频繁的发布版本。
    -  依赖管理 所有项目必须依赖 common-parent
      
 -  类命的预定
 - 区分po、dto，不要把po中的所有字段都返回给前端。 前端需要什么字段，就返回什么字段
 - 类名：首字母大写驼峰规则；方法名：首字母小写驼峰规则；常量：全大写；变量：首字母小写驼峰规则，尽量非缩写
 - 数据表命名为：`子系统`_`表`，如`b_role`
 - 更多规范，参考[阿里巴巴Java开发手册]
 
 

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
     本项目正在开发阶段，望大家一起帮忙补全，有问题请联系 502238410@qq.com(熊成威),wangsongjun@yofish.com(王宋军 qq:1013381075)

## 版本发布

**3.6.0**
- common-dao        操作记录拦截器: 支持自定义insert、update方法，支持多参数和List类型参数


**3.0.0**
- commo-config      Apollo 配置动态更新
- common-dao        ExampleEnhancer 增强通用 Mapper 的 Example#createCriteria,自动根据查询对象中的属性解析出 criteria 的 condition
- common-dao        添加了拦截器，自动注入创建人修改人信息
- common-feign      FeignClient 请求头添加App-Token(调用中台服务时，经过网关需要验证 app token)
- common-workwechat apollo 配置动态更新（需要将配置写在"work-we-chat"namespaces下）
- common-log        转自定义 servlet 有变动
- common-monitor    添加 cors filter 的开关
- common-swagger    自动配置swagger，方法上加 @ApiOperation 注解就可以
- common-api        BizException 构造函数添加 desc 属性赋值


**2.0.3**
- common-feign 添加 MultipartFile 文件上传支持，支持多文件 MultipartFile[] 上传。
- common-feign 添加 FeignMultipartFile 类，实现了 MultipartFile 方便 Feign Client 文件上传。


**2.0.1**
- common-dao 中 com.youyu.common.type.BaseCodeEnum 移到 common-api com.youyu.common.api.BaseCodeEnum
- common-feign Feign请求拦截器（设置请求头，传递登录信息）
- common-api 添加 @Trimmed 用于 reqeut param body(jackson) trim 方法去前后空格.
- common-cache-cachecloud 添加 jetcache 缓存框架支持. 
- common-dao 升级 druid 从 0.2.23 到 1.1.12
