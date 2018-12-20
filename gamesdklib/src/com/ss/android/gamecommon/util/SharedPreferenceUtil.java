package com.ss.android.gamecommon.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Base64;

import com.ss.android.game.sdk.info.UserInfo;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SharedPreferenceUtil {
    private static SharedPreferences preferences;
    private static Context context;
    public String spName = "ss_sdkPreference";
    public static final String DEVICE_ID = "device_id";
    public static final String HAS_LOGIN = "has_login";
    public static final String USER_INFO = "user_info";
    public static final String ALL_USER = "all_user_info";
    public static final String HAS_VERIFIED = "has_verified";
    public static final String NEED_VERIFY = "need_verify";
    public static final String PAY_METHOD = "way";

    private static SharedPreferenceUtil sharedPreferenceUtil;

    public static SharedPreferenceUtil getInstance(Context context) {
        if (sharedPreferenceUtil == null) {
            synchronized (SharedPreferenceUtil.class) {
                if (sharedPreferenceUtil == null) {
                    sharedPreferenceUtil = new SharedPreferenceUtil(context);
                }
            }
        }
        return sharedPreferenceUtil;
    }

    private SharedPreferenceUtil(Context context) {
        preferences = context.getSharedPreferences(spName, context.MODE_PRIVATE | Context.MODE_MULTI_PROCESS);
    }

    /**
     * 向SP存入指定key对应的数据     * 其中value可以是String、boolean、float、int、long等各种基本类型的值     * @param key     * @param value
     */
    public void putString(String key, String value) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public void putBoolean(String key, boolean value) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public void putFloat(String key, float value) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putFloat(key, value);
        editor.commit();
    }

    public void putInt(String key, int value) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public void putLong(String key, long value) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putLong(key, value);
        editor.commit();
    }

    /**
     * 清空SP里所以数据
     */
    public void clear() {
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.commit();
    }

    /**
     * 删除SP里指定key对应的数据项     * @param key
     */
    public void remove(String key) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove(key);
        editor.commit();
    }

    /**
     * 获取SP数据里指定key对应的value。如果key不存在，则返回默认值defValue。     * @param key     * @param defValue     * @return
     */
    public String getString(String key, String defValue) {
        return preferences.getString(key, defValue);
    }

    public boolean getBoolean(String key, boolean defValue) {
        return preferences.getBoolean(key, defValue);
    }

    public float getFloat(String key, float defValue) {
        return preferences.getFloat(key, defValue);
    }

    public int getInt(String key, int defValue) {
        return preferences.getInt(key, defValue);
    }

    public long getLong(String key, long defValue) {
        return preferences.getLong(key, defValue);
    }

    /**
     * 判断SP是否包含特定key的数据     * @param key     * @return
     */
    public boolean contains(String key) {
        return preferences.contains(key);
    }

    public void setHasLogin(boolean isLoginSuccess) {
        putString(HAS_LOGIN, String.valueOf(isLoginSuccess));
    }

    public boolean isAutoLogin() {
        String hasLogin = getString(HAS_LOGIN, "false");
        if ("false".equalsIgnoreCase(hasLogin)) {
            return false;
        } else {
            return true;
        }

    }

    public void setAccount(String account) {
        if (!TextUtils.isEmpty(account)) {
            putString("account", account);
        }

    }

    public String getAccount() {
        return getString("account", "null");
    }

    /**
     * 保存账号信息
     *
     * @param sUserInfo
     */
    public void saveAccountInfo(UserInfo sUserInfo) {

    }

    /**
     * 存放实体类以及任意类型
     *
     * @param key
     * @param obj
     */
    public void putBean(String key, Object obj) {
        if (obj instanceof Serializable) {// obj必须实现Serializable接口，否则会出问题
            try {
                SharedPreferences.Editor editor = preferences.edit();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(baos);
                oos.writeObject(obj);
                String string64 = new String(Base64.encode(baos.toByteArray(),
                        0));
                editor.putString(key, string64).commit();
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {
            throw new IllegalArgumentException(
                    "the obj must implement Serializble");
        }

    }

    public Object getBean(String key) {
        Object obj = null;
        try {
            String base64 = preferences.getString(key, "");
            if (base64.equals("")) {
                return null;
            }
            byte[] base64Bytes = Base64.decode(base64.getBytes(), 1);
            ByteArrayInputStream bais = new ByteArrayInputStream(base64Bytes);
            ObjectInputStream ois = new ObjectInputStream(bais);
            obj = ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return obj;
    }

    public void saveAllAccountInfo(UserInfo userInfo) {
        boolean isContain = false;
        List<UserInfo> allUserInfos = new ArrayList<>();
        List<UserInfo> userInfos = ListDataSaveUtil.getDataList(preferences, ALL_USER, UserInfo.class);
        if (userInfos != null && !userInfos.isEmpty()) {
            for (UserInfo user : userInfos) {
                if (user.getOpenId().equals(userInfo.getOpenId())) {
                    //更新user信息
                    user.setAccessToken(userInfo.getAccessToken());
                    user.setOpenId(userInfo.getOpenId());
                    user.setUserId(userInfo.getUserId());
                    user.setUserType(userInfo.getUserType());
                    user.setMobile(userInfo.getMobile());
                    user.setLoginId(userInfo.getLoginId());
                    allUserInfos.add(user);
                    isContain = true;
                } else {
                    allUserInfos.add(user);
                }
            }
            if (!isContain) {
                allUserInfos.add(userInfo);
            }
            ListDataSaveUtil.setDataList(preferences, ALL_USER, allUserInfos);

        } else {
            allUserInfos.add(userInfo);
            ListDataSaveUtil.setDataList(preferences, ALL_USER, allUserInfos);
        }


    }

    public List<UserInfo> getAllAccountInfo() {
        return ListDataSaveUtil.getDataList(preferences, ALL_USER, UserInfo.class);
    }

    public UserInfo getCurrentUserInfo() {
        return (UserInfo) getBean(USER_INFO);
    }

    /**
     * @param userInfo--当前要删除的用户信息
     */
    public void removeUserInfoFromAllUserInfos(UserInfo userInfo) {
        List<UserInfo> userInfos = getAllAccountInfo();
        if (userInfos != null) {
            if (userInfos.contains(userInfo)) {
                if (userInfos.remove(userInfo)) {
                    //删除成功，
                    ListDataSaveUtil.setDataList(preferences, ALL_USER, userInfos);
                } else {
                    LogUtil.e("用户删除失败");
                }

            }
        }

    }

    /**
     * 是否需要进行身份验证
     *
     * @param context
     * @return
     */
    public boolean isNeedVerified(Context context) {
        String hasVerified = SharedPreferenceUtil.getInstance(context.getApplicationContext()).getString(SharedPreferenceUtil.HAS_VERIFIED, "-1");
        int hasVerifiedInt = -1;
        if (!TextUtils.isEmpty(hasVerified)) {
            hasVerifiedInt = Integer.parseInt(hasVerified);
        }
        if (hasVerifiedInt == 1) {
            //已经进行过了身份验证
            return false;
        } else {
            return true;
        }
    }
}
