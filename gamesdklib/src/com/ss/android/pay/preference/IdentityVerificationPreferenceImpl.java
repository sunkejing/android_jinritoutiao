package com.ss.android.pay.preference;

import android.content.Context;

import com.ss.android.game.sdk.info.UserInfo;
import com.ss.android.login.sdk.model.RequestCallback;
import com.ss.android.pay.model.IdentityVerificationModel;
import com.ss.android.pay.model.IdentityVerificationModelImpl;
import com.ss.android.pay.view.IdentityVerificationView;

public class IdentityVerificationPreferenceImpl implements IdentityVerificationPreference {
    private IdentityVerificationView identityVerificationView;
    private IdentityVerificationModel identityVerificationModel;

    public IdentityVerificationPreferenceImpl(IdentityVerificationView identityVerificationView) {
        this.identityVerificationView = identityVerificationView;
        this.identityVerificationModel = new IdentityVerificationModelImpl();

    }

    public void verifyIdentity(Context context, String userName, String idCrad, UserInfo userInfo) {


        identityVerificationModel.doVerifyIdentity(context, userName, idCrad, userInfo, new RequestCallback() {
            @Override
            public void onRequestSuccess(Object object) {
                identityVerificationView.onVerifyIdentitySuccess();
            }

            @Override
            public void onRequestFailture(String errorCode, String errorMessage) {
                identityVerificationView.onVerifyIdentityFailture(errorCode, errorMessage);
            }
        });
    }


}
