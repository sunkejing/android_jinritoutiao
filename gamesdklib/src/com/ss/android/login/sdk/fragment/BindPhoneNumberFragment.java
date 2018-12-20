package com.ss.android.login.sdk.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.ss.android.game.sdk.info.UserInfo;
import com.ss.android.gamecommon.model.Authorize;
import com.ss.android.gamecommon.model.GameLogin;
import com.ss.android.gamecommon.model.QuickLogin;
import com.ss.android.gamecommon.model.SendCode;
import com.ss.android.gamecommon.model.TokenModel;
import com.ss.android.gamecommon.util.RUtil;
import com.ss.android.gamecommon.util.SharedPreferenceUtil;
import com.ss.android.gamecommon.util.ToastUtil;
import com.ss.android.login.sdk.activity.MobileActivity;
import com.ss.android.login.sdk.activity.UserProtocolActivity;
import com.ss.android.login.sdk.preference.BindPhoneNumberPreference;
import com.ss.android.login.sdk.preference.BindPhoneNumberPreferenceImpl;
import com.ss.android.login.sdk.view.BindPhoneNumberView;

import java.text.MessageFormat;

import static com.ss.android.gamecommon.util.ConstantUtil.ACCOUNT_NUMBER;
import static com.ss.android.login.sdk.activity.MobileActivity.loginCallback;
import static com.ss.android.login.sdk.activity.MobileActivity.sUserInfo;

public class BindPhoneNumberFragment extends BaseFragment implements View.OnClickListener, BindPhoneNumberView {
    private EditText etAccount;//绑定账号
    private EditText etCode;//验证码
    private ImageView imgClearAccount;//清空账号
    private ImageView imgClearCode;//清空验证码
    private TextView tvSendCode;//验证码倒计时
    private TextView tvUserProtocol;//用户协议
    private TextView tvBindImmediate;//立即绑定
    private CheckBox cbUserProtocol;
    private TextView tvLoginOtherAccount;//登录其他账号


    private int clearAccountId;
    private int clearCodeId;
    private int sendCodeId;
    private int userProtocolId;
    private int bindImmediateId;
    private int loginOtherAccountId;


    private boolean running;
    private CountDownTimer downTimer;

    private BindPhoneNumberPreference bindPhoneNumberPreference;
    private String token;

    private Dialog alertDialog;

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        etAccount = (EditText) view.findViewById(RUtil.getId(getActivity(), "btn_bind_account"));
        etCode = (EditText) view.findViewById(RUtil.getId(getActivity(), "btn_bind_code"));
        clearAccountId = RUtil.getId(getActivity(), "img_clear_account");
        imgClearAccount = (ImageView) view.findViewById(clearAccountId);
        imgClearAccount.setVisibility(View.INVISIBLE);
        imgClearAccount.setOnClickListener(this);
        clearCodeId = RUtil.getId(getActivity(), "img_clear_code");
        imgClearCode = (ImageView) view.findViewById(clearCodeId);
        imgClearCode.setVisibility(View.INVISIBLE);
        imgClearCode.setOnClickListener(this);
        sendCodeId = RUtil.getId(getActivity(), "tv_send_code");
        tvSendCode = (TextView) view.findViewById(sendCodeId);
        tvSendCode.setOnClickListener(this);
        userProtocolId = RUtil.getId(getActivity(), "tv_user_protocol");
        tvUserProtocol = (TextView) view.findViewById(userProtocolId);
        tvUserProtocol.setOnClickListener(this);
        initTextWatcher();
        bindImmediateId = RUtil.getId(getActivity(), "tv_bind_immediate");
        tvBindImmediate = (TextView) view.findViewById(bindImmediateId);
        tvBindImmediate.setOnClickListener(this);
        cbUserProtocol = findViewId(view, RUtil.getId(getActivity(), "cb_user_protocol"));
        loginOtherAccountId = RUtil.getId(getActivity(), "tv_login_other_account");
        tvLoginOtherAccount = findViewId(view, loginOtherAccountId);
        tvLoginOtherAccount.setOnClickListener(this);


