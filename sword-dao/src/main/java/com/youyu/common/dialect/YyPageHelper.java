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
package com.youyu.common.dialect;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.youyu.common.api.PageData;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

/**
 * 支持 PagedData
 *
 * @author superwen
 * @date 2018/9/13 下午5:37
 */
@Slf4j
public class YyPageHelper extends PageHelper {


    @Override
    public boolean beforeCount(MappedStatement ms, Object parameterObject, RowBounds rowBounds) {
        if (isPageData(rowBounds)) {
            //自定义 pagedata 存在,即分页.将 page 的 count设置为 true.
            Page page = getLocalPage();
            page.count(true);
        }
        return super.beforeCount(ms, parameterObject, rowBounds);
    }


    @Override
    public Object afterPage(List pageList, Object parameterObject, RowBounds rowBounds) {
        Object obj = super.afterPage(pageList, parameterObject, rowBounds);
        PageData pageData = toPageData(rowBounds);
        if (pageData != null) {
            if (obj instanceof Page) {
                Page page = (Page) obj;
                pageData.setPageNum(page.getPageNum());
                pageData.setPageSize(page.getPageSize());
                pageData.setTotalCount(page.getTotal());
                pageData.setTotalPage(page.getPages());
                pageData.setRows(page);
            }
        }
        return obj;
    }

    boolean isPageData(RowBounds rowBounds) {
        return rowBounds instanceof PageData;
    }

    PageData toPageData(RowBounds rowBounds) {
        if (isPageData(rowBounds)) {
            return (PageData) rowBounds;
        }
        return null;
    }
}
