package com.ss.android.login.sdk.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.ss.android.login.sdk.fragment.BaseFragment;

import java.util.List;

public abstract class SDKActivity extends BaseActivity {
    //获取第一个fragment
    protected abstract BaseFragment getFristFragment();


    //获取Intent
    protected void handleIntent(Intent intent) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayout());
        if (null != getIntent()) {
            handleIntent(getIntent());
        }
        //避开重复添加Fragment
        List<Fragment> fragments = getSupportFragmentManager().getFragments();

        if (null == fragments || fragments.isEmpty()) {
            BaseFragment firstFragment = getFristFragment();
            if (null != firstFragment) {
                addFragment(firstFragment);
            }
        }


    }
}
