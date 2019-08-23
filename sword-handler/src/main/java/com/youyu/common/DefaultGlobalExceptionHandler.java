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
