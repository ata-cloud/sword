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
package com.youyu.common.transfer;

import org.apache.commons.beanutils.converters.DateTimeConverter;

import java.util.Date;

public class DateConverter extends DateTimeConverter {
    /**
     * Construct a <b>java.util.Date</b> <i>Converter</i> that throws
     * a <code>ConversionException</code> if an error occurs.
     */
    public DateConverter() {
        super();
    }

    /**
     * Construct a <b>java.util.Date</b> <i>Converter</i> that returns
     * a default value if an error occurs.
     *
     * @param defaultValue The default value to be returned
     * if the value to be converted is missing or an error
     * occurs converting the value.
     */
    public DateConverter(Object defaultValue) {
        super(defaultValue);
    }

    /**
     * Return the default type this <code>Converter</code> handles.
     *
     * @return The default type this <code>Converter</code> handles.
     */
    protected Class getDefaultType() {
        return Date.class;
    }

    @Override
    public String convertToString(Object value) throws Throwable {
        //		System.out.println("test date convertToString start");
        if (value == null) {
            return null;
        }
        return super.convertToString(value);
    }

    public String convertToString(Object value, String pattern) throws Throwable {
        //        		System.out.println("test date convertToString start");
        if (value == null) {
            return null;
        }
        String[] patterns = super.getPatterns();
        super.setPattern(pattern);
        String returnValue = super.convertToString(value);
        super.setPatterns(patterns);
        return returnValue;
    }

    @Override
    public <T> T convertToType(Class<T> targetType, Object value) throws Exception {
        //		System.out.println("test date convertToType start"+value);
        if (value == null) {
            return null;
        }
        String v = value.toString().trim();
        if (v.length() == 0) {
            return null;
        }
        return super.convertToType(targetType, value);
    }

    /**
     * 覆盖这个方法即可，如果value为空，跳出来（李强）
     */
    @Override
    public <T> T convert(Class<T> type, Object value) {
        if (value == null) {
            return null;
        }
        return super.convert(type, value);
    }

}
