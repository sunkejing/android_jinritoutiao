package com.ss.android.pay.preference;

import android.content.Context;

import com.ss.android.game.sdk.info.UserInfo;

public interface IdentityVerificationPreference {
    public void verifyIdentity(Context context, String userName, String idCrad, UserInfo userInfo);
}
