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
 * Created by xiongchengwei on 2018/8/16.
 */
@Data
public class PageDataResult<T> extends Result<PageData<T>> {


    protected PageDataResult() {
        // Default constructor, no code.
    }

    protected PageDataResult(PageData<T> pageData) {
        this.data = pageData;
    }

    public static <T> PageDataResult<T> okPageData(PageData<T> data) {
        return new PageDataResult(data);
    }
}
