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
package com.youyu.common.config;

import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.youyu.common.trim.TrimmedAnnotationFormatterFactory;
import com.youyu.common.trim.TrimmedAnnotationIntrospector;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.PostConstruct;

@Configuration
@ConditionalOnClass({WebMvcConfigurer.class})
@Slf4j
public class TrimmedConfig implements WebMvcConfigurer {

    @Override
    public void addFormatters(FormatterRegistry registry) {
        log.info("request param 处理 @Trimmed ");
        registry.addFormatterForFieldAnnotation(new TrimmedAnnotationFormatterFactory());
    }

    @Autowired(required = false)
    private ObjectMapper objectMapper;

    @PostConstruct
    public void init() {
        if (objectMapper != null) {
            AnnotationIntrospector deserAnnotationIntrospector = objectMapper.getDeserializationConfig().getAnnotationIntrospector();
            AnnotationIntrospector serAnnotationIntrospector = objectMapper.getSerializationConfig().getAnnotationIntrospector();
            TrimmedAnnotationIntrospector introspector = new TrimmedAnnotationIntrospector(deserAnnotationIntrospector);
            objectMapper.setAnnotationIntrospectors(serAnnotationIntrospector, introspector);
            log.info("jackson 处理 @Trimmed");
        }
    }


}