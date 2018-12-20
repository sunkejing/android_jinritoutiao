package com.ss.android.pay.bean;

import java.io.Serializable;

/**
 * 支付请求model
 */
public class PayRequestData implements Serializable {
    private String outTradeNo;//订单号
    private String productName;//商品名称
    private int productPrice;//商品价格，单位分

    public PayRequestData(String outTradeNo, String productName, int productPrice) {
        this.outTradeNo = outTradeNo;
        this.productName = productName;
        this.productPrice = productPrice;
    }

    public PayRequestData() {
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public String getProductName() {
        return productName;
    }

    public int getProductPrice() {
        return productPrice;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setProductPrice(int productPrice) {
        this.productPrice = productPrice;
    }

    @Override
    public String toString() {
        return "PayRequestData{" +
                "outTradeNo='" + outTradeNo + '\'' +
                ", productName='" + productName + '\'' +
                ", productPrice=" + productPrice +
                '}';
    }
}
