package com.youyu.common.annotations.mapper;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author WangSongJun
 * @date 2018-11-26
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface AndGreaterThanOrEqualTo {

    /**
     * (Optional) The name of the column. Defaults to
     * the property or field name.
     */
    String name() default "";
}
