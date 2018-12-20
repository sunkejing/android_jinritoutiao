package com.ss.android.pay.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alipay.sdk.app.PayTask;
import com.ss.android.game.sdk.GameSdk;
import com.ss.android.gamecommon.customview.adpter.PaymethodRecyclerViewAdapter;
import com.ss.android.gamecommon.model.CreateOrder;
import com.ss.android.gamecommon.thread.ThreadPoolManager;
import com.ss.android.gamecommon.util.ConstantUtil;
import com.ss.android.gamecommon.util.IntentUtil;
import com.ss.android.gamecommon.util.LogUtil;
import com.ss.android.gamecommon.util.NumberEncryption;
import com.ss.android.gamecommon.util.RUtil;
import com.ss.android.gamecommon.util.ToastUtil;
import com.ss.android.login.sdk.activity.MobileActivity;
import com.ss.android.login.sdk.fragment.BaseFragment;
import com.ss.android.pay.SSPayManager;
import com.ss.android.pay.activity.WXH5PayActivity;
import com.ss.android.pay.bean.PayMethodInfo;
import com.ss.android.pay.bean.PayResult;
import com.ss.android.pay.preference.PaymentSelectPreference;
import com.ss.android.pay.preference.PaymentSelectPreferenceImpl;
import com.ss.android.pay.view.PaymentSelectView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.ss.android.gamecommon.util.ConstantUtil.OUT_TRADE_NO;
import static com.ss.android.gamecommon.util.ConstantUtil.PAY_EXT_PARMAS;
import static com.ss.android.gamecommon.util.ConstantUtil.PAY_INFO;
import static com.ss.android.gamecommon.util.ConstantUtil.PAY_METHOD_ALI;
import static com.ss.android.gamecommon.util.ConstantUtil.PAY_METHOD_WX;

public class PaymentSelectFragment extends BaseFragment implements View.OnClickListener, PaymentSelectView {
    private CreateOrder createOrder;
    private JSONObject payExtParams;
    private TextView productName;//商品名称
    private TextView productPrice;//价格
    private RecyclerView rvPaymethod;
    private TextView startPay;//开始支付
    private ImageView imgPayClose;

    private int startPayId;
    private int imgPayCloseId;

    private PaymethodRecyclerViewAdapter paymethodRecyclerViewAdapter;
    private List<PayMethodInfo> payMethodInfos;

    private PaymentSelectPreference paymentSelectPreference;
    private int payMethod;

