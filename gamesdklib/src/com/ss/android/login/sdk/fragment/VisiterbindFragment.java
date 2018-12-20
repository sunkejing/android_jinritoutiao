package com.ss.android.login.sdk.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.ss.android.game.sdk.info.UserInfo;
import com.ss.android.gamecommon.model.Verified;
import com.ss.android.gamecommon.thread.RetrofitManager;
import com.ss.android.gamecommon.util.RUtil;
import com.ss.android.gamecommon.util.SharedPreferenceUtil;
import com.ss.android.gamecommon.util.UrlUtil;
import com.ss.android.login.sdk.activity.MobileActivity;
import com.ss.android.login.sdk.model.RequestCallback;

import java.util.HashMap;

import static com.ss.android.login.sdk.activity.MobileActivity.sUserInfo;

public class VisiterbindFragment extends BaseFragment implements View.OnClickListener {
    private Button visitorBind;//游客绑定
    private Button visitorUnBind;//暂不绑定
    private int bindId;
    private int unBindId;

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        unBindId = RUtil.getId(getActivity(), "btn_unbind");
        visitorUnBind = (Button) view.findViewById(unBindId);
        visitorUnBind.setOnClickListener(this);
        bindId = RUtil.getId(getActivity(), "btn_bind");
        visitorBind = (Button) view.findViewById(bindId);
        visitorBind.setOnClickListener(this);

    }

    @Override
    public int getLayoutId() {
        return RUtil.getLayout(getActivity(), "fragment_visitor_bind");
    }


    public static VisiterbindFragment newInstance() {
        return new VisiterbindFragment();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == bindId) {
            //绑定
            addFragment(BindPhoneNumberFragment.newInstance());
            saveUserInfo();
            final SharedPreferenceUtil sharedPreferenceUtil = SharedPreferenceUtil.getInstance(getActivity());
            onHasVerified(getActivity(), MobileActivity.sUserInfo.getOpenId(), MobileActivity.sUserInfo.getUserType(), new RequestCallback<Verified>() {
                @Override
                public void onRequestSuccess(Verified verified) {
                    sharedPreferenceUtil.putString("has_verified", verified.getHas_verified() + "");
                    sharedPreferenceUtil.putString("need_verify", verified.getNeed_verify() + "");
                }

                @Override
                public void onRequestFailture(String errorCode, String message) {
                    sharedPreferenceUtil.putString("has_verified", "");
                    sharedPreferenceUtil.putString("need_verify", "");
                }
            });
        } else if (v.getId() == unBindId) {
            //不绑定
            final SharedPreferenceUtil sharedPreferenceUtil = SharedPreferenceUtil.getInstance(getActivity());
            saveUserInfo();
            MobileActivity.loginCallback.onLoginSuccess(MobileActivity.sUserInfo.getAccessToken(),
                    Long.valueOf(MobileActivity.sUserInfo.getUserId()),
                    MobileActivity.sUserInfo.getOpenId(),
                    MobileActivity.sUserInfo.getUserType());
            UserInfo saveUser = new UserInfo(sUserInfo.getAccessToken(), sUserInfo.getOpenId(), sUserInfo.getUserId(), sUserInfo.getUserType(), "游客登录", sUserInfo.getLoginId());
            sharedPreferenceUtil.saveAllAccountInfo(saveUser);
            onHasVerified(getActivity(), MobileActivity.sUserInfo.getOpenId(), MobileActivity.sUserInfo.getUserType(), new RequestCallback<Verified>() {
                @Override
                public void onRequestSuccess(Verified verified) {
                    sharedPreferenceUtil.putString("has_verified", verified.getHas_verified() + "");
                    sharedPreferenceUtil.putString("need_verify", verified.getNeed_verify() + "");
                }

                @Override
                public void onRequestFailture(String errorCode, String message) {
                    sharedPreferenceUtil.putString("has_verified", "");
                    sharedPreferenceUtil.putString("need_verify", "");
                }
            });
            getActivity().finish();
        }


    }

    public boolean onBackPressed() {
        MobileActivity.loginCallback.onLoginSuccess(MobileActivity.sUserInfo.getAccessToken(),
                Long.valueOf(MobileActivity.sUserInfo.getUserId()),
                MobileActivity.sUserInfo.getOpenId(),
                MobileActivity.sUserInfo.getUserType());
        final SharedPreferenceUtil sharedPreferenceUtil = SharedPreferenceUtil.getInstance(getActivity());
        UserInfo saveUser = new UserInfo(sUserInfo.getAccessToken(), sUserInfo.getOpenId(), sUserInfo.getUserId(), sUserInfo.getUserType(), "游客登录", sUserInfo.getLoginId());
        sharedPreferenceUtil.saveAllAccountInfo(saveUser);

        onHasVerified(getActivity(), MobileActivity.sUserInfo.getOpenId(), MobileActivity.sUserInfo.getUserType(), new RequestCallback<Verified>() {
            @Override
            public void onRequestSuccess(Verified verified) {
                sharedPreferenceUtil.putString("has_verified", verified.getHas_verified() + "");
                sharedPreferenceUtil.putString("need_verify", verified.getNeed_verify() + "");
            }

            @Override
            public void onRequestFailture(String errorCode, String message) {
                sharedPreferenceUtil.putString("has_verified", "");
                sharedPreferenceUtil.putString("need_verify", "");
            }
        });
        saveUserInfo();
        getActivity().finish();
        return true;
    }

    public void saveUserInfo() {
        SharedPreferenceUtil sharedPreferenceUtil = SharedPreferenceUtil.getInstance(getActivity());
        sharedPreferenceUtil.putBean(SharedPreferenceUtil.USER_INFO, sUserInfo);
    }

    public void onHasVerified(Context context, String open_id, int accountNumber, final RequestCallback<Verified> requestCallback) {
        HashMap<String, Object> mHashMap = new HashMap<String, Object>();
        mHashMap.put("open_id", open_id);
        mHashMap.put("user_type", accountNumber);
        HashMap<String, Object> mDataMap = new HashMap<String, Object>();
        mDataMap.putAll(mHashMap);
        mDataMap.putAll(UrlUtil.extMap(getActivity(), false));
        RetrofitManager.getInstance().onHasVerified(mDataMap, new RequestCallback<Verified>() {

            @Override
            public void onRequestSuccess(Verified verified) {
                requestCallback.onRequestSuccess(verified);

            }

            @Override
            public void onRequestFailture(String errorCode, String message) {
                requestCallback.onRequestFailture(errorCode, message);
            }
        });

    }
}
