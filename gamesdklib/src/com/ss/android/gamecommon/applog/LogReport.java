package com.ss.android.gamecommon.applog;

import android.content.Context;
import android.text.TextUtils;

import com.ss.android.gamecommon.thread.RetrofitManager;
import com.ss.android.gamecommon.util.JsonMap;
import com.ss.android.gamecommon.util.LogUtil;
import com.ss.android.gamecommon.util.NetworkType;
import com.ss.android.gamecommon.util.ToolUtils;
import com.ss.android.gamecommon.util.UrlUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

import static com.ss.android.gamecommon.util.LogConstantUtil.UMENG_CATEGORY;

public class LogReport {
    private static LogReport sInstance;

    public static LogReport getInstance() {
        if (null == sInstance) {
            synchronized (LogReport.class) {
                if (null == sInstance) {
                    sInstance = new LogReport();
                }
            }
        }
        return sInstance;
    }

    private String mPage;
    public static String sSessionId;

    public LogReport() {
        if (TextUtils.isEmpty(sSessionId)) {
            sSessionId = UUID.randomUUID().toString();
        }

    }

    public LogReport(String page) {
        this.mPage = page;
    }

    public void onEvent(Context context, String event, String eventType) {
        onEvent(context, event, eventType, 0);
    }

    public void onEvent(Context context, String event, String eventType, JSONObject launchEvent) {
        onEvent(context, event, eventType, 0, launchEvent);
    }


    private void onEvent(Context context, String event, String eventType, int errorCode) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("event_type_value", eventType);
            onEvent(context, mPage, event, jsonObject, null);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void onEvent(Context context, String event, String eventType, int errorCode, JSONObject launchEvent) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("event_type_value", eventType);
            onEvent(context, mPage, event, jsonObject, launchEvent);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void onEvent(Context context, String tag, String label, JSONObject ext_json, JSONObject launchEvent) {
        ext_json = onEvent(context, UMENG_CATEGORY, tag, label, 0, 0, true, ext_json);
        if (launchEvent != null) {
            sendEvent(context, ext_json, launchEvent);
        } else {
            sendEvent(context, ext_json, null);
        }

    }

    private JSONObject onEvent(Context context, String category, String tag, String label, long value, long ext_value, boolean istant_obly, JSONObject ext_json) {
        try {
            NetworkType networkType = ToolUtils.getNetWorkType(context.getApplicationContext());
            if (networkType == NetworkType.NONE) {
                LogUtil.e(MessageFormat.format("网络异常，日志上报终止,tag={0},label={1}", tag, label));
                return null;
            }
            if (ext_json == null) {
                ext_json = new JSONObject();
            }
            ext_json.put("nt", networkType.getNetworkType());
            ext_json.put("session_id", sSessionId);
            ext_json.put("category", category);
            ext_json.put("tag", tag);
            ext_json.put("value", value);
            ext_json.put("label", label);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String dateTime = simpleDateFormat.format(new Date());
            ext_json.put("datetime", dateTime);

            return ext_json;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void sendEvent(Context context, JSONObject ext_json, JSONObject launch_json) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("magic_tag", "ss_app_log");
            jsonObject.put("header", BaseAppLog.getInstance(context).getHeaders(context));
            if (launch_json != null) {
                jsonObject.put("launch", launch_json);
            }
            JSONArray jsonArray = new JSONArray();
            jsonArray.put(ext_json);
            jsonObject.put("event", jsonArray);
            //开始上报数据
            HashMap<String, Object> mHashMap = new HashMap<String, Object>();
            //    mHashMap = UrlUtil.extMap(context, true);

            //    RetrofitManager.getInstance().reportLog(context, mHashMap, jsonObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void sendLog(Context context, JSONObject content) {
        RetrofitManager.getInstance().sendLog(context, content);
    }

    public void sendLog(Context context, String eventType, String eventLabel) {
        JSONObject sendData = new JSONObject();
        try {
            sendData.put("magic_tag", "ss_app_log");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        sendData = putHeader(context, sendData);
        sendData = putLaunch(sendData);
        sendData = putEvent(sendData, eventType, eventLabel);

        sendLog(context, sendData);

    }

    private JSONObject putLaunch(JSONObject jsonObject) {
        if (jsonObject == null) {
            jsonObject = new JSONObject();
        }
        JSONObject launchJson = new JSONObject();
        try {
            launchJson.put("session_id", sSessionId);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String date = simpleDateFormat.format(new Date());
            launchJson.put("datetime", date);
            jsonObject.put("launch", launchJson);
            return jsonObject;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    private JSONObject putHeader(Context context, JSONObject jsonObject) {
        if (jsonObject == null) {
            jsonObject = new JSONObject();
        }
        try {
            //jsonObject.put("header", JsonMap.getJson(UrlUtil.extMap(context, false)));
            jsonObject.put("header", JsonMap.getJson(headerMap()));
            return jsonObject;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    private JSONObject putEvent(JSONObject jsonObject, String eventType, String eventLabel) {
        if (jsonObject == null) {
            jsonObject = new JSONObject();
        }
        JSONArray arrayEvent = new JSONArray();
        JSONObject jsonEvent = new JSONObject();
        try {
            jsonEvent.put("event_type_value", eventType);
            jsonEvent.put("nt", 4);
            jsonEvent.put("category", "umeng");
            jsonEvent.put("tag", "SDK_GAME");
            jsonEvent.put("label", eventLabel);
            jsonEvent.put("value", 0);
            jsonEvent.put("session_id", sSessionId);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String date = simpleDateFormat.format(new Date());
            jsonEvent.put("datetime", date);
            arrayEvent.put(jsonEvent);
            jsonObject.put("event", arrayEvent);
            return jsonObject;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;

    }

    private HashMap<String, Object> headerMap() {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("udid", "352105061736667");
        map.put("openudid", "9a72ef359c705f5e");
        map.put("sdk_version", 334);
        map.put("package", "com.ss.android.GameSdkDemo");
        map.put("display_name", "GameSdk");
        map.put("app_version", "3.3.4");
        map.put("version_code", 334);
        map.put("timezone", 8);
        map.put("access", "wifi");
        map.put("os_version", "6.0.1");
        map.put("os_api", 23);
        map.put("os", "Android");
        map.put("device_model", "SM-G9008W");
        map.put("language", "zh");
        map.put("resolution", "1920x1080");
        map.put("display_density", "mdpi");
        map.put("mc", "F4:09:D8:11:B9:69");
        map.put("clientudid", "48a14e03-ae78-4254-9e25-0804e91397d9");
        map.put("install_id", "52669568942");
        map.put("device_id", "54160797054");
        map.put("sig_hash", "ba149aded052a345f5b96dca34eea7ae");
        map.put("aid", 0);
        // int[] array = {1, 2, 4};
        //map.put("push_sdk", array);
        map.put("rom", "G9008WZMU1CQB1");
        map.put("access_token", "");
        map.put("client_key", "tt");
        map.put("ut", 0);
        return map;

    }


}
