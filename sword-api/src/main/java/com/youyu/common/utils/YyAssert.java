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
package com.youyu.common.utils;

import com.youyu.common.api.IBaseResultCode;
import com.youyu.common.enums.BaseResultCode;
import com.youyu.common.exception.BizException;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

import javax.validation.constraints.NotNull;


/**
 * @author xiongchengwei
 * @date 2018/9/5
 */
public class YyAssert extends Assert {


    /**
     * 假设满足条件，则抛出异常
     *
     * @param expression
     * @param iBaseResultCode
     * @return
     */
    public static Class<YyAssert> assertTrue(boolean expression, IBaseResultCode iBaseResultCode) {
        if (expression) {
            throw new BizException(iBaseResultCode);
        }
        return YyAssert.class;
    }

    /**
     * 假设满足条件，则抛出异常
     *
     * @param expression
     * @param code
     * @param message
     * @return
     */
    public static Class<YyAssert> asserTrue(boolean expression, String code, String message) {
        if (expression) {
            throw new BizException(code, message);
        }
        return YyAssert.class;
    }

    /***
     * 假设不满足条件，则抛出异常
     *
     * @param expression
     * @param iBaseResultCode
     */
    public static Class<YyAssert> assertFalse(boolean expression, IBaseResultCode iBaseResultCode) {
        if (!expression) {
            throw new BizException(iBaseResultCode);
        }
        return YyAssert.class;
    }

    /**
     * 假设不满足条件，则抛出异常
     *
     * @param expression
     * @param code
     * @param message
     */

    public static Class<YyAssert> assertFalse(boolean expression, String code, String message) {
        if (!expression) {
            throw new BizException(code, message);
        }
        return YyAssert.class;
    }


    @Deprecated
    public static void isTrue(boolean expression, IBaseResultCode iBaseResultCode) {
        if (!expression) {
            throw new BizException(iBaseResultCode);
        }
    }

    public static void isTrue(boolean expression, String code, String message) {
        if (!expression) {
            throw new BizException(code, message);
        }
    }

    /***
     * 假设满足条件，则抛出异常
     *
     * @param expression
     * @param iBaseResultCode
     */
    @Deprecated
    public static void isFalse(boolean expression, IBaseResultCode iBaseResultCode) {
        if (expression) {
            throw new BizException(iBaseResultCode);
        }
    }

    /**
     * 假设满足条件，则抛出异常
     *
     * @param expression
     * @param code
     * @param message
     */
    @Deprecated
    public static void isFalse(boolean expression, String code, String message) {
        if (expression) {
            throw new BizException(code, message);
        }
    }


    public static Class<YyAssert> paramCheck(boolean expression, String message) {
        if (expression) {
            throw new BizException(BaseResultCode.REQUEST_PARAMS_WRONG, message);
        }

        return YyAssert.class;
    }


    public static void isNull(@Nullable Object obj, IBaseResultCode iBaseResultCode) {
        if (!ObjectUtils.isEmpty(obj)) {
            throw new BizException(iBaseResultCode);
        }
    }

    public static void isNotNull(@Nullable Object obj, IBaseResultCode iBaseResultCode) {
        if (ObjectUtils.isEmpty(obj)) {
            throw new BizException(iBaseResultCode);
        }
    }


    /**
     * 如果对象里有一个为null，则抛出异常
     *
     * @param iBaseResultCode
     * @param objs
     * @return
     */
    public static Class<YyAssert> assertEmptys(@NotNull IBaseResultCode iBaseResultCode, Object... objs) {
        if (YyObjectUtil.isEmptys(objs)) {
            throw new BizException(iBaseResultCode);
        }
        return YyAssert.class;
    }

    /**
     * 如果对象里有一个为null，则抛出异常
     *
     * @param code
     * @param message
     * @param objs
     * @return
     */
    public static Class assertEmptys(@NotNull String code, @NotNull String message, Object... objs) {
        if (YyObjectUtil.isEmptys(objs)) {
            throw new BizException(code, message);
        }
        return YyAssert.class;
    }

}
