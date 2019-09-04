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
package com.youyu.common;


import com.youyu.common.api.IBaseResultCode;
import com.youyu.common.exception.BizException;

/**
 * @author superwen
 * @date 2018/5/3 下午3:15
 */
public class FeignServiceException extends BizException {

    public FeignServiceException(String code) {
        super(code);
    }

    public FeignServiceException(String code, String message) {
        super(code, message);
    }

    public FeignServiceException(IBaseResultCode iBaseResultCode) {
        super(iBaseResultCode);
    }

    public FeignServiceException(String code, String message, Throwable cause) {
        super(code, message, cause);
    }

    public FeignServiceException(Throwable cause, String code) {
        super(code, cause);
    }

    public FeignServiceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, String code) {
        super(message, cause, enableSuppression, writableStackTrace, code);
    }
}
