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
package com.youyu.common.dialect;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.Configuration;
import org.springframework.util.StringUtils;

/**
 * 数据库方言工厂,产生方言对象
 **/
@Slf4j
public class DialectFactory {

    public static String dialectClass = null;

    public static Dialect buildDialect(Configuration configuration) {
        if (dialectClass == null) {
            synchronized (DialectFactory.class) {
                if (dialectClass == null) {
                    // dialectClass =
                    dialectClass = configuration.getVariables().getProperty("dialectClass");
                    if (StringUtils.isEmpty(dialectClass)) {
                        dialectClass = MySql5Dialect.class.getCanonicalName();
//                        dialectClass = "com.youyu.common.dialect.MySql5Dialect";
                    }
                }
            }
        }
        Dialect dialect = null;
        try {
            dialect = (Dialect) Class.forName(dialectClass).newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            log.error("请检查 mybatis-config.xml 中  dialectClass 是否配置正确?");
            System.err.println("请检查 mybatis-config.xml 中  dialectClass 是否配置正确?");
        }
        return dialect;
    }
}
