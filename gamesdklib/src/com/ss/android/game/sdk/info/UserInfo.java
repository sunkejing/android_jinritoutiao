package com.ss.android.game.sdk.info;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/*
记录用户登录成功之后的重要信息
 */
public class UserInfo implements Serializable {
    private String accessToken;
    private String openId;
    private long userId;
    private String loginId;
    private int userType;
    private int hasPlayed;
    private String mobile;

    public String getOpenId() {
        return openId;
    }

    public long getUserId() {
        return userId;
    }

    public String getLoginId() {
        return loginId;
    }

    public int getHasPlayed() {
        return hasPlayed;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public void setHasPlayed(int hasPlayed) {
        this.hasPlayed = hasPlayed;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }


    public UserInfo() {
    }

    public UserInfo(String accessToken, String openId, long userId, int userType, String mobile, String loginId) {
        this.accessToken = accessToken;
        this.openId = openId;
        this.userId = userId;
        this.userType = userType;
        this.mobile = mobile;
        this.loginId = loginId;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof UserInfo) {
            UserInfo userInfo = (UserInfo) o;
            return this.accessToken.equals(userInfo.getAccessToken()) && this.openId.equals(userInfo.getOpenId()) && this.userId == userInfo.getUserId() && this.mobile.equals(userInfo.getMobile()) && this.loginId.equals(userInfo.getLoginId());
        }
        return super.equals(o);
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "accessToken='" + accessToken + '\'' +
                ", openId='" + openId + '\'' +
                ", userId=" + userId +
                ", loginId='" + loginId + '\'' +
                ", userType=" + userType +
                ", hasPlayed=" + hasPlayed +
                ", mobile='" + mobile + '\'' +
                '}';
    }
}
