package com.ss.android.gamecommon.util;

import android.content.SharedPreferences;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;

public class ListDataSaveUtil {

    public static <T> void setDataList(SharedPreferences sharedPreferences, String tag, List<T> dataList) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (null == dataList || dataList.size() <= 0) {
            return;
        }
        Gson gson = new Gson();
        String strJson = gson.toJson(dataList);
        LogUtil.e("保存用户信息:" + strJson);
        editor.putString(tag, strJson);
        editor.commit();

    }

    public static <T> List<T> getDataList(SharedPreferences sharedPreferences, String tag, Class<T> clazz) {
        List<T> dataList = new ArrayList<T>();
        String strJson = sharedPreferences.getString(tag, null);
        LogUtil.e("获取用户信息:" + strJson);
        if (!TextUtils.isEmpty(strJson)) {
            JsonArray array = new JsonParser().parse(strJson).getAsJsonArray();
            for (final JsonElement element : array) {
                dataList.add(new Gson().fromJson(element, clazz));
            }
            return dataList;
        }
        return null;


    }


}
