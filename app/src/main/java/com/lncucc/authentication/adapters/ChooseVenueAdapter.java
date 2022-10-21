package com.lncucc.authentication.adapters;

import android.widget.CheckBox;

import com.askia.coremodel.datamodel.database.db.DBExamLayout;
import com.askia.coremodel.datamodel.database.repository.ExamItemCheck;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.lncucc.authentication.R;
import com.lncucc.authentication.callback.VenveItemClick;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;


public class ChooseVenueAdapter extends BaseQuickAdapter<ExamItemCheck, BaseViewHolder> {
    private VenveItemClick mItemClick;
    public ChooseVenueAdapter(@Nullable List<ExamItemCheck> data, VenveItemClick itemClick) {
        super(R.layout.item_choose_venue, data);
        this.mItemClick = itemClick;
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, ExamItemCheck s) {
        baseViewHolder.setText(R.id.tv_room_name,s.getRoomNo());
        ((CheckBox)baseViewHolder.getView(R.id.cx_ex)).setChecked(s.isChecked());
        baseViewHolder.getView(R.id.cx_ex).setOnClickListener(v -> mItemClick.onItemClickListener(baseViewHolder.getLayoutPosition()));
    }
}