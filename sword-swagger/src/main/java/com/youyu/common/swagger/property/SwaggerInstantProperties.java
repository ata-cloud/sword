package com.youyu.common.swagger.property;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.validation.constraints.NotEmpty;
import java.util.Collections;
import java.util.List;

/**
 * @author Ping
 * @date 2018/12/19
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "youyu.common.swagger.instant")
public class SwaggerInstantProperties {

    @NotEmpty
    private String host = "localhost:8080";

    @NotEmpty
    private String docketBeanName = "docket";

    private String groupName = "default";

    @NotEmpty
    private String selectPath = "/**";

    private List<String> apiKeys = Collections.singletonList("Token");
}
