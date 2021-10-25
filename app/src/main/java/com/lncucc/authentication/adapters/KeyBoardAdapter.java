package com.lncucc.authentication.adapters;

import com.askia.coremodel.datamodel.data.KeyBoardBean;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.lncucc.authentication.R;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * 人工审核键盘
 */
public class KeyBoardAdapter extends BaseQuickAdapter<KeyBoardBean.DataBean, BaseViewHolder> {


    public KeyBoardAdapter(@Nullable List<KeyBoardBean.DataBean> data) {
        super(R.layout.item_keyboard, data);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, KeyBoardBean.DataBean keyBoardBean) {
        baseViewHolder.setText(R.id.tv_key,keyBoardBean.getName());
    }
}
