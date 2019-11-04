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
package com.youyu.common;

import com.youyu.common.bean.RequestDetail;
import com.youyu.common.constant.CaiyiCommonsWebConstant;
import com.youyu.common.util.ServletContextHolder;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.time.OffsetDateTime;


/**
 * Description:
 * <p/>
 * <p> RequestLoggingAop
 * <p/>
 *
 * @author Ping
 * @date 2018/5/7
 */
@Aspect
@Slf4j
public class RequestLoggingAop {

    public RequestLoggingAop() {
        log.info("RequestLoggingAop...");
    }

    @Around(value = " @annotation(" + CaiyiCommonsWebConstant.ANNOTATION_CLASS + ")")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        RequestDetail requestDetail = initRequestDetail();

        setApiDesc(joinPoint, requestDetail);

        final Object proceed = joinPoint.proceed();

        requestDetail.setResponseTime(OffsetDateTime.now());

        log.info("request log: {}", requestDetail);

        return proceed;
    }

    private void setApiDesc(ProceedingJoinPoint joinPoint, RequestDetail requestDetail) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        try {
            final ApiOperation operate = method.getAnnotation(ApiOperation.class);
            if (operate != null) {
                requestDetail.setApiDesc(operate.value());
            }
        } catch (NoClassDefFoundError e) {
            log.debug("不存在ApiOperation 注解,没有引用 swagger", e);
        }

    }

    private RequestDetail initRequestDetail() {
        HttpServletRequest request = ServletContextHolder.getRequest();
        RequestDetail requestDetail = (RequestDetail) request.getAttribute(CaiyiCommonsWebConstant.X_LOG_DETAIL);
        if (requestDetail == null) {
            requestDetail = new RequestDetail().init();
            request.setAttribute(CaiyiCommonsWebConstant.X_LOG_DETAIL, requestDetail);
        }
        return requestDetail;
    }
}
