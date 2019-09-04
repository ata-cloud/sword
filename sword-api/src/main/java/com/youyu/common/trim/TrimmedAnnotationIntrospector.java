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
package com.youyu.common.trim;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.core.Versioned;
import com.fasterxml.jackson.core.util.VersionUtil;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import com.youyu.common.trim.annotation.Trimmed;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;


/**
 * @author superwen
 * @date 2018/11/8 下午6:43
 */
@Slf4j
@SuppressWarnings(value = {"all"})
public class TrimmedAnnotationIntrospector extends JacksonAnnotationIntrospector implements Versioned {


    private AnnotationIntrospector delegate;

    public TrimmedAnnotationIntrospector(AnnotationIntrospector delegate) {
        if (delegate instanceof TrimmedAnnotationIntrospector) {
            delegate = ((TrimmedAnnotationIntrospector) delegate).delegate;
        }
        this.delegate = delegate;
        log.info("处理@Trimmed 的 TrimmedAnnotationIntrospector");
    }

    @Override
    public Version version() {
        return VersionUtil.versionFor(getClass());
    }

    @Override
    public Object findDeserializer(Annotated a) {
        if (a.hasAnnotation(Trimmed.class)) {
            Trimmed ann = a.getAnnotation(Trimmed.class);
            switch (ann.value()) {
                case ALL_WHITESPACES:
                    return TrimmedJsonDeserializer.ALL_WHITESPACES;
                case EXCEPT_LINE_BREAK:
                    return TrimmedJsonDeserializer.EXCEPT_LINE_BREAK;
                case SIMPLE:
                    return TrimmedJsonDeserializer.SIMPLE;
                default:
                    //not possible
                    throw new AssertionError();
            }
        }
        return delegate.findDeserializer(a);
    }

    @Override
    public Collection<AnnotationIntrospector> allIntrospectors() {
        return delegate.allIntrospectors();
    }

    @Override
    public Collection<AnnotationIntrospector> allIntrospectors(Collection<AnnotationIntrospector> result) {
        return delegate.allIntrospectors(result);
    }

    @Override
    public String[] findPropertiesToIgnore(Annotated ac, boolean forSerialization) {
        return delegate.findPropertiesToIgnore(ac, forSerialization);
    }

    @Override
    public String[] findPropertiesToIgnore(Annotated ac) {
        return delegate.findPropertiesToIgnore(ac);
    }

    @Override
    public Boolean findIgnoreUnknownProperties(AnnotatedClass ac) {
        return delegate.findIgnoreUnknownProperties(ac);
    }

    @Override
    public PropertyName findWrapperName(Annotated ann) {
        return delegate.findWrapperName(ann);
    }

    @Override
    public JsonInclude.Include findSerializationInclusion(Annotated a, JsonInclude.Include defValue) {
        return delegate.findSerializationInclusion(a, defValue);
    }

    @Override
    public JsonInclude.Include findSerializationInclusionForContent(Annotated a, JsonInclude.Include defValue) {
        return delegate.findSerializationInclusionForContent(a, defValue);
    }

//    @Override
//    protected <A extends Annotation> A _findAnnotation(Annotated annotated, Class<A> annoClass) {
//        return (A) delegate._findAnnotation(annotated, annoClass);
//    }
//
//    @Override
//    protected boolean _hasAnnotation(Annotated annotated, Class<? extends Annotation> annoClass) {
//        return (boolean) delegate._hasAnnotation(annotated, annoClass);
//    }
//
//    @Override
//    protected boolean _hasOneOf(Annotated annotated, Class<? extends Annotation>[] annoClasses) {
//        return (boolean) delegate._hasOneOf(annotated, annoClasses);
//    }
}
