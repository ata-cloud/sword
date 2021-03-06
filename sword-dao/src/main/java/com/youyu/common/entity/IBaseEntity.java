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

import java.io.Serializable;

/**
 * 顶级接口, entity
 *
 * @author superwen
 * @date 2018/9/5 上午10:09
 */
public interface IBaseEntity<T> extends Serializable {

    /**
     * 获得id
     *
     * @return
     */
    T getId();

    /**
     * 设置id
     *
     * @param id
     */
    void setId(T id);

}
