package com.viet.mine.adapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.viet.mine.R;
import com.viet.mine.activity.MineWalletActivity;

import java.util.List;

/**
 * Created by Administrator on 2017/2/6.
 */

public class CardRvAdapter extends RecyclerView.Adapter<CardRvAdapter.ItemViewHolder> {
    private List<MineWalletActivity.Entity> list;
    private Context context;

    public CardRvAdapter(Context context, List<MineWalletActivity.Entity> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.wallet_card_item, parent, false);
        return new ItemViewHolder(itemView);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        holder.iv.setText(list.get(position).getName());
        holder.tv.setText(list.get(position).getCount());
        if (position == 0) {
            holder.cons.setBackground(context.getResources().getDrawable(R.drawable.shape_wallet_card_red));
            ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(new ViewGroup.LayoutParams(dip2px(context, 300), dip2px(context, 120)));
            params.setMarginStart(dip2px(context, 16));
            holder.cons.setLayoutParams(params);
        } else {
            holder.cons.setBackground(context.getResources().getDrawable(R.drawable.shape_wallet_card_yellow));
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        private TextView iv;
        private TextView tv;
        private ConstraintLayout cons;

        public ItemViewHolder(View itemView) {
            super(itemView);
            iv = (TextView) itemView.findViewById(R.id.tv_wallet_name);
            tv = (TextView) itemView.findViewById(R.id.tv_wallet_asset);
            cons = (ConstraintLayout) itemView.findViewById(R.id.card);
        }
    }

    public int dip2px(Context context, float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

}
