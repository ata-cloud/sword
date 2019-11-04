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

import com.youyu.common.api.Result;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.support.FeignUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.client.HttpMessageConverterExtractor;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author superwen
 * @date 2018/5/17 下午4:15
 */
@Slf4j
public class OpsErrorDecoder implements ErrorDecoder {

    private final ErrorDecoder delegate;

    private final HttpMessageConverterExtractor httpMessageConverterExtractor;

    public OpsErrorDecoder(ErrorDecoder delegate, List<HttpMessageConverter<?>> messageConverters) {
        this.delegate = delegate;
        this.httpMessageConverterExtractor = new HttpMessageConverterExtractor<>(Result.class, messageConverters);
    }

    @Override
    public Exception decode(String methodKey, Response response) {
        //只处理400和500 , 和 application/json
        //todo: 按新的重构,业务异常只会返回 http 200.此处应该移除或者修改.
        FeignResponseAdapter responseAdapter = new FeignResponseAdapter(response);
        if (isDisposeStatus(response.status()) && isDisposeMediaType(response)) {
            return decode(responseAdapter);
        }
        return delegate.decode(methodKey, response);

    }

    boolean isDisposeStatus(int status) {
        return status == HttpStatus.BAD_REQUEST.value() || status == HttpStatus.INTERNAL_SERVER_ERROR.value();
    }

    boolean isDisposeMediaType(Response response) {
        MediaType mediaType = new FeignResponseAdapter(response).getHeaders().getContentType();
        return mediaType != null && mediaType.includes(MediaType.APPLICATION_JSON);
    }

    FeignServiceException decode(ClientHttpResponse response) {
        try {
            Result result = (Result) httpMessageConverterExtractor.extractData(response);
            return new FeignServiceException(result.getCode(), result.getDesc());
        } catch (IOException e) {
            throw new FeignServiceException(FeignResultCodeConstant.FEIGN_IO_EXCEPTION, "IO序列化失败", e);
        }
    }

    /**
     * 抄 {@link org.springframework.cloud.openfeign.support.SpringDecoder.FeignResponseAdapter}
     */
    private class FeignResponseAdapter implements ClientHttpResponse {

        private final Response response;

        private FeignResponseAdapter(Response response) {
            this.response = response;
        }

        @Override
        public HttpStatus getStatusCode() throws IOException {
            return HttpStatus.valueOf(this.response.status());
        }

        @Override
        public int getRawStatusCode() throws IOException {
            return this.response.status();
        }

        @Override
        public String getStatusText() throws IOException {
            return this.response.reason();
        }

        @Override
        public void close() {
            try {
                this.response.body().close();
            } catch (IOException ex) {
                // Ignore exception on close...
            }
        }

        @Override
        public InputStream getBody() throws IOException {
            return this.response.body().asInputStream();
        }

        @Override
        public HttpHeaders getHeaders() {
            return getHttpHeaders(this.response.headers());
        }

    }

    /**
     * 抄 {@link org.springframework.cloud.openfeign.support.FeignUtils#getHttpHeaders(java.util.Map)}
     */
    static HttpHeaders getHttpHeaders(Map<String, Collection<String>> headers) {
        HttpHeaders httpHeaders = new HttpHeaders();
        for (Map.Entry<String, Collection<String>> entry : headers.entrySet()) {
            httpHeaders.put(entry.getKey(), new ArrayList<>(entry.getValue()));
        }
        return httpHeaders;
    }

}
