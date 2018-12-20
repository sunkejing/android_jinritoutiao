package com.ss.android.login.sdk.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.ss.android.game.sdk.info.UserInfo;
import com.ss.android.gamecommon.model.Authorize;
import com.ss.android.gamecommon.model.GameLogin;
import com.ss.android.gamecommon.model.TicketModel;
import com.ss.android.gamecommon.model.TokenModel;
import com.ss.android.gamecommon.model.Verified;
import com.ss.android.gamecommon.util.RUtil;
import com.ss.android.gamecommon.util.SharedPreferenceUtil;
import com.ss.android.gamecommon.util.ToastUtil;
import com.ss.android.login.sdk.activity.MobileActivity;
import com.ss.android.login.sdk.model.RequestCallback;
import com.ss.android.login.sdk.preference.LoginByTTAccountPreference;
import com.ss.android.login.sdk.preference.LoginByTTAccountPreferenceImpl;
import com.ss.android.login.sdk.view.LoginByTTAccountView;

import static com.ss.android.gamecommon.util.ConstantUtil.ACCOUNT_NUMBER;
import static com.ss.android.login.sdk.activity.MobileActivity.loginCallback;
import static com.ss.android.login.sdk.activity.MobileActivity.sUserInfo;

public class LoginByTTAccountFragment extends BaseFragment implements View.OnClickListener, LoginByTTAccountView {
    private EditText ttAccount;//账号
    private EditText ttPwd;//密码
    private ImageView clearAccount;//清除账号
    private ImageView clearPwd;//清除密码
    private TextView forgetPwd;//忘记密码
    private TextView enterGame;//进入游戏
    private TextView registeredAccount;//注册账号
    private TextView passwordFreeLogin;//免密码登录


    private int ttAccountId;
    private int ttPwdId;
    private int clearAccountId;
    private int clearPwdId;
    private int forgetPwdId;
    private int enterGameId;
    private int registeredAccountId;
    private int passwordFreeLoginId;

    private LoginByTTAccountPreference loginByTTAccountPreference;
    private Dialog dialog;
    private String token;

    private String account;


    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        ttAccountId = RUtil.getId(getActivity(), "et_ttaccount");
        ttAccount = findViewId(view, ttAccountId);
        ttPwdId = RUtil.getId(getActivity(), "et_ttpwd");
        ttPwd = findViewId(view, ttPwdId);
        clearAccountId = RUtil.getId(getActivity(), "img_clear_account");
        clearAccount = findViewId(view, clearAccountId);
        clearAccount.setOnClickListener(this);
        clearAccount.setVisibility(View.INVISIBLE);
        clearPwdId = RUtil.getId(getActivity(), "img_clear_pwd");
        clearPwd = findViewId(view, clearPwdId);
        clearPwd.setVisibility(View.INVISIBLE);
        clearPwd.setOnClickListener(this);
        forgetPwdId = RUtil.getId(getActivity(), "btn_forgetpwd");
        forgetPwd = findViewId(view, forgetPwdId);
        forgetPwd.setOnClickListener(this);
        enterGameId = RUtil.getId(getActivity(), "btn_entergame");
        enterGame = findViewId(view, enterGameId);
        enterGame.setOnClickListener(this);
        registeredAccountId = RUtil.getId(getActivity(), "btn_registered_account");
        registeredAccount = findViewId(view, registeredAccountId);
        registeredAccount.setOnClickListener(this);
        passwordFreeLoginId = RUtil.getId(getActivity(), "btn_password_free_login");
        passwordFreeLogin = findViewId(view, passwordFreeLoginId);
        passwordFreeLogin.setOnClickListener(this);
        initTextWatcher();

