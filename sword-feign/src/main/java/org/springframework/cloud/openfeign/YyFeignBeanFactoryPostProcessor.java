package org.springframework.cloud.openfeign;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;

import java.util.Map;

/**
 * 自动给应用注入url
 *
 * @author xiongchengwei
 */
@Slf4j
public class YyFeignBeanFactoryPostProcessor implements BeanFactoryPostProcessor, EnvironmentAware {
    private Environment environment;

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        Map<String, FeignClientFactoryBean> feignClientMap = beanFactory.getBeansOfType(FeignClientFactoryBean.class);
        feignClientMap.forEach((name, feignClient) -> {

            String appName = feignClient.getName();
            String appUrl = appName + ".url";
            if (!StringUtils.isEmpty(environment.getProperty(appUrl))) {
                feignClient.setUrl(environment.getProperty(appUrl));
                log.info("-YyFeignBeanFactoryPostProcessor 自动给应用:{},注入url:{}", appName, appUrl);
            }
        });
        log.info("-YyFeignBeanFactoryPostProcessor 开始生效--------------");

    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;

    }
}
