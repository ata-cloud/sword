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
package com.youyu.common.factory;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.youyu.common.trim.TrimmedAnnotationIntrospector;
import com.youyu.common.trim.annotation.Trimmed;
import lombok.Data;
import org.junit.Test;

import java.io.IOException;

/**
 * @author superwen
 * @date 2018/11/8 下午7:38
 */
public class TrimmedAnnotationIntrospectorTest {

    @Test
    public void test1() throws IOException {
        Demo demo = new Demo();
        demo.setName(" asdfafs ");
        ObjectMapper objectMapper = new ObjectMapper();
//        objectMapper.setd
//        objectMapper.getDeserializationConfig().getHandlerInstantiator()
//        objectMapper.setAnnotationIntrospectors()

        objectMapper.setAnnotationIntrospector(new TrimmedAnnotationIntrospector(objectMapper.getDeserializationConfig().getAnnotationIntrospector()));
        System.out.println(objectMapper.writeValueAsString(demo));

        String str = "{\"name\":\" asdfafs \"}";
        System.out.println(objectMapper.readValue(str, Demo.class));
        System.out.println(objectMapper.readValue(str, Demo.class));
        System.out.println(objectMapper.readValue(str, Demo.class));
        System.out.println(objectMapper.readValue(str, Demo.class));
    }


    @Data
//    @JsonDeserialize(using = TrimmedJsonDeserializer.class)
    static class Demo {
        @Trimmed
//        @JsonDeserialize(using = TrimmedJsonDeserializer.class)

        private String name;
    }
}
