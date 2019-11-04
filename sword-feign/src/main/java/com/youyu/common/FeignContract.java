/*
 *    Copyright 2018-2019 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package com.youyu.common;

import feign.MethodMetadata;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.AnnotatedParameterProcessor;
import org.springframework.cloud.openfeign.support.SpringMvcContract;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

import static feign.Util.emptyToNull;

/**
 * @author superwen
 * @date 2018/5/3 下午3:09
 */
@Slf4j
public class FeignContract extends SpringMvcContract {

    private static final String FORWARD_SLASH = "/";

    private ResourceLoader resourceLoader = new DefaultResourceLoader();

    public FeignContract(List<AnnotatedParameterProcessor> annotatedParameterProcessors, ConversionService
            conversionService) {
        super(annotatedParameterProcessors, conversionService);
        log.info("自定义FeignContract，处理FeignClient继承");

    }

    /**
     * 唯一与SpringMvcContract不同的方法
     *
     * @param data
     * @param clz
     */
    @Override
    protected void processAnnotationOnClass(MethodMetadata data, Class<?> clz) {
        // 将 if (clz.getInterfaces().length == 0) { ... } 移除
        RequestMapping classAnnotation = AnnotatedElementUtils.findMergedAnnotation(clz,
                RequestMapping.class);
        if (classAnnotation != null) {
            // Prepend path from class annotation if specified
            if (classAnnotation.value().length > 0) {
                String pathValue = emptyToNull(classAnnotation.value()[0]);
                pathValue = resolve(pathValue);
                if (!pathValue.startsWith(FORWARD_SLASH)) {
                    pathValue = FORWARD_SLASH + pathValue;
                }
                data.template().insert(0, pathValue);
            }
        }

    }

    private String resolve(String value) {
        if (StringUtils.hasText(value)
                && this.resourceLoader instanceof ConfigurableApplicationContext) {
            return ((ConfigurableApplicationContext) this.resourceLoader).getEnvironment()
                    .resolvePlaceholders(value);
        }
        return value;
    }
}
