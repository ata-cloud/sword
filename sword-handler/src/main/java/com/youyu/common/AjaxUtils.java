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

/**
 *
 * @author 熊成威
 * @date 2017/11/6   
 * @version 1.0
 */

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import jdk.nashorn.internal.runtime.logging.Logger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class AjaxUtils {
	private static ObjectMapper mapper = new ObjectMapper();

	/**
	 * 验证是否是ajax请求
	 * @param webRequest
	 * @return
	 */
	public static boolean isAjaxRequest(WebRequest webRequest) {
		String requestedWith = webRequest.getHeader("X-Requested-With");
		return requestedWith != null ? "XMLHttpRequest".equals(requestedWith) : false;
	}

	public static boolean isAjaxUploadRequest(WebRequest webRequest) {
		return webRequest.getParameter("ajaxUpload") != null;
	}

	public static void writeJson(Object value, HttpServletResponse response) {
		JsonGenerator jsonGenerator = null;
		try {
			jsonGenerator = mapper.getFactory().createGenerator(response.getOutputStream(), JsonEncoding.UTF8);
			if (jsonGenerator != null) {
				jsonGenerator.writeObject(value);
			}
		} catch (IOException e) {
			log.error("exception happend",e);
		}

	}

	private AjaxUtils() {
	}

}
