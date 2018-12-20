package com.ss.android.pay.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import com.ss.android.game.sdk.GameSdk;
import com.ss.android.gamecommon.model.CreateOrder;
import com.ss.android.gamecommon.util.ConstantUtil;
import com.ss.android.gamecommon.util.IntentUtil;
import com.ss.android.gamecommon.util.RUtil;
import com.ss.android.gamecommon.util.ToastUtil;
import com.ss.android.login.sdk.LoginCallback;
import com.ss.android.login.sdk.activity.MobileActivity;
import com.ss.android.login.sdk.activity.SDKActivity;
import com.ss.android.login.sdk.fragment.BaseFragment;
import com.ss.android.pay.PayCallback;
import com.ss.android.pay.SSPayManager;
import com.ss.android.pay.fragment.IdentityVerificationFragment;
import com.ss.android.pay.tools.BackHandlerHelper;

import java.lang.ref.WeakReference;

import static com.ss.android.gamecommon.util.ConstantUtil.IdentityVerificationFragment;
import static com.ss.android.gamecommon.util.ConstantUtil.LoginSelectFragment;
import static com.ss.android.gamecommon.util.ConstantUtil.PAY_EXT_PARMAS;
import static com.ss.android.gamecommon.util.ConstantUtil.PAY_FAIL_NOT_LOGIN;
import static com.ss.android.gamecommon.util.ConstantUtil.PAY_INFO;
import static com.ss.android.gamecommon.util.ConstantUtil.PaymentSelectFragment;
import static com.ss.android.gamecommon.util.ConstantUtil.TARGET_FRAGMENT;

public class PayActivity extends SDKActivity {
    //fragment管理器
    private FragmentManager fragmentManager;
    private static WeakReference<Activity> gameActivity;
    private Intent intent;
    private static int targetFragment = -1;
    private String extParams;
    private CreateOrder mCreateOrder;


    @Override
    protected void initData() {
        //获取fragment的管理器
        fragmentManager = getSupportFragmentManager();
        intent = getIntent();
        if (intent != null) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                targetFragment = bundle.getInt(TARGET_FRAGMENT);
                if (targetFragment == PaymentSelectFragment) {
                    mCreateOrder = (CreateOrder) bundle.getSerializable(PAY_INFO);
                    extParams = bundle.getString(PAY_EXT_PARMAS);
                }
            } else {
                targetFragment = LoginSelectFragment;
            }
        }

    }

    @Override
    public void initView() {

    }

    @Override
    public int getLayout() {
        return RUtil.getLayout(this, "activity_pay");
    }

    @Override
    protected int getFragmentContentId() {
        return RUtil.getId(this, "fragments");
    }


    @Override
    protected BaseFragment getFristFragment() {
        switch (targetFragment) {
            case ConstantUtil.IdentityVerificationFragment:
                return com.ss.android.pay.fragment.IdentityVerificationFragment.newInstance();
            case ConstantUtil.PaymentSelectFragment:
                return com.ss.android.pay.fragment.PaymentSelectFragment.newInstance(mCreateOrder, extParams);
        }
        return null;
    }

    //身份验证fragment
    public static void showIdentityVerification(Activity activity, PayCallback payCallback) {
        //跳转到身份验证界面
        gameActivity = new WeakReference<Activity>(activity);
        Bundle bundle = new Bundle();
        bundle.putInt(TARGET_FRAGMENT, IdentityVerificationFragment);
        IntentUtil.getInstance().goActivity(gameActivity.get(), PayActivity.class, bundle);
    }

    /**
     * 身份验证成功
     */
    public void onVerifyIdentitySuccess() {
        ToastUtil.showToast(gameActivity.get(), "认证成功");
        //开始处理支付
        if (MobileActivity.sUserInfo != null) {

        }

    }

    /**
     * 身份验证取消
     */
    public void onVerifyIdentityCancel() {
        ToastUtil.showToast(gameActivity.get(), "认证取消");
        //开始处理支付
        if (MobileActivity.sUserInfo != null) {
            //开始支付逻辑
            SSPayManager.getInstance(PayActivity.this).createOrder(this, SSPayManager.sPayRequestData);

        } else {
            GameSdk.inst().login(this, new LoginCallback() {
                @Override
                public void onLoginSuccess(String accessToken, long uid, String openId, int userType) {
                    //开始支付逻辑
                    SSPayManager.getInstance(PayActivity.this).createOrder(PayActivity.this, SSPayManager.sPayRequestData);
                    finish();
                }

                @Override
                public void onLoginFail(int errorCode, String errorMsg) {
                    ToastUtil.showToast(gameActivity.get(), "用户登录失败，请重试");
                    if (SSPayManager.getInstance(PayActivity.this).sPayCallback != null) {
                        SSPayManager.getInstance(PayActivity.this).sPayCallback.onPayFail(PAY_FAIL_NOT_LOGIN, "用户登录失败，请重试");
                    }
                    finish();
                }
            });

        }

        finish();
    }

    public CreateOrder getCreateOrder() {
        return mCreateOrder;
    }

    @Override
    public void onBackPressed() {
        if (!BackHandlerHelper.handleBackPress(this)) {
            super.onBackPressed();
        }
    }
}
