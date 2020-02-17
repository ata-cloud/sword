package com.youyu.common.config;

import com.youyu.common.constant.CommonConfigConstant;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author superwen
 * @date 2018/8/22 下午3:34
 */
@ConfigurationProperties(prefix = CommonConfigConstant.COMMON_PREFIX + "xxl.job")
@Getter
@Setter
public class XxlJobProperties {

    /**
     * 是否开启,默认引入 jar 即开启
     */
    private boolean enabled = true;
    /**
     * xxl-job admin 地址
     */
    private String adminAddresses;
    /**
     * 当前 job app name
     */
    private String appName;
    /**
     * ip,注册到 xxl-job 的 ip;
     */
    private String ip;
    /**
     * port,注册到 xxl-job 的 port;
     */
    private int port;
    /**
     * 权限 token
     */
    private String accessToken;
    /**
     * 日志存放路径
     */
    private String logPath;
    /**
     * 日志保留天数
     */
    private int logRetentionDays;
    /**
     * 注册到 xxl-job 的实际地址.
     * 默认注册到 xx-job admin 为 ip:port.
     * 在当前 dcos 集群,宿主机 ip 没有互通,需要将 宿主机 ip+ 映射端口注册到 admin 中方便调用.
     */
    private String appAddr;


}
