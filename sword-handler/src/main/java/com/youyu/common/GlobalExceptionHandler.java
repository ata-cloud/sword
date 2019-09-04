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

import com.alibaba.fastjson.JSONObject;
import com.youyu.common.annotation.RequestLogging;
import com.youyu.common.api.Result;
import com.youyu.common.enums.BaseResultCode;
import com.youyu.common.exception.BizException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.*;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import java.util.List;
import java.util.Set;

/**
 * Description:
 * 保留 HttpServletResponse response 的原因,是方便后续继承覆盖需要用到response的地方
 * <p/>
 * <p> GlobalExceptionHandler
 * <p/>
 *
 * @author Ping
 * @date 2018/5/7
 */

public interface GlobalExceptionHandler {

    Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 默认的 log 类,输出异常处理信息@
     * 异常还是得 log 日志输出,不然不能定位异常抛出点
     *
     * @return
     */
    default Logger log() {
        return LOGGER;
    }


    @ExceptionHandler(BizException.class)
    @ResponseBody
    @RequestLogging
    default Result bizException(BizException exception, HttpServletResponse response) {
        log().warn("baseExceptionHandler", exception);
        return Result.fail(exception);
    }

    /**
     * MethodArgumentNotValid exception
     *
     * @param exception exception
     * @param response  response
     * @return result
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    @RequestLogging
    default Result methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException exception, HttpServletResponse response) {
        BindingResult bindingResult = exception.getBindingResult();
        String defaultMessage = bindingResult.getFieldError().getDefaultMessage();
        return Result.fail(BaseResultCode.REQUEST_PARAMS_WRONG, defaultMessage);
    }

    /**
     * MethodArgumentTypeMismatch exception
     *
     * @param exception exception
     * @param response  response
     * @return result
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseBody
    @RequestLogging
    default Result methodArgumentTypeMismatchExceptionHandler(MethodArgumentTypeMismatchException exception, HttpServletResponse response) {
        String msg = String.format("[%s] require type [%s]; your input value: %s", exception.getName(), exception.getRequiredType(), exception.getValue());
        log().warn("methodArgumentTypeMismatchExceptionHandler", exception);
        return Result.fail(BaseResultCode.REQUEST_PARAMS_WRONG, msg);
    }

    /**
     * HttpMessageNotReadable exception
     *
     * @param exception exception
     * @param response  response
     * @return result
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseBody
    @RequestLogging
    default Result httpMessageNotReadableExceptionHandler(HttpMessageNotReadableException exception, HttpServletResponse response) {
        log().warn("httpMessageNotReadableExceptionHandler", exception);
        return Result.fail(BaseResultCode.REQUEST_PARAMS_TYPE_WRONG, exception.getMessage());
    }

    /**
     * illegalArgument exception
     *
     * @param exception exception
     * @param response  response
     * @return result
     */
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseBody
    @RequestLogging
    default Result illegalArgumentExceptionHandler(IllegalArgumentException exception, HttpServletResponse response) {
        log().warn("illegalArgumentExceptionHandler", exception);
        return Result.fail(BaseResultCode.REQUEST_PARAMS_TYPE_WRONG, exception.getMessage());
    }

