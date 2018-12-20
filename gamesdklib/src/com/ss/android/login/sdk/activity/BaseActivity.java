package com.ss.android.login.sdk.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;

import com.ss.android.game.sdk.SsGameApi;
import com.ss.android.gamecommon.applog.LogReport;
import com.ss.android.gamecommon.util.LogConstantUtil;
import com.ss.android.login.sdk.fragment.BaseFragment;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public abstract class BaseActivity extends FragmentActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayout());
        initView();
        initData();
    }

    protected abstract void initData();

    public abstract void initView();

    //获取布局文件的id
    public abstract int getLayout();


    //添加fragment
    public void addFragment(BaseFragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(getFragmentContentId(), fragment, fragment.getClass().getSimpleName())
//                    .addToBackStack(fragment.getClass().getSimpleName())
                    .setTransition(FragmentTransaction.TRANSIT_NONE)
                    .commitAllowingStateLoss();

        }
    }

    //移除单个fragment
    public void removeFragment() {
        if (getSupportFragmentManager().getBackStackEntryCount() >= 1) {
            getSupportFragmentManager().popBackStack();
        } else {
            finish();
        }
    }

    //移除所有fragment
    public void removeAllFragment() {
        while (getSupportFragmentManager().getBackStackEntryCount() >= 1) {
            getSupportFragmentManager().popBackStack();
        }
    }


    //布局中Fragment的ID
    protected abstract int getFragmentContentId();

    //返回键返回事件
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (KeyEvent.KEYCODE_BACK == keyCode) {
            if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
                finish();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    public <T> T findViewId(int resID) {
        return (T) findViewById(resID);
    }

    @Override
    protected void onResume() {
        super.onResume();
        SsGameApi.onResume(this);
        LogReport logReport = new LogReport("SDK_GAME");
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("session_id", LogReport.sSessionId);
            jsonObject.put("datetime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        logReport.onEvent(getApplicationContext(), LogConstantUtil.EVENT_WINDOW_SHOW, LogConstantUtil.EVENT_NAME_INIT_WINDOW, jsonObject);

    }
}
