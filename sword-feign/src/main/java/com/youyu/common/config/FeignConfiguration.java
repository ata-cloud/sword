package com.youyu.common.config;

import com.youyu.common.FeignContract;
import com.youyu.common.FeignOptionalDecoder;
import com.youyu.common.OpsErrorDecoder;
import com.youyu.common.constant.CommonConfigConstant;
import com.youyu.common.constant.ProfileConstant;
import com.youyu.common.support.SpringFormEncoderEnhancer;
import feign.Contract;
import feign.Logger;
import feign.codec.Decoder;
import feign.codec.Encoder;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcRegistrations;
import org.springframework.cloud.openfeign.AnnotatedParameterProcessor;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.FeignFormatterRegistrar;
import org.springframework.cloud.openfeign.support.ResponseEntityDecoder;
import org.springframework.cloud.openfeign.support.SpringDecoder;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.Order;
import org.springframework.core.convert.ConversionService;
import org.springframework.format.support.DefaultFormattingConversionService;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.ArrayList;
import java.util.List;

/**
 * @author superwen
 * @date 2018/5/3 下午3:09
 */
@Configuration
@ConditionalOnClass({FeignClient.class})
@ConditionalOnProperty(name = CommonConfigConstant.COMMON_PREFIX + "feign.enabled", havingValue = "true", matchIfMissing = true)
@Slf4j
public class FeignConfiguration {

    @Autowired
    private ObjectFactory<HttpMessageConverters> messageConverters;

    @Autowired(required = false)
    private List<AnnotatedParameterProcessor> parameterProcessors = new ArrayList<>();

    @Autowired(required = false)
    private List<FeignFormatterRegistrar> feignFormatterRegistrars = new ArrayList<>();


    @ConditionalOnProperty(name = CommonConfigConstant.COMMON_PREFIX + "feign.mvc.enabled", havingValue = "true", matchIfMissing = true)
    @Configuration
    @ConditionalOnClass(RequestMappingHandlerMapping.class)
    public static class MvcConfig {
        @Bean
        public WebMvcRegistrations registrations() {
            return new WebMvcRegistrations() {
                @Override
                public RequestMappingHandlerMapping getRequestMappingHandlerMapping() {
                    return new FeignRequestMappingHandlerMapping();
                }
            };
        }

        private static class FeignRequestMappingHandlerMapping extends RequestMappingHandlerMapping {
            @Override
            protected boolean isHandler(Class<?> beanType) {
                return super.isHandler(beanType) &&
                        !AnnotatedElementUtils.isAnnotated(beanType, FeignClient.class);
            }
        }
    }


    @Bean
    @Profile(ProfileConstant.DEV)
    @ConditionalOnMissingBean
    @Order(1)
    public Logger.Level fullLoggerLevel() {
        log.info("定义Feign.Logger.Level为FULL");
        return Logger.Level.FULL;
    }

    @Bean
    @ConditionalOnMissingBean
    @Order(1)
    @ConditionalOnProperty(name = CommonConfigConstant.COMMON_PREFIX + "feign.encoder.enabled", havingValue = "true", matchIfMissing = true)
    public Encoder feignFormEncoder() {
        log.info("use SpringFormEncoderEnhancer");
        return new SpringFormEncoderEnhancer(new SpringEncoder(messageConverters));
    }

    @Bean
    @ConditionalOnMissingBean
    @Order(1)
    @ConditionalOnProperty(name = CommonConfigConstant.COMMON_PREFIX + "feign.decoder.enabled", havingValue = "true", matchIfMissing = true)
    public Decoder feignDecoder() {
        log.info("use feignOptionalDecoder");
        return new FeignOptionalDecoder(new ResponseEntityDecoder(new SpringDecoder(this.messageConverters)));
    }

    @Bean
    @ConditionalOnMissingBean
    @Order(1)
    @ConditionalOnProperty(name = CommonConfigConstant.COMMON_PREFIX + "feign.error-decoder.enabled", havingValue = "true", matchIfMissing = true)
    public ErrorDecoder errorDecoder() {
        log.info("use errorDecoder");
        return new OpsErrorDecoder(new ErrorDecoder.Default(), messageConverters.getObject().getConverters());
    }


    @Bean
    @ConditionalOnProperty(name = CommonConfigConstant.COMMON_PREFIX + "feign.contract.enabled", havingValue = "true", matchIfMissing = true)
    @Order(1)
    public Contract feignContract(ConversionService feignConversionService) {
        log.info("启用feignContract,用于可继承的 feignClient");
        return new FeignContract(this.parameterProcessors, feignConversionService);
    }

    @Bean
    @ConditionalOnProperty(name = CommonConfigConstant.COMMON_PREFIX + "feign.conversion.enabled", havingValue = "true", matchIfMissing = true)
    public FormattingConversionService feignConversionService() {

        FormattingConversionService conversionService = new DefaultFormattingConversionService();
        for (FeignFormatterRegistrar feignFormatterRegistrar : feignFormatterRegistrars) {
            feignFormatterRegistrar.registerFormatters(conversionService);
        }
        return conversionService;
    }


}
