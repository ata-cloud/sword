package com.youyu.common.config;

import com.youyu.common.constant.CommonConfigConstant;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author superwen
 * @date 2018/5/22 下午4:04
 */
@Data
@ConfigurationProperties(value = CommonConfigConstant.COMMON_PREFIX + "request.logging")
public class RequestLoggingAutoConfigurationProperty {
    private boolean enabled = false;
}
