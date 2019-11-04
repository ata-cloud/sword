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
 * 统一应用管理
 * <p>
 * Created by xiongchengwei on 2018/8/17.
 */
public enum YyApplication {
    Gjj_MANAGER(YyApp.GJJ.getAppCode() + "01", YyApp.GJJ.getAppName() + YySymBol.SYMBOL_UNDERLINE + "管理后台"),
    HSK_MANAGER(YyApp.HSK.getAppCode() + "01", YyApp.HSK.getAppName() + YySymBol.SYMBOL_UNDERLINE + "管理后台");
    /**
     * App 统一编码
     */
    private String applicationCode;
    /**
     * App 名称
     */
    private String applicationName;

    YyApplication(String applicationCode, String applicationName) {
        this.applicationCode = applicationCode;
        this.applicationName = applicationName;
    }


}
