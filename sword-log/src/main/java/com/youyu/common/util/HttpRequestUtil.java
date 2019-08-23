package com.youyu.common.util;

import com.youyu.common.http.BodyAndHeaderReaderHttpServletRequestWrapper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.util.WebUtils;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Objects;

/**
 * @Author: xiongchengwei
 * @Date: 2018/10/12 下午2:12
 */
public class HttpRequestUtil {

    /**
     * 这个方法不要滥用, 原生的 request 只能获取一次 input 流,即一次 body
     *
     * @param request
     * @return
     * @throws IOException
     */
    public static String getRequestBody(HttpServletRequest request) throws IOException {
        if (request instanceof BodyAndHeaderReaderHttpServletRequestWrapper) {
            return ((BodyAndHeaderReaderHttpServletRequestWrapper) request).getBody();
        }
        StringBuilder requestBody = new StringBuilder();

        BufferedReader br = request.getReader();
        String str;
        while ((str = br.readLine()) != null) {
            requestBody.append(str);
        }
        return requestBody.toString();
    }

    public static boolean isHttpLogMethod(ServletRequest request) {
        return request instanceof HttpServletRequest &&
                (Objects.equals(HttpMethod.POST.name(), ((HttpServletRequest) request).getMethod()) ||
                        Objects.equals(HttpMethod.PUT.name(), ((HttpServletRequest) request).getMethod()) ||
                        Objects.equals(HttpMethod.PATCH.name(), ((HttpServletRequest) request).getMethod()) ||
                        Objects.equals(HttpMethod.DELETE.name(), ((HttpServletRequest) request).getMethod())) &&
                ((HttpServletRequest) request).getHeader(HttpHeaders.CONTENT_TYPE) != null && ((HttpServletRequest) request).getHeader(HttpHeaders.CONTENT_TYPE).startsWith(MediaType.APPLICATION_JSON_VALUE) &&
                request.getAttribute(WebUtils.ERROR_EXCEPTION_ATTRIBUTE) == null;
    }
}
