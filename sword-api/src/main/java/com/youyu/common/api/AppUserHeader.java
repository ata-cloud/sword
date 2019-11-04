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

import lombok.Data;

/**
 * @author liujianming
 * @date 2018/12/11 17:01
 */
@Data
public class AppUserHeader {
    /**
     * token令牌字符串
     */
    private String token;
    /**
     * token令牌密钥
     */
    private String appId;
    /**
     * 用户来源
     */
    private String loginFrom;
    /**
     * 用户id
     */
    private String userId;
    /**
     *设备类型 android 或 ios
     */
    private String mobileType;
    /**
     * 渠道值
     */
    private String source;
    /**
     *包名
     */
    private String appPkgName;
    /**
     * APP应用版本（自定义版本）
     */
    private String appVersionName;
    /**
     * 设置包类型，用于包用户统计
     */
    private String appMgr;
    /**
     * app版本号（具体app在应用市场的版本）
     */
    private String appVersion;
    /**
     * gps
     */
    private String gps;
    /**
     * 操作时间
     */
    private String opTime;
    /**
     * 客户端请求ip地址
     */
    private String clientIp;
}
