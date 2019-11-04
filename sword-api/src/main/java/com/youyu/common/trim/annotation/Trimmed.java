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
package com.youyu.common.trim.annotation;


import java.lang.annotation.*;

/**
 * https://jira.spring.io/browse/SPR-7768
 *
 * @author superwen
 * @date 2018/11/8 下午3:53
 */
@Target(value = {ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Trimmed {


    TrimmerType value() default TrimmerType.SIMPLE;

    enum TrimmerType {
        /**
         * ，执行trim
         */
        SIMPLE,
        /**
         * 执行trim（）并用一个空格替换任何一组空白字符（\ s）
         * default operation, performs trim() and replaces any group of whitespace
         * characters (\s) with a single whitespace; I defaulted to this because I believe most input fields on web pages do not care about multiple whitespaces and indeed would prefer that they do not happen at all;
         */
        ALL_WHITESPACES,
        /**
         * the same as ALL_WHITESPACES, but groups with at least one line break inside them will be replaced by a single line break; this is intended for textarea inputs, where line breaks are allowed,
         * but still avoiding multiple (annoying) line breaks one after another.
         */
        EXCEPT_LINE_BREAK;
    }
}