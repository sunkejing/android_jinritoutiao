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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ss.android.game.sdk.info.UserInfo;
import com.ss.android.gamecommon.model.Authorize;
import com.ss.android.gamecommon.model.GameLogin;
import com.ss.android.gamecommon.model.QuickLogin;
import com.ss.android.gamecommon.model.SendCode;
import com.ss.android.gamecommon.model.TokenModel;
import com.ss.android.gamecommon.model.Verified;
import com.ss.android.gamecommon.util.RUtil;
import com.ss.android.gamecommon.util.SharedPreferenceUtil;
import com.ss.android.gamecommon.util.ToastUtil;
import com.ss.android.login.sdk.activity.MobileActivity;
import com.ss.android.login.sdk.activity.UserProtocolActivity;
import com.ss.android.login.sdk.model.RequestCallback;
import com.ss.android.login.sdk.preference.MobilePhoneLoginPreference;
import com.ss.android.login.sdk.preference.MobilePhoneLoginPreferenceImpl;
import com.ss.android.login.sdk.view.MobilePhoneLoginView;

import java.text.MessageFormat;

import static com.ss.android.gamecommon.util.ConstantUtil.ACCOUNT_NUMBER;
import static com.ss.android.login.sdk.activity.MobileActivity.sUserInfo;

public class MobilePhoneLoginFragment extends BaseFragment implements View.OnClickListener, MobilePhoneLoginView {
    private EditText loginPhoneNum;//手机号
    private EditText loginCode;//验证码
    private ImageView clearAccount;//清除手机号
    private ImageView clearCode;//清除验证码
    private TextView enterGame;//进入游戏
    private TextView userProtocol;
    private TextView loginOtherAccount;//账号密码登录
    private TextView sendCode;
    private CheckBox cbUserProtocol;
    private RelativeLayout rl_back;//返回键


    private int loginPhoneNumId;
    private int loginCodeId;
    private int clearAccountId;
    private int clearCodeId;
    private int enterGameId;
    private int userProtocolId;//
    private int loginOtherAccountId;
    private int sendCodeId;
    private int rlBackId;

    private CountDownTimer downTimer;
    private boolean running;
    private MobilePhoneLoginPreference mobilePhoneLoginPreference;
    private static String token;

