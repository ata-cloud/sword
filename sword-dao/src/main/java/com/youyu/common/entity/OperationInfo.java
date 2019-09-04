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
package com.youyu.common.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 操作信息
 *
 * @author WangSongJun
 * @date 2018-12-21
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OperationInfo {
    public OperationInfo(String createAuthor, LocalDateTime createTime) {
        this.createAuthor = createAuthor;
        this.createTime = createTime;
    }

    /**创建人**/
    private String createAuthor;
    /**创建时间**/
    private LocalDateTime createTime;
    /**修改人**/
    private String updateAuthor;
    /**修改时间**/
    private LocalDateTime updateTime;
}
