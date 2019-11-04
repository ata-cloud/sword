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
import com.youyu.common.entity.IBaseEntity;
import com.youyu.common.mapper.YyMapper;
import com.youyu.common.utils.YyBeanUtils;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


/**
 * 抽象service
 *
 * @author superwen
 * @date 2017/10/23 下午2:20
 */
@Data
public abstract class AbstractService<IDTYPE, DTO extends IBaseDto<IDTYPE>, ENTITY extends IBaseEntity<IDTYPE>, M extends YyMapper<ENTITY>> implements IService<DTO, ENTITY> {

    @Autowired
    protected M mapper;

    private Class entityClass;
    private Class dtoClass;

    @Override
    public DTO selectOne(ENTITY record) {
        return convertEntityToDto(mapper.selectOne(record));
    }

    @Override
    public DTO convertEntityToDto(ENTITY entity) {
        if (entity == null) {
            return null;
        }
        DTO dto = null;
        try {
            if (dtoClass == null) {
                dtoClass = Class.forName(getDtoClassName());
            }
            dto = (DTO) dtoClass.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        YyBeanUtils.copyProperties(entity, (DTO) dto);
        assignEntityToDto(entity, dto);
        return dto;
    }

    @Override
    public void assignEntityToDto(ENTITY entity, DTO dto) {

    }

    @Override
    public ENTITY convertDtoToEntity(DTO dto) {
        if (dto == null) {
            return null;
        }
        ENTITY entity = null;
        try {
            if (entityClass == null) {
                entityClass = Class.forName(getEntityClassName());
            }
            entity = (ENTITY) entityClass.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        YyBeanUtils.copyProperties(dto, entity);
        assignDtoToEntity(dto, entity);
        return entity;
    }

    private String getEntityClassName() {
        //直接获得 ENTITY type.
        return ((ParameterizedType) this.getClass().getGenericSuperclass())
                .getActualTypeArguments()[2]
                .getTypeName();
    }

    private String getDtoClassName() {
        return ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[1].getTypeName();
    }

    @Override
    public void assignDtoToEntity(DTO dto, ENTITY entity) {

    }

    @Override
    public List<DTO> select(ENTITY record) {
        List<ENTITY> entityList = mapper.select(record);
        if (CollectionUtils.isEmpty(entityList)) {
            return null;
        }
        List<DTO> dtoList = Optional.ofNullable(entityList).get().stream().map(
                entity -> convertEntityToDto(entity)
        ).collect(Collectors.toList());
        return dtoList;
    }

    @Override
    public List<DTO> selectByExample(Object object) {
        List<ENTITY> entityList = mapper.selectByExample(object);
        if (CollectionUtils.isEmpty(entityList)) {
            return null;
        }
        List<DTO> dtoList = Optional.ofNullable(entityList).get().stream().map(
                entity -> convertEntityToDto(entity)
        ).collect(Collectors.toList());
        return dtoList;
    }

    @Override
    public List<ENTITY> selectEntityByExample(Object example) {
        return mapper.selectByExample(example);
    }

    @Override
    public List<DTO> selectByExampleInPage(Object object, PageData pageData) {
        List<ENTITY> entityList = mapper.selectByExampleAndRowBounds(object, pageData);
        if (CollectionUtils.isEmpty(entityList)) {
            return null;
        }
        List<DTO> dtoList = Optional.ofNullable(entityList).get().stream().map(
                entity -> convertEntityToDto(entity)
        ).collect(Collectors.toList());
        return dtoList;
    }

    @Override
    public List<ENTITY> selectEntityByExampleInPage(Object example, PageData pageData) {
        return mapper.selectByExampleAndRowBounds(example, pageData);
    }

    /**
     * 根据实体中的属性进行查询，只能有一个返回值，有多个结果是抛出异常，查询条件使用等号
     *
     * @param record
     * @return
     */
    @Override
    public ENTITY selectOneEntity(ENTITY record) {
        return mapper.selectOne(record);
    }

    /**
     * 根据实体中的属性值进行查询，查询条件使用等号
     *
     * @param record
     * @return
     */
    @Override
    public List<ENTITY> selectEntity(ENTITY record) {
        return mapper.select(record);
    }

    /**
     * 查询全部结果
     *
     * @return
     */
    @Override
    public List<ENTITY> selectAllEntity() {
        return mapper.selectAll();
    }

    /**
     * 根据主键字段进行查询，方法参数必须包含完整的主键属性，查询条件使用等号
     *
     * @param key
     * @return
     */
    @Override
    public ENTITY selectEntityByPrimaryKey(Object key) {
        return mapper.selectByPrimaryKey(key);
    }

    @Override
    public PageData<DTO> selectInPage(ENTITY record, PageData pageData) {
        List<ENTITY> entityList = mapper.selectInPage(record, pageData);
        if (CollectionUtils.isEmpty(entityList)) {
            return null;
        }
        List<DTO> dtoList = entityList.stream().map(
                entity -> convertEntityToDto(entity)
        ).collect(Collectors.toList());
        pageData.setRows(dtoList);
        return pageData;
    }

    @Override
    public List<DTO> selectAll() {

        List<ENTITY> entityList = mapper.selectAll();
        if (CollectionUtils.isEmpty(entityList)) {
            return null;
        }
        List<DTO> dtoList = Optional.ofNullable(entityList).get().stream().map(
                entity -> convertEntityToDto(entity)
        ).collect(Collectors.toList());
        return dtoList;
    }

    @Override
    public PageData<DTO> selectAllInPage(PageData pageData) {

        List<ENTITY> entityList = mapper.selectAllInPage(pageData);
        if (CollectionUtils.isEmpty(entityList)) {
            return null;
        }
        List<DTO> dtoList = entityList.stream().map(
                entity -> convertEntityToDto(entity)
        ).collect(Collectors.toList());
        pageData.setRows(dtoList);
        return pageData;
    }

    @Override
    public int selectCount(DTO dto) {
        ENTITY entity = convertDtoToEntity(dto);
        return mapper.selectCount(entity);
    }

    @Override
    public int selectCount(ENTITY record) {
        return mapper.selectCount(record);
    }

    @Override
    public DTO selectByPrimaryKey(Object key) {
        return convertEntityToDto(mapper.selectByPrimaryKey(key));
    }

    @Override
    public int insert(DTO dto) {
        ENTITY entity = convertDtoToEntity(dto);
        int addNum = mapper.insert(entity);
        dto.setId(entity.getId());
        return addNum;
    }

    @Override
    public int insertSelective(DTO dto) {
        ENTITY entity = convertDtoToEntity(dto);
        int addNum = mapper.insertSelective(entity);
        dto.setId(entity.getId());
        return addNum;
    }

    @Override
    public int insert(ENTITY entity) {
        return mapper.insert(entity);
    }

    @Override
    public int insertSelective(ENTITY entity) {
        return mapper.insertSelective(entity);
    }

    @Override
    public int updateByPrimaryKey(DTO dto) {
        ENTITY entity = convertDtoToEntity(dto);
        return mapper.updateByPrimaryKey(entity);
    }

    @Override
    public int updateByPrimaryKeySelective(DTO dto) {
        ENTITY entity = convertDtoToEntity(dto);
        return mapper.updateByPrimaryKeySelective(entity);
    }

    @Override
    public int updateByPrimaryKey(ENTITY entity) {
        return mapper.updateByPrimaryKey(entity);
    }

    @Override
    public int updateByPrimaryKeySelective(ENTITY entity) {
        return mapper.updateByPrimaryKeySelective(entity);
    }

    @Override
    public int updateByExample(ENTITY record, Object example) {
        return mapper.updateByExample(record, example);
    }

    @Override
    public int updateByExampleSelective(ENTITY record, Object example) {
        return mapper.updateByExampleSelective(record, example);
    }

    @Override
    public int delete(DTO dto) {
        ENTITY entity = convertDtoToEntity(dto);
        return mapper.delete(entity);
    }

    @Override
    public int deleteByPrimaryKey(Object key) {
        return mapper.deleteByPrimaryKey(key);
    }
}
