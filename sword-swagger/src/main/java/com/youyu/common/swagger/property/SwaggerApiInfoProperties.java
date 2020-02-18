package com.youyu.common.swagger.property;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.validation.constraints.NotEmpty;

/**
 * @author Ping
 * @date 2018/12/19
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "youyu.common.swagger.api")
public class SwaggerApiInfoProperties {

    @NotEmpty
    private String title;

    @NotEmpty
    private String description = "Api Documents";

    @NotEmpty
    private String version = "1.0.0";

    private Contact contact = new Contact();

    @Getter
    @Setter
    public static class Contact {

        private String name = "";

        private String url = "";

        private String email = "";
    }

}
