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



import com.alibaba.fastjson.JSONObject;
import com.youyu.common.api.Result;
import com.youyu.common.constant.ResultCodeConstant;
import com.youyu.common.exception.BizException;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.entity.ContentType;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author liujianming
 * @date 2018/12/17 11:40
 */
@Slf4j
public class HttpUtil {
    public static void setErrorResponse(HttpServletResponse response, int httpStatus, String code, String message) {
        response.addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(httpStatus);
        try (PrintWriter writer = response.getWriter()) {
            writer.print(JSONObject.toJSONString(Result.fail(code, message)));
            writer.flush();
        } catch (IOException e) {
            log.error("Error Response can't getWrite", e);
            throw new BizException(ResultCodeConstant.HTTP_RESPONSE_ERROR,"返回值写入失败");
        }
    }
}
