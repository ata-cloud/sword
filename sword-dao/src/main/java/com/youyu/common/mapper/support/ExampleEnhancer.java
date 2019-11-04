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
package com.youyu.common.mapper.support;

import com.youyu.common.annotations.mapper.*;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import java.lang.reflect.Field;
import java.util.Arrays;

/**
 * ExampleEnhancer 通用 Mapper 的 Example 的增强器
 * <p>
 * 增强了createCriteria
 * @see #createCriteria(java.lang.Object)
 * <p>
 * 自动根据查询对象中的属性解析出 criteria 的 condition
 * 1.对象中属性值不为空添加为查询条件
 * 2.对象字段上没有以下注解，如果字段类型是 List 使用 andIn，其他默认 andEqualTo
 * 3.对象字段上有相关注解的，根据注解使用条件
 * 4.如果注解中 name 属性有值，条件中使用 name （与Entity中属性名相同）
 * <p>
 * 支持对以下注解的解析：
 * @see AndEqualTo
 * @see AndLike
 * @see AndIn
 * @see AndGreaterThan
 * @see AndGreaterThanOrEqualTo
 * @see AndLessThan
 * @see AndLessThanOrEqualTo
 *
 * @author WangSongJun
 * @date 2018-11-26
 */
public class ExampleEnhancer extends Example {
    /**
     * 字段类型
     */
    private static final String LIST_TYPE = "java.util.List";

    public ExampleEnhancer(Class<?> entityClass) {
        super(entityClass);
    }

    public Example.Criteria createCriteria(Object o) {
        Example.Criteria criteria = this.createCriteriaInternal();
        if (this.oredCriteria.size() == 0) {
            criteria.setAndOr("and");
            this.oredCriteria.add(criteria);
        }
        if (o == null) {
            return criteria;
        } else {
            return addConditionByObject(criteria, o);
        }
    }

    private Example.Criteria addConditionByObject(Criteria criteria, Object object) {
        Class paramClass = object.getClass();
        Arrays.stream(paramClass.getDeclaredFields())
                .forEach(field -> {
                    try {
                        field.setAccessible(true);
                        Object fieldValue = field.get(object);
                        if (!ObjectUtils.isEmpty(fieldValue)) {
                            addConditionByField(criteria, field, fieldValue);
                        }
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                });

        return criteria;
    }

    private void addConditionByField(Criteria criteria, Field field, Object fieldValue) {
        if (field.isAnnotationPresent(AndEqualTo.class)) {
            String name = field.getAnnotation(AndEqualTo.class).name();
            name = StringUtils.hasText(name) ? name : field.getName();
            criteria.andEqualTo(name, fieldValue);
        } else if (field.isAnnotationPresent(AndLike.class)) {
            String name = field.getAnnotation(AndLike.class).name();
            name = StringUtils.hasText(name) ? name : field.getName();
            criteria.andLike(name, "%" + fieldValue + "%");
        } else if (field.isAnnotationPresent(AndGreaterThan.class)) {
            String name = field.getAnnotation(AndGreaterThan.class).name();
            name = StringUtils.hasText(name) ? name : field.getName();
            criteria.andGreaterThan(name, fieldValue);
        } else if (field.isAnnotationPresent(AndGreaterThanOrEqualTo.class)) {
            String name = field.getAnnotation(AndGreaterThanOrEqualTo.class).name();
            name = StringUtils.hasText(name) ? name : field.getName();
            criteria.andGreaterThanOrEqualTo(name, fieldValue);
        } else if (field.isAnnotationPresent(AndLessThan.class)) {
            String name = field.getAnnotation(AndLessThan.class).name();
            name = StringUtils.hasText(name) ? name : field.getName();
            criteria.andLessThan(name, fieldValue);
        } else if (field.isAnnotationPresent(AndLessThanOrEqualTo.class)) {
            String name = field.getAnnotation(AndLessThanOrEqualTo.class).name();
            name = StringUtils.hasText(name) ? name : field.getName();
            criteria.andLessThanOrEqualTo(name, fieldValue);
        } else if (field.isAnnotationPresent(AndIn.class)) {
            String name = field.getAnnotation(AndIn.class).name();
            name = StringUtils.hasText(name) ? name : field.getName();
            criteria.andIn(name, (Iterable) fieldValue);
        } else {
            //没有注解，如果是list类型就用andIn，其他默认equalTo
            if (LIST_TYPE.equals(field.getType().getTypeName())) {
                criteria.andIn(field.getName(), (Iterable) fieldValue);
            } else {
                criteria.andEqualTo(field.getName(), fieldValue);
            }
        }

    }

}
