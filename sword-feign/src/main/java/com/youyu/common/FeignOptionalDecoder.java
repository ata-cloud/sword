package com.youyu.common;

import com.youyu.common.enums.BaseResultCode;
import feign.Response;
import feign.Util;
import feign.codec.Decoder;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Objects;
import java.util.Optional;

/**
 * @author superwen
 * @date 2018/5/3 下午3:12
 * 重写OptionalDecoder,使用
 */
public class FeignOptionalDecoder implements Decoder {
    final Decoder delegate;

    public FeignOptionalDecoder(Decoder delegate) {
        Objects.requireNonNull(delegate, "Decoder must not be null. ");
        this.delegate = delegate;
    }

    @Override
    public Object decode(Response response, Type type) throws IOException {
        if (!isOptional(type)) {
            return delegate.decode(response, type);
        }

        if (response.status() == HttpStatus.NOT_FOUND.value()) {
            throw new FeignServiceException(BaseResultCode.UPLOAD_STATIC_DATA_FIND_FAILED);
        }
        Type enclosedType = Util.resolveLastTypeParameter(type, Optional.class);
        //暂时修改此处.改为ofNullable 创建
        return Optional.ofNullable(delegate.decode(response, enclosedType));
    }

    static boolean isOptional(Type type) {
        if (!(type instanceof ParameterizedType)) {
            return false;
        }
        ParameterizedType parameterizedType = (ParameterizedType) type;
        return parameterizedType.getRawType().equals(Optional.class);
    }
}
