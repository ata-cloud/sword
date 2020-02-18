package com.youyu.common.swagger;

import com.youyu.common.swagger.property.SwaggerApiInfoProperties;
import com.youyu.common.swagger.property.SwaggerInstantProperties;
import io.swagger.annotations.ApiOperation;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Ping
 * @date 2018/12/19
 */
@Configuration
@EnableSwagger2
@EnableConfigurationProperties({SwaggerApiInfoProperties.class, SwaggerInstantProperties.class})
public class Swagger2Configuration {

    @Bean(name = "${youyu.common.swagger.instant.docketBeanName}")
    public Docket docket(SwaggerApiInfoProperties swaggerApiProperties, SwaggerInstantProperties swaggerInstantProperties) {
        SwaggerApiInfoProperties.Contact contact = swaggerApiProperties.getContact();
        ApiInfo apiInfo = new ApiInfoBuilder()
                .title(swaggerApiProperties.getTitle())
                .description(swaggerApiProperties.getDescription())
                .version(swaggerApiProperties.getVersion())
                .contact(new Contact(contact.getName(), contact.getUrl(), contact.getName()))
                .build();
        List<String> propertiesApiKeys = swaggerInstantProperties.getApiKeys();
        Docket docket = new Docket(DocumentationType.SWAGGER_2)
                .groupName(swaggerInstantProperties.getGroupName())
                .apiInfo(apiInfo)
                .host(swaggerInstantProperties.getHost())
                .select()
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                .paths(PathSelectors.ant(swaggerInstantProperties.getSelectPath()))
                .build()
                .pathMapping("/");
        if (propertiesApiKeys.isEmpty()) {
            return docket;
        }
        AuthorizationScope[] authorizationScopes = {
                new AuthorizationScope("global", "access")
        };
        List<ApiKey> apiKeys = propertiesApiKeys.stream()
                .map(propertiesApiKey -> new ApiKey(propertiesApiKey, propertiesApiKey, "header"))
                .collect(Collectors.toList());
        List<SecurityReference> securityReferences = propertiesApiKeys.stream()
                .map(propertiesApiKey -> new SecurityReference(propertiesApiKey, authorizationScopes))
                .collect(Collectors.toList());
        SecurityContext securityContext = SecurityContext.builder()
                .securityReferences(securityReferences)
                .forPaths(PathSelectors.any())
                .build();
        return docket.securitySchemes(apiKeys)
                .securityContexts(Collections.singletonList(securityContext));
    }

}

