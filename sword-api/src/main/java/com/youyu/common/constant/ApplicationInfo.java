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

import com.youyu.common.api.AppInfo;
import lombok.Data;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

@Data
public class ApplicationInfo {
    /**
     * 应用名称
     **/
    public static String applicationName = null;


    public static String getApplicationNameFromHttpContext(HttpServletRequest httpServletRequest) {

        return httpServletRequest.getHeader(RequestHeaderConst.APPLICATION_NAME);
    }

    public static AppInfo getAppInfoFromHttpContext(HttpServletRequest httpServletRequest) {
        return new AppInfo(
                httpServletRequest.getHeader(RequestHeaderConst.APPLICATION_NAME),
                Arrays.stream(
                        Optional.ofNullable(httpServletRequest.getHeader(RequestHeaderConst.AUTHORIZED_APPS)).orElseGet(() -> new String())
                                .replaceAll("[\\[\\]\\s]", "")
                                .split(",")
                )
                        .filter(s -> StringUtils.hasText(s))
                        .collect(Collectors.toList()),
                httpServletRequest.getHeader(RequestHeaderConst.TARGET_APP)
        );
    }


}
