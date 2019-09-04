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

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import org.apache.ibatis.session.RowBounds;

import java.io.Serializable;
import java.util.List;


/**
 * @author superwen
 * 全局的分页 bean
 * pagehelper 中的 page 可以通过 PageDataConverter 转换
 * 如果是 jpa, 暂时要自己转换
 */
@Getter
public class PageData<T> extends RowBounds implements Serializable {

    @ApiModelProperty(value = "总行数,总条数")
    private long totalCount;

    @ApiModelProperty(value = "当前页码")
    private int pageNum = 1;

    @ApiModelProperty(value = "数量")
    private int pageSize = 10;

    @ApiModelProperty(value = "总页数")
    private int totalPage;


    /**
     * 偏移量 : 第一条数据在表中的位置
     */
    protected int offset;

    /**
     * 限定数 : 每页的数量
     */
    protected int limit;


    @ApiModelProperty(value = "结果集合")
    private List<T> rows;

    /**
     * 计算偏移量
     */
    private void calcOffset() {
        this.offset = ((pageNum - 1) * pageSize);
    }

    /**
     * 计算限定数
     */
    private void calcLimit() {
        this.limit = pageSize;
    }

    // -- 构造函数 --//
    public PageData() {
        this.calcOffset();
        this.calcLimit();
    }

    public PageData(int pageNum, int pageSize) {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.calcOffset();
        this.calcLimit();
    }

    public void setTotalCount(long totalCount) {
        this.totalCount = totalCount;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
        this.calcOffset();
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
        this.calcOffset();
        this.calcLimit();
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }
}
