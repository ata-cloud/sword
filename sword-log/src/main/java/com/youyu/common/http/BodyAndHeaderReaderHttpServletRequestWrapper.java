package com.youyu.common.http;

import com.youyu.common.util.HttpRequestUtil;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

/**
 * 1:request.getInputStream();   request.getReader();  和request.getParameter("key");
 * <p>
 * 这三个函数中任何一个函数执行一次后（可正常读取body数据），之后再执行就无效了。
 * 解决方法： 包装HttpServletRequest对象，缓存body数据，再次读取的时候将缓存的值写出
 * <p>
 * 2:增加http的请求头的增强封装
 *
 * @Author: xiongchengwei
 * @Date: 2018/10/12 上午11:03
 */
public class BodyAndHeaderReaderHttpServletRequestWrapper extends HttpServletRequestWrapper {

    private final String body;

    // 保存新增的 http 头
    public Map<String, String> customHeaders;


    public void putHeader(String name, String value) {
        this.customHeaders.put(name, value);
    }

    @Override
    public String getHeader(String name) {
        // check the custom headers first
        String headerValue = customHeaders.get(name);

        if (headerValue != null) {
            return headerValue;
        }
        // else return from into the original wrapped object
        return ((HttpServletRequest) getRequest()).getHeader(name);
    }


    @Override
    public Enumeration<String> getHeaderNames() {
        // create a set of the custom header names
        Set<String> headNameset = new HashSet<String>(customHeaders.keySet());

        // now add the headers from the wrapped request object
        @SuppressWarnings("unchecked")
        Enumeration<String> e = ((HttpServletRequest) getRequest()).getHeaderNames();
        while (e.hasMoreElements()) {
            // add the names of the request headers into the list
            String n = e.nextElement();
            headNameset.add(n);
        }
        return Collections.enumeration(headNameset);

    }


    public BodyAndHeaderReaderHttpServletRequestWrapper(HttpServletRequest request) throws IOException {
        super(request);
        this.customHeaders = new HashMap<>();

        body = HttpRequestUtil.getRequestBody(request);
    }

    @Override
    public ServletInputStream getInputStream() {
        final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(body.getBytes());
        ServletInputStream servletInputStream = new ServletInputStream() {


            @Override
            public int read() {
                return byteArrayInputStream.read();
            }

            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return true;
            }

            @Override
            public void setReadListener(ReadListener listener) {

            }
        };
        return servletInputStream;
    }

    @Override
    public BufferedReader getReader() {
        return new BufferedReader(new InputStreamReader(this.getInputStream()));
    }

    public String getBody() {
        return this.body;
    }
}
