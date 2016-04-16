package com.java.lib.oil.json;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wb-liutiantian.h on 2016/3/17.
 */
public class JSON {
    private static Gson mGson = new Gson();

    public static <T> String toJsonString(T object, Class<T> classObject) {
        if (object == null || classObject == null) {
            return null;
        }
        return mGson.toJson(object, classObject);
    }

    public static <T> T parseObject(String text, Class<T> classObject) {
        if (text == null || classObject == null) {
            return null;
        }
        return mGson.fromJson(text, classObject);
    }

    public static <T> List<T> parseArray(String text, Class<T> classObject) {
        if (text == null || classObject == null) {
            return null;
        }
        JsonParser parser = new JsonParser();
        JsonArray array = parser.parse(text).getAsJsonArray();
        if (array != null && array.size() > 0) {
            List<T> list = new ArrayList<>();
            for (JsonElement element : array) {
                list.add(mGson.fromJson(element, classObject));
            }
            return list;
        }
        return null;
    }

    public static Gson getGsonInstance() {
        return mGson;
    }
}