        loginByTTAccountPreference = new LoginByTTAccountPreferenceImpl(this);
    }

    @Override
    public int getLayoutId() {
        return RUtil.getLayout(getActivity(), "fragment_login_by_tt_account");
    }

    public static LoginByTTAccountFragment newInstance() {
        return new LoginByTTAccountFragment();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == clearAccountId) {
            ttAccount.setText("");
            clearAccount.setVisibility(View.INVISIBLE);
        } else if (id == clearPwdId) {
            ttPwd.setText("");
            clearPwd.setVisibility(View.INVISIBLE);
        } else if (id == forgetPwdId) {
            //忘记密码
            addFragment(MobilePhoneLoginFragment.newInstance());

        } else if (id == enterGameId) {
            //进入游戏
            account = ttAccount.getText().toString().trim();//手机号
            String pwd = ttPwd.getText().toString().trim();//密码
            if (TextUtils.isEmpty(account)) {
                ToastUtil.showToast(getActivity(), "手机号不能为空");
                return;
            }
            if (TextUtils.isEmpty(pwd)) {
                ToastUtil.showToast(getActivity(), "密码不能为空");
                return;
            }
            dialog = showLoadingDialog();
            loginByTTAccountPreference.enterGameForTicker(getActivity(), account, pwd);


        } else if (id == registeredAccountId) {
            //注册账号
            addFragment(MobilePhoneLoginFragment.newInstance());
        } else if (id == passwordFreeLoginId) {
            //免密码登录
            addFragment(MobilePhoneLoginFragment.newInstance());
        } else if (id == clearPwdId) {
            //清空密码
            ttPwd.setText("");

        } else if (id == clearAccountId) {
            //清空账号
            ttAccount.setText("");
        }

    }

    private void initTextWatcher() {
        ttAccount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String content = ttAccount.getText().toString().trim();
                if (!TextUtils.isEmpty(content)) {
                    clearAccount.setVisibility(View.VISIBLE);
                } else {
                    clearAccount.setVisibility(View.INVISIBLE);
                }

            }
        });

        ttPwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String content = ttPwd.getText().toString().trim();
                if (!TextUtils.isEmpty(content)) {
                    clearPwd.setVisibility(View.VISIBLE);

                } else {
                    clearPwd.setVisibility(View.INVISIBLE);
                }
            }
        });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (dialog != null) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }
    }

    //获取ticket成功
    @Override
    public void onEnterGameForTickerSuccess(TicketModel ticketModel) {
        //获取code
        loginByTTAccountPreference.enterGameForCode(getActivity());

    }

    @Override
    public void onEnterGameForTickerFailture(String errorCode, String message) {
        if (dialog != null) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }
        ToastUtil.showToast(getActivity(), message);
    }

    //请求code成功
    @Override
    public void onRequestCodeSuccess(Authorize authorize) {
        String code = authorize.getCode();
        loginByTTAccountPreference.enterGameForToken(getActivity(), code);
    }

    //请求code失败
    @Override
    public void onRequestCodeFailture(String errorCode, String message) {
        if (dialog != null) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }
        ToastUtil.showToast(getActivity(), message);

    }

    //获取token成功
    @Override
    public void onEnterGameForTokenSuccess(TokenModel tokenModel) {
        //调用gameLogin接口
        token = tokenModel.getAccess_token();
        loginByTTAccountPreference.onGameLogin(getActivity(), tokenModel);
    }

    //获取token失败
    @Override
    public void onEnterGameForTokenFailture(String errorCode, String message) {
        if (dialog != null) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }
        ToastUtil.showToast(getActivity(), message);
    }

    //登录成功
    @Override
    public void onGameLoginSuccess(GameLogin gameLogin) {
        if (dialog != null) {
            if (dialog.isShowing()) {
                dialog.dismiss();
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

        loginCallback.onLoginSuccess(token, gameLogin.getUser_id(), gameLogin.getOpen_id(), ACCOUNT_NUMBER);
        UserInfo saveUser = new UserInfo(sUserInfo.getAccessToken(), sUserInfo.getOpenId(), sUserInfo.getUserId(), sUserInfo.getUserType(), account, gameLogin.getLogin_id());
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

    //登录失败
    @Override
    public void onGameLoginFailture(String errorCode, String message) {
        if (dialog != null) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }
        ToastUtil.showToast(getActivity(), message);

        SharedPreferenceUtil sharedPreferenceUtil = SharedPreferenceUtil.getInstance(getActivity());
        sharedPreferenceUtil.setHasLogin(false);

        sharedPreferenceUtil.setAccount("");
    }

    public void saveUserInfo() {
        SharedPreferenceUtil sharedPreferenceUtil = SharedPreferenceUtil.getInstance(getActivity());
        sharedPreferenceUtil.putBean(SharedPreferenceUtil.USER_INFO, sUserInfo);
    }
}
