package com.youyu.common.config;

import com.xxl.job.core.executor.XxlJobExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import static com.youyu.common.constant.CommonConfigConstant.COMMON_PREFIX;

/**
 * xxl-job config
 *
 * @author xuxueli 2017-04-28
 */
@Configuration
@ConditionalOnProperty(name = COMMON_PREFIX + "xxl.job.enabled", matchIfMissing = true)
@EnableConfigurationProperties(XxlJobProperties.class)
@Slf4j
public class XxlJobConfig {

    @Autowired
    XxlJobProperties xxlJobProperties;

    @Value("${spring.application.name}")
    private String appName;

    @ConditionalOnMissingBean
    @Bean(initMethod = "start", destroyMethod = "destroy")
    public XxlJobExecutor xxlJobExecutor() {
        log.info("xxlJobExecutor 初始化...");
        XxlJobExecutor xxlJobExecutor = new XxlJobExecutor();
        xxlJobExecutor.setAdminAddresses(xxlJobProperties.getAdminAddresses());
        if (StringUtils.isEmpty(xxlJobProperties.getAppName())) {
            xxlJobExecutor.setAppName(appName);
        } else {
            xxlJobExecutor.setAppName(xxlJobProperties.getAppName());
        }

        xxlJobExecutor.setIp(xxlJobProperties.getIp());
        xxlJobExecutor.setPort(xxlJobProperties.getPort());
        xxlJobExecutor.setAccessToken(xxlJobProperties.getAccessToken());
        xxlJobExecutor.setLogPath(xxlJobProperties.getLogPath());
        xxlJobExecutor.setLogRetentionDays(xxlJobProperties.getLogRetentionDays());
        xxlJobExecutor.setAppAddr(xxlJobProperties.getAppAddr());
        return xxlJobExecutor;
    }

}
