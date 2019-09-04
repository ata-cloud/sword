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
package com.youyu.common.http;


import com.youyu.common.util.HttpRequestUtil;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * http request 中的body请求的封装类，便于后续获取http body
 *
 * @Author: xiongchengwei
 * @Date: 2018/10/12 上午11:05
 */

@Slf4j
@WebFilter(filterName = "httpServletRequestWrapperFilter", urlPatterns = "/*")
public class HttpServletRequestWrapperFilter implements Filter {

    public static final String HTTP_BODY = "http-body";

    @Override
    public void init(FilterConfig filterConfig) {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        BodyAndHeaderReaderHttpServletRequestWrapper requestWrapper = null;
        if (HttpRequestUtil.isHttpLogMethod(request)) {
            requestWrapper = new BodyAndHeaderReaderHttpServletRequestWrapper((HttpServletRequest) request);
            //打印日志
            String body = recordRequestInfoLog(requestWrapper);
            //添加http请求体到头中，提供问题排查和监控自动化测试
        }
        if (null == requestWrapper) {

            chain.doFilter(request, response);
        } else {
            chain.doFilter(requestWrapper, response);
        }
    }

    private String recordRequestInfoLog(BodyAndHeaderReaderHttpServletRequestWrapper requestWrapper) {
        String host = requestWrapper.getHeader("host");
        String queryUrl = host + requestWrapper.getRequestURI();
        String remoteAddr = requestWrapper.getRemoteAddr();
        String body = requestWrapper.getBody();
        log.info("统一请求remoteAddr:" + remoteAddr + ":统一的请求" + queryUrl + ":统一的请求日志的body：" + body);
        return body;
    }


    @Override
    public void destroy() {

    }

}
