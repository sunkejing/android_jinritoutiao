package com.ss.android.pay.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.ss.android.gamecommon.util.IDCard;
import com.ss.android.gamecommon.util.RUtil;
import com.ss.android.gamecommon.util.ToastUtil;
import com.ss.android.login.sdk.activity.MobileActivity;
import com.ss.android.login.sdk.fragment.BaseFragment;
import com.ss.android.pay.activity.PayActivity;
import com.ss.android.pay.preference.IdentityVerificationPreference;
import com.ss.android.pay.preference.IdentityVerificationPreferenceImpl;
import com.ss.android.pay.tools.BackHandlerHelper;
import com.ss.android.pay.tools.FragmentBackHandler;
import com.ss.android.pay.view.IdentityVerificationView;

/**
 * 身份验证fragment
 */
public class IdentityVerificationFragment extends BaseFragment implements View.OnClickListener, IdentityVerificationView, FragmentBackHandler {
    private ImageView imgClose;//关闭按钮
    private EditText enterName;//输入姓名
    private ImageView clearName;//清空姓名
    private EditText etEnterId;//输入身份证
    private ImageView imgClearId;//清空身份证
    private TextView errorTip;//身份证错误提示信息
    private TextView tvVerification;//验证


    private int closeId;
    private int enterNameId;
    private int enterId;
    private int clearNameId;
    private int clearId;
    private int verificationId;

    private IdentityVerificationPreference identityVerificationPreference;
    private Dialog alertDialog;


    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        closeId = RUtil.getId(getActivity(), "img_close");
        imgClose = findViewId(view, closeId);
        imgClose.setOnClickListener(this);
        enterNameId = RUtil.getId(getActivity(), "et_enter_name");
        enterName = findViewId(view, enterNameId);
        enterId = RUtil.getId(getActivity(), "et_enter_id");
        etEnterId = findViewId(view, enterId);
        clearNameId = RUtil.getId(getActivity(), "img_clear_name");
        clearName = findViewId(view, clearNameId);
        clearName.setOnClickListener(this);
        clearId = RUtil.getId(getActivity(), "img_clear_id");
        imgClearId = findViewId(view, clearId);
        imgClearId.setOnClickListener(this);
        initTextWatcher();
        errorTip = findViewId(view, RUtil.getId(getActivity(), "tv_error_tip"));
        errorTip.setVisibility(View.GONE);
        verificationId = RUtil.getId(getActivity(), "tv_verification");
        tvVerification = findViewId(view, verificationId);
        tvVerification.setOnClickListener(this);
        identityVerificationPreference = new IdentityVerificationPreferenceImpl(this);
    }

    @Override
    public int getLayoutId() {
        return RUtil.getLayout(getActivity(), "fragment_identity_verification");
    }

    public static IdentityVerificationFragment newInstance() {
        return new IdentityVerificationFragment();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == closeId) {
            //关闭之后走支付的逻辑
            if (getActivity() != null) {
                ((PayActivity) getActivity()).onVerifyIdentityCancel();
            }

        } else if (v.getId() == clearNameId) {
            //清空姓名
            enterName.setText("");
            clearName.setVisibility(View.INVISIBLE);

        } else if (v.getId() == clearId) {
            //清空身份证
            etEnterId.setText("");
            imgClearId.setVisibility(View.INVISIBLE);
        } else if (v.getId() == verificationId) {
            //开始验证
            String userName = enterName.getText().toString().trim();
            if (TextUtils.isEmpty(userName)) {
                ToastUtil.showToast(getActivity(), "姓名不能为空");
                return;
            }
            String id = etEnterId.getText().toString().trim();
            boolean isValidate = IDCard.IDCardValidate(id);
            if (!isValidate) {
                ToastUtil.showToast(getActivity(), "身份证不合法");
                return;
            }
            alertDialog = showLoadingDialog("验证中");
            //开始验证
            identityVerificationPreference.verifyIdentity(getActivity(), userName, id, MobileActivity.sUserInfo);
        }
    }

    private void initTextWatcher() {
        enterName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String content = enterName.getText().toString().trim();
                if (!TextUtils.isEmpty(content)) {
                    clearName.setVisibility(View.VISIBLE);
                } else {
                    clearName.setVisibility(View.INVISIBLE);
                }

            }
        });

        etEnterId.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String content = etEnterId.getText().toString().trim();
                if (!TextUtils.isEmpty(content)) {
                    imgClearId.setVisibility(View.VISIBLE);
                } else {
                    imgClearId.setVisibility(View.INVISIBLE);
                }
            }
        });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (alertDialog != null) {
            alertDialog.dismiss();
        }
    }

    @Override
    public void onVerifyIdentitySuccess() {
        if (alertDialog != null) {
            alertDialog.dismiss();
        }
        if (getActivity() != null) {
            ((PayActivity) getActivity()).onVerifyIdentitySuccess();
        }


    }

    @Override
    public void onVerifyIdentityFailture(String errorCode, String errorMessage) {
        if (alertDialog != null) {
            alertDialog.dismiss();
        }
        if (errorCode.equals("-1")) {
            ToastUtil.showToast(getActivity(), errorMessage);
        } else {
            ToastUtil.showToast(getActivity(), "认证失败，请稍后重试");
        }

    }

    @Override
    public boolean onBackPressed() {
        return true;
    }
}
