package com.ss.android.gamecommon.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;

/**
 * Intent跳转工具类
 */
public class IntentUtil {
    private static IntentUtil sInstance;
    private static Intent intent;
    public static final String OPEN_ACTIVITY_KEY = "open_activity";//intent跳转传递传输key

    public static IntentUtil getInstance() {
        if (null == sInstance) {
            synchronized (IntentUtil.class) {
                if (null == sInstance) {
                    sInstance = new IntentUtil();
                }
            }
        }
        return sInstance;
    }

    public IntentUtil() {
        intent = new Intent();
    }

    /**
     * 启动一个activity
     *
     * @param _this
     * @param _class
     */
    public void goActivity(Context _this, Class<? extends Activity> _class) {
        intent.setClass(_this, _class);
        _this.startActivity(intent);
        _this = null;

    }

    public void goActivity(Context _this, Class<? extends Activity> _class, Bundle bundle) {
        intent.setClass(_this, _class);
        intent.putExtras(bundle);
        _this.startActivity(intent);
        _this = null;

    }

    /**
     * 启动一个activity后kill前一个
     *
     * @param _this
     * @param _class
     */
    public void goActivityKill(Context _this, Class<? extends Activity> _class) {
        intent.setClass(_this, _class);
        _this.startActivity(intent);
        ((Activity) _this).finish();
        _this = null;
    }

    /**
     * 回调跳转
     *
     * @param _this
     * @param _class
     * @param requestCode
     */
    public void goActivityResult(Context _this, Class<? extends Activity> _class, int requestCode) {
        intent.setClass(_this, _class);
        ((Activity) _this).startActivityForResult(intent, requestCode);
        _this = null;
    }

    /**
     * 回调跳转，并finish当前界面
     *
     * @param _this
     * @param _class
     * @param requestCode
     */
    public void goActivityResultKill(Context _this, Class<? extends Activity> _class, int requestCode) {
        intent.setClass(_this, _class);
        ((Activity) _this).startActivityForResult(intent, requestCode);
        ((Activity) _this).finish();
        _this = null;

    }

    /**
     * 传递一个序列化实体类
     *
     * @param _this
     * @param _class
     * @param parcelable
     */
    public void goActivity(Context _this, Class<? extends Activity> _class, Parcelable parcelable) {
        intent.setClass(_this, _class);
        putParcelable(parcelable);
        _this.startActivity(intent);
        _this = null;
    }

    public void goActivityKill(Context _this, Class<? extends Activity> _class, Parcelable parcelable) {
        intent.setClass(_this, _class);
        putParcelable(parcelable);
        _this.startActivity(intent);
        ((Activity) _this).finish();
        _this = null;
    }

    public void goActivityResult(Activity _this, Class<? extends Activity> _class, Parcelable parcelable, int requestCode) {
        intent.setClass(_this, _class);
        putParcelable(parcelable);
        _this.startActivityForResult(intent, requestCode);
        _this = null;
    }

    public void goActivityResultKill(Activity _this, Class<? extends Activity> _class, Parcelable parcelable, int requestCode) {
        intent.setClass(_this, _class);
        putParcelable(parcelable);
        _this.startActivityForResult(intent, requestCode);
        _this.finish();
        _this = null;
    }

    private void putParcelable(Parcelable parcelable) {
        if (parcelable == null) return;
        intent.putExtra(OPEN_ACTIVITY_KEY, parcelable);
    }


}
