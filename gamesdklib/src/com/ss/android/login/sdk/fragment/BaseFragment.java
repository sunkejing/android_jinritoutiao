package com.ss.android.login.sdk.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ss.android.gamecommon.applog.LogReport;
import com.ss.android.gamecommon.model.Verified;
import com.ss.android.gamecommon.thread.RetrofitManager;
import com.ss.android.gamecommon.thread.ThreadPoolManager;
import com.ss.android.gamecommon.util.ConstantUtil;
import com.ss.android.gamecommon.util.LoadProgressDialog;
import com.ss.android.gamecommon.util.UrlUtil;
import com.ss.android.login.sdk.activity.BaseActivity;
import com.ss.android.login.sdk.model.RequestCallback;
import com.ss.android.pay.tools.BackHandlerHelper;
import com.ss.android.pay.tools.FragmentBackHandler;

import java.util.HashMap;

public abstract class BaseFragment extends Fragment implements FragmentBackHandler {
    protected BaseActivity mActivity;

    private ProgressDialog mProgressDialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //添加页面的打点操作,event_type_value:window_show
        LogReport.getInstance().sendLog(getActivity(), ConstantUtil.EVENT_TYPE_WINDOW_SHOW, ConstantUtil.EVENT_LABEL_INIT_WINDOW);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutId(), container, false);
        initView(view, savedInstanceState);
        return view;

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.mActivity = (BaseActivity) activity;
    }

    protected abstract void initView(View view, Bundle savedInstanceState);

    public abstract int getLayoutId();

    //添加fragment
    protected void addFragment(BaseFragment fragment) {
        if (null != fragment) {
            mActivity.addFragment(fragment);
        }
    }

    //移除fragment
    protected void removeFragment() {
        mActivity.removeFragment();
    }

    protected void removeAllFragment() {
        mActivity.removeAllFragment();
    }

    public <T> T findViewId(View view, int resID) {
        return (T) view.findViewById(resID);
    }

    //普通加载中对话框：上下结构的显示
    protected Dialog showLoadingDialog() {
        return LoadProgressDialog.show(getActivity(), "登录中...", false, null);

    }

    protected Dialog showLoadingDialog(String message) {
        return LoadProgressDialog.show(getActivity(), message, false, null);
    }

    public void onHasVerified(Context context, String open_id, int accountNumber, final RequestCallback<Verified> requestCallback) {
        HashMap<String, Object> mHashMap = new HashMap<String, Object>();
        mHashMap.put("open_id", open_id);
        mHashMap.put("user_type", accountNumber);
        HashMap<String, Object> mDataMap = new HashMap<String, Object>();
        mDataMap.putAll(mHashMap);
        mDataMap.putAll(UrlUtil.extMap(context, false));
        RetrofitManager.getInstance().onHasVerified(mDataMap, new RequestCallback<Verified>() {

            @Override
            public void onRequestSuccess(Verified verified) {
                requestCallback.onRequestSuccess(verified);

            }

            @Override
            public void onRequestFailture(String errorCode, String message) {
                requestCallback.onRequestFailture(errorCode, message);
            }
        });

    }

    @Override
    public boolean onBackPressed() {
        return BackHandlerHelper.handleBackPress(this);
    }
}
