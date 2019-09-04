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
package com.youyu.common.enums;

import com.youyu.common.api.BaseCodeEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 是否被删除
 *
 * @author WangSongJun
 * @date 2019-03-12
 */
@Getter
@AllArgsConstructor
public enum IsDeleted implements BaseCodeEnum {
    NOT_DELETED(0, "未删除"),
    DELETED(1, "已删除");

    private int code;
    private String desc;
}
