package com.ss.android.gamecommon.util;

public enum NetworkType {
    NONE(0), MOBILE(1), MOBILE_2G(2), MOBILE_3G(3), WIFI(4), MOBILE_4G(5);

    public int getNetworkType() {
        return networkType;
    }

    NetworkType(int i) {
        this.networkType = i;
    }

    int networkType;
}
