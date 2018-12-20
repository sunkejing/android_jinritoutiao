package com.ss.android.gamecommon.util;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.util.Base64;

import org.w3c.dom.Text;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import static com.ss.android.game.sdk.SsGameApi.CLIENTID;
import static com.ss.android.gamecommon.util.ConstantUtil.SDK_VERSION_CODE;

//路径相关的工具类
public class UrlUtil {
    private static final String MAGIC_STR = "72f0296b";
    private static SharedPreferenceUtil sharedPreferenceUtil;

    public static String extUrl(Context context, boolean isPost) {
        return buildMap((LinkedHashMap<String, Object>) extMap(context, isPost));

    }

    public static HashMap extMap(Context context, boolean isPost) {
        sharedPreferenceUtil = SharedPreferenceUtil.getInstance(context);
        LinkedHashMap<String, Object> mHashMap = new LinkedHashMap<String, Object>();
        String iidStr = sharedPreferenceUtil.getString("iid", "");
        LogUtil.e("iid:" + iidStr);
        if (!TextUtils.isEmpty(iidStr)) {
            mHashMap.put("iid", iidStr);
        }
        String deviceId = sharedPreferenceUtil.getString(SharedPreferenceUtil.DEVICE_ID, "");
        if (!TextUtils.isEmpty(deviceId)) {
            mHashMap.put("device_id", deviceId);
        }
        String networkType = ToolUtils.getNetworkState(context);
        mHashMap.put("ac", networkType);
        mHashMap.put("utm_campaign", "open");
        mHashMap.put("utm_medium", "sdk");
        mHashMap.put("client_key", CLIENTID);
        mHashMap.put("sdk_version", SDK_VERSION_CODE);
        mHashMap.put("package", ToolUtils.getPackName(context));
        long timeStap = System.currentTimeMillis();
        mHashMap.put("timestamp", timeStap);
        String sign = ComputeSign(timeStap, isPost).trim();
        if (!TextUtils.isEmpty(sign)) {
            mHashMap.put("sign", sign);
        }
        mHashMap.put("app_name", ToolUtils.getAppName(context));
        mHashMap.put("version_code", ToolUtils.getVersionCode(context));
        mHashMap.put("version_name", ToolUtils.getVersionName(context));
        mHashMap.put("device_platform", "android");
        mHashMap.put("ssmix", "a");
        mHashMap.put("device_type", Build.MODEL);//手机型号
        mHashMap.put("os_api", String.valueOf(Build.VERSION.SDK_INT));
        mHashMap.put("os_version", Build.VERSION.RELEASE);
        String openudid = ToolUtils.openUDID(context);
        if (!TextUtils.isEmpty(openudid)) {
            mHashMap.put("openudid", openudid);
        }


        mHashMap.put("uuid", ToolUtils.getUDID(context));

        return mHashMap;


    }

    public static String ComputeSign(long timeStap, boolean isPost) {
        HashMap<String, Object> mMap = new HashMap<String, Object>();
        mMap.put("client_key", CLIENTID);
        mMap.put("timestamp", String.valueOf(timeStap));
        mMap.put("version", Integer.parseInt(ConstantUtil.SDK_VERSION_CODE));
        mMap.put("magic", MAGIC_STR);
        if (isPost) {
            mMap.put("method", "post");
        } else {
            mMap.put("method", "get");
        }

        Object[] key = mMap.keySet().toArray();
        Arrays.sort(key);
        StringBuffer sb = null;
        for (int i = 0; i < key.length; i++) {
            String keyStr = (String) key[i];
            Object value = mMap.get(keyStr);
            if (sb == null) {
                sb = new StringBuffer();
            } else {
                sb.append("&");
            }
            sb.append(keyStr);
            sb.append("=");
            sb.append(value);
        }
        String unSign = sb.toString();
        String unicode = null;
        try {
            unicode = URLEncoder.encode(unSign, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        //hmacsha1加密
        try {
            String sign = hmacSha1(unicode, CLIENTID);
            return sign;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        return null;

    }

    public static String buildMap(HashMap<String, Object> map) {
        // 添加url参数  
        if (map != null) {
            Iterator<String> it = map.keySet().iterator();
            StringBuffer sb = null;
            while (it.hasNext()) {
                String key = it.next();
                Object value = map.get(key);
                if (sb == null) {
                    sb = new StringBuffer();
                    sb.append("?");
                } else {
                    sb.append("&");
                }
                sb.append(key);
                sb.append("=");
                sb.append(value);
            }
            return sb.toString();
        }
        return null;

    }

    public static String hmacSha1(String base, String key) throws NoSuchAlgorithmException, InvalidKeyException {
        if(TextUtils.isEmpty(key)){
            return "";
        }
        String type = "HmacSHA1";
        SecretKeySpec secret = new SecretKeySpec(key.getBytes(), type);
        Mac mac = Mac.getInstance(type);
        mac.init(secret);
        byte[] digest = mac.doFinal(base.getBytes());
        return Base64.encodeToString(digest, Base64.DEFAULT);

    }


}
