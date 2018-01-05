package cn.trustway.nb.core.base.util;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GsonUtil {
    private static Gson gson = getGson();

    private GsonUtil() {
    }

    private static Gson getGson() {
        if (gson == null) {
            gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        }

        return gson;
    }

    public static String toJson(Object obj) {
        String json = gson.toJson(obj);
        try {
            return URLEncoder.encode(json, "utf-8");
        } catch (Exception e) {
            return null;
        }
    }

    public static String toJsonNoEncode(Object obj) {
        String json = gson.toJson(obj);
        try {
            return json;
        } catch (Exception e) {
            return null;
        }
    }

    public static <T> T fromJson(String json, @NonNull Class<T> classoft) {
        try {
            json = URLDecoder.decode(json, "utf-8");
            return gson.fromJson(json, classoft);
        } catch (Exception e) {
            return null;
        }
    }

    public static <T> T fromJsonNoDecode(@Nullable String json, @NonNull Class<T> classoft) {
        try {
//			json=URLDecoder.decode(json, "utf-8");
            return gson.fromJson(json, classoft);
        } catch (Exception e) {
            return null;
        }
    }

    public static <T> T fromJsonNoDecode(String json, Type type) {
        try {
            return gson.fromJson(json, type);
        } catch (Exception e) {
            return null;
        }
    }

    public static <T> ArrayList<T> jsonToArrayList(String json, @NonNull Class<T> clazz) {
        try {
            Type type = new TypeToken<ArrayList<JsonObject>>() {
            }.getType();
            ArrayList<JsonObject> jsonObjects = new Gson().fromJson(json, type);

            ArrayList<T> arrayList = new ArrayList<>();
            for (JsonObject jsonObject : jsonObjects) {
                arrayList.add(new Gson().fromJson(jsonObject, clazz));
            }
            return arrayList;
        }catch (Exception e){
            return null;
        }
    }

//	public static List fromJsonNoDecode(String json, Type type){
//		try {
//			return gson.fromJson(json, type);
//		} catch (Exception e) {
//			LogUtil.log(LogUtil.tag_app,e.getMessage());
//			return null;
//		}
//	}

    public static Map<String, Object> toMap(String json){
        Type type = new TypeToken<HashMap<String,Object>>(){}.getType();
        return new Gson().<HashMap<String, Object>>fromJson(json, type);
    }


}
