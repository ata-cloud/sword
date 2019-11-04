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
package com.youyu.common.constant;

/**
 * Description:
 * <p/>
 * <p> CaiyiCommonsWebConstant
 * <p/>
 *
 * @author Ping
 * @date 2018/5/7
 */
public final class CaiyiCommonsWebConstant {

    private CaiyiCommonsWebConstant() {
    }

    public static final String CAIYI_DOMAIN = "com.caiyi";

    public static final String AOP_PACKAGE = "within(com.caiyi..*)";

    public static final String ANNOTATION_CLASS = "com.youyu.common.annotation.RequestLogging";

    public static final String X_LOG_DETAIL = "X-Log-Detail";

    public static final String X_REQUEST_BODY = "X-Request-Body";

    public static final String X_REQUEST_ID = "X-Request-Id";
}
