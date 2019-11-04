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



import com.youyu.common.utils.YyBeanUtils;

import java.lang.reflect.InvocationTargetException;

/**
 * @author superwen
 * @date 2018/9/4 上午11:11
 */
public class DemoTest {
    public static void main(String[] args) throws InvocationTargetException, IllegalAccessException {

        DemoEntity entity = new DemoEntity();
        entity.setId(1000L);
        entity.setName("name");

        DemoDto d = new DemoDto();


        Class clazz = DemoEntity.class;
        clazz.getMethods();

        long start = System.currentTimeMillis();

//        BeanUtils.copyProperties(d,entity);
//        for (int i = 0; i < 1000000L; i++) {
//            BeanUtils.copyProperties(d, entity);
            YyBeanUtils.copyProperties(entity,d);

//        }


        long end = System.currentTimeMillis();
        System.out.println(end - start);
        System.out.println(d);
    }
}
