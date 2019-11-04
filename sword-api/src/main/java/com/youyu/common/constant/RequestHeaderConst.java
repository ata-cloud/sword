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
package com.youyu.common.constant;

import java.util.Arrays;
import java.util.List;

/**
 * 需要放在请求头中的信息
 *
 * @author WangSongJun
 * @date 2018-12-11
 */
public interface RequestHeaderConst {
    /**
     * 用户登录信息
     */
    String TOKEN = "Token";
    String USER_ID_HEADER = "X-Uid";
    String USER_NAME_HEADER = "X-Un";
    String REAL_NAME_HEADER = "X-Rn";
    String LOG_HEADER = "X-Global-Log";
    /**
     * 前台用户登陆信息
     */
    String APP_USER_INFO_HEADER="X-App-User-Info";

    /**
     * 应用信息
     */
    String APP_TOKEN = "App-Token";
    String APPLICATION_NAME = "applicationName";
    String AUTHORIZED_APPS = "authorizedApps";
    String TARGET_APP = "targetApp";

    /**
     * 放在请求头的权限信息，Feign调用会一直传递下去
     */
    List<String> AUTH_IN_HEADER = Arrays.asList(
            USER_ID_HEADER,
            USER_NAME_HEADER,
            REAL_NAME_HEADER,
            AUTHORIZED_APPS,
            TARGET_APP,
            APP_USER_INFO_HEADER
    );
}
