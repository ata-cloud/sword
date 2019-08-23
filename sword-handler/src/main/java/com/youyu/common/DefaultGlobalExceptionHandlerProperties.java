package com.youyu.common;

import com.youyu.common.constant.CommonConfigConstant;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;


/**
 * @author superwen
 * @date 2018/5/22 下午3:19
 */
@Data
@ConfigurationProperties(value = CommonConfigConstant.COMMON_PREFIX + "exception.handler")
public class DefaultGlobalExceptionHandlerProperties {
    private boolean enabled = false;
}