    private Dialog alertDialog;
    private String account;


    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        loginPhoneNumId = RUtil.getId(getActivity(), "tt_sms_login_phone_input");
        loginPhoneNum = findViewId(view, loginPhoneNumId);
        loginCodeId = RUtil.getId(getActivity(), "tt_sms_code_input");
        loginCode = findViewId(view, loginCodeId);
        initTextWatcher();
        clearAccountId = RUtil.getId(getActivity(), "img_clear_account");
        clearAccount = findViewId(view, clearAccountId);
        clearAccount.setOnClickListener(this);
        clearCodeId = RUtil.getId(getActivity(), "img_clear_code");
        clearCode = findViewId(view, clearCodeId);
        clearCode.setOnClickListener(this);
        clearAccount.setVisibility(View.INVISIBLE);
        clearCode.setVisibility(View.INVISIBLE);
        enterGameId = RUtil.getId(getActivity(), "btn_entergame");
        enterGame = findViewId(view, enterGameId);
        enterGame.setOnClickListener(this);
        userProtocolId = RUtil.getId(getActivity(), "tv_user_protocol");
        userProtocol = findViewId(view, userProtocolId);
        userProtocol.setOnClickListener(this);
        loginOtherAccountId = RUtil.getId(getActivity(), "btn_login_other_account");
        loginOtherAccount = findViewId(view, loginOtherAccountId);
        loginOtherAccount.setOnClickListener(this);
        sendCodeId = RUtil.getId(getActivity(), "btn_send_code");
        sendCode = findViewId(view, sendCodeId);
        sendCode.setOnClickListener(this);
        cbUserProtocol = findViewId(view, RUtil.getId(getActivity(), "cb_user_protocol"));
        rlBackId = RUtil.getId(getActivity(), "rl_back");
        rl_back = findViewId(view, rlBackId);
        rl_back.setOnClickListener(this);
        mobilePhoneLoginPreference = new MobilePhoneLoginPreferenceImpl(this);

    }

    private void initTextWatcher() {
        loginPhoneNum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String content = loginPhoneNum.getText().toString().trim();
                if (!TextUtils.isEmpty(content)) {
                    clearAccount.setVisibility(View.VISIBLE);
                } else {
                    clearAccount.setVisibility(View.INVISIBLE);
                }

            }
        });

        loginCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String content = loginCode.getText().toString().trim();
                if (!TextUtils.isEmpty(content)) {
                    clearCode.setVisibility(View.VISIBLE);

                } else {
                    clearCode.setVisibility(View.INVISIBLE);
                }
            }
        });

    }

    @Override
    public int getLayoutId() {
        return RUtil.getLayout(getActivity(), "fragment_mobile_phone_login");
    }

    public static MobilePhoneLoginFragment newInstance() {
        return new MobilePhoneLoginFragment();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == sendCodeId) {
            //发送验证码
            String phoneNum = loginPhoneNum.getText().toString().trim();
            if (TextUtils.isEmpty(phoneNum)) {
                ToastUtil.showToast(getActivity(), "手机号不能为空");
                return;
            }
            alertDialog = showLoadingDialog("获取中...");
            mobilePhoneLoginPreference.sendCode(getActivity(), phoneNum);

        } else if (v.getId() == enterGameId) {
            //进入游戏
            //1.验证code是否正确
            String phoneNum = loginPhoneNum.getText().toString().trim();
            account = phoneNum;
            String code = loginCode.getText().toString().trim();
            if (TextUtils.isEmpty(phoneNum)) {
                ToastUtil.showToast(getActivity(), "手机号不能为空");
                return;
            }
            if (TextUtils.isEmpty(code)) {
                ToastUtil.showToast(getActivity(), "验证码不能为空");
                return;
            }

            if (!cbUserProtocol.isChecked()) {
                ToastUtil.showToast(getActivity(), "请先勾选用户协议");
                return;
            }
            alertDialog = null;
            alertDialog = showLoadingDialog();
            mobilePhoneLoginPreference.quickLogin(getActivity(), phoneNum, code);

        } else if (v.getId() == clearAccountId) {
            loginPhoneNum.setText("");
            clearAccount.setVisibility(View.INVISIBLE);
        } else if (v.getId() == clearCodeId) {
            loginCode.setText("");
            clearCode.setVisibility(View.INVISIBLE);
        } else if (v.getId() == userProtocolId) {
            Intent intent = new Intent(getActivity(), UserProtocolActivity.class);
            startActivity(intent);

        } else if (rlBackId == v.getId()) {
            addFragment(LoginSelectFragment.newInstance());
        } else if (loginOtherAccountId == v.getId()) {
            //账号密码登录
            addFragment(LoginByTTAccountFragment.newInstance());

        }


    }

    @Override
    public void onGetCodeSuccess(SendCode sendCode) {
        //开始倒计时
        if (alertDialog != null) {
            if (alertDialog.isShowing()) {
                alertDialog.dismiss();
            }
        }
        int countDownTime = sendCode.getRetry_time();
        String mobile = sendCode.getMobile();
        if (running) {
        } else {
            createCountDown(countDownTime);
            downTimer.start();

        }

    }

    @Override
    public void onGetCodeFailture(String errorCode, String errorMsg) {
        if (alertDialog != null) {
            if (alertDialog.isShowing()) {
                alertDialog.dismiss();
            }
        }
        ToastUtil.showToast(getActivity(), errorMsg);
    }

    @Override
    public void onQuickLoginSuccess(QuickLogin quickLogin) {
        mobilePhoneLoginPreference.queryOAuthCode(getActivity());

    }

    @Override
    public void onQuickLoginFailture(String errorCode, String errorMessage) {
        if (alertDialog != null) {
            if (alertDialog.isShowing()) {
                alertDialog.dismiss();
            }
        }
        ToastUtil.showToast(getActivity(), errorMessage);
    }

    @Override
    public void onQueryOAuthCodeSuccess(Authorize authorize) {
        String code = authorize.getCode();
        //请求token
        mobilePhoneLoginPreference.queryToken(getActivity(), code);


    }

    @Override
    public void onQueryOAuthCodeFailture(String errorCode, String errorMessage) {
        if (alertDialog != null) {
            if (alertDialog.isShowing()) {
                alertDialog.dismiss();
            }
        }
        ToastUtil.showToast(getActivity(), "errorCode:" + errorCode + ",errorMessage:" + errorMessage);
    }

    //获取到token成功
    @Override
    public void onQueryTokenSuccess(TokenModel tokenModel) {
        MobilePhoneLoginFragment.token = tokenModel.getAccess_token();
        //请求game_login接口
        mobilePhoneLoginPreference.gameLogin(getActivity(), tokenModel);

    }

    //获取到token失败
    @Override
    public void onQueryTokenFailture(String errorCode, String errorMessage) {
        if (alertDialog != null) {
            if (alertDialog.isShowing()) {
                alertDialog.dismiss();
            }
        }
        ToastUtil.showToast(getActivity(), MessageFormat.format("errorCode={0},errorMessage={1}", errorCode, errorMessage));
    }

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

        final SharedPreferenceUtil sharedPreferenceUtil = SharedPreferenceUtil.getInstance(getActivity());
        sharedPreferenceUtil.setHasLogin(true);
        sharedPreferenceUtil.setAccount(account);
        saveUserInfo();

        UserInfo saveUser = new UserInfo(sUserInfo.getAccessToken(), sUserInfo.getOpenId(), sUserInfo.getUserId(), sUserInfo.getUserType(), account, gameLogin.getLogin_id());
        sharedPreferenceUtil.saveAllAccountInfo(saveUser);
        MobileActivity.loginCallback.onLoginSuccess(MobilePhoneLoginFragment.token, gameLogin.getUser_id(), gameLogin.getOpen_id(), ACCOUNT_NUMBER);
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

    @Override
    public void onGameLoginFailture(String errorCode, String errorMessage) {
        if (alertDialog != null) {
            if (alertDialog.isShowing()) {
                alertDialog.dismiss();
            }
        }
        //登录失败
        ToastUtil.showToast(getActivity(), MessageFormat.format("errorCode={0},errorMessage={1}", errorCode, errorMessage));

        SharedPreferenceUtil sharedPreferenceUtil = SharedPreferenceUtil.getInstance(getActivity());
        sharedPreferenceUtil.setHasLogin(false);
        sharedPreferenceUtil.setAccount("");
    }

    public void createCountDown(int countDownTime) {
        downTimer = new CountDownTimer(countDownTime * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                running = true;
                sendCode.setText("重新发送 " + Math.round((double) millisUntilFinished / 1000));
                sendCode.setTextColor(getResources().getColor(RUtil.getColor(getActivity(), "biaozhunhong3_disable")));
            }

            @Override
            public void onFinish() {
                running = false;
                sendCode.setText("重新发送");
                sendCode.setTextColor(getResources().getColor(RUtil.getColor(getActivity(), "biaozhunhong3")));
            }
        };
    }


    public void saveUserInfo() {
        SharedPreferenceUtil sharedPreferenceUtil = SharedPreferenceUtil.getInstance(getActivity());
        sharedPreferenceUtil.putBean(SharedPreferenceUtil.USER_INFO, sUserInfo);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (downTimer != null) {
            downTimer.cancel();
        }
    }
}
