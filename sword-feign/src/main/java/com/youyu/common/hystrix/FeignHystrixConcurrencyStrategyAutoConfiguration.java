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
