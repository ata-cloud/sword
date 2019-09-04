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
package com.youyu.common.trim;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.youyu.common.trim.annotation.Trimmed;

import java.io.IOException;

/**
 * @author superwen
 * @date 2018/11/8 下午7:00
 */
public class TrimmedJsonDeserializer extends StdDeserializer<String> {

    public static TrimmedJsonDeserializer SIMPLE = new TrimmedJsonDeserializer(Trimmed.TrimmerType.SIMPLE);
    public static TrimmedJsonDeserializer ALL_WHITESPACES = new TrimmedJsonDeserializer(Trimmed.TrimmerType.ALL_WHITESPACES);
    public static TrimmedJsonDeserializer EXCEPT_LINE_BREAK = new TrimmedJsonDeserializer(Trimmed.TrimmerType.EXCEPT_LINE_BREAK);

    private Trimmed.TrimmerType trimmerType;

    protected TrimmedJsonDeserializer() {
        super(String.class);

    }

    TrimmedJsonDeserializer(Trimmed.TrimmerType trimmerType) {
        super(String.class);
        this.trimmerType = trimmerType;
    }


    @Override
    public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        return TrimmedUtils.parse(p.getValueAsString(), trimmerType);
    }

}
