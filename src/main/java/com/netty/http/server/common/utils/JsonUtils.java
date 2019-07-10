package com.netty.http.server.common.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JsonUtils {

    private static final GsonBuilder GSON_BUILDER = new GsonBuilder();
    private static final Gson GSON = GSON_BUILDER.disableHtmlEscaping().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

    private JsonUtils() {

    }

    public static String toJson(Object o) {
        return GSON.toJson(o);
    }

    public static <T> T fromJson(String json, Class<T> clazz) {
        return GSON.fromJson(json, clazz);
    }
}
