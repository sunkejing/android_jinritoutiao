package com.ss.android.login.sdk.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import com.ss.android.game.sdk.info.UserInfo;
import com.ss.android.gamecommon.util.ConstantUtil;
import com.ss.android.gamecommon.util.IntentUtil;
import com.ss.android.gamecommon.util.RUtil;
import com.ss.android.login.sdk.LoginCallback;
import com.ss.android.login.sdk.fragment.BaseFragment;
import com.ss.android.login.sdk.fragment.LoginSelectFragment;
import com.ss.android.pay.tools.BackHandlerHelper;

import java.lang.ref.WeakReference;

import static com.ss.android.gamecommon.util.ConstantUtil.TARGET_FRAGMENT;

public class MobileActivity extends SDKActivity {
    public static LoginCallback loginCallback;
    //fragment管理器
    private FragmentManager fragmentManager;
    public static UserInfo sUserInfo;

    private Intent intent;
    private static int targetFragment = -1;

    private static WeakReference<Activity> gameActivity;


    @Override
    protected void initData() {
        //获取fragment的管理器
        fragmentManager = getSupportFragmentManager();
        intent = getIntent();
        if (intent != null) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                targetFragment = bundle.getInt(TARGET_FRAGMENT);
            } else {
                targetFragment = ConstantUtil.LoginSelectFragment;
            }

        }


    }

    @Override
    public void initView() {

    }

    @Override
    public int getLayout() {
        return RUtil.getLayout(this, "activity_mobile");
    }

    @Override
    protected int getFragmentContentId() {
        return RUtil.getId(this, "fragments");
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        if (!BackHandlerHelper.handleBackPress(this)) {
            super.onBackPressed();
        }
    }

    //跳转到登录选择界面
    public static void showLoginBySelectUi(Activity activity, LoginCallback loginCallback) {
        gameActivity = new WeakReference<Activity>(activity);
        MobileActivity.loginCallback = loginCallback;
        Bundle bundle = new Bundle();
        bundle.putInt(TARGET_FRAGMENT, ConstantUtil.LoginSelectFragment);
        IntentUtil.getInstance().goActivity(gameActivity.get(), MobileActivity.class, bundle);

    }

    //跳转到自动登录界面
    public static void startAutoLogin(Activity activity, LoginCallback loginCallback) {
        MobileActivity.loginCallback = loginCallback;
        gameActivity = new WeakReference<Activity>(activity);
        Bundle bundle = new Bundle();
        bundle.putInt(TARGET_FRAGMENT, ConstantUtil.AutoLoginFragment);
        IntentUtil.getInstance().goActivity(gameActivity.get(), MobileActivity.class, bundle);
    }

    @Override
    protected BaseFragment getFristFragment() {
        switch (targetFragment) {
            case ConstantUtil.LoginSelectFragment:
                return LoginSelectFragment.newInstance();
            case ConstantUtil.AutoLoginFragment:
                return com.ss.android.login.sdk.fragment.AutoLoginFragment.newInstance();

        }
        return null;

    }

}
