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
