package com.ss.android.login.sdk.fragment;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import com.ss.android.game.sdk.info.UserInfo;
import com.ss.android.gamecommon.customview.adpter.RecyclerViewAdapter;
import com.ss.android.gamecommon.model.AutoLogin;
import com.ss.android.gamecommon.util.ConstantUtil;
import com.ss.android.gamecommon.util.NumberEncryption;
import com.ss.android.gamecommon.util.RUtil;
import com.ss.android.gamecommon.util.SharedPreferenceUtil;
import com.ss.android.gamecommon.util.ToastUtil;
import com.ss.android.gamecommon.util.ToolUtils;
import com.ss.android.login.sdk.activity.MobileActivity;
import com.ss.android.login.sdk.preference.AutoLoginPreference;
import com.ss.android.login.sdk.preference.AutoLoginPreferenceImpl;
import com.ss.android.login.sdk.view.AutoLoginView;
import com.ss.gamesdk.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.ss.android.login.sdk.activity.MobileActivity.loginCallback;
import static com.ss.android.login.sdk.activity.MobileActivity.sUserInfo;

public class SwitchAccountFragment extends BaseFragment implements View.OnClickListener, AutoLoginView {
    private LinearLayout ll_group;
    private TextView enterGame;
    private TextView userName;
    private ImageView userImage;
    private ImageView downUpImage;

    private int enterGameId;
    private int groupId;
    private int userImageId;
    private int downUpImageId;

    private SharedPreferenceUtil sharedPreferenceUtil;
    private List<UserInfo> userInfos = new ArrayList<UserInfo>();
    private UserInfo currentUserInfo;
    private String mobile;

    private PopupWindow popupWindow = null;
    private static final int popWidth = 275;
    private static final int popHeight = 120;
    private RecyclerViewAdapter recyclerViewAdapter;
    private RecyclerView recyclerView;
    private Dialog alertDialog;
    private boolean isPopshow = false;

    private AutoLoginPreference autoLoginPreference;


    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        groupId = RUtil.getId(getActivity(), "ll_group");
        ll_group = findViewId(view, groupId);
        ll_group.setOnClickListener(this);

        userName = findViewId(ll_group, RUtil.getId(getActivity(), "tv_username"));
        userImageId = RUtil.getId(getActivity(), "img_user_icon");
        userImage = findViewId(ll_group, userImageId);
        downUpImageId = RUtil.getId(getActivity(), "img_up_down");
        downUpImage = findViewId(ll_group, downUpImageId);
        downUpImage.setImageResource(RUtil.getDrawable(getActivity(), "down_arrow"));
        downUpImage.setTag(RUtil.getDrawable(getActivity(), "down_arrow"));

        enterGameId = RUtil.getId(getActivity(), "btn_entergame");
        enterGame = findViewId(view, enterGameId);
        enterGame.setOnClickListener(this);

        sharedPreferenceUtil = SharedPreferenceUtil.getInstance(getContext());
        userInfos = sharedPreferenceUtil.getAllAccountInfo();//所有账号
        Collections.reverse(userInfos);
        mobile = sharedPreferenceUtil.getAccount();
        currentUserInfo = sharedPreferenceUtil.getCurrentUserInfo();