    /**
     * MissingServletRequestParameter exception
     *
     * @param exception exception
     * @param response  response
     * @return result
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseBody
    @RequestLogging
    default Result missingServletRequestParameterException(MissingServletRequestParameterException exception, HttpServletResponse response) {
        log().warn("missingServletRequestParameterException", exception);

        return Result.fail(BaseResultCode.MISSING_SERVLET_REQUEST_PARAMETER, exception.getMessage());
    }

    /**
     * BindException
     * @param exception
     * @param response
     * @return
     */
    @ExceptionHandler(BindException.class)
    @RequestLogging
    @ResponseBody
    default Result bindException(BindException exception, HttpServletResponse response) {
        List<FieldError> fieldErrors = exception.getBindingResult().getFieldErrors();
        StringBuilder builder = new StringBuilder();
        for (FieldError error : fieldErrors) {
            builder.append(error.getField())
                    .append(":")
                    .append(error.getDefaultMessage())
                    .append(". ");
        }
        log().warn("bindException", exception);
        String msg = builder.toString();
        log().warn("msg:" + msg);
        return Result.fail(BaseResultCode.REQUEST_PARAMS_WRONG, msg);

    }


    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    @RequestLogging
    default Result handle(Exception exception) {
        if (exception instanceof NullPointerException) {
            log().error("空指针异常", exception);
            return Result.fail(BaseResultCode.UNKNOWN_ERROR, "空指针异常");

        }
        if (exception instanceof DataAccessException) {
            log().error("db 异常", exception);
            if (exception instanceof DataIntegrityViolationException) {
                //主键或唯一约束
                return Result.fail(BaseResultCode.DB_DATA_ERROR, exception);
            }

            if (exception instanceof CannotSerializeTransactionException) {
                //序列化模式更新失败.
                return Result.fail(BaseResultCode.DB_UPDATE_ERROR, exception);

            }
            if (exception instanceof CleanupFailureDataAccessException) {
                //清理资源失败,例如 连接未释放等.
                return Result.fail(BaseResultCode.DB_ACCESS_ERROR, exception);

            }
            if (exception instanceof DataRetrievalFailureException) {
                // 未获得预期数据等
                return Result.fail(BaseResultCode.DB_SELECT_ERROR, exception);

            }
            if (exception instanceof EmptyResultDataAccessException) {
                // 空返回
                return Result.fail(BaseResultCode.DB_SELECT_ERROR, exception);

            }

            return Result.fail(BaseResultCode.DB_OPERATION_ERROR, exception);
        }

        if (exception instanceof MethodArgumentNotValidException) {
            List<FieldError> fieldErrors = ((MethodArgumentNotValidException) exception).getBindingResult().getFieldErrors();
            StringBuffer errorMsg = new StringBuffer();
            for (FieldError error : fieldErrors) {
                log().warn(error.getField() + ":" + error.getDefaultMessage());
                errorMsg.append(error.getDefaultMessage() + ";");
            }
            return Result.fail(BaseResultCode.REQUEST_PARAMS_WRONG.getCode(), errorMsg.toString().substring(0, errorMsg.length() - 1));
        }

        if (exception instanceof ConstraintViolationException) {
            Set<ConstraintViolation<?>> constraintViolationSet = ((ConstraintViolationException) exception).getConstraintViolations();
            StringBuffer errorMsg = new StringBuffer();
            if (constraintViolationSet != null && constraintViolationSet.size() > 0) {
                constraintViolationSet.forEach(active -> {
                    log().warn(active.getRootBeanClass() + active.getMessage());
                    errorMsg.append(active.getMessage()).append(".");
                });
            } else {
                errorMsg.append("参数校验异常");
            }

            return Result.fail(BaseResultCode.REQUEST_PARAMS_WRONG.getCode(), errorMsg.toString().substring(0, errorMsg.length()));
        }

        if (exception instanceof ValidationException) {
            log().warn("validationException", exception);
            return Result.fail(BaseResultCode.REQUEST_PARAMS_WRONG, exception.getCause());
        }
        //请求异常
        if (exception instanceof HttpMediaTypeException) {
            log().warn("HttpMediaTypeException", exception);
            return Result.fail(BaseResultCode.REQUEST_MEDIA_WRONG, exception.getCause());
        }

        if (exception instanceof HttpRequestMethodNotSupportedException) {
            log().warn("HttpRequestMethodNotSupportedException", exception);
            return Result.fail(BaseResultCode.REQUEST_METHOD_WRONG, exception.getCause());
        }

        if (exception instanceof MaxUploadSizeExceededException) {
            LOGGER.warn("上传文件大小超限",exception);
            return Result.fail(BaseResultCode.REQUEST_PARAMS_TYPE_WRONG, ((MaxUploadSizeExceededException) exception).getMessage());
        }

        log().error("system error find", exception);
        return Result.fail(BaseResultCode.SYSTEM_ERROR);

    }


}
