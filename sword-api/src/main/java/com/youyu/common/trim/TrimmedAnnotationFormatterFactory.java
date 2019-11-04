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

import com.youyu.common.trim.annotation.Trimmed;
import com.youyu.common.trim.annotation.Trimmed.TrimmerType;
import org.springframework.format.AnnotationFormatterFactory;
import org.springframework.format.Formatter;
import org.springframework.format.Parser;
import org.springframework.format.Printer;

import java.util.*;

/**
 * https://jira.spring.io/browse/SPR-7768
 * 处理 param 传参
 */
public class TrimmedAnnotationFormatterFactory implements AnnotationFormatterFactory<Trimmed> {

    private static final Map<TrimmerType, TrimmerFormatter> TRIMMER_FORMATTER_MAP;

    static {
        TrimmerType[] values = Trimmed.TrimmerType.values();
        Map<TrimmerType, TrimmerFormatter> map = new HashMap<>(values.length);
        for (TrimmerType type : values) {
            map.put(type, new TrimmerFormatter(type));
        }
        TRIMMER_FORMATTER_MAP = Collections.unmodifiableMap(map);
    }

    @Override
    public Set<Class<?>> getFieldTypes() {
        Set<Class<?>> fieldTypes = new HashSet<Class<?>>(1, 1);
        fieldTypes.add(String.class);
        return fieldTypes;
    }

    @Override
    public Parser<?> getParser(Trimmed annotation, Class<?> fieldType) {
        return TRIMMER_FORMATTER_MAP.get(annotation.value());
    }

    @Override
    public Printer<?> getPrinter(Trimmed annotation, Class<?> fieldType) {
        return TRIMMER_FORMATTER_MAP.get(annotation.value());
    }

    private static class TrimmerFormatter implements Formatter<String> {


        private final TrimmerType type;

        public TrimmerFormatter(TrimmerType type) {
            if (type == null) {
                throw new NullPointerException();
            }
            this.type = type;
        }

        @Override
        public String print(String object, Locale locale) {
            return object;
        }

        @Override
        public String parse(String text, Locale locale) {
            return TrimmedUtils.parse(text, type);
        }

    }

}