        initData();
        initPopupWindow(ll_group);
        autoLoginPreference = new AutoLoginPreferenceImpl(this);
    }

    @Override
    public int getLayoutId() {
        return RUtil.getLayout(getActivity(), "fragment_switch_account");
    }

    public static SwitchAccountFragment newInstance() {
        return new SwitchAccountFragment();
    }

    public void initData() {
        userName.setText(NumberEncryption.phoneReplaceMiddle(mobile));
        if (currentUserInfo.getUserType() == ConstantUtil.ACCOUNT_NUMBER) {
            userImage.setImageResource(RUtil.getDrawable(getActivity(), "phone_icon"));
        } else if (currentUserInfo.getUserType() == ConstantUtil.TOURIST) {
            userImage.setImageResource(RUtil.getDrawable(getActivity(), "visitor_icon"));
        }

    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        if (viewId == enterGameId) {
            if (alertDialog == null) {
                alertDialog = showLoadingDialog();
            }
            if (!alertDialog.isShowing()) {
                alertDialog.show();
            }
            autoLoginPreference.autoLogin(getActivity(), currentUserInfo.getLoginId());


        } else if (viewId == groupId) {
            if (popupWindow != null) {
                int tag = (int) downUpImage.getTag();
                if (tag == RUtil.getDrawable(getActivity(), "down_arrow")) {
                    downUpImage.setImageResource(RUtil.getDrawable(getActivity(), "up_arrow"));
                    downUpImage.setTag(RUtil.getDrawable(getActivity(), "up_arrow"));
                } else if (tag == RUtil.getDrawable(getActivity(), "up_arrow")) {
                    downUpImage.setImageResource(RUtil.getDrawable(getActivity(), "down_arrow"));
                    downUpImage.setTag(RUtil.getDrawable(getActivity(), "down_arrow"));
                }
                if (popupWindow.isShowing()) {
                    popupWindow.dismiss();
                } else {
                    popupWindow.showAsDropDown(ll_group);
                }


            }
        }


    }


    private void initPopupWindow(View view) {
        View contentView = getActivity().getLayoutInflater().inflate(RUtil.getLayout(getActivity(), "item_popupwindow_layout"), null);
        //对布局中的控件进行初始化
        recyclerView = (RecyclerView) contentView.findViewById(RUtil.getId(getActivity(), "recv_user_infos"));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerViewAdapter = new RecyclerViewAdapter(getActivity(), userInfos);

        recyclerViewAdapter.setOnItemClickListener(new RecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (view instanceof ImageView) {
                    //删除,弹出删除确认对话框
                    showDeleteAccountDialog(getActivity(), position);

                } else if (view instanceof TextView) {
                    if (position < userInfos.size()) {
                        //切换其他账号
                        if (popupWindow != null) {
                            popupWindow.dismiss();
                        }
                        refreshCurrentUserInfo(recyclerViewAdapter.getUserInfos().get(position));

                    } else {
                        //登录其他账号
                        if (popupWindow != null) {
                            popupWindow.dismiss();
                        }
                        addFragment(LoginSelectFragment.newInstance());

                    }

                }
            }
        });
        recyclerView.setAdapter(recyclerViewAdapter);

        popupWindow = new PopupWindow(contentView, ToolUtils.dip2px(getActivity(), popWidth), ToolUtils.dip2px(getActivity(), popHeight), true);
        popupWindow.setTouchable(true);
        popupWindow.setOutsideTouchable(false);  // 设置popupwindow外部可点击
        popupWindow.setFocusable(false);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());


    }

    //更新ui
    private void refreshCurrentUserInfo(UserInfo userInfo) {
        currentUserInfo = userInfo;
        System.out.println("currentUserInfo:" + currentUserInfo.toString());
        if (userInfo.getUserType() == ConstantUtil.TOURIST) {
            //游客登录
            userName.setText(userInfo.getMobile());
            userImage.setImageResource(RUtil.getDrawable(getActivity(), "visitor_icon"));

        } else if (userInfo.getUserType() == ConstantUtil.ACCOUNT_NUMBER) {
            String phoneNum = NumberEncryption.phoneReplaceMiddle(userInfo.getMobile());
            userName.setText(phoneNum);
            userImage.setImageResource(RUtil.getDrawable(getActivity(), "phone_icon"));
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (alertDialog != null) {
            alertDialog.dismiss();
        }
    }

    @Override
    public void onAutoLoginSuccess(AutoLogin autoLogin) {
        if (alertDialog != null) {
            alertDialog.dismiss();
        }
        MobileActivity.sUserInfo.setUserId(autoLogin.getUser_id());
        MobileActivity.sUserInfo.setOpenId(autoLogin.getOpen_id());
        MobileActivity.sUserInfo.setUserType(currentUserInfo.getUserType());
        MobileActivity.sUserInfo.setLoginId(currentUserInfo.getLoginId());
        MobileActivity.sUserInfo.setHasPlayed(currentUserInfo.getHasPlayed());
        MobileActivity.sUserInfo.setAccessToken(currentUserInfo.getAccessToken());
        saveUserInfo();
        ToastUtil.showToast(getActivity(), autoLogin.toString());
        loginCallback.onLoginSuccess(sUserInfo.getAccessToken(), sUserInfo.getUserId(), sUserInfo.getOpenId(), sUserInfo.getUserType());
        UserInfo saveUser = new UserInfo(sUserInfo.getAccessToken(), sUserInfo.getOpenId(), sUserInfo.getUserId(), sUserInfo.getUserType(), currentUserInfo.getMobile(), currentUserInfo.getLoginId());
        sharedPreferenceUtil.saveAllAccountInfo(saveUser);
        sharedPreferenceUtil.setAccount(currentUserInfo.getMobile());
        getActivity().finish();
    }

    @Override
    public void onAutoLoginFailture(String errorCode, String errorMessage) {
        if (alertDialog != null) {
            alertDialog.dismiss();
        }
        addFragment(LoginSelectFragment.newInstance());
    }

    public void saveUserInfo() {
        SharedPreferenceUtil sharedPreferenceUtil = SharedPreferenceUtil.getInstance(getActivity());
        sharedPreferenceUtil.putBean(SharedPreferenceUtil.USER_INFO, sUserInfo);
    }

    private void showDeleteAccountDialog(final Context context, final int position) {
        final Dialog dialog = new Dialog(context, R.style.add_dialog);
        View dialogView = LayoutInflater.from(context).inflate(RUtil.getLayout(context, "dialog_delete_account"), null);
        TextView tvSure = (TextView) dialogView.findViewById(RUtil.getId(context, "tv_sure"));
        TextView tvCancel = (TextView) dialogView.findViewById(RUtil.getId(context, "tv_cancel"));
        tvSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                //处理删除逻辑
                UserInfo deleteUser = recyclerViewAdapter.getUserInfos().get(position);
                boolean isDelete = recyclerViewAdapter.removeItem(position);
                if (isDelete) {
                    //从sharedPreference中删除
                    SharedPreferenceUtil sharedPreferenceUtil = SharedPreferenceUtil.getInstance(context.getApplicationContext());
                    sharedPreferenceUtil.removeUserInfoFromAllUserInfos(deleteUser);

                }

                int itemCount = recyclerViewAdapter.getItemCount();
                if (itemCount <= 1) {
                    if (popupWindow != null) {
                        popupWindow.dismiss();
                    }
                    addFragment(LoginSelectFragment.newInstance());
                }
            }
        });
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.setTitle(null);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(dialogView);
        dialog.show();
    }
}
