package com.hyd.ssdb.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;

/**
 * Created by lilg1 on 2017/7/20.
 */
public class JSONUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(JSONUtil.class);

    private static ObjectMapper om = new ObjectMapper();

    static {
        //提供其它默认设置
        om.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        om.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        om.setFilters(new SimpleFilterProvider().setFailOnUnknownId(false));

    }


    public static String object2json(Object object) throws JsonProcessingException {
        return om.writeValueAsString(object);
    }

    public static String object2jsonWithoutException(Object object) {
        try {
            return object2json(object);
        } catch (JsonProcessingException e) {
            LOGGER.debug("Exception", e);
            return null;
        }
    }

    public static <T> Map<String, T> json2Map(String json) {
        return JSON.toJavaObject(JSON.parseObject(json), Map.class);
    }

    public static JSONObject json2JSONObject(String json){
        return JSON.parseObject(json);
    }

    public static final <T> T json2Object(String text, Class<T> clazz) throws IOException {

        return om.readValue(text, clazz);
    }

    public static final <T> T json2Object(String text, TypeToken<T> typeToken) {
        GsonBuilder gsonBuilder = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm");
        Gson gson = gsonBuilder.create();
        return gson.fromJson(text, typeToken.getType());
    }

    public static String map2json(Map<String, ? extends Object> map) {
        return JSON.toJSONString(map);
    }
}
