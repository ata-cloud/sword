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

/**
 * 统一项目管理
 * <p>
 * Created by xiongchengwei on 2018/8/17.
 */
public enum YyProject {

    Auth("01", "权限管理后台");
    /**
     * App 统一编码
     */
    private String projectCode;
    /**
     * App 名称
     */
    private String projectName;

    YyProject(String projectCode, String projectName) {
        this.projectCode = projectCode;
        this.projectName = projectName;
    }
}
