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

import java.util.regex.Pattern;

/**
 * @author superwen
 * @date 2018/11/8 下午6:54
 */
public class TrimmedUtils {

    private static final Pattern PATTERN_WHITESPACES = Pattern.compile("\\s+");
    private static final Pattern PATTERN_WHITESPACES_WITH_LINE_BREAK = Pattern.compile("\\s*\\n\\s*");
    private static final Pattern PATTERN_WHITESPACES_EXCEPT_LINE_BREAK = Pattern.compile("[\\s&&[^\\n]]+");

    public static String parse(String text, Trimmed.TrimmerType type) {
        text = text.trim();
        switch (type) {
            case ALL_WHITESPACES:
                return PATTERN_WHITESPACES.matcher(text).replaceAll(" ");
            case EXCEPT_LINE_BREAK:
                return PATTERN_WHITESPACES_EXCEPT_LINE_BREAK.matcher(
                        PATTERN_WHITESPACES_WITH_LINE_BREAK.matcher(text).replaceAll("\n"))
                        .replaceAll(" ");
            case SIMPLE:
                return text;
            default:
                //not possible
                throw new AssertionError();
        }
    }

}
