package com.ss.android.gamecommon.model;

public class Verified {
    private int error_code;
    private int has_verified;
    private int need_verify;

    public int getError_code() {
        return error_code;
    }

    public int getHas_verified() {
        return has_verified;
    }

    public int getNeed_verify() {
        return need_verify;
    }

    public void setError_code(int error_code) {
        this.error_code = error_code;
    }

    public void setHas_verified(int has_verified) {
        this.has_verified = has_verified;
    }

    public void setNeed_verify(int need_verify) {
        this.need_verify = need_verify;
    }

    @Override
    public String toString() {
        return "Verified{" +
                "error_code=" + error_code +
                ", has_verified=" + has_verified +
                ", need_verify=" + need_verify +
                '}';
    }
}
