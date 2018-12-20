package com.ss.android.GameSdkDemo;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.blackwings.sgzdz.lzyx.R;
import com.ss.android.game.sdk.SsGameApi;
import com.ss.android.gamecommon.util.NumberEncryption;
import com.ss.android.gamecommon.util.ToastUtil;
import com.ss.android.login.sdk.LoginCallback;
import com.ss.android.pay.PayCallback;
import com.ss.android.pay.bean.PayRequestData;

import java.text.MessageFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class DemoActivity extends Activity {
    private String cliendId = "tt";
    private String payKey = "38bd0bbbb76c9d2b255784f1fa520e1b";

    @BindView(R.id.trade_no)
    EditText tradeNo;
    @BindView(R.id.subject)
    EditText subject;
    @BindView(R.id.price)
    EditText price;
    @BindView(R.id.pay)
    Button pay;
    @BindView(R.id.login)
    Button login;
    @BindView(R.id.logout)
    Button logout;
    @BindView(R.id.visitor_bind)
    Button visitorBind;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        SsGameApi.init(this, cliendId, payKey);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        SsGameApi.onResume(this);

    }

    @OnClick({R.id.login, R.id.pay})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login:
                SsGameApi.login(this, new LoginCallback() {

                    @Override
                    public void onLoginSuccess(String accessToken, long uid, String openId, int userType) {
                        ToastUtil.showToast(DemoActivity.this, MessageFormat.format("登录成功,accesstoken={0},uid={1},openId={2},userType={3}", accessToken, uid, openId, userType));
                    }

                    @Override
                    public void onLoginFail(int errorCode, String errorMsg) {
                        ToastUtil.showToast(DemoActivity.this, "登录失败：" + errorMsg);
                    }
                });


                break;
            case R.id.logout:


                break;
            case R.id.pay:
                String orderId = tradeNo.getText().toString().trim();//订单
                if (TextUtils.isEmpty(orderId)) {
                    orderId = String.valueOf(System.currentTimeMillis());
                }
                String productName = subject.getText().toString().trim();//商品名称
                if (TextUtils.isEmpty(productName)) {
                    productName = "测试商品";
                }
                String priceStr = price.getText().toString().trim();
                if (TextUtils.isEmpty(priceStr)) {
                    priceStr = String.valueOf(1);
                }
                int productPrice;
                if (NumberEncryption.isNumeric(priceStr)) {
                    productPrice = Integer.parseInt(priceStr);
                } else {
                    ToastUtil.showToast(this, "商品价格不合法");
                    return;
                }
                PayRequestData payRequestData = new PayRequestData();
                payRequestData.setOutTradeNo(orderId);
                payRequestData.setProductName(productName);
                payRequestData.setProductPrice(productPrice);
                SsGameApi.pay(this, payRequestData, new PayCallback() {


                    @Override
                    public void onPaySuccess(int errorCode, String errorMsg) {
                        ToastUtil.showToast(DemoActivity.this, errorMsg);
                    }

                    @Override
                    public void onPayCancel(int errorCode, String errorMsg) {
                        ToastUtil.showToast(DemoActivity.this, errorMsg);
                    }


                    @Override
                    public void onPayFail(int errorCode, String errorMsg) {
                        ToastUtil.showToast(DemoActivity.this, errorMsg);
                    }
                });
                break;
        }
    }


}
