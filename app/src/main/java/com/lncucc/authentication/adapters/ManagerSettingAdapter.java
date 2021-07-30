package com.lncucc.authentication.adapters;

import com.askia.coremodel.datamodel.manager.ManagerItemBean;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.lncucc.authentication.R;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ManagerSettingAdapter extends BaseQuickAdapter<ManagerItemBean.ItemBean, BaseViewHolder> {

    public ManagerSettingAdapter(@Nullable List<ManagerItemBean.ItemBean> data) {
        super(R.layout.item_manager, data);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, ManagerItemBean.ItemBean s) {
        baseViewHolder.setText(R.id.tv_manager_item, s.getName());
        baseViewHolder.setImageResource(R.id.iv_manager_holder,
                getContext().getResources().getIdentifier(s.getPic(),
                        "mipmap", getContext().getPackageName()));
    }


}
