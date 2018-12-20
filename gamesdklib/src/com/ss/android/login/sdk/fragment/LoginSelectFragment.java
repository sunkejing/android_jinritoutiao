package com.ss.android.login.sdk.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.ss.android.game.sdk.info.UserInfo;
import com.ss.android.gamecommon.applog.LogReport;
import com.ss.android.gamecommon.model.GameLogin;
import com.ss.android.gamecommon.model.VisitorLogin;
import com.ss.android.gamecommon.util.ConstantUtil;
import com.ss.android.gamecommon.util.LogConstantUtil;
import com.ss.android.gamecommon.util.RUtil;
import com.ss.android.gamecommon.util.SharedPreferenceUtil;
import com.ss.android.gamecommon.util.ToastUtil;
import com.ss.android.login.sdk.preference.LoginSelectPreference;
import com.ss.android.login.sdk.preference.LoginSelectPreferenceImpl;
import com.ss.android.login.sdk.view.LoginSelectView;

import static com.ss.android.login.sdk.activity.MobileActivity.sUserInfo;

public class LoginSelectFragment extends BaseFragment implements View.OnClickListener, LoginSelectView {
    private TextView visitorLogin, mobileLogin;
    private int visitorLoginId, mobileLoginId;
    private LogReport logReport;

    private Dialog alertDialog;

    private LoginSelectPreference loginSelectPreference;


    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        visitorLoginId = RUtil.getId(mActivity, "tv_visitor_login");
        visitorLogin = findViewId(view, visitorLoginId);
        visitorLogin.setOnClickListener(this);
        mobileLoginId = RUtil.getId(mActivity, "tv_mobile_login");
        mobileLogin = findViewId(view, mobileLoginId);
        mobileLogin.setOnClickListener(this);
        logReport = new LogReport("SDK_GAME");

        loginSelectPreference = new LoginSelectPreferenceImpl(this);
    }

    @Override
    public int getLayoutId() {
        return RUtil.getLayout(this.getActivity(), "fragment_login_select");
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == mobileLoginId) {
            //手机号登录
            if (sUserInfo == null) {
                sUserInfo = new UserInfo();
            }
            sUserInfo.setUserType(12);
            addFragment(MobilePhoneLoginFragment.newInstance());
            //上报手机号登录的日志
            LogReport.getInstance().sendLog(getActivity(), LogConstantUtil.EVENT_TYPE_NAME_TEL_LOGIN_BTN_CLICK, LogConstantUtil.EVENT_NAME_INIT_WINDOW);

        } else if (v.getId() == visitorLoginId) {
            if (sUserInfo == null) {
                sUserInfo = new UserInfo();
            }
            sUserInfo.setUserType(14);
            //游客登录
            //展示一个登陆中的dialog
            alertDialog = showLoadingDialog();
            loginSelectPreference.hasBound(getActivity().getApplicationContext());
            //上报游客登录的日志
            LogReport.getInstance().sendLog(getActivity(), LogConstantUtil.EVENT_TYPE_NAME_GUEST_LOGIN_BTN_CLICK, LogConstantUtil.EVENT_NAME_INIT_WINDOW);

        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (alertDialog != null) {
            alertDialog.dismiss();
        }
    }

    public static LoginSelectFragment newInstance() {
        return new LoginSelectFragment();
    }

    /**
     * true代表已经绑定
     * false代表没有绑定
     *
     * @param isBound
     */
    @Override
    public void isBound(boolean isBound) {
        if (!isBound) {
            //没有绑定过,执行游客登录流程
            loginSelectPreference.visitorLogin(getActivity().getApplicationContext());

        } else {
            //已经绑定过
            if (alertDialog != null) {
                if (alertDialog.isShowing()) {
                    alertDialog.dismiss();
                }
            }
            ToastUtil.showToast(getActivity(), "已经绑定过");

        }
    }

    @Override
    public void onBoundFailture(String errorCode, String message) {
        if (alertDialog != null) {
            if (alertDialog.isShowing()) {
                alertDialog.dismiss();
            }
        }
        ToastUtil.showToast(getActivity(), message);

    }

    @Override
    public void onVisitorLoginSuccess(VisitorLogin visitorLogin) {
        //
        loginSelectPreference.gameLogin(getActivity().getApplicationContext(), visitorLogin);

    }

    @Override
    public void onVisitorLoginFailture(String errorCode, String message) {
        if (alertDialog != null) {
            if (alertDialog.isShowing()) {
                alertDialog.dismiss();
            }
        }
        if (!TextUtils.isEmpty(message)) {
            ToastUtil.showToast(getActivity(), message);
        }
        //跳转到手机号登录界面
        addFragment(MobilePhoneLoginFragment.newInstance());
        SharedPreferenceUtil sharedPreferenceUtil = SharedPreferenceUtil.getInstance(getActivity());
        sharedPreferenceUtil.setHasLogin(false);
        getActivity().finish();
    }

    @Override
    public void onGameLoginSuccess(GameLogin gameLogin) {
        //登录成功
        if (alertDialog != null) {
            if (alertDialog.isShowing()) {
                alertDialog.dismiss();
            }
        }
        sUserInfo.setAccessToken("");
        sUserInfo.setUserType(ConstantUtil.TOURIST);
        sUserInfo.setMobile("游客账号");
        sUserInfo.setOpenId(gameLogin.getOpen_id());
        sUserInfo.setUserId(gameLogin.getUser_id());
        sUserInfo.setLoginId(gameLogin.getLogin_id());
        sUserInfo.setHasPlayed(gameLogin.getHas_played());
        SharedPreferenceUtil sharedPreferenceUtil = SharedPreferenceUtil.getInstance(getActivity());
        sharedPreferenceUtil.setHasLogin(true);
        sharedPreferenceUtil.setAccount("游客账号");
        sharedPreferenceUtil.saveAllAccountInfo(sUserInfo);
        addFragment(VisiterbindFragment.newInstance());

    }

    @Override
    public void onGameLoginFailture(String errorCode, String message) {
        if (alertDialog != null) {
            if (alertDialog.isShowing()) {
                alertDialog.dismiss();
            }
        }
        //跳转到手机号登录界面
        addFragment(MobilePhoneLoginFragment.newInstance());
        SharedPreferenceUtil sharedPreferenceUtil = SharedPreferenceUtil.getInstance(getActivity());
        sharedPreferenceUtil.setHasLogin(false);
        sharedPreferenceUtil.setAccount("");
    }


}
