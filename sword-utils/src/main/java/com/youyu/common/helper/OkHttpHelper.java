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
package com.youyu.common.helper;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;

import java.io.IOException;

/**
 * OkHttp 帮助类
 *
 * @author WangSongJun
 * @date 2018-07-25 9:14 星期三
 */
@Slf4j
public class OkHttpHelper {
    private final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private OkHttpClient client;
    private Gson gson = new Gson();

    public OkHttpHelper() {
        this.client = new OkHttpClient();
    }

    public OkHttpHelper(OkHttpClient client) {
        this.client = client;
    }

    public enum RequestMethod{
        GET,POST,PUT,DELETE;
    }

    /**
     * get请求
     *
     * @param url
     * @param ref
     * @param <T>
     * @return
     */
    public <T> T get(String url, TypeReference<T> ref) {
        return call(RequestMethod.GET, url, null, ref);
    }

    /**
     * post请求
     *
     * @param url
     * @param data
     * @param ref
     * @param <T>
     * @return
     */
    public <T> T post(String url, String data, TypeReference<T> ref) {
        return call(RequestMethod.POST, url, data, ref);
    }

    /**
     * 调用api
     *
     * @param method
     * @param url
     * @param data
     * @param ref
     * @param <T>
     * @return
     */
    public <T> T call(RequestMethod method, String url, String data, TypeReference<T> ref) {
        log.debug("call:{} {}\ndata:{}",method,url,data);
        try {
            Request.Builder builder;
            if (RequestMethod.POST.equals(method)) {
                RequestBody body = RequestBody.create(JSON, data);
                builder = new Request.Builder().url(url).post(body);
            } else {
                builder = new Request.Builder().url(url).get();
            }
            Request request = builder.build();
            Response response = client.newCall(request).execute();
            String s = response.body().string();
            log.debug("返回数据：{}",s);
            if (String.class.getTypeName().equals(ref.getType().getTypeName())) {
                return (T) s;
            } else {
                return gson.fromJson(s, ref.getType());
            }
        } catch (IOException e) {
            log.warn("网络连接异常",e);
            return null;
        }
    }
}
