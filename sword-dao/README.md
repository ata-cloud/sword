 
 # common-dao
 
  有鱼金融公共依赖项目 dao.
   
  对 mybatis,tkmapper,pagehelper 包装.
  
  
  本 dao. 与下述依赖冲突,请一定移除
  `
    <dependency>
      <groupId>com.github.pagehelper</groupId>
      <artifactId>pagehelper-spring-boot-starter</artifactId>
    </dependency>
  `
  
**2.0.1版本发布**
枚举类型转换


**3.6.0版本发布**
操作记录拦截器: 支持自定义insert、update方法，支持多参数和List类型参数