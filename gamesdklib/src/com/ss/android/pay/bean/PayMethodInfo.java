package com.ss.android.pay.bean;

public class PayMethodInfo {
    private String methodName;//支付方式
    private int imgIcon;
    private int methodType;//支付类型

    public PayMethodInfo(String methodName, int imgIcon, int methodType) {
        this.methodName = methodName;
        this.imgIcon = imgIcon;
        this.methodType = methodType;
    }

    public PayMethodInfo() {

    }

    public String getMethodName() {
        return methodName;
    }

    public int getImgIcon() {
        return imgIcon;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public void setImgIcon(int imgIcon) {
        this.imgIcon = imgIcon;
    }

    public int getMethodType() {
        return methodType;
    }

    public void setMethodType(int methodType) {
        this.methodType = methodType;
    }

    @Override
    public String toString() {
        return "PayMethodInfo{" +
                "methodName='" + methodName + '\'' +
                ", imgIcon=" + imgIcon +
                ", methodType=" + methodType +
                '}';
    }
}