        bindPhoneNumberPreference = new BindPhoneNumberPreferenceImpl(this);

    }

    private void initTextWatcher() {
        etAccount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String content = etAccount.getText().toString().trim();
                if (!TextUtils.isEmpty(content)) {
                    imgClearAccount.setVisibility(View.VISIBLE);
                } else {
                    imgClearAccount.setVisibility(View.INVISIBLE);
                }

            }
        });

        etCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String content = etCode.getText().toString().trim();
                if (!TextUtils.isEmpty(content)) {
                    imgClearCode.setVisibility(View.VISIBLE);

                } else {
                    imgClearCode.setVisibility(View.INVISIBLE);
                }
            }
        });

    }

    @Override
    public int getLayoutId() {
        return RUtil.getLayout(getActivity(), "fragment_bind_phonenumber");
    }

    public static BindPhoneNumberFragment newInstance() {
        return new BindPhoneNumberFragment();
    }


    @Override
    public void onClick(View v) {
        if (clearAccountId == v.getId()) {
            etAccount.setText("");
            imgClearAccount.setVisibility(View.INVISIBLE);
        } else if (clearCodeId == v.getId()) {
            etCode.setText("");
            imgClearCode.setVisibility(View.INVISIBLE);
        } else if (sendCodeId == v.getId()) {
            //判断手机号是否输入，是否合法
            String phoneNum = etAccount.getText().toString().trim();
            if (!TextUtils.isEmpty(phoneNum)) {
                //手机号不是空，获取验证码
                bindPhoneNumberPreference.sendCode(getActivity(), phoneNum);

            } else {
                ToastUtil.showToast(getActivity(), "手机号不能为空");
                return;
            }

        } else if (userProtocolId == v.getId()) {
            Intent intent = new Intent();
            intent.setClass(getActivity(), UserProtocolActivity.class);
            getActivity().startActivity(intent);


        } else if (bindImmediateId == v.getId()) {
            String phoneNum = etAccount.getText().toString().trim();
            String code = etCode.getText().toString().trim();
            if (TextUtils.isEmpty(phoneNum)) {
                ToastUtil.showToast(getActivity(), "手机号不能为空");
                return;
            }
            if (TextUtils.isEmpty(code)) {
                ToastUtil.showToast(getActivity(), "验证码不能为空");
                return;
            }
            if (!cbUserProtocol.isChecked()) {
                ToastUtil.showToast(getActivity(), "请勾选用户协议");
                return;
            }
            alertDialog = showLoadingDialog();
            bindPhoneNumberPreference.quickLogin(getActivity(), phoneNum, code);
        } else if (loginOtherAccountId == v.getId()) {
            //登录其他账号
            addFragment(MobilePhoneLoginFragment.newInstance());
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (downTimer != null) {
            if (running) {
                downTimer.cancel();
            }
        }
        if (alertDialog != null) {
            if (alertDialog.isShowing()) {
                alertDialog.dismiss();
            }
        }
    }

    //获取短信成功
    @Override
    public void onGetCodeSuccess(SendCode sendCode) {
        int countDownTime = sendCode.getRetry_time();
        String mobile = sendCode.getMobile();
        if (running) {
        } else {
            createCountDown(countDownTime);
            downTimer.start();

        }

    }

    //获取验证码时报
    @Override
    public void onGetCodeFailture(String errorCode, String errorMsg) {
        if (alertDialog != null) {
            if (alertDialog.isShowing()) {
                alertDialog.dismiss();
            }
        }
        ToastUtil.showToast(getActivity(), errorMsg);

    }

    //验证码校验成功
    @Override
    public void onQuickLoginSuccess(QuickLogin quickLogin) {
        //验证设备是否已经绑定过账号
        bindPhoneNumberPreference.queryOAuthCode(getActivity());
    }

    @Override
    public void onQuickLoginFailture(String errorCode, String message) {
        if (alertDialog != null) {
            if (alertDialog.isShowing()) {
                alertDialog.dismiss();
            }
        }
        ToastUtil.showToast(getActivity(), message);
    }

    //请求code成功
    @Override
    public void onQueryOAuthCodeSuccess(Authorize authorize) {
        //请求token
        String code = authorize.getCode();
        bindPhoneNumberPreference.queryToken(getActivity(), code);

    }

    @Override
    public void onQueryOAuthCodeFailture(String errorCode, String errorMessage) {
        if (alertDialog != null) {
            if (alertDialog.isShowing()) {
                alertDialog.dismiss();
            }
        }
        ToastUtil.showToast(getActivity(), errorMessage);

    }

    //获取token成功
    @Override
    public void onQueryTokenSuccess(TokenModel tokenModel) {
        //请求gameLogin接口
        token = tokenModel.getAccess_token();
        bindPhoneNumberPreference.gameLogin(getActivity(), tokenModel);


    }

    @Override
    public void onQueryTokenFailture(String errorCode, String errorMessage) {
        if (alertDialog != null) {
            if (alertDialog.isShowing()) {
                alertDialog.dismiss();
            }
        }
        ToastUtil.showToast(getActivity(), "errorCode:" + errorCode + ",errorMessage:" + errorMessage);
    }

    //绑定成功，登录成功
    @Override
    public void onGameLoginSuccess(GameLogin gameLogin) {
        if (alertDialog != null) {
            if (alertDialog.isShowing()) {
                alertDialog.dismiss();
            }
        }
        //登录成功,给出回调
        sUserInfo.setAccessToken(token);
        sUserInfo.setHasPlayed(gameLogin.getHas_played());
        sUserInfo.setUserType(ACCOUNT_NUMBER);
        sUserInfo.setLoginId(gameLogin.getLogin_id());
        sUserInfo.setOpenId(gameLogin.getOpen_id());
        sUserInfo.setUserId(gameLogin.getUser_id());
        SharedPreferenceUtil sharedPreferenceUtil = SharedPreferenceUtil.getInstance(getActivity());
        UserInfo saveUser = new UserInfo(sUserInfo.getAccessToken(), sUserInfo.getOpenId(), sUserInfo.getUserId(), sUserInfo.getUserType(), "游客登录", gameLogin.getLogin_id());
        sharedPreferenceUtil.saveAllAccountInfo(saveUser);
        loginCallback.onLoginSuccess(token, gameLogin.getUser_id(), gameLogin.getOpen_id(), ACCOUNT_NUMBER);

    }

    @Override
    public void onGameLoginFailture(String errorCode, String errorMessage) {
        if (alertDialog != null) {
            if (alertDialog.isShowing()) {
                alertDialog.dismiss();
            }
        }
        if ("7".equals(errorCode)) {
            errorMessage = "您所绑定的手机号已注册，请绑定其他手机号或直接登录";
        }
        ToastUtil.showToast(getActivity(), MessageFormat.format("{0}", errorMessage));
    }

    public void createCountDown(int countDownTime) {
        downTimer = new CountDownTimer(countDownTime * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                running = true;
                tvSendCode.setText("重新发送 " + Math.round((double) millisUntilFinished / 1000));
                tvSendCode.setTextColor(getResources().getColor(RUtil.getColor(getActivity(), "biaozhunhong3_disable")));
            }

            @Override
            public void onFinish() {
                running = false;
                tvSendCode.setText("重新发送");
                tvSendCode.setTextColor(getResources().getColor(RUtil.getColor(getActivity(), "biaozhunhong3")));
            }
        };
    }

    public boolean onBackPressed() {
        if (alertDialog != null) {
            if (alertDialog.isShowing()) {
                alertDialog.dismiss();
            }
        }

        loginCallback.onLoginSuccess(MobileActivity.sUserInfo.getAccessToken(),
                Long.valueOf(MobileActivity.sUserInfo.getUserId()),
                MobileActivity.sUserInfo.getOpenId(),
                MobileActivity.sUserInfo.getUserType());
        SharedPreferenceUtil sharedPreferenceUtil = SharedPreferenceUtil.getInstance(getActivity());
        UserInfo saveUser = new UserInfo(sUserInfo.getAccessToken(), sUserInfo.getOpenId(), sUserInfo.getUserId(), sUserInfo.getUserType(), "游客登录", sUserInfo.getLoginId());
        sharedPreferenceUtil.saveAllAccountInfo(saveUser);
        getActivity().finish();
        return true;
    }
}
