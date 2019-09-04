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

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;

/**
 * @see com.youyu.common.constant.ApplicationInfo
 * @author WangSongJun
 * @date 2018-12-03
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppInfo {
    /**
     * 请求的来源
     */
    private String applicationName;

    /**
     * 请求具有的app数据权限
     * <p>
     * 如果是后端服务调用它的权限就只有它自己
     * 如果是用户调用这里就是用户所拥有的后台app
     */
    private List<String> authorizedApps;

    /**
     * 操作的目标app
     * <p>
     * 在中台服务里，这次操作记录到哪个第三方应用中
     * 如果为空就取 applicationName，主要为了支持用户在中台操作时可从authorizedApps中选择一个app
     */
    private String targetApp;

    public List<String> getAuthorizedApps() {
        return ObjectUtils.isEmpty(this.authorizedApps)? Arrays.asList(this.applicationName):this.authorizedApps;
    }

    public String getTargetApp() {
        return StringUtils.isEmpty(this.targetApp)?this.applicationName:this.targetApp;
    }
}
