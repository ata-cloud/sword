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


import com.youyu.common.FeignBasicAuthRequestInterceptor;
import com.youyu.common.constant.CommonConfigConstant;
import feign.RequestInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.YyFeignBeanFactoryPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

/**
 * @author xiaongchengwei
 */
@Configuration
@ConditionalOnClass({FeignClient.class})
@ConditionalOnProperty(name = CommonConfigConstant.COMMON_PREFIX + "feign.enabled", havingValue = "true", matchIfMissing = true)
@Slf4j
public class FeignClientConfiguration {

    @Bean
    @ConditionalOnProperty(name = CommonConfigConstant.COMMON_PREFIX + "feign.interceptor.enabled", havingValue = "true", matchIfMissing = true)
    public RequestInterceptor requestInterceptor() {
        return new FeignBasicAuthRequestInterceptor();
    }

    @Bean
    @Order(1)
    @ConditionalOnProperty(name = CommonConfigConstant.COMMON_PREFIX + "feign.client.enabled", havingValue = "true", matchIfMissing = true)
    public BeanFactoryPostProcessor yYFeignConfig() {
        log.info("yYFeignConfig,支持和模拟多种rpc");
        return new YyFeignBeanFactoryPostProcessor();
    }
}
