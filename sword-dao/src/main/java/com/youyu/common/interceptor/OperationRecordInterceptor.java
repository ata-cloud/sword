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
package com.youyu.common.interceptor;

import com.youyu.common.constant.RequestHeaderConst;
import com.youyu.common.dialect.YyOperationRecordHelper;
import com.youyu.common.entity.OperationInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.RowBounds;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Pattern;

/**
 * 操作记录拦截器
 * <p>
 * insert|update 操作自动记录创建人、创建时间、修改人、修改时间
 *
 * @author WangSongJun
 * @date 2018-12-11
 * @see com.youyu.common.entity.BaseEntity
 */
@Slf4j
@Intercepts({
        @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class})
})
public class OperationRecordInterceptor implements Interceptor {

    private Properties properties;

    /**
     * 拦截 insert 或者 update 类型的方法
     * 如果 entity 是 OperationInfo 类型(有创建人创建时间相关属性)
     * 如果 entity 中相关属性为空 进行自动注入
     *
     * @param invocation
     * @return
     * @throws Throwable
     */
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        try {
            Object[] args = invocation.getArgs();
            //方法的参数
            Object methodParams = args[1];

            MappedStatement mappedStatement = (MappedStatement) args[0];

            String sqlId = mappedStatement.getId();
            log.trace("mybatis-sqlId:{}", sqlId);

            //拿到SQL判断类型，insert方法才注入create信息
            Configuration configuration = mappedStatement.getConfiguration();
            StatementHandler handler = configuration.newStatementHandler((Executor) invocation.getTarget(), mappedStatement, methodParams, RowBounds.DEFAULT, null, null);
            String sql = handler.getBoundSql().getSql();
            if (isDelete(sql)) {
                log.trace("is delete method");
                return invocation.proceed();
            }

            //拿到基本数据
            boolean isInsert = isInsert(sql);
            LocalDateTime currentTime = LocalDateTime.now();
            String currentUser = getCurrentUser();

            //判断方法参数类型，如果方法参数列表只有一个对象参数，直接操作。如果方法有多个参数或者是List类型参数，解析StrictMap
            if (methodParams instanceof OperationInfo) {
                setOperationInfo(isInsert, (OperationInfo) methodParams, currentUser, currentTime);
            } else if (methodParams instanceof Map) {
                ((Map) methodParams).forEach((k, v) -> {
                    if (v instanceof OperationInfo) {
                        setOperationInfo(isInsert, (OperationInfo) v, currentUser, currentTime);
                    } else if (v instanceof Collection) {
                        ((Collection) v).forEach(item -> {
                            if (item instanceof OperationInfo) {
                                setOperationInfo(isInsert, (OperationInfo) item, currentUser, currentTime);
                            }
                        });
                    }
                });
            }
        } catch (Exception e) {
            //ignore
        }
        return invocation.proceed();
    }

    /**
     * 设置操作人和时间信息
     *
     * @param isInsert
     * @param operationInfo
     * @param currentUser
     * @param currentTime
     */
    private void setOperationInfo(boolean isInsert, OperationInfo operationInfo, String currentUser, LocalDateTime currentTime) {
        if (isInsert) {
            // insert方法注入create信息
            if (!StringUtils.isEmpty(currentUser)) {
                operationInfo.setCreateAuthor(currentUser);
            }
            operationInfo.setCreateTime(currentTime);
        }
        //设置修改人和修改时间
        if (!StringUtils.isEmpty(currentUser)) {
            operationInfo.setUpdateAuthor(currentUser);
        }
        operationInfo.setUpdateTime(currentTime);
    }

    /**
     * 获取当前用户
     * ThreadLocal 拿不到再去 Request 里面拿一下
     *
     * @return
     */
    private String getCurrentUser() {
        try {
            String currentUser = YyOperationRecordHelper.getLocalOperator();
            if (StringUtils.isEmpty(currentUser)) {
                ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
                currentUser = ObjectUtils.isEmpty(attributes) ? null : attributes.getRequest().getHeader(RequestHeaderConst.REAL_NAME_HEADER);
            }
            if (currentUser != null) {
                currentUser = URLDecoder.decode(currentUser, StandardCharsets.UTF_8.toString());
            }
            return currentUser;
        } catch (Exception e) {
            return null;
        }
    }

    //类型匹配

    private Pattern insertPattern = Pattern.compile("^\\s*insert", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);

    protected boolean isInsert(String sql) {
        return insertPattern.matcher(sql).find();
    }
    private Pattern deletePattern = Pattern.compile("^\\s*delete", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);

    protected boolean isDelete(String sql) {
        return deletePattern.matcher(sql).find();
    }

    @Override
    public Object plugin(Object o) {
        return Plugin.wrap(o, this);
    }

    @Override
    public void setProperties(Properties properties) {
        log.debug("properties:{}", properties);
        this.properties = properties;
    }
}
