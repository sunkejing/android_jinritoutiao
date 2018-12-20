package com.ss.android.gamecommon.customview.adpter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ss.android.gamecommon.util.ConstantUtil;
import com.ss.android.gamecommon.util.RUtil;
import com.ss.android.gamecommon.util.SharedPreferenceUtil;
import com.ss.android.pay.bean.PayMethodInfo;

import java.util.List;

import static com.ss.android.gamecommon.util.SharedPreferenceUtil.PAY_METHOD;

public class PaymethodRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private List<PayMethodInfo> mPayMethods;
    private int mPosition = 0;
    private int lastSelect;

    public PaymethodRecyclerViewAdapter(Context context, List<PayMethodInfo> paymethod) {
        this.mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
        this.mPayMethods = paymethod;
        lastSelect = SharedPreferenceUtil.getInstance(context.getApplicationContext()).getInt(PAY_METHOD, ConstantUtil.PAY_METHOD_ALI);

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //内容
        final View view = mLayoutInflater.inflate(RUtil.getLayout(mContext, "item_paymethod_child"), parent, false);
        final ContentViewHolder contentViewHolder = new ContentViewHolder(view);
        ImageView img_pay_icon = (ImageView) view.findViewById(RUtil.getId(mContext, "img_pay_icon"));
        TextView tv_pay_name = (TextView) view.findViewById(RUtil.getId(mContext, "tv_pay_name"));
        ImageView img_select = (ImageView) view.findViewById(RUtil.getId(mContext, "img_select"));
        LinearLayout ll_pay = (LinearLayout) view.findViewById(RUtil.getId(mContext, "ll_pay"));
        ll_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPosition = contentViewHolder.getAdapterPosition();
                contentViewHolder.img_select.setVisibility(View.VISIBLE);
                SharedPreferenceUtil.getInstance(mContext).putInt(ConstantUtil.PAY_WAY, mPayMethods.get(mPosition).getMethodType());
                lastSelect = mPayMethods.get(mPosition).getMethodType();
                notifyDataSetChanged();
            }
        });
        return contentViewHolder;

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        PayMethodInfo payMethodInfo = mPayMethods.get(position);
        ((PaymethodRecyclerViewAdapter.ContentViewHolder) holder).img_pay_icon.setImageResource(payMethodInfo.getImgIcon());
        ((PaymethodRecyclerViewAdapter.ContentViewHolder) holder).tv_pay_name.setText(payMethodInfo.getMethodName());
        if (lastSelect == payMethodInfo.getMethodType()) {
            ((PaymethodRecyclerViewAdapter.ContentViewHolder) holder).img_select.setVisibility(View.VISIBLE);
        } else {
            ((PaymethodRecyclerViewAdapter.ContentViewHolder) holder).img_select.setVisibility(View.INVISIBLE);
        }

    }

    @Override
    public int getItemCount() {
        if (mPayMethods != null) {
            return mPayMethods.size();
        }
        return 0;
    }

    //内容ViewHolder
    public class ContentViewHolder extends RecyclerView.ViewHolder {
        private ImageView img_pay_icon;//支付方式icon
        private TextView tv_pay_name;//支付方式name
        private ImageView img_select;//选中当前的支付方式
        private LinearLayout ll_pay;

        public ContentViewHolder(View itemView) {
            super(itemView);
            img_pay_icon = (ImageView) itemView.findViewById(RUtil.getId(mContext, "img_pay_icon"));
            tv_pay_name = (TextView) itemView.findViewById(RUtil.getId(mContext, "tv_pay_name"));
            img_select = (ImageView) itemView.findViewById(RUtil.getId(mContext, "img_select"));
            ll_pay = (LinearLayout) itemView.findViewById(RUtil.getId(mContext, "ll_pay"));
        }
    }

    public int getSelectPosition() {
        return mPosition;
    }


}
