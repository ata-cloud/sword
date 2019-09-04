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

import com.youyu.common.constant.ApplicationInfo;
import com.youyu.common.constant.RequestHeaderConst;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.ObjectUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.List;

/**
 * Feign请求拦截器（设置请求头，传递登录信息）
 *
 * @author simon
 * @create 2018-09-07 9:51
 **/
@Slf4j
public class FeignBasicAuthRequestInterceptor implements RequestInterceptor {
    @Value("${middleground.access-token:${middleground.accessToken:}}")
    private String appToken;

    @Override
    public void apply(RequestTemplate requestTemplate) {
        //上下文中传递 appNames
        requestTemplate.header("applicationName", ApplicationInfo.applicationName);
        //调用中台服务时，经过网关需要验证 app token
        if (!ObjectUtils.isEmpty(appToken)) {
            log.trace("apply template.header App-Token:{}", appToken);
            requestTemplate.header("App-Token", appToken);
        }

        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();

            Enumeration<String> headerNames = request.getHeaderNames();
            if (headerNames != null) {
                //传递登录信息
                List<String> authInHeader = RequestHeaderConst.AUTH_IN_HEADER;
                while (headerNames.hasMoreElements()) {
                    String name = headerNames.nextElement();
                    log.debug("header element name: {},value: {}",name,request.getHeader(name));
                    if (containsIgnoreCase(authInHeader,name)) {
                        String values = request.getHeader(name);
                        requestTemplate.header(name, values);
                    }
                }
            }
        }

    }

    private boolean containsIgnoreCase(List<String> source, String target) {
        if (target == null) {
            for (String s : source) {
                if (s == null) {
                    return true;
                }
            }
        } else {
            for (String s : source) {
                if (target.equalsIgnoreCase(s)) {
                    return true;
                }
            }
        }
        return false;
    }
}
