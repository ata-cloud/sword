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
