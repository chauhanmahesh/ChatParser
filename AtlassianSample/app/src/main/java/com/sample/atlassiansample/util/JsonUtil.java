package com.sample.atlassiansample.util;

import android.content.Context;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.lang.reflect.Type;

public class JsonUtil {

    public static Object parseJsonToType(Context context,
            JSONObject jsonObject, Type type) {
        Gson gson = new Gson();
        Object result = gson.fromJson(jsonObject.toString(), type);
        return result;
    }

    public static String convertTypeToJson(Context context, Object object,
            Type type) {
        Gson gson = new Gson();
        String result = gson.toJson(object, type);
        return result;
    }

}
