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

/**
 * 数据库字段枚举类型接口
 *
 * @author WangSongJun
 * @date 2018-06-21 10:12 星期四
 */
public interface BaseCodeEnum {
    /**
     * 类型编号
     *
     * @return
     */
    int getCode();

    /**
     * 根据编号获取到相应的枚举类型
     *
     * @param enumClass
     * @param code
     * @param <E>
     * @return
     */
    static <E extends Enum<?> & BaseCodeEnum> E codeOf(Class<E> enumClass, int code) {
        for (E e : enumClass.getEnumConstants()) {
            if (e.getCode() == code) {
                return e;
            }
        }
        return null;
    }
}
