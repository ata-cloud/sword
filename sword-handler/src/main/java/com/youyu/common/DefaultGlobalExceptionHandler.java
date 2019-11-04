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


import com.youyu.common.constant.CommonConfigConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;


/**
 * @author superwen
 * @date 2018/5/18 下午5:54
 */
@Slf4j
@Component
@ControllerAdvice
@ConditionalOnProperty(name = CommonConfigConstant.COMMON_PREFIX + "exception.handler.enabled", matchIfMissing = true)
@EnableConfigurationProperties(DefaultGlobalExceptionHandlerProperties.class)
public class DefaultGlobalExceptionHandler implements GlobalExceptionHandler {
    public DefaultGlobalExceptionHandler() {
        log.info("use default GlobalExceptionHandler");
    }
}
