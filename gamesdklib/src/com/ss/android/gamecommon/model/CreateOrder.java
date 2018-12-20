package com.ss.android.gamecommon.model;

import java.io.Serializable;

/**
 * 订单bean
 */
public class CreateOrder implements Serializable {
    private String consult_telephone;//咨询电话
    private String prepay_id;//订单id
    private String tt_sign;
    private String sub_way;
    private String ways;
    private String total_fee;//总金额
    private String tt_sign_type;//签名类型
    private String subject;//商品名称

    public CreateOrder(String consult_telephone, String prepay_id, String tt_sign, String sub_way, String ways, String total_fee, String tt_sign_type, String subject) {
        this.consult_telephone = consult_telephone;
        this.prepay_id = prepay_id;
        this.tt_sign = tt_sign;
        this.sub_way = sub_way;
        this.ways = ways;
        this.total_fee = total_fee;
        this.tt_sign_type = tt_sign_type;
        this.subject = subject;
    }

    public CreateOrder() {

    }

    public String getConsult_telephone() {
        return consult_telephone;
    }

    public String getPrepay_id() {
        return prepay_id;
    }

    public String getTt_sign() {
        return tt_sign;
    }

    public String getSub_way() {
        return sub_way;
    }

    public String getWays() {
        return ways;
    }

    public String getTotal_fee() {
        return total_fee;
    }

    public String getTt_sign_type() {
        return tt_sign_type;
    }

    public String getSubject() {
        return subject;
    }

    public void setConsult_telephone(String consult_telephone) {
        this.consult_telephone = consult_telephone;
    }

    public void setPrepay_id(String prepay_id) {
        this.prepay_id = prepay_id;
    }

    public void setTt_sign(String tt_sign) {
        this.tt_sign = tt_sign;
    }

    public void setSub_way(String sub_way) {
        this.sub_way = sub_way;
    }

    public void setWays(String ways) {
        this.ways = ways;
    }

    public void setTotal_fee(String total_fee) {
        this.total_fee = total_fee;
    }

    public void setTt_sign_type(String tt_sign_type) {
        this.tt_sign_type = tt_sign_type;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    @Override
    public String toString() {
        return "CreateOrder{" +
                "consult_telephone='" + consult_telephone + '\'' +
                ", prepay_id='" + prepay_id + '\'' +
                ", tt_sign='" + tt_sign + '\'' +
                ", sub_way='" + sub_way + '\'' +
                ", ways='" + ways + '\'' +
                ", total_fee='" + total_fee + '\'' +
                ", tt_sign_type='" + tt_sign_type + '\'' +
                ", subject='" + subject + '\'' +
                '}';
    }
}
