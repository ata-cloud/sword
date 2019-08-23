package com.youyu.common.bean;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import com.youyu.common.http.BodyAndHeaderReaderHttpServletRequestWrapper;
import com.youyu.common.util.ServletContextHolder;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.Enumeration;
import java.util.Map;

/**
 * Description:
 * <p/>
 * <p> RequestDetail
 * <p/>
 *
 * @author Ping
 * @date 2018/5/7
 */
public class RequestDetail implements Serializable {

    private String requestId;

    private String url;

    private String method;

    private ImmutableMap<String, Object> paramsMap;

    private ImmutableMap<String, Object> headers;

    private String apiDesc;

    private String requestBody;

    private OffsetDateTime requestTime = OffsetDateTime.now();

    private OffsetDateTime responseTime;

    private String characterEncoding;

    private long contentLength;

    private String remoteHost;

    private int remotePort;

    public RequestDetail init() {
        HttpServletRequest request = ServletContextHolder.getRequest();
        this.requestId = ServletContextHolder.fetchRequestId();
        this.url = request.getRequestURL().toString();
        this.method = request.getMethod();
        this.paramsMap = fetParamsMap(request);
        this.headers = fetchHttpHeaders(request);
        if (request instanceof BodyAndHeaderReaderHttpServletRequestWrapper) {
            this.requestBody = ((BodyAndHeaderReaderHttpServletRequestWrapper) request).getBody();
        }
        this.characterEncoding = request.getCharacterEncoding();
        this.contentLength = request.getContentLength();
        this.remoteHost = request.getRemoteHost();
        this.remotePort = request.getRemotePort();
        return this;
    }

    private ImmutableMap<String, Object> fetParamsMap(HttpServletRequest request) {
        final Map<String, String[]> parameterMap = request.getParameterMap();
        final ImmutableMap.Builder<String, Object> singleValueParams = ImmutableMap.builder();
        for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
            singleValueParams.put(entry.getKey(), entry.getValue()[0]);
        }
        return singleValueParams.build();
    }

    private ImmutableMap<String, Object> fetchHttpHeaders(HttpServletRequest request) {
        final ImmutableMap.Builder<String, Object> headerBuilder = ImmutableMap.builder();
        final Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            final String headerName = headerNames.nextElement();
            headerBuilder.put(headerName, request.getHeader(headerName));
        }
        return headerBuilder.build();
    }

    public String getRequestId() {
        return requestId;
    }

    public String getUrl() {
        return url;
    }

    public String getMethod() {
        return method;
    }

    public ImmutableMap<String, Object> getParamsMap() {
        return paramsMap;
    }

    public ImmutableMap<String, Object> getHeaders() {
        return headers;
    }

    public String getApiDesc() {
        return apiDesc;
    }

    public String getRequestBody() {
        return requestBody;
    }

    public OffsetDateTime getRequestTime() {
        return requestTime;
    }

    public OffsetDateTime getResponseTime() {
        return responseTime;
    }

    public String getCharacterEncoding() {
        return characterEncoding;
    }

    public long getContentLength() {
        return contentLength;
    }

    public String getRemoteHost() {
        return remoteHost;
    }

    public int getRemotePort() {
        return remotePort;
    }

    public void setApiDesc(String apiDesc) {
        this.apiDesc = apiDesc;
    }

    public void setResponseTime(OffsetDateTime responseTime) {
        this.responseTime = responseTime;
    }

    @Override
    public String toString() {
        String str;
        ObjectMapper mapper = new ObjectMapper();
        try {
            str = mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            //...
            str = null;
        }
        return str;
    }
}
