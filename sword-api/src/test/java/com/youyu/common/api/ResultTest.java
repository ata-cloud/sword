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

import com.youyu.common.exception.BizException;
import org.junit.Test;

import java.util.Arrays;

/**
 * @author WangSongJun
 * @date 2018-11-06
 */
public class ResultTest {

    @Test(expected = BizException.class)
    public void ifSuccess() {
        Result.ok(Arrays.asList("a", "b", "c"))
                .ifNotSuccessThrowException()
                .ifSuccess(data -> {
                    data.forEach(d -> System.out.println(d));
                    return Result.ok(data);
                })
                .ifSuccess(data -> {
                    data.forEach(d -> System.out.println(d));
                    return Result.fail("只是测试");
                })
                .ifNotSuccessThrowException(new BizException("500", "some exception"))
        ;
    }
}