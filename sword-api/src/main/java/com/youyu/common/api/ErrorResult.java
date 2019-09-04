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
package com.youyu.common.api;

import brave.internal.HexCodec;
import brave.propagation.ThreadLocalSpan;
import brave.propagation.TraceContext;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * @author superwen
 * @date 2018/5/21 下午3:20
 */
@Getter
@Setter
@ToString(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResult<T> extends Result<T> {

    private String spanId;

    private String traceId;

    private LocalDateTime timestamp = LocalDateTime.now();

    protected ErrorResult() {
        initScope();
    }

    protected ErrorResult(String code, String desc) {
        super(code, desc);
        initScope();
    }

    protected ErrorResult(IBaseResultCode baseResultCode) {
        super(baseResultCode);
        initScope();
    }

    protected ErrorResult(IBaseResultCode baseResultCode, String desc) {
        super(baseResultCode);
        this.desc = desc;
        initScope();
    }


    protected ErrorResult(IBaseResultCode baseResultCode, Throwable throwable) {
        super(baseResultCode, throwable);
        initScope();
    }

    protected ErrorResult(String code, String desc, T data) {
        super(code, desc, data);
        initScope();
    }

    /**
     * spanId 和 traceId 可能不存在类或者为空,
     * 这里直接写死获取,将异常处理掉
     */
    void initScope() {
        try {
            TraceContext traceContext = ThreadLocalSpan.CURRENT_TRACER.next().context();
            spanId = HexCodec.toLowerHex(traceContext.spanId());
            traceId = HexCodec.toLowerHex(traceContext.traceId());
        } catch (Throwable e) {
            //...
        }
    }
}
