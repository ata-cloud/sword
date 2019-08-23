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
