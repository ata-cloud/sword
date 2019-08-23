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
