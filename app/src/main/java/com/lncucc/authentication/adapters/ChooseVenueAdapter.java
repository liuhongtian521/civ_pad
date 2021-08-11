package com.lncucc.authentication.adapters;

import android.graphics.Color;

import com.askia.coremodel.datamodel.database.db.DBExamLayout;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.lncucc.authentication.R;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;


public class ChooseVenueAdapter extends BaseQuickAdapter<DBExamLayout, BaseViewHolder> {
    public ChooseVenueAdapter(@Nullable List<DBExamLayout> data) {
        super(R.layout.item_choose_venue, data);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, DBExamLayout s) {
        baseViewHolder.setText(R.id.tv_room_name,s.getRoomNo());
    }


}