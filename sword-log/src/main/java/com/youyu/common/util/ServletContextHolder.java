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
package com.youyu.common.util;

import com.youyu.common.constant.CaiyiCommonsWebConstant;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;
import java.util.UUID;

/**
 * Description:
 * <p/>
 * <p> ServletContextHolder
 * <p/>
 *
 * @author Ping
 * @date 2018/5/7
 */
public final class ServletContextHolder {

    private ServletContextHolder() {
    }

    public static HttpServletRequest getRequest() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }

    public static String fetchRequestId() {
        String requestId = (String) getRequest().getAttribute(CaiyiCommonsWebConstant.X_REQUEST_ID);
        if (requestId == null) {
            requestId = Optional.ofNullable(getRequest().getHeader(CaiyiCommonsWebConstant.X_REQUEST_ID)).orElse(UUID.randomUUID().toString());
            getRequest().setAttribute(CaiyiCommonsWebConstant.X_REQUEST_ID, requestId);
        }
        return requestId;
    }
}
