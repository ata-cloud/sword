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
package com.youyu.common.hystrix;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.concurrent.Callable;

/**
 * 传递 ServletRequestAttributes
 *
 * @author superwen
 * @date 2018/10/12 下午6:13
 */
public class FeignBasicAuthRequestInterceptorCallable<V> implements Callable<V> {

    private final ServletRequestAttributes attributes;
    private final Callable<V> delegate;

    public FeignBasicAuthRequestInterceptorCallable(Callable delegate) {
        this.attributes = (ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes();
        this.delegate = delegate;
    }

    @Override
    public V call() throws Exception {

        try {
            RequestContextHolder.setRequestAttributes(attributes);
            return delegate.call();
        } catch (Throwable e) {
            throw e;
        } finally {
            RequestContextHolder.resetRequestAttributes();
        }
    }
}
