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
package com.youyu.common.service;

import com.youyu.common.api.PageData;
import com.youyu.common.dto.IBaseDto;
import com.youyu.common.entity.BaseEntity;
import com.youyu.common.dto.BaseDto;
import com.youyu.common.entity.IBaseEntity;

import java.util.List;

/**
 * @author superwen
 * @date 2017/12/20 下午4:05
 */
public interface IService<DTO extends IBaseDto, T extends IBaseEntity> {

    /**
     * 根据实体中的属性进行查询，只能有一个返回值，有多个结果是抛出异常，查询条件使用等号
     *
     * @param record
     * @return
     */
    DTO selectOne(T record);
    /**
     * 根据实体中的属性进行查询，只能有一个返回值，有多个结果是抛出异常，查询条件使用等号
     *
     * @param record
     * @return
     */
    T selectOneEntity(T record);

    /**
     * 根据实体中的属性值进行查询，查询条件使用等号
     *
     * @param record
     * @return
     */
    List<DTO> select(T record);
    /**
     * 根据实体中的属性值进行查询，查询条件使用等号
     *
     * @param record
     * @return
     */
    List<T> selectEntity(T record);

    /**
     * 根据实体中的属性值进行查询，查询条件使用等号
     *
     * @param record
     * @return
     */
    PageData<DTO> selectInPage(T record, PageData pageData);

    /**
     * 查询全部结果
     *
     * @return
     */
    List<DTO> selectAll();

    /**
     * 查询全部结果
     *
     * @return
     */
    List<T> selectAllEntity();

    /**
     * 分页查询全部结果
     *
     * @return
     */
    PageData<DTO> selectAllInPage(PageData pageData);


    List<DTO> selectByExample(Object example);
    List<T> selectEntityByExample(Object example);

    List<DTO> selectByExampleInPage(Object example, PageData pageData);
    List<T> selectEntityByExampleInPage(Object example, PageData pageData);


    /**
     * 根据实体中的属性查询总数，查询条件使用等号
     *
     * @param record
     * @return
     */
    int selectCount(DTO record);

    /**
     * 根据实体中的属性查询总数，查询条件使用等号
     *
     * @param record
     * @return
     */
    int selectCount(T record);

    /**
     * 根据主键字段进行查询，方法参数必须包含完整的主键属性，查询条件使用等号
     *
     * @param key
     * @return
     */
    DTO selectByPrimaryKey(Object key);

    /**
     * 根据主键字段进行查询，方法参数必须包含完整的主键属性，查询条件使用等号
     *
     * @param key
     * @return
     */
    T selectEntityByPrimaryKey(Object key);



    /**
     * 保存一个实体，null的属性也会保存，不会使用数据库默认值
     *
     * @param record
     * @return
     */
    int insert(DTO record);

    /**
     * 保存一个实体，null的属性不会保存，会使用数据库默认值
     *
     * @param record
     * @return
     */
    int insertSelective(DTO record);

    /**
     * 保存一个实体，null的属性也会保存，不会使用数据库默认值
     *
     * @param record
     * @return
     */
    int insert(T record);

    /**
     * 保存一个实体，null的属性不会保存，会使用数据库默认值
     *
     * @param record
     * @return
     */
    int insertSelective(T record);

    /**
     * 根据主键更新实体全部字段，null值会被更新
     *
     * @param record
     * @return
     */
    int updateByPrimaryKey(DTO record);

    /**
     * 根据主键更新属性不为null的值
     *
     * @param record
     * @return
     */
    int updateByPrimaryKeySelective(DTO record);

    /**
     * 根据主键更新实体全部字段，null值会被更新
     *
     * @param record
     * @return
     */
    int updateByPrimaryKey(T record);

    /**
     * 根据主键更新属性不为null的值
     *
     * @param record
     * @return
     */
    int updateByPrimaryKeySelective(T record);


    /**
     * 根据Example更新实体全部字段，null值会被更新
     *
     * @param record
     * @return
     */
    int updateByExample(T record,Object example);

    /**
     * 根据Example更新属性不为null的值
     *
     * @param record
     * @return
     */
    int updateByExampleSelective(T record,Object example);

    /**
     * 根据实体属性作为条件进行删除，查询条件使用等号
     *
     * @param record
     * @return
     */
    int delete(DTO record);

    /**
     * 根据主键字段进行删除，方法参数必须包含完整的主键属性
     *
     * @param key
     * @return
     */
    int deleteByPrimaryKey(Object key);

    /**
     * Entity转成DTO
     *
     * @param entity
     * @return
     */
    DTO convertEntityToDto(T entity);

    /**
     * DTO转成Entity
     *
     * @param dto
     * @return
     */
    T convertDtoToEntity(DTO dto);

    /**
     * Entity的特殊逻辑转换的数据赋值给DTO
     *
     * @param entity
     * @param dto
     */
    void assignEntityToDto(T entity, DTO dto);

    /**
     * DTO的特殊逻辑转换的数据赋值给Entity
     *
     * @param dto
     * @param entity
     */
    void assignDtoToEntity(DTO dto, T entity);
}
