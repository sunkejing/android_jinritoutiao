package com.ss.android.pay;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.WindowManager;

import com.ss.android.game.sdk.GameSdk;
import com.ss.android.gamecommon.model.CreateOrder;
import com.ss.android.gamecommon.util.ConstantUtil;
import com.ss.android.gamecommon.util.IntentUtil;
import com.ss.android.gamecommon.util.RUtil;
import com.ss.android.gamecommon.util.SharedPreferenceUtil;
import com.ss.android.gamecommon.util.ToastUtil;
import com.ss.android.login.sdk.LoginCallback;
import com.ss.android.login.sdk.activity.MobileActivity;
import com.ss.android.pay.activity.PayActivity;
import com.ss.android.pay.bean.PayRequestData;
import com.ss.android.pay.preference.CreateOrderPreference;
import com.ss.android.pay.preference.CreateOrderPreferenceImpl;
import com.ss.android.pay.view.CreateOrderView;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;

import static com.ss.android.gamecommon.util.ConstantUtil.OUT_TRADE_NO;
import static com.ss.android.gamecommon.util.ConstantUtil.PAY_EXT_PARMAS;
import static com.ss.android.gamecommon.util.ConstantUtil.PAY_FAIL_NOT_LOGIN;
import static com.ss.android.gamecommon.util.ConstantUtil.PAY_INFO;
import static com.ss.android.gamecommon.util.ConstantUtil.PaymentSelectFragment;
import static com.ss.android.gamecommon.util.ConstantUtil.TARGET_FRAGMENT;

/**
 * 支付管理类
 */
public class SSPayManager implements CreateOrderView {
    private static SSPayManager sInstance;
    private AlertDialog mAlertDialog;
    private WeakReference<Activity> weakReference;
    private PayCallback payCallback;
    private CreateOrderPreference createOrderPreference;

    public static PayRequestData sPayRequestData;
    public static PayCallback sPayCallback;

    private String outTradeNo;


    public static SSPayManager getInstance(Activity activity) {
        if (sInstance == null) {
            synchronized (SSPayManager.class) {
                if (sInstance == null) {
                    sInstance = new SSPayManager(activity);
                }
            }
        }
        return sInstance;

    }

    public SSPayManager(Activity activity) {
        weakReference = new WeakReference<Activity>(activity);
        createOrderPreference = new CreateOrderPreferenceImpl(this);
    }

    public void createOrder(Context context, PayRequestData payRequestData) {
        showCreateOrderDialog();
        if (payRequestData != null) {
            outTradeNo = payRequestData.getOutTradeNo();
        }
        //执行生成订单的逻辑
        createOrderPreference.onCreateOrder(context, payRequestData, MobileActivity.sUserInfo);
    }

    public void showCreateOrderDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(weakReference.get(), RUtil.getStyle(weakReference.get(), "add_dialog"));
        builder.setTitle(null);
        builder.setMessage(RUtil.getString(weakReference.get(), "ss_creating_orders"));
        mAlertDialog = builder.create();
        mAlertDialog.show();
        WindowManager.LayoutParams lp = mAlertDialog.getWindow().getAttributes();
        lp.width = (weakReference.get().getWindow().getWindowManager().getDefaultDisplay().getWidth() * 2) / 3;
        mAlertDialog.getWindow().setAttributes(lp);//设置宽度
    }

    @Override
    public void createOrderSuccess(CreateOrder createOrder) {
        if (mAlertDialog != null) {
            mAlertDialog.dismiss();
        }
        //弹出一个支付方式选择Fragment
        Bundle bundle = new Bundle();
        bundle.putSerializable(TARGET_FRAGMENT, PaymentSelectFragment);
        bundle.putSerializable(PAY_INFO, createOrder);
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(OUT_TRADE_NO, outTradeNo);
            bundle.putString(PAY_EXT_PARMAS, jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        IntentUtil.getInstance().goActivity(weakReference.get(), PayActivity.class, bundle);
    }

    @Override
    public void createOrderFailture(String errorCode, String errorMessage) {
        if (mAlertDialog != null) {
            mAlertDialog.dismiss();
        }
        //给出支付失败的回调
        if (payCallback != null) {
            payCallback.onPayFail(ConstantUtil.PAY_FAIL_BECAUSE_CREATE_ORDER_FAIL, "创建订单失败导致支付失败");
        }
    }

    public void setPayCallback(PayCallback payCallback) {
        this.payCallback = payCallback;
    }

    public void startPay(final Activity activity, final PayRequestData payRequestData, final PayCallback payCallback) {
        sPayRequestData = payRequestData;
        sPayCallback = payCallback;
        //判断是否需要进行实名认证
        boolean isNeedVerified = SharedPreferenceUtil.getInstance(activity.getApplicationContext()).isNeedVerified(activity);
        if (!isNeedVerified) {
            //需要进行身份验证
            PayActivity.showIdentityVerification(activity, payCallback);
        } else {
            //不需要进行身份验证，直接进行支付逻辑
            if (MobileActivity.sUserInfo != null) {
                //登录成功直接支付
                SSPayManager.getInstance(activity).createOrder(activity, payRequestData);
            } else {
                //先登录
                GameSdk.inst().login(activity, new LoginCallback() {
                    @Override
                    public void onLoginSuccess(String accessToken, long uid, String openId, int userType) {
                        //跳转到支付界面
                        SSPayManager.getInstance(activity).setPayCallback(payCallback);
                        SSPayManager.getInstance(activity).createOrder(activity, payRequestData);
                    }

                    @Override
                    public void onLoginFail(int errorCode, String errorMsg) {
                        ToastUtil.showToast(activity, "用户登录失败，请重试");
                        if (payCallback != null) {
                            payCallback.onPayFail(PAY_FAIL_NOT_LOGIN, "用户登录失败，请重试");
                        }
                    }
                });

            }

        }

    }
}
