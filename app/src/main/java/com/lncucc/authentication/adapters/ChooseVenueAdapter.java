package com.lncucc.authentication.adapters;

import android.graphics.Color;
import android.view.View;

import com.askia.coremodel.datamodel.database.db.DBExamLayout;
import com.blankj.utilcode.util.LogUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.lncucc.authentication.R;
import com.lncucc.authentication.callback.VenveItemClick;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;


public class ChooseVenueAdapter extends BaseQuickAdapter<DBExamLayout, BaseViewHolder> {
    private VenveItemClick mItemClick;
    public ChooseVenueAdapter(@Nullable List<DBExamLayout> data, VenveItemClick itemClick) {
        super(R.layout.item_choose_venue, data);
        this.mItemClick = itemClick;
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, DBExamLayout s) {
        baseViewHolder.setText(R.id.tv_room_name,s.getRoomNo());
        baseViewHolder.getView(R.id.cx_ex).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mItemClick.onItemClickListener(baseViewHolder.getLayoutPosition());
            }
        });
    }


}