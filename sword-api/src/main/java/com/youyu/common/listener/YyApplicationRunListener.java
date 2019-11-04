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
package com.youyu.common.listener;

import com.youyu.common.constant.ApplicationInfo;
import com.youyu.common.utils.YyAssert;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringApplicationRunListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

import static com.youyu.common.enums.BaseResultCode.BUSINESS_ERROR;

@Data
@Slf4j
public class YyApplicationRunListener implements SpringApplicationRunListener {

    private SpringApplication springApplication;

    private String[] args;

    private String applicationName;

    public YyApplicationRunListener(String arg) {

    }

    public YyApplicationRunListener(SpringApplication application, String[] args) {
        this.args = args;
        this.springApplication = application;

    }

    @Override
    public void starting() {


    }

    @Override
    public void environmentPrepared(ConfigurableEnvironment environment) {
        log.info("环境变量加载完成" + environment);


    }

    @Override
    public void contextPrepared(ConfigurableApplicationContext context) {
        log.info("系统上下文已准备" + context);
    }

    @Override
    public void contextLoaded(ConfigurableApplicationContext context) {
        log.info("应用" + applicationName + "系统上下文加载完成" + context);

    }

    @Override
    public void started(ConfigurableApplicationContext context) {

        log.info("应用" + applicationName + "系统已启动" + context);

    }

    @Override
    public void running(ConfigurableApplicationContext context) {
        log.info("应用" + applicationName +"系统运行中" + context);

    }

    @Override
    public void failed(ConfigurableApplicationContext context, Throwable exception) {
        log.error("应用" + applicationName +"系统启动失败" + context + "异常" + exception);

    }


}
