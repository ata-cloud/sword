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
package com.youyu.common.exception;


import com.youyu.common.api.IBaseResultCode;
import com.youyu.common.api.Result;
import lombok.Data;

/**
 * Description:
 * <p/>
 * <p> BizException
 * <p/>
 *
 * @author Ping
 * @date 2018/5/7
 */
@Data
public class BizException extends RuntimeException implements IBaseResultCode {

    /**
     * 返回错误码
     **/
    private String code;
    /**
     * 返回错误信息
     **/
    private String desc;

    public BizException(String code) {
        super(code);
        this.code = code;
    }

    public BizException(String code, String desc) {
        super(desc);
        this.desc = desc;
        this.code = code;
    }

    public BizException(IBaseResultCode iBaseResultCode) {
        super(iBaseResultCode.getDesc());
        this.desc = iBaseResultCode.getDesc();
        this.code = iBaseResultCode.getCode();
    }

    public BizException(IBaseResultCode iBaseResultCode, Throwable e) {
        super(e);
        this.desc = iBaseResultCode.getDesc();
        this.code = iBaseResultCode.getCode();
    }

    public BizException(IBaseResultCode iBaseResultCode, String desc) {
        super(desc);
        this.desc = desc;
        this.code = iBaseResultCode.getCode();
    }

    public BizException(String code, String desc, Throwable cause) {
        super(desc, cause);
        this.code = code;
        this.desc = desc;
    }

    public BizException(String code, Throwable cause) {
        super(cause);
        this.code = code;
    }

    public BizException(String desc, Throwable cause, boolean enableSuppression, boolean writableStackTrace, String code) {
        super(desc, cause, enableSuppression, writableStackTrace);
        this.code = code;
        this.desc = desc;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Result getResult() {
        return Result.fail(code, this.getDesc() != null ? this.getDesc() : "something wrong!");
    }




}
