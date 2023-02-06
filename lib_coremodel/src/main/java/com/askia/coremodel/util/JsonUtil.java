package com.askia.coremodel.util;

import android.util.JsonReader;

import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.LogUtils;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class JsonUtil {

    private static Gson gson = null;

    static {
        if (gson == null) {
            gson = new Gson();
        }
    }

    public static <T> T Str2JsonBean(String json, Class<T> clazz) {
        T bean = null;
        if (null != gson) {
            try {
                bean = gson.fromJson(json, clazz);
            } catch (JsonSyntaxException e) {
                e.printStackTrace();
            }
        }
        return bean;
    }


    public static String JsonBean2Str(Object object) {
        String str = null;
        if (null != gson) {
            try {
                str = gson.toJson(object);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return str;
    }

    public static String JsonList2Str(List list) {
        String str = null;
        if (null != gson && list.size() > 0) {
            str = "[";
            for (int i = 0; i < list.size(); i++) {
                if (i != list.size() - 1) {
                    str += gson.toJson(list.get(i)) + ",";
                } else if (i == list.size() - 1) {
                    str += gson.toJson(list.get(i)) + "]";
                }
            }
        }
        return str;
    }

    public static JsonObject JavaBean2JsonObject(Object object) {
        if (gson == null) {
            gson = new Gson();
        }
        return gson.fromJson(JsonBean2Str(object), JsonObject.class);
    }

    public static JsonObject Str2JsonObject(String key, String str) {
        JsonObject object = new JsonObject();
        object.addProperty(key, str);
        return gson.fromJson(JsonBean2Str(object), JsonObject.class);
    }

    public static <T> List<T> file2JsonArray(String path, Class<T> t) {
        File file = FileUtils.getFileByPath(path);
        List<T> list = new ArrayList<>();
        String result = null;
        try {
            FileInputStream inputStream = new FileInputStream(file);
            byte[] bytes = new byte[inputStream.available()];
            inputStream.read(bytes);
            result = new String(bytes);
//            Type type = new TypeToken<List<T>>(){}.getType();
//            bean = gson.fromJson(result,type);
            JsonArray array = new JsonParser().parse(result).getAsJsonArray();
            for (JsonElement element : array){
                list.add(gson.fromJson(element,t));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 读取文件内容并转json
     * @param path 文件路径
     * @param t 转换类型
     * @param <T> 泛型
     * @return
     */
    public static <T> T file2JsonObject(String path, Class<T> t){
        T bean = null;
        File file = FileUtils.getFileByPath(path);
        String json = null;
        try {
            FileInputStream inputStream = new FileInputStream(file);
            byte [] bytes = new byte[inputStream.available()];
            inputStream.read(bytes);
            json = new String(bytes);
            bean = Str2JsonBean(json,t);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  bean;
    }
}
