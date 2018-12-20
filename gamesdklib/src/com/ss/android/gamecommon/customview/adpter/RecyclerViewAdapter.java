package com.ss.android.gamecommon.customview.adpter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ss.android.game.sdk.info.UserInfo;
import com.ss.android.gamecommon.util.ConstantUtil;
import com.ss.android.gamecommon.util.NumberEncryption;
import com.ss.android.gamecommon.util.RUtil;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {
    //item类型
    public static final int ITEM_TYPE_CONTENT = 0;
    public static final int ITEM_TYPE_BOTTOM = 1;

    private int mBottomCount = 1;//底部view的个数
    private Context context;
    private LayoutInflater mLayoutInflater;
    private List<UserInfo> userInfos;

    private OnItemClickListener mOnItemClickListener;

    public RecyclerViewAdapter(Context context, List<UserInfo> userInfos) {
        this.context = context;
        mLayoutInflater = LayoutInflater.from(context);
        this.userInfos = userInfos;

    }

    /**
     * 删除某个位置的item
     *
     * @param position
     */
    public boolean removeItem(int position) {
        try {
            userInfos.remove(position);
            notifyItemRemoved(position);
            if (position != getItemCount()) {
                notifyItemRangeChanged(position, getItemCount());
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<UserInfo> getUserInfos() {
        return userInfos;
    }

    public void addItem(int position, UserInfo userInfo) {
        userInfos.add(position, userInfo);
        notifyItemInserted(position);

    }

    //内容长度
    public int getContentItemCount() {
        return userInfos.size();
    }

    //判断当前item是否是FooterView
    public boolean isBottomView(int position) {
        return mBottomCount != 0 && position >= getContentItemCount();
    }

    //判断当前item的类型
    @Override
    public int getItemViewType(int position) {
        int dataItemCount = getContentItemCount();
        if (mBottomCount != 0 && position >= dataItemCount) {
            //底部view
            return ITEM_TYPE_BOTTOM;
        } else {
            //内容view
            return ITEM_TYPE_CONTENT;
        }

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_TYPE_CONTENT) {
            //内容
            View view = mLayoutInflater.inflate(RUtil.getLayout(context, "item_expendlist_child"), parent, false);
            ContentViewHolder contentViewHolder = new ContentViewHolder(view);
            ImageView imgDelete = (ImageView) view.findViewById(RUtil.getId(context, "img_delete"));
            imgDelete.setOnClickListener(this);
            TextView tvUsername = (TextView) view.findViewById(RUtil.getId(context, "tv_username"));
            tvUsername.setOnClickListener(this);

            return contentViewHolder;
        } else if (viewType == ITEM_TYPE_BOTTOM) {
            //底部
            View view = mLayoutInflater.inflate(RUtil.getLayout(context, "item_expendlist_footer"), parent, false);
            BottomViewHolder bottomViewHolder = new BottomViewHolder(view);
            TextView tvLoginOtherAccount = (TextView) view.findViewById(RUtil.getId(context, "tv_login_other_account"));
            tvLoginOtherAccount.setOnClickListener(this);
            return bottomViewHolder;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ContentViewHolder) {
            UserInfo userInfo = userInfos.get(position);
            if (userInfo.getUserType() == ConstantUtil.TOURIST) {
                //游客
                ((ContentViewHolder) holder).img_user_icon.setImageResource(RUtil.getDrawable(context, "visitor_icon"));
            } else {
                ((ContentViewHolder) holder).img_user_icon.setImageResource(RUtil.getDrawable(context, "phone_icon"));
            }
            ((ContentViewHolder) holder).tv_user_name.setText(NumberEncryption.phoneReplaceMiddle(userInfo.getMobile()));
            ((ContentViewHolder) holder).img_delete.setImageResource(RUtil.getDrawable(context, "close"));
            ((ContentViewHolder) holder).img_delete.setTag(position);
            ((ContentViewHolder) holder).tv_user_name.setTag(position);

        } else if (holder instanceof BottomViewHolder) {
            ((BottomViewHolder) holder).tv_login_other_account.setTag(position);
        }
    }

    @Override
    public int getItemCount() {
        return getContentItemCount() + mBottomCount;
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            mOnItemClickListener.onItemClick(v, (int) v.getTag());
        }

    }


    //内容ViewHolder
    public class ContentViewHolder extends RecyclerView.ViewHolder {
        private ImageView img_user_icon;
        private TextView tv_user_name;
        private ImageView img_delete;

        public ContentViewHolder(View itemView) {
            super(itemView);
            img_user_icon = (ImageView) itemView.findViewById(RUtil.getId(context, "img_user_icon"));
            tv_user_name = (TextView) itemView.findViewById(RUtil.getId(context, "tv_username"));
            img_delete = (ImageView) itemView.findViewById(RUtil.getId(context, "img_delete"));
        }
    }

    //底部ViewHolder
    public class BottomViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_login_other_account;

        public BottomViewHolder(final View itemView) {
            super(itemView);
            tv_login_other_account = (TextView) itemView.findViewById(RUtil.getId(context, "tv_login_other_account"));
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);

    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }
}
