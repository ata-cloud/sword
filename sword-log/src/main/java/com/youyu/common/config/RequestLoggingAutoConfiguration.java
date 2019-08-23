package com.youyu.common.config;

import com.youyu.common.RequestLoggingAop;
import com.youyu.common.constant.CommonConfigConstant;
import com.youyu.common.http.HttpServletRequestWrapperFilter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * Description:
 * <p/>
 * <p> LoggingAutoConfiguration
 * <p/>
 *
 * @author Ping
 * @date 2018/5/7
 */
@Configuration
@ConditionalOnProperty(name = CommonConfigConstant.COMMON_PREFIX + "request.logging.enabled", matchIfMissing = true)
@EnableConfigurationProperties(RequestLoggingAutoConfigurationProperty.class)
public class RequestLoggingAutoConfiguration {

    @Bean
    public HttpServletRequestWrapperFilter httpServletRequestWrapperFilter() {
        return new HttpServletRequestWrapperFilter();
    }

    @Bean
    public RequestLoggingAop requestLoggingAop() {
        return new RequestLoggingAop();
    }

}
