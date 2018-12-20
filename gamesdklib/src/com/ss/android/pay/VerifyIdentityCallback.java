package com.ss.android.pay;

/**
 * 身份验证结果
 */
public interface VerifyIdentityCallback {
    void onVerifyIdentitySuccess();//身份验证成功

    void onVerifyIdentityCancel();//身份验证失败
}
