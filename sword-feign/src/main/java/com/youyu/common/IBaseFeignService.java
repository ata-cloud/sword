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
package com.youyu.common;

import com.youyu.common.api.PageData;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * 抽象 feignService 远程调用
 *
 * @author superwen
 * @date 2018/5/17 下午3:22
 */
public interface IBaseFeignService<K, T> {

    /**
     * 通用get
     *
     * @param id id
     * @return t
     */
    @GetMapping("/{id}")
    Optional<T> get(@PathVariable("id") K id);

    /**
     * 通用 update,基于主键修改
     *
     * @param t t
     * @return i
     */
    @PutMapping("/update")
    Integer update(@RequestBody T t);

    /**
     * 通用 del
     *
     * @param id id
     * @return i
     */
    @DeleteMapping("/{id}")
    Integer delete(@PathVariable("id") K id);

    /**
     * 通用 save
     *
     * @param t t
     * @return t
     */
    @PostMapping("/save")
    T save(@RequestBody T t);

    /**
     * 通用查询
     *
     * @param t t
     * @return ts
     */
    @PostMapping("/query")
    List<T> query(@RequestBody T t);

    /**
     * 通用分页查询
     *
     * @param t        t
     * @param pageNum  pageNum
     * @param pageSize pageSize
     * @return page t
     */
    @PostMapping("/queryPage")
    PageData<T> queryPage(@RequestBody T t, @RequestParam("pageNum") Integer pageNum, @RequestParam("pageSize") Integer pageSize);
}
