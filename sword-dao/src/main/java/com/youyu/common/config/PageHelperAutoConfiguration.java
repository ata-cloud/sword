/*
 *    Copyright 2018-2019 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package com.youyu.common.config;

import com.github.pagehelper.PageInterceptor;
import com.youyu.common.constant.CommonConfigConstant;
import com.youyu.common.dialect.YyPageHelper;
import com.youyu.common.interceptor.OperationRecordInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

/**
 * 替换
 * <dependency>
 * <groupId>com.github.pagehelper</groupId>
 * <artifactId>pagehelper-spring-boot-starter</artifactId>
 * </dependency>
 * 依赖,自己引入 pagehelper.
 * 如果依赖错误,请排除上面依赖.
 */
@Slf4j
@Configuration
@ConditionalOnBean({SqlSessionFactory.class})
@EnableConfigurationProperties({PageHelperProperties.class})
@AutoConfigureAfter({MybatisAutoConfiguration.class})
@ConditionalOnProperty(name = CommonConfigConstant.COMMON_PREFIX + "pagehelper.enabled", matchIfMissing = true)
public class PageHelperAutoConfiguration {
    @Autowired
    private List<SqlSessionFactory> sqlSessionFactoryList;
    @Autowired
    private PageHelperProperties properties;

    public PageHelperAutoConfiguration() {
    }

    @Bean
    @ConfigurationProperties(
            prefix = "pagehelper"
    )
    public Properties pageHelperProperties() {
        return new Properties();
    }

    @PostConstruct
    public void addPageInterceptor() {
        log.info("启用YyPageHelper");
        PageInterceptor interceptor = new PageInterceptor();
        Properties properties = new Properties();
        if (StringUtils.isEmpty(this.properties.getDialect())) {
            //讲默认 Dialect 替换为内部的 YyPageHelper
            this.properties.setDialect(YyPageHelper.class.getCanonicalName());
        }
        properties.putAll(this.pageHelperProperties());
        properties.putAll(this.properties.getProperties());
        interceptor.setProperties(properties);
        Iterator var3 = this.sqlSessionFactoryList.iterator();

        while (var3.hasNext()) {
            SqlSessionFactory sqlSessionFactory = (SqlSessionFactory) var3.next();
            sqlSessionFactory.getConfiguration().addInterceptor(interceptor);
        }

    }
}
