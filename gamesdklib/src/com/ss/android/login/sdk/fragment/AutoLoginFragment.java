package com.ss.android.login.sdk.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.ss.android.game.sdk.info.UserInfo;
import com.ss.android.gamecommon.model.AutoLogin;
import com.ss.android.gamecommon.util.ConstantUtil;
import com.ss.android.gamecommon.util.NumberEncryption;
import com.ss.android.gamecommon.util.RUtil;
import com.ss.android.gamecommon.util.SharedPreferenceUtil;
import com.ss.android.gamecommon.util.ToastUtil;
import com.ss.android.login.sdk.activity.MobileActivity;
import com.ss.android.login.sdk.preference.AutoLoginPreference;
import com.ss.android.login.sdk.preference.AutoLoginPreferenceImpl;
import com.ss.android.login.sdk.view.AutoLoginView;

import java.text.MessageFormat;
import java.util.Timer;
import java.util.TimerTask;

import static com.ss.android.gamecommon.util.ConstantUtil.TOURIST;
import static com.ss.android.login.sdk.activity.MobileActivity.loginCallback;
import static com.ss.android.login.sdk.activity.MobileActivity.sUserInfo;

/**
 * 自动登录Fragment
 */
public class AutoLoginFragment extends BaseFragment implements View.OnClickListener, AutoLoginView {
    private TextView switchAccount;//切换账号or绑定账号
    private TextView autoBindTips;//绑定账号提醒
    private TextView userName;


    private int switchAccountId;
    private int autoBindTipsId;
    private SharedPreferenceUtil sharedPreferenceUtil;
    private String account;
    private String openId;
    private int userType;
    private UserInfo userInfo;

    private AutoLoginPreference autoLoginPreference;
    private int delay = 1500;


    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        switchAccountId = RUtil.getId(getActivity(), "tv_switch_account");
        switchAccount = findViewId(view, switchAccountId);
        switchAccount.setOnClickListener(this);
        autoBindTipsId = RUtil.getId(getActivity(), "auto_bind_tips_tv");
        autoBindTips = findViewId(view, autoBindTipsId);
        userName = findViewId(view, RUtil.getId(getActivity(), "auto_user_tv"));
        autoLoginPreference = new AutoLoginPreferenceImpl(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        initData();
        return view;

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


    }

    @Override
    public void onResume() {
        super.onResume();
        Timer timer = new Timer();
        timer.schedule(task, delay);

    }

    TimerTask task = new TimerTask() {
        public void run() {
            autoLoginPreference.autoLogin(getActivity(), userInfo.getLoginId());
        }
    };

    private void initData() {
        sharedPreferenceUtil = SharedPreferenceUtil.getInstance(getActivity());
        account = sharedPreferenceUtil.getAccount();
        account = NumberEncryption.phoneReplaceMiddle(account);
        userName.setText(MessageFormat.format("{0} 正在登录中...", account));

        userInfo = (UserInfo) sharedPreferenceUtil.getBean(SharedPreferenceUtil.USER_INFO);
        sUserInfo = userInfo;
        if (userInfo != null) {
            openId = userInfo.getOpenId();
            userType = userInfo.getUserType();
            if (userType == ConstantUtil.ACCOUNT_NUMBER) {
                //账号自动登录
                autoBindTips.setVisibility(View.GONE);
                switchAccount.setText(getString(RUtil.getString(getActivity(), "ss_switch_account")));


            } else if (userType == TOURIST) {
                autoBindTips.setVisibility(View.VISIBLE);
                switchAccount.setText(getString(RUtil.getString(getActivity(), "ss_bind_account_tip")));
            }
        } else {
            addFragment(LoginSelectFragment.newInstance());
        }


    }

    @Override
    public int getLayoutId() {
        return RUtil.getLayout(getActivity(), "fragment_auto_login");
    }

    public static AutoLoginFragment newInstance() {
        return new AutoLoginFragment();
    }


    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        if (viewId == switchAccountId) {
            if (userType == ConstantUtil.ACCOUNT_NUMBER) {
                //头条账号
                task.cancel();
                addFragment(SwitchAccountFragment.newInstance());

            } else if (userType == ConstantUtil.TOURIST) {
                //游客账号
                task.cancel();
                addFragment(BindPhoneNumberFragment.newInstance());

            }
        }

    }

    @Override
    public void onAutoLoginSuccess(AutoLogin autoLogin) {
        MobileActivity.sUserInfo.setUserId(autoLogin.getUser_id());
        MobileActivity.sUserInfo.setOpenId(autoLogin.getOpen_id());
        MobileActivity.sUserInfo.setUserType(userInfo.getUserType());
        MobileActivity.sUserInfo.setLoginId(userInfo.getLoginId());
        MobileActivity.sUserInfo.setHasPlayed(userInfo.getHasPlayed());
        MobileActivity.sUserInfo.setAccessToken(userInfo.getAccessToken());
        saveUserInfo();
        loginCallback.onLoginSuccess(sUserInfo.getAccessToken(), sUserInfo.getUserId(), sUserInfo.getOpenId(), sUserInfo.getUserType());
        UserInfo saveUser = new UserInfo(sUserInfo.getAccessToken(), sUserInfo.getOpenId(), sUserInfo.getUserId(), sUserInfo.getUserType(), account, userInfo.getLoginId());
        sharedPreferenceUtil.saveAllAccountInfo(saveUser);
        if (getActivity() != null) {
            if (!getActivity().isFinishing()) {
                getActivity().finish();
            }
        }
    }

    @Override
    public void onAutoLoginFailture(String errorCode, String errorMessage) {
        ToastUtil.showToast(getActivity(), "自动登录失败" + errorMessage);
        addFragment(LoginSelectFragment.newInstance());
    }

    public void saveUserInfo() {
        SharedPreferenceUtil sharedPreferenceUtil = SharedPreferenceUtil.getInstance(getActivity());
        sharedPreferenceUtil.putBean(SharedPreferenceUtil.USER_INFO, sUserInfo);
    }
}
