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
package com.youyu.common.hystrix;

import com.netflix.hystrix.HystrixCommand;
import com.youyu.common.config.FeignClientConfiguration;
import feign.RequestInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author superwen
 * @date 2018/10/12 下午6:23
 */
@Configuration
@ConditionalOnClass(HystrixCommand.class)
@ConditionalOnBean(value = {FeignClientConfiguration.class, RequestInterceptor.class})
@Slf4j
public class FeignHystrixConcurrencyStrategyAutoConfiguration {

    @Bean
    public FeignHystrixConcurrencyStrategy feignHystrixConcurrencyStrategy() {
        log.info("create feignHystrixConcurrencyStrategy");
        return new FeignHystrixConcurrencyStrategy();
    }
}
