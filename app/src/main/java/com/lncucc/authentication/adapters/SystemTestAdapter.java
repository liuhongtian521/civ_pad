package com.lncucc.authentication.adapters;

import android.graphics.Color;

import com.askia.coremodel.datamodel.system.SystemTestBean;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.lncucc.authentication.R;
import com.lncucc.authentication.adapters.itemclick.ItemClickListener;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SystemTestAdapter extends BaseQuickAdapter<SystemTestBean.DataBean, BaseViewHolder> {

    private ItemClickListener itemClickListener;

    public SystemTestAdapter(List<SystemTestBean.DataBean> list, ItemClickListener clickListener) {
        super(R.layout.item_system_test, list);
        this.itemClickListener = clickListener;
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, SystemTestBean.DataBean dataBean) {
        baseViewHolder.setText(R.id.tv_test_type, dataBean.getName());
        baseViewHolder.setImageResource(R.id.iv_sys,
                getContext().getResources().getIdentifier(dataBean.getPic(),
                        "mipmap", getContext().getPackageName()));
        String state = dataBean.getState() == 0 ? "待测试" : "正常";
        int textColor = dataBean.getState() == 0 ? Color.parseColor("#999999") : Color.parseColor("#0EBD35");
        baseViewHolder.setText(R.id.tv_state, state);
        baseViewHolder.setTextColor(R.id.tv_state, textColor);
        baseViewHolder.getView(R.id.iv_test_state).setBackgroundResource(dataBean.getState() == 0? R.mipmap.icon_test_waiting: R.mipmap.icon_test_pass);
        baseViewHolder.getView(R.id.btn_test).setOnClickListener(v -> itemClickListener.onItemClick(baseViewHolder.getLayoutPosition()));
    }
}