    private Dialog alertDialog;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case ConstantUtil.PAY_METHOD_ALI: {
                    @SuppressWarnings("unchecked")
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    System.out.println("payResult:" + payResult.toString());

                    /**
                     对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    switch (resultStatus) {
                        // 判断resultStatus 为9000则代表支付成功
                        case "9000":
                            SSPayManager.sPayCallback.onPaySuccess(ConstantUtil.PAY_SUCCESS, "支付成功");
                            break;
                        case "6001":
                            //取消支付
                            SSPayManager.sPayCallback.onPayCancel(ConstantUtil.PAY_CANCEL, "支付取消");
                            break;
                        case "6002":
                            SSPayManager.sPayCallback.onPayFail(ConstantUtil.PAY_FAIL, "网络连接出错");
                            break;
                        case "4000":
                            SSPayManager.sPayCallback.onPayFail(ConstantUtil.PAY_FAIL, "支付失败");
                            break;
                        default:
                            SSPayManager.sPayCallback.onPayFail(ConstantUtil.PAY_FAIL, "支付失败");
                            break;

                    }
                    if (getActivity() != null) {
                        getActivity().finish();
                    }
                    break;
                }
            }
        }
    };


    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        if (getArguments() != null) {
            createOrder = (CreateOrder) getArguments().getSerializable(PAY_INFO);
            String payExtParam = getArguments().getString(PAY_EXT_PARMAS);
            if (!TextUtils.isEmpty(payExtParam)) {
                try {
                    payExtParams = new JSONObject(payExtParam);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }
        productName = findViewId(view, RUtil.getId(getActivity(), "tv_product_name"));
        productName.setText(createOrder.getSubject());
        productPrice = findViewId(view, RUtil.getId(getActivity(), "tv_product_price"));
        productPrice.setText(createOrder.getTotal_fee());
        rvPaymethod = findViewId(view, RUtil.getId(getActivity(), "rv_paymethod"));
        startPayId = RUtil.getId(getActivity(), "start_pay");
        startPay = findViewId(view, startPayId);
        startPay.setOnClickListener(this);
        imgPayCloseId = RUtil.getId(getActivity(), "img_pay_close");
        imgPayClose = findViewId(view, imgPayCloseId);
        imgPayClose.setOnClickListener(this);
        initRecyclerView(view);

        paymentSelectPreference = new PaymentSelectPreferenceImpl(this);


    }

    private void initRecyclerView(View view) {
        rvPaymethod = (RecyclerView) view.findViewById(RUtil.getId(getActivity(), "rv_paymethod"));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvPaymethod.setLayoutManager(linearLayoutManager);
        initRecyclerViewData();
        paymethodRecyclerViewAdapter = new PaymethodRecyclerViewAdapter(getActivity(), payMethodInfos);
        //rvPaymethod.getItemAnimator().setSupportsChangeAnimations(false);
        rvPaymethod.setAdapter(paymethodRecyclerViewAdapter);
    }

    private void initRecyclerViewData() {
        payMethodInfos = new ArrayList<PayMethodInfo>();
        payMethodInfos.add(new PayMethodInfo("支付宝支付", RUtil.getDrawable(getActivity(), "tt_icon_alipay"), PAY_METHOD_ALI));
        if (isWxInstall(getActivity())) {
            payMethodInfos.add(new PayMethodInfo("微信支付", RUtil.getDrawable(getActivity(), "tt_icon_wechatpay"), PAY_METHOD_WX));
        }
    }

    @Override
    public int getLayoutId() {
        return RUtil.getLayout(getActivity(), "fragment_payment_method_choice");
    }

    public static BaseFragment newInstance(CreateOrder createOrder, String extParmas) {
        PaymentSelectFragment fragment = new PaymentSelectFragment();
        Bundle args = new Bundle();
        args.putSerializable(PAY_INFO, createOrder);
        args.putString(PAY_EXT_PARMAS, extParmas);

        fragment.setArguments(args);
        return fragment;
    }

    /**
     * 判断是否安装了微信
     *
     * @param context
     * @return
     */
    private boolean isWxInstall(Context context) {
        PackageManager packageManager = context.getPackageManager();//获取packagemanager
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);//获取所有已安装程序的包信息
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if ("com.tencent.mm".equals(pn)) {
                    return true;
                }
            }
        }
        return false;

    }


    @Override
    public void onClick(View v) {
        if (v.getId() == startPayId) {
            //开始支付
            HashMap<String, Object> extMap = new HashMap<>();
            String productName = this.productName.getText().toString().trim();//商品名称
            String productPrice = this.productPrice.getText().toString().trim();//商品价格
            int selectPosition = paymethodRecyclerViewAdapter.getSelectPosition();
            if (selectPosition >= 0) {
                int methodType = payMethodInfos.get(paymethodRecyclerViewAdapter.getSelectPosition()).getMethodType();
                switch (methodType) {
                    case PAY_METHOD_ALI:
                        //支付宝支付
                        payMethod = PAY_METHOD_ALI;
                        extMap.put("way", "2");
                        break;
                    case PAY_METHOD_WX:
                        //微信支付
                        payMethod = PAY_METHOD_WX;
                        extMap.put("way", "1");
                        extMap.put("trade_type", "MWEB");
                        extMap.put("sub_way", createOrder.getSub_way());
                        break;

                }
            } else {
                ToastUtil.showToast(getActivity(), getString(RUtil.getString(getActivity(), "ss_please_choose_mode_of_payment")));
                return;
            }
            if (alertDialog == null) {
                alertDialog = showLoadingDialog(getString(RUtil.getString(getActivity(), "ss_safety_environment_detection")));
            }
            if (!alertDialog.isShowing()) {
                alertDialog.show();
            }
            //组装创建订单的数据
            extMap.put("out_trade_no", payExtParams.optString(OUT_TRADE_NO));
            extMap.put("user_type", MobileActivity.sUserInfo.getUserType());
            extMap.put("content", getContent());
            paymentSelectPreference.createOrder(getActivity(), extMap);
        } else if (imgPayCloseId == v.getId()) {
            //支付取消
            SSPayManager.sPayCallback.onPayCancel(ConstantUtil.PAY_CANCEL, "支付取消");
            if (getActivity() != null) {
                getActivity().finish();
            }
        }
    }

    private String getContent() {
        HashMap<String, String> comSign = new HashMap<String, String>();
        comSign.put("client_id", GameSdk.clientId);
        comSign.put("prepay_id", createOrder.getPrepay_id());
        JSONObject jsonObject = new JSONObject();
        try {
            String sign = getSign(comSign);
            jsonObject.put("sign", sign);
            jsonObject.put("sign_type", "MD5");
            jsonObject.put("prepay_id", createOrder.getPrepay_id());
            jsonObject.put("client_id", GameSdk.clientId);
            return jsonObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getSign(HashMap<String, String> map) {
        StringBuffer content = new StringBuffer();
        // 按照key做首字母升序排列
        List<String> keys = new ArrayList<String>(map.keySet());
        Collections.sort(keys, String.CASE_INSENSITIVE_ORDER);
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i).toString();
            String value = map.get(key).toString();
            // 空串不参与签名
            content.append((i == 0 ? "" : "&") + key + "=" + value);
        }
        content.append("&key=").append(GameSdk.payKey);
        String unSign = content.toString();
        String sign = NumberEncryption.md5(unSign);
        return sign;

    }

    @Override
    public void onCreateOrderSuccess(String jsonString) {
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONObject sdk_info = jsonObject.optJSONObject("sdk_info");
            LogUtil.e("sdk_info:" + sdk_info.toString());
            JSONObject trade_info = jsonObject.optJSONObject("trade_info");
            LogUtil.e("trade_info:" + trade_info.toString());

            switch (payMethod) {
                case PAY_METHOD_ALI:
                    if (alertDialog != null) {
                        if (alertDialog.isShowing()) {
                            alertDialog.dismiss();
                        }
                    }
                    aliPay(sdk_info);
                    break;
                case PAY_METHOD_WX:
                    Bundle bundle = new Bundle();
                    String mweb_url = sdk_info.optString("mweb_url");
                    bundle.putString(ConstantUtil.MWEB_URL, mweb_url);
                    bundle.putString(ConstantUtil.OUT_TRADE_NO, payExtParams.optString(OUT_TRADE_NO));
                    bundle.putInt(ConstantUtil.PAY_WAY, 1);
                    IntentUtil.getInstance().goActivity(getActivity(), WXH5PayActivity.class, bundle);
                    if (alertDialog != null && alertDialog.isShowing()) {
                        alertDialog.dismiss();
                    }
                    getActivity().finish();
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onCreateOrderFailture(String errorCode, String message) {
        //返回支付失败的回调
        if (alertDialog != null && alertDialog.isShowing()) {
            alertDialog.dismiss();
        }
        SSPayManager.sPayCallback.onPayFail(ConstantUtil.PAY_FAIL_BECAUSE_CREATE_ORDER_FAIL, "创建订单失败");
        if (getActivity() != null) {
            getActivity().finish();
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (alertDialog != null && alertDialog.isShowing()) {
            alertDialog.dismiss();
        }
    }

    @Override
    public boolean onBackPressed() {
        return super.onBackPressed();
    }

    private void aliPay(final JSONObject sdk_info) {
        if (sdk_info != null) {
            final String orderInfo = sdk_info.optString("order_info");
            if (TextUtils.isEmpty(orderInfo)) {
                SSPayManager.sPayCallback.onPayFail(ConstantUtil.PAY_FAIL_BECAUSE_ORDERINFO_IS_NULL, "支付宝支付失败");
            } else {
                Runnable payRunnable = new Runnable() {
                    @Override
                    public void run() {
                        PayTask alipay = new PayTask(getActivity());
                        final String payInfo = orderInfo + "&sign=\"" + sdk_info.optString("sign") + "\"&sign_type=" + "\"" + sdk_info.optString("sign_type") + "\"";
                        Map<String, String> result = alipay.payV2(payInfo, true);
                        Message msg = new Message();
                        msg.what = ConstantUtil.PAY_METHOD_ALI;
                        msg.obj = result;
                        mHandler.sendMessage(msg);
                    }
                };
                ThreadPoolManager.getInstance().executor(payRunnable);

            }
        } else {
            SSPayManager.sPayCallback.onPayFail(ConstantUtil.PAY_FAIL_BECAUSE_ORDERINFO_IS_NULL, "支付宝支付失败");
        }
    }
}